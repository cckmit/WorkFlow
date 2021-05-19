/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.User;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Radha.Adhimoolam
 */
public class RejectPlans {

    private User user;
    private Map<String, Date> dependentPlanIds = new TreeMap();
    private Boolean isReject = Boolean.TRUE;
    private Boolean isFallback = Boolean.FALSE;

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Map<String, Date> getDependentPlanIds() {
	return dependentPlanIds;
    }

    public void setDependentPlanIds(Map<String, Date> dependentPlanIds) {
	this.dependentPlanIds = dependentPlanIds;
    }

    public Boolean getIsReject() {
	return isReject;
    }

    public void setIsReject(Boolean isReject) {
	this.isReject = isReject;
    }

    public Boolean getIsFallback() {
	return isFallback;
    }

    public void setIsFallback(Boolean isFallback) {
	this.isFallback = isFallback;
    }

}
