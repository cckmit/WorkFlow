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
 * @author User
 */
public class FallBackRejectUnSecMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    public String fileName;
    public String fallbackPlan;
    public String dependentPlan;
    public String depPlanLeadId;

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getFallbackPlan() {
	return fallbackPlan;
    }

    public void setFallbackPlan(String fallbackPlan) {
	this.fallbackPlan = fallbackPlan;
    }

    public String getDependentPlan() {
	return dependentPlan;
    }

    public void setDependentPlan(String dependentPlan) {
	this.dependentPlan = dependentPlan;
    }

    public String getDepPlanLeadId() {
	return depPlanLeadId;
    }

    public void setDepPlanLeadId(String depPlanLeadId) {
	this.depPlanLeadId = depPlanLeadId;
    }

    @Override
    public void processMessage() {
	// T1800273 : Resolve source contention due to the FALLBACK of Plan <T1800306>
	// Source Artifacts <XXXX.c[PRE]> fallen back from implementation plan
	// <T1800306>. Kindly remove the <T1800306>
	// changes from plan T1800273, if any
	String subject = dependentPlan + " : Resolve source contention due to FALLBACK of plan " + fallbackPlan;

	String message = MessageFormat.format("Source Artifacts {0} fallen back from implementation plan {1}. Kindly remove the {1} changes from plan {2}, if any", fileName, fallbackPlan, dependentPlan);
	this.addToAddressUserId(depPlanLeadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }

}
