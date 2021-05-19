/**
 *
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;

/**
 * @author vinoth.ponnurangan
 *
 */
public class DevManagerApproveActivityMessage extends ActivityLogMessage {

    public DevManagerApproveActivityMessage() {

    }

    public DevManagerApproveActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    private String devManger;
    private String devLead;
    private String status;
    private String dependentPlanMessage;
    private Boolean leadCondition;

    public String getDevManger() {
	return devManger;
    }

    public void setDevManger(String devManger) {
	this.devManger = devManger;
    }

    public String getDevLead() {
	return devLead;
    }

    public void setDevLead(String devLead) {
	this.devLead = devLead;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getDependentPlanMessage() {
	return dependentPlanMessage;
    }

    public void setDependentPlanMessage(String dependentPlanMessage) {
	this.dependentPlanMessage = dependentPlanMessage;
    }

    public Boolean getLeadCondition() {
	return leadCondition;
    }

    public void setLeadCondition(Boolean leadCondition) {
	this.leadCondition = leadCondition;
    }

    @Override
    public String processMessage() {
	if (status.equals("failed")) {
	    StringBuilder activityMessage = new StringBuilder();
	    if (getLeadCondition()) {
		activityMessage.append("Approval Failed: ");
		activityMessage.append(" ADL - ");
		activityMessage.append(devLead);
		activityMessage.append(" and ");
		activityMessage.append("Approving manager -");
		activityMessage.append(devManger);
		activityMessage.append(" cannot be the same person. ");
		activityMessage.append("Please re-delegate to a different Dev Manager for approval or contact tool admin for assistance");
	    } else {
		activityMessage.append("Approval Failed: ");
		activityMessage.append(" Developer - ");
		activityMessage.append(devLead);
		activityMessage.append(" and ");
		activityMessage.append("Approving manager -");
		activityMessage.append(devManger);
		activityMessage.append(" cannot be the same person. ");
		activityMessage.append("Please re-delegate to a different Dev Manager for approval or contact tool admin for assistance");

	    }
	    return activityMessage.toString();
	} else {
	    StringBuilder activityMessage = new StringBuilder();
	    if (status.equals("dependent")) {
		activityMessage.append(dependentPlanMessage.toString());
	    } else {

		activityMessage.append(devManger);
		activityMessage.append("- Plan approved successfully completed ");
		activityMessage.append(" for the implementation plan " + impPlan.getId());
	    }
	    return activityMessage.toString();
	}
    }

    @Override
    public Priority getLogLevel() {
	if (status.equals("failed")) {
	    return Priority.ERROR;
	}
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
