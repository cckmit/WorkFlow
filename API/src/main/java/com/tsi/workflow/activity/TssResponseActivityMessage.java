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

public class TssResponseActivityMessage extends ActivityLogMessage {

    Priority lPriority;
    String systemName;
    String vparName;
    String loadStatus;
    String env;

    public TssResponseActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public String getVparName() {
	return vparName;
    }

    public void setVparName(String vparName) {
	this.vparName = vparName;
    }

    public String getLoadStatus() {
	return loadStatus;
    }

    public void setLoadStatus(String loadStatus) {
	this.loadStatus = loadStatus;
    }

    public String getEnv() {
	return env;
    }

    public void setEnv(String env) {
	this.env = env;
    }

    @Override
    public String processMessage() {
	String lMessage = "";
	lPriority = Priority.INFO;
	lMessage = user.getCurrentRole() + " " + user.getDisplayName() + " has accepted the plan  " + impPlan.getId() + " for QA " + getEnv() + " testing in " + getVparName();
	return lMessage;

    }

    @Override
    public Priority getLogLevel() {
	return lPriority;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
