/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.utils.Constants;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class RejectionActivityMessage extends ActivityLogMessage {

    String comments;
    Boolean tsdActivity = Boolean.FALSE;

    public String getComments() {
	return comments;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public boolean isTsdActivity() {
	return tsdActivity;
    }

    public void setTsdActivity(boolean tsdActivity) {
	this.tsdActivity = tsdActivity;
    }

    public RejectionActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    @Override
    public String processMessage() {
	String lLogMessage = "";
	if (comments != null && comments.contains(Constants.AUTOREJECT_COMMENT.LOAD_DATE_CHANGE.getValue())) {
	    lLogMessage = lLogMessage + user.getDisplayName() + " has rejected the implementation plan - " + impPlan.getId();
	    lLogMessage = lLogMessage + ", due to  \" " + comments + "\" ";
	} else {
	    if (user.getCurrentRole() == null) {
		user.setCurrentRole("");
	    }
	    lLogMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has rejected the implementation plan - " + impPlan.getId();
	    if (comments != null) {
		lLogMessage = lLogMessage + " with comment - " + comments;
	    } else {
		lLogMessage = lLogMessage + " without comment ";
	    }
	}

	if (user.getCurrentDelegatedUser() != null) {
	    lLogMessage = lLogMessage + ", on behalf of " + user.getCurrentDelegatedUser().getCurrentRole() + " " + user.getCurrentDelegatedUser().getDisplayName();
	}
	return lLogMessage;
    }

    @Override
    public Priority getLogLevel() {
	return Priority.ERROR;
    }

}
