/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author USER
 */
public class ImpPlanView {

    ImpPlan plan;
    Boolean isSubmitReady;
    String isInProgress;
    Boolean isDeleteAllowed;
    Boolean isDeploymentFlag;
    Map<String, Integer> impDeleteStatus = new HashMap();

    public ImpPlanView() {
	isSubmitReady = false;
    }

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public Boolean getIsSubmitReady() {
	return isSubmitReady;
    }

    public void setIsSubmitReady(Boolean isSubmitReady) {
	this.isSubmitReady = isSubmitReady;
    }

    public void setIsInProgress(String metaData) {
	this.isInProgress = metaData;
    }

    public String getIsInProgress() {
	return isInProgress;
    }

    public Boolean getIsDeleteAllowed() {
	return isDeleteAllowed;
    }

    public void setIsDeleteAllowed(Boolean isDeleteAllowed) {
	this.isDeleteAllowed = isDeleteAllowed;
    }

    public Map<String, Integer> getImpDeleteStatus() {
	return impDeleteStatus;
    }

    public void setImpDeleteStatus(Map<String, Integer> impDeleteStatus) {
	this.impDeleteStatus = impDeleteStatus;
    }

    /**
     * @return the isDeploymentFlag
     */
    public Boolean getIsDeploymentFlag() {
	return isDeploymentFlag;
    }

    /**
     * @param isDeploymentFlag
     *            the isDeploymentFlag to set
     */
    public void setIsDeploymentFlag(Boolean isDeploymentFlag) {
	this.isDeploymentFlag = isDeploymentFlag;
    }

}
