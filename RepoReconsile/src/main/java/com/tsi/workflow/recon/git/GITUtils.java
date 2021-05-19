package com.tsi.workflow.recon.git;

import com.tsi.workflow.DAO.RepoFileListDAO;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.GitHelper;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GITUtils {

    private static final Logger LOG = Logger.getLogger(GITUtils.class.getName());

    public List<GitMetaResult> getFilesList(String pRepoPath) throws IOException {
	Git lGit = Git.open(new File(pRepoPath));
	List<GitMetaResult> lResult = new ArrayList<>();
	List<Ref> listBranches = listBranches(lGit);
	for (Ref lBranches : listBranches) {
	    lResult.addAll(getFilesList(lGit.getRepository(), lBranches, ""));
	}
	lGit.close();
	return lResult;
    }

    private List<GitMetaResult> getFilesList(Repository lRepository, Ref pBranch, String pFileFilter) throws WorkflowException {
	List<GitMetaResult> lFilteredFiles = new ArrayList<>();
	try {
	    ObjectId lLastCommitId = lRepository.resolve(pBranch.getName());
	    if (lLastCommitId != null) {
		RevWalk revWalk = new RevWalk(lRepository);
		RevCommit commit = revWalk.parseCommit(lLastCommitId);
		RevTree tree = commit.getTree();
		TreeWalk treeWalk = new TreeWalk(lRepository);
		treeWalk.addTree(tree);
		treeWalk.setRecursive(Boolean.TRUE);
		while (treeWalk.next()) {
		    String lFileName = FilenameUtils.getName(treeWalk.getPathString());
		    // Added code to get all Files for data migration
		    if (pFileFilter == null || pFileFilter.trim().isEmpty()) {
			lFilteredFiles.add(new GitMetaResult(null, null, pBranch.getName(), treeWalk.getPathString()));
		    } else if (lFileName.toLowerCase().startsWith(pFileFilter.toLowerCase())) {
			lFilteredFiles.add(new GitMetaResult(null, null, pBranch.getName(), treeWalk.getPathString()));
		    }
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Exception in getting file list", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	}
	return lFilteredFiles;
    }

    public Map<String, List> getCommitedTags(Repository lRepository) throws MissingObjectException, IOException {
	Map<String, List> lRedefinedTags = new HashMap<>();
	Map<String, Ref> tags = lRepository.getTags();
	RevWalk walk = new RevWalk(lRepository);

	for (Map.Entry<String, Ref> entrySet : tags.entrySet()) {
	    String lValue = entrySet.getKey();
	    Ref lKey = entrySet.getValue();
	    RevObject obj = walk.parseAny(lKey.getObjectId());
	    RevCommit tagCommit = walk.parseCommit(((RevTag) obj).getObject());
	    List lList = lRedefinedTags.get(tagCommit.getId().name());
	    if (lList == null) {
		lList = new ArrayList();
		lRedefinedTags.put(tagCommit.getId().name(), lList);
	    }
	    lList.add(lValue);
	}
	return lRedefinedTags;
    }

    public List<GitBranchSearchResult> getCommitList(String pRepoPath, String pRepoPath1, List<GitMetaResult> pMetaList, List<GitBranchSearchResult> updateSearchRes) throws SQLException, IOException {
	List<GitBranchSearchResult> searchResult = new ArrayList<>();

	try (Git lGit = Git.open(new File(pRepoPath))) {
	    if (lGit != null) {
		RevWalk walk = new RevWalk(lGit.getRepository());
		Map<String, List> lRedefinedTags = getCommitedTags(lGit.getRepository());
		Map<String, List<GitMetaResult>> lMetaFileList = pMetaList.stream().collect(Collectors.groupingBy(x -> x.getBranch()));
		for (Map.Entry<String, List<GitMetaResult>> entry : lMetaFileList.entrySet()) {
		    String lBranchName = entry.getKey();
		    List<GitMetaResult> lMetaList = entry.getValue();
		    ObjectId lBranch = lGit.getRepository().getRefDatabase().getRef(lBranchName).getObjectId();
		    for (int i = 0; i < lMetaList.size(); i++) {
			walk.reset();
			GitMetaResult gitMetaResult = lMetaList.get(i);
			List<GitBranchSearchResult> lFileResult = getSearchResult(gitMetaResult, lRedefinedTags, lGit, walk, lBranch, updateSearchRes);
			if (i % 100 == 0) {
			    LOG.info("Repo " + pRepoPath1 + ", Branch " + lBranchName + ", " + i + "/" + lMetaList.size() + " Files Completed" + ", " + searchResult.size() + " Commits");
			}
			Collections.reverse(lFileResult);
			searchResult.addAll(lFileResult);
		    }
		}
		walk.close();
	    } else {
		// LOG
	    }
	}
	return searchResult;
    }

    public List<GitBranchSearchResult> getSearchResult(GitMetaResult lFile, Map<String, List> lRedefinedTags, Git lGit, RevWalk walk, ObjectId lBranch, List<GitBranchSearchResult> updateGitBranchRes) throws WorkflowException {
	List<GitBranchSearchResult> lReturn = new ArrayList<>();
	RepoFileListDAO lRepoFileListDAO = new RepoFileListDAO();
	GitHelper gitHelper = new GitHelper();
	try {
	    HashMap<String, String> fileAndSubRepoBasedCommitId = lRepoFileListDAO.getCommitIdBasedOnFileAndSubRepo(lFile.getFileName(), lFile.getSubRepoId());
	    Iterable<RevCommit> logs;
	    if (fileAndSubRepoBasedCommitId != null && fileAndSubRepoBasedCommitId.containsKey(lFile.getFileName() + "-" + lFile.getSubRepoId() + "-" + lFile.getTargetSystem())) {
		ObjectId upstreamCommit = ObjectId.fromString(fileAndSubRepoBasedCommitId.get(lFile.getFileName() + "-" + lFile.getSubRepoId() + "-" + lFile.getTargetSystem()));
		logs = lGit.log().setRevFilter(RevFilter.NO_MERGES).add(lBranch).addPath(lFile.getFileName()).addRange(upstreamCommit, lBranch).call();
	    } else {
		logs = lGit.log().setRevFilter(RevFilter.NO_MERGES).add(lBranch).addPath(lFile.getFileName()).call();
	    }

	    for (Iterator<RevCommit> iterator = logs.iterator(); iterator.hasNext();) {
		RevCommit lCommitLogs = iterator.next();
		if (walk.isMergedInto(walk.parseCommit(lCommitLogs), walk.parseCommit(lBranch))) {
		    // LOG.info("File name: " + lFile.getFileName() + " Sub Repo id: " +
		    // lFile.getSubRepoId() + " Commit Id: " + lCommitLogs.getId().name());
		    if (!CacheClient.getInstance().getAllCommitNames().contains(lFile.getSubRepoId() + "|" + lCommitLogs.getId().name())) {
			// LOG.info("New Commit which is not available in cache.");
			gitHelper.addOrUpdateGitBrachSearchList(lFile, lRedefinedTags, lGit, lReturn, lCommitLogs, null);
		    } else if (CacheClient.getInstance().getAllCommitNames().contains(lFile.getSubRepoId() + "|" + lCommitLogs.getId().name())) {
			// LOG.info("Existing Commit which is available in cache.");
			gitHelper.addOrUpdateGitBrachSearchList(lFile, lRedefinedTags, lGit, lReturn, lCommitLogs, updateGitBranchRes);
		    }
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Exception in getting file list", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	}
	return lReturn;
    }

    private List<Ref> listBranches(Git lGit) throws WorkflowException {
	List<Ref> lReturn = new ArrayList<>();
	try {
	    List<Ref> branches = lGit.branchList().call();
	    for (Ref branch : branches) {
		if (branch.getName().startsWith(Constants.R_HEADS + Constants.MASTER) && !branch.getName().equals(Constants.R_HEADS + Constants.MASTER)) {
		    lReturn.add(branch);
		}
	    }
	} catch (GitAPIException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	}
	return lReturn;
    }

    public List<String> listProductionRepositories(File pSourceDirectory) {
	return listProductionRepositories(null, null, pSourceDirectory);
    }

    private List<String> listProductionRepositories(File pSourceDir, File pDerivedDir, File pBaseDir) {
	return listProductionRepositories(pSourceDir, pDerivedDir, pBaseDir, false);
    }

    private List<String> listProductionRepositories(File pSourceDir, File pDerivedDir, File pBaseDir, boolean isDerivedNeeded) {
	List<String> lReturn = new ArrayList<>();
	if (pSourceDir == null) {
	    pSourceDir = new File(pBaseDir, AppConfig.getInstance().getGitSourcePath());
	}
	if (pDerivedDir == null) {
	    pDerivedDir = new File(pBaseDir, AppConfig.getInstance().getGitDerivedPath());
	}
	if (pSourceDir.getAbsolutePath().equals(pBaseDir.getAbsolutePath())) {
	    return new ArrayList<>();
	}
	if (pDerivedDir.getAbsolutePath().equals(pBaseDir.getAbsolutePath())) {
	    return new ArrayList<>();
	}
	File[] lDirs = pBaseDir.listFiles((File pathname) -> {
	    if (pathname.isDirectory()) {
		if (pathname.getName().startsWith("derived_") && isDerivedNeeded) {
		    return true;
		} else if (pathname.getName().startsWith("derived_") && !isDerivedNeeded) {
		    return false;
		} else {
		    return true;
		}
	    } else {
		return false;
	    }
	});

	if (lDirs != null) {
	    for (File lDir : lDirs) {
		if (lDir.getName().endsWith(".git")) {
		    lReturn.add(lDir.getAbsolutePath());
		} else {
		    lReturn.addAll(listProductionRepositories(pSourceDir, pDerivedDir, lDir, isDerivedNeeded));
		}
	    }
	}
	return lReturn;
    }
}
