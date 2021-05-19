/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PutLevel;

/**
 *
 * @author Radha.Adhimoolam
 */
public class RejectionOnPutUpgradeMail extends MailMessage {

    private ImpPlan plan;
    private PutLevel backUpPut;
    private PutLevel prodPutLevel;

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public PutLevel getBackUpPut() {
	return backUpPut;
    }

    public void setBackUpPut(PutLevel backUpPut) {
	this.backUpPut = backUpPut;
    }

    public PutLevel getProdPutLevel() {
	return prodPutLevel;
    }

    public void setProdPutLevel(PutLevel prodPutLevel) {
	this.prodPutLevel = prodPutLevel;
    }

    @Override
    public void processMessage() {
	String lSubject = "Plan - " + plan.getId() + " rejected due to PUT level upgrade for System - " + backUpPut.getSystemId().getName();
	StringBuilder lMessage = new StringBuilder();
	lMessage.append("Plan - ").append(plan.getId()).append(" was rejected as it had IBM source artifacts belonging to an older PUT level - ").append(backUpPut.getPutLevel());
	lMessage.append("<br>Default PUT level for system - ").append(backUpPut.getSystemId().getName()).append(" was changed from ").append(backUpPut.getPutLevel()).append(" to ").append(prodPutLevel.getPutLevel());
	lMessage.append("<br><br>Plan Id - ").append(plan.getId());
	lMessage.append("<br> Plan Description: ").append(plan.getPlanDesc());

	this.setSubject(lSubject);
	this.setMessage(lMessage.toString());
    }

}
