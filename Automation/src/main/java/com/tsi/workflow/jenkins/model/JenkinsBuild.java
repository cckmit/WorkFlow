/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins.model;

import com.tsi.workflow.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author
 */
public class JenkinsBuild {

    private String jobName;
    private String queueUrl;
    private Integer buildNumber;
    private Date buildTime;
    private String systemLoadId;
    private User user;
    private Boolean byPassRegression;
    private String reason;
    private List<String> changedFiles = new ArrayList<>();
    private Date startedDate;
    private String buildType;
    private String planId;

    public JenkinsBuild(User user) {
	this.user = user;
    }

    public JenkinsBuild() {
    }

    public Boolean getByPassRegression() {
	return byPassRegression;
    }

    public void setByPassRegression(Boolean byPassRegression) {
	this.byPassRegression = byPassRegression;
    }

    public Date getBuildTime() {
	return buildTime;
    }

    public void setBuildTime(Date buildTime) {
	this.buildTime = buildTime;
    }

    public String getJobName() {
	return jobName;
    }

    public void setJobName(String jobName) {
	this.jobName = jobName;
    }

    public String getQueueUrl() {
	return queueUrl;
    }

    public void setQueueUrl(String queueUrl) {
	this.queueUrl = queueUrl;
    }

    public Integer getBuildNumber() {
	return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
	this.buildNumber = buildNumber;
    }

    public String getSystemLoadId() {
	return systemLoadId;
    }

    public void setSystemLoadId(String systemLoadId) {
	this.systemLoadId = systemLoadId;
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public List<String> getChangedFiles() {
	return changedFiles;
    }

    public void setChangedFiles(List<String> changedFiles) {
	this.changedFiles = changedFiles;
    }

    public Date getStartedDate() {
	return startedDate;
    }

    public void setStartedDate(Date startedDate) {
	this.startedDate = startedDate;
    }

    public String getBuildType() {
	return buildType;
    }

    public void setBuildType(String buildType) {
	this.buildType = buildType;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof JenkinsBuild) {
	    JenkinsBuild lBuild = (JenkinsBuild) obj;
	    return lBuild.getJobName().equals(this.jobName) && lBuild.getBuildNumber().equals(this.buildNumber) && lBuild.getSystemLoadId().equals(this.systemLoadId);
	} else {
	    return false;
	}
    }

    @Override
    public int hashCode() {
	int hash = 3;
	hash = 59 * hash + Objects.hashCode(this.jobName); // 177 + 2043893461
	hash = 59 * hash + this.buildNumber;
	hash = 59 * hash + Objects.hashCode(this.systemLoadId);
	return hash;
    }

}
