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
 * @author gn.ebinezardharmaraj
 */
public class ReassignImplementationPlanMail extends MailMessage {

    private String impPlanId;
    private String newLead;
    private String oldLead;
    private String role;
    private String devManager;
    private String currentUser;

    public String getImpPlanId() {
	return impPlanId;
    }

    public void setImpPlanId(String impPlanId) {
	this.impPlanId = impPlanId;
    }

    public String getNewLead() {
	return newLead;
    }

    public void setNewLead(String newLead) {
	this.newLead = newLead;
    }

    public String getOldLead() {
	return oldLead;
    }

    public void setOldLead(String oldLead) {
	this.oldLead = oldLead;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public String getDevManager() {
	return devManager;
    }

    public void setDevManager(String devManager) {
	this.devManager = devManager;
    }

    public String getCurrentUser() {
	return currentUser;
    }

    public void setCurrentUser(String currentUser) {
	this.currentUser = currentUser;
    }

    @Override
    public void processMessage() {
	String message = "";
	String subject = "";
	if (role.equals(Constants.UserGroup.DevManager.name())) {
	    message = MessageFormat.format(" Implementation plan {0} Application Developer Lead has been reassigned from {1} to {2}  by   {3}  {4} ", impPlanId, oldLead, newLead, role, currentUser);
	} else if (role.equals(Constants.UserGroup.Lead.name())) {
	    message = MessageFormat.format(" Implementation plan {0} Application Developer Lead has been reassigned from {1} to {2}  by  {3}  {4} ", impPlanId, oldLead, newLead, role, currentUser);
	}

	subject = MessageFormat.format("{0} Application Developer Lead reassignment", impPlanId);

	this.setMessage(message);
	this.setSubject(subject);

    }
}
