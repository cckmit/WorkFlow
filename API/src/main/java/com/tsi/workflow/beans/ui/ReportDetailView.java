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
public class ReportDetailView {

    private String userName;
    private Integer totoalDeployments = 0; // Plan in Online and Fallback Status
    private Integer totalSharedObjects = 0;
    private Integer totalOnlineDeployments = 0;
    private Integer totalOnlineSharedObjects = 0;
    private Integer totalFallbackDeployments = 0;
    private Integer totalFallbackSharedObjects = 0;
    private Integer successPerForDeployment = 0;
    private Integer SuccessPerForSourceObjects = 0;
    // 1693
    private String funcArea;
    private String FuncAreaDesc;
    private Integer totalOnlineSegmentsCount = 0;
    private Integer totalFallbackSegmentsCount = 0;
    private Integer successPerFunc = 0;

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public Integer getTotoalDeployments() {
	return totoalDeployments;
    }

    public void setTotoalDeployments(Integer totoalDeployments) {
	this.totoalDeployments = totoalDeployments;
    }

    public Integer getTotalSharedObjects() {
	return totalSharedObjects;
    }

    public void setTotalSharedObjects(Integer totalSharedObjects) {
	this.totalSharedObjects = totalSharedObjects;
    }

    public Integer getTotalOnlineDeployments() {
	return totalOnlineDeployments;
    }

    public void setTotalOnlineDeployments(Integer totalOnlineDeployments) {
	this.totalOnlineDeployments = totalOnlineDeployments;
    }

    public Integer getTotalOnlineSharedObjects() {
	return totalOnlineSharedObjects;
    }

    public void setTotalOnlineSharedObjects(Integer totalOnlineSharedObjects) {
	this.totalOnlineSharedObjects = totalOnlineSharedObjects;
    }

    public Integer getTotalFallbackDeployments() {
	return totalFallbackDeployments;
    }

    public void setTotalFallbackDeployments(Integer totalFallbackDeployments) {
	this.totalFallbackDeployments = totalFallbackDeployments;
    }

    public Integer getTotalFallbackSharedObjects() {
	return totalFallbackSharedObjects;
    }

    public void setTotalFallbackSharedObjects(Integer totalFallbackSharedObjects) {
	this.totalFallbackSharedObjects = totalFallbackSharedObjects;
    }

    public Integer getSuccessPerForDeployment() {
	return successPerForDeployment;
    }

    public void setSuccessPerForDeployment(Integer successPerForDeployment) {
	this.successPerForDeployment = successPerForDeployment;
    }

    public Integer getSuccessPerForSourceObjects() {
	return SuccessPerForSourceObjects;
    }

    public void setSuccessPerForSourceObjects(Integer SuccessPerForSourceObjects) {
	this.SuccessPerForSourceObjects = SuccessPerForSourceObjects;
    }

    public Integer getSuccessPerFunc() {
	return successPerFunc;
    }

    public void setSuccessPerFunc(Integer successPerFunc) {
	this.successPerFunc = successPerFunc;
    }

    public void setSuccessPercentage() {
	this.successPerFunc = (totalOnlineSegmentsCount / (totalOnlineSegmentsCount + totalFallbackSegmentsCount)) * 100;
    }

    public String getFuncArea() {
	return funcArea;
    }

    public void setFuncArea(String funcArea) {
	this.funcArea = funcArea;
    }

    public String getFuncAreaDesc() {
	return FuncAreaDesc;
    }

    public void setFuncAreaDesc(String FuncAreaDesc) {
	this.FuncAreaDesc = FuncAreaDesc;
    }

    public Integer getTotalOnlineSegmentsCount() {
	return totalOnlineSegmentsCount;
    }

    public void setTotalOnlineSegmentsCount(Integer totalOnlineSegmentsCount) {
	this.totalOnlineSegmentsCount = totalOnlineSegmentsCount;
    }

    public Integer getTotalFallbackSegmentsCount() {
	return totalFallbackSegmentsCount;
    }

    public void setTotalFallbackSegmentsCount(Integer totalFallbackSegmentsCount) {
	this.totalFallbackSegmentsCount = totalFallbackSegmentsCount;
    }

}
