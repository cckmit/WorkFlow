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
import com.tsi.workflow.utils.Constants;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class LoadSetActivityMessage extends ActivityLogMessage {

    SystemLoadActions loadActions;
    Priority lPriority;
    boolean isAutoReject = false;

    public LoadSetActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public boolean isIsAutoReject() {
	return isAutoReject;
    }

    public void setIsAutoReject(boolean isAutoReject) {
	this.isAutoReject = isAutoReject;
    }

    @Override
    public String processMessage() {
	String lQAEnvi = "Regression";
	if (Constants.PlanStatus.getQAFunctionalDeploymentStatus().keySet().contains(impPlan.getPlanStatus())) {
	    lQAEnvi = "Functional";
	}
	String lMessage = "";
	if (loadActions.getTestStatus() != null) {
	    if (loadActions.getTestStatus().equals("PASS")) {
		lPriority = Priority.INFO;
		lMessage = "QA " + lQAEnvi + " testing of the plan " + impPlan.getId() + " for the system " + loadActions.getSystemId().getName() + " has PASSED" + " on the test system " + loadActions.getVparId().getName() + (isAutoReject ? " via plan rejection" : "");
	    } else {
		lPriority = Priority.INFO;
		lMessage = "QA " + lQAEnvi + " testing of the plan " + impPlan.getId() + " for the system " + loadActions.getSystemId().getName() + " has FAILED" + " on the test system " + loadActions.getVparId().getName() + (isAutoReject ? " via plan rejection" : "");
	    }
	}
	if (!lMessage.isEmpty()) {
	    return lMessage;
	}
	if (loadActions.getLastActionStatus().equals("SUCCESS")) {
	    lPriority = Priority.INFO;
	    lMessage = "Loadset " + loadActions.getSystemLoadId().getLoadSetName() + " of the plan " + impPlan.getId() + " for the system " + loadActions.getSystemId().getName() + " has been " + loadActions.getStatus() + " on the test system " + loadActions.getVparId().getName() + (isAutoReject ? " via plan rejection" : "");
	} else {
	    lPriority = Priority.ERROR;
	    lMessage = "Loadset " + loadActions.getSystemLoadId().getLoadSetName() + " of the plan " + impPlan.getId() + " for the system " + loadActions.getSystemId().getName() + " has been failed to " + loadActions.getStatus() + " on the test system " + loadActions.getVparId().getName() + (isAutoReject ? " via plan rejection" : "");
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
