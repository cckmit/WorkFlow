/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.text.MessageFormat;

/**
 *
 * @author USER
 */
public class DelegateActivationMail extends MailMessage {

    Boolean activated = false;
    Boolean superuser = false;
    String assignee;
    String fromUser;

    public String getAssignee() {
	return assignee;
    }

    public Boolean getSuperuser() {
	return superuser;
    }

    public void setSuperuser(Boolean superuser) {
	this.superuser = superuser;
    }

    public void setAssignee(String assignee) {
	this.assignee = assignee;
    }

    public String getFromUser() {
	return fromUser;
    }

    public void setFromUser(String fromUser) {
	this.fromUser = fromUser;
    }

    public Boolean isActivated() {
	return activated;
    }

    public void setActivated(Boolean acivated) {
	this.activated = acivated;
    }

    @Override
    public void processMessage() {
	if (activated == null) {
	    String message = MessageFormat.format("User {0} has assigned you as delegate for {1}", assignee, fromUser);
	    this.setMessage(message);
	} else if (superuser) {
	    String message = MessageFormat.format("Super User {0} has got delegation access for {1}", assignee, fromUser);
	    this.setMessage(message);
	} else if (activated) {
	    String message = MessageFormat.format("Dev Manager {0} has delegated approval authority to {1}", fromUser, assignee);
	    this.setMessage(message);
	} else {
	    String message = MessageFormat.format("Dev Manager {0} has turned OFF approval authority for {1}", fromUser, assignee);
	    this.setMessage(message);
	}
	this.setSubject("Delegate Assignment/Activation");
    }

}
