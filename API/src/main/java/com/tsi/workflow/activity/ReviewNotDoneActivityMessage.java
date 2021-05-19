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
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Priority;

/**
 *
 * @author USER
 */
public class ReviewNotDoneActivityMessage extends ActivityLogMessage {

    boolean reviewNotDoneRequest = false;
    private List<String> lReviewerNotDoneList = new ArrayList<>();

    public List<String> getlReviewerNotDoneList() {
	return lReviewerNotDoneList;
    }

    public void setlReviewerNotDoneList(List<String> lReviewerNotDoneList) {
	this.lReviewerNotDoneList = lReviewerNotDoneList;
    }

    public ReviewNotDoneActivityMessage(ImpPlan impPlan, Implementation implementation) {
	super(impPlan, implementation);
    }

    public boolean isReviewNotDoneRequest() {
	return reviewNotDoneRequest;
    }

    public void setReviewNotDoneRequest(boolean reviewNotDoneRequest) {
	this.reviewNotDoneRequest = reviewNotDoneRequest;
    }

    @Override
    public String processMessage() {
	if (isReviewNotDoneRequest()) {
	    return "Reviewers " + String.join(",", getlReviewerNotDoneList()) + " have to mark the implementation  " + implementation.getId() + " as Peer Review Completed";
	}
	return org.apache.commons.lang.StringUtils.EMPTY;
    }

    @Override
    public Priority getLogLevel() {
	return Priority.INFO;
    }

    @Override
    public void setArguments(IBeans... beans) {
    }

}
