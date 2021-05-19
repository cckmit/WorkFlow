/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.GitUserModel;
import com.tsi.workflow.beans.ui.GitUsers;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.beans.ui.SourceArtifactSearchResult;
import com.tsi.workflow.beans.ui.UserDetails;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.gitblit.model.AccessPermission;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.RepositoryPermission;
import com.tsi.workflow.gitblit.model.UserModel;
import com.tsi.workflow.utils.Constants;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author vinoth
 */
@Component
public class GITHelper {

    private static final Logger LOG = Logger.getLogger(GITHelper.class.getName());

    @Autowired
    PlatformDAO platformDAO;
    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    JGitClientUtils lGitUtils;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    CacheClient cacheClient;
    List<String> lExcceptionalPaths = new ArrayList<>();
    @Autowired
    PutLevelDAO putLevelDAO;
    @Autowired
    ImplementationDAO implementationDAO;
    @Autowired
    GitSearchDAO gitSearchDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;

    public PlatformDAO getPlatformDAO() {
	return platformDAO;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public CheckoutSegmentsDAO getCheckoutSegmentsDAO() {
	return checkoutSegmentsDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public GitSearchDAO gitSearchDAO() {
	return gitSearchDAO;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public SystemLoadDAO getSystemLoadDAO() {
	return systemLoadDAO;
    }

    public String getRepositoryNameBySourceURL(String pSourceURL) {
	String lRepoNamePrefix = "ssh://" + gITConfig.getWfLoadBalancerHost() + ":" + gITConfig.getGitDataPort() + "/";
	return pSourceURL.replace(lRepoNamePrefix, "");
    }

    public String getSourceURLByRepoName(String pRepoName) {
	String lRepoNamePrefix = "ssh://" + gITConfig.getWfLoadBalancerHost() + ":" + gITConfig.getGitDataPort() + "/";
	return lRepoNamePrefix + pRepoName;
    }

    public Boolean populateGitCacheNew() {
	LOG.info("Populating Production Repositories Cache from Gitblit DB");
	HashMap<String, HashMap<String, AccessPermission>> RepoWiseUserPermission = new HashMap<>();
	HashMap<String, RepositoryView> lAllProdRepoView = new HashMap<>();
	Map<String, Set<Repository>> lAllRepositoryMap = new HashMap<>();

	List<GitUserModel> gitDBUserModel = gitSearchDAO().getGitUserPermDetails();

	HashMap<String, UserModel> UserNameWise = convertToGitblitUserModel(gitDBUserModel);

	populateRepositoryWisePermissions(gitDBUserModel, RepoWiseUserPermission, lAllProdRepoView, lAllRepositoryMap);

	updateCache(UserNameWise, RepoWiseUserPermission, lAllProdRepoView, lAllRepositoryMap);

	updateImplementationPlanRepoPermissions(null);
	return true;
    }

    private HashMap<String, UserModel> convertToGitblitUserModel(List<GitUserModel> gitUserModel) {

	HashMap<String, UserModel> userModelMap = new HashMap<>();

	gitUserModel.forEach(userModel -> {
	    UserModel uModel = new UserModel();
	    if (userModelMap.containsKey(userModel.getUsername())) {
		uModel = userModelMap.get(userModel.getUsername());
	    } else {
		uModel.setUsername(userModel.getUsername());
		uModel.setDisplayName(userModel.getDisplayname());
	    }
	    uModel.getRepositories().add(userModel.getSubsourcerepo());
	    uModel.getRepositories().add(userModel.getSubderivedrepo());

	    if (userModel.getPermission().equalsIgnoreCase("OWNER")) {
		uModel.getPermissions().put(userModel.getSubsourcerepo(), AccessPermission.OWNER);
		uModel.getPermissions().put(userModel.getSubderivedrepo(), AccessPermission.OWNER);
	    } else if (userModel.getPermission().equalsIgnoreCase("READ_WRITE")) {
		uModel.getPermissions().put(userModel.getSubsourcerepo(), AccessPermission.CREATE);
		uModel.getPermissions().put(userModel.getSubderivedrepo(), AccessPermission.CREATE);
	    } else if (userModel.getPermission().equalsIgnoreCase("READ")) {
		uModel.getPermissions().put(userModel.getSubsourcerepo(), AccessPermission.CLONE);
		uModel.getPermissions().put(userModel.getSubderivedrepo(), AccessPermission.CLONE);
	    } else if (userModel.getPermission().equalsIgnoreCase("RESTRICTED")) {
		uModel.getPermissions().put(userModel.getSubsourcerepo(), AccessPermission.EXCLUDE);
		uModel.getPermissions().put(userModel.getSubderivedrepo(), AccessPermission.EXCLUDE);
	    }

	    userModelMap.put(userModel.getUsername(), uModel);
	});
	return userModelMap;
    }

    private void populateRepositoryWisePermissions(List<GitUserModel> gitUserModel, HashMap<String, HashMap<String, AccessPermission>> repoBasedPermission, HashMap<String, RepositoryView> repoAndRepoview, Map<String, Set<Repository>> lAllRepositoryMap) {

	HashMap<String, Set<String>> repoBasedTargetSystem = getAvailableTargetSys(true);
	for (GitUserModel userModel : gitUserModel) {

	    // Get only Special permissions
	    if (userModel.havingSplPermission()) {
		HashMap<String, AccessPermission> srcRepoBasedPerm = new HashMap<>();
		HashMap<String, AccessPermission> derRepoBasedPerm = new HashMap<>();

		if (repoBasedPermission.containsKey(userModel.getSubsourcerepo())) {
		    srcRepoBasedPerm = repoBasedPermission.get(userModel.getSubsourcerepo());
		    derRepoBasedPerm = repoBasedPermission.get(userModel.getSubderivedrepo());
		}

		if (userModel.getPermission().equalsIgnoreCase("OWNER")) {
		    srcRepoBasedPerm.put(userModel.getUsername(), AccessPermission.OWNER);
		    derRepoBasedPerm.put(userModel.getUsername(), AccessPermission.OWNER);
		} else if (userModel.getPermission().equalsIgnoreCase("READ_WRITE")) {
		    srcRepoBasedPerm.put(userModel.getUsername(), AccessPermission.CREATE);
		    derRepoBasedPerm.put(userModel.getUsername(), AccessPermission.CREATE);
		} else if (userModel.getPermission().equalsIgnoreCase("READ")) {
		    srcRepoBasedPerm.put(userModel.getUsername(), AccessPermission.CLONE);
		    derRepoBasedPerm.put(userModel.getUsername(), AccessPermission.CLONE);
		} else if (userModel.getPermission().equalsIgnoreCase("RESTRICTED")) {
		    srcRepoBasedPerm.put(userModel.getUsername(), AccessPermission.EXCLUDE);
		    derRepoBasedPerm.put(userModel.getUsername(), AccessPermission.EXCLUDE);
		}
		repoBasedPermission.put(userModel.getSubsourcerepo(), srcRepoBasedPerm);
		repoBasedPermission.put(userModel.getSubderivedrepo(), derRepoBasedPerm);
	    }

	    // Source Repo details update
	    String srcRepoName = userModel.getSourcerepo().replace(".git", "").toUpperCase();
	    // Derived Repo details update
	    String derRepoName = userModel.getDerivedrepo().replace(".git", "").toUpperCase();

	    if (userModel.getPermission().equalsIgnoreCase("OWNER")) {
		if (repoAndRepoview.containsKey(srcRepoName)) {
		    repoAndRepoview.get(srcRepoName).getRepository().getOwners().add(userModel.getUsername());
		} else {
		    RepositoryView repositoryView = new RepositoryView();
		    Repository srcRepository = new Repository();
		    srcRepository.setName(srcRepoName);
		    srcRepository.setDescription(userModel.getDescription());
		    srcRepository.setCurrentAccess(userModel.getDefultpermission());
		    srcRepository.getOwners().add(userModel.getUsername());
		    List<String> temp = new ArrayList<>();
		    if (repoBasedTargetSystem.containsKey(userModel.getSourcerepo())) {
			temp.addAll(repoBasedTargetSystem.get(userModel.getSourcerepo()));
		    }
		    srcRepository.setAvailableRefs(temp);
		    repositoryView.setRepository(srcRepository);
		    repoAndRepoview.put(srcRepoName, repositoryView);
		}

		// Derived Repoview
		if (repoAndRepoview.containsKey(derRepoName)) {
		    repoAndRepoview.get(derRepoName).getRepository().getOwners().add(userModel.getUsername());
		} else {
		    Repository derivedRepository = new Repository();
		    derivedRepository.setName(derRepoName);
		    derivedRepository.setCurrentAccess(userModel.getDefultpermission());
		    derivedRepository.getOwners().add(userModel.getUsername());
		    // derivedRepository.setAvailableRefs(srcRepoAndAvailableRefs.get());
		    RepositoryView repositoryView = new RepositoryView();
		    repositoryView.setRepository(derivedRepository);
		    repoAndRepoview.put(derRepoName, repositoryView);
		}
	    }

	    // All source repository details
	    if (!lAllRepositoryMap.containsKey(srcRepoName)) {
		lAllRepositoryMap.put(srcRepoName, new HashSet<>());
	    }
	    Repository subSrcRepository = new Repository();
	    subSrcRepository.setName(userModel.getSubsourcerepo());
	    if (lAllRepositoryMap.get(srcRepoName).contains(subSrcRepository)) {
		subSrcRepository = lAllRepositoryMap.get(srcRepoName).stream().filter(x -> x.getName().equalsIgnoreCase(userModel.getSubsourcerepo())).findFirst().get();
	    } else {
		subSrcRepository.setDescription(userModel.getDescription());
		subSrcRepository.setCurrentAccess(userModel.getDefultpermission());
	    }
	    if (userModel.getPermission().equalsIgnoreCase("OWNER")) {
		subSrcRepository.getOwners().add(userModel.getUsername());
	    }
	    lAllRepositoryMap.get(srcRepoName).add(subSrcRepository);

	    // All derived repository details
	    if (!lAllRepositoryMap.containsKey(derRepoName)) {
		lAllRepositoryMap.put(derRepoName, new HashSet<>());
	    }

	    Repository subDerivedRepository = new Repository();
	    subDerivedRepository.setName(userModel.getSubderivedrepo());
	    if (lAllRepositoryMap.get(derRepoName).contains(subDerivedRepository)) {
		subDerivedRepository = lAllRepositoryMap.get(derRepoName).stream().filter(x -> x.getName().equalsIgnoreCase(userModel.getSubderivedrepo())).findFirst().get();
	    } else {
		subDerivedRepository.setDescription(userModel.getDescription());
		subDerivedRepository.setCurrentAccess(userModel.getDefultpermission());
	    }
	    if (userModel.getPermission().equalsIgnoreCase("OWNER")) {
		subDerivedRepository.getOwners().add(userModel.getUsername());
	    }
	    lAllRepositoryMap.get(derRepoName).add(subDerivedRepository);
	}
    }

    private void updateCache(Map UserNameWise, Map RepoWiseUserPermission, Map lAllProdRepoView, Map lAllRepositoryMap) {
	// 1. <UserName, UserModel>
	getCacheClient().getAllRepoUsersMap().clear();
	getCacheClient().getAllRepoUsersMap().putAll(UserNameWise);

	// 2. <RepositoryName, <UserName, AccessPermission>
	getCacheClient().getRepoWiseUserMap().clear();
	getCacheClient().getRepoWiseUserMap().putAll(RepoWiseUserPermission);

	// 3. <RepositoryCommonName, List<Repository>>
	getCacheClient().getAllRepositoryMap().clear();
	Map<String, List<Repository>> allRepoMap = new HashMap<>();
	lAllRepositoryMap.forEach((key, value) -> {
	    allRepoMap.put((String) key, new ArrayList<>((Set) value));
	});
	getCacheClient().getAllRepositoryMap().putAll(allRepoMap);

	// 4. <RepositoryCommonName, RepositoryView>
	getCacheClient().getFilteredRepositoryMap().clear();
	getCacheClient().getFilteredRepositoryMap().putAll(lAllProdRepoView);

	LOG.info("<UserName, UserModel>: " + getCacheClient().getAllRepoUsersMap().size());
	LOG.info("<RepositoryName, <UserName, AccessPermission>: " + getCacheClient().getRepoWiseUserMap().size());
	LOG.info("<RepositoryCommonName, List<Repository>>: " + getCacheClient().getAllRepositoryMap().size());
	LOG.info(" <RepositoryCommonName, RepositoryView>: " + getCacheClient().getFilteredRepositoryMap().size());

    }

    private void updateImplementationPlanRepoPermissions(List<String> lPlans) {
	LOG.info("Populating Implementation Repositories Cache from Gitblit DB");
	List<Object[]> activePlans = getImpPlanDAO().getActivePlanUserDetails(lPlans);
	LOG.info("# of Plans : " + activePlans.size());

	for (Object[] planDetail : activePlans) {
	    String planId = planDetail[0].toString();
	    String companySrcPath = "";
	    String companydrvPath = "";

	    if (planId.startsWith("T")) {
		companySrcPath = "tpf/tp/source/" + planId.toLowerCase() + ".git";
		companydrvPath = "tpf/tp/derived/" + planId.toLowerCase() + ".git";
	    } else if (planId.startsWith("D")) {
		companySrcPath = "tpf/dl/source/" + planId.toLowerCase() + ".git";
		companydrvPath = "tpf/dl/derived/" + planId.toLowerCase() + ".git";
	    }
	    // Lead data population
	    String leadName = planDetail[1].toString();
	    UserModel lLead = getCacheClient().getAllRepoUsersMap().get(leadName);
	    if (lLead != null) {
		lLead.getRepositories().add(companySrcPath);
		lLead.getRepositories().add(companydrvPath);
		lLead.getPermissions().put(companySrcPath, AccessPermission.CREATE);
		lLead.getPermissions().put(companydrvPath, AccessPermission.CREATE);
		getCacheClient().getAllRepoUsersMap().put(leadName, lLead);
	    } else {
		LOG.info("Invalid Lead " + leadName + " for plan " + planId);
	    }

	    if (planDetail[4] != null && Constants.ImplementationStatus.IN_PROGRESS.name().equalsIgnoreCase(planDetail[4].toString())) {
		// Dev & Peer data population
		List<String> userList = new ArrayList<>();

		String peerNames = planDetail[2] != null ? planDetail[2].toString() : "";
		if (peerNames != null && !peerNames.isEmpty()) {
		    userList.addAll(Arrays.asList(peerNames.split(",")));
		}

		if (planDetail[3].toString() != null && !planDetail[3].toString().isEmpty()) {
		    userList.add(planDetail[3].toString().trim());
		}
		for (String userName : userList) {
		    if (userName != null && !userName.trim().isEmpty()) {
			UserModel lRev = getCacheClient().getAllRepoUsersMap().get(userName);
			if (lRev != null) {
			    lRev.getRepositories().add(companySrcPath);
			    lRev.getRepositories().add(companydrvPath);
			    lRev.getPermissions().put(companySrcPath, AccessPermission.CREATE);
			    lRev.getPermissions().put(companydrvPath, AccessPermission.CREATE);
			    getCacheClient().getAllRepoUsersMap().put(userName, lRev);
			} else {
			    LOG.info("Invalid Reviewer " + userName + " for plan " + planId);
			}
		    }
		}
	    }

	}
    }

    public Boolean updateImplementationPlanRepoPermissions(String planId, List<String> userToBeDeleted) {
	if (planId != null && !planId.isEmpty()) {
	    String companySrcPath = "";
	    String companydrvPath = "";

	    if (planId.startsWith("T")) {
		companySrcPath = "tpf/tp/source/" + planId.toLowerCase() + ".git";
		companydrvPath = "tpf/tp/derived/" + planId.toLowerCase() + ".git";
	    } else if (planId.startsWith("D")) {
		companySrcPath = "tpf/dl/source/" + planId.toLowerCase() + ".git";
		companydrvPath = "tpf/dl/derived/" + planId.toLowerCase() + ".git";
	    }
	    LOG.info("User To be deleted >> " + userToBeDeleted);
	    if (userToBeDeleted != null && !userToBeDeleted.isEmpty()) {
		for (String user : userToBeDeleted) {
		    UserModel lUserModel = getCacheClient().getAllRepoUsersMap().get(user);
		    if (lUserModel != null) {
			lUserModel.getRepositories().remove(companySrcPath);
			lUserModel.getRepositories().remove(companydrvPath);
			lUserModel.getPermissions().remove(companySrcPath);
			lUserModel.getPermissions().remove(companydrvPath);
			getCacheClient().getAllRepoUsersMap().put(user, lUserModel);
		    }
		}
	    }

	    if (planId != null && !planId.trim().isEmpty()) {
		List<String> planIds = new ArrayList<>();
		planIds.add(planId.toUpperCase());
		updateImplementationPlanRepoPermissions(planIds);
	    }
	}

	return true;
    }

    public boolean updateRepoPermission(Set<String> pRepoNames, List<RepositoryPermission> lGITUpdateUser) {
	HashMap<String, Integer> lSrcOrDerRepoNameAndId = new HashMap<>();
	HashMap<String, String> lUserAndRepoBasedPermission = new HashMap<>();
	List<UserDetails> permToBeUpdated = new ArrayList<>();
	List<UserDetails> userDetailToAdd = new ArrayList<>();
	List<UserDetails> gitUserDetails = gitSearchDAO().getGitUseDetails();

	// <(user name+repo id), permission>
	gitUserDetails.forEach(userDetail -> {
	    lUserAndRepoBasedPermission.put(userDetail.getUsername() + "-" + userDetail.getRepoid(), userDetail.getPermission());
	});

	// Repo Details
	List<Object[]> repoDetails = gitSearchDAO().getRepoIds(null);

	// Group src and derived repo details together and resp id
	for (Object[] repo : repoDetails) {
	    Integer repoId = Integer.valueOf(repo[0].toString());
	    String srcRepo = repo[1].toString();
	    String drvRepo = repo[2].toString();

	    lSrcOrDerRepoNameAndId.put(srcRepo, repoId);
	    lSrcOrDerRepoNameAndId.put(drvRepo, repoId);
	}

	pRepoNames.forEach(repoName -> {
	    lGITUpdateUser.forEach(repoPerm -> {
		// Get repo id based on given repo name
		Integer repoId = lSrcOrDerRepoNameAndId.get(repoName);
		UserDetails userDetails = new UserDetails();
		userDetails.setUsername(repoPerm.getRegistrant());
		userDetails.setRepoid(repoId);
		userDetails.setPermission(Constants.RepoPermission.getKey(repoPerm.getPermission()));
		if (lSrcOrDerRepoNameAndId.containsKey(repoName) && lUserAndRepoBasedPermission.containsKey(repoPerm.getRegistrant() + "-" + repoId) && lUserAndRepoBasedPermission.get(repoPerm.getRegistrant() + "-" + repoId) != null && !lUserAndRepoBasedPermission.get(repoPerm.getRegistrant() + "-" + repoId).isEmpty()) {
		    int pRepoOridinal = getRepoPermissionEnum(Constants.RepoPermission.getKey(repoPerm.getPermission())).ordinal();
		    int lRepoOridinal = getRepoPermissionEnum(lUserAndRepoBasedPermission.get(repoPerm.getRegistrant() + "-" + repoId)).ordinal();
		    if (pRepoOridinal > lRepoOridinal) {
			permToBeUpdated.add(userDetails);
		    }
		} else if (lSrcOrDerRepoNameAndId.containsKey(repoName)) {
		    userDetailToAdd.add(userDetails);
		}
	    });
	});
	if (permToBeUpdated.size() > 0) {
	    gitSearchDAO().updateUserDetails(permToBeUpdated);
	}
	if (userDetailToAdd.size() > 0) {
	    gitSearchDAO().insertUserDetails(userDetailToAdd);
	}

	populateGitCacheNew();
	return true;

    }

    private Constants.RepoPermission getRepoPermissionEnum(String permission) {

	Constants.RepoPermission repoPermissionEnum;
	if ("RESTRICTED".equalsIgnoreCase(permission)) {
	    repoPermissionEnum = Constants.RepoPermission.RESTRICTED;
	} else if ("READ".equalsIgnoreCase(permission)) {
	    repoPermissionEnum = Constants.RepoPermission.READ;
	} else if ("READ_WRITE".equalsIgnoreCase(permission)) {
	    repoPermissionEnum = Constants.RepoPermission.READ_WRITE;
	} else if ("OWNER".equalsIgnoreCase(permission)) {
	    repoPermissionEnum = Constants.RepoPermission.OWNER;
	} else {
	    LOG.error("Repo permission has new value. Please check with tool admin.. " + permission);
	    throw new WorkflowException("Repo permission has new value. Please check with tool admin..");
	}
	return repoPermissionEnum;

    }

    public void searchFileResultFilter(String pUserId, String pCompanyName, List<GitSearchResult> pSearchResult) {
	searchFileResultFilter(pUserId, pSearchResult, pCompanyName, null);
    }

    public Boolean searchFileResultFilter(String pUserId, Collection<GitSearchResult> pSearchResult, String pCompanyName, String searchType) {
	try {
	    Set<String> lRWAccessList = getUserAllowedReadAndWriteRepos(pUserId, pCompanyName);
	    List<GitSearchResult> lRemoveSearchResult = new ArrayList();
	    for (GitSearchResult searchResult : pSearchResult) {
		String lRepoName = getRepositoryNameBySourceURL(searchResult.getBranch().get(0).getSourceUrl());
		String lRepoAccess = lRWAccessList.contains(lRepoName) ? "RW" : "R";
		searchResult.setRepoAccess(lRepoAccess);

		List<GitBranchSearchResult> lBranches = searchResult.getBranch();
		if (searchType != null && (searchType.equals(Constants.FILE_MIG_NONIBM) || searchType.equals(Constants.FILE_MIG_OBS))) {
		    List<GitBranchSearchResult> lRemovalBranch = new ArrayList();
		    for (GitBranchSearchResult lBranch : lBranches) {
			if (!(lBranch.getRefStatus().equalsIgnoreCase("online") || lBranch.getRefStatus().equalsIgnoreCase("newfile"))) {
			    lRemovalBranch.add(lBranch);
			    continue;
			}
			List<String> remarks = new ArrayList();
			if (lRepoAccess.equals("R")) {
			    lBranch.setIsCheckoutAllowed(false);
			    lBranch.setIsBranchSelected(Boolean.FALSE);
			    remarks.add("Functional Package " + lBranch.getFuncArea().replace(".git", "") + " restricted. Contact package owner to action");
			}

			List<String> lSegments = getCheckoutSegmentsDAO().findByFileName(new ArrayList<>(Constants.PlanStatus.getOnlineAndAbove().keySet()), searchResult.getFileName(), lBranch.getTargetSystem().replace("master_", "").toUpperCase());

			if (lSegments != null && !lSegments.isEmpty()) {
			    lBranch.setIsCheckoutAllowed(false);
			    lBranch.setIsBranchSelected(Boolean.FALSE);
			    remarks.add("segment checked out in plan(s) - " + String.join(",", lSegments));
			}
			lBranch.setRemarks(remarks);
		    }
		    lBranches.removeAll(lRemovalBranch);
		    searchResult.setBranch(lBranches);
		    if (lBranches.isEmpty()) {
			lRemoveSearchResult.add(searchResult);
		    }
		}
		for (GitBranchSearchResult lBranch : lBranches) {
		    if (lRepoAccess.equals("R")) {
			lBranch.setIsCheckoutAllowed(false);
		    }
		}
	    }
	    pSearchResult.removeAll(lRemoveSearchResult);
	} catch (Exception ex) {
	    LOG.info("Error occurs in filtering the result by user " + pUserId, ex);
	    throw new WorkflowException("Unable to filter the search results ", ex);
	}
	return true;
    }
    // TODO: REMOVE
    // public List<GitSearchResult> SearchAllRepos(String pUserId, String pCompany,
    // String pFileFilter, Boolean
    // pMacroHeader) {
    // List<GitSearchResult> lReturnList = new ArrayList();
    // try {
    // lReturnList = (List) getJGitClientUtils().SearchAllRepos(pCompany,
    // pFileFilter, pMacroHeader);
    // } catch (Exception ex) {
    // LOG.error("Unable to get results for search - " + pFileFilter);
    // throw new WorkflowException("Unable to get results for search ", ex);
    // }
    // return lReturnList;
    // }

    // @Transactional
    public boolean isProdPath(String pPath, Boolean derivedExcep) {
	// LIST of paths to SKIP
	if (lExcceptionalPaths.isEmpty()) {
	    List<Platform> platforms = getPlatformDAO().findAll();
	    if (getWFConfig().getProfileName().startsWith("TP")) {
		// SKIP DL Repos for all TP Build, SPL for PREPROD
		lExcceptionalPaths.add(getGITConfig().getGitProdPath() + "dl");
	    } else if (getWFConfig().getProfileName().startsWith("DL")) {
		// SKIP TP Repos for all DL Build, SPL for PREPROD
		lExcceptionalPaths.add(getGITConfig().getGitProdPath() + "tp");
	    }
	    for (Platform platform : platforms) {
		if (derivedExcep) {
		    lExcceptionalPaths.add(getGITConfig().getGitProdPath() + platform.getNickName() + "/ibm/derived_");
		    lExcceptionalPaths.add(getGITConfig().getGitProdPath() + platform.getNickName() + "/nonibm/derived_");
		}
		lExcceptionalPaths.add(getGITConfig().getGitProdPath() + platform.getNickName() + "/ibm/ibm_conf");
		lExcceptionalPaths.add(getGITConfig().getGitProdPath() + platform.getNickName() + "/nonibm/nonibm_conf");
		lExcceptionalPaths.add(getGITConfig().getGitProdPath() + platform.getNickName() + "/" + getGITConfig().getGitSourcePath());
		lExcceptionalPaths.add(getGITConfig().getGitProdPath() + platform.getNickName() + "/" + getGITConfig().getGitDerivedPath());
	    }
	    LOG.debug("Exception Paths : " + String.join(",", lExcceptionalPaths));
	}
	Boolean isException = lExcceptionalPaths.stream().anyMatch((path) -> pPath.startsWith(path)) || !pPath.startsWith(getGITConfig().getGitProdPath());
	LOG.debug(pPath + " is Prod " + !isException);
	return !isException;
    }

    // public JSONResponse updateRepoPermission(RepositoryView pReopsitory) {
    // JSONResponse lResponse = new JSONResponse();
    // Repository lRepo = pReopsitory.getRepository();
    // String defaultAccess = "";
    // try {
    // if (!lRepo.getOwners().contains(getGITConfig().getServiceUserID())) {
    // lRepo.getOwners().add(getGITConfig().getServiceUserID());
    // }
    // defaultAccess = pReopsitory.getDefaultAccess();
    // if
    // (defaultAccess.equalsIgnoreCase(Constants.RepoPermission.RESTRICTED.name()))
    // {
    // lRepo.setAccessRestriction(Constants.VIEW);
    // lRepo.setAuthorizationControl(Constants.NAMED);
    // } else if
    // (defaultAccess.equalsIgnoreCase(Constants.RepoPermission.READ.name())) {
    // lRepo.setAccessRestriction(Constants.PUSH);
    // lRepo.setAuthorizationControl(Constants.NAMED);
    // } else if
    // (defaultAccess.equalsIgnoreCase(Constants.RepoPermission.READ_WRITE.name()))
    // {
    // lRepo.setAccessRestriction(Constants.PUSH);
    // lRepo.setAuthorizationControl(Constants.AUTHENTICATED);
    // }
    // if (!updateGitRepository(lRepo)) {
    // throw new WorkflowException("Unable to update the repository - " +
    // lRepo.getName());
    // }
    // lResponse.setStatus(Boolean.TRUE);
    // } catch (WorkflowException ex) {
    // throw ex;
    // } catch (Exception ex) {
    // LOG.error("Unable to Update permission for this Repository", ex);
    // throw new WorkflowException("Unable to Update permission for this
    // Repository", ex);
    // }
    // return lResponse;
    // }
    public Map<String, String> getRepositoryList(String companyName, String repoType) {
	Map<String, String> lReturnList = new HashMap();
	Map<String, List<Repository>> lAllRepositoryMap = getCacheClient().getAllRepositoryMap();
	String lFilterName = (getGITConfig().getGitProdPath() + companyName + "/" + repoType + "/" + repoType + "_").toUpperCase();

	for (Map.Entry<String, List<Repository>> lRepoMap : lAllRepositoryMap.entrySet()) {
	    if (lRepoMap.getKey().startsWith(lFilterName)) {
		if (lRepoMap.getValue().isEmpty()) {
		    continue;
		}
		String lRepoName = lRepoMap.getValue().get(lRepoMap.getValue().size() - 1).getName();
		String funcName = lRepoMap.getKey().replace(lFilterName, "");
		lReturnList.put(funcName, lRepoName);
	    }
	}
	return lReturnList;
    }

    public Set<String> getUserAllowedReadAndAboveRepos(String pUserId, String pCompanyName) {
	return getUserAllowedRepoList(pUserId, "R", pCompanyName);
    }

    public Set<String> getUserAllowedReadAndWriteRepos(String pUserId, String pCompanyName) {
	return getUserAllowedRepoList(pUserId, "RW", pCompanyName);
    }

    private Set<String> getUserAllowedRepoList(String pUserId, String pAccess, String pCompanyName) {
	UserModel lUserModel = getCacheClient().getAllRepoUsersMap().get(pUserId);
	if (lUserModel == null) {
	    return null;
	}
	Set<String> lReturnList = new HashSet();
	for (Map.Entry<String, AccessPermission> lRepoVsAccess : lUserModel.getPermissions().entrySet()) {
	    if (!lRepoVsAccess.getValue().toString().startsWith(pAccess) || lRepoVsAccess.getKey().contains("derived_") || !lRepoVsAccess.getKey().contains("/" + pCompanyName + "/")) {
		continue;
	    }
	    lReturnList.add(lRepoVsAccess.getKey());
	}
	return lReturnList;
    }

    public Set<String> getObsRepoList(String companyName) {
	Set<String> lReturnList = new TreeSet();
	String lRepoName = (getGITConfig().getGitProdPath() + companyName + "/nonibm/nonibm_obs").toUpperCase();
	List<Repository> lObsRepos = getCacheClient().getAllRepositoryMap().get(lRepoName);
	if (lObsRepos != null) {
	    lObsRepos.forEach((lObsRepo) -> {
		lReturnList.add(lObsRepo.getName());
	    });
	}
	return lReturnList;
    }

    public void getSorceArtifactLink(List<SourceArtifactSearchResult> sourceArtifactList) {
	sourceArtifactList.forEach(srcArtifact -> {
	    if (srcArtifact.getSourcerepo() != null && !srcArtifact.getSourcerepo().isEmpty() && srcArtifact.getCommitid() != null && !srcArtifact.getCommitid().isEmpty()) {

		String sourceArtifactLink = gITConfig.getGitblitTicketUrl() + FilenameUtils.separatorsToUnix(srcArtifact.getSourcerepo().replace("/", "!") + File.separator + srcArtifact.getCommitid() + File.separator + srcArtifact.getFilename().replace("/", "!"));
		srcArtifact.setSourceartifactlink(sourceArtifactLink.replace(Constants.TICKETS, Constants.BLOB));
	    } else {
		String companyPath = "";
		if (srcArtifact.getPlanid().toUpperCase().contains("T")) {
		    companyPath = "tpf/tp/source/";
		} else if (srcArtifact.getPlanid().toUpperCase().contains("D")) {
		    companyPath = "tpf/dl/source/";
		}
		String sourceArtifactLink = gITConfig.getGitblitTicketUrl() + FilenameUtils.separatorsToUnix((companyPath + srcArtifact.getPlanid().toLowerCase() + ".git").replace("/", "!") + File.separator + Constants.BRANCH_MASTER + srcArtifact.getTargetsystem().toLowerCase() + File.separator + srcArtifact.getFilename().replace("/", "!"));
		srcArtifact.setSourceartifactlink(sourceArtifactLink.replace(Constants.TICKETS, Constants.BLOB));
	    }
	});
    }

    public void getListingUrlLink(List<SourceArtifactSearchResult> sourceArtifactList) {

	Pattern regx = Pattern.compile("[T|D]\\d{7}");

	List<String> lstBasedFiles = new ArrayList<>();
	lstBasedFiles.add("asm");
	lstBasedFiles.add("sbt");
	lstBasedFiles.add("c");
	lstBasedFiles.add("cpp");

	List<String> mapBasedFiles = new ArrayList<>();
	mapBasedFiles.add("mak");

	sourceArtifactList.forEach(srcArtifact -> {
	    String fileExt = FilenameUtils.getExtension(srcArtifact.getFilename()).toLowerCase();
	    String plainFileName = FilenameUtils.removeExtension(FilenameUtils.getName(srcArtifact.getFilename()));
	    String fileName1 = FilenameUtils.removeExtension(srcArtifact.getFilename());

	    List<SystemLoad> lLoadList = getSystemLoadDAO().findByImpPlan(srcArtifact.getPlanid());
	    List<SystemLoad> result = lLoadList.stream().filter(x -> x.getDerivedSegmentsMovedDt() != null).collect(Collectors.toList());

	    if (regx.matcher(srcArtifact.getPlanid()).matches() && !result.isEmpty() && Constants.PlanStatus.getSecuredStatusMap().containsKey(srcArtifact.getPlanstatus()) && (lstBasedFiles.contains(fileExt) || mapBasedFiles.contains(fileExt))) {
		String companyPath = "";
		if (srcArtifact.getPlanid().toUpperCase().contains("T")) {
		    companyPath = "tpf/tp/derived/";
		} else if (srcArtifact.getPlanid().toUpperCase().contains("D")) {
		    companyPath = "tpf/dl/derived/";
		}
		String fileName = "";
		if (lstBasedFiles.contains(fileExt)) {
		    srcArtifact.setListingFile(plainFileName + ".lst");
		    fileName = fileName1 + ".lst";
		} else if (mapBasedFiles.contains(fileExt)) {
		    srcArtifact.setListingFile(plainFileName + ".map");
		    fileName = fileName1.replaceAll(plainFileName, plainFileName.toUpperCase()) + ".map";
		}
		String updatedFileName = "";

		if (srcArtifact.getFiletype().equalsIgnoreCase("NON_IBM")) {
		    updatedFileName = FilenameUtils.getName(fileName);
		    String listingFileLink = gITConfig.getGitblitTicketUrl() + (FilenameUtils.separatorsToUnix((companyPath + srcArtifact.getPlanid().toLowerCase() + ".git").replace("/", "!") + File.separator + Constants.BRANCH_MASTER + srcArtifact.getTargetsystem().toLowerCase() + "/lst!" + updatedFileName));
		    srcArtifact.setListingfilelink(listingFileLink.replace(Constants.TICKETS, Constants.BLOB));
		} else {
		    if (!fileName.isEmpty()) {
			String listingFileLink = gITConfig.getGitblitTicketUrl() + (FilenameUtils.separatorsToUnix((companyPath + srcArtifact.getPlanid().toLowerCase() + ".git").replace("/", "!") + File.separator + Constants.BRANCH_MASTER + srcArtifact.getTargetsystem().toLowerCase() + File.separator + fileName.replace("/rt", "/lst").replace("/", "!")));
			srcArtifact.setListingfilelink(listingFileLink.replace(Constants.TICKETS, Constants.BLOB));
		    }
		}
	    }

	});
    }

    public Collection<String> validatePutLevelToAllowCheckout(String pImplId, Collection<GitSearchResult> pSearchResult) {
	HashMap<String, String> ImpSysAndPutIdMap = new HashMap<>();
	Implementation lImplementation = implementationDAO.find(pImplId);
	Set<String> differentTarSys = new HashSet<>();
	// To get Prod Put level id for each system from implementation
	lImplementation.getPlanId().getSystemLoadList().stream().filter(sys -> sys.getActive().equals("Y")).forEach(sys -> {
	    ImpSysAndPutIdMap.put(sys.getSystemId().getName(), sys.getPutLevelId().getPutLevel());
	});

	for (GitSearchResult searchResult : pSearchResult) {

	    // If Selected Put Level is differ
	    searchResult.getBranch().stream().filter(p -> p.getFileType() != null && p.getFileType().equalsIgnoreCase(Constants.FILE_TYPE.IBM.name())).forEach(branch -> {
		String targetSys = branch.getTargetSystem().replace("master_", "").toUpperCase();
		if (ImpSysAndPutIdMap.containsKey(targetSys) && !ImpSysAndPutIdMap.get(targetSys).equalsIgnoreCase(branch.getFuncArea())) {
		    branch.setIsCheckoutAllowed(Boolean.FALSE);
		    branch.setIsBranchSelected(Boolean.FALSE);
		    differentTarSys.add(targetSys);
		}
	    });
	}
	return differentTarSys;
    }

    public Collection<String> validateIBMVanillaToAllowCheckout(String pImplId, Collection<GitSearchResult> pSearchResult) {
	HashMap<String, String> ImpSysAndPutIdMap = new HashMap<>();
	Implementation lImplementation = implementationDAO.find(pImplId);
	Set<String> differentTarSys = new HashSet<>();

	// To get Prod Put level id for each system from implementation
	lImplementation.getPlanId().getSystemLoadList().stream().filter(sys -> sys.getActive().equals("Y")).forEach(sys -> {
	    ImpSysAndPutIdMap.put(sys.getSystemId().getName(), sys.getPutLevelId().getPutLevel());
	});

	// Get Put levels for each systems
	List<String> putLevelStatus = new ArrayList<>();
	putLevelStatus.add(Constants.PUTLevelOptions.PRODUCTION.name());
	HashMap<String, String> tarSysAndPutLevel = getSystemBasedPutLevels(putLevelStatus);

	pSearchResult.forEach(searchRes -> {
	    SortedSet<String> allowedSystem = new TreeSet<>();
	    searchRes.getTargetSystems().forEach(sys -> {
		if (ImpSysAndPutIdMap.get(sys) != null && tarSysAndPutLevel.get(sys).equalsIgnoreCase(ImpSysAndPutIdMap.get(sys))) {
		    allowedSystem.add(sys);
		} else if (ImpSysAndPutIdMap.get(sys) == null) {
		    allowedSystem.add(sys);
		} else {
		    differentTarSys.add(sys);
		}
	    });
	    searchRes.setAllowedTargetSystems(allowedSystem);
	});
	return differentTarSys;
    }

    public HashMap<String, String> getSystemBasedPutLevels(List<String> putLevelStatus) {
	HashMap<String, String> tarSysAndPutLevel = new HashMap<>();

	List<PutLevel> putLevels = putLevelDAO.findByStatus(putLevelStatus);
	putLevels.stream().forEach(put -> {
	    tarSysAndPutLevel.put(put.getSystemId().getName(), put.getPutLevel());
	});
	return tarSysAndPutLevel;
    }

    public void updateHighlightGroupFlag(List<SourceArtifactSearchResult> sourceArtifactList) {

	HashMap<String, List<SourceArtifactSearchResult>> impAndPlanBasedGroup = new HashMap<>();

	boolean ishighlightGroup = Boolean.TRUE;
	sourceArtifactList.sort(Comparator.comparing(SourceArtifactSearchResult::getLoaddatetime, Comparator.nullsLast(Comparator.reverseOrder())).thenComparing(SourceArtifactSearchResult::getTargetsystem).thenComparing(SourceArtifactSearchResult::getPlanid, Comparator.nullsLast(Comparator.naturalOrder())));
	for (SourceArtifactSearchResult srcArtifact : sourceArtifactList) {
	    String key = srcArtifact.getPlanid() + srcArtifact.getTargetsystem() + srcArtifact.getLoaddatetime();
	    if (impAndPlanBasedGroup.containsKey(key)) {
		srcArtifact.setHighlightGroupFlag(!ishighlightGroup);
		impAndPlanBasedGroup.get(key).add(srcArtifact);
	    } else {
		srcArtifact.setHighlightGroupFlag(ishighlightGroup);
		List<SourceArtifactSearchResult> tempList = new ArrayList<>();
		tempList.add(srcArtifact);
		impAndPlanBasedGroup.put(key, tempList);
		ishighlightGroup = !ishighlightGroup;
	    }
	}
    }

    public void updateOnlineVersions(List<SourceArtifactSearchResult> sourceArtifactList) {
	sourceArtifactList.stream().filter(src -> src.getPlanstatus().equalsIgnoreCase(Constants.PlanStatus.ONLINE.name()) && src.getStatusrank() > 1).forEach(src -> src.setPlanstatus(src.getPlanstatus() + "-" + (src.getStatusrank() - 1)));
    }

    // new User
    public List<String> getNewUsers(List<GitUsers> gitUsers, Set<String> userList) {
	List<String> gitUserNames = gitUsers.stream().map(x -> x.getUsername()).collect(Collectors.toList());

	Collection newUsers = CollectionUtils.subtract(gitUserNames, userList);
	return newUsers != null ? new ArrayList<String>(newUsers) : null;
    }

    // Existing active user
    public List<String> getActiveUsers(List<GitUsers> gitUsers, Set<String> userList) {
	return gitUsers.stream().map(x -> x.getUsername()).filter(aObject -> {
	    return userList.contains(aObject);
	}).collect(Collectors.toList());
    }

    public void updateRepoBasedTarSystems() throws IOException {
	Map<String, List<String>> repoBasedBranches = getGitBlitClientUtils().getGitBranchList();
	List<Object[]> repoBasedTarSys = gitSearchDAO().getTarSysFromSubRepoDetails();
	HashMap<String, Set<String>> repoBasedTargetSystem = getAvailableTargetSys(false);

	for (Object[] repo : repoBasedTarSys) {
	    if (!repoBasedTargetSystem.containsKey(repo[1].toString())) {
		repoBasedTargetSystem.put(repo[1].toString(), new HashSet<String>());
	    }
	    if (repo[0] != null && !repo[0].toString().isEmpty()) {
		repoBasedTargetSystem.get(repo[1].toString()).addAll(Arrays.asList(repo[0].toString().split(",")));
	    }

	}

	repoBasedBranches.forEach((key, values) -> {
	    // Repository repository = new Repository();
	    // repository.setName(key);
	    // String repoName = repository.getTrimmedName().toLowerCase()+".git";
	    if (repoBasedTargetSystem.containsKey(key)) {
		repoBasedTargetSystem.get(key).addAll(values.stream().filter(x -> x.contains("refs/heads/master_")).collect(Collectors.toSet()));
	    }

	});
	gitSearchDAO().updateTargetSysBasedOnSubRepo(repoBasedTargetSystem);
    }

    private HashMap<String, Set<String>> getAvailableTargetSys(Boolean repoAsKey) {

	List<Object[]> repoBasedTarSys = gitSearchDAO().getTarSysFromSubRepoDetails();
	HashMap<String, Set<String>> repoBasedTargetSystem = new HashMap<>();

	for (Object[] repo : repoBasedTarSys) {

	    String key;
	    if (repoAsKey) {
		Repository repository = new Repository();
		repository.setName(repo[1].toString());
		key = repository.getTrimmedName().toLowerCase() + ".git";
	    } else {
		key = repo[1].toString();
	    }
	    if (!repoBasedTargetSystem.containsKey(key)) {
		repoBasedTargetSystem.put(key, new HashSet<String>());
	    }
	    if (repo[0] != null && !repo[0].toString().isEmpty()) {
		repoBasedTargetSystem.get(key).addAll(Arrays.asList(repo[0].toString().split(",")).stream().collect(Collectors.toSet()));
	    }
	}

	return repoBasedTargetSystem;
    }

    /*
     * JIRA Number : 2340 Created By: Radhakrishnan Created Dt: 13-Aug-2019
     * Description: get all repository list based upon the Company Name
     */
    public Set<String> getAllReposList(String companyName) throws Exception {
	Map<String, List<Repository>> lAllRepositoryMap = getCacheClient().getAllRepositoryMap();
	if (lAllRepositoryMap == null) {
	    return null;
	}
	Set<String> lReturnList = new HashSet();
	lAllRepositoryMap.entrySet().stream().forEach((t) -> {
	    t.getValue().stream().filter(lRepo -> lRepo.getName().contains("/" + companyName.toLowerCase() + "/")).forEach(lRepoName -> lReturnList.add(lRepoName.getName()));
	});
	return lReturnList;
    }

    public String getTrimmedName(String repoName) {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(repoName)).toUpperCase();
	String lReturn1 = FilenameUtils.removeExtension(repoName).toUpperCase().replaceAll("\\d+$", "");
	String lReturn2 = FilenameUtils.removeExtension(repoName).toUpperCase();
	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    return lReturn1;
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    return lReturn2;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    return lReturn1;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    return lReturn2;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    return lReturn1;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    return lReturn2;
	} else {
	    return lReturn1;
	}
    }
}
