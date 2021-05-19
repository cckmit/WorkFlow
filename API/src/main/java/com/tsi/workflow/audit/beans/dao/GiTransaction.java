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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Radha.Adhimoolam
 */
@Entity
@Table(name = "audit.gi_transaction")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "GiTransaction.findAll", query = "SELECT g FROM GiTransaction g") })
public class GiTransaction implements Serializable, AuditIBeans {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "started_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startedDt;
    @Column(name = "end_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDt;
    @Size(max = 50)
    @Column(name = "user_name")
    private String userName;
    @Size(max = 50)
    @Column(name = "user_id")
    private String userId;
    @Size(max = 50)
    @Column(name = "user_role")
    private String userRole;
    @Column(name = "response_time")
    private Long responseTime;
    @Size(max = 50)
    @Column(name = "plan_id")
    private String planId;
    @Size(max = 50)
    @Column(name = "impl_id")
    private String implId;
    @Size(max = 25)
    @Column(name = "target_system")
    private String targetSystem;
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
    @Size(max = 2147483647)
    @Column(name = "segments")
    private String segments;
    @Size(max = 2147483647)
    @Column(name = "message")
    private String message;
    @Size(max = 2147483647)
    @Column(name = "action")
    private String action;
    @JoinColumn(name = "actions_id", referencedColumnName = "id")
    @ManyToOne
    private ApiActions actionsId;

    public GiTransaction() {
    }

    public GiTransaction(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public Date getStartedDt() {
	return startedDt;
    }

    public void setStartedDt(Date startedDt) {
	this.startedDt = startedDt;
    }

    public Date getEndDt() {
	return endDt;
    }

    public void setEndDt(Date endDt) {
	this.endDt = endDt;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getUserRole() {
	return userRole;
    }

    public void setUserRole(String userRole) {
	this.userRole = userRole;
    }

    public Long getResponseTime() {
	return responseTime;
    }

    public void setResponseTime(Long responseTime) {
	this.responseTime = responseTime;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getImplId() {
	return implId;
    }

    public void setImplId(String implId) {
	this.implId = implId;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
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

    public String getSegments() {
	return segments;
    }

    public void setSegments(String segments) {
	this.segments = segments;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public ApiActions getActionsId() {
	return actionsId;
    }

    public void setActionsId(ApiActions actionsId) {
	this.actionsId = actionsId;
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
	if (!(object instanceof GiTransaction)) {
	    return false;
	}
	GiTransaction other = (GiTransaction) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.audit.dao.GiTransaction[ id=" + id + " ]";
    }

}
