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
 * @author vamsi.krishnarao
 */
public class DevManagerChangeActivityMessage extends ActivityLogMessage {

    private String oldDevManager;
    private String newDevManager;

    public void setOldDevManager(String oldDevManager) {
	this.oldDevManager = oldDevManager;
    }

    public void setNewDevManager(String newDevManager) {
	this.newDevManager = newDevManager;
    }

    public DevManagerChangeActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {

	String lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has changed the  dev Manager from " + oldDevManager + " to " + newDevManager;

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
