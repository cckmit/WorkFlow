/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.activity.TOSActivityMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.tos.ProdMessageProcessor;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
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
public class ProdMultiAcceptSchedular {

    private static final Logger LOG = Logger.getLogger(ProdMultiAcceptSchedular.class.getName());

    @Autowired
    SystemDAO systemDAO;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    SystemLoadDAO systemLoadDAO;

    @Autowired
    ProductionLoadsDAO productionLoadsDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    WSMessagePublisher wsserver;

    @Autowired
    TOSHelper tOSHelper;

    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;

    @Autowired
    FallbackHelper fallbackHelper;

    @Autowired
    ProdMessageProcessor prodMessageProcessor;

    @Autowired
    PlanHelper planHelper;

    @Autowired
    CacheClient cacheClient;

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public TOSHelper getTOSHelper() {
	return tOSHelper;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND * 10)
    @Transactional
    public void monitor() {
	if (!isPlanAcceptInProgress()) {
	    updateAcceptProcessedPlan();
	    if (isAnyPlantoAccept()) {
		accpetLoadsetInSystem();
	    }
	}
    }

    private boolean isAnyPlantoAccept() {
	Boolean lPlanInprgress = cacheClient.getProdTOSAcceptPlansMap().entrySet().stream().filter(t -> t.getValue().getQueueStatus().equalsIgnoreCase("TOPROCESS")).findAny().isPresent();
	return lPlanInprgress;
    }

    private boolean isPlanAcceptInProgress() {
	Boolean lPlanInprgress = cacheClient.getProdTOSAcceptPlansMap().entrySet().stream().filter(t -> t.getValue().getQueueStatus().equalsIgnoreCase("INPROCESS")).findAny().isPresent();
	return lPlanInprgress;
    }

    private void updateAcceptProcessedPlan() {
	cacheClient.getProdTOSAcceptPlansMap().entrySet().forEach((t) -> {
	    String planId = t.getKey();
	    if (t.getValue().getQueueStatus().equalsIgnoreCase("FAILED")) {
		LOG.info("In Progress plan - " + planId);
		ImpPlan lPlan = getImpPlanDAO().find(planId);
		lPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
		getImpPlanDAO().update(t.getValue().getUser(), lPlan);

		cacheClient.getProdTOSAcceptPlansMap().entrySet().stream().filter(tosAction -> tosAction.getValue().getUser().getId().equalsIgnoreCase(t.getValue().getUser().getId()) && tosAction.getValue().getQueueStatus().equalsIgnoreCase("TOPROCESS")).forEach(tosAction -> {
		    tosAction.getValue().setQueueStatus("FAILED");
		    tosAction.getValue().setTosResponseMessage("Accept fails as plan Id - " + planId + " accept process fails and tool stop processing this plan, Please try again");
		    ImpPlan lOtherPlan = getImpPlanDAO().find(tosAction.getKey());
		    lOtherPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
		    getImpPlanDAO().update(t.getValue().getUser(), lOtherPlan);
		});
	    }
	});
    }

    private void accpetLoadsetInSystem() {
	JobDetails lwssSetOnline = new JobDetails();
	for (Map.Entry<String, TosActionQueue> planVsAction : cacheClient.getProdTOSAcceptPlansMap().entrySet()) {
	    TosActionQueue lPlanToAccept = planVsAction.getValue();
	    User user = lPlanToAccept.getUser();
	    Boolean tosOperation = Boolean.TRUE;
	    if (!lPlanToAccept.getQueueStatus().equalsIgnoreCase("TOPROCESS")) {
		continue;
	    }
	    LOG.info("Processing Plan - " + lPlanToAccept.getPlanId());

	    ImpPlan lPlan = getImpPlanDAO().find(lPlanToAccept.getPlanId());
	    int validationCode = validatePlanForAccept(user, lPlan);
	    LOG.info("Validation Code " + validationCode + " for plan " + lPlan.getId());
	    if (validationCode == 0) {
		List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(lPlan);
		for (SystemLoad systemLoad : lSystemLoads) {
		    LOG.info("Plan " + lPlan.getId() + " processing started for ONLINE");
		    ProductionLoads lProdLoad = getProductionLoadsDAO().findByPlanId(lPlan, systemLoad.getSystemId());
		    String lOldStatus = lProdLoad.getStatus();
		    if (!lOldStatus.equals(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {

			if (!lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
			    lAsyncPlansStartTimeMap.put(lPlan.getId() + "-" + Constants.LOAD_SET_STATUS.ACCEPTED.name(), java.lang.System.currentTimeMillis());
			}
			lwssSetOnline.setStatus(lPlan.getId() + ": zTPF Online feedback has started for " + systemLoad.getSystemId().getName());
			wsserver.sendMessage(Constants.Channels.ONLINE_PROCESS, user, lwssSetOnline);

			lProdLoad.setStatus(Constants.LOAD_SET_STATUS.ACCEPTED.name());

			boolean lResult = getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.ACCEPT, lProdLoad, lOldStatus, systemLoad);
			setStatus(lResult, lProdLoad, lOldStatus);
			getProductionLoadsDAO().update(user, lProdLoad);
			getActivityLogDAO().save(user, new TOSActivityMessage(lProdLoad.getPlanId(), null, lProdLoad));

			if (!lResult) {
			    tosOperation = Boolean.FALSE;
			    lPlanToAccept.setTosResponseMessage("Unable to trigger the TPF Command to system - " + systemLoad.getSystemId().getName());
			}
		    }
		}
		if (tosOperation) {
		    lPlanToAccept.setQueueStatus("INPROCESS");
		} else {
		    lPlanToAccept.setQueueStatus("FAILED");
		}
	    } else if (validationCode == 8) {
		lPlanToAccept.setQueueStatus("FAILED");
		lPlanToAccept.setTosResponseMessage("Parent plan was not accepted");
	    }
	    cacheClient.getProdTOSAcceptPlansMap().put(lPlanToAccept.getPlanId(), lPlanToAccept);
	    break;
	}
    }

    private void setStatus(Boolean lResult, ProductionLoads lLoad, String lOldStatus) {
	if (!lResult) {
	    lLoad.setLastActionStatus("FAILED");
	    if (lOldStatus != null) {
		lLoad.setStatus(lOldStatus);
	    } else {
		lLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    }
	} else {
	    lLoad.setLastActionStatus("INPROGRESS");
	}
    }

    private int validatePlanForAccept(User user, ImpPlan plan) {
	LOG.info("Valdiating plan for processing " + plan.getId());
	int returnCode = 0;

	// Plan Validation - Segment and Manual Dependency Plan List
	Set<String> lDepPlans = new TreeSet();
	if (plan.getRelatedPlans() != null && !plan.getRelatedPlans().isEmpty()) {
	    lDepPlans.addAll(Arrays.asList(plan.getRelatedPlans().split(",")));
	}
	List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(plan.getId());
	segmentRelatedPlans.stream().map((lPlan) -> lPlan[0].toString().split("/")[0]).forEachOrdered((lPlanId) -> {
	    lDepPlans.add(lPlanId);
	});

	for (String lPlanId : lDepPlans) {
	    ImpPlan lDepPlanInfo = getImpPlanDAO().find(lPlanId);
	    LOG.info("Parent Plan " + lDepPlanInfo.getId());
	    List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(lDepPlanInfo);
	    if (lProdLoads.stream().filter(t -> t.getSystemId() == null && t.getLastActionStatus().equalsIgnoreCase("INPROGRESS")).findAny().isPresent()) {
		LOG.info("Plan - " + plan.getId() + " needs to wait for dependent plan - " + lDepPlanInfo.getId());
		returnCode = 4;
	    }
	    if (lDepPlanInfo.getInprogressStatus() != null && (Constants.PlanInProgressStatus.ACCEPT.name().equalsIgnoreCase(lDepPlanInfo.getInprogressStatus()))) {
		returnCode = 4;
	    } else if (!(Constants.PlanStatus.getPreSegmentOnlineStatus().containsKey(lDepPlanInfo.getPlanStatus()))) {
		returnCode = 8;
	    }
	}
	return returnCode;
    }

}
