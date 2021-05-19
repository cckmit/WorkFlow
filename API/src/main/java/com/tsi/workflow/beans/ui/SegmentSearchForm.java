package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import java.util.List;

public class SegmentSearchForm {

    private String environment;
    private List<Integer> targetSys;
    private List<String> actionList;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDate;
    private List<String> programNames;
    private List<String> functionPackage;

    public String getEnvironment() {
	return environment;
    }

    public void setEnvironment(String environment) {
	this.environment = environment;
    }

    public List<Integer> getTargetSys() {
	return targetSys;
    }

    public void setTargetSys(List<Integer> targetSys) {
	this.targetSys = targetSys;
    }

    public List<String> getActionList() {
	return actionList;
    }

    public void setActionList(List<String> actionList) {
	this.actionList = actionList;
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

    public List<String> getProgramNames() {
	return programNames;
    }

    public void setProgramNames(List<String> programNames) {
	this.programNames = programNames;
    }

    public List<String> getFunctionPackage() {
	return functionPackage;
    }

    public void setFunctionPackage(List<String> functionPackage) {
	this.functionPackage = functionPackage;
    }
}
