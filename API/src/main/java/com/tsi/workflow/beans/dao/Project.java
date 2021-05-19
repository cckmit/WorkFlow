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
@Table(name = "project")
@NamedQueries({ @NamedQuery(name = "Project.findAll", query = "SELECT p FROM Project p") })
public class Project extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "project_number")
    private String projectNumber;
    @Size(max = 2147483647)
    @Column(name = "project_name")
    private String projectName;
    @Size(max = 2147483647)
    @Column(name = "line_of_business")
    private String lineOfBusiness;
    @Size(max = 2147483647)
    @Column(name = "manager_id")
    private String managerId;
    @Size(max = 2147483647)
    @Column(name = "sponsor_id")
    private String sponsorId;
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
    @OneToMany(mappedBy = "projectId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ImpPlan> impPlanList;

    @Column(name = "is_delta")
    private Boolean isDelta;

    @Size(max = 2147483647)
    @Column(name = "maintance_flag")
    private String maintanceFlag;

    @Size(max = 2147483647)
    @Column(name = "manager_name")
    private String managerName;

    public Project() {
    }

    public Project(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getProjectNumber() {
	return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
	this.projectNumber = projectNumber;
    }

    public String getProjectName() {
	return projectName;
    }

    public void setProjectName(String projectName) {
	this.projectName = projectName;
    }

    public String getLineOfBusiness() {
	return lineOfBusiness;
    }

    public void setLineOfBusiness(String lineOfBusiness) {
	this.lineOfBusiness = lineOfBusiness;
    }

    public String getManagerId() {
	return managerId;
    }

    public void setManagerId(String managerId) {
	this.managerId = managerId;
    }

    public String getSponsorId() {
	return sponsorId;
    }

    public void setSponsorId(String sponsorId) {
	this.sponsorId = sponsorId;
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

    public List<ImpPlan> getImpPlanList() {
	return impPlanList;
    }

    public void setImpPlanList(List<ImpPlan> impPlanList) {
	this.impPlanList = impPlanList;
    }

    public Boolean getIsDelta() {
	return isDelta;
    }

    public void setIsDelta(Boolean isDelta) {
	this.isDelta = isDelta;
    }

    public String getMaintanceFlag() {
	return maintanceFlag;
    }

    public void setMaintanceFlag(String maintanceFlag) {
	this.maintanceFlag = maintanceFlag;
    }

    public String getManagerName() {
	return managerName;
    }

    public void setManagerName(String managerName) {
	this.managerName = managerName;
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
	if (!(object instanceof Project)) {
	    return false;
	}
	Project other = (Project) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.Project[ id=" + id + " ]";
    }

}
