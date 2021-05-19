/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import java.util.Date;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GitProdCommitMessage implements Comparable<GitProdCommitMessage> {

    private Date Date;
    private String Type;
    private String PlanID;
    private String PlanOwner;
    private String SourceRef;
    private String Status;

    public GitProdCommitMessage() {
    }

    public GitProdCommitMessage(String planId, Date ldate) {
	this.PlanID = planId;
	this.Date = ldate;
    }

    public Date getDate() {
	return Date;
    }

    public void setDate(Date Date) {
	this.Date = Date;
    }

    public String getType() {
	return Type;
    }

    public void setType(String Type) {
	this.Type = Type;
    }

    public String getPlanID() {
	return PlanID;
    }

    public void setPlanID(String PlanID) {
	this.PlanID = PlanID;
    }

    public String getPlanOwner() {
	return PlanOwner;
    }

    public void setPlanOwner(String PlanOwner) {
	this.PlanOwner = PlanOwner;
    }

    public String getSourceRef() {
	return SourceRef;
    }

    public void setSourceRef(String SourceRef) {
	this.SourceRef = SourceRef;
    }

    public String getStatus() {
	return Status;
    }

    public void setStatus(String Status) {
	this.Status = Status;
    }

    @Override
    public int compareTo(GitProdCommitMessage lGitProdCommMsg) {
	return lGitProdCommMsg.getDate().compareTo(this.getDate());
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof GitProdCommitMessage)) {
	    return false;
	}
	GitProdCommitMessage l = (GitProdCommitMessage) obj;
	return (Date.toString() + PlanID).equals(l.getDate().toString() + l.getPlanID());
    }

    @Override
    public int hashCode() {
	return 17 * PlanID.hashCode() + Date.hashCode();
    }

}
