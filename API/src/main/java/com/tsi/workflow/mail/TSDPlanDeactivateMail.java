/**
 *
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vinoth.ponnurangan
 *
 */
public class TSDPlanDeactivateMail extends MailMessage {

    private String parentPlan;

    private Date loadDateTime;

    private Set<String> targetSystem = new HashSet<>();
    private Boolean tsdDeactivateFlag;
    private String dependentPlan;

    public String getParentPlan() {
	return parentPlan;
    }

    public void setParentPlan(String parentPlan) {
	this.parentPlan = parentPlan;
    }

    public Date getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(Date loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    public Set<String> getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(Set<String> targetSystem) {
	this.targetSystem = targetSystem;
    }

    public Boolean getTsdDeactivateFlag() {
	return tsdDeactivateFlag;
    }

    public void setTsdDeactivateFlag(Boolean tsdDeactivateFlag) {
	this.tsdDeactivateFlag = tsdDeactivateFlag;
    }

    public String getDependentPlan() {
	return dependentPlan;
    }

    public void setDependentPlan(String dependentPlan) {
	this.dependentPlan = dependentPlan;
    }

    @Override
    public void processMessage() {

	StringBuilder message = new StringBuilder();
	String subject = "";
	if (getTsdDeactivateFlag()) {
	    subject = " Implementation Plan " + getParentPlan() + " got deactivated for target system(s) " + String.join(",", getTargetSystem()) + " Action needed!";
	    message.append("Implementation Plan ").append(getParentPlan()).append(" got deactivated for target system(s) ").append(String.join(",", getTargetSystem())).append(" on ").append(DateHelper.convertGMTtoEST(getLoadDateTime())).append(". ").append(" Please take appropriate action to reload it or fall it back to avoid any problem with any dependent plans. ");
	} else {
	    subject = " Implementation Plan " + getDependentPlan() + " is dependent on " + getParentPlan() + " which got deactivated  for target system(s)  " + String.join(",", getTargetSystem()) + " . Attention required! ";
	    message.append("Implementation Plan ").append(getParentPlan()).append(" got deactivated for target system(s) ").append(String.join(",", getTargetSystem())).append(" on ").append(DateHelper.convertGMTtoEST(getLoadDateTime())).append(". ").append(" Your plan will not be deployed to production until ").append(getParentPlan()).append(" is fallen back or redeployed in production fully. ");
	}

	this.setSubject(subject);
	this.setMessage(message.toString());

    }

}
