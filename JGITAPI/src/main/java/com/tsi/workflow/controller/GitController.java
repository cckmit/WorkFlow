/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GITUtils;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitMetaResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author USER
 */
@Controller
@RequestMapping("jgit")
public class GitController {

    @Autowired
    GITConfig gITConfig;
    private static final Logger LOG = Logger.getLogger(GitController.class.getName());

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public void setGITConfig(GITConfig gITConfig) {
	this.gITConfig = gITConfig;
    }

    @ResponseBody
    @RequestMapping(value = "/SearchAllRepos", method = RequestMethod.GET)
    public Collection<GitSearchResult> SearchAllRepos(@RequestParam String pCompany, @RequestParam String pFileFilter, @RequestParam(required = false, defaultValue = "false") Boolean pMacroHeader, @RequestParam(required = false, defaultValue = "1") Integer pStatusReq, @RequestParam(required = false) List<String> pReposAllowed) {
	try {
	    return new GITUtils(getGITConfig()).searchAllRepos(pCompany, pFileFilter, pMacroHeader, null, pStatusReq, pReposAllowed);
	} catch (Exception ex) {
	    LOG.error("Error in Searching File", ex);
	}
	return new ArrayList<>();
    }

    @ResponseBody
    @RequestMapping(value = "/SearchAllRepos", method = RequestMethod.POST)
    public Collection<GitSearchResult> searchSegmentInAllRepos(@RequestParam String pCompany, @RequestParam String pFileFilter, @RequestParam(required = false, defaultValue = "false") Boolean pMacroHeader, @RequestParam(required = false, defaultValue = "1") Integer pStatusReq, @RequestBody List<String> pReposAllowed) {
	try {
	    return new GITUtils(getGITConfig()).searchAllRepos(pCompany, pFileFilter, pMacroHeader, null, pStatusReq, pReposAllowed);
	} catch (Exception ex) {
	    LOG.error("Error in Searching File", ex);
	}
	return new ArrayList<>();
    }

    @ResponseBody
    @RequestMapping(value = "/doGC", method = RequestMethod.GET)
    public Boolean doGC(@RequestParam String pCompany, @RequestParam String pRepoName, @RequestParam(required = false, defaultValue = "true") Boolean pisSource) {
	return new GITUtils(getGITConfig()).doGC(pCompany, pRepoName, pisSource);
    }

    @ResponseBody
    @RequestMapping(value = "/FindAllRepos", method = RequestMethod.GET)
    public void FindAllRepos(HttpServletRequest request, HttpServletResponse response, @RequestParam String pCompany, @RequestParam String pFileFilter, @RequestParam String pBranch) {
	try {
	    Collection<GitSearchResult> lGitSearchResults = new GITUtils(getGITConfig()).searchAllRepos(pCompany, pFileFilter, false, pBranch, Constants.PRODSearchType.BOTH.ordinal(), null);
	    StringBuilder builder = new StringBuilder();
	    for (GitSearchResult lGitSearchResult : lGitSearchResults) {
		List<GitBranchSearchResult> lBranches = lGitSearchResult.getBranch();
		for (GitBranchSearchResult lBranch : lBranches) {
		    builder.append(lGitSearchResult.getProgramName()).append(",").append(lBranch.getRefStatus()).append(",").append(lBranch.getRefPlan()).append(",").append(Constants.APP_DATE_TIME_FORMAT.get().format(lBranch.getRefLoadDate())).append(",").append(lGitSearchResult.getFileName()).append(",").append(lBranch.getSourceUrl().replaceAll("ssh.*:\\d*", "")).append("/").append(lBranch.getCommitId().substring(0, 6)).append("/").append(lGitSearchResult.getFileName()).append(",")
			    .append(lBranch.getFuncArea()).append(System.lineSeparator());
		}
	    }
	    if (builder.length() == 0) {
		response.getWriter().write("NOT FOUND");
	    } else {
		response.getWriter().write(builder.toString());
	    }
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("Error in Searching File", ex);
	}
    }

    @ResponseBody
    @RequestMapping(value = "/getPlanRepoName", method = RequestMethod.GET)
    public String getPlanRepoName(@RequestParam String nickName, @RequestParam String pPlanId) {
	return new GITUtils(getGITConfig()).getPlanRepoName(nickName, pPlanId);
    }

    @ResponseBody
    @RequestMapping(value = "/getPlanRepoFullName", method = RequestMethod.GET)
    public String getPlanRepoFullName(@RequestParam String nickName, @RequestParam String pPlanId) {
	return new GITUtils(getGITConfig()).getPlanRepoFullName(nickName, pPlanId);
    }

    @ResponseBody
    @RequestMapping(value = "/getPlanLFSRepoFullName", method = RequestMethod.GET)
    public String getPlanLFSRepoFullName(@RequestParam String nickName, @RequestParam String pPlanId) {
	return new GITUtils(getGITConfig()).getPlanLFSRepoFullName(nickName, pPlanId);
    }

    @ResponseBody
    @RequestMapping(value = "/getMakFileDetail", method = RequestMethod.GET)
    public List<GitMetaResult> getMakFileDetail(@RequestParam String pUrl, @RequestParam String pCommit, @RequestParam String pFileName) {
	return new GITUtils(getGITConfig()).getFilesListOfCommit(pUrl, pCommit, pFileName);
    }

    @ResponseBody
    @RequestMapping(value = "/getAllBranchList", method = RequestMethod.GET)

    public List<String> getAllBranchList(@RequestParam String nickName, @RequestParam String pPlanId) {
	try {
	    return new GITUtils(getGITConfig()).getAllBranchList(nickName, pPlanId);
	} catch (Exception ex) {
	    LOG.error("Error in Searching Branch", ex);
	}
	return new ArrayList<>();
    }

    @ResponseBody
    @RequestMapping(value = "/createBranches", method = RequestMethod.GET)
    public Map<String, Boolean> createBranches(@RequestParam String id, @RequestParam Set<String> lBranchList, @RequestParam String lNickName) {
	return new GITUtils(getGITConfig()).createBranches(id, lBranchList, lNickName);
    }

    @ResponseBody
    @RequestMapping(value = "/getPlanSSHURL", method = RequestMethod.GET)
    public String getPlanSSHURL(@RequestParam String nickName, @RequestParam String pPlanid, @RequestParam String pUserId) {
	return new GITUtils(getGITConfig()).getPlanSSHURL(nickName, pPlanid, pUserId);
    }

    @ResponseBody
    @RequestMapping(value = "/getProductionRepoList", method = RequestMethod.GET)
    public List<String> getProductionRepoList(@RequestParam String nickName) {
	return new GITUtils(getGITConfig()).getProductionRepoList(nickName);
    }

    @ResponseBody
    @RequestMapping(value = "/isRepositoryExist", method = RequestMethod.GET)
    public boolean isRepositoryExist(@RequestParam String scmUrl) {
	if (scmUrl == null || scmUrl.isEmpty()) {
	    return false;
	}
	return new GITUtils(getGITConfig()).isRepositoryExist(scmUrl);
    }

    @ResponseBody
    @RequestMapping(value = "/isBranchExistInRepository", method = RequestMethod.GET)
    public boolean isBranchExistInRepository(@RequestParam String scmUrl, @RequestParam String branchName) {
	try {
	    return new GITUtils(getGITConfig()).isBranchExistInRepository(scmUrl, branchName);
	} catch (Exception ex) {
	    LOG.error("Error in Checking Repository", ex);
	}
	return false;
    }

    @ResponseBody
    @RequestMapping(value = "/deleteProdBranches", method = RequestMethod.GET)
    public Boolean deleteProdBranches(@RequestParam String companyName, @RequestParam List<String> branchList) throws WorkflowException, IOException, GitAPIException {
	return new GITUtils(getGITConfig()).deleteProdBranches(companyName, branchList);
    }

    @ResponseBody
    @RequestMapping(value = "/getProdRepoBranches", method = RequestMethod.GET)
    public List<String> getProdRepoBranches(@RequestParam String repoName) throws WorkflowException, IOException, GitAPIException {
	return new GITUtils(getGITConfig()).getProdRepoBranches(repoName);
    }

    @ResponseBody
    @RequestMapping(value = "/deletePlansImpBranches", method = RequestMethod.GET)
    public Boolean deletePlansImpBranches(@RequestParam String planId, @RequestParam String impId, @RequestParam String companyName, @RequestParam List<String> branchList) throws WorkflowException, IOException, GitAPIException {
	return new GITUtils(getGITConfig()).deletePlansImpBranches(planId, impId, companyName, branchList);
    }

}
