/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.dao;

import com.tsi.workflow.base.BaseBeans;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author DINESH.RAMANATHAN
 */
@Entity
@Table(name = "git_prod_search_db")
@NamedQueries({ @NamedQuery(name = "GitProdSearchDb.findAll", query = "SELECT g FROM GitProdSearchDb g") })
public class GitProdSearchDb extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "repo_commit_id")
    private Integer repocommitid;
    @Column(name = "func_area")
    private String funcarea;
    @Column(name = "version")
    private Integer version;
    @Column(name = "sub_repo_id")
    private Integer subrepoid;
    @Column(name = "file_id")
    private Integer fileid;
    @Column(name = "file_name")
    private String filename;
    @Column(name = "program_name")
    private String programname;
    @Column(name = "source_commit_id")
    private String sourcecommitid;
    @Column(name = "derived_commit_id")
    private String derivedcommitid;
    @Column(name = "committer_name")
    private String committername;
    @Column(name = "committer_mail_id")
    private String committermailid;
    @Column(name = "commit_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date commitdatetime;
    @Column(name = "ref_plan")
    private String refplan;
    @Column(name = "ref_status")
    private String refstatus;
    @Column(name = "ref_load_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refloaddatetime;
    @Column(name = "file_hashcode")
    private String filehashcode;
    @Column(name = "target_system")
    private String targetsystem;
    @Column(name = "repo_id")
    private Integer repoid;
    @Column(name = "sub_source_repo")
    private String subsourcerepo;
    @Column(name = "sub_derived_repo")
    private String subderivedrepo;
    @Column(name = "source_url")
    private String sourceurl;
    @Column(name = "derived_url")
    private String derivedurl;
    @Column(name = "source_repo")
    private String sourcerepo;
    @Column(name = "derived_repo")
    private String derivedrepo;
    @Column(name = "company")
    private String company;
    @Column(name = "file_type")
    private String filetype;
    @Column(name = "file_ext")
    private String fileext;
    private String active;
    @Column(name = "repo_desc")
    private String repodesc;

    public Integer getRepoCommitId() {
	return repocommitid;
    }

    public void setRepoCommitId(Integer repoCommitId) {
	this.repocommitid = repoCommitId;
    }

    public String getFuncArea() {
	return funcarea;
    }

    public void setFuncArea(String funcArea) {
	this.funcarea = funcArea;
    }

    public Integer getVersion() {
	return version;
    }

    public void setVersion(BigInteger version) {
	this.version = version.intValue();
    }

    public Integer getSubRepoId() {
	return subrepoid;
    }

    public void setSubRepoId(Integer subRepoId) {
	this.subrepoid = subRepoId;
    }

    public Integer getFileId() {
	return fileid;
    }

    public void setFileId(Integer fileId) {
	this.fileid = fileId;
    }

    public String getFileName() {
	return filename;
    }

    public void setFileName(String fileName) {
	this.filename = fileName;
    }

    public String getProgramName() {
	return programname;
    }

    public void setProgramName(String programName) {
	this.programname = programName;
    }

    public String getSourceCommitId() {
	return sourcecommitid;
    }

    public void setSourceCommitId(String sourceCommitId) {
	this.sourcecommitid = sourceCommitId;
    }

    public String getDerivedCommitId() {
	return derivedcommitid;
    }

    public void setDerivedCommitId(String derivedCommitId) {
	this.derivedcommitid = derivedCommitId;
    }

    public String getCommitterName() {
	return committername;
    }

    public void setCommitterName(String committerName) {
	this.committername = committerName;
    }

    public String getCommitterMailId() {
	return committermailid;
    }

    public void setCommitterMailId(String committerMailId) {
	this.committermailid = committerMailId;
    }

    public Date getCommitDateTime() {
	return commitdatetime;
    }

    public void setCommitDateTime(Date commitDateTime) {
	this.commitdatetime = commitDateTime;
    }

    public String getRefPlan() {
	return refplan;
    }

    public void setRefPlan(String refPlan) {
	this.refplan = refPlan;
    }

    public String getRefStatus() {
	return refstatus;
    }

    public void setRefStatus(String refStatus) {
	this.refstatus = refStatus;
    }

    public Date getRefLoadDateTime() {
	return refloaddatetime;
    }

    public void setRefLoadDateTime(Date refLoadDateTime) {
	this.refloaddatetime = refLoadDateTime;
    }

    public String getFileHashcode() {
	return filehashcode;
    }

    public void setFileHashcode(String fileHashcode) {
	this.filehashcode = fileHashcode;
    }

    public String getTargetSystem() {
	return targetsystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetsystem = targetSystem;
    }

    public Integer getRepoId() {
	return repoid;
    }

    public void setRepoId(Integer repoId) {
	this.repoid = repoId;
    }

    public String getSubSourceRepo() {
	return subsourcerepo;
    }

    public void setSubSourceRepo(String subSourceRepo) {
	this.subsourcerepo = subSourceRepo;
    }

    public String getSubDerivedRepo() {
	return subderivedrepo;
    }

    public void setSubDerivedRepo(String subDerivedRepo) {
	this.subderivedrepo = subDerivedRepo;
    }

    public String getSourceUrl() {
	return sourceurl;
    }

    public void setSourceUrl(String sourceUrl) {
	this.sourceurl = sourceUrl;
    }

    public String getDerivedUrl() {
	return derivedurl;
    }

    public void setDerivedUrl(String derivedUrl) {
	this.derivedurl = derivedUrl;
    }

    public String getSourceRepo() {
	return sourcerepo;
    }

    public void setSourceRepo(String sourceRepo) {
	this.sourcerepo = sourceRepo;
    }

    public String getDerivedRepo() {
	return derivedrepo;
    }

    public void setDerivedRepo(String derivedRepo) {
	this.derivedrepo = derivedRepo;
    }

    public String getCompany() {
	return company;
    }

    public void setCompany(String company) {
	this.company = company;
    }

    public String getFileType() {
	return filetype;
    }

    public void setFileType(String fileType) {
	this.filetype = fileType;
    }

    public String getFileExt() {
	return fileext;
    }

    public void setFileExt(String fileExt) {
	this.fileext = fileExt;
    }

    public String getRepodesc() {
	return repodesc;
    }

    public void setRepodesc(String repodesc) {
	this.repodesc = repodesc;
    }

    @Override
    public String getActive() {
	return "Y";
    }

    @Override
    public void setActive(String active) {
	// TODO Auto-generated method stub

    }

    @Override
    public String getCreatedBy() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setCreatedBy(String createdBy) {
	// TODO Auto-generated method stub

    }

    @Override
    public Date getCreatedDt() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setCreatedDt(Date createdDt) {
	// TODO Auto-generated method stub

    }

    @Override
    public String getModifiedBy() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setModifiedBy(String modifiedBy) {
	// TODO Auto-generated method stub

    }

    @Override
    public Date getModifiedDt() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void setModifiedDt(Date modifiedDt) {
	// TODO Auto-generated method stub

    }

}
