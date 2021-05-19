package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class RfcFieldsAlertMail extends MailMessage {

    private String planId;
    private String loadDateTime;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getLoadDateTime() {
	String dateTime = "";
	try {
	    dateTime = DateHelper.convertGMTtoEST(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(loadDateTime));
	} catch (ParseException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return dateTime;
    }

    public void setLoadDateTime(String loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    @Override
    public void processMessage() {
	String subject = "REMINDER!! Please update the RFC process tab for Implementation Plan " + getPlanId();
	StringBuffer message = new StringBuffer();
	message.append("Please complete the RFC fields as required and attach the business approval and test script for the Implementation plan");

	message.append(" " + getPlanId() + " which has the load date time of " + getLoadDateTime()).append("<br><br>");

	message.append("If these requirements cannot be met, then please reschedule your implementation plan (change the load date/time).");
	this.setMessage(message.toString());
	this.setSubject(subject);

    }

}
