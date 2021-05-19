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

public class MacroHeaderOnlineMessage extends ActivityLogMessage {

    Priority lPriority;

    public MacroHeaderOnlineMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	String lMessage = "";
	lPriority = Priority.INFO;
	lMessage = "UTILJCLM called as dlmcrnms.mac is present in implementation plan " + impPlan.getId();
	return lMessage;

    }

    @Override
    public Priority getLogLevel() {
	return lPriority;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
