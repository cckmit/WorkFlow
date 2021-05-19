/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins.model;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author USER
 */
public class JenkinsLog {

    Integer totalCount;
    Integer failedCount;
    String startTime;
    String endTime;
    List<String> failedFiles;
    String errorMessage;
    Boolean jobStatus = false;
    List<JenkinsNewSegmentInfo> newSegmentInfo;
    String failedFileNames;

    public Integer getTotalCount() {
	return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
	this.totalCount = totalCount;
    }

    public Integer getFailedCount() {
	return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
	this.failedCount = failedCount;
    }

    public String getStartTime() {
	return startTime;
    }

    public void setStartTime(String startTime) {
	this.startTime = startTime;
    }

    public String getEndTime() {
	return endTime;
    }

    public void setEndTime(String endTime) {
	this.endTime = endTime;
    }

    public List<String> getFailedFiles() {
	return failedFiles;
    }

    public void setFailedFiles(List<String> failedFiles) {
	this.failedFiles = failedFiles;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
	this.errorMessage = errorMessage;
    }

    public Boolean getJobStatus() {
	return jobStatus;
    }

    public void setJobStatus(Boolean jobStatus) {
	this.jobStatus = jobStatus;
    }

    public List<JenkinsNewSegmentInfo> getNewSegmentInfo() {
	return newSegmentInfo;
    }

    public void setNewSegmentInfo(List<JenkinsNewSegmentInfo> newSegmentInfo) {
	this.newSegmentInfo = newSegmentInfo;
    }

    public List<String> getFailedFileNameList() {
	if (failedFileNames != null && !failedFileNames.isEmpty()) {
	    return Arrays.asList(failedFileNames.split(","));
	} else {
	    return null;
	}
    }

    public String getFailedFileNames() {
	return this.failedFileNames;
    }

    public void setFailedFileNames(String failedFileNames) {
	this.failedFileNames = failedFileNames;
    }

}
