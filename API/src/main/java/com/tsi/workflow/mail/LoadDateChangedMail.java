/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.DateHelper;
import java.text.MessageFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author User
 */
public class LoadDateChangedMail extends MailMessage {

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    private String planId;
    private String toPlanId;
    private String planLoadType;
    private String developerId;
    private String leadId;
    private String system;
    private Date beforeUpdate;
    private Date afterUpdate;

    public void setPlanLoadType(String planLoadType) {
	this.planLoadType = planLoadType;
    }

    public void setToPlanId(String toPlanId) {
	this.toPlanId = toPlanId;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return authenticator;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public void setDeveloperId(String developerId) {
	this.developerId = developerId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    public void setSystem(String system) {
	this.system = system;
    }

    public void setBeforeUpdate(Date beforeUpdate) {
	this.beforeUpdate = beforeUpdate;
    }

    public void setAfterUpdate(Date afterUpdate) {
	this.afterUpdate = afterUpdate;
    }

    @Override
    public void processMessage() {
	String stringPreviousDate = null;
	String stringCurrentDate = null;
	if (beforeUpdate != null) {
	    try {
		stringPreviousDate = DateHelper.convertGMTtoEST(beforeUpdate);
	    } catch (Exception e) {
	    }
	}
	try {
	    stringCurrentDate = DateHelper.convertGMTtoEST(afterUpdate);
	} catch (Exception e) {
	}
	String message = MessageFormat.format("Load date changed for {0} from {1} to {2} for target system {3}. Please sync up your {4} source code with the latest from {0}.", planId, stringPreviousDate, stringCurrentDate, system, toPlanId);

	String subject = MessageFormat.format("{0} : Resolve {1} source contention with the {2} Load {3}", toPlanId, system, planLoadType, planId);

	this.addToAddressUserId(developerId, Constants.MailSenderRole.DEVELOPER);
	this.addcCAddressUserId(leadId, Constants.MailSenderRole.LEAD);
	this.setMessage(message);
	this.setSubject(subject);
    }

}
