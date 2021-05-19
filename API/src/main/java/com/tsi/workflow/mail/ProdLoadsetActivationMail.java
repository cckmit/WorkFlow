/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ProdLoadsetActivationMail extends MailMessage {

    private String planId;
    private String loadsetName;
    private String prodSystemName;
    private Date activationDateTime;
    private String status;
    private Boolean fallbackActivity = Boolean.FALSE;
    private String cpuName = "ALL";
    private String planDesc;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getLoadsetName() {
	return loadsetName;
    }

    public void setLoadsetName(String loadsetName) {
	this.loadsetName = loadsetName;
    }

    public String getProdSystemName() {
	return prodSystemName;
    }

    public void setProdSystemName(String prodSystemName) {
	this.prodSystemName = prodSystemName;
    }

    public Date getActivationDateTime() {
	return activationDateTime;
    }

    public void setActivationDateTime(Date activationDateTime) {
	this.activationDateTime = activationDateTime;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Boolean getFallbackActivity() {
	return fallbackActivity;
    }

    public void setFallbackActivity(Boolean fallbackActivity) {
	this.fallbackActivity = fallbackActivity;
    }

    public String getCpuName() {
	return cpuName;
    }

    public void setCpuName(String cpuName) {
	this.cpuName = cpuName;
    }

    public String getPlanDesc() {
	return planDesc;
    }

    public void setPlanDesc(String planDesc) {
	this.planDesc = planDesc;
    }

    @Override
    public void processMessage() {
	StringBuilder message = new StringBuilder();
	String subject = "";
	if (status.startsWith("FALLBACK")) {
	    message = message.append("Fallback ");
	    subject = subject + "Fallback ";
	    status = status.replace("FALLBACK_", "");
	}
	if (status.equals(Constants.LOAD_SET_STATUS.ACTIVATED.name()) || status.equals(Constants.LOAD_SET_STATUS.DEACTIVATED.name())) {
	    subject = subject + "Loadset of Plan - " + planId + " - " + status + " to Production " + prodSystemName + " System - CPU " + cpuName;
	    message.append("Loadset - ").append(loadsetName).append(" of implementation plan ").append(planId).append(" has been successfully ").append(status).append(" into the production ").append(prodSystemName).append(" system - CPU ").append(cpuName).append(" at ").append(DateHelper.convertGMTtoEST(activationDateTime));
	    message.append(" <br><br>").append(" Plan Description: ").append(getPlanDesc());
	} else {
	    subject = subject + "Loadset of Plan - " + planId + " - " + status + " to Production " + prodSystemName + " System";

	    message.append("Loadset - ").append(loadsetName).append(" of implementation plan ").append(planId).append(" has been successfully ").append(status).append(" on the production system ").append(prodSystemName).append(" at " + DateHelper.convertGMTtoEST(activationDateTime));
	    message.append(" <br><br>").append(" Plan Description: ").append(getPlanDesc());
	}
	this.setMessage(message.toString());
	this.setSubject(subject);
    }

}
