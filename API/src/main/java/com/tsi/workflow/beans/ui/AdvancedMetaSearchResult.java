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
import java.util.Date;

/**
 *
 * @author gn.ebinezardharmaraj
 */
public class AdvancedMetaSearchResult {

    private String programname;
    private String planid;
    private String plandescription;
    private String planstatus;
    private String targetsystem;
    private String developername;
    private String qastatus;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date fallbackdatetime;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date activateddatetime;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String leadname;
    private String managername;
    private String loadcategory;
    private String loadattendee;
    private String functionalarea;
    private String csrnumber;
    private String projectname;
    private String peerreviewer;
    private String dbcrname;
    private String loadinstruction;
    private String problemticketnum;
    private String loadtype;
    private String rfcnumber;

    public String getLoadcategory() {
	return loadcategory;
    }

    public void setLoadcategory(String loadcategory) {
	this.loadcategory = loadcategory;
    }

    public String getLeadname() {
	return leadname;
    }

    public void setLeadname(String leadname) {
	this.leadname = leadname;
    }

    public String getManagername() {
	return managername;
    }

    public void setManagername(String managername) {
	this.managername = managername;
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

    public String getPlandescription() {
	return plandescription;
    }

    public void setPlandescription(String plandescription) {
	this.plandescription = plandescription;
    }

    public String getPlanstatus() {
	return planstatus;
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
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

    public Date getActivateddatetime() {
	return activateddatetime;
    }

    public void setActivateddatetime(Date activateddatetime) {
	this.activateddatetime = activateddatetime;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getQastatus() {
	return qastatus;
    }

    public void setQastatus(String qastatus) {
	this.qastatus = qastatus;
    }

    public String getLoadattendee() {
	return loadattendee;
    }

    public void setLoadattendee(String loadattendee) {
	this.loadattendee = loadattendee;
    }

    public String getFunctionalarea() {
	return functionalarea;
    }

    public void setFunctionalarea(String functionalarea) {
	this.functionalarea = functionalarea;
    }

    public String getCsrnumber() {
	return csrnumber;
    }

    public void setCsrnumber(String csrnumber) {
	this.csrnumber = csrnumber;
    }

    public String getProjectname() {
	return projectname;
    }

    public void setProjectname(String projectname) {
	this.projectname = projectname;
    }

    public String getPeerreviewer() {
	return peerreviewer;
    }

    public void setPeerreviewer(String peerreviewer) {
	this.peerreviewer = peerreviewer;
    }

    public String getDbcrname() {
	return dbcrname;
    }

    public void setDbcrname(String dbcrname) {
	this.dbcrname = dbcrname;
    }

    public String getLoadinstruction() {
	return loadinstruction;
    }

    public void setLoadinstruction(String loadinstruction) {
	this.loadinstruction = loadinstruction;
    }

    public String getProblemticketnum() {
	return problemticketnum;
    }

    public void setProblemticketnum(String problemticketnum) {
	this.problemticketnum = problemticketnum;
    }

    public String getLoadtype() {
	return loadtype;
    }

    public void setLoadtype(String loadtype) {
	this.loadtype = loadtype;
    }

    /**
     * @return the rfcnumber
     */
    public String getRfcnumber() {
	return rfcnumber;
    }

    /**
     * @param rfcnumber
     *            the rfcnumber to set
     */
    public void setRfcnumber(String rfcnumber) {
	this.rfcnumber = rfcnumber;
    }

}
