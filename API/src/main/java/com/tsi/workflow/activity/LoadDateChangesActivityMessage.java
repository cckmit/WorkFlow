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
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;
import org.apache.log4j.Priority;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LoadDateChangesActivityMessage extends ActivityLogMessage {

    private SystemLoad systemLoad;
    private Date previousLoadDate;

    public void setSystemLoad(SystemLoad systemLoad) {
	this.systemLoad = systemLoad;
    }

    public LoadDateChangesActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public void setPreviousLoadDate(Date previousLoadDate) {
	this.previousLoadDate = previousLoadDate;
    }

    @Override
    public String processMessage() {
	String lPreviousETDate = null;
	String lCurrentETLoadDateTime = null;

	if (previousLoadDate != null) {
	    try {
		lPreviousETDate = DateHelper.convertGMTtoEST(previousLoadDate);
		lCurrentETLoadDateTime = DateHelper.convertGMTtoEST(systemLoad.getLoadDateTime());
	    } catch (Exception e) {
	    }
	}

	String lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has changed the load date of system " + systemLoad.getSystemId().getName() + " from " + lPreviousETDate + " to " + lCurrentETLoadDateTime;

	if (user.getCurrentDelegatedUser() != null) {
	    lActivityMessage = lActivityMessage + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}

	return lActivityMessage;
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
