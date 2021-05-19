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
import java.text.MessageFormat;
import org.apache.log4j.Priority;

public class DbcrValidationMessage extends ActivityLogMessage {

    private String dbcrName;
    private String environment;
    private String systemName;

    public void setDbcrName(String dbcrName) {
	this.dbcrName = dbcrName;
    }

    public void setEnvironment(String environment) {
	this.environment = environment;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public DbcrValidationMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	return MessageFormat.format("Associated DBCR \"{0}\" for \"{1}\" needs to be applied in \"{2}\" before deploying loadset.", dbcrName, systemName, environment);
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
