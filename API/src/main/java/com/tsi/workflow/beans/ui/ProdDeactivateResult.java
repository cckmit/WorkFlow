/**
 * 
 */
package com.tsi.workflow.beans.ui;

import java.util.Date;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ProdDeactivateResult {

    private String planid;
    private String sysname;
    private Date loaddate;
    private String reviewers;
    private String leadid;
    private String devmanager;
    private String developerid;
    private String planstatus;

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getSysname() {
	return sysname;
    }

    public void setSysname(String sysname) {
	this.sysname = sysname;
    }

    public Date getLoaddate() {
	return loaddate;
    }

    public void setLoaddate(Date loaddate) {
	this.loaddate = loaddate;
    }

    public String getReviewers() {
	return reviewers;
    }

    public void setReviewers(String reviewers) {
	this.reviewers = reviewers;
    }

    public String getLeadid() {
	return leadid;
    }

    public void setLeadid(String leadid) {
	this.leadid = leadid;
    }

    public String getDevmanager() {
	return devmanager;
    }

    public void setDevmanager(String devmanager) {
	this.devmanager = devmanager;
    }

    public String getDeveloperid() {
	return developerid;
    }

    public void setDeveloperid(String developerid) {
	this.developerid = developerid;
    }

    public String getPlanstatus() {
	return planstatus;
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
    }

    @Override
    public String toString() {
	return "ProdDeactivateResult [planid=" + planid + ", sysname=" + sysname + ", loaddate=" + loaddate + ", reviewers=" + reviewers + ", leadid=" + leadid + ", devmanager=" + devmanager + ", developerid=" + developerid + ", planstatus=" + planstatus + "]";
    }

}
