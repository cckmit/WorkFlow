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
public class AutoRejectMail extends MailMessage {

    private String planId;
    private String leadId;
    private String dependentPlanId;
    private String status;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getDependentPlanId() {
	return dependentPlanId;
    }

    public void setDependentPlanId(String dependentPlanId) {
	this.dependentPlanId = dependentPlanId;
    }

    public String getLeadId() {
	return leadId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public void processMessage() {
	String message = "";
	if (status.contains(Constants.REJECT_REASON.LOAD_DATE_CHANGE.getValue())) {
	    message = MessageFormat.format("Implementation Plan - {0}  is auto-rejected due to {1}. ", dependentPlanId, status);
	}
	// ZTPFM - 1319 Code changes to change content format
	else {
	    message = MessageFormat.format("Implementation Plan - {0}  is auto-rejected due to Implementation plan - {1}.", dependentPlanId, planId);
	}
	message = message + " Please update your source artifacts where applicable. ";
	String subject = dependentPlanId + " : " + " Auto Rejected due to the Plan " + planId;
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
