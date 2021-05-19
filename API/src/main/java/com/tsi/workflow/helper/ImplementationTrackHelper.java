package com.tsi.workflow.helper;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.tracker.ImplementationTrackerData;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImplementationTrackHelper {

    private static final Logger LOG = Logger.getLogger(ImplementationTrackHelper.class.getName());

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    @Autowired
    CommonHelper commonHelper;

    public CommonHelper getCommonHelper() {
	return commonHelper;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public List<ImplementationTrackerData> getImplementationStatus(ImpPlan lPlan) {
	List<ImplementationTrackerData> impTrackData = new ArrayList<>();

	lPlan.getImplementationList().stream().filter(x -> x.getActive().equals("Y")).forEach(imp -> {
	    ImplementationTrackerData lImplementationTrackerData = new ImplementationTrackerData();
	    lImplementationTrackerData.setImplementationId(imp.getId());
	    lImplementationTrackerData.setDeveloperName(imp.getDevName());
	    setCurrentStageStatus(lImplementationTrackerData, imp);
	    impTrackData.add(lImplementationTrackerData);
	});
	return impTrackData.stream().sorted(Comparator.comparing(ImplementationTrackerData::getImplementationId)).collect(Collectors.toList());
    }

    private void setCurrentStageStatus(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	if (imp.getIsCheckedin() == null || !imp.getIsCheckedin()) {
	    lImplementationTrackerData.setCurrentStage(Constants.ImpTrackStatus.getImpTrackStatusMap().get(Constants.ImpTrackStatus.CODE_PREPARATION.name()));
	    setCodePrepMessage(lImplementationTrackerData, imp);
	} else if (imp.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name()) && (imp.getSubstatus() == null || imp.getSubstatus().isEmpty())) {
	    lImplementationTrackerData.setCurrentStage(Constants.ImpTrackStatus.getImpTrackStatusMap().get(Constants.ImpTrackStatus.UNIT_TESTING.name()));
	    setUnitTestMessage(lImplementationTrackerData, imp);
	} else if (!isPeerReviewCompleted(imp)) {
	    lImplementationTrackerData.setCurrentStage(Constants.ImpTrackStatus.getImpTrackStatusMap().get(Constants.ImpTrackStatus.PEER_REVIEW.name()));
	    setPeerReviewMessage(lImplementationTrackerData, imp);
	} else if (!isIntegrationCompleted(imp, lImplementationTrackerData)) {
	    lImplementationTrackerData.setCurrentStage(Constants.ImpTrackStatus.getImpTrackStatusMap().get(Constants.ImpTrackStatus.INTEGRATION_TESTING.name()));
	} else if (!isReadyForQACompleted(imp)) {
	    lImplementationTrackerData.setCurrentStage(Constants.ImpTrackStatus.getImpTrackStatusMap().get(Constants.ImpTrackStatus.READY_FOR_QA.name()));
	    boolean messageAdded = false;
	    if (!imp.getPlanId().getMacroHeader()) {
		if (!getCommonHelper().isDevBuildCompleted(imp.getPlanId())) {
		    setDevOrLoadsetMessage(imp, lImplementationTrackerData, true);
		    messageAdded = true;
		} else if (!getCommonHelper().isDevLoadSetCompleted(imp.getPlanId())) {
		    setDevOrLoadsetMessage(imp, lImplementationTrackerData, false);
		    messageAdded = true;
		}
	    }
	    if (!messageAdded) {
		setReadyForQAMessage(lImplementationTrackerData, imp);
	    }
	} else if (imp.getImpStatus().equals(Constants.ImplementationStatus.READY_FOR_QA.name())) {
	    lImplementationTrackerData.setCurrentStage(Constants.ImpTrackStatus.getImpTrackStatusMap().get(Constants.ImpTrackStatus.IMPL_COMPLETED.name()));
	    setImplCompletedMessage(lImplementationTrackerData, imp);
	}
    }

    private boolean isPeerReviewCompleted(Implementation imp) {
	boolean isPeerReviewDone = true;
	if (imp.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name()) && imp.getSubstatus().equals(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name()) && !imp.getBypassPeerReview()) {
	    isPeerReviewDone = false;
	    if (imp.getReviewersDone() != null && !imp.getReviewersDone().isEmpty() && imp.getPeerReviewers().equalsIgnoreCase(imp.getReviewersDone().trim())) {
		isPeerReviewDone = true;
	    }
	}
	return isPeerReviewDone;
    }

    private boolean isIntegrationCompleted(Implementation imp, ImplementationTrackerData lImplementationTrackerData) {
	boolean isIntegrationCompleted = true;
	if (isPeerReviewCompleted(imp) && imp.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name()) && (imp.getSubstatus().equals(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name()) || imp.getSubstatus().equals(Constants.ImplementationSubStatus.BYPASSED_PEER_REVIEW.name()))) {
	    if (!imp.getPlanId().getMacroHeader() && !getCommonHelper().isDevBuildCompleted(imp.getPlanId())) {
		isIntegrationCompleted = false;
		setDevOrLoadsetMessage(imp, lImplementationTrackerData, true);
	    } else if (!imp.getPlanId().getMacroHeader() && !getCommonHelper().isDevLoadSetCompleted(imp.getPlanId())) {
		isIntegrationCompleted = false;
		setDevOrLoadsetMessage(imp, lImplementationTrackerData, false);
	    } else {
		isIntegrationCompleted = false;
		if (lImplementationTrackerData != null) {
		    setIntegrationTestMessage(lImplementationTrackerData, imp);
		}
	    }
	}
	return isIntegrationCompleted;
    }

    private boolean isReadyForQACompleted(Implementation imp) {
	boolean isReadyForQA = true;
	if (isIntegrationCompleted(imp, null) && imp.getImpStatus().equals(Constants.ImplementationStatus.IN_PROGRESS.name()) && imp.getSubstatus().equals(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name())) {
	    isReadyForQA = false;
	}
	return isReadyForQA;
    }

    private void setCodePrepMessage(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	StringBuilder sb = new StringBuilder();
	sb.append("Developer ").append(imp.getDevName());
	sb.append(" has to update the source artifacts and perform Commit/Checkin on Implementation");
	sb.append(imp.getId());
	lImplementationTrackerData.getMessages().add(sb.toString());
    }

    private void setUnitTestMessage(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	StringBuilder sb = new StringBuilder();
	sb.append("Developer ").append(imp.getDevName());
	sb.append(" has to mark the implementation ");
	sb.append(imp.getId()).append(" as Unit Testing Completed");
	lImplementationTrackerData.getMessages().add(sb.toString());
    }

    private void setPeerReviewMessage(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	StringBuilder sb = new StringBuilder();

	if ((imp.getTktUrl() == null || imp.getTktUrl().isEmpty()) && (imp.getTktNum() == null || imp.getTktNum().isEmpty())) {
	    sb.append("Developer ").append(imp.getDevName());
	    sb.append(" has to send request for peer review");
	} else {
	    String reviewerToComplete = "";
	    if (imp.getReviewersDone() == null || imp.getReviewersDone().trim().isEmpty()) {
		reviewerToComplete = imp.getPeerReviewersName();
	    } else {
		List<String> reviewerDoneList = Arrays.asList(imp.getReviewersDone().split(","));
		List<String> pendingReviewers = new ArrayList<>();
		Arrays.asList(imp.getPeerReviewers().split(",")).stream().filter(reviewer -> !reviewerDoneList.contains(reviewer)).forEach(reviewer -> {
		    User lReviewer = getLDAPAuthenticatorImpl().getUserDetails(reviewer);
		    if (lReviewer != null && lReviewer.getDisplayName() != null) {
			pendingReviewers.add(lReviewer.getDisplayName());
		    }
		});

		reviewerToComplete = pendingReviewers.stream().collect(Collectors.joining(","));
	    }
	    sb.append("Reviewer(s) ").append(reviewerToComplete);
	    sb.append(" have to mark the implementation ");
	    sb.append(imp.getId()).append(" as Peer Review Completed");
	}
	lImplementationTrackerData.getMessages().add(sb.toString());
    }

    private void setIntegrationTestMessage(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	StringBuilder sb = new StringBuilder();
	sb.append("Developer ").append(imp.getDevName());
	sb.append(" has to mark the implementation ");
	sb.append(imp.getId()).append(" as Integration Testing Completed");
	lImplementationTrackerData.getMessages().add(sb.toString());
    }

    private void setReadyForQAMessage(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	StringBuilder sb = new StringBuilder();
	sb.append("Developer ").append(imp.getDevName());
	sb.append(" has to mark the implementation ");
	sb.append(imp.getId()).append(" as Ready for QA");
	lImplementationTrackerData.getMessages().add(sb.toString());
    }

    private void setImplCompletedMessage(ImplementationTrackerData lImplementationTrackerData, Implementation imp) {
	StringBuilder sb = new StringBuilder();
	sb.append("Developer ").append(imp.getDevName());
	sb.append(" has marked the implementation ");
	sb.append(imp.getId()).append(" as Ready for QA.");
	lImplementationTrackerData.getMessages().add(sb.toString());
    }

    private void setDevOrLoadsetMessage(Implementation imp, ImplementationTrackerData lImplementationTrackerData, boolean isDevBuildMsg) {
	StringBuilder sb = new StringBuilder();
	if (isDevBuildMsg) {
	    sb.append("Developer ").append(imp.getDevName());
	    sb.append(" or Lead ").append(imp.getPlanId().getLeadName());
	    sb.append(" have to perform a DEVL build for implementation plan");
	    sb.append(imp.getPlanId().getId());
	    if (lImplementationTrackerData != null) {
		lImplementationTrackerData.getMessages().add(sb.toString());
	    }
	} else {
	    sb.append("Developer ").append(imp.getDevName());
	    sb.append(" or Lead ").append(imp.getPlanId().getLeadName());
	    sb.append(" have to generate a DEVL loadset for implementation plan");
	    sb.append(imp.getPlanId().getId());
	    lImplementationTrackerData.getMessages().add(sb.toString());
	}
    }

}
