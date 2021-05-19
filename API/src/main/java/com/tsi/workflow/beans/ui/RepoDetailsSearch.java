/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

/**
 *
 * @author vamsi.krishnarao
 */
public class RepoDetailsSearch {
    String description;
    String repoOwners;
    String functionalArea;

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getRepoOwners() {
	return repoOwners;
    }

    public void setRepoOwners(String repoOwners) {
	this.repoOwners = repoOwners;
    }

    public String getFunctionalArea() {
	return functionalArea;
    }

    public void setFunctionalArea(String functionalArea) {
	this.functionalArea = functionalArea;
    }

    @Override
    public String toString() {
	return "RepoDetailsSearch{" + "description=" + description + ", repoOwners=" + repoOwners + ", functionalArea=" + functionalArea + '}';
    }

}
