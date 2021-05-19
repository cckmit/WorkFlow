/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;
import java.util.SortedSet;
import java.util.TreeSet;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class NewTargetSystemMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;
    private User userDetails;
    private SortedSet<String> targetSystem = new TreeSet<>();
    private String leadId;
    private String implementationId;

    public User getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(User userDetails) {
	this.userDetails = userDetails;
    }

    public LDAPAuthenticatorImpl getAuthenticator() {
	return authenticator;
    }

    public void setAuthenticator(LDAPAuthenticatorImpl authenticator) {
	this.authenticator = authenticator;
    }

    public SortedSet<String> getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(SortedSet<String> targetSystem) {
	this.targetSystem = targetSystem;
    }

    public void addTargetSystem(String targetSystem) {
	this.targetSystem.add(targetSystem);
    }

    public String getLeadId() {
	return leadId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    public String getImplementationId() {
	return implementationId;
    }

    public void setImplementationId(String implementationId) {
	this.implementationId = implementationId;
    }

    @Override
    public void processMessage() {
	String lMailSubject = "Checkout from new target system for implementation " + implementationId;
	String lMailMessage = MessageFormat.format("Developer {0} has checked out code from new target system(s) {1} for implementation {2}. Please provide load information for newly added target systems within the implementation plan for the build to be successful.", userDetails.getDisplayName(), String.join(",", targetSystem), implementationId);
	this.setSubject(lMailSubject);
	this.addToAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(lMailMessage);

    }
}
