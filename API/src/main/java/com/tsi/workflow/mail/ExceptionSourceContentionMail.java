/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ExceptionSourceContentionMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

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

	String message = MessageFormat.format("Exception Plan {0} was marked Online. Please sync up your {1} source code with the latest in {0}.", onlinePlan, dependentPlan);
	String subject = dependentPlan + " : Resolve source contention with the Exception Load " + onlinePlan;
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }
}
