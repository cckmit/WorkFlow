package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;

public class PreProdActionsActivityLog extends ActivityLogMessage {

    boolean status;
    String message;

    public PreProdActionsActivityLog(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public boolean getStatus() {
	return status;
    }

    public void setStatus(boolean lStatus) {
	this.status = lStatus;
    }

    @Override
    public String processMessage() {
	return getMessage();
    }

    @Override
    public Priority getLogLevel() {
	if (!status) {
	    return Priority.ERROR;
	}
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
	// TODO Auto-generated method stub
    }

}
