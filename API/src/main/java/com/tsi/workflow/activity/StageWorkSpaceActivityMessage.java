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
import com.tsi.workflow.beans.dao.System;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author Radha.Adhimoolam
 */
public class StageWorkSpaceActivityMessage extends ActivityLogMessage {

    String status;
    System system;
    String comment;
    String errorMessage;

    public StageWorkSpaceActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public StageWorkSpaceActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String lStatus) {
	this.status = lStatus;
    }

    public void setComment(String comment) {
	this.comment = comment;
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
	if (status.equals("initiated")) {
	    Message = "{0} {1} has {2} the staging workspace creation for the implementation plan {3}, system {4}";
	} else if (system == null) {
	    Message = "{0} {1} has {2} the staging workspace creation for the implementation plan {3}";
	} else {
	    Message = "{0} {1} has triggered the staging workspace creation for the implementation plan {3} for the system {4}. Status : {2}";
	}
	if (status.equals("failed")) {
	    Message = Message + ". Console Message - " + errorMessage;
	}
	if (comment != null) {
	    Message = Message.concat(" with comments \"").concat(comment).concat("\"");
	}

	if (user.getCurrentDelegatedUser() != null) {
	    Message = Message + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}

	if (user.getCurrentDelegatedUser() == null) {
	    if (system == null) {
		return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId());
	    } else {
		return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId(), system.getName());
	    }
	} else {
	    if (system == null) {
		return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId(), user.getCurrentDelegatedUser().getDisplayName());
	    } else {
		return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId(), system.getName(), user.getCurrentDelegatedUser().getDisplayName());
	    }
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
