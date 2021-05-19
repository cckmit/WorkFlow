/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tdx.executor.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.CheckoutActivityMessage;
import com.tsi.workflow.activity.SystemAddActivityMessage;
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
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.mail.CheckoutSourceContentMail;
import com.tsi.workflow.mail.NewTargetSystemMail;
import com.tsi.workflow.mail.UnsecuredCheckoutSourceContentMail;
import com.tsi.workflow.tdx.executor.models.TdxShellExecutorModel;
import com.tsi.workflow.utils.CheckoutUtils;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 *
 * @author ramkumar.seenivasan
 */
@Component
public class CheckOutExecutor {
    private static final Logger LOG = Logger.getLogger(CheckOutExecutor.class.getName());

    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    GITConfig gITConfig;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    ImplementationDAO impDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    SystemDAO systemDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;

    public Future<TdxShellExecutorModel> executeCheckout(TdxShellExecutorModel executorModel) throws IOException {

	JSONResponse lResponse = new JSONResponse();
	NewTargetSystemMail newTargetSystemMailNotification = executorModel.getNewTargetSystemMailNotification();
	try {
	    String lSystemName = executorModel.getlSystemEntry().getKey().toString();
	    List<CheckoutSegments> lSegmentsList = (List<CheckoutSegments>) executorModel.getlSystemEntry().getValue();
	    com.tsi.workflow.beans.dao.System lSystem = systemDAO.findByName(lSystemName);
	    String lCompanyName = lSystem.getPlatformId().getNickName().toLowerCase();
	    String lNickName = executorModel.getImpl().getPlanId().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
	    ImpPlan lPlan = executorModel.getImpl().getPlanId();
	    User lUser = executorModel.getUser();
	    Implementation lImplementation = executorModel.getImpl();

	    List<GitSearchResult> pSearchResults = executorModel.getpSearchResults();

	    Map<String, GitBranchSearchResult> lTempGitSearchSegments = executorModel.getlTempGitSearchSegments();
	    // Check for Defualt Put Level For each System
	    if (lSystem.getDefalutPutLevel() == null) {
		for (CheckoutSegments lSegment : lSegmentsList) {
		    lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).addAdditionalInfo("checkoutLog", "No Default Put Level assigned for the System " + lSystemName + ", Contact Tool Support");
		}
		LOG.error("No Default Put Level for the System " + lSystemName);
	    }

	    String lRepoName = lGitUtils.getPlanRepoName(lNickName, lPlan.getId());
	    // Creating workspace for each system
	    String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + lImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	    lResponse = sSHClientUtils.executeCommand(lUser, lSystem, lCommand);

	    if (!lResponse.getStatus()) {
		for (CheckoutSegments lSegment : lSegmentsList) {
		    lTempGitSearchSegments.get(CheckoutUtils.getIdString(lSegment)).addAdditionalInfo("checkoutLog", lResponse.getDisplayErrorMessage().trim());
		}
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setData(pSearchResults);
		executorModel.setJsonResponse(lResponse);
		return new AsyncResult<>(executorModel);
	    }

	    // For each segments on System LOOP
	    for (CheckoutSegments lSegment : lSegmentsList) {
		LOG.info("From ID of the Source File " + lSegment.getFileName() + " Checked out : " + lSegment.getId());
		CheckoutSegments lAlreadyCheckoutSegment = checkoutSegmentsDAO.findByFileName(lSegment.getFileName(), lImplementation.getId(), lSystem.getName(), lSegment.getFuncArea().toUpperCase());
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

		lResponse = sSHClientUtils.executeCommand(lUser, lSystem, lCommand);

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
		    SystemLoad lSystemLoad = systemLoadDAO.find(lPlan, lSystem);
		    if (lSystemLoad == null) {
			lSystemLoad = new SystemLoad();
			PutLevel lPutLevel = putLevelDAO.find(lSystem.getDefalutPutLevel());
			lSystemLoad.setPutLevelId(lPutLevel);
			lSystemLoad.setPlanId(lPlan);
			lSystemLoad.setSystemId(lSystem);
			lSystemLoad.setPreload("No");
			systemLoadDAO.save(lUser, lSystemLoad);
			activityLogDAO.save(lUser, new SystemAddActivityMessage(lPlan, lImplementation, lSystemLoad));
			newTargetSystemMailNotification.addTargetSystem(lSystemName);
		    }
		    lSegment.setSystemLoad(lSystemLoad);
		    lSegment.setPlanId(lPlan);
		    lSegment.setImpId(lImplementation);
		    lSegment.setReviewStatus(Boolean.FALSE);
		    if (lSegment.getId() == 0) {
			checkoutSegmentsDAO.save(lUser, lSegment);
		    } else {
			checkoutSegmentsDAO.update(lUser, lSegment);
		    }
		    activityLogDAO.save(lUser, new CheckoutActivityMessage(lPlan, lImplementation, lSegment));

		    // Self notification about Unsecured plans
		    List<Object[]> segmentDetails = impPlanDAO.getDevelopersBySegment(lSegment.getPlanId().getId(), Arrays.asList(Constants.PlanStatus.ACTIVE.name()), lSegment.getFileName(), lSegment.getTargetSystem());

		    for (Object[] segment : segmentDetails) {
			UnsecuredCheckoutSourceContentMail checkoutSourceContentMail = (UnsecuredCheckoutSourceContentMail) mailMessageFactory.getTemplate(UnsecuredCheckoutSourceContentMail.class);
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
			mailMessageFactory.push(checkoutSourceContentMail);
		    }
		    // Self notification about Secured plans
		    List<Object[]> securedSegmentDetails = impPlanDAO.getDevelopersBySegment(lSegment.getPlanId().getId(), new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()), lSegment.getFileName(), lSegment.getTargetSystem());

		    for (Object[] segment : securedSegmentDetails) {
			CheckoutSourceContentMail checkoutSourceContentMail = (CheckoutSourceContentMail) mailMessageFactory.getTemplate(CheckoutSourceContentMail.class);
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
				mailMessageFactory.push(checkoutSourceContentMail);
			    }
			} else {
			    mailMessageFactory.push(checkoutSourceContentMail);
			}

		    }

		    List<Object[]> planList = impPlanDAO.getAllDependentDevelopersBySegment(lSegment.getPlanId().getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), lSegment.getFileName(), lSegment.getTargetSystem());

		    for (Object[] segment : planList) {
			UnsecuredCheckoutSourceContentMail checkoutSourceContentMail = (UnsecuredCheckoutSourceContentMail) mailMessageFactory.getTemplate(UnsecuredCheckoutSourceContentMail.class);
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
			mailMessageFactory.push(checkoutSourceContentMail);
		    }

		}
	    }
	} catch (Exception ex) {
	    LOG.info("Exception on child", ex);
	    lResponse.setStatus(Boolean.FALSE);
	    executorModel.setJsonResponse(lResponse);
	}
	executorModel.setNewTargetSystemMailNotification(newTargetSystemMailNotification);
	return new AsyncResult<>(executorModel);
    }

}
