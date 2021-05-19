package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoad;
import org.apache.log4j.Priority;

public class DSLFilePopulateActivityMsg extends ActivityLogMessage {

    boolean status;
    SystemLoad pSystemLoad;

    public DSLFilePopulateActivityMsg(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public boolean getStatus() {
	return status;
    }

    public void setStatus(boolean lStatus) {
	this.status = lStatus;
    }

    @Override
    public String processMessage() {
	StringBuilder lMessage = new StringBuilder();
	if (status) {
	    lMessage.append("WSP  loadset: ").append(pSystemLoad.getLoadSetName() + " for plan ").append(pSystemLoad.getPlanId().getId() + " to LTQA load deck is completed sucessfully");
	} else {
	    lMessage.append("WSP  loadset: ").append(pSystemLoad.getLoadSetName() + " for plan ").append(pSystemLoad.getPlanId().getId() + " to LTQA load deck is failed");
	}
	return lMessage.toString();
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
	pSystemLoad = (SystemLoad) beans[0];
    }

}
