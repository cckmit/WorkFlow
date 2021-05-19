/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.DevManagerApproveActivityMessage;
import com.tsi.workflow.activity.MacroHeaderOnlineMessage;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.activity.PlanSubmitActivityMessage;
import com.tsi.workflow.activity.QATestingStatusActivityMessage;
import com.tsi.workflow.activity.RejectionActivityMessage;
import com.tsi.workflow.activity.StageBuildActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.RFCDetails;
import com.tsi.workflow.beans.dao.RepositoryDetails;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.dto.SystemLoadDTO;
import com.tsi.workflow.beans.ui.AssigendPlanView;
import com.tsi.workflow.beans.ui.DependentPlanRejectDetail;
import com.tsi.workflow.beans.ui.RepoUserView;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.RFCDetailsDAO;
import com.tsi.workflow.dao.RepoDetailsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitUserDetails;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.gitblit.model.AccessPermission;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.RepositoryPermission;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.ExceptionLoadNotificationHelper;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.mail.AccessPermissionMail;
import com.tsi.workflow.mail.DevManagerAssignmentMail;
import com.tsi.workflow.mail.ExceptionSourceContentionMail;
import com.tsi.workflow.mail.RejectMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.report.SearchExcelCreator;
import com.tsi.workflow.schedular.jenkins.DEVLBuildMonitor;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.WFLOGGER;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
public class DeveloperManagerService extends BaseService {

    private static final Logger LOG = Logger.getLogger(DeveloperManagerService.class.getName());

    // Should be local one, on Cache implementation
    // we need to Set Keys in Cache and
    // need to change the isSubmitInprogress API from Cache
    /*
     * @Autowired
     *
     * @Qualifier("lPlanUpdateStatusMap") ConcurrentHashMap<String, User>
     * lPlanUpdateStatusMap;
     */
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    RejectHelper rejectHelper;
    @Autowired
    BPMClientUtils lBPMClientUtils;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    LDAPAuthenticatorImpl authenticator;
    @Autowired
    ConcurrentLinkedQueue stagingWorkspaceCreationJobs;
    @Autowired
    ExceptionLoadNotificationHelper exceptionLoadNotificationHelper;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    PlatformDAO platformDAO;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    PRNumberHelper prStatusUpdateinNAS;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    FallbackHelper fallbackHelper;
    @Autowired
    @Qualifier("lPlanOnlineFallbackStatusMap")
    ConcurrentHashMap<String, String> lPlanOnlineFallbackStatusMap;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    GitUserDetails gitUserDetails;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    RFCDetailsDAO lRFCDetailsDAO;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    CommonBaseService commonBaseService;
    @Autowired
    AuditCommonHelper auditCommonHelper;
    @Autowired
    RepoDetailsDAO repoDetailsDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    CommonService commonService;

    public CommonService getCommonService() {
	return commonService;
    }

    public CommonBaseService getCommonBaseService() {
	return commonBaseService;
    }

    public RFCDetailsDAO getRFCDetailsDAO() {
	return lRFCDetailsDAO;
    }

    public PlatformDAO getPlatformDAO() {
	return platformDAO;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public RejectHelper getRejectHelper() {
	return rejectHelper;
    }

    public BPMClientUtils getBPMClientUtils() {
	return lBPMClientUtils;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public JenkinsClient getJenkinsClient() {
	return lJenkinsClient;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }
    // public ConcurrentLinkedQueue getStagingWorkspaceCreationJobs() {
    // return stagingWorkspaceCreationJobs;
    // }

    public ExceptionLoadNotificationHelper getExceptionLoadNotificationHelper() {
	return exceptionLoadNotificationHelper;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public void setGitHelper(GITHelper gitHelper) {
	this.gitHelper = gitHelper;
    }

    public ProjectDAO getProjectDAO() {
	return projectDAO;
    }

    public PRNumberHelper getPRNumberHelper() {
	return prStatusUpdateinNAS;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public GitUserDetails gitUserDetails() {
	return gitUserDetails;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public AuditCommonHelper getAuditCommonHelper() {
	return auditCommonHelper;
    }

    public RepoDetailsDAO getRepoDetailsDAO() {
	return repoDetailsDAO;
    }

    @Transactional
    public JSONResponse getMyPlanTasks(User pCurrentUser, Integer offset, Integer limit, HashMap<String, String> lOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
	HashMap<String, Serializable> pFilter = new HashMap();
	// BYPASSED_ACCEPTANCE_TESTING
	pFilter.put("planStatus", Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name());
	pFilter.put("devManager", currentOrDelagateUser.getId());
	Long count = getImpPlanDAO().count(pFilter);
	List<ImpPlan> lReturnPlanList = new ArrayList();
	if (count != 0) {
	    if (lOrderBy != null && !lOrderBy.isEmpty()) {
		for (Map.Entry<String, String> pOrderBy : lOrderBy.entrySet()) {

		    if (pOrderBy.getKey().equals("loaddatetime")) {
			lReturnPlanList = getImpPlanDAO().getPlansByLoadDateTime(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name(), currentOrDelagateUser.getId(), offset, limit, lOrderBy);

		    } else {
			lReturnPlanList = getImpPlanDAO().findAll(pFilter, offset, limit, new LinkedHashMap(lOrderBy));
		    }
		}
	    } else {
		lReturnPlanList = getImpPlanDAO().getPlansByLoadDateTime(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name(), currentOrDelagateUser.getId(), offset, limit, null);
	    }
	}

	lResponse.setData(lReturnPlanList);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setCount(count);
	return lResponse;
    }

    @Transactional
    public JSONResponse getAssignedPlans(User pCurrentUser, Integer offset, Integer limit, HashMap<String, String> lOrderBy, String planId, String planStatus) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("devManager", currentOrDelagateUser.getId());
	if (planId != null && !planId.trim().isEmpty()) {
	    pFilter.put("id", "%" + planId + "%");
	}
	if (planStatus != null && !planStatus.trim().isEmpty()) {
	    pFilter.put("planStatus", planStatus);
	}
	Long count = getImpPlanDAO().count(pFilter);
	LOG.info("count value " + count);
	List<ImpPlan> lReturnPlanList = new ArrayList();
	if (lOrderBy == null) {
	    lReturnPlanList = getImpPlanDAO().getPlansByLoadDateTime(currentOrDelagateUser.getId(), offset, limit, planId, planStatus, null);
	} else {
	    lReturnPlanList = getImpPlanDAO().findAll(pFilter, offset, limit, new LinkedHashMap(lOrderBy));
	}
	lResponse.setData(lReturnPlanList);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setCount(count);
	return lResponse;
    }

    @Transactional
    public JSONResponse approvePlan(User pUser, String pPlanId, String comments) {
	JSONResponse lResponse = new JSONResponse();
	ImpPlan pImpPlan = getImpPlanDAO().find(pPlanId);

	if (pImpPlan.getId().startsWith("D") && pImpPlan.getRfcFlag()) {
	    List<RFCDetails> rfcDetails = getRFCDetailsDAO().findByImpPlan(pPlanId);
	    if (rfcDetails.stream().filter(rfc -> rfc.getActive().equals("Y") && (rfc.getRfcNumber() == null || rfc.getRfcNumber().isEmpty())).findAny().isPresent()) {
		throw new WorkflowException("Approval Failed: RFC Number needed to proceed further. Please Contact DL Core Change team.");
	    }
	}
	if (pImpPlan.getPlanStatus().equals(Constants.PlanStatus.DEV_MGR_APPROVED.name())) {
	    throw new WorkflowException("The Plan " + pPlanId + "is already approved by " + pImpPlan.getApproverName());
	}
	List<Implementation> lImplementation = getImplementationDAO().findByImpPlan(pImpPlan);
	List<String> developers = lImplementation.stream().map(Implementation::getDevId).distinct().collect(Collectors.toList());
	Boolean leadCondition = !pUser.getId().equalsIgnoreCase(pImpPlan.getLeadId());
	Boolean developerCondition = !developers.contains(pUser.getId());
	if (leadCondition && developerCondition) {
	    try {
		List<TaskVariable> lTaskVars = new ArrayList<>();
		lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", pImpPlan.getMacroHeader()));
		lTaskVars.add(new TaskVariable("Approver", pUser.getDisplayName()));
		getBPMClientUtils().setTaskAsCompletedWithVariables(pUser, pImpPlan.getProcessId(), lTaskVars);
		String lOldPlanStatus = pImpPlan.getPlanStatus();

		if (!pImpPlan.getMacroHeader()) {
		    StringBuilder lGroupNames = new StringBuilder();
		    List<LDAPGroup> lGroups = getLdapGroupConfig().getLoadsControlGroups();
		    for (LDAPGroup lLDAPGroup : lGroups) {
			lGroupNames.append(lLDAPGroup.getLdapGroupName()).append(",");
		    }
		    if (lGroupNames != null && !lGroupNames.toString().isEmpty()) {
			lGroupNames.setLength(lGroupNames.length() - 1);
		    }
		    getBPMClientUtils().assignTaskToGroup(pUser, pImpPlan.getProcessId(), lGroupNames.toString(), lTaskVars);
		    pImpPlan.setPlanStatus(Constants.PlanStatus.DEV_MGR_APPROVED.name());
		} else {
		    StringBuilder lGroupNames = new StringBuilder();
		    List<LDAPGroup> lGroups = getLdapGroupConfig().getServiceDeskGroups();
		    for (LDAPGroup lLDAPGroup : lGroups) {
			lGroupNames.append(lLDAPGroup.getLdapGroupName()).append(",");
		    }
		    lGroupNames.setLength(lGroupNames.length() - 1);
		    getBPMClientUtils().assignTaskToGroup(pUser, pImpPlan.getProcessId(), lGroupNames.toString(), lTaskVars);
		    pImpPlan.setPlanStatus(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());
		}

		pImpPlan.setDevMgrComment(comments);
		pImpPlan.setApprover(pUser.getId());
		pImpPlan.setApproverName(pUser.getDisplayName());
		pImpPlan.setDevMgrApproveDateTime(new Date());
		pImpPlan.setApproveMailFlag(Boolean.FALSE);
		getImpPlanDAO().update(pUser, pImpPlan);

		String devManager = getLDAPAuthenticatorImpl().getUserDetails(pUser.getId()).getDisplayName();
		DevManagerApproveActivityMessage activityMessage = new DevManagerApproveActivityMessage(pImpPlan, null);
		activityMessage.setDevManger(devManager);
		activityMessage.setStatus("passed");
		getActivityLogDAO().save(pUser, activityMessage);

		StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class);
		statusChangeToDependentPlanMail.addToAddressUserId(pImpPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		statusChangeToDependentPlanMail.setImpPlanId(pImpPlan.getId());
		statusChangeToDependentPlanMail.setOldStatus(lOldPlanStatus);
		statusChangeToDependentPlanMail.setNewStatus(pImpPlan.getPlanStatus());
		getMailMessageFactory().push(statusChangeToDependentPlanMail);

	    } catch (WorkflowException Ex) {
		LOG.error("Unable to Approve Implmentation Plan", Ex);
		throw Ex;
	    } catch (Exception ex) {
		LOG.error("Unable to Approve Implmentation Plan", ex);
		throw new WorkflowException("Unable to Approve Implementation Plan - " + pImpPlan, ex);
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} else {

	    String errorMessage = null;
	    String errorLog = null;
	    DevManagerApproveActivityMessage activityMessage = new DevManagerApproveActivityMessage(pImpPlan, null);
	    if (!leadCondition) {
		// Activity Log Lead same person
		String devLead = getLDAPAuthenticatorImpl().getUserDetails(pImpPlan.getLeadId()).getDisplayName();
		String devManager = getLDAPAuthenticatorImpl().getUserDetails(pUser.getId()).getDisplayName();
		activityMessage.setDevLead(devLead);
		activityMessage.setStatus("failed");
		activityMessage.setLeadCondition(Boolean.TRUE);
		activityMessage.setDevManger(devManager);
		errorLog = "Approval Failed: Application Developer Lead and Approving manager cannot be the same person. ";
		errorMessage = "Approval Failed: Application Developer Lead and Approving manager cannot be the same person. " + "Please re-delegate to a different Dev Manager for approval or contact tool admin for assistance";
	    } else {
		// Activity Log developer same person
		String developer = lImplementation.stream().map(Implementation::getDevName).distinct().collect(Collectors.joining(","));
		String devManager = getLDAPAuthenticatorImpl().getUserDetails(pUser.getId()).getDisplayName();
		activityMessage.setDevLead(developer);
		activityMessage.setStatus("failed");
		activityMessage.setLeadCondition(Boolean.FALSE);
		activityMessage.setDevManger(devManager);
		errorLog = "Approval Failed: Application Developer  and Approving manager cannot be the same person. ";
		errorMessage = "Approval Failed: Application Developer  and Approving manager cannot be the same person. " + "Please re-delegate to a different Dev Manager for approval or contact tool admin for assistance";
	    }
	    getActivityLogDAO().save(pUser, activityMessage);
	    lResponse.setStatus(Boolean.FALSE);
	    LOG.error(errorLog);
	    lResponse.setErrorMessage(errorMessage);

	}
	return lResponse;
    }

    // Should be moved to PlanHelper
    public JSONResponse doStagingBuild(User pUser, String pImpPlanId) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String lAudit = "";
	    ImpPlan pImpPlan = getImpPlanDAO().find(pImpPlanId);
	    // Below process are done in the submit itself.
	    // if
	    // (pImpPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name()))
	    // {
	    // lAudit = getImpPlanDAO().doPlanAudit(pImpPlan.getId(),
	    // Constants.PlanStatus.APPROVED.name());
	    // if (!lAudit.isEmpty()) {
	    // LOG.info("Approval date audit fails, " + lAudit);
	    // throw new WorkflowException("Approval date audit fails, " + lAudit);
	    // }
	    // }

	    List<Implementation> lImpList = getImplementationDAO().findByImpPlan(pImpPlan.getId());
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(pImpPlan.getId());
	    for (SystemLoad lSystemLoad : lSystemLoadList) {

		Boolean byPassRegression = Boolean.FALSE;
		if (pImpPlan.getQaBypassStatus().equalsIgnoreCase(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name())) {
		    byPassRegression = Boolean.TRUE;
		}
		/**
		 * zTPFM-2575 - NewFile validation for git call
		 */
		String newFile = "false";
		List<CheckoutSegments> checkoutList = getCheckoutSegmentsDAO().findNewFileCreationPlanBySystem(pImpPlanId, lSystemLoad.getSystemId().getName());
		if (checkoutList.stream().filter((t) -> t.getRefStatus() == null).findAny().isPresent()) {
		    newFile = "true";
		}

		Map<String, String> lJobParams = new HashMap<>();
		lJobParams.put("ImplementationID", lImpList.get(0).getId().toLowerCase() + "_" + lSystemLoad.getSystemId().getName().toLowerCase());
		lJobParams.put("NewFile", newFile);
		JenkinsBuild lExecuteJob = null;
		if (getWFConfig().isMultipleBuildAllowed()) {
		    lExecuteJob = getJenkinsClient().executeJob(pUser, Constants.STAGING.CWS_STG_.name() + lSystemLoad.getSystemId().getName(), lJobParams);
		} else {
		    lJobParams.put("BuildType", Constants.TYPE.STG.name());
		    lExecuteJob = getJenkinsClient().executeJob(pUser, Constants.BUILD.CWS_ALL_.name() + lSystemLoad.getSystemId().getAliasName(), lJobParams);
		}
		lExecuteJob.setSystemLoadId("" + lSystemLoad.getId());
		lExecuteJob.setByPassRegression(byPassRegression);
		if (!stagingWorkspaceCreationJobs.contains(lExecuteJob)) {
		    Build lbuild = new Build();
		    lbuild.setPlanId(pImpPlan);
		    lbuild.setSystemId(lSystemLoad.getSystemId());
		    lbuild.setBuildType(Constants.BUILD_TYPE.STG_CWS.name());
		    lbuild.setBuildDateTime(lExecuteJob.getBuildTime());
		    lbuild.setBuildNumber(lExecuteJob.getBuildNumber());
		    lbuild.setJenkinsUrl(lExecuteJob.getQueueUrl());
		    lbuild.setJobStatus("P");
		    getBuildDAO().save((User) pUser, lbuild);
		    lExecuteJob.setBuildType(Constants.BUILD_TYPE.STG_CWS.name());
		    lExecuteJob.setStartedDate(lbuild.getCreatedDt());
		    lExecuteJob.setUser(pUser);
		    lExecuteJob.setPlanId(pImpPlan.getId());

		    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(pImpPlan.getId())) {
			DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(pImpPlan.getId());
			RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(pImpPlan, null);
			if (rejectDetail.getAutoRejectReason() != null) {
			    rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
			}
			getPlanHelper().revertSubmittedPlanToActive(rejectDetail.getUser(), pImpPlan.getId());
			getActivityLogDAO().save(rejectDetail.getUser(), rejectActivityMessage);
		    } else {
			stagingWorkspaceCreationJobs.add(lExecuteJob);
		    }
		}

		StageBuildActivityMessage lMessage = new StageBuildActivityMessage(pImpPlan, null, lSystemLoad.getSystemId());
		lMessage.setStatus("initiated");
		lMessage.setComment(pImpPlan.getDevMgrComment());
		getActivityLogDAO().save(pUser, lMessage);
	    }
	} catch (WorkflowException Ex) {
	    throw Ex;
	} catch (Exception ex) {
	    LOG.error("Unable to Submit Implmentation Plan", ex);
	    throw new WorkflowException("Unable to Submit Implementation Plan - " + pImpPlanId, ex);
	} finally {
	    cacheClient.getPlanUpdateStatusMap().remove(pImpPlanId);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse approveProcessinBPMForPlan(User pUser, String pPlanId, Boolean byPassRegression) {
	JSONResponse lResponse = new JSONResponse();
	cacheClient.getSocketUserMap().put(pPlanId, pUser.getId());
	lResponse.setUserId(pUser.getId());
	lResponse.setMetaData(pPlanId);
	try {
	    ImpPlan lPlan = getImpPlanDAO().find(pPlanId);
	    List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(pPlanId);
	    List<System> systemList = new ArrayList<>();
	    List<Build> lBuilds = new ArrayList<>();
	    for (SystemLoad systemLoad : systemLoadList) {
		if (systemLoad.getActive().equals("Y")) {
		    systemList.add(systemLoad.getSystemId());
		}
	    }
	    if (!systemList.isEmpty()) {
		lBuilds.addAll(getBuildDAO().findLastBuild(pPlanId, systemList, Constants.BUILD_TYPE.STG_CWS));
		lBuilds.addAll(getBuildDAO().findLastBuild(pPlanId, systemList, Constants.BUILD_TYPE.STG_BUILD));
		lBuilds.addAll(getBuildDAO().findLastBuild(pPlanId, systemList, Constants.BUILD_TYPE.STG_LOAD));
	    }
	    for (Build lBuild : lBuilds) {
		if (lBuild.getJobStatus() == null || lBuild.getJobStatus().equals("P")) {
		    lResponse.setStatus(Boolean.FALSE);
		    LOG.warn("Still Not ALL Systems Build Completed, So Skipping the Approval for Plan Id: " + pPlanId);
		    return lResponse;
		}
	    }

	    LOG.info("ALL Systems Build Completed, Approving for Plan Id: " + pPlanId);
	    StageBuildActivityMessage lMessage = new StageBuildActivityMessage(lPlan, null);
	    lMessage.setStatus("approved");
	    getActivityLogDAO().save(pUser, lMessage);

	    String lOldPlanStatus = lPlan.getPlanStatus();
	    List<TaskVariable> lTaskVars = new ArrayList<>();

	    if (lPlan.getMacroHeader()) {
		lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
		lTaskVars.add(new TaskVariable("Sumbitted", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
		getBPMClientUtils().setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars);
		lTaskVars.clear();

		getBPMClientUtils().assignTask(pUser, lPlan.getProcessId(), lPlan.getDevManager(), lTaskVars);
		lTaskVars.clear();

		PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lPlan, null);
		// Actual Status is Approved, i.e., internal status
		planStatusActivityMessage.setStatus(Constants.PlanStatus.SUBMITTED.name());
		getActivityLogDAO().save(pUser, planStatusActivityMessage);
		Date loadDate = systemLoadList.stream().map(SystemLoad::getLoadDateTime).max(Date::compareTo).get();
		DevManagerAssignmentMail devManagerAssignmentMail = (DevManagerAssignmentMail) mailMessageFactory.getTemplate(DevManagerAssignmentMail.class);
		devManagerAssignmentMail.setLeadName(lPlan.getLeadName());
		devManagerAssignmentMail.setPlanId(lPlan.getId());
		devManagerAssignmentMail.addToAddressUserId(lPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		if (loadDate != null) {
		    devManagerAssignmentMail.setLoadDateTime(loadDate);
		}
		devManagerAssignmentMail.setPlanStatus(Constants.PlanStatus.APPROVED.name());
		mailMessageFactory.push(devManagerAssignmentMail);

	    } else if (byPassRegression) {
		lPlan.setPlanStatus(Constants.PlanStatus.BYPASSED_REGRESSION_TESTING.name());
		lTaskVars.add(new TaskVariable("Sumbitted", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", false));
		getBPMClientUtils().setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars);

		lTaskVars.clear();
		StringBuilder lGroupNames = new StringBuilder();
		List<LDAPGroup> lGroups = getLdapGroupConfig().getSystemSupportGroups();
		for (LDAPGroup lLDAPGroup : lGroups) {
		    lGroupNames.append(lLDAPGroup.getLdapGroupName()).append(",");
		}
		lGroupNames.setLength(lGroupNames.length() - 1);
		getBPMClientUtils().assignTaskToGroup(pUser, lPlan.getProcessId(), lGroupNames.toString(), lTaskVars);

		// Activity Log on QA Functional Testing ByPassed
		PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lPlan, null);
		// Actual Status is Approved, i.e., internal status
		planStatusActivityMessage.setStatus(Constants.PlanStatus.SUBMITTED.name());
		getActivityLogDAO().save(pUser, planStatusActivityMessage);

		planStatusActivityMessage.setStatus(Constants.PlanStatus.BYPASSED_FUNCTIONAL_TESTING.name());
		getActivityLogDAO().save(pUser, planStatusActivityMessage);

		planStatusActivityMessage.setStatus(Constants.PlanStatus.BYPASSED_REGRESSION_TESTING.name());
		getActivityLogDAO().save(pUser, planStatusActivityMessage);

	    } else {
		lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
		lTaskVars.add(new TaskVariable("Sumbitted", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
		lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
		lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", false));
		getBPMClientUtils().setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars);

		StringBuilder lGroupNames = new StringBuilder();
		List<LDAPGroup> lGroups = getLdapGroupConfig().getQAGroups();
		for (LDAPGroup lLDAPGroup : lGroups) {
		    lGroupNames.append(lLDAPGroup.getLdapGroupName()).append(",");
		}
		lGroupNames.setLength(lGroupNames.length() - 1);
		getBPMClientUtils().assignTaskToGroup(pUser, lPlan.getProcessId(), lGroupNames.toString(), lTaskVars);

		PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lPlan, null);
		// Actual Status is Approved, i.e., internal status
		planStatusActivityMessage.setStatus(Constants.PlanStatus.SUBMITTED.name());
		getActivityLogDAO().save(pUser, planStatusActivityMessage);

		List<SystemLoad> lSysLoadList = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()));
		for (SystemLoad sysLoad : lSysLoadList) {
		    QATestingStatusActivityMessage lActSysMessage = new QATestingStatusActivityMessage(lPlan, null);
		    lActSysMessage.setSystemName(sysLoad.getSystemId().getName());
		    lActSysMessage.setQaPhaseName("Functional");
		    getActivityLogDAO().save(pUser, lActSysMessage);

		}
		if (lPlan.getQaBypassStatus().equals(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name())) {
		    lPlan.setPlanStatus(Constants.PlanStatus.BYPASSED_FUNCTIONAL_TESTING.name());
		    planStatusActivityMessage.setStatus(lPlan.getPlanStatus());
		    getActivityLogDAO().save(pUser, planStatusActivityMessage);
		}
		// ZTPFM-1455 QA Functional Testers Mail Send
		if (lPlan.getPlanStatus().equals(Constants.PlanStatus.APPROVED.name())) {
		    getPlanHelper().sendMailNotificationQAFunTester(lPlan, false);
		}

	    }

	    if (!lPlan.getMacroHeader()) {
		// ZTPFM-1693 Update SO information against Plan
		getPlanHelper().getAndUpdateSOInfo(pUser, pPlanId);
	    }

	    // lPlan.setApprover(pUser.getId());
	    // lPlan.setApproverName(pUser.getDisplayName());
	    lPlan.setApproveDateTime(new Date());
	    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(lPlan.getId())) {
		DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(lPlan.getId());
		RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(lPlan, null);
		if (rejectDetail.getAutoRejectReason() != null) {
		    rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
		}
		planHelper.revertSubmittedPlanToActive(rejectDetail.getUser(), lPlan.getId());
		activityLogDAO.save(rejectDetail.getUser(), rejectActivityMessage);
		lResponse.setStatus(Boolean.FALSE);
	    } else {
		getImpPlanDAO().update(pUser, lPlan);
		getPRNumberHelper().writeFileInNAS(lPlan);

		getActivityLogDAO().save(pUser, new PlanSubmitActivityMessage(lPlan, null));

		List<ImpPlan> lDependentPlanList = getImpPlanDAO().findDependentPlans(lPlan.getId());
		if (!lDependentPlanList.isEmpty()) {
		    StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
		    lDependentPlanList.stream().forEach((lImpPlans) -> {
			statusChangeToDependentPlanMail.addToAddressUserId(lImpPlans.getLeadId(), Constants.MailSenderRole.LEAD);
		    });

		    statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
		    statusChangeToDependentPlanMail.setOldStatus(Constants.PlanStatus.ACTIVE.name());
		    // Actual Status is Approved, i.e., internal status
		    statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.APPROVED.name());
		    mailMessageFactory.push(statusChangeToDependentPlanMail);
		}

		if (lPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name())) {
		    exceptionLoadNotificationHelper.notifyDevelopers(pPlanId, Constants.PlanStatus.valueOf(lPlan.getPlanStatus()));
		}
		lResponse.setStatus(Boolean.TRUE);
	    }

	} catch (Exception Ex) {
	    LOG.error("Unable to Approve Implmentation Plan BPM ", Ex);
	    lResponse.setStatus(Boolean.FALSE);
	    wsserver.sendMessage(Constants.Channels.PLAN_SUBMIT, pUser.getId(), pPlanId, lResponse);
	    if (lAsyncPlansStartTimeMap.containsKey(pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name())) {
		WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/Submit" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name())) + " ms, )");
		lAsyncPlansStartTimeMap.remove(pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name());
	    }
	    return lResponse;
	} finally {
	    cacheClient.getPlanUpdateStatusMap().remove(pPlanId);
	}

	wsserver.sendMessage(Constants.Channels.PLAN_SUBMIT, pUser.getId(), pPlanId, lResponse);
	if (lAsyncPlansStartTimeMap.containsKey(pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name())) {
	    LOG.info("Async key is matched.Key: " + pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name());
	    WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/Submit" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name())) + " ms, )");
	    lAsyncPlansStartTimeMap.remove(pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name());
	} else {
	    LOG.info("Async key is not matched.Key: " + pPlanId + "-" + Constants.Channels.PLAN_SUBMIT.name());
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse rejectPlan(User pUser, String pPlanId, String pComments, boolean builFailure, boolean deleteLoadSetFlag) {
	JSONResponse lResponse = new JSONResponse();

	try {
	    ImpPlan lPlan = getImpPlanDAO().find(pPlanId);
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(pPlanId);
	    // Provide Access to ADL and assign BPM Activiti
	    List<TaskVariable> lTaskVars = new ArrayList<>();
	    lTaskVars.add(new TaskVariable("Submitted", "local", "boolean", false));
	    lTaskVars.add(new TaskVariable("Approved", "local", "boolean", false));
	    lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	    getBPMClientUtils().setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars);
	    lTaskVars.clear();
	    lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lPlan.getId()));
	    boolean lStatus;
	    if (lPlan.getProcessId() != null && !lPlan.getProcessId().isEmpty()) {
		lStatus = getBPMClientUtils().assignTask(pUser, lPlan.getProcessId(), lPlan.getLeadId(), lTaskVars);
		if (!lStatus) {
		    LOG.info("Unable to Set Permission for ADL -" + lPlan.getLeadId());
		}
	    }

	    // Provide Access to Developers and assign BPM Activiti
	    for (Implementation lImplementation : lPlan.getImplementationList()) {
		if (lImplementation.getProcessId() != null && !lImplementation.getProcessId().isEmpty()) {
		    lTaskVars.clear();
		    lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_ID, lImplementation.getId()));
		    lStatus = getBPMClientUtils().assignTask(pUser, lImplementation.getProcessId(), lImplementation.getDevId(), lTaskVars);
		    if (!lStatus) {
			LOG.info("Unable to Set Permission for Developer -" + lImplementation.getDevName());
		    }
		}
	    }
	    // Update the Plan and Implementation Status
	    getRejectHelper().updatePlanAndImplementationStatus((User) pUser, lPlan, pComments, null, Boolean.FALSE, builFailure);

	    Map<String, Date> lRejectedPlans = new HashMap();
	    List<Date> lMinLoadDate = new ArrayList();
	    String lCompanyName = "";
	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		if (lSystemLoad != null) {
		    lMinLoadDate.add(lSystemLoad.getLoadDateTime());
		    lCompanyName = lSystemLoad.getSystemId().getPlatformId().getNickName();
		}
	    }

	    String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, lPlan.getId().toLowerCase());
	    // Remove SCM Tag - Secured
	    if (!getsSHClientUtils().removeTag(lRepoName, Constants.TagStatus.SECURED)) {
		LOG.info("Unable to Remove Tag - " + Constants.TagStatus.SECURED);
	    }
	    getRejectHelper().tagImplementationPlan(pUser, lPlan, Constants.TagStatus.REJECTED);

	    List<PreProductionLoads> lLoadSets = preProductionLoadsDAO.findByPlanId(lPlan.getId());
	    lLoadSets.stream().filter(lPreProdLoad -> !lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && !lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).forEach(lPreProdLoad -> preProductionLoadsDAO.delete(pUser, lPreProdLoad));
	    Boolean isTOSActionNeeded = lLoadSets.stream().filter(lPreProdLoad -> lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).findAny().isPresent();

	    if (isTOSActionNeeded) {
		lPlan.setInprogressStatus(Constants.PlanInProgressStatus.REJECT.name());
		getImpPlanDAO().update(pUser, lPlan);

		lRejectedPlans.put(lPlan.getId(), Collections.min(lMinLoadDate));
	    } else {
		getRejectHelper().deleteStagingWorkspace(pUser, lPlan);
		getRejectHelper().deleteBuilds(pUser, lPlan);
	    }

	    getRejectHelper().deleteLoadSetActivated(pUser, lPlan, Boolean.TRUE);
	    getRejectHelper().removeDeployment(pUser, lPlan);

	    // Adding all dependent plans to avoid parallel actions.
	    List<String> dependentPlans = rejectHelper.getDependententPlans(lPlan.getId());
	    LOG.info(" Reject Dependent plans: " + dependentPlans.stream().collect(Collectors.joining(",")));
	    dependentPlans.forEach(depPlanId -> {
		cacheClient.getInProgressRelatedPlanMap().put(depPlanId, new DependentPlanRejectDetail(pUser, lPlan.getId(), pComments, pComments));
	    });
	    getRejectHelper().rejectDependentPlans(pUser, lPlan.getId(), pComments, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.FALSE, Boolean.TRUE, Boolean.FALSE, null, deleteLoadSetFlag, lRejectedPlans);

	    // ZTPFM-2625 Update status of PR Ticket as Reject
	    getPRNumberHelper().updatePRNumber(lPlan, Constants.PRNumberStatuses.REJECTED.getPRStatus());

	    // Send Mail Notification
	    RejectMail lRejectMail = (RejectMail) getMailMessageFactory().getTemplate(RejectMail.class);
	    lRejectMail.setLeadId(lPlan.getLeadId());
	    lRejectMail.setBuildFailure(builFailure);
	    lRejectMail.setCurrentUser((User) pUser);
	    lRejectMail.setPlanId(lPlan.getId());
	    getMailMessageFactory().push(lRejectMail);
	    // Removing Rejected plan from cache
	    cacheClient.getInProgressRelatedPlanMap().remove(lPlan.getId());

	} catch (Exception Ex) {
	    LOG.error("Unable to Reject the Implementaiton Plan - " + pPlanId, Ex);
	    throw new WorkflowException("Unable to Reject the Implementation Plan - " + pPlanId, Ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getApproveStatusByPlan(List<String> ids) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<String> lInProgressPlan = getBuildDAO().getBuildInProgressPlan(ids, new ArrayList(Constants.BUILD_TYPE.getStagingBuildType().keySet()));

	    Map<String, Boolean> lReturnList = new HashMap();
	    lInProgressPlan.forEach((lPlan) -> {
		lReturnList.put(lPlan, Boolean.TRUE);
	    });
	    Collection<String> lCompletedPlan = CollectionUtils.subtract(ids, lInProgressPlan);
	    lCompletedPlan.forEach((lPlan) -> {
		lReturnList.put(lPlan, Boolean.FALSE);
	    });
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(lReturnList);
	} catch (Exception Ex) {
	    LOG.error("Unable to get the Approval status", Ex);
	    throw new WorkflowException("Unable to get the Approval status", Ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getProductionRepoList(User pUser) {
	JSONResponse lResponse = new JSONResponse();
	User lUser = pUser.getCurrentOrDelagateUser();
	List<RepositoryView> repoList = new ArrayList<>();
	try {
	    repoList = getCacheClient().getFilteredRepositoryMap().values().stream().sorted().filter(x -> x.getRepository().getOwners().contains(lUser.getId()) && !x.getRepository().getTrimmedName().contains("DERIVED")).collect(Collectors.toList());
	    Comparator<RepositoryView> repoViewSorted = (RepositoryView o1, RepositoryView o2) -> o1.getRepository().getFuncArea().compareTo(o2.getRepository().getFuncArea());
	    Collections.sort(repoList, repoViewSorted);
	} catch (Exception ex) {
	    LOG.error("Unable to get the Repo List", ex);
	    throw new WorkflowException("Unable to get the Repo List", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(repoList);
	return lResponse;
    }

    @Transactional
    public JSONResponse updateRepoPermission(RepositoryView pReopsitoryView) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String lDerivedRepoName = pReopsitoryView.getRepository().getTrimmedName().replace("NONIBM_", "DERIVED_").replace("IBM_", "DERIVED_");
	    List<Repository> lSourceRepoList = new ArrayList();
	    List<Repository> lDerivedRepoList = new ArrayList();
	    User serviceUser = lDAPAuthenticatorImpl.getServiceUser();
	    lSourceRepoList.addAll(getCacheClient().getAllRepositoryMap().get(pReopsitoryView.getRepository().getTrimmedName()));
	    lDerivedRepoList.addAll(getCacheClient().getAllRepositoryMap().get(lDerivedRepoName));
	    // RepositoryView lSourceRepoView =
	    // getCacheClient().getFilteredRepositoryMap().get(pReopsitoryView.getRepository().getTrimmedName());

	    if (lSourceRepoList.isEmpty()) {
		throw new WorkflowException("Unable to update the Repository Owners");
	    }

	    for (Repository lRepos : lSourceRepoList) {
		lRepos.setCurrentAccess(pReopsitoryView.getDefaultAccess());
		lRepos.setDescription(pReopsitoryView.getRepository().getDescription());
		RepositoryDetails lRepoDetails = getRepoDetailsDAO().findByTrimeName(lRepos.getTrimmedName());
		lRepoDetails.setDefaultFileCreate(pReopsitoryView.getFilecreateFlag());
		lRepoDetails.setRepoDescription(pReopsitoryView.getRepository().getDescription());
		LOG.info("ADDED " + lRepos.getTrimmedName() + " Before Update Flag " + lRepoDetails.getDefaultFileCreate() + " After Update Flag " + pReopsitoryView.getFilecreateFlag());
		getRepoDetailsDAO().update(serviceUser, lRepoDetails);
		if (!getGitBlitClientUtils().updateGitRepository(lRepos)) {
		    throw new WorkflowException("Unable to update the Repository Owners");
		}
		// RepositoryView lRepoView =
		// getCacheClient().getFilteredRepositoryMap().get(lRepos.getTrimmedName());
		// lRepoView.setRepository(lRepos);
		// getCacheClient().getFilteredRepositoryMap().replace(lRepos.getTrimmedName(),
		// lRepoView);
	    }
	    for (Repository lRepos : lDerivedRepoList) {
		// PRCP - Added
		lRepos.setCurrentAccess(pReopsitoryView.getDefaultAccess());
		lRepos.setDescription(pReopsitoryView.getRepository().getDescription());
		if (!getGitBlitClientUtils().updateGitRepository(lRepos)) {
		    throw new WorkflowException("Unable to update the Repository Owners");
		}
		// RepositoryView lRepoView =
		// getCacheClient().getFilteredRepositoryMap().get(lRepos.getTrimmedName());
		// lRepoView.setRepository(lRepos);
		// getCacheClient().getFilteredRepositoryMap().replace(lRepos.getTrimmedName(),
		// lRepoView);
	    }

	    // Map<String, List<Repository>> lAllProdRepos = lSourceRepoList.stream()
	    // .collect(Collectors.groupingBy(x -> x.getTrimmedName()));
	    //
	    // for (Map.Entry<String, List<Repository>> lRepos : lAllProdRepos.entrySet()) {
	    // getCacheClient().getAllRepositoryMap().replace(lRepos.getKey(),
	    // lRepos.getValue());
	    // }
	    gitUserDetails().populateGitUserDetails();
	    getGitHelper().populateGitCacheNew();
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to update the default access for repository " + pReopsitoryView.getRepository().getTrimmedName(), ex);
	    throw new WorkflowException("Unable to update the default access for repository " + pReopsitoryView.getRepository().getTrimmedName());
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    // @Transactional
    // public JSONResponse getRepositoryByName(String repoName) {
    // JSONResponse lResponse = new JSONResponse();
    // RepositoryView lRepo = new RepositoryView();
    // try {
    // lRepo = getGitHelper().getRepositoryByName(repoName);
    // lResponse.setData(lRepo);
    // lResponse.setStatus(Boolean.TRUE);
    // } catch (Exception ex) {
    // LOG.error("Unable to get the Repository Details", ex);
    // throw new WorkflowException("Unable to get the Repository Details", ex);
    // }
    // return lResponse;
    // }
    public JSONResponse getProdRepoUsers(String pRepoName) {
	JSONResponse lResponse = new JSONResponse();
	Map<String, String> lReturnList = new HashMap();
	try {
	    Repository lRepo = getCacheClient().getAllRepositoryMap().get(pRepoName).get(0);
	    Map<String, AccessPermission> lUserAccessList = getCacheClient().getRepoWiseUserMap().get(lRepo.getName());
	    for (Map.Entry<String, AccessPermission> lUserAccess : lUserAccessList.entrySet()) {
		lReturnList.put(lUserAccess.getKey(), lUserAccess.getValue().toString());
	    }
	    lResponse.setData(lReturnList);
	    lResponse.setCount(lReturnList.size());
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error Occurs while fetching user list for repository - " + pRepoName, ex);
	    throw new WorkflowException("Unable to get Developers list for Repository - " + pRepoName, ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse setProdRepoUsers(User pUser, String pRepoName, String pAccess, RepoUserView lRepoUserListAccess) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    // PRCP
	    // Need to check for Mulitiple repos bby repo trim name
	    Map<String, String> pRepoUsersAccess = lRepoUserListAccess.getUserList();
	    String lRepoName = pRepoName.replace(".git", "").concat(".git").toLowerCase();

	    String lUpdatedPermission = Constants.RepoPermission.getKey(pAccess);

	    String lDerivedRepoName = pRepoName.replace("NONIBM_", "DERIVED_").replace("IBM_", "DERIVED_");
	    List<Repository> lSourceRepos = getCacheClient().getAllRepositoryMap().get(pRepoName);
	    List<Repository> lDerivedRepos = getCacheClient().getAllRepositoryMap().get(lDerivedRepoName);

	    Repository lRepo = !lSourceRepos.isEmpty() ? lSourceRepos.get(0) : null;
	    if (lRepo == null) {
		throw new WorkflowException("Unable to update User information, repository is not found");
	    }
	    Map<String, AccessPermission> lUserAccessList = getCacheClient().getRepoWiseUserMap().get(lRepo.getName());

	    Map<String, String> lRepoUsersAccess = new HashMap();
	    for (Map.Entry<String, AccessPermission> lUserAccess : lUserAccessList.entrySet()) {
		lRepoUsersAccess.put(lUserAccess.getKey(), lUserAccess.getValue().toString());
	    }

	    Map<String, String> lNewUser = new HashMap();
	    Map<String, String> lUpdatedUser = new HashMap();

	    List<RepositoryPermission> lGITUpdateUser = new ArrayList();
	    for (Map.Entry<String, String> lRepoUserAccess : pRepoUsersAccess.entrySet()) {
		User lUser = getLDAPAuthenticatorImpl().getUserDetails(lRepoUserAccess.getKey());
		RepositoryPermission lRepoPermission = new RepositoryPermission(lRepoUserAccess.getKey(), pAccess);
		lGITUpdateUser.add(lRepoPermission);
		if (lRepoUserAccess.getValue().equals("NEW")) {
		    lNewUser.put(lUser.getId(), lUser.getDisplayName());
		} else if (lRepoUsersAccess.containsKey(lRepoUserAccess.getKey()) && !lRepoUsersAccess.get(lRepoUserAccess.getKey()).equals(pAccess)) {
		    lUpdatedUser.put(lUser.getId(), lUser.getDisplayName());
		}
		lUserAccessList.put(lRepoUserAccess.getKey(), AccessPermission.fromCode(pAccess));
	    }

	    List<String> lMaliingRepoOwners = new ArrayList();
	    for (String lOwnersId : lRepo.getOwners()) {
		UserSettings lUserSettings = getUserSettingsDAO().find(lOwnersId, Constants.UserSettings.REPO_OWNER_ALERT.name());
		if (lUserSettings != null && lUserSettings.getValue().equals("Y")) {
		    lMaliingRepoOwners.add(lOwnersId);
		}
	    }

	    Set<String> repoNames = new HashSet<>();

	    for (Repository lRepos : lSourceRepos) {
		repoNames.add(lRepos.getName());
		// if (!getGitBlitClientUtils().setPermissionForGitRepository(lRepos.getName(),
		// lGITUpdateUser)) {
		// LOG.error("Error occurs while getting User list for repo - " +
		// lRepos.getName());
		// throw new WorkflowException("Unable to set repository access permission for
		// developer - " +
		// lRepos.getName());
		// }
	    }
	    for (Repository lRepos : lDerivedRepos) {
		repoNames.add(lRepos.getName());
		// if (!getGitBlitClientUtils().setPermissionForGitRepository(lRepos.getName(),
		// lGITUpdateUser)) {
		// LOG.error("Error occurs while getting User list for repo - " +
		// lRepos.getName());
		// throw new WorkflowException("Unable to set repository access permission for
		// developer - " +
		// lRepos.getName());
		// }
	    }
	    getGitHelper().updateRepoPermission(repoNames, lGITUpdateUser);
	    // Removal Mail for Owners
	    if (!lUpdatedUser.isEmpty()) {
		AccessPermissionMail lAccessMail = (AccessPermissionMail) getMailMessageFactory().getTemplate(AccessPermissionMail.class);
		lAccessMail.setUserList(new ArrayList(lUpdatedUser.values()));
		lAccessMail.setRepoName(pRepoName);
		lAccessMail.setIsAdded(Boolean.FALSE);
		lAccessMail.setRepoPermission(lUpdatedPermission);
		lAccessMail.setIsUserMail(Boolean.FALSE);
		lAccessMail.setDevManager(pUser.getDisplayName());
		for (String repoOwners : lMaliingRepoOwners) {
		    lAccessMail.addToAddressUserId(repoOwners, Constants.MailSenderRole.REPO_OWNERS);
		}
		getMailMessageFactory().push(lAccessMail);

		for (Map.Entry<String, String> tUser : lUpdatedUser.entrySet()) {
		    lAccessMail = (AccessPermissionMail) getMailMessageFactory().getTemplate(AccessPermissionMail.class);
		    lAccessMail.setUserList(Arrays.asList(tUser.getValue()));
		    lAccessMail.setRepoName(pRepoName);
		    lAccessMail.setIsAdded(Boolean.FALSE);
		    lAccessMail.setIsUserMail(Boolean.TRUE);
		    lAccessMail.setRepoPermission(lUpdatedPermission);
		    lAccessMail.setDevManager(pUser.getDisplayName());
		    lAccessMail.addToAddressUserId(tUser.getKey(), Constants.MailSenderRole.REPO_OWNERS);
		    getMailMessageFactory().push(lAccessMail);
		}
	    }

	    if (!lNewUser.isEmpty()) {
		AccessPermissionMail lAccessMail = (AccessPermissionMail) getMailMessageFactory().getTemplate(AccessPermissionMail.class);
		lAccessMail.setUserList(new ArrayList(lNewUser.values()));
		lAccessMail.setRepoName(pRepoName);
		lAccessMail.setIsAdded(Boolean.TRUE);
		lAccessMail.setIsUserMail(Boolean.FALSE);
		lAccessMail.setRepoPermission(lUpdatedPermission);
		lAccessMail.setDevManager(pUser.getDisplayName());
		for (String repoOwners : lMaliingRepoOwners) {
		    lAccessMail.addToAddressUserId(repoOwners, Constants.MailSenderRole.REPO_OWNERS);
		}
		getMailMessageFactory().push(lAccessMail);

		for (Map.Entry<String, String> tUser : lNewUser.entrySet()) {
		    lAccessMail = (AccessPermissionMail) getMailMessageFactory().getTemplate(AccessPermissionMail.class);
		    lAccessMail.setUserList(Arrays.asList(tUser.getValue()));
		    lAccessMail.setRepoName(pRepoName);
		    lAccessMail.setIsAdded(Boolean.TRUE);
		    lAccessMail.setIsUserMail(Boolean.TRUE);
		    lAccessMail.setRepoPermission(lUpdatedPermission);
		    lAccessMail.setDevManager(pUser.getDisplayName());
		    lAccessMail.addToAddressUserId(tUser.getKey(), Constants.MailSenderRole.REPO_OWNERS);
		    getMailMessageFactory().push(lAccessMail);
		}
	    }
	    // getGitHelper().populateGitCache();
	} catch (WorkflowException ex) {
	    LOG.error("Unable to setRepoOwnersPermission for this Owners", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to setRepoOwnersPermission for this Owners", ex);
	    throw new WorkflowException("Unable to set access permission for users for repository -  " + pRepoName, ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getProjectList(User pUser, Integer pOffset, Integer pLimit, String filter, LinkedHashMap pOrderBy, String searchField) {
	JSONResponse lResponse = new JSONResponse();
	List<Project> lCSR = getProjectDAO().findFiltered(filter, pOffset, pLimit, pOrderBy, searchField, Boolean.TRUE);
	Long count = getProjectDAO().countBy(filter, Boolean.TRUE, searchField);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lCSR);
	lResponse.setCount(count);
	return lResponse;
    }

    @Transactional
    public JSONResponse saveProject(User pUser, Project project) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    setManagerName(project);
	    getProjectDAO().save(pUser, project);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Unable to save Project ", ex);
	    throw new WorkflowException("Unable to save Project  ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteProject(User pUser, Project project) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    getProjectDAO().delete(pUser, project);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Unable to delete Project ", ex);
	    throw new WorkflowException("Unable to delete Project  ", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse updateProject(User pUser, Project project) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    setManagerName(project);
	    getProjectDAO().update(pUser, project);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Unable to update Project ", ex);
	    throw new WorkflowException("Unable to update Project  ", ex);
	}
	return lResponse;
    }

    private void setManagerName(Project project) {
	List<String> managerIds = Arrays.asList(project.getManagerId().split(","));
	List<String> managerNames = new ArrayList<>();
	for (String managerId : managerIds) {
	    if (managerId != null && !managerId.isEmpty()) {
		managerNames.add(getLDAPAuthenticatorImpl().getUserDetails(managerId).getDisplayName());
	    }
	}
	project.setManagerName(String.join(",", managerNames));
    }

    @Transactional
    public JSONResponse getMacroHeaderList(User pCurrentUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
	try {
	    // List<ImpPlan> findAll =
	    // getImpPlanDAO().getPlansByMacroHeader(currentOrDelagateUser.getId(),
	    // Constants.PlanStatus.APPROVED.name(), Boolean.TRUE, pOffset, pLimit,
	    // pOrderBy);
	    Long count = getImpPlanDAO().getPlansCountByMacroHeader(currentOrDelagateUser.getId(), Constants.PlanStatus.APPROVED.name(), Boolean.TRUE);

	    List<ImpPlan> lReturnPlanList = new ArrayList();
	    if (count != 0) {
		if (pOrderBy != null && !pOrderBy.isEmpty()) {
		    for (Map.Entry<String, String> lOrderBy : pOrderBy.entrySet()) {

			if (lOrderBy.getKey().equals("loaddatetime")) {
			    lReturnPlanList = getImpPlanDAO().getMacroHeaderPlansByLoadDateTime(Constants.PlanStatus.APPROVED.name(), currentOrDelagateUser.getId(), pOffset, pLimit, pOrderBy);

			} else {
			    lReturnPlanList = getImpPlanDAO().getPlansByMacroHeader(currentOrDelagateUser.getId(), Constants.PlanStatus.APPROVED.name(), Boolean.TRUE, pOffset, pLimit, pOrderBy);
			}
		    }
		} else {
		    lReturnPlanList = getImpPlanDAO().getMacroHeaderPlansByLoadDateTime(Constants.PlanStatus.APPROVED.name(), currentOrDelagateUser.getId(), pOffset, pLimit, null);
		}
	    }
	    lResponse.setData(lReturnPlanList);
	    lResponse.setCount(count);
	} catch (Exception ex) {
	    LOG.error("Unable to get Macro/Header Plan List", ex);
	    throw new WorkflowException("Unable to get Macro/Header/Include Plan List", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse markOnlineForMacroHeaderPlan(User lUser, String pPlanId) {
	JSONResponse lResponse = new JSONResponse();
	ImpPlan lPlan = getImpPlanDAO().find(pPlanId);
	if (!lUser.getId().equalsIgnoreCase(lPlan.getLeadId())) {
	    try {

		StringBuilder lErrReturn = new StringBuilder("");
		SortedSet<String> lSet = new TreeSet<String>();
		List<String> split = Arrays.asList(lPlan.getRelatedPlans().split(","));
		if (!split.isEmpty()) {
		    List<Object[]> relatedPlanDetails = getImpPlanDAO().getAllRelatedPlanDetail(lPlan.getId(), split);
		    for (Object[] plan : relatedPlanDetails) {
			String planStatus = plan[1].toString();
			if (!(Constants.PlanStatus.getOnlineAndAbove().containsKey(planStatus))) {
			    lSet.add(plan[0].toString());
			}
		    }
		}

		List<Object[]> segmentRelatedPlans = getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId());
		for (Object[] plan : segmentRelatedPlans) {
		    String planStatus = plan[1].toString();
		    if (!(Constants.PlanStatus.getOnlineAndAbove().containsKey(planStatus))) {
			lSet.add(plan[0].toString());
		    }
		}
		if (!lSet.isEmpty()) {
		    lErrReturn.append("Plans (").append(lPlan.getId()).append(") cannot be marked ONLINE yet as dependent implementation plan(s)  ").append(StringUtils.join(lSet, ", ")).append(". ");
		    lErrReturn.append(StringUtils.join(lSet, ", ")).append(" has/have not yet been marked ONLINE .");
		    DevManagerApproveActivityMessage activityMessage = new DevManagerApproveActivityMessage(lPlan, null);
		    activityMessage.setDependentPlanMessage(lErrReturn.toString());
		    activityMessage.setStatus("dependent");
		    getActivityLogDAO().save(lUser, activityMessage);
		    lResponse.setErrorMessage(lErrReturn.toString());
		    lResponse.setStatus(Boolean.FALSE);
		    return lResponse;
		}

		List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(lPlan);
		for (SystemLoad lSystemLoad : lSystemLoads) {
		    Date currentDateTime = new Date();

		    Date loadDateTime = lSystemLoad.getLoadDateTime();

		    if (!Constants.isAllowLoadAnyTime) {
			if (currentDateTime.before(loadDateTime)) {
			    lResponse.setStatus(Boolean.FALSE);
			    lResponse.setErrorMessage("Marking plan as ONLINE is not allowed as the Plan has not reached its Load date and time!");
			    return lResponse;
			}
		    }
		}
		/**
		 * ZTPFM-2014 Activity Log added UTILJCLM called as dlmcrnms.mac is present in
		 * implementation plan
		 */
		String macroHeaderFileName = "macro/dlmcrnms.mac";
		List<CheckoutSegments> lCheckoutSeg = getCheckoutSegmentsDAO().getMacroSegemntsByPlanId(lPlan.getId(), macroHeaderFileName);
		if (!lCheckoutSeg.isEmpty()) {
		    MacroHeaderOnlineMessage activityMessage = new MacroHeaderOnlineMessage(lPlan, null);
		    getActivityLogDAO().save(lUser, activityMessage);
		}

		getFallbackHelper().onlineJenkinsJob(lUser, lPlan);
		if (lPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name())) {
		    rejectHelper.rejectDependentPlans(lUser, lPlan.getId(), Constants.REJECT_REASON.ONLINE.getValue(), Constants.AUTOREJECT_COMMENT.ONLINE.getValue(), true, Boolean.TRUE, Boolean.FALSE, null, Boolean.FALSE);
		    // Get List of Unsecured dependent plans and send source contention mail
		    List<Object[]> autoDependentPlanList = impPlanDAO.getPostSegmentRelatedPlans(lPlan.getId(), Boolean.FALSE);
		    SortedSet<String> planIds = new TreeSet();
		    for (Object[] plan : autoDependentPlanList) {
			if (Constants.PlanStatus.ACTIVE.name().equalsIgnoreCase(plan[1].toString())) {
			    planIds.add(plan[2].toString());
			}
		    }
		    for (String plan : planIds) {
			ImpPlan depImpPlan = impPlanDAO.find(plan);
			ExceptionSourceContentionMail sourceContentionMail = (ExceptionSourceContentionMail) mailMessageFactory.getTemplate(ExceptionSourceContentionMail.class);
			sourceContentionMail.setOnlinePlan(lPlan.getId());
			sourceContentionMail.setDependentPlan(depImpPlan.getId());
			sourceContentionMail.setLeadId(depImpPlan.getLeadId());
			mailMessageFactory.push(sourceContentionMail);
		    }
		}
	    } catch (Exception ex) {
		LOG.error("Unable to mark the implementation plan as ONLINE for Macro/Header type ", ex);
		throw new WorkflowException("Unable to Mark the Plan as ONLINE", ex);
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} else {
	    // Activity Log
	    String devLead = getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId()).getDisplayName();
	    String devManager = getLDAPAuthenticatorImpl().getUserDetails(lUser.getId()).getDisplayName();
	    DevManagerApproveActivityMessage activityMessage = new DevManagerApproveActivityMessage(lPlan, null);
	    activityMessage.setDevLead(devLead);
	    activityMessage.setStatus("failed");
	    activityMessage.setDevManger(devManager);
	    getActivityLogDAO().save(lUser, activityMessage);
	    lResponse.setStatus(Boolean.FALSE);
	    LOG.error("Approval Failed: Application Developer Lead and Approving manager cannot be the same person. ");
	    lResponse.setErrorMessage("Approval Failed: Application Developer Lead and Approving manager cannot be the same person. " + "Please re-delegate to a different Dev Manager for approval or contact tool admin for assistance");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse macroHeaderReject(User lUser, String pPlanId, String rejectReason) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    getRejectHelper().rejectPlan(lUser, pPlanId, rejectReason, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to reject the plan", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getFallBackMacroHeaderPlan(User pCurrentUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setMetaData(new Date());
	User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
	lResponse.setData(getImpPlanDAO().findFallBackMacroHeaderPlan(currentOrDelagateUser.getId(), pOffset, pLimit));
	lResponse.setCount(getImpPlanDAO().countFallBackMacroHeaderPlan(currentOrDelagateUser.getId()));
	return lResponse;
    }

    /*
     * Created By :Ramkumar Seenivasan Date:08/09/2019 JIRA :2224 Exporting Data For
     * Project NBR Display Screen
     */
    @Transactional
    public JSONResponse exportDTNReport(User lUser, Integer offset, Integer limit, String filter, LinkedHashMap lOrderBy, String searchField) {
	JSONResponse lReturn = new JSONResponse();
	SearchExcelCreator creator = new SearchExcelCreator();
	List<Project> dTNReportList = getProjectDAO().findFiltered(filter, offset, limit, lOrderBy, searchField, Boolean.TRUE);
	try {
	    creator.generateDTNReport(dTNReportList);
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    creator.getWorkBook().write(lExcelStream);
	    creator.getWorkBook().close();
	    lReturn.setData(lExcelStream.toByteArray());
	    lExcelStream.close();
	    lReturn.setMetaData("application/DTNReport.ms-excel");
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lReturn.setErrorMessage("Error in Downloading Report");
	    lReturn.setStatus(Boolean.TRUE);
	}

	return lReturn;
    }

    @Transactional
    public JSONResponse getAssignedPlansAndSysLoad(User pCurrentUser, Integer offset, Integer limit, HashMap<String, String> lOrderBy, String planId, String planStatus) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	int start = limit * offset;
	User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("devManager", currentOrDelagateUser.getId());
	if (planId != null && !planId.trim().isEmpty()) {
	    pFilter.put("id", "%" + planId + "%");
	}
	if (planStatus != null && !planStatus.trim().isEmpty()) {
	    pFilter.put("planStatus", planStatus);
	}
	Long count = getImpPlanDAO().count(pFilter);
	List<ImpPlan> lImpPlanList = new ArrayList();
	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : lOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime")) {
		    lImpPlanList = getImpPlanDAO().getPlansByLoadDateTime(currentOrDelagateUser.getId(), offset, limit, planId, planStatus, lOrderBy);
		} else {
		    lImpPlanList = getImpPlanDAO().findAll(pFilter, offset, limit, new LinkedHashMap(lOrderBy));
		}
	    }
	} else {
	    lImpPlanList = getImpPlanDAO().getPlansByLoadDateTime(currentOrDelagateUser.getId(), offset, limit, planId, planStatus, null);
	}
	if (lImpPlanList.size() == 0) {
	    lResponse.setCount(0);
	    lResponse.setErrorMessage("No Plans Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}

	Set<String> planIDs = lImpPlanList.stream().map(ImpPlan::getId).collect(Collectors.toSet());

	String[] planStrings = new String[planIDs.size()];
	planIDs.toArray(planStrings);
	List<SystemLoadDTO> systemLoadDTOs = getCommonBaseService().getSystemLoadDetails(planStrings);

	Map<String, List<SystemLoadDTO>> systemLoadMap = systemLoadDTOs.stream().collect(Collectors.groupingBy(T -> T.getSystemLoad().getPlanId().getId()));

	List<AssigendPlanView> finalImpPlaninboxList = new ArrayList<AssigendPlanView>();

	lImpPlanList.forEach(plan -> {
	    AssigendPlanView assigendPlanView = new AssigendPlanView();
	    assigendPlanView.setPlan(plan);
	    if (systemLoadMap.containsKey(plan.getId())) {
		assigendPlanView.setSystemLoadDetails(systemLoadMap.get(plan.getId()));
	    }
	    finalImpPlaninboxList.add(assigendPlanView);
	});

	// List<AssigendPlanView> sortedPlansForms =
	// getSortedList(finalImpPlaninboxList, lOrderBy);
	LinkedHashMap<String, AssigendPlanView> planViewMap = new LinkedHashMap<>();
	finalImpPlaninboxList.stream().forEach(planView -> {
	    String planID = planView.getPlanID();
	    if (!planViewMap.containsKey(planID)) {
		planViewMap.put(planID, planView);
	    }
	});

	int countForMap = 0;
	Map<String, AssigendPlanView> finalPlanViewMap = new LinkedHashMap<>();
	for (Map.Entry<String, AssigendPlanView> entry : planViewMap.entrySet()) {
	    countForMap++;
	    finalPlanViewMap.put(String.format("%010d", countForMap) + "_" + entry.getKey(), entry.getValue());
	}

	lResponse.setData(finalPlanViewMap);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setCount(count);
	return lResponse;
    }

    private List<AssigendPlanView> getSortedList(List<AssigendPlanView> finalImpPlaninboxList, HashMap<String, String> pOrderBy) {
	List<AssigendPlanView> sortedPlansForms = new ArrayList<>();
	if (pOrderBy == null || pOrderBy.isEmpty()) {
	    sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getMinLoadDateTime)).collect(Collectors.toList());
	} else if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		if ("id".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getPlanID)).collect(Collectors.toList());
		    } else {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getPlanID).reversed()).collect(Collectors.toList());
		    }
		} else if ("planDesc".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getPlanDesc)).collect(Collectors.toList());
		    } else {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getPlanDesc).reversed()).collect(Collectors.toList());
		    }
		} else if ("loaddatetime".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getMinLoadDateTime)).collect(Collectors.toList());
		    } else {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getMinLoadDateTime).reversed()).collect(Collectors.toList());
		    }
		} else if ("planDesc".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getPlanDesc)).collect(Collectors.toList());
		    } else {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getPlanDesc).reversed()).collect(Collectors.toList());
		    }
		} else if ("createdBy".equalsIgnoreCase(key)) {
		    if (value.equalsIgnoreCase("asc")) {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getCreatedBy)).collect(Collectors.toList());
		    } else {
			sortedPlansForms = finalImpPlaninboxList.stream().sorted(Comparator.comparing(AssigendPlanView::getCreatedBy).reversed()).collect(Collectors.toList());
		    }
		}
	    }
	}
	return sortedPlansForms.isEmpty() ? finalImpPlaninboxList : sortedPlansForms;
    }

    @Transactional
    public JSONResponse checkNewFileCreateFlag(String repoName) {
	JSONResponse lJSONResponse = new JSONResponse();
	try {
	    RepositoryDetails lRepoDetails = getRepoDetailsDAO().findByTrimeName(repoName);
	    lJSONResponse.setData(lRepoDetails.getDefaultFileCreate());
	    lJSONResponse.setStatus(true);
	} catch (Exception ex) {
	    LOG.error("Unable to get Repo Details " + repoName, ex);
	    throw new WorkflowException("Unable to get Repo Details " + repoName, ex);
	}
	return lJSONResponse;
    }

}
