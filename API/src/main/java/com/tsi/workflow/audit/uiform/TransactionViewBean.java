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
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ramkumar.seenivasan
 */
public class TransactionViewBean implements Serializable {

    private static final long serialVersionUID = 2L;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startdate;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date enddate;
    // private String userName;
    private String useraction;
    private String planid;
    private String implid;
    private String userrole;
    private String initiatedby;
    private String targetsystem;
    private BigInteger sbtcount;
    private BigInteger asmcount;
    private BigInteger makcount;
    private BigInteger ccppcount;
    private BigInteger headercount;
    private BigInteger totalcount;
    private BigInteger socount;
    private BigInteger repocount;
    private String reponamelist;
    private BigInteger responsetime;
    private String hostname;
    private String tdx;

    public Date getStartdate() {
	return startdate;
    }

    public void setStartdate(Date startdate) {
	this.startdate = startdate;
    }

    public Date getEnddate() {
	return enddate;
    }

    public void setEnddate(Date enddate) {
	this.enddate = enddate;
    }

    public String getUseraction() {
	return useraction;
    }

    public void setUseraction(String useraction) {
	this.useraction = useraction;
    }

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getImplid() {
	return implid;
    }

    public void setImplid(String implid) {
	this.implid = implid;
    }

    public String getUserrole() {
	return userrole;
    }

    public void setUserrole(String userrole) {
	this.userrole = userrole;
    }

    public String getInitiatedby() {
	return initiatedby;
    }

    public void setInitiatedby(String initiatedby) {
	this.initiatedby = initiatedby;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public BigInteger getSbtcount() {
	return sbtcount;
    }

    public void setSbtcount(BigInteger sbtcount) {
	this.sbtcount = sbtcount;
    }

    public BigInteger getAsmcount() {
	return asmcount;
    }

    public void setAsmcount(BigInteger asmcount) {
	this.asmcount = asmcount;
    }

    public BigInteger getMakcount() {
	return makcount;
    }

    public void setMakcount(BigInteger makcount) {
	this.makcount = makcount;
    }

    public BigInteger getCcppcount() {
	return ccppcount;
    }

    public void setCcppcount(BigInteger ccppcount) {
	this.ccppcount = ccppcount;
    }

    public BigInteger getHeadercount() {
	return headercount;
    }

    public void setHeadercount(BigInteger headercount) {
	this.headercount = headercount;
    }

    public BigInteger getTotalcount() {
	return totalcount;
    }

    public void setTotalcount(BigInteger totalcount) {
	this.totalcount = totalcount;
    }

    public BigInteger getSocount() {
	return socount;
    }

    public void setSocount(BigInteger socount) {
	this.socount = socount;
    }

    public BigInteger getRepocount() {
	return repocount;
    }

    public void setRepocount(BigInteger repocount) {
	this.repocount = repocount;
    }

    public String getReponamelist() {
	return reponamelist;
    }

    public void setReponamelist(String reponamelist) {
	this.reponamelist = reponamelist;
    }

    public BigInteger getResponsetime() {
	return responsetime;
    }

    public void setResponsetime(BigInteger responsetime) {
	this.responsetime = responsetime;
    }

    public String getHostname() {
	return hostname;
    }

    public void setHostname(String hostname) {
	this.hostname = hostname;
    }

    public String getTdx() {
	return tdx;
    }

    public void setTdx(String tdx) {
	this.tdx = tdx;
    }

}
