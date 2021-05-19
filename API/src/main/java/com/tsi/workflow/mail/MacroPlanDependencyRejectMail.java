package com.tsi.workflow.mail;

import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class MacroPlanDependencyRejectMail extends MailMessage {

    private static final Logger LOG = Logger.getLogger(MacroPlanDependencyRejectMail.class.getName());
    private String message;
    private String planId;
    private List<String> dependencyPlanMsg;
    private String actionType;
    private List<String> mailIds = new ArrayList<>();

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public List<String> getDependencyPlanMsg() {
	return dependencyPlanMsg;
    }

    public void setDependencyPlanMsg(List<String> dependencyPlanMsg) {
	this.dependencyPlanMsg = dependencyPlanMsg;
    }

    @Override
    public void processMessage() {
	StringBuilder sb = new StringBuilder();
	sb.append("The following implementation plans may have been affected by the macro/header from plan ").append(getPlanId());
	getDependencyPlanMsg().forEach(dependentMsg -> {
	    sb.append("<br><br>").append(dependentMsg);
	});

	LOG.info("Mail message: " + sb.toString());
	getMailIds().forEach(mailId -> {
	    this.addToAddressUserId(mailId, Constants.MailSenderRole.DEVELOPER);
	});
	this.setMessage(sb.toString());
	this.setSubject("Builds that were done against the macro/header from " + getPlanId());
    }

    public String getActionType() {
	return actionType;
    }

    public void setActionType(String actionType) {
	this.actionType = actionType;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public List<String> getMailIds() {
	return mailIds;
    }

    public void setMailIds(List<String> mailIds) {
	this.mailIds = mailIds;
    }

}
