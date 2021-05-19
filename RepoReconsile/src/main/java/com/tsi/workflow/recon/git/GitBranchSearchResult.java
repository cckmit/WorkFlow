/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.recon.git;

import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author User
 */
public class GitBranchSearchResult implements Comparator<GitBranchSearchResult> {

    private Integer id;
    private Integer fileId;
    private Integer subRepoId;
    private String commitId;
    private String committerName;
    private String committerMailId;
    private Date commitDateTime;
    private String targetSystem;
    private String fileType;
    private Date refLoadDate;
    private String refStatus;
    private String refPlan;
    private String fileHashCode;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getFileHashCode() {
	return fileHashCode;
    }

    public void setFileHashCode(String fileHashCode) {
	this.fileHashCode = fileHashCode;
    }

    public Integer getFileId() {
	return fileId;
    }

    public void setFileId(Integer fileId) {
	this.fileId = fileId;
    }

    public Integer getSubRepoId() {
	return subRepoId;
    }

    public void setSubRepoId(Integer subRepoId) {
	this.subRepoId = subRepoId;
    }

    public String getCommitId() {
	return commitId;
    }

    public void setCommitId(String commitId) {
	this.commitId = commitId;
    }

    public String getCommitterName() {
	return committerName;
    }

    public void setCommitterName(String committerName) {
	this.committerName = committerName;
    }

    public String getCommitterMailId() {
	return committerMailId;
    }

    public void setCommitterMailId(String committerMailId) {
	this.committerMailId = committerMailId;
    }

    public Date getCommitDateTime() {
	return commitDateTime;
    }

    public void setCommitDateTime(Date commitDateTime) {
	this.commitDateTime = commitDateTime;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String fileType) {
	this.fileType = fileType;
    }

    public Date getRefLoadDate() {
	return refLoadDate;
    }

    public void setRefLoadDate(Date refLoadDate) {
	this.refLoadDate = refLoadDate;
    }

    public String getRefStatus() {
	return refStatus;
    }

    public void setRefStatus(String refStatus) {
	this.refStatus = refStatus;
    }

    public String getRefPlan() {
	return refPlan;
    }

    public void setRefPlan(String refPlan) {
	this.refPlan = refPlan;
    }

    @Override
    public int compare(GitBranchSearchResult o1, GitBranchSearchResult o2) {
	return o2.getRefLoadDate().compareTo(o1.getRefLoadDate());
    }
}
