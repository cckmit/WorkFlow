/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;

/**
 *
 * @author gn.ebinezardharmaraj
 */
public class LoadDatePassedMacroPlan extends MailMessage {
    private String impPlan;
    private Date loadDate;

    public String getImpPlan() {
	return impPlan;
    }

    public void setImpPlan(String impPlan) {
	this.impPlan = impPlan;
    }

    public Date getLoadDate() {
	return loadDate;
    }

    public void setLoadDate(Date loadDate) {
	this.loadDate = loadDate;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	String subject = "  Load Date Reached for  " + getImpPlan();
	message.append(" <b> Macro/header/Include </b> ").append(" - only implementation plan ").append(getImpPlan()).append(" is ready to be marked as ONLINE. ").append("  Please provide your approval as soon as Load date/Time ").append(DateHelper.convertGMTtoEST(getLoadDate())).append(" reached.");
	this.setSubject(subject);
	this.setMessage(message.toString());
    }
}
