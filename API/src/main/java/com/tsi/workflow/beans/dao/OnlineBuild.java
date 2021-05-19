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
@Table(name = "online_build")
@NamedQueries({ @NamedQuery(name = "OnlineBuild.findAll", query = "SELECT b FROM OnlineBuild b") })
public class OnlineBuild extends BaseBeans implements Serializable {

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
    @Size(max = 10)
    @Column(name = "build_type")
    private String buildType;
    @Size(max = 10)
    @Column(name = "load_set_type")
    private String loadSetType;
    @Size(max = 2147483647)
    @Column(name = "jenkins_url")
    private String jenkinsUrl;
    @Size(max = 20)
    @Column(name = "build_status")
    private String buildStatus;
    @Size(max = 1)
    @Column(name = "job_status")
    private String jobStatus;
    @Column(name = "build_number")
    private Integer buildNumber;
    @Column(name = "build_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date buildDateTime;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;

    public OnlineBuild() {
    }

    public OnlineBuild(Integer id) {
	this.id = id;
    }

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

    public String getBuildType() {
	return buildType;
    }

    public void setBuildType(String buildType) {
	this.buildType = buildType;
    }

    public String getLoadSetType() {
	return loadSetType;
    }

    public void setLoadSetType(String loadSetType) {
	this.loadSetType = loadSetType;
    }

    public String getJenkinsUrl() {
	return jenkinsUrl;
    }

    public void setJenkinsUrl(String jenkinsUrl) {
	this.jenkinsUrl = jenkinsUrl;
    }

    public String getBuildStatus() {
	return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
	this.buildStatus = buildStatus;
    }

    public String getJobStatus() {
	return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
	this.jobStatus = jobStatus;
    }

    public Integer getBuildNumber() {
	return buildNumber;
    }

    public void setBuildNumber(Integer buildNumber) {
	this.buildNumber = buildNumber;
    }

    public Date getBuildDateTime() {
	return buildDateTime;
    }

    public void setBuildDateTime(Date buildDateTime) {
	this.buildDateTime = buildDateTime;
    }

    public ImpPlan getPlanId() {
	return planId;
    }

    public void setPlanId(ImpPlan planId) {
	this.planId = planId;
    }

    public System getSystemId() {
	return systemId;
    }

    public void setSystemId(System systemId) {
	this.systemId = systemId;
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
	if (!(object instanceof OnlineBuild)) {
	    return false;
	}
	OnlineBuild other = (OnlineBuild) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.Build[ id=" + id + " ]";
    }

}
