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
@Table(name = "dbcr")
@NamedQueries({ @NamedQuery(name = "Dbcr.findAll", query = "SELECT d FROM Dbcr d") })
public class Dbcr extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 10)
    @Column(name = "dbcr_name")
    private String dbcrName;
    @Size(max = 10)
    @Column(name = "environment")
    private String environment;
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
    @Column(name = "status")
    private String status;
    @Size(max = 1)
    @Column(name = "mandatory")
    private String mandatory;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;

    public Dbcr() {
    }

    public Dbcr(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getDbcrName() {
	return dbcrName;
    }

    public void setDbcrName(String dbcrName) {
	this.dbcrName = dbcrName;
    }

    public String getEnvironment() {
	return environment;
    }

    public void setEnvironment(String environment) {
	this.environment = environment;
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

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public String getMandatory() {
	return mandatory;
    }

    public void setMandatory(String mandatory) {
	this.mandatory = mandatory;
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
	if (!(object instanceof Dbcr)) {
	    return false;
	}
	Dbcr other = (Dbcr) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.Dbcr[ id=" + id + " ]";
    }

}
