/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.mail.CommonMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class PlanSubmissionGitOperation {

    private static final Logger LOG = Logger.getLogger(PlanSubmissionGitOperation.class.getName());

    @Autowired
    WFConfig wfConfig;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    SystemLoadDAO systemLoadDAO;

    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    PutLevelDAO putLevelDAO;

    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    LDAPAuthenticatorImpl ldapAuthenticator;

    @Scheduled(cron = "0 0 0-23/1 * * ?")
    @Transactional
    public void doRun() {
	try {
	    moveDerivedArtifactsToGit();
	} catch (Exception ex) {
	    LOG.info("Exception Occurs on GIT Operationt to move the dervied artifacts", ex);
	}
    }

    public Boolean moveDerivedArtifactsToGit() throws Exception {
	if (wfConfig.getPrimary()) {
	    ZonedDateTime zonedDateTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("America/Denver"));
	    Date startDateTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).minusDays(1L).toInstant());
	    Date endDateTime = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).minusSeconds(1L).toInstant());

	    LOG.info("Start Date Time - " + startDateTime);
	    LOG.info("End Date Time - " + endDateTime);
	    LOG.info("Zoned Date Time - " + zonedDateTime + "MT Time -" + zonedDateTime.getHour());

	    if (zonedDateTime.getHour() == wfConfig.getMoveDerivedArtifactsScheduledTime().intValue()) {
		List<ImpPlan> plans = impPlanDAO.getSubmittedPlansForGitOperation(startDateTime, endDateTime);
		plans.stream().forEach(plan -> {
		    try {
			movePlanDerivedArtifactsToGit(plan);
		    } catch (Exception ex) {
			LOG.info("Error in moving the derived artifacts to Git Server " + plan.getId(), ex);
		    }
		});
	    }

	}
	return Boolean.TRUE;
    }

    public Boolean movePlanDerivedArtifactsToGit(ImpPlan plan) throws Exception {
	List<SystemLoad> systemLoads = systemLoadDAO.findByImpPlan(plan);
	LOG.info("Processing plan for derived artifacts - " + plan.getId());
	systemLoads.stream().filter(t -> t.getDerivedSegmentsMovedDt() == null).forEach(sysLoad -> {
	    try {
		movePlanDerivedArtifactsToGit(sysLoad);
	    } catch (Exception ex) {
		LOG.info("Error Occurs on moving derived artifacts into Git", ex);
	    }
	});
	return Boolean.TRUE;
    }

    public Boolean movePlanDerivedArtifactsToGit(SystemLoad sysLoad) throws Exception {
	User user = ldapAuthenticator.getServiceUser();
	LOG.info("Processing the system Load - " + sysLoad.getId() + " , plan id - " + sysLoad.getPlanId().getId());
	PutLevel putLevel = putLevelDAO.find(sysLoad.getPutLevelId().getId());
	String command = Constants.SystemScripts.MOVE_DERIVED_SEGMENTS_ON_SCHEDULAR.getScript() + (sysLoad.getPlanId().getId() + "_001" + "_" + sysLoad.getSystemId().getName() + "_").toLowerCase() + Constants.JENKINS_DATEFORMAT.get().format(sysLoad.getLoadDateTime()) + "0000 " + putLevel.getPutLevel().toLowerCase();
	JSONResponse lReturn = sSHClientUtils.executeCommand(sysLoad.getSystemId(), command);

	if (!lReturn.getStatus()) {
	    LOG.info("Error in moving the derived artifacts to Git Server " + sysLoad.getPlanId().getId() + " for system " + sysLoad.getSystemId().getName());

	    // Send Email to development Team
	    CommonMail lActionMail = (CommonMail) mailMessageFactory.getTemplate(CommonMail.class);
	    String lSubject = "Action Required: Issues in moving derived artifacts into git server, plan - " + sysLoad.getPlanId().getId() + " for system " + sysLoad.getSystemId().getName();
	    lActionMail.addToDevOpsCentre(true);
	    lActionMail.setMessage("");
	    lActionMail.setSubject(lSubject);
	    mailMessageFactory.push(lActionMail);

	} else {
	    sysLoad.setDerivedSegmentsMovedDt(new Date());
	    systemLoadDAO.update(user, sysLoad);

	    StringBuilder sb = new StringBuilder();
	    sb.append("Derived Artifacts are moved to Git server for system ").append(sysLoad.getSystemId().getName());
	    CommonActivityMessage lMessage = new CommonActivityMessage(sysLoad.getPlanId(), null);
	    lMessage.setMessage(sb.toString());
	    activityLogDAO.save(user, lMessage);
	}

	return Boolean.TRUE;
    }
}
