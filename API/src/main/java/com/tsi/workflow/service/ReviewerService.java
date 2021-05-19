/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.ReviewNotDoneActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Service
public class ReviewerService extends BaseService {

    private static final Logger LOG = Logger.getLogger(ReviewerService.class.getName());

    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    JGitClientUtils jGitClientUtils;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    LDAPAuthenticatorImpl authenticator;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public JGitClientUtils getJGitClientUtils() {
	return jGitClientUtils;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    @Transactional
    public JSONResponse getUserTaskList(User pUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setCount(getImplementationDAO().countByReviewer(pUser.getCurrentOrDelagateUser().getId()).intValue());
	lResponse.setData(getImplementationDAO().findByReviewer(pUser.getCurrentOrDelagateUser().getId(), pOffset, pLimit, pOrderBy));
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getUserTaskListHistory(User pUser, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setCount(getImplementationDAO().countByReviewerHistory(pUser.getCurrentOrDelagateUser().getId()).intValue());
	lResponse.setData(getImplementationDAO().findByReviewerHistory(pUser.getCurrentOrDelagateUser().getId(), pOffset, pLimit, pOrderBy));
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse approveReview(User pUser, String pImplId) {
	JSONResponse lJSONResponse = new JSONResponse();
	try {
	    Implementation lImplementation = getImplementationDAO().find(pImplId);
	    ImpPlan lPlan = getImpPlanDAO().find(lImplementation.getPlanId().getId());
	    // Reviewer Check

	    List<String> lAllReviewers = new ArrayList<>(Arrays.asList(lImplementation.getPeerReviewers().split("\\,")));
	    Set<String> lDoneReviewers = new HashSet<>();
	    if (lImplementation.getReviewersDone() != null && !lImplementation.getReviewersDone().isEmpty()) {
		lDoneReviewers = new HashSet<>(Arrays.asList(lImplementation.getReviewersDone().split("\\,")));
	    }
	    lDoneReviewers.add(pUser.getCurrentOrDelagateUser().getId());
	    if (lDoneReviewers.containsAll(lAllReviewers)) {
		List<CheckoutSegments> findByImplementation = getCheckoutSegmentsDAO().findByImplementation(lImplementation);
		List<CheckoutSegments> findAllUniqSegments = removeDuplicates(findByImplementation);
		for (CheckoutSegments checkoutSegments : findAllUniqSegments) {
		    if (!checkoutSegments.getReviewStatus()) {
			lJSONResponse.setStatus(Boolean.FALSE);
			lJSONResponse.setErrorMessage("There are few segments which are not yet reviewed");
			return lJSONResponse;
		    }
		}
		String lCompanyName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
		String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, lImplementation.getPlanId().getId().toLowerCase());
		List<String> lBranchList = new ArrayList();
		lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, lImplementation.getPlanId().getId());

		for (int i = 0; i < lBranchList.size(); i++) {
		    if (!lBranchList.get(i).contains(lImplementation.getId().toLowerCase())) {
			lBranchList.remove(i);
		    }
		}

		if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED, lBranchList)) {
		    LOG.error("Peer Review Completed Tag is not completed for Implementation Id -" + lImplementation.getId());
		    lJSONResponse.setStatus(Boolean.FALSE);
		    lJSONResponse.setErrorMessage("Error in Approving Implementation");
		    return lJSONResponse;
		}

		lImplementation.setPeerReview("Y");
		lImplementation.setReviewMailFlag(Boolean.FALSE);
		lImplementation.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	    }
	    getPlanHelper().peerReviwerCompleted(pUser, pImplId, lImplementation, lPlan, lAllReviewers);
	    lImplementation.setReviewersDone(String.join(",", lDoneReviewers));
	    getImplementationDAO().update(pUser, lImplementation);

	    /**
	     * ZTPFM-2440 Reviewer Not Done List added
	     * 
	     * @author vinoth.ponnurangan
	     */

	    String reviewerNotDone = getReviewerNotCompletedList(pImplId, pUser, lPlan);
	    if (reviewerNotDone != null) {
		lJSONResponse.setData(reviewerNotDone);
	    }

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Error in Approval", ex);
	}
	lJSONResponse.setStatus(Boolean.TRUE);
	return lJSONResponse;
    }

    /**
     * Gets the reviewer not completed list.
     *
     */
    public String getReviewerNotCompletedList(String pImplId, User pUser, ImpPlan lPlan) {
	String lNotDoneReviewer = null;
	Implementation lImplementation = getImplementationDAO().find(pImplId);
	List<String> lNotDoneReviewerNameList = new ArrayList<>();
	List<String> lReviewerList = Arrays.asList(lImplementation.getPeerReviewers().split(","));
	List<String> lDoneReviewerList = Arrays.asList(lImplementation.getReviewersDone().split(","));
	List<String> lNotDoneReviewerList = new ArrayList<>(CollectionUtils.subtract(lReviewerList, lDoneReviewerList));
	if (!lNotDoneReviewerList.isEmpty()) {
	    for (String lReviewer : lNotDoneReviewerList) {
		lNotDoneReviewerNameList.add(getLDAPAuthenticatorImpl().getUserDetails(lReviewer).getDisplayName());
	    }
	    if (!lNotDoneReviewerNameList.isEmpty()) {
		lNotDoneReviewer = "Reviewers " + String.join(",", lNotDoneReviewerNameList) + " have to mark the implementation  " + pImplId + " as Peer Review Completed";
		// Activity Log
		ReviewNotDoneActivityMessage reviewNotDoneActivityMessage = new ReviewNotDoneActivityMessage(lPlan, lImplementation);
		reviewNotDoneActivityMessage.setReviewNotDoneRequest(Boolean.TRUE);
		reviewNotDoneActivityMessage.setlReviewerNotDoneList(lNotDoneReviewerNameList);
		getActivityLogDAO().save(pUser, reviewNotDoneActivityMessage);
	    }

	}
	return lNotDoneReviewer;
    }

    @Transactional
    public JSONResponse reviewSegments(User lUser, String implId, List<GitSearchResult> pSegments) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	try {
	    for (GitSearchResult lSearchResult : pSegments) {
		List<GitBranchSearchResult> lBranch = lSearchResult.getBranch();
		for (GitBranchSearchResult result : lBranch) {
		    List<CheckoutSegments> segmentList = getCheckoutSegmentsDAO().getCheckoutListfindByFileName(lSearchResult.getFileName(), implId, result.getTargetSystem(), result.getFuncArea().toUpperCase());
		    List<CheckoutSegments> findAllUniqSegments = removeDuplicates(segmentList);
		    for (CheckoutSegments segment : findAllUniqSegments) {
			if (segment.getTargetSystem().equals(result.getTargetSystem())) {
			    segment.setReviewStatus(Boolean.TRUE);
			    getCheckoutSegmentsDAO().update(lUser, segment);
			}
		    }
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Error in Marking Segemnts as Reviewed for Implementation", ex);
	    throw new WorkflowException("Error in Marking Checkedout Segments as Reviewed", ex);
	}
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public List<CheckoutSegments> removeDuplicates(List<CheckoutSegments> list) {
	Set set = new TreeSet(new Comparator() {

	    @Override
	    public int compare(Object o1, Object o2) {
		if (((CheckoutSegments) o1).getProgramName().equalsIgnoreCase(((CheckoutSegments) o2).getProgramName()) && ((CheckoutSegments) o1).getTargetSystem().equalsIgnoreCase(((CheckoutSegments) o2).getTargetSystem()) && ((CheckoutSegments) o1).getFuncArea().equalsIgnoreCase(((CheckoutSegments) o2).getFuncArea()) && ((CheckoutSegments) o1).getActive().equals("Y") && ((CheckoutSegments) o2).getActive().equals("Y")) {
		    return 0;
		}
		return 1;
	    }
	});
	set.addAll(list);

	final List newList = new ArrayList(set);
	return newList;
    }

}
