/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.User;
import com.tsi.workflow.activity.DeleteImplementationsActivityMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.mail.DeleteImplementationsMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public class ImplementationHelper {

    private static final Logger LOG = Logger.getLogger(ImplementationHelper.class.getName());

    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    JGitClientUtils lGitUtils;

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public PutLevelDAO getPutLevelDAO() {
	return putLevelDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public JSONResponse deleteImplementation(User pUser, String pId) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);

	try {
	    Implementation lImp = getImplementationDAO().find(pId);
	    List<CheckoutSegments> lSegments = getCheckoutSegmentsDAO().findByImplementation(lImp.getId());
	    if (lSegments != null && !lSegments.isEmpty()) {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("Unable to delete the Implementation - " + pId + " as it contains source artifacts");
		return lResponse;
	    }

	    DeleteImplementationsMail lDeleteImpMail = (DeleteImplementationsMail) getMailMessageFactory().getTemplate(DeleteImplementationsMail.class);

	    lDeleteImpMail.addToAddressUserId(lImp.getDevId(), Constants.MailSenderRole.DEVELOPER);
	    lDeleteImpMail.addToAddressUserId(lImp.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
	    String[] lReviewerList = lImp.getPeerReviewers().split(",");
	    for (String lReviewer : lReviewerList) {
		lDeleteImpMail.addToAddressUserId(lReviewer, Constants.MailSenderRole.PEER_REVIEWER);
	    }

	    // Send Mail
	    lDeleteImpMail.setlImp(lImp);
	    lDeleteImpMail.setlPlan(lImp.getPlanId());
	    lDeleteImpMail.setDeletedBy(pUser.getDisplayName());
	    getMailMessageFactory().push(lDeleteImpMail);

	    getImplementationDAO().delete(pUser, lImp);
	    deleteImplementationBranchesInPlan(lImp);
	    // Activity Log
	    DeleteImplementationsActivityMessage lDeleteActiLog = new DeleteImplementationsActivityMessage(lImp.getPlanId(), lImp);
	    lDeleteActiLog.setUser(pUser);
	    getActivityLogDAO().save(pUser, lDeleteActiLog);

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    throw new WorkflowException("Unable to Delete Implementation - " + pId, ex);
	}
	return lResponse;

    }

    public JSONResponse putLevelSegmentValidation(SystemLoad systemLoad, String impId) throws Exception {
	JSONResponse lResponse = new JSONResponse();

	PutLevel lProdPutLevel = getPutLevelDAO().getPutLevel(systemLoad.getSystemId(), Constants.PUTLevelOptions.PRODUCTION.name());
	List<CheckoutSegments> lSegmentList = getCheckoutSegmentsDAO().getSegmentsBySystemAndPut(impId, lProdPutLevel.getScmUrl(), systemLoad.getSystemId().getName());

	if (lSegmentList != null && !lSegmentList.isEmpty()) {
	    List<String> lSegmentName = new ArrayList();
	    lSegmentList.stream().forEach((segment) -> lSegmentName.add(segment.getFileName()));
	    lResponse.setStatus(Boolean.FALSE);
	    lResponse.setErrorMessage("Error: IBM Source Artificats - " + String.join(",", lSegmentName) + " of system " + systemLoad.getSystemId().getName() + " belonging to an older PUT level have not been removed from Implementation. Kindly do action to proceed further");
	    return lResponse;
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public Boolean deleteImplementationBranchesInPlan(Implementation imp) throws Exception {
	ImpPlan lPlan = imp.getPlanId();
	List<SystemLoad> systemLoadList = getSystemLoadDAO().findByImpPlan(lPlan.getId());
	String lCompanyName = lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();// platformDAO.findPlatformByPlanId(planId);
	Set<String> implementationBranchList = new HashSet<>();
	for (SystemLoad lSystemLoad : systemLoadList) {
	    implementationBranchList.add(imp.getId() + "_" + lSystemLoad.getSystemId().getName().toLowerCase());
	}
	getJGitClientUtils().deletePlansImpBranches(lPlan.getId(), imp.getId(), lCompanyName, implementationBranchList);
	return Boolean.TRUE;
    }
}
