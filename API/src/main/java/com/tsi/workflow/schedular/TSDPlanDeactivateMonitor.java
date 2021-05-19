package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.ui.ProdDeactivateResult;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.mail.TSDPlanDeactivateMail;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vinoth.ponnurangan
 *
 */
@Component
public class TSDPlanDeactivateMonitor {
    private static final Logger LOG = Logger.getLogger(LoadWindowMonitor.class.getName());
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    MailMessageFactory mailMessageFactory;

    @Autowired
    WFConfig wFConfig;

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_DAY)
    @Transactional
    public void planFromProductionLoadDeactivateCheckProcess() {
	if (wFConfig.getPrimary()) {
	    List<ProdDeactivateResult> impPlanAndLoadList = impPlanDAO.getImpPlanFromProductionLoadDeactivate(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
	    Set<String> planList = new HashSet<>();
	    for (ProdDeactivateResult impPlanAndLoad : impPlanAndLoadList) {
		Set<String> leadandDeveloprs = new HashSet<>();
		Set<String> Reviwers = new HashSet<>();
		Set<String> targetSystems = new HashSet<>();

		List<String> reviewerList = Arrays.asList(impPlanAndLoad.getReviewers().split(","));
		for (String reviewer : reviewerList) {
		    Reviwers.add(reviewer);
		}
		List<String> lTargetSystemList = Arrays.asList(impPlanAndLoad.getSysname().split(","));
		for (String lTagetSystem : lTargetSystemList) {
		    targetSystems.add(lTagetSystem);
		}

		leadandDeveloprs.add(impPlanAndLoad.getLeadid());
		leadandDeveloprs.add(impPlanAndLoad.getDeveloperid());
		planList.add(impPlanAndLoad.getPlanid());

		ImpPlan lImpPlan = impPlanDAO.find(impPlanAndLoad.getPlanid());
		User serviceUser = lDAPAuthenticatorImpl.getServiceUser();

		if (!lImpPlan.getDeactivateTSDMailFlag()) {
		    TSDPlanDeactivateMail tsdPlanDeactivateMail = (TSDPlanDeactivateMail) getMailMessageFactory().getTemplate(TSDPlanDeactivateMail.class);
		    tsdPlanDeactivateMail.setParentPlan(impPlanAndLoad.getPlanid());
		    tsdPlanDeactivateMail.setTargetSystem(targetSystems);
		    tsdPlanDeactivateMail.setLoadDateTime(impPlanAndLoad.getLoaddate());
		    tsdPlanDeactivateMail.setTsdDeactivateFlag(Boolean.TRUE);
		    leadandDeveloprs.stream().forEach(t -> tsdPlanDeactivateMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
		    Reviwers.stream().forEach(c -> tsdPlanDeactivateMail.addcCAddressUserId(c, Constants.MailSenderRole.PEER_REVIEWER));
		    tsdPlanDeactivateMail.addcCAddressUserId(impPlanAndLoad.getDevmanager(), Constants.MailSenderRole.DEV_MANAGER);

		    getMailMessageFactory().push(tsdPlanDeactivateMail);

		    lImpPlan.setDeactivateTSDMailFlag(Boolean.TRUE);
		    impPlanDAO.update(serviceUser, lImpPlan);
		}

	    }
	    // Get dependent Plan List from Deactivate Plan List
	    for (String planId : planList) {
		List<ProdDeactivateResult> dependentPlanList = impPlanDAO.getDependentPlanFromDeactivatePlanList(new ArrayList(Constants.PlanStatus.getPlanStatus().keySet()), planId);
		for (ProdDeactivateResult dependentPlan : dependentPlanList) {
		    Set<String> leadandDeveloprs = new HashSet<>();
		    Set<String> Reviwers = new HashSet<>();
		    Set<String> targetSystems = new HashSet<>();

		    List<String> reviewerList = Arrays.asList(dependentPlan.getReviewers().split(","));
		    for (String reviewer : reviewerList) {
			Reviwers.add(reviewer);
		    }
		    List<String> lTargetSystemList = Arrays.asList(dependentPlan.getSysname().split(","));
		    for (String lTagetSystem : lTargetSystemList) {
			targetSystems.add(lTagetSystem);
		    }

		    leadandDeveloprs.add(dependentPlan.getLeadid());
		    leadandDeveloprs.add(dependentPlan.getDeveloperid());

		    ImpPlan lDependentPlan = impPlanDAO.find(dependentPlan.getPlanid());
		    User serviceUser = lDAPAuthenticatorImpl.getServiceUser();

		    if (!lDependentPlan.getDeactivateTSDMailFlag()) {
			TSDPlanDeactivateMail tsdPlanDeactivateMail = (TSDPlanDeactivateMail) getMailMessageFactory().getTemplate(TSDPlanDeactivateMail.class);
			tsdPlanDeactivateMail.setParentPlan(planId);
			tsdPlanDeactivateMail.setTargetSystem(targetSystems);
			tsdPlanDeactivateMail.setDependentPlan(dependentPlan.getPlanid());
			tsdPlanDeactivateMail.setLoadDateTime(dependentPlan.getLoaddate());
			tsdPlanDeactivateMail.setTsdDeactivateFlag(Boolean.FALSE);
			leadandDeveloprs.stream().forEach(t -> tsdPlanDeactivateMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
			Reviwers.stream().forEach(c -> tsdPlanDeactivateMail.addcCAddressUserId(c, Constants.MailSenderRole.PEER_REVIEWER));
			tsdPlanDeactivateMail.addcCAddressUserId(dependentPlan.getDevmanager(), Constants.MailSenderRole.DEV_MANAGER);

			getMailMessageFactory().push(tsdPlanDeactivateMail);

			lDependentPlan.setDeactivateTSDMailFlag(Boolean.TRUE);
			impPlanDAO.update(serviceUser, lDependentPlan);
		    }
		}

	    }

	}
    }

}
