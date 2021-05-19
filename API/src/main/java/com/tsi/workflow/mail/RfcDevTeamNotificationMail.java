package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;

public class RfcDevTeamNotificationMail extends MailMessage {

    private String rfcNumber;
    private String targetSystem;
    private Date loadDateTime;
    private ImpPlan impPlan;
    private String developerName;
    private Boolean devFlagMailEnable;

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

    public Boolean getDevFlagMailEnable() {
	return devFlagMailEnable;
    }

    public void setDevFlagMailEnable(Boolean devFlagMailEnable) {
	this.devFlagMailEnable = devFlagMailEnable;
    }

    @Override
    public void processMessage() {
	String subject = null;
	StringBuilder message = new StringBuilder();
	if (getDevFlagMailEnable()) {
	    subject = "RFC Number Added/Updated for Implementation Plan - " + getImpPlan().getId();
	    message.append(getTargetSystem() + " - " + getImpPlan().getId() + " - " + getRfcNumber()).append("<br><br>");

	    message.append(" Project Number & Project Name: ").append(getImpPlan().getProjectId().getProjectNumber() + " - " + getImpPlan().getProjectId().getProjectName()).append("<br><br>");
	    message.append(" Impl Plan Description: ").append(getImpPlan().getPlanDesc()).append("<br><br>");
	    if (getDeveloperName() != null && !getDeveloperName().isEmpty()) {
		message.append(" Developer: ").append(getDeveloperName() + "/ ");
	    }
	    message.append(" DevManager: ").append(getImpPlan().getDevManagerName()).append("<br><br>");
	    message.append(" Load Date/Time: ").append(DateHelper.convertGMTtoEST(getLoadDateTime()));
	} else {

	    subject = "Implementation Plan " + getImpPlan().getId() + " scheduled for load on " + DateHelper.convertGMTtoEST(getLoadDateTime()) + " is ready for RFC processing for " + getTargetSystem();
	    message.append("Implementation Plan - " + getImpPlan().getId() + " scheduled for load on " + DateHelper.convertGMTtoEST(getLoadDateTime()) + " is ready for RFC processing for " + getTargetSystem()).append("<br><br>");

	    message.append(" Project  Number & Project Name: ").append(getImpPlan().getProjectId().getProjectNumber() + " - " + getImpPlan().getProjectId().getProjectName()).append("<br><br>");
	    message.append(" Impl Plan Description: ").append(getImpPlan().getPlanDesc()).append("<br><br>");
	    if (getDeveloperName() != null && !getDeveloperName().isEmpty()) {
		message.append(" Developer: ").append(getDeveloperName() + "/ ");
	    }
	    message.append(" DevManager: ").append(getImpPlan().getDevManagerName()).append("<br><br>");
	    message.append(" Load Date/Time: ").append(DateHelper.convertGMTtoEST(getLoadDateTime()));
	}
	this.setSubject(subject);
	this.setMessage(message.toString());
    }

}
