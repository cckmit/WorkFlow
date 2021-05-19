package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

public class SegmentBasedActionDetail {

    private String id;
    private int loadid;
    private int sysid;
    private int preid;
    private int segid;
    private String impid;
    private int catid;
    private int planDesc;

    private String plandesc;
    private String loadtype;
    private String leadid;
    private String impplanactive;
    private String planstatus;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date fallbackdatetime;
    private String leadname;
    private String devmanagername;
    private String loadactive;
    private String loadsetname;
    private String fallbackloadsetname;
    private int platformid;
    private int systemid;
    private String sysname;
    private String programname;
    private String repodesc;
    private String funcarea;
    private String targetsystem;
    private String devname;
    private String preproductionstatus;
    private String prelastactionstatus;
    private String productionstatus;
    private String lastactionstatus;
    private String catname;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date modifieddate;
    private String environment;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String peerreviewer;
    private String loadattendee;
    private String csrno;
    private String projectname;
    private String qastatus;
    private String loadinstruction;

    public String getId() {
	return id;
    }

    public String getCsrno() {
	return csrno;
    }

    public void setCsrno(String csrno) {
	this.csrno = csrno;
    }

    public String getProjectname() {
	return projectname;
    }

    public void setProjectname(String projectname) {
	this.projectname = projectname;
    }

    public String getQastatus() {
	return qastatus;
    }

    public void setQastatus(String qastatus) {
	this.qastatus = qastatus;
    }

    public void setId(String id) {
	this.id = id;
    }

    public int getSysid() {
	return sysid;
    }

    public void setSysid(int sysid) {
	this.sysid = sysid;
    }

    public int getPreid() {
	return preid;
    }

    public void setPreid(int preid) {
	this.preid = preid;
    }

    public int getSegid() {
	return segid;
    }

    public void setSegid(int segid) {
	this.segid = segid;
    }

    public String getImpid() {
	return impid;
    }

    public void setImpid(String impid) {
	this.impid = impid;
    }

    public int getCatid() {
	return catid;
    }

    public void setCatid(int catid) {
	this.catid = catid;
    }

    public int getPlanDesc() {
	return planDesc;
    }

    public void setPlanDesc(int planDesc) {
	this.planDesc = planDesc;
    }

    public String getPlandesc() {
	return plandesc;
    }

    public void setPlandesc(String plandesc) {
	this.plandesc = plandesc;
    }

    public String getLoadtype() {
	return loadtype;
    }

    public void setLoadtype(String loadtype) {
	this.loadtype = loadtype;
    }

    public String getLeadid() {
	return leadid;
    }

    public void setLeadid(String leadid) {
	this.leadid = leadid;
    }

    public String getImpplanactive() {
	return impplanactive;
    }

    public void setImpplanactive(String impplanactive) {
	this.impplanactive = impplanactive;
    }

    public String getPlanstatus() {
	return planstatus;
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
    }

    public Date getFallbackdatetime() {
	return fallbackdatetime;
    }

    public void setFallbackdatetime(Date fallbackdatetime) {
	this.fallbackdatetime = fallbackdatetime;
    }

    public String getLeadname() {
	return leadname;
    }

    public void setLeadname(String leadname) {
	this.leadname = leadname;
    }

    public String getDevmanagername() {
	return devmanagername;
    }

    public void setDevmanagername(String devmanagername) {
	this.devmanagername = devmanagername;
    }

    public String getLoadactive() {
	return loadactive;
    }

    public void setLoadactive(String loadactive) {
	this.loadactive = loadactive;
    }

    public String getLoadsetname() {
	return loadsetname;
    }

    public void setLoadsetname(String loadsetname) {
	this.loadsetname = loadsetname;
    }

    public String getFallbackloadsetname() {
	return fallbackloadsetname;
    }

    public void setFallbackloadsetname(String fallbackloadsetname) {
	this.fallbackloadsetname = fallbackloadsetname;
    }

    public int getPlatformid() {
	return platformid;
    }

    public void setPlatformid(int platformid) {
	this.platformid = platformid;
    }

    public int getSystemid() {
	return systemid;
    }

    public void setSystemid(int systemid) {
	this.systemid = systemid;
    }

    public String getSysname() {
	return sysname;
    }

    public void setSysname(String sysname) {
	this.sysname = sysname;
    }

    public String getProgramname() {
	return programname;
    }

    public void setProgramname(String programname) {
	this.programname = programname;
    }

    public String getRepodesc() {
	return repodesc;
    }

    public void setRepodesc(String repodesc) {
	this.repodesc = repodesc;
    }

    public String getFuncarea() {
	return funcarea;
    }

    public void setFuncarea(String funcarea) {
	this.funcarea = funcarea;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public String getDevname() {
	return devname;
    }

    public void setDevname(String devname) {
	this.devname = devname;
    }

    public String getPreproductionstatus() {
	return preproductionstatus;
    }

    public void setPreproductionstatus(String preproductionstatus) {
	this.preproductionstatus = preproductionstatus;
    }

    public String getPrelastactionstatus() {
	return prelastactionstatus;
    }

    public void setPrelastactionstatus(String prelastactionstatus) {
	this.prelastactionstatus = prelastactionstatus;
    }

    public String getProductionstatus() {
	return productionstatus;
    }

    public void setProductionstatus(String productionstatus) {
	this.productionstatus = productionstatus;
    }

    public String getCatname() {
	return catname;
    }

    public void setCatname(String catname) {
	this.catname = catname;
    }

    public Date getModifieddate() {
	return modifieddate;
    }

    public void setModifieddate(Date modifieddate) {
	this.modifieddate = modifieddate;
    }

    public String getEnvironment() {
	return environment;
    }

    public void setEnvironment(String environment) {
	this.environment = environment;
    }

    public String getLastactionstatus() {
	return lastactionstatus;
    }

    public void setLastactionstatus(String lastactionstatus) {
	this.lastactionstatus = lastactionstatus;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public int getLoadid() {
	return loadid;
    }

    public void setLoadid(int loadid) {
	this.loadid = loadid;
    }

    public String getPeerreviewer() {
	return peerreviewer;
    }

    public void setPeerreviewer(String peerreviewer) {
	this.peerreviewer = peerreviewer;
    }

    public String getLoadattendee() {
	return loadattendee;
    }

    public void setLoadattendee(String loadattendee) {
	this.loadattendee = loadattendee;
    }

    public String getLoadinstruction() {
	return loadinstruction;
    }

    public void setLoadinstruction(String loadinstruction) {
	this.loadinstruction = loadinstruction;
    }
}
