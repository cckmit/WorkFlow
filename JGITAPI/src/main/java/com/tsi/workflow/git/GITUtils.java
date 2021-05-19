package com.tsi.workflow.git;

import static com.tsi.workflow.utils.Constants.JGIT_COMMENT_DATEFORMAT_1;
import static com.tsi.workflow.utils.Constants.JGIT_COMMENT_DATEFORMAT_2;
import static com.tsi.workflow.utils.Constants.JGIT_COMMENT_DATEFORMAT_3;
import static com.tsi.workflow.utils.Constants.JGIT_COMMENT_DATEFORMAT_4;
import static com.tsi.workflow.utils.Constants.JGIT_COMMENT_DATEFORMAT_5;
import static com.tsi.workflow.utils.Constants.JGIT_COMMENT_DATEFORMAT_6;
import static com.tsi.workflow.utils.Constants.LOG_MESSAGE_FORMAT1;
import static com.tsi.workflow.utils.Constants.LOG_MESSAGE_FORMAT2;
import static com.tsi.workflow.utils.Constants.LOG_MESSAGE_FORMAT3;
import static com.tsi.workflow.utils.Constants.LOG_MESSAGE_FORMAT4;
import static com.tsi.workflow.utils.Constants.LOG_MESSAGE_FORMAT5;
import static com.tsi.workflow.utils.Constants.LOG_MESSAGE_FORMAT6;

import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.interfaces.IGitConfig;
import com.tsi.workflow.utils.Constants.FILE_TYPE;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.lib.CommitBuilder;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectInserter;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefUpdate;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.lib.TreeFormatter;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevObject;
import org.eclipse.jgit.revwalk.RevTag;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GITUtils {

    IGitConfig gitConfig;

    public GITUtils(IGitConfig gitConfig) {
	this.gitConfig = gitConfig;

    }

    private static final Logger LOG = Logger.getLogger(GITUtils.class.getName());

    public Boolean doGC(String pCompany, String pRepoName, Boolean isSource) {
	List<String> lRepos = new ArrayList<>();
	if (!isSource && pRepoName.startsWith("derived_")) {
	    String lRepoIBMName = FilenameUtils.separatorsToUnix(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + "ibm" + File.separator + pRepoName.toLowerCase() + ".git");
	    String lRepoNonIBMName = FilenameUtils.separatorsToUnix(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + "nonibm" + File.separator + pRepoName.toLowerCase() + ".git");
	    if (new File(lRepoIBMName).exists()) {
		lRepos.add(lRepoIBMName);
	    }
	    if (new File(lRepoNonIBMName).exists()) {
		lRepos.add(lRepoNonIBMName);
	    }
	} else if (isSource && pRepoName.startsWith("nonibm_")) {
	    lRepos.add(FilenameUtils.separatorsToUnix(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + "nonibm" + File.separator + pRepoName.toLowerCase() + ".git"));
	} else if (isSource && pRepoName.startsWith("ibm_")) {
	    lRepos.add(FilenameUtils.separatorsToUnix(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + "ibm" + File.separator + pRepoName.toLowerCase() + ".git"));
	} else if (isSource) {
	    lRepos.add(FilenameUtils.separatorsToUnix(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pRepoName.toLowerCase() + ".git"));
	} else {
	    lRepos.add(FilenameUtils.separatorsToUnix(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitDerivedPath() + pRepoName.toLowerCase() + ".git"));
	}
	for (String lRepo : lRepos) {
	    Git lGit = null;
	    File lRepoSourceDir = new File(lRepo);
	    if (!lRepoSourceDir.exists()) {
		LOG.error("Repo not Exists : " + lRepo);
		return false;
	    }
	    try {
		lGit = Git.open(lRepoSourceDir);
		lGit.gc().call();
		lGit.close();
	    } catch (Exception ex) {
		LOG.error("Exception in Reading Repo : " + lRepo, ex);
		return false;
	    } finally {
		if (lGit != null) {
		    lGit.close();
		}
	    }
	}
	return true;
    }

    public Collection<GitSearchResult> searchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, String pBranchName, Integer pPendingStatusReq, List<String> pReposAllowed) throws WorkflowException {
	File lBaseDir = new File(gitConfig.getGitBasePath() + gitConfig.getGitProdPath(), pCompany);

	if (!lBaseDir.exists()) {
	    throw new WorkflowException("Company Path (" + lBaseDir.getAbsolutePath() + ") doesn't Exist");
	}

	if (pFileFilter == null || pFileFilter.isEmpty()) {
	    throw new WorkflowException("No Search Criteria");
	}

	SortedMap<String, GitSearchResult> lResult = new TreeMap<>();

	if (pReposAllowed == null || pReposAllowed.isEmpty()) {
	    List<String> lRepositoryPaths = listProductionRepositories(lBaseDir);
	    LOG.info("Searching Repositories Count : " + lRepositoryPaths.size());
	    for (String lRepositoryPath : lRepositoryPaths) {
		searchRepo(lResult, lRepositoryPath, pFileFilter, pMacroHeader, pBranchName, pPendingStatusReq);
	    }
	} else {
	    LOG.info("Searching Repositories Count : " + pReposAllowed.size());
	    for (String lRepositoryPath : pReposAllowed) {
		if (!lRepositoryPath.contains("/derived_")) {
		    searchRepo(lResult, gitConfig.getGitBasePath() + lRepositoryPath, pFileFilter, pMacroHeader, pBranchName, pPendingStatusReq);
		}
	    }
	}

	ArrayList<GitSearchResult> values = new ArrayList<>(lResult.values());
	Collections.sort(values, new GitSearchResult());
	return values;
    }

    private void searchRepo(Map<String, GitSearchResult> lResult, String pRepoPath, String pFileFilter, Boolean pMacroHeader, String pBranchName, Integer pPendingStatusReq) throws WorkflowException {
	LOG.debug("Searching Repo : " + pRepoPath);
	Git lGit = null;
	Repository lRepository = null;
	File lRepoDir = new File(pRepoPath);

	if (!lRepoDir.exists()) {
	    throw new WorkflowException("Git Repository (" + lRepoDir + ") doesn't Exist");
	}

	try {
	    lGit = Git.open(lRepoDir);
	    lRepository = lGit.getRepository();
	    List<Ref> lFullBranchesNames = new ArrayList<>();
	    if (pBranchName != null) {
		lFullBranchesNames = listBranches(lGit, pBranchName);
	    } else {
		lFullBranchesNames = listBranches(lGit);
	    }

	    List<GitMetaResult> lFilteredFiles = new ArrayList<>();
	    for (Ref lBranch : lFullBranchesNames) {
		lFilteredFiles.addAll(getFilesList(lRepository, lBranch, pFileFilter));
	    }

	    if (!lFilteredFiles.isEmpty()) {
		Map<String, List> lRedefinedTags = getCommitedTags(lRepository);
		getSearchResults(lResult, lFilteredFiles, lRedefinedTags, lGit, pMacroHeader, pPendingStatusReq);
	    }
	} catch (Exception ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}

    }

    public List<GitMetaResult> getFilesListOfCommit(String lUrl, String pCommit, String pFileFilter) throws WorkflowException {
	List<GitMetaResult> lFilteredFiles = new ArrayList<>();
	Git lGit = null;
	try {
	    String lPath = lUrl.replace("ssh://" + gitConfig.getGitHost() + ":" + gitConfig.getGitDataPort(), gitConfig.getGitBasePath());
	    lGit = Git.open(new File(lPath));
	    Repository lRepository = lGit.getRepository();
	    RevWalk revWalk = new RevWalk(lRepository);
	    RevCommit commit = revWalk.parseCommit(ObjectId.fromString(pCommit));
	    RevTree tree = commit.getTree();
	    TreeWalk treeWalk = new TreeWalk(lRepository);
	    treeWalk.addTree(tree);
	    treeWalk.setRecursive(Boolean.TRUE);
	    while (treeWalk.next()) {
		String lFileName = FilenameUtils.getName(treeWalk.getPathString());
		if (lFileName.toLowerCase(com.tsi.workflow.utils.Constants.LOCALE).equals(pFileFilter.toLowerCase(com.tsi.workflow.utils.Constants.LOCALE))) {
		    lFilteredFiles.add(new GitMetaResult(null, null, treeWalk.getObjectId(0).name(), treeWalk.getPathString()));
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Exception in getting file list", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}
	return lFilteredFiles;
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
		    if (lFileName.toLowerCase(com.tsi.workflow.utils.Constants.LOCALE).startsWith(pFileFilter.toLowerCase(com.tsi.workflow.utils.Constants.LOCALE))) {
			lFilteredFiles.add(new GitMetaResult(lRepository, pBranch, treeWalk.getObjectId(0).name(), treeWalk.getPathString()));
		    }
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Exception in getting file list", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	}
	return lFilteredFiles;
    }

    private Map<String, List> getCommitedTags(Repository lRepository) throws Exception {
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

    private void getSearchResults(Map<String, GitSearchResult> lResult, List<GitMetaResult> lFilteredFiles, Map<String, List> lRedefinedTags, Git lGit, Boolean pMacroHeader, Integer pPendingStatusReq) throws WorkflowException {
	LOG.info("Searching for Files Count : " + lFilteredFiles.size());
	for (GitMetaResult lFile : lFilteredFiles) {
	    getSearchResult(lFile, lResult, lRedefinedTags, lGit, pMacroHeader, pPendingStatusReq);
	}
	for (Map.Entry<String, GitSearchResult> entrySet : lResult.entrySet()) {
	    GitSearchResult lSearchResult = entrySet.getValue();
	    if (lSearchResult.getBranch().size() > 0) {
		Collections.sort(lSearchResult.getBranch(), lSearchResult.getBranch().get(0));
	    }
	}
    }

    private void getSearchResult(GitMetaResult lFile, Map<String, GitSearchResult> lResult, Map<String, List> lRedefinedTags, Git lGit, Boolean pMacroHeader, Integer pPendingStatusReq) throws WorkflowException {
	try {
	    RevWalk walk = new RevWalk(lFile.getRepository());
	    Iterable<RevCommit> logs = lGit.log().add(lFile.getBranch().getObjectId()).addPath(lFile.getFileName()).call();

	    String lUrl = "ssh://" + gitConfig.getWfLoadBalancerHost() + ":" + gitConfig.getGitDataPort() + lFile.getRepository().getDirectory().getAbsolutePath().replace(gitConfig.getGitBasePath(), File.separator);
	    String lRepoName = lFile.getRepository().getDirectory().getName();
	    StoredConfig lConfig = lFile.getRepository().getConfig();
	    String lDesc = lConfig.getString("gitblit", null, "description");
	    String isFrozen = lConfig.getString("gitblit", null, "isFrozen");

	    int onlineCount = 0;
	    int fallbackCount = 0;

	    for (RevCommit lCommitLogs : logs) {
		if (walk.isMergedInto(walk.parseCommit(lCommitLogs), walk.parseCommit(lFile.getBranch().getObjectId()))) {
		    GitBranchSearchResult lBranchSearchResult = new GitBranchSearchResult();
		    List lTags = lRedefinedTags.get(lCommitLogs.getId().name());
		    if (lTags.get(0).toString().startsWith("online")) {
			if (pPendingStatusReq == com.tsi.workflow.utils.Constants.PRODSearchType.ONLINE_ONLY.ordinal() || pPendingStatusReq == com.tsi.workflow.utils.Constants.PRODSearchType.BOTH.ordinal()) {
			    if (onlineCount == 0) {
				lBranchSearchResult.setRefStatus("Online");
			    } else {
				lBranchSearchResult.setRefStatus("Online-" + onlineCount);
				lBranchSearchResult.setIsCheckoutAllowed(Boolean.FALSE);
			    }
			    onlineCount++;
			} else {
			    onlineCount++;
			    continue;
			}
		    } else if (lTags.get(0).toString().startsWith("fallback")) {
			if (pPendingStatusReq == com.tsi.workflow.utils.Constants.PRODSearchType.ONLINE_ONLY.ordinal() || pPendingStatusReq == com.tsi.workflow.utils.Constants.PRODSearchType.BOTH.ordinal()) {
			    lBranchSearchResult.setRefStatus("Fallback");
			    fallbackCount++;
			} else {
			    continue;
			}
		    } else if (lTags.get(0).toString().startsWith("delete")) {
			return;
		    } else if (lTags.get(0).toString().startsWith("pending")) {
			if (pPendingStatusReq == com.tsi.workflow.utils.Constants.PRODSearchType.PENDING_ONLY.ordinal() || pPendingStatusReq == com.tsi.workflow.utils.Constants.PRODSearchType.BOTH.ordinal()) {
			    lBranchSearchResult.setRefStatus("Pending");
			} else {
			    continue;
			}
		    } else {
			continue;
		    }
		    // Changed using regex, Supported Formats
		    // Date:201404090000, Type:Load, PlanID:_d140076001, PlanOwner:c190282,
		    // SourcRef:xxxxxxx, Status:online
		    // Date:20140409, Type:Load, PlanID:_d140076001, PlanOwner:c190282,
		    // SourcRef:xxxxxxx, Status:online
		    // {Date:20140409, Type:Load, PlanID:_d140076001, PlanOwner:c190282,
		    // SourcRef:xxxxxxx, Status:online}
		    // {Date:201404090000, Type:Load, PlanID:_d140076001, PlanOwner:c190282,
		    // SourcRef:xxxxxxx, Status:online}
		    Matcher matcher1 = LOG_MESSAGE_FORMAT1.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
		    Matcher matcher2 = LOG_MESSAGE_FORMAT2.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
		    Matcher matcher3 = LOG_MESSAGE_FORMAT3.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
		    Matcher matcher4 = LOG_MESSAGE_FORMAT4.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
		    Matcher matcher5 = LOG_MESSAGE_FORMAT5.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());
		    Matcher matcher6 = LOG_MESSAGE_FORMAT6.matcher(lCommitLogs.getShortMessage().replaceAll("\\{", "").replaceAll("\\}", "").trim());

		    Date lLoadDate = null;
		    // String lStatus = "";
		    String lRefPlan = "";
		    if (matcher1.matches()) {
			lLoadDate = JGIT_COMMENT_DATEFORMAT_1.get().parse(matcher1.group(1));
			lRefPlan = matcher1.group(2);
			// lStatus = matcher1.group(3);
		    } else if (matcher2.matches()) {
			lLoadDate = JGIT_COMMENT_DATEFORMAT_2.get().parse(matcher2.group(1));
			lRefPlan = matcher2.group(2);
			// lStatus = matcher2.group(3);
		    } else if (matcher3.matches()) {
			lLoadDate = JGIT_COMMENT_DATEFORMAT_3.get().parse(matcher3.group(1));
			lRefPlan = matcher3.group(2);
			// lStatus = matcher3.group(3);
		    } else if (matcher4.matches()) {
			lLoadDate = JGIT_COMMENT_DATEFORMAT_4.get().parse(matcher4.group(1));
			lRefPlan = matcher4.group(2);
			// lStatus = matcher4.group(3);
		    } else if (matcher5.matches()) {
			lLoadDate = JGIT_COMMENT_DATEFORMAT_5.get().parse(matcher5.group(1));
			lRefPlan = matcher5.group(2);
			// lStatus = matcher5.group(3);
		    } else if (matcher6.matches()) {
			lLoadDate = JGIT_COMMENT_DATEFORMAT_6.get().parse(matcher6.group(1));
			lRefPlan = matcher6.group(2);
			// lStatus = matcher6.group(3);
		    } else {
			continue;
		    }

		    lBranchSearchResult.setRefPlan(lRefPlan);
		    lBranchSearchResult.setRefLoadDate(lLoadDate);
		    lBranchSearchResult.setRepoDesc(lDesc);
		    lBranchSearchResult.setSourceUrl(lUrl);
		    lBranchSearchResult.setFileType(lUrl.contains(File.separator + "ibm" + File.separator) ? FILE_TYPE.IBM.name() : FILE_TYPE.NON_IBM.name());
		    lBranchSearchResult.setFuncArea(lRepoName);
		    // lBranchSearchResult.setStatus(isFrozen);
		    lBranchSearchResult.setCommitId(lCommitLogs.getId().name());

		    RevTree tree = lCommitLogs.getTree();
		    TreeWalk treeWalk = new TreeWalk(lFile.getRepository());
		    treeWalk.addTree(tree);
		    treeWalk.setRecursive(Boolean.TRUE);
		    GitMetaResult lNewFile = null;
		    while (treeWalk.next()) {
			if (treeWalk.getPathString().toLowerCase().equals(lFile.getFileName().toLowerCase())) {
			    lNewFile = new GitMetaResult(lFile.getRepository(), lFile.getBranch(), treeWalk.getObjectId(0).name(), treeWalk.getPathString());
			    break;
			}
		    }
		    if (lNewFile == null) {
			continue;
		    }

		    lBranchSearchResult.setAuthorName(lCommitLogs.getAuthorIdent().getName());
		    lBranchSearchResult.setTargetSystem(lNewFile.getBranch().getName().replace(Constants.R_HEADS, ""));
		    lBranchSearchResult.setAuthorMailId(lCommitLogs.getAuthorIdent().getEmailAddress());
		    lBranchSearchResult.setCommitterName(lCommitLogs.getCommitterIdent().getName());
		    lBranchSearchResult.setCommitterMailId(lCommitLogs.getCommitterIdent().getEmailAddress());
		    lBranchSearchResult.setCommitDateTime(new Date(lCommitLogs.getCommitTime() * 1000L));
		    lBranchSearchResult.setSubject(lCommitLogs.getShortMessage());
		    lBranchSearchResult.setTags(lTags);

		    if (pMacroHeader && !(lNewFile.getProgramName().matches("^.*\\.mac$|^.*\\.h$|^.*\\.hpp$|^.*\\.cpy$|^.*\\.inc$|^.*\\.incafs$"))) {
			lBranchSearchResult.setIsCheckoutAllowed(Boolean.FALSE);
		    }

		    GitSearchResult lSearchResult = lResult.get(lNewFile.getFileNametoGroupSort());
		    if (lSearchResult == null) {
			lSearchResult = new GitSearchResult();
			lSearchResult.setFileName(lNewFile.getFileName());
			lSearchResult.setFileHashCode(lNewFile.getFileHashCode());
			lSearchResult.setFileNameWithHash(lNewFile.getFileNameWithHash());
			lSearchResult.setProgramName(lNewFile.getProgramName());
			lResult.put(lNewFile.getFileNametoGroupSort(), lSearchResult);
		    }
		    lSearchResult.getTargetSystems().add(lBranchSearchResult.getTargetSystem());
		    lSearchResult.addBranch(lBranchSearchResult);
		}
		if (onlineCount > 2) {
		    return;
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Exception in getting file list", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	}

    }

    public boolean isImplementationPlanRepoExist(String pCompany, String pImplPlanId) throws WorkflowException {
	File lRepoDir = new File(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	LOG.info(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	return lRepoDir.exists();

    }

    public boolean isImplementationBranchExist(String pCompany, String pImplPlanId, String pBranchName) throws WorkflowException {
	File lRepoDir = new File(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	LOG.info("Searching for Repo : " + gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	if (!lRepoDir.exists()) {
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist");
	}
	Git lGit = null;
	try {
	    lGit = Git.open(lRepoDir);
	    List<Ref> lBranches = lGit.branchList().call();
	    for (Ref lBranch : lBranches) {
		if (lBranch.getName().equals(Constants.R_HEADS + pBranchName.toLowerCase())) {
		    return Boolean.TRUE;
		}
	    }
	} catch (IOException ex) {
	    LOG.error("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist", ex);
	} catch (GitAPIException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}
	return Boolean.FALSE;
    }

    public List<String> getAllBranchList(String pCompany, String pImplPlanId) throws WorkflowException {
	File lRepoDir = new File(gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	LOG.info("Searching for Repo : " + gitConfig.getGitBasePath() + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	if (!lRepoDir.exists()) {
	    LOG.error("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist");
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist");
	}

	List<String> lReturn = new ArrayList<>();
	Git lGit = null;
	try {
	    lGit = Git.open(lRepoDir);
	    List<Ref> lBranches = lGit.branchList().call();
	    for (Ref lBranch : lBranches) {
		lReturn.add(lBranch.getName().replaceAll(Constants.R_HEADS, ""));
	    }
	} catch (IOException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist", ex);
	} catch (GitAPIException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
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

    private List<Ref> listBranches(Git lGit, String pBranchName) throws WorkflowException {
	List<Ref> lReturn = new ArrayList<>();
	try {
	    List<Ref> branches = lGit.branchList().call();
	    for (Ref branch : branches) {
		if (branch.getName().equals(Constants.R_HEADS + Constants.MASTER + "_" + pBranchName.toLowerCase())) {
		    lReturn.add(branch);
		}
	    }
	} catch (GitAPIException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	}
	return lReturn;
    }

    private List<String> listProductionRepositories(File pSourceDirectory) {
	return listProductionRepositories(null, null, pSourceDirectory);
    }

    private List<String> listProductionRepositories(File pSourceDir, File pDerivedDir, File pBaseDir) {
	return listProductionRepositories(pSourceDir, pDerivedDir, pBaseDir, false);
    }

    public Map<String, Boolean> createBranches(String pImplPlanId, Set<String> BranchNames, String pCompany) throws WorkflowException {
	String lSourceRepository = getPlanRepoFullName(pCompany, pImplPlanId);
	Map<String, Boolean> lResult = createBranches(lSourceRepository, BranchNames, true);
	String lDerivedRepository = getPlanLFSRepoFullName(pCompany, pImplPlanId);
	lResult.putAll(createBranches(lDerivedRepository, BranchNames, false));
	return lResult;
    }

    private Map<String, Boolean> createBranches(String pRepositoty, Set<String> BranchNames, boolean isSource) throws WorkflowException {
	Git lGit = null;
	String lType = isSource ? "Source" : "Derived";
	Map<String, Boolean> lResult = new HashMap();
	try {
	    LOG.info("Creating Branches for : " + pRepositoty);

	    File lRepoDir = new File(gitConfig.getGitBasePath(), pRepositoty);

	    if (!lRepoDir.exists()) {
		throw new WorkflowException("Git Repository (" + lRepoDir + ") doesn't Exist");
	    }

	    lGit = Git.open(lRepoDir);
	    List<Ref> lBranches = lGit.branchList().call();
	    boolean isMasterPresent = Boolean.FALSE;
	    for (Ref lBranch : lBranches) {
		if (lBranch.getName().equals(Constants.R_HEADS + Constants.MASTER)) {
		    isMasterPresent = Boolean.TRUE;
		}
		BranchNames.remove(lBranch.getName().replace(Constants.R_HEADS, ""));
	    }

	    if (!isMasterPresent) {
		createOrphanBranch(lGit.getRepository(), Constants.MASTER, isSource);
	    }

	    for (String BranchName : BranchNames) {
		try {
		    if (isSource || BranchName.contains(Constants.MASTER)) {
			lGit.branchCreate().setStartPoint(Constants.MASTER).setName(BranchName.toLowerCase()).call();
			lResult.put(lType + " " + BranchName, Boolean.TRUE);
		    }
		} catch (Exception e) {
		    LOG.warn(lType + " " + BranchName + " is not Created", e);
		    lResult.put(lType + " " + BranchName, Boolean.FALSE);
		}
	    }
	} catch (Exception ex) {
	    LOG.error("Error on Execution : ", ex);
	    throw new WorkflowException("Unable to create branches", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}
	return lResult;
    }

    private boolean createOrphanBranch(Repository repository, String branchName, boolean isSource) {
	boolean success = Boolean.FALSE;
	String lCommitMessage = "Initial Commit";
	String lGitIgnoreContent = ".gitref/*\n.mtpgitref";
	String lGitAttribContent = "lib/* binary\nload/* binary\nexp/* binary\nobj/* binary\nbase/lib/* binary\nbase/load/* binary\nbase/exp/* binary\nbase/obj/* binary\nbss/lib/* binary\nbss/load/* binary\nbss/exp/* binary\nbss/obj/* binary\nderived/* binary\nlib/** filter=lfs diff=lfs merge=lfs -text\nload/** filter=lfs diff=lfs merge=lfs -text\nexp/** filter=lfs diff=lfs merge=lfs -text\nobj/** filter=lfs diff=lfs merge=lfs -text\nbase/lib/** filter=lfs diff=lfs merge=lfs -text\nbase/load/** filter=lfs diff=lfs merge=lfs -text\nbase/exp/** filter=lfs diff=lfs merge=lfs -text\nbase/obj/** filter=lfs diff=lfs merge=lfs -text\nbss/lib/** filter=lfs diff=lfs merge=lfs -text\nbss/load/** filter=lfs diff=lfs merge=lfs -text\nbss/exp/** filter=lfs diff=lfs merge=lfs -text\nbss/obj/** filter=lfs diff=lfs merge=lfs -text\nderived/** filter=lfs diff=lfs merge=lfs -text";
	PersonIdent author = new PersonIdent(gitConfig.getServiceUserID(), "");

	try {
	    ObjectInserter odi = repository.newObjectInserter();
	    try {
		ObjectId gitIgnoreBlobId = odi.insert(Constants.OBJ_BLOB, lGitIgnoreContent.getBytes(Constants.CHARACTER_ENCODING));
		TreeFormatter tree = new TreeFormatter();
		if (!isSource) {
		    ObjectId gitAttributesBlobId = odi.insert(Constants.OBJ_BLOB, lGitAttribContent.getBytes(Constants.CHARACTER_ENCODING));
		    tree.append(".gitattributes", FileMode.REGULAR_FILE, gitAttributesBlobId);
		}
		tree.append(".gitignore", FileMode.REGULAR_FILE, gitIgnoreBlobId);
		ObjectId treeId = odi.insert(tree);

		CommitBuilder commit = new CommitBuilder();
		commit.setAuthor(author);
		commit.setCommitter(author);
		commit.setEncoding(Constants.CHARACTER_ENCODING);
		commit.setMessage(lCommitMessage);
		commit.setTreeId(treeId);

		ObjectId commitId = odi.insert(commit);
		odi.flush();

		RevWalk revWalk = new RevWalk(repository);
		try {
		    RevCommit revCommit = revWalk.parseCommit(commitId);
		    if (!branchName.startsWith("refs/")) {
			branchName = "refs/heads/" + branchName;
		    }
		    RefUpdate ru = repository.updateRef(branchName);
		    ru.setNewObjectId(commitId);
		    ru.setRefLogMessage("commit: " + revCommit.getShortMessage(), false);
		    RefUpdate.Result rc = ru.forceUpdate();
		    switch (rc) {
		    case NEW:
		    case FORCED:
		    case FAST_FORWARD:
			success = true;
			break;
		    default:
			success = false;
		    }
		} finally {
		    revWalk.close();
		}
	    } finally {
		odi.close();
	    }
	} catch (Exception e) {
	    LOG.info("Unable to create Master Branch", e);
	    System.out.println("Failed to create orphan branch " + branchName + " in repository ");
	}
	return success;
    }

    public boolean isRepositoryExist(String pRepositoryName) {
	File lRepoDir = new File(gitConfig.getGitBasePath() + pRepositoryName);
	LOG.info("Checking for Repo " + gitConfig.getGitBasePath() + pRepositoryName);
	return lRepoDir.exists();
    }

    public boolean isBranchExistInRepository(String pRepositoryName, String pBranchName) throws WorkflowException {
	File lRepoDir = new File(gitConfig.getGitBasePath() + pRepositoryName);
	LOG.debug("Searching for Repo : " + gitConfig.getGitBasePath() + pRepositoryName);
	if (!lRepoDir.exists()) {
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist");
	}
	Git lGit = null;
	try {
	    lGit = Git.open(lRepoDir);
	    List<Ref> lBranches = lGit.branchList().call();
	    for (Ref lBranch : lBranches) {
		LOG.debug("Branches : " + lBranch.getName());
		if (lBranch.getName().equals(Constants.R_HEADS + pBranchName.toLowerCase())) {
		    return Boolean.TRUE;
		}
	    }
	} catch (IOException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Repository (" + lRepoDir.getAbsolutePath() + ") doesn't Exist", ex);
	} catch (GitAPIException ex) {
	    LOG.info("Exception in Reading Repo ", ex);
	    throw new WorkflowException("GIT Api execution error", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}
	return Boolean.FALSE;
    }

    public List<String> getProductionRepoList(String pCompanyName) throws WorkflowException {
	File lBaseDir = new File(gitConfig.getGitBasePath() + gitConfig.getGitProdPath(), pCompanyName);
	if (!lBaseDir.exists()) {
	    throw new WorkflowException("Company Path (" + lBaseDir.getAbsolutePath() + ") doesn't Exist");
	}
	List<String> lRepoList = new ArrayList<>();
	for (String lRepoName : listProductionRepositories(lBaseDir)) {
	    lRepoName = lRepoName.replaceAll(gitConfig.getGitBasePath(), "");
	    lRepoList.add(lRepoName);
	}
	return lRepoList;
    }

    // <editor-fold defaultstate="collapsed" desc="Repo URL APIs">
    public String getPlanSSHURL(String pCompany, String pImplPlanId, String pUser) {
	return "ssh://" + pUser + "@" + gitConfig.getGitHost() + ":" + gitConfig.getGitDataPort() + FilenameUtils.separatorsToUnix(File.separator + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Repo Name APIs">
    public String getPlanRepoName(String pCompany, String pImplPlanId) {
	return FilenameUtils.separatorsToUnix(File.separator + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase());
    }

    public String getPlanLFSRepoName(String pCompany, String pImplPlanId) {
	return FilenameUtils.separatorsToUnix(File.separator + gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitDerivedPath() + pImplPlanId.toLowerCase());
    }

    public String getPlanRepoFullName(String pCompany, String pImplPlanId) {
	return getPlanRepoName(pCompany, pImplPlanId) + ".git";
    }

    public String getPlanLFSRepoFullName(String pCompany, String pImplPlanId) {
	return getPlanLFSRepoName(pCompany, pImplPlanId) + ".git";
    }
    // </editor-fold>

    public Boolean deleteProdBranches(String pCompanyName, List<String> pDeleteBranches) throws WorkflowException, IOException, GitAPIException {
	Git lGit = null;
	try {
	    File lBaseDir = new File(gitConfig.getGitBasePath() + gitConfig.getGitProdPath(), pCompanyName);
	    List<String> listProductionRepositories = listProductionRepositories(lBaseDir, true);
	    for (String lRepo : listProductionRepositories) {
		File lRepoDir = new File(lRepo);
		if (!lRepoDir.exists()) {
		    throw new WorkflowException("Git Repository (" + lRepoDir + ") doesn't Exist");
		}
		lGit = Git.open(lRepoDir);

		for (String pDeleteBranch : pDeleteBranches) {
		    if (!pDeleteBranch.startsWith("master")) {
			String lBranchName = Constants.R_HEADS + pDeleteBranch.toLowerCase();
			try {
			    List<Ref> call = lGit.branchList().setContains(lBranchName).call();

			    if (!call.isEmpty()) {
				LOG.info("Deleting Branch Name - " + lBranchName + " from Repository " + lRepo);
				lGit.branchDelete().setBranchNames(lBranchName).setForce(true).call();
			    }
			} catch (RefNotFoundException ex) {
			    LOG.warn("Branch is not avaiable in Repo - " + lRepo + " , Branch Name - " + lBranchName, ex);
			}
		    }
		}
	    }
	} catch (WorkflowException ex) {
	    LOG.error("Error in Deleting the Branches", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error in Deleting the Branches", ex);
	    throw new WorkflowException("Error in Deleting the Branches", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}
	return true;
    }

    private List<String> listProductionRepositories(File pSourceDirectory, boolean isDerivedNeeded) {
	return listProductionRepositories(null, null, pSourceDirectory, isDerivedNeeded);
    }

    private List<String> listProductionRepositories(File pSourceDir, File pDerivedDir, File pBaseDir, boolean isDerivedNeeded) {
	List<String> lReturn = new ArrayList<>();
	if (pSourceDir == null) {
	    pSourceDir = new File(pBaseDir, gitConfig.getGitSourcePath());
	}
	if (pDerivedDir == null) {
	    pDerivedDir = new File(pBaseDir, gitConfig.getGitDerivedPath());
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

	for (File lDir : lDirs) {
	    if (lDir.getName().endsWith(".git")) {
		lReturn.add(lDir.getAbsolutePath());
	    } else {
		lReturn.addAll(listProductionRepositories(pSourceDir, pDerivedDir, lDir, isDerivedNeeded));
	    }
	}
	return lReturn;
    }

    public List<String> getProdRepoBranches(String lRepoName) {
	List<String> lReturnList = new ArrayList();
	try {
	    if (isRepositoryExist(lRepoName)) {
		File lFile = new File(gitConfig.getGitBasePath() + lRepoName);
		Git lGit = Git.open(lFile);
		List<Ref> lBranclist = listBranches(lGit);
		lBranclist.forEach((lBranch) -> {
		    lReturnList.add(lBranch.getName().replaceAll(Constants.R_HEADS, ""));
		});
	    }
	} catch (Exception ex) {
	    LOG.error("unable to get Production Repository branches, Repo Name -  " + lRepoName, ex);
	    throw new WorkflowException("unable to get Production Repository branches, Repo Name -  " + lRepoName, ex);
	}
	return lReturnList;
    }

    public Boolean deletePlansImpBranches(String pPlanId, String pImpId, String pCompanyName, List<String> pDeleteBranches) throws WorkflowException, IOException, GitAPIException {
	Git lGit = null;
	try {

	    String lSourceRepository = getPlanRepoFullName(pCompanyName, pPlanId);
	    String lDerivedRepository = getPlanLFSRepoFullName(pCompanyName, pPlanId);
	    LOG.info(lSourceRepository);
	    File lRepoDir = new File(gitConfig.getGitBasePath(), lSourceRepository);
	    if (!lRepoDir.exists()) {
		throw new WorkflowException("Git Repository (" + lRepoDir + ") doesn't Exist");
	    }
	    lGit = Git.open(lRepoDir);

	    for (String pDeleteBranch : pDeleteBranches) {
		if (!pDeleteBranch.startsWith("master")) {
		    String lBranchName = Constants.R_HEADS + pDeleteBranch.toLowerCase();
		    try {
			List<Ref> call = lGit.branchList().setContains(lBranchName).call();

			if (!call.isEmpty()) {
			    LOG.info("Deleting Branch Name - " + lBranchName + " from Repository " + lSourceRepository);
			    lGit.branchDelete().setBranchNames(lBranchName).setForce(true).call();
			}
		    } catch (RefNotFoundException ex) {
			LOG.warn("Branch is not avaiable in Repo - " + lSourceRepository + " , Branch Name - " + lBranchName, ex);
		    }
		}
	    }

	} catch (WorkflowException ex) {
	    LOG.error("Error in Deleting the Branches", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Error in Deleting the Branches", ex);
	    throw new WorkflowException("Error in Deleting the Branches", ex);
	} finally {
	    if (lGit != null) {
		lGit.close();
	    }
	}
	return true;
    }

}
