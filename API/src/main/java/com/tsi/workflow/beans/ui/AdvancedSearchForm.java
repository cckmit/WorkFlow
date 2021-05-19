/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author gn.ebinezardharmaraj
 */
public class AdvancedSearchForm {

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDate;
    List<System> targetSystems = new ArrayList<System>();

    private List<String> name;
    private String programName;
    private List<String> csrNumber;
    private List<String> implPlanStatus;
    private List<String> functionalPackages;
    private List<String> role;
    private String implPlanId;
    private String implId;
    private Boolean exactSegment;

    public Boolean getExactSegment() {
	return exactSegment;
    }

    public void setExactSegment(Boolean exactSegment) {
	this.exactSegment = exactSegment;
    }

    public String getImplPlanId() {
	return implPlanId;
    }

    public void setImplPlanId(String implPlanId) {
	this.implPlanId = implPlanId;
    }

    public String getImplId() {
	return implId;
    }

    public void setImplId(String implId) {
	this.implId = implId;
    }

    public List<String> getName() {
	return name;
    }

    public void setName(List<String> name) {
	this.name = name;
    }

    public String getProgramName() {
	return programName;
    }

    public void setProgramName(String programName) {
	this.programName = programName;
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

    public List<System> getTargetSystems() {
	return targetSystems;
    }

    public void setTargetSystems(List<System> targetSystems) {
	this.targetSystems = targetSystems;
    }

    public List<String> getCsrNumber() {
	return csrNumber;
    }

    public void setCsrNumber(List<String> csrNumber) {
	this.csrNumber = csrNumber;
    }

    public List<String> getImplPlanStatus() {
	return implPlanStatus;
    }

    public void setImplPlanStatus(List<String> implPlanStatus) {
	this.implPlanStatus = implPlanStatus;
    }

    public List<String> getFunctionalPackages() {
	return functionalPackages;
    }

    public void setFunctionalPackages(List<String> functionalPackages) {
	this.functionalPackages = functionalPackages;
    }

    public List<String> getRole() {
	return role;
    }

    public void setRole(List<String> role) {
	this.role = role;
    }

}
