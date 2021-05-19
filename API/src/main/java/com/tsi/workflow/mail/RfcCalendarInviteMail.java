package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;

public class RfcCalendarInviteMail extends MailMessage {

    private String rfcNumber;
    private String targetSystem;
    private Date loadDateTime;
    private ImpPlan impPlan;
    private String developerName;

    public String getRfcNumber() {
	return rfcNumber;
    }

    public void setRfcNumber(String rfcNumber) {
	this.rfcNumber = rfcNumber;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public Date getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(Date loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    @Override
    public void processMessage() {

	String subject = getImpPlan().getDevManagerName() + " - " + getTargetSystem() + " - " + getImpPlan().getId() + " - " + getRfcNumber();
	StringBuilder message = new StringBuilder();
	message.append(getTargetSystem() + " - " + getImpPlan().getId() + " - " + getRfcNumber()).append("<br><br>");

	message.append("Project Number & Project Name: ").append(getImpPlan().getProjectId().getProjectNumber() + " - " + getImpPlan().getProjectId().getProjectName()).append("<br><br>");
	message.append(" Impl Plan Description: ").append(getImpPlan().getPlanDesc()).append("<br><br>");
	if (getDeveloperName() != null && !getDeveloperName().isEmpty()) {
	    message.append("Developer: ").append(getDeveloperName() + "/ ");
	}
	message.append("DevManager: ").append(getImpPlan().getDevManagerName()).append("<br><br>");
	message.append("Load Date/Time: ").append(DateHelper.convertGMTtoEST(getLoadDateTime()));

	this.setSubject(subject);
	this.setMessage(message.toString());
    }

    public ImpPlan getImpPlan() {
	return impPlan;
    }

    public void setImpPlan(ImpPlan impPlan) {
	this.impPlan = impPlan;
    }

    public String getDeveloperName() {
	return developerName;
    }

    public void setDeveloperName(String developerName) {
	this.developerName = developerName;
    }

}
