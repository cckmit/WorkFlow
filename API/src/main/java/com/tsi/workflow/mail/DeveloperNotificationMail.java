/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessage;
import java.text.MessageFormat;

/**
 *
 * @author Dinesh Ramanathan
 */
public class DeveloperNotificationMail extends MailMessage {

    private String ticketUrl;
    private String reviewer;
    private String implementationId;
    private User user;

    public String getTicketUrl() {
	return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
	this.ticketUrl = ticketUrl;
    }

    public String getReviewer() {
	return reviewer;
    }

    public void setReviewer(String reviewer) {
	this.reviewer = reviewer;
    }

    public String getImplementationId() {
	return implementationId;
    }

    public void setImplementationId(String implementationId) {
	this.implementationId = implementationId;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    @Override
    public void processMessage() {

	String subject = "Peer review completed for " + getImplementationId();
	String reviewer = "Reviewer " + getReviewer();
	if (user.getCurrentDelegatedUser() != null) {
	    reviewer = reviewer + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}

	String message = MessageFormat.format(" {0} has completed the peer review for {1} <br><br> <b>Ticket URL :</b><br> <a href = {2}> {2}</a>", reviewer, getImplementationId(), getTicketUrl());

	this.setSubject(subject);
	this.setMessage(message);
    }

}
