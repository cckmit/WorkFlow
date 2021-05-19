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
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class DevlLoadActivityMessage extends ActivityLogMessage {

    String status;
    String oldrType;
    boolean result;
    com.tsi.workflow.beans.dao.System system;
    String errorMessage;

    public String getStatus() {
	return status;
    }

    public void setOldrType(String oldrType) {
	this.oldrType = oldrType;
    }

    public void setResult(boolean result) {
	this.result = result;
    }

    public void setStatus(String lStatus) {
	this.status = lStatus;
    }

    public DevlLoadActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public DevlLoadActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    @Override
    public String processMessage() {
	String Message = "";
	if (oldrType != null) {
	    Message = "{0} {1}, DEVL (" + "{5}" + "-Type) loader file generation for the implementation plan {3}, system {4} has {2} ";
	} else {
	    Message = "{0} {1}, DEVL Loader file generation for the Implementation Plan {3}, System {4}  has {2}";
	}
	if (status.equals("failed")) {
	    Message = Message + ". Console Message - " + errorMessage;
	}
	if (user.getCurrentDelegatedUser() != null) {
	    Message = Message + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}
	if (oldrType != null) {
	    return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId(), system.getName(), oldrType);
	} else {
	    return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId(), system.getName());
	}
    }

    @Override
    public Priority getLogLevel() {
	if (status.equals("failed")) {
	    return Priority.ERROR;
	}
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
	system = (com.tsi.workflow.beans.dao.System) beans[0];
    }

}
