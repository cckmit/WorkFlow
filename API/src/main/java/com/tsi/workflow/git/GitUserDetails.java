package com.tsi.workflow.git;

import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.ui.GitUsers;
import com.tsi.workflow.dao.GitSearchDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.helper.GITHelper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GitUserDetails {

    private static final Logger LOG = Logger.getLogger(GitUserDetails.class.getName());

    @Autowired
    LdapGroupConfig ldapGroupConfig;

    @Autowired
    LDAPAuthenticatorImpl lDAPAuthenticatorImpl;

    @Autowired
    GitSearchDAO gitSearchDAO;

    @Autowired
    GITHelper gitHelper;

    @Autowired
    GitBlitClientUtils lGitBlitClientUtils;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    WFConfig wfConfig;

    public LdapGroupConfig getLdapGroupConfig() {
	return ldapGroupConfig;
    }

    public LDAPAuthenticatorImpl getLDAPAuthenticatorImpl() {
	return lDAPAuthenticatorImpl;
    }

    public GitSearchDAO gitSearchDAO() {
	return gitSearchDAO;
    }

    public GITHelper gitHelper() {
	return gitHelper;
    }

    public GitBlitClientUtils getGitBlitClientUtils() {
	return lGitBlitClientUtils;
    }

    public ImpPlanDAO getImpPlanDAO() {
	return impPlanDAO;
    }

    public WFConfig getWFConfig() {
	return wfConfig;
    }

    public void populateGitUserDetails() {
	// Get All the group user details
	LOG.info("Ldap User details Master population started");
	Set<String> lCurrentLDAPUserNames = new HashSet<>();
	getLdapGroupConfig().getLdapRolesMap().forEach((role, ldapGroupList) -> {
	    ldapGroupList.forEach(lGroup -> {
		lCurrentLDAPUserNames.addAll(getLDAPAuthenticatorImpl().getLinuxUsers(lGroup));
	    });
	});

	// To Add MTPService user id in user table by defualt
	lCurrentLDAPUserNames.add(getWFConfig().getServiceUserID());
	Map<String, Repository> repoMap;
	try {
	    repoMap = getGitBlitClientUtils().getGitRepositoryList();
	    gitHelper().updateRepoBasedTarSystems();
	    Map<String, Repository> prodRepoMap = repoMap.values().stream().filter(x -> gitHelper().isProdPath(x.getName(), false)).collect(Collectors.toMap(Repository::getName, Function.identity()));
	    // Update Repo detail table default permission
	    updateProductionDefaultPermissions(prodRepoMap);
	    // Update User Details owners permission
	    updateProductionOwnerPermissions(prodRepoMap);
	} catch (IOException ex) {
	    LOG.error("Error in Getting Repo Details ", ex);
	}

	if (!lCurrentLDAPUserNames.isEmpty()) {
	    List<GitUsers> lAllUsers = gitSearchDAO().getExistingGitUsers(new ArrayList<>());
	    List<String> lAllUserNames = lAllUsers.stream().map(x -> x.getUsername()).collect(Collectors.toList());
	    List<String> lUsersToDelete = new ArrayList<>(CollectionUtils.subtract(lAllUserNames, lCurrentLDAPUserNames));
	    List<String> lUsersToAdd = new ArrayList<>(CollectionUtils.subtract(lCurrentLDAPUserNames, lAllUserNames));

	    LOG.info("# of LDAP Users: " + lCurrentLDAPUserNames.size());
	    LOG.info("# of DB Users: " + lAllUserNames.size());
	    LOG.info("# of Delete Users: " + lUsersToDelete.size());
	    LOG.info("# of New Users: " + lUsersToAdd.size());
	    if (!lUsersToAdd.isEmpty()) {
		List<GitUsers> lUsersToAdd1 = new ArrayList<>();
		lUsersToAdd.forEach(gUser -> {
		    User user = getLDAPAuthenticatorImpl().getUserDetails(gUser);
		    GitUsers lGitUsers = new GitUsers();
		    if (user != null) {
			lGitUsers.setUsername(gUser);
			lGitUsers.setDisplayname(user.getDisplayName());
		    }
		    lUsersToAdd1.add(lGitUsers);
		});
		gitSearchDAO().saveNewUsers(lUsersToAdd1);
	    }
	    if (!lUsersToDelete.isEmpty()) {
		gitSearchDAO().updateInActiveFlag(lCurrentLDAPUserNames);
		gitSearchDAO().updateActiveFlag(lCurrentLDAPUserNames);
	    }
	    // Update Permissions foe Deleted Users
	    gitSearchDAO().updateRepoPermissions();
	}
    }

    private void updateProductionDefaultPermissions(Map<String, Repository> repoMap) {
	HashMap<String, String> repoAndDefPermDetails = new HashMap<>();
	HashMap<String, String> repoAndDescDetails = new HashMap<>();
	repoMap.forEach((key, repo) -> {
	    String repoName = repo.getTrimmedName().toLowerCase() + ".git";
	    repoAndDefPermDetails.put(repoName, repo.getCurrentAccess());
	    repoAndDescDetails.put(repoName, repo.getDescription());
	});
	gitSearchDAO().updateDefaultPermAndDesc(repoAndDefPermDetails, repoAndDescDetails);
    }

    private void updateProductionOwnerPermissions(Map<String, Repository> repoMap) {
	HashMap<Integer, Set<String>> lCurrentOwnersMap = new HashMap<>();
	HashMap<String, Integer> lSrcOrDerRepoNameAndId = new HashMap<>();
	Set<String> repoNames = new HashSet<>();

	List<String> permList = new ArrayList<String>();
	permList.add("OWNER");
	List<Object[]> userDetails = gitSearchDAO().getUserDetails(permList);
	List<Object[]> repoDetails = gitSearchDAO().getRepoIds(null);

	for (Object[] repo : repoDetails) {
	    Integer repoId = Integer.valueOf(repo[0].toString());
	    String srcRepo = repo[1].toString();
	    String drvRepo = repo[2].toString();

	    lSrcOrDerRepoNameAndId.put(srcRepo, repoId);
	    lSrcOrDerRepoNameAndId.put(drvRepo, repoId);
	}

	for (Object[] userDetail : userDetails) {
	    String userName = userDetail[0].toString();
	    Integer repoId = Integer.valueOf(userDetail[1].toString());

	    if (userDetail[2].toString() != null && !userDetail[2].toString().isEmpty()) {
		repoNames.add(userDetail[2].toString());
	    }

	    if (userDetail[3].toString() != null && !userDetail[3].toString().isEmpty()) {
		repoNames.add(userDetail[3].toString());
	    }

	    if (lCurrentOwnersMap.containsKey(repoId)) {
		lCurrentOwnersMap.get(repoId).add(userName);
	    } else {
		Set<String> tempUserNames = new HashSet<>();
		tempUserNames.add(userName);
		lCurrentOwnersMap.put(repoId, tempUserNames);
	    }
	}

	HashMap<Integer, Set<String>> repoAndUsersToBeAdded = new HashMap<>();
	HashMap<Integer, Set<String>> repoAndUsersToBeDeleted = new HashMap<>();
	Set<String> currentRepos = new HashSet<>();

	repoMap.forEach((key, repo) -> {
	    currentRepos.add(repo.getName());
	    if (lSrcOrDerRepoNameAndId.containsKey(repo.getTrimmedName().toLowerCase() + ".git")) {
		Integer repoId = lSrcOrDerRepoNameAndId.get(repo.getTrimmedName().toLowerCase() + ".git");
		if (lCurrentOwnersMap.containsKey(repoId) && !repoAndUsersToBeAdded.containsKey(repoId) && !repoAndUsersToBeDeleted.containsKey(repoId)) {
		    // To Add new owners
		    Collection lNewOwners = CollectionUtils.subtract(repo.getOwners(), lCurrentOwnersMap.get(repoId));
		    if (lNewOwners != null && !lNewOwners.isEmpty()) {
			repoAndUsersToBeAdded.put(repoId, new HashSet<>(lNewOwners));
		    }

		    // To delete existing owners
		    Collection lRemoveOwners = CollectionUtils.subtract(lCurrentOwnersMap.get(repoId), repo.getOwners());
		    if (lRemoveOwners != null && !lRemoveOwners.isEmpty()) {
			repoAndUsersToBeDeleted.put(repoId, new HashSet<>(lRemoveOwners));
		    }
		} else if (!repoAndUsersToBeAdded.containsKey(repoId)) {
		    repoAndUsersToBeAdded.put(repoId, repo.getOwners());
		}
	    }
	});
	// To delete existing repo details from User details
	Collection repoList = CollectionUtils.subtract(currentRepos, repoNames);
	if (repoList != null && !repoList.isEmpty()) {
	    repoList.forEach(repo -> {
		if (lSrcOrDerRepoNameAndId.containsKey(repo.toString())) {
		    repoAndUsersToBeDeleted.put(lSrcOrDerRepoNameAndId.get(repo.toString()), lCurrentOwnersMap.get(repo.toString()));
		}
	    });
	}
	gitSearchDAO().insertOrDelUserOwnerDetails(repoAndUsersToBeAdded, repoAndUsersToBeDeleted);
    }

    public void deleteOldDelegations() {
	gitSearchDAO().deleteOldDelegations();
    }
}
