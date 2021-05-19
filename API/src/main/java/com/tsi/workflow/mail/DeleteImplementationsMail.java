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
import java.util.Date;

/**
 *
 * @author User
 */
public class DeleteImplementationsMail extends MailMessage {

    private ImpPlan lPlan;
    private Implementation lImp;
    private String deletedBy;

    public ImpPlan getlPlan() {
	return lPlan;
    }

    public void setlPlan(ImpPlan lPlan) {
	this.lPlan = lPlan;
    }

    public Implementation getlImp() {
	return lImp;
    }

    public void setlImp(Implementation lImp) {
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

	String subject = "Implementation " + getlImp().getId() + " has been deleted";
	StringBuilder message = new StringBuilder();
	String convertGMTtoEST = "";

	convertGMTtoEST = DateHelper.convertGMTtoEST(new Date());

	message.append("Implementation ").append(getlImp().getId()).append(" has been deleted by ").append(getDeletedBy()).append(" on ").append(convertGMTtoEST).append("</br></br>Implementation Plan Description: ").append(getlPlan().getPlanDesc()).append("</br>   - Implementation Description: ").append(getlImp().getImpDesc());

	this.setMessage(message.toString());
	this.setSubject(subject);
    }
}
