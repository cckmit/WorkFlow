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
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class DevlBuildActivityMessage extends ActivityLogMessage {

    System system;
    JenkinsLog jenkinsLog;
    String status;

    public JenkinsLog getJenkinsLog() {
	return jenkinsLog;
    }

    public void setJenkinsLog(JenkinsLog jenkinsLog) {
	this.jenkinsLog = jenkinsLog;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public DevlBuildActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    @Override
    public String processMessage() {
	String Message;
	if (jenkinsLog != null && jenkinsLog.getJobStatus()) {
	    Message = "DEVL build {0} for the implementation plan {1}, system {2}";
	    return MessageFormat.format(Message, "successfully completed", impPlan.getId(), system.getName());
	} else if (jenkinsLog != null && !jenkinsLog.getJobStatus()) {
	    if (jenkinsLog.getErrorMessage() != null && !jenkinsLog.getErrorMessage().isEmpty()) {
		// 2311 Message = "DEVL build {0} for the implementation plan {1}, system {2},
		// due to the following error \"{3}\"";
		Message = "DEVL build failed for system{0} due to the following errors: {1}"; // 2311
		return MessageFormat.format(Message, system.getName(), jenkinsLog.getErrorMessage());
	    } else if (jenkinsLog.getFailedFiles() != null && !jenkinsLog.getFailedFiles().isEmpty()) {
		// 2311 Message = "DEVL build {0} for the implementation plan {1}, system {2},
		// due to build error in the files ({3})";
		Message = "DEVL build failed for system{0} due to the following errors: {1}"; // 2311
		return MessageFormat.format(Message, system.getName(), String.join(",", jenkinsLog.getFailedFiles()));
	    } else {
		Message = "DEVL build failed for system {0} and no error message found from jenkins";
		return MessageFormat.format(Message, system.getName());
	    }
	} else if (status != null && !status.isEmpty()) {
	    Message = "{0} {1} has {2} the DEVL build for the implementation plan {3}, system {4}";
	    if (user.getCurrentDelegatedUser() != null) {
		Message = Message + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	    }
	    return MessageFormat.format(Message, user.getCurrentRole(), user.getDisplayName(), status, impPlan.getId(), system.getName());
	} else {
	    return "";
	}
    }

    @Override
    public Priority getLogLevel() {
	if ((jenkinsLog != null && !jenkinsLog.getJobStatus()) || status.equals("failed") || status.equals("cancelled")) {
	    return Priority.ERROR;
	}
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
	system = (System) beans[0];
    }

}
