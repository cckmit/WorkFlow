/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.DateHelper;
import java.text.MessageFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author USER
 */
public class DevManagerAssignmentMail extends MailMessage {

    @Autowired
    WFConfig wFConfig;

    public WFConfig getwFConfig() {
	return wFConfig;
    }

    public void setwFConfig(WFConfig wFConfig) {
	this.wFConfig = wFConfig;
    }

    String leadName;
    String devManagerName;
    String planId;
    boolean assignment;
    private Date loadDateTime;
    boolean devManagerChange;
    String oldDevManager;
    String newDevManager;

    public void setOldDevManager(String oldDevManager) {
	this.oldDevManager = oldDevManager;
    }

    public void setNewDevManager(String newDevManager) {
	this.newDevManager = newDevManager;
    }

    public void setDevManagerChange(boolean devManagerChange) {
	this.devManagerChange = devManagerChange;
    }

    public String getPlanStatus() {
	return planStatus;
    }

    public void setPlanStatus(String planStatus) {
	this.planStatus = planStatus;
    }

    private String planStatus;

    public void setAssignment(boolean assignment) {
	this.assignment = assignment;
    }

    public void setLeadName(String leadName) {
	this.leadName = leadName;
    }

    public void setDevManagerName(String devManagerName) {
	this.devManagerName = devManagerName;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public Date getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(Date loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    @Override
    public void processMessage() {
	String applicationURL = null;
	if (getwFConfig().getIsDeltaApp()) {
	    applicationURL = getwFConfig().getSsoAPIUrl();
	} else {
	    applicationURL = getwFConfig().getApiUrl();
	}
	String requestURL = applicationURL.replace("WorkFlowAPI", "WorkFlow/#/app/redirectDevManger");

	if (assignment) {
	    String lMessage = MessageFormat.format("Lead {0} created the Implementation Plan <a href = {3}> {1}</a>, and assigned {2} as Dev Manager.", leadName, planId, devManagerName, requestURL);
	    this.setMessage(lMessage);
	    this.setSubject("Creation/Assignment of new Implementation Plan - " + planId);
	} else if (devManagerChange) {
	    String lMessage = MessageFormat.format("Lead {0} has changed the dev Manager from {1} to {2} for plan {3}.", leadName, oldDevManager, newDevManager, planId);
	    this.setMessage(lMessage);
	    this.setSubject(" Dev Manager changed from " + oldDevManager + " to " + newDevManager + " for plan " + planId);
	} else {
	    String lMessage;
	    if (planStatus != null && planStatus.equals(Constants.PlanStatus.APPROVED.name())) {
		lMessage = MessageFormat.format("Implementation Plan <a href = {1}> {0}</a> has Submitted for approval. Please provide your approval as soon as Load date/Time {2} reached.", planId, requestURL, DateHelper.convertGMTtoEST(getLoadDateTime()));
	    } else {
		lMessage = MessageFormat.format("Implementation Plan <a href = {1}> {0}</a> has Passed Acceptance Testing. Please approve the Plan.", planId, requestURL);
	    }
	    this.setMessage(lMessage);
	    this.setSubject("Request for Dev Manager approval");
	}
    }
}
