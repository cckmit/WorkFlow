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
import java.util.Set;
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

/**
 *
 * @author Radha.Adhimoolam
 */
@Entity
@Table(name = "audit.api_transaction")
@NamedQueries({ @NamedQuery(name = "ApiTransaction.findAll", query = "SELECT a FROM ApiTransaction a") })
public class ApiTransaction implements Serializable, AuditIBeans {

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
    @Size(max = 2147483647)
    @Column(name = "params")
    private String params;
    @Size(max = 2147483647)
    @Column(name = "message_body")
    private String messageBody;
    @Size(max = 2147483647)
    @Column(name = "response_msg")
    private String responseMsg;
    @JoinColumn(name = "actions_id", referencedColumnName = "id")
    @ManyToOne
    private ApiActions actionsId;
    @JoinColumn(name = "lnx_server_id", referencedColumnName = "id")
    @ManyToOne
    private LinuxServers lnxServerId;
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
    // @Column(name = "start_dt_long")
    // private Long startDtLong;
    @OneToMany(mappedBy = "tranxId", fetch = FetchType.LAZY)
    private Set<PlanDetails> planDetailsList;

    public ApiTransaction() {
    }

    public ApiTransaction(Integer id) {
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

    public String getParams() {
	return params;
    }

    public void setParams(String params) {
	this.params = params;
    }

    public String getMessageBody() {
	return messageBody;
    }

    public void setMessageBody(String messageBody) {
	this.messageBody = messageBody;
    }

    public String getResponseMsg() {
	return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
	this.responseMsg = responseMsg;
    }

    public ApiActions getActionsId() {
	return actionsId;
    }

    public void setActionsId(ApiActions actionsId) {
	this.actionsId = actionsId;
    }

    public LinuxServers getLnxServerId() {
	return lnxServerId;
    }

    public void setLnxServerId(LinuxServers lnxServerId) {
	this.lnxServerId = lnxServerId;
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

    // public Long getStartDtLong() {
    // return startDtLong;
    // }
    //
    // public void setStartDtLong(Long startDtLong) {
    // this.startDtLong = startDtLong;
    // }

    public Set<PlanDetails> getPlanDetailsList() {
	return planDetailsList;
    }

    public void setPlanDetailsList(Set<PlanDetails> planDetailsList) {
	this.planDetailsList = planDetailsList;
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
	if (!(object instanceof ApiTransaction)) {
	    return false;
	}
	ApiTransaction other = (ApiTransaction) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.audit.dao.ApiTransaction[ id=" + id + " ]";
    }

}
