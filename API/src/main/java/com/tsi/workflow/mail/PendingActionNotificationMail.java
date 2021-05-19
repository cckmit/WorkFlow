/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.MailMessage;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author vamsi.krishnarao
 */

public class PendingActionNotificationMail extends MailMessage {

    List<String> implementation;
    String action;
    String ticketUrl;

    @Autowired
    WFConfig wFConfig;

    public WFConfig getwFConfig() {
	return wFConfig;
    }

    public String getTicketUrl() {
	return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
	this.ticketUrl = ticketUrl;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public List<String> getImplementation() {
	return implementation;
    }

    public void setImplementation(List<String> implementation) {
	this.implementation = implementation;
    }

    @Override
    public void processMessage() {
	String requestURL;
	if (getwFConfig().getIsDeltaApp()) {
	    requestURL = getwFConfig().getSsoAPIUrl();
	} else {
	    requestURL = getwFConfig().getApiUrl();
	}
	if (action.equals("Approved")) {
	    ticketUrl = requestURL.replace("WorkFlowAPI", "WorkFlow/#/app/manageTasks");
	} else if (action.equals("Peer Reviewed")) {
	    ticketUrl = requestURL.replace("WorkFlowAPI", "WorkFlow/#/app/reviewTasks");
	}

	StringBuilder message = new StringBuilder();
	String subject = "";

	if (action.equals("Approved")) {
	    subject = "Action(s) Required: Implementation Plan(s) need to be " + action;
	    message.append("The following implementation plan(s) are waiting to be ").append(action).append("<br><br>");
	} else if (action.equals("Peer Reviewed")) {
	    subject = "Action(s) Required: Implementation (s) need to be " + action;
	    message.append("The following implementation (s) are waiting to be ").append(action).append("<br><br>");
	}
	for (String imp : implementation) {
	    message.append("<a href = ").append(ticketUrl).append(">").append(imp).append("</a>").append("<br>");
	}

	this.setMessage(message.toString());
	this.setSubject(subject);

    }

}
