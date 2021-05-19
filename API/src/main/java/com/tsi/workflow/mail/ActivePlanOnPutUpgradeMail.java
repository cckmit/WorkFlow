/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PutLevel;
import java.text.MessageFormat;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ActivePlanOnPutUpgradeMail extends MailMessage {
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
	String lSubject = "Plan - " + plan.getId() + " needs action due to PUT level upgrade";

	String lMessage = MessageFormat.format("{0} has been upgraded to {1}. Since {2} had IBM source artifacts that were originally checked out from " + "{3} repository, developers need to do the below:" + "<ul>" + "<li>Create a new implementation within {2} eg., {2}_002</li>" + "<li>In new implementation {2}_002, check out {1} versions of the IBM code currently checked out from the {3} repository in {2}_001</li>" + "<li>Merge IBM code updates from {2}_001 into {2}_002</li>"
		+ "<li>Delete IBM code from {2}_001</li>" + "<li>Delete {2}_001 if it only has IBM source artifacts</li>" + "</ul>", backUpPut.getSystemId().getName(), prodPutLevel.getPutLevel(), plan.getId(), backUpPut.getPutLevel());
	this.setSubject(lSubject);
	this.setMessage(lMessage);
    }
}
