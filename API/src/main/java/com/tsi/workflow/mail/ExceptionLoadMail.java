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
 * @author
 */
public class ExceptionLoadMail extends MailMessage {

    private String planId;
    private String status;
    private String planDetails;

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public void setPlanDetails(String planDetails) {
	this.planDetails = planDetails;
    }

    @Override
    public void processMessage() {
	String message = MessageFormat.format("Source Artifact : [{0}] belonging to implementation plan <b> {1} </b> " + "has been {2} for exception load. <br>" + "Please sync up your codes with the latest.", planDetails, planId, status);
	this.setMessage(message);
    }

}
