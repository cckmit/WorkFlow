/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static com.tsi.workflow.Main.excludedRepos;
import static com.tsi.workflow.Main.includedRepos;
import static com.tsi.workflow.utils.Constants.lAllFileExtTypes;

import com.gitblit.models.RepositoryModel;
import com.gitblit.utils.RpcUtils;
import com.hazelcast.core.IMap;
import com.tsi.workflow.DAO.RepoCommitDAO;
import com.tsi.workflow.DAO.RepoDetailsDAO;
import com.tsi.workflow.DAO.RepoFileListDAO;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.config.AppConfig;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.recon.git.GITUtils;
import com.tsi.workflow.recon.git.GitMetaResult;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author DINESH.RAMANATHAN
 */
public class RepoDetailPopulate {

    RepoDetailsDAO lRepoDetailsDAO;
    RepoFileListDAO lRepoFileListDAO;
    RepoCommitDAO lRepoCommitDAO;
    ExecutorService fixedExecutorService;
    private static final Logger LOG = Logger.getLogger(RepoDetailPopulate.class.getName());

    public RepoDetailPopulate() {
	lRepoDetailsDAO = new RepoDetailsDAO();
	lRepoCommitDAO = new RepoCommitDAO();
	lRepoFileListDAO = new RepoFileListDAO();
	fixedExecutorService = Executors.newFixedThreadPool(4);
    }

    private List<RepositoryModel> getProductionSourceRepoNames() throws IOException {
	LOG.info("Gitblit URL = " + AppConfig.getInstance().getGitblitURL());
	Map<String, RepositoryModel> repositories = RpcUtils.getRepositories(AppConfig.getInstance().getGitblitURL(), AppConfig.getInstance().getServiceUserName(), AppConfig.getInstance().getServicePassword().toCharArray());
	List<RepositoryModel> lProdRepos = repositories.values().stream().filter(value -> (value.name.startsWith("tpf/tp/nonibm/") || value.name.startsWith("tpf/tp/ibm/") || value.name.startsWith("tpf/dl/nonibm/") || value.name.startsWith("tpf/dl/ibm/")) && !value.name.contains("derived_")).collect(Collectors.toList());
	return lProdRepos;
    }

    public void populateRepositories() {
	Set<String> lCacheRepoNames = CacheClient.getInstance().getAllSubRepositoryNames();
	List<RepositoryModel> lProdRepos = new ArrayList<>();
	try {
	    lProdRepos = getProductionSourceRepoNames();
	} catch (IOException ex) {
	    LOG.info("Error", ex);
	}
	Map<String, RepositoryModel> lProdRepoMap = lProdRepos.stream().collect(Collectors.toMap(RepositoryModel::toString, Function.identity()));
	Collection<String> lNewRepoNames = CollectionUtils.subtract(lProdRepoMap.keySet(), lCacheRepoNames);
	List<RepositoryModel> lNewRepos = new ArrayList<>();
	for (String lNewRepoName : lNewRepoNames) {
	    RepositoryModel lNewRepo = lProdRepoMap.get(lNewRepoName);
	    lNewRepos.add(lNewRepo);
	}
	try {
	    lRepoDetailsDAO.saveRepoDetails(lNewRepos);
	    populateRepoDetails();
	    lRepoDetailsDAO.saveSubRepoDetails(lNewRepos);
	    populateSubRepoDetails();
	    lRepoDetailsDAO.updateRepoPermissions();
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	}
    }

    void clearCache() {
	CacheClient.getInstance().clearAllCache();
    }

    void initSchema(String pDBSchema) {
	try {
	    if (!lRepoDetailsDAO.exist(pDBSchema)) {
		List<String> readQueries = readQueries(Constants.APP_DB_SCRIPTPATH);
		List<String> DBQueries = new ArrayList<>();
		for (String lQuery : readQueries) {
		    DBQueries.add(lQuery.replaceAll("<schema>", pDBSchema));
		}
		lRepoDetailsDAO.createSchema(DBQueries);
	    }
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	    throw new WorkflowException("Error in Schema Cereation");
	}
    }

    private List<String> readQueries(String pFileName) {
	List<String> lQueries = new ArrayList<String>();
	InputStream stream = null;
	try {
	    stream = this.getClass().getClassLoader().getResourceAsStream(pFileName);
	    lQueries = IOUtils.readLines(stream);
	} catch (IOException ex) {
	    LOG.error("Error reading Queries");
	} finally {
	    IOUtils.closeQuietly(stream);
	}
	return lQueries;
    }

    void populateCache() {
	try {
	    List<String> lRepoList = lRepoDetailsDAO.getRepoDetails();
	    CacheClient.getInstance().getAllRepositoryNames().addAll(lRepoList);
	    LOG.info("Size of getAllRepositoryNames = " + CacheClient.getInstance().getAllRepositoryNames().size());
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	    throw new WorkflowException("Error in Cache Population");
	}

	try {
	    List<String> lSubRepoList = lRepoDetailsDAO.getSubRepoDetails();
	    CacheClient.getInstance().getAllSubRepositoryNames().addAll(lSubRepoList);
	    LOG.info("Size of getAllSubRepositoryNames = " + CacheClient.getInstance().getAllSubRepositoryNames().size());
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	    throw new WorkflowException("Error in Cache Population");
	}

	populateRepoFileDetails();

	populateCommitDetails();
    }

    public void populateCommitDetails() {
	try {
	    List<String> lCommitList = lRepoCommitDAO.getFilesCommitList();
	    CacheClient.getInstance().getAllCommitNames().addAll(lCommitList);
	    LOG.info("Size of getAllCommitNames = " + CacheClient.getInstance().getAllCommitNames().size());
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	    throw new WorkflowException("Error in Cache Population");
	}
    }

    private void populateRepoFileDetails() {
	try {
	    Map<String, Integer> lSubRepoList = lRepoFileListDAO.getFilesRepoMap();
	    CacheClient.getInstance().getAllSystemFileMap().putAll(lSubRepoList);
	    LOG.info("Size of getAllSystemFileMap = " + CacheClient.getInstance().getAllSystemFileMap().size());
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	    throw new WorkflowException("Error in Cache Population");
	}
    }

    private void populateRepoDetails() {
	try {
	    Map<String, Integer> lRepoList = lRepoDetailsDAO.getRepoDetailsMap();
	    CacheClient.getInstance().getAllRepositoryDetails().putAll(lRepoList);
	    LOG.info("Size of getAllRepositoryDetails = " + CacheClient.getInstance().getAllRepositoryDetails().size());
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	}
    }

    private void populateSubRepoDetails() {
	try {
	    Map<String, Integer> lRepoList = lRepoDetailsDAO.getSubRepoDetailsMap();
	    CacheClient.getInstance().getAllSubRepositoryDetails().putAll(lRepoList);
	    LOG.info("Size of getAllSubRepositoryDetails = " + CacheClient.getInstance().getAllSubRepositoryDetails().size());

	    Map<Integer, String> lRepoIDList = lRepoDetailsDAO.getSubRepoIDWiseDetailsMap();
	    CacheClient.getInstance().getAllSubRepositoryIDDetails().putAll(lRepoIDList);
	    LOG.info("Size of getAllSubRepositoryIDDetails = " + CacheClient.getInstance().getAllSubRepositoryIDDetails().size());
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	}
    }

    public void populateFiles() {
	IMap<String, Integer> lFilesMap = CacheClient.getInstance().getAllSystemFileMap();
	IMap<String, Integer> lRepoMap = CacheClient.getInstance().getAllSubRepositoryDetails();
	IMap<Integer, String> lRepoIDMap = CacheClient.getInstance().getAllSubRepositoryIDDetails();
	List<GitMetaResult> lReturn = new ArrayList<>();
	for (Map.Entry<String, Integer> entry : lRepoMap.entrySet()) {
	    String lRepoName = entry.getKey();
	    Integer lRepoId = entry.getValue();
	    List<GitMetaResult> lFilesList = new ArrayList<>();
	    try {
		lFilesList = new GITUtils().getFilesList(AppConfig.getInstance().getGitBasePath() + lRepoName);
	    } catch (IOException ex) {
		LOG.info("Error", ex);
	    }
	    List<String> lExceptionFileNames = getExceptionFileNames();
	    for (GitMetaResult lFile : lFilesList) {
		if (lExceptionFileNames.contains(FilenameUtils.getName(lFile.getFileName()))) {
		    continue;
		}
		if (!lAllFileExtTypes.contains(FilenameUtils.getExtension(lFile.getFileName().toLowerCase()))) {
		    if (!(lRepoIDMap.get(lRepoId).contains("/ibm/") || (lFile.getFileName().toLowerCase().startsWith("oco/") && lFile.getFileName().toLowerCase().endsWith(".so")))) {
			Main.suspiciousFiles.add(FilenameUtils.removeExtension(FilenameUtils.getName(lFile.getFileName().toLowerCase())) + " | " + lFile.getFileName() + " | " + lFile.getTargetSystem() + " | " + lRepoIDMap.get(lRepoId));
			LOG.info("Suspicious File " + lFile.getFileName() + " " + lFile.getTargetSystem());
		    }
		}
		String key = lFile.getFileName() + "|" + lFile.getTargetSystem();
		if (lRepoName.contains("/ibm")) {
		    key = key + "|" + lRepoName;
		}
		Integer lDBFileRepoId = lFilesMap.get(key);
		if (lDBFileRepoId == null) {
		    // new GitMetaResult from GIT New File for a Repo
		    lFile.setSubRepoId(lRepoId);
		    lFilesMap.put(key, lRepoId);
		    lReturn.add(lFile);
		    // New Record
		    LOG.info("New File " + lFile.getFileName() + " " + lFile.getTargetSystem());
		} else if (!lDBFileRepoId.equals(lRepoId)) {
		    LOG.info("Moved/Duplicate File " + lFile.getFileName() + " " + lFile.getTargetSystem() + " " + lDBFileRepoId + " " + lRepoId);
		    Main.duplicateFiles.add(FilenameUtils.removeExtension(FilenameUtils.getName(lFile.getFileName().toLowerCase())) + " | " + lFile.getFileName() + " | " + lFile.getTargetSystem() + " | " + lRepoIDMap.get(lDBFileRepoId) + " | " + lRepoIDMap.get(lRepoId));
		    // File Moved
		}
	    }
	}
	try {
	    lRepoFileListDAO.saveRepoFileList(lReturn);
	    populateRepoFileDetails();
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	}
    }

    private List<String> getExceptionFileNames() {
	List<String> fileNames = new ArrayList<>();
	fileNames.add(".gitignore");
	fileNames.add(".gitattributes");
	fileNames.add("MigrationSummary.csv");
	fileNames.add("Missingnotok.txt");
	fileNames.add("README.md");
	fileNames.add("maketpf.cfg");
	return fileNames;
    }

    public void populateCommits() {
	IMap<String, Integer> lRepoMap = CacheClient.getInstance().getAllSubRepositoryDetails();
	// List<GitBranchSearchResult> branchSearchResults = new ArrayList<>();
	// Iterating All Repos
	List<Future> lResults = new ArrayList<>();
	for (Map.Entry<String, Integer> entry : lRepoMap.entrySet()) {
	    String lRepoName = entry.getKey();
	    Integer lRepoId = entry.getValue();

	    try {
		// Skip based on config
		if (excludedRepos.contains(lRepoName)) {
		    LOG.info("Skipping Repo " + lRepoName);
		    continue;
		}
		if (!includedRepos.isEmpty() && !includedRepos.contains(lRepoName)) {
		    LOG.info("Skipping Repo " + lRepoName);
		    continue;
		}

		lResults.add(fixedExecutorService.submit(new RepoCommitPopulate(this, lRepoName, lRepoId)));
	    } catch (Exception ex) {
		LOG.info("Error", ex);
	    }
	} // End Repo Loop

	for (Future lResult : lResults) {
	    try {
		lResult.get();
	    } catch (Exception ex) {
		LOG.info("Error", ex);
	    }
	}
	fixedExecutorService.shutdown();
    }

    void swapDatabase() {
	try {
	    List<String> oldPermissions = lRepoDetailsDAO.getOldPermissions();
	    lRepoDetailsDAO.switchData();
	    lRepoDetailsDAO.populatePermissions(oldPermissions);
	} catch (Exception ex) {
	    LOG.info("Error", ex);
	}
    }
}
