package com.tsi.workflow.git;

import com.tsi.workflow.interfaces.IGitConfig;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpStatus;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

public class JGitClientUtils {

    IGitConfig gitConfig;

    public JGitClientUtils(IGitConfig gitConfig) {
	this.gitConfig = gitConfig;
    }

    // public Collection<GitBaseMetaResult> findMakFile(String url, String commit,
    // String fileName) throws IOException {
    // JGitClient lClient = new
    // JGitClient.Builder().connect(gitConfig.getJGitRestUrl(),
    // gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
    // Call<Collection<GitBaseMetaResult>> lResult =
    // lClient.getJGitAPI().getMakFileDetail(url, commit, fileName);
    // Response<Collection<GitBaseMetaResult>> lResponse = lResult.execute();
    // if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() ==
    // HttpStatus.SC_OK) {
    // return lResponse.body();
    // }
    // return new ArrayList<>();
    // }
    public String getPlanRepoName(String nickName, String pPlanId) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<String> lResult = lClient.getJGitAPI().getPlanRepoName(nickName, pPlanId);
	Response<String> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return "";
    }

    public String getPlanRepoFullName(String nickName, String pPlanId) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<String> lResult = lClient.getJGitAPI().getPlanRepoFullName(nickName, pPlanId);
	Response<String> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return "";
    }

    public String getPlanLFSRepoFullName(String nickName, String pPlanId) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<String> lResult = lClient.getJGitAPI().getPlanLFSRepoFullName(nickName, pPlanId);
	Response<String> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return "";
    }

    public List<String> getAllBranchList(String nickName, String pPlanId) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<List<String>> lResult = lClient.getJGitAPI().getAllBranchList(nickName, pPlanId);
	Response<List<String>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new ArrayList<>();
    }

    public Map<String, Boolean> createBranches(String id, Set<String> lBranchList, String lNickName) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<Map<String, Boolean>> lResult = lClient.getJGitAPI().createBranches(id, lBranchList, lNickName);
	Response<Map<String, Boolean>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new HashMap<>();
    }

    public String getPlanSSHURL(String nickName, String pPlanid, String pUserId) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<String> lResult = lClient.getJGitAPI().getPlanSSHURL(nickName, pPlanid, pUserId);
	Response<String> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return "";
    }

    public List<String> getProductionRepoList(String nickName) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<List<String>> lResult = lClient.getJGitAPI().getProductionRepoList(nickName);
	Response<List<String>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new ArrayList<>();
    }

    public Boolean isRepositoryExist(String scmUrl) {
	try {
	    JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	    Call<Boolean> lResult = lClient.getJGitAPI().isRepositoryExist(scmUrl);
	    Response<Boolean> lResponse = lResult.execute();
	    if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
		return lResponse.body();
	    }
	} catch (IOException ex) {
	    Logger.getLogger(JGitClientUtils.class.getName()).log(Level.ERROR, null, ex);
	}
	return false;
    }

    public Boolean isBranchExistInRepository(String scmUrl, String branchName) throws IOException {
	try {
	    JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	    Call<Boolean> lResult = lClient.getJGitAPI().isBranchExistInRepository(scmUrl, branchName);
	    Response<Boolean> lResponse = lResult.execute();
	    if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
		return lResponse.body();
	    }
	} catch (IOException ex) {
	    Logger.getLogger(JGitClientUtils.class.getName()).log(Level.ERROR, null, ex);
	}
	return false;
    }

    public Boolean deleteProdBranches(String companyName, List<String> branchList) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<Boolean> lResult = lClient.getJGitAPI().deleteProdBranches(companyName, branchList);
	Response<Boolean> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return false;
    }

    public List<String> getProdRepoBranches(String pRepoName) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<List<String>> lResult = lClient.getJGitAPI().getProdRepoBranches(pRepoName);
	Response<List<String>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new ArrayList();
    }

    public Boolean deletePlansImpBranches(String planId, String impId, String companyName, Set<String> lBranchList) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<Boolean> lResult = lClient.getJGitAPI().deletePlansImpBranches(planId, impId, companyName, lBranchList);
	Response<Boolean> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return Boolean.TRUE;
    }

}
