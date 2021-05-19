/**
 *
 */
package com.tsi.workflow.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.BaseService;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitUserDetails;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.mail.RepoCheckoutMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vinoth
 *
 */
@Service
public class ToolAdminService extends BaseService {

    private static final Logger LOG = Logger.getLogger(ToolAdminService.class.getName());
    @Autowired
    GITHelper gitHelper;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    UserSettingsDAO userSettingsDAO;
    @Autowired
    GitBlitClientUtils gitBlitClientUtils;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    PlatformDAO platformDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    GitUserDetails gitUserDetails;
    @Autowired
    GitSearchDAO gitSearchDAO;

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    public MailMessageFactory getMailMessageFactory() {
	return mailMessageFactory;
    }

    public UserSettingsDAO getUserSettingsDAO() {
	return userSettingsDAO;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return gitBlitClientUtils;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public PlatformDAO getPlatformDAO() {
	return platformDAO;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GitUserDetails gitUserDetails() {
	return gitUserDetails;
    }

    public GitSearchDAO gitSearchDAO() {
	return gitSearchDAO;
    }

    @Transactional
    public JSONResponse getRepoList(Integer limit, Integer offset) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    Collection<RepositoryView> values = getCacheClient().getFilteredRepositoryMap().values();
	    Set<RepositoryView> lReturn = new TreeSet<>(values.stream().filter((t) -> !t.getRepository().getName().contains("DERIVED_")).sorted().collect(Collectors.toSet()));
	    int start = limit * offset;
	    lResponse.setData(lReturn.stream().skip(start).limit(limit).collect(Collectors.toList()));
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(lReturn.size());
	} catch (Exception ex) {
	    LOG.error("error occurs in getProductionRepoList ", ex);
	    throw new WorkflowException("Unable to get Production Repository list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getAllRepoList() {
	JSONResponse lResponse = new JSONResponse();
	try {
	    lResponse.setData(getCacheClient().getAllRepositoryMap());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(getCacheClient().getAllRepositoryMap().size());
	} catch (Exception ex) {
	    LOG.error("error occurs in getProductionRepoList ", ex);
	    throw new WorkflowException("Unable to get Production Repository list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse setRepositoryOwners(User user, RepositoryView pSourceRepo) {
	JSONResponse lResponse = new JSONResponse();
	try {
	    String lSourceRepoName = pSourceRepo.getRepository().getTrimmedName();
	    String lDerivedRepoName = lSourceRepoName.replace("NONIBM_", "DERIVED_").replace("IBM_", "DERIVED_");
	    List<Repository> lSourceRepoList = new ArrayList();
	    lSourceRepoList.addAll(getCacheClient().getAllRepositoryMap().get(lSourceRepoName));
	    lSourceRepoList.addAll(getCacheClient().getAllRepositoryMap().get(lDerivedRepoName));
	    SortedSet<String> lUpdateOwnerList = pSourceRepo.getRepository().getOwners();
	    String lUpdatedRepoDesc = pSourceRepo.getRepository().getDescription();
	    RepositoryView lSourceRepoView = getCacheClient().getFilteredRepositoryMap().get(lSourceRepoName);
	    Repository lSourceRepo = new Repository();

	    if (lSourceRepoView == null) {
		throw new WorkflowException("Repository " + lSourceRepoName + " does not exist");
	    }
	    lSourceRepo = lSourceRepoView.getRepository();

	    SortedSet<String> existingOwnerList = lSourceRepoView.getRepository().getOwners();
	    Collection<String> removedOwnerList = CollectionUtils.subtract(existingOwnerList, lUpdateOwnerList);
	    Collection<String> newUpdateOwnerList = CollectionUtils.subtract(lUpdateOwnerList, existingOwnerList);

	    if (lSourceRepoList.isEmpty()) {
		throw new WorkflowException("Unable to update the Repository Owners");
	    }

	    for (Repository lRepos : lSourceRepoList) {
		lRepos.setOwners(lUpdateOwnerList);
		lRepos.setDescription(lUpdatedRepoDesc);
		if (!getGitBlitClientUtils().updateGitRepository(lRepos)) {
		    throw new WorkflowException("Unable to update the Repository Owners in updateGitRepository");
		}
	    }

	    List<String> updatedOwnerDisplayName = new ArrayList<>();
	    for (String updatedOwner : newUpdateOwnerList) {
		updatedOwnerDisplayName.add(getLDAPAuthenticatorImpl().getUserDetails(updatedOwner).getDisplayName());
		UserSettings userSettings = getUserSettingsDAO().find(updatedOwner, Constants.UserSettings.REPO_OWNER_ALERT.name());
		if (userSettings == null) {
		    userSettings = new UserSettings();
		    userSettings.setUserId(updatedOwner);
		    userSettings.setName(Constants.UserSettings.REPO_OWNER_ALERT.name());
		    userSettings.setValue("N");
		    getUserSettingsDAO().save(user, userSettings);
		}
	    }
	    if (!newUpdateOwnerList.isEmpty()) {
		RepoCheckoutMail reoCheckoutMail = (RepoCheckoutMail) getMailMessageFactory().getTemplate(RepoCheckoutMail.class);
		newUpdateOwnerList.stream().forEach(t -> reoCheckoutMail.addToAddressUserId(t, Constants.MailSenderRole.REPO_OWNERS));
		lSourceRepo.getOwners().stream().forEach(c -> reoCheckoutMail.addcCAddressUserId(c, Constants.MailSenderRole.REPO_OWNERS));
		reoCheckoutMail.setRepoName(pSourceRepo.getRepository().getName());
		reoCheckoutMail.setNewOwnerList(newUpdateOwnerList);
		reoCheckoutMail.setNewOwnerDisplayName(updatedOwnerDisplayName);
		reoCheckoutMail.setIsNewOwnerUpdate(true);
		getMailMessageFactory().push(reoCheckoutMail);
	    }

	    if (!lUpdatedRepoDesc.equalsIgnoreCase(lSourceRepo.getDescription())) {
		RepoCheckoutMail reoCheckoutMail = (RepoCheckoutMail) getMailMessageFactory().getTemplate(RepoCheckoutMail.class);
		lSourceRepo.getOwners().stream().forEach(c -> reoCheckoutMail.addToAddressUserId(c, Constants.MailSenderRole.REPO_OWNERS));
		reoCheckoutMail.setRepoName(pSourceRepo.getRepository().getName());
		reoCheckoutMail.setDescription(lUpdatedRepoDesc);
		reoCheckoutMail.setDescUpdate(true);
		getMailMessageFactory().push(reoCheckoutMail);
	    }

	    List<String> removedOwnerDisplayName = new ArrayList<>();
	    for (String removedOwner : removedOwnerList) {
		removedOwnerDisplayName.add(getLDAPAuthenticatorImpl().getUserDetails(removedOwner).getDisplayName());
	    }

	    List<String> totalOwnerList = new ArrayList<>(lSourceRepo.getOwners());
	    totalOwnerList.removeAll(removedOwnerList);
	    List<String> removedDuplicateOwnerList = totalOwnerList.stream().distinct().collect(Collectors.toList());

	    if (!removedOwnerList.isEmpty()) {
		RepoCheckoutMail reoCheckoutMail = (RepoCheckoutMail) getMailMessageFactory().getTemplate(RepoCheckoutMail.class);
		removedOwnerList.stream().forEach(t -> reoCheckoutMail.addToAddressUserId(t, Constants.MailSenderRole.REPO_OWNERS));
		removedDuplicateOwnerList.stream().forEach(c -> reoCheckoutMail.addcCAddressUserId(c, Constants.MailSenderRole.REPO_OWNERS));
		reoCheckoutMail.setIsNewOwnerUpdate(false);
		reoCheckoutMail.setRepoName(pSourceRepo.getRepository().getName());
		reoCheckoutMail.setRemovedOwnerList(removedOwnerList);
		reoCheckoutMail.setRemovedOwnerDisplayName(removedOwnerDisplayName);
		getMailMessageFactory().push(reoCheckoutMail);
	    }
	    gitUserDetails().populateGitUserDetails();
	    getGitHelper().populateGitCacheNew();

	} catch (WorkflowException ex) {
	    LOG.error("Unable to get the  Repository Details", ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.error("Unable to get the  Repository Details", ex);
	    throw new WorkflowException("Unable to get the  Repository Details", ex);
	}

	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    @Transactional
    public JSONResponse getRepoWiseUserList() {
	JSONResponse lResponse = new JSONResponse();
	try {
	    lResponse.setData(getCacheClient().getRepoWiseUserMap());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(getCacheClient().getRepoWiseUserMap().size());
	} catch (Exception ex) {
	    LOG.error("error occurs in getProductionRepoList ", ex);
	    throw new WorkflowException("Unable to get Production Repository list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getAllRepoUserList() {
	JSONResponse lResponse = new JSONResponse();
	try {
	    lResponse.setData(getCacheClient().getAllRepoUsersMap());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(getCacheClient().getAllRepoUsersMap().size());
	} catch (Exception ex) {
	    LOG.error("error occurs in getProductionRepoList ", ex);
	    throw new WorkflowException("Unable to get Production Repository list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getFilteredRepoList() {
	JSONResponse lResponse = new JSONResponse();
	try {
	    lResponse.setData(getCacheClient().getFilteredRepositoryMap());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setCount(getCacheClient().getFilteredRepositoryMap().size());
	} catch (Exception ex) {
	    LOG.error("error occurs in getFilteredRepositoryMap ", ex);
	    throw new WorkflowException("Unable to get Production Repository list", ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse getRepositoryInfo(User pUser, String pRepoName, String pPlatform) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    String lRepoName = (getGITConfig().getGitProdPath() + pPlatform + "/NONIBM/NONIBM_" + pRepoName).toUpperCase();
	    RepositoryView lRepository = getCacheClient().getFilteredRepositoryMap().get(lRepoName);

	    if (lRepository != null) {
		List<String> lRefs = lRepository.getRepository().getAvailableRefs();
		List<System> lSystems = new ArrayList();
		Map<String, Boolean> lBranches = new HashMap();
		Platform lPlatform = getPlatformDAO().getPlatformByNickName(pPlatform.toLowerCase());
		lSystems = getSystemDAO().findByPlatform(lPlatform.getId());

		for (System lSystem : lSystems) {
		    String lBranch = "refs/heads/master_" + lSystem.getName().toLowerCase();
		    Boolean branchExist = lRefs.contains(lBranch) ? Boolean.TRUE : Boolean.FALSE;
		    lBranches.put(lSystem.getName(), branchExist);
		}
		lRepository.setBranches(lBranches);
	    }
	    lResponse.setData(lRepository);
	} catch (Exception ex) {
	    LOG.info("Error occurs in get repository information for repo - " + pRepoName, ex);
	    throw new WorkflowException("Unable to get repository information for  " + pRepoName, ex);
	}
	return lResponse;
    }

    @Transactional
    public JSONResponse createRepository(User pUser, RepositoryView pRepositoryView, String companyName) {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    Repository lRepository = pRepositoryView.getRepository();
	    Map<String, Boolean> lBranches = pRepositoryView.getBranches();
	    List<String> lRefs = new ArrayList();
	    JSONResponse createRepoResponse = new JSONResponse();
	    JSONResponse createBranchResponse = new JSONResponse();
	    Set<String> lRepoToCreateBranch = new TreeSet();
	    String lRepoName = FilenameUtils.removeExtension(lRepository.getName()).toUpperCase();
	    List<Repository> lAllRepository = getCacheClient().getAllRepositoryMap().get(lRepoName);

	    if (lAllRepository != null && !lAllRepository.isEmpty()) {
		lAllRepository.stream().forEach(t -> lRepoToCreateBranch.add(FilenameUtils.removeExtension(FilenameUtils.getName(t.getName())).toLowerCase().replace("nonibm_", "")));
		createRepoResponse.setStatus(Boolean.TRUE);
	    } else {
		lRepository.getOwners().add(getGITConfig().getServiceUserID());
		String lCommand = Constants.SystemScripts.CREATE_REPOSITORY.getScript() + " " + getGITConfig().getGitProdPath().replace("/", "") + " " + companyName + " " + "nonibm" + " " + lRepository.getName().toLowerCase() + " " + "\"" + lRepository.getDescription().toUpperCase() + "\"" + " " + String.join(",", lRepository.getOwners());

		createRepoResponse = getsSHClientUtils().executeCommand(lCommand);
		if (!createRepoResponse.getStatus()) {
		    LOG.info("unable to create repository, Error Message - " + createRepoResponse.getErrorMessage());
		    throw new WorkflowException("Unable to create repository");
		} else {
		    String prefixSubRepoName = getGITConfig().getGitProdPath().replace("/", "") + "/" + companyName + "/" + "nonibm/nonibm_";
		    String lDBSubRepoName = prefixSubRepoName + lRepository.getName().toLowerCase() + ".git";
		    Repository repository = new Repository();
		    repository.setName(lDBSubRepoName);
		    String lDBRepoName = repository.getTrimmedName().toLowerCase() + ".git";
		    List<String> repoNames = new ArrayList<>();
		    repoNames.add(lDBRepoName);
		    HashMap<String, Integer> repoMap = getRepoMap(repoNames);
		    int repoId = 0;
		    if (repoMap.containsKey(lDBRepoName)) {
			repoId = repoMap.get(lDBRepoName);
		    } else {
			gitSearchDAO().saveRepoDetail(lDBRepoName, getFuncArea(lDBRepoName), "NON_IBM", companyName);
			repoMap = getRepoMap(repoNames);
			if (repoMap.containsKey(lDBRepoName)) {
			    repoId = repoMap.get(lDBRepoName);
			}
		    }
		    if (repoId > 0) {
			String srcUrl = "ssh://" + getGITConfig().getWfLoadBalancerHost() + ":" + getGITConfig().getGitDataPort() + "/" + lDBSubRepoName;
			gitSearchDAO().saveSubRepoDetail(lDBSubRepoName, repoId, srcUrl);
		    } else {
			LOG.error("Repo id creation in DB failed while creating new repository..");
		    }
		}
		lRepoToCreateBranch.add(lRepository.getName().toLowerCase());
	    }

	    if (lRepository.getAvailableRefs() != null || !lRepository.getAvailableRefs().isEmpty()) {
		lRefs = lRepository.getAvailableRefs().stream().filter((t) -> t.contains("master_")).collect(Collectors.toList());
	    }
	    for (Map.Entry<String, Boolean> lBranch : lBranches.entrySet()) {
		String lBranchName = "refs/heads/master_" + lBranch.getKey().toLowerCase();
		if (lBranch.getValue() && !lRefs.contains(lBranchName)) {
		    for (String repoName : lRepoToCreateBranch) {
			String lCommand = Constants.SystemScripts.CREATE_BRANCH.getScript() + " " + lBranch.getKey().toLowerCase() + " " + companyName + " " + repoName;

			createBranchResponse = getsSHClientUtils().executeCommand(lCommand);
			if (!createBranchResponse.getStatus()) {
			    LOG.info("unable to create branch for repository, Error Message - " + createBranchResponse.getErrorMessage());
			    throw new WorkflowException("Unable to create branch for repository");
			}
		    }
		}
	    }

	    if (createRepoResponse.getStatus() && createBranchResponse.getStatus()) {
		gitUserDetails().populateGitUserDetails();
		getGitHelper().populateGitCacheNew();
	    }
	} catch (WorkflowException ex) {
	    LOG.info("Unable to create repository " + ex.getMessage(), ex);
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to create repository", ex);
	    throw new WorkflowException("Unable to create repository", ex);
	}
	return lResponse;
    }

    private HashMap<String, Integer> getRepoMap(List<String> repoNames) {
	List<Object[]> repoDetails = gitSearchDAO().getRepoIds(repoNames);
	HashMap<String, Integer> lSrcOrDerRepoNameAndId = new HashMap<>();
	for (Object[] repo : repoDetails) {
	    Integer repoId = Integer.valueOf(repo[0].toString());
	    String srcRepo = repo[1].toString();
	    String drvRepo = repo[2].toString();

	    lSrcOrDerRepoNameAndId.put(srcRepo, repoId);
	    lSrcOrDerRepoNameAndId.put(drvRepo, repoId);
	}
	return lSrcOrDerRepoNameAndId;
    }

    private String getFuncArea(String repoName) {
	// TEMP FIX
	String lRepoName = FilenameUtils.removeExtension(FilenameUtils.getName(repoName)).toUpperCase();
	String lReturn = FilenameUtils.getName(repoName).toLowerCase().replace("nonibm_", "").replace("ibm_", "").replace(".git", "").toUpperCase();

	if (lRepoName.startsWith("NONIBM_") && lRepoName.length() > 10) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("NONIBM_") && lRepoName.length() <= 10) {
	    return lReturn;
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() > 7) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("IBM_") && lRepoName.length() <= 7) {
	    return lReturn;
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() > 11) {
	    return lReturn.replaceAll("\\d+$", "");
	} else if (lRepoName.startsWith("DERIVED_") && lRepoName.length() <= 11) {
	    return lReturn;
	} else {
	    return lReturn.replaceAll("\\d+$", "");
	}
    }
}
