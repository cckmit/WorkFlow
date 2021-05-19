/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.ssh;

import com.tsi.workflow.AuthModel;
import com.tsi.workflow.AuthSystem;
import com.tsi.workflow.ExecModel;
import com.tsi.workflow.User;
import com.tsi.workflow.interfaces.IGitConfig;
import com.tsi.workflow.interfaces.ISystem;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author User
 */
public class SSHClientUtils {

    private static final Logger LOG = Logger.getLogger(SSHClientUtils.class.getName());

    IGitConfig gitConfig;

    public SSHClientUtils(IGitConfig gitConfig) {
	this.gitConfig = gitConfig;
    }

    public Boolean addImplementationTag(String pRepoName, Constants.PlanStatus pTagName, List<String> pBranchList) throws Exception {
	String command = Constants.GitScripts.TAG_CMD.getScript() + pTagName.name() + " " + pRepoName + " " + String.join(",", pBranchList);
	SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, null, command));
	Response<JSONResponse> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body().getStatus();
	}
	return false;
    }

    public Boolean addImplementationTag(String pRepoName, Constants.TagStatus pTagName, List<String> pBranchList) throws Exception {
	String command = Constants.GitScripts.TAG_CMD.getScript() + pTagName.name() + " " + pRepoName + " " + String.join(",", pBranchList);
	SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, null, command));
	Response<JSONResponse> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body().getStatus();
	}
	return false;
    }

    public Boolean addImplementationTag(String pRepoName, Constants.ImplementationSubStatus pTagName, List<String> pBranchList) throws Exception {
	String command = Constants.GitScripts.TAG_CMD.getScript() + pTagName.name() + " " + pRepoName + " " + String.join(",", pBranchList);
	SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, null, command));
	Response<JSONResponse> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body().getStatus();
	}
	return false;
    }

    public Boolean removeTag(String pRepoName, Constants.TagStatus pTagName) throws Exception {
	String command = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + pTagName.name() + " " + pRepoName;
	SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, null, command));
	Response<JSONResponse> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body().getStatus();
	}
	return false;
    }

    public Boolean removeTag(String pRepoName, List<String> planSubStatus) throws Exception {
	String command = Constants.GitScripts.REMOVE_TAG_CMD.getScript() + String.join(",", planSubStatus) + " " + pRepoName;
	SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, null, command));
	Response<JSONResponse> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body().getStatus();
	}
	return false;
    }

    public Boolean authenticate(User pUser, List<ISystem> pSystems) throws Exception {
	AuthModel authModel = new AuthModel();
	authModel.setUser(pUser);
	for (ISystem pSystem : pSystems) {
	    authModel.addSystem(pSystem);
	}
	SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<JSONResponse> lResult = lClient.getSSHAPI().authenticate(authModel);
	Response<JSONResponse> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body().getStatus();
	}
	return false;
    }

    public JSONResponse executeCommand(String pCommand) {
	try {
	    SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	    Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, null, pCommand));
	    Response<JSONResponse> lResponse = lResult.execute();
	    if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
		return lResponse.body();
	    }
	} catch (IOException ex) {
	    LOG.error("Error in SSH Client Call", ex);
	}
	return new JSONResponse();
    }

    public JSONResponse executeCommand(ISystem pSystem, String pCommand) {
	try {
	    SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	    Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(null, new AuthSystem(pSystem.getIpaddress(), pSystem.getPortno(), pSystem.getName()), pCommand));
	    Response<JSONResponse> lResponse = lResult.execute();
	    if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
		return lResponse.body();
	    }
	} catch (IOException ex) {
	    LOG.error("Error in SSH Client Call", ex);
	}
	return new JSONResponse();
    }

    public JSONResponse executeCommand(User pUser, String pCommand) {
	try {
	    SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	    Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(pUser, null, pCommand));
	    Response<JSONResponse> lResponse = lResult.execute();
	    if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
		return lResponse.body();
	    }
	} catch (IOException ex) {
	    LOG.error("Error in SSH Client Call", ex);
	}
	return new JSONResponse();
    }

    public JSONResponse executeCommand(User pUser, ISystem pSystem, String pCommand) {
	try {
	    SSHClient lClient = new SSHClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	    Call<JSONResponse> lResult = lClient.getSSHAPI().execute(new ExecModel(pUser, new AuthSystem(pSystem.getIpaddress(), pSystem.getPortno(), pSystem.getName()), pCommand));
	    Response<JSONResponse> lResponse = lResult.execute();
	    if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
		return lResponse.body();
	    }
	} catch (IOException ex) {
	    LOG.error("Error in SSH Client Call", ex);
	}
	return new JSONResponse();
    }

}
