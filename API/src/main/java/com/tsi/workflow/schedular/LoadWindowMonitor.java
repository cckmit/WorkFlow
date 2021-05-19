/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.LoadDatePassedMacroPlan;
import com.tsi.workflow.mail.LoadWindowPassedMail;
import com.tsi.workflow.mail.PutLevelDeploymentDateMail;
import com.tsi.workflow.utils.Constants;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author
 */
@Component
public class LoadWindowMonitor {

    private static final Logger LOG = Logger.getLogger(LoadWindowMonitor.class.getName());
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    @Autowired
    PutLevelDAO putLevelDAO;

    @Autowired
    ImplementationDAO implementationDAO;

    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    WFConfig wFConfig;

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    // @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_HOUR)
    @Transactional
    public void LoadWindowCheckProcess() {
	if (wFConfig.getPrimary()) {
	    try {
		Map<String, Integer> impPlanIdList = impPlanDAO.getSecuredPassedLoadDate(new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()));
		for (Map.Entry<String, Integer> entrySet : impPlanIdList.entrySet()) {
		    String planId = entrySet.getKey();
		    Integer loadedCount = entrySet.getValue();
		    List<SystemLoad> findByImpPlan = systemLoadDAO.findByImpPlan(planId);
		    if (loadedCount == findByImpPlan.size()) {
			User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
			LOG.info("Load window monitor - REJECT plan - " + planId);
			rejectHelper.rejectPlan(serviceUser, planId, Constants.REJECT_REASON.LOAD_DATE.getValue(), Constants.AUTOREJECT_COMMENT.LOAD_DATE.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
		    }
		}
	    } catch (Exception e) {
		LOG.error("Unable to reject plan during automated load window check process : ", e);
	    }
	}
    }

    // @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_DAY)
    // @Scheduled(cron = "0 6 0 * * ? ")
    @Scheduled(cron = "${cron.load.mail}")
    @Transactional
    public synchronized void loadWindowPassedMailProccess() {
	if (wFConfig.getPrimary()) {
	    // Email sending Before approved status
	    List<Object[]> impPlanSystemLoadList = impPlanDAO.getImpPlanFromLoadDateTime(new ArrayList(Constants.PlanStatus.getSecuredBeforeReadyForProduction().keySet()));
	    for (Object[] lSysLoadAndPlan : impPlanSystemLoadList) {
		Set<String> leadandDeveloprs = new HashSet<>();
		Set<String> reviwers = new HashSet<>();
		Set<String> targetSystems = new HashSet<>();
		String impPlan = lSysLoadAndPlan[0].toString();
		String lead = lSysLoadAndPlan[1].toString();
		String devManager = lSysLoadAndPlan[2].toString();
		Date loadDateTime = (Date) lSysLoadAndPlan[3];
		String targetSystem = lSysLoadAndPlan[4].toString();
		String peerReviwers = lSysLoadAndPlan[5].toString();
		String devloperId = lSysLoadAndPlan[6].toString();
		String planDesc = lSysLoadAndPlan[7].toString();
		List<String> reviewerList = Arrays.asList(peerReviwers.split(","));
		for (String reviewer : reviewerList) {
		    reviwers.add(reviewer);
		}
		List<String> lTargetSystemList = Arrays.asList(targetSystem.split(","));
		for (String lTagetSystem : lTargetSystemList) {
		    targetSystems.add(lTagetSystem.trim());
		}

		leadandDeveloprs.add(lead);
		leadandDeveloprs.add(devloperId);

		ImpPlan lImpPlan = impPlanDAO.find(impPlan);
		User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
		/**
		 * Release#: 23.0 JIRA#: ZTPFM-2857 | Defect Modified Date: 23/MAR/2020 Author:
		 * Ramkumar Seenivasan Description: Mail flag was handled in target system level
		 * to avoid Load Windows passed mail not sending for all the target systems
		 */
		Set<String> finalTargetSystems = new HashSet<>();
		systemLoadDAO.findByImpPlan(impPlan).stream().forEachOrdered(t -> {
		    if (targetSystems.contains(t.getSystemId().getName().toUpperCase())) {
			Boolean loadSetMailFalg = t.getLoadDateMailFlag() != null ? t.getLoadDateMailFlag() : Boolean.FALSE;
			if (!loadSetMailFalg) {
			    finalTargetSystems.add(t.getSystemId().getName());
			    t.setLoadDateMailFlag(Boolean.TRUE);
			    systemLoadDAO.update(serviceUser, t);
			}
		    }
		});
		if (!finalTargetSystems.isEmpty()) {
		    LoadWindowPassedMail loadWindowPassedMail = (LoadWindowPassedMail) getMailMessageFactory().getTemplate(LoadWindowPassedMail.class);
		    loadWindowPassedMail.setImpPlan(impPlan);
		    leadandDeveloprs.stream().forEach(t -> loadWindowPassedMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
		    reviwers.stream().forEach(t -> loadWindowPassedMail.addcCAddressUserId(t, Constants.MailSenderRole.PEER_REVIEWER));
		    loadWindowPassedMail.addcCAddressUserId(devManager, Constants.MailSenderRole.DEV_MANAGER);
		    loadWindowPassedMail.setLoadDateTime(loadDateTime);
		    loadWindowPassedMail.setPlanDesc(planDesc);
		    loadWindowPassedMail.setTargetSystem(targetSystems);
		    getMailMessageFactory().push(loadWindowPassedMail);
		} else {
		    LOG.info("For this implementation plan " + impPlan + "  email already send. ");
		}
	    }
	}
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE)
    @Transactional
    public synchronized void loadDateEqualMacroHeaderPlanMailProccess() throws ParseException {
	if (wFConfig.getPrimary()) {
	    List<Object[]> lListofImpPlan = impPlanDAO.getImpPlanFromMacroHeaderPlan(Constants.PlanStatus.APPROVED.name(), Boolean.TRUE);
	    for (Object[] impPlanObj : lListofImpPlan) {
		String impPlan = impPlanObj[0].toString();
		Date loadDateTime = (Date) impPlanObj[1];
		String devManager = impPlanObj[2].toString();
		String lead = impPlanObj[3].toString();
		User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
		ImpPlan lImpPlan = impPlanDAO.find(impPlan);
		LoadDatePassedMacroPlan loadDatePassedMacroPlan = (LoadDatePassedMacroPlan) getMailMessageFactory().getTemplate(LoadDatePassedMacroPlan.class);
		loadDatePassedMacroPlan.setImpPlan(lImpPlan.getId());
		loadDatePassedMacroPlan.setLoadDate(loadDateTime);
		loadDatePassedMacroPlan.addToAddressUserId(devManager, Constants.MailSenderRole.DEV_MANAGER);
		loadDatePassedMacroPlan.addcCAddressUserId(lead, Constants.MailSenderRole.LEAD);
		getMailMessageFactory().push(loadDatePassedMacroPlan);
		lImpPlan.setLoadDateMacroMailFlag(Boolean.TRUE);
		impPlanDAO.update(serviceUser, lImpPlan);

	    }
	}
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE)
    @Transactional
    public synchronized void deploymentDateReachedMail() {
	if (wFConfig.getPrimary()) {
	    List<String> putStatus = new ArrayList<>();
	    putStatus.add(Constants.PUTLevelOptions.LOCKDOWN.name());
	    List<PutLevel> lPutLevelList = putLevelDAO.getPutLevelList(putStatus);
	    for (PutLevel lPutLevel : lPutLevelList) {
		User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
		PutLevelDeploymentDateMail putLevelDeploymentDateMail = (PutLevelDeploymentDateMail) getMailMessageFactory().getTemplate(PutLevelDeploymentDateMail.class);
		putLevelDeploymentDateMail.setTargetSystem(lPutLevel.getSystemId().getName());
		putLevelDeploymentDateMail.setDeploymentDate(lPutLevel.getPutDateTime());
		if (wFConfig.getLoadsControlCentreMailId() != null && wFConfig.getEaVmDevCentreMailId() != null && lPutLevel.getOwnerids() != null) {
		    putLevelDeploymentDateMail.addCcLoadsControlCentre();
		    putLevelDeploymentDateMail.addToEaVmDevCentre();
		    List<String> lOwerList = new ArrayList<>(Arrays.asList(lPutLevel.getOwnerids().split("\\,")));
		    for (String owner : lOwerList) {
			putLevelDeploymentDateMail.addToAddressUserId(owner, Constants.MailSenderRole.REPO_OWNERS);
		    }
		}
		getMailMessageFactory().push(putLevelDeploymentDateMail);
		lPutLevel.setDeploymentDateMailFlag(Boolean.TRUE);
		putLevelDAO.update(serviceUser, lPutLevel);
	    }
	}
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE)
    @Transactional
    public synchronized void deploymentDateReachedDevelopmentAndInitalMail() {
	if (wFConfig.getPrimary()) {
	    List<String> putStatus = new ArrayList<>();
	    putStatus.add(Constants.PUTLevelOptions.INITIAL.name());
	    putStatus.add(Constants.PUTLevelOptions.DEVELOPMENT.name());
	    List<PutLevel> lPutLevelList = putLevelDAO.getPutLevelList(putStatus);
	    List<String> putLevelStatus = new ArrayList<>();
	    for (PutLevel lPutLevel : lPutLevelList) {
		putLevelStatus.add(lPutLevel.getStatus());
		User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
		PutLevelDeploymentDateMail putLevelDeploymentDateMail = (PutLevelDeploymentDateMail) getMailMessageFactory().getTemplate(PutLevelDeploymentDateMail.class);
		putLevelDeploymentDateMail.setTargetSystem(lPutLevel.getSystemId().getName());
		putLevelDeploymentDateMail.setPutLevelStatus(putLevelStatus);
		putLevelDeploymentDateMail.setDevFlag(Boolean.TRUE);
		if (wFConfig.getLoadsControlCentreMailId() != null && wFConfig.getEaVmDevCentreMailId() != null && lPutLevel.getOwnerids() != null) {
		    putLevelDeploymentDateMail.addCcLoadsControlCentre();
		    putLevelDeploymentDateMail.addToEaVmDevCentre();
		    List<String> lOwerList = new ArrayList<>(Arrays.asList(lPutLevel.getOwnerids().split("\\,")));
		    for (String owner : lOwerList) {
			putLevelDeploymentDateMail.addToAddressUserId(owner, Constants.MailSenderRole.REPO_OWNERS);
		    }
		}
		getMailMessageFactory().push(putLevelDeploymentDateMail);
		lPutLevel.setDeploymentDateMailFlag(Boolean.TRUE);
		putLevelDAO.update(serviceUser, lPutLevel);

	    }
	}
    }

}
