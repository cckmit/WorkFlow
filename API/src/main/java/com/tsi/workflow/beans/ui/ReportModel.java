/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ReportModel {

    private String userName;
    private String planId;
    private String planStatus;
    private Integer soCount;
    private String funcArea;
    private Integer segmentsCount;
    private String systemName;

    public ReportModel() {
    }

    public ReportModel(String userName, String planId, String planStatus, Integer soCount) {
	this.userName = userName;
	this.planId = planId;
	this.planStatus = planStatus;
	this.soCount = soCount;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getPlanStatus() {
	return planStatus;
    }

    public void setPlanStatus(String planStatus) {
	this.planStatus = planStatus;
    }

    public Integer getSoCount() {
	return soCount;
    }

    public void setSoCount(Integer soCount) {
	this.soCount = soCount;
    }

    public String getFuncArea() {
	return funcArea;
    }

    public void setFuncArea(String funcArea) {
	this.funcArea = funcArea;
    }

    public Integer getSegmentsCount() {
	return segmentsCount;
    }

    public void setSegmentsCount(Integer segmentsCount) {
	this.segmentsCount = segmentsCount;
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }
}
