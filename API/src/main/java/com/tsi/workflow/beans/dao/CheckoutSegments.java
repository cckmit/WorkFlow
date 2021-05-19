/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.base.BaseBeans;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "checkout_segments")
@NamedQueries({ @NamedQuery(name = "CheckoutSegments.findAll", query = "SELECT c FROM CheckoutSegments c") })
public class CheckoutSegments extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "program_name")
    private String programName;
    @Size(max = 2147483647)
    @Column(name = "repo_desc")
    private String repoDesc;
    @Size(max = 2147483647)
    @Column(name = "commit_id")
    private String commitId;
    @Size(max = 2147483647)
    @Column(name = "author_name")
    private String authorName;
    @Size(max = 2147483647)
    @Column(name = "author_mail_id")
    private String authorMailId;
    @Size(max = 2147483647)
    @Column(name = "committer_name")
    private String committerName;
    @Column(name = "common_file")
    private Boolean commonFile;
    @Size(max = 2147483647)
    @Column(name = "committer_mail_id")
    private String committerMailId;
    @Column(name = "commit_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date commitDateTime;
    @Size(max = 2147483647)
    @Column(name = "subject")
    private String subject;
    @Size(max = 2147483647)
    @Column(name = "func_area")
    private String funcArea;
    @Size(max = 2147483647)
    @Column(name = "target_system")
    private String targetSystem;
    @Size(max = 1)
    @Column(name = "status")
    private String status;
    @Size(max = 2147483647)
    @Column(name = "source_url")
    private String sourceUrl;
    @Size(max = 10)
    @Column(name = "prod_flag")
    private String prodFlag;
    @Size(max = 2147483647)
    @Column(name = "file_name")
    private String fileName;
    @Size(max = 2147483647)
    @Column(name = "file_hash_code")
    private String fileHashCode;
    @Size(max = 2147483647)
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date createdDt;
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "modified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date modifiedDt;
    @Size(max = 50)
    @Column(name = "modified_by")
    private String modifiedBy;
    @Size(max = 1)
    @Column(name = "active")
    private String active;
    @Column(name = "review_status")
    private Boolean reviewStatus;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @JoinColumn(name = "imp_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Implementation impId;
    @JoinColumn(name = "system_load", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SystemLoad systemLoad;
    @Column(name = "ref_load_date")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date refLoadDate;
    @Size(max = 50)
    @Column(name = "ref_status")
    private String refStatus;
    @Size(max = 50)
    @Column(name = "ref_plan")
    private String refPlan;

    @Size(max = 2147483647)
    @Column(name = "so_name")
    private String soName;

    @Column(name = "last_changed_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date lastChangedTime;

    public Date getLastChangedTime() {
	return lastChangedTime;
    }

    public void setLastChangedTime(Date lastChangedTime) {
	this.lastChangedTime = lastChangedTime;
    }

    public CheckoutSegments() {
	commonFile = false;
	lastChangedTime = new Date();
    }

    public CheckoutSegments(Integer id) {
	this.id = id;
	commonFile = false;
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

    public Boolean getCommonFile() {
	return commonFile;
    }

    public void setCommonFile(Boolean commonFile) {
	this.commonFile = commonFile;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getProgramName() {
	return programName;
    }

    public void setProgramName(String programName) {
	this.programName = programName;
    }

    public String getRepoDesc() {
	return repoDesc;
    }

    public void setRepoDesc(String repoDesc) {
	this.repoDesc = repoDesc;
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

    public String getFuncArea() {
	return funcArea;
    }

    public void setFuncArea(String funcArea) {
	this.funcArea = funcArea;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getSourceUrl() {
	return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
	this.sourceUrl = sourceUrl;
    }

    public String getProdFlag() {
	return prodFlag;
    }

    public void setProdFlag(String prodFlag) {
	this.prodFlag = prodFlag;
    }

    public String getFileName() {
	return fileName;
    }

    public void setFileName(String fileName) {
	this.fileName = fileName;
    }

    public String getFileHashCode() {
	return fileHashCode;
    }

    public void setFileHashCode(String fileHashCode) {
	this.fileHashCode = fileHashCode;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String fileType) {
	this.fileType = fileType;
    }

    @Override
    public Date getCreatedDt() {
	return createdDt;
    }

    @Override
    public void setCreatedDt(Date createdDt) {
	this.createdDt = createdDt;
    }

    @Override
    public String getCreatedBy() {
	return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    @Override
    public Date getModifiedDt() {
	return modifiedDt;
    }

    @Override
    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    @Override
    public String getModifiedBy() {
	return modifiedBy;
    }

    @Override
    public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
    }

    @Override
    public String getActive() {
	return active;
    }

    @Override
    public void setActive(String active) {
	this.active = active;
    }

    public Boolean getReviewStatus() {
	return reviewStatus;
    }

    public void setReviewStatus(Boolean reviewStatus) {
	this.reviewStatus = reviewStatus;
    }

    public ImpPlan getPlanId() {
	return planId;
    }

    public void setPlanId(ImpPlan planId) {
	this.planId = planId;
    }

    public Implementation getImpId() {
	return impId;
    }

    public void setImpId(Implementation impId) {
	this.impId = impId;
    }

    public SystemLoad getSystemLoad() {
	return systemLoad;
    }

    public void setSystemLoad(SystemLoad systemLoad) {
	this.systemLoad = systemLoad;
    }

    public String getSoName() {
	return soName;
    }

    public void setSoName(String soName) {
	this.soName = soName;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof CheckoutSegments)) {
	    return false;
	}
	CheckoutSegments other = (CheckoutSegments) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.CheckoutSegments[ id=" + id + " ]";
    }

}
