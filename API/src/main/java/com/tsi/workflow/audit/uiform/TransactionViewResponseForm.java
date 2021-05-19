/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.uiform;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import java.util.Map;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ramkumar.seenivasan
 */
public class TransactionViewResponseForm {

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDateTime;
    private String userName;
    private String userAction;
    private String planId;
    private String impId;
    private String userRole;
    private String initiatedBy;
    private String targetsystem;
    private Integer sbtcount;
    private Integer asmcount;
    private Integer makcount;
    private Integer ccppcount;
    private Integer headercount;
    private Map<String, Integer> TotalCountBySystem;
    private Map<String, Integer> socount;
    private Integer repocount;
    private String reponamelist;
    private Long responseTimeMs;
    private double responseTimeSec;
    private String responseTimeMin;
    private String hostName;
    private String tdx;
    private String zOs;

    public Date getStartDateTime() {
	return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
	this.startDateTime = startDateTime;
    }

    public Date getEndDateTime() {
	return endDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
	this.endDateTime = endDateTime;
    }

    public String getUserName() {
	return userName;
    }

    public void setUserName(String userName) {
	this.userName = userName;
    }

    public String getUserAction() {
	return userAction;
    }

    public void setUserAction(String userAction) {
	this.userAction = userAction;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getImpId() {
	return impId;
    }

    public void setImpId(String impId) {
	this.impId = impId;
    }

    public String getUserRole() {
	return userRole;
    }

    public void setUserRole(String userRole) {
	this.userRole = userRole;
    }

    public String getInitiatedBy() {
	return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
	this.initiatedBy = initiatedBy;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public Integer getSbtcount() {
	return sbtcount;
    }

    public void setSbtcount(Integer sbtcount) {
	this.sbtcount = sbtcount;
    }

    public Integer getAsmcount() {
	return asmcount;
    }

    public void setAsmcount(Integer asmcount) {
	this.asmcount = asmcount;
    }

    public Integer getMakcount() {
	return makcount;
    }

    public void setMakcount(Integer makcount) {
	this.makcount = makcount;
    }

    public Integer getCcppcount() {
	return ccppcount;
    }

    public void setCcppcount(Integer ccppcount) {
	this.ccppcount = ccppcount;
    }

    public Integer getHeadercount() {
	return headercount;
    }

    public void setHeadercount(Integer headercount) {
	this.headercount = headercount;
    }

    public Map<String, Integer> getTotalCountBySystem() {
	return TotalCountBySystem;
    }

    public void setTotalCountBySystem(Map<String, Integer> TotalCountBySystem) {
	this.TotalCountBySystem = TotalCountBySystem;
    }

    public Map<String, Integer> getSocount() {
	return socount;
    }

    public void setSocount(Map<String, Integer> socount) {
	this.socount = socount;
    }

    public Integer getRepocount() {
	return repocount;
    }

    public void setRepocount(Integer repocount) {
	this.repocount = repocount;
    }

    public String getReponamelist() {
	return reponamelist;
    }

    public void setReponamelist(String reponamelist) {
	this.reponamelist = reponamelist;
    }

    public Long getResponseTimeMs() {
	return responseTimeMs;
    }

    public void setResponseTimeMs(Long responseTimeMs) {
	this.responseTimeMs = responseTimeMs;
    }

    public double getResponseTimeSec() {
	return responseTimeSec;
    }

    public void setResponseTimeSec(double responseTimeSec) {
	this.responseTimeSec = responseTimeSec;
    }

    public String getResponseTimeMin() {
	return responseTimeMin;
    }

    public void setResponseTimeMin(String responseTimeMin) {
	this.responseTimeMin = responseTimeMin;
    }

    public String getHostName() {
	return hostName;
    }

    public void setHostName(String hostName) {
	this.hostName = hostName;
    }

    public String getTdx() {
	return tdx;
    }

    public void setTdx(String tdx) {
	this.tdx = tdx;
    }

    public String getzOs() {
	return zOs;
    }

    public void setzOs(String zOs) {
	this.zOs = zOs;
    }

}
