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
public class CheckinActivityMessage extends ActivityLogMessage {

    boolean error = false;
    String comments;

    public void setComments(String comments) {
	this.comments = comments;
    }

    public void setError(boolean error) {
	this.error = error;
    }

    public CheckinActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    @Override
    public String processMessage() {
	if (error) {
	    return MessageFormat.format("{0} {1} has initiated check-in for implementation {2}. {3}", user.getCurrentRole(), user.getDisplayName(), implementation.getId(), comments);
	} else {
	    return MessageFormat.format("{1} {2} has checked-in Implementation - {0} ", implementation.getId(), user.getCurrentRole(), user.getDisplayName());
	}
    }

    @Override
    public Priority getLogLevel() {
	if (error) {
	    return Priority.ERROR;
	}
	return Priority.INFO;
    }

}
