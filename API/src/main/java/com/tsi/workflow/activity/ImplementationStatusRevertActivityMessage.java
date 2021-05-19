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

public class ImplementationStatusRevertActivityMessage extends ActivityLogMessage {

    String status;

    public void setStatus(String status) {
	this.status = status;
    }

    public ImplementationStatusRevertActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	if (user.getCurrentDelegatedUser() == null) {
	    return MessageFormat.format("{0} {1} has reverted the status of implementation {2} as {3}", user.getCurrentRole(), user.getDisplayName(), implementation.getId(), status);
	} else {
	    return MessageFormat.format("{0} {1} has reverted the status of implementation {2} as {4} on behalf of {3}", user.getCurrentDelegatedUser().getCurrentRole(), user.getDisplayName(), implementation.getId(), user.getCurrentDelegatedUser().getDisplayName(), status);
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
