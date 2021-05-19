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
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Priority;

/**
 *
 * @author Vinoth
 */
public class QAFunTesterAssignReAssignActivityMessage extends ActivityLogMessage {

    private String action;
    private List<String> qaFunTesterList = new ArrayList<>();

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public List<String> getQaFunTesterList() {
	return qaFunTesterList;
    }

    public void setQaFunTesterList(List<String> qaFunTesterList) {
	this.qaFunTesterList = qaFunTesterList;
    }

    public QAFunTesterAssignReAssignActivityMessage(ImpPlan impPlan, Implementation imp) {
	super(impPlan, imp);
    }

    @Override
    public String processMessage() {
	StringBuilder lMessage = new StringBuilder();

	String lAdded = " from";
	if (getAction().equalsIgnoreCase("added")) {
	    lAdded = " for";
	}
	lMessage.append(user.getCurrentRole()).append(" ").append(user.getDisplayName()).append(" has ").append(getAction()).append(" QA Functional Tester - ").append(String.join(",", getQaFunTesterList())).append(lAdded).append(" the implementation plan - ").append(impPlan.getId());

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
