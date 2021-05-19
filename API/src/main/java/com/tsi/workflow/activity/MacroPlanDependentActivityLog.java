package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;

public class MacroPlanDependentActivityLog extends ActivityLogMessage {

    private boolean status;
    private String dependentPlans;
    private String actionType;
    private ImpPlan lImpPlan;

    public boolean isStatus() {
	return status;
    }

    public void setStatus(boolean status) {
	this.status = status;
    }

    public String getDependentPlans() {
	return dependentPlans;
    }

    public void setDependentPlans(String dependentPlans) {
	this.dependentPlans = dependentPlans;
    }

    public String getActionType() {
	return actionType;
    }

    public void setActionType(String actionType) {
	this.actionType = actionType;
    }

    public ImpPlan getlImpPlan() {
	return lImpPlan;
    }

    public void setlImpPlan(ImpPlan lImpPlan) {
	this.lImpPlan = lImpPlan;
    }

    public MacroPlanDependentActivityLog(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
	this.lImpPlan = impPlan;
    }

    @Override
    public String processMessage() {
	StringBuilder sb = new StringBuilder();
	if (status) {
	    sb.append("Plans ").append(dependentPlans.toString().trim().toUpperCase()).append(" had source artifacts that were built against the ").append(actionType);
	    sb.append(" macro/header for the plan ").append(lImpPlan.getId());
	    // }else {
	    // sb.append("No Plans had source artifacts that were built against the
	    // ").append(actionType);
	    // sb.append(" macro/header for the plan ").append(lImpPlan.getId());
	    // }
	} else {
	    sb.append("Error While getting dependent plans which had same source artifacts that were built against the macro/header");
	}
	return sb.toString();
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
