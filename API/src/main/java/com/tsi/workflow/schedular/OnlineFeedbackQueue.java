/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.management.timer.Timer;
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
public class OnlineFeedbackQueue {

    private static final Logger LOG = Logger.getLogger(OnlineFeedbackQueue.class.getName());

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    FallbackHelper fallbackHelper;

    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    @Autowired
    CacheClient cacheClient;

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND * 10)
    @Transactional
    public void monitor() {
	if (cacheClient.getOnlineAcceptPlanMap().isEmpty()) {
	    return;
	}

	clearAcceptInprogressPlan();
	String inProgressPlanId = getInProgressAcceptPlan();
	if (inProgressPlanId == null) {
	    ImpPlan plan = validateAndGetPlan();
	    if (plan != null) {
		executeOnlineFeedback(plan);
	    }
	}
    }

    private String getInProgressAcceptPlan() {
	String lPlanId = null;
	List<String> lPlanList = cacheClient.getOnlineAcceptPlanMap().entrySet().stream().filter(lPlanInfo -> lPlanInfo.getValue().getAction().equalsIgnoreCase("INPROGRESS")).map(lPlanInfo -> lPlanInfo.getKey()).collect(Collectors.toList());
	LOG.info("Positive Feedback processing plan id -> " + lPlanId);
	if (lPlanList != null && !lPlanList.isEmpty()) {
	    lPlanId = lPlanList.stream().findFirst().get();
	}
	return lPlanId;
    }

    private void clearAcceptInprogressPlan() {
	List<String> lPlanLists = cacheClient.getOnlineAcceptPlanMap().keySet().stream().collect(Collectors.toList());
	if (lPlanLists != null && !lPlanLists.isEmpty()) {
	    lPlanLists.stream().forEach(t -> {
		ImpPlan lPlan = getImpPlanDAO().find(t);
		if (lPlan.getInprogressStatus().equalsIgnoreCase(Constants.PlanInProgressStatus.NONE.name()) && lPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name())) {
		    cacheClient.getOnlineAcceptPlanMap().remove(t);
		}
	    });
	}
    }

    private ImpPlan validateAndGetPlan() {
	List<String> lPlanLists = cacheClient.getOnlineAcceptPlanMap().keySet().stream().collect(Collectors.toList());
	if (lPlanLists == null || lPlanLists.isEmpty()) {
	    return null;
	}

	ImpPlan lPlan = getImpPlanDAO().getAcceptedInProductionPlan(new ArrayList(lPlanLists));
	if (lPlan == null) {
	    return null;
	}
	return lPlan;
    }

    private void executeOnlineFeedback(ImpPlan plan) {
	TosActionQueue lAcceptPlan = cacheClient.getOnlineAcceptPlanMap().get(plan.getId());
	lAcceptPlan.setAction("INPROGRESS");
	cacheClient.getOnlineAcceptPlanMap().put(plan.getId(), lAcceptPlan);

	LOG.info("Processing plan for Online Positive Feedback - " + plan.getId());
	plan.setInprogressStatus(Constants.PlanInProgressStatus.ONLINE.name());
	getImpPlanDAO().update(lAcceptPlan.getUser(), plan);
	getFallbackHelper().onlineJenkinsJob(lAcceptPlan.getUser(), plan);
    }

}
