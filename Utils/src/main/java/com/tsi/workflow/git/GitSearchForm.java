/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import java.util.Set;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GitSearchForm {

    private Set<String> companyName;
    private String filter;
    private Boolean macroHeader;
    private Boolean pendingStatusReq;
    private String repoType;
    private String branchName;
    private String searchType;

    public Set<String> getCompanyName() {
	return companyName;
    }

    public void setCompanyName(Set<String> companyName) {
	this.companyName = companyName;
    }

    public String getFilter() {
	return filter;
    }

    public void setFilter(String filter) {
	this.filter = filter;
    }

    public Boolean getMacroHeader() {
	return macroHeader;
    }

    public void setMacroHeader(Boolean macroHeader) {
	this.macroHeader = macroHeader;
    }

    public Boolean getPendingStatusReq() {
	return pendingStatusReq;
    }

    public void setPendingStatusReq(Boolean pendingStatusReq) {
	this.pendingStatusReq = pendingStatusReq;
    }

    public String getRepoType() {
	return repoType;
    }

    public void setRepoType(String repoType) {
	this.repoType = repoType;
    }

    public String getBranchName() {
	return branchName;
    }

    public void setBranchName(String branchName) {
	this.branchName = branchName;
    }

    public String getSearchType() {
	return searchType;
    }

    public void setSearchType(String searchType) {
	this.searchType = searchType;
    }

}
