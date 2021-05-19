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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

@Entity
@Table(name = "repository_details")
@NamedQueries({ @NamedQuery(name = "RepositoryDetails.findAll", query = "SELECT b FROM RepositoryDetails b") })
public class RepositoryDetails extends BaseBeans implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 1)
    @Column(name = "active")
    private String active;
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date createdDt;
    @Size(max = 50)
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date modifiedDt;
    @Size(max = 2147483647)
    @Column(name = "repo_name")
    private String repoName;

    @Size(max = 2147483647)
    @Column(name = "trimmed_name")
    private String trimmedName;

    @Size(max = 2147483647)
    @Column(name = "repo_description")
    private String repoDescription;

    @Size(max = 2147483647)
    @Column(name = "func_area")
    private String funcArea;

    @Size(max = 2147483647)
    @Column(name = "default_file_create")
    private String defaultFileCreate;

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    @Override
    public String getActive() {
	return active;
    }

    @Override
    public void setActive(String active) {
	this.active = active;
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
    public Date getCreatedDt() {
	return createdDt;
    }

    @Override
    public void setCreatedDt(Date createdDt) {
	this.createdDt = createdDt;
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
    public Date getModifiedDt() {
	return modifiedDt;
    }

    @Override
    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    public String getRepoName() {
	return repoName;
    }

    public void setRepoName(String repoName) {
	this.repoName = repoName;
    }

    public String getTrimmedName() {
	return trimmedName;
    }

    public void setTrimmedName(String trimmedName) {
	this.trimmedName = trimmedName;
    }

    public String getDefaultFileCreate() {
	return defaultFileCreate;
    }

    public void setDefaultFileCreate(String defaultFileCreate) {
	this.defaultFileCreate = defaultFileCreate;
    }

    public String getRepoDescription() {
	return repoDescription;
    }

    public void setRepoDescription(String repoDescription) {
	this.repoDescription = repoDescription;
    }

    public String getFuncArea() {
	return funcArea;
    }

    public void setFuncArea(String funcArea) {
	this.funcArea = funcArea;
    }

    @Override
    public String toString() {
	return "RepositoryDetails [id=" + id + ", active=" + active + ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", modifiedBy=" + modifiedBy + ", modifiedDt=" + modifiedDt + ", repoName=" + repoName + ", trimmedName=" + trimmedName + ", repoDescription=" + repoDescription + ", funcArea=" + funcArea + ", defaultFileCreate=" + defaultFileCreate + "]";
    }

}
