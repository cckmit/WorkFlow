/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

/**
 *
 * @author
 */
public class JobDetails {

    private String planId;
    private String systemName;
    // private long percentageCompleted;
    private String status;
    private String message;

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    // public long getPercentageCompleted() {
    // return percentageCompleted;
    // }
    //
    // public void setPercentageCompleted(long percentageCompleted) {
    // this.percentageCompleted = percentageCompleted;
    // }
}
