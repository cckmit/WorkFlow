//createSourceArtifact - service pending
package com.tsi.workflow.service;

import static com.tsi.workflow.utils.Constants.lUploadFileTypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.ApproveReviewActivityMessage;
import com.tsi.workflow.activity.CheckinActivityMessage;
import com.tsi.workflow.activity.CheckoutActivityMessage;
import com.tsi.workflow.activity.CommitActivityMessage;
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.ImplementationCreationActivityMessage;
import com.tsi.workflow.activity.ImplementationReassignActivityMessage;
import com.tsi.workflow.activity.ImplementationStatusActivityMessage;
import com.tsi.workflow.activity.PeerReviewActivityMessage;
import com.tsi.workflow.activity.ReviewerAssignReAssignActivityMessage;
import com.tsi.workflow.activity.SystemAddActivityMessage;
import com.tsi.workflow.activity.TestResultsActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.GiPorts;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskReponse;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.GiPortsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.gi.request.ConfigFile;
import com.tsi.workflow.gi.request.Directory;
import com.tsi.workflow.gi.request.ProjectAddDirRule;
import com.tsi.workflow.gi.request.ProjectCreate;
import com.tsi.workflow.gi.request.ProjectDelete;
import com.tsi.workflow.gi.request.Request;
import com.tsi.workflow.gi.response.Project;
import com.tsi.workflow.gi.response.Projects;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.IJGITSearchUtils;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.gitblit.model.AccessPermission;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.UserModel;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.ImplementationHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.mail.CheckInMail;
import com.tsi.workflow.mail.CheckoutDeleteMail;
import com.tsi.workflow.mail.CheckoutSourceContentMail;
import com.tsi.workflow.mail.CommonMail;
import com.tsi.workflow.mail.DeveloperReassignmentAccessMail;
import com.tsi.workflow.mail.DeveloperReassignmentMail;
import com.tsi.workflow.mail.DivergedCheckoutSourceArtifactMail;
import com.tsi.workflow.mail.ImplementationStatusCompletionMail;
import com.tsi.workflow.mail.NewTargetSystemMail;
import com.tsi.workflow.mail.PeerReviewRequestMail;
import com.tsi.workflow.mail.ReviewerAssignmentMail;
import com.tsi.workflow.mail.ReviewerReassignmentMail;
import com.tsi.workflow.mail.UnsecuredCheckoutSourceContentMail;
import com.tsi.workflow.tdx.executor.TdxShellExecutor;
import com.tsi.workflow.tdx.executor.models.TdxShellExecutorModel;
import com.tsi.workflow.utils.CheckoutUtils;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.GIHTTPClient;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.SequenceGenerator;
import com.workflow.ssh.SSHClientUtils;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
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
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author USER
 */
@Service
public class DeveloperService extends BaseService {

    private static final Logger LOG = Logger.getLogger(DeveloperService.class.getName());
    private static final String ERROR_ON_EXECUTION = "Error on Execution : ";
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    PutLevelDAO putLevelDAO;
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
    GITConfig gITConfig;
    @Autowired
    LDAPAuthenticatorImpl authenticator;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    GITHelper gitHelper;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    ProtectedService protectedService;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    IJGITSearchUtils jGITSearchUtils;
    @Autowired
    ImplementationHelper implementationHelper;
    @Autowired
    GiPortsDAO giPortsDAO;
    @Autowired
    CommonHelper commonHelper;
    @Autowired
    CommonBaseService commonBaseService;
    @Autowired
    TdxShellExecutor tdxShellExecutor;

    public CommonBaseService getCommonBaseService() {
	return commonBaseService;
    }

    public IJGITSearchUtils getjGITSearchUtils() {
	return jGITSearchUtils;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public PutLevelDAO getPutLevelDAO() {
	return putLevelDAO;
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

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public ImplementationHelper getImplementationHelper() {
	return implementationHelper;
    }

    public TdxShellExecutor getTdxShellExecutor() {
	return tdxShellExecutor;
    }

    @Transactional
    public JSONResponse getImplementationList(User pUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setData(getImplementationDAO().findImpByDeveloper(pUser.getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), pOffset, pLimit, pOrderBy, pFilter));
	lResponse.setCount(getImplementationDAO().countImpByDeveloper(pUser.getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), pFilter).intValue());
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getUserTaskList(User pCurrentUser, Integer offset, Integer limit, Map<String, String> lOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    List<Implementation> lImpList;
	    List<String> lImplementationList = new ArrayList<>();
	    String lSortKey = "createTime";
	    String lSortValue = "desc";
	    if (lOrderBy != null) {
		for (Map.Entry<String, String> entrySet : lOrderBy.entrySet()) {
		    lSortKey = entrySet.getKey();
		    lSortValue = entrySet.getValue();
		}
	    }

	    TaskReponse taskReponse = getBPMClientUtils().getTaskList(pCurrentUser, pCurrentUser.getId(), Constants.BPM_IMPLEMENTATION_ID, (offset * limit), limit, lSortKey, lSortValue);

	    taskReponse.getData().stream().map((lTaskVariable) -> lTaskVariable.getVariables()).forEach((List<TaskVariable> variables) -> {
		variables.stream().filter((variable) -> (variable.getName().equalsIgnoreCase(Constants.BPM_IMPLEMENTATION_ID))).forEach((variable) -> {
		    lImplementationList.add(variable.getValue().toString());
		});
	    });

	    if (!lImplementationList.isEmpty()) {
		lImpList = getImplementationDAO().find(lImplementationList);
		lResponse.setData(lImpList);
	    }
	    lResponse.setCount(taskReponse.getTotal());
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    throw new WorkflowException("Unable to get list of Task", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse saveImplementation(User pUser, Implementation pImplementation) throws WorkflowException {
	boolean bypasspeerreview;
	// TODO: Need to change to DeveoperLead
	JSONResponse lResponse = new JSONResponse();
	pImplementation.setReassignFlag("N");
	pImplementation.setReviewMailFlag(Boolean.FALSE);
	ImpPlan lPlan = getImpPlanDAO().find(pImplementation.getPlanId().getId());
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lPlan);
	try {
	    // String imp_id = getSequenceGenerator().getNewImplementationId(lPlan.getId());
	    String imp_id = getNewImplementationId(lPlan.getId());
	    pImplementation.setId(imp_id);

	    // User userDetails =
	    // getLDAPAuthenticatorImpl().getUserDetails(pImplementation.getDevId());
	    // TODO: Need to send Mail
	    String lProcessId = getBPMClientUtils().createDeveloperProcess(pUser, imp_id);

	    if (lProcessId != null && !lProcessId.isEmpty()) {
		pImplementation.setProcessId(lProcessId);
		List<TaskVariable> lTaskVars = new ArrayList<>();
		lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_ID, imp_id));
		getBPMClientUtils().assignTask(pUser, lProcessId, pImplementation.getDevId(), lTaskVars);
		List<String> reviewerList = Arrays.asList(pImplementation.getPeerReviewers().split(","));
		List<String> reviewerNamesIds = new ArrayList<>();
		if (!pImplementation.getBypassPeerReview()) {
		    bypasspeerreview = true;
		    for (String reviewer : reviewerList) {
			User lReviewer = getLDAPAuthenticatorImpl().getUserDetails(reviewer);
			if (lReviewer != null && lReviewer.getDisplayName() != null) {
			    reviewerNamesIds.add(lReviewer.getDisplayName());
			}
		    }
		}
		pImplementation.setPeerReviewersName(String.join(",", reviewerNamesIds));
		getImplementationDAO().save(pUser, pImplementation);
		getActivityLogDAO().save(pUser, new ImplementationCreationActivityMessage(pImplementation.getPlanId(), pImplementation));

		ReviewerAssignReAssignActivityMessage lReviewerActivity = new ReviewerAssignReAssignActivityMessage(pImplementation.getPlanId(), pImplementation);
		lReviewerActivity.setUser(pUser);
		if (!pImplementation.getBypassPeerReview()) {
		    lReviewerActivity.setReviewerList(reviewerNamesIds);
		    lReviewerActivity.setAction("added");
		}
		getActivityLogDAO().save(pUser, lReviewerActivity);

		String lCompanyName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();// platformDAO.findPlatformByPlanId(planId);
		Set<String> implementationBranchList = new HashSet<>();
		for (SystemLoad lSystemLoad : lSystemLoadList) {
		    implementationBranchList.add(pImplementation.getId() + "_" + lSystemLoad.getSystemId().getName().toLowerCase());
		    implementationBranchList.add(Constants.BRANCH_MASTER + lSystemLoad.getSystemId().getName().toLowerCase());
		}
		getJGitClientUtils().createBranches(lPlan.getId(), implementationBranchList, lCompanyName);
		// String repositoryName =
		// getJGitClientUtils().getPlanRepoFullName(lCompanyName, lPlan.getId());
		// getGitBlitClientUtils().setPermissionForGitRepository(repositoryName,
		// pImplementation.getDevId(),
		// Constants.GIT_PERMISSION_READWRITE);
		getGitHelper().updateImplementationPlanRepoPermissions(lPlan.getId(), null);
		// Adding Plan and Implementation Problem TicketNumer
		HashSet<String> lProblemTicketNumList = new HashSet<>();
		if (lPlan.getSdmTktNum() != null && !lPlan.getSdmTktNum().equals("")) {
		    lProblemTicketNumList.add(lPlan.getSdmTktNum());
		}
		if (pImplementation.getPrTktNum() != null && !pImplementation.getPrTktNum().equals("")) {
		    lProblemTicketNumList.add(pImplementation.getPrTktNum());
		}

		ReviewerAssignmentMail reviewerAssignmentMailNotification = (ReviewerAssignmentMail) getMailMessageFactory().getTemplate(ReviewerAssignmentMail.class);
		reviewerAssignmentMailNotification.setImplementation(pImplementation);
		reviewerAssignmentMailNotification.setUserDetails(pUser);
		reviewerAssignmentMailNotification.setProjectName(lPlan.getProjectId().getProjectName());
		reviewerAssignmentMailNotification.setProblemTicketNum(lProblemTicketNumList);
		reviewerAssignmentMailNotification.addcCAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		getMailMessageFactory().push(reviewerAssignmentMailNotification);

		DeveloperReassignmentMail developerReassignmentMail = (DeveloperReassignmentMail) getMailMessageFactory().getTemplate(DeveloperReassignmentMail.class);
		developerReassignmentMail.setImplementation(pImplementation);
		developerReassignmentMail.setNewDeveloperName(pImplementation.getDevId());
		developerReassignmentMail.setUserDetails(pUser);
		getMailMessageFactory().push(developerReassignmentMail);

	    } else {
		throw new WorkflowException("Unable to create implementation due to BPM Process creation error");
	    }
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setMetaData(pImplementation.getId());
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    throw new WorkflowException("Unable to create Implementation", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse updateImplementation(User pUser, Implementation pImplementation) throws WorkflowException {
	// TODO: Need to change to Protected
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation lImplementation = getImplementationDAO().find(pImplementation.getId());
	    ImpPlan lImpPlan = lImplementation.getPlanId();
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lImpPlan);
	    String lNewSubStatus = pImplementation.getSubstatus();
	    String lOldSubStatus = lImplementation.getSubstatus();
	    List<String> lOldReviewers = new ArrayList<>();
	    List<String> lNewReviewers = new ArrayList<>();
	    List<String> userToBeRemoved = new ArrayList<>();
	    // 1853 - PutLevel Validation
	    if (!lImplementation.getCheckoutSegmentsList().isEmpty()) {
		for (SystemLoad lSystemLoad : lSystemLoadList) {
		    JSONResponse lPutResponse = getImplementationHelper().putLevelSegmentValidation(lSystemLoad, lImplementation.getId());
		    if (!lPutResponse.getStatus()) {
			CommonActivityMessage lMessage = new CommonActivityMessage(lImpPlan, null);
			lMessage.setMessage(lPutResponse.getErrorMessage());
			lMessage.setStatus("Fail");
			getActivityLogDAO().save(pUser, lMessage);
			return lPutResponse;
		    }
		}
	    }

	    if (!lImplementation.getPeerReviewers().isEmpty()) {
		lOldReviewers = Arrays.asList(lImplementation.getPeerReviewers().split(","));
		userToBeRemoved.addAll(lOldReviewers);
	    }
	    if (!pImplementation.getPeerReviewers().isEmpty()) {
		lNewReviewers = Arrays.asList(pImplementation.getPeerReviewers().split(","));
	    }

	    // Removed Peer Reviewer
	    Set<String> removedReviwers = new HashSet<>();
	    for (String reviewer : lOldReviewers) {
		if (!lNewReviewers.contains(reviewer)) {
		    removedReviwers.add(getLDAPAuthenticatorImpl().getUserDetails(reviewer).getId());
		}
	    }
	    // Done Review Validation
	    if (lImplementation.getReviewersDone() != null && !lImplementation.getReviewersDone().isEmpty()) {
		Set<String> lDoneReviewers = new HashSet<>(Arrays.asList(lImplementation.getReviewersDone().split("\\,")));
		List<String> lDoneReviewersName = new ArrayList<>();
		for (String removedReviwer : removedReviwers) {
		    if (lDoneReviewers.contains(removedReviwer)) {
			for (String doneReviwer : lDoneReviewers) {
			    lDoneReviewersName.add(getLDAPAuthenticatorImpl().getUserDetails(doneReviwer).getDisplayName());
			}
			throw new WorkflowException(String.join(",", lDoneReviewersName) + " has already completed peer reviewer ");
		    }

		}

	    }

	    if (lNewSubStatus != null && !lNewSubStatus.equalsIgnoreCase(lOldSubStatus)) {
		String lCompanyName = "";
		String lRepoName = "";
		List<String> lBranchList = new ArrayList();

		if (lNewSubStatus.equalsIgnoreCase(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name()) || lNewSubStatus.equalsIgnoreCase(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name())) {
		    lCompanyName = lImpPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		    lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, lImpPlan.getId().toLowerCase());
		    lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, lImpPlan.getId());
		    for (int i = 0; i < lBranchList.size(); i++) {
			if (!lBranchList.get(i).contains(pImplementation.getId().toLowerCase())) {
			    lBranchList.remove(i);
			}
		    }
		}

		if (lNewSubStatus.equalsIgnoreCase(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name())) {
		    Set<System> systemsWithSegList = new HashSet<>();
		    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findByImplementation(lImplementation.getId());
		    for (CheckoutSegments seg : lSegmentList) {
			if (seg.getActive().equals("Y")) {
			    systemsWithSegList.add(seg.getSystemLoad().getSystemId());
			}
		    }

		    for (System sys : systemsWithSegList) {
			JSONResponse sshResponse = getsSHClientUtils().executeCommand(pUser, sys, Constants.SystemScripts.MAK_FILE_VALIDATION.getScript() + lImplementation.getId().toLowerCase() + "_" + sys.getName().toLowerCase());
			LOG.info(".MAK File validation Status: " + sshResponse.getStatus() + " Error Message: " + sshResponse.getErrorMessage());
			// IF .MAK file is available proceed further else throw error and log it in
			// activity log
			if (!sshResponse.getStatus()) {
			    PeerReviewActivityMessage lPeerReviewActivityMessage = new PeerReviewActivityMessage(lImplementation.getPlanId(), lImplementation);
			    lPeerReviewActivityMessage.setMessage(sshResponse.getErrorMessage().replace("Error Code: 8 ERROR:", ""));
			    lPeerReviewActivityMessage.setStatus(false);
			    activityLogDAO.save(pUser, lPeerReviewActivityMessage);
			    lResponse.setStatus(Boolean.FALSE);
			    if (sshResponse.getErrorMessage().contains("Please do commit and check-in and try again.")) {
				lResponse.setErrorMessage(sshResponse.getErrorMessage().replace("Error Code: 8", ""));
			    } else {
				lResponse.setErrorMessage(".MAK File not found. Check Activity log for more detail.");
			    }
			    return lResponse;
			}
		    }
		    if (!lBranchList.isEmpty()) {
			if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED, lBranchList)) {
			    LOG.error("Unit Testing Tagging is not completed for Implementation Id -" + pImplementation.getId());
			    throw new WorkflowException("Unable to tag the Implementation as Unit Testing Completed. Please try again. ");
			}
			ImplementationStatusActivityMessage lMessage = new ImplementationStatusActivityMessage(pImplementation.getPlanId(), pImplementation);
			lMessage.setStatus("Unit Testing ");
			getActivityLogDAO().save(pUser, lMessage);
		    }
		    if (lImplementation.getBypassPeerReview() != null && lImplementation.getBypassPeerReview()) {
			LOG.info("Since Emergency Load & Bypassed, Changing into Bypass Peer Review");
			pImplementation.setSubstatus(Constants.ImplementationSubStatus.BYPASSED_PEER_REVIEW.name());
		    } else {
			// 2413 - If there no segment for peer review then mark the implementation as
			// "PEER REVIEW COMPLETED"
			List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImplementation(lImplementation);
			if (!lSegments.stream().filter(t -> t.getReviewStatus() != null && t.getReviewStatus().equals(Boolean.FALSE)).findAny().isPresent()) {
			    JSONResponse lReviewRequest = markImplementationAsReviewCompleted(pUser, lImplementation);
			    if (lReviewRequest.getStatus()) {
				// 2413 - Externally updating the Implementation status and peerreview indicator
				// as we are updating the DB based on UI based object
				pImplementation.setPeerReview("Y");
				pImplementation.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
			    }
			}
		    }

		} else if (lNewSubStatus.equalsIgnoreCase(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name())) {
		    if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED, lBranchList)) {
			LOG.error("Integration Testing Completed Tagging is not completed for Implementation Id -" + pImplementation.getId());
			throw new WorkflowException("Unable to tag the Implementation as Integration Testing Completed. Please try again. ");
		    }
		    ImplementationStatusActivityMessage lMessage = new ImplementationStatusActivityMessage(pImplementation.getPlanId(), pImplementation);
		    lMessage.setStatus("Integration Testing ");
		    getActivityLogDAO().save(pUser, lMessage);
		    ImplementationStatusCompletionMail statusChangeNotification = (ImplementationStatusCompletionMail) getMailMessageFactory().getTemplate(ImplementationStatusCompletionMail.class);
		    statusChangeNotification.setImpPlan(lImpPlan);
		    statusChangeNotification.setImplementation(lImplementation);
		    statusChangeNotification.setStatus("Integration Testing ");
		    getMailMessageFactory().push(statusChangeNotification);
		}
	    }
	    /**
	     * Developer Reassignment
	     */
	    if (!lImplementation.getDevId().equalsIgnoreCase(pImplementation.getDevId())) {
		getBPMClientUtils().removeUserFromTask(pUser, pImplementation.getProcessId());
		getBPMClientUtils().assignTask(pUser, pImplementation.getProcessId(), pImplementation.getDevId(), new ArrayList<>());
		Long implCount = getImplementationDAO().countBy(lImpPlan, lImplementation.getDevId());
		if (implCount == 1) {
		    userToBeRemoved.add(lImplementation.getDevId());
		}
		pImplementation.setReassignFlag("Y");

		ImplementationReassignActivityMessage lMessage = new ImplementationReassignActivityMessage(lImpPlan, pImplementation);
		lMessage.setOldUser(lImplementation.getDevName());
		getActivityLogDAO().save(pUser, lMessage);
		DeveloperReassignmentMail developerReassignmentMail = (DeveloperReassignmentMail) getMailMessageFactory().getTemplate(DeveloperReassignmentMail.class);
		developerReassignmentMail.setImplementation(lImplementation);
		developerReassignmentMail.setOldDeveloperName(lImplementation.getDevId());
		developerReassignmentMail.setNewDeveloperName(pImplementation.getDevId());
		developerReassignmentMail.setUserDetails(pUser);
		getMailMessageFactory().push(developerReassignmentMail);

		String lCompanyName = lImplementation.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		Set<String> lUserAllowedRepos = getGitHelper().getUserAllowedReadAndWriteRepos(pImplementation.getDevId(), lCompanyName);
		if (lUserAllowedRepos == null) {
		    throw new WorkflowException("Unable to update Implementation, new developer dont have access permission for any repositories.");
		}
		Set<String> lRepoList = new HashSet();
		for (String lRepo : lUserAllowedRepos) {
		    lRepoList.add(getGitHelper().getSourceURLByRepoName(lRepo));
		}

		List<CheckoutSegments> lSegmentWithNoAccess = new ArrayList();
		lSegmentWithNoAccess = getCheckoutSegmentsDAO().getSegmentWithNoAccess(lImplementation.getId(), lRepoList);

		Map<String, List<String>> lRepoSegmensMapper = new HashMap();
		List<String> lPgmName = new ArrayList();
		for (CheckoutSegments lSegment : lSegmentWithNoAccess) {
		    String lRepoName = getGitHelper().getRepositoryNameBySourceURL(lSegment.getSourceUrl());
		    lPgmName.add(lSegment.getProgramName());
		    if (!lRepoSegmensMapper.containsKey(lSegment.getSourceUrl())) {
			List<String> lSegmentName = new ArrayList();
			lSegmentName.add(lSegment.getProgramName());
			lRepoSegmensMapper.put(lRepoName, lSegmentName);
		    } else {
			lRepoSegmensMapper.get(lRepoName).add(lSegment.getProgramName());
		    }
		}
		String lDevDisplayName = getLDAPAuthenticatorImpl().getUserDetails(pImplementation.getDevId()).getDisplayName();

		for (Map.Entry<String, List<String>> lSegmentMapper : lRepoSegmensMapper.entrySet()) {
		    UserModel lUserModel = getCacheClient().getAllRepoUsersMap().get(pImplementation.getDevId());
		    AccessPermission lRepoAccess = lUserModel.getPermissions().get(lSegmentMapper.getKey());
		    if (!lRepoAccess.toString().startsWith("RW")) {
			RepositoryView lRepo = getCacheClient().getFilteredRepositoryMap().get(lSegmentMapper.getKey().replace(".git", "").toUpperCase());
			ArrayList<String> mailingOwmers = new ArrayList<>();
			for (String lOwnersId : lRepo.getRepository().getOwners()) {
			    UserSettings lUserSettings = getUserSettingsDAO().find(lOwnersId, Constants.UserSettings.REPO_OWNER_ALERT.name());
			    if (lUserSettings != null && lUserSettings.getValue().equals("Y")) {
				mailingOwmers.add(lOwnersId);
			    }
			}
			if (!mailingOwmers.isEmpty()) {
			    DeveloperReassignmentAccessMail lReassignMail = (DeveloperReassignmentAccessMail) getMailMessageFactory().getTemplate(DeveloperReassignmentAccessMail.class);
			    lReassignMail.setRepoName(lRepo.getRepository().getTrimmedName());
			    lReassignMail.setDevId(lDevDisplayName);
			    lReassignMail.setImpId(lImplementation.getId());
			    lReassignMail.setProgramName(lSegmentMapper.getValue());
			    mailingOwmers.stream().forEach(t -> lReassignMail.addToAddressUserId(t, Constants.MailSenderRole.REPO_OWNERS));
			    LOG.info("DevManager Recevie Notification " + pUser.getId());
			    getMailMessageFactory().push(lReassignMail);
			}
		    }
		}
		if (!lPgmName.isEmpty()) {
		    lResponse.setErrorMessage(lDevDisplayName + " could not have permission to update source artificats " + String.join(",", lPgmName));
		}
	    }
	    /**
	     * On Peer Reviewer Change
	     */
	    if (!lNewReviewers.equals(lOldReviewers)) {
		List<String> currentReviewersList = Arrays.asList(pImplementation.getPeerReviewers().split(","));
		List<String> currentreviewerNameList = new ArrayList<>();
		List<String> allReviewers = new ArrayList<>();

		for (String reviewer : currentReviewersList) {
		    currentreviewerNameList.add(getLDAPAuthenticatorImpl().getUserDetails(reviewer).getDisplayName());
		}

		pImplementation.setPeerReviewersName(String.join(",", currentreviewerNameList));

		List<String> removedReviewersName = new ArrayList<>();
		List<String> newReviewersName = new ArrayList<>();

		for (String reviewer : lOldReviewers) {
		    if (!lNewReviewers.contains(reviewer)) {
			removedReviewersName.add(getLDAPAuthenticatorImpl().getUserDetails(reviewer).getDisplayName());
		    }
		}
		for (String reviewer : lNewReviewers) {
		    if (!lOldReviewers.contains(reviewer)) {
			newReviewersName.add(getLDAPAuthenticatorImpl().getUserDetails(reviewer).getDisplayName());
		    }
		}

		allReviewers.addAll(lOldReviewers);
		allReviewers.addAll(lNewReviewers);

		/**
		 * Activity Log
		 */
		ReviewerAssignReAssignActivityMessage lReviewerActivity = new ReviewerAssignReAssignActivityMessage(pImplementation.getPlanId(), pImplementation);
		lReviewerActivity.setUser(pUser);
		if (!newReviewersName.isEmpty()) {
		    lReviewerActivity.setReviewerList(newReviewersName);
		    lReviewerActivity.setAction("added");
		    getActivityLogDAO().save(pUser, lReviewerActivity);
		}
		if (!removedReviewersName.isEmpty()) {
		    lReviewerActivity.setReviewerList(removedReviewersName);
		    lReviewerActivity.setAction("removed");
		    getActivityLogDAO().save(pUser, lReviewerActivity);
		}

		// Adding Plan and Implementation Problem TicketNumer
		HashSet<String> lProblemTicketNumList = new HashSet<>();
		lProblemTicketNumList.add(lImpPlan.getSdmTktNum());
		lProblemTicketNumList.add(pImplementation.getPrTktNum());
		/**
		 * Mails
		 */
		if (!newReviewersName.isEmpty()) {
		    ReviewerReassignmentMail reviewerReassignmentMail = (ReviewerReassignmentMail) getMailMessageFactory().getTemplate(ReviewerReassignmentMail.class);
		    reviewerReassignmentMail.setRemoved(false);
		    reviewerReassignmentMail.setImplementationId(pImplementation.getId());
		    reviewerReassignmentMail.setUser(pUser);
		    reviewerReassignmentMail.setReviewersId(allReviewers);
		    reviewerReassignmentMail.setProblemTicketNum(lProblemTicketNumList);
		    reviewerReassignmentMail.setDescription(pImplementation.getImpDesc());
		    reviewerReassignmentMail.setProjectName(lImpPlan.getProjectId().getProjectName());
		    reviewerReassignmentMail.setAddedreviewersName(newReviewersName);
		    reviewerReassignmentMail.addcCAddressUserId(lImpPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		    if (!newReviewersName.isEmpty() && !removedReviewersName.isEmpty()) {
			reviewerReassignmentMail.setReAssigned(true);
			reviewerReassignmentMail.setRemovedreviewersName(removedReviewersName);
		    }
		    if (lImplementation.getTktNum() != null && !lImplementation.getTktNum().isEmpty() && lImplementation.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name())) {
			pImplementation.setTktNum(null);
			pImplementation.setTktUrl(null);
			lImplementation.setSubstatus(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name());
			reviewerReassignmentMail.setNewRequest(true);
		    }
		    reviewerReassignmentMail.setDeveloperId(pImplementation.getDevId());
		    getMailMessageFactory().push(reviewerReassignmentMail);
		}
		if (!removedReviewersName.isEmpty() && newReviewersName.isEmpty()) {
		    ReviewerReassignmentMail reviewerReassignmentMail = (ReviewerReassignmentMail) getMailMessageFactory().getTemplate(ReviewerReassignmentMail.class);
		    reviewerReassignmentMail.setRemoved(true);
		    reviewerReassignmentMail.setImplementationId(pImplementation.getId());
		    reviewerReassignmentMail.setUser(pUser);
		    reviewerReassignmentMail.setProblemTicketNum(lProblemTicketNumList);
		    reviewerReassignmentMail.setDescription(pImplementation.getImpDesc());
		    reviewerReassignmentMail.setDeveloperId(pImplementation.getDevId());
		    reviewerReassignmentMail.setProjectName(lImpPlan.getProjectId().getProjectName());
		    reviewerReassignmentMail.setReviewersId(allReviewers);
		    reviewerReassignmentMail.setRemovedreviewersName(removedReviewersName);
		    reviewerReassignmentMail.addcCAddressUserId(lImpPlan.getLeadId(), Constants.MailSenderRole.LEAD);
		    getMailMessageFactory().push(reviewerReassignmentMail);
		}
	    }
	    setReviwersDone(pImplementation, lImplementation, lImpPlan, lNewSubStatus, lNewReviewers);
	    getImplementationDAO().update(pUser, pImplementation);
	    getGitHelper().updateImplementationPlanRepoPermissions(lImpPlan.getId(), userToBeRemoved);
	    lResponse.setStatus(Boolean.TRUE);
	} catch (WorkflowException ex) {
	    lResponse.setErrorMessage(ex.getMessage());
	    throw ex;
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    lResponse.setErrorMessage(ex.getMessage());
	    throw new WorkflowException("Unable to update Implementation", ex);
	}
	return lResponse;
    }

    private void setReviwersDone(Implementation pImplementation, Implementation lImplementation, ImpPlan lImpPlan, String lNewSubStatus, List<String> lNewReviewers) {
	// ZTPFM-1790
	if (lImplementation.getReviewersDone() != null && !lImplementation.getReviewersDone().isEmpty()) {
	    Set<String> lDoneReviewers = new HashSet<>(Arrays.asList(lImplementation.getReviewersDone().split("\\,")));
	    List<String> lAllReviewers = new ArrayList<>(Arrays.asList(pImplementation.getPeerReviewers().split("\\,")));
	    if (lDoneReviewers.containsAll(lNewReviewers) && pImplementation.getTktUrl() != null && lNewSubStatus.equalsIgnoreCase(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name())) {
		pImplementation.setPeerReview("Y");
		pImplementation.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
		for (String userId : lDoneReviewers) {
		    User lUser = getLDAPAuthenticatorImpl().getUserDetails(userId);
		    lUser.setCurrentRole("Reviewer");
		    getPlanHelper().peerReviwerCompleted(lUser, pImplementation.getId(), pImplementation, lImpPlan, lAllReviewers);
		}
	    }
	}
    }

    @Transactional
    public JSONResponse requestPeerReview(User pUser, String pImplId) {
	JSONResponse lJSONResponse = new JSONResponse();

	try {
	    Implementation lImpl = getImplementationDAO().find(pImplId);

	    if (lImpl.getTktNum() == null || lImpl.getTktNum().isEmpty()) {
		// TODO: Error in Mail id going to prabhu.prabhakaran@VHLDVZTDT001.tvlport.net
		// instead of
		// prabhu.prabhakaran@travelport.com
		List<String> reviewerList = Arrays.asList(lImpl.getPeerReviewers().split(","));
		ImpPlan planId = lImpl.getPlanId();
		System lSystem = lImpl.getPlanId().getSystemLoadList().get(0).getSystemId();
		String nickName = lSystem.getPlatformId().getNickName();
		String lRepoUrl = getJGitClientUtils().getPlanSSHURL(nickName, planId.getId(), pUser.getId());
		JSONResponse sshResponse = getsSHClientUtils().executeCommand(pUser, lSystem, Constants.GitScripts.CREATE_TICKET.getScript() + lRepoUrl + " " + pImplId.toLowerCase() + " " + lImpl.getPeerReviewers());

		if (sshResponse.getStatus()) {
		    String lTicket = (String) sshResponse.getData();
		    String lTicketURL = getGitBlitClientUtils().getImplementationTicketURL(nickName, planId.getId(), lTicket);
		    lImpl.setTktUrl(lTicketURL);
		    lImpl.setTktNum(lTicket.trim());
		    lImpl.setPeerReviewRequestDateTime(new Date());
		    getImplementationDAO().update(pUser, lImpl);
		    lJSONResponse.setStatus(Boolean.TRUE);

		    ApproveReviewActivityMessage lMessage = new ApproveReviewActivityMessage(lImpl.getPlanId(), lImpl);
		    lMessage.setReviewRequest(true);
		    activityLogDAO.save(pUser, lMessage);

		    // getting programName and TargetSystem form checkoutSegment
		    List<CheckoutSegments> lSegementList = getCheckoutSegmentsDAO().getSgementsByPlan(lImpl.getId());
		    Map<String, List<String>> progNameAndTrgtSysMap = new HashMap<String, List<String>>();
		    lSegementList.stream().forEach(checkoutSeg -> {
			List<String> targetSystemList = new ArrayList<>();
			if (progNameAndTrgtSysMap.containsKey(checkoutSeg.getProgramName())) {
			    targetSystemList = progNameAndTrgtSysMap.get(checkoutSeg.getProgramName());
			}
			targetSystemList.add(checkoutSeg.getTargetSystem());
			progNameAndTrgtSysMap.put(checkoutSeg.getProgramName(), targetSystemList);
		    });

		    // Adding Problem TicketNum Both plan and implementation
		    HashSet<String> lProblemTicket = new HashSet<>();
		    lProblemTicket.add(lImpl.getPrTktNum());
		    lProblemTicket.add(lImpl.getPlanId().getSdmTktNum());

		    PeerReviewRequestMail peerReviewRequestMail = (PeerReviewRequestMail) getMailMessageFactory().getTemplate(PeerReviewRequestMail.class);
		    peerReviewRequestMail.setReviewerList(reviewerList);
		    peerReviewRequestMail.setDeveloper(pUser.getDisplayName());
		    peerReviewRequestMail.setImplementationId(pImplId);
		    peerReviewRequestMail.setProblemTicketNum(lProblemTicket);
		    peerReviewRequestMail.setImpDesc(lImpl.getImpDesc());
		    peerReviewRequestMail.setProjectName(lImpl.getPlanId().getProjectId().getProjectName());
		    peerReviewRequestMail.setProgramNameTargetSys(progNameAndTrgtSysMap);
		    peerReviewRequestMail.setTicketUrl(lTicketURL);
		    peerReviewRequestMail.addcCAddressUserId(lImpl.getDevId(), Constants.MailSenderRole.DEVELOPER);
		    peerReviewRequestMail.addcCAddressUserId(lImpl.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
		    getMailMessageFactory().push(peerReviewRequestMail);
		    // getGitBlitClientUtils().refreshIndex(nickName, planId.getId());
		} else {
		    lJSONResponse.setErrorMessage("Unable to create ticket " + sshResponse.getDisplayErrorMessage());
		    lJSONResponse.setStatus(Boolean.FALSE);
		}
	    } else {
		lJSONResponse.setErrorMessage("Ticket already exists.");
		lJSONResponse.setStatus(Boolean.FALSE);
	    }
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    throw new WorkflowException("Unable to create ticket", ex);
	}
	return lJSONResponse;
    }

    @Transactional
    public JSONResponse deleteFile(User pUser, Integer[] ids) {
	// TODO need to revisit the code
	JSONResponse lJSONResponse = new JSONResponse();

	if (getWFConfig().getRunShellScriptParallel()) {
	    return deleteFileParallellyForSystems(pUser, ids);
	}

	HashMap<String, HashMap> lResult = new HashMap<>();
	Map<System, List<CheckoutSegments>> lSystemsWiseSegments = new HashMap<>();
	lJSONResponse.setStatus(Boolean.TRUE);
	String lImpId = "";

	String planId = null;
	List<String> targetSystems = new ArrayList<>();
	String programName = null;
	try {
	    List<CheckoutSegments> segsToBeDeleted = new ArrayList<>();
	    for (Integer id : ids) {
		lJSONResponse.setStatus(Boolean.TRUE);
		CheckoutSegments lSegment = getCheckoutSegmentsDAO().find(id);
		lImpId = lSegment.getImpId().getId();
		System lSystem = getSystemDAO().findByName(lSegment.getTargetSystem());

		planId = lSegment.getPlanId().getId();
		programName = lSegment.getProgramName();
		targetSystems.add(lSegment.getTargetSystem());

		if (lSystemsWiseSegments.containsKey(lSystem)) {
		    List<CheckoutSegments> lSegments = lSystemsWiseSegments.get(lSystem);
		    lSegments.add(lSegment);
		    lSystemsWiseSegments.put(lSystem, lSegments);
		} else {
		    List<CheckoutSegments> lSegments = new ArrayList<>();
		    lSegments.add(lSegment);
		    lSystemsWiseSegments.put(lSystem, lSegments);
		}
	    }
	    Implementation impl = getImplementationDAO().find(lImpId);
	    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findByImplementation(lImpId);
	    Map<String, List<CheckoutSegments>> lGrouped = lSegmentList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));

	    Map<String, List<String>> systemBasedChangedSegs = new HashMap<>();
	    for (Map.Entry<System, List<CheckoutSegments>> lSystemWiseSegment : lSystemsWiseSegments.entrySet()) {
		System lSystem = lSystemWiseSegment.getKey();
		List<CheckoutSegments> lSegments = lSystemWiseSegment.getValue();

		lResult.put(lSystem.getName(), new HashMap<>());
		// Auto checkin when file deleted

		// ZTPFM-2447 Code changes to add Last check in date in checkin script param
		String lastCheckInDateTime = (impl.getCheckinDateTime() != null) ? Constants.JENKINS_DATEFORMAT.get().format(impl.getCheckinDateTime()) : "NULL";
		String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (lImpId + "_" + lSystem.getName()).toLowerCase() + " " + lastCheckInDateTime;
		String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (lImpId + "_" + lSystem.getName()).toLowerCase();
		String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + pUser.getId() + "/projects/" + (lImpId + "/" + lSystem.getName()).toLowerCase() + " \"" + (lImpId + "_" + lSystem.getName()).toLowerCase() + "\" \"" + getGITConfig().getServiceUserID() + "\"";

		JSONResponse lCheckinResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lCheckInCommand);

		// ZTPFM-2447 Add the list of changed segments
		String changedSegs = null;
		String deletedSegs = null;
		if (lCheckinResponse.getStatus()) {
		    Optional<String> changedString = Arrays.asList(lCheckinResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("changedFiles")).findAny();
		    changedSegs = (changedString != null && changedString.isPresent() && !changedString.get().replace("changedFiles:", "").trim().isEmpty()) ? changedString.get().replace("changedFiles:", "") : "NULL";

		    if (changedSegs != null && !changedSegs.isEmpty() && changedSegs != "NULL") {
			List<String> changedSegList = new ArrayList<>();
			Arrays.asList(changedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
			    changedSegList.add(seg.trim());
			});
			systemBasedChangedSegs.put(lSystem.getName(), changedSegList);
		    }

		    Optional<String> deletedString = Arrays.asList(lCheckinResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("deletedFiles")).findAny();
		    deletedSegs = (deletedString != null && deletedString.isPresent() && !deletedString.get().replace("deletedFiles:", "").trim().isEmpty()) ? deletedString.get().replace("deletedFiles:", "") : "NULL";

		    if (deletedSegs != null && !deletedSegs.isEmpty() && deletedSegs != "NULL") {
			List<String> deletedSegList = new ArrayList<>();
			Arrays.asList(deletedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
			    deletedSegList.add(seg.trim());
			});
			if (systemBasedChangedSegs.get(lSystem.getName()) != null) {
			    systemBasedChangedSegs.get(lSystem.getName()).addAll(deletedSegList);
			} else {
			    systemBasedChangedSegs.put(lSystem.getName(), deletedSegList);
			}

		    }
		    commonHelper.updateLatestChangeDate(pUser, systemBasedChangedSegs, lGrouped);
		}

		lResult.get(lSystem.getName()).put("CHECK_IN", lCheckinResponse.getStatus().toString());
		if (!lCheckinResponse.getStatus()) {
		    lJSONResponse.setStatus(Boolean.FALSE);
		    lResult.get(lSystem.getName()).put("CHECK_IN_ERROR_MESSAGE", lCheckinResponse.getDisplayErrorMessage());
		}

		lCheckinResponse = getsSHClientUtils().executeCommand(lSystem, lDevlWorkspaceCommand);
		lResult.get(lSystem.getName()).put("DEVL_WORKSPACE", lCheckinResponse.getStatus().toString());
		if (!lCheckinResponse.getStatus()) {
		    lJSONResponse.setStatus(Boolean.FALSE);
		    lResult.get(lSystem.getName()).put("DEVL_WORKSPACE_ERROR_MESSAGE", lCheckinResponse.getDisplayErrorMessage());
		}
		if (lCheckinResponse.getStatus() && (((changedSegs == null || changedSegs.equalsIgnoreCase("NULL")) && impl.getPlanId().getFullBuildDt() == null) || ((changedSegs != null && !changedSegs.equalsIgnoreCase("NULL")) || (deletedSegs != null && !deletedSegs.equalsIgnoreCase("NULL"))))) {
		    lCheckinResponse = getsSHClientUtils().executeCommand(lSystem, lExportCommand + " \"" + changedSegs + "\"" + " \"" + deletedSegs + "\"");
		    lResult.get(lSystem.getName()).put("EXPORT_WORKSPACE", lCheckinResponse.getStatus().toString());
		    if (!lCheckinResponse.getStatus()) {
			lJSONResponse.setStatus(Boolean.FALSE);
			lResult.get(lSystem.getName()).put("EXPORT_WORKSPACE_ERROR_MESSAGE", lCheckinResponse.getDisplayErrorMessage());
		    }
		}

		LOG.info("Dbug - Before deletion " + lJSONResponse.getStatus());
		if (lJSONResponse.getStatus()) {
		    for (CheckoutSegments lSegment : lSegments) {
			String lArguments = lSegment.getImpId().getId().toLowerCase() + " " + lSegment.getTargetSystem().toLowerCase() + " " + lSegment.getFileName();
			String lCommand = Constants.SystemScripts.DELETE_FILE.getScript() + lArguments;

			JSONResponse sshResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lCommand);
			if (sshResponse.getStatus()) {
			    // getCheckoutSegmentsDAO().delete(pUser, lSegment);
			    segsToBeDeleted.add(lSegment);
			    CheckoutActivityMessage lMessage = new CheckoutActivityMessage(lSegment.getPlanId(), lSegment.getImpId(), lSegment);
			    lMessage.setDeleteFile(true);
			    getActivityLogDAO().save(pUser, lMessage);
			    // Delete build for the plan
			    if (impl.getPlanId().getRejectedDateTime() == null) { // 2413
				List<Build> lBuildList = getBuildDAO().findByImpPlan(planId);
				for (Build lbuild : lBuildList) {
				    getBuildDAO().delete(pUser, lbuild);
				}
			    }
			}
			// ZTPFM-2404 - Additional Validation for new file's when deleted.
			if (lSegment.getRefStatus() != null && lSegment.getRefStatus().equalsIgnoreCase("newfile") && lSegment.getPlanId().getRejectedDateTime() != null) {
			    String url = "ssh://" + gITConfig.getWfLoadBalancerHost() + ":" + gITConfig.getGitDataPort() + "/";
			    String repoName = lSegment.getSourceUrl().replace(url, "");
			    String lArgument = lSegment.getTargetSystem().toLowerCase() + " " + lSegment.getImpId().getPlanId().getId().toLowerCase() + " " + repoName + "," + lSegment.getFileName();
			    String lScript = Constants.SystemScripts.DELETE_NEW_SOURCE_ARTIFACT.getScript() + lArgument;
			    JSONResponse lResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lScript);
			    if (!lResponse.getStatus()) {
				LOG.info(" ERROR WHILE DELETING NEW FILE : " + lResponse.getErrorMessage());
			    }
			}

		    }

		    // Auto checkin when file deleted
		    // String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() +
		    // (lImpId + "_" +
		    // lSystem.getName()).toLowerCase();
		    // String lDevlWorkspaceCommand =
		    // Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() +
		    // (lImpId + "_" + lSystem.getName()).toLowerCase();
		    // String lExportCommand = Constants.SystemScripts.EXPORT.getScript()
		    // + "/home/" + pUser.getId() + "/projects/" + (lImpId + "/" +
		    // lSystem.getName().toLowerCase()
		    // + " \"" + (lImpId + "_" + lSystem.getName()).toLowerCase() + "\" \"" +
		    // getGITConfig().getServiceUserID() + "\"";
		    lCheckinResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lCheckInCommand);

		    if (lCheckinResponse.getStatus()) {
			systemBasedChangedSegs = new HashMap<>();
			Optional<String> changedString = Arrays.asList(lCheckinResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("changedFiles")).findAny();
			changedSegs = (changedString != null && changedString.isPresent() && !changedString.get().replace("changedFiles:", "").isEmpty()) ? changedString.get().replace("changedFiles:", "") : "NULL";

			if (changedSegs != null && !changedSegs.isEmpty() && changedSegs != "NULL") {
			    List<String> changedSegList = new ArrayList<>();
			    Arrays.asList(changedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
				changedSegList.add(seg.trim());
			    });
			    systemBasedChangedSegs.put(lSystem.getName(), changedSegList);
			}

			Optional<String> deletedString = Arrays.asList(lCheckinResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("deletedFiles")).findAny();
			deletedSegs = (deletedString != null && deletedString.isPresent() && !deletedString.get().replace("deletedFiles:", "").isEmpty()) ? deletedString.get().replace("deletedFiles:", "") : "NULL";

			if (deletedSegs != null && !deletedSegs.isEmpty() && deletedSegs != "NULL") {
			    List<String> deletedSegList = new ArrayList<>();
			    Arrays.asList(deletedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
				deletedSegList.add(seg.trim());
			    });
			    if (systemBasedChangedSegs.get(lSystem.getName()) != null) {
				systemBasedChangedSegs.get(lSystem.getName()).addAll(deletedSegList);
			    } else {
				systemBasedChangedSegs.put(lSystem.getName(), deletedSegList);
			    }

			}
			commonHelper.updateLatestChangeDate(pUser, systemBasedChangedSegs, lGrouped);
		    }
		    lResult.get(lSystem.getName()).put("CHECK_IN", lCheckinResponse.getStatus().toString());
		    if (!lCheckinResponse.getStatus()) {
			lJSONResponse.setStatus(Boolean.FALSE);
			lResult.get(lSystem.getName()).put("CHECK_IN_ERROR_MESSAGE", lCheckinResponse.getDisplayErrorMessage());
		    }

		    lCheckinResponse = getsSHClientUtils().executeCommand(lSystem, lDevlWorkspaceCommand);
		    lResult.get(lSystem.getName()).put("DEVL_WORKSPACE", lCheckinResponse.getStatus().toString());
		    if (!lCheckinResponse.getStatus()) {
			lJSONResponse.setStatus(Boolean.FALSE);
			lResult.get(lSystem.getName()).put("DEVL_WORKSPACE_ERROR_MESSAGE", lCheckinResponse.getDisplayErrorMessage());
		    }

		    if (lCheckinResponse.getStatus() && (((changedSegs == null || changedSegs.equalsIgnoreCase("NULL")) && impl.getPlanId().getFullBuildDt() == null) || ((changedSegs != null && !changedSegs.equalsIgnoreCase("NULL")) || (deletedSegs != null && !deletedSegs.equalsIgnoreCase("NULL"))))) {
			lCheckinResponse = getsSHClientUtils().executeCommand(lSystem, lExportCommand + " \"" + changedSegs + "\"" + " \"" + deletedSegs + "\"");
			lResult.get(lSystem.getName()).put("EXPORT_WORKSPACE", lCheckinResponse.getStatus().toString());
			if (!lCheckinResponse.getStatus()) {
			    lJSONResponse.setStatus(Boolean.FALSE);
			    lResult.get(lSystem.getName()).put("EXPORT_WORKSPACE_ERROR_MESSAGE", lCheckinResponse.getDisplayErrorMessage());
			}
		    }
		}
	    }

	    if (!segsToBeDeleted.isEmpty()) {
		Date recentCheckedInDate = new Date();
		segsToBeDeleted.forEach(seg -> {
		    seg.setLastChangedTime(recentCheckedInDate);
		    getCheckoutSegmentsDAO().delete(pUser, seg);
		});
	    }

	    if (impl.getSubstatus() != null) {
		revertImpStatusUnitTesing(pUser, impl.getId());
	    } else {
		revertImpStatusUnitTesing(pUser, impl.getId());
	    }

	    if (lJSONResponse.getStatus()) {

		List<Object[]> segmentDetails = getImpPlanDAO().getDevelopersBySegmentForDelete(planId, new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), programName, targetSystems);
		for (Object[] segment : segmentDetails) {
		    ArrayList<String> toAddress = new ArrayList<>();
		    ArrayList<String> ccAddress = new ArrayList<>();
		    ccAddress.add(getLDAPAuthenticatorImpl().getUserDetails(segment[2].toString()).getMailId());
		    toAddress.add(getLDAPAuthenticatorImpl().getUserDetails(segment[3].toString()).getMailId());
		    CheckoutDeleteMail checkoutDeleteMail = (CheckoutDeleteMail) getMailMessageFactory().getTemplate(CheckoutDeleteMail.class);
		    ccAddress.stream().forEach(c -> checkoutDeleteMail.addcCAddressUserId(c, Constants.MailSenderRole.LEAD));
		    toAddress.stream().forEach(t -> checkoutDeleteMail.addToAddressUserId(t, Constants.MailSenderRole.DEVELOPER));
		    checkoutDeleteMail.setOtherPlanId(planId);
		    checkoutDeleteMail.setToPlanId(segment[1].toString());
		    checkoutDeleteMail.setOtherPlanLoadType(segment[5].toString());
		    checkoutDeleteMail.setToImpId(lImpId);
		    checkoutDeleteMail.setSourceArtifact(programName);
		    checkoutDeleteMail.setTargetSystem(targetSystems);

		    getMailMessageFactory().push(checkoutDeleteMail);
		}
	    }
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    throw new WorkflowException("Error in deleting file", ex);
	}
	if (lResult != null && !lResult.isEmpty()) {
	    lJSONResponse.setData(lResult);
	}
	return lJSONResponse;
    }

    @Transactional
    public JSONResponse deleteFileParallellyForSystems(User pUser, Integer[] ids) {
	// TODO need to revisit the code
	JSONResponse lJSONResponse = new JSONResponse();
	HashMap<String, HashMap> lResult = new HashMap<>();
	Map<System, List<CheckoutSegments>> lSystemsWiseSegments = new HashMap<>();
	lJSONResponse.setStatus(Boolean.TRUE);
	Boolean lCheckinStatus = Boolean.TRUE;
	String lImpId = "";

	String planId = null;
	List<String> targetSystems = new ArrayList<>();
	String programName = null;
	try {
	    List<CheckoutSegments> segsToBeDeleted = new ArrayList<>();
	    for (Integer id : ids) {
		lJSONResponse.setStatus(Boolean.TRUE);
		CheckoutSegments lSegment = getCheckoutSegmentsDAO().find(id);
		lImpId = lSegment.getImpId().getId();
		System lSystem = getSystemDAO().findByName(lSegment.getTargetSystem());

		planId = lSegment.getPlanId().getId();
		programName = lSegment.getProgramName();
		targetSystems.add(lSegment.getTargetSystem());

		if (lSystemsWiseSegments.containsKey(lSystem)) {
		    List<CheckoutSegments> lSegments = lSystemsWiseSegments.get(lSystem);
		    lSegments.add(lSegment);
		    lSystemsWiseSegments.put(lSystem, lSegments);
		} else {
		    List<CheckoutSegments> lSegments = new ArrayList<>();
		    lSegments.add(lSegment);
		    lSystemsWiseSegments.put(lSystem, lSegments);
		}
	    }
	    Implementation impl = getImplementationDAO().find(lImpId);
	    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findByImplementation(lImpId);
	    Map<String, List<CheckoutSegments>> lGrouped = lSegmentList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));

	    Map<String, List<String>> systemBasedChangedSegs = new HashMap<>();
	    Map<System, Boolean> sysAndStatus = new HashMap<>();
	    Set<String> lSystemList = lSystemsWiseSegments.keySet().stream().map(t -> t.getName()).collect(Collectors.toSet());

	    createParallelThreadsForCheckin(pUser, impl, lSystemList, systemBasedChangedSegs, lResult, sysAndStatus, lCheckinStatus, true);
	    commonHelper.updateLatestChangeDate(pUser, systemBasedChangedSegs, lGrouped);

	    Set<String> deleteForSystems = new HashSet<>();
	    for (Map.Entry<System, Boolean> successChekinSystems : sysAndStatus.entrySet()) {
		if (successChekinSystems.getValue()) {
		    deleteForSystems.add(successChekinSystems.getKey().getName());
		}
	    }
	    List<Future<TdxShellExecutorModel>> systemWiseDelete = new ArrayList();
	    for (String lSystemName : deleteForSystems) {
		System lSystem = getSystemDAO().findByName(lSystemName);
		TdxShellExecutorModel executorModel = new TdxShellExecutorModel();
		executorModel.setImpl(impl);
		executorModel.setPlan(impl.getPlanId());
		executorModel.setSystem(lSystem);
		executorModel.setUser(pUser);
		executorModel.setSegmentList(lSystemsWiseSegments.get(lSystem));
		Future<TdxShellExecutorModel> executeDelete = tdxShellExecutor.executeDelete(executorModel);
		systemWiseDelete.add(executeDelete);
	    }

	    int DeleteSystemCount = 0;
	    while (DeleteSystemCount != deleteForSystems.size()) {
		List<Future<TdxShellExecutorModel>> removeSystem = new ArrayList();
		for (Future<TdxShellExecutorModel> executeDelete : systemWiseDelete) {
		    while (executeDelete.isDone()) {
			DeleteSystemCount += 1;
			TdxShellExecutorModel executorModel = executeDelete.get();
			if (executorModel.getSegmentsToBeDeleted() != null && !executorModel.getSegmentsToBeDeleted().isEmpty()) {
			    segsToBeDeleted.addAll(executorModel.getSegmentsToBeDeleted());
			}
			removeSystem.add(executeDelete);
			break;
		    }
		}
		if (!removeSystem.isEmpty()) {
		    systemWiseDelete.removeAll(removeSystem);
		}
	    }

	    createParallelThreadsForCheckin(pUser, impl, deleteForSystems, systemBasedChangedSegs, lResult, sysAndStatus, lCheckinStatus, true);
	    Set<String> secondCheckinSuccessSys = new HashSet<>();
	    for (Map.Entry<System, Boolean> secondSuccessChekinSystems : sysAndStatus.entrySet()) {
		if (secondSuccessChekinSystems.getValue()) {
		    secondCheckinSuccessSys.add(secondSuccessChekinSystems.getKey().getName());
		}
	    }
	    commonHelper.updateLatestChangeDate(pUser, systemBasedChangedSegs, lGrouped);
	    if (!segsToBeDeleted.isEmpty()) {
		Date recentCheckedInDate = new Date();
		segsToBeDeleted.forEach(seg -> {
		    seg.setLastChangedTime(recentCheckedInDate);
		    getCheckoutSegmentsDAO().delete(pUser, seg);
		});
	    }

	    if (impl.getSubstatus() != null) {
		revertImpStatusUnitTesing(pUser, impl.getId());
	    } else {
		revertImpStatusUnitTesing(pUser, impl.getId());
	    }

	    if (lSystemList.size() == deleteForSystems.size()) {

		List<Object[]> segmentDetails = getImpPlanDAO().getDevelopersBySegmentForDelete(planId, new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), programName, targetSystems);
		for (Object[] segment : segmentDetails) {
		    ArrayList<String> toAddress = new ArrayList<>();
		    ArrayList<String> ccAddress = new ArrayList<>();
		    ccAddress.add(getLDAPAuthenticatorImpl().getUserDetails(segment[2].toString()).getMailId());
		    toAddress.add(getLDAPAuthenticatorImpl().getUserDetails(segment[3].toString()).getMailId());
		    CheckoutDeleteMail checkoutDeleteMail = (CheckoutDeleteMail) getMailMessageFactory().getTemplate(CheckoutDeleteMail.class);
		    ccAddress.stream().forEach(c -> checkoutDeleteMail.addcCAddressUserId(c, Constants.MailSenderRole.LEAD));
		    toAddress.stream().forEach(t -> checkoutDeleteMail.addToAddressUserId(t, Constants.MailSenderRole.DEVELOPER));
		    checkoutDeleteMail.setOtherPlanId(planId);
		    checkoutDeleteMail.setToPlanId(segment[1].toString());
		    checkoutDeleteMail.setOtherPlanLoadType(segment[5].toString());
		    checkoutDeleteMail.setToImpId(lImpId);
		    checkoutDeleteMail.setSourceArtifact(programName);
		    checkoutDeleteMail.setTargetSystem(targetSystems);

		    getMailMessageFactory().push(checkoutDeleteMail);
		}
	    } else if (lSystemList.size() != deleteForSystems.size() || deleteForSystems.size() != secondCheckinSuccessSys.size()) {
		lJSONResponse.setStatus(Boolean.FALSE);
	    }
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    throw new WorkflowException("Error in deleting file", ex);
	}
	if (lResult != null && !lResult.isEmpty()) {
	    lJSONResponse.setData(lResult);
	}
	return lJSONResponse;
    }

    public void createParallelThreadsForCheckin(User pUser, Implementation impl, Set<String> lSystemList, Map<String, List<String>> systemBasedChangedSegs, HashMap<String, HashMap> lResult, Map<System, Boolean> sysAndStatus, Boolean lCheckinStatus) {
	createParallelThreadsForCheckin(pUser, impl, lSystemList, systemBasedChangedSegs, lResult, sysAndStatus, lCheckinStatus, false);
    }

    public void createParallelThreadsForCheckin(User pUser, Implementation impl, Set<String> lSystemList, Map<String, List<String>> systemBasedChangedSegs, HashMap<String, HashMap> lResult, Map<System, Boolean> sysAndStatus, Boolean lCheckinStatus, Boolean isSegmentDelete) {
	try {
	    List<Future<TdxShellExecutorModel>> systemWiseCheckin = new ArrayList();
	    for (String lSystemName : lSystemList) {
		System lSystem = getSystemDAO().findByName(lSystemName);
		TdxShellExecutorModel executorModel = new TdxShellExecutorModel();
		executorModel.setImpl(impl);
		executorModel.setPlan(impl.getPlanId());
		executorModel.setSystem(lSystem);
		executorModel.setUser(pUser);
		executorModel.setIsSegmentDelete(isSegmentDelete);
		Future<TdxShellExecutorModel> executeCheckin = tdxShellExecutor.executeCheckin(executorModel);
		systemWiseCheckin.add(executeCheckin);
	    }
	    // // Continuous Loop to get the results of Async Thread
	    int systemCount = 0;
	    while (systemCount != lSystemList.size()) {
		List<Future<TdxShellExecutorModel>> removeSystem = new ArrayList();
		for (Future<TdxShellExecutorModel> exeCheckin : systemWiseCheckin) {
		    while (exeCheckin.isDone()) {
			systemCount += 1;
			TdxShellExecutorModel exeModel = exeCheckin.get();
			lCheckinStatus = exeModel.getJsonResponse().getStatus();
			if (exeModel.getJsonResponse().getStatus() && exeModel.getReturnData() != null && !exeModel.getReturnData().isEmpty()) {
			    LOG.info("Changed Files Data --> " + exeModel.getReturnData());
			    List<String> lUpdatedSegments = Arrays.asList(exeModel.getReturnData().split(","));
			    if (!lUpdatedSegments.isEmpty()) {
				systemBasedChangedSegs.put(exeModel.getSystem().getName(), lUpdatedSegments);
			    }
			}
			sysAndStatus.put(exeModel.getSystem(), exeModel.getActivityStatus());
			LOG.info(exeModel.getSystem().getName() + " : Data from Child Thread : " + exeModel.getJsonResponse().getData().toString());
			ObjectMapper mapper = new ObjectMapper();
			HashMap<String, String> status = mapper.readValue(exeModel.getJsonResponse().getData().toString(), HashMap.class);
			lResult.put(exeModel.getSystem().getName(), status);
			// dhgfgfdgdgf
			removeSystem.add(exeCheckin);
			break;
		    }
		}
		if (!removeSystem.isEmpty()) {
		    systemWiseCheckin.removeAll(removeSystem);
		}
	    }
	} catch (Exception ex) {
	    throw new WorkflowException("Error in Checkin file", ex);
	}
    }

    public JSONResponse deleteTestCase(String pPlanId, String pImplementationId, String pFileName) {
	JSONResponse lResponse = new JSONResponse();
	File lTestFile = new File(getWFConfig().getAttachmentDirectory() + pPlanId + File.separator + getWFConfig().getTestResultsDir() + File.separator + pImplementationId + File.separator + pFileName);
	if (lTestFile.exists()) {
	    boolean delete = lTestFile.delete();
	    if (!delete) {
		lResponse.setErrorMessage(" Unable to to delete test results file ");
	    }
	    lResponse.setStatus(delete);
	} else {
	    lResponse.setErrorMessage("Testcase not found");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }

    public JSONResponse downloadTestCase(String pPlanId, String pImplementationId, String pFileName) {
	JSONResponse lResponse = new JSONResponse();
	File lTestFile = new File(getWFConfig().getAttachmentDirectory() + pPlanId + File.separator + getWFConfig().getTestResultsDir() + File.separator + pImplementationId + File.separator + pFileName);
	if (lTestFile.exists()) {
	    try {
		lResponse.setData(FileUtils.readFileToByteArray(lTestFile));
		lResponse.setMetaData(Files.probeContentType(Paths.get(lTestFile.toURI())));
		lResponse.setStatus(Boolean.TRUE);
	    } catch (IOException ex) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Testcase not found");
	    }
	} else {
	    lResponse.setErrorMessage("Testcase not found");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;

    }

    @Transactional
    public JSONResponse uploadTestCase(User pUser, String pPlanId, String pImplementationId, List<MultipartFile> pFiles) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    if (pFiles.isEmpty()) {
		lResponse.setErrorMessage("No files listed");
		lResponse.setStatus(Boolean.FALSE);
		return lResponse;
	    }
	    for (MultipartFile pFile : pFiles) {
		if (!lUploadFileTypes.contains(FilenameUtils.getExtension(pFile.getOriginalFilename()))) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage(pFile.getOriginalFilename() + " File Type not supported");
		    return lResponse;
		}
	    }
	    for (MultipartFile file : pFiles) {
		if (file.isEmpty()) {
		    lResponse.setErrorMessage(file.getOriginalFilename() + " is empty");
		    lResponse.setStatus(Boolean.FALSE);
		    return lResponse;
		}
	    }
	    String lDirName = getWFConfig().getAttachmentDirectory() + pPlanId + File.separator + getWFConfig().getTestResultsDir() + pImplementationId + File.separator;
	    File lDir = new File(lDirName);
	    if (!lDir.exists()) {
		lDir.mkdirs();
	    }
	    if (lDir.exists()) {
		for (MultipartFile file : pFiles) {
		    String lFileName = lDirName + file.getOriginalFilename();
		    File newFile = new File(lFileName);
		    file.transferTo(newFile);
		    getActivityLogDAO().save(pUser, new TestResultsActivityMessage(new ImpPlan(pPlanId), new Implementation(pImplementationId)));
		}
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage(" Error encountered during upload : Unable to create output directory ");
		return lResponse;
	    }
	    lResponse.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.error(ERROR_ON_EXECUTION, ex);
	    throw new WorkflowException("Error with Upload", ex);
	}
	return lResponse;
    }

    public JSONResponse listTestCases(String pPlanId, String pImplementationId) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	List<String> lResult = new ArrayList<>();
	File lDir = new File(getWFConfig().getAttachmentDirectory() + pPlanId + File.separator + getWFConfig().getTestResultsDir() + File.separator + pImplementationId + File.separator);
	if (lDir.exists()) {
	    List<File> files = (List<File>) FileUtils.listFiles(lDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
	    lResponse.setCount(files.size());
	    lResponse.setMetaData(FileUtils.sizeOfDirectory(lDir));
	    for (File file : files) {
		lResult.add(file.getName());
	    }
	}
	lResponse.setData(lResult);
	return lResponse;
    }

    @Transactional
    public JSONResponse checkoutFile(User lUser, String implId, List<GitSearchResult> pSearchResults) {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    if (getWFConfig().getRunShellScriptParallel()) {
		return checkoutParallelForSystem(lUser, implId, pSearchResults);
	    }
	    LOG.info("Is Present in In-progress queue : " + getCacheClient().getInprogressCheckoutMap().containsKey(implId));
	    if (getCacheClient().getInprogressCheckoutMap().containsKey(implId)) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Previous Checkout operation for implementation : " + implId + " is In-Progress, please try to checkout after completion of previous operation");
		return lResponse;
	    }

	    // segment added to checkout-inprogress map
	    getCacheClient().getInprogressCheckoutMap().put(implId, "CHECKOUT_INPROGRESS");
	    LOG.info("ADDED to In-progress queue : " + getCacheClient().getInprogressCheckoutMap().containsKey(implId));

	    Implementation lImplementation = getImplementationDAO().find(implId);
	    ImpPlan lPlan = lImplementation.getPlanId();
	    String lNickName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();

	    Set<String> lBranchList = new HashSet<>();
	    Map<String, List<CheckoutSegments>> lSegmentsMap = new HashMap<>();
	    Map<String, GitBranchSearchResult> lTempGitSearchSegments = new HashMap<>();
	    Map<String, GitSearchResult> lTempGitMAKSearchSegments = new HashMap<>();

	    // Populating Set and Map
	    for (GitSearchResult lSearchResult : pSearchResults) {
		List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
		long count = lBranches.stream().filter(p -> p.getIsBranchSelected()).count();
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (lBranch.getIsBranchSelected()) {
			String lMasterBranchName = lBranch.getTargetSystem();
			lBranchList.add(lMasterBranchName.toLowerCase());
			String lSystem = lMasterBranchName.replace(Constants.BRANCH_MASTER, "");
			lBranchList.add(lMasterBranchName.replace("master", lImplementation.getId().toLowerCase()));
			CheckoutSegments lSegment = new CheckoutSegments();
			BeanUtils.copyProperties(lSegment, lBranch);
			BeanUtils.copyProperties(lSegment, lSearchResult);
			String targetSystem = lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase();

			lSegment.setTargetSystem(targetSystem);
			lSegment.setCommonFile(count > 1);
			if (lSegmentsMap.get(lSystem) == null) {
			    lSegmentsMap.put(lSystem, new ArrayList<>());
			}
			lSegmentsMap.get(lSystem).add(lSegment);
			String idString = CheckoutUtils.getIdString(lSegment);
			lTempGitSearchSegments.put(idString, lBranch);
		    }
		}
	    }

	    // Creating Branches
	    Map<String, Boolean> lBranchStatus = getJGitClientUtils().createBranches(lPlan.getId(), lBranchList, lNickName);
	    if (!lBranchStatus.isEmpty()) {
		for (Map.Entry<String, Boolean> entrySet : lBranchStatus.entrySet()) {
		    String key = entrySet.getKey();
		    Boolean value = entrySet.getValue();
		    if (!value) {
			LOG.error("Error in creating branches for " + key);
			throw new WorkflowException("Unable to checkout the segments as error occurs on creating branches, Contact Tool Support");
		    }
		}
	    }

	    NewTargetSystemMail newTargetSystemMailNotification = (NewTargetSystemMail) getMailMessageFactory().getTemplate(NewTargetSystemMail.class);
	    newTargetSystemMailNotification.setImplementationId(implId);
	    newTargetSystemMailNotification.setUserDetails(lUser);
	    newTargetSystemMailNotification.setLeadId(lPlan.getLeadId());

	    // For each System LOOP
	    for (Map.Entry<String, List<CheckoutSegments>> lSystemEntry : lSegmentsMap.entrySet()) {
		String lSystemName = lSystemEntry.getKey();
		List<CheckoutSegments> lSegmentsList = lSystemEntry.getValue();
		System lSystem = getSystemDAO().findByName(lSystemName);
		String lCompanyName = lSystem.getPlatformId().getNickName().toLowerCase();
		// Check for Defualt Put Level For each System
		if (lSystem.getDefalutPutLevel() == null) {
		    for (CheckoutSegments lSegment : lSegmentsList) {
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).addAdditionalInfo("checkoutLog", "No Default Put Level assigned for the System " + lSystemName + ", Contact Tool Support");
		    }
		    LOG.error("No Default Put Level for the System " + lSystemName);
		    continue;
		}

		String lRepoName = getJGitClientUtils().getPlanRepoName(lNickName, lPlan.getId());
		// Creating workspace for each system
		String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + lImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
		lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);

		if (!lResponse.getStatus()) {
		    for (CheckoutSegments lSegment : lSegmentsList) {
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).addAdditionalInfo("checkoutLog", lResponse.getDisplayErrorMessage().trim());
		    }
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setData(pSearchResults);
		    return lResponse;
		}

		// For each segments on System LOOP
		for (CheckoutSegments lSegment : lSegmentsList) {
		    LOG.info("From ID of the Source File " + lSegment.getFileName() + " Checked out : " + lSegment.getId());
		    CheckoutSegments lAlreadyCheckoutSegment = getCheckoutSegmentsDAO().findByFileName(lSegment.getFileName(), lImplementation.getId(), lSystem.getName(), lSegment.getFuncArea().toUpperCase());
		    if (lAlreadyCheckoutSegment != null) {
			lSegment.setId(lAlreadyCheckoutSegment.getId());
			lSegment.setCreatedBy(lAlreadyCheckoutSegment.getCreatedBy());
			lSegment.setCreatedDt(lAlreadyCheckoutSegment.getCreatedDt());
			lSegment.setActive(lAlreadyCheckoutSegment.getActive());
			lSegment.setLastChangedTime(lAlreadyCheckoutSegment.getLastChangedTime());
		    } else if (lAlreadyCheckoutSegment == null && lSegment.getProdFlag().equals(Constants.FILE_SEARCH_NONPRODFLAG)) {
			lSegment.setId(0);
		    }
		    LOG.info("Changed ID of the Source File " + lSegment.getFileName() + " Checked out : " + lSegment.getId());

		    String lSourcePlanId = lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).getAdditionalInfo().get("planId");
		    if (lSourcePlanId == null) {
			lSourcePlanId = lPlan.getId();
		    }

		    // Checkout actual file
		    lCommand = Constants.SystemScripts.CHECKOUT.getScript() + lImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase() + " " + lSegment.getFileName() + " " + lSegment.getFileHashCode() + " " + lSegment.getCommitId() + " " + lSegment.getSourceUrl() + " " + lSourcePlanId.toLowerCase();

		    lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);

		    if (lResponse.getStatus()) {
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).setIsCheckedout(true);
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).addAdditionalInfo("checkoutLog", lSegment.getProgramName() + " checked out Successfully");
		    } else {
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).setIsCheckedout(false);
			String errorMsg = lResponse.getDisplayErrorMessage().trim();
			if (lResponse.getDisplayErrorMessage().trim().contains("No Error Message to Display")) {
			    errorMsg = "Unexpected Error occurs while checking out segment " + lSegment.getProgramName();
			}
			lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).addAdditionalInfo("checkoutLog", errorMsg);
		    }

		    // Save to DB
		    if (lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).getIsCheckedout()) {
			SystemLoad lSystemLoad = getSystemLoadDAO().find(lPlan, lSystem);
			if (lSystemLoad == null) {
			    lSystemLoad = new SystemLoad();
			    PutLevel lPutLevel = getPutLevelDAO().find(lSystem.getDefalutPutLevel());
			    lSystemLoad.setPutLevelId(lPutLevel);
			    lSystemLoad.setPlanId(lPlan);
			    lSystemLoad.setSystemId(lSystem);
			    lSystemLoad.setPreload("No");
			    getSystemLoadDAO().save(lUser, lSystemLoad);
			    getActivityLogDAO().save(lUser, new SystemAddActivityMessage(lPlan, lImplementation, lSystemLoad));
			    newTargetSystemMailNotification.addTargetSystem(lSystemName);
			}
			lSegment.setSystemLoad(lSystemLoad);
			lSegment.setPlanId(lPlan);
			lSegment.setImpId(lImplementation);
			lSegment.setReviewStatus(Boolean.FALSE);
			if (lSegment.getId() == 0) {
			    getCheckoutSegmentsDAO().save(lUser, lSegment);
			} else {
			    getCheckoutSegmentsDAO().update(lUser, lSegment);
			}
			getActivityLogDAO().save(lUser, new CheckoutActivityMessage(lPlan, lImplementation, lSegment));

			// Self notification about Unsecured plans
			List<Object[]> segmentDetails = getImpPlanDAO().getDevelopersBySegment(lSegment.getPlanId().getId(), Arrays.asList(Constants.PlanStatus.ACTIVE.name()), lSegment.getFileName(), lSegment.getTargetSystem());

			for (Object[] segment : segmentDetails) {
			    UnsecuredCheckoutSourceContentMail checkoutSourceContentMail = (UnsecuredCheckoutSourceContentMail) getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class);
			    checkoutSourceContentMail.addToAddressUserId(lSegment.getImpId().getDevId(), Constants.MailSenderRole.DEVELOPER);
			    checkoutSourceContentMail.addcCAddressUserId(lSegment.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
			    checkoutSourceContentMail.addcCAddressUserId(segment[3].toString(), Constants.MailSenderRole.DEVELOPER);
			    checkoutSourceContentMail.setToPlanId(lSegment.getPlanId().getId());
			    checkoutSourceContentMail.setOtherPlanId(segment[1].toString());
			    checkoutSourceContentMail.setOtherPlanLoadType(segment[5].toString());
			    if (lSegment.getFileType().equalsIgnoreCase("IBM")) {
				checkoutSourceContentMail.setSourceArtifact(Constants.DVL_IBM_BASE_PATH + lSegment.getPlanId().getId() + "/" + lSegment.getTargetSystem() + "/" + segment[4].toString());
			    } else {
				checkoutSourceContentMail.setSourceArtifact(Constants.DVL_NON_IBM_BASE_PATH + lSegment.getPlanId().getId() + "/" + lSegment.getTargetSystem() + "/" + segment[4].toString());
			    }
			    getMailMessageFactory().push(checkoutSourceContentMail);
			}
			// Self notification about Secured plans
			List<Object[]> securedSegmentDetails = getImpPlanDAO().getDevelopersBySegment(lSegment.getPlanId().getId(), new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()), lSegment.getFileName(), lSegment.getTargetSystem());

			for (Object[] segment : securedSegmentDetails) {
			    CheckoutSourceContentMail checkoutSourceContentMail = (CheckoutSourceContentMail) getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class);
			    checkoutSourceContentMail.addToAddressUserId(lSegment.getImpId().getDevId(), Constants.MailSenderRole.DEVELOPER);
			    checkoutSourceContentMail.addcCAddressUserId(lSegment.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
			    if (lSegment.getFileType().equalsIgnoreCase("IBM")) {
				checkoutSourceContentMail.setSourceArtifact(Constants.STAGE_IBM_BASE_PATH + lSegment.getPlanId().getId() + "/" + lSegment.getTargetSystem() + "/" + segment[4].toString());
			    } else {
				checkoutSourceContentMail.setSourceArtifact(Constants.STAGE_NON_IBM_BASE_PATH + lSegment.getPlanId().getId() + "/" + lSegment.getTargetSystem() + "/" + segment[4].toString());
			    }
			    checkoutSourceContentMail.setSecured(true);
			    checkoutSourceContentMail.setToPlanId(lSegment.getPlanId().getId());
			    checkoutSourceContentMail.setOtherPlanId(segment[1].toString());
			    checkoutSourceContentMail.setOtherPlanLoadType(segment[5].toString());
			    checkoutSourceContentMail.setOtherPlanStatus(segment[6].toString());
			    checkoutSourceContentMail.setSourceArtifact(segment[4].toString());
			    if (lSegment.getProdFlag().equalsIgnoreCase(Constants.FILE_SEARCH_NONPRODFLAG)) {
				lSourcePlanId = lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).getAdditionalInfo().get("planId");
				if (lSourcePlanId != null && !lSourcePlanId.equalsIgnoreCase(segment[1].toString())) {
				    getMailMessageFactory().push(checkoutSourceContentMail);
				}
			    } else {
				getMailMessageFactory().push(checkoutSourceContentMail);
			    }

			}

			List<Object[]> planList = getImpPlanDAO().getAllDependentDevelopersBySegment(lSegment.getPlanId().getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), lSegment.getFileName(), lSegment.getTargetSystem());

			for (Object[] segment : planList) {
			    UnsecuredCheckoutSourceContentMail checkoutSourceContentMail = (UnsecuredCheckoutSourceContentMail) getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class);
			    checkoutSourceContentMail.addToAddressUserId(segment[3].toString(), Constants.MailSenderRole.DEVELOPER);
			    checkoutSourceContentMail.setDependent(true);
			    checkoutSourceContentMail.addcCAddressUserId(segment[2].toString(), Constants.MailSenderRole.LEAD);
			    checkoutSourceContentMail.addcCAddressUserId(lSegment.getImpId().getDevId(), Constants.MailSenderRole.DEVELOPER);
			    checkoutSourceContentMail.setToPlanId(lSegment.getPlanId().getId());
			    checkoutSourceContentMail.setOtherPlanId(segment[1].toString());
			    checkoutSourceContentMail.setOtherPlanLoadType(segment[5].toString());
			    if (lSegment.getFileType().equalsIgnoreCase("IBM")) {
				checkoutSourceContentMail.setSourceArtifact(Constants.DVL_IBM_BASE_PATH + lSegment.getPlanId().getId() + "/" + lSegment.getTargetSystem() + "/" + segment[4].toString());
			    } else {
				checkoutSourceContentMail.setSourceArtifact(Constants.DVL_NON_IBM_BASE_PATH + lSegment.getPlanId().getId() + "/" + lSegment.getTargetSystem() + "/" + segment[4].toString());
			    }
			    getMailMessageFactory().push(checkoutSourceContentMail);
			}

		    }
		}
		// lSSHUtil.disconnectSSH();
	    }
	    if (!newTargetSystemMailNotification.getTargetSystem().isEmpty()) {
		getMailMessageFactory().push(newTargetSystemMailNotification);
	    }
	    if (lImplementation.getSubstatus() != null) {
		revertImpStatus(lUser, implId);
	    } else {
		lImplementation.setIsCheckedin(Boolean.FALSE);
		getImplementationDAO().update(lUser, lImplementation);
	    }
	    // Diverged File notification starts
	    for (GitSearchResult lSearchResult : pSearchResults) {
		SortedSet<String> targetSystemsSet = lSearchResult.getTargetSystems();
		List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
		long count = lBranches.stream().filter(p -> p.getIsBranchSelected()).count();
		List<GitBranchSearchResult> divergedGitBranchList = new ArrayList<>();
		String programName = lSearchResult.getProgramName();
		String lRepoName = getGitHelper().getRepositoryNameBySourceURL(lSearchResult.getBranch().get(0).getSourceUrl());
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (targetSystemsSet.size() != count) {
			divergedGitBranchList.add(lBranch);
		    }
		}
		if (divergedGitBranchList.size() > 0 && targetSystemsSet.size() != divergedGitBranchList.size()) {
		    RepositoryView repoValue1 = getCacheClient().getFilteredRepositoryMap().get(lRepoName.replace(".git", "").toUpperCase());
		    if (repoValue1 == null) {
			LOG.info("Unable to get repository details for " + lRepoName + " so, unable to send mail lead about diverge of sgement");
			continue;
		    }
		    Repository repoValue = repoValue1.getRepository();
		    if (repoValue.getOwners() != null && repoValue.getOwners().size() > 0) {
			Set<String> checkoutTargetSystem = divergedGitBranchList.stream().map(GitBranchSearchResult::getTargetSystem).collect(Collectors.toSet());
			DivergedCheckoutSourceArtifactMail divergedSourceArtifactMail = (DivergedCheckoutSourceArtifactMail) getMailMessageFactory().getTemplate(DivergedCheckoutSourceArtifactMail.class);
			divergedSourceArtifactMail.setSourceArtifact(programName);
			divergedSourceArtifactMail.setTargetSystemBefore(String.join(",", targetSystemsSet).replace("master_", ""));
			divergedSourceArtifactMail.setCheckedOutTargetSystem(String.join(",", checkoutTargetSystem).replace("master_", ""));
			divergedSourceArtifactMail.setDeveloper(lUser.getDisplayName());
			targetSystemsSet.removeAll(checkoutTargetSystem);
			divergedSourceArtifactMail.setTargetSystemAfter(String.join(",", targetSystemsSet).replace("master_", ""));
			repoValue.getOwners().stream().forEach(t -> divergedSourceArtifactMail.addToAddressUserId(t, Constants.MailSenderRole.REPO_OWNERS));
			getMailMessageFactory().push(divergedSourceArtifactMail);
		    }
		}
	    }
	    // Diverged File notification Ends
	    pSearchResults.addAll(lTempGitMAKSearchSegments.values());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(pSearchResults);
	    return lResponse;
	} catch (Exception ex) {
	    LOG.error("unable to checkout", ex);
	    throw new WorkflowException("Unable to checkout the segments ", ex);
	} finally {
	    LOG.info("REMOVED");
	    getCacheClient().getInprogressCheckoutMap().remove(implId);
	    LOG.info("REMOVED : " + getCacheClient().getInprogressCheckoutMap().containsKey(implId));
	}
    }

    @Transactional
    public JSONResponse createSourceArtifact(User pUser, String pImplId, List<GitSearchResult> pFileList) {
	JSONResponse lResponse = new JSONResponse();
	Map<String, List<CheckoutSegments>> lSystemWise = new HashMap<>();
	Map<String, GitBranchSearchResult> lCodeWise = new HashMap<>();
	Map<String, Date> lSystemAndLoadDate = new HashMap<>();

	try {
	    Implementation lImpl = getImplementationDAO().find(pImplId);
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(lImpl.getPlanId().getId());
	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		lSystemAndLoadDate.put(lSystemLoad.getSystemId().getName().toUpperCase(), lSystemLoad.getLoadDateTime());
	    }
	    String lCompanyName = lSystemLoadList.get(0).getSystemId().getPlatformId().getNickName();
	    NewTargetSystemMail newTargetSystemMailNotification = (NewTargetSystemMail) getMailMessageFactory().getTemplate(NewTargetSystemMail.class);
	    newTargetSystemMailNotification.setImplementationId(lImpl.getId());
	    newTargetSystemMailNotification.setUserDetails(pUser);
	    newTargetSystemMailNotification.setLeadId(lImpl.getPlanId().getId());

	    for (GitSearchResult pFile : pFileList) {
		pFile.setFileName(pFile.getFileName().replaceAll("^\\/", ""));
		if (lImpl.getPlanId().getMacroHeader() && !(pFile.getFileName().endsWith(".mac") || pFile.getFileName().endsWith(".hpp") || pFile.getFileName().endsWith(".h") || pFile.getFileName().endsWith(".cpy") || pFile.getFileName().endsWith(".inc") || pFile.getFileName().endsWith(".incafs"))) {
		    throw new WorkflowException("Unable to create new source artificat - " + pFile.getFileName() + " as current plan is MACRO/HEADER type, allowed file extensions are h,hpp,mac,cpy,inc,incafs");
		}

		// 2011 - Dont allow .sbt file to create
		if (pFile.getFileName().endsWith(".sbt")) {
		    throw new WorkflowException("Unable to create file " + pFile.getFileName() + ", SBT segments creation are restricted");
		}
		// String extension = FilenameUtils.getExtension(pFile.getFileName());
		pFile.setProgramName(FilenameUtils.getName(pFile.getFileName()));
		List<GitBranchSearchResult> branches = pFile.getBranch();
		long count = branches.stream().filter(p -> p.getIsBranchSelected()).count();
		for (GitBranchSearchResult branch : branches) {
		    CheckoutSegments lSegments = new CheckoutSegments();
		    BeanUtils.copyProperties(lSegments, pFile);
		    BeanUtils.copyProperties(lSegments, branch);
		    String idString = CheckoutUtils.getIdStringWithFileName(lSegments);
		    lCodeWise.put(idString, branch);
		    List<CheckoutSegments> get = lSystemWise.get(lSegments.getTargetSystem());

		    if (get == null) {
			get = new ArrayList<>();
			lSystemWise.put(lSegments.getTargetSystem(), get);
		    }

		    lSegments.setImpId(lImpl);
		    lSegments.setReviewStatus(Boolean.FALSE);
		    lSegments.setPlanId(lImpl.getPlanId());
		    // lSegments.setSystemLoad(lLoad);
		    lSegments.setCommitDateTime(new Date());
		    lSegments.setCommitterName(pUser.getDisplayName());
		    lSegments.setCommitterMailId(pUser.getMailId());
		    lSegments.setAuthorName(pUser.getDisplayName());
		    lSegments.setAuthorMailId(pUser.getMailId());
		    lSegments.setCommonFile(count > 1);
		    get.add(lSegments);
		}
	    }
	    if (!newTargetSystemMailNotification.getTargetSystem().isEmpty()) {
		getMailMessageFactory().push(newTargetSystemMailNotification);
	    }
	    if (lImpl.getSubstatus() != null) {
		revertImpStatus(pUser, lImpl.getId());
	    } else {
		lImpl.setIsCheckedin(Boolean.FALSE);
		getImplementationDAO().update(pUser, lImpl);
	    }
	    for (Map.Entry<String, List<CheckoutSegments>> entrySet : lSystemWise.entrySet()) {
		String lSystemName = entrySet.getKey();
		List<CheckoutSegments> lSegments = entrySet.getValue();

		LOG.info("Creating Source Artifact for " + lSystemName);
		System lSystem = getSystemDAO().findByName(lSystemName);
		if (lSystem == null) {
		    for (CheckoutSegments lSegment : lSegments) {
			lSegment.setStatus("N");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("N");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "System " + lSystemName + " not found");
		    }
		    continue;
		}

		String nickName = lSystem.getPlatformId().getNickName();

		// Non Production File Validation
		for (CheckoutSegments lSegment : lSegments) {
		    lSegment.setStatus("Y");
		    // Check whether segments been created already in same Implementation
		    CheckoutSegments lAlreadyCheckedOutSegments = getCheckoutSegmentsDAO().findByFileName(lSegment.getFileName(), pImplId, lSystem.getName());
		    if (lAlreadyCheckedOutSegments != null) {
			lSegment.setStatus("W");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("W");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "Already Exists in " + lAlreadyCheckedOutSegments.getImpId().getId());
			LOG.warn(lSegment.getFileName() + " File Already Found in NON PROD Implementation - " + lAlreadyCheckedOutSegments.getImpId().getId());
			continue;
		    }

		    // Check whether segments been created across Modern plans - Devops Plan
		    lAlreadyCheckedOutSegments = getCheckoutSegmentsDAO().findByFileNameAndSystem(lSegment.getFileName(), lSystem.getName());
		    if (lAlreadyCheckedOutSegments != null) {
			lSegment.setStatus("W");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("W");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "Already Exists in " + lAlreadyCheckedOutSegments.getImpId().getId());
			LOG.warn(lSegment.getFileName() + " File Already created in Implementation - " + lAlreadyCheckedOutSegments.getImpId().getId());
			continue;
		    }

		    if (lSegment.getStatus().equals("Y")) {
			String lRepoName = (getGITConfig().getGitProdPath() + nickName + "/" + lSegment.getFileType().replace("_", "") + "/" + lSegment.getFileType().replace("_", "") + "_" + lSegment.getFuncArea()).toUpperCase();
			List<Repository> lRepoList = getCacheClient().getAllRepositoryMap().get(lRepoName);
			Set<String> lSearchRepoList = new HashSet();
			if (lRepoList == null || lRepoList.isEmpty()) {
			    LOG.error("Unable to find repository details for " + lRepoName);
			    lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("N");
			    lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "Func Area " + lSegment.getFuncArea().toUpperCase() + " Not exists");
			    lSegment.setStatus("N");
			    continue;
			}
			for (Repository lRepo : lRepoList) {
			    lSearchRepoList.add(lRepo.getName());
			}

			String lRepoDesc = getCacheClient().getFilteredRepositoryMap().get(FilenameUtils.removeExtension(lRepoName.toUpperCase())).getRepository().getDescription();
			lSegment.setRepoDesc(lRepoDesc);
			lRepoName = lRepoList.isEmpty() ? lRepoName + ".git" : lRepoList.get(lRepoList.size() - 1).getName();
			lSegment.setSourceUrl("ssh://" + gITConfig.getWfLoadBalancerHost() + ":" + gITConfig.getGitDataPort() + "/" + lRepoName);

			Collection<GitSearchResult> lProdFileResult = getjGITSearchUtils().SearchAllRepos(nickName, lSegment.getProgramName().toLowerCase(), lImpl.getPlanId().getMacroHeader(), null, new ArrayList(lSearchRepoList));
			LOOP: for (GitSearchResult lSearchedFile : lProdFileResult) {
			    String lProdFileName = lSearchedFile.getFileName().replace("/", "").replace("\\", "");
			    String lInputFileName = lSegment.getFileName().replace("/", "").replace("\\", "");
			    LOG.info(lSearchedFile.getFileName() + lSegment.getFileName());
			    LOG.info(lProdFileName + lInputFileName);

			    if (lProdFileName.equalsIgnoreCase(lInputFileName)) {
				List<GitBranchSearchResult> lBranchList = lSearchedFile.getBranch();
				for (GitBranchSearchResult lBranch : lBranchList) {
				    if (lBranch.getTargetSystem().toLowerCase().contains(lSegment.getTargetSystem().toLowerCase())) {
					lSegment.setStatus("W");
					lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("W");
					lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "Already Exists in " + lBranch.getFuncArea());
					LOG.warn(lSegment.getFileName() + " File Already Found in PROD");
					break LOOP;
				    }
				}
			    }
			}
		    }
		}

		if (!lSystemName.equalsIgnoreCase(lSystem.getName())) {
		    continue;
		}

		List<String> lExistingBranchList = new ArrayList();
		List<String> lWorkspaceBranchList = new ArrayList();
		String lRepoName;

		lRepoName = getJGitClientUtils().getPlanRepoName(nickName, lImpl.getPlanId().getId());
		Set<String> lNewBranchList = new HashSet<>();
		lNewBranchList.add("master_" + lSystem.getName().toLowerCase());
		lNewBranchList.add((pImplId + "_" + lSystem.getName()).toLowerCase());
		Map<String, Boolean> lBranchStatus = getJGitClientUtils().createBranches(lImpl.getPlanId().getId(), lNewBranchList, nickName);
		lExistingBranchList = getJGitClientUtils().getAllBranchList(nickName, lImpl.getPlanId().getId());
		for (String lExistingBranchName : lExistingBranchList) {
		    if (lExistingBranchName.contains((pImplId + "_" + lSystem.getName()).toLowerCase())) {
			lWorkspaceBranchList.add(lExistingBranchName);
		    }
		}

		if (lWorkspaceBranchList.isEmpty()) {
		    for (CheckoutSegments lSegment : lSegments) {
			lSegment.setStatus("N");
			lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("N");
		    }
		    continue;
		}

		lResponse = getsSHClientUtils().executeCommand(pUser, lSystem, Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + String.join(",", lWorkspaceBranchList));

		if (!lResponse.getStatus()) {
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Workspace are not Created for Implementation - " + lImpl.getId());
		    return lResponse;
		}

		for (CheckoutSegments lSegment : lSegments) {
		    if (lSegment.getStatus().equals("Y")) {

			String lCommand = Constants.SystemScripts.CREATE_SOURCE_ARTIFACT.getScript() + pImplId.toLowerCase() + " " + lSegment.getTargetSystem().toLowerCase() + " " + lSegment.getSourceUrl() + " " + lSegment.getFileName();
			lResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lCommand);

			if (lResponse.getStatus()) {
			    lSegment.setStatus("Y");
			    lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("Y");
			    lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "File created successfully");
			    SystemLoad lLoad = getSystemLoadDAO().findBy(lImpl.getPlanId(), lSystem);
			    if (lLoad == null) {
				lLoad = new SystemLoad();
				PutLevel lPutLevel = getPutLevelDAO().find(lSystem.getDefalutPutLevel());
				lLoad.setPutLevelId(lPutLevel);
				lLoad.setPlanId(lImpl.getPlanId());
				lLoad.setSystemId(lSystem);
				getSystemLoadDAO().save(pUser, lLoad);
				getActivityLogDAO().save(pUser, new SystemAddActivityMessage(lImpl.getPlanId(), lImpl, lLoad));
				newTargetSystemMailNotification.addTargetSystem(lSegment.getTargetSystem());
			    }
			    lSegment.setRefLoadDate(lSystemAndLoadDate.get(lSegment.getTargetSystem()));
			    lSegment.setRefPlan(lImpl.getPlanId().getId());

			    lSegment.setSystemLoad(lLoad);
			    CheckoutActivityMessage lMessage = new CheckoutActivityMessage(lImpl.getPlanId(), lImpl, lSegment);
			    lMessage.setNewFile(true);
			    getActivityLogDAO().save(pUser, lMessage);
			    this.getCheckoutSegmentsDAO().save(pUser, lSegment);

			} else {

			    String lResData = (String) lResponse.getErrorMessage();
			    lSegment.setStatus("N");
			    lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("N");
			    lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", lResData);
			    if (lResData.toLowerCase().contains("already")) {
				lSegment.setStatus("W");
				lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).setStatus("W");
				lCodeWise.get(CheckoutUtils.getIdStringWithFileName(lSegment)).addAdditionalInfo("statusMsg", "File Exists Already");
			    }
			}
		    }
		}

	    }
	    lResponse.setData(pFileList);
	    lResponse.setStatus(true);
	} catch (WorkflowException ex) {
	    LOG.error(ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to create the source artifact", ex);
	    throw new WorkflowException("Unable to create the source artifact", ex);
	}

	return lResponse;
    }

    @Transactional
    public JSONResponse populateIBMSegment(User lUser, String implId, List<GitSearchResult> pSegmentList) {
	JSONResponse lResponse = new JSONResponse();
	Map<System, List<CheckoutSegments>> lResultSegment = new HashMap();
	Map<String, GitBranchSearchResult> lSegmentsMap = new HashMap();
	Map<String, GitSearchResult> lTempGitMAKSearchSegments = new HashMap<>();
	Map<String, Integer> lCommonSegmentMap = new HashMap<>();

	List<System> lSystemList;
	try {
	    Implementation lImpl = getImplementationDAO().find(implId);
	    ImpPlan lPlan = lImpl.getPlanId();
	    Platform lPlatform = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId();
	    lSystemList = getSystemDAO().findByPlatform(lPlatform);
	    String lCompanyName = lSystemList.get(0).getPlatformId().getNickName().toLowerCase();

	    if (lSystemList.size() > 0) {
		for (System lSystem : lSystemList) {
		    PutLevel lPutLevel = getPutLevelDAO().find(lSystem.getDefalutPutLevel());
		    List<CheckoutSegments> lTempSegmentList = new ArrayList<>();
		    lResultSegment.put(lSystem, lTempSegmentList);
		    for (GitSearchResult lSegment : pSegmentList) {
			List<GitBranchSearchResult> branches = lSegment.getBranch();
			Set<String> lTargetSystemList = lSegment.getTargetSystems().stream().map(t -> t.toUpperCase().replace("MASTER_", "").toLowerCase()).collect(Collectors.toSet());
			Integer count = lTargetSystemList.size();
			if (lTargetSystemList.contains(lSystem.getName().toLowerCase())) {

			    CheckoutSegments lTempSegment = new CheckoutSegments();
			    lTempSegment.setFileName(lSegment.getFileName().trim());
			    lTempSegment.setProgramName(lSegment.getProgramName().trim());
			    lTempSegment.setTargetSystem(lSystem.getName());
			    lTempSegmentList.add(lTempSegment);

			    String idString = CheckoutUtils.getIdString(lTempSegment);
			    GitBranchSearchResult lRes = new GitBranchSearchResult();
			    for (GitBranchSearchResult sysBranch : branches) {
				if (sysBranch.getTargetSystem().toLowerCase().contains(lSystem.getName().toLowerCase())) {
				    lRes = sysBranch;
				}
			    }
			    if (lSegment.getProgramName().endsWith(".mak")) {
				Set<String> lAllowedRepos = getGitHelper().getUserAllowedReadAndAboveRepos(lUser.getId(), lCompanyName);
				Set<String> lObsRepos = getGitHelper().getObsRepoList(lCompanyName);
				lAllowedRepos.removeAll(lObsRepos);
				// Collection<GitSearchResult> lGITMakFileList =
				// getjGITSearchUtils().SearchAllRepos(lCompanyName, lSegment.getProgramName(),
				// Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, lAllowedRepos);
				Collection<GitSearchResult> lGITMakFileList = commonHelper.getProdAndNonProdMakSegs(lUser, lSegment.getProgramName(), lSystem.getName(), implId);
				if (lGITMakFileList.isEmpty()) {
				    lRes.setIsPopulated(false);
				}
			    } else {
				Collection<GitSearchResult> lGITFileList = getjGITSearchUtils().SearchAllRepos(lCompanyName, lSegment.getProgramName(), lPlan.getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, Arrays.asList(lPutLevel.getScmUrl()));
				if (lGITFileList.isEmpty()) {
				    lRes.setIsPopulated(false);
				}
			    }
			    lSegmentsMap.put(idString, lRes);
			    lCommonSegmentMap.put(CheckoutUtils.getIBMIdString(lTempSegment), count);
			}
		    }
		}
	    }

	    for (Map.Entry<System, List<CheckoutSegments>> lSystemProgramList : lResultSegment.entrySet()) {
		System lSystem = lSystemProgramList.getKey();
		List<CheckoutSegments> lSystemSegmentList = lSystemProgramList.getValue();
		if (lSystemSegmentList.size() > 0) {
		    PutLevel lPutLevelInfo = getPutLevelDAO().find(lSystem.getDefalutPutLevel());

		    if (lSystem.getDefalutPutLevel() == null || lPutLevelInfo == null) {
			for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
			    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");
			}
			continue;
		    }

		    boolean lRepoStatus = false;
		    try {
			lRepoStatus = getJGitClientUtils().isRepositoryExist(lPutLevelInfo.getScmUrl());
			lRepoStatus = lRepoStatus && getJGitClientUtils().isBranchExistInRepository(lPutLevelInfo.getScmUrl(), Constants.BRANCH_MASTER + lSystem.getName().toLowerCase());
		    } catch (Exception ex) {
			for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
			    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");
			}
			LOG.error("Error occurs in search put repository for System " + lSystem.getName());
			continue;
		    }
		    if (!lRepoStatus) {
			for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
			    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");
			}
			continue;
		    }

		    // SSHUtil lSSHUtil = getSSHUtil();
		    // if (!lSSHUtil.connectSSH(lSystem)) {
		    // LOG.warn("Cannot Connect to SSH for System " + lSystem.getName());
		    // continue;
		    // }
		    for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
			if (!lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).isIsPopulated()) {
			    JSONResponse lResponseData = null;

			    String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + lSystemSegment.getFileName().trim() + " " + lPutLevelInfo.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevelInfo.getPutDateTime());
			    lResponseData = getsSHClientUtils().executeCommand(lSystem, lCommand);
			    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");

			    if (lResponseData.getStatus()) {
				lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("Y");
			    } else {
				String lData = (String) lResponseData.getErrorMessage();
				if (lData.toLowerCase().contains("already")) {
				    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("W");
				}
			    }
			} else {
			    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("Y");
			}
		    }

		    // Checkout File
		    Set<String> lBranchList = new HashSet<>();
		    lBranchList.add("master" + "_" + lSystem.getName().toLowerCase());
		    lBranchList.add(lImpl.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase());
		    Map<String, Boolean> lBranchStatus = getJGitClientUtils().createBranches(lPlan.getId(), lBranchList, lSystem.getPlatformId().getNickName());
		    if (!lBranchStatus.isEmpty()) {
			for (Map.Entry<String, Boolean> entrySet : lBranchStatus.entrySet()) {
			    String key = entrySet.getKey();
			    Boolean value = entrySet.getValue();
			    if (!value) {
				for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
				    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");
				}
				LOG.error("Error in Checkout of " + key);
			    }
			}
		    }

		    // SSHUtil lUserSSH = getSSHUtil();
		    // if (!lUserSSH.connectSSH(lUser, lSystem)) {
		    // for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
		    // lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");
		    // }
		    // LOG.error("unable to connect using SSH to system " + lSystem.getName() + " by
		    // user " +
		    // lUser.getDisplayName());
		    // continue;
		    // }
		    String lRepoName = getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId());
		    String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + lImpl.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
		    lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);

		    if (!lResponse.getStatus()) {
			for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
			    lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment)).setStatus("N");
			}
			LOG.error("Unable to Checkout file for system - " + lSystem.getName() + " Plan Id - " + lPlan.getId());
			continue;

		    }

		    NewTargetSystemMail newTargetSystemMailNotification = (NewTargetSystemMail) getMailMessageFactory().getTemplate(NewTargetSystemMail.class);
		    newTargetSystemMailNotification.setImplementationId(implId);
		    newTargetSystemMailNotification.setUserDetails(lUser);
		    newTargetSystemMailNotification.setLeadId(lPlan.getLeadId());

		    for (CheckoutSegments lSystemSegment : lSystemSegmentList) {
			GitBranchSearchResult lTempResultSegment = lSegmentsMap.get(CheckoutUtils.getIdString(lSystemSegment));
			if (lTempResultSegment.getStatus().equals("Y")) {
			    lTempResultSegment.setStatus("N");
			    // Checkout the Populated File
			    Collection<GitSearchResult> lGITFileList = getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), lSystemSegment.getProgramName(), lPlan.getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, Arrays.asList(lPutLevelInfo.getScmUrl()));
			    for (GitSearchResult lFileResult : lGITFileList) {
				for (GitBranchSearchResult lBranch : lFileResult.getBranch()) {

				    String lFileName = lSystemSegment.getFileName().replace("/templates/", "/");
				    if (lPutLevelInfo.getScmUrl().contains(lBranch.getFuncArea().toLowerCase()) && lBranch.getTargetSystem().contains(lSystemSegment.getTargetSystem().toLowerCase()) && lFileName.contains(lFileResult.getFileName()) && lSystemSegment.getProgramName().equals(lFileResult.getProgramName()) && lBranch.getRefStatus().equalsIgnoreCase("online")) {
					String lFuncArea = lBranch.getFuncArea();
					CheckoutSegments lIsAlreadyCheckout = getCheckoutSegmentsDAO().findByFileName(lFileResult.getFileName(), lImpl.getId(), lSystem.getName(), lFuncArea);

					if (lIsAlreadyCheckout != null) {
					    LOG.info(lFileResult.getFileName() + " is already Checked out for the implementation " + lImpl.getId() + " System " + lSystem.getName());
					    continue;
					}

					lCommand = Constants.SystemScripts.CHECKOUT.getScript() + lImpl.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase() + " " + lFileResult.getFileName() + " " + lFileResult.getFileHashCode() + " " + lBranch.getCommitId() + " " + lBranch.getSourceUrl() + " " + lPlan.getId().toLowerCase();
					lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);
					if (!lResponse.getStatus()) {
					    if (lResponse.getErrorMessage().contains("exists")) {
						lTempResultSegment.setStatus("W");
					    } else {
						lTempResultSegment.setStatus("N");
					    }
					    LOG.error("Error in Checkout File (" + lFileResult.getProgramName() + " for System " + lSystem.getName().toLowerCase() + "), Reason: " + lResponse.getErrorMessage());
					    continue;
					} else {
					    lTempResultSegment.setStatus("Y");
					}
					lBranch.addAdditionalInfo("checkoutLog", lResponse.getData().toString().trim());
					CheckoutSegments lSaveFileInfo = new CheckoutSegments();
					// lSaveFileInfo.setCommonFile(count > 1);
					BeanUtils.copyProperties(lSaveFileInfo, lBranch);
					BeanUtils.copyProperties(lSaveFileInfo, lFileResult);
					lSaveFileInfo.setTargetSystem(lSystem.getName());
					String idString = CheckoutUtils.getIBMIdString(lSaveFileInfo);
					lSaveFileInfo.setCommonFile(lCommonSegmentMap.get(idString) > 1);
					lSaveFileInfo.setFuncArea(lFuncArea);
					SystemLoad lSystemLoad = getSystemLoadDAO().find(lPlan, lSystem);
					if (lSystemLoad == null) {
					    lSystemLoad = new SystemLoad();
					    PutLevel lPutLevel = getPutLevelDAO().find(lSystem.getDefalutPutLevel());
					    lSystemLoad.setPutLevelId(lPutLevel);
					    lSystemLoad.setPlanId(lPlan);
					    lSystemLoad.setSystemId(lSystem);
					    lSystemLoad.setPreload("No");
					    lSystemLoad.setIplRequired("No");
					    // lSystemLoad.setLoadSetName(LoadSetUtils.getLoadSetName(lImpl.getPlanId(),
					    // lSystemLoad));
					    // lSystemLoad.setFallbackLoadSetName(LoadSetUtils.getFallbackLoadSetName(lImpl.getPlanId(),
					    // lSystemLoad));
					    getSystemLoadDAO().save(lUser, lSystemLoad);
					    getActivityLogDAO().save(lUser, new SystemAddActivityMessage(lPlan, lImpl, lSystemLoad));
					    newTargetSystemMailNotification.addTargetSystem(lSystem.getName());
					}
					lSaveFileInfo.setSystemLoad(lSystemLoad);
					lSaveFileInfo.setPlanId(lPlan);
					lSaveFileInfo.setImpId(lImpl);
					if (lSaveFileInfo.getId() == 0) {
					    getCheckoutSegmentsDAO().save(lUser, lSaveFileInfo);
					} else {
					    getCheckoutSegmentsDAO().update(lUser, lSaveFileInfo);
					}
					CheckoutActivityMessage lMessage = new CheckoutActivityMessage(lPlan, lImpl, lSaveFileInfo);
					lMessage.setPopulate(true);
					getActivityLogDAO().save(lUser, lMessage);
				    }
				}
			    }
			}
		    }
		    if (!newTargetSystemMailNotification.getTargetSystem().isEmpty()) {
			getMailMessageFactory().push(newTargetSystemMailNotification);
		    }
		    // lUserSSH.disconnectSSH();
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Error in Getting Platform Details", ex);
	    throw new WorkflowException("Unable to populate segment into the specified target system ", ex);
	}

	lResponse.setStatus(Boolean.TRUE);
	pSegmentList.addAll(lTempGitMAKSearchSegments.values());
	lResponse.setData(pSegmentList);
	return lResponse;
    }

    @Transactional
    public JSONResponse getActivityLogList(String pImplId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("impId", new Implementation(pImplId));
	List findAll = getActivityLogDAO().findAll(lFilter, pOffset, pLimit, pOrderBy);
	Long count = getActivityLogDAO().count(lFilter);
	lResponse.setCount(count);
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(findAll);
	return lResponse;
    }

    private void sendNotificationOnCheckIn(String pImpId) throws WorkflowException {
	Implementation lImp = getImplementationDAO().find(pImpId);
	List<CheckoutSegments> lCommonSegmentList = getCheckoutSegmentsDAO().getSameSegmentDevelopersByImpId(pImpId, new ArrayList<>(Constants.PlanStatus.getNonProdStatusMap().keySet()));
	Map<Implementation, List<String>> lImpWithIBMSegments = new HashMap();
	Map<Implementation, List<String>> lImpWithNONIBMSegments = new HashMap();
	for (CheckoutSegments lCheckoutSeg : lCommonSegmentList) {
	    if (lCheckoutSeg.getFileType().equals("IBM")) {
		if (lImpWithIBMSegments.containsKey(lCheckoutSeg.getImpId())) {
		    lImpWithIBMSegments.get(lCheckoutSeg.getImpId()).add(Constants.DVL_IBM_BASE_PATH + lCheckoutSeg.getPlanId().getId() + "/" + lCheckoutSeg.getTargetSystem() + "/" + lCheckoutSeg.getFileName() + "[" + lCheckoutSeg.getTargetSystem() + "]");
		} else {
		    List<String> lTempList = new ArrayList();
		    lTempList.add(Constants.DVL_IBM_BASE_PATH + lCheckoutSeg.getPlanId().getId() + "/" + lCheckoutSeg.getTargetSystem() + "/" + lCheckoutSeg.getFileName() + "[" + lCheckoutSeg.getTargetSystem() + "]");
		    lImpWithIBMSegments.put(lCheckoutSeg.getImpId(), lTempList);
		}
	    } else {
		if (lImpWithNONIBMSegments.containsKey(lCheckoutSeg.getImpId())) {
		    lImpWithNONIBMSegments.get(lCheckoutSeg.getImpId()).add(Constants.DVL_NON_IBM_BASE_PATH + lCheckoutSeg.getPlanId().getId() + "/" + lCheckoutSeg.getTargetSystem() + "/" + lCheckoutSeg.getFileName() + "[" + lCheckoutSeg.getTargetSystem() + "]");
		} else {
		    List<String> lTempList = new ArrayList();
		    lTempList.add(Constants.DVL_NON_IBM_BASE_PATH + lCheckoutSeg.getPlanId().getId() + "/" + lCheckoutSeg.getTargetSystem() + "/" + lCheckoutSeg.getFileName() + "[" + lCheckoutSeg.getTargetSystem() + "]");
		    lImpWithNONIBMSegments.put(lCheckoutSeg.getImpId(), lTempList);
		}
	    }
	}
	Set<Implementation> keySet = new HashSet<>();
	keySet.addAll(lImpWithIBMSegments.keySet());
	keySet.addAll(lImpWithNONIBMSegments.keySet());
	for (Implementation implementation : keySet) {
	    List<String> lList = new ArrayList<>();
	    if (lImpWithIBMSegments.get(implementation) != null) {
		lList.addAll(lImpWithIBMSegments.get(implementation));
	    }
	    if (lImpWithNONIBMSegments.get(implementation) != null) {
		lList.addAll(lImpWithNONIBMSegments.get(implementation));

	    }
	    CheckInMail checkInMail = (CheckInMail) getMailMessageFactory().getTemplate(CheckInMail.class);
	    checkInMail.setDependentPlan(implementation.getPlanId());
	    checkInMail.setSourcePlan(lImp.getPlanId());
	    checkInMail.setSourceArtifacts(String.join(",", lList));
	    checkInMail.addToAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
	    checkInMail.addcCAddressUserId(implementation.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
	    getMailMessageFactory().push(checkInMail);
	}
    }

    @Transactional
    public JSONResponse commit(User lUser, String implId, List<GitSearchResult> pSegments, String pCommitMessage) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	Boolean revertFlag = Boolean.FALSE;
	HashMap<String, String> lResult = new HashMap<>();
	HashMap<String, String> lResultMetaData = new HashMap<>();
	List<CheckoutSegments> lSegmentsList = new ArrayList<>();

	if (getWFConfig().getRunShellScriptParallel()) {
	    return parallelCommitForSystem(lUser, implId, pSegments, pCommitMessage);
	}

	for (GitSearchResult lSearchResult : pSegments) {
	    List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
	    for (GitBranchSearchResult lBranch : lBranches) {
		if (lBranch.getIsBranchSelected()) {
		    CheckoutSegments lSegment = new CheckoutSegments();
		    BeanUtils.copyProperties(lSegment, lBranch);
		    BeanUtils.copyProperties(lSegment, lSearchResult);
		    lSegment.setTargetSystem(lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase());
		    lSegmentsList.add(lSegment);
		}
	    }
	}

	Map<String, List<CheckoutSegments>> lGrouped = lSegmentsList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));
	for (Map.Entry<String, List<CheckoutSegments>> entrySet : lGrouped.entrySet()) {
	    String lSystemName = entrySet.getKey();
	    List<CheckoutSegments> lSegments = entrySet.getValue();
	    System lSystem = getSystemDAO().findByName(lSystemName);
	    ArrayList<String> lList = new ArrayList<>();
	    List<CheckoutSegments> reviewedSegmentList = getCheckoutSegmentsDAO().findByImplementation(implId).stream().filter(lSegment -> Boolean.TRUE.equals(lSegment.getReviewStatus())).collect(Collectors.toList());
	    // if (reviewedSegmentList.size() > 0 && reviewedSegmentList.size() <=
	    // lSegments.size()) {

	    // }
	    lSegments.stream().forEach(t -> lList.add(t.getFileName()));
	    if (!lList.isEmpty()) {
		String lCommand = Constants.SystemScripts.COMMIT.getScript() + " \"" + (implId + "_" + lSystemName).toLowerCase() + "\" \"" + String.join(",", lList) + "\" \"" + pCommitMessage + "\"";
		// SSHUtil lSSHUtil = getSSHUtil();
		// lSSHUtil.connectSSH(lUser, lSystem);
		lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);
		String lErrMsg = "";
		String lResponseData = "";
		// lSSHUtil.disconnectSSH();
		Implementation imp = getImplementationDAO().find(implId);
		LOG.info("Shell Script Message " + lResponse.getData() + " Error  " + lResponse.getErrorMessage() + " Display   " + lResponse.getDisplayErrorMessage());
		if (!lResponse.getStatus()) {
		    lErrMsg = lResponse.getDisplayErrorMessage();
		    CommitActivityMessage lErrorMessage = new CommitActivityMessage(imp.getPlanId(), imp);
		    lErrorMessage.setComments(lErrMsg);
		    lErrorMessage.setCommit(true);
		    getActivityLogDAO().save(lUser, lErrorMessage);
		} else {
		    lResponseData = lResponse.getData().toString();
		    for (CheckoutSegments segment : lSegments) {
			CheckoutActivityMessage lMessage = new CheckoutActivityMessage(imp.getPlanId(), imp, segment);
			lMessage.setCommit(true);
			getActivityLogDAO().save(lUser, lMessage);
		    }
		}
		lResult.put(lSystem.getName(), lErrMsg);
		lResultMetaData.put(lSystem.getName(), lResponseData);
	    }
	    // Get keys and values
	    for (Map.Entry<String, String> entry : lResultMetaData.entrySet()) {
		String v = entry.getValue();
		if (!v.contains("Nothing to commit")) {
		    lResponse.setMetaData(StringUtils.EMPTY);
		    revertFlag = Boolean.TRUE;
		    break;
		} else {
		    lResponse.setMetaData("Nothing-to-Commit");
		}
	    }
	    if (revertFlag) {
		Implementation impl = revertImpStatus(lUser, implId);

		// Delete build for the plan
		if (impl.getPlanId().getRejectedDateTime() == null) { // 2413
		    List<Build> lBuildList = getBuildDAO().findByImpPlan(impl.getPlanId());
		    for (Build lbuild : lBuildList) {
			getBuildDAO().delete(lUser, lbuild);
		    }
		}
	    }
	}

	// Removed Tag from Git repository
	List<String> lPlanSubStatusList = new ArrayList<>();
	lPlanSubStatusList.add(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	lPlanSubStatusList.add(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name());
	lPlanSubStatusList.add(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	Implementation impl = getImplementationDAO().find(implId);
	String lCompanyName = "";
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(impl.getPlanId());
	for (SystemLoad lSystemLoad : lSystemLoadList) {
	    if (lSystemLoad != null) {
		lCompanyName = lSystemLoad.getSystemId().getPlatformId().getNickName();
	    }
	}
	String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, impl.getPlanId().getId());
	getsSHClientUtils().removeTag(lRepoName, lPlanSubStatusList);

	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lResult);
	return lResponse;
    }

    @Transactional
    public JSONResponse parallelCommitForSystem(User lUser, String implId, List<GitSearchResult> pSegments, String pCommitMessage) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	Boolean revertFlag = Boolean.FALSE;
	HashMap<String, String> lResult = new HashMap<>();
	HashMap<String, String> lResultMetaData = new HashMap<>();
	List<CheckoutSegments> lSegmentsList = new ArrayList<>();

	for (GitSearchResult lSearchResult : pSegments) {
	    List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
	    for (GitBranchSearchResult lBranch : lBranches) {
		if (lBranch.getIsBranchSelected()) {
		    CheckoutSegments lSegment = new CheckoutSegments();
		    BeanUtils.copyProperties(lSegment, lBranch);
		    BeanUtils.copyProperties(lSegment, lSearchResult);
		    lSegment.setTargetSystem(lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase());
		    lSegmentsList.add(lSegment);
		}
	    }
	}

	Map<String, List<CheckoutSegments>> lGrouped = lSegmentsList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));
	Implementation implementaion = getImplementationDAO().find(implId);
	Set<String> lSystemList = lGrouped.keySet();
	List<Future<TdxShellExecutorModel>> systemWiseCommit = new ArrayList();
	for (String lSystemName : lSystemList) {
	    System lSystem = getSystemDAO().findByName(lSystemName);
	    TdxShellExecutorModel executorModel = new TdxShellExecutorModel();
	    executorModel.setImpl(implementaion);
	    executorModel.setPlan(implementaion.getPlanId());
	    executorModel.setSystem(lSystem);
	    executorModel.setUser(lUser);
	    executorModel.setSegmentList(lGrouped.get(lSystemName));
	    executorModel.setCommitMessage(pCommitMessage);
	    Future<TdxShellExecutorModel> executeCommit = tdxShellExecutor.executeCommit(executorModel);
	    systemWiseCommit.add(executeCommit);
	}

	int sysCount = 0;
	while (sysCount != lSystemList.size()) {
	    List<Future<TdxShellExecutorModel>> removeSystem = new ArrayList();
	    for (Future<TdxShellExecutorModel> exeCommit : systemWiseCommit) {
		while (exeCommit.isDone()) {
		    sysCount += 1;
		    TdxShellExecutorModel executorModel = exeCommit.get();
		    LOG.info(executorModel.getSystem().getName() + " : Data from Child Thread : " + executorModel.getJsonResponse().getData().toString());
		    String errMsg = executorModel.getReturnData();
		    lResultMetaData.put(executorModel.getSystem().getName(), errMsg);
		    ObjectMapper mapper = new ObjectMapper();
		    HashMap<String, String> status = mapper.readValue(executorModel.getJsonResponse().getData().toString(), HashMap.class);
		    lResult.put(executorModel.getSystem().getName(), status.get(executorModel.getSystem().getName()));
		    removeSystem.add(exeCommit);
		    break;
		}
	    }
	    if (!removeSystem.isEmpty()) {
		systemWiseCommit.removeAll(removeSystem);
	    }
	}

	// Get keys and values
	for (Map.Entry<String, String> entry : lResultMetaData.entrySet()) {
	    String v = entry.getValue();
	    if (!v.contains("Nothing to commit")) {
		lResponse.setMetaData(StringUtils.EMPTY);
		revertFlag = Boolean.TRUE;
		break;
	    } else {
		lResponse.setMetaData("Nothing-to-Commit");
	    }
	}
	if (revertFlag) {
	    Implementation imp = revertImpStatus(lUser, implId);
	    if (imp.getPlanId().getRejectedDateTime() == null) { // 2413
		List<Build> lBuildList = getBuildDAO().findByImpPlan(implementaion.getPlanId());
		for (Build lbuild : lBuildList) {
		    getBuildDAO().delete(lUser, lbuild);
		}
	    }
	}

	// Removed Tag from Git repository
	List<String> lPlanSubStatusList = new ArrayList<>();
	lPlanSubStatusList.add(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	lPlanSubStatusList.add(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name());
	lPlanSubStatusList.add(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	Implementation impl = getImplementationDAO().find(implId);
	String lCompanyName = "";
	List<SystemLoad> lSystemLoadList = getSystemLoadDAO().findByImpPlan(impl.getPlanId());
	for (SystemLoad lSystemLoad : lSystemLoadList) {
	    if (lSystemLoad != null) {
		lCompanyName = lSystemLoad.getSystemId().getPlatformId().getNickName();
	    }
	}
	String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, impl.getPlanId().getId());
	getsSHClientUtils().removeTag(lRepoName, lPlanSubStatusList);

	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lResult);
	return lResponse;
    }

    public Implementation revertImpStatus(User lUser, String implId) {
	Implementation impl = getImplementationDAO().find(implId);
	// impl.setCheckinDateTime(null);
	impl.setIsCheckedin(Boolean.FALSE);
	impl.setReviewersDone("");
	impl.setImpStatus(Constants.ImplementationStatus.IN_PROGRESS.name());
	impl.setSubstatus(null);
	impl.setTktNum(null);
	impl.setTktUrl(null);
	getImplementationDAO().update(lUser, impl);
	return impl;
    }

    /**
     * When the developer has done delete action on that implementation and the
     * artifacts deleted are no longer present in devl workspace(as part of the
     * internal checkin from delete)
     *
     * Then the "Unit Testing Completed" button in the implementation will continue
     * to remain enabled.
     */
    public Implementation revertImpStatusUnitTesing(User lUser, String implId) {
	Implementation impl = getImplementationDAO().find(implId);
	List<CheckoutSegments> lListOfSeg = getCheckoutSegmentsDAO().findByImplementation(impl);
	if (!lListOfSeg.isEmpty() && impl.getIsCheckedin()) {
	    impl.setIsCheckedin(Boolean.TRUE);
	    impl.setImpStatus(Constants.ImplementationStatus.IN_PROGRESS.name());
	} else {
	    impl.setIsCheckedin(Boolean.FALSE);
	    impl.setImpStatus(Constants.ImplementationStatus.IN_PROGRESS.name());
	    impl.setSubstatus(null);
	}
	impl.setReviewersDone("");
	impl.setTktNum(null);
	impl.setTktUrl(null);
	getImplementationDAO().update(lUser, impl);
	return impl;
    }

    @Transactional
    public JSONResponse checkin(User lUser, String implId) {

	JSONResponse lResponse = new JSONResponse();
	HashMap<String, HashMap> lResult = new HashMap<>();

	try {

	    if (getWFConfig().getRunShellScriptParallel()) {
		return checkinParallelForSystem(lUser, implId);
	    }

	    Implementation lImplementation = getImplementationDAO().find(implId);

	    // 2156 - Common code validation
	    JSONResponse lCCValidation = doCommonCodeValidationOnImpl(lUser, implId);
	    if (!lCCValidation.getStatus()) {
		LOG.info("Error occurs in Common code validation for Implementation - " + implId);
		lCCValidation.setStatus(Boolean.FALSE);
		return lCCValidation;
	    }

	    Boolean lCheckinStatus = Boolean.TRUE;
	    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findByImplementation(implId);
	    Implementation impl = getImplementationDAO().find(implId);
	    impl.setTktNum(null);
	    impl.setTktUrl(null);
	    getImplementationDAO().update(lUser, impl);
	    Map<String, List<CheckoutSegments>> lGrouped = lSegmentList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));

	    Map<String, List<String>> systemBasedChangedSegs = new HashMap<>();

	    Set<String> lSystemList = lGrouped.keySet();
	    for (String lSystemName : lSystemList) {
		lResult.put(lSystemName, new HashMap());
		System lSystem = getSystemDAO().findByName(lSystemName);

		// ZTPFM-2447 Code changes to add Last check in date in checkin script param
		String lastCheckInDateTime = (impl.getCheckinDateTime() != null) ? Constants.JENKINS_DATEFORMAT.get().format(impl.getCheckinDateTime()) : "NULL";

		String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (implId + "_" + lSystemName).toLowerCase() + " " + lastCheckInDateTime;
		String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (implId + "_" + lSystemName).toLowerCase();
		String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + lUser.getId() + "/projects/" + (implId + "/" + lSystemName).toLowerCase() + " \"" + (implId + "_" + lSystemName).toLowerCase() + "\" \"" + getGITConfig().getServiceUserID() + "\"";

		lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCheckInCommand);

		// ZTPFM-2447 Add the list of changed segments
		String changedSegs = null;
		String deletedSegs = null;
		if (lResponse.getStatus()) {
		    Optional<String> changedString = Arrays.asList(lResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("changedFiles")).findAny();
		    changedSegs = (changedString != null && changedString.isPresent() && !changedString.get().replace("changedFiles:", "").trim().isEmpty()) ? changedString.get().replace("changedFiles:", "") : "NULL";
		    if (changedSegs != null && !changedSegs.isEmpty() && !"NULL".equalsIgnoreCase(changedSegs)) {
			List<String> changedSegList = new ArrayList<>();
			Arrays.asList(changedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
			    changedSegList.add(seg.trim());
			});
			systemBasedChangedSegs.put(lSystemName, changedSegList);
		    }

		    Optional<String> deletedString = Arrays.asList(lResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("deletedFiles")).findAny();
		    deletedSegs = (deletedString != null && deletedString.isPresent() && !deletedString.get().replace("deletedFiles:", "").trim().isEmpty()) ? deletedString.get().replace("deletedFiles:", "") : "NULL";

		    if (deletedSegs != null && !deletedSegs.isEmpty() && !"NULL".equalsIgnoreCase(deletedSegs)) {
			List<String> deletedSegList = new ArrayList<>();
			Arrays.asList(deletedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
			    deletedSegList.add(seg.trim());
			});
			if (systemBasedChangedSegs.get(lSystemName) != null) {
			    systemBasedChangedSegs.get(lSystemName).addAll(deletedSegList);
			} else {
			    systemBasedChangedSegs.put(lSystemName, deletedSegList);
			}

		    }
		}

		lResult.get(lSystem.getName()).put("CHECK_IN", lResponse.getStatus().toString());
		if (!lResponse.getStatus()) {
		    lResult.get(lSystem.getName()).put("CHECK_IN_ERROR_MESSAGE", lResponse.getDisplayErrorMessage());
		    CheckinActivityMessage lMessage = new CheckinActivityMessage(lImplementation.getPlanId(), lImplementation);
		    lMessage.setError(true);
		    lMessage.setComments(lResponse.getDisplayErrorMessage());
		    lCheckinStatus = Boolean.FALSE;
		    getActivityLogDAO().save(lUser, lMessage);
		}

		if (lCheckinStatus) {
		    lResponse = getsSHClientUtils().executeCommand(lSystem, lDevlWorkspaceCommand);
		    lResult.get(lSystem.getName()).put("DEVL_WORKSPACE", lResponse.getStatus().toString());
		    if (!lResponse.getStatus()) {
			lResult.get(lSystem.getName()).put("DEVL_WORKSPACE_ERROR_MESSAGE", lResponse.getDisplayErrorMessage());
			CheckinActivityMessage lMessage = new CheckinActivityMessage(lImplementation.getPlanId(), lImplementation);
			lMessage.setError(true);
			lMessage.setComments(lResponse.getDisplayErrorMessage());
			lCheckinStatus = Boolean.FALSE;
			getActivityLogDAO().save(lUser, lMessage);
		    }
		}

		if (lCheckinStatus && (((changedSegs == null || changedSegs.equalsIgnoreCase("NULL")) && impl.getPlanId().getFullBuildDt() == null) || ((changedSegs != null && !changedSegs.equalsIgnoreCase("NULL")) || (deletedSegs != null && !deletedSegs.equalsIgnoreCase("NULL"))))) {
		    LOG.info("Export command: " + lExportCommand + " \"" + changedSegs + "\"" + " \"" + deletedSegs + "\"");
		    lResponse = getsSHClientUtils().executeCommand(lSystem, lExportCommand + " \"" + changedSegs + "\"" + " \"" + deletedSegs + "\"");
		    lResult.get(lSystem.getName()).put("EXPORT_WORKSPACE", lResponse.getStatus().toString());
		    if (!lResponse.getStatus()) {
			lResult.get(lSystem.getName()).put("EXPORT_WORKSPACE_ERROR_MESSAGE", lResponse.getDisplayErrorMessage());
			CheckinActivityMessage lMessage = new CheckinActivityMessage(lImplementation.getPlanId(), lImplementation);
			lMessage.setError(true);
			lMessage.setComments(lResponse.getDisplayErrorMessage());
			lCheckinStatus = Boolean.FALSE;
			getActivityLogDAO().save(lUser, lMessage);
		    }
		}
		if (!lCheckinStatus) {
		    break;
		}
	    }

	    // ZTPFM-2447 Code changes to upate Last changed value for recently updated
	    // segments
	    commonHelper.updateLatestChangeDate(lUser, systemBasedChangedSegs, lGrouped);
	    // 2413 we cannt use systemBasedChangedSegs as deleted file information not
	    // received from Shell during check in.
	    List<CheckoutSegments> lUpdatedSegments = getCheckoutSegmentsDAO().getChangedFilesInWorkspace(lImplementation.getId()); // 2413

	    if (lUpdatedSegments != null && !lUpdatedSegments.isEmpty()) { // 2413
		sendCheckInMailOnSegmentsUpdates(lImplementation, new ArrayList());
		updateActivityLogOnSegmentsUpdates(lUser, lImplementation, new ArrayList());
	    }

	    if (lCheckinStatus) {
		lImplementation.setIsCheckedin(Boolean.TRUE);
		lImplementation.setLastCheckinStatus(Constants.CheckinStatus.SUCCESS.getDescription());
		CheckinActivityMessage lMessage = new CheckinActivityMessage(lImplementation.getPlanId(), lImplementation);
		getActivityLogDAO().save(lUser, lMessage);
		sendNotificationOnCheckIn(implId);
	    } else {
		lImplementation.setIsCheckedin(Boolean.FALSE);
		lImplementation.setLastCheckinStatus(Constants.CheckinStatus.FAILED.getDescription());
	    }
	    if (lCheckinStatus && lImplementation.getPlanId().getRejectedDateTime() == null) {
		List<Build> lBuildList = getBuildDAO().findByImpPlan(lImplementation.getPlanId());
		for (Build lbuild : lBuildList) {
		    getBuildDAO().delete(lUser, lbuild);
		}
	    }
	    lImplementation.setCheckinDateTime(new Date());
	    getImplementationDAO().update(lUser, lImplementation);

	} catch (WorkflowException ex) {
	    LOG.info("Unable to Check-in the changes for Implementation - " + implId, ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to Check-in the changes for Implementation - " + implId, ex);
	    throw new WorkflowException("Unable to Check-in the changes for Implementation - " + implId);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lResult);
	return lResponse;
    }

    @Transactional
    public JSONResponse getLatest(User lUser, String implId, String pType) {
	JSONResponse lResponse = new JSONResponse();
	HashMap<String, String> lResult = new HashMap<>();
	Implementation lImplementation = getImplementationDAO().find(implId);
	ImpPlan imp = getImpPlanDAO().find(implId);
	List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findByImplementation(implId);
	Map<String, List<CheckoutSegments>> lGrouped = lSegmentList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));

	Set<String> lSystemList = lGrouped.keySet();
	for (String lSystemName : lSystemList) {
	    System lSystem = getSystemDAO().findByName(lSystemName);
	    String lCommand = Constants.SystemScripts.GET_LATEST.getScript() + " " + (implId + "_" + lSystemName).toLowerCase() + " " + pType + " " + lImplementation.getPlanId().getId().toLowerCase();
	    // SSHUtil lSSHUtil = getSSHUtil();
	    // lSSHUtil.connectSSH(lUser, lSystem);
	    lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);
	    if (lResponse.getStatus()) {
		lResult.put(lSystem.getName(), lResponse.getStatus().toString());
	    } else {
		int errorPosition = lResponse.getErrorMessage().indexOf("ERROR:");
		String errorMessage = "";
		if (errorPosition != 0) {
		    errorMessage = lResponse.getErrorMessage().substring(errorPosition);
		}
		CommitActivityMessage lMessage = new CommitActivityMessage(imp, lImplementation);
		lMessage.setComments(errorMessage);
		getActivityLogDAO().save(lUser, lMessage);
		lResult.put(lSystem.getName(), errorMessage);
	    }
	    // lSSHUtil.disconnectSSH();
	}

	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lResult);
	return lResponse;
    }

    @Transactional
    public JSONResponse createWorkspace(User user, String implId) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Implementation lImp = getImplementationDAO().find(implId);
	    ImpPlan lPlan = lImp.getPlanId();
	    String lNickName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    String lRepoName = getJGitClientUtils().getPlanRepoName(lNickName, lPlan.getId());
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().getSystemLoadsFromImp(lImp.getId());
	    lResponse.setStatus(Boolean.TRUE);
	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		System lSystem = lSystemLoad.getSystemId();
		// SSHUtil lSSHUtil = getSSHUtil();
		// if (!lSSHUtil.connectSSH(user, lSystem)) {
		// lResponse.setStatus(Boolean.FALSE);
		// lResponse.setErrorMessage("Unable to create workspace");
		// continue;
		// }
		JSONResponse lCommandResponse = new JSONResponse();
		String lCommand = Constants.SystemScripts.IMPORT_WORKSPACE.getScript() + lRepoName + " " + lImp.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
		lCommandResponse = getsSHClientUtils().executeCommand(user, lSystem, lCommand);

		if (!lCommandResponse.getStatus()) {
		    LOG.error("Unable to create workspace for Implementation -" + lImp.getId());
		    lResponse.setStatus(Boolean.FALSE);
		    lResponse.setErrorMessage("Unable to create workspace");
		}
		// lSSHUtil.disconnectSSH();
	    }

	    if (lResponse.getStatus()) {
		lImp.setReassignFlag("N");
		getImplementationDAO().update(user, lImp);
	    }
	} catch (Exception E) {
	    LOG.error("Unable to Create workspace for User " + user.getDisplayName(), E);
	    throw new WorkflowException("Unable to create workspace", E);
	}

	return lResponse;
    }

    @Transactional
    public JSONResponse developerGitRestore(User user, String implId) throws WorkflowException {
	JSONResponse lResponse = new JSONResponse();
	List<Boolean> statusReponseList = new ArrayList<Boolean>();
	List<String> lErrorMessage = new ArrayList<String>();
	try {
	    Implementation lImp = getImplementationDAO().find(implId);
	    List<SystemLoad> lSystemLoadList = getSystemLoadDAO().getSystemLoadsFromImp(lImp.getId());

	    for (SystemLoad lSystemLoad : lSystemLoadList) {
		System lSystem = lSystemLoad.getSystemId();
		JSONResponse lCommandResponse = new JSONResponse();
		String lCommand = Constants.SystemScripts.GIT_RESTORE.getScript() + lImp.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
		lCommandResponse = getsSHClientUtils().executeCommand(user, lSystem, lCommand);
		statusReponseList.add(lCommandResponse.getStatus());
		lErrorMessage.add(lCommandResponse.getErrorMessage());
	    }
	    if (statusReponseList.stream().allMatch(t -> t.equals(Boolean.FALSE))) {
		if (lErrorMessage.stream().allMatch(t -> t.equals("Error Code: 8 INFO: Nothing to restore git files."))) {
		    LOG.info("Nothing to restore from git  " + lImp.getId());
		    lResponse.setStatus(Boolean.TRUE);
		    lResponse.setData("Nothing to restore from git ");
		} else {
		    for (String failErrorMessage : lErrorMessage) {
			if (failErrorMessage.contains("Contact zTPF devops toolchain support")) {
			    LOG.error("Git Restore fail " + failErrorMessage + lImp.getId());
			    lResponse.setStatus(Boolean.FALSE);
			    String lFailMessage = failErrorMessage.replace("Error Code: 8", "");
			    lResponse.setErrorMessage(lFailMessage);
			    break;
			}
		    }
		}
	    } else {
		LOG.info("Project Restore Successfully  " + lImp.getId());
		lResponse.setData("Project Restored Successfully");
		lResponse.setStatus(Boolean.TRUE);
	    }

	} catch (Exception e) {
	    LOG.error("Git Restore fail " + user.getDisplayName(), e);
	    throw new WorkflowException("Git Restore fail", e);
	}

	return lResponse;
    }

    public Boolean isMakFileExistInUserInput(List<CheckoutSegments> lUserSegmentList, String fileName, String targetSystem) {
	for (CheckoutSegments lSegment : lUserSegmentList) {
	    if (lSegment.getFileName().contains(fileName) && targetSystem.toUpperCase().contains(lSegment.getTargetSystem().toUpperCase())) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    public Boolean isMakFileExistInUserInputForCheckout(List<CheckoutSegments> lUserSegmentList, String fileName, String targetSystem) {
	for (CheckoutSegments lSegment : lUserSegmentList) {
	    if (lSegment.getFileName().equals(fileName) && targetSystem.toUpperCase().contains(lSegment.getTargetSystem().toUpperCase())) {
		return Boolean.TRUE;
	    }
	}
	return Boolean.FALSE;
    }

    @Transactional
    public JSONResponse validateMakFile(User lUser, String implId, List<GitSearchResult> pSearchResults) {
	JSONResponse lResponse = new JSONResponse();
	HashMap<String, Set<String>> systemAndCheckoutFile = new HashMap<>();
	List<GitSearchResult> gitSearchResults = new ArrayList<>();
	HashMap<String, Set<String>> systemBasedFileMap = new HashMap<>();
	HashMap<String, Set<String>> fileAndSystemsMap = new HashMap<>();
	HashMap<String, HashMap<String, Set<String>>> sysAndFileBasedMakMap = new HashMap<>();
	HashMap<String, Set<String>> makAndFileMap = new HashMap<>();
	String cachekey = implId + "_" + lUser.getDisplayName();
	int count = 0;
	try {
	    List<String> uiDataList = new ArrayList<>();
	    Map<String, String> SysBasedFileMakinfo = new HashMap<>();
	    Map<List<String>, Map<String, String>> cacheData = getCacheClient().getValidateMakFileMap().get(cachekey) != null ? getCacheClient().getValidateMakFileMap().get(cachekey) : new HashMap<List<String>, Map<String, String>>();

	    pSearchResults.forEach(res -> {
		res.getBranch().forEach(sys -> {
		    String systemName = sys.getTargetSystem().trim().toUpperCase();
		    if (sys.getTargetSystem().trim().toUpperCase().startsWith("MASTER_")) {
			systemName = Arrays.asList(sys.getTargetSystem().trim().toUpperCase().split("_")).get(1);
		    }
		    uiDataList.add(res.getProgramName().trim() + "_" + systemName);
		});
	    });
	    // Get System based files
	    pSearchResults.forEach(res -> {
		List<GitBranchSearchResult> removeBranchList = new ArrayList<>();
		String programName = res.getProgramName().trim();
		Boolean isBranchSelected = Boolean.TRUE;
		Set<String> targetSys = res.getTargetSystems().stream().map(t -> t.trim().toUpperCase().replace("MASTER_", "")).collect(Collectors.toSet());
		res.getBranch().forEach(sys -> {
		    List<String> cacheList = new ArrayList<>();
		    Map<String, String> cacheMakfileInfoMap = new HashMap();
		    for (Map.Entry<List<String>, Map<String, String>> entry : cacheData.entrySet()) {
			cacheList = entry.getKey();
			cacheMakfileInfoMap = entry.getValue();
		    }
		    cacheList = cacheList.stream().filter(t -> uiDataList.contains(t)).collect(Collectors.toList());
		    cacheData.put(cacheList, cacheMakfileInfoMap);
		    getCacheClient().getValidateMakFileMap().put(cachekey, cacheData);
		    String systemName = sys.getTargetSystem().trim().toUpperCase();
		    if (sys.getTargetSystem().trim().toUpperCase().startsWith("MASTER_")) {
			systemName = Arrays.asList(sys.getTargetSystem().trim().toUpperCase().split("_")).get(1);
		    }
		    String fileName = res.getProgramName().trim() + "_" + systemName;

		    boolean makFileFlag = Boolean.FALSE;
		    if (cacheMakfileInfoMap.containsKey(fileName)) {
			if (uiDataList.contains(cacheMakfileInfoMap.get(fileName))) {
			    makFileFlag = Boolean.TRUE;
			} else if (cacheMakfileInfoMap.get(fileName).equalsIgnoreCase("MAK_NOT_FOUND")) {
			    makFileFlag = Boolean.TRUE;
			}
		    }

		    if (res.getProgramName().trim().endsWith(".mak") && cacheList.contains(fileName)) {
			makFileFlag = Boolean.TRUE;
		    }

		    if (systemBasedFileMap.get(systemName) == null) {
			systemBasedFileMap.put(systemName, new HashSet<String>());
		    }
		    systemBasedFileMap.get(systemName).add(programName);

		    if (!cacheList.contains(fileName) || !makFileFlag) {
			if (targetSys.contains(sys.getTargetSystem().trim().toUpperCase().replace("MASTER_", ""))) {
			    // <System Name+File Name, fileNAmeWIthHash>
			    LOG.info("Processing " + res.getProgramName().trim() + "_" + systemName);
			    String sysName = sys.getTargetSystem().trim().toUpperCase().replace("MASTER_", "");
			    // if (systemBasedFileMap.get(sysName) == null) {
			    // systemBasedFileMap.put(sysName, new HashSet<String>());
			    // }
			    // systemBasedFileMap.get(sysName).add(programName);

			    if (fileAndSystemsMap.get(programName) == null) {
				fileAndSystemsMap.put(programName, new HashSet<>());
			    }
			    fileAndSystemsMap.get(programName).add(sysName);
			    if (sys.isIsCheckoutAllowed()) {
				if (systemAndCheckoutFile.containsKey(sysName)) {
				    systemAndCheckoutFile.get(sysName).add(programName);
				} else {
				    Set<String> fileNames = new HashSet<>();
				    fileNames.add(programName);
				    systemAndCheckoutFile.put(sysName, fileNames);
				}
			    }
			} else {
			    removeBranchList.add(sys);
			}
		    }

		});
		res.getBranch().removeAll(removeBranchList);
	    });

	    // Add the existing checkout segments with respect to implementation
	    List<CheckoutSegments> existingCheckoutSegs = getCheckoutSegmentsDAO().findByImplementation(implId);
	    if (existingCheckoutSegs != null && !existingCheckoutSegs.isEmpty()) {
		existingCheckoutSegs.forEach(seg -> {
		    if (systemBasedFileMap.get(seg.getTargetSystem()) == null) {
			systemBasedFileMap.put(seg.getTargetSystem(), new HashSet<String>());
		    }
		    systemBasedFileMap.get(seg.getTargetSystem()).add(seg.getProgramName());
		});
	    }

	    // ZTPFM-1686 Checkout file in LocalWorkSpace
	    String warningMsg = checkoutFileLocalWorkSpace(lUser, implId, pSearchResults);

	    List<String> lSystemList = new ArrayList<String>();

	    // Call shell based on system
	    for (Map.Entry<String, Set<String>> entry : systemAndCheckoutFile.entrySet()) {
		String fileNames = entry.getValue().stream().map(t -> t.trim()).collect(Collectors.joining(","));
		lSystemList.add(entry.getKey());
		JSONResponse lCommandResponse = new JSONResponse();
		String command = Constants.SystemScripts.CHECK_ALL_MAK_FILE.getScript() + fileNames + " " + entry.getKey();
		System lSystem = getSystemDAO().findByName(entry.getKey());
		lCommandResponse = getsSHClientUtils().executeCommand(lUser, lSystem, command);
		HashMap<String, Set<String>> fileAndMakFileMap = new HashMap<>();
		if (lCommandResponse.getStatus() && lCommandResponse.getData() != null && !lCommandResponse.getData().toString().trim().isEmpty()) {
		    Set<String> fileAndRespMak = Arrays.asList(lCommandResponse.getData().toString().split(" ")).stream().map(x -> x.trim()).collect(Collectors.toSet());
		    // Split File name and mak files
		    for (String fileAndMak : fileAndRespMak) {
			String[] sepFileAndMak = fileAndMak.split(":");
			String fileName = sepFileAndMak[0].toString();
			if (sepFileAndMak.length > 1 && sepFileAndMak[1] != null && !sepFileAndMak[1].toString().toUpperCase().contains("NOTFOUND")) {
			    Set<String> lMakFileSet = Arrays.asList(sepFileAndMak[1].split(",")).stream().filter(t -> !t.trim().isEmpty()).collect(Collectors.toSet());
			    if (lMakFileSet != null && !lMakFileSet.isEmpty()) {
				lMakFileSet.forEach(mak -> {
				    if (makAndFileMap.get(mak) == null) {
					makAndFileMap.put(mak, new HashSet<>());
				    }
				    makAndFileMap.get(mak).add(fileName);
				});
				fileAndMakFileMap.put(fileName, lMakFileSet);
				SysBasedFileMakinfo.put(fileName + "_" + lSystem.getName(), lMakFileSet.stream().collect(Collectors.joining()) + "_" + lSystem.getName());
			    }
			}
		    }
		}

		if (!fileAndMakFileMap.isEmpty()) {
		    sysAndFileBasedMakMap.put(entry.getKey(), fileAndMakFileMap);
		}
	    }
	    StringBuilder checkoutMsg = new StringBuilder();
	    // Check Mak file is available with respect to system
	    boolean isMakFileMissing = false;
	    for (Map.Entry<String, HashMap<String, Set<String>>> sysAndMakFileMap : sysAndFileBasedMakMap.entrySet()) {
		if (isMakFileMissing) {
		    break;
		}
		if (systemBasedFileMap.get(sysAndMakFileMap.getKey()) != null) {
		    Set<String> fileNames = systemBasedFileMap.get(sysAndMakFileMap.getKey());
		    for (Map.Entry<String, Set<String>> fileAndMakFile : sysAndMakFileMap.getValue().entrySet()) {
			Optional<String> missingMakFile = fileAndMakFile.getValue().stream().filter(makFile -> !fileNames.contains(makFile)).findFirst();
			if (missingMakFile != null && missingMakFile.isPresent()) {
			    List<GitSearchResult> searchRes = commonHelper.getProdAndNonProdMakSegs(lUser, missingMakFile.get(), sysAndMakFileMap.getKey(), implId);
			    if (searchRes != null && !searchRes.isEmpty()) {
				gitSearchResults.addAll(searchRes);
			    }
			    if (makAndFileMap.get(missingMakFile.get()) != null) {
				checkoutMsg.append("Please select from the displayed .mak file, as you have associated component(s) ");
				checkoutMsg.append(makAndFileMap.get(missingMakFile.get()).stream().collect(Collectors.joining(","))).append(" selected for checkout.");

				for (String file : makAndFileMap.get(missingMakFile.get())) {
				    if (fileAndSystemsMap.get(file) != null && !fileAndSystemsMap.get(file).isEmpty()) {
					for (String sys : fileAndSystemsMap.get(file)) {
					    if (!sys.equalsIgnoreCase(sysAndMakFileMap.getKey()) && systemBasedFileMap.get(sys) != null && !systemBasedFileMap.get(sys).contains(missingMakFile.get())) {
						searchRes = new ArrayList<>();
						searchRes = commonHelper.getProdAndNonProdMakSegs(lUser, missingMakFile.get(), sys, implId);
						if (searchRes != null && !searchRes.isEmpty()) {
						    gitSearchResults.addAll(searchRes);
						}
					    }
					}
				    }
				}
			    }
			    isMakFileMissing = true;
			    break;
			}
		    }
		}
	    }

	    HashMap<String, List<GitSearchResult>> fileBasedFinalSearchResult = new HashMap<>();
	    HashMap<String, GitSearchResult> fileWithHashAndGitRes = new HashMap<>();
	    Collections.sort(gitSearchResults, new GitSearchResult());
	    gitSearchResults.forEach(gitRes -> {
		if (fileWithHashAndGitRes.containsKey(gitRes.getFileNameWithHash()) && !fileWithHashAndGitRes.get(gitRes.getFileNameWithHash()).getTargetSystems().stream().anyMatch(sys -> gitRes.getTargetSystems().contains(sys))) {
		    GitSearchResult gitSearchResult = fileWithHashAndGitRes.get(gitRes.getFileNameWithHash());
		    gitSearchResult.getTargetSystems().addAll(gitRes.getTargetSystems());
		    gitSearchResult.getBranch().addAll(gitRes.getBranch());
		    fileWithHashAndGitRes.put(gitRes.getFileNameWithHash(), gitSearchResult);
		} else if (!fileWithHashAndGitRes.containsKey(gitRes.getFileNameWithHash())) {
		    fileWithHashAndGitRes.put(gitRes.getFileNameWithHash(), gitRes);
		}
	    });

	    count = fileWithHashAndGitRes.size();
	    List<GitSearchResult> searchList = new ArrayList<>();

	    if (count == 0 && fileWithHashAndGitRes.isEmpty()) {
		searchList = pSearchResults;
		addCacheData(systemAndCheckoutFile, SysBasedFileMakinfo, cachekey);

	    } else {
		for (Map.Entry<String, GitSearchResult> finalResult : fileWithHashAndGitRes.entrySet()) {
		    searchList.add(finalResult.getValue());
		}
	    }

	    Collections.sort(searchList, new GitSearchResult());
	    List<GitSearchResult> sortedSearchList = searchList.stream().sorted(Comparator.comparing(GitSearchResult::getBranchMaxLoadDate, Comparator.nullsFirst(Date::compareTo)).reversed()).collect(Collectors.toList());
	    int searchCount = 0;
	    for (GitSearchResult gitRes : sortedSearchList) {
		searchCount += 1;
		fileBasedFinalSearchResult.put((String.format("%010d", searchCount)), Arrays.asList(gitRes));
	    }

	    if (!checkoutMsg.toString().isEmpty()) {
		lResponse.setMetaData(checkoutMsg.toString());
	    }
	    lResponse.setCount(count);
	    lResponse.setStatus(true);
	    lResponse.setData(fileBasedFinalSearchResult);
	    lResponse.setErrorMessage(warningMsg);
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in validate Mak files from Non-production", ex);
	}
	return lResponse;
    }

    @Transactional
    public String checkoutFileLocalWorkSpace(User lUser, String implId, List<GitSearchResult> pSearchResults) {
	HashMap<String, Set<String>> systemAndCheckoutFileName = new HashMap<>();
	HashMap<String, String> systemWithFileNameAndHashVal = new HashMap<>();
	pSearchResults.forEach(pRes -> {
	    pRes.getBranch().forEach(sys -> {
		String sysName = sys.getTargetSystem().trim().toUpperCase().replace("MASTER_", "");
		systemWithFileNameAndHashVal.put(sysName + "-" + pRes.getFileName(), pRes.getFileNameWithHash());
		if (sys.isIsCheckoutAllowed()) {
		    if (systemAndCheckoutFileName.containsKey(sysName)) {
			systemAndCheckoutFileName.get(sysName).add(pRes.getFileName());
		    } else {
			Set<String> fileNames = new HashSet<>();
			fileNames.add(pRes.getFileName());
			systemAndCheckoutFileName.put(sysName, fileNames);
		    }
		}
	    });
	});
	// ZTPFM-1686
	StringBuilder sb = new StringBuilder();
	Implementation lImp = getImplementationDAO().find(implId);
	systemAndCheckoutFileName.entrySet().stream().map((entryFile) -> {
	    JSONResponse lCommandJsonResponse = new JSONResponse();
	    String fileNames = entryFile.getValue().stream().collect(Collectors.joining(","));
	    System lSystem = getSystemDAO().findByName(entryFile.getKey());
	    LOG.info("System Name " + lSystem.getName() + "File Names " + fileNames);
	    String command = Constants.SystemScripts.CHECK_LOCAL_WORKSPACE_FILE.getScript() + lImp.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase() + " " + fileNames;
	    lCommandJsonResponse = getsSHClientUtils().executeCommand(lUser, lSystem, command);
	    return lCommandJsonResponse;
	}).filter((lCommandJsonResponse) -> (lCommandJsonResponse.getStatus())).forEachOrdered((lCommandJsonResponse) -> {
	    sb.append(sb.toString().isEmpty() ? "" : ",");
	    sb.append(lCommandJsonResponse.getData().toString());
	});
	String warningMsg = "";
	if (!sb.toString().isEmpty()) {
	    warningMsg = MessageFormat.format("The following Segments are already present in local workspace - {0}." + " Please proceed to override the file to continue checkout.", sb.toString());
	}
	return warningMsg;
    }

    @Transactional
    public JSONResponse getGitRevision(User lUser, String implId, List<GitSearchResult> pSearchResult) throws Exception {
	JSONResponse lResponse = new JSONResponse();

	StringBuffer gitRevisionMessage = new StringBuffer();
	List<CheckoutSegments> lSegmentsList = new ArrayList<>();

	for (GitSearchResult lSearchResult : pSearchResult) {
	    List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
	    for (GitBranchSearchResult lBranch : lBranches) {
		if (lBranch.getIsBranchSelected()) {
		    CheckoutSegments lSegment = new CheckoutSegments();
		    BeanUtils.copyProperties(lSegment, lBranch);
		    BeanUtils.copyProperties(lSegment, lSearchResult);
		    lSegment.setTargetSystem(lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase());
		    lSegmentsList.add(lSegment);
		}
	    }
	}

	Map<String, List<CheckoutSegments>> lGrouped = lSegmentsList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));
	for (Map.Entry<String, List<CheckoutSegments>> entrySet : lGrouped.entrySet()) {
	    String lSystemName = entrySet.getKey();
	    List<CheckoutSegments> lSegments = entrySet.getValue();
	    System lSystem = getSystemDAO().findByName(lSystemName);
	    ArrayList<String> lList = new ArrayList<>();
	    lSegments.stream().forEach(t -> lList.add(t.getFileName()));
	    if (!lList.isEmpty()) {
		// Git comment revision
		String lCommand = Constants.SystemScripts.GIT_REVISION.getScript() + " \"" + (implId + "_" + lSystemName).toLowerCase() + "\" \"" + String.join(",", lList) + "\"";
		lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);
		if (!lResponse.getStatus()) {
		    if (gitRevisionMessage.length() > 0) {
			gitRevisionMessage.append(" , ");
		    }
		    gitRevisionMessage.append(lResponse.getErrorMessage().replace("Error Code: 8 ", ""));

		}
	    }
	}
	if (gitRevisionMessage.length() > 0) {
	    gitRevisionMessage.insert(0, "Attn: No revision history has been added to the following:");
	}
	lResponse.setStatus(true);
	lResponse.setData(gitRevisionMessage);
	return lResponse;
    }

    @Transactional
    public JSONResponse createXMLforGIProject(User pUser, String pImpl) {
	Projects lProjects = null;
	HashMap<String, String> lProjectPaths = new HashMap<>();
	HashMap<String, String> lProjectDelPaths = new HashMap<>();
	List<String> lActualProjectPaths = new ArrayList<>();
	Request lRequest = new Request();
	JSONResponse lResponse = new JSONResponse();
	GIHTTPClient lClient = new GIHTTPClient();

	Set<String> lSystems = getCheckoutSegmentsDAO().findByImplementation(pImpl).stream().map(CheckoutSegments::getTargetSystem).collect(Collectors.toSet());
	for (String lSystem : lSystems) {
	    // 2798 - Remove the extra slash at the end of path
	    lActualProjectPaths.add(pUser.getHomeDirectory() + "/projects/" + pImpl.toLowerCase() + "/" + lSystem.toLowerCase());
	    LOG.info("Actual Paths : " + pUser.getHomeDirectory() + "/projects/" + pImpl.toLowerCase() + "/" + lSystem.toLowerCase());
	}

	List<GiPorts> lPorts = giPortsDAO.findByUserId(pUser.getId());
	if (lPorts == null || lPorts.size() != 1) {
	    throw new WorkflowException("GI Ports are not Configured Properly");
	}

	try {
	    lProjects = lClient.getProjectList(lPorts.get(0).getIpAddr(), lPorts.get(0).getPortNo());
	} catch (Exception e) {
	    LOG.error("Error in getting Project List from GI", e);
	    throw new WorkflowException("Error in getting Project List from GI");
	}

	if (lProjects == null) {
	    lProjects = new Projects();
	}

	for (Project project : lProjects.getProject()) {
	    if (project.getProjName().startsWith(pImpl.toLowerCase())) {
		lProjectPaths.put(project.getDirRule().get(0).getDirectory().getPath(), project.getProjName());
		LOG.info("Already Existin Paths : " + project.getDirRule().get(0).getDirectory().getPath());
	    }
	}

	ArrayList<String> lPathsToDelete = new ArrayList<>();
	for (Project project : lProjects.getProject()) {
	    if (!project.getProjName().startsWith(pImpl.toLowerCase())) {
		for (String lSystem : lSystems) {
		    if (!project.getDirRule().isEmpty() && project.getDirRule().get(0).getDirectory().getPath().endsWith("projects/" + pImpl.toLowerCase() + "/" + lSystem.toLowerCase())) {
			LOG.info("Already Existin Paths (1) : " + project.getDirRule().get(0).getDirectory().getPath());
			if (!lProjectPaths.containsKey(project.getDirRule().get(0).getDirectory().getPath())) {
			    lProjectPaths.put(project.getDirRule().get(0).getDirectory().getPath(), project.getProjName());
			    LOG.info("Already Existin Paths (2) : " + project.getDirRule().get(0).getDirectory().getPath());
			} else {
			    String lRandom = RandomStringUtils.randomAlphanumeric(5);
			    lPathsToDelete.add(project.getDirRule().get(0).getDirectory().getPath() + "_" + lRandom);
			    lProjectDelPaths.put(project.getDirRule().get(0).getDirectory().getPath() + "_" + lRandom, project.getProjName());
			    LOG.info("Deleting Paths : " + project.getDirRule().get(0).getDirectory().getPath() + " Project : " + project.getProjName());
			}
			break;
		    }
		}
	    }
	}

	Collection<String> lPathsToAdd = CollectionUtils.subtract(lActualProjectPaths, lProjectPaths.keySet());
	lPathsToDelete.addAll(CollectionUtils.subtract(lProjectPaths.keySet(), lActualProjectPaths));

	for (String lDelPath : lPathsToDelete) {
	    LOG.info("Delete Project Path : " + lDelPath);
	    com.tsi.workflow.gi.request.Service lService = new com.tsi.workflow.gi.request.Service();
	    ProjectDelete projectDelete = new ProjectDelete();
	    String lDeleteProject = lProjectDelPaths.get(lDelPath);
	    if (lDeleteProject == null) {
		lDeleteProject = lProjectPaths.get(lDelPath);
	    }
	    projectDelete.setProjName(lDeleteProject);
	    lService.getProjectCreateOrProjectAddDirRuleOrProjectAddFileRuleOrProjectRefreshOrProjectDeleteOrProjectsListOrProjectSetTargetOrFilesOpenFile().add(projectDelete);
	    lRequest.getService().add(lService);
	}

	for (String lAddProjPath : lPathsToAdd) {
	    LOG.info("Add Project Path : " + lAddProjPath);
	    String[] lValues = lAddProjPath.split("\\/");
	    LOG.info(lValues.length + " " + String.join(",", lValues));
	    System lSystem = getSystemDAO().findByName(lValues[5].toUpperCase());
	    {
		com.tsi.workflow.gi.request.Service lService = new com.tsi.workflow.gi.request.Service();
		ProjectCreate projectCreate = new ProjectCreate();
		projectCreate.setProjName(lValues[4] + "_" + lValues[5]);
		projectCreate.setProcessingType(Constants.GIProcessingType.get(lValues[5].toUpperCase()));
		projectCreate.setPlatform(Constants.GIPlatform.get(lValues[5].toUpperCase()));
		projectCreate.setTarget(Constants.GITarget.get(lValues[5].toUpperCase()));
		ConfigFile lConfigFile = new ConfigFile();
		lConfigFile.setFileName("proj.maketpf.cfg");
		lConfigFile.setHostOrIP(lSystem.getIpaddress());
		lConfigFile.setPath(lAddProjPath);
		lConfigFile.setUserID(pUser.getId());
		projectCreate.setConfigFile(lConfigFile);
		lService.getProjectCreateOrProjectAddDirRuleOrProjectAddFileRuleOrProjectRefreshOrProjectDeleteOrProjectsListOrProjectSetTargetOrFilesOpenFile().add(projectCreate);
		lRequest.getService().add(lService);
	    }
	    {
		com.tsi.workflow.gi.request.Service lService = new com.tsi.workflow.gi.request.Service();
		ProjectAddDirRule projectRule = new ProjectAddDirRule();
		projectRule.setMask("*");
		projectRule.setRecurse("true");
		projectRule.setProjName(lValues[4] + "_" + lValues[5]);
		Directory lDirectory = new Directory();
		lDirectory.setHostOrIP(lSystem.getIpaddress());
		lDirectory.setPath(lAddProjPath);
		lDirectory.setUserID(pUser.getId());
		projectRule.setDirectory(lDirectory);
		lService.getProjectCreateOrProjectAddDirRuleOrProjectAddFileRuleOrProjectRefreshOrProjectDeleteOrProjectsListOrProjectSetTargetOrFilesOpenFile().add(projectRule);
		lRequest.getService().add(lService);
	    }
	}
	if (!lRequest.getService().isEmpty()) {
	    try {
		lResponse = lClient.processProjectSync(lPorts.get(0).getIpAddr(), lPorts.get(0).getPortNo(), lRequest);
	    } catch (Exception e) {
		LOG.error("Error in getting Project Sync from GI", e);
		throw new WorkflowException("Error in getting Project Sync from GI");
	    }
	    if (lResponse.getStatus()) {
		lResponse.setData("Project Workspace created/updated " + pImpl + " for the applicable target system(s) ");
	    }
	    return lResponse;
	} else {
	    lResponse.setStatus(Boolean.FALSE);
	    if (!lSystems.isEmpty()) {
		lResponse.setErrorMessage("Project Workspace already exists for " + pImpl + " for target system(s) " + String.join(",", lSystems));
	    } else {
		lResponse.setErrorMessage("Please checkout any segments to create Project Workspace for " + pImpl);
	    }
	    return lResponse;
	}
    }

    // 2156 - Common Code validation for Implementation during check in
    private JSONResponse doCommonCodeValidationOnImpl(User user, String impId) throws WorkflowException, Exception {
	JSONResponse lReturn = new JSONResponse();
	Map<String, String> lSegWithHashCode = new HashMap();
	Map<String, Set<String>> lSystemWithSegments = new HashMap();
	Map<String, Set<String>> lErrSegWithCommonSystems = new HashMap();
	Implementation lImplementation = getImplementationDAO().find(impId);
	List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImplementation(impId);
	lSegments.stream().filter(t -> t.getCommonFile()).forEach((lSegment) -> {
	    if (lSystemWithSegments.get(lSegment.getTargetSystem()) == null) {
		lSystemWithSegments.put(lSegment.getTargetSystem(), new HashSet(Arrays.asList(lSegment.getFileName())));
	    } else {
		lSystemWithSegments.get(lSegment.getTargetSystem()).add(lSegment.getFileName());
	    }
	});
	for (Map.Entry<String, Set<String>> lSystemWithSegment : lSystemWithSegments.entrySet()) {
	    System lSystem = getSystemDAO().findByName(lSystemWithSegment.getKey());

	    // Input Param: ${MTP_ENV}/mtptpfcommoncode “t1900001_001_wsp”
	    // “src/abcd.asm,src/efch.asm,src/ijkl.asm”
	    String lCommand = Constants.SystemScripts.COMMON_CODE_VALIDATION.getScript() + (impId + "_" + lSystemWithSegment.getKey()).toLowerCase() + " " + String.join(",", lSystemWithSegment.getValue());
	    JSONResponse lResponse = getsSHClientUtils().executeCommand(user, lSystem, lCommand);

	    if (!lResponse.getStatus()) {
		String errorMsg = lResponse.getErrorMessage();
		lResponse.clearErrorMessage();
		lResponse.setErrorMessage("Error Occurs during the Common code validation for Implementation - " + impId + " , Log Message: " + errorMsg);
		CheckinActivityMessage lMessage = new CheckinActivityMessage(lImplementation.getPlanId(), lImplementation);
		lMessage.setError(true);
		lMessage.setComments(lResponse.getErrorMessage());
		getActivityLogDAO().save(user, lMessage);
		lResponse.setStatus(Boolean.FALSE);
		LOG.info("Error Message from Shell - " + lResponse.getErrorMessage());
		return lResponse;
	    } else {
		// Output: src/abcd.asm:hashcode,src/efch.asm:hashcode, src/ijkl.asm:hashcode
		String[] lSegVsHashcodes = lResponse.getData().toString().trim().split(",");

		Arrays.asList(lSegVsHashcodes).stream().forEach((lSegment) -> {
		    LOG.info("Data :" + lSegment);
		    String[] lSegVsHashcode = lSegment.trim().split(":");
		    if (lSegWithHashCode.get(lSegVsHashcode[0]) == null) {
			lSegWithHashCode.put(lSegVsHashcode[0], lSegVsHashcode[1]);
		    } else {
			String lHashCode = lSegWithHashCode.get(lSegVsHashcode[0]);
			if (!lHashCode.equals(lSegVsHashcode[1])) {
			    Set<String> lSystemLists = lErrSegWithCommonSystems.get(lSegVsHashcode[0]);
			    if (lSystemLists == null) {
				lSystemLists = new HashSet();
				lSystemLists.add(lSystem.getName());
				lErrSegWithCommonSystems.put(lSegVsHashcode[0], lSystemLists);
			    } else {
				lErrSegWithCommonSystems.get(lSegVsHashcode[0]).add(lSystem.getName());
			    }
			}
		    }
		});
	    }
	}

	if (!lErrSegWithCommonSystems.isEmpty()) {
	    StringBuilder lErrorMessage = new StringBuilder("");
	    lErrorMessage.append("Source artifact(s) indicated as common at checkout are not identical now. Please ensure they are edited and saved using zTPFGI Project View").append("<br>");
	    lErrSegWithCommonSystems.entrySet().forEach((lErrSegWithCommonSystem) -> {
		lErrorMessage.append(lErrSegWithCommonSystem.getKey()).append(" on ").append(String.join(",", lErrSegWithCommonSystem.getValue())).append("<br>");
	    });
	    throw new WorkflowException(lErrorMessage.toString());
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    /*
     * Method Name: sendCheckInMailOnSegmentsUpdated Created BY: Radhakrishnan
     * Created Dt: 19-Sept-2019 Created for: ZTPFM-2413 Description: Send mail
     * notification to all developer and Lead when there is changes in the SO
     * (Deleted or Updated)
     */
    private void sendCheckInMailOnSegmentsUpdates(Implementation impl, List<String> soLists) {
	CommonMail lSegmentsUpdatedMail = (CommonMail) mailMessageFactory.getTemplate(CommonMail.class);
	String lSubject = impl.getId() + " - checked in and some previously created Integration Build .so files are deleted or new .so files need to be built ";
	StringBuilder lMessage = new StringBuilder("");
	lMessage.append(impl.getId()).append(" has been checked in. Some previously created Integration Build .so files are deleted or new .so files need to be built . Please perform a build / loadset generation if required for Integration Testing");
	impl.getPlanId().getImplementationList().stream().forEach(t -> {
	    lSegmentsUpdatedMail.addToAddressUserId(t.getDevId(), Constants.MailSenderRole.DEVELOPER);
	});
	lSegmentsUpdatedMail.addcCAddressUserId(impl.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
	lSegmentsUpdatedMail.setMessage(lMessage.toString());
	lSegmentsUpdatedMail.setSubject(lSubject);
	mailMessageFactory.push(lSegmentsUpdatedMail);
    }

    /*
     * Method Name: updateActivityLogOnSegmentsUpdates Created BY: Radhakrishnan
     * Created Dt: 19-Sept-2019 Created for: ZTPFM-2413 Description: update activity
     * log of plan when there is changes in the SO (Deleted or Updated) in
     * implementation
     */
    private void updateActivityLogOnSegmentsUpdates(User user, Implementation impl, List<String> soLists) {
	String lMessage = impl.getId() + " - Checked in and some previously created Integration Build .so files are deleted ";
	CommonActivityMessage lLogMessage = new CommonActivityMessage(impl.getPlanId(), null);
	lLogMessage.setMessage(lMessage);
	lLogMessage.setStatus("Pass");
	getActivityLogDAO().save(user, lLogMessage);

    }

    private JSONResponse markImplementationAsReviewCompleted(User user, Implementation impl) throws Exception {
	JSONResponse lReturn = new JSONResponse();
	String lCompanyName = impl.getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, impl.getPlanId().getId().toLowerCase());
	List<String> lBranchList = new ArrayList();
	lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, impl.getPlanId().getId());

	for (int i = 0; i < lBranchList.size(); i++) {
	    if (!lBranchList.get(i).contains(impl.getId().toLowerCase())) {
		lBranchList.remove(i);
	    }
	}

	if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED, lBranchList)) {
	    LOG.error("Peer Review Completed Tag is not completed for Implementation Id -" + impl.getId());
	}

	impl.setPeerReview("Y");
	impl.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	getImplementationDAO().update(user, impl);

	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    private String getNewImplementationId(String planId) {
	List<Implementation> implementationList = getImplementationDAO().getImplementationList(planId);
	if (implementationList != null && !implementationList.isEmpty()) {
	    return planId + "_" + String.format("%03d", implementationList.stream().mapToInt(imp -> Integer.parseInt(imp.getId().replaceAll(planId + "_", ""))).max().getAsInt() + 1);
	}
	return planId + "_" + String.format("%03d", 1);
    }

    @Transactional
    public JSONResponse getInboxImplementationList(User pUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) {

	JSONResponse lResponse = new JSONResponse();
	int start = pLimit * pOffset;
	List<Implementation> implementationList = new ArrayList<>();
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : pOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime")) {
		    implementationList = getImplementationDAO().findImpByLoadDateTime(pUser.getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), pOffset, pLimit, pOrderBy, pFilter);
		} else {
		    implementationList = getImplementationDAO().findImpByDeveloper(pUser.getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), pOffset, pLimit, pOrderBy, pFilter);
		}
	    }
	} else {
	    implementationList = getImplementationDAO().findImpByDeveloper(pUser.getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), pOffset, pLimit, pOrderBy, pFilter);
	}

	if (implementationList.size() == 0) {
	    lResponse.setCount(0);
	    lResponse.setErrorMessage("No Plans Found");
	    lResponse.setStatus(Boolean.FALSE);
	    return lResponse;
	}
	lResponse.setData(getCommonBaseService().getImplAndSysLoadDetails(implementationList));
	lResponse.setCount(getImplementationDAO().countImpByDeveloper(pUser.getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), pFilter).intValue());
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;

    }

    public JSONResponse checkinParallelForSystem(User lUser, String implId) throws InterruptedException {
	JSONResponse lResponse = new JSONResponse();
	HashMap<String, HashMap> lResult = new HashMap<>();

	try {
	    Implementation lImplementation = getImplementationDAO().find(implId);

	    // 2156 - Common code validation
	    JSONResponse lCCValidation = doCommonCodeValidationOnImpl(lUser, implId);
	    if (!lCCValidation.getStatus()) {
		LOG.info("Error occurs in Common code validation for Implementation - " + implId);
		lCCValidation.setStatus(Boolean.FALSE);
		return lCCValidation;
	    }

	    Map<System, Boolean> sysAndStatus = new HashMap<>();
	    Boolean lCheckinStatus = Boolean.TRUE;
	    List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().findByImplementation(implId);
	    Implementation impl = getImplementationDAO().find(implId);
	    impl.setTktNum(null);
	    impl.setTktUrl(null);
	    getImplementationDAO().update(lUser, impl);
	    Map<String, List<CheckoutSegments>> lGrouped = lSegmentList.stream().collect(Collectors.groupingBy(t -> t.getTargetSystem()));

	    Map<String, List<String>> systemBasedChangedSegs = new HashMap<>();

	    Set<String> lSystemList = lGrouped.keySet();
	    createParallelThreadsForCheckin(lUser, impl, lSystemList, systemBasedChangedSegs, lResult, sysAndStatus, lCheckinStatus);

	    // ZTPFM-2447 Code changes to upate Last changed value for recently updated
	    // segments
	    commonHelper.updateLatestChangeDate(lUser, systemBasedChangedSegs, lGrouped);
	    // 2413 we cannt use systemBasedChangedSegs as deleted file information not
	    // received from Shell during check in.
	    List<CheckoutSegments> lUpdatedSegments = getCheckoutSegmentsDAO().getChangedFilesInWorkspace(lImplementation.getId()); // 2413

	    if (lUpdatedSegments != null && !lUpdatedSegments.isEmpty()) { // 2413
		sendCheckInMailOnSegmentsUpdates(lImplementation, new ArrayList());
		updateActivityLogOnSegmentsUpdates(lUser, lImplementation, new ArrayList());
	    }

	    if (lCheckinStatus) {
		lImplementation.setIsCheckedin(Boolean.TRUE);
		lImplementation.setLastCheckinStatus(Constants.CheckinStatus.SUCCESS.getDescription());
		CheckinActivityMessage lMessage = new CheckinActivityMessage(lImplementation.getPlanId(), lImplementation);
		getActivityLogDAO().save(lUser, lMessage);
		sendNotificationOnCheckIn(implId);
	    } else {
		lImplementation.setIsCheckedin(Boolean.FALSE);
		lImplementation.setLastCheckinStatus(Constants.CheckinStatus.FAILED.getDescription());
	    }
	    if (lCheckinStatus && lImplementation.getPlanId().getRejectedDateTime() == null) {
		List<Build> lBuildList = getBuildDAO().findByImpPlan(lImplementation.getPlanId());
		for (Build lbuild : lBuildList) {
		    getBuildDAO().delete(lUser, lbuild);
		}
	    }
	    lImplementation.setCheckinDateTime(new Date());
	    getImplementationDAO().update(lUser, lImplementation);

	} catch (WorkflowException ex) {
	    LOG.info("Unable to Check-in the changes for Implementation - " + implId, ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to Check-in the changes for Implementation - " + implId, ex);
	    throw new WorkflowException("Unable to Check-in the changes for Implementation - " + implId);
	}
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(lResult);
	return lResponse;
    }

    private JSONResponse checkoutParallelForSystem(User lUser, String implId, List<GitSearchResult> pSearchResults) {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    LOG.info("Is Present in In-progress queue : " + getCacheClient().getInprogressCheckoutMap().containsKey(implId));
	    if (getCacheClient().getInprogressCheckoutMap().containsKey(implId)) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Previous Checkout operation for implementation : " + implId + " is In-Progress, please try to checkout after completion of previous operation");
		return lResponse;
	    }

	    // segment added to checkout-inprogress map
	    getCacheClient().getInprogressCheckoutMap().put(implId, "CHECKOUT_INPROGRESS");
	    LOG.info("ADDED to In-progress queue : " + getCacheClient().getInprogressCheckoutMap().containsKey(implId));

	    Implementation lImplementation = getImplementationDAO().find(implId);
	    ImpPlan lPlan = lImplementation.getPlanId();
	    String lNickName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();

	    Set<String> lBranchList = new HashSet<>();
	    Map<String, List<CheckoutSegments>> lSegmentsMap = new HashMap<>();
	    Map<String, GitBranchSearchResult> lTempGitSearchSegments = new HashMap<>();
	    Map<String, GitSearchResult> lTempGitMAKSearchSegments = new HashMap<>();

	    // Populating Set and Map
	    for (GitSearchResult lSearchResult : pSearchResults) {
		List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
		long count = lBranches.stream().filter(p -> p.getIsBranchSelected()).count();
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (lBranch.getIsBranchSelected()) {
			String lMasterBranchName = lBranch.getTargetSystem();
			lBranchList.add(lMasterBranchName.toLowerCase());
			String lSystem = lMasterBranchName.replace(Constants.BRANCH_MASTER, "");
			lBranchList.add(lMasterBranchName.replace("master", lImplementation.getId().toLowerCase()));
			CheckoutSegments lSegment = new CheckoutSegments();
			BeanUtils.copyProperties(lSegment, lBranch);
			BeanUtils.copyProperties(lSegment, lSearchResult);
			String targetSystem = lSegment.getTargetSystem().replaceAll(Constants.BRANCH_MASTER, "").toUpperCase();

			lSegment.setTargetSystem(targetSystem);
			lSegment.setCommonFile(count > 1);
			if (lSegmentsMap.get(lSystem) == null) {
			    lSegmentsMap.put(lSystem, new ArrayList<>());
			}
			lSegmentsMap.get(lSystem).add(lSegment);
			String idString = CheckoutUtils.getIdString(lSegment);
			lTempGitSearchSegments.put(idString, lBranch);
		    }
		}
	    }

	    // Creating Branches
	    Map<String, Boolean> lBranchStatus = getJGitClientUtils().createBranches(lPlan.getId(), lBranchList, lNickName);
	    if (!lBranchStatus.isEmpty()) {
		for (Map.Entry<String, Boolean> entrySet : lBranchStatus.entrySet()) {
		    String key = entrySet.getKey();
		    Boolean value = entrySet.getValue();
		    if (!value) {
			LOG.error("Error in creating branches for " + key);
			throw new WorkflowException("Unable to checkout the segments as error occurs on creating branches, Contact Tool Support");
		    }
		}
	    }

	    NewTargetSystemMail newTargetSystemMailNotification = (NewTargetSystemMail) getMailMessageFactory().getTemplate(NewTargetSystemMail.class);
	    newTargetSystemMailNotification.setImplementationId(implId);
	    newTargetSystemMailNotification.setUserDetails(lUser);
	    newTargetSystemMailNotification.setLeadId(lPlan.getLeadId());

	    // For each System LOOP
	    List<Future<TdxShellExecutorModel>> systemWiseCheckout = new ArrayList();
	    for (Map.Entry<String, List<CheckoutSegments>> lSystemEntry : lSegmentsMap.entrySet()) {
		TdxShellExecutorModel executorModel = new TdxShellExecutorModel();
		executorModel.setImpl(lImplementation);
		executorModel.setlSystemEntry(lSystemEntry);
		executorModel.setUser(lUser);
		executorModel.setpSearchResults(pSearchResults);
		executorModel.setlTempGitSearchSegments(lTempGitSearchSegments);
		executorModel.setNewTargetSystemMailNotification(newTargetSystemMailNotification);
		Future<TdxShellExecutorModel> executeCheckout = tdxShellExecutor.executeCheckOut(executorModel);
		systemWiseCheckout.add(executeCheckout);

	    }

	    int sysCount = 0;
	    while (sysCount != lSegmentsMap.size()) {
		List<Future<TdxShellExecutorModel>> removeSystem = new ArrayList();
		for (Future<TdxShellExecutorModel> executeCheckout : systemWiseCheckout) {
		    while (executeCheckout.isDone()) {
			sysCount += 1;
			if (executeCheckout.get() != null) {
			    TdxShellExecutorModel executorModel = executeCheckout.get();
			    if (executorModel.getNewTargetSystemMailNotification() != null) {
				newTargetSystemMailNotification = executorModel.getNewTargetSystemMailNotification();
			    }
			    removeSystem.add(executeCheckout);
			    break;
			}
		    }
		}
		if (!removeSystem.isEmpty()) {
		    systemWiseCheckout.removeAll(removeSystem);
		}
	    }

	    if (!newTargetSystemMailNotification.getTargetSystem().isEmpty()) {
		getMailMessageFactory().push(newTargetSystemMailNotification);
	    }
	    if (lImplementation.getSubstatus() != null) {
		revertImpStatus(lUser, implId);
	    } else {
		lImplementation.setIsCheckedin(Boolean.FALSE);
		getImplementationDAO().update(lUser, lImplementation);
	    }
	    // Diverged File notification starts
	    for (GitSearchResult lSearchResult : pSearchResults) {
		SortedSet<String> targetSystemsSet = lSearchResult.getTargetSystems();
		List<GitBranchSearchResult> lBranches = lSearchResult.getBranch();
		long count = lBranches.stream().filter(p -> p.getIsBranchSelected()).count();
		List<GitBranchSearchResult> divergedGitBranchList = new ArrayList<>();
		String programName = lSearchResult.getProgramName();
		String lRepoName = getGitHelper().getRepositoryNameBySourceURL(lSearchResult.getBranch().get(0).getSourceUrl());
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (targetSystemsSet.size() != count) {
			divergedGitBranchList.add(lBranch);
		    }
		}
		if (divergedGitBranchList.size() > 0 && targetSystemsSet.size() != divergedGitBranchList.size()) {
		    RepositoryView repoValue1 = getCacheClient().getFilteredRepositoryMap().get(lRepoName.replace(".git", "").toUpperCase());
		    if (repoValue1 == null) {
			LOG.info("Unable to get repository details for " + lRepoName + " so, unable to send mail lead about diverge of sgement");
			continue;
		    }
		    Repository repoValue = repoValue1.getRepository();
		    if (repoValue.getOwners() != null && repoValue.getOwners().size() > 0) {
			Set<String> checkoutTargetSystem = divergedGitBranchList.stream().map(GitBranchSearchResult::getTargetSystem).collect(Collectors.toSet());
			DivergedCheckoutSourceArtifactMail divergedSourceArtifactMail = (DivergedCheckoutSourceArtifactMail) getMailMessageFactory().getTemplate(DivergedCheckoutSourceArtifactMail.class);
			divergedSourceArtifactMail.setSourceArtifact(programName);
			divergedSourceArtifactMail.setTargetSystemBefore(String.join(",", targetSystemsSet).replace("master_", ""));
			divergedSourceArtifactMail.setCheckedOutTargetSystem(String.join(",", checkoutTargetSystem).replace("master_", ""));
			divergedSourceArtifactMail.setDeveloper(lUser.getDisplayName());
			targetSystemsSet.removeAll(checkoutTargetSystem);
			divergedSourceArtifactMail.setTargetSystemAfter(String.join(",", targetSystemsSet).replace("master_", ""));
			repoValue.getOwners().stream().forEach(t -> divergedSourceArtifactMail.addToAddressUserId(t, Constants.MailSenderRole.REPO_OWNERS));
			getMailMessageFactory().push(divergedSourceArtifactMail);
		    }
		}
	    }
	    // Diverged File notification Ends
	    pSearchResults.addAll(lTempGitMAKSearchSegments.values());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setData(pSearchResults);
	    return lResponse;
	} catch (Exception ex) {
	    LOG.error("unable to checkout", ex);
	    throw new WorkflowException("Unable to checkout the segments ", ex);
	} finally {
	    LOG.info("REMOVED");
	    getCacheClient().getInprogressCheckoutMap().remove(implId);
	    LOG.info("REMOVED : " + getCacheClient().getInprogressCheckoutMap().containsKey(implId));
	}

    }

    private void addCacheData(HashMap<String, Set<String>> systemAndCheckoutFile, Map<String, String> SysBasedFileMakinfo, String cachekey) {
	List<String> programNameList = new ArrayList<>();
	systemAndCheckoutFile.forEach((key, value) -> {
	    value.forEach((t) -> {
		programNameList.add(t + "_" + key);

		if (!t.endsWith(".mak") && !SysBasedFileMakinfo.containsKey(t + "_" + key)) {
		    SysBasedFileMakinfo.put(t + "_" + key, "MAK_NOT_FOUND");
		}
	    });
	});

	if (!getCacheClient().getValidateMakFileMap().containsKey(cachekey)) {
	    getCacheClient().getValidateMakFileMap().put(cachekey, new HashMap<>());
	}
	List<String> finalProgramNameList = new ArrayList<>();
	Map<String, String> finalMAKfileMap = new HashMap<>();
	Map<List<String>, Map<String, String>> finalCacheData = new HashMap<>();
	for (Map.Entry<List<String>, Map<String, String>> entry : getCacheClient().getValidateMakFileMap().get(cachekey).entrySet()) {
	    finalProgramNameList = entry.getKey();
	    finalMAKfileMap = entry.getValue();
	}
	finalProgramNameList.addAll(programNameList);
	finalMAKfileMap.putAll(SysBasedFileMakinfo);
	finalCacheData.put(finalProgramNameList.stream().distinct().collect(Collectors.toList()), finalMAKfileMap);
	getCacheClient().getValidateMakFileMap().put(cachekey, finalCacheData);
    }

}
