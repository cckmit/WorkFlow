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
@Table(name = "system_load_actions")
@NamedQueries({ @NamedQuery(name = "SystemLoadActions.findAll", query = "SELECT s FROM SystemLoadActions s") })
public class SystemLoadActions extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "status")
    private String status;
    @Column(name = "activated_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date activatedDateTime;
    @Column(name = "de_activated_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date deActivatedDateTime;
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
    @Size(max = 2147483647)
    @Column(name = "last_action_status")
    private String lastActionStatus;
    @Size(max = 2147483647)
    @Column(name = "test_status")
    private String testStatus;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;
    @JoinColumn(name = "system_load_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SystemLoad systemLoadId;
    @JoinColumn(name = "vpar_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vpars vparId;
    @Column(name = "is_auto_deploy")
    private Boolean isAutoDeploy;
    // ZTPFM-1497 Code removed
    @Size(max = 1)
    @Column(name = "dsl_update")
    private String dslUpdate;

    @Column(name = "is_vpar_activated")
    private Boolean isVparActivated;

    public Boolean getIsAutoDeploy() {
	return isAutoDeploy;
    }

    public void setIsAutoDeploy(Boolean isAutoDeploy) {
	this.isAutoDeploy = isAutoDeploy;
    }

    public Boolean getIsVparActivated() {
	return isVparActivated;
    }

    public void setIsVparActivated(Boolean isVparActivated) {
	this.isVparActivated = isVparActivated;
    }

    public SystemLoadActions() {
    }

    public SystemLoadActions(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Date getActivatedDateTime() {
	return activatedDateTime;
    }

    public void setActivatedDateTime(Date activatedDateTime) {
	this.activatedDateTime = activatedDateTime;
    }

    public Date getDeActivatedDateTime() {
	return deActivatedDateTime;
    }

    public void setDeActivatedDateTime(Date deActivatedDateTime) {
	this.deActivatedDateTime = deActivatedDateTime;
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

    public String getLastActionStatus() {
	return lastActionStatus;
    }

    public void setLastActionStatus(String lastActionStatus) {
	this.lastActionStatus = lastActionStatus;
    }

    public String getTestStatus() {
	return testStatus;
    }

    public void setTestStatus(String testStatus) {
	this.testStatus = testStatus;
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

    public SystemLoad getSystemLoadId() {
	return systemLoadId;
    }

    public void setSystemLoadId(SystemLoad systemLoadId) {
	this.systemLoadId = systemLoadId;
    }

    public Vpars getVparId() {
	return vparId;
    }

    public void setVparId(Vpars vparId) {
	this.vparId = vparId;
    }

    // ZTPFM-1497 Code removed
    public String getDslUpdate() {
	return dslUpdate;
    }

    public void setDslUpdate(String dslUpdate) {
	this.dslUpdate = dslUpdate;
    }
    // ZTPFM-1497 changes completed

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof SystemLoadActions)) {
	    return false;
	}
	SystemLoadActions other = (SystemLoadActions) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.SystemLoadActions[ id=" + id + " ]";
    }

}
