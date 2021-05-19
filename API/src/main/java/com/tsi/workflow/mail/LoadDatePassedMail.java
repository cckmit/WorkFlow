/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;

/**
 *
 * @author User
 */
public class LoadDatePassedMail extends MailMessage {

    private String planId;
    private String leadId;
    private User currentUser;

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

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Load Date passed more than two days for implementation plan - {0}.", planId);
	String subject = "Load Date Passed for Implementation Plan " + planId;
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
