/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class ReviewerAssignmentMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    Implementation implementation;

    User userDetails;

    private String projectName;

    private Set<String> problemTicketNum;

    public Implementation getImplementation() {
	return implementation;
    }

    public void setImplementation(Implementation implementation) {
	this.implementation = implementation;
    }

    public User getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(User userDetails) {
	this.userDetails = userDetails;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public Set<String> getProblemTicketNum() {
	return problemTicketNum;
    }

    public void setProblemTicketNum(Set<String> problemTicketNum) {
	this.problemTicketNum = problemTicketNum;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	if (!implementation.getPeerReviewers().trim().isEmpty()) {
	    List<String> reviewerList = Arrays.asList(implementation.getPeerReviewers().split(","));
	    for (String reviewer : reviewerList) {
		this.addToAddressUserId(reviewer, Constants.MailSenderRole.PEER_REVIEWER);
	    }
	    this.addcCAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
	    this.setSubject("Review task Assignment " + implementation.getId());
	    if (userDetails.getCurrentDelegatedUser() == null) {
		message.append(userDetails.getDisplayName()).append("  has assigned you as Reviewer for the implementation - ").append(implementation.getId()).append(".").append("<br><br> Project/CSR  : ").append(getProjectName()).append(".");
		if (!getProblemTicketNum().isEmpty() && getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum()).append(".");
		}
		message.append("<br><br> Description of Change: ").append(implementation.getImpDesc()).append(".");
	    } else {
		message.append(userDetails.getDisplayName()).append("  has assigned you as Reviewer for the implementation - ").append(implementation.getId()).append(".").append("<br><br> Project/CSR  : ").append(getProjectName()).append(".");
		if (!getProblemTicketNum().toString().isEmpty() && getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket:  ").append(getProblemTicketNum()).append(".");
		}
		message.append("<br><br> Description of Change: ").append(implementation.getImpDesc()).append(" on behalf of ").append(userDetails.getCurrentDelegatedUser().getDisplayName()).append(".");
	    }
	    this.setMessage(message.toString());
	}
    }
}
