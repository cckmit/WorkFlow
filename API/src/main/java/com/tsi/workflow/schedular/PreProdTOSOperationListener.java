/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static com.tsi.workflow.utils.Constants.LOAD_SET_STATUS.LOADED;

import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.PreTOSActivityMessage;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.service.TestSystemSupportService;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
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
public class PreProdTOSOperationListener {

    private static final Logger LOG = Logger.getLogger(PreProdTOSOperationListener.class.getName());

    @Autowired
    ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;

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
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
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

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doTOSOperation() {
	for (Map.Entry<String, Set<TosActionQueue>> lPlanVsPreProd : lPreProdTOSOperationMap.entrySet()) {
	    String[] lPlanAndSystemId = lPlanVsPreProd.getKey().split("_");
	    ImpPlan lPlan = getImpPlanDAO().find(lPlanAndSystemId[0]);
	    Set<TosActionQueue> lRemovedList = new HashSet();
	    Boolean isActionFails = false;
	    for (TosActionQueue lPreProdAction : lPlanVsPreProd.getValue()) {
		PreProductionLoads lPreProdLoad = getPreProductionLoadsDAO().find(lPreProdAction.getTosRecId());
		try {
		    Constants.LOAD_SET_STATUS lStatus = Constants.LOAD_SET_STATUS.valueOf(lPreProdLoad.getStatus());
		    SystemLoad lSystemLoad = getSystemLoadDAO().find(lPlan, lPreProdLoad.getSystemId());

		    // Dependency Check - Plan which are prior should be loaded first
		    SortedSet<String> deptPlans = new TreeSet<String>();
		    SortedSet<String> inprogressPlans = new TreeSet<String>();
		    List<String> split = Arrays.asList(lPreProdLoad.getPlanId().getRelatedPlans().split(","));
		    if (!split.isEmpty()) {
			List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lPreProdLoad.getPlanId().getId(), split);
			for (Object[] plan : relatedPlanDetails) {
			    String planStatus = plan[1].toString();
			    String lPlanId = plan[0].toString().split("/")[0].toUpperCase();
			    if ((Constants.PlanStatus.getSubmittedToPreProd().containsKey(planStatus))) {
				List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findByPlanId(lPlanId);
				for (PreProductionLoads lPreProd : lPreProdList) {
				    if ((lPreProd.getStatus() == null)) {
					deptPlans.add(lPlanId);
				    } else if (lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name())) {
					if (Constants.LOAD_SET_STATUS.ACTIVATED.name().equalsIgnoreCase(lPreProd.getStatus())) {
					} else if (Constants.LOAD_SET_STATUS.LOADED.name().equalsIgnoreCase(lPreProd.getStatus()) && (lPreProd.getLastActionStatus().equalsIgnoreCase("FAILED"))) {
					    deptPlans.add(lPlanId);
					} else if (Constants.LOAD_SET_STATUS.LOADED.name().equalsIgnoreCase(lPreProd.getStatus()) && (lPreProd.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
					    inprogressPlans.add(lPlanId);
					} else if (Constants.LOAD_SET_STATUS.LOADED.name().equalsIgnoreCase(lPreProd.getStatus()) && (lPreProd.getLastActionStatus().equalsIgnoreCase("SUCCESS"))) {
					} else {
					    deptPlans.add(lPlanId);
					}
				    } else if (lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
					if (Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lPreProd.getStatus()) && (lPreProd.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
					    inprogressPlans.add(lPlanId);
					} else if (Constants.LOAD_SET_STATUS.ACTIVATED.name().equalsIgnoreCase(lPreProd.getStatus()) && (lPreProd.getLastActionStatus().equalsIgnoreCase("SUCCESS"))) {
					} else {
					    deptPlans.add(lPlanId);
					}
				    } else {
					deptPlans.add(lPlanId);
				    }
				}
			    }
			}
		    }

		    List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lPreProdLoad.getPlanId().getId());
		    for (Object[] plan : segmentRelatedPlans) {
			String planStatus = plan[1].toString();
			String[] lTempPlanId = plan[0].toString().split("/");
			String lPlanId = lTempPlanId[0];
			String lSystemName = lTempPlanId[1];
			if (lSystemName.equalsIgnoreCase(lSystemLoad.getSystemId().getName()) && Constants.PlanStatus.getSubmittedToPreProd().containsKey(planStatus)) {
			    List<PreProductionLoads> lLoadedList = getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(lPlanId), lSystemLoad.getSystemId());
			    if (lLoadedList == null || lLoadedList.isEmpty()) {
				deptPlans.add(lPlanId);
			    } else {
				for (PreProductionLoads lLoaded : lLoadedList) {
				    if ((lLoaded.getStatus() == null)) {
					deptPlans.add(lPlanId);
				    } else if (lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name())) {
					if (Constants.LOAD_SET_STATUS.ACTIVATED.name().equalsIgnoreCase(lLoaded.getStatus())) {
					} else if (Constants.LOAD_SET_STATUS.LOADED.name().equalsIgnoreCase(lLoaded.getStatus()) && (lLoaded.getLastActionStatus().equalsIgnoreCase("FAILED"))) {
					    deptPlans.add(lPlanId);
					} else if (Constants.LOAD_SET_STATUS.LOADED.name().equalsIgnoreCase(lLoaded.getStatus()) && (lLoaded.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
					    inprogressPlans.add(lPlanId);
					} else if (Constants.LOAD_SET_STATUS.LOADED.name().equalsIgnoreCase(lLoaded.getStatus()) && (lLoaded.getLastActionStatus().equalsIgnoreCase("SUCCESS"))) {
					} else {
					    deptPlans.add(lPlanId);
					}
				    } else if (lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
					if (Constants.LOAD_SET_STATUS.getActivateAndAbove().contains(lLoaded.getStatus()) && (lLoaded.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
					    inprogressPlans.add(lPlanId);
					} else if (Constants.LOAD_SET_STATUS.ACTIVATED.name().equalsIgnoreCase(lLoaded.getStatus()) && (lLoaded.getLastActionStatus().equalsIgnoreCase("SUCCESS"))) {
					} else {
					    deptPlans.add(lPlanId);
					}
				    }
				}
			    }
			}
		    }

		    if (deptPlans.size() > 0) {
			StringBuilder sb = new StringBuilder();
			sb.append(lPreProdLoad.getPlanId().getId()).append("/").append(lSystemLoad.getSystemId().getName()).append(" cannot be ").append(lPreProdLoad.getStatus()).append(" to Pre-Prod as dependent Plan(s) ").append(StringUtils.join(deptPlans, ", ")).append(" are not yet ").append(lPreProdLoad.getStatus()).append(" in Pre-Prod. Please contact leads of dependent plans to get them loaded in Pre-Prod ");
			CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
			lMessage.setMessage(sb.toString());
			lMessage.setStatus("Fail");
			getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);
			isActionFails = true;
			wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lPreProdAction.getUser(), (Object) sb);
			LOG.info("dependent plans are - " + String.join(",", deptPlans));
		    }
		    if (!isActionFails && !inprogressPlans.isEmpty()) {
			LOG.info("Inprogress plans are - " + String.join(",", inprogressPlans) + " for plan id - " + lSystemLoad.getPlanId().getId());
			continue;
		    }
		    if (!isActionFails) {
			switch (lStatus) {
			case LOADED: {
			    LOG.info("Calling IBM PRE PROD TOS Load Activate : " + lSystemLoad.getLoadSetName());
			    boolean ip = getTOSHelper().requestPreProdIP(lPreProdAction.getUser(), lSystemLoad, lPreProdLoad.getCpuId());

			    if (!ip) {
				LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());

				CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
				lMessage.setMessage("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
				lMessage.setStatus("Fail");
				getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);
				String lWSMessage = "Error: Plan - " + lPlan.getId() + " Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName() + " CPU id - " + lPreProdLoad.getCpuId();
				wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lPreProdAction.getUser(), (Object) lWSMessage);
				isActionFails = true;
				break;
			    }

			    String ipAddress = getTOSHelper().getIP(lSystemLoad.getId());

			    if (ipAddress.isEmpty()) {
				LOG.error("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
				CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
				lMessage.setMessage("Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName());
				lMessage.setStatus("Fail");
				getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);
				String lWSMessage = "Error: Plan - " + lPlan.getId() + " Unable to get IP Adress for system " + lSystemLoad.getSystemId().getName() + " CPU id - " + lPreProdLoad.getCpuId();
				wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lPreProdAction.getUser(), (Object) lWSMessage);
				isActionFails = true;
				break;
			    }

			    Constants.BUILD_TYPE lBuildType = Constants.BUILD_TYPE.STG_LOAD;
			    Build lBuild = getBuildDAO().findLastSuccessfulBuild(lPlan.getId(), lSystemLoad.getSystemId().getId(), lBuildType);
			    if (lBuild == null) {
				CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
				lMessage.setMessage("Staging loadset not created");
				lMessage.setStatus("Fail");
				getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);

				isActionFails = true;
				break;
			    }
			    ProdFTPActivityMessage lMessage = new ProdFTPActivityMessage(lPlan, null, lSystemLoad);
			    lMessage.setIpAddress(ipAddress);
			    getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);
			    JSONResponse lSSHResponse = getFTPHelper().doFTP(lPreProdAction.getUser(), lSystemLoad, lBuild, ipAddress, false);
			    if (!lSSHResponse.getStatus()) {
				lMessage.setStatus("Failed");
				getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);
				String lWSMessage = "Error: Plan - " + lPlan.getId() + " Unable to FTP Loadset for system " + lSystemLoad.getSystemId().getName() + " CPU id - " + lPreProdLoad.getCpuId();
				wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lPreProdAction.getUser(), (Object) lWSMessage);
				isActionFails = true;
				break;
			    } else {
				lMessage.setStatus("Success");
				getActivityLogDAO().save(lPreProdAction.getUser(), lMessage);
				getFTPHelper().getYodaLoadSetPath(lPreProdAction.getUser(), lPlan, lSSHResponse);
			    }

			    boolean lResult = getTOSHelper().doPreTOSOperation(lPreProdAction.getUser(), Constants.LoadSetCommands.LOAD, lPreProdLoad, lPreProdAction.getOldStatus(), lSystemLoad, false);
			    if (!lResult) {
				String lWSMessage = "Error: Plan - " + lPlan.getId() + ", Unable to Load the Loadset for system " + lSystemLoad.getSystemId().getName() + " CPU id - " + lPreProdLoad.getCpuId();
				wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lPreProdAction.getUser(), (Object) lWSMessage);
				isActionFails = true;
			    } else {
				lAsyncPlansStartTimeMap.put(lPlan.getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lPreProdLoad.getStatus(), java.lang.System.currentTimeMillis());
				PreTOSActivityMessage lActivityMessage = new PreTOSActivityMessage(lPreProdLoad.getPlanId(), null, lPreProdLoad);
				lActivityMessage.setLoadActionStatus(lPreProdLoad.getStatus());
				activityLogDAO.save(lPreProdAction.getUser(), lActivityMessage);
				lRemovedList.add(lPreProdAction);
			    }
			    break;
			}
			case ACTIVATED:
			    boolean lResult = getTOSHelper().doPreTOSOperation(lPreProdAction.getUser(), Constants.LoadSetCommands.ACTIVATE, lPreProdLoad, lPreProdAction.getOldStatus(), lSystemLoad, false);
			    if (!lResult) {
				String lWSMessage = "Error: Plan - " + lPlan.getId() + ", Unable to Activate Loadset for system " + lSystemLoad.getSystemId().getName() + " CPU id - " + lPreProdLoad.getCpuId();
				wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, lPreProdAction.getUser(), (Object) lWSMessage);
				isActionFails = true;
			    } else {
				lAsyncPlansStartTimeMap.put(lPlan.getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lPreProdLoad.getStatus(), java.lang.System.currentTimeMillis());
				PreTOSActivityMessage lActivityMessage = new PreTOSActivityMessage(lPreProdLoad.getPlanId(), null, lPreProdLoad);
				lActivityMessage.setLoadActionStatus(lPreProdLoad.getStatus());
				activityLogDAO.save(lPreProdAction.getUser(), lActivityMessage);
				lRemovedList.add(lPreProdAction);
			    }
			    break;

			case DEACTIVATED:
			case DELETED:
			}
		    }
		    if (isActionFails) {
			lPreProdLoad.setStatus(lPreProdAction.getOldStatus());
			lPreProdLoad.setLastActionStatus("FAILED");
			getPreProductionLoadsDAO().update(lPreProdAction.getUser(), lPreProdLoad);
			PreTOSActivityMessage lActivityMessage = new PreTOSActivityMessage(lPreProdLoad.getPlanId(), null, lPreProdLoad);
			lActivityMessage.setLoadActionStatus(lPreProdLoad.getStatus());
			activityLogDAO.save(lPreProdAction.getUser(), lActivityMessage);
			lRemovedList.add(lPreProdAction);
		    }
		} catch (Exception ex) {
		    LOG.info("Exception in TOS Variant Operation", ex);
		    lPreProdLoad.setStatus(lPreProdAction.getOldStatus());
		    lPreProdLoad.setLastActionStatus("FAILED");
		    getPreProductionLoadsDAO().update(lPreProdAction.getUser(), lPreProdLoad);
		    getActivityLogDAO().save(lPreProdAction.getUser(), new PreTOSActivityMessage(lPreProdLoad.getPlanId(), null, lPreProdLoad));
		    lRemovedList.add(lPreProdAction);
		}
	    }
	    if (!lRemovedList.isEmpty()) {
		Set<TosActionQueue> lTempQueue = lPlanVsPreProd.getValue();
		lTempQueue.removeAll(lRemovedList);
		if (lTempQueue.isEmpty()) {
		    lPreProdTOSOperationMap.remove(lPlanVsPreProd.getKey());
		} else {
		    lPreProdTOSOperationMap.replace(lPlanVsPreProd.getKey(), lTempQueue);
		}
	    }
	}
    }

}
