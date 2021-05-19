/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.utils.DateHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author User
 */
public class DeletePlanMail extends MailMessage {

    private ImpPlan lPlan;
    private List<Implementation> lImp = new ArrayList();
    private String deletedBy;

    public ImpPlan getlPlan() {
	return lPlan;
    }

    public void setlPlan(ImpPlan lPlan) {
	this.lPlan = lPlan;
    }

    public List<Implementation> getlImp() {
	return lImp;
    }

    public void setlImp(List<Implementation> lImp) {
	this.lImp = lImp;
    }

    public String getDeletedBy() {
	return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
	this.deletedBy = deletedBy;
    }

    @Override
    public void processMessage() {

	String subject = "Implementation Plan - " + getlPlan().getId() + " has been deleted";
	StringBuilder message = new StringBuilder();
	String convertGMTtoEST = "";

	convertGMTtoEST = DateHelper.convertGMTtoEST(new Date());
	// Addition to TO address
	message.append("Implementation Plan ").append(getlPlan().getId()).append(" has been deleted by ").append(getDeletedBy()).append(" on ").append(convertGMTtoEST).append("</br></br>Implementation Plan Description: ").append(getlPlan().getPlanDesc());

	for (Implementation lImp : getlImp()) {
	    message.append("</br>    - Implementation Description: ").append(lImp.getImpDesc());

	}
	this.setMessage(message.toString());
	this.setSubject(subject);
    }

}
