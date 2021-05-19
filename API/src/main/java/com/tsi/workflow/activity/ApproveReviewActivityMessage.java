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
public class ApproveReviewActivityMessage extends ActivityLogMessage {

    boolean reviewRequest = false;

    public ApproveReviewActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public void setReviewRequest(boolean reviewRequest) {
	this.reviewRequest = reviewRequest;
    }

    @Override
    public String processMessage() {
	if (reviewRequest) {
	    return MessageFormat.format("{0} {1} has requested peer review for implementation {2}.", user.getCurrentRole(), user.getDisplayName(), implementation.getId());
	} else {
	    if (user.getCurrentDelegatedUser() == null) {
		return MessageFormat.format("{0} {1} has completed peer review process for implementation {2}.", user.getCurrentRole(), user.getDisplayName(), implementation.getId());
	    } else {
		return MessageFormat.format("{0} {1} has marked the Implementation {2} as Peer Review Completed on behalf of {3}", user.getCurrentDelegatedUser().getCurrentRole(), user.getDisplayName(), implementation.getId(), user.getCurrentDelegatedUser().getDisplayName());
	    }
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
