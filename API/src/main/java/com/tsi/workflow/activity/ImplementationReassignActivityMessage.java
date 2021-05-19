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
 * @author user
 */
public class ImplementationReassignActivityMessage extends ActivityLogMessage {

    String oldUser;

    public void setOldUser(String oldUser) {
	this.oldUser = oldUser;
    }

    public ImplementationReassignActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	if (user.getCurrentDelegatedUser() == null) {
	    return MessageFormat.format("{0} has reassigned the implementation {1} from {2} to {3}", user.getDisplayName(), implementation.getId(), oldUser, implementation.getDevName());
	} else {
	    return MessageFormat.format("{0} has reassigned the implementation {1} from {2} to {3} on behalf of {4}", user.getDisplayName(), implementation.getId(), oldUser, implementation.getDevName(), user.getCurrentDelegatedUser().getDisplayName());
	}
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
