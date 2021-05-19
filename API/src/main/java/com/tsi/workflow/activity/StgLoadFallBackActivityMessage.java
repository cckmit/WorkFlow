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
import java.text.MessageFormat;
import java.util.Set;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class StgLoadFallBackActivityMessage extends ActivityLogMessage {

    Set<String> lPlanList;
    com.tsi.workflow.beans.dao.System system;

    public StgLoadFallBackActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public StgLoadFallBackActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public Set<String> getlPlanList() {
	return lPlanList;
    }

    public void setlPlanList(Set<String> lPlanList) {
	this.lPlanList = lPlanList;
    }

    @Override
    public String processMessage() {
	String Message = "";
	Message = "FallBack LoadSet has been  created with the following plans {0}";
	return MessageFormat.format(Message, String.join(",", getlPlanList()));
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
	system = (com.tsi.workflow.beans.dao.System) beans[0];
    }

}
