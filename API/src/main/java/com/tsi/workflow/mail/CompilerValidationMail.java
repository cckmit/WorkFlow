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
public class CompilerValidationMail extends MailMessage {

    private String ipAddress;
    private String system;

    public String getIpAddress() {
	return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
	this.ipAddress = ipAddress;
    }

    public String getSystem() {
	return system;
    }

    public void setSystem(String system) {
	this.system = system;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Compiler Control table is not available for system - {0} and target system - {1}</br>", ipAddress, system);
	String subject = MessageFormat.format("Compiler Control table is not available for system - {0} and Target System - {1}", ipAddress, system);
	this.setMessage(message);
	this.setSubject(subject);
    }

}
