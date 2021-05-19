/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;

/**
 *
 * @author Vinoth.ponnurangam
 */
public class QALoadsetActivationMail extends MailMessage {

    private String planId;
    private String vparsName;
    private String action;
    private String loadSetName;
    User userDetails;
    Boolean tssdeploy;
    private String env;

    public Boolean getTssdeploy() {
	return tssdeploy;
    }

    public void setTssdeploy(Boolean tssdeploy) {
	this.tssdeploy = tssdeploy;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getVparsName() {
	return vparsName;
    }

    public void setVparsName(String vparsName) {
	this.vparsName = vparsName;
    }

    public String getLoadSetName() {
	return loadSetName;
    }

    public void setLoadSetName(String loadSetName) {
	this.loadSetName = loadSetName;
    }

    public User getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(User userDetails) {
	this.userDetails = userDetails;
    }

    public String getEnv() {
	return env;
    }

    public void setEnv(String env) {
	this.env = env;
    }

    @Override
    public void processMessage() {
	String load = " from ";

	if (getAction().equals(Constants.LOAD_SET_STATUS.LOADED.name()) || getAction().equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
	    load = " to ";
	}
	load = load + " QA " + getEnv() + " lab";

	StringBuilder message = new StringBuilder();
	if (userDetails.getCurrentDelegatedUser() == null) {
	    if (!tssdeploy) {
		message.append(userDetails.getDisplayName()).append(" has ").append(getAction()).append(" loadset  ").append(getLoadSetName()).append(" of implementation plan ").append(getPlanId()).append(load).append(getVparsName());
	    } else {
		message.append(userDetails.getDisplayName()).append(" has ").append(" INITIATED ").append(" loadset  ").append(getLoadSetName()).append(" of implementation plan ").append(getPlanId()).append(load).append(getVparsName());
	    }

	    StringBuilder subject = new StringBuilder();
	    if (!tssdeploy) {
		subject.append("Implementation Plan - ").append(planId).append(" ").append(action).append(load).append(getVparsName());
	    } else {
		subject.append("Implementation Plan - ").append(planId).append(" ").append(" INITIATED ").append(load).append(getVparsName());
	    }
	    this.setMessage(message.toString());
	    this.setSubject(subject.toString());
	}

    }
}
