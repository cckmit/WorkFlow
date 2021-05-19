/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.uiform;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TransactionResponseForm {

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDateTime;
    private String userName;
    private String userAction;
    private String planId;
    private String impId;
    private String userRole;
    private Long responseTimeMs;
    private String responseTimeSec;
    private String responseTimeMin;
    private String hostName;
    private String tdx;
    private String zOs;

    public String getzOs() {
	return zOs;
    }

    public void setzOs(String zOs) {
	this.zOs = zOs;
    }

    public Long getResponseTimeMs() {
	return responseTimeMs;
    }

    public void setResponseTimeMs(Long responseTimeMs) {
	this.responseTimeMs = responseTimeMs;
    }

    public String getResponseTimeSec() {
	return responseTimeSec;
    }

    public void setResponseTimeSec(String responseTimeSec) {
	this.responseTimeSec = responseTimeSec;
    }

    public String getResponseTimeMin() {
	return responseTimeMin;
    }

    public void setResponseTimeMin(String responseTimeMin) {
	this.responseTimeMin = responseTimeMin;
    }

    public Date getStartDateTime() {
	return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
	this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
	return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
	this.endDateTime = endDateTime;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getUserAction() {
	return userAction;
    }

    public void setUserAction(String userAction) {
	this.userAction = userAction;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getImpId() {
	return impId;
    }

    public void setImpId(String impId) {
	this.impId = impId;
    }

    public String getUserRole() {
	return userRole;
    }

    public void setUserRole(String userRole) {
	this.userRole = userRole;
    }

    public String getHostName() {
	return hostName;
    }

    public void setHostName(String hostName) {
	this.hostName = hostName;
    }

    public String getTdx() {
	return tdx;
    }

    public void setTdx(String tdx) {
	this.tdx = tdx;
    }

}
