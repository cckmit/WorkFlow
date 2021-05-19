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
 * @author gn.ebinezardharmaraj
 */
public class DbcrActivityMessage extends ActivityLogMessage {

    DbcrActivityMessage() {

    }

    public DbcrActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    private String impPlanId;
    private String createdBy;
    private String dbcrName;
    private String action;
    private String targetSystem;

    public String getImpPlanId() {
	return impPlanId;
    }

    public void setImpPlanId(String impPlanId) {
	this.impPlanId = impPlanId;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public String getDbcrName() {
	return dbcrName;
    }

    public void setDbcrName(String dbcrName) {
	this.dbcrName = dbcrName;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    @Override
    public String processMessage() {
	StringBuilder activityMessage = new StringBuilder();
	activityMessage.append(createdBy);
	activityMessage.append(" has ");
	activityMessage.append(action);
	activityMessage.append(" DBCR(s)");
	activityMessage.append(dbcrName);
	activityMessage.append(" for target System ");
	activityMessage.append(targetSystem);

	return activityMessage.toString();
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

}
