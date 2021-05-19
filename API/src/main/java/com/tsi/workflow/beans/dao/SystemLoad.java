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
@Table(name = "system_load")
@NamedQueries({ @NamedQuery(name = "SystemLoad.findAll", query = "SELECT s FROM SystemLoad s") })
public class SystemLoad extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 2147483647)
    @Column(name = "load_scr")
    private String loadScr;
    @Size(max = 2147483647)
    @Column(name = "preload")
    private String preload;
    @Size(max = 2147483647)
    @Column(name = "preload_desc")
    private String preloadDesc;
    @Size(max = 2147483647)
    @Column(name = "flbk_scr")
    private String flbkScr;
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
    @Size(max = 2147483647)
    @Column(name = "load_attendee")
    private String loadAttendee;
    @Size(max = 2147483647)
    @Column(name = "preload_just")
    private String preloadJust;
    @Size(max = 2147483647)
    @Column(name = "load_scr_desc")
    private String loadScrDesc;
    @Size(max = 2147483647)
    @Column(name = "flbk_scr_desc")
    private String flbkScrDesc;
    @Column(name = "load_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loadDateTime;
    @Size(max = 25)
    @Column(name = "load_set_name")
    private String loadSetName;
    @Size(max = 25)
    @Column(name = "fallback_load_set_name")
    private String fallbackLoadSetName;
    @Size(max = 2147483647)
    @Column(name = "prod_load_status")
    private String prodLoadStatus;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @JoinColumn(name = "load_category_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private LoadCategories loadCategoryId;
    @JoinColumn(name = "put_level_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PutLevel putLevelId;
    @JoinColumn(name = "system_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private System systemId;
    @OneToMany(mappedBy = "systemLoadId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoadActions> systemLoadActionsList;
    @OneToMany(mappedBy = "systemLoadId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ProductionLoads> productionLoadsList;
    @OneToMany(mappedBy = "systemLoad", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<CheckoutSegments> checkoutSegmentsList;
    @Size(max = 2147483647)
    @Column(name = "load_instruction")
    private String loadInstruction;
    @Size(max = 2147483647)
    @Column(name = "qa_bypass_status")
    private String qaBypassStatus;
    @Size(max = 2147483647)
    @Column(name = "qa_functional_bypass_comment")
    private String qaFunctionalBypassComment;
    @Size(max = 2147483647)
    @Column(name = "qa_regression_bypass_comment")
    private String qaRegressionBypassComment;
    @OneToMany(mappedBy = "systemLoadId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemPdddsMapping> systemPdddsMappingList;
    @Size(max = 2147483647)
    @Column(name = "load_attendee_id")
    private String loadAttendeeId;

    @Size(max = 2147483647)
    @Column(name = "special_instruction_qa")
    private String specialInstructionQA;

    @Size(max = 2147483647)
    @Column(name = "qa_functional_testers")
    private String qaFunctionalTesters;

    @Size(max = 2147483647)
    @Column(name = "qa_functional_tester_name")
    private String qaFunctionalTesterName;

    @Size(max = 2147483647)
    @Column(name = "pre_prod_load_status")
    private String preProdLoadStatus;

    @Size(max = 2147483647)
    @Column(name = "ipl_required")
    private String iplRequired;

    @Size(max = 12)
    @Column(name = "load_attendee_contact")
    private String loadAttendeeContact;
    @Column(name = "derived_segments_moved_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date derivedSegmentsMovedDt;

    @Column(name = "load_date_mail_flag")
    private Boolean loadDateMailFlag;

    public SystemLoad() {
    }

    public SystemLoad(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getLoadScr() {
	return loadScr;
    }

    public void setLoadScr(String loadScr) {
	this.loadScr = loadScr;
    }

    public String getPreload() {
	return preload;
    }

    public void setPreload(String preload) {
	this.preload = preload;
    }

    public String getPreloadDesc() {
	return preloadDesc;
    }

    public void setPreloadDesc(String preloadDesc) {
	this.preloadDesc = preloadDesc;
    }

    public String getFlbkScr() {
	return flbkScr;
    }

    public void setFlbkScr(String flbkScr) {
	this.flbkScr = flbkScr;
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

    public String getLoadAttendee() {
	return loadAttendee;
    }

    public void setLoadAttendee(String loadAttendee) {
	this.loadAttendee = loadAttendee;
    }

    public String getPreloadJust() {
	return preloadJust;
    }

    public void setPreloadJust(String preloadJust) {
	this.preloadJust = preloadJust;
    }

    public String getLoadScrDesc() {
	return loadScrDesc;
    }

    public void setLoadScrDesc(String loadScrDesc) {
	this.loadScrDesc = loadScrDesc;
    }

    public String getFlbkScrDesc() {
	return flbkScrDesc;
    }

    public void setFlbkScrDesc(String flbkScrDesc) {
	this.flbkScrDesc = flbkScrDesc;
    }

    public Date getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(Date loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    public String getLoadSetName() {
	return loadSetName;
    }

    public void setLoadSetName(String loadSetName) {
	this.loadSetName = loadSetName;
    }

    public String getFallbackLoadSetName() {
	return fallbackLoadSetName;
    }

    public void setFallbackLoadSetName(String fallbackLoadSetName) {
	this.fallbackLoadSetName = fallbackLoadSetName;
    }

    public ImpPlan getPlanId() {
	return planId;
    }

    public void setPlanId(ImpPlan planId) {
	this.planId = planId;
    }

    public LoadCategories getLoadCategoryId() {
	return loadCategoryId;
    }

    public void setLoadCategoryId(LoadCategories loadCategoryId) {
	this.loadCategoryId = loadCategoryId;
    }

    public PutLevel getPutLevelId() {
	return putLevelId;
    }

    public void setPutLevelId(PutLevel putLevelId) {
	this.putLevelId = putLevelId;
    }

    public System getSystemId() {
	return systemId;
    }

    public void setSystemId(System systemId) {
	this.systemId = systemId;
    }

    public List<SystemLoadActions> getSystemLoadActionsList() {
	return systemLoadActionsList;
    }

    public void setSystemLoadActionsList(List<SystemLoadActions> systemLoadActionsList) {
	this.systemLoadActionsList = systemLoadActionsList;
    }

    public List<ProductionLoads> getProductionLoadsList() {
	return productionLoadsList;
    }

    public void setProductionLoadsList(List<ProductionLoads> productionLoadsList) {
	this.productionLoadsList = productionLoadsList;
    }

    public List<CheckoutSegments> getCheckoutSegmentsList() {
	return checkoutSegmentsList;
    }

    public void setCheckoutSegmentsList(List<CheckoutSegments> checkoutSegmentsList) {
	this.checkoutSegmentsList = checkoutSegmentsList;
    }

    public String getProdLoadStatus() {
	return prodLoadStatus;
    }

    public void setProdLoadStatus(String prodLoadStatus) {
	this.prodLoadStatus = prodLoadStatus;
    }

    public String getLoadInstruction() {
	return loadInstruction;
    }

    public void setLoadInstruction(String loadInstruction) {
	this.loadInstruction = loadInstruction;
    }

    public String getQaBypassStatus() {
	return qaBypassStatus;
    }

    public void setQaBypassStatus(String qaBypassStatus) {
	this.qaBypassStatus = qaBypassStatus;
    }

    public String getQaFunctionalBypassComment() {
	return qaFunctionalBypassComment;
    }

    public void setQaFunctionalBypassComment(String qaFunctionalBypassComment) {
	this.qaFunctionalBypassComment = qaFunctionalBypassComment;
    }

    public String getQaRegressionBypassComment() {
	return qaRegressionBypassComment;
    }

    public void setQaRegressionBypassComment(String qaRegressionBypassComment) {
	this.qaRegressionBypassComment = qaRegressionBypassComment;
    }

    public List<SystemPdddsMapping> getSystemPdddsMappingList() {
	return systemPdddsMappingList;
    }

    public void setSystemPdddsMappingList(List<SystemPdddsMapping> systemPdddsMappingList) {
	this.systemPdddsMappingList = systemPdddsMappingList;
    }

    public String getLoadAttendeeId() {
	return loadAttendeeId;
    }

    public void setLoadAttendeeId(String loadAttendeeId) {
	this.loadAttendeeId = loadAttendeeId;
    }

    // ZTPFM-1455 Implemented
    public String getSpecialInstructionQA() {
	return specialInstructionQA;
    }

    public void setSpecialInstructionQA(String specialInstructionQA) {
	this.specialInstructionQA = specialInstructionQA;
    }

    public String getQaFunctionalTesters() {
	return qaFunctionalTesters;
    }

    public void setQaFunctionalTesters(String qaFunctionalTesters) {
	this.qaFunctionalTesters = qaFunctionalTesters;
    }

    public String getQaFunctionalTesterName() {
	return qaFunctionalTesterName;
    }

    public void setQaFunctionalTesterName(String qaFunctionalTesterName) {
	this.qaFunctionalTesterName = qaFunctionalTesterName;
    }

    public String getPreProdLoadStatus() {
	return preProdLoadStatus;
    }

    public void setPreProdLoadStatus(String preProdLoadStatus) {
	this.preProdLoadStatus = preProdLoadStatus;
    }

    public String getIplRequired() {
	return iplRequired;
    }

    public void setIplRequired(String iplRequired) {
	this.iplRequired = iplRequired;
    }

    public String getLoadAttendeeContact() {
	return loadAttendeeContact;
    }

    public void setLoadAttendeeContact(String loadAttendeeContact) {
	this.loadAttendeeContact = loadAttendeeContact;
    }

    public Date getDerivedSegmentsMovedDt() {
	return derivedSegmentsMovedDt;
    }

    public void setDerivedSegmentsMovedDt(Date derivedSegmentsMovedDt) {
	this.derivedSegmentsMovedDt = derivedSegmentsMovedDt;
    }

    public Boolean getLoadDateMailFlag() {
	return loadDateMailFlag;
    }

    public void setLoadDateMailFlag(Boolean loadDateMailFlag) {
	this.loadDateMailFlag = loadDateMailFlag;
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
	if (!(object instanceof SystemLoad)) {
	    return false;
	}
	SystemLoad other = (SystemLoad) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.SystemLoad[ id=" + id + " ]";
    }

}
