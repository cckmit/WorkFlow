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
public class DeletePlanActivityMessage extends ActivityLogMessage {

    public DeletePlanActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	StringBuilder lMessage = new StringBuilder();
	lMessage.append(user.getCurrentRole()).append(" ").append(user.getDisplayName()).append(" has deleted the Implementation Plan - ").append(impPlan.getId());
	if (user.getCurrentDelegatedUser() != null) {
	    lMessage.append(", on behalf of ").append(user.getCurrentDelegatedUser().getDisplayName());
	}
	return lMessage.toString();
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
