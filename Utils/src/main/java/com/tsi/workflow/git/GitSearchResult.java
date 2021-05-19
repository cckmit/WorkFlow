/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author USER
 */
public final class GitSearchResult implements Comparator<GitSearchResult> {

    private String fileName;
    private String programName;
    private String fileHashCode;
    private SortedSet<String> targetSystems;
    private List<GitBranchSearchResult> branch;
    private String prodFlag = "PROD";
    private String fileNameWithHash;
    private HashMap<String, String> additionalInfo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date maxLoadDate;
    private String repoAccess;
    private SortedSet<String> allowedTargetSystems;

    public GitSearchResult() {
	branch = new ArrayList();
	targetSystems = new TreeSet();
	additionalInfo = new HashMap();
    }

    public HashMap<String, String> getAdditionalInfo() {
	return additionalInfo;
    }

    public void setAdditionalInfo(HashMap<String, String> additionalInfo) {
	this.additionalInfo = additionalInfo;
    }

    public void addAdditionalInfo(String pKey, String pValue) {
	this.additionalInfo.put(pKey, pValue);
    }

    public SortedSet<String> getTargetSystems() {
	return targetSystems;
    }

    public void setTargetSystems(SortedSet<String> targetSystems) {
	this.targetSystems = targetSystems;
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

    public String getFileHashCode() {
	return fileHashCode;
    }

    public void setFileHashCode(String fileHashCode) {
	this.fileHashCode = fileHashCode;
    }

    public List<GitBranchSearchResult> getBranch() {
	return branch;
    }

    public void setBranch(List<GitBranchSearchResult> branch) {
	this.branch = branch;
    }

    public void addBranch(GitBranchSearchResult branch) {
	this.branch.add(branch);
	if (branch.getRefLoadDate() != null) {
	    if (this.getMaxLoadDate() == null) {
		this.setMaxLoadDate(branch.getRefLoadDate());
	    } else if (this.getMaxLoadDate().before(branch.getRefLoadDate())) {
		this.setMaxLoadDate(branch.getRefLoadDate());
	    }
	}
    }

    public String getFileNameWithHash() {
	return fileNameWithHash;
    }

    public void setFileNameWithHash(String fileNameWithHash) {
	this.fileNameWithHash = fileNameWithHash;
    }

    public String getProdFlag() {
	return prodFlag;
    }

    public void setProdFlag(String prodFlag) {
	this.prodFlag = prodFlag;
    }

    public Date getMaxLoadDate() {
	return maxLoadDate;
    }

    public void setMaxLoadDate(Date minLoadDate) {
	this.maxLoadDate = minLoadDate;
    }

    public String getRepoAccess() {
	return repoAccess;
    }

    public void setRepoAccess(String repoAccess) {
	this.repoAccess = repoAccess;
    }

    public SortedSet<String> getAllowedTargetSystems() {
	return allowedTargetSystems;
    }

    public void setAllowedTargetSystems(SortedSet<String> allowedTargetSystems) {
	this.allowedTargetSystems = allowedTargetSystems;
    }

    public Date getBranchMaxLoadDate() {
	return getBranch().stream().filter(x -> x.getRefLoadDate() != null).map(x -> x.getRefLoadDate()).max(Date::compareTo).isPresent() ? getBranch().stream().filter(x -> x.getRefLoadDate() != null).map(x -> x.getRefLoadDate()).max(Date::compareTo).get() : null;
    }

    @Override
    public int compare(GitSearchResult o1, GitSearchResult o2) {
	int compareTo = o1.getFileName().compareTo(o2.getFileName());
	if (compareTo == 0) {
	    return o2.getMaxLoadDate().compareTo(o1.getMaxLoadDate());
	} else {
	    return compareTo;
	}
    }

}
