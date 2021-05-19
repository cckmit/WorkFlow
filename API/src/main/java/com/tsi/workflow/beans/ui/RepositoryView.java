/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.gitblit.model.Repository;
import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Radha.Adhimoolam
 */
public class RepositoryView implements Serializable, Comparable<RepositoryView>, Comparator<RepositoryView> {

    private static final long serialVersionUID = -4387765870107697622L;

    private Repository repository;
    private Map<String, Boolean> branches = new HashMap();
    private String filecreateFlag;

    public RepositoryView() {
	repository = new Repository();
    }

    public String getDefaultAccess() {
	return repository.getCurrentAccess();
    }

    public void setDefaultAccess(String defaultAccess) {
	repository.setCurrentAccess(defaultAccess);
    }

    public Repository getRepository() {
	return repository;
    }

    public void setRepository(Repository repository) {
	this.repository = repository;
    }

    public Map<String, Boolean> getBranches() {
	return branches;
    }

    public void setBranches(Map<String, Boolean> branches) {
	this.branches = branches;
    }

    public String getFilecreateFlag() {
	return filecreateFlag;
    }

    public void setFilecreateFlag(String filecreateFlag) {
	this.filecreateFlag = filecreateFlag;
    }

    @Override
    public int compareTo(RepositoryView o) {
	// TODO Auto-generated method stub
	return this.getRepository().getName().compareTo(o.getRepository().getName());
    }

    @Override
    public int compare(RepositoryView o1, RepositoryView o2) {
	return o1.getRepository().getName().compareTo(o2.getRepository().getName());
    }

}
