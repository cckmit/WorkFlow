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
public class SetReadyForQAActivityMessage extends ActivityLogMessage {

    public SetReadyForQAActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public String processMessage() {
	if (user.getCurrentDelegatedUser() == null) {
	    return MessageFormat.format("{0} {1} has marked the implementation {2} as ready for QA", user.getCurrentRole(), user.getDisplayName(), implementation.getId());
	} else {
	    return MessageFormat.format("{0} {1} has marked the implementation {2} as ready for QA on behalf of {3}", user.getCurrentDelegatedUser().getCurrentRole(), user.getDisplayName(), implementation.getId(), user.getCurrentDelegatedUser().getDisplayName());
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
