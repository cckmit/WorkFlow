package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;

public class PeerReviewActivityMessage extends ActivityLogMessage {

    boolean status;
    String message;

    public boolean isStatus() {
	return status;
    }

    public void setStatus(boolean status) {
	this.status = status;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public PeerReviewActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	StringBuilder lMessage = new StringBuilder();
	if (!status) {
	    lMessage.append("Error : " + getMessage());
	}
	return lMessage.toString();
    }

    @Override
    public Priority getLogLevel() {
	if (status) {
	    return Priority.INFO;
	} else {
	    return Priority.ERROR;
	}
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
