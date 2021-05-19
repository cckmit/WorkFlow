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
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class ExceptionOccurOnlineActivityMessage extends ActivityLogMessage {

    public ExceptionOccurOnlineActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    @Override
    public String processMessage() {
	String lActivityMessage = "";
	lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " Jenkins Job failed while doing online/fallback feedback on GIT for the Plan " + impPlan.getId();
	if (user.getCurrentDelegatedUser() != null) {
	    lActivityMessage = lActivityMessage + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}
	return lActivityMessage;
    }

}
