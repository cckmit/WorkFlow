/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static com.tsi.workflow.utils.Constants.VPARSEnvironment.PRE_PROD;
import static com.tsi.workflow.utils.Constants.VPARSEnvironment.QA_FUCTIONAL;
import static com.tsi.workflow.utils.Constants.VPARSEnvironment.QA_REGRESSION;

import com.google.gson.Gson;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.DeletePlanActivityMessage;
import com.tsi.workflow.activity.DevManagerActivityMessage;
import com.tsi.workflow.activity.ExceptionOccurYodaActivityMessage;
import com.tsi.workflow.activity.PlanCreationActivityMessage;
import com.tsi.workflow.activity.YodaResponseActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.beans.dao.ActivityLog;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PdddsLibrary;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.DependenciesForm;
import com.tsi.workflow.beans.ui.ImpPlanForm;
import com.tsi.workflow.beans.ui.ImpPlanView;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PdddsLibraryDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.CSRNumberDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.DateAuditCrossCheck;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.ExceptionLoadNotificationHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.ImplementationHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.mail.CompilerValidationMail;
import com.tsi.workflow.mail.DeletePlanMail;
import com.tsi.workflow.mail.DevManagerAssignmentMail;
import com.tsi.workflow.mail.YodaExceutionFaildMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.SequenceGenerator;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
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
import java.util.stream.Collectors;
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
public class DeveloperLeadService extends BaseService {

    private static final Logger LOG = Logger.getLogger(DeveloperLeadService.class.getName());

    @Autowired
    CSRNumberDAO cSRNumberDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    ProjectDAO projectDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SequenceGenerator lGenerator;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    BPMClientUtils lBPMClientUtils;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    LDAPAuthenticatorImpl authenticator;
    @Autowired
    LdapGroupConfig ldapGroupConfig;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    DbcrHelper dbcrHelper;
    @Autowired
    DbcrDAO dbcrDAO;
    @Autowired
    ExceptionLoadNotificationHelper exceptionLoadNotificationHelper;
    @Autowired
    DateAuditCrossCheck dateAuditCrossCheck;
    // Should be local one, on Cache implementation
    // we need to Set Keys in Cache and
    // need to change the isSubmitInprogress API from Cache
    @Autowired
    @Qualifier("lPlanUpdateStatusMap")
    ConcurrentHashMap<String, User> lPlanUpdateStatusMap;

    @Autowired
    ImplementationHelper implementationHelper;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    PdddsLibraryDAO pdddsLibraryDAO;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    VparsDAO vparsDAO;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    PreProductionLoadsDAO preProdLoadDAO;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    TestSystemLoadDAO testSystemLoadDAO;
    @Autowired
    CommonBaseService commonBaseService;

    public CommonBaseService getCommonBaseService() {
	return commonBaseService;
    }

    public CommonHelper getCommonHelper() {
	return commonHelper;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public PdddsLibraryDAO getPdddsLibraryDAO() {
	return pdddsLibraryDAO;
    }

    public CSRNumberDAO getCSRNumberDAO() {
	return cSRNumberDAO;
    }

    public ProjectDAO getProjectDAO() {
	return projectDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public DbcrDAO getDbcrDAO() {
	return dbcrDAO;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public SequenceGenerator getSequenceGenerator() {
	return lGenerator;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public BPMClientUtils getBPMClientUtils() {
	return lBPMClientUtils;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public DbcrHelper getDbcrHelper() {
	return dbcrHelper;
    }

    public ExceptionLoadNotificationHelper getExceptionLoadNotificationHelper() {
	return exceptionLoadNotificationHelper;
    }

    public DateAuditCrossCheck getDateAuditCrossCheck() {
	return dateAuditCrossCheck;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public ImplementationHelper getImplementationHelper() {
	return implementationHelper;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public PreProductionLoadsDAO getPreProductionLoadDAO() {
	return preProdLoadDAO;
    }

    public TestSystemLoadDAO getTestSystemLoadDAO() {
	return testSystemLoadDAO;
    }

    @Transactional
    public JSONResponse savePlan(User pUser, ImpPlan pPlan) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    User currentOrDelagateUser = pUser.getCurrentOrDelagateUser();
	    // TODO: Check the dependent Plan Load Dates
	    List<String> lDependentPlanLists = new ArrayList<>();
	    List<String> invalidRelatedPlans = new ArrayList<>();
	    if (pPlan.getRelatedPlans() != null && !pPlan.getRelatedPlans().isEmpty()) {
		lDependentPlanLists = Arrays.asList(pPlan.getRelatedPlans().split(","));
	    }
	    List<SystemLoad> lLoadLists = pPlan.getSystemLoadList();
	    for (SystemLoad lLoadList : lLoadLists) {
		if (lLoadList.getPutLevelId() == null) {
		    throw new WorkflowException(lLoadList.getSystemId().getName() + " doesn't have Putlevel");
		}
	    }

	    // 1980 - R Category Validation
	    JSONResponse lTResponse = getPlanHelper().planRCategoryValidation(lLoadLists);
	    if (!lTResponse.getStatus()) {
		throw new WorkflowException(lTResponse.getErrorMessage());
	    }

	    for (SystemLoad lLoadList : lLoadLists) {
		if (lLoadList.getLoadCategoryId() == null) {
		    throw new WorkflowException("select loadCategory for this  system " + lLoadList.getSystemId().getName());
		}
	    }
	    if (!lDependentPlanLists.isEmpty()) {
		for (SystemLoad lLoadList : lLoadLists) {
		    invalidRelatedPlans.addAll(getImpPlanDAO().getInvalidRelatedPlans(lDependentPlanLists, lLoadList.getSystemId(), lLoadList.getLoadDateTime()));
		}
	    }
	    if (!invalidRelatedPlans.isEmpty()) {
		lResponse.setErrorMessage("Load date should be later than " + invalidRelatedPlans);
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    // TODO: Call BPM Create
	    String lProcessId = getBPMClientUtils().createADLProcess(pUser);
	    if (lProcessId != null && !lProcessId.isEmpty()) {
		// List<SystemLoad> lLoadLists = pPlan.getSystemLoadList();
		Platform lPlatform = lLoadLists.get(0).getSystemId().getPlatformId();
		char lSystemChar = lPlatform.getName().charAt(0);
		String plan_id = getSequenceGenerator().getNewImplementationPlanId("" + lSystemChar);
		// TODO: Call Assign BPM
		List<TaskVariable> lVars = new ArrayList<>();
		lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, plan_id));
		Boolean status = getBPMClientUtils().assignTask(pUser, lProcessId, currentOrDelagateUser.getId(), lVars);
		if (!status) {
		    throw new WorkflowException("Unable to Create New Implementation Plan due to BPM assign task error");
		}
		pPlan.setId(plan_id);
		pPlan.setLeadId(currentOrDelagateUser.getId());
		pPlan.setLeadName(currentOrDelagateUser.getDisplayName());
		pPlan.setLeadEmail(currentOrDelagateUser.getMailId());
		pPlan.setProcessId(lProcessId);
		pPlan.setLoadDateMailFlag(Boolean.FALSE);
		pPlan.setLoadDateMacroMailFlag(Boolean.FALSE);
		pPlan.setDeactivateTSDMailFlag(Boolean.FALSE);
		pPlan.setIsAcceptEnabled(Boolean.FALSE);
		pPlan.setApproveMailFlag(Boolean.FALSE);
		pPlan.setDeployedMailFlag(Boolean.FALSE);
		pPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
		pPlan.setDeploymentStatus(Constants.PlanInProgressStatus.NONE.name());
		pPlan.setSystemLoadList(null);
		getImpPlanDAO().save(pUser, pPlan);
		for (SystemLoad lLoadList : lLoadLists) {
		    lLoadList.setPlanId(pPlan);
		    lLoadList.setLoadDateMailFlag(Boolean.FALSE);
		    if (lLoadList.getQaFunctionalTesters() != null) {
			List<String> qaFunctionalTesterList = Arrays.asList(lLoadList.getQaFunctionalTesters().split(","));
			List<String> qaFunctionalNamesIds = new ArrayList<>();
			for (String qaFunctional : qaFunctionalTesterList) {
			    User qaUser = getLDAPAuthenticatorImpl().getUserDetails(qaFunctional);
			    if (qaUser != null && qaUser.getDisplayName() != null) {
				qaFunctionalNamesIds.add(qaUser.getDisplayName());
			    }
			}
			lLoadList.setQaFunctionalTesterName(String.join(",", qaFunctionalNamesIds));
		    }
		    getSystemLoadDAO().save(pUser, lLoadList);
		    if (lLoadList.getSystemId().getDbcrList() != null) {
			List<Dbcr> ldbcrs = lLoadList.getSystemId().getDbcrList();
			for (Dbcr ldbcr : ldbcrs) {
			    ldbcr.setPlanId(pPlan);
			    getDbcrDAO().save(pUser, ldbcr);
			}
		    }
		}
		PlanCreationActivityMessage planCreationActivityMessage = new PlanCreationActivityMessage(pPlan, null);
		planCreationActivityMessage.setCreate(true);
		planCreationActivityMessage.setSystemLoads(lLoadLists);
		getActivityLogDAO().save(pUser, planCreationActivityMessage);

		DevManagerAssignmentMail devManagerAssignmentMail = (DevManagerAssignmentMail) getMailMessageFactory().getTemplate(DevManagerAssignmentMail.class);
		devManagerAssignmentMail.setDevManagerName(pPlan.getDevManagerName());
		devManagerAssignmentMail.setLeadName(pPlan.getLeadName());
		devManagerAssignmentMail.setPlanId(pPlan.getId());
		devManagerAssignmentMail.setAssignment(true);
		devManagerAssignmentMail.addToAddressUserId(currentOrDelagateUser.getId(), Constants.MailSenderRole.LEAD);
		devManagerAssignmentMail.addToAddressUserId(pPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);
		getMailMessageFactory().push(devManagerAssignmentMail);

		if (!pPlan.getAuxtype() && !pPlan.getMacroHeader()) {
		    // ZTPFM-1455 QA Functional Testers Mail Send
		    List<SystemLoad> lDbLoadList = getSystemLoadDAO().findByImpPlan(pPlan.getId());
		    Project lProject = getProjectDAO().find(pPlan.getProjectId().getId());
		    pPlan.setSystemLoadList(lDbLoadList);
		    pPlan.setProjectId(lProject);
		    getPlanHelper().sendMailNotificationQAFunTester(pPlan, true);
		}
		// TODO: GIT REPO Create
		String lSourceRepositoryName = getJGitClientUtils().getPlanRepoFullName(lPlatform.getNickName(), pPlan.getId());
		String lDerivedRepositoryName = getJGitClientUtils().getPlanLFSRepoFullName(lPlatform.getNickName(), pPlan.getId());
		SortedSet<String> lOwners = new TreeSet<>();
		lOwners.add(pPlan.getLeadId());
		getGitBlitClientUtils().createGitRepository(lSourceRepositoryName, pPlan.getPlanDesc(), lOwners);
		// getGitBlitClientUtils().setPermissionForGitRepository(lSourceRepositoryName,
		// pUser.getId(),
		// Constants.GIT_PERMISSION_READWRITE);
		getGitBlitClientUtils().createGitRepository(lDerivedRepositoryName, pPlan.getPlanDesc(), lOwners);
		// getGitBlitClientUtils().setPermissionForGitRepository(lDerivedRepositoryName,
		// pUser.getId(),
		// Constants.GIT_PERMISSION_READWRITE);
		getGitHelper().updateImplementationPlanRepoPermissions(pPlan.getId(), null);

		lResponse.setStatus(Boolean.TRUE);
	    } else {
		throw new WorkflowException("Unable to Create New Implementation Plan due to BPM process creation error");
	    }
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to Create New Implementation Plan", ex);
	}
	lResponse.setMetaData(pPlan.getId());
	return lResponse;
    }

    // @Transactional
    // public JSONResponse passAcceptanceTesting(User pUser, ImpPlan pPlan) {
    // //doubt not called
    // JSONResponse lResponse = new JSONResponse();
    // try {
    // String lOldPlanStatus = pPlan.getPlanStatus();
    // List<TaskVariable> lTaskVars = new ArrayList<>();
    // lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID,
    // pPlan.getId()));
    // getBPMClientUtils().updateProcessVariables(pUser, pPlan.getProcessId(),
    // lTaskVars);
    // getBPMClientUtils().setTaskAsCompleted(pUser, pPlan.getProcessId());
    // String lLoadsControlGroupName =
    // getLdapGroupConfig().getLoadsControlGroups().get(0).getLdapGroupName();
    // getBPMClientUtils().assignTaskToGroup(pUser, pPlan.getProcessId(),
    // lLoadsControlGroupName, lTaskVars);
    //
    // String lPlatform =
    // pPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
    // List<String> lBranchList = getJGitClientUtils().getAllBranchList(lPlatform,
    // pPlan.getId().toLowerCase());
    // String lRepoName = getJGitClientUtils().getPlanRepoName(lPlatform,
    // pPlan.getId().toLowerCase());
    //
    // getGITSSHUtils().addImplementationTag(pUser, lRepoName,
    // Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING,
    // lBranchList);
    //
    // pPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());
    // getImpPlanDAO().update(pUser, pPlan);
    //
    // List<ImpPlan> lDependentPlanList =
    // getImpPlanDAO().findDependentPlans(pPlan.getId());
    //
    // if (!lDependentPlanList.isEmpty()) {
    // StatusChangeToDependentPlanMail statusChangeToDependentPlanMail =
    // (StatusChangeToDependentPlanMail)
    // mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
    //
    // HashSet<String> lMailIdList = new HashSet();
    // statusChangeToDependentPlanMail.setToAddressList(new
    // ArrayList<>(lMailIdList));
    // lDependentPlanList.stream().forEach((lImpPlan) -> {
    // lMailIdList.add(authenticator.getUserDetails(lImpPlan.getLeadId()).getMailId());
    // });
    //
    // statusChangeToDependentPlanMail.setImpPlanId(pPlan.getId());
    // statusChangeToDependentPlanMail.setOldStatus(lOldPlanStatus);
    // statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name());
    // mailMessageFactory.push(statusChangeToDependentPlanMail);
    // }
    // lResponse.setStatus(Boolean.TRUE);
    // } catch (Exception ex) {
    // LOG.error("Error on Execution : ", ex);
    // throw new WorkflowException("Unable to mark the Acceptance Testing", ex);
    // }
    // return lResponse;
    // }
    // @Transactional
    // public JSONResponse getUserTaskList(User pCurrentUser, Integer offset,
    // Integer limit, HashMap<String, String>
    // lOrderBy) throws WorkflowException {
    // //TODO Need to remove method, not in use
    // JSONResponse lResponse = new JSONResponse();
    // List<ImpPlan> lPlanList = new ArrayList();
    // try {
    // String lSortKey = "createTime";
    // String lSortValue = "desc";
    // if (lOrderBy != null) {
    // for (Map.Entry<String, String> entrySet : lOrderBy.entrySet()) {
    // lSortKey = entrySet.getKey();
    // lSortValue = entrySet.getValue();
    // }
    // }
    // List<String> lPlanIdList = new ArrayList<>();
    // User currentOrDelagateUser = pCurrentUser.getCurrentOrDelagateUser();
    // TaskReponse taskReponse = getBPMClientUtils().getTaskList(pCurrentUser,
    // currentOrDelagateUser.getId(),
    // Constants.BPM_IMPLEMENTATION_PLAN_ID,
    // (offset * limit), limit, lSortKey, lSortValue);
    //
    // taskReponse.getData().stream().map((lTaskVariable) ->
    // lTaskVariable.getVariables()).forEach((List<TaskVariable>
    // variables) -> {
    // variables.stream().filter((variable) ->
    // (variable.getName().equalsIgnoreCase(Constants.BPM_IMPLEMENTATION_PLAN_ID))).forEach((variable)
    // -> {
    // lPlanIdList.add(variable.getValue().toString());
    // });
    // });
    //
    // if (!lPlanIdList.isEmpty()) {
    // lPlanList = getImpPlanDAO().find(lPlanIdList);
    // lResponse.setData(lPlanList);
    // }
    // lResponse.setCount(taskReponse.getTotal());
    // } catch (Exception ex) {
    // LOG.error("Error on Execution : ", ex);
    // throw new WorkflowException("Unable to get list of Task", ex);
    // }
    // lResponse.setStatus(Boolean.TRUE);
    // return lResponse;
    // }
    @Transactional
    public JSONResponse planSubmit(User lUser, ImpPlan lImpPlan) {
	JSONResponse lJSONResponse = new JSONResponse();
	if (cacheClient.getPlanUpdateStatusMap().containsKey(lImpPlan.getId())) {
	    lJSONResponse.setStatus(Boolean.FALSE);
	    lJSONResponse.setErrorMessage("Plan - " + lImpPlan.getId() + " is already in progress for submission, please wait for completion of submission process");
	    return lJSONResponse;
	}

	cacheClient.getPlanUpdateStatusMap().put(lImpPlan.getId(), lUser);
	cacheClient.getSocketUserMap().put(lImpPlan.getId(), lUser.getId());
	ImpPlan impPlan = null;
	boolean isAuditCompleted = Boolean.TRUE;
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lImpPlan.getId());
	try {
	    // ZTPFM-2271 If in progress status is REJECT. Then plan update shoundn't be
	    // allowed.
	    ImpPlan lPlan = getImpPlanDAO().find(lImpPlan.getId());
	    List<PreProductionLoads> lPreProdList = getPreProductionLoadDAO().findByPlanId(lImpPlan.getId());
	    if ((lPreProdList != null && !lPreProdList.isEmpty()) && (lPlan.getInprogressStatus() != null || lPlan.getInprogressStatus() != null) && (lPlan.getInprogressStatus().equals(Constants.PlanInProgressStatus.REJECT.name()) || lPlan.getInprogressStatus().equals(Constants.PlanInProgressStatus.REJECT.name()))) {
		throw new WorkflowException("Rejection is In-progress. Please try again after sometime.");
	    }

	    /**
	     * Validations Block
	     */
	    // 1980 - R Category Validation
	    JSONResponse lTResponse = getPlanHelper().planRCategoryValidation(lImpPlan.getId());
	    if (!lTResponse.getStatus()) {
		throw new WorkflowException(lTResponse.getErrorMessage());
	    }

	    // 2330 Validation to restrict submit if lead Plan is in progress
	    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(lImpPlan.getId())) {
		String leadPlanId = cacheClient.getInProgressRelatedPlanMap().get(lImpPlan.getId()).getPlanId();
		throw new WorkflowException(lImpPlan.getId() + " Plan cannot be submitted. Lead plan " + leadPlanId + " rejection process is in progress.");
	    }

	    if (!lImpPlan.getMacroHeader()) {
		List<Build> lBuilds = getBuildDAO().findAll(lImpPlan, Constants.BUILD_TYPE.DVL_LOAD);
		List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(lImpPlan.getId());
		if (lBuilds == null || lBuilds.isEmpty()) {
		    for (SystemLoad lSystemLoad : lSystemLoads) {
			throw new WorkflowException("DEVL Loadset(s) for  " + lSystemLoad.getSystemId().getName() + "not found.  DEVL build and loadset creation must be done before SUBMIT");
		    }
		}
		for (Build lBuild : lBuilds) {
		    if (!lBuild.getJobStatus().equals("S")) {
			throw new WorkflowException("Unable to Submit the Implementation Plan as there are NO DEVL Loader Found for the system " + lBuild.getSystemId().getName());
		    }
		}
	    }
	    LOG.info("Plan Status --> " + lPlan.getPlanStatus());
	    if (lPlan != null && !lPlan.getPlanStatus().equals(Constants.PlanStatus.ACTIVE.name())) {
		throw new WorkflowException("Implementation plan - " + lImpPlan.getId() + " is already in secured state.");
	    }

	    if (lImpPlan.getDevManager() == null || lImpPlan.getDevManager().isEmpty()) {
		throw new WorkflowException("Dev Manager is not updated in the implementation plan - " + lImpPlan);
	    }

	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		JSONResponse lPutResponse = getPlanHelper().putLevelSegmentValidation(lSystemLoad, lImpPlan.getId());
		if (!lPutResponse.getStatus()) {
		    CommonActivityMessage lMessage = new CommonActivityMessage(lImpPlan, null);
		    lMessage.setMessage(lPutResponse.getErrorMessage());
		    lMessage.setStatus("Fail");
		    getActivityLogDAO().save(lUser, lMessage);
		    throw new WorkflowException(lPutResponse.getErrorMessage());
		}
	    }

	    getImpPlanDAO().update(lUser, lImpPlan);

	    for (SystemLoad lSystemLoad : lImpPlan.getSystemLoadList()) {
		getSystemLoadDAO().update(lUser, lSystemLoad);
		DevManagerActivityMessage lMessage = new DevManagerActivityMessage(lImpPlan, null, lSystemLoad.getSystemId());
		// Need to check
		lMessage.setQaFunctionalComment(lSystemLoad.getQaFunctionalBypassComment());
		lMessage.setQaRegressionComment(lSystemLoad.getQaRegressionBypassComment());
		getActivityLogDAO().save(lUser, lMessage);
	    }
	    // QA Functional Tester's Validation
	    List<String> systems = new ArrayList<>();
	    if (!lImpPlan.getMacroHeader()) {
		lImpPlan.getSystemLoadList().stream().filter((lSystemLoad) -> (lSystemLoad.getQaFunctionalTesters() == null && ((lSystemLoad.getQaBypassStatus().equals(Constants.SYSTEM_QA_TESTING_STATUS.NONE.name())) || (lSystemLoad.getQaBypassStatus().equals(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name()))))).forEachOrdered((lSystemLoad) -> {
		    systems.add(lSystemLoad.getSystemId().getName());
		});
		if (!systems.isEmpty()) {
		    throw new WorkflowException("Select QA Functional Tester(s) for " + String.join(",", systems));
		}
	    }

	    JobDetails lwssMessageDateAudit = new JobDetails();
	    lwssMessageDateAudit.setStatus(lImpPlan.getId() + ": Date Audit has started");
	    wsserver.sendMessage(Constants.Channels.DATE_AUDIT, lUser.getId(), lImpPlan.getId(), lwssMessageDateAudit);

	    impPlan = getImpPlanDAO().find(lImpPlan.getId());
	    List<Implementation> lImpList = getImplementationDAO().findByImpPlan(impPlan.getId());
	    /**
	     * Date Audit for New Process
	     */
	    String lAudit = "";
	    if (impPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name())) {
		lAudit = getImpPlanDAO().doPlanAudit(impPlan.getId(), Constants.PlanStatus.APPROVED.name());
		if (!lAudit.isEmpty()) {
		    isAuditCompleted = Boolean.FALSE;
		    throw new WorkflowException(lAudit);
		}
	    }

	    // Validation: if parent and child load date and time are equal then return
	    // error
	    // Manual Dependent Plans validation
	    Set<String> lErrPlanList = new HashSet();
	    List<String> lManPlanList = Arrays.asList(impPlan.getRelatedPlans().split(","));
	    List<DependenciesForm> lErrManPlanList = getImpPlanDAO().getAllManualPlanForEqualLoadDate(impPlan.getId(), lManPlanList);
	    if (!lErrManPlanList.isEmpty()) {
		lErrManPlanList.stream().forEach(t -> lErrPlanList.add(t.getPlanid() + "/" + t.getTargetsystem()));
	    }
	    List<Object[]> lEqPlanList = getImpPlanDAO().getEqualDateSegmentRelatedPlans(impPlan.getId());
	    if (!lEqPlanList.isEmpty()) {
		lEqPlanList.stream().forEach(t -> lErrPlanList.add(t[0].toString()));
	    }

	    if (!lErrPlanList.isEmpty()) {
		throw new WorkflowException(impPlan.getId() + " is dependent on plan(s) " + String.join(",", lErrPlanList) + " and have same load date and time. Kindly reschedule your plan accordingly");
	    }
	    /**
	     * Date Audit for Old Process
	     */
	    if (impPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name())) {
		String valMessage = getDateAuditCrossCheck().dateAutditForMigration(lUser, impPlan);
		if (!valMessage.equals("PASSED")) {
		    isAuditCompleted = Boolean.FALSE;
		    LOG.error("Date Audit for Old Process fails for Plan - " + impPlan.getId());
		    throw new WorkflowException("Plan submission failed: Load date is earlier than load date of non moderization Implementation Plan(s) -  " + valMessage);
		}
	    }

	    /**
	     * Compiler Table Validation
	     */
	    List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(impPlan);
	    if (lSystemLoads != null) {
		Date lCurrentDate = new Date();
		for (SystemLoad lSystemLoad : lSystemLoads) {

		    if (lSystemLoad.getLoadDateTime().before(lCurrentDate)) {
			isAuditCompleted = Boolean.FALSE;
			throw new WorkflowException("Load Date of target system " + lSystemLoad.getSystemId().getName() + " should be later than current date");
		    }

		    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findBySystemLoad(lSystemLoad.getId());
		    if (lSegmentList == null || lSegmentList.isEmpty()) {
			continue;
		    }

		    JSONResponse lCommandResponse = new JSONResponse();

		    String lCommand = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + lImpList.get(0).getId().toLowerCase() + "_" + lSystemLoad.getSystemId().getName().toLowerCase();
		    lCommandResponse = getsSHClientUtils().executeCommand(lSystemLoad.getSystemId(), lCommand);

		    if (!lCommandResponse.getStatus()) {
			if (lCommandResponse.getErrorMessage().contains("Warning")) {
			    CompilerValidationMail lCompilerValidationMail = (CompilerValidationMail) mailMessageFactory.getTemplate(CompilerValidationMail.class);
			    lCompilerValidationMail.setSystem(lSystemLoad.getSystemId().getName());
			    lCompilerValidationMail.setIpAddress(lSystemLoad.getSystemId().getIpaddress());
			    lCompilerValidationMail.addToDEVCentre();
			    mailMessageFactory.push(lCompilerValidationMail);
			    String error = lCommandResponse.getErrorMessage().replace("Error Code: 8 Warning", "").replace("Warning: ", "");
			    LOG.error(error + ".");
			    isAuditCompleted = Boolean.FALSE;
			    throw new WorkflowException(error + ".");
			} else {
			    String error = lCommandResponse.getErrorMessage().replace("Error Code: 8 ", "").replaceAll(" ", ",");
			    String message = error.contains(",") ? error.substring(0, error.length() - 1) : error;
			    LOG.error(message + " has compiler override statements that are not present in the compiler control table.");
			    isAuditCompleted = Boolean.FALSE;
			    throw new WorkflowException(message + " has compiler override statements that are not present in the compiler control table.");
			}
		    }
		}
	    }

	    boolean lReturn = getBPMClientUtils().setTaskAsCompleted(lUser, impPlan.getProcessId());

	    String lCompanyName = impPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, impPlan.getId().toLowerCase());
	    List<String> lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, impPlan.getId().toLowerCase());
	    lReturn = getsSHClientUtils().addImplementationTag(lRepoName, Constants.TagStatus.SECURED, lBranchList);
	    if (!lReturn) {
		LOG.error("Error in Submission(Tag), Plan - " + impPlan.getId());
		// throw new WorkflowException("Implementation plan - " + impPlan.getId() + "
		// has not been submitted to
		// QA");
		throw new WorkflowException("Error in Submission(Tag), Plan - " + impPlan.getId() + ". Plan not submitted to QA.");
	    }
	    // lReturn = getGitBlitClientUtils().freezeRepository(lRepoName.concat(".git"));
	    String lRepoFullName = getJGitClientUtils().getPlanRepoFullName(lCompanyName, impPlan.getId().toLowerCase());
	    lJSONResponse.setStatus(lReturn);

	    if (impPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name())) {
		exceptionLoadNotificationHelper.notifyDevelopers(impPlan.getId(), Constants.PlanStatus.SUBMITTED);
	    }

	    impPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());
	    if (impPlan.getMacroHeader()) {
		impPlan.setApproveRequestDateTime(new Date());
	    }
	    // 2330 Validation to restrict submit if lead Plan is in progress
	    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(lImpPlan.getId())) {
		String leadPlanId = cacheClient.getInProgressRelatedPlanMap().get(lImpPlan.getId()).getPlanId();
		throw new WorkflowException(lImpPlan.getId() + " Plan cannot be submitted. Lead plan " + leadPlanId + " rejection process is in progress.");
	    } else {
		getImpPlanDAO().update(lUser, impPlan);
	    }

	    if (!lAsyncPlansStartTimeMap.containsKey(impPlan.getId() + "-" + Constants.Channels.PLAN_SUBMIT.name())) {
		LOG.info("Insied tehe Async process..Key value: " + impPlan.getId() + "-" + Constants.Channels.PLAN_SUBMIT.name());
		lAsyncPlansStartTimeMap.put(impPlan.getId() + "-" + Constants.Channels.PLAN_SUBMIT.name(), java.lang.System.currentTimeMillis());
		LOG.info("Size of map: " + lAsyncPlansStartTimeMap.size());
	    }

	    lJSONResponse.setStatus(Boolean.TRUE);
	    List<String> usersToRemove = new ArrayList<>();
	    usersToRemove.add(lUser.getId());
	    getGitHelper().updateImplementationPlanRepoPermissions(impPlan.getId(), usersToRemove);
	    lPlanUpdateStatusMap.put(lImpPlan.getId(), lUser);
	    LOG.info("Return Value: " + new Gson().toJson(lJSONResponse));

	    if (!isAuditCompleted) {
		lwssMessageDateAudit.setStatus(lImpPlan.getId() + ": Date Audit has Failed");
	    } else {
		lwssMessageDateAudit.setStatus(lImpPlan.getId() + ": Date Audit has completed");
	    }
	    wsserver.sendMessage(Constants.Channels.DATE_AUDIT, lUser.getId(), lImpPlan.getId(), lwssMessageDateAudit);
	    lJSONResponse.setMetaData(lImpPlan.getId());

	} catch (WorkflowException e) {
	    lJSONResponse.setErrorMessage(e.getMessage());
	    cacheClient.getPlanUpdateStatusMap().remove(lImpPlan.getId());
	    throw e;
	} catch (Exception e) {
	    LOG.error("System error in plan submission", e);
	    lJSONResponse.setErrorMessage(e.getMessage());
	    cacheClient.getPlanUpdateStatusMap().remove(lImpPlan.getId());
	    throw new WorkflowException("System error in plan submission", e);
	}
	return lJSONResponse;
    }

    @Transactional
    public JSONResponse isSubmitReady(String pPlanId) {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	List<Implementation> lImplementationList = new ArrayList<>();
	ImpPlan impPlan = getImpPlanDAO().find(pPlanId);
	List<SystemLoad> lSysLoad = getSystemLoadDAO().findByImpPlan(impPlan);
	if (!impPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ACTIVE.name())) {
	    response.setStatus(Boolean.FALSE);
	} else {
	    lImplementationList = getImplementationDAO().findByImpPlan(pPlanId);
	    if (!lImplementationList.isEmpty()) {
		for (Implementation lImplementation : lImplementationList) {
		    if (!lImplementation.getImpStatus().equalsIgnoreCase(Constants.ImplementationStatus.READY_FOR_QA.name())) {
			response.setStatus(Boolean.FALSE);
			break;
		    }
		}
	    } else {
		response.setStatus(Boolean.FALSE);
	    }
	}
	List<Build> lBuildList = getBuildDAO().findAllWithSuccess(impPlan, Constants.BUILD_TYPE.DVL_LOAD);
	if (!impPlan.getMacroHeader() && lSysLoad.size() != lBuildList.size()) {
	    response.setStatus(Boolean.FALSE);
	}
	response.setMetaData(cacheClient.getPlanUpdateStatusMap().keySet().contains(pPlanId) ? "INPROGRESS" : "");

	return response;
    }

    @Transactional
    public JSONResponse getProjectList(User pUser, Integer pOffset, Integer pLimit, String filter, HashMap pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	LOG.info("Project Number -" + filter);
	try {
	    List<Project> lCRSList = new ArrayList<>();

	    if (filter != null && !filter.equals("")) {
		lCRSList = getCSRNumberDAO().findFiltered(filter);
	    } else {
		lCRSList = getCSRNumberDAO().findAll();
	    }
	    List<Project> lNewCSRsList = new ArrayList<>();
	    for (Project lCsrs : lCRSList) {
		List<Project> lCSR = getProjectDAO().findFiltered(lCsrs.getProjectNumber());
		if (!lCSR.isEmpty()) {
		    lNewCSRsList.add(lCSR.get(0));
		} else {
		    if (lCsrs.getLineOfBusiness() != null) {
			int indexOf = lCsrs.getLineOfBusiness().indexOf("/");
			lCsrs.setLineOfBusiness(lCsrs.getLineOfBusiness().substring(0, indexOf));
		    }
		    getProjectDAO().save(pUser, lCsrs);
		    lNewCSRsList.add(lCsrs);
		}
	    }
	    lResponse.setData(lNewCSRsList);
	} catch (Exception e) {
	    LOG.warn("Cannot read from MSSQL Database, " + e.getMessage());
	    LOG.error("Cannot read from MSSQL Database, " + e);
	    List findAll = getProjectDAO().findFiltered(filter);
	    lResponse.setData(findAll);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getDeltaProjectList(User pUser, Integer pOffset, Integer pLimit, String filter, HashMap pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	List findAll = getProjectDAO().findFiltered(filter);
	lResponse.setData(findAll);
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getActivityLogList(String pPlanId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List<ActivityLog> findAll = getActivityLogDAO().findByPlanId(pPlanId, pFilter, pOffset, pLimit, pOrderBy);
	Long count = getActivityLogDAO().countByPlanId(pPlanId, pFilter);
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    @Transactional
    public JSONResponse validateDbcr(User user, String dbcrName, String systemId) throws WorkflowException {
	return getDbcrHelper().validateDbcr(Integer.parseInt(systemId), dbcrName);
    }

    @Transactional
    public JSONResponse getDbcrList(User user, String[] planIds) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<Dbcr> dbcrList = getDbcrDAO().findByPlanId(planIds);
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(dbcrList);
	} catch (Exception ex) {
	    LOG.error("Unable to fetch Dbcr List for the plans " + Arrays.toString(planIds), ex);
	    throw new WorkflowException("Unable to fetch Dbcr List for the plans " + Arrays.toString(planIds), ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getInboxPlanList(User user, Integer pOffset, Integer pLimit, String filter, LinkedHashMap<String, String> pOrderBy, String planFilter) {
	JSONResponse lResponse = new JSONResponse();
	int start = pLimit * pOffset;
	List<ImpPlanView> lImpPlanList = new ArrayList<>();
	List<String> status = new ArrayList();
	if (filter.isEmpty()) {
	    status = new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet());
	} else {
	    // Seems Having BUG
	    status = Arrays.asList(filter);
	}
	User currentOrDelagateUser = user.getCurrentOrDelagateUser();
	List<ImpPlan> lImpPlans = new ArrayList<>();
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : pOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime")) {
		    lImpPlans = getImpPlanDAO().findPlansByLoadDateTime(status, currentOrDelagateUser.getId(), pOffset, pLimit, pOrderBy, planFilter);
		} else {
		    lImpPlans = getImpPlanDAO().findByStatusListAndOwner(status, currentOrDelagateUser.getId(), pOffset, pLimit, pOrderBy, planFilter);
		}
	    }
	} else {
	    lImpPlans = getImpPlanDAO().findByStatusListAndOwner(status, currentOrDelagateUser.getId(), pOffset, pLimit, pOrderBy, planFilter);
	}
	if (lImpPlans.size() == 0) {
	    lResponse.setCount(0);
	    lResponse.setErrorMessage("No Plans Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}
	for (ImpPlan lImpPlan : lImpPlans) {
	    ImpPlanView planView = new ImpPlanView();
	    JSONResponse submitReady = isSubmitReady(lImpPlan.getId());
	    planView.setPlan(lImpPlan);
	    planView.setIsSubmitReady(submitReady.getStatus());
	    planView.setIsInProgress(submitReady.getMetaData().toString());
	    planView.setIsDeleteAllowed(Boolean.FALSE);
	    planView.setIsDeploymentFlag(Boolean.FALSE);
	    List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImpPlan(lImpPlan.getId());
	    if (lSegments != null && lSegments.isEmpty()) {
		planView.setIsDeleteAllowed(Boolean.TRUE);
	    }
	    // ZTPFM-2275
	    if (Constants.PlanStatus.getDeploymentPlanStatus().keySet().contains(lImpPlan.getPlanStatus())) {
		planView.setIsDeploymentFlag(Boolean.TRUE);
	    }
	    Map<String, Integer> lDeleteImpStatus = new HashMap();
	    for (Implementation lImp : getImplementationDAO().findByImpPlan(lImpPlan.getId())) {
		List<CheckoutSegments> lSegmentCount = getCheckoutSegmentsDAO().findByImplementation(lImp.getId());
		if (lSegmentCount == null) {
		    lDeleteImpStatus.put(lImp.getId(), 0);
		} else {
		    lDeleteImpStatus.put(lImp.getId(), lSegmentCount.size());
		}
	    }
	    planView.setImpDeleteStatus(lDeleteImpStatus);
	    lImpPlanList.add(planView);
	}
	Long count = getImpPlanDAO().countByStatusListAndOwner(status, currentOrDelagateUser.getId(), planFilter);

	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(getCommonBaseService().getPlansAndSysLoadDetails(lImpPlanList));
	return lResponse;
    }

    @Transactional
    public JSONResponse getPlanList(User user, Integer pOffset, Integer pLimit, String filter, LinkedHashMap<String, String> pOrderBy, String planFilter) {
	JSONResponse lResponse = new JSONResponse();
	List lImpPlanList = new ArrayList<>();
	List<String> status = new ArrayList();
	if (filter.isEmpty()) {
	    status = new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet());
	} else {
	    // Seems Having BUG
	    status = Arrays.asList(filter);
	}
	User currentOrDelagateUser = user.getCurrentOrDelagateUser();
	List<ImpPlan> lImpPlans = getImpPlanDAO().findByStatusListAndOwner(status, currentOrDelagateUser.getId(), pOffset, pLimit, planFilter);
	for (ImpPlan lImpPlan : lImpPlans) {
	    ImpPlanView planView = new ImpPlanView();
	    JSONResponse submitReady = isSubmitReady(lImpPlan.getId());
	    planView.setPlan(lImpPlan);
	    planView.setIsSubmitReady(submitReady.getStatus());
	    planView.setIsInProgress(submitReady.getMetaData().toString());
	    planView.setIsDeleteAllowed(Boolean.FALSE);
	    planView.setIsDeploymentFlag(Boolean.FALSE);
	    List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImpPlan(lImpPlan.getId());
	    if (lSegments != null && lSegments.isEmpty()) {
		planView.setIsDeleteAllowed(Boolean.TRUE);
	    }
	    // ZTPFM-2275
	    if (Constants.PlanStatus.getDeploymentPlanStatus().keySet().contains(lImpPlan.getPlanStatus())) {
		planView.setIsDeploymentFlag(Boolean.TRUE);
	    }
	    Map<String, Integer> lDeleteImpStatus = new HashMap();
	    for (Implementation lImp : getImplementationDAO().findByImpPlan(lImpPlan.getId())) {
		List<CheckoutSegments> lSegmentCount = getCheckoutSegmentsDAO().findByImplementation(lImp.getId());
		if (lSegmentCount == null) {
		    lDeleteImpStatus.put(lImp.getId(), 0);
		} else {
		    lDeleteImpStatus.put(lImp.getId(), lSegmentCount.size());
		}
	    }
	    planView.setImpDeleteStatus(lDeleteImpStatus);
	    lImpPlanList.add(planView);
	}
	Long count = getImpPlanDAO().countByStatusListAndOwner(status, currentOrDelagateUser.getId(), planFilter);
	lResponse.setCount(count);

	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lImpPlanList);
	return lResponse;
    }

    @Transactional
    public JSONResponse isCheckForCSV(String pPlanId) {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	List<CheckoutSegments> segments = getCheckoutSegmentsDAO().findByImpPlan(pPlanId);
	for (CheckoutSegments segment : segments) {
	    if (segment.getFileName().contains(".csv")) {
		response.setMetaData("CSV file included, so generate TLDR and FTP manually.");
		break;
	    }
	}
	return response;
    }

    @Transactional
    public JSONResponse checkForDvlBuildInProgress(String pPlanId) {
	JSONResponse response = new JSONResponse();
	List<String> lSystemNameList = new ArrayList<>();
	List<System> systemList = new ArrayList<>();
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(pPlanId);
	for (SystemLoad pLoad : lSystemLoads) {
	    systemList.add(pLoad.getSystemId());
	}
	List<Build> buildList = getBuildDAO().findLastBuildInProgress(pPlanId, systemList, BUILD_TYPE.DVL_BUILD);
	for (Build lBuild : buildList) {
	    lSystemNameList.add(lBuild.getSystemId().getName());
	}
	if (!lSystemNameList.isEmpty()) {
	    response.setData(lSystemNameList);
	    response.setStatus(Boolean.TRUE);
	}
	return response;
    }

    @Transactional
    public JSONResponse checkForDvlBuild(String pPlanId) {
	JSONResponse response = new JSONResponse();
	Map<String, String> lBuildIconInfo = new HashMap();
	response.setStatus(Boolean.TRUE);
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(pPlanId);
	Boolean enableButton = Boolean.TRUE;
	for (SystemLoad lSystemLoad : lSystemLoads) {
	    String lErrorMessage = "";
	    List<CheckoutSegments> segments = getCheckoutSegmentsDAO().findBySystemLoad(lSystemLoad.getId());
	    if (lSystemLoad.getLoadDateTime() != null && segments.isEmpty()) {
		enableButton = Boolean.FALSE;
		lErrorMessage = "No source artifacts checked out";
	    }
	    for (CheckoutSegments segment : segments) {
		if (segment.getFileName().contains(".csv")) {
		    enableButton = Boolean.FALSE;
		    lErrorMessage = ".CSV source artifacts checked out";
		    break;
		}
	    }
	    lBuildIconInfo.put(lSystemLoad.getSystemId().getName(), lErrorMessage);
	}
	if (enableButton) {
	    enableButton = Boolean.FALSE;
	    ImpPlan lPlan = impPlanDAO.find(pPlanId);
	    if (lPlan.getPlanStatus().equals(Constants.PlanStatus.ACTIVE.name())) {
		List<Implementation> lImplementation = getImplementationDAO().findByImpPlan(pPlanId);
		for (Implementation implementation : lImplementation) {
		    if (implementation.getLastCheckinStatus() != null && implementation.getLastCheckinStatus().equalsIgnoreCase("S")) {
			enableButton = Boolean.TRUE;
		    }
		}
	    }
	}
	lBuildIconInfo.put("enableBuildButton", "N");
	if (enableButton) {
	    lBuildIconInfo.put("enableBuildButton", "Y");
	}
	response.setData(lBuildIconInfo);
	return response;
    }

    @Transactional
    public JSONResponse getPlan(String id) {
	JSONResponse lResponse = new JSONResponse();
	ImpPlan lPlan = getImpPlanDAO().find(id);
	if (lPlan != null) {
	    Set<String> lBlockedSystem = new HashSet(getCheckoutSegmentsDAO().getBlockedSystemsByPlan(id));
	    ImpPlanForm lPlanForm = new ImpPlanForm(lPlan, lBlockedSystem);
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(lPlanForm);
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Requested ID not Found");
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse deleteImplementations(User pUser, String pId) {
	return getImplementationHelper().deleteImplementation(pUser, pId);
    }

    @Transactional
    public JSONResponse deletePlan(User pUser, String pId) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	List<String> lToUsers = new ArrayList();
	try {
	    ImpPlan lPlan = getImpPlanDAO().find(pId);
	    List<Implementation> lImpList = getImplementationDAO().findByImpPlan(lPlan.getId());
	    for (Implementation lImp : lImpList) {
		lToUsers.add(lImp.getDevId());
		JSONResponse lImpResponse = getImplementationHelper().deleteImplementation(pUser, lImp.getId());
		if (!lImpResponse.getStatus()) {
		    throw new WorkflowException("Unable to delete the Plan - " + pId + " as Implementation - " + lImp.getId() + " has source artifacts");
		}
	    }

	    /**
	     * ZTPFM-2639
	     *
	     * @author vinoth.ponnurangan As a lead when I delete the implementation plan, I
	     *         should not get any error due the loadset is active in integration
	     *         VPAR and Tool must handle this situation without causing issue for
	     *         user so that user experience is improved.
	     *
	     */
	    List<SystemLoadActions> lLoadaction = getSystemLoadActionsDAO().findByPlanId(pId);
	    for (SystemLoadActions lLoadSet : lLoadaction) {
		if (lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && lLoadSet.getVparId().getType().equals(Constants.VPARSEnvironment.INTEGRATION.name())) {
		    deActivateAndDeleteLoadSetByPlan(pUser, lLoadSet);
		}
	    }

	    List<SystemLoad> lLoadList = getSystemLoadDAO().findByImpPlan(lPlan);
	    Platform lPlatform = lLoadList.get(0).getSystemId().getPlatformId();

	    List<String> lDeleteBranches = new ArrayList();
	    for (SystemLoad lSystemLoad : lLoadList) {
		lDeleteBranches.add((lPlan.getId() + "_" + lSystemLoad.getSystemId().getName()).toLowerCase());
		Date lDate = lSystemLoad.getLoadDateTime() != null ? lSystemLoad.getLoadDateTime() : new Date();

		String lCommand = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + lSystemLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lDate) + "0000").toLowerCase();
		JSONResponse lCmdResponse = getsSHClientUtils().executeCommand(lSystemLoad.getSystemId(), lCommand);
		if (!lCmdResponse.getStatus()) {
		    LOG.info("Error in executing the command for deleting the plan, error message from shell script - " + lCmdResponse.getErrorMessage());
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Unable to delete the Plan - " + pId);
		    return lResponse;
		}
	    }

	    // Delete Git Repository
	    JSONResponse lRepoDeleteResponse = getPlanHelper().deletePlanGitRepos(lPlatform.getNickName(), lPlan.getId());
	    if (!lRepoDeleteResponse.getStatus()) {
		throw new WorkflowException("Unable to Delete the Implementation Plan - " + pId + " as error occurs in deleting GIT Repository");
	    }

	    // Delete Production Repository Branches
	    if (!getJGitClientUtils().deleteProdBranches(lPlatform.getNickName(), lDeleteBranches)) {
		throw new WorkflowException("Unable to Delete the Implementation Plan - " + pId + " as error occurs in deleting Prod branches");
	    }

	    // Activity Log
	    DeletePlanActivityMessage lDeleteActiLog = new DeletePlanActivityMessage(lPlan, null);
	    lDeleteActiLog.setUser(pUser);
	    getActivityLogDAO().save(pUser, lDeleteActiLog);

	    // Send Mail
	    lToUsers.add(lPlan.getLeadId());
	    DeletePlanMail lDeletePlanMail = (DeletePlanMail) getMailMessageFactory().getTemplate(DeletePlanMail.class);
	    lDeletePlanMail.setlImp(lImpList);
	    lDeletePlanMail.setlPlan(lPlan);
	    lDeletePlanMail.setDeletedBy(pUser.getDisplayName());
	    lToUsers.stream().forEach(t -> lDeletePlanMail.addToAddressUserId(t, Constants.MailSenderRole.LEAD));
	    getMailMessageFactory().push(lDeletePlanMail);

	    // Update DB
	    lPlan.setPlanStatus(Constants.PlanStatus.DELETED.name());
	    getImpPlanDAO().update(pUser, lPlan);

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("unable to delete the plan - " + pId, ex);
	    throw new WorkflowException("Unable to Delete the Implementation Plan - " + pId);
	}
	return lResponse;
    }

    private void deActivateAndDeleteLoadSetByPlan(User pUser, SystemLoadActions lLoadSet) {
	boolean lResult = false;
	try {
	    List<YodaResult> lList = getTestSystemLoadDAO().deleteAndDeActivate(pUser, lLoadSet.getVparId().getName(), lLoadSet.getSystemLoadId().getLoadSetName());

	    if (lList != null && !lList.isEmpty()) {
		YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lLoadSet.getPlanId(), null);
		responseActivityMessage.setlYodaResult(lList.get(0));
		responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.DEACTIVATEANDDELETE);
		getActivityLogDAO().save(pUser, responseActivityMessage);
		lResult = lList.get(0).getRc() == 0;
	    }
	} catch (Exception e) {
	    LOG.error("Error in the plan  " + lLoadSet.getPlanId().getId() + "_ " + Constants.YodaActivtiyMessage.DEACTIVATEANDDELETE + " the loadset and vpars " + lLoadSet.getVparId().getName() + "_ " + lLoadSet.getSystemLoadId().getLoadSetName());

	    ExceptionOccurYodaActivityMessage exceptionOccurYodaActivityMessage = new ExceptionOccurYodaActivityMessage(lLoadSet.getPlanId(), null);
	    exceptionOccurYodaActivityMessage.setPlanId(lLoadSet.getPlanId().getId());
	    exceptionOccurYodaActivityMessage.setVparList(Arrays.asList(lLoadSet.getVparId().getName()));
	    exceptionOccurYodaActivityMessage.setLoadSetName(lLoadSet.getSystemLoadId().getLoadSetName());
	    activityLogDAO.save(pUser, exceptionOccurYodaActivityMessage);

	    YodaExceutionFaildMail yodaExceutionFaildMail = (YodaExceutionFaildMail) getMailMessageFactory().getTemplate(YodaExceutionFaildMail.class);
	    yodaExceutionFaildMail.setPlanId(lLoadSet.getPlanId().getId());
	    yodaExceutionFaildMail.setVparList(Arrays.asList(lLoadSet.getVparId().getName()));
	    yodaExceutionFaildMail.setLoadSetName(lLoadSet.getSystemLoadId().getLoadSetName());
	    if (wFConfig.getDevOpsCentreMailId() != null) {
		yodaExceutionFaildMail.addToDevOpsCentre(true);
	    }
	    getMailMessageFactory().push(yodaExceutionFaildMail);

	}
	if (!lResult) {
	    lLoadSet.setLastActionStatus("FAILED");
	} else {
	    lLoadSet.setLastActionStatus("SUCCESS");
	    lLoadSet.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	    lLoadSet.setDeActivatedDateTime(new Date());
	}
	getSystemLoadActionsDAO().update(pUser, lLoadSet);
    }

    @Transactional
    public JSONResponse getPdddsLibrary(String pSystemName) {
	JSONResponse iResponse = new JSONResponse();
	System lSystem = getSystemDAO().findByName(pSystemName);
	LOG.info("System Name" + lSystem.getName() + lSystem.getId() + "input System " + pSystemName);
	List<PdddsLibrary> pdddsLibraryList = getPdddsLibraryDAO().findBySystem(lSystem);
	iResponse.setStatus(Boolean.TRUE);
	iResponse.setData(pdddsLibraryList);
	return iResponse;
    }

    @Transactional
    public JSONResponse getDepencyPlanList(User lUser, String pPlanId, String pFilter, Integer offset, Integer limit) {
	JSONResponse lResponse = new JSONResponse();
	ImpPlan lPlan = getImpPlanDAO().find(pPlanId);
	List<DependenciesForm> lReturnPlanList = new ArrayList<>();
	try {
	    Map<String, List<DependenciesForm>> lDepPlanMap = getCommonHelper().getPlanDependencyList(Arrays.asList(pPlanId), pFilter, offset, limit);
	    List<DependenciesForm> lDepPlanList = new ArrayList<>();
	    lDepPlanMap.forEach((key, value) -> {
		lDepPlanList.addAll(value);
	    });
	    // Sort the plan by Load Date and Time
	    lReturnPlanList = lDepPlanList.stream().sorted(Comparator.comparing(DependenciesForm::getLoaddatetime)).collect(Collectors.toList());

	} catch (Exception ex) {
	    LOG.error("Unable to get the depenent Plan", ex);
	    throw new WorkflowException("Unable to get the depenent Plan", ex);
	}
	lResponse.setData(lReturnPlanList);
	lResponse.setCount(lReturnPlanList.size());
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;

    }

    @Transactional
    public JSONResponse isPrivateVpars(User user, Integer systemId, String vparsName) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<Constants.VPARSEnvironment> lVparsEnvList = new ArrayList();
	    lVparsEnvList.add(QA_FUCTIONAL);
	    lVparsEnvList.add(QA_REGRESSION);
	    lVparsEnvList.add(PRE_PROD);

	    System lSystem = getSystemDAO().find(systemId);
	    List<Vpars> lVparsList = getVparsDAO().findBySystem(lSystem, vparsName.toUpperCase(), lVparsEnvList);
	    if (lVparsList != null && !lVparsList.isEmpty()) {
		throw new WorkflowException("Test System - " + vparsName.toUpperCase() + " is not private");
	    }

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception Ex) {
	    throw new WorkflowException("Unable to process the request");
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    /**
     * ZTPFM-2533 get plan status by implementation
     *
     * @param user
     * @param impId
     * @return planStatus JSON object
     */
    @Transactional
    public JSONResponse getPlanStatusByImp(User user, String impId) {
	JSONResponse lResponse = new JSONResponse();
	String planStatus = null;
	try {
	    planStatus = getImpPlanDAO().getPlanStatusByImp(impId);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable get plan status: " + ex);
	    throw new WorkflowException("Unable get plan status");
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(planStatus);
	return lResponse;
    }

    public JSONResponse removePlanFromSubmissionCache(User user, String planId) {
	JSONResponse lreturn = new JSONResponse();
	cacheClient.getPlanUpdateStatusMap().remove(planId);
	lreturn.setStatus(Boolean.TRUE);
	return lreturn;
    }
}
