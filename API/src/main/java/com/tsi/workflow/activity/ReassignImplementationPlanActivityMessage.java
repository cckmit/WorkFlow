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
 * @author gn.ebinezardharmaraj
 */
public class ReassignImplementationPlanActivityMessage extends ActivityLogMessage {

    private String role;
    private String oldLeadName;

    public ReassignImplementationPlanActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public String getOldLeadName() {
	return oldLeadName;
    }

    public void setOldLeadName(String oldLeadName) {
	this.oldLeadName = oldLeadName;
    }

    @Override
    public String processMessage() {
	StringBuilder activityMessage = new StringBuilder();
	activityMessage.append(impPlan.getId());
	activityMessage.append(" Application Developer Lead has been reassigned from ");
	activityMessage.append(getOldLeadName());
	activityMessage.append(" to ");
	activityMessage.append(impPlan.getLeadName());
	activityMessage.append(" by ");
	activityMessage.append(role);
	activityMessage.append("  ");
	activityMessage.append(impPlan.getModifiedBy());

	return activityMessage.toString();
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
