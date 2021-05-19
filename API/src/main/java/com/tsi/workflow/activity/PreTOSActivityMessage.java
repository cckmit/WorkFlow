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
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.tos.model.TOSResult;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class PreTOSActivityMessage extends ActivityLogMessage {

    PreProductionLoads loadActions;
    Priority lPriority;
    TOSResult tOSResult;
    String loadActionStatus;

    public String getLoadActionStatus() {
	return loadActionStatus;
    }

    public void setLoadActionStatus(String loadActionStatus) {
	this.loadActionStatus = loadActionStatus;
    }

    public PreTOSActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public TOSResult gettOSResult() {
	return tOSResult;
    }

    public void settOSResult(TOSResult tOSResult) {
	this.tOSResult = tOSResult;
    }

    @Override
    public String processMessage() {
	if (loadActions.getLastActionStatus().equals("SUCCESS")) {
	    lPriority = Priority.INFO;
	    StringBuilder lReturn = new StringBuilder().append("Loadset ").append(loadActions.getSystemLoadId().getLoadSetName()).append(" of the plan ").append(impPlan.getId()).append(" for the system ").append(loadActions.getCpuId().getCpuName()).append(" has been ").append(getLoadActionStatus()).append(" on pre prod: Status - SUCCESS");
	    if (loadActions.getCpuId() != null) {
		lReturn.append(" CPU : ").append(loadActions.getCpuId().getCpuCode());
	    }
	    if (tOSResult != null) {
		lReturn.append(" ,TOSMessage: ").append(tOSResult.getMessage()).append(" ,RC: ").append(tOSResult.getReturnValue());
		if (tOSResult.getMessage().toUpperCase().contains("DELETE ALREADY SCHEDULED")) {
		    lReturn.append(", Please check the zTPF system status prior to attempting a load/activate");
		}
	    }
	    return lReturn.toString();
	} else if (loadActions.getLastActionStatus().equals("INPROGRESS")) {
	    lPriority = Priority.INFO;
	    StringBuilder lReturn = new StringBuilder().append("Loadset ").append(loadActions.getSystemLoadId().getLoadSetName()).append(" of the plan ").append(impPlan.getId()).append(" for the system ").append(loadActions.getCpuId().getCpuName()).append(" has been ").append(getLoadActionStatus()).append(" on pre prod : Status - INPROGRESS ");
	    if (loadActions.getCpuId() != null) {
		lReturn.append(" with CPU ").append(loadActions.getCpuId().getCpuCode());
	    }
	    if (tOSResult != null) {
		lReturn.append(" ,TOSMessage: ").append(tOSResult.getMessage()).append(" ,RC: ").append(tOSResult.getReturnValue());
	    }
	    return lReturn.toString();
	} else {
	    lPriority = Priority.ERROR;
	    StringBuilder lReturn = new StringBuilder().append("Loadset ").append(loadActions.getSystemLoadId().getLoadSetName()).append(" of the plan ").append(impPlan.getId()).append(" for the system ").append(loadActions.getCpuId().getCpuName()).append(" has been failed and reverted to ").append(getLoadActionStatus()).append(" on pre prod");
	    if (loadActions.getCpuId() != null) {
		lReturn.append(" with CPU ").append(loadActions.getCpuId().getCpuCode()).append(" ,RC: ").append(tOSResult.getReturnValue());
	    }
	    if (tOSResult != null) {
		lReturn.append(" ,TOSMessage: ").append(tOSResult.getMessage());
	    }
	    return lReturn.toString();
	}
    }

    @Override
    public Priority getLogLevel() {
	return lPriority;
    }

    @Override
    public void setArguments(IBeans... beans) {
	loadActions = (PreProductionLoads) beans[0];
    }

}
