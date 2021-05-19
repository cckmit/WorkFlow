/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.recon.git;

import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author USER
 */
public class GitMetaResult {

    Integer id;
    Integer subRepoId;
    String branch;
    String fileName;
    String programName;
    String targetSystem;

    public GitMetaResult(Integer id, Integer subRepoId, String branch, String fileName) {
	if (branch != null && branch.startsWith("refs")) {
	    this.targetSystem = branch.replaceAll("refs/heads/master_", "").toUpperCase();
	    this.branch = branch;
	} else if (branch != null && !branch.startsWith("refs")) {
	    this.branch = "refs/heads/master_" + branch.toLowerCase();
	    this.targetSystem = branch;
	}
	this.fileName = fileName;
	this.programName = FilenameUtils.getName(fileName);
	this.id = id;
	this.subRepoId = subRepoId;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Integer getSubRepoId() {
	return subRepoId;
    }

    public void setSubRepoId(Integer subRepoId) {
	this.subRepoId = subRepoId;
    }

    public String getBranch() {
	return branch;
    }

    public void setBranch(String branch) {
	this.branch = branch;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getProgramName() {
	return programName;
    }

    public void setProgramName(String programName) {
	this.programName = programName;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

}
