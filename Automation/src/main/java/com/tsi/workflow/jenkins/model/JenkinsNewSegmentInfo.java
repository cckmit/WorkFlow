/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins.model;

/**
 *
 * @author Radha.Adhimoolam
 */
public class JenkinsNewSegmentInfo {

    private String fileName;
    private String commitId;
    private String fileHashCode;
    private String sourceUrl;

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getCommitId() {
	return commitId;
    }

    public void setCommitId(String commitId) {
	this.commitId = commitId;
    }

    public String getFileHashCode() {
	return fileHashCode;
    }

    public void setFileHashCode(String fileHashCode) {
	this.fileHashCode = fileHashCode;
    }

    public String getSourceUrl() {
	return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
	this.sourceUrl = sourceUrl;
    }

}
