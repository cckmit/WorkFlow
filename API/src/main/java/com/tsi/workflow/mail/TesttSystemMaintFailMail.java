/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import java.text.MessageFormat;

/**
 *
 * @author User
 */
public class TesttSystemMaintFailMail extends MailMessage {

    private String systemName;

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Unable to execute the script for the system - {0}", systemName);
	String subject = "TPF Test System Maintenance Fails";
	this.setMessage(message);
	this.setSubject(subject);
    }

}
