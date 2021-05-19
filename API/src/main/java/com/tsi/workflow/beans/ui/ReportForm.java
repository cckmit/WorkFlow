/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ReportForm {

    private String reportType;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDate;
    private List<String> systems;
    private String role;
    private List<String> userIds;
    private List<String> funcAreas;

    public String getReportType() {
	return reportType;
    }

    public void setReportType(String reportType) {
	this.reportType = reportType;
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

    public List<String> getSystems() {
	return systems;
    }

    public void setSystems(List<String> systems) {
	this.systems = systems;
    }

    public String getRole() {
	return role;
    }

    public void setRole(String role) {
	this.role = role;
    }

    public List<String> getUserIds() {
	return userIds;
    }

    public void setUserIds(List<String> userIds) {
	this.userIds = userIds;
    }

    public List<String> getFuncAreas() {
	return funcAreas;
    }

    public void setFuncAreas(List<String> funcAreas) {
	this.funcAreas = funcAreas;
    }

}
