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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Filter;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "implementation")
@NamedQueries({ @NamedQuery(name = "Implementation.findAll", query = "SELECT i FROM Implementation i") })
public class Implementation extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "id")
    private String id;
    @Size(max = 2147483647)
    @Column(name = "dev_id")
    private String devId;
    @Size(max = 2147483647)
    @Column(name = "dev_name")
    private String devName;
    @Size(max = 2147483647)
    @Column(name = "dev_contact")
    private String devContact;
    @Size(max = 2147483647)
    @Column(name = "dev_location")
    private String devLocation;
    @Size(max = 2147483647)
    @Column(name = "imp_desc")
    private String impDesc;
    @Size(max = 2147483647)
    @Column(name = "tkt_num")
    private String tktNum;
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
    @Column(name = "peer_review")
    private String peerReview;
    @Size(max = 2147483647)
    @Column(name = "peer_reviewers")
    private String peerReviewers;
    @Size(max = 2147483647)
    @Column(name = "reviewers_done")
    private String reviewersDone;
    @Size(max = 2147483647)
    @Column(name = "peer_reviewers_name")
    private String peerReviewersName;
    @Size(max = 2147483647)
    @Column(name = "prod_ver")
    private String prodVer;
    @Size(max = 2147483647)
    @Column(name = "dbcr_ref")
    private String dbcrRef;
    @Size(max = 2147483647)
    @Column(name = "imp_status")
    private String impStatus;
    @Size(max = 10)
    @Column(name = "process_id")
    private String processId;
    @Size(max = 2147483647)
    @Column(name = "substatus")
    private String substatus;
    @Size(max = 10)
    @Column(name = "reassign_flag")
    private String reassignFlag;
    @Size(max = 2147483647)
    @Column(name = "tkt_url")
    private String tktUrl;
    @Column(name = "checkin_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date checkinDateTime;
    @Size(max = 2147483647)
    @Column(name = "pr_tkt_num")
    private String prTktNum;
    @Column(name = "is_checkin")
    private Boolean isCheckedin;
    @Column(name = "bypass_peer_review")
    private Boolean bypassPeerReview;
    @Column(name = "last_checkin_status")
    private String lastCheckinStatus;
    @JoinColumn(name = "plan_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ImpPlan planId;
    @OneToMany(mappedBy = "impId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<CheckoutSegments> checkoutSegmentsList;
    @OneToMany(mappedBy = "impId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ActivityLog> activityLogList;
    @Column(name = "review_request_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date peerReviewRequestDateTime;
    @Column(name = "review_mail_flag")
    private Boolean reviewMailFlag;

    public Boolean getReviewMailFlag() {
	return reviewMailFlag;
    }

    public void setReviewMailFlag(Boolean reviewMailFlag) {
	this.reviewMailFlag = reviewMailFlag;
    }

    public Date getPeerReviewRequestDateTime() {
	return peerReviewRequestDateTime;
    }

    public void setPeerReviewRequestDateTime(Date peerReviewRequestDateTime) {
	this.peerReviewRequestDateTime = peerReviewRequestDateTime;
    }

    public Implementation() {
    }

    public Implementation(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Boolean getBypassPeerReview() {
	return bypassPeerReview;
    }

    public void setBypassPeerReview(Boolean bypassPeerReview) {
	this.bypassPeerReview = bypassPeerReview;
    }

    public String getDevId() {
	return devId;
    }

    public void setDevId(String devId) {
	this.devId = devId;
    }

    public String getDevName() {
	return devName;
    }

    public void setDevName(String devName) {
	this.devName = devName;
    }

    public String getDevContact() {
	return devContact;
    }

    public void setDevContact(String devContact) {
	this.devContact = devContact;
    }

    public String getDevLocation() {
	return devLocation;
    }

    public void setDevLocation(String devLocation) {
	this.devLocation = devLocation;
    }

    public String getImpDesc() {
	return impDesc;
    }

    public void setImpDesc(String impDesc) {
	this.impDesc = impDesc;
    }

    public String getTktNum() {
	return tktNum;
    }

    public void setTktNum(String tktNum) {
	this.tktNum = tktNum;
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

    public String getPeerReview() {
	return peerReview;
    }

    public void setPeerReview(String peerReview) {
	this.peerReview = peerReview;
    }

    public String getReviewersDone() {
	return reviewersDone;
    }

    public void setReviewersDone(String reviewersDone) {
	this.reviewersDone = reviewersDone;
    }

    public String getPeerReviewers() {
	return peerReviewers;
    }

    public void setPeerReviewers(String peerReviewers) {
	this.peerReviewers = peerReviewers;
    }

    public String getPeerReviewersName() {
	return peerReviewersName;
    }

    public void setPeerReviewersName(String peerReviewersName) {
	this.peerReviewersName = peerReviewersName;
    }

    public String getProdVer() {
	return prodVer;
    }

    public void setProdVer(String prodVer) {
	this.prodVer = prodVer;
    }

    public String getDbcrRef() {
	return dbcrRef;
    }

    public void setDbcrRef(String dbcrRef) {
	this.dbcrRef = dbcrRef;
    }

    public String getImpStatus() {
	return impStatus;
    }

    public void setImpStatus(String impStatus) {
	this.impStatus = impStatus;
    }

    public String getProcessId() {
	return processId;
    }

    public void setProcessId(String processId) {
	this.processId = processId;
    }

    public String getSubstatus() {
	return substatus;
    }

    public void setSubstatus(String substatus) {
	this.substatus = substatus;
    }

    public String getReassignFlag() {
	return reassignFlag;
    }

    public void setReassignFlag(String reassignFlag) {
	this.reassignFlag = reassignFlag;
    }

    public String getTktUrl() {
	return tktUrl;
    }

    public void setTktUrl(String tktUrl) {
	this.tktUrl = tktUrl;
    }

    public Date getCheckinDateTime() {
	return checkinDateTime;
    }

    public void setCheckinDateTime(Date checkinDateTime) {
	this.checkinDateTime = checkinDateTime;
    }

    public String getPrTktNum() {
	return prTktNum;
    }

    public void setPrTktNum(String prTktNum) {
	this.prTktNum = prTktNum;
    }

    public ImpPlan getPlanId() {
	return planId;
    }

    public void setPlanId(ImpPlan planId) {
	this.planId = planId;
    }

    public List<CheckoutSegments> getCheckoutSegmentsList() {
	return checkoutSegmentsList;
    }

    public void setCheckoutSegmentsList(List<CheckoutSegments> checkoutSegmentsList) {
	this.checkoutSegmentsList = checkoutSegmentsList;
    }

    public List<ActivityLog> getActivityLogList() {
	return activityLogList;
    }

    public void setActivityLogList(List<ActivityLog> activityLogList) {
	this.activityLogList = activityLogList;
    }

    public Boolean getIsCheckedin() {
	return isCheckedin;
    }

    public void setIsCheckedin(Boolean isCheckedin) {
	this.isCheckedin = isCheckedin;
    }

    public String getLastCheckinStatus() {
	return lastCheckinStatus;
    }

    public void setLastCheckinStatus(String lastCheckinStatus) {
	this.lastCheckinStatus = lastCheckinStatus;
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
	if (!(object instanceof Implementation)) {
	    return false;
	}
	Implementation other = (Implementation) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.Implementation[ id=" + id + " ]";
    }

}
