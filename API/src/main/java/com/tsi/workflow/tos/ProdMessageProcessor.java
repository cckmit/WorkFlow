/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.activity.TOSActivityMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.ExceptionSourceContentionMail;
import com.tsi.workflow.mail.ProdLoadsetActivationMail;
import com.tsi.workflow.mail.StackHolderLoadsetMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.mail.TSDDependentPlanMail;
import com.tsi.workflow.schedular.jenkins.DEVLBuildMonitor;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.WFLOGGER;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author prabhu.prabhakaran
 */
@Component
public class ProdMessageProcessor {

    @Autowired
    TOSConfig tosConfig;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    FallbackHelper fallbackHelper;
    @Autowired
    PRNumberHelper prStatusUpdateinNAS;
    @Autowired
    BPMClientUtils bPMClientUtils;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    JenkinsClient jenkinsClient;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    OnlineBuildDAO onlineBuildDAO;
    @Autowired
    @Qualifier("onlineBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> onlineBuildJobs;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    TSDService tsdService;
    @Autowired
    CacheClient cacheClient;

    // <editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public TOSConfig getTOSConfig() {
	return tosConfig;
    }

    public void setTOSConfig(TOSConfig tosConfig) {
	this.tosConfig = tosConfig;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public void setProductionLoadsDAO(ProductionLoadsDAO productionLoadsDAO) {
	this.productionLoadsDAO = productionLoadsDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public void setSystemLoadDAO(SystemLoadDAO systemLoadDAO) {
	this.systemLoadDAO = systemLoadDAO;
    }

    public WSMessagePublisher getWsserver() {
	return wsserver;
    }

    public void setWsserver(WSMessagePublisher wsserver) {
	this.wsserver = wsserver;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public void setRejectHelper(RejectHelper rejectHelper) {
	this.rejectHelper = rejectHelper;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public void setImpPlanDAO(ImpPlanDAO impPlanDAO) {
	this.impPlanDAO = impPlanDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public void setMailMessageFactory(MailMessageFactory mailMessageFactory) {
	this.mailMessageFactory = mailMessageFactory;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public void setFallbackHelper(FallbackHelper fallbackHelper) {
	this.fallbackHelper = fallbackHelper;
    }

    public PRNumberHelper getPRStatusUpdateinNAS() {
	return prStatusUpdateinNAS;
    }

    public void setPRStatusUpdateinNAS(PRNumberHelper prStatusUpdateinNAS) {
	this.prStatusUpdateinNAS = prStatusUpdateinNAS;
    }

    public BPMClientUtils getBPMClientUtils() {
	return bPMClientUtils;
    }

    public void setBPMClientUtils(BPMClientUtils bPMClientUtils) {
	this.bPMClientUtils = bPMClientUtils;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public void setActivityLogDAO(ActivityLogDAO activityLogDAO) {
	this.activityLogDAO = activityLogDAO;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public void setJGitClientUtils(JGitClientUtils lGitUtils) {
	this.lGitUtils = lGitUtils;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public void setsSHClientUtils(SSHClientUtils sSHClientUtils) {
	this.sSHClientUtils = sSHClientUtils;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public JenkinsClient getJenkinsClient() {
	return jenkinsClient;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public OnlineBuildDAO getOnlineBuildDAO() {
	return onlineBuildDAO;
    }

    // </editor-fold>
    private static final Logger LOG = Logger.getLogger(ProdMessageProcessor.class.getName());

    @Transactional
    public int processProductionTOSMessage(TOSResult result) {
	int retVal = 0;
	try {
	    ProductionLoads lLoad = productionLoadsDAO.find(result.getId());
	    JSONResponse lResponse = new JSONResponse();
	    JobDetails lwssMessage = new JobDetails();
	    SystemLoad lSystemLoad = lLoad.getSystemLoadId();

	    if (result.isLast()) {

		if (result.getReturnValue() == 0) {
		    // SUCESS CASE
		    lLoad.setLastActionStatus("SUCCESS");
		    lResponse.setStatus(true);

		    // Success Update in Loads
		    productionLoadsDAO.update(result.getUser(), lLoad);

		    switch (result.getCommand()) {
		    case "AUTOLOAD": {
			lwssMessage.setMessage(lSystemLoad.getLoadSetName() + " Load was successful");
			wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name())) {
			    // Main Process - Updating System Load
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.FALLBACK_LOADED.name());
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }

			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name())) {
			    // Main Process - Updating System Load
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.LOADED.name());
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    // Reject Dependent on Emergency/Exception
			    if (lLoad.getPlanId().getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name()) || lLoad.getPlanId().getLoadType().equalsIgnoreCase(Constants.LoadTypes.EMERGENCY.name())) {
				rejectHelper.rejectDependentPlans(result.getUser(), lLoad.getPlanId().getId(), Constants.REJECT_REASON.ONLINE.getValue(), Constants.AUTOREJECT_COMMENT.ONLINE.getValue(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE);
				// Get List of Unsecured dependent plans and send source contention mail
				List<Object[]> autoDependentPlanList = impPlanDAO.getPostSegmentRelatedPlans(lLoad.getPlanId().getId(), Boolean.FALSE);
				SortedSet<String> planIds = new TreeSet();
				for (Object[] plan : autoDependentPlanList) {
				    if (Constants.PlanStatus.ACTIVE.name().equalsIgnoreCase(plan[1].toString())) {
					planIds.add(plan[2].toString());
				    }
				}
				for (String plan : planIds) {
				    ImpPlan depImpPlan = impPlanDAO.find(plan);
				    ExceptionSourceContentionMail sourceContentionMail = (ExceptionSourceContentionMail) mailMessageFactory.getTemplate(ExceptionSourceContentionMail.class);
				    sourceContentionMail.setOnlinePlan(lLoad.getPlanId().getId());
				    sourceContentionMail.setDependentPlan(depImpPlan.getId());
				    sourceContentionMail.setLeadId(depImpPlan.getLeadId());
				    mailMessageFactory.push(sourceContentionMail);
				}
			    }
			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}
		    }
			break;
		    case "LOADACT0":
		    case "LOADACT": {
			lwssMessage.setMessage(lSystemLoad.getLoadSetName() + " Activate was successful");
			wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    // Main Process - Updating Time
			    lLoad.setActivatedDateTime(new Date());
			    lLoad.setDeActivatedDateTime(null);
			    productionLoadsDAO.update(result.getUser(), lLoad);

			    // Main Process - Updating System Load
			    if (lLoad.getCpuId() == null) {
				productionLoadsDAO.updateAllActivated(result.getUser(), lLoad);
				lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name());
			    } else {
				// TODO: Need to Change the conditions
				if ("C".equals(lSystemLoad.getLoadCategoryId().getName()) || "F".equals(lSystemLoad.getLoadCategoryId().getName())) {
				    setProdLoadStatus(result.getUser(), lLoad, lSystemLoad);
				}
			    }
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Loadset " + lSystemLoad.getLoadSetName() + " has been activated");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			    sendTOSOperationMailToADL(lLoad, result.getLoadset());
			    retVal = 5;
			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())) {
			    // Main Process - Updating Time
			    lLoad.setFallbackActivatedDateTime(new Date());
			    lLoad.setFallbackDeActivatedDateTime(null);
			    productionLoadsDAO.update(result.getUser(), lLoad);

			    // Main Process - Updating System Load
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.FALLBACK_ACTIVATED.name());
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Fallback loadset " + lSystemLoad.getLoadSetName() + " has been activated");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			    sendTOSOperationMailToADL(lLoad, result.getLoadset());
			    retVal = 6;
			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}
		    }
			break;
		    case "LOADDEA": {
			lwssMessage.setMessage(lSystemLoad.getLoadSetName() + " Deactivate was successful");
			wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);

			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
			    // Main Process - Updating Time
			    lLoad.setDeActivatedDateTime(new Date());
			    productionLoadsDAO.update(result.getUser(), lLoad);

			    // Updating System Loads
			    if (lLoad.getCpuId() == null) {
				productionLoadsDAO.updateAllDeActivated(result.getUser(), lLoad);
				lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.DEACTIVATED_ON_ALL_CPU.name());
			    } else {
				// TODO: Need to Change the conditions
				if ("C".equals(lSystemLoad.getLoadCategoryId().getName()) || "F".equals(lSystemLoad.getLoadCategoryId().getName())) {
				    setProdLoadStatus(result.getUser(), lLoad, lSystemLoad);
				}
			    }
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Loadset " + lSystemLoad.getLoadSetName() + " has been deactivated");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			    sendTOSOperationMailToADL(lLoad, result.getLoadset());
			    retVal = 7;
			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_DEACTIVATED.name())) {
			    // Main Process - Updating Time
			    lLoad.setFallbackDeActivatedDateTime(new Date());
			    productionLoadsDAO.update(result.getUser(), lLoad);

			    // Updating System loads
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.FALLBACK_DEACTIVATED.name());
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Fallback loadset " + lSystemLoad.getLoadSetName() + " has been deactivated");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			    retVal = 6;
			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}
		    }
			break;
		    case "LOADDEL": {
			lwssMessage.setMessage(lSystemLoad.getLoadSetName() + " Delete was successful");
			wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);

			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name())) {

			    // Main Process - Updating System Loads
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.DELETED.name());
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Loadset " + lSystemLoad.getLoadSetName() + " has been deleted");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);

			    // Check All Delete
			    retVal = 1;
			    sendTOSOperationMailToADL(lLoad, result.getLoadset());

			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name())) {
			    // Main Process - Upadating System Load
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.FALLBACK_DELETED.name());
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/postProdSystemLoad" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lSystemLoad.getSystemId().getName() + "-" + lLoad.getStatus());
			    }
			    systemLoadDAO.update(result.getUser(), lSystemLoad);
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Fallback loadset " + lSystemLoad.getLoadSetName() + " has been deleted");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);

			    // Check All Fallback Delete
			    retVal = 2;
			    sendTOSOperationMailToADL(lLoad, result.getLoadset());

			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}

			// Mark All records related with system are "FALLBACK_DELETED/DELETED"
			List<ProductionLoads> lProdLoadsList = getProductionLoadsDAO().findALLByPlanId(lSystemLoad.getPlanId(), lSystemLoad.getSystemId());
			for (ProductionLoads lProdLoads : lProdLoadsList) {
			    lProdLoads.setStatus(lLoad.getStatus());
			    lProdLoads.setLastActionStatus("SUCCESS");
			    getProductionLoadsDAO().update(result.getUser(), lProdLoads);
			}

		    }
			break;
		    case "LOADACC": {
			lwssMessage.setMessage(lSystemLoad.getLoadSetName() + " Accept was successful");
			wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);

			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
			    // Main Process
			    if (lLoad.getCpuId() == null) {
				productionLoadsDAO.updateAllAccepted(result.getUser(), lLoad);
			    }
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACCEPTED.name());
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Loadset " + lSystemLoad.getSystemId().getName() + " has been accepted");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			    retVal = 3;
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 https://workflowApi/tsd/acceptFallback" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + Constants.LOAD_SET_STATUS.ACCEPTED.name())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + Constants.LOAD_SET_STATUS.ACCEPTED.name());
			    }
			    // sendTOSOperationMailToADL(lLoad, result.getLoadset());

			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
			    // Main Process - Updating System Load
			    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.FALLBACK_ACCEPTED.name());
			    lwssMessage.setMessage(lLoad.getPlanId().getId() + ": Fallback loadset " + lSystemLoad.getSystemId().getName() + " has been accepted");
			    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lwssMessage);
			    retVal = 4;
			    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/acceptFallback " + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name());
			    }
			    // sendTOSOperationMailToADL(lLoad, result.getLoadset());

			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}
		    }
		    }
		} else {
		    // FAILED CASE
		    String lCurrentTOSRequest = lLoad.getStatus();
		    lResponse.setStatus(false);
		    lLoad.setLastActionStatus("FAILED");
		    lResponse.setErrorMessage(result.getMessage());
		    lLoad.setStatus(result.getOldStatus());

		    if (lCurrentTOSRequest.equals(Constants.LOAD_SET_STATUS.ACCEPTED.name())) {
			if (cacheClient.getProdTOSAcceptPlansMap().containsKey(lLoad.getPlanId().getId())) {
			    TosActionQueue lTosAction = cacheClient.getProdTOSAcceptPlansMap().get(lLoad.getPlanId().getId());
			    lTosAction.setQueueStatus(lLoad.getLastActionStatus());
			    lTosAction.setTosResponseMessage(result.getMessage());
			    cacheClient.getProdTOSAcceptPlansMap().put(lLoad.getPlanId().getId(), lTosAction);
			}
			// 2502 getPlanHelper().resetOnlineInProgressFlag(result.getUser(),
			// lLoad.getPlanId().getId());
		    }

		}
		// SUCCESS OR FAIL
		// ZTPFM--2275 Need to check this logic in Co-Exist
		TOSActivityMessage lActivityMessage = new TOSActivityMessage(lLoad.getPlanId(), null, lLoad);
		lActivityMessage.settOSResult(result);
		activityLogDAO.save(result.getUser(), lActivityMessage);
	    } else if (result.getReturnValue() == 0) {
		// Dead Code
		lLoad.setLastActionStatus("SUCCESS");
		lResponse.setStatus(true);
	    }
	    // SUCCESS OR FAIL OR IN PROGRESS
	    productionLoadsDAO.update(result.getUser(), lLoad);

	    TosActionQueue lTosActionQueu = cacheClient.getProdTOSLoadActPlansMap().get(lLoad.getPlanId().getId() + "_" + lLoad.getSystemId().getName());
	    if ((lTosActionQueu != null) && (lTosActionQueu.getOldStatus().equalsIgnoreCase(result.getOldStatus()))) {
		lTosActionQueu.setQueueStatus(lLoad.getLastActionStatus());
		lTosActionQueu.setTosResponseMessage(result.getMessage());
		cacheClient.getProdTOSLoadActPlansMap().put(lLoad.getPlanId().getId() + "_" + lLoad.getSystemId().getName(), lTosActionQueu);

	    }

	    LOG.info("UPDATE  -> ID: " + lLoad.getId() + " Status: " + lLoad.getStatus() + " Action: " + lLoad.getLastActionStatus());
	    lResponse.setData(result);
	    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lResponse);
	} catch (WorkflowException ex) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage(ex.getMessage());
	    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lResponse);
	    LOG.error(ex);
	    retVal = -1;
	} catch (Exception ex) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in Receiving Message");
	    wsserver.sendMessage(Constants.Channels.PROD_LOAD, result.getUser(), lResponse);
	    LOG.error("Error in Receiving Message", ex);
	    retVal = -1;
	}
	return retVal;
    }

    @Transactional
    public void checkAllDeleted(User pUser, Integer lLoadId) {
	ProductionLoads lLoad = productionLoadsDAO.find(lLoadId);
	List<ProductionLoads> lProdLoads = productionLoadsDAO.findByPlanId(lLoad.getPlanId().getId());
	Boolean lReturnBackToLC = lProdLoads.stream().filter(t -> t.getActivatedDateTime() != null).findAny().isPresent();
	if (!lReturnBackToLC) {
	    Boolean isALLDeleted = lProdLoads.stream().filter(t -> !t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()) || (t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()) && t.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))).findAny().isPresent();
	    if (!isALLDeleted) {
		LOG.info("All the production loads are deleted");
		ImpPlan lPlan = impPlanDAO.find(lLoad.getPlanId().getId());
		lPlan.setPlanStatus(Constants.PlanStatus.DEV_MGR_APPROVED.name());
		impPlanDAO.update(pUser, lPlan);

		lProdLoads.stream().forEach((t) -> {
		    productionLoadsDAO.delete(pUser, t);
		});

		PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lPlan, null);
		planStatusActivityMessage.setStatus(Constants.PlanStatus.DEV_MGR_APPROVED.getDisplayName());
		getActivityLogDAO().save(pUser, planStatusActivityMessage);
	    } else {
		lProdLoads.stream().filter(t -> t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) && !t.getLastActionStatus().equalsIgnoreCase("INPROGRESS")).forEach((lProdload) -> {
		    LOG.info("Plan id - " + lProdload.getPlanId().getId() + " Deleting for system " + lProdload.getSystemId().getId());
		    lProdload.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
		    tsdService.postActivationAction(pUser, lProdload, false, StringUtils.EMPTY);
		});
	    }

	} else {
	    List<ProductionLoads> prodLoads = productionLoadsDAO.findAllLastSuccessfulBuild(lLoad.getPlanId().getId());
	    // If All Loads Deleted do plan Fallback
	    boolean allLoadsetsDeleted = Boolean.TRUE;
	    ArrayList<String> loadsettobeDeleted = new ArrayList<>();
	    Boolean lSendTosMail = Boolean.TRUE;
	    List<ProductionLoads> planToBeDeleted = new ArrayList<>();
	    for (ProductionLoads prodLoad : prodLoads) {
		if (!prodLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()) || (prodLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DELETED.name()) && prodLoad.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
		    allLoadsetsDeleted = Boolean.FALSE;
		    lSendTosMail = Boolean.FALSE;
		    loadsettobeDeleted.add(prodLoad.getSystemLoadId().getLoadSetName());
		    if (!lLoadId.equals(prodLoad.getId())) {
			if (prodLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.LOADED.name()) && prodLoad.getLastActionStatus().equalsIgnoreCase("SUCCESS")) {
			    LOG.info("Plan id - " + prodLoad.getPlanId().getId() + " Deleting for system " + prodLoad.getSystemId().getId());
			    prodLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
			    tsdService.postActivationAction(pUser, prodLoad, false, StringUtils.EMPTY);
			}
		    }
		}
	    }

	    if (!loadsettobeDeleted.isEmpty()) {
		JSONResponse lDResponse = new JSONResponse();
		lDResponse.setStatus(false);
		lDResponse.setErrorMessage("Action Required! Please delete other loaded/activated loadsets " + String.join(",", loadsettobeDeleted) + " within this implementation plan " + lLoad.getPlanId().getId());
		wsserver.sendMessage(Constants.Channels.PROD_LOAD, pUser, lDResponse);
	    }

	    if (allLoadsetsDeleted) {
		fallbackHelper.fallBackStatusUpdate(pUser, lLoad.getPlanId().getId(), Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET, null, null);
	    }
	}
    }

    @Transactional
    public void checkFallbackDeleted(User pUser, Integer lLoadId) {
	ProductionLoads lLoad = productionLoadsDAO.find(lLoadId);
	// If All Loads Deleted do plan Fallback
	boolean allLoadsetsDeleted = Boolean.TRUE;
	Boolean lSendTosMail = Boolean.TRUE;
	ArrayList<String> loadsettobeDeleted = new ArrayList<>();
	List<ProductionLoads> buildList = productionLoadsDAO.findAllLastSuccessfulBuild(lLoad.getPlanId().getId());
	for (ProductionLoads build : buildList) {
	    if (!build.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name()) || (build.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name()) && build.getLastActionStatus().equalsIgnoreCase("INPROGRESS"))) {
		allLoadsetsDeleted = Boolean.FALSE;
		lSendTosMail = Boolean.FALSE;
		loadsettobeDeleted.add(build.getSystemLoadId().getLoadSetName());
		break;
	    }
	}
	if (!loadsettobeDeleted.isEmpty()) {
	    JSONResponse lDResponse = new JSONResponse();
	    lDResponse.setStatus(false);
	    lDResponse.setErrorMessage("Action Required! Please delete other loaded/activated fallback loadsets " + String.join(",", loadsettobeDeleted) + " within this implementation plan " + lLoad.getPlanId().getId());
	    wsserver.sendMessage(Constants.Channels.PROD_LOAD, pUser, lDResponse);
	}
	if (allLoadsetsDeleted) {
	    ImpPlan planId = lLoad.getPlanId();
	    planId.setPlanStatus(Constants.PlanStatus.ONLINE_RELOAD.name());
	    impPlanDAO.update(pUser, planId);
	    // fallbackHelper.fallBackStatusUpdate(result.getUser(),
	    // lLoad.getPlanId().getId(),
	    // Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET, null);
	}
    }

    @Transactional
    public void checkAllAccepted(User pUser, Integer lLoadId, String loadset) {
	ProductionLoads lLoad = productionLoadsDAO.find(lLoadId);
	// IF ALL Accepted Make Plan ONLINE
	Long lCount = productionLoadsDAO.getAllAccepted(lLoad.getPlanId().getId());
	Long countByImpPlan = systemLoadDAO.countByImpPlan(lLoad.getPlanId());
	if (lCount.equals(countByImpPlan)) {
	    ImpPlan onlineImpPlan = getImpPlanDAO().find(lLoad.getPlanId().getId());
	    onlineImpPlan.setPlanStatus(Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name()); // 2502
	    onlineImpPlan.setTpfAcceptedDateTime(new Date()); // 2502
	    onlineImpPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name()); // 2502
	    getImpPlanDAO().update(pUser, onlineImpPlan); // 2502

	    // 2502 fallbackHelper.onlineJenkinsJob(pUser, onlineImpPlan);
	    TosActionQueue lAcceptQueue = new TosActionQueue();
	    lAcceptQueue.setAction("TOPROCESS");
	    lAcceptQueue.setUser(pUser);
	    cacheClient.getOnlineAcceptPlanMap().put(lLoad.getPlanId().getId(), lAcceptQueue);

	    sendTOSOperationMailToADL(lLoad, loadset);
	    if (cacheClient.getProdTOSAcceptPlansMap().containsKey(lLoad.getPlanId().getId())) {
		TosActionQueue lTosActionQueue = cacheClient.getProdTOSAcceptPlansMap().get(lLoad.getPlanId().getId());
		lTosActionQueue.setQueueStatus(lLoad.getLastActionStatus());
		lTosActionQueue.setTosResponseMessage("Accept commands are successfully processed");
		cacheClient.getProdTOSAcceptPlansMap().put(lLoad.getPlanId().getId(), lTosActionQueue);
	    }
	} else {
	    LOG.info(lLoad.getPlanId().getId() + " " + lLoad.getSystemId().getName() + " Accept Completed, Someother System InProgress");
	}
    }

    @Transactional
    public void checkAllFallbackAccepted(User pUser, Integer lLoadId, String loadset, String rejectReason) {
	ProductionLoads lLoad = productionLoadsDAO.find(lLoadId);
	Long lCount = productionLoadsDAO.getAllFallBackAccepted(lLoad.getPlanId().getId());
	List<SystemLoad> lSysLoads = systemLoadDAO.findByImpPlan(lLoad.getPlanId().getId());

	if (lCount.equals(Long.valueOf(lSysLoads.size()))) {
	    String lOldStatus = lLoad.getPlanId().getPlanStatus();

	    fallbackHelper.fallBackStatusUpdate(pUser, lLoad.getPlanId().getId(), Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET, rejectReason, lLoad);

	    getPRStatusUpdateinNAS().writeFileInNAS(lLoad.getPlanId());

	    StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
	    statusChangeToDependentPlanMail.addToAddressUserId(lLoad.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
	    statusChangeToDependentPlanMail.setImpPlanId(lLoad.getPlanId().getId());
	    statusChangeToDependentPlanMail.setOldStatus(lOldStatus);
	    statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.FALLBACK.name());
	    statusChangeToDependentPlanMail.setReason(rejectReason);
	    mailMessageFactory.push(statusChangeToDependentPlanMail);
	    sendTOSOperationMailToADL(lLoad, loadset);
	} else {
	    LOG.info(lLoad.getPlanId().getId() + " " + lLoad.getSystemId().getName() + " Fallback Accept Completed, Someother System InProgress");
	}
    }

    private void setProdLoadStatus(User pUser, ProductionLoads lLoad, SystemLoad lSystemLoad) {
	Integer lActivatedCount = 0, lDeactivatedCount = 0;
	Integer lAllActivatedCount = 0, lAllDeactivatedCount = 0;

	List<Object[]> lProdLoadsCount = productionLoadsDAO.getLoadsCountByLoadStatus(lLoad.getPlanId().getId(), lLoad.getSystemId().getId());
	List<Object[]> lAllProdLoadsCount = productionLoadsDAO.getAllLoadsCountByLoadStatus(lLoad.getPlanId().getId(), lLoad.getSystemId().getId());

	for (Object[] lProdLoad : lProdLoadsCount) {
	    if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
		lActivatedCount = Integer.valueOf(lProdLoad[1].toString());
	    } else if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
		lDeactivatedCount = Integer.valueOf(lProdLoad[1].toString());
	    }
	}
	for (Object[] lProdLoad : lAllProdLoadsCount) {
	    if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
		lAllActivatedCount = Integer.valueOf(lProdLoad[1].toString());
	    } else if (lProdLoad[0].toString().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
		lAllDeactivatedCount = Integer.valueOf(lProdLoad[1].toString());
	    }
	}
	List<SystemCpu> lSystemCpuList = new ArrayList();
	if (getTOSConfig().getTosSystemType().equals(Constants.TOSEnvironment.PRODUCTION.name())) {
	    lSystemCpuList = systemCpuDAO.findCpusOtherthan(lSystemLoad.getSystemId(), new ArrayList(), Constants.TOSEnvironment.PRODUCTION.name(), false);
	} else {
	    lSystemCpuList = systemCpuDAO.findCpusOtherthan(lSystemLoad.getSystemId(), new ArrayList(), Constants.TOSEnvironment.NATIVE_CPU.name(), false);
	}

	LOG.info("DEBUG - activated count - " + lActivatedCount + " deactivated Count - " + lDeactivatedCount + " Activated in ALL count - " + lAllActivatedCount + " Deactivated ALL count - " + lAllDeactivatedCount);
	if (lAllActivatedCount == 0 && lAllDeactivatedCount == 0 && lDeactivatedCount == 0 && lActivatedCount == 0) {
	    // lSystemLoad.setProdLoadStatus("");
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 0 && lDeactivatedCount == 0 && lActivatedCount == 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_SINGLE_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 0 && lDeactivatedCount == 0 && lActivatedCount > 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	    if (lSystemCpuList.size() == lActivatedCount) {
		ProductionLoads lLoad1 = new ProductionLoads();
		try {
		    BeanUtils.copyProperties(lLoad1, lLoad);
		    lLoad1.setId(null);
		    lLoad1.setCpuId(null);
		    productionLoadsDAO.save(pUser, lLoad1);
		    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name());
		} catch (Exception ex) {
		    LOG.error("Error in Saving for ALL Loads", ex);
		}
	    }
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 0 && lDeactivatedCount > 0 && lActivatedCount == 0) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.DEACTIVATED_ON_ALL_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 0 && lDeactivatedCount > 0 && lActivatedCount == 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_SINGLE_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 0 && lDeactivatedCount > 0 && lActivatedCount > 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	} else if (lAllActivatedCount == 1 && lAllDeactivatedCount == 0 && lDeactivatedCount == 0 && lActivatedCount == 0) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name());
	} else if (lAllActivatedCount == 1 && lAllDeactivatedCount == 0 && lDeactivatedCount == 0 && lActivatedCount == 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name());
	} else if (lAllActivatedCount == 1 && lAllDeactivatedCount == 0 && lDeactivatedCount == 0 && lActivatedCount > 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name());
	} else if (lAllActivatedCount == 1 && lAllDeactivatedCount == 0 && lDeactivatedCount > 0 && lActivatedCount == 0) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	} else if (lAllActivatedCount == 1 && lAllDeactivatedCount == 0 && lDeactivatedCount > 0 && lActivatedCount == 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	} else if (lAllActivatedCount == 1 && lAllDeactivatedCount == 0 && lDeactivatedCount > 0 && lActivatedCount > 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 1 && lDeactivatedCount == 0 && lActivatedCount == 0) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.DEACTIVATED_ON_ALL_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 1 && lDeactivatedCount == 0 && lActivatedCount == 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_SINGLE_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 1 && lDeactivatedCount == 0 && lActivatedCount > 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 1 && lDeactivatedCount > 0 && lActivatedCount == 0) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.DEACTIVATED_ON_ALL_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 1 && lDeactivatedCount > 0 && lActivatedCount == 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_SINGLE_CPU.name());
	} else if (lAllActivatedCount == 0 && lAllDeactivatedCount == 1 && lDeactivatedCount > 0 && lActivatedCount > 1) {
	    lSystemLoad.setProdLoadStatus(Constants.PROD_LOAD_STATUS.ACTIVATED_ON_MULTIPLE_CPU.name());
	}
	LOG.info("DEBUG - Production Load status : " + lSystemLoad.getProdLoadStatus());
    }

    private Boolean sendTOSOperationMailToADL(ProductionLoads pLoad, String pLoadsetName) {
	// Send Mail Notification to ADL
	try {
	    ProdLoadsetActivationMail lProdLoadsetActivationMail = (ProdLoadsetActivationMail) mailMessageFactory.getTemplate(ProdLoadsetActivationMail.class);
	    lProdLoadsetActivationMail.setPlanId(pLoad.getPlanId().getId());
	    lProdLoadsetActivationMail.setPlanDesc(pLoad.getPlanId().getPlanDesc());
	    lProdLoadsetActivationMail.addToAddressUserId(pLoad.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
	    lProdLoadsetActivationMail.addToAddressUserId(pLoad.getSystemLoadId().getLoadAttendeeId(), Constants.MailSenderRole.LOADS_ATTENDEE);
	    // ZTPFM-1740
	    lProdLoadsetActivationMail.addToAddressUserId(pLoad.getPlanId().getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
	    lProdLoadsetActivationMail.addToAddressUserId(pLoad.getPlanId().getImplementationList().stream().map(Implementation::getDevId).distinct().collect(Collectors.joining(",")), Constants.MailSenderRole.DEVELOPER);
	    // ZTPFM-1824 && 1826
	    if (wFConfig.getIsDeltaApp() && wFConfig.getDeltaFallbackOnloneMailId() != null && !wFConfig.getDeltaFallbackOnloneMailId().isEmpty() && (pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name()) || pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name()))) {
		lProdLoadsetActivationMail.addCcDeltaFallbackCentre();
	    }
	    lProdLoadsetActivationMail.setActivationDateTime(new Date());
	    lProdLoadsetActivationMail.setLoadsetName(pLoadsetName);
	    lProdLoadsetActivationMail.setProdSystemName(pLoad.getSystemId().getName());
	    lProdLoadsetActivationMail.setStatus(pLoad.getStatus());
	    if (pLoad.getCpuId() != null) {
		lProdLoadsetActivationMail.setCpuName(pLoad.getCpuId().getDisplayName());
	    }
	    if (pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACCEPTED.name()) || pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
		List<String> lLoadsetName = new ArrayList();
		List<String> lSystemName = new ArrayList();
		List<SystemLoad> lSystemLoads = systemLoadDAO.findByImpPlan(pLoad.getPlanId().getId());
		for (SystemLoad lSystemLoad : lSystemLoads) {

		    if (pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name())) {
			lLoadsetName.add(lSystemLoad.getFallbackLoadSetName());
		    } else {
			lLoadsetName.add(lSystemLoad.getLoadSetName());
		    }
		    lSystemName.add(lSystemLoad.getSystemId().getName());
		}
		lProdLoadsetActivationMail.setLoadsetName(String.join(",", lLoadsetName));
		lProdLoadsetActivationMail.setProdSystemName(String.join(",", lSystemName));
	    }

	    mailMessageFactory.push(lProdLoadsetActivationMail);
	    // ZTPFM-1765
	    if (pLoad.getPlanId().getStackHolderEmail() != null && !pLoad.getPlanId().getStackHolderEmail().isEmpty() && ((pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) || (pLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())))) {
		StackHolderLoadsetMail stackHolderLoadsetMail = (StackHolderLoadsetMail) mailMessageFactory.getTemplate(StackHolderLoadsetMail.class);
		stackHolderLoadsetMail.setPlanId(pLoad.getPlanId().getId());
		stackHolderLoadsetMail.setActivationDateTime(new Date());
		stackHolderLoadsetMail.setProdSystemName(pLoad.getSystemId().getName());
		stackHolderLoadsetMail.setStatus(pLoad.getStatus());
		if (pLoad.getCpuId() != null) {
		    stackHolderLoadsetMail.setCpuName(pLoad.getCpuId().getDisplayName());
		}

		List<String> lStackHolders = Arrays.asList(pLoad.getPlanId().getStackHolderEmail().split(","));
		for (String lStackHolder : lStackHolders) {
		    stackHolderLoadsetMail.addToAddressEmailId(lStackHolder, "Stack Holder");
		}
		stackHolderLoadsetMail.addcCAddressUserId(pLoad.getPlanId().getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		stackHolderLoadsetMail.addcCAddressUserId(pLoad.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
		stackHolderLoadsetMail.addcCAddressUserId(pLoad.getPlanId().getImplementationList().stream().map(Implementation::getDevId).distinct().collect(Collectors.joining(",")), Constants.MailSenderRole.DEVELOPER);
		stackHolderLoadsetMail.addcCAddressUserId(pLoad.getPlanId().getImplementationList().stream().map(Implementation::getPeerReviewers).distinct().collect(Collectors.joining(",")), Constants.MailSenderRole.PEER_REVIEWER);
		stackHolderLoadsetMail.setPlanDescription(pLoad.getPlanId().getPlanDesc());
		Set<String> lProblemTicket = new HashSet<>();
		if (pLoad.getPlanId().getSdmTktNum() != null && !pLoad.getPlanId().getSdmTktNum().equals("")) {
		    lProblemTicket.add(pLoad.getPlanId().getSdmTktNum());
		}
		String implProblemTicketNumber = pLoad.getPlanId().getImplementationList().stream().map(Implementation::getPrTktNum).distinct().collect(Collectors.joining(","));
		if (implProblemTicketNumber != null && !implProblemTicketNumber.equals("")) {
		    lProblemTicket.add(implProblemTicketNumber);
		}
		stackHolderLoadsetMail.setProblemTicketSet(lProblemTicket);
		stackHolderLoadsetMail.setTargetSystems(pLoad.getPlanId().getSystemLoadList().stream().filter(t -> t.getActive().equalsIgnoreCase("Y")).map(v -> v.getSystemId()).map(System::getName).collect(Collectors.joining(",")));
		stackHolderLoadsetMail.setDbcr(pLoad.getPlanId().getDbcrList().stream().map(Dbcr::getDbcrName).distinct().collect(Collectors.joining(",")));
		mailMessageFactory.push(stackHolderLoadsetMail);
	    }
	} catch (Exception Ex) {
	    LOG.error("Unable to send mail during TOS operation ", Ex);
	    return false;
	}
	return true;
    }

    void checkAllActivated(User user, Integer id) {
	ProductionLoads lLoad = productionLoadsDAO.find(id);
	Long lCount = productionLoadsDAO.getAllActivated(lLoad.getPlanId().getId());
	Long countByImpPlan = systemLoadDAO.countByImpPlan(lLoad.getPlanId());
	ImpPlan planId = lLoad.getPlanId();
	String oldPlanStatus = planId.getPlanStatus();

	List<ProductionLoads> lLoads = productionLoadsDAO.findByPlanId(lLoad.getPlanId().getId());
	Boolean isDeactivatedInAnySys = lLoads.stream().filter(t -> t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).findAny().isPresent();
	if (isDeactivatedInAnySys) {
	    lCount = 0L;
	}

	if (lCount.equals(countByImpPlan)) {
	    planId.setPlanStatus(Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name());
	} else {
	    planId.setPlanStatus(Constants.PlanStatus.PARTIALLY_DEPLOYED_IN_PRODUCTION.name());
	}
	impPlanDAO.update(user, planId);

	if (!oldPlanStatus.equals(planId.getPlanStatus())) {
	    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(planId, null);
	    planStatusActivityMessage.setStatus(planId.getPlanStatus());
	    activityLogDAO.save(user, planStatusActivityMessage);
	}

    }

    void checkAllFallbackActivated(User user, Integer id) {
	ProductionLoads lLoad = productionLoadsDAO.find(id);
	Long lCount = productionLoadsDAO.getAllFallBackActivated(lLoad.getPlanId().getId());
	Long countByImpPlan = systemLoadDAO.countByImpPlan(lLoad.getPlanId());
	if (lCount.equals(countByImpPlan)) {
	    ImpPlan planId = lLoad.getPlanId();
	    planId.setPlanStatus(Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name());
	    impPlanDAO.update(user, planId);
	} else {
	    ImpPlan planId = lLoad.getPlanId();
	    if (planId.getPlanStatus().equals(Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name())) {
		planId.setPlanStatus(Constants.PlanStatus.ONLINE.name());
		impPlanDAO.update(user, planId);
	    }
	}
    }

    private void sendNotificationToDependentPlan(String impPlanId) {
	ImpPlan lImpPlan = getImpPlanDAO().find(impPlanId);
	// 2107
	List<String> lDevAndADLlist = new ArrayList<>();
	List<Implementation> lImplementationList = getImplementationDAO().findByImpPlan(lImpPlan);
	lDevAndADLlist.add(lImpPlan.getLeadId());
	lImplementationList.forEach((lImplementation) -> {
	    lDevAndADLlist.add(lImplementation.getDevId());
	});

	Set<String> lChildPlans = new HashSet();

	// Mail Notification send ADL,Developer and Dev Manager
	TSDDependentPlanMail tsdDependentPlanMail = (TSDDependentPlanMail) getMailMessageFactory().getTemplate(TSDDependentPlanMail.class);
	tsdDependentPlanMail.setDependentPlan(lChildPlans);
	tsdDependentPlanMail.setLoadedPlan(lImpPlan.getId());
	lDevAndADLlist.stream().forEach(t -> tsdDependentPlanMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
	tsdDependentPlanMail.addcCAddressUserId(lImpPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
	getMailMessageFactory().push(tsdDependentPlanMail);
    }

    void checkAllDeActivated(User user, Integer id) {
	ProductionLoads lLoad = productionLoadsDAO.find(id);
	List<ProductionLoads> lLoads = productionLoadsDAO.findByPlanId(lLoad.getPlanId().getId());
	Boolean isAnyLoadsetActivated = lLoads.stream().filter(t -> t.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name())).findAny().isPresent();
	ImpPlan planId = lLoad.getPlanId();
	String oldPlanStatus = planId.getPlanStatus();

	if (!isAnyLoadsetActivated) {
	    planId.setPlanStatus(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
	} else {
	    planId.setPlanStatus(Constants.PlanStatus.PARTIALLY_DEPLOYED_IN_PRODUCTION.name());
	}
	impPlanDAO.update(user, planId);

	if (!oldPlanStatus.equals(planId.getPlanStatus())) {
	    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(planId, null);
	    planStatusActivityMessage.setStatus(planId.getPlanStatus());
	    activityLogDAO.save(user, planStatusActivityMessage);
	}
    }

}
