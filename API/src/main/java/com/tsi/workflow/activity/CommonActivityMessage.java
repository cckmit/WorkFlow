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
public class CommonActivityMessage extends ActivityLogMessage {

    private String message;
    private String status;

    public CommonActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public String processMessage() {
	return message;
    }

    @Override
    public Priority getLogLevel() {
	if (getStatus() != null && !getStatus().isEmpty() && getStatus().contains("Fail")) {
	    return Priority.ERROR;
	}
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
