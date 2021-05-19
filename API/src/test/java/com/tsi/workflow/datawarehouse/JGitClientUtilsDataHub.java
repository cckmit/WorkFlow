/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.datawarehouse;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static com.tsi.workflow.DataWareHouse.getUser;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.ParamInOut;
import com.tsi.workflow.ParameterMap;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.git.GitBranchSearchResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Radha.Adhimoolam
 */
public class JGitClientUtilsDataHub {

    public static void init() {
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getImplementationList().get(0).getCheckoutSegmentsList().get(0).getFileName());
	    paramInOut.setOut(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    ParameterMap.addParameterInOut("JGitClientUtils.SearchAllRepos", paramInOut);
	}
	for (GitBranchSearchResult lBranch : DataWareHouse.getGitSearchResult().getBranch()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(DataWareHouse.getGitSearchResult().getProgramName() + ".mak");
	    paramInOut.setOut(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    ParameterMap.addParameterInOut("JGitClientUtils.SearchAllRepos", paramInOut);
	}

	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getId().toLowerCase());
	    paramInOut.setOut(DataWareHouse.RepoName);
	    ParameterMap.addParameterInOut("JGitClientUtils.getPlanRepoName", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.RepoName);
	    ParameterMap.addParameterInOut("JGitClientUtils.getPlanRepoFullName", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.RepoLFSName);
	    ParameterMap.addParameterInOut("JGitClientUtils.getPlanLFSRepoFullName", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getId().toLowerCase());
	    paramInOut.setOut(DataWareHouse.getBranchList());
	    ParameterMap.addParameterInOut("JGitClientUtils.getAllBranchList", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.setOut(DataWareHouse.getBranchList());
	    ParameterMap.addParameterInOut("JGitClientUtils.getAllBranchList", paramInOut);
	}

	{
	    Set<String> branchList = new HashSet<>();
	    Map<String, Boolean> returnValue = new HashMap();
	    // returnValue.put(getPlan().getImplementationList().get(0).getId().toLowerCase()
	    // + "_" +
	    // DataWareHouse.getSystemList().get(0).getName().toLowerCase(),
	    // Boolean.TRUE);
	    branchList.add(getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + DataWareHouse.getSystemList().get(0).getName().toLowerCase());
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(branchList);
	    paramInOut.setOut(returnValue);
	    ParameterMap.addParameterInOut("JGitClientUtils.createBranches", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.addIn(getPlan().getId());
	    paramInOut.addIn(getUser().getId());
	    paramInOut.setOut(DataWareHouse.PlanSSHUrl);
	    ParameterMap.addParameterInOut("JGitClientUtils.getPlanSSHURL", paramInOut);
	}
	{
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(getPlan().getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName());
	    paramInOut.setOut(new ArrayList<String>());
	    ParameterMap.addParameterInOut("JGitClientUtils.getProductionRepoList", paramInOut);
	}
	for (PutLevel lPutLevel : DataWareHouse.getPutLevelList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lPutLevel.getScmUrl());
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("JGitClientUtils.isRepositoryExist", paramInOut);
	}
	for (PutLevel lPutLevel : DataWareHouse.getPutLevelList()) {
	    ParamInOut paramInOut = new ParamInOut();
	    paramInOut.addIn(lPutLevel.getScmUrl());
	    paramInOut.addIn("master");
	    paramInOut.setOut(Boolean.TRUE);
	    ParameterMap.addParameterInOut("JGitClientUtils.isRepositoryExist", paramInOut);
	}

    }

}
