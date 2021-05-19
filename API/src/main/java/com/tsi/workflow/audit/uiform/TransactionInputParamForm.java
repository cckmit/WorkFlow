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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TransactionInputParamForm {
    private String hostName;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDate;
    private List<String> userName = new ArrayList<>();
    private String planId;
    private List<String> userAction = new ArrayList<>();

    public String getHostName() {
	return hostName;
    }

    public void setHostName(String hostName) {
	this.hostName = hostName;
    }

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public List<String> getUserName() {
	return userName;
    }

    public void setUserName(List<String> userName) {
	this.userName = userName;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public List<String> getUserAction() {
	return userAction;
    }

    public void setUserAction(List<String> userAction) {
	this.userAction = userAction;
    }

}
