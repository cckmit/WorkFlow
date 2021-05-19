package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.RepositoryDetails;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class RepoDetailsDAO extends BaseDAO<RepositoryDetails> {

    public List<RepositoryDetails> findByRepoName(String repoName) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("repoName", repoName);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public RepositoryDetails findByTrimeName(String trimeName) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("trimmedName", trimeName);
	lFilter.put("active", "Y");
	return find(lFilter);
    }

    public List<RepositoryDetails> getDefaultFileCreateNoData() throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("defaultFileCreate", "No");
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }
}
