package com.tsi.workflow.beans.ui;

import java.util.List;

public class AdvancedSearchResultbyPlan {

    private String planid;
    private String plandescription;
    private String planstatus;
    private String projectname;
    private String developername;
    private String projectNumber;

    private List<AdvanceSearchSystemBasedDetails> advanceSearchSystemBasedDetails;

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

    public String getProjectname() {
	return projectname;
    }

    public void setProjectname(String projectname) {
	this.projectname = projectname;
    }

    public String getDevelopername() {
	return developername;
    }

    public void setDevelopername(String developername) {
	this.developername = developername;
    }

    public String getProjectNumber() {
	return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
	this.projectNumber = projectNumber;
    }

    public List<AdvanceSearchSystemBasedDetails> getAdvanceSearchSystemBasedDetails() {
	return advanceSearchSystemBasedDetails;
    }

    public void setAdvanceSearchSystemBasedDetails(List<AdvanceSearchSystemBasedDetails> advanceSearchSystemBasedDetails) {
	this.advanceSearchSystemBasedDetails = advanceSearchSystemBasedDetails;
    }

}
