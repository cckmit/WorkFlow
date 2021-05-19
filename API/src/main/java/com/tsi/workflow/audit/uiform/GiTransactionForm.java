/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.uiform;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GiTransactionForm {

    private String action;
    private String targetSystem;
    private String userId;
    private String startDateTime;
    private String endDateTime;
    private String planId;
    private String implId;
    private Long responseTime;
    private String message;
    private String segments;
    private Integer id;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	if (planId != null) {
	    planId = planId.toUpperCase();
	}
	this.planId = planId;
    }

    public Long getResponseTime() {
	return responseTime;
    }

    public void setResponseTime(Long responseTime) {
	this.responseTime = responseTime;
    }

    public String getStartDateTime() {
	return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
	this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
	return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
	this.endDateTime = endDateTime;
    }

    public String getImplId() {
	return implId;
    }

    public void setImplId(String implId) {
	if (implId != null) {
	    implId = implId.toUpperCase();
	}
	this.implId = implId;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getSegments() {
	return segments;
    }

    public void setSegments(String segments) {
	this.segments = segments;
    }
}
