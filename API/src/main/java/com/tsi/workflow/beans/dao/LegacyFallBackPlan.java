/**
 * 
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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 * @author vinoth.ponnurangan
 *
 */
@Entity
@Table(name = "legacy_fallback_plan")
@NamedQueries({ @NamedQuery(name = "LegacyFallBackPlan.findAll", query = "SELECT i FROM LegacyFallBackPlan i") })
public class LegacyFallBackPlan extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "load_date_time")
    private String loadDateTime;

    @Size(max = 2147483647)
    @Column(name = "plan_id")
    private String planId;

    @Size(max = 2147483647)
    @Column(name = "program_name")
    private String programName;

    @Size(max = 2147483647)
    @Column(name = "target_system")
    private String targetSystem;

    @Size(max = 2147483647)
    @Column(name = "func_area")
    private String funcArea;

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

    @Column(name = "devops_plan")
    private String devopsPlan;

    @Size(max = 1)
    @Column(name = "active")
    private String active;

    public LegacyFallBackPlan() {
    }

    public LegacyFallBackPlan(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getProgramName() {
	return programName;
    }

    public void setProgramName(String programName) {
	this.programName = programName;
    }

    public String getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(String loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getCreatedDt() {
	return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
	this.createdDt = createdDt;
    }

    public String getModifiedBy() {
	return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDt() {
	return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    public String getActive() {
	return active;
    }

    public void setActive(String active) {
	this.active = active;
    }

    public String getFuncArea() {
	return funcArea;
    }

    public void setFuncArea(String funcArea) {
	this.funcArea = funcArea;
    }

    public String getDevopsPlan() {
	return devopsPlan;
    }

    public void setDevopsPlan(String devopsPlan) {
	this.devopsPlan = devopsPlan;
    }

    @Override
    public String toString() {
	return "LegacyFallBackPlan [id=" + id + ", loadDateTime=" + loadDateTime + ", planId=" + planId + ", programName=" + programName + ", targetSystem=" + targetSystem + ", createdBy=" + createdBy + ", createdDt=" + createdDt + ", modifiedBy=" + modifiedBy + ", modifiedDt=" + modifiedDt + ", active=" + active + "]";
    }

}
