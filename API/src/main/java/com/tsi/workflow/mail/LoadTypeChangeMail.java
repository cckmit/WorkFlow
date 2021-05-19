/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.DateHelper;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Vinoth
 */
public class LoadTypeChangeMail extends MailMessage {

    private static final Logger LOG = Logger.getLogger(LoadTypeChangeMail.class.getName());

    private String planId;
    private String planDescrption;
    private Set<String> prNumberList;
    private Map<String, Date> newLoadDateTargetSys;
    private String loadType;
    private String loadTypeComment;
    private Map<String, Date> oldLoadDateTargetSys;

    @Autowired
    LDAPAuthenticatorImpl authenticator;

    User userDetails;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public User getUserDetails() {
	return userDetails;
    }

    public void setUserDetails(User userDetails) {
	this.userDetails = userDetails;
    }

    public String getPlanDescrption() {
	return planDescrption;
    }

    public void setPlanDescrption(String planDescrption) {
	this.planDescrption = planDescrption;
    }

    public Set<String> getPrNumberList() {
	return prNumberList;
    }

    public void setPrNumberList(Set<String> prNumberList) {
	this.prNumberList = prNumberList;
    }

    public Map<String, Date> getNewLoadDateTargetSys() {
	return newLoadDateTargetSys;
    }

    public void setNewLoadDateTargetSys(Map<String, Date> newLoadDateTargetSys) {
	this.newLoadDateTargetSys = newLoadDateTargetSys;
    }

    public String getLoadType() {
	return loadType;
    }

    public void setLoadType(String loadType) {
	this.loadType = loadType;
    }

    public String getLoadTypeComment() {
	return loadTypeComment;
    }

    public void setLoadTypeComment(String loadTypeComment) {
	this.loadTypeComment = loadTypeComment;
    }

    public Map<String, Date> getOldLoadDateTargetSys() {
	return oldLoadDateTargetSys;
    }

    public void setOldLoadDateTargetSys(Map<String, Date> oldLoadDateTargetSys) {
	this.oldLoadDateTargetSys = oldLoadDateTargetSys;
    }

    @Override
    public void processMessage() {

	StringBuilder message = new StringBuilder();
	this.setSubject(getPlanId() + " Load type changed to  " + getLoadType());
	message.append(getPlanId() + "  Load type changed to  " + getLoadType()).append("<br><br>");

	getOldLoadDateTargetSys().forEach((tarSys, oldLoadDate) -> {
	    if (oldLoadDate != null) {
		message.append(tarSys).append("  load date changed from  ").append(DateHelper.convertGMTtoEST(oldLoadDate)).append(" to ");
		getNewLoadDateTargetSys().forEach((tarSystem, newLoadDate) -> {
		    if (newLoadDate != null && tarSys.equals(tarSystem)) {
			message.append(DateHelper.convertGMTtoEST(newLoadDate)).append(" <br>");
		    }
		});
	    }
	});
	message.append("<br><br> Description of the Implementation Plan : ").append(getPlanDescrption()).append(".");
	if (!getPrNumberList().isEmpty() && getPrNumberList() != null && getPrNumberList().size() > 0) {
	    message.append("<br><br> PR Number : ").append(getPrNumberList()).append(".");
	}
	message.append("<br><br> Reason for changing the load type : ").append(getLoadTypeComment()).append(".").append("<br><br>");

	this.setMessage(message.toString());

    }
}
