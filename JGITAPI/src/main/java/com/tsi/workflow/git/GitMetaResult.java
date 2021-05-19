/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

/**
 *
 * @author USER
 */
public class GitMetaResult extends GitBaseMetaResult {

    private Repository repository;
    private Ref branch;

    public GitMetaResult(Repository repository, Ref branch, String fileHashCode, String fileName) {
	super(fileHashCode, fileName);
	this.repository = repository;
	this.branch = branch;
	if (repository != null) {
	    setFuncArea(repository.getDirectory().getName());
	}
    }

    public Repository getRepository() {
	return repository;
    }

    public Ref getBranch() {
	return branch;
    }

}
