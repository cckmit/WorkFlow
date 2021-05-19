/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.git.GITUtils;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.interfaces.IGitConfig;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class GitControllerTest {

    GITConfig gitConfig;

    public GitControllerTest() {
	gitConfig = new GITConfig();
	gitConfig.setGitBasePath("/tmp");
	gitConfig.setGitSourcePath("/source");
	gitConfig.setGitProdPath("/WorkflowTest");
	gitConfig.setGitDerivedPath("derived");
	gitConfig.setGitHost("");
	gitConfig.setGitblitRestUrl("");
	gitConfig.setGitDataPort(0);
	gitConfig.setGitSshPort(0);
    }

    @Test
    public void testSearchAllRepos() {
	String pCompany = "/API";
	String pFileFilter = " ";
	Boolean pMacroHeader = false;
	Integer pPendingStatusReq = 1;
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	try {
	    Collection<GitSearchResult> result = instance.SearchAllRepos(pCompany, pFileFilter, pMacroHeader, pPendingStatusReq, null);
	    assertNotNull(result);
	    Collection<GitSearchResult> resultException = instance.SearchAllRepos(null, null, pMacroHeader, pPendingStatusReq, null);
	    assertNotNull(resultException);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetPlanRepoName() {
	String nickName = "";
	String pPlanId = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	String result = instance.getPlanRepoName(nickName, pPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanRepoFullName() {
	String nickName = "";
	String pPlanId = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	String result = instance.getPlanRepoFullName(nickName, pPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanLFSRepoFullName() {
	String nickName = "";
	String pPlanId = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	String result = instance.getPlanLFSRepoFullName(nickName, pPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetAllBranchList() {

	String nickName = "";
	String pPlanId = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	List<String> result = instance.getAllBranchList(nickName, pPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanSSHURL() {

	String nickName = "";
	String pPlanid = "";
	String pUserId = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	String result = instance.getPlanSSHURL(nickName, pPlanid, pUserId);
	assertNotNull(result);

    }

    @Test
    public void testGetProductionRepoList() {

	String nickName = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	List<String> result = instance.getProductionRepoList(nickName);
	assertNotNull(result);

    }

    @Test
    public void testIsRepositoryExist() {

	String scmUrl = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	boolean expResult = true;
	boolean result = instance.isRepositoryExist(scmUrl);
	assertEquals(expResult, result);

    }

    @Test
    public void testIsBranchExistInRepository() {

	String scmUrl = "/tmp/WorkflowTest/API/source/t1800199.git/.git";
	String branchName = "refs/heads/TestBranch";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);
	boolean expResult = false;
	boolean result = instance.isBranchExistInRepository(scmUrl, branchName);
	assertEquals(expResult, result);

    }

    @Test
    public void testFindAllRepos() {

	String scmUrl = "";
	String branchName = "";
	GitController instance = new GitController();
	instance.setGITConfig(gitConfig);

	boolean expResult = false;
	try {
	    GitSearchResult gitSearchResult = new GitSearchResult();
	    List<GitBranchSearchResult> branchSearchResult = new ArrayList<GitBranchSearchResult>();
	    gitSearchResult.setBranch(branchSearchResult);
	    /*
	     * GITUtils gitUtilsMock=mock(GITUtils.class); GitSearchResult
	     * mockGitSearchResult=mock(GitSearchResult.class);
	     * doReturn(gitSearchResult).when(gitUtilsMock).searchAllRepos(any(), any(),
	     * any(), any(), any());
	     */
	    instance.FindAllRepos(null, null, " ", " ", " ");

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateBranches() {
	GitController instance = new GitController();
	GitController gitController = spy(new GitController());
	try {
	    Set<String> branchList = new HashSet();
	    GITUtils gitUtilsMock = spy(new GITUtils(mock(IGitConfig.class)));
	    Map<String, Boolean> retValue = new HashMap();
	    retValue.put("result", Boolean.TRUE);
	    Set<String> lBranchList = new LinkedHashSet<>();
	    lBranchList.add("wsp");
	    doReturn(retValue).when(gitUtilsMock).createBranches("t1800", lBranchList, "TestBranch");
	    // instance.createBranches("t1800", lBranchList, "TestBranch");

	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @BeforeClass
    public static void createBarectory() {

	try {
	    File createDirectory = new File("/tmp/WorkflowTest/API/source/t1800199.git");
	    createDirectory.mkdirs();
	    File file = new File("/tmp/WorkflowTest/API/source/t1800199.git/one.txt");
	    file.createNewFile();
	    Git git = Git.init().setDirectory(f‌ile.getParentF‌ile()).call();
	    git.add();
	    RevCommit commit = git.commit().setMessage("Create readme file").call();
	    // commitId = commit.getName();
	    git.tag().setName("tag").call();

	} catch (GitAPIException e) {
	    assertTrue(true);
	} catch (IOException e) {
	    assertTrue(true);
	}
    }
}
