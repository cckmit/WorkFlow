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
public class ImplementationCreationActivityMessage extends ActivityLogMessage {

    public ImplementationCreationActivityMessage(ImpPlan impPlan, Implementation implementation) {
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
	    return MessageFormat.format("{1} has created the implementation {0} and assigned to developer {2}", implementation.getId(), user.getDisplayName(), implementation.getDevName());
	} else {
	    return MessageFormat.format("{1} has created the implementation {0} and assigned to developer {2} on behalf of {3}", implementation.getId(), user.getDisplayName(), implementation.getDevName(), user.getCurrentDelegatedUser().getDisplayName());
	}
    }
}
