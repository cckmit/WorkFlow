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
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Filter;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "put_level")
@NamedQueries({ @NamedQuery(name = "PutLevel.findAll", query = "SELECT p FROM PutLevel p") })
public class PutLevel extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(max = 10)
    @Column(name = "put_level")
    private String putLevel;

    @Column(name = "put_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date putDateTime;

    @Size(max = 2147483647)
    @Column(name = "scm_url")
    private String scmUrl;

    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;

    @Column(name = "starting_sequence")
    @Size(max = 2147483647)
    private String startingSequence;

    @Column(name = "ending_sequence")
    @Size(max = 2147483647)
    private String endingSequence;

    @Column(name = "owner_ids")
    @Size(max = 2147483647)
    private String ownerids;

    @Column(name = "owner_names")
    @Size(max = 2147483647)
    private String ownerNames;

    @Column(name = "status")
    @Size(max = 2147483647)
    private String status;

    @Size(max = 1)
    @Column(name = "active")
    private String active;
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @Size(max = 50)
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date createdDt;
    @Column(name = "modified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date modifiedDt;

    @OneToMany(mappedBy = "putLevelId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoad> systemLoadList;

    @Column(name = "deployment_date_mail_flag")
    private Boolean deploymentDateMailFlag;

    public PutLevel() {
    }

    public String getStartingSequence() {
	return startingSequence;
    }

    public void setStartingSequence(String startingSequence) {
	this.startingSequence = startingSequence;
    }

    public String getEndingSequence() {
	return endingSequence;
    }

    public void setEndingSequence(String endingSequence) {
	this.endingSequence = endingSequence;
    }

    public String getOwnerids() {
	return ownerids;
    }

    public void setOwnerids(String ownerids) {
	this.ownerids = ownerids;
    }

    public String getOwnerNames() {
	return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
	this.ownerNames = ownerNames;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public PutLevel(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getPutLevel() {
	return putLevel;
    }

    public void setPutLevel(String putLevel) {
	this.putLevel = putLevel;
    }

    public Date getPutDateTime() {
	return putDateTime;
    }

    public void setPutDateTime(Date putDateTime) {
	this.putDateTime = putDateTime;
    }

    public String getScmUrl() {
	return scmUrl;
    }

    public void setScmUrl(String scmUrl) {
	this.scmUrl = scmUrl;
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
    public String getModifiedBy() {
	return modifiedBy;
    }

    @Override
    public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
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
    public Date getModifiedDt() {
	return modifiedDt;
    }

    @Override
    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    public List<SystemLoad> getSystemLoadList() {
	return systemLoadList;
    }

    public void setSystemLoadList(List<SystemLoad> systemLoadList) {
	this.systemLoadList = systemLoadList;
    }

    public System getSystemId() {
	return systemId;
    }

    public void setSystemId(System systemId) {
	this.systemId = systemId;
    }

    public Boolean getDeploymentDateMailFlag() {
	return deploymentDateMailFlag;
    }

    public void setDeploymentDateMailFlag(Boolean deploymentDateMailFlag) {
	this.deploymentDateMailFlag = deploymentDateMailFlag;
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
	if (!(object instanceof PutLevel)) {
	    return false;
	}
	PutLevel other = (PutLevel) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.PutLevel[ id=" + id + " ]";
    }

}
