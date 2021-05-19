/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import java.io.Serializable;
import java.math.BigInteger;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PlanPerformanceView implements Serializable {

    private static final long serialVersionUID = 1L;
    private String planid;
    private String impid;
    private String targetsystem;
    private String tdx;
    private Integer sbtcount;
    private Integer asmcount;
    private Integer makcount;
    private Integer ccppcount;
    private Integer headercount;
    private Integer totalcount;
    private Integer socount;
    private Integer repocount;
    private String reponamelist;

    public PlanPerformanceView() {
    }

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getImpid() {
	return impid;
    }

    public void setImpid(String impid) {
	this.impid = impid;
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

    public void setSbtcount(BigInteger sbtcount) {
	this.sbtcount = sbtcount.intValue();
    }

    public Integer getAsmcount() {
	return asmcount;
    }

    public void setAsmcount(BigInteger asmcount) {
	this.asmcount = asmcount.intValue();
    }

    public Integer getMakcount() {
	return makcount;
    }

    public void setMakcount(BigInteger makcount) {
	this.makcount = makcount.intValue();
    }

    public Integer getCcppcount() {
	return ccppcount;
    }

    public void setCcppcount(BigInteger ccppcount) {
	this.ccppcount = ccppcount.intValue();
    }

    public Integer getHeadercount() {
	return headercount;
    }

    public void setHeadercount(BigInteger headercount) {
	this.headercount = headercount.intValue();
    }

    public Integer getTotalcount() {
	return totalcount;
    }

    public void setTotalcount(BigInteger totalcount) {
	this.totalcount = totalcount.intValue();
    }

    public Integer getSocount() {
	return socount;
    }

    public void setSocount(BigInteger socount) {
	this.socount = socount.intValue();
    }

    public Integer getRepocount() {
	return repocount;
    }

    public void setRepocount(BigInteger repocount) {
	this.repocount = repocount.intValue();
    }

    public String getReponamelist() {
	return reponamelist;
    }

    public void setReponamelist(String reponamelist) {
	this.reponamelist = reponamelist;
    }

    public String getTdx() {
	return tdx;
    }

    public void setTdx(String tdx) {
	this.tdx = tdx;
    }

}
