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
@Table(name = "vpars")
@NamedQueries({ @NamedQuery(name = "Vpars.findAll", query = "SELECT v FROM Vpars v") })
public class Vpars extends BaseBeans implements Serializable, Comparable<Vpars> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 10)
    @Column(name = "name")
    private String name;
    @Size(max = 15)
    @Column(name = "ipaddress")
    private String ipaddress;
    @Size(max = 4)
    @Column(name = "port")
    private String port;
    @Size(max = 20)
    @Column(name = "type")
    private String type;
    @Size(max = 1)
    @Column(name = "active")
    private String active;
    @Size(max = 50)
    @Column(name = "owner_id")
    private String ownerId;
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
    @OneToMany(mappedBy = "vparId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoadActions> systemLoadActionsList;
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;

    @Column(name = "tss_deploy")
    private Boolean tssDeploy;

    @Size(max = 5)
    @Column(name = "default_cpu")
    private String default_cpu;

    @Column(name = "qa_vpars")
    private Boolean qaVpars;

    public String getDefault_cpu() {
	return default_cpu;
    }

    public void setDefault_cpu(String default_cpu) {
	this.default_cpu = default_cpu;
    }

    public Vpars() {
    }

    public Vpars(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getOwnerId() {
	return ownerId;
    }

    public void setOwnerId(String ownerId) {
	this.ownerId = ownerId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public String getPort() {
	return port;
    }

    public void setPort(String port) {
	this.port = port;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
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

    public List<SystemLoadActions> getSystemLoadActionsList() {
	return systemLoadActionsList;
    }

    public void setSystemLoadActionsList(List<SystemLoadActions> systemLoadActionsList) {
	this.systemLoadActionsList = systemLoadActionsList;
    }

    public System getSystemId() {
	return systemId;
    }

    public void setSystemId(System systemId) {
	this.systemId = systemId;
    }

    public Boolean getTssDeploy() {
	return tssDeploy;
    }

    public void setTssDeploy(Boolean tssDeploy) {
	this.tssDeploy = tssDeploy;
    }

    public Boolean getQaVpars() {
	return qaVpars;
    }

    public void setQaVpars(Boolean qaVpars) {
	this.qaVpars = qaVpars;
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
	if (!(object instanceof Vpars)) {
	    return false;
	}
	Vpars other = (Vpars) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.Vpars[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Vpars lValue) {
	return name.compareTo(lValue.getName());
    }

}
