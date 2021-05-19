/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class ReviewerReassignmentMail extends MailMessage {

    private static final Logger LOG = Logger.getLogger(ReviewerReassignmentMail.class.getName());
    boolean removed;
    boolean newRequest;
    boolean reAssigned;
    String implementationId;
    String developerId;
    User lUser;
    List<String> reviewersId = new ArrayList<>();
    List<String> addedreviewersName = new ArrayList<>();
    List<String> removedreviewersName = new ArrayList<>();
    private Set<String> problemTicketNum;
    private String description;
    private String projectName;

    public User getUser() {
	return lUser;
    }

    public void setUser(User lUser) {
	this.lUser = lUser;
    }

    public String getDeveloperId() {
	return developerId;
    }

    public void setDeveloperId(String developerId) {
	this.developerId = developerId;
    }

    public boolean isReAssigned() {
	return reAssigned;
    }

    public void setReAssigned(boolean reAssigned) {
	this.reAssigned = reAssigned;
    }

    public boolean isNewRequest() {
	return newRequest;
    }

    public void setNewRequest(boolean newRequest) {
	this.newRequest = newRequest;
    }

    public List<String> getReviewersId() {
	return reviewersId;
    }

    public void setReviewersId(List<String> reviewersId) {
	this.reviewersId = reviewersId;
    }

    public List<String> getAddedreviewersName() {
	return addedreviewersName;
    }

    public void setAddedreviewersName(List<String> addedreviewersName) {
	this.addedreviewersName = addedreviewersName;
    }

    public List<String> getRemovedreviewersName() {
	return removedreviewersName;
    }

    public void setRemovedreviewersName(List<String> removedreviewersName) {
	this.removedreviewersName = removedreviewersName;
    }

    public String getImplementationId() {
	return implementationId;
    }

    public void setImplementationId(String implementationId) {
	this.implementationId = implementationId;
    }

    public boolean isRemoved() {
	return removed;
    }

    public void setRemoved(boolean removed) {
	this.removed = removed;
    }

    public Set<String> getProblemTicketNum() {
	return problemTicketNum;
    }

    public void setProblemTicketNum(Set<String> problemTicketNum) {
	this.problemTicketNum = problemTicketNum;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	reviewersId.stream().forEach(c -> this.addToAddressUserId(c, Constants.MailSenderRole.PEER_REVIEWER));
	if (removed) {
	    this.setSubject("Review task Removed - " + getImplementationId());
	    this.addcCAddressUserId(developerId, Constants.MailSenderRole.DEVELOPER);
	    if (lUser.getCurrentDelegatedUser() == null) {
		message.append(lUser.getDisplayName()).append(" has removed ").append(String.join(",", getRemovedreviewersName())).append(" as Reviewer for ").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
		if (getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
		}
		message.append("<br><br> Description of Change:  ").append(getDescription());
	    } else {
		message.append(lUser.getDisplayName()).append("  has removed ").append(String.join(",", getRemovedreviewersName())).append("  as Reviewer for ").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
		if (getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
		}
		message.append("<br><br> Description of Change:  ").append(getDescription()).append(" on behalf of").append(lUser.getCurrentDelegatedUser().getDisplayName());
	    }
	} else if (reAssigned) {
	    this.setSubject("Review task ReAssigned - " + getImplementationId());
	    if (lUser.getCurrentDelegatedUser() == null) {
		message.append(lUser.getDisplayName()).append("  has re-assigned from ").append(String.join(",", getRemovedreviewersName())).append(" to ").append(String.join(",", getAddedreviewersName())).append(" as Reviewer for ").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
		if (getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
		}
		message.append("<br><br> Description of Change:  ").append(getDescription());

	    } else {
		message.append(lUser.getDisplayName()).append(" has re-assigned from ").append(String.join(",", getRemovedreviewersName())).append(" to ").append(String.join(",", getAddedreviewersName())).append(" as Reviewer for ").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
		if (getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
		}
		message.append("<br><br> Description of Change:  ").append(getDescription()).append(" on behalf of").append(lUser.getCurrentDelegatedUser().getDisplayName());
	    }
	    if (newRequest) {
		this.addToAddressUserId(developerId, Constants.MailSenderRole.DEVELOPER);
		message = message.append(", Please request for peer review again.");
	    } else {
		this.addcCAddressUserId(developerId, Constants.MailSenderRole.DEVELOPER);
	    }
	} else {
	    this.setSubject("Review task Added - " + getImplementationId());
	    if (lUser.getCurrentDelegatedUser() == null) {
		message.append(lUser.getDisplayName()).append(" has added  ").append(String.join(",", getAddedreviewersName())).append(" as Reviewer for ").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
		if (getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
		}
		message.append("<br><br> Description of Change:  ").append(getDescription());
	    } else {
		message.append(lUser.getDisplayName()).append(" has added  ").append(String.join(",", getAddedreviewersName())).append(" as Reviewer for ").append(implementationId).append("<br><br> Project/CSR  : ").append(getProjectName());
		if (getProblemTicketNum() != null && getProblemTicketNum().size() > 0) {
		    message.append("<br><br> Problem Ticket: ").append(getProblemTicketNum());
		}
		message.append("<br><br> Description of Change:  ").append(getDescription()).append(" on behalf of").append(lUser.getCurrentDelegatedUser().getDisplayName());
	    }
	    if (newRequest) {
		this.addToAddressUserId(developerId, Constants.MailSenderRole.DEVELOPER);
		message = message.append(", Please request for peer review again.");
	    } else {
		this.addcCAddressUserId(developerId, Constants.MailSenderRole.DEVELOPER);
	    }
	}
	this.setMessage(message.toString());
    }
}
