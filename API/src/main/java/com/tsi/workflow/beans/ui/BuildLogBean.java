package com.tsi.workflow.beans.ui;

import java.util.SortedSet;

public class BuildLogBean {

    private String planId;
    private String systemName;
    private SortedSet<String> buildFileNameList;
    private SortedSet<String> loaderFileNameList;

    public SortedSet<String> getBuildFileNameList() {
	return buildFileNameList;
    }

    public void setBuildFileNameList(SortedSet<String> buildFileNameList) {
	this.buildFileNameList = buildFileNameList;
    }

    public SortedSet<String> getLoaderFileNameList() {
	return loaderFileNameList;
    }

    public void setLoaderFileNameList(SortedSet<String> loaderFileNameList) {
	this.loaderFileNameList = loaderFileNameList;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

}
