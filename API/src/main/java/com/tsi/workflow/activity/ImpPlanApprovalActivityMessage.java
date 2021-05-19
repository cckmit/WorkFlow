/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;

/**
 *
 * @author Vinoth
 */
public class ImpPlanApprovalActivityMessage extends ActivityLogMessage {

    private String action;
    private String approvalComment;
    private String fileName;
    private String oldComment;

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public ImpPlanApprovalActivityMessage(ImpPlan impPlan, Implementation imp) {
	super(impPlan, imp);
    }

    public String getApprovalComment() {
	return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
	this.approvalComment = approvalComment;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getOldComment() {
	return oldComment;
    }

    public void setOldComment(String oldComment) {
	this.oldComment = oldComment;
    }

    @Override
    public String processMessage() {
	StringBuilder lMessage = new StringBuilder();

	String laction = "";
	if (getAction().equals("added")) {
	    laction = "added";
	} else if (getAction().equals("updated")) {
	    laction = "updated";
	} else if (getAction().equals("deleted")) {
	    laction = "deleted";
	} else {
	    laction = "";
	}

	if (getOldComment() != null) {
	    lMessage.append(user.getCurrentRole()).append(" ").append(user.getDisplayName()).append(" has deleted Approval document ").append(getFileName()).append(" with the comments ").append(getOldComment());
	} else if (getFileName() != null) {
	    lMessage.append(user.getCurrentRole()).append(" ").append(user.getDisplayName()).append(" has uploaded Approval document ").append(getFileName()).append(" with the comments ").append(getApprovalComment());
	} else {
	    lMessage.append(user.getCurrentRole()).append(" ").append(user.getDisplayName()).append(" has ").append(laction).append(" the Approval comment ").append(getApprovalComment());
	}

	if (user.getCurrentDelegatedUser() != null) {
	    lMessage.append(", on behalf of ").append(user.getCurrentDelegatedUser().getDisplayName());
	}
	return lMessage.toString();
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
