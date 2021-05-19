/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class ImplementationStatusCompletionMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    Implementation implementation;

    ImpPlan impPlan;

    String status;

    public ImpPlan getImpPlan() {
	return impPlan;
    }

    public void setImpPlan(ImpPlan impPlan) {
	this.impPlan = impPlan;
    }

    public Implementation getImplementation() {
	return implementation;
    }

    public void setImplementation(Implementation implementation) {
	this.implementation = implementation;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public void processMessage() {
	String lMailSubject = "Implementation " + implementation.getId() + " - " + status + " Completed ";
	this.setSubject(lMailSubject);
	this.addToAddressUserId(impPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	this.addcCAddressUserId(implementation.getDevId(), Constants.MailSenderRole.DEVELOPER);
	String lMailMessage = status + " has been completed for implementation - " + implementation.getId() + " implementation plan - " + impPlan.getId();
	this.setMessage(lMailMessage);
    }
}
