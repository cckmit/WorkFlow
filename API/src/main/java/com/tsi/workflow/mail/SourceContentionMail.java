/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;

/**
 *
 * @author User
 */
public class SourceContentionMail extends MailMessage {

    private String dependentPlan;
    private String onlinePlan;
    private String leadId;

    public String getDependentPlan() {
	return dependentPlan;
    }

    public void setDependentPlan(String dependentPlan) {
	this.dependentPlan = dependentPlan;
    }

    public String getOnlinePlan() {
	return onlinePlan;
    }

    public void setOnlinePlan(String onlinePlan) {
	this.onlinePlan = onlinePlan;
    }

    public String getLeadId() {
	return leadId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Implementation Plan - {0} is marked as Online, Please do source contention of plan - {1}.", onlinePlan, dependentPlan);
	String subject = "Source contention required for plan - " + dependentPlan;
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }

}
