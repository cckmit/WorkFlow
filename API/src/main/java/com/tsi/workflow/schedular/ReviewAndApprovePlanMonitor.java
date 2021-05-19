/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.mail.PendingActionNotificationMail;
import com.tsi.workflow.service.ReviewerService;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vamsi.krishnarao
 */
@Component
public class ReviewAndApprovePlanMonitor {

    private static final Logger LOG = Logger.getLogger(ReviewAndApprovePlanMonitor.class.getName());

    @Autowired
    ReviewerService reviewerService;
    @Autowired
    @Qualifier("lLdapUserMap")
    ConcurrentHashMap<String, User> lLdapUserMap;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public ReviewerService getReviewerService() {
	return reviewerService;
    }

    /*
     * Corn Job scheduled changed every day at 12AM as per tim request .
     */
    @Scheduled(cron = "0 1 4 * * *")
    @Transactional
    public void doMonitor() {
	// Reviewer
	List<String> lImplementationList = getImplementationDAO().findAllPlansAndReviewers();
	Set<String> reviewersList = new HashSet<>();

	Set<String> impIdList = new HashSet<>();
	lImplementationList.forEach((imp) -> {
	    Implementation lImpl = getImplementationDAO().find(imp);
	    String peerReviewers = lImpl.getPeerReviewers();
	    if (peerReviewers.contains(",")) {
		List<String> split = Arrays.asList(peerReviewers.split(","));
		for (String lsplit : split) {
		    reviewersList.add(lsplit);
		}
	    } else {
		reviewersList.add(peerReviewers);
	    }
	});
	User pUser = lDAPAuthenticatorImpl.getServiceUser();
	Set<Implementation> lImplementations = new HashSet<>();
	// Need to remove review list send as single email for all
	for (String lreviewer : reviewersList) {
	    List<Implementation> pendingReview = new ArrayList<>();
	    List<String> devAndleadReviewtask = new ArrayList<>();
	    List<String> reviewImplementation = new ArrayList<>();

	    List<Object[]> lpendingReview = getImplementationDAO().findByReviewerBasedOnTime(lreviewer, Boolean.TRUE);
	    for (Object[] obj : lpendingReview) {
		String id = obj[0].toString();
		Implementation imp = getImplementationDAO().find(id);
		lImplementations.add(imp);
		pendingReview.add(imp);
	    }

	    if (!pendingReview.isEmpty()) {
		PendingActionNotificationMail reviewerMail = (PendingActionNotificationMail) getMailMessageFactory().getTemplate(PendingActionNotificationMail.class);
		for (Implementation list : pendingReview) {
		    devAndleadReviewtask.add(list.getDevId());
		    devAndleadReviewtask.add(list.getPlanId().getLeadId());
		    reviewerMail.addToAddressUserId(lreviewer, Constants.MailSenderRole.PEER_REVIEWER);
		    devAndleadReviewtask.stream().forEach(t -> reviewerMail.addcCAddressUserId(t, Constants.MailSenderRole.LEAD));
		    reviewImplementation.add(list.getId());
		    impIdList.add(list.getId());
		}
		reviewerMail.setAction("Peer Reviewed");
		reviewerMail.setImplementation(reviewImplementation);
		getMailMessageFactory().push(reviewerMail);
	    }
	}
	impIdList.forEach(id -> {
	    Implementation lImpl = lImplementations.stream().filter(imp -> imp.getId().equals(id)).findAny().get();
	    if (lImpl != null) {
		lImpl.setReviewMailFlag(Boolean.TRUE);
		getImplementationDAO().update(pUser, lImpl);
	    }
	});
	// DevManager
	Set<String> devManagerList = new HashSet<>();
	List<String> status = new ArrayList<>();
	status.add(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name());
	status.add(Constants.PlanStatus.APPROVED.name());
	List<ImpPlan> devList = getImpPlanDAO().passedAcceptanceList(status);
	for (ImpPlan imp : devList) {
	    devManagerList.add(imp.getDevManager());
	}
	for (String user : devManagerList) {
	    List<ImpPlan> pendingApprove = new ArrayList<>();
	    List<String> devAndleadApprovetask = new ArrayList<>();
	    List<String> devManagerImpPlan = new ArrayList<>();
	    List<ImpPlan> approveList = new ArrayList<>();
	    List<ImpPlan> macroList = new ArrayList<>();

	    List<Object[]> lapproveList = getImpPlanDAO().getToBeApprovedList(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name(), user, Boolean.TRUE);
	    for (Object[] obj : lapproveList) {
		String id = obj[0].toString();
		ImpPlan imp = getImpPlanDAO().find(id);
		approveList.add(imp);
	    }
	    List<Object[]> lmacroList = getImpPlanDAO().getPlansByMacroHeaderBasedOnTime(user, Constants.PlanStatus.APPROVED.name(), Boolean.TRUE, Boolean.TRUE);
	    for (Object[] obj : lmacroList) {
		String macroid = obj[0].toString();
		ImpPlan imp = getImpPlanDAO().find(macroid);
		macroList.add(imp);
	    }
	    User lUser = lDAPAuthenticatorImpl.getServiceUser();
	    if ((approveList != null && !approveList.isEmpty()) || (macroList != null && !macroList.isEmpty())) {
		pendingApprove.addAll(approveList);
		pendingApprove.addAll(macroList);
	    }

	    if (!pendingApprove.isEmpty()) {
		PendingActionNotificationMail approvalMail = (PendingActionNotificationMail) getMailMessageFactory().getTemplate(PendingActionNotificationMail.class);
		for (ImpPlan impPlan : pendingApprove) {
		    List<Implementation> impList = getImplementationDAO().findByImpPlan(impPlan);
		    for (Implementation imp : impList) {
			devAndleadApprovetask.add(imp.getDevId());
		    }
		    approvalMail.addToAddressUserId(user, Constants.MailSenderRole.DEV_MANAGER);
		    devAndleadApprovetask.stream().forEach(t -> approvalMail.addcCAddressUserId(t, Constants.MailSenderRole.LEAD));
		    devAndleadApprovetask.add(impPlan.getLeadId());
		    devManagerImpPlan.add(impPlan.getId());
		    impPlan.setApproveMailFlag(Boolean.TRUE);
		    impPlanDAO.update(lUser, impPlan);
		}
		approvalMail.setAction("Approved");
		approvalMail.setImplementation(devManagerImpPlan);
		getMailMessageFactory().push(approvalMail);
	    }
	}

    }

}
