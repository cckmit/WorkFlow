package com.tsi.workflow.mail;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class DSLFilePopulateFailureMail extends MailMessage {
    @Autowired
    LDAPAuthenticatorImpl authenticator;
    private static final Logger LOG = Logger.getLogger(DSLFilePopulateFailureMail.class.getName());
    @Autowired
    WFConfig wFConfig;

    private String planId;
    private String loadSetName;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getLoadSetName() {
	return loadSetName;
    }

    public void setLoadSetName(String loadSetName) {
	this.loadSetName = loadSetName;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    @Override
    public void processMessage() {
	StringBuilder lMessage = new StringBuilder();
	lMessage.append("Unable to add WSP  loadset ").append(getLoadSetName() + " for plan ").append(getPlanId() + " to LTQA load deck. Kindly resolve the issue by manually recreating the QA load deck.");
	String subject = "LTQA load deck creation failed";
	this.addToDEVCentre();
	this.setMessage(lMessage.toString());
	this.setSubject(subject);
    }

}
