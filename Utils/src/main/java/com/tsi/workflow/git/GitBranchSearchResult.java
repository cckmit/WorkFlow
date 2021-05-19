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
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author User
 */
public class GitBranchSearchResult implements Comparator<GitBranchSearchResult> {

    private static final Logger LOG = Logger.getLogger(GitBranchSearchResult.class.getName());

    private Integer id;
    private String commitId;
    private String authorName;
    private String authorMailId;
    private String committerName;
    private String committerMailId;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date commitDateTime;
    private String subject;
    private List<String> tags;
    private String targetSystem;
    private String funcArea;
    private String sourceUrl;
    private String fileType;
    private String repoDesc;
    private String status;
    private boolean isCheckedout;
    private boolean isBranchSelected = true;
    private boolean isCheckoutAllowed = true;
    private boolean isPopulated = true;
    private HashMap<String, String> additionalInfo;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loadDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date refLoadDate;
    private String refStatus;
    private String refPlan;
    private String gitSourceURL;
    private List<String> remarks;
    private String targetFuncArea;

    public Date getLoadDate() {
	return loadDate;
    }

    public void setLoadDate(Date loadDate) {
	this.loadDate = loadDate;
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

    public GitBranchSearchResult() {
	tags = new ArrayList();
	additionalInfo = new HashMap();
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
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

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String fileType) {
	this.fileType = fileType;
    }

    public String getCommitId() {
	return commitId;
    }

    public void setCommitId(String commitId) {
	this.commitId = commitId;
    }

    public String getAuthorName() {
	return authorName;
    }

    public void setAuthorName(String authorName) {
	this.authorName = authorName;
    }

    public String getAuthorMailId() {
	return authorMailId;
    }

    public void setAuthorMailId(String authorMailId) {
	this.authorMailId = authorMailId;
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

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public List<String> getTags() {
	return tags;
    }

    public void setTags(List<String> tags) {
	this.tags = tags;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public String getFuncArea() {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(this.funcArea)).toUpperCase();
	String lReturn = FilenameUtils.getName(this.funcArea).toLowerCase().replace("nonibm_", "").replace("ibm_", "").replace(".git", "").toUpperCase();

	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    return lReturn;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    return lReturn;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    return lReturn;
	} else {
	    return lReturn.replaceAll("\\d+$", "");
	}
    }

    public void setFuncArea(String funcArea) {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(funcArea)).toUpperCase();
	String lReturn = FilenameUtils.getName(funcArea).toLowerCase().replace("nonibm_", "").replace("ibm_", "").replace(".git", "").toUpperCase();

	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    this.funcArea = lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    this.funcArea = lReturn;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    this.funcArea = lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    this.funcArea = lReturn;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    this.funcArea = lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    this.funcArea = lReturn;
	} else {
	    this.funcArea = lReturn.replaceAll("\\d+$", "");
	}
    }

    public String getSourceUrl() {
	return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
	this.sourceUrl = sourceUrl;
    }

    public String getRepoDesc() {
	return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
	this.repoDesc = repoDesc;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public boolean getIsCheckedout() {
	return isCheckedout;
    }

    public void setIsCheckedout(boolean isCheckedout) {
	this.isCheckedout = isCheckedout;
    }

    public boolean getIsBranchSelected() {
	return isBranchSelected;
    }

    public void setIsBranchSelected(boolean isBranchSelected) {
	this.isBranchSelected = isBranchSelected;
    }

    public boolean isIsCheckoutAllowed() {
	return isCheckoutAllowed;
    }

    public void setIsCheckoutAllowed(boolean isCheckoutAllowed) {
	this.isCheckoutAllowed = isCheckoutAllowed;
    }

    public String getGitSourceURL() {
	return gitSourceURL;
    }

    public void setGitSourceURL(String gitSourceURL) {
	this.gitSourceURL = gitSourceURL;
    }

    public List<String> getRemarks() {
	return remarks;
    }

    public void setRemarks(List<String> remarks) {
	this.remarks = remarks;
    }

    public String getTargetFuncArea() {
	return targetFuncArea;
    }

    public void setTargetFuncArea(String targetFuncArea) {
	this.targetFuncArea = targetFuncArea;
    }

    public boolean isIsPopulated() {
	return isPopulated;
    }

    public void setIsPopulated(boolean isPopulated) {
	this.isPopulated = isPopulated;
    }

    @Override
    public int compare(GitBranchSearchResult o1, GitBranchSearchResult o2) {
	return o2.getRefLoadDate().compareTo(o1.getRefLoadDate());
    }
}
