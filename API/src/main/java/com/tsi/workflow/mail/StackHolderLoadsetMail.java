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
import java.util.Set;

/**
 *
 * @author gn.Ebinezardharmaraj
 */
public class StackHolderLoadsetMail extends MailMessage {

    private String planId;
    private String prodSystemName;
    private Date activationDateTime;
    private String status;
    private String cpuName = "ALL";

    private String planDescription;
    private Set<String> problemTicketSet;
    private String dbcr;
    private String targetSystems;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
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

    public String getCpuName() {
	return cpuName;
    }

    public void setCpuName(String cpuName) {
	this.cpuName = cpuName;
    }

    public String getPlanDescription() {
	return planDescription;
    }

    public void setPlanDescription(String planDescription) {
	this.planDescription = planDescription;
    }

    public Set<String> getProblemTicketSet() {
	return problemTicketSet;
    }

    public void setProblemTicketSet(Set<String> problemTicketSet) {
	this.problemTicketSet = problemTicketSet;
    }

    public String getDbcr() {
	return dbcr;
    }

    public void setDbcr(String dbcr) {
	this.dbcr = dbcr;
    }

    public String getTargetSystems() {
	return targetSystems;
    }

    public void setTargetSystems(String targetSystems) {
	this.targetSystems = targetSystems;
    }

    @Override
    public void processMessage() {
	StringBuffer subject = new StringBuffer();
	subject.append(planId);
	subject.append(":");
	subject.append(status);
	subject.append(" in ");
	subject.append(prodSystemName);
	StringBuffer message = new StringBuffer();
	message.append(planId);
	message.append(":");
	message.append(status);
	message.append(" in ");
	message.append(prodSystemName);
	message.append(" ");
	subject.append(" ");
	if (status.equals(Constants.LOAD_SET_STATUS.ACTIVATED.name())) {
	    message.append(" on ");
	    subject.append(" on ");
	}
	subject.append(cpuName);
	message.append(cpuName);
	message.append(" at ");
	message.append(DateHelper.convertGMTtoEST(activationDateTime));
	subject.append(" at ");
	subject.append(DateHelper.convertGMTtoEST(activationDateTime));
	message.append("<br><br>  Description of the Implementation Plan:");
	message.append(planDescription);
	if (problemTicketSet != null && !problemTicketSet.isEmpty()) {
	    message.append("<br> PR #(s) ");
	    message.append(problemTicketSet);
	}
	message.append("<br> Target System:");
	message.append(targetSystems);
	if (dbcr != null && !dbcr.isEmpty()) {
	    message.append("<br> DBCR(s):");
	    message.append(dbcr);
	}
	this.setMessage(message.toString());
	this.setSubject(subject.toString());
    }

}
