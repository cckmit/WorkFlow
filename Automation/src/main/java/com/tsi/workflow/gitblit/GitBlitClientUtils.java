/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit;

import com.tsi.workflow.gitblit.model.AccessRestrictionType;
import com.tsi.workflow.gitblit.model.AuthorizationControl;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.RepositoryPermission;
import com.tsi.workflow.gitblit.model.UserModel;
import com.tsi.workflow.interfaces.IGitConfig;
import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author USER
 */

public class GitBlitClientUtils {

    private static final Logger LOG = Logger.getLogger(GitBlitClientUtils.class.getName());

    IGitConfig gitConfig;

    public GitBlitClientUtils(IGitConfig gitConfig) {
	this.gitConfig = gitConfig;
    }

    /**
     * This method creates a repository in glitblit using REST client
     *
     * @param pRepoName
     *            - specifies the repository name
     * @param pRepoDesc
     *            - specifies the description of repository
     * @param pOwnerList
     *            - specifies list of owners for the repository
     * @return returns true on successful creation of repository, else return
     * 
     * @throws IOException
     */
    public Boolean createGitRepository(String pRepoName, String pRepoDesc, SortedSet<String> pOwnerList) throws IOException {

	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	// Add Service Id as one of the Owner
	pOwnerList.add(gitConfig.getServiceUserID());

	Repository lRepos = new Repository();
	lRepos.setAuthorizationControl(AuthorizationControl.NAMED);
	lRepos.setAllowAuthenticated(true);
	lRepos.setAccessRestriction(AccessRestrictionType.PUSH);
	lRepos.setUseIncrementalPushTags(true);
	lRepos.setOwners(pOwnerList);
	lRepos.setDescription(pRepoDesc);
	lRepos.setName((pRepoName.startsWith(File.separator)) ? pRepoName.substring(1) : pRepoName);

	// Call REST API
	Call<Void> lCall = lClient.getGitBlitAPI().createRepository(lRepos, "CREATE_REPOSITORY");
	Response<Void> lResponse = lCall.execute();

	return lResponse.code() == HttpStatus.SC_OK;
    }

    public Map<String, Repository> getGitRepositoryList() {

	Map<String, Repository> repo = new HashMap();
	try {
	    GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	    Call<Map<String, Repository>> lRepoCall = lClient.getGitBlitAPI().listRepositories("LIST_REPOSITORIES");
	    Response<Map<String, Repository>> lResponse = lRepoCall.execute();
	    repo = lResponse.body();
	} catch (IOException e) {
	    try {
		if (e instanceof SocketTimeoutException) {
		    throw new SocketTimeoutException();
		}
	    } catch (SocketTimeoutException f) {
	    }
	}
	return repo;
    }

    /**
     * This method update a repository in glitblit using REST client
     * 
     * @param pRepository
     * @return returns true on successful update of repository, else return true
     * @throws IOException
     */
    public Boolean updateGitRepository(Repository pRepository) throws IOException {

	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	// Call REST API
	Call<Void> lCall = lClient.getGitBlitAPI().editRepository(pRepository, "EDIT_REPOSITORY", pRepository.getName());
	Response<Void> lResponse = lCall.execute();

	return lResponse.code() == HttpStatus.SC_OK;
    }

    /**
     * This method creates a repository in glitblit using REST client
     *
     * @param pRepoName
     *            - specifies the repository name
     * @return returns true on successful deletion of repository, else return
     * 
     * @throws IOException
     */
    public Boolean deleteGitRepository(String pRepoName) throws IOException {

	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	Repository lRepos = new Repository();
	lRepos.setName(pRepoName);

	// Call REST API
	Call<Void> lCall = lClient.getGitBlitAPI().deleteRepository(lRepos, "DELETE_REPOSITORY");
	Response<Void> lResponse = lCall.execute();

	return lResponse.code() == HttpStatus.SC_OK;
    }

    // public Boolean setPermissionForGitRepository(String pRepoName, String
    // pDeveloperId, String pPermission) throws IOException, InterruptedException {
    // //Call REST API
    // ArrayList<RepositoryPermission> lRepoPermissions = new ArrayList<>();
    // lRepoPermissions.add(new RepositoryPermission(pDeveloperId, pPermission));
    // return setPermissionForGitRepository(pRepoName, lRepoPermissions);
    // }

    // public Boolean setPermissionForGitRepository(String pRepoName,
    // List<RepositoryPermission> pUserList) throws IOException,
    // InterruptedException {
    // for (int loopCounter = 1; loopCounter <= 5; loopCounter++) {
    // GitBlitClient lClient = new
    // GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(),
    // gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
    // Call<Void> lCall =
    // lClient.getGitBlitAPI().setRepositoryMemberPermission(pUserList,
    // "SET_REPOSITORY_MEMBER_PERMISSIONS", pRepoName);
    // Response<Void> lResponse = lCall.execute();
    // Boolean setPermissionStatus = true;
    // Boolean updateStatus = (lResponse.code() == HttpStatus.SC_OK);
    // if (updateStatus) {
    // Map<String, String> repositoryUsersList = getRepositoryUsersList(pRepoName);
    // if (repositoryUsersList != null && !repositoryUsersList.isEmpty()) {
    // for (RepositoryPermission repoUser : pUserList) {
    // String permission = repositoryUsersList.get(repoUser.getRegistrant());
    // if (permission == null) {
    // setPermissionStatus = false;
    // break;
    // } else {
    // LOG.info("User id - " + repoUser.getRegistrant() + " Permission - " +
    // permission);
    // if (permission.equalsIgnoreCase("RW+")) {
    // continue;
    // } else if (repoUser.getPermission().startsWith("RW") &&
    // permission.startsWith("RW")) {
    // continue;
    // } else if (!permission.equalsIgnoreCase(repoUser.getPermission())) {
    // setPermissionStatus = false;
    // break;
    // }
    // }
    // }
    // }
    // }
    //
    // if (!updateStatus || !setPermissionStatus) {
    // LOG.info("Issue Occurs in Repo permission setting - Going for Wait. Loop
    // Counter - " + loopCounter + " repo name - " + pRepoName);
    // Thread.sleep(15 * 1000L);
    // } else {
    // return Boolean.TRUE;
    // }
    // }
    //
    // return Boolean.TRUE;
    // }

    // /**
    // * This Method freeze the Repository in GitBlit using REST Client
    // *
    // * @param pRepoName - specifies the Repository Name with full path with .git
    // * Extension
    // * @return returns true on successful Freeze of Repository else returns
    // * false
    // * @throws IOException
    // */
    // public Boolean freezeRepository(String pRepoName) throws IOException {
    //
    // GitBlitClient lClient = new
    // GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(),
    // gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
    // Call<Map<String, Repository>> lListRepoCall =
    // lClient.getGitBlitAPI().listRepositories("LIST_REPOSITORIES");
    // Response<Map<String, Repository>> lListRepoResponse =
    // lListRepoCall.execute();
    //
    // if (lListRepoResponse.code() != HttpStatus.SC_OK) {
    // return false;
    // }
    //
    // Repository lRepo = lListRepoResponse.body().get(pRepoName.replace("rpc",
    // "r"));
    // if (lRepo != null) {
    // lRepo.setIsFrozen(true);
    // Call<Void> lCall = lClient.getGitBlitAPI().editRepository(lRepo,
    // "EDIT_REPOSITORY", lRepo.getName());
    // Response<Void> lResponse = lCall.execute();
    // return lResponse.code() == HttpStatus.SC_OK;
    // }
    //
    // return lListRepoResponse.code() == HttpStatus.SC_OK;
    // }
    //
    // /**
    // * This Method unfreeze the Repository in GitBlit using REST Client
    // *
    // * @param pRepoName - specifies the Repository Name with full path including
    // * .git extension
    // * @return returns true on successful unFreeze of Repository else returns
    // * false
    // * @throws IOException
    // */
    // public Boolean unFreezeRepository(String pRepoName) throws IOException {
    // GitBlitClient lClient = new
    // GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(),
    // gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
    // Call<Map<String, Repository>> lListRepoCall =
    // lClient.getGitBlitAPI().listRepositories("LIST_REPOSITORIES");
    // Response<Map<String, Repository>> lListRepoResponse =
    // lListRepoCall.execute();
    //
    // if (lListRepoResponse.code() != HttpStatus.SC_OK) {
    // return false;
    // }
    //
    // Repository lRepo = lListRepoResponse.body().get(pRepoName);
    // if (lRepo != null) {
    // lRepo.setIsFrozen(false);
    // Call<Void> lCall = lClient.getGitBlitAPI().editRepository(lRepo,
    // "EDIT_REPOSITORY", lRepo.getName());
    // Response<Void> lResponse = lCall.execute();
    // return lResponse.code() == HttpStatus.SC_OK;
    // }
    //
    // return lListRepoResponse.code() == HttpStatus.SC_OK;
    // }
    //
    public String getImplementationTicketURL(String pCompany, String pImplPlanId, String pTicket) throws Exception {
	return gitConfig.getGitblitTicketUrl() + FilenameUtils.separatorsToUnix(gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git").replace("/", "!") + "/" + pTicket;
    }

    public String getSegmentImplementationTicketURL(String pCompany, String pImplPlanId, String pImplId, String targetSystem, String fileName) throws Exception {
	return gitConfig.getGitblitTicketUrl() + FilenameUtils.separatorsToUnix(gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git").replace("/", "!") + File.separator + pImplId.toLowerCase() + "_" + targetSystem.toLowerCase() + File.separator + fileName.replace("/", "!");
    }

    public void refreshIndex(String pCompany, String pImplPlanId) throws IOException {
	String lRepoName = FilenameUtils.separatorsToUnix(gitConfig.getGitProdPath() + pCompany + File.separator + gitConfig.getGitSourcePath() + pImplPlanId.toLowerCase() + ".git");
	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	// Call REST API
	Call<Void> lCall = lClient.getGitBlitAPI().refreshIndex(lRepoName, "REINDEX_TICKETS");
	Response<Void> lResponse = lCall.execute();
    }

    public Map<String, String> getRepositoryUsersList(String pRepoName) throws IOException {
	List<RepositoryPermission> lUserList = new ArrayList<>();
	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	// Call REST API
	Call<List<RepositoryPermission>> lCall = lClient.getGitBlitAPI().listRepositoryUserList(pRepoName, "LIST_REPOSITORY_MEMBER_PERMISSIONS");
	Response<List<RepositoryPermission>> lResponse = lCall.execute();

	Map<String, String> lUserPermList = new HashMap();
	lUserList = lResponse.body();
	for (RepositoryPermission lUserAccess : lUserList) {
	    lUserPermList.put(lUserAccess.getRegistrant(), lUserAccess.getPermission());
	}

	return lUserPermList;
    }

    public List<UserModel> getUsersList() throws IOException {
	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<List<UserModel>> lCall = lClient.getGitBlitAPI().getUsersList("LIST_USERS");
	Response<List<UserModel>> lResponse = lCall.execute();
	return lResponse.body();
    }

    public UserModel getUser(String userId) throws IOException {
	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<UserModel> lCall = lClient.getGitBlitAPI().getUser("GET_USER", userId);
	Response<UserModel> lResponse = lCall.execute();
	return lResponse.body();
    }

    public Map<String, List<String>> getGitBranchList() throws IOException {

	Map<String, List<String>> repo = new HashMap();
	GitBlitClient lClient = new GitBlitClient.Builder().connect(gitConfig.getGitblitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();

	Call<Map<String, List<String>>> lRepoCall = lClient.getGitBlitAPI().listRepositoryBranches("LIST_BRANCHES");
	Response<Map<String, List<String>>> lResponse = lRepoCall.execute();
	repo = lResponse.body();
	return repo;
    }
}
