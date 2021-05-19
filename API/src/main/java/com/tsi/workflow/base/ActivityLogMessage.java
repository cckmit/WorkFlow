/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ActivityLog;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public abstract class ActivityLogMessage {

    protected User user;
    protected ImpPlan impPlan;
    protected Implementation implementation;

    public ActivityLogMessage() {

    }

    public ActivityLogMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	this.impPlan = impPlan;
	this.implementation = implementation;
	if (beans != null) {
	    this.setArguments(beans);
	}
    }

    public ActivityLogMessage(ImpPlan impPlan, Implementation implementation) {
	this.impPlan = impPlan;
	this.implementation = implementation;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public User getUser() {
	return user;
    }

    public abstract String processMessage();

    public abstract Priority getLogLevel();

    public abstract void setArguments(IBeans... beans);

    public final ActivityLog getActivityLog() throws Exception {
	ActivityLog lLog = new ActivityLog();
	lLog.setImpId(implementation);
	lLog.setPlanId(impPlan);
	lLog.setMessage(processMessage());
	lLog.setLogLevel(getLogLevel().toString());
	return lLog;
    }
}
