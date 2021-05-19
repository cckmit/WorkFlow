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
public class StagingBuildFailureMail extends MailMessage {

    private String planId;
    private String leadId;
    private String system;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getLeadId() {
	return leadId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    public String getSystem() {
	return system;
    }

    public void setSystem(String system) {
	this.system = system;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Staging Build/Loadset/Fallback loadset generation for plan - {0} is unsuccessful during plan SUBMISSION. Hence plan has been reverted to ACTIVE status", planId);
	String subject = "Staging Build / Loadset /Fallback loadset generation failed - " + planId + "/" + system;
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
