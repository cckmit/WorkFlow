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
@Table(name = "system_cpu")
@NamedQueries({ @NamedQuery(name = "SystemCpu.findAll", query = "SELECT s FROM SystemCpu s") })
public class SystemCpu extends BaseBeans implements Serializable, Comparable<SystemCpu> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 25)
    @Column(name = "cpu_name")
    private String cpuName;
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
    @Size(max = 1)
    @Column(name = "active")
    private String active;
    @Size(max = 2147483647)
    @Column(name = "primary_ip_address")
    private String primaryIpAddress;
    @Size(max = 2147483647)
    @Column(name = "secondary_ip_address")
    private String secondaryIpAddress;
    @Size(max = 2147483647)
    @Column(name = "cpu_type")
    private String cpuType;
    @Size(max = 25)
    @Column(name = "display_name")
    private String displayName;
    @Size(max = 25)
    @Column(name = "cpu_code")
    private String cpuCode;
    @OneToMany(mappedBy = "cpuId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ProductionLoads> productionLoadsList;
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;
    @Size(max = 5)
    @Column(name = "default_cpu")
    private String default_cpu;

    public String getDefault_cpu() {
	return default_cpu;
    }

    public void setDefault_cpu(String default_cpu) {
	this.default_cpu = default_cpu;
    }

    public SystemCpu() {
    }

    public SystemCpu(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getCpuName() {
	return cpuName;
    }

    public void setCpuName(String cpuName) {
	this.cpuName = cpuName;
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

    @Override
    public String getActive() {
	return active;
    }

    @Override
    public void setActive(String active) {
	this.active = active;
    }

    public String getPrimaryIpAddress() {
	return primaryIpAddress;
    }

    public void setPrimaryIpAddress(String primaryIpAddress) {
	this.primaryIpAddress = primaryIpAddress;
    }

    public String getSecondaryIpAddress() {
	return secondaryIpAddress;
    }

    public void setSecondaryIpAddress(String secondaryIpAddress) {
	this.secondaryIpAddress = secondaryIpAddress;
    }

    public String getCpuType() {
	return cpuType;
    }

    public void setCpuType(String cpuType) {
	this.cpuType = cpuType;
    }

    public String getDisplayName() {
	return displayName;
    }

    public void setDisplayName(String displayName) {
	this.displayName = displayName;
    }

    public String getCpuCode() {
	return cpuCode;
    }

    public void setCpuCode(String cpuCode) {
	this.cpuCode = cpuCode;
    }

    public List<ProductionLoads> getProductionLoadsList() {
	return productionLoadsList;
    }

    public void setProductionLoadsList(List<ProductionLoads> productionLoadsList) {
	this.productionLoadsList = productionLoadsList;
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
	if (!(object instanceof SystemCpu)) {
	    return false;
	}
	SystemCpu other = (SystemCpu) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.SystemCpu[ id=" + id + " ]";
    }

    @Override
    public int compareTo(SystemCpu lValue) {
	return cpuName.compareTo(lValue.getCpuName());
    }

}
