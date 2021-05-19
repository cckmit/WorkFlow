package com.tsi.workflow.activity;

import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.System;
import java.text.MessageFormat;
import org.apache.log4j.Priority;

public class DevManagerActivityMessage extends ActivityLogMessage {

    System system;
    String qaFunctionalComment;
    String qaRegressionComment;
    boolean isCommentPresent;
    String errorMessage;

    public System getSystem() {
	return system;
    }

    public void setSystem(System system) {
	this.system = system;
    }

    public String getQaFunctionalComment() {
	return qaFunctionalComment;
    }

    public void setQaFunctionalComment(String qaFunctionalComment) {
	this.qaFunctionalComment = qaFunctionalComment;
    }

    public String getQaRegressionComment() {
	return qaRegressionComment;
    }

    public void setQaRegressionComment(String qaRegressionComment) {
	this.qaRegressionComment = qaRegressionComment;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public DevManagerActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public DevManagerActivityMessage(ImpPlan impPlan, Implementation implementation, IBeans... beans) {
	super(impPlan, implementation, beans);
    }

    @Override
    public String processMessage() {
	String Message = "Lead {0} has initiated submit for the implementation plan {1}";

	if (system != null) {
	    Message = Message + ", system {2}";
	}
	if (qaFunctionalComment != null && !qaFunctionalComment.isEmpty()) {
	    Message = Message.concat(" with QA Functional Comments \"").concat(qaFunctionalComment).concat("\"");
	    isCommentPresent = true;
	}
	if (qaRegressionComment != null && !qaRegressionComment.isEmpty()) {
	    if (isCommentPresent) {
		Message = Message.concat(", QA Regression Comments \"").concat(qaRegressionComment).concat("\"");
	    } else {
		Message = Message.concat(" with QA Regression Comments \"").concat(qaRegressionComment).concat("\"");
	    }
	}
	if (user.getCurrentDelegatedUser() != null) {
	    Message = Message + ", on behalf of " + user.getCurrentDelegatedUser().getDisplayName();
	}

	if (system == null) {
	    return MessageFormat.format(Message, user.getDisplayName(), impPlan.getId());
	} else {
	    return MessageFormat.format(Message, user.getDisplayName(), impPlan.getId(), system.getName());
	}
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
	system = (System) beans[0];
    }

}
