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
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.DateHelper;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class TOSActivityMessage extends ActivityLogMessage {

    ProductionLoads loadActions;
    Priority lPriority;
    TOSResult tOSResult;
    String loadSetName = "";
    boolean forceActivate = false;

    public TOSActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    public TOSResult gettOSResult() {
	return tOSResult;
    }

    public void settOSResult(TOSResult tOSResult) {
	this.tOSResult = tOSResult;
    }

    public boolean getforceActivate() {
	return forceActivate;
    }

    public void setforceActivate(boolean forceActivate) {
	this.forceActivate = forceActivate;
    }

    @Override
    public String processMessage() {
	if (loadActions.getStatus().startsWith("FALLBACK")) {
	    loadSetName = loadActions.getSystemLoadId().getFallbackLoadSetName();
	} else {
	    loadSetName = loadActions.getSystemLoadId().getLoadSetName();
	}
	/*
	 * author: Ramkumar Seenivasan Created date: 29/July/2019 Story No: 2386
	 * Comments: ForceActivate activity log was generated based on user input
	 */

	if (forceActivate) {
	    lPriority = Priority.INFO;
	    StringBuilder lReturn = new StringBuilder().append("Force Activate action was used by ").append(user.getCurrentOrDelagateUser().getDisplayName()).append(" to activate the loadset ").append(loadSetName).append(" ahead of its scheduled time at ").append(DateHelper.convertGMTtoEST(loadActions.getSystemLoadId().getLoadDateTime()));
	    return lReturn.toString();
	} else {
	    if (loadActions.getLastActionStatus().equals("SUCCESS")) {
		lPriority = Priority.INFO;
		StringBuilder lReturn = new StringBuilder().append("Loadset ").append(loadSetName).append(" of the plan ").append(impPlan.getId()).append(" for the system ").append(loadActions.getSystemId().getName()).append(" has been ").append(loadActions.getStatus()).append(" on production : Status - SUCCESS ");
		if (user.getCurrentDelegatedUser() != null) {
		    lReturn.append(", on behalf of ").append(user.getCurrentDelegatedUser().getDisplayName());
		}
		if (loadActions.getCpuId() != null) {
		    lReturn.append(" CPU : ").append(loadActions.getCpuId().getCpuCode());
		}
		if (tOSResult != null) {
		    lReturn.append(" ,TOSMessage: ").append(tOSResult.getMessage()).append(" ,RC: ").append(tOSResult.getReturnValue());
		    // if (tOSResult.getMessage().toUpperCase().contains("DELETE ALREADY
		    // SCHEDULED")) {
		    // lReturn.append(", Please check the zTPF system status prior to attempting a
		    // load/activate");
		    // }
		}
		if (loadActions.getStatus().startsWith("FALLBACK")) {
		    lReturn.insert(0, "Fallback ");
		}
		return lReturn.toString();
	    } else if (loadActions.getLastActionStatus().equals("INPROGRESS")) {
		lPriority = Priority.INFO;
		StringBuilder lReturn = new StringBuilder().append("Loadset of the plan ").append(impPlan.getId()).append(" for the system ").append(loadActions.getSystemId().getName()).append(" has been ").append(loadActions.getStatus()).append(" on production : Status - INPROGRESS ");
		if (user.getCurrentDelegatedUser() != null) {
		    lReturn.append(", on behalf of ").append(user.getCurrentDelegatedUser().getDisplayName());
		}
		if (loadActions.getCpuId() != null) {
		    lReturn.append(" with CPU ").append(loadActions.getCpuId().getCpuCode());
		}
		if (tOSResult != null) {
		    lReturn.append(" ,TOSMessage: ").append(tOSResult.getMessage()).append(" ,RC: ").append(tOSResult.getReturnValue());
		}
		if (loadActions.getStatus().startsWith("FALLBACK")) {
		    lReturn.insert(0, "Fallback ");
		}
		return lReturn.toString();
	    } else {
		lPriority = Priority.ERROR;
		StringBuilder lReturn = new StringBuilder().append("Loadset ").append(loadSetName).append(" of the plan ").append(impPlan.getId()).append(" for the system ").append(loadActions.getSystemId().getName()).append(" has been failed and reverted to ").append(loadActions.getStatus()).append(" on production");
		if (user.getCurrentDelegatedUser() != null) {
		    lReturn.append(", on behalf of ").append(user.getCurrentDelegatedUser().getDisplayName());
		}
		if (loadActions.getCpuId() != null) {
		    lReturn.append(" with CPU ").append(loadActions.getCpuId().getCpuCode()).append(" ,RC: ").append(tOSResult.getReturnValue());
		}
		if (tOSResult != null) {
		    lReturn.append(" ,TOSMessage: ").append(tOSResult.getMessage());
		}
		if (loadActions.getStatus().startsWith("FALLBACK")) {
		    lReturn.insert(0, "Fallback ");
		}
		return lReturn.toString();
	    }
	}
    }

    @Override
    public Priority getLogLevel() {
	return lPriority;
    }

    @Override
    public void setArguments(IBeans... beans) {
	loadActions = (ProductionLoads) beans[0];
    }

}
