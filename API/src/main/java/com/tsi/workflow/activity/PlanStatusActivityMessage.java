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
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class PlanStatusActivityMessage extends ActivityLogMessage {

    String status = "";
    String reason;
    private String lSystem;

    public void setStatus(String status) {
	this.status = status;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public PlanStatusActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    public String getlSystem() {
	return lSystem;
    }

    public void setlSystem(String lSystem) {
	this.lSystem = lSystem;
    }

    @Override
    public String processMessage() {
	String lsystemMessage = " ";
	if (impPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name()) || impPlan.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.FALLBACK.name())) {
	    lsystemMessage = " system " + getlSystem();
	}
	if (user.getCurrentDelegatedUser() == null) {
	    if (reason != null) {
		return MessageFormat.format("{0} {1} has updated the status of implementation plan {2}  {5} as , {3}  with comment - {4}", user.getCurrentRole(), user.getDisplayName(), impPlan.getId(), status, reason, lsystemMessage);
	    }
	    return MessageFormat.format("{0} {1} has updated the status of implementation plan {2} {4}  as , {3}", user.getCurrentRole(), user.getDisplayName(), impPlan.getId(), status, lsystemMessage);
	} else {
	    if (reason != null) {
		return MessageFormat.format("{0} {1} has updated the status of implementation plan {2}  {6} as , {4} on behalf of {3}  with comment - {5}", user.getCurrentDelegatedUser().getCurrentRole(), user.getDisplayName(), impPlan.getId(), user.getCurrentDelegatedUser().getDisplayName(), status, reason, lsystemMessage);
	    }
	    return MessageFormat.format("{0} {1} has updated the status of implementation plan {2}  {5} as {4} on behalf of {3}", user.getCurrentDelegatedUser().getCurrentRole(), user.getDisplayName(), impPlan.getId(), user.getCurrentDelegatedUser().getDisplayName(), status, lsystemMessage);
	}

    }

}
