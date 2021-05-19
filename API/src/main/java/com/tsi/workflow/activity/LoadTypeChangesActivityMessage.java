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
 * @author Radha.Adhimoolam
 */
public class LoadTypeChangesActivityMessage extends ActivityLogMessage {

    private String oldLoadType;
    private String loadTypeComments;

    public LoadTypeChangesActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public void setOldLoadType(String oldLoadType) {
	this.oldLoadType = oldLoadType;
    }

    public String getLoadTypeComments() {
	return loadTypeComments;
    }

    public void setLoadTypeComments(String loadTypeComments) {
	this.loadTypeComments = loadTypeComments;
    }

    @Override
    public String processMessage() {
	String lActivityMessage = "";
	if (getLoadTypeComments() != null && !getLoadTypeComments().equals("")) {
	    lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has changed the load type  " + oldLoadType + "  to  " + impPlan.getLoadType() + " for the implementation plan -  " + impPlan.getId() + "  with comments -  " + getLoadTypeComments();
	    if (user.getCurrentDelegatedUser() != null) {
		lActivityMessage = lActivityMessage + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	    }
	} else {
	    lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has changed the load type from " + oldLoadType + " to " + impPlan.getLoadType();
	    if (user.getCurrentDelegatedUser() != null) {
		lActivityMessage = lActivityMessage + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	    }
	}
	return lActivityMessage;
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
