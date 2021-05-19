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
public class CommitActivityMessage extends ActivityLogMessage {

    String comments;
    boolean commit = false;

    public void setCommit(boolean commit) {
	this.commit = commit;
    }

    public void setComments(String comments) {
	this.comments = comments;
    }

    public CommitActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

    @Override
    public String processMessage() {
	if (commit) {
	    return MessageFormat.format("{0} {1} has initiated local commit for implementation {2}. {3}", user.getCurrentRole(), user.getDisplayName(), implementation.getId(), comments);
	} else {
	    return MessageFormat.format("{0} {1} has initiated import workspace for implementation {2}. {3}", user.getCurrentRole(), user.getDisplayName(), implementation.getId(), comments);
	}
    }

    @Override
    public Priority getLogLevel() {
	return Priority.ERROR;
    }

}
