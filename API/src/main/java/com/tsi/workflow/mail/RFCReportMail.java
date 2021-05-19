package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;

public class RFCReportMail extends MailMessage {

    private String reportStartDate;
    private String reportEndDate;

    public String getReportStartDate() {
	return reportStartDate;
    }

    public void setReportStartDate(String reportStartDate) {
	this.reportStartDate = reportStartDate;
    }

    public String getReportEndDate() {
	return reportEndDate;
    }

    public void setReportEndDate(String reportEndDate) {
	this.reportEndDate = reportEndDate;
    }

    @Override
    public void processMessage() {

	String subject = "Automated Mail with RFC report generated on " + getReportStartDate();

	String message = "Please find the attached automated report which has list of implementation plans with RFC details.";

	this.setSubject(subject);
	this.setMessage(message);

    }
}
