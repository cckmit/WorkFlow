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

/**
 *
 * @author USER
 */
public class PlanSubmitActivityMessage extends ActivityLogMessage {

    public PlanSubmitActivityMessage(ImpPlan impPlan, Implementation implementation) {
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
	if (user.getCurrentDelegatedUser() == null) {
	    return MessageFormat.format("{0} {1} has submitted the implementation plan {2}", user.getCurrentRole(), user.getDisplayName(), impPlan.getId());
	} else {
	    return MessageFormat.format("{0} {1} has submitted the implementation plan {2} on behalf of {3}", user.getCurrentDelegatedUser().getCurrentRole(), user.getDisplayName(), impPlan.getId(), user.getCurrentDelegatedUser().getDisplayName());
	}

    }

}
