/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.utils.Constants;
import java.text.MessageFormat;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class PutDateChangeMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    PutLevel putBeforeUpdate;
    PutLevel putAfterUpdate;
    ImpPlan plan;

    public PutLevel getPutBeforeUpdate() {
	return putBeforeUpdate;
    }

    public void setPutBeforeUpdate(PutLevel putBeforeUpdate) {
	this.putBeforeUpdate = putBeforeUpdate;
    }

    public PutLevel getPutAfterUpdate() {
	return putAfterUpdate;
    }

    public void setPutAfterUpdate(PutLevel putAfterUpdate) {
	this.putAfterUpdate = putAfterUpdate;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    @Override
    public void processMessage() {
	String previousPutDate = Constants.APP_DATE_TIME_FORMAT.get().format(putBeforeUpdate.getPutDateTime());
	String currentPutDate = Constants.APP_DATE_TIME_FORMAT.get().format(putAfterUpdate.getPutDateTime());
	String lMessage = MessageFormat.format("{0} deployment for {1} system has been changed from {2} to {3}.<br/>" + "Kindly change the load date of implementation plan {4} for System - {1} to the date after PUT level date {3}", putAfterUpdate.getPutLevel(), putBeforeUpdate.getSystemId().getName(), previousPutDate, currentPutDate, plan.getId());
	this.addToAddressUserId(plan.getLeadId(), Constants.MailSenderRole.LEAD);
	this.setSubject("Put Level deployment change");
	this.setMessage(lMessage);
    }
}
