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
import com.tsi.workflow.beans.dao.OnlineBuild;
import org.apache.log4j.Priority;

/**
 *
 * @author Radha.Adhimoolam
 */
public class MergeOnlineActivityMessage extends ActivityLogMessage {

    private String system;
    private OnlineBuild jobInfo;

    public MergeOnlineActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    public String getSystem() {
	return system;
    }

    public void setSystem(String system) {
	this.system = system;
    }

    public OnlineBuild getJobInfo() {
	return jobInfo;
    }

    public void setJobInfo(OnlineBuild jobInfo) {
	this.jobInfo = jobInfo;
    }

    @Override
    public String processMessage() {
	String lActivityMessage = "";
	if (getJobInfo().getJobStatus().equalsIgnoreCase("S")) {
	    lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has merged the segments from implementation plan " + impPlan.getId() + " system " + getSystem() + " to online";
	} else {
	    lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + ", failed to merge the segments from implementation plan " + impPlan.getId() + " system " + getSystem() + " to online";
	}
	if (user.getCurrentDelegatedUser() != null) {
	    lActivityMessage = lActivityMessage + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}
	return lActivityMessage;
    }

}
