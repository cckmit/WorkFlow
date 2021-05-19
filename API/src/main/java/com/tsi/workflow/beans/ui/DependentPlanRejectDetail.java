package com.tsi.workflow.beans.ui;

import com.tsi.workflow.User;
import java.io.Serializable;

public class DependentPlanRejectDetail implements Serializable {

    private static final long serialVersionUID = -3032034971814265315L;
    private User user;
    private String planId;
    private String rejectReason;
    private String autoRejectReason;

    public DependentPlanRejectDetail(User currentUser, String planId, String rejectReason, String autoRejectReason) {
	this.user = currentUser;
	this.planId = planId;
	this.rejectReason = rejectReason;
	this.autoRejectReason = autoRejectReason;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User currentUser) {
	this.user = currentUser;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getRejectReason() {
	return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
	this.rejectReason = rejectReason;
    }

    public String getAutoRejectReason() {
	return autoRejectReason + " " + planId;
    }

    public void setAutoRejectReason(String autoRejectReason) {
	this.autoRejectReason = autoRejectReason;
    }
}
