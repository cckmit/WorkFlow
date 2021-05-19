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
 * @author Radha.Adhimoolam
 */
public class QATestingStatusActivityMessage extends ActivityLogMessage {

    private String systemName;
    private String qaPhaseName;

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public String getQaPhaseName() {
	return qaPhaseName;
    }

    public void setQaPhaseName(String qaPhaseName) {
	this.qaPhaseName = qaPhaseName;
    }

    public QATestingStatusActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	return getQaPhaseName() + " testing has been ByPassed for System " + getSystemName();
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
