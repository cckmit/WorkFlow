/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.DAO.RepoCommitDAO;
import com.tsi.workflow.DAO.RepoFileListDAO;
import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.recon.git.GITUtils;
import com.tsi.workflow.recon.git.GitBranchSearchResult;
import com.tsi.workflow.recon.git.GitMetaResult;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author prabhu.prabhakaran
 */
public class RepoCommitPopulate extends Thread {

    RepoFileListDAO lRepoFileListDAO;
    RepoCommitDAO lRepoCommitDAO;
    RepoDetailPopulate lRepoDetailPopulate;
    private static final Logger LOG = Logger.getLogger(RepoCommitPopulate.class.getName());
    String lRepoName;
    Integer lRepoId;

    public RepoCommitPopulate(RepoDetailPopulate pRepoDetailPopulate, String pRepoName, Integer pRepoId) {
	lRepoId = pRepoId;
	lRepoName = pRepoName;
	lRepoDetailPopulate = pRepoDetailPopulate;

	lRepoCommitDAO = new RepoCommitDAO();
	lRepoFileListDAO = new RepoFileListDAO();
    }

    @Override
    public void run() {
	LOG.info("Searching in Repo " + lRepoName);
	try {

	    // Getting File list for ALL Target Systems in a Repo
	    List<GitMetaResult> repoFileList = lRepoFileListDAO.getRepoFileList(lRepoId);
	    LOG.info("Total Files in Repo " + lRepoName + " : " + repoFileList.size());

	    // PRCP: Loop this branch wise / Target System wise
	    // Getting Commit list for ALL Files & All Target Systems in a Repo
	    List<GitBranchSearchResult> updateCommitList = new ArrayList<>();
	    List<GitBranchSearchResult> commitList = new GITUtils().getCommitList(AppConfig.getInstance().getGitBasePath() + lRepoName, lRepoName, repoFileList, updateCommitList);
	    LOG.info("Total Commits in Repo " + lRepoName + " : " + commitList.size());

	    if (commitList.size() > 0) {
		LOG.info("Saving Commits for repo " + lRepoName);
		lRepoCommitDAO.saveRepoCommitList(commitList);
	    }

	    // PRCP: Need to check from here Pending
	    if (updateCommitList.size() > 0) {
		LOG.info("Updating Existing records. Update Commit list size: " + updateCommitList.size());
		lRepoCommitDAO.updateRepoCommitList(updateCommitList);
	    }

	    LOG.info("Populating Cache repo " + lRepoName);
	    lRepoDetailPopulate.populateCommitDetails();

	} catch (Exception ex) {
	    LOG.info("Error", ex);
	}
    }

}
