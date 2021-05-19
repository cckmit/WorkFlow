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
 * @author USER
 */
public class ExceptionOccurYodaActivityMessage extends ActivityLogMessage {

    private String planId;
    private List<String> vparList = new ArrayList<>();
    private String loadSetName;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public List<String> getVparList() {
	return vparList;
    }

    public void setVparList(List<String> vparList) {
	this.vparList = vparList;
    }

    public String getLoadSetName() {
	return loadSetName;
    }

    public void setLoadSetName(String loadSetName) {
	this.loadSetName = loadSetName;
    }

    public ExceptionOccurYodaActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    @Override
    public String processMessage() {
	String lActivityMessage = "";
	lActivityMessage = user.getCurrentRole() + " " + user.getDisplayName() + " Plan " + getPlanId() + "  Deactive and delete loadset following vpars " + getVparList() + " and LoadSet Name " + getLoadSetName() + "  we are getting the exception .";
	if (user.getCurrentDelegatedUser() != null) {
	    lActivityMessage = lActivityMessage + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}
	return lActivityMessage;
    }

}
