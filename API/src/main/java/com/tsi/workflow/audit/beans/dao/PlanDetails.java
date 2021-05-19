/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.beans.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.audit.base.AuditIBeans;
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
 * @author Radha.Adhimoolam
 */
@Entity
@Table(name = "audit.plan_details")
@NamedQueries({ @NamedQuery(name = "PlanDetails.findAll", query = "SELECT p FROM PlanDetails p") })
public class PlanDetails implements Serializable, AuditIBeans {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 15)
    @Column(name = "planid")
    private String planid;
    @Size(max = 15)
    @Column(name = "impid")
    private String impid;
    @Size(max = 25)
    @Column(name = "targetsystem")
    private String targetsystem;
    @Size(max = 50)
    @Column(name = "tdx")
    private String tdx;
    @Column(name = "sbtcount")
    private Integer sbtcount;
    @Column(name = "asmcount")
    private Integer asmcount;
    @Column(name = "makcount")
    private Integer makcount;
    @Column(name = "ccppcount")
    private Integer ccppcount;
    @Column(name = "headercount")
    private Integer headercount;
    @Column(name = "totalcount")
    private Integer totalcount;
    @Column(name = "socount")
    private Integer socount;
    @Column(name = "repocount")
    private Integer repocount;
    @Size(max = 2147483647)
    @Column(name = "reponamelist")
    private String reponamelist;
    @JoinColumn(name = "tranx_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ApiTransaction tranxId;
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

    public PlanDetails() {
    }

    public PlanDetails(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getImpid() {
	return impid;
    }

    public void setImpid(String impid) {
	this.impid = impid;
    }

    public Integer getSbtcount() {
	return sbtcount;
    }

    public void setSbtcount(Integer sbtcount) {
	this.sbtcount = sbtcount;
    }

    public Integer getAsmcount() {
	return asmcount;
    }

    public void setAsmcount(Integer asmcount) {
	this.asmcount = asmcount;
    }

    public Integer getMakcount() {
	return makcount;
    }

    public void setMakcount(Integer makcount) {
	this.makcount = makcount;
    }

    public Integer getCcppcount() {
	return ccppcount;
    }

    public void setCcppcount(Integer ccppcount) {
	this.ccppcount = ccppcount;
    }

    public Integer getHeadercount() {
	return headercount;
    }

    public void setHeadercount(Integer headercount) {
	this.headercount = headercount;
    }

    public Integer getTotalcount() {
	return totalcount;
    }

    public void setTotalcount(Integer totalcount) {
	this.totalcount = totalcount;
    }

    public Integer getSocount() {
	return socount;
    }

    public void setSocount(Integer socount) {
	this.socount = socount;
    }

    public Integer getRepocount() {
	return repocount;
    }

    public void setRepocount(Integer repocount) {
	this.repocount = repocount;
    }

    public String getReponamelist() {
	return reponamelist;
    }

    public void setReponamelist(String reponamelist) {
	this.reponamelist = reponamelist;
    }

    public ApiTransaction getTranxId() {
	return tranxId;
    }

    public void setTranxId(ApiTransaction tranxId) {
	this.tranxId = tranxId;
    }

    public Date getCreatedDt() {
	return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
	this.createdDt = createdDt;
    }

    public Date getModifiedDt() {
	return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public String getTdx() {
	return tdx;
    }

    public void setTdx(String tdx) {
	this.tdx = tdx;
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
	if (!(object instanceof PlanDetails)) {
	    return false;
	}
	PlanDetails other = (PlanDetails) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.audit.beans.dao.PlanDetails[ id=" + id + " ]";
    }

}
