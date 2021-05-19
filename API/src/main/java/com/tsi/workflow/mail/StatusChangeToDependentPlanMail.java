/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.DateHelper;
import java.text.MessageFormat;
import java.util.Date;

/**
 *
 * @author User
 */
public class StatusChangeToDependentPlanMail extends MailMessage {

    private boolean tssDeployStatus = false;
    private String oldStatus;
    private String newStatus;
    private String impPlanId;
    private Date activityTime = null;
    private String reason;

    public String getOldStatus() {
	return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
	this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
	return newStatus;
    }

    public void setNewStatus(String newStatus) {
	this.newStatus = newStatus;
    }

    public String getImpPlanId() {
	return impPlanId;
    }

    public void setImpPlanId(String impPlanId) {
	this.impPlanId = impPlanId;
    }

    public Date getActivityTime() {
	return activityTime;
    }

    public void setActivityTime(Date activityTime) {
	this.activityTime = activityTime;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public boolean isTssDeployStatus() {
	return tssDeployStatus;
    }

    public void setTssDeployStatus(boolean tssDeployStatus) {
	this.tssDeployStatus = tssDeployStatus;
    }

    @Override
    public void processMessage() {
	if (activityTime == null) {
	    String lSubject = "Implementation Plan " + impPlanId + " - Status Changed";
	    String lMessage = "";
	    if (isTssDeployStatus()) {
		lMessage = MessageFormat.format("Status of implementation plan - {0} has been changed to {1}", impPlanId, Constants.PlanStatus.valueOf(newStatus).getDisplayName());

	    } else {
		lMessage = MessageFormat.format("Status of implementation plan - {0} has been changed from {1} to {2}", impPlanId, Constants.PlanStatus.valueOf(oldStatus).getDisplayName(), Constants.PlanStatus.valueOf(newStatus).getDisplayName());
		if (reason != null) {
		    lMessage = lMessage + " with comment - " + reason;
		}
	    }

	    this.setSubject(lSubject);
	    this.setMessage(lMessage);
	} else {
	    String lSubject = "Implementation Plan " + impPlanId + " - Status Changed";
	    String lMessage = MessageFormat.format("Status of implementation plan - {0} has been changed from {1} to {2} at {3}", impPlanId, Constants.PlanStatus.valueOf(oldStatus).getDisplayName(), Constants.PlanStatus.valueOf(newStatus).getDisplayName(), DateHelper.convertGMTtoEST(activityTime));
	    if (reason != null) {
		lMessage = lMessage + " with comment - " + reason;
	    }
	    this.setSubject(lSubject);
	    this.setMessage(lMessage);
	}
    }

}
