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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Radha.Adhimoolam
 */
@Entity
@Table(name = "audit.api_actions")
@NamedQueries({ @NamedQuery(name = "ApiActions.findAll", query = "SELECT a FROM ApiActions a") })
public class ApiActions implements Serializable, AuditIBeans {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "action_url")
    private String actionUrl;
    @Size(max = 50)
    @Column(name = "action_method")
    private String actionMethod;
    @Size(max = 50)
    @Column(name = "action_name")
    private String actionName;
    @Column(name = "is_schedular")
    private Boolean isSchedular;
    @OneToMany(mappedBy = "actionsId")
    private Collection<ApiTransaction> apiTransactionCollection;
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

    @Column(name = "active")
    private String active;

    @Column(name = "info_level")
    private String infoLevel;

    public ApiActions() {
    }

    public ApiActions(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getActionUrl() {
	return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
	this.actionUrl = actionUrl;
    }

    public String getActionMethod() {
	return actionMethod;
    }

    public void setActionMethod(String actionMethod) {
	this.actionMethod = actionMethod;
    }

    public String getActionName() {
	return actionName;
    }

    public void setActionName(String actionName) {
	this.actionName = actionName;
    }

    public Boolean getIsSchedular() {
	return isSchedular;
    }

    public void setIsSchedular(Boolean isSchedular) {
	this.isSchedular = isSchedular;
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

    public String getInfoLevel() {
	return infoLevel;
    }

    public void setInfoLevel(String infoLevel) {
	this.infoLevel = infoLevel;
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
	if (!(object instanceof ApiActions)) {
	    return false;
	}
	ApiActions other = (ApiActions) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.audit.dao.ApiActions[ id=" + id + " ]";
    }

    public String getActive() {
	return active;
    }

    public void setActive(String active) {
	this.active = active;
    }

}
