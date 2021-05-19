/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.math.BigInteger;
import java.util.Date;

/**
 *
 * @author gn.ebinezardharmaraj
 */
public class SourceArtifactSearchResult {

    private String programname;
    private String funcpackage;
    private String planid;
    private String planstatus;
    private String targetsystem;
    private String developername;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date fallbackdatetime;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String listingfilelink;
    private String sourceartifactlink;
    private String impid;
    private String sourcerepo;
    private String commitid;
    private String filename;
    private String filetype;
    private String listingFile;
    private Boolean highlightGroupFlag;
    private Integer statusrank;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date checkindatetime;
    private String checkoutrefstatus;
    private String sourceurl;
    private String repodesc;

    public String getSourcerepo() {
	return sourcerepo;
    }

    public void setSourcerepo(String sourcerepo) {
	this.sourcerepo = sourcerepo;
    }

    public String getCommitid() {
	return commitid;
    }

    public void setCommitid(String commitid) {
	this.commitid = commitid;
    }

    public String getFuncpackage() {
	return funcpackage;
    }

    public void setFuncpackage(String funcpackage) {
	this.funcpackage = funcpackage;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getProgramname() {
	return programname;
    }

    public void setProgramname(String programname) {
	this.programname = programname;
    }

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getPlanstatus() {
	return planstatus;
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus != null ? planstatus.toUpperCase() : planstatus;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public String getDevelopername() {
	return developername;
    }

    public void setDevelopername(String developername) {
	this.developername = developername;
    }

    public Date getFallbackdatetime() {
	return fallbackdatetime;
    }

    public void setFallbackdatetime(Date fallbackdatetime) {
	this.fallbackdatetime = fallbackdatetime;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getListingfilelink() {
	return listingfilelink;
    }

    public void setListingfilelink(String listingfilelink) {
	this.listingfilelink = listingfilelink;
    }

    public String getImpid() {
	return impid;
    }

    public void setImpid(String impid) {
	this.impid = impid;
    }

    public String getSourceartifactlink() {
	return sourceartifactlink;
    }

    public void setSourceartifactlink(String sourceartifactlink) {
	this.sourceartifactlink = sourceartifactlink;
    }

    public String getFiletype() {
	return filetype;
    }

    public void setFiletype(String filetype) {
	this.filetype = filetype;
    }

    public String getListingFile() {
	return listingFile;
    }

    public void setListingFile(String listingFile) {
	this.listingFile = listingFile;
    }

    public Boolean getHighlightGroupFlag() {
	return highlightGroupFlag;
    }

    public void setHighlightGroupFlag(Boolean highlightGroupFlag) {
	this.highlightGroupFlag = highlightGroupFlag;
    }

    public Integer getStatusrank() {
	return statusrank;
    }

    public void setStatusrank(BigInteger statusrank) {
	this.statusrank = statusrank.intValue();
    }

    public Date getCheckindatetime() {
	return checkindatetime;
    }

    public void setCheckindatetime(Date checkindatetime) {
	this.checkindatetime = checkindatetime;
    }

    /**
     * @return the checkoutrefstatus
     */
    public String getCheckoutrefstatus() {
	return checkoutrefstatus;
    }

    /**
     * @param checkoutrefstatus
     *            the checkoutrefstatus to set
     */
    public void setCheckoutrefstatus(String checkoutrefstatus) {
	this.checkoutrefstatus = checkoutrefstatus;
    }

    public String getSourceurl() {
	return sourceurl;
    }

    public void setSourceurl(String sourceurl) {
	this.sourceurl = sourceurl;
    }

    public String getRepodesc() {
	return repodesc;
    }

    public void setRepodesc(String repodesc) {
	this.repodesc = repodesc;
    }

}
