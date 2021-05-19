
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.ExceptionOccurYodaActivityMessage;
import com.tsi.workflow.activity.LoadSetActivityMessage;
import com.tsi.workflow.activity.MacroPlanDependentActivityLog;
import com.tsi.workflow.activity.RejectionActivityMessage;
import com.tsi.workflow.activity.YodaDeleteDeactivateActivityMessage;
import com.tsi.workflow.activity.YodaResponseActivityMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.ui.DependentPlanRejectDetail;
import com.tsi.workflow.beans.ui.RejectPlans;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.mail.AutoRejectMail;
import com.tsi.workflow.mail.ExceptionSourceContentionMail;
import com.tsi.workflow.mail.FallBackRejectUnSecMail;
import com.tsi.workflow.mail.LoadDatePassedMail;
import com.tsi.workflow.mail.MacroPlanDependencyRejectMail;
import com.tsi.workflow.mail.RejectMail;
import com.tsi.workflow.mail.YodaExceutionFaildMail;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.TagStatus;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class RejectHelper {

    private static final Logger LOG = Logger.getLogger(RejectHelper.class.getName());

    @Autowired
    BPMClientUtils lBPMClientUtils;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    JGitClientUtils jGitClientUtils;
    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    TestSystemLoadDAO testSystemLoadDAO;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    TOSHelper tOSHelper;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    VparsDAO vparsDAO;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    @Qualifier("preProdRejectPlans")
    ConcurrentLinkedQueue<RejectPlans> lRejectPlans;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    PRNumberHelper prNumberHelper;

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public BPMClientUtils getBPMClientUtils() {
	return lBPMClientUtils;
    }

    public PreProductionLoadsDAO getPreProductionLoadsDAO() {
	return preProductionLoadsDAO;
    }

    public TOSHelper gettOSHelper() {
	return tOSHelper;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public JGitClientUtils getJGitClientUtils() {
	return jGitClientUtils;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public TestSystemLoadDAO getTestSystemLoadDAO() {
	return testSystemLoadDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public GitBlitClientUtils getlGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public VparsDAO getVparsDAO() {
	return vparsDAO;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public TOSHelper getTOSHelper() {
	return tOSHelper;
    }

    public PRNumberHelper getPRNumberHelper() {
	return prNumberHelper;
    }

    public SortedSet<String> findAllDependentPlanIds(String planId, Boolean planApprovalTime, HashMap<String, List<String>> parentIdAndDeptList) {
	SortedSet<String> dependentPlanIds = new TreeSet();
	findAllDependentPlanIds(dependentPlanIds, planId, planApprovalTime, parentIdAndDeptList);
	return dependentPlanIds;
    }

    /**
     * This method retrieves all the Dependent plans for the given planId. Dependent
     * plans are identified based on, - having the given planId in related plan
     * field (Manual dependents) - having same source and load date after the given
     * plan (Auto-dependent) Resultant PlanIds will be stored in dependentPlanIds.
     *
     * @param dependentPlanIds
     * @param planId
     *            - Implementation Plan Id of the Rejected plan
     *
     *            NOTE : Always clear the dependentPlanIds before calling this
     *            method.
     */
    private void findAllDependentPlanIds(SortedSet<String> dependentPlanIds, String planId, Boolean planApprovalTime, HashMap<String, List<String>> parentIdAndDeptList) {
	dependentPlanIds.add(planId);
	List<Object[]> autoDependentPlanList;
	List<ImpPlan> manualDependentPlanList;
	SortedSet<String> planIds = new TreeSet();

	/**
	 * Auto-dependent identified based on segment and having load date later than
	 * this plan's load date
	 */
	autoDependentPlanList = getImpPlanDAO().getPostSegmentRelatedPlans(planId, planApprovalTime);

	// Manual-dependent identified based on Related plan field
	manualDependentPlanList = getImpPlanDAO().findDependentPlans(planId);

	for (Object[] plan : autoDependentPlanList) {
	    String lPlanId = plan[2].toString();
	    if (!Constants.PlanStatus.getOnlineAndAbove().containsKey(plan[1].toString()) && !Constants.PlanStatus.getBeforeApprovedStatus().containsKey(plan[1].toString())) {
		List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(lPlanId); // 2028
		if (lProdLoads == null || lProdLoads.isEmpty()) {
		    planIds.add(plan[2].toString());
		}
	    }
	}

	for (ImpPlan impPlan : manualDependentPlanList) {
	    List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(impPlan); // 2028
	    if (lProdLoads == null || lProdLoads.isEmpty()) {
		planIds.add(impPlan.getId());
	    }
	}

	// 1319 - Get list of plan ids and its parent Imp Id
	if (planIds.size() > 0 && parentIdAndDeptList != null) {
	    List<String> deptIds = new ArrayList<>();
	    if (parentIdAndDeptList.containsKey(planId)) {
		deptIds = parentIdAndDeptList.get(planId);
	    }

	    List<String> tmpDeptIds = new ArrayList<>();
	    for (String impId : planIds) {
		if (!dependentPlanIds.contains(impId) && !deptIds.contains(impId)) {
		    tmpDeptIds.add(impId);
		}
	    }

	    if (tmpDeptIds.size() > 0) {
		deptIds.addAll(tmpDeptIds);
		parentIdAndDeptList.put(planId, deptIds);
	    }
	}

	for (String dependentPlanId : planIds) {
	    if (!dependentPlanIds.contains(dependentPlanId)) {
		dependentPlanIds.add(dependentPlanId);
		findAllDependentPlanIds(dependentPlanIds, dependentPlanId, planApprovalTime, parentIdAndDeptList);
	    }
	}
    }

    public List<ImpPlan> getSecuredPlans(List<String> impPlans) {
	List<ImpPlan> securedPlanIds = new ArrayList();
	securedPlanIds = getImpPlanDAO().findByPlanStatus(impPlans, new ArrayList(Constants.PlanStatus.getSecuredStatusMap().keySet()));
	return securedPlanIds;
    }

    public List<ImpPlan> getUnSecuredPlans(List<String> impPlans) {
	List<ImpPlan> unsecuredPlanIds = new ArrayList();
	unsecuredPlanIds = getImpPlanDAO().findByPlanStatus(impPlans, Arrays.asList(Constants.PlanStatus.ACTIVE.name()));
	return unsecuredPlanIds;
    }

    public void notifyADLOnReject(String planId, String leadId, User currentUser, String rejectReason, boolean deleteLoadSetFlag) {
	ImpPlan impPlan = getImpPlanDAO().find(planId);
	RejectMail rejectMail = (RejectMail) getMailMessageFactory().getTemplate(RejectMail.class);
	rejectMail.setPlanId(planId);
	rejectMail.setCurrentUser(currentUser);
	rejectMail.setLeadId(leadId);
	rejectMail.setDeleteLoadSetFlag(deleteLoadSetFlag);
	rejectMail.setPlanDescription(impPlan.getPlanDesc());

	List<Implementation> findByImpPlan = getImplementationDAO().findByImpPlan(planId);

	for (Implementation implementation : findByImpPlan) {
	    rejectMail.addcCAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
	    String[] reviewers = implementation.getPeerReviewers().split("\\,");
	    for (String reviewer : reviewers) {
		rejectMail.addcCAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
	    }
	}
	rejectMail.setComment(rejectReason);
	getMailMessageFactory().push(rejectMail);
    }

    public void notifyADLOnLoadDatePass(String planId, String leadId, User currentUser) {
	LoadDatePassedMail loadDatePassedMail = (LoadDatePassedMail) getMailMessageFactory().getTemplate(LoadDatePassedMail.class);
	loadDatePassedMail.setPlanId(planId);
	loadDatePassedMail.setCurrentUser(currentUser);
	loadDatePassedMail.setLeadId(leadId);
	getMailMessageFactory().push(loadDatePassedMail);
    }

    public void notifyADLOnAutoReject(String planId, String leadId, String dependentPlanId, String status) {
	AutoRejectMail autoRejectMail = (AutoRejectMail) getMailMessageFactory().getTemplate(AutoRejectMail.class);
	autoRejectMail.setPlanId(planId);
	autoRejectMail.setDependentPlanId(dependentPlanId);
	autoRejectMail.setLeadId(leadId);
	autoRejectMail.setStatus(status);
	getMailMessageFactory().push(autoRejectMail);
    }

    public void notifyADLOnUnsecuredPlan(ImpPlan planId, String leadId, String dependentPlanId, String status) {
	if (planId.getLoadType().equalsIgnoreCase(Constants.LoadTypes.EXCEPTION.name()) && planId.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name())) {
	    ExceptionSourceContentionMail autoRejectMail = (ExceptionSourceContentionMail) getMailMessageFactory().getTemplate(ExceptionSourceContentionMail.class);
	    autoRejectMail.setOnlinePlan(planId.getId());
	    autoRejectMail.setDependentPlan(dependentPlanId);
	    autoRejectMail.setLeadId(leadId);
	    getMailMessageFactory().push(autoRejectMail);
	} else if (planId.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.FALLBACK.name())) {
	    List<String> lSegments = new ArrayList();
	    lSegments = getCheckoutSegmentsDAO().findCommonFilesBtwnPlan(planId.getId(), dependentPlanId);
	    FallBackRejectUnSecMail fallbackUnsecMail = (FallBackRejectUnSecMail) getMailMessageFactory().getTemplate(FallBackRejectUnSecMail.class);
	    fallbackUnsecMail.setFileName(String.join(",", lSegments));
	    fallbackUnsecMail.setFallbackPlan(planId.getId());
	    fallbackUnsecMail.setDependentPlan(dependentPlanId);
	    fallbackUnsecMail.setDepPlanLeadId(leadId);
	    getMailMessageFactory().push(fallbackUnsecMail);
	}
    }

    public Boolean bpmRejectTask(User user, String processId, String leadId, String planId) throws Exception {
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Submitted", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", false));
	getBPMClientUtils().setTaskAsCompletedWithVariables(user, processId, lTaskVars);
	List<TaskVariable> lVars = new ArrayList<>();
	return getBPMClientUtils().assignTask(user, processId, leadId, lVars);
    }

    public void updatePlanAndImplementationStatus(User user, ImpPlan plan, String comments, String parentPlanId, Boolean tsdActivity, boolean stagingLoadFail) throws Exception {
	plan.setPlanStatus(Constants.PlanStatus.ACTIVE.toString());
	plan.setApproveDateTime(null);
	plan.setApprover(null);
	plan.setApproverName(null);
	plan.setQaBypassStatus(null);
	plan.setDeploymentStatus("NONE");
	plan.setDeployedMailFlag(Boolean.FALSE);
	plan.setLoadDateMacroMailFlag(Boolean.FALSE);
	plan.setRejectedDateTime(new Date());
	getImpPlanDAO().update(user, plan);
	// Update System Load - clear production loadset operation status
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(plan.getId());
	for (SystemLoad lSystemLoad : lSystemLoads) {
	    lSystemLoad.setProdLoadStatus(null);
	    lSystemLoad.setQaBypassStatus(null);
	    lSystemLoad.setQaFunctionalBypassComment(null);
	    lSystemLoad.setQaRegressionBypassComment(null);
	    lSystemLoad.setDerivedSegmentsMovedDt(null);

	    List<PreProductionLoads> lPreProdList = getPreProductionLoadsDAO().findBySystemId(lSystemLoad.getSystemId().getId());
	    List<SystemLoadActions> lSystemLoadActions = getSystemLoadActionsDAO().findBySystemLoadEnv(lSystemLoad, Constants.VPARSEnvironment.PRE_PROD);
	    if (lPreProdList == null && lSystemLoadActions == null) {
		lSystemLoad.setPreProdLoadStatus(null);
	    } else if (lPreProdList != null && lPreProdList.isEmpty() && lSystemLoadActions != null && lSystemLoadActions.isEmpty()) {
		lSystemLoad.setPreProdLoadStatus(null);
	    }
	    getSystemLoadDAO().update(user, lSystemLoad);
	}
	// Log Activity Message
	RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(plan, null);
	rejectActivityMessage.setTsdActivity(tsdActivity);
	if (comments != null) {
	    rejectActivityMessage.setComments(comments);
	}
	if (parentPlanId != null) {
	    rejectActivityMessage.setComments(comments + parentPlanId);
	}
	if (comments != null && comments.contains(Constants.AUTOREJECT_COMMENT.LOAD_DATE_CHANGE.getValue())) {
	    rejectActivityMessage.setComments(comments);
	}
	// Update Implementation Plan Repo Permmission
	String lCompanyName = "";
	for (SystemLoad lSystemLoad : plan.getSystemLoadList()) {
	    if (lSystemLoad != null) {
		lCompanyName = lSystemLoad.getSystemId().getPlatformId().getNickName();
	    }
	}
	String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, plan.getId().toLowerCase());
	getActivityLogDAO().save(user, rejectActivityMessage);
	if (!stagingLoadFail) {
	    getGitHelper().updateImplementationPlanRepoPermissions(plan.getId(), null);
	}
	/*
	 * 2413 if (!stagingLoadFail) { List<Implementation> implementationList =
	 * plan.getImplementationList(); for (Implementation implementation :
	 * implementationList) {
	 * implementation.setImpStatus(Constants.ImplementationStatus.IN_PROGRESS.
	 * toString()); implementation.setSubstatus(null);
	 * implementation.setIsCheckedin(Boolean.FALSE);
	 * implementation.setReviewersDone(""); implementation.setCheckinDateTime(null);
	 * implementation.setLastCheckinStatus(null);
	 * getImplementationDAO().update(user, implementation); //
	 * getlGitBlitClientUtils().setPermissionForGitRepository(lRepoName, //
	 * implementation.getDevId(), // Constants.GIT_PERMISSION_READWRITE); //
	 * getActivityLogDAO().save(user, new RejectionActivityMessage(plan, //
	 * implementation)); }}
	 */
    }

    public Boolean tagImplementationPlan(User currentUser, ImpPlan impPlan, TagStatus status) throws Exception {
	List<String> taggingBranches = new ArrayList();
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(impPlan);
	for (SystemLoad systemLoad : lSystemLoadList) {
	    taggingBranches.add(Constants.BRANCH_MASTER.concat(systemLoad.getSystemId().getName().toLowerCase()));
	}
	Platform platform = impPlan.getSystemLoadList().get(0).getSystemId().getPlatformId();
	String repositoryName = getJGitClientUtils().getPlanRepoName(platform.getNickName(), impPlan.getId().toLowerCase());
	return getsSHClientUtils().addImplementationTag(repositoryName, status, taggingBranches);
    }

    public void deleteBuilds(User pUser, ImpPlan pPlan) {
	List<Build> lBuilds = new ArrayList();
	lBuilds.addAll(getBuildDAO().findAll(pPlan, Constants.BUILD_TYPE.STG_BUILD));
	lBuilds.addAll(getBuildDAO().findAll(pPlan, Constants.BUILD_TYPE.STG_LOAD));
	lBuilds.addAll(getBuildDAO().findAll(pPlan, Constants.BUILD_TYPE.STG_CWS));
	/*
	 * 2413 lBuilds.addAll(getBuildDAO().findAll(pPlan,
	 * Constants.BUILD_TYPE.DVL_BUILD)); lBuilds.addAll(getBuildDAO().findAll(pPlan,
	 * Constants.BUILD_TYPE.DVL_LOAD));
	 */
	for (Build lBuild : lBuilds) {
	    getBuildDAO().delete(pUser, lBuild);
	}
    }

    public void deleteStagingWorkspace(User currentUser, ImpPlan impPlan) throws Exception {
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(impPlan);
	for (SystemLoad systemLoad : lSystemLoadList) {
	    com.tsi.workflow.beans.dao.System system = systemLoad.getSystemId();
	    String command = Constants.SystemScripts.DELETE_STAGING_WORKSPACE.getScript() + impPlan.getId().toLowerCase();
	    LOG.info(command);
	    JSONResponse lResponse = getsSHClientUtils().executeCommand(system, command);
	    LOG.info("Response from SSH : " + lResponse.getStatus());
	    if (!lResponse.getStatus()) {
		throw new WorkflowException("Unable to delete staging workspace for Plan - " + impPlan.getId());
	    }
	}
    }

    public void removeDeployment(User currentUser, ImpPlan impPlan) throws Exception {
	List<SystemLoadActions> sysLoadActions = getSystemLoadActionsDAO().findByPlanId(impPlan.getId());
	for (SystemLoadActions sysLoadAction : sysLoadActions) {
	    getSystemLoadActionsDAO().delete(currentUser, sysLoadAction);
	    LoadSetActivityMessage lMessage = new LoadSetActivityMessage(impPlan, null, sysLoadAction);
	    getActivityLogDAO().save(currentUser, lMessage);
	}
    }

    public List<ImpPlan> getApprovedPlans(List<String> impPlans) {
	List<ImpPlan> ApprovedPlanIds = new ArrayList();
	ApprovedPlanIds = getImpPlanDAO().findByPlanStatus(impPlans, new ArrayList(Constants.PlanStatus.getApprovedAndAboveStatus().keySet()));
	return ApprovedPlanIds;
    }

    /**
     * This method rejects all the Dependent plans for the given planId. For secured
     * dependent plans - status changed to Active - implementation status changed to
     * In-Progress - Tagged in Repository as AUTO-REJECTED - Notification sent to
     * ADL For unsecured dependent plans - Notification sent to ADL
     *
     * @param currentUser
     *            - Logged in user information
     * @param planId
     *            - Implementation Plan Id of the Rejected plan
     * @param rejectReason
     *            - reason for reject to include in mail notification
     * @param autoRejectReason
     *            - reason for autoReject to include Plan comments
     * @param planApprovalTime
     *            - is need to consider staging build date and time of plan
     * @param tsdActivity
     *            - is need to log the rejection reason in activity Log
     * @param includeThisPlan
     *            - is need to include the plan in dependent plan list
     * @param removePlan
     *            - plan id to be removed from dependent plans
     * @param deleteLoadSetFlag
     *            - is Loadset needs to be removed from Integration/QA/PreProd
     *            systems
     * @throws java.lang.Exception
     */
    public void rejectDependentPlans(User currentUser, String planId, String rejectReason, String autoRejectReason, Boolean planApprovalTime, Boolean tsdActivity, Boolean includeThisPlan, String removePlan, Boolean deleteLoadSetFlag) throws Exception {
	Map<String, Date> lPlanVsDate = new HashMap();
	rejectDependentPlans(currentUser, planId, rejectReason, autoRejectReason, planApprovalTime, tsdActivity, includeThisPlan, removePlan, deleteLoadSetFlag, lPlanVsDate);
    }

    public void rejectDependentPlans(User currentUser, String planId, String rejectReason, String autoRejectReason, Boolean planApprovalTime, Boolean tsdActivity, Boolean includeThisPlan, String removePlan, Boolean deleteLoadSetFlag, Map<String, Date> lDepPlanVsDate) throws Exception {
	ImpPlan impPlan = getImpPlanDAO().find(planId);
	HashMap<String, List<String>> parentIdAndDeptList = new HashMap<>();

	SortedSet<String> dependentPlans = findAllDependentPlanIds(planId, planApprovalTime, parentIdAndDeptList);

	if (!includeThisPlan) {
	    dependentPlans.remove(planId);
	}
	if (removePlan != null) {
	    dependentPlans.remove(removePlan);
	}
	LOG.info("Processing Plan - " + dependentPlans);
	if (!dependentPlans.isEmpty()) {
	    List<ImpPlan> securedPlanIds = getSecuredPlans(new ArrayList(dependentPlans));
	    List<ImpPlan> unsecuredPlanIds = getUnSecuredPlans(new ArrayList(dependentPlans));
	    for (ImpPlan securedImpPlan : securedPlanIds) {

		String comment = securedImpPlan.getMgmtComment();
		if (comment != null) {
		    if (!autoRejectReason.contains(Constants.AUTOREJECT_COMMENT.LOAD_DATE_CHANGE.getValue())) {
			securedImpPlan.setMgmtComment(comment.concat(autoRejectReason + planId + ". "));
		    } else {
			securedImpPlan.setMgmtComment(comment.concat(autoRejectReason));
		    }
		} else {
		    if (!autoRejectReason.contains(Constants.AUTOREJECT_COMMENT.LOAD_DATE_CHANGE.getValue())) {
			securedImpPlan.setMgmtComment(autoRejectReason + planId + ". ");
		    } else {
			securedImpPlan.setMgmtComment(autoRejectReason);
		    }
		}

		notifyMacroHeaderDeptPlanDetail(currentUser, securedImpPlan, "REJECTED");

		if (!securedImpPlan.getMacroHeader()) {
		    List<Date> lPlanLoadDate = new ArrayList();
		    List<PreProductionLoads> lPreProdLoads = getPreProductionLoadsDAO().findByPlanId(securedImpPlan);
		    if (lPreProdLoads != null && !lPreProdLoads.isEmpty()) {
			Map<SystemLoad, List<PreProductionLoads>> lSystemLoadVsPreProdLoads = lPreProdLoads.stream().collect(Collectors.groupingBy(t -> t.getSystemLoadId()));
			if (deleteLoadSetFlag) {
			    lSystemLoadVsPreProdLoads.entrySet().stream().forEach(lSystemLoadVsPPLoads -> {
				Boolean isTOSActionNeeded = lSystemLoadVsPPLoads.getValue().stream().filter(lPreProdLoad -> lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).findAny().isPresent();
				if (isTOSActionNeeded) {
				    lPlanLoadDate.add(lSystemLoadVsPPLoads.getKey().getLoadDateTime());
				} else {
				    lSystemLoadVsPPLoads.getKey().setPreProdLoadStatus(null);
				    getSystemLoadDAO().update(currentUser, lSystemLoadVsPPLoads.getKey());
				}
				lSystemLoadVsPPLoads.getValue().stream().filter(lPreProdLoad -> !lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && !lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).forEach(lPreProdLoad -> getPreProductionLoadsDAO().delete(currentUser, lPreProdLoad));
			    });
			} else {
			    lPreProdLoads.stream().forEach(lPreProdLoad -> getPreProductionLoadsDAO().delete(currentUser, lPreProdLoad));
			}
		    }
		    if (lPlanLoadDate.isEmpty()) {
			deleteStagingWorkspace(currentUser, securedImpPlan);
			deleteBuilds(currentUser, securedImpPlan);
		    } else {
			securedImpPlan.setInprogressStatus(Constants.PlanInProgressStatus.REJECT.name());
			lDepPlanVsDate.put(securedImpPlan.getId(), Collections.min(lPlanLoadDate));
		    }
		    // Delete the Loadset from YODA
		    deleteLoadSetActivated(currentUser, securedImpPlan, Boolean.TRUE);
		}

		// 1319 : In rejection log Parent id is not updated correctly. To get secured
		// plan id's Parent imp id,
		// code changes were done.
		Optional<Entry<String, List<String>>> parentIdMap = parentIdAndDeptList.entrySet().stream().filter(p -> p.getValue().contains(securedImpPlan.getId())).findAny();
		String parentId = parentIdMap.isPresent() && parentIdMap.get() != null ? parentIdMap.get().getKey() : planId;

		String lCompanyName = securedImpPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, securedImpPlan.getId().toLowerCase());
		// Remove SCM Tag - Secured
		if (!getsSHClientUtils().removeTag(lRepoName, Constants.TagStatus.SECURED)) {
		    LOG.info("Unable to Remove Tag - " + Constants.TagStatus.SECURED);
		}
		tagImplementationPlan(currentUser, securedImpPlan, Constants.TagStatus.AUTO_REJECTED);
		notifyADLOnAutoReject(parentId, securedImpPlan.getLeadId(), securedImpPlan.getId(), rejectReason);
		bpmRejectTask(currentUser, securedImpPlan.getProcessId(), securedImpPlan.getLeadId(), securedImpPlan.getId());

		removeDeployment(currentUser, securedImpPlan);
		updatePlanAndImplementationStatus(currentUser, securedImpPlan, autoRejectReason, parentId, tsdActivity, false);

	    }

	    for (ImpPlan unsecuredPlan : unsecuredPlanIds) {
		notifyADLOnUnsecuredPlan(impPlan, unsecuredPlan.getLeadId(), unsecuredPlan.getId(), rejectReason);
	    }

	    // Remove all the plans which got rejected
	    dependentPlans.forEach(depPlan -> {
		cacheClient.getInProgressRelatedPlanMap().remove(depPlan);
	    });
	}

	// Sort the Plan by Date in desending order.
	if (lDepPlanVsDate != null && !lDepPlanVsDate.isEmpty()) {
	    Map<String, Date> lSortedPlan = lDepPlanVsDate.entrySet().stream().sorted((Map.Entry.<String, Date> comparingByValue().reversed())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

	    RejectPlans lRejectPlan = new RejectPlans();
	    lRejectPlan.setUser(currentUser);
	    lRejectPlan.setDependentPlanIds(lSortedPlan);
	    lSortedPlan.entrySet().stream().forEach(t -> LOG.info("Reject Plan " + t.getKey() + " " + t.getValue()));
	    lRejectPlans.add(lRejectPlan);
	}
    }

    /**
     * This method rejects the given Implementation plan - Plan status changed to
     * Active - Implementation status changed to In-Progress - Tagged in Repository
     * as REJECTED - Notification sent to ADL if the role is not ADL For unsecured -
     * Calls rejectDependetPlans() to reject dependent plans
     *
     * @param currentUser
     *            - Logged in user information
     * @param planId
     *            - Implementation Plan Id of the Rejected plan
     * @param rejectReason
     *            - reason for reject to include in mail notification
     * @param autoRejectReason
     *            - reason for autoReject to include Plan comments
     * @param dependent
     *            - dependent plans has to be rejected as well
     * @throws java.lang.Exception
     */
    public void rejectPlan(User currentUser, String planId, String rejectReason, String autoRejectReason, boolean dependent, Boolean tsdActivity, Boolean deleteLoadSetFlag) throws Exception {
	ImpPlan rejectedImpPlan = getImpPlanDAO().find(planId);
	if (rejectedImpPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ACTIVE.name())) {
	    if (rejectReason.equalsIgnoreCase(Constants.REJECT_REASON.LOAD_DATE.getValue())) {
		notifyADLOnLoadDatePass(rejectedImpPlan.getId(), rejectedImpPlan.getLeadId(), currentUser);
	    }
	} else {

	    // Adding all dependent plans to avoid parallel actions.
	    if (dependent) {
		List<String> dependentPlans = getDependententPlans(planId);
		LOG.info(" Reject Dependent plans: " + dependentPlans.stream().collect(Collectors.joining(",")));
		dependentPlans.forEach(depPlanId -> {
		    cacheClient.getInProgressRelatedPlanMap().put(depPlanId, new DependentPlanRejectDetail(currentUser, planId, rejectReason, autoRejectReason));
		});
	    }
	    notifyMacroHeaderDeptPlanDetail(currentUser, rejectedImpPlan, "REJECTED");
	    notifyADLOnReject(rejectedImpPlan.getId(), rejectedImpPlan.getLeadId(), currentUser, rejectReason, deleteLoadSetFlag);

	    Map<String, Date> lPlanVsDate = new TreeMap();
	    String lCompanyName = rejectedImpPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, rejectedImpPlan.getId().toLowerCase());
	    // Remove SCM Tag - Secured
	    if (!getsSHClientUtils().removeTag(lRepoName, Constants.TagStatus.SECURED)) {
		LOG.info("Unable to Remove Tag - " + Constants.TagStatus.SECURED + " for plan - " + rejectedImpPlan.getId());
	    }
	    tagImplementationPlan(currentUser, rejectedImpPlan, Constants.TagStatus.REJECTED);
	    bpmRejectTask(currentUser, rejectedImpPlan.getProcessId(), rejectedImpPlan.getLeadId(), rejectedImpPlan.getId());

	    if (!rejectedImpPlan.getMacroHeader()) {
		List<Date> lPlanLoadDate = new ArrayList();
		List<PreProductionLoads> lPreProdLoads = getPreProductionLoadsDAO().findByPlanId(rejectedImpPlan);
		if (lPreProdLoads != null && !lPreProdLoads.isEmpty()) {
		    Map<SystemLoad, List<PreProductionLoads>> lSystemLoadVsPreProdLoads = lPreProdLoads.stream().collect(Collectors.groupingBy(t -> t.getSystemLoadId()));
		    if (deleteLoadSetFlag) {
			lSystemLoadVsPreProdLoads.entrySet().stream().forEach(lSystemLoadVsPPLoads -> {
			    Boolean isTOSActionNeeded = lSystemLoadVsPPLoads.getValue().stream().filter(lPreProdLoad -> lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).findAny().isPresent();
			    if (isTOSActionNeeded) {
				lPlanLoadDate.add(lSystemLoadVsPPLoads.getKey().getLoadDateTime());
			    } else {
				lSystemLoadVsPPLoads.getKey().setPreProdLoadStatus(null);
				getSystemLoadDAO().update(currentUser, lSystemLoadVsPPLoads.getKey());
			    }
			    lSystemLoadVsPPLoads.getValue().stream().filter(lPreProdLoad -> !lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && !lPreProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.DEACTIVATED.name())).forEach(lPreProdLoad -> getPreProductionLoadsDAO().delete(currentUser, lPreProdLoad));
			});
		    } else {
			lPreProdLoads.stream().forEach(lPreProdLoad -> getPreProductionLoadsDAO().delete(currentUser, lPreProdLoad));
		    }
		}
		if (lPlanLoadDate.isEmpty()) {
		    deleteStagingWorkspace(currentUser, rejectedImpPlan);
		    deleteBuilds(currentUser, rejectedImpPlan);
		} else {
		    rejectedImpPlan.setInprogressStatus(Constants.PlanInProgressStatus.REJECT.name());
		    getImpPlanDAO().update(currentUser, rejectedImpPlan);
		    lPlanVsDate.put(rejectedImpPlan.getId(), Collections.min(lPlanLoadDate));
		}
		// Delete the Loadset from YODA
		if (deleteLoadSetFlag) {
		    deleteLoadSetActivated(currentUser, rejectedImpPlan, Boolean.TRUE);
		}
	    }

	    removeDeployment(currentUser, rejectedImpPlan);
	    if (rejectReason.equalsIgnoreCase(Constants.REJECT_REASON.REJECTION.getValue())) {
		updatePlanAndImplementationStatus(currentUser, rejectedImpPlan, null, null, tsdActivity, false);
	    } else {
		updatePlanAndImplementationStatus(currentUser, rejectedImpPlan, rejectReason, null, tsdActivity, false);
	    }

	    // ZTPFM-2625 Update status of PR Ticket as Reject
	    getPRNumberHelper().updatePRNumber(rejectedImpPlan, Constants.PRNumberStatuses.REJECTED.getPRStatus());

	    // Removing Rejected plan from cache
	    cacheClient.getInProgressRelatedPlanMap().remove(rejectedImpPlan.getId());

	    if (dependent) {
		rejectDependentPlans(currentUser, planId, rejectReason, autoRejectReason, false, tsdActivity, Boolean.FALSE, null, deleteLoadSetFlag, lPlanVsDate);
	    }

	}
    }

    public void notifyMacroHeaderDeptPlanDetail(User currentUser, ImpPlan rejectedImpPlan, String actionType) {
	// Call Shell script to check the plans that has a macro/header is rejected or
	// takes a fall back and notify
	// ADL/Dev
	Set<String> plansToNotify = new HashSet<>();
	getSystemLoadDAO().findByImpPlan(rejectedImpPlan).forEach(sytemLoad -> {
	    String command = Constants.SystemScripts.MACRO_HEADER_DEPENDENCY.getScript() + rejectedImpPlan.getId().toLowerCase() + "_" + sytemLoad.getSystemId().getName().toLowerCase() + "_" + Constants.JENKINS_DATEFORMAT.get().format(sytemLoad.getLoadDateTime()) + "0000";
	    JSONResponse lResponse = getsSHClientUtils().executeCommand(sytemLoad.getSystemId(), command);
	    if (lResponse.getStatus() && lResponse.getData() != null && !lResponse.getData().toString().trim().equalsIgnoreCase("empty")) {
		plansToNotify.addAll(Arrays.asList(lResponse.getData().toString().trim().toUpperCase().split(",")));
	    }
	    String dependentPlans = lResponse.getData() != null && !lResponse.getData().toString().trim().equalsIgnoreCase("empty") ? lResponse.getData().toString().trim().toUpperCase() : "";
	    if (dependentPlans != null && !dependentPlans.isEmpty()) {
		MacroPlanDependentActivityLog macroPlanDepActivityLog = new MacroPlanDependentActivityLog(rejectedImpPlan, null);
		macroPlanDepActivityLog.setStatus(lResponse.getStatus());
		macroPlanDepActivityLog.setActionType(actionType);
		macroPlanDepActivityLog.setDependentPlans(dependentPlans);
		getActivityLogDAO().save(currentUser, macroPlanDepActivityLog);
	    }
	});

	List<String> depPlanMailDetail = new ArrayList<>();
	if (!plansToNotify.isEmpty()) {
	    plansToNotify.forEach(depPlanId -> {
		StringBuilder sb = new StringBuilder();
		ImpPlan dependentPlan = getImpPlanDAO().find(depPlanId.toUpperCase());
		if (dependentPlan != null) {
		    dependentPlan.getBuildList().forEach(build -> {
			if (build.getActive().equals("Y") && build.getJobStatus().equals("S") && build.getBuildType().equals(Constants.BUILD_TYPE.STG_LOAD.name())) {
			    sb.append(depPlanId).append(" ").append(dependentPlan.getLeadName() + " " + build.getLoadSetType() + " ");
			    sb.append(dependentPlan.getPlanStatus());
			    depPlanMailDetail.add(sb.toString());
			}
		    });
		}
	    });

	    if (!depPlanMailDetail.isEmpty()) {
		MacroPlanDependencyRejectMail planRejectionMail = (MacroPlanDependencyRejectMail) mailMessageFactory.getTemplate(MacroPlanDependencyRejectMail.class);
		planRejectionMail.setPlanId(rejectedImpPlan.getId());
		planRejectionMail.setDependencyPlanMsg(depPlanMailDetail);
		planRejectionMail.setActionType("REJECTED");

		List<String> mailIds = new ArrayList<>();
		rejectedImpPlan.getImplementationList().forEach(imp -> {
		    if (imp.getActive().equals("Y")) {
			mailIds.add(imp.getDevId());
		    }
		});
		planRejectionMail.setMailIds(mailIds);
		mailMessageFactory.push(planRejectionMail);
	    }
	}
    }

    /*
     * Story/Defect#: ZTPFM-2381 Created By: Radha.Adhimoolam Created Date:
     * 08/02/2019 Modified By: Modified Date: MM/DD/YYYY Description: Deactivate and
     * Delete the loadsets of plan from YODA system and update the Activity log of
     * the plan Accordingly.
     */
    public void deleteLoadSetActivated(User pUser, ImpPlan plan, Boolean pAutoReject) {
	deleteLoadSetActivated(pUser, plan, pAutoReject, Boolean.FALSE);
    }

    public void deleteLoadSetActivated(User pUser, ImpPlan plan, Boolean pAutoReject, Boolean isFallback) {
	LOG.info("Deleting YODA Loads for the plan " + plan.getId());
	LinkedHashMap<String, String> loadsetDeActivationStatusMap = Constants.PlanStatus.getAfterSubmitStatus();
	if (loadsetDeActivationStatusMap.keySet().contains(plan.getPlanStatus())) {
	    List<SystemLoadActions> lLoadSets = getSystemLoadActionsDAO().findByPlanId(plan);
	    for (SystemLoadActions lLoadSet : lLoadSets) {
		if (lLoadSet.getVparId() != null && !lLoadSet.getVparId().getTssDeploy() && lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || lLoadSet.getStatus().equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
		    boolean lResult = false;
		    try {
			List<YodaResult> lList = getTestSystemLoadDAO().deleteAndDeActivate(pUser, lLoadSet.getVparId().getName(), lLoadSet.getSystemLoadId().getLoadSetName());

			if (lList != null && lList.size() > 0) {
			    YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(plan, null);
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

		    // if (lLoadSet.getDslUpdate().equals("Y")) {
		    // dSLFileHelper.deletePlanInfoInDSLFile(pUser, lLoadSet);
		    // }
		    // lLoadSet.setTestStatus(null);
		    getSystemLoadActionsDAO().update(pUser, lLoadSet);
		    YodaDeleteDeactivateActivityMessage lMessage = new YodaDeleteDeactivateActivityMessage(plan, null, lLoadSet);
		    lMessage.setIsAutoReject(pAutoReject);
		    lMessage.setIsFallback(isFallback);
		    getActivityLogDAO().save(pUser, lMessage);
		}
	    }

	}
    }

    private void setStatus(Boolean lResult, PreProductionLoads lLoad, String lOldStatus) {
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

    /*
     * It get the list of child plan with load date time. Parameter 1 - Parent Plan
     * Id Parameter 2 - return variable, list of child plans with load date and time
     * NOTE: Caller should clear the parameter 2 before calling this method
     */
    private void getDependentPlans(String planId, Map<String, Date> planVsDate) {
	List<Object[]> lDependentPlan = getImpPlanDAO().getPostSegmentRelatedPlans(planId, Boolean.FALSE);
	for (Object[] lPlanInfo : lDependentPlan) {
	    String lChildPlanStatus = lPlanInfo[1].toString();
	    String lChildPlanId = lPlanInfo[2].toString();
	    Date lChildLoadDate = (Date) lPlanInfo[3];
	    if (!Constants.PlanStatus.getOnlineAndAbove().containsKey(lChildPlanStatus) && !Constants.PlanStatus.ACTIVE.name().equalsIgnoreCase(lChildPlanStatus)) {
		if (planVsDate.get(lChildPlanId) == null) {
		    planVsDate.put(lChildPlanId, lChildLoadDate);
		}
	    }
	    getDependentPlans(lChildPlanId, planVsDate);
	}
    }

    public Boolean rejectPlan(User user, String planId, String rejectReason, Boolean deleteLoadset) throws Exception {
	ImpPlan lPlan = getImpPlanDAO().find(planId);
	List<SystemLoad> lSystemLoad = getSystemLoadDAO().findByImpPlan(planId);
	Map<String, Date> lPlanVsDate = new HashMap();
	lPlanVsDate.put(planId, lSystemLoad.get(0).getLoadDateTime());

	// Get the Dependent Plan list
	getDependentPlans(planId, lPlanVsDate);

	LOG.info("List of plans and its Load Date");
	lPlanVsDate.entrySet().forEach(lTemp -> LOG.info(lTemp.getKey() + "  " + lTemp.getValue()));

	return Boolean.TRUE;
    }

    public List<String> getDependententPlans(String planId) {
	List<String> depPlans = new ArrayList<>();
	List<Object[]> autoDependentPlanList = getImpPlanDAO().getPostSegmentRelatedPlans(planId, false);
	for (Object[] plan : autoDependentPlanList) {
	    String lPlanId = plan[2].toString();
	    if (!Constants.PlanStatus.getOnlineAndAbove().containsKey(plan[1].toString())) {
		List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(lPlanId); // 2028
		if (lProdLoads == null || lProdLoads.isEmpty()) {
		    depPlans.add(plan[2].toString());
		}
	    }
	}
	return depPlans;
    }
}
