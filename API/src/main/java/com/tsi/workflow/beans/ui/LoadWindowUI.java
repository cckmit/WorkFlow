/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

/**
 *
 * @author prabhu.prabhakaran
 */
public class LoadWindowUI {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String daysOfWeek;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date timeSlot;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date createdDt;
    private String createdBy;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date modifiedDt;
    private String modifiedBy;
    private String active;
    private LoadCategories loadCategoryId;

    public LoadWindowUI() {
    }

    public LoadWindowUI(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getDaysOfWeek() {
	return daysOfWeek;
    }

    public void setDaysOfWeek(String daysOfWeek) {
	this.daysOfWeek = daysOfWeek;
    }

    public Date getTimeSlot() {
	return timeSlot;
    }

    public void setTimeSlot(Date timeSlot) {
	this.timeSlot = timeSlot;
    }

    public Date getCreatedDt() {
	return createdDt;
    }

    public void setCreatedDt(Date createdDt) {
	this.createdDt = createdDt;
    }

    public String getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getModifiedDt() {
	return modifiedDt;
    }

    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    public String getModifiedBy() {
	return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
    }

    public String getActive() {
	return active;
    }

    public void setActive(String active) {
	this.active = active;
    }

    public LoadCategories getLoadCategoryId() {
	return loadCategoryId;
    }

    public void setLoadCategoryId(LoadCategories loadCategoryId) {
	this.loadCategoryId = loadCategoryId;
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
	if (!(object instanceof LoadWindowUI)) {
	    return false;
	}
	LoadWindowUI other = (LoadWindowUI) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.LoadWindowUI[ id=" + id + " ]";
    }

}
