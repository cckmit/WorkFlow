/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.text.MessageFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PreProdLoadsetActivationMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    private String planId;
    private String loadsetName;
    private String preProdSystemName;
    private Date activationDateTime;
    private String action;

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getLoadsetName() {
	return loadsetName;
    }

    public void setLoadsetName(String loadsetName) {
	this.loadsetName = loadsetName;
    }

    public String getPreProdSystemName() {
	return preProdSystemName;
    }

    public void setPreProdSystemName(String preProdSystemName) {
	this.preProdSystemName = preProdSystemName;
    }

    public Date getActivationDateTime() {
	return activationDateTime;
    }

    public void setActivationDateTime(Date activationDateTime) {
	this.activationDateTime = activationDateTime;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Loadset - {0} of implementation plan {1} has been successfully {2} into the pre-production system - {3} at {4}.", loadsetName, planId, action, preProdSystemName, DateHelper.convertGMTtoEST(activationDateTime));
	String subject = "Implementation Plan - " + planId + " " + action + " to Pre-Production System - " + preProdSystemName;
	this.setMessage(message);
	this.setSubject(subject);
    }

}
