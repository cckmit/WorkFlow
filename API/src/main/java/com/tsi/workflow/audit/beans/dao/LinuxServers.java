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
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Radha.Adhimoolam
 */
@Entity
@Table(name = "audit.linux_servers")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "LinuxServers.findAll", query = "SELECT l FROM LinuxServers l") })
public class LinuxServers implements Serializable, AuditIBeans {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 100)
    @Column(name = "dns_name")
    private String dnsName;
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
    @Size(max = 100)
    @Column(name = "host_profile")
    private String hostProfile;

    @OneToMany(mappedBy = "lnxServerId")
    private Collection<ApiTransaction> apiTransactionCollection;

    public LinuxServers() {
    }

    public LinuxServers(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getDnsName() {
	return dnsName;
    }

    public void setDnsName(String dnsName) {
	this.dnsName = dnsName;
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

    public String getHostProfile() {
	return hostProfile;
    }

    public void setHostProfile(String hostProfile) {
	this.hostProfile = hostProfile;
    }

    @XmlTransient
    public Collection<ApiTransaction> getApiTransactionCollection() {
	return apiTransactionCollection;
    }

    public void setApiTransactionCollection(Collection<ApiTransaction> apiTransactionCollection) {
	this.apiTransactionCollection = apiTransactionCollection;
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
	if (!(object instanceof LinuxServers)) {
	    return false;
	}
	LinuxServers other = (LinuxServers) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.audit.dao.LinuxServers[ id=" + id + " ]";
    }
}
