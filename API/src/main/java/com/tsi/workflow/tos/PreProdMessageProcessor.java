/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.PreTOSActivityMessage;
import com.tsi.workflow.activity.ProdFTPActivityMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.PreProdLoadsetActivationMail;
import com.tsi.workflow.schedular.jenkins.DEVLBuildMonitor;
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
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author prabhu.prabhakaran
 */
@Component
public class PreProdMessageProcessor {

    private static final Logger LOG = Logger.getLogger(PreProdMessageProcessor.class.getName());

    // <editor-fold defaultstate="collapsed" desc="variables">
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    FallbackHelper fallbackHelper;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    TOSHelper tOSHelper;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    TOSConfig tosConfig;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    SystemCpuDAO systemCpuDAO;
    @Autowired
    BPMClientUtils bPMClientUtils;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    PRNumberHelper prStatusUpdateinNAS;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    FTPHelper fTPHelper;
    @Autowired
    ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
    }

    public void setPreProductionLoadsDAO(PreProductionLoadsDAO preProductionLoadsDAO) {
	this.preProductionLoadsDAO = preProductionLoadsDAO;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public void setFallbackHelper(FallbackHelper fallbackHelper) {
	this.fallbackHelper = fallbackHelper;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public void setRejectHelper(RejectHelper rejectHelper) {
	this.rejectHelper = rejectHelper;
    }

    public TOSHelper gettOSHelper() {
	return tOSHelper;
    }

    public void settOSHelper(TOSHelper tOSHelper) {
	this.tOSHelper = tOSHelper;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public void setImpPlanDAO(ImpPlanDAO impPlanDAO) {
	this.impPlanDAO = impPlanDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public void setBuildDAO(BuildDAO buildDAO) {
	this.buildDAO = buildDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public void setActivityLogDAO(ActivityLogDAO activityLogDAO) {
	this.activityLogDAO = activityLogDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public void setSystemLoadDAO(SystemLoadDAO systemLoadDAO) {
	this.systemLoadDAO = systemLoadDAO;
    }

    public GITConfig getgITConfig() {
	return gITConfig;
    }

    public void setgITConfig(GITConfig gITConfig) {
	this.gITConfig = gITConfig;
    }

    public TOSConfig getTosConfig() {
	return tosConfig;
    }

    public void setTosConfig(TOSConfig tosConfig) {
	this.tosConfig = tosConfig;
    }

    public JGitClientUtils getlGitUtils() {
	return lGitUtils;
    }

    public void setlGitUtils(JGitClientUtils lGitUtils) {
	this.lGitUtils = lGitUtils;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public void setMailMessageFactory(MailMessageFactory mailMessageFactory) {
	this.mailMessageFactory = mailMessageFactory;
    }

    public SystemCpuDAO getSystemCpuDAO() {
	return systemCpuDAO;
    }

    public void setSystemCpuDAO(SystemCpuDAO systemCpuDAO) {
	this.systemCpuDAO = systemCpuDAO;
    }

    public BPMClientUtils getbPMClientUtils() {
	return bPMClientUtils;
    }

    public void setbPMClientUtils(BPMClientUtils bPMClientUtils) {
	this.bPMClientUtils = bPMClientUtils;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public void setsSHClientUtils(SSHClientUtils sSHClientUtils) {
	this.sSHClientUtils = sSHClientUtils;
    }

    public PRNumberHelper getPrStatusUpdateinNAS() {
	return prStatusUpdateinNAS;
    }

    public void setPrStatusUpdateinNAS(PRNumberHelper prStatusUpdateinNAS) {
	this.prStatusUpdateinNAS = prStatusUpdateinNAS;
    }

    public WSMessagePublisher getWsserver() {
	return wsserver;
    }

    public void setWsserver(WSMessagePublisher wsserver) {
	this.wsserver = wsserver;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public void setImplementationDAO(ImplementationDAO implementationDAO) {
	this.implementationDAO = implementationDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public void setSystemLoadActionsDAO(SystemLoadActionsDAO systemLoadActionsDAO) {
	this.systemLoadActionsDAO = systemLoadActionsDAO;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public void setPlanHelper(PlanHelper planHelper) {
	this.planHelper = planHelper;
    }

    public FTPHelper getFTPHelper() {
	return fTPHelper;
    }
    // </editor-fold>

    @Transactional
    public int processPreProductionTOSMessage(TOSResult result) {
	int retVal = 0;

	ImpPlan impPlan = null;
	try {
	    PreProductionLoads lLoad = preProductionLoadsDAO.find(result.getId());
	    JSONResponse lResponse = new JSONResponse();

	    // Getting Developer Id from Implementation
	    List<String> lDevAndADLlist = new ArrayList<>();
	    impPlan = getImpPlanDAO().find(lLoad.getPlanId().getId());
	    List<Implementation> lImplementationList = getImplementationDAO().findByImpPlan(lLoad.getPlanId().getId());
	    lDevAndADLlist.add(lLoad.getPlanId().getLeadId());
	    for (Implementation lImplementation : lImplementationList) {
		lDevAndADLlist.add(lImplementation.getDevId());
	    }

	    if (result.isLast()) {
		if (result.getReturnValue() == 0) {
		    // SUCCESS
		    lLoad.setLastActionStatus("SUCCESS");
		    lResponse.setStatus(true);
		    preProductionLoadsDAO.update(result.getUser(), lLoad);
		    SystemLoad systemLoadId = lLoad.getSystemLoadId();

		    switch (result.getCommand()) {
		    case "AUTOLOAD": {
			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name())) {
			    // Updating System Load
			    systemLoadId.setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_FALLBACK_LOADED.name());
			    systemLoadDAO.update(result.getUser(), systemLoadId);
			    retVal = 1;
			    // 1965 - below logic moved to TOSMessageReceiver
			    // if (result.getRejectReason() != null &&
			    // result.getRejectReason().equals("CASCADE")) {
			    // // do FALLBACK ACTIVATE
			    // // This works for Reject
			    // }
			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.LOADED.name())) {
			    // Main Task
			    systemLoadId.setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_LOADED.name());
			    systemLoadDAO.update(result.getUser(), systemLoadId);

			    // Delete for CUSTA/CUSTB if Load Failed
			    List<PreProductionLoads> findBySystemLoadId = preProductionLoadsDAO.findBySystemLoadId(systemLoadId);
			    for (PreProductionLoads preProductionLoads : findBySystemLoadId) {
				if (preProductionLoads.getActive().equals("Y") && preProductionLoads.getStatus().equals("LOADED") && (preProductionLoads.getLastActionStatus().equals("FAILED") || preProductionLoads.getLastActionStatus().equals("INPROGRESS")) && preProductionLoads.getId().intValue() != lLoad.getId().intValue() && preProductionLoads.getCpuId().getId().intValue() != lLoad.getCpuId().getId().intValue()) {
				    preProductionLoadsDAO.delete(result.getUser(), preProductionLoads);
				}
			    }

			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}
		    }
			break;
		    case "LOADACT0":
		    case "LOADACT": {
			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
			    // Main Task
			    lLoad.setActivatedDateTime(new Date());
			    lLoad.setDeActivatedDateTime(null);
			    preProductionLoadsDAO.update(result.getUser(), lLoad);

			    systemLoadId.setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_ACTIVATED.name());
			    systemLoadDAO.update(result.getUser(), systemLoadId);

			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())) {

			    // If Fallback activated, delete status needs to be shown in UI.
			    systemLoadId.setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_FALLBACK_ACTIVATED.name());
			    systemLoadDAO.update(result.getUser(), systemLoadId);

			    // If Fallback activated and rejection reason
			    if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {
				lLoad.setActive("N");
				systemLoadId.setPreProdLoadStatus(null);
			    }
			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}

			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name())) {

			    Set<String> qaFunTesters = new HashSet<String>();
			    if (lLoad.getSystemLoadId().getQaFunctionalTesters() != null) {
				List<String> qaFunctionalTesterList = Arrays.asList(lLoad.getSystemLoadId().getQaFunctionalTesters().split(","));
				for (String qaFunctionalTester : qaFunctionalTesterList) {
				    qaFunTesters.add(qaFunctionalTester);
				}
			    }
			    // Send Mail Notification to ADL and Developer
			    PreProdLoadsetActivationMail lPreProdLoadsetActivationMail = (PreProdLoadsetActivationMail) mailMessageFactory.getTemplate(PreProdLoadsetActivationMail.class);
			    lPreProdLoadsetActivationMail.setPlanId(lLoad.getPlanId().getId());
			    lPreProdLoadsetActivationMail.setActivationDateTime(lLoad.getActivatedDateTime());
			    lPreProdLoadsetActivationMail.setLoadsetName(result.getLoadset());
			    lPreProdLoadsetActivationMail.setPreProdSystemName(lLoad.getCpuId().getCpuName());
			    lPreProdLoadsetActivationMail.setAction(lLoad.getStatus());
			    lDevAndADLlist.stream().forEach(t -> lPreProdLoadsetActivationMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
			    if (lLoad.getSystemLoadActionsId() != null && qaFunTesters != null && !qaFunTesters.isEmpty()) {
				qaFunTesters.stream().forEach(t -> lPreProdLoadsetActivationMail.addcCAddressUserId(t, Constants.MailSenderRole.QA_FUNCTIONAL));
			    }
			    mailMessageFactory.push(lPreProdLoadsetActivationMail);
			}
		    }
			break;
		    case "LOADDEA": {
			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {

			    lLoad.setDeActivatedDateTime(new Date());
			    preProductionLoadsDAO.update(result.getUser(), lLoad);

			    systemLoadId.setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_DEACTIVATED.name());
			    systemLoadDAO.update(result.getUser(), systemLoadId);
			    if (result.getMessage() != null && (result.getMessage().contains("LOAD DOES NOT EXIST") || result.getMessage().contains("LOADSET DOES NOT EXIST"))) {
				// do FALLBACK LOAD
				// This works for Reject and Deactivate
				retVal = 2;
			    } else if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {
				// do DELETE
				// This works for Reject
				retVal = 3;
			    }

			    // Send Mail Notification to ADL and Developer
			    PreProdLoadsetActivationMail lPreProdLoadsetActivationMail = (PreProdLoadsetActivationMail) mailMessageFactory.getTemplate(PreProdLoadsetActivationMail.class);
			    lPreProdLoadsetActivationMail.setPlanId(lLoad.getPlanId().getId());
			    lPreProdLoadsetActivationMail.setActivationDateTime(lLoad.getDeActivatedDateTime());
			    lPreProdLoadsetActivationMail.setLoadsetName(result.getLoadset());
			    lPreProdLoadsetActivationMail.setPreProdSystemName(lLoad.getCpuId().getCpuName());
			    lPreProdLoadsetActivationMail.setAction(lLoad.getStatus());
			    lDevAndADLlist.stream().forEach(t -> lPreProdLoadsetActivationMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
			    mailMessageFactory.push(lPreProdLoadsetActivationMail);
			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_DEACTIVATED.name())) {
			    // TODO: Code changes yet to be made
			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}
		    }
			break;
		    case "LOADDEL": {
			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name())) {
			    systemLoadId.setPreProdLoadStatus(Constants.PROD_LOAD_STATUS.PRE_PROD_DELETED.name());
			    systemLoadDAO.update(result.getUser(), systemLoadId);

			    if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {
				lLoad.setActive("N");
				preProductionLoadsDAO.update(result.getUser(), lLoad);
				systemLoadId.setPreProdLoadStatus(null);
			    }
			} else if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name())) {

			} else {
			    LOG.error("For " + result.getLoadset() + " " + lLoad.getStatus() + " is not in proper state for action " + result.getCommand());
			}

			if (lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DELETED.name()) || lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name())) {
			    // Send Mail Notification to ADL and Developer
			    PreProdLoadsetActivationMail lPreProdLoadsetActivationMail = (PreProdLoadsetActivationMail) mailMessageFactory.getTemplate(PreProdLoadsetActivationMail.class);
			    lPreProdLoadsetActivationMail.setPlanId(lLoad.getPlanId().getId());
			    lPreProdLoadsetActivationMail.setActivationDateTime(new Date());
			    lPreProdLoadsetActivationMail.setLoadsetName(result.getLoadset());
			    lPreProdLoadsetActivationMail.setPreProdSystemName(lLoad.getCpuId().getCpuName());
			    lPreProdLoadsetActivationMail.setAction(lLoad.getStatus());
			    lDevAndADLlist.stream().forEach(t -> lPreProdLoadsetActivationMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
			    mailMessageFactory.push(lPreProdLoadsetActivationMail);
			}
		    }
		    }
		} else {
		    // FAIL
		    lResponse.setStatus(false);
		    lLoad.setLastActionStatus("FAILED");
		    // Message from TOS comes with null, temp.. giving below message, need to change
		    // it
		    String lErrorMessage = "TOS opeation - " + lLoad.getStatus() + " failed for plan - " + lLoad.getPlanId().getId();
		    lResponse.setErrorMessage(lErrorMessage);
		    lLoad.setStatus(result.getOldStatus());
		    if (result.getRejectReason() != null && result.getRejectReason().equals("CASCADE")) {
			lLoad.setActive("N");
		    }
		    String QueueKey = lLoad.getPlanId().getId() + "_" + lLoad.getSystemId().getId();
		    if (lPreProdTOSOperationMap.containsKey(QueueKey)) {
			Set<TosActionQueue> lPPQueue = lPreProdTOSOperationMap.get(QueueKey);
			TosActionQueue lPreProdQueue = new TosActionQueue(result.getUser(), result.getOldStatus(), lLoad.getId());
			if (lPPQueue.contains(lPreProdQueue)) {
			    lPPQueue.remove(lPreProdQueue);
			}
			if (lPPQueue.isEmpty()) {
			    lPreProdTOSOperationMap.remove(QueueKey);
			} else {
			    lPreProdTOSOperationMap.replace(QueueKey, lPPQueue);
			}
		    }
		}
		// SUCCESS OR FAIL
		PreTOSActivityMessage lActivityMessage = new PreTOSActivityMessage(lLoad.getPlanId(), null, lLoad);
		lActivityMessage.setLoadActionStatus(lLoad.getStatus());
		lActivityMessage.settOSResult(result);
		activityLogDAO.save(result.getUser(), lActivityMessage);

		// UpdateIfQATSSDeploy(result.getUser(), lLoad);
	    } else if (result.getReturnValue() == 0) {
		// Dead Code
		lLoad.setLastActionStatus("SUCCESS");
		lResponse.setStatus(true);
	    }
	    // SUCCESS OR FAIL OR INPROGRESS

	    preProductionLoadsDAO.update(result.getUser(), lLoad);
	    LOG.info("UPDATE -> PRE PROD ID: " + lLoad.getId() + " Status: " + lLoad.getStatus() + " Action: " + lLoad.getLastActionStatus());
	    lResponse.setData(result);
	    wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, result.getUser(), lResponse);

	    String lStatus = lLoad.getStatus().equals(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name()) ? Constants.LOAD_SET_STATUS.LOADED.name() : lLoad.getStatus();
	    if (lAsyncPlansStartTimeMap.containsKey(lLoad.getPlanId().getId() + "-" + lLoad.getSystemId().getName() + "-" + lStatus)) {
		WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tss/(postPreProdSystemLoad/loadAndActivateInTOS/removeLoadAndActivateInTOS) (" + lLoad.getSystemId().getName() + ")" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lLoad.getPlanId().getId() + "-" + lLoad.getSystemId().getName() + "-" + lStatus)) + " ms, )");
		lAsyncPlansStartTimeMap.remove(lLoad.getPlanId().getId() + "-" + lLoad.getSystemId().getName() + "-" + lStatus);
	    }
	} catch (WorkflowException ex) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage(ex.getMessage());
	    wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, result.getUser(), lResponse);
	    LOG.error(ex);
	    retVal = -1;
	} catch (Exception ex) {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(false);
	    lResponse.setErrorMessage("Error in Receiving Message");
	    wsserver.sendMessage(Constants.Channels.PRE_PROD_LOAD, result.getUser(), lResponse);
	    LOG.error("Error in Receiving Message", ex);
	    retVal = -1;
	} finally {
	    if (retVal == 0 && impPlan != null) {
		impPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
		getImpPlanDAO().update(result.getUser(), impPlan);
	    }
	}
	return retVal;
    }

    // ON Deactivate if Accepted
    @Transactional
    public void doFtpAndFallbackLoad(User pUser, Integer lLoadId, boolean isReject) {
	PreProductionLoads lLoad = preProductionLoadsDAO.find(lLoadId);
	boolean ip = gettOSHelper().requestPreProdIP(pUser, lLoad.getSystemLoadId(), lLoad.getCpuId());
	if (!ip) {
	    LOG.error("Unable to get IP Adress for system " + lLoad.getSystemLoadId().getSystemId().getName());
	    throw new WorkflowException("Unable to request for system " + lLoad.getSystemLoadId().getSystemId().getName());
	}

	String ipAddress = gettOSHelper().getIP(lLoad.getSystemLoadId().getId());

	if (ipAddress.isEmpty()) {
	    LOG.error("Unable to get IP Adress for system " + lLoad.getSystemLoadId().getSystemId().getName());
	    throw new WorkflowException("Unable to get IP Address for system " + lLoad.getSystemLoadId().getSystemId().getName());
	}

	Constants.BUILD_TYPE lBuildType = Constants.BUILD_TYPE.STG_LOAD;
	Build lBuild = buildDAO.findLastSuccessfulBuild(lLoad.getPlanId().getId(), lLoad.getSystemLoadId().getSystemId().getId(), lBuildType);
	if (lBuild == null) {
	    throw new WorkflowException("Staging loadset not created");
	}
	ProdFTPActivityMessage lFTPActivityMessage = new ProdFTPActivityMessage(lLoad.getPlanId(), null, lLoad.getSystemLoadId());
	lFTPActivityMessage.setIpAddress(ipAddress);
	lFTPActivityMessage.setFallback(true);
	getActivityLogDAO().save(pUser, lFTPActivityMessage);
	JSONResponse lSSHResponse = getFTPHelper().doFTP(pUser, lLoad.getSystemLoadId(), lBuild, ipAddress, true);

	if (!lSSHResponse.getStatus()) {
	    lFTPActivityMessage.setStatus("Failed");
	    getActivityLogDAO().save(pUser, lFTPActivityMessage);
	    lLoad.setLastActionStatus("FAILED");
	    // TODO: Since Accept Loadset for pre prod is not available, marking it as
	    // Delete. Need to change when
	    // implement
	    lLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    preProductionLoadsDAO.update(pUser, lLoad);
	} else {
	    lFTPActivityMessage.setStatus("Success");
	    getActivityLogDAO().save(pUser, lFTPActivityMessage);
	    getFTPHelper().getYodaLoadSetPath(pUser, lLoad.getPlanId(), lSSHResponse);

	    /*
	     * ZTPFM-2381 Below Logic Moved to Schedular. If we have more than one target
	     * system then after doing the FTP we are clearing the build and staging space
	     * for others system // Delete staging workspace and build details only if it is
	     * reject if (isReject) { try { LOG.info("Deleting Staging workspace for plan "
	     * + lLoad.getPlanId().getId()); rejectHelper.deleteStagingWorkspace(pUser,
	     * lLoad.getPlanId()); rejectHelper.deleteBuilds(pUser, lLoad.getPlanId()); }
	     * catch (Exception ex) {
	     * LOG.error("Error in Deleting Staging Workspace on Delete", ex); } }
	     */
	    lLoad.setLastActionStatus("INPROGRESS");
	    lLoad.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name());
	    preProductionLoadsDAO.update(pUser, lLoad);

	    String rejectReason = isReject ? "CASCADE" : "";
	    // Sub Process
	    Boolean lFBLoadPreTOSOperation = tOSHelper.doFallbackPreProdTOSOperation(pUser, Constants.LoadSetCommands.LOAD, lLoad, Constants.LOAD_SET_STATUS.DELETED.name(), lLoad.getSystemLoadId(), rejectReason);
	    if (!lFBLoadPreTOSOperation) {
		LOG.error("Error in Fallback loading in  Pre Prod for the plan id " + lLoad.getPlanId().getId() + ", system " + lLoad.getSystemId().getName() + ".");
		lLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
		lLoad.setLastActionStatus("FAILED");
		preProductionLoadsDAO.update(pUser, lLoad);
	    }
	}
    }

    @Transactional
    public void initDelete(User pUser, Integer lLoadId) {
	PreProductionLoads lLoad = preProductionLoadsDAO.find(lLoadId);
	try {
	    LOG.info("Deleting Staging workspace for plan " + lLoad.getPlanId().getId());
	    rejectHelper.deleteStagingWorkspace(pUser, lLoad.getPlanId());
	    rejectHelper.deleteBuilds(pUser, lLoad.getPlanId());
	} catch (Exception ex) {
	    LOG.error("Error in Deleting Staging Workspace on Delete", ex);
	}
	lLoad.setLastActionStatus("INPROGRESS");
	lLoad.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	preProductionLoadsDAO.update(pUser, lLoad);

	Boolean lPreTOSOperation = tOSHelper.doPreTOSOperation(pUser, Constants.LoadSetCommands.DELETE, lLoad, Constants.LOAD_SET_STATUS.DEACTIVATED.name(), lLoad.getSystemLoadId(), true);
	if (!lPreTOSOperation) {
	    LOG.error("Error in Deleting Pre Prod for the plan id" + lLoad.getPlanId().getId() + ", system " + lLoad.getSystemId().getName() + " on reject");
	    lLoad.setLastActionStatus("FAILED");
	    lLoad.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	    preProductionLoadsDAO.update(pUser, lLoad);
	}
    }

    public void initFallBackActivate(User pUser, Integer lLoadId, Boolean isReject) {
	PreProductionLoads lLoad = preProductionLoadsDAO.find(lLoadId);
	lLoad.setLastActionStatus("INPROGRESS");
	lLoad.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name());
	preProductionLoadsDAO.update(pUser, lLoad);
	String rejectReason = isReject ? "CASCADE" : "";
	Boolean lFBLoadPreTOSOperation = tOSHelper.doFallbackPreProdTOSOperation(pUser, Constants.LoadSetCommands.ACTIVATE, lLoad, Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name(), lLoad.getSystemLoadId(), rejectReason);
	if (!lFBLoadPreTOSOperation) {
	    LOG.error("Error in Fallback activate in  Pre Prod for the plan id" + lLoad.getPlanId().getId() + ", system " + lLoad.getSystemId().getName() + ".");
	    lLoad.setLastActionStatus("FAILED");
	    lLoad.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name());
	    preProductionLoadsDAO.update(pUser, lLoad);
	}
    }

    public void initFallBackActivate(User pUser, Integer lLoadId) {
	initFallBackActivate(pUser, lLoadId, Boolean.TRUE);
    }

    @Transactional
    public void updateIfQATSSDeploy(User pUser, Integer lLoadId) {
	PreProductionLoads lLoad = preProductionLoadsDAO.find(lLoadId);
	if (Constants.PlanStatus.getQAPlanStatusBetweenApprovedAndRegDepl().containsKey(lLoad.getPlanId().getPlanStatus()) && lLoad.getSystemLoadActionsId() != null) {
	    getPreProductionLoadsDAO().update(pUser, lLoad);
	    lLoad.getSystemLoadActionsId().setStatus(lLoad.getStatus());
	    lLoad.getSystemLoadActionsId().setLastActionStatus(lLoad.getLastActionStatus());
	    getSystemLoadActionsDAO().update(pUser, lLoad.getSystemLoadActionsId());
	    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), Constants.VPARSEnvironment.valueOf(lLoad.getSystemLoadActionsId().getVparId().getType()), true);
	} else if (Constants.PlanStatus.getReadyForProductionandAboveMap().containsKey(lLoad.getPlanId().getPlanStatus())) {
	} else {
	    getPlanHelper().updatePlanAsDeployedInPreProd(pUser, lLoad.getPlanId().getId(), Constants.VPARSEnvironment.PRE_PROD, false);
	}
    }
}
