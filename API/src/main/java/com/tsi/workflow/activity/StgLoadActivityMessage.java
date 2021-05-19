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
public class StgLoadActivityMessage extends ActivityLogMessage {

    String status;
    String oldrType;
    boolean result;
    com.tsi.workflow.beans.dao.System system;

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

    public StgLoadActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public StgLoadActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    @Override
    public String processMessage() {
	String Message = "";
	Message = "{0} {1} has {2} the Staging Loader file creation for the Implementation Plan {3}, System {4}";
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
