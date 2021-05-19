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
public class SummaryDetailView {

    private Integer totalUsers;
    private Integer totalDeployments = 0;
    private Integer totalSourceObjects = 0;
    private Integer totalOnlineDeployments = 0;
    private Integer totalOnlineSharedObjects = 0;
    private Integer totalFallbackDeployments = 0;
    private Integer totalFallbackSharedObjects = 0;
    private Integer averageSuccessPerOnOnlineDeploy = 0;
    private Integer averageSuccessPerOnSO = 0;

    // 1693
    private Integer totalFuncAreaCnt = 0;
    private Integer totalOnlineSegments = 0;
    private Integer totalFallbackSegments = 0;
    private Integer successPerFunc = 0;

    public SummaryDetailView() {
    }

    public Integer getTotalUsers() {
	return totalUsers;
    }

    public void setTotalUsers(Integer totalUsers) {
	this.totalUsers = totalUsers;
    }

    public Integer getTotalDeployments() {
	return totalDeployments;
    }

    public void setTotalDeployments(Integer totalDeployments) {
	this.totalDeployments = totalDeployments;
    }

    public Integer getTotalSourceObjects() {
	return totalSourceObjects;
    }

    public void setTotalSourceObjects(Integer totalSourceObjects) {
	this.totalSourceObjects = totalSourceObjects;
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

    public Integer getAverageSuccessPerOnOnlineDeploy() {
	return averageSuccessPerOnOnlineDeploy;
    }

    public void setAverageSuccessPerOnOnlineDeploy(Integer averageSuccessPerOnOnlineDeploy) {
	this.averageSuccessPerOnOnlineDeploy = averageSuccessPerOnOnlineDeploy;
    }

    public Integer getAverageSuccessPerOnSO() {
	return averageSuccessPerOnSO;
    }

    public void setAverageSuccessPerOnSO(Integer averageSuccessPerOnSO) {
	this.averageSuccessPerOnSO = averageSuccessPerOnSO;
    }

    public Integer getTotalOnlineSegments() {
	return totalOnlineSegments;
    }

    public void setTotalOnlineSegments(Integer totalOnlineSegments) {
	this.totalOnlineSegments = totalOnlineSegments;
    }

    public Integer getTotalFallbackSegments() {
	return totalFallbackSegments;
    }

    public void setTotalFallbackSegments(Integer totalFallbackSegments) {
	this.totalFallbackSegments = totalFallbackSegments;
    }

    public Integer getSuccessPerFunc() {
	return successPerFunc;
    }

    public void setSuccessPerFunc(Integer successPerFunc) {
	this.successPerFunc = successPerFunc;
    }

    public Integer getTotalFuncAreaCnt() {
	return totalFuncAreaCnt;
    }

    public void setTotalFuncAreaCnt(Integer totalFuncAreaCnt) {
	this.totalFuncAreaCnt = totalFuncAreaCnt;
    }

}
