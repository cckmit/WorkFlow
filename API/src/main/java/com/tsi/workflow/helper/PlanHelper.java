/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.ApproveReviewActivityMessage;
import com.tsi.workflow.activity.LoadTypeChangesActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.mail.DeveloperNotificationMail;
import com.tsi.workflow.mail.LoadTypeChangeMail;
import com.tsi.workflow.mail.QAAssignmentMail;
import com.tsi.workflow.mail.QAFunctionalTesterPassFailMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class PlanHelper {

    private static final Logger LOG = Logger.getLogger(PlanHelper.class.getName());

    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    BPMClientUtils lBPMClientUtils;
    @Autowired
    PreProductionLoadsDAO preProductionLoadsDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    SystemLoadActionsDAO systemLoadActionsDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    LDAPAuthenticatorImpl authenticator;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    OnlineBuildDAO onlineBuildDAO;
    @Autowired
    ProductionLoadsDAO productionLoadsDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    CacheClient cacheClient;

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
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

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public SystemLoadActionsDAO getSystemLoadActionsDAO() {
	return systemLoadActionsDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public PutLevelDAO getPutLevelDAO() {
	return putLevelDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public OnlineBuildDAO getOnlineBuildDAO() {
	return onlineBuildDAO;
    }

    public ProductionLoadsDAO getProductionLoadsDAO() {
	return productionLoadsDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public Boolean deletePlanSourceGit(String pCompanyName, String pPlanId) throws IOException {
	String pPlanSourceRepositoryName = getJGitClientUtils().getPlanRepoFullName(pCompanyName, pPlanId);
	return getGitBlitClientUtils().deleteGitRepository(pPlanSourceRepositoryName);
    }

    public Boolean deletePlanDerivedGit(String pCompanyName, String pPlanId) throws IOException {
	String pPlanSourceRepositoryName = getJGitClientUtils().getPlanLFSRepoFullName(pCompanyName, pPlanId);
	return getGitBlitClientUtils().deleteGitRepository(pPlanSourceRepositoryName);
    }

    public JSONResponse deletePlanGitRepos(String pCompanyName, String pPlanId) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    if (!deletePlanSourceGit(pCompanyName, pPlanId)) {
		LOG.info("Error occured in Deleting Source plan in GIT Server - " + pPlanId + " ");
		throw new WorkflowException("Error occured in Deleting Source Repository in GIT Server Implementation Plan - " + pPlanId);
	    }
	    if (!deletePlanDerivedGit(pCompanyName, pPlanId)) {
		LOG.info("Error occured in Deleting Derived plan in GIT Server - " + pPlanId + " ");
		throw new WorkflowException("Error occured in Deleting Derived Repository in GIT Server for Implementation Plan - " + pPlanId);
	    }

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    throw new WorkflowException("Error in Deleting Git repository of Plan", ex);
	}
	return lResponse;
    }

    public JSONResponse revertSubmittedPlanToActive(User user, String planId) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ImpPlan lPlan = getImpPlanDAO().find(planId);
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(planId);
	List<System> lSystemList = new ArrayList();
	List<Build> lBuilds = new ArrayList();

	if (cacheClient.getInProgressRelatedPlanMap().containsKey(lPlan.getId())) {
	    lPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());
	}

	if (lPlan.getPlanStatus().equals(Constants.PlanStatus.SUBMITTED.name())) {

	    String lCompanyName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, lPlan.getId().toLowerCase());
	    lSystemLoadList.forEach((lSystemLoad) -> {
		lSystemList.add(lSystemLoad.getSystemId());
	    });

	    if (!lSystemList.isEmpty()) {
		lBuilds.addAll(getBuildDAO().findLastBuild(planId, lSystemList, Constants.BUILD_TYPE.STG_CWS));
		lBuilds.addAll(getBuildDAO().findLastBuild(planId, lSystemList, Constants.BUILD_TYPE.STG_BUILD));
		lBuilds.addAll(getBuildDAO().findLastBuild(planId, lSystemList, Constants.BUILD_TYPE.STG_LOAD));
	    }

	    // Remove SCM Tag - Secured
	    if (!getsSHClientUtils().removeTag(lRepoName, Constants.TagStatus.SECURED)) {
		LOG.info("Unable to Remove Tag - " + Constants.TagStatus.SECURED);
	    }

	    // Update BPM activity for ADL
	    List<TaskVariable> lTaskVars = new ArrayList<>();
	    lTaskVars.add(new TaskVariable("Submitted", "local", "boolean", false));
	    lTaskVars.add(new TaskVariable("Approved", "local", "boolean", false));
	    lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	    getBPMClientUtils().setTaskAsCompletedWithVariables(user, lPlan.getProcessId(), lTaskVars);
	    lTaskVars.clear();
	    lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lPlan.getId()));
	    boolean lStatus;
	    if (lPlan.getProcessId() != null && !lPlan.getProcessId().isEmpty()) {
		lStatus = getBPMClientUtils().assignTask(user, lPlan.getProcessId(), lPlan.getLeadId(), lTaskVars);
		lStatus = lStatus && getGitHelper().updateImplementationPlanRepoPermissions(lPlan.getId(), null);
		if (!lStatus) {
		    LOG.info("Unable to Set Permission for ADL -" + lPlan.getLeadId());
		}
	    }

	    lBuilds.forEach(t -> getBuildDAO().delete(user, t));
	    lPlan.setPlanStatus(Constants.PlanStatus.ACTIVE.name());
	    getImpPlanDAO().update(user, lPlan);
	    if (cacheClient.getInProgressRelatedPlanMap() != null) {
		cacheClient.getInProgressRelatedPlanMap().remove(lPlan.getId());
	    }
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public Boolean updatePlanAsDeployedInPreProd(User user, String planId, Constants.VPARSEnvironment env, Boolean isYODA) {
	ImpPlan lPlan = getImpPlanDAO().find(planId);
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(planId);
	Set<String> lSystemList = new TreeSet();
	Set<String> lPreProdLoadSystemList = new TreeSet();
	String lPlanStatus = lPlan.getPlanStatus();

	for (SystemLoad lSystemLoad : lSystemLoads) {
	    lSystemList.add(lSystemLoad.getSystemId().getName());
	    if (isYODA) {
		List<SystemLoadActions> lPreProductionLoads = getSystemLoadActionsDAO().findBySystemLoadEnv(lSystemLoad, env);
		int actCount = 0;
		for (SystemLoadActions lPreProdLoad : lPreProductionLoads) {
		    if (lPreProdLoad != null && (lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
			actCount++;
		    }
		}
		if (actCount != 0 && actCount == lPreProductionLoads.size()) {
		    lPreProdLoadSystemList.add(lSystemLoad.getSystemId().getName());
		}
	    } else {
		List<PreProductionLoads> lPreProductionLoads = getPreProductionLoadsDAO().findBySystemLoadId(lSystemLoad);
		int actCount = 0;
		LOG.info(lPreProductionLoads.size());
		for (PreProductionLoads lPreProdLoad : lPreProductionLoads) {
		    if (lPreProdLoad != null && (lPreProdLoad.getStatus().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()))) {
			actCount++;
		    }
		}
		if (actCount != 0 && actCount == lPreProductionLoads.size()) {
		    lPreProdLoadSystemList.add(lSystemLoad.getSystemId().getName());
		}
	    }
	}
	LOG.info("System count and Load Count " + lPreProdLoadSystemList.size() + "/" + lSystemList.size());
	LOG.info("Plan Status after Regression " + lPlanStatus);

	if (null != env) {
	    switch (env) {
	    case PRE_PROD: {
		if (lPreProdLoadSystemList.size() == lSystemList.size()) {
		    lPlanStatus = Constants.PlanStatus.DEPLOYED_IN_PRE_PRODUCTION.name();
		} else {
		    List<SystemLoad> lActiveSystemLoads = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList("NONE", Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name()));
		    lPlanStatus = Constants.PlanStatus.PARTIAL_REGRESSION_TESTING.name();
		    if (lActiveSystemLoads.isEmpty()) {
			lPlanStatus = Constants.PlanStatus.BYPASSED_REGRESSION_TESTING.name();
		    } else if (lSystemList.size() == lActiveSystemLoads.size()) {
			lPlanStatus = Constants.PlanStatus.PASSED_REGRESSION_TESTING.name();
		    }
		}
		break;
	    }
	    case QA_FUCTIONAL: {
		List<SystemLoad> lActiveSystemLoads = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList("NONE", Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name()));
		if (lPreProdLoadSystemList.size() == lActiveSystemLoads.size()) {
		    lPlanStatus = Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name();
		} else {
		    lPlanStatus = Constants.PlanStatus.APPROVED.name();
		}
		break;
	    }
	    case QA_REGRESSION: {
		List<SystemLoad> lActiveSystemLoads = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList("NONE", Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name()));
		if (lPreProdLoadSystemList.size() > 0) {
		    lPlanStatus = Constants.PlanStatus.DEPLOYED_IN_QA_REGRESSION.name();
		} else if (lPreProdLoadSystemList.size() == 0) {
		    List<SystemLoad> lByPassedSystems = getSystemLoadDAO().findPlanByQATestingStatus(lPlan.getId(), Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name()));
		    lPlanStatus = Constants.PlanStatus.PARTIAL_FUNCTIONAL_TESTING.name();
		    if (lByPassedSystems.isEmpty()) {
			lPlanStatus = Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name();
		    } else if (lSystemList.size() == lByPassedSystems.size()) {
			lPlanStatus = Constants.PlanStatus.BYPASSED_FUNCTIONAL_TESTING.name();
		    }
		}
		break;
	    }
	    default:
		break;
	    }
	}
	LOG.info("Plan status before updates of preProd " + lPlanStatus);
	if (!lPlan.getPlanStatus().equals(lPlanStatus)) {
	    lPlan.setPlanStatus(lPlanStatus);
	    getImpPlanDAO().update(user, lPlan);
	}
	return Boolean.TRUE;
    }

    public void sendMailNotificationQAFunTester(ImpPlan lImpPlan, boolean lAssign) {

	Map<String, String> qaInsAndTrgtSysMap = getSytemandQAInstruction(lImpPlan);
	// ZTPFM-1455 QA Functional Testers Name Notify the email
	Set<String> qaFunTestersName = new HashSet<String>();
	for (SystemLoad lSystemLoad : lImpPlan.getSystemLoadList()) {
	    if (lSystemLoad.getQaFunctionalTesters() != null) {
		List<String> qaFunctionalTesterList = Arrays.asList(lSystemLoad.getQaFunctionalTesters().split(","));
		for (String qaFunctionalTester : qaFunctionalTesterList) {
		    qaFunTestersName.add(getLDAPAuthenticatorImpl().getUserDetails(qaFunctionalTester).getDisplayName());
		}
	    }
	}
	Set<String> qaFunTesters = new HashSet<String>();
	for (SystemLoad lSystemLoad : lImpPlan.getSystemLoadList()) {
	    if (lSystemLoad.getQaFunctionalTesters() != null) {
		List<String> qaFunctionalTesterList = Arrays.asList(lSystemLoad.getQaFunctionalTesters().split(","));
		for (String qaFunctionalTester : qaFunctionalTesterList) {
		    qaFunTesters.add(qaFunctionalTester);
		}
	    }

	}
	List<String> lDevAndADLlist = getDevAndAdlList(lImpPlan);
	QAAssignmentMail qaAssignmentMail = (QAAssignmentMail) getMailMessageFactory().getTemplate(QAAssignmentMail.class);
	qaAssignmentMail.setPlanId(lImpPlan.getId());
	qaAssignmentMail.setQaFunctionalTestersList(qaFunTestersName);
	qaAssignmentMail.setProjectName(lImpPlan.getProjectId().getProjectName());
	qaAssignmentMail.setProjectCSR(lImpPlan.getProjectId().getProjectNumber());
	qaAssignmentMail.setAssignment(lAssign);
	qaAssignmentMail.setProgramNameTargetSys(qaInsAndTrgtSysMap);
	qaFunTesters.stream().forEach(t -> qaAssignmentMail.addToAddressUserId(t, Constants.MailSenderRole.QA_FUNCTIONAL));
	lDevAndADLlist.stream().forEach(c -> qaAssignmentMail.addcCAddressUserId(c, Constants.MailSenderRole.LEAD));
	getMailMessageFactory().push(qaAssignmentMail);
    }

    public void setLoadTypeChangePlan(User pUser, ImpPlan newPlan, ImpPlan oldPlan, String loadTypeChangeComment) {

	// Activity Log
	LoadTypeChangesActivityMessage lLoadTypeChangesActivityMessage = new LoadTypeChangesActivityMessage(newPlan, null);
	lLoadTypeChangesActivityMessage.setOldLoadType(oldPlan.getLoadType());
	lLoadTypeChangesActivityMessage.setLoadTypeComments(loadTypeChangeComment);
	getActivityLogDAO().save(pUser, lLoadTypeChangesActivityMessage);

	HashSet<String> lProblemTicketNumList = getPRNumberList(newPlan);
	Map<String, Date> oldLoadDateAndTrgtSysMap = getSytemandLoadDate(oldPlan);
	Map<String, Date> newLoadDateAndTrgtSysMap = getSytemandLoadDate(newPlan);
	List<String> lDevAndADLReviwerlist = getDevAndAdlReviwersList(newPlan);
	// Mail Notification
	LoadTypeChangeMail loadTypeChangeMail = (LoadTypeChangeMail) getMailMessageFactory().getTemplate(LoadTypeChangeMail.class);
	loadTypeChangeMail.setPlanId(newPlan.getId());
	loadTypeChangeMail.setPlanDescrption(newPlan.getPlanDesc());
	loadTypeChangeMail.setLoadType(newPlan.getLoadType());
	loadTypeChangeMail.setLoadTypeComment(loadTypeChangeComment);
	loadTypeChangeMail.setOldLoadDateTargetSys(oldLoadDateAndTrgtSysMap);
	loadTypeChangeMail.setNewLoadDateTargetSys(newLoadDateAndTrgtSysMap);
	loadTypeChangeMail.setPrNumberList(lProblemTicketNumList);
	lDevAndADLReviwerlist.stream().forEach(c -> loadTypeChangeMail.addToAddressUserId(c, Constants.MailSenderRole.DEVELOPER));
	if (wFConfig.getLoadsControlCentreMailId() != null) {
	    loadTypeChangeMail.addCcLoadsControlCentre();
	}
	getMailMessageFactory().push(loadTypeChangeMail);
    }

    private HashSet<String> getPRNumberList(ImpPlan newPlan) {
	// Adding Plan and Implementation Problem TicketNumer
	HashSet<String> lProblemTicketNumList = new HashSet<>();
	if (newPlan.getSdmTktNum() != null && !newPlan.getSdmTktNum().equals("")) {
	    lProblemTicketNumList.add(newPlan.getSdmTktNum());
	}
	List<Implementation> lImplementationList = getImplementationDAO().findByImpPlan(newPlan.getId());
	for (Implementation lImp : lImplementationList) {
	    if (lImp.getPrTktNum() != null && !lImp.getPrTktNum().equals("")) {
		lProblemTicketNumList.add(lImp.getPrTktNum());
	    }
	}
	return lProblemTicketNumList;
    }

    public List<String> getDevAndAdlReviwersList(ImpPlan lImpPlan) {
	// Getting Developer Id from Implementation
	List<String> lDevAndADLReviwerlist = new ArrayList<>();
	List<Implementation> lImplementationList = getImplementationDAO().findByImpPlan(lImpPlan.getId());

	String loadAttendees = lImpPlan.getSystemLoadList().stream().filter(t -> t.getLoadAttendee() != null).map(SystemLoad::getLoadAttendeeId).distinct().collect(Collectors.joining(","));
	String peerReviwer = lImplementationList.stream().filter(t -> t.getPeerReviewers() != null).map(Implementation::getPeerReviewers).distinct().collect(Collectors.joining(","));
	lDevAndADLReviwerlist.add(lImpPlan.getLeadId());
	lDevAndADLReviwerlist.add(lImpPlan.getDevManager());
	lDevAndADLReviwerlist.addAll(Arrays.asList(loadAttendees.split(",")));
	lDevAndADLReviwerlist.addAll(Arrays.asList(peerReviwer.split(",")));
	lImplementationList.forEach((lImplementation) -> {
	    lDevAndADLReviwerlist.add(lImplementation.getDevId());
	});
	return lDevAndADLReviwerlist;
    }

    public Map<String, Date> getSytemandLoadDate(ImpPlan lImpPlan) {
	// Map Target System Based on Load Date
	Map<String, Date> loadDateAndTrgtSysMap = new HashMap<String, Date>();
	lImpPlan.getSystemLoadList().stream().filter(t -> t.getId() != null && t.getLoadDateTime() != null && t.getActive().equalsIgnoreCase("Y")).forEach(lSystemName -> {
	    loadDateAndTrgtSysMap.put(lSystemName.getSystemId().getName(), lSystemName.getLoadDateTime());
	});
	return loadDateAndTrgtSysMap;
    }

    public List<String> getDevAndAdlList(ImpPlan lImpPlan) {
	// Getting Developer Id from Implementation
	List<String> lDevAndADLlist = new ArrayList<>();
	List<Implementation> lImplementationList = getImplementationDAO().findByImpPlan(lImpPlan.getId());
	lDevAndADLlist.add(lImpPlan.getLeadId());
	for (Implementation lImplementation : lImplementationList) {
	    lDevAndADLlist.add(lImplementation.getDevId());
	}
	return lDevAndADLlist;
    }

    public Map<String, String> getSytemandQAInstruction(ImpPlan lImpPlan) {
	// Map Target System Based on QA Tester's
	Map<String, String> qaInsAndTrgtSysMap = new HashMap<String, String>();
	lImpPlan.getSystemLoadList().stream().forEach(lSystemName -> {
	    // Based on understanding from Vinoth code changes done to correct the method
	    // function
	    if (!qaInsAndTrgtSysMap.containsKey(lSystemName.getSystemId().getName())) {
		qaInsAndTrgtSysMap.put(lSystemName.getSystemId().getName(), lSystemName.getSpecialInstructionQA() == null ? "None" : lSystemName.getSpecialInstructionQA());
	    }
	});
	return qaInsAndTrgtSysMap;
    }

    // Mail Notification send QA Functional Tester's Plan Pass or Fail
    public void sendMailNotificationQAFunTesterPassFail(ImpPlan lImpPlan, User pUser, String planStatus) {
	Set<String> qaFunTesters = new HashSet<String>();
	qaFunTesters.add(pUser.getId());
	for (SystemLoad lSystemLoad : lImpPlan.getSystemLoadList()) {
	    if (lSystemLoad.getQaFunctionalTesters() != null) {
		List<String> qaFunctionalTesterList = Arrays.asList(lSystemLoad.getQaFunctionalTesters().split(","));
		for (String qaFunctionalTester : qaFunctionalTesterList) {
		    qaFunTesters.add(qaFunctionalTester);
		}
	    }
	}
	List<String> lDevAndADLlist = getDevAndAdlList(lImpPlan);

	QAFunctionalTesterPassFailMail qaFunctionalTesterPassFailMail = (QAFunctionalTesterPassFailMail) getMailMessageFactory().getTemplate(QAFunctionalTesterPassFailMail.class);
	qaFunctionalTesterPassFailMail.setPlanId(lImpPlan.getId());
	qaFunctionalTesterPassFailMail.setProjectName(lImpPlan.getProjectId().getProjectName());
	qaFunctionalTesterPassFailMail.setProjectCSR(lImpPlan.getProjectId().getProjectNumber());
	qaFunctionalTesterPassFailMail.setPlanStatus(planStatus);
	qaFunctionalTesterPassFailMail.setCurrentUser(pUser.getDisplayName());
	qaFunctionalTesterPassFailMail.setCurrentRole(pUser.getCurrentRole());
	qaFunTesters.stream().forEach(t -> qaFunctionalTesterPassFailMail.addToAddressUserId(t, Constants.MailSenderRole.QA_FUNCTIONAL));
	lDevAndADLlist.stream().forEach(c -> qaFunctionalTesterPassFailMail.addcCAddressUserId(c, Constants.MailSenderRole.LEAD));
	getMailMessageFactory().push(qaFunctionalTesterPassFailMail);
    }

    // PeerReviwer Completed
    public void peerReviwerCompleted(User pUser, String pImplId, Implementation lImplementation, ImpPlan lPlan, List<String> lAllReviewers) {
	getActivityLogDAO().save(pUser, new ApproveReviewActivityMessage(lImplementation.getPlanId(), lImplementation));

	DeveloperNotificationMail developerNotificationMail = (DeveloperNotificationMail) getMailMessageFactory().getTemplate(DeveloperNotificationMail.class);
	developerNotificationMail.setImplementationId(pImplId);
	developerNotificationMail.setTicketUrl(lImplementation.getTktUrl());
	developerNotificationMail.setReviewer(pUser.getDisplayName());
	developerNotificationMail.setUser(pUser);
	developerNotificationMail.addToAddressUserId(lImplementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
	developerNotificationMail.addcCAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	for (String reviewer : lAllReviewers) {
	    developerNotificationMail.addcCAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
	}
	getMailMessageFactory().push(developerNotificationMail);
    }

    public JSONResponse putLevelSegmentValidation(SystemLoad systemLoad, String planId) throws Exception {
	JSONResponse lResponse = new JSONResponse();

	PutLevel lProdPutLevel = getPutLevelDAO().getPutLevel(systemLoad.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name());
	List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().getSegmentsBySystemAndPutAndPlan(planId, lProdPutLevel.getScmUrl(), systemLoad.getSystemId().getName());

	if (lSegmentList != null && !lSegmentList.isEmpty()) {
	    List<String> lSegmentName = new ArrayList();
	    lSegmentList.stream().forEach((segment) -> lSegmentName.add(segment.getFileName()));
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Error: IBM Source Artificats - " + String.join(",", lSegmentName) + " belonging to an NON-Production PUT level have not been removed from Implementation. Kindly do action to proceed further.");
	    return lResponse;
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public Integer getFailedOnlineJobCnt(String planId) {
	ImpPlan lPlan = getImpPlanDAO().find(planId);
	List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(lPlan);
	List<OnlineBuild> lOnlineBuilds = getOnlineBuildDAO().findAll(lPlan, Constants.BUILD_TYPE.ONL_BUILD);
	if (lSystemLoads.size() != lOnlineBuilds.size()) {
	    return -1;
	}

	for (OnlineBuild lOnlineBuild : lOnlineBuilds) {
	    if (lOnlineBuild.getJobStatus().equals("F")) {
		return 1;
	    } else if (lOnlineBuild.getJobStatus().equals("P")) {
		return -1;
	    }
	}
	return 0;
    }

    public void resetOnlineInProgressFlag(User user, String planId) {
	// Remove ONLINE InProgress Flag from ImpPlan Table during ACCEPT
	ImpPlan lPlan = getImpPlanDAO().find(planId);
	Long lSysCnt = getSystemLoadDAO().countByImpPlan(lPlan);
	List<ProductionLoads> lProdLoads = getProductionLoadsDAO().findByPlanId(lPlan);
	Integer lFailCnt = 0;
	Integer lPassCnt = 0;
	for (ProductionLoads lProdLoad : lProdLoads) {
	    if (lProdLoad.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
		return;
	    } else if (lProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACTIVATED.name()) && lProdLoad.getLastActionStatus().equalsIgnoreCase("FAILED")) {
		lFailCnt = lFailCnt + 1;
	    } else if (lProdLoad.getStatus().equalsIgnoreCase(Constants.LOAD_SET_STATUS.ACCEPTED.name()) && lProdLoad.getLastActionStatus().equalsIgnoreCase("SUCCESS")) {
		lPassCnt = lPassCnt + 1;
	    }
	}
	LOG.info("Fail/Pass Count - " + lFailCnt + " -> " + lPassCnt);
	if (lSysCnt.equals(Long.valueOf(lFailCnt + lPassCnt))) {
	    if (lFailCnt >= 1) {
		lPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
		getImpPlanDAO().update(user, lPlan);
	    }
	}
    }

    public JSONResponse planRCategoryValidation(String planId) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<SystemLoad> lSystemLoads = getSystemLoadDAO().findByImpPlan(planId);
	    lResponse = planRCategoryValidation(lSystemLoads);
	} catch (Exception ex) {
	    LOG.info("Error occurs in R Category Validation for Plan - " + planId, ex);
	    throw new WorkflowException("Error occurs in R Category Validation for Plan - " + planId);
	}
	return lResponse;
    }

    public JSONResponse planRCategoryValidation(List<SystemLoad> systemLoads) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<String> lErrorSystems = systemLoads.stream().filter(lSystemLoad -> (lSystemLoad.getLoadCategoryId() != null && lSystemLoad.getLoadCategoryId().getName().equalsIgnoreCase(Constants.RestrictedCatForAcceptPlan.R.name()) && (lSystemLoad.getLoadInstruction() == null || lSystemLoad.getLoadInstruction().isEmpty()))).map(t -> t.getSystemId().getName()).collect(Collectors.toList());
	    LOG.info("System List without Load Instructions" + lErrorSystems.toString());
	    if (!lErrorSystems.isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Unable to process your request, System - " + lErrorSystems.toString() + " are marked as R category and dont have special instruction for TSD. ");
	    }
	} catch (Exception ex) {
	    LOG.info("Error occurs in R Category Validation", ex);
	    throw new WorkflowException("Error occurs in R Category Validation");
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public JSONResponse getAndUpdateSOInfo(User user, String planId) throws Exception {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(Boolean.TRUE);
	Map<String, List<CheckoutSegments>> lSysVsSegments = new HashMap();
	List<CheckoutSegments> lAllSegments = getCheckoutSegmentsDAO().findByImpPlan(planId);

	if (lAllSegments == null || lAllSegments.isEmpty()) {
	    LOG.info("No Object Segments found for plan - " + planId);
	    return lReturn;
	}

	List<CheckoutSegments> lSegments = lAllSegments.stream().filter(ts -> (ts.getSoName() == null || ts.getSoName().isEmpty()) && (ts.getProgramName().endsWith(".sbt") || ts.getProgramName().endsWith(".cpp") || ts.getProgramName().endsWith(".c") || ts.getProgramName().endsWith(".asm"))).collect(Collectors.toList());

	if (lSegments == null || lSegments.isEmpty()) {
	    return lReturn;
	}

	lSysVsSegments = lSegments.stream().collect(Collectors.groupingBy(CheckoutSegments::getTargetSystem));
	for (Map.Entry<String, List<CheckoutSegments>> lSysSegments : lSysVsSegments.entrySet()) {
	    System lSystem = getSystemDAO().findByName(lSysSegments.getKey());
	    List<String> lFileNames = lSysSegments.getValue().stream().map(t -> t.getFileName()).collect(Collectors.toList());
	    // Input src/absc.asm,rb/base/sigt.cpp
	    String lCommand = Constants.SystemScripts.GET_SO_NAME_BY_FILE_NAME.getScript() + String.join(",", lFileNames);
	    LOG.info(lCommand);
	    JSONResponse lResponse = getsSHClientUtils().executeCommand(user, lSystem, lCommand);
	    if (!lResponse.getStatus()) {
		lReturn.setStatus(Boolean.FALSE);
		lReturn.setErrorMessage("Unable to get the SO information for Plan - " + planId);
		return lReturn;
	    } else {
		// output : src/absc.asm:ABSC.so,rb/base/sigt.cpp:SIGT.so
		LOG.info("Shell Response --> " + lResponse.getData().toString());
		String[] lSegVsSONames = lResponse.getData().toString().trim().split(",");

		if (lSegVsSONames == null || lSegVsSONames.length == 0) {
		    continue;
		}

		for (String lSegVsSOName : lSegVsSONames) {
		    String fileName = lSegVsSOName.trim().split(":")[0];
		    String soName = lSegVsSOName.trim().split(":")[1];
		    lSegments.stream().filter(t -> t.getTargetSystem().equalsIgnoreCase(lSystem.getName()) && t.getFileName().equalsIgnoreCase(fileName)).forEach((lSegment) -> {
			lSegment.setSoName(soName);
			getCheckoutSegmentsDAO().update(user, lSegment);
		    });
		}
	    }
	}
	return lReturn;
    }

    public Boolean clearPlanActionFromCache(String planId, String action) {
	Boolean isPlanAvailable = cacheClient.getPlanAndActions().containsKey(planId);
	if (!isPlanAvailable) {
	    return Boolean.TRUE;
	} else {
	    Set<String> planActions = cacheClient.getPlanAndActions().get(planId);
	    if (planActions == null) {
		return Boolean.TRUE;
	    }
	    if (planActions.contains(action)) {
		planActions.remove(action);
		cacheClient.getPlanAndActions().put(planId, planActions);
	    }
	}
	return Boolean.TRUE;
    }

    public Boolean addPlanActionFromCache(String planId, String action) {
	Boolean isPlanAvailable = cacheClient.getPlanAndActions().containsKey(planId);
	Set<String> planActions = new HashSet();
	if (!isPlanAvailable) {
	    planActions.add(action);
	    cacheClient.getPlanAndActions().put(planId, planActions);
	    return Boolean.TRUE;
	} else {
	    planActions = cacheClient.getPlanAndActions().get(planId);
	    if (planActions == null || planActions.isEmpty()) {
		planActions = new HashSet();
		planActions.add(action);
		cacheClient.getPlanAndActions().put(planId, planActions);
		return Boolean.TRUE;
	    } else {
		if (planActions.contains(action)) {
		    return Boolean.FALSE;
		}

		planActions.add(action);
		cacheClient.getPlanAndActions().put(planId, planActions);
		return Boolean.TRUE;
	    }
	}
    }

}
