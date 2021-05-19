/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static com.tsi.workflow.utils.Constants.LOAD_SET_STATUS.LOADED;

import com.tsi.workflow.User;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.TOSActivityMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.service.TestSystemSupportService;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.management.timer.Timer;
import org.apache.commons.lang.StringUtils;
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
public class ProdTOSOperationListener {

    private static final Logger LOG = Logger.getLogger(ProdTOSOperationListener.class.getName());

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    ProductionLoadsDAO productionLoadsDAO;

    @Autowired
    TestSystemSupportService tssService;

    @Autowired
    SystemLoadDAO systemLoadDAO;

    @Autowired
    TOSHelper tOSHelper;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    BuildDAO buildDAO;

    @Autowired
    FTPHelper fTPHelper;

    @Autowired
    WSMessagePublisher wsserver;

    @Autowired
    SystemDAO systemDAO;

    // @Autowired
    // ConcurrentLinkedQueue<TosActionQueue> prodTOSInProgressPlan;
    @Autowired
    CacheClient cacheClient;

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public TestSystemSupportService getTestSystemSupportService() {
	return tssService;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public TOSHelper getTOSHelper() {
	return tOSHelper;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doTOSOperation() {
	Boolean isPlanInProgress = cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(t -> t.getValue().getQueueStatus().equalsIgnoreCase("INPROGRESS")).findAny().isPresent();
	if (isPlanInProgress) {
	    return;
	}

	cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().forEach(tosAction -> {
	    TosActionQueue t = tosAction.getValue();
	    if (t.getQueueStatus().equalsIgnoreCase("FAILED") && t.getAction().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
		updateOtherTOSRequestAsFailed(t.getUser(), t);
	    }
	});

	for (Map.Entry<String, TosActionQueue> lPlanVsAction : cacheClient.getProdTOSLoadActPlansMap().entrySet()) {
	    User user = lPlanVsAction.getValue().getUser();
	    TosActionQueue lProdAction = lPlanVsAction.getValue();
	    if (lProdAction.getQueueStatus() != null && lProdAction.getQueueStatus().equalsIgnoreCase("TOPROCESS")) {
		LOG.info(lProdAction.getTosRecId());
		int prodLoadValidation = planProdTOSValidation(user, lProdAction);
		Boolean tosOperation = true;
		if (prodLoadValidation == 0) {
		    tosOperation = tosOperation(user, lProdAction);
		    if (tosOperation) {
			lProdAction.setQueueStatus("INPROGRESS");
			cacheClient.getProdTOSLoadActPlansMap().put(lProdAction.getPlanId() + "_" + lProdAction.getSystemName(), lProdAction);
		    } else {
			lProdAction.setQueueStatus("FAILED");
			lProdAction.setTosResponseMessage("Unable to process the TOS TPF Command for system - " + lProdAction.getSystemName());
			cacheClient.getProdTOSLoadActPlansMap().put(lProdAction.getPlanId() + "_" + lProdAction.getSystemName(), lProdAction);
		    }
		    return; // Run the loop for one time
		}
	    }
	}
    }

    private boolean updateOtherTOSRequestAsFailed(User user, TosActionQueue tosActionQueue) {
	ProductionLoads lFailedPlan = getProductionLoadsDAO().find(tosActionQueue.getTosRecId());
	List<TosActionQueue> lProdLoadQueues = new ArrayList();
	cacheClient.getProdTOSLoadActPlansMap().entrySet().stream().filter(t -> t.getValue().getSystemName().equalsIgnoreCase(tosActionQueue.getSystemName()) && t.getValue().getUser().getId().equalsIgnoreCase(user.getId()) && t.getValue().getAction().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && t.getValue().getQueueStatus().equalsIgnoreCase("TOPROCESS")).forEach(t -> lProdLoadQueues.add(t.getValue()));

	lProdLoadQueues.stream().forEach(lQueueLoad -> {
	    ProductionLoads lProductionLoad = getProductionLoadsDAO().find(lQueueLoad.getTosRecId());
	    lProductionLoad.setStatus(lQueueLoad.getOldStatus());
	    lProductionLoad.setLastActionStatus("FAILED");
	    getProductionLoadsDAO().update(lQueueLoad.getUser(), lProductionLoad);

	    String sb = "Unable to Activate the Loadset for system " + lProductionLoad.getSystemId().getName() + " as activate action failed for plan " + lFailedPlan.getPlanId().getId() + " which is part of multi select operation";
	    CommonActivityMessage lMessage = new CommonActivityMessage(lProductionLoad.getPlanId(), null);
	    lMessage.setMessage(sb);
	    lMessage.setStatus("Fail");
	    getActivityLogDAO().save(lQueueLoad.getUser(), lMessage);

	    lQueueLoad.setTosResponseMessage(sb);
	    lQueueLoad.setQueueStatus("FAILED");
	    cacheClient.getProdTOSLoadActPlansMap().put(lQueueLoad.getPlanId() + "_" + lQueueLoad.getSystemName(), lQueueLoad);

	});
	return true;
    }

    private int planProdTOSValidation(User user, TosActionQueue tosActionQueue) {
	ProductionLoads lProdLoad = getProductionLoadsDAO().find(tosActionQueue.getTosRecId());
	ImpPlan lPlan = lProdLoad.getPlanId();
	SystemLoad lSystemLoad = lProdLoad.getSystemLoadId();
	System lSystem = lProdLoad.getSystemId();
	int prodLoadValidation = 0;

	// Dependency Check - Plan which are prior should be loaded first
	Set<String> lParentPlans = new TreeSet();
	SortedSet<String> deptPlans = new TreeSet<>();
	SortedSet<String> inprogressPlans = new TreeSet<>();
	List<ProductionLoads> lParentProdLoads = new ArrayList();

	// Fetch Dependent Plans - both Manual and Auto Dependent Plans
	List<String> split = Arrays.asList(lProdLoad.getPlanId().getRelatedPlans().split(","));
	if (split != null && !split.isEmpty()) {
	    List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split);
	    if (relatedPlanDetails != null) {
		relatedPlanDetails.forEach(t -> lParentPlans.add(t[0].toString() + "/" + t[1].toString()));
	    }
	}

	List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId());
	if (segmentRelatedPlans != null && !segmentRelatedPlans.isEmpty()) {
	    segmentRelatedPlans.forEach(t -> lParentPlans.add(t[0].toString() + "/" + t[1].toString()));
	}

	for (String lParentPlan : lParentPlans) {
	    String lPlanId = lParentPlan.split("/")[0];
	    String lTarSystem = lParentPlan.split("/")[1];
	    String lPlanStatus = lParentPlan.split("/")[2];

	    LOG.info("Dep Plan Info - " + lParentPlan);
	    if (!lTarSystem.equalsIgnoreCase(lSystem.getName())) {
		continue;
	    }

	    ImpPlan lTempPlan = getImpPlanDAO().find(lPlanId);

	    if (!Constants.PlanStatus.getPreSegmentProdLoadActivateStatus().containsKey(lPlanStatus)) {
		deptPlans.add(lPlanId);
		continue;
	    }

	    // Validation for macro/header plan - if it is macro then exclude the plan from
	    // validation
	    if (lTempPlan.getMacroHeader()) {
		continue;
	    }

	    if (Constants.PlanStatus.ONLINE.name().equalsIgnoreCase(lPlanStatus)) {
		List<ProductionLoads> lTProdLoads = new ArrayList();
		lTProdLoads = getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(lPlanId), new System(lProdLoad.getSystemId().getId()));
		if (lTProdLoads.stream().filter(t -> !t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name())).findAny().isPresent()) {
		    deptPlans.add(lPlanId);
		}
	    }

	    if (Constants.PlanStatus.getPRODDeploymentPlanStatus().keySet().contains(lPlanStatus)) {
		lParentProdLoads = getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(lPlanId), new com.tsi.workflow.beans.dao.System(lProdLoad.getSystemId().getId()));
		if (lParentProdLoads == null || lParentProdLoads.isEmpty()) {
		    deptPlans.add(lPlanId);
		} else {
		    for (ProductionLoads lTProdLoad : lParentProdLoads) {
			if ((lTProdLoad.getStatus() == null)) {
			    deptPlans.add(lPlanId);
			} else if (lTProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    if (Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lTProdLoad.getStatus()) && (lTProdLoad.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
				inprogressPlans.add(lPlanId);
			    } else if (Constants.LOAD_SET_STATUS.ACTIVATED.name().equalsIgnoreCase(lTProdLoad.getStatus()) && (lTProdLoad.getLastActionStatus().equalsIgnoreCase("SUCCESS"))) {
			    } else {
				deptPlans.add(lPlanId);
			    }
			}
		    }
		}
	    }
	}
	if (!deptPlans.isEmpty()) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(lProdLoad.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName()).append(" cannot be ").append(lProdLoad.getStatus()).append(" in Prod until ").append(StringUtils.join(deptPlans, ", ")).append(" are ").append(lProdLoad.getStatus());

	    CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
	    lMessage.setMessage(sb.toString());
	    lMessage.setStatus("Fail");
	    getActivityLogDAO().save(user, lMessage);
	    prodLoadValidation = 8;
	    wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, user, (Object) sb);
	    LOG.info("dependent plans are - " + String.join(",", deptPlans));

	    tosActionQueue.setTosResponseMessage(sb.toString());
	    tosActionQueue.setQueueStatus("FAILED");
	}

	if (deptPlans.isEmpty() && !inprogressPlans.isEmpty()) {
	    prodLoadValidation = 4;
	    LOG.info("Inprogress plans are - " + String.join(",", inprogressPlans));
	}

	return prodLoadValidation;
    }

    private boolean tosOperation(User user, TosActionQueue tosActionQueue) {

	ProductionLoads lProdLoad = getProductionLoadsDAO().find(tosActionQueue.getTosRecId());
	ImpPlan lPlan = lProdLoad.getPlanId();
	SystemLoad lSystemLoad = lProdLoad.getSystemLoadId();
	Constants.LOAD_SET_STATUS loadSetActions = Constants.LOAD_SET_STATUS.valueOf(lProdLoad.getStatus());
	boolean loadsetAction = true;
	switch (loadSetActions) {
	case LOADED: {
	    LOG.info("Calling IBM PROD TOS Load : " + lSystemLoad.getLoadSetName());
	    boolean lResult = getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.LOAD, lProdLoad, tosActionQueue.getOldStatus(), lSystemLoad);
	    if (!lResult) {
		String lWSMessage = "Error: Plan - " + lPlan.getId() + ", Unable to Load the Loadset for system " + lSystemLoad.getSystemId().getName();
		wsserver.sendMessage(Constants.Channels.PROD_LOAD, user, (Object) lWSMessage);
		tosActionQueue.setQueueStatus("FAILED");
		tosActionQueue.setTosResponseMessage(lWSMessage);
		loadsetAction = false;
	    } else {
		getActivityLogDAO().save(user, new TOSActivityMessage(lProdLoad.getPlanId(), null, lProdLoad));
	    }
	    break;
	}

	case ACTIVATED:
	    LOG.info("Calling IBM PROD TOS Activate : " + lSystemLoad.getLoadSetName());
	    boolean lResult = getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.ACTIVATE, lProdLoad, tosActionQueue.getOldStatus(), lSystemLoad);
	    if (!lResult) {
		String lWSMessage = "Error: Plan - " + lPlan.getId() + ", Unable to Activate Loadset for system " + lSystemLoad.getSystemId().getName();
		wsserver.sendMessage(Constants.Channels.PROD_LOAD, user, (Object) lWSMessage);
		tosActionQueue.setQueueStatus("FAILED");
		tosActionQueue.setTosResponseMessage(lWSMessage);
		loadsetAction = false;
	    } else {
		getActivityLogDAO().save(user, new TOSActivityMessage(lProdLoad.getPlanId(), null, lProdLoad));
	    }
	    break;

	case DEACTIVATED:
	case DELETED:
	}
	if (!loadsetAction) {
	    lProdLoad.setStatus(tosActionQueue.getOldStatus());
	    lProdLoad.setLastActionStatus("FAILED");
	    getProductionLoadsDAO().update(user, lProdLoad);
	    getActivityLogDAO().save(user, new TOSActivityMessage(lProdLoad.getPlanId(), null, lProdLoad));
	}

	return loadsetAction;
    }
}
