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

@Entity
@Table(name = "rfc_details")
@NamedQueries({ @NamedQuery(name = "RFCDetails.findAll", query = "SELECT s FROM RFCDetails s") })
public class RFCDetails extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "system_load_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SystemLoad systemLoadId;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @Size(max = 50)
    @Column(name = "rfc_number")
    private String rfcNumber;
    @Size(max = 2147483647)
    @Column(name = "rfc_desc")
    private String rfcDesc;
    @Size(max = 50)
    @Column(name = "impact_level")
    private String impactLevel;
    @Size(max = 50)
    @Column(name = "config_item")
    private String configItem;
    @Column(name = "vs_flag")
    private Boolean vsFlag;
    @Column(name = "vs_area")
    private String vsArea;
    @Column(name = "vs_test_flag")
    private Boolean vsTestFlag;
    @Column(name = "is_test_script_attached")
    private Boolean isTestScriptAttached;
    @Column(name = "is_business_approval_attached")
    private Boolean isBusinessApprovalAttached;
    @Column(name = "ready_to_schedule")
    private Boolean readyToSchedule;
    @Column(name = "vs_desc")
    private String vsDesc;
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

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public SystemLoad getSystemLoadId() {
	return systemLoadId;
    }

    public void setSystemLoadId(SystemLoad systemLoadId) {
	this.systemLoadId = systemLoadId;
    }

    public ImpPlan getPlanId() {
	return planId;
    }

    public void setPlanId(ImpPlan planId) {
	this.planId = planId;
    }

    public String getRfcNumber() {
	return rfcNumber;
    }

    public void setRfcNumber(String rfcNumber) {
	this.rfcNumber = rfcNumber;
    }

    public String getRfcDesc() {
	return rfcDesc;
    }

    public void setRfcDesc(String rfcDesc) {
	this.rfcDesc = rfcDesc;
    }

    public String getImpactLevel() {
	return impactLevel;
    }

    public void setImpactLevel(String impactLevel) {
	this.impactLevel = impactLevel;
    }

    public String getConfigItem() {
	return configItem;
    }

    public void setConfigItem(String configItem) {
	this.configItem = configItem;
    }

    public Boolean getVsFlag() {
	return vsFlag;
    }

    public void setVsFlag(Boolean vsFlag) {
	this.vsFlag = vsFlag;
    }

    public String getVsArea() {
	return vsArea;
    }

    public void setVsArea(String vsArea) {
	this.vsArea = vsArea;
    }

    public Boolean getVsTestFlag() {
	return vsTestFlag;
    }

    public void setVsTestFlag(Boolean vsTestFlag) {
	this.vsTestFlag = vsTestFlag;
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

    public Boolean getIsTestScriptAttached() {
	return isTestScriptAttached;
    }

    public void setIsTestScriptAttached(Boolean isTestScriptAttached) {
	this.isTestScriptAttached = isTestScriptAttached;
    }

    public Boolean getIsBusinessApprovalAttached() {
	return isBusinessApprovalAttached;
    }

    public void setIsBusinessApprovalAttached(Boolean isBusinessApprovalAttached) {
	this.isBusinessApprovalAttached = isBusinessApprovalAttached;
    }

    public Boolean getReadyToSchedule() {
	return readyToSchedule;
    }

    public void setReadyToSchedule(Boolean readyToSchedule) {
	this.readyToSchedule = readyToSchedule;
    }

    public String getVsDesc() {
	return vsDesc;
    }

    public void setVsDesc(String vsDesc) {
	this.vsDesc = vsDesc;
    }

    public boolean isRFCDetailsFilled() {
	boolean isRFCDetailsCompletd = true;
	if (getRfcNumber() == null || getRfcNumber().isEmpty()) {
	    isRFCDetailsCompletd = false;
	} else if (getImpactLevel() == null || getImpactLevel().isEmpty()) {
	    isRFCDetailsCompletd = false;
	} else if (getConfigItem() == null || getConfigItem().isEmpty()) {
	    isRFCDetailsCompletd = false;
	} else if (getVsFlag() == null) {
	    isRFCDetailsCompletd = false;
	} else if (getVsFlag()) {
	    if (getVsArea() == null || getVsArea().isEmpty()) {
		isRFCDetailsCompletd = false;
	    } else if (getVsTestFlag() == null) {
		isRFCDetailsCompletd = false;
	    } else if (getVsDesc() == null || getVsDesc().isEmpty()) {
		isRFCDetailsCompletd = false;
	    }
	}
	return isRFCDetailsCompletd;
    }
}
