/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.RejectPlans;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.PlanChangedOnlineMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class FallbackHelper {

    private static final Logger LOG = Logger.getLogger(FallbackHelper.class.getName());

    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    BPMClientUtils bPMClientUtils;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    MailMessageFactory mailMessageFactory;
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
    @Qualifier("fallBackBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> fallBackBuildJobs;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    @Qualifier("onlineBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> onlineBuildJobs;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    @Qualifier("preProdRejectPlans")
    ConcurrentLinkedQueue<RejectPlans> lRejectPlans;
    @Autowired
    AuditCommonHelper auditCommonHelper;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public BPMClientUtils getBPMClientUtils() {
	return bPMClientUtils;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
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

    public AuditCommonHelper getAuditCommonHelper() {
	return auditCommonHelper;
    }

    public void fallBackStatusUpdate(User currentUser, String planId, Constants.FALLBACK_STATUS type, String rejectReason, ProductionLoads prodLoads) {
	try {
	    ImpPlan fallbackImpPlan = getImpPlanDAO().find(planId);
	    if (type.name().equalsIgnoreCase(Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET.name())) {
		LOG.info("Plan Status " + fallbackImpPlan.getPlanStatus() + "Plan " + fallbackImpPlan.getId() + "Fall Back Status " + Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET.name());
		fallBackJenkinsJob(currentUser, rejectReason, fallbackImpPlan, Constants.PROD_SCRIPT_PARAMS.FALLBACK_ACCEPT.name(), prodLoads);
	    } else if (type.name().equalsIgnoreCase(Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET.name())) {
		LOG.info("Plan Status " + fallbackImpPlan.getPlanStatus() + "Plan " + fallbackImpPlan.getId() + "Fall Back Status " + Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET.name());
		fallBackJenkinsJob(currentUser, rejectReason, fallbackImpPlan, Constants.PROD_SCRIPT_PARAMS.ONLINE_REVERT.name(), prodLoads);
	    }
	    // PP Check
	    List<TaskVariable> lTaskVars = new ArrayList<>();
	    lTaskVars.add(new TaskVariable("planStatus", Constants.PROD_SCRIPT_PARAMS.ONLINE_ACCEPT.name()));
	    getBPMClientUtils().setTaskAsCompletedWithVariables(currentUser, fallbackImpPlan.getProcessId(), lTaskVars);
	    List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(planId);
	    boolean pCategoryCheck = Boolean.FALSE;
	    for (SystemLoad systemLoad : systemLoadList) {
		if (systemLoad.getLoadCategoryId().getName().trim().equalsIgnoreCase(Constants.LOAD_CATEGORY_P)) {
		    pCategoryCheck = Boolean.TRUE;
		    break;
		}
	    }
	    if (!pCategoryCheck) {
		String rejectReasonText = (rejectReason == null || rejectReason.isEmpty()) ? Constants.REJECT_REASON.FALLBACK.getValue() : rejectReason;
		getRejectHelper().rejectDependentPlans(currentUser, planId, rejectReasonText, Constants.AUTOREJECT_COMMENT.FALLBACK.getValue(), false, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE);

		// 2381 -> Remove Loadset of current plan from NON Production Environment
		// Remove loadset from YODA Test Labs
		getRejectHelper().deleteLoadSetActivated(currentUser, fallbackImpPlan, Boolean.TRUE, Boolean.TRUE);
		Map<String, Date> lRejectedPlans = new HashMap();
		// Remove Loadset from TOS System
		lRejectedPlans.put(planId, Collections.min(systemLoadList.stream().map(t -> t.getLoadDateTime()).collect(Collectors.toList())));
		RejectPlans lRejectPlan = new RejectPlans();
		lRejectPlan.setUser(currentUser);
		lRejectPlan.setDependentPlanIds(lRejectedPlans);
		lRejectPlan.setIsReject(Boolean.FALSE);
		lRejectPlan.setIsFallback(Boolean.TRUE);
		lRejectPlans.add(lRejectPlan);
	    }
	} catch (Exception e) {
	    LOG.error("Unable to Change the plan to Fallback", e);
	    throw new WorkflowException("Unable to Change the plan to Fallback", e);
	}
    }

    private void fallBackJenkinsJob(User currentUser, String rejectReason, ImpPlan pPlan, String param, ProductionLoads prodLoads) {
	List<Implementation> lImpList = getImplementationDAO().findByImpPlan(pPlan.getId());
	List<SystemLoad> lSystemLoadList = new ArrayList<>();
	lSystemLoadList = getSystemLoadDAO().findByImpPlan(pPlan.getId());

	for (SystemLoad pSystemLoad : lSystemLoadList) {

	    String lPlanSystemInfo = lImpList.get(0).getId() + "_" + pSystemLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(pSystemLoad.getLoadDateTime());

	    HashMap<String, String> params = new HashMap<>();
	    params.put("IMP_ID_LoadDate", lPlanSystemInfo.toLowerCase());
	    params.put("Feedback_Status", param);

	    JenkinsBuild executeJob = getJenkinsClient().executeJob(currentUser, Constants.ONLINE.FEEDBCK_.name() + pSystemLoad.getSystemId().getName(), params);
	    executeJob.setSystemLoadId("" + pSystemLoad.getId());
	    if (rejectReason != null) {
		executeJob.setReason(rejectReason);
	    }
	    if (!fallBackBuildJobs.contains(executeJob)) {
		onlineFallbackBuildSave(currentUser, pPlan, pSystemLoad, executeJob, Constants.BUILD_TYPE.FAL_BUILD.name());
		fallBackBuildJobs.add(executeJob);
	    }

	}
    }

    public void exceptionOccurMail(String planId, Map<String, Date> lSystemDate) {
	PlanChangedOnlineMail planChangedOnlineMail = (PlanChangedOnlineMail) getMailMessageFactory().getTemplate(PlanChangedOnlineMail.class);
	planChangedOnlineMail.setPlanId(planId);
	planChangedOnlineMail.setLoadDateTargetSys(lSystemDate);
	planChangedOnlineMail.setTosServerId(wFConfig.getTOSServerId());
	if (wFConfig.getDevOpsCentreMailId() != null) {
	    planChangedOnlineMail.addToDevOpsCentre(true);
	}
	getMailMessageFactory().push(planChangedOnlineMail);
    }

    public void mergeSourceToProduction(User pUser, String planId, Constants.PROD_SCRIPT_PARAMS param) {
	JSONResponse lResponse = new JSONResponse();
	JobDetails lwssSetOnline = new JobDetails();
	ImpPlan impPlan = getImpPlanDAO().find(planId);
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(planId);
	for (SystemLoad systemLoad : lSystemLoadList) {
	    lwssSetOnline.setStatus(impPlan.getId() + ": Merge Source/Dervied artifacts to Production has started for " + systemLoad.getSystemId().getName());
	    wsserver.sendMessage(Constants.Channels.ONLINE_PROCESS, pUser, lwssSetOnline);
	    com.tsi.workflow.beans.dao.System system = systemLoad.getSystemId();
	    String command = Constants.SystemScripts.PRODUCTION_MERGE.getScript() + impPlan.getImplementationList().get(0).getId().toLowerCase() + "_" + system.getName().toLowerCase() + "_" + Constants.JENKINS_DATEFORMAT.get().format(systemLoad.getLoadDateTime()) + " " + param.name();
	    LOG.info(command);
	    lResponse = getsSHClientUtils().executeCommand(system, command);
	    LOG.info("Response from SSH : " + lResponse.getStatus());
	    if (!lResponse.getStatus()) {
		throw new WorkflowException(impPlan.getId() + ":Unable to merge source/derived artifacts to Production for " + systemLoad.getSystemId().getName());
	    }
	    lwssSetOnline.setStatus(impPlan.getId() + ":Merge Source/Derived artifacts  to Production has completed for " + systemLoad.getSystemId().getName());
	    wsserver.sendMessage(Constants.Channels.ONLINE_PROCESS, pUser, lwssSetOnline);
	}
    }

    public void onlineJenkinsJob(User pUser, ImpPlan pPlan) {
	List<Implementation> lImpList = getImplementationDAO().findByImpPlan(pPlan.getId());
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(pPlan.getId());
	for (SystemLoad pSystemLoad : lSystemLoadList) {

	    // If there is Inprogress Build then continue with other systems
	    List<OnlineBuild> lInprogressJobs = getOnlineBuildDAO().findLastBuildInProgress(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), Constants.BUILD_TYPE.ONL_BUILD);
	    if (!lInprogressJobs.isEmpty()) {
		LOG.info("Jenkins Jobs are inprogress for system - " + pSystemLoad.getSystemId().getId() + " Plan id - " + pPlan.getId());
		continue;
	    }

	    LOG.info("Plan Status " + pPlan.getPlanStatus() + " Plan " + pPlan.getId());
	    // • JenkinsAPI -> jobs/FEEDBACK? IMP_ID_LoadDate=
	    // t1800914_001_wsp_20190202090000&Feedback_Status=ONLINE_ACCEPT
	    String lPlanSystemInfo = lImpList.get(0).getId() + "_" + pSystemLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(pSystemLoad.getLoadDateTime());

	    HashMap<String, String> params = new HashMap<>();
	    params.put("IMP_ID_LoadDate", lPlanSystemInfo.toLowerCase());
	    params.put("Feedback_Status", Constants.PROD_SCRIPT_PARAMS.ONLINE_ACCEPT.name());

	    JenkinsBuild executeJob = getJenkinsClient().executeJob(pUser, Constants.ONLINE.FEEDBCK_.name() + pSystemLoad.getSystemId().getName(), params);
	    executeJob.setSystemLoadId("" + pSystemLoad.getId());
	    if (!onlineBuildJobs.contains(executeJob)) {
		if (!lAsyncPlansStartTimeMap.containsKey(pPlan.getId() + "-" + Constants.BUILD_TYPE.ONL_BUILD.name())) {
		    lAsyncPlansStartTimeMap.put(pPlan.getId() + "-" + Constants.BUILD_TYPE.ONL_BUILD.name(), java.lang.System.currentTimeMillis());
		}
		onlineFallbackBuildSave(pUser, pPlan, pSystemLoad, executeJob, Constants.BUILD_TYPE.ONL_BUILD.name());
		onlineBuildJobs.add(executeJob);
		LOG.info("Online Build Job for Plan " + pPlan.getId() + "/" + pSystemLoad.getSystemId().getName() + " is added into Queue Successfully and QueueURL is :" + executeJob.getQueueUrl() + " and Build Number is : " + executeJob.getBuildNumber());
	    }

	}
    }

    public void onlineFallbackBuildSave(User pUser, ImpPlan pPlan, SystemLoad pSystemLoad, JenkinsBuild executeJob, String buildType) {
	// 891 - Remove the previously ACTIVE records
	List<OnlineBuild> lActiveBuilds = getOnlineBuildDAO().findBySystemAndType(pPlan, pSystemLoad.getSystemId(), buildType);
	if (lActiveBuilds != null) {
	    lActiveBuilds.forEach(t -> getOnlineBuildDAO().delete(pUser, t));
	}

	OnlineBuild lbuild = new OnlineBuild();
	lbuild.setPlanId(pPlan);
	lbuild.setSystemId(pSystemLoad.getSystemId());
	lbuild.setBuildType(buildType);
	lbuild.setBuildDateTime(executeJob.getBuildTime());
	lbuild.setBuildNumber(executeJob.getBuildNumber());
	lbuild.setJenkinsUrl(executeJob.getQueueUrl());
	lbuild.setJobStatus("P");
	getOnlineBuildDAO().save((User) pUser, lbuild);

	executeJob.setBuildType(buildType);
	executeJob.setStartedDate(lbuild.getCreatedDt());
	executeJob.setUser(pUser);
	executeJob.setPlanId(pSystemLoad.getPlanId().getId());

    }

}
