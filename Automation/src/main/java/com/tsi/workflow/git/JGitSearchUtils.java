/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import com.tsi.workflow.interfaces.IGitConfig;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.PRODSearchType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpStatus;
import retrofit2.Call;
import retrofit2.Response;

/**
 *
 * @author prabhu.prabhakaran
 */
public class JGitSearchUtils implements IJGITSearchUtils {

    IGitConfig gitConfig;

    public JGitSearchUtils(IGitConfig gitConfig) {
	this.gitConfig = gitConfig;
    }

    @Override
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, Constants.PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<Collection<GitSearchResult>> lResult = lClient.getJGitAPI().SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq.ordinal(), pAllowedRepos);
	Response<Collection<GitSearchResult>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new ArrayList<>();
    }

    @Override
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, Constants.PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<Collection<GitSearchResult>> lResult = lClient.getJGitAPI().SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq.ordinal(), pAllowedRepos);
	Response<Collection<GitSearchResult>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new ArrayList<>();
    }

    @Override
    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems, Set<String> SystemAndFuncBasedFiles) throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect(gitConfig.getJGitRestUrl(), gitConfig.getServiceUserID(), gitConfig.getServiceSecret()).build();
	Call<Collection<GitSearchResult>> lResult = lClient.getJGitAPI().SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq.ordinal(), pAllowedRepos);
	Response<Collection<GitSearchResult>> lResponse = lResult.execute();
	if (lResponse.code() == HttpStatus.SC_CREATED || lResponse.code() == HttpStatus.SC_OK) {
	    return lResponse.body();
	}
	return new ArrayList<>();
    }

}
