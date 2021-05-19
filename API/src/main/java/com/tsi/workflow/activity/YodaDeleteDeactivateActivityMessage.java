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
import com.tsi.workflow.beans.dao.SystemLoadActions;
import org.apache.log4j.Priority;

/**
 *
 * @author Radha.Adhimoolam
 */
public class YodaDeleteDeactivateActivityMessage extends ActivityLogMessage {

    SystemLoadActions loadActions;
    Priority lPriority;
    boolean isAutoReject = false;
    boolean isFallback = false;

    public YodaDeleteDeactivateActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public boolean isIsAutoReject() {
	return isAutoReject;
    }

    public void setIsAutoReject(boolean isAutoReject) {
	this.isAutoReject = isAutoReject;
    }

    public boolean isIsFallback() {
	return isFallback;
    }

    public void setIsFallback(boolean isFallback) {
	this.isFallback = isFallback;
    }

    /*
     * This message will be executed only for Yoda deletion and deactivation, only
     * the plan is for rejection and Fallback
     */
    @Override
    public String processMessage() {
	String lMessage = "";
	if (loadActions.getLastActionStatus().equals("SUCCESS")) {
	    lPriority = Priority.INFO;
	    String rejectQuote = (isAutoReject ? " auto-rejected" : "rejected");
	    rejectQuote = isFallback ? "Fallenback" : rejectQuote;
	    lMessage = "Loadset " + loadActions.getSystemLoadId().getLoadSetName() + " was " + loadActions.getStatus() + " from " + loadActions.getVparId().getName() + " as plan " + impPlan.getId() + " was " + rejectQuote;
	} else {
	    lPriority = Priority.ERROR;
	    String rejectQuote = (isAutoReject ? " via plan rejection" : "");
	    rejectQuote = isFallback ? "via plan Fallenback" : rejectQuote;
	    lMessage = "Loadset " + loadActions.getSystemLoadId().getLoadSetName() + " of the plan " + impPlan.getId() + " for the system " + loadActions.getSystemId().getName() + " has been failed to " + loadActions.getStatus() + " on the test system " + loadActions.getVparId().getName() + rejectQuote;
	}
	if (user.getCurrentDelegatedUser() != null) {
	    lMessage = lMessage + " , on behalf of " + user.getCurrentDelegatedUser().getCurrentRole() + " " + user.getCurrentDelegatedUser().getDisplayName();
	}
	return lMessage;
    }

    @Override
    public Priority getLogLevel() {
	return lPriority;
    }

    @Override
    public void setArguments(IBeans... beans) {
	loadActions = (SystemLoadActions) beans[0];
    }

}
