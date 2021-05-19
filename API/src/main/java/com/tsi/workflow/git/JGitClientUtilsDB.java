package com.tsi.workflow.git;

import com.tsi.workflow.beans.dao.GitProdSearchDb;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.PRODSearchType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author DINESH.RAMANATHAN
 */
@Service
@Transactional
public class JGitClientUtilsDB implements IJGITSearchUtils {

    private static final Logger LOG = Logger.getLogger(JGitClientUtilsDB.class.getName());
    @Autowired
    GitSearchDAO gitSearchDAO;
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    GITHelper gitHelper;

    public GitSearchDAO getGitSearchDAO() {
	return gitSearchDAO;
    }

    @Transactional
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos) throws IOException {
	return SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq, pAllowedRepos, null);
    }

    @Transactional
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems) throws IOException {
	return SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq, pAllowedRepos, systems, null);
    }

    @Transactional
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems, Set<String> SystemAndFuncBasedFiles) throws IOException {

	// Get Put levels for each systems
	List<String> putLevelStatus = new ArrayList<>();
	putLevelStatus.add(Constants.PUTLevelOptions.PRODUCTION.name());
	HashMap<String, String> tarSysAndPutLevel = gitHelper.getSystemBasedPutLevels(putLevelStatus);

	List<GitProdSearchDb> searchRes = getGitSearchDAO().findByProgramName(pCompany, pFileFilter, pPendingStatusReq, null, systems);

	Set<Integer> otherStatusFiles = new HashSet<>();
	otherStatusFiles = searchRes.stream().filter(t -> !t.getRefStatus().equalsIgnoreCase("New File")).map(t -> t.getFileId()).collect(Collectors.toSet());

	// Consider only new file if it is not available in online/fallback
	List<GitProdSearchDb> finalSearchRes = new ArrayList<>();
	for (GitProdSearchDb res : searchRes) {
	    if (res.getRefStatus().equalsIgnoreCase("New File") && otherStatusFiles.contains(res.getFileId())) {
		continue;
	    }
	    finalSearchRes.add(res);
	}

	HashMap<String, GitSearchResult> gitSearchMap = new HashMap<>();
	for (GitProdSearchDb git : finalSearchRes) {
	    String tarSystem = git.getTargetSystem().replace("master_", "").toUpperCase();

	    // ZTPFM-1271: IBM seg, based on target system prod put level isn't matching
	    // with the func area, then those
	    // segments should be ignored.
	    if (git.getFileType().equalsIgnoreCase(Constants.FILE_TYPE.IBM.name()) && !tarSysAndPutLevel.get(tarSystem).equalsIgnoreCase(git.getFuncArea())) {
		continue;
	    }
	    String key = git.getFileName() + "|" + git.getFileHashcode() + "|" + git.getFuncArea();
	    GitBranchSearchResult lBranchSearchResult = new GitBranchSearchResult();
	    lBranchSearchResult.setRefStatus(git.getRefStatus().equalsIgnoreCase("Online") && git.getVersion() > 1 ? git.getRefStatus() + "-" + (git.getVersion() - 1) : git.getRefStatus());
	    lBranchSearchResult.setRefPlan(git.getRefPlan());
	    lBranchSearchResult.setRefLoadDate(git.getRefLoadDateTime());
	    lBranchSearchResult.setSourceUrl(git.getSourceUrl());
	    lBranchSearchResult.setFileType(git.getFileType());
	    lBranchSearchResult.setFuncArea(git.getFuncArea());
	    lBranchSearchResult.setRepoDesc(git.getRepodesc());
	    lBranchSearchResult.setCommitId(git.getSourceCommitId());
	    lBranchSearchResult.setTargetSystem(git.getTargetSystem());
	    lBranchSearchResult.setCommitterName(git.getCommitterName());
	    lBranchSearchResult.setCommitterMailId(git.getCommitterMailId());
	    lBranchSearchResult.setCommitDateTime(git.getCommitDateTime());
	    if (pMacroHeader && !(git.getProgramName().matches("^.*\\.mac$|^.*\\.h$|^.*\\.hpp$|^.*\\.cpy$|^.*\\.inc$|^.*\\.incafs$"))) {
		lBranchSearchResult.setIsCheckoutAllowed(Boolean.FALSE);
		lBranchSearchResult.setIsBranchSelected(Boolean.FALSE);
	    }

	    if (git.getRefStatus().equalsIgnoreCase("Online") && git.getVersion() > 1) {
		lBranchSearchResult.setIsCheckoutAllowed(Boolean.FALSE);
		lBranchSearchResult.setIsBranchSelected(Boolean.FALSE);
	    }

	    // ZTPFM-1272: Restrict checkout based on the repo permission
	    if (pAllowedRepos != null && !pAllowedRepos.contains(git.getSubSourceRepo())) {
		lBranchSearchResult.setIsCheckoutAllowed(Boolean.FALSE);
		lBranchSearchResult.setIsBranchSelected(Boolean.FALSE);
	    }
	    if (gitSearchMap.containsKey(key)) {
		GitSearchResult gitSearchResult = gitSearchMap.get(key);
		boolean isBranchCanBeAdded = true;
		if (git.getRefStatus().equalsIgnoreCase("New File") && SystemAndFuncBasedFiles != null && !SystemAndFuncBasedFiles.isEmpty() && SystemAndFuncBasedFiles.contains(gitSearchResult.getFileName() + "-" + lBranchSearchResult.getTargetSystem() + "-" + lBranchSearchResult.getFuncArea())) {
		    isBranchCanBeAdded = false;
		}
		if (isBranchCanBeAdded) {
		    gitSearchResult.getTargetSystems().add(git.getTargetSystem());
		    gitSearchResult.addBranch(lBranchSearchResult);
		}
	    } else {
		GitSearchResult gitSearchResult = new GitSearchResult();
		gitSearchResult.setProdFlag(git.getRefStatus().equalsIgnoreCase("Pending") ? "LEGACY" : "PROD");
		gitSearchResult.setFileName(git.getFileName());
		gitSearchResult.setFileHashCode(git.getFileHashcode());
		gitSearchResult.setFileNameWithHash(DigestUtils.md5Hex(git.getFileName() + "|" + git.getFuncArea() + "|" + git.getFileHashcode()));
		gitSearchResult.setProgramName(git.getProgramName());
		gitSearchResult.getTargetSystems().add(git.getTargetSystem());
		gitSearchResult.addBranch(lBranchSearchResult);

		// 2061 Adding Plan id in Addt info
		gitSearchResult.addAdditionalInfo("planId", git.getRefPlan());

		boolean isBranchCanBeAdded = true;
		if (git.getRefStatus().equalsIgnoreCase("New File") && SystemAndFuncBasedFiles != null && !SystemAndFuncBasedFiles.isEmpty() && SystemAndFuncBasedFiles.contains(gitSearchResult.getFileName() + "-" + lBranchSearchResult.getTargetSystem() + "-" + lBranchSearchResult.getFuncArea())) {
		    isBranchCanBeAdded = false;
		}
		if (isBranchCanBeAdded) {
		    gitSearchMap.put(key, gitSearchResult);
		}
	    }
	}

	for (Map.Entry<String, GitSearchResult> entrySet : gitSearchMap.entrySet()) {
	    GitSearchResult lSearchResult = entrySet.getValue();
	    if (lSearchResult.getBranch().size() > 0) {
		Collections.sort(lSearchResult.getBranch(), lSearchResult.getBranch().get(0));
	    }
	}

	ArrayList<GitSearchResult> values = new ArrayList<>(gitSearchMap.values());
	Collections.sort(values, new GitSearchResult());
	return values;
    }
}
