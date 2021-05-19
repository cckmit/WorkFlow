/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tdx.executor.models;

import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.mail.NewTargetSystemMail;
import com.tsi.workflow.utils.JSONResponse;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TdxShellExecutorModel<T> {

    private JSONResponse jsonResponse;
    private User user;
    private System system;
    private ImpPlan plan;
    private Implementation impl;
    private SystemLoad systemLoad;
    private String returnData;
    private Map<String, GitBranchSearchResult> lTempGitSearchSegments;
    private NewTargetSystemMail newTargetSystemMailNotification;
    private Map.Entry<String, List<CheckoutSegments>> lSystemEntry;
    private List<GitSearchResult> pSearchResults;
    private List<CheckoutSegments> segmentList;
    private String commitMessage;
    private Boolean activityStatus;
    private List<CheckoutSegments> segmentsToBeDeleted;
    private Boolean isSegmentDelete;

    public TdxShellExecutorModel() {
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public System getSystem() {
	return system;
    }

    public void setSystem(System system) {
	this.system = system;
    }

    public SystemLoad getSystemLoad() {
	return systemLoad;
    }

    public void setSystemLoad(SystemLoad systemLoad) {
	this.systemLoad = systemLoad;
    }

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public Implementation getImpl() {
	return impl;
    }

    public void setImpl(Implementation impl) {
	this.impl = impl;
    }

    public JSONResponse getJsonResponse() {
	return jsonResponse;
    }

    public void setJsonResponse(JSONResponse jsonResponse) {
	this.jsonResponse = jsonResponse;
    }

    public String getReturnData() {
	return returnData;
    }

    public void setReturnData(String returnData) {
	this.returnData = returnData;
    }

    public Map<String, GitBranchSearchResult> getlTempGitSearchSegments() {
	return lTempGitSearchSegments;
    }

    public void setlTempGitSearchSegments(Map<String, GitBranchSearchResult> lTempGitSearchSegments) {
	this.lTempGitSearchSegments = lTempGitSearchSegments;
    }

    public NewTargetSystemMail getNewTargetSystemMailNotification() {
	return newTargetSystemMailNotification;
    }

    public void setNewTargetSystemMailNotification(NewTargetSystemMail newTargetSystemMailNotification) {
	this.newTargetSystemMailNotification = newTargetSystemMailNotification;
    }

    public Map.Entry<String, List<CheckoutSegments>> getlSystemEntry() {
	return lSystemEntry;
    }

    public void setlSystemEntry(Map.Entry<String, List<CheckoutSegments>> lSystemEntry) {
	this.lSystemEntry = lSystemEntry;
    }

    public List<GitSearchResult> getpSearchResults() {
	return pSearchResults;
    }

    public void setpSearchResults(List<GitSearchResult> pSearchResults) {
	this.pSearchResults = pSearchResults;
    }

    public List<CheckoutSegments> getSegmentList() {
	return segmentList;
    }

    public void setSegmentList(List<CheckoutSegments> segmentList) {
	this.segmentList = segmentList;
    }

    public String getCommitMessage() {
	return commitMessage;
    }

    public void setCommitMessage(String commitMessage) {
	this.commitMessage = commitMessage;
    }

    public Boolean getActivityStatus() {
	return activityStatus;
    }

    public void setActivityStatus(Boolean activityStatus) {
	this.activityStatus = activityStatus;
    }

    public List<CheckoutSegments> getSegmentsToBeDeleted() {
	return segmentsToBeDeleted;
    }

    public void setSegmentsToBeDeleted(List<CheckoutSegments> segmentsToBeDeleted) {
	this.segmentsToBeDeleted = segmentsToBeDeleted;
    }

    public Boolean getIsSegmentDelete() {
	return isSegmentDelete;
    }

    public void setIsSegmentDelete(Boolean isSegmentDelete) {
	this.isSegmentDelete = isSegmentDelete;
    }

}
