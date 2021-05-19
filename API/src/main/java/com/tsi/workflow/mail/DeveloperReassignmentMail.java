/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class DeveloperReassignmentMail extends MailMessage {

    private String oldDeveloperName;
    private String newDeveloperName;
    private Implementation implementation;
    private User userDetails;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    public String getOldDeveloperName() {
	return oldDeveloperName;
    }

    public void setOldDeveloperName(String oldDeveloperName) {
	this.oldDeveloperName = oldDeveloperName;
    }

    public String getNewDeveloperName() {
	return newDeveloperName;
    }

    public void setNewDeveloperName(String newDeveloperName) {
	this.newDeveloperName = newDeveloperName;
    }

    public Implementation getImplementation() {
	return implementation;
    }

    public void setImplementation(Implementation implementation) {
	this.implementation = implementation;
    }

    public User getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(User userDetails) {
	this.userDetails = userDetails;
    }

    @Override
    public void processMessage() {
	if (oldDeveloperName != null) {
	    this.addToAddressUserId(oldDeveloperName, Constants.MailSenderRole.DEVELOPER);
	    this.addToAddressUserId(newDeveloperName, Constants.MailSenderRole.DEVELOPER);
	    this.setSubject("Task Reassignment - " + implementation.getId());
	    String message = MessageFormat.format("{0} has reassigned Implementation {1} ( {2} ) to developer - {3}", userDetails.getDisplayName(), implementation.getId(), implementation.getImpDesc(), authenticator.getUserDetails(newDeveloperName).getDisplayName());
	    this.setMessage(message);
	} else {
	    this.addToAddressUserId(newDeveloperName, Constants.MailSenderRole.DEVELOPER);
	    this.setSubject("Task Assignment - " + implementation.getId());
	    String message = MessageFormat.format("{0} has assigned Implementation {1} ( {2} ) to developer - {3}", userDetails.getDisplayName(), implementation.getId(), implementation.getImpDesc(), authenticator.getUserDetails(newDeveloperName).getDisplayName());
	    this.setMessage(message);
	}
    }
}
