/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;

/**
 *
 * @author vamsi.krishnarao
 */
public class MNFExecutionMail extends MailMessage {
    private String systemName;
    private String info;
    private String key;

    public String getKey() {
	return key;
    }

    public void setKey(String key) {
	this.key = key;
    }

    public String getInfo() {
	return info;
    }

    public void setInfo(String info) {
	this.info = info;
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    @Override
    public void processMessage() {

	String message = null;
	String subject = null;

	if (key.equals(Constants.GenericMailKey.NFM_FAIL_MAIL.name())) {
	    message = MessageFormat.format("TPF test system maintenance for system - {0} has failed, Please start manual process. " + "\n" + "{1}", systemName, info);
	    subject = "TPF Test System Maintenance Fails";
	} else if (key.equals(Constants.GenericMailKey.NFM_SUCESS_MAIL.name())) {
	    message = MessageFormat.format("TPF test system maintenance for system - {0} is successful. " + "\n" + "{1}", systemName, info);
	    subject = "TPF Test System Maintenance Successful";
	} else if (key.equals(Constants.GenericMailKey.NFM_ALREADY_RUNNING_MAIL.name())) {
	    message = MessageFormat.format("TPF test system maintenance for system - {0} is already running. " + "\n" + "{1}", systemName, info);
	    subject = "TPF Test System Maintenance Already Runnig";
	}

	this.setMessage(message);
	this.setSubject(subject);
    }
}
