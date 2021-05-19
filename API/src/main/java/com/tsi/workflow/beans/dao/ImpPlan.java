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
import java.util.Set;
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
@Table(name = "imp_plan")
@NamedQueries({ @NamedQuery(name = "ImpPlan.findAll", query = "SELECT i FROM ImpPlan i") })
public class ImpPlan extends BaseBeans implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "id")
    private String id;
    @Size(max = 2147483647)
    @Column(name = "plan_desc")
    private String planDesc;
    @Size(max = 2147483647)
    @Column(name = "approver")
    private String approver;
    @Size(max = 2147483647)
    @Column(name = "approver_name")
    private String approverName;
    @Size(max = 2147483647)
    @Column(name = "delegate_id")
    private String delegateId;
    @Size(max = 2147483647)
    @Column(name = "delegate_name")
    private String delegateName;
    @Size(max = 10)
    @Column(name = "load_type")
    private String loadType;
    @Size(max = 2147483647)
    @Column(name = "lead_id")
    private String leadId;
    @Size(max = 2147483647)
    @Column(name = "lead_name")
    private String leadName;
    @Size(max = 12)
    @Column(name = "lead_contact")
    private String leadContact;
    @Size(max = 2147483647)
    @Column(name = "cls_comment")
    private String clsComment;
    @Size(max = 2147483647)
    @Column(name = "dev_mgr_comment")
    private String devMgrComment;
    @Size(max = 2147483647)
    @Column(name = "sdm_tkt_num")
    private String sdmTktNum;
    @Size(max = 2147483647)
    @Column(name = "related_plans")
    private String relatedPlans;
    @Size(max = 100)
    @Column(name = "oth_contact")
    private String othContact;
    @Size(max = 2147483647)
    @Column(name = "mgmt_comment")
    private String mgmtComment;
    @Size(max = 20)
    @Column(name = "load_attendee")
    private String loadAttendee;
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
    @Column(name = "plan_status")
    private String planStatus;
    @Size(max = 2147483647)
    @Column(name = "prod_ver")
    private String prodVer;
    @Size(max = 10)
    @Column(name = "process_id")
    private String processId;
    @Size(max = 2147483647)
    @Column(name = "dev_manager")
    private String devManager;
    @Size(max = 2147483647)
    @Column(name = "dev_manager_name")
    private String devManagerName;
    @Column(name = "approve_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date approveDateTime;
    @Column(name = "dev_mgr_approve_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date devMgrApproveDateTime;
    @Column(name = "accepted_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date acceptedDateTime;
    @Column(name = "fallback_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date fallbackDateTime;
    @Column(name = "macro_header")
    private Boolean macroHeader;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<Dbcr> dbcrList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<Implementation> implementationList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoad> systemLoadList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoadActions> systemLoadActionsList;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Project projectId;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ProductionLoads> productionLoadsList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<CheckoutSegments> checkoutSegmentsList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ActivityLog> activityLogList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<Build> buildList;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ImpPlanApprovals> impPlanApprovalsList;

    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private Set<PreProductionLoads> preProductionLoadsList;

    @Size(max = 50)
    @Column(name = "qa_bypass_status")
    private String qaBypassStatus;
    @OneToMany(mappedBy = "planId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemPdddsMapping> systemPdddsMappingList;
    @Column(name = "load_date_mail_flag")
    private Boolean loadDateMailFlag;

    @Column(name = "aux_type")
    private Boolean auxtype;
    @Column(name = "stackholder_email")
    private String stackHolderEmail;

    @Column(name = "load_date_macro_mail_flag")
    private Boolean loadDateMacroMailFlag;

    @Column(name = "deactivate_tsd_mail_flag")
    private Boolean deactivateTSDMailFlag;

    @Column(name = "inprogress_status")
    private String inprogressStatus;

    @Column(name = "is_accept_enabled")
    private Boolean isAcceptEnabled;

    @Column(name = "approve_request_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date approveRequestDateTime;

    @Column(name = "approve_mail_flag")
    private Boolean approveMailFlag;

    @Column(name = "deployed_mail_flag")
    private Boolean deployedMailFlag;
    @Column(name = "rfc_flag")
    private Boolean rfcFlag;

    @Column(name = "rfc_mail_flag")
    private Boolean rfcMailFlag;

    @Column(name = "full_build_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date fullBuildDt;

    @Column(name = "rejected_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date rejectedDateTime;

    @Column(name = "tpf_accepted_date_time")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date tpfAcceptedDateTime;

    @Size(max = 2147483647)
    @Column(name = "lead_email")
    private String leadEmail;

    public Date getFullBuildDt() {
	return fullBuildDt;
    }

    public void setFullBuildDt(Date fullBuildDt) {
	this.fullBuildDt = fullBuildDt;
    }

    public Date getApproveRequestDateTime() {
	return approveRequestDateTime;
    }

    public void setApproveRequestDateTime(Date approveRequestDateTime) {
	this.approveRequestDateTime = approveRequestDateTime;
    }

    public Boolean getApproveMailFlag() {
	return approveMailFlag;
    }

    public void setApproveMailFlag(Boolean approveMailFlag) {
	this.approveMailFlag = approveMailFlag;
    }

    @Size(max = 2147483647)
    @Column(name = "deployment_status")
    private String deploymentStatus;

    public ImpPlan() {
    }

    public ImpPlan(String id) {
	this.id = id;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getPlanDesc() {
	return planDesc;
    }

    public void setPlanDesc(String planDesc) {
	this.planDesc = planDesc;
    }

    public String getDelegateId() {
	return delegateId;
    }

    public void setDelegateId(String delegateId) {
	this.delegateId = delegateId;
    }

    public String getDelegateName() {
	return delegateName;
    }

    public void setDelegateName(String delegateName) {
	this.delegateName = delegateName;
    }

    public String getApprover() {
	return approver;
    }

    public void setApprover(String approver) {
	this.approver = approver;
    }

    public String getApproverName() {
	return approverName;
    }

    public void setApproverName(String approverName) {
	this.approverName = approverName;
    }

    public String getLoadType() {
	return loadType;
    }

    public void setLoadType(String loadType) {
	this.loadType = loadType;
    }

    public String getLeadId() {
	return leadId;
    }

    public void setLeadId(String leadId) {
	this.leadId = leadId;
    }

    public String getLeadName() {
	return leadName;
    }

    public void setLeadName(String leadName) {
	this.leadName = leadName;
    }

    public String getLeadContact() {
	return leadContact;
    }

    public void setLeadContact(String leadContact) {
	this.leadContact = leadContact;
    }

    public String getClsComment() {
	return clsComment;
    }

    public void setClsComment(String clsComment) {
	this.clsComment = clsComment;
    }

    public String getDevMgrComment() {
	return devMgrComment;
    }

    public void setDevMgrComment(String devMgrComment) {
	this.devMgrComment = devMgrComment;
    }

    public String getSdmTktNum() {
	return sdmTktNum;
    }

    public void setSdmTktNum(String sdmTktNum) {
	this.sdmTktNum = sdmTktNum;
    }

    public String getRelatedPlans() {
	return relatedPlans;
    }

    public void setRelatedPlans(String relatedPlans) {
	this.relatedPlans = relatedPlans;
    }

    public String getOthContact() {
	return othContact;
    }

    public void setOthContact(String othContact) {
	this.othContact = othContact;
    }

    public String getMgmtComment() {
	return mgmtComment;
    }

    public void setMgmtComment(String mgmtComment) {
	this.mgmtComment = mgmtComment;
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

    public String getPlanStatus() {
	return planStatus;
    }

    public void setPlanStatus(String planStatus) {
	this.planStatus = planStatus;
    }

    public String getLoadAttendee() {
	return loadAttendee;
    }

    public void setLoadAttendee(String loadAttendee) {
	this.loadAttendee = loadAttendee;
    }

    public String getProdVer() {
	return prodVer;
    }

    public void setProdVer(String prodVer) {
	this.prodVer = prodVer;
    }

    public String getProcessId() {
	return processId;
    }

    public void setProcessId(String processId) {
	this.processId = processId;
    }

    public String getDevManager() {
	return devManager;
    }

    public void setDevManager(String devManager) {
	this.devManager = devManager;
    }

    public String getDevManagerName() {
	return devManagerName;
    }

    public void setDevManagerName(String devManagerName) {
	this.devManagerName = devManagerName;
    }

    public Date getApproveDateTime() {
	return approveDateTime;
    }

    public void setApproveDateTime(Date approveDateTime) {
	this.approveDateTime = approveDateTime;
    }

    public Date getAcceptedDateTime() {
	return acceptedDateTime;
    }

    public void setAcceptedDateTime(Date acceptedDateTime) {
	this.acceptedDateTime = acceptedDateTime;
    }

    public Date getFallbackDateTime() {
	return fallbackDateTime;
    }

    public void setFallbackDateTime(Date fallbackDateTime) {
	this.fallbackDateTime = fallbackDateTime;
    }

    public List<Dbcr> getDbcrList() {
	return dbcrList;
    }

    public void setDbcrList(List<Dbcr> dbcrList) {
	this.dbcrList = dbcrList;
    }

    public List<Implementation> getImplementationList() {
	return implementationList;
    }

    public void setImplementationList(List<Implementation> implementationList) {
	this.implementationList = implementationList;
    }

    public List<SystemLoad> getSystemLoadList() {
	return systemLoadList;
    }

    public void setSystemLoadList(List<SystemLoad> systemLoadList) {
	this.systemLoadList = systemLoadList;
    }

    public List<SystemLoadActions> getSystemLoadActionsList() {
	return systemLoadActionsList;
    }

    public void setSystemLoadActionsList(List<SystemLoadActions> systemLoadActionsList) {
	this.systemLoadActionsList = systemLoadActionsList;
    }

    public Project getProjectId() {
	return projectId;
    }

    public void setProjectId(Project projectId) {
	this.projectId = projectId;
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

    public List<ActivityLog> getActivityLogList() {
	return activityLogList;
    }

    public void setActivityLogList(List<ActivityLog> activityLogList) {
	this.activityLogList = activityLogList;
    }

    public List<Build> getBuildList() {
	return buildList;
    }

    public void setBuildList(List<Build> buildList) {
	this.buildList = buildList;
    }

    public List<ImpPlanApprovals> getImpPlanApprovalsList() {
	return impPlanApprovalsList;
    }

    public void setImpPlanApprovalsList(List<ImpPlanApprovals> impPlanApprovalsList) {
	this.impPlanApprovalsList = impPlanApprovalsList;
    }

    public Set<PreProductionLoads> getPreProductionLoadsList() {
	return preProductionLoadsList;
    }

    public void setPreProductionLoadsList(Set<PreProductionLoads> preProductionLoadsList) {
	this.preProductionLoadsList = preProductionLoadsList;
    }

    public Boolean getMacroHeader() {
	return macroHeader;
    }

    public void setMacroHeader(Boolean macroHeader) {
	this.macroHeader = macroHeader;
    }

    public String getQaBypassStatus() {
	return qaBypassStatus;
    }

    public void setQaBypassStatus(String qaBypassStatus) {
	this.qaBypassStatus = qaBypassStatus;
    }

    public List<SystemPdddsMapping> getSystemPdddsMappingList() {
	return systemPdddsMappingList;
    }

    public void setSystemPdddsMappingList(List<SystemPdddsMapping> systemPdddsMappingList) {
	this.systemPdddsMappingList = systemPdddsMappingList;
    }

    public Date getDevMgrApproveDateTime() {
	return devMgrApproveDateTime;
    }

    public void setDevMgrApproveDateTime(Date devMgrApproveDateTime) {
	this.devMgrApproveDateTime = devMgrApproveDateTime;
    }

    public Boolean getLoadDateMailFlag() {
	return loadDateMailFlag;
    }

    public void setLoadDateMailFlag(Boolean loadDateMailFlag) {
	this.loadDateMailFlag = loadDateMailFlag;
    }

    public Boolean getAuxtype() {
	return auxtype;
    }

    public void setAuxtype(Boolean auxtype) {
	this.auxtype = auxtype;
    }

    public String getStackHolderEmail() {
	return stackHolderEmail;
    }

    public void setStackHolderEmail(String stackHolderEmail) {
	this.stackHolderEmail = stackHolderEmail;
    }

    public Boolean getLoadDateMacroMailFlag() {
	return loadDateMacroMailFlag;
    }

    public void setLoadDateMacroMailFlag(Boolean loadDateMacroMailFlag) {
	this.loadDateMacroMailFlag = loadDateMacroMailFlag;
    }

    public Boolean getDeactivateTSDMailFlag() {
	return deactivateTSDMailFlag;
    }

    public void setDeactivateTSDMailFlag(Boolean deactivateTSDMailFlag) {
	this.deactivateTSDMailFlag = deactivateTSDMailFlag;
    }

    public String getInprogressStatus() {
	return inprogressStatus;
    }

    public void setInprogressStatus(String inprogressStatus) {
	this.inprogressStatus = inprogressStatus;
    }

    public Boolean getIsAcceptEnabled() {
	return isAcceptEnabled;
    }

    public void setIsAcceptEnabled(Boolean isAcceptEnabled) {
	this.isAcceptEnabled = isAcceptEnabled;
    }

    public String getDeploymentStatus() {
	return deploymentStatus;
    }

    public void setDeploymentStatus(String deploymentStatus) {
	this.deploymentStatus = deploymentStatus;
    }

    /**
     * @return the deployedMailFlag
     */
    public Boolean getDeployedMailFlag() {
	return deployedMailFlag;
    }

    /**
     * @param deployedMailFlag
     *            the deployedMailFlag to set
     */
    public void setDeployedMailFlag(Boolean deployedMailFlag) {
	this.deployedMailFlag = deployedMailFlag;
    }

    public Boolean getRfcFlag() {
	return rfcFlag;
    }

    public void setRfcFlag(Boolean rfcFlag) {
	this.rfcFlag = rfcFlag;
    }

    public Boolean getRfcMailFlag() {
	return rfcMailFlag;
    }

    public void setRfcMailFlag(Boolean rfcMailFlag) {
	this.rfcMailFlag = rfcMailFlag;
    }

    public Date getRejectedDateTime() {
	return rejectedDateTime;
    }

    public void setRejectedDateTime(Date rejectedDateTime) {
	this.rejectedDateTime = rejectedDateTime;
    }

    public Date getTpfAcceptedDateTime() {
	return tpfAcceptedDateTime;
    }

    public void setTpfAcceptedDateTime(Date tpfAcceptedDateTime) {
	this.tpfAcceptedDateTime = tpfAcceptedDateTime;
    }

    public String getLeadEmail() {
	return leadEmail;
    }

    public void setLeadEmail(String leadEmail) {
	this.leadEmail = leadEmail;
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
	if (!(object instanceof ImpPlan)) {
	    return false;
	}
	ImpPlan other = (ImpPlan) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.ImpPlan[ id=" + id + " ]";
    }

}
