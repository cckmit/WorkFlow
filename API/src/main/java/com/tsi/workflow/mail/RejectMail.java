/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class RejectMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    private String planId;
    private String leadId;
    private User currentUser;
    private String comment;
    private boolean buildFailure = false;
    private boolean deleteLoadSetFlag = true;
    private String planDescription;

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public boolean isBuildFailure() {
	return buildFailure;
    }

    public void setBuildFailure(boolean buildFailure) {
	this.buildFailure = buildFailure;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public User getCurrentUser() {
	return currentUser;
    }

    public void setCurrentUser(User currentUser) {
	this.currentUser = currentUser;
    }

    public String getLeadId() {
	return leadId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    public boolean isDeleteLoadSetFlag() {
	return deleteLoadSetFlag;
    }

    public void setDeleteLoadSetFlag(boolean deleteLoadSetFlag) {
	this.deleteLoadSetFlag = deleteLoadSetFlag;
    }

    public String getPlanDescription() {
	return planDescription;
    }

    public void setPlanDescription(String planDescription) {
	this.planDescription = planDescription;
    }

    @Override
    public void processMessage() {
	String message = null;
	String loadSetMessage = null;
	if (isDeleteLoadSetFlag()) {
	    loadSetMessage = "Implementation Plan loadset has been deleted from all test systems where it was deployed";

	} else {
	    loadSetMessage = "Implementation Plan loadset will remain in all test systems where it was deployed until next restore/refresh";
	}
	if (!isBuildFailure()) {
	    if (comment != null && comment.length() > 0) {
		message = MessageFormat.format("{1} has rejected the implementation plan - {0} with comment {2} <br><br>{3}. <br>Description of Implementation Plan : {4}", planId, currentUser.getDisplayName(), comment, loadSetMessage, planDescription);
	    } else {
		message = MessageFormat.format("{1} has rejected the implementation plan - {0}. <br><br>{2}.<br>Description of Implementation Plan : {3}", planId, currentUser.getDisplayName(), loadSetMessage, planDescription);
	    }
	} else {
	    message = MessageFormat.format("The Implementation plan {0} was rejected as staging build for target system failed", planId);
	}
	String subject = "Rejection of Implementation Plan";
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
