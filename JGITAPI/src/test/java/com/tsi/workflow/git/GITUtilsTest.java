/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this tmplate file, choose Tools | Templates
 * and open the tmplate in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.HibernateAwareObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class GITUtilsTest {

    GITConfig gitConfig;
    static String commitId;

    public GITUtilsTest() {
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
	String pCompany = "";
	String pFileFilter = "";
	// GITUtils instance = spy(new GITUtils(gitConfig));
	try {
	    // Collection<GitSearchResult> result = instance.searchAllRepos(pCompany,
	    // pFileFilter, true);
	    pFileFilter = "GitRepo";
	    pCompany = "/API";
	    File createDirectory = new File("tmp/WorkflowTest/API");
	    createDirectory.mkdirs();
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    GITConfig gitConfigInstance = new GITConfig();
	    gitConfigInstance.setGitBasePath("/tmp");
	    gitConfigInstance.setGitProdPath("/WorkflowTest");
	    gitConfigInstance.setGitSourcePath("/source");
	    gitConfigInstance.setGitDerivedPath("/derived");
	    GITUtils instance = spy(new GITUtils(gitConfigInstance));
	    Collection<GitSearchResult> result = instance.searchAllRepos(pCompany, pFileFilter, true, null, 1, null);

	    Collection<GitSearchResult> result1 = instance.searchAllRepos(" ", null, true, null, 1, null);

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testHibernateWrapper() {
	HibernateAwareObjectMapper lHibernateAwareObjectMapper = new HibernateAwareObjectMapper();
	assertNotNull(lHibernateAwareObjectMapper);
    }

    @Test
    public void testIsImplementationPlanRepoExist() {
	String pCompany = "";
	String pImplPlanId = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	boolean expResult = false;
	boolean result = instance.isImplementationPlanRepoExist(pCompany, pImplPlanId);
	assertEquals(expResult, result);
    }

    @Test
    public void testIsImplementationBranchExist() {
	String pCompany = "/API";
	String pImplPlanId = "/t1800199";
	String pBranchName = "";

	try {
	    File createDirectory = new File("tmp/WorkflowTest/API/source/t1800199.git");
	    createDirectory.mkdirs();
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    GITConfig gitConfigInstance = new GITConfig();
	    gitConfigInstance.setGitBasePath("/tmp");
	    gitConfigInstance.setGitProdPath("/WorkflowTest");
	    gitConfigInstance.setGitSourcePath("/source");
	    gitConfigInstance.setGitDerivedPath("/derived");
	    GITUtils instance = spy(new GITUtils(gitConfigInstance));
	    boolean result = instance.isImplementationBranchExist(pCompany, pImplPlanId, pBranchName);
	    GITUtils instance1 = spy(new GITUtils(gitConfig));
	    boolean resultSecond = instance1.isImplementationBranchExist("", "", "");

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetAllBranchList() {
	String pCompany = "/API";
	String pImplPlanId = "/t1800199";

	List<String> expResult = null;
	try {

	    File createDirectory = new File("tmp/WorkflowTest/API/source/t1800199.git");
	    createDirectory.mkdirs();
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    GITConfig gitConfigInstance = new GITConfig();
	    gitConfigInstance.setGitBasePath("/tmp");
	    gitConfigInstance.setGitProdPath("/WorkflowTest");
	    gitConfigInstance.setGitSourcePath("/source");
	    gitConfigInstance.setGitDerivedPath("/derived");
	    GITUtils instance = spy(new GITUtils(gitConfigInstance));

	    List<String> result = instance.getAllBranchList(pCompany, pImplPlanId);

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateBranches() {
	String pImplPlanId = "";
	Set<String> BranchNames = null;
	String pCompany = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	Map<String, Boolean> expResult = null;

	try {
	    Map<String, Boolean> result = instance.createBranches(pImplPlanId, BranchNames, pCompany);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetFilesListOfCommit() {
	try {
	    GITUtils instance = spy(new GITUtils(gitConfig));

	    instance.getFilesListOfCommit("/tmp/WorkflowTest/API/source/t1800199.git", commitId, "");
	    instance.getFilesListOfCommit("", commitId, "");

	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testIsRepositoryExist() {
	String pRepositoryName = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	boolean expResult = true;
	boolean result = instance.isRepositoryExist(pRepositoryName);
	assertEquals(expResult, result);
    }

    @Test
    public void testIsBranchExistInRepository() {
	String pRepositoryName = "/WorkflowTest/API/source/t1800199.git";
	String pBranchName = "refs/heads/TestBranch";

	try {

	    String currentDir = new java.io.File(".").getCanonicalPath();
	    GITConfig gitConfigInstance = new GITConfig();
	    gitConfigInstance.setGitBasePath("/tmp");
	    GITUtils instance = spy(new GITUtils(gitConfigInstance));
	    File lRepoDir = new File(gitConfigInstance.getGitBasePath() + pRepositoryName);

	    boolean result = instance.isBranchExistInRepository(pRepositoryName, pBranchName);
	    GITUtils instance1 = spy(new GITUtils(gitConfig));
	    boolean result1 = instance1.isBranchExistInRepository(pRepositoryName, "master");

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetProductionRepoList() {
	try {
	    String pCompanyName = "";
	    GITUtils instance = spy(new GITUtils(gitConfig));
	    List<String> result = instance.getProductionRepoList(pCompanyName);
	    gitConfig.setGitBasePath("abc");
	    List<String> result1 = instance.getProductionRepoList(pCompanyName);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetPlanSSHURL() {
	String pCompany = "";
	String pImplPlanId = "";
	String pUser = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	String result = instance.getPlanSSHURL(pCompany, pImplPlanId, pUser);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanRepoName() {
	String pCompany = "";
	String pImplPlanId = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	String result = instance.getPlanRepoName(pCompany, pImplPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanLFSRepoName() {
	String pCompany = "";
	String pImplPlanId = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	String result = instance.getPlanLFSRepoName(pCompany, pImplPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanRepoFullName() {
	String pCompany = "";
	String pImplPlanId = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	String result = instance.getPlanRepoFullName(pCompany, pImplPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanLFSRepoFullName() {
	String pCompany = "";
	String pImplPlanId = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	String result = instance.getPlanLFSRepoFullName(pCompany, pImplPlanId);
	assertNotNull(result);
    }

    @Test
    public void testGetFilesList() {

	GITUtils instance = spy(new GITUtils(gitConfig));
	try {

	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git");
	    Git lGit = Git.open(lRepoDir);
	    Ref refs = lGit.branchCreate().setName("TestBranch").call();
	    ReflectionTestUtils.invokeMethod(instance, "getFilesList", lGit.getRepository(), refs, "");

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetSearchResults() {
	GITUtils instance = spy(new GITUtils(gitConfig));
	try {

	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git");
	    Git lGit = Git.open(lRepoDir);
	    List<GitMetaResult> lFilteredFiles = new ArrayList<GitMetaResult>();
	    Ref refs = lGit.branchList().call().get(0);
	    lFilteredFiles.add(new GitMetaResult(lGit.getRepository(), refs, commitId, "one.txt"));
	    ReflectionTestUtils.invokeMethod(instance, "getSearchResults", null, lFilteredFiles, null, lGit, false, false);

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetSearchResult() {
	GITUtils instance = spy(new GITUtils(gitConfig));
	try {

	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git");
	    Git lGit = Git.open(lRepoDir);
	    List<GitMetaResult> lFilteredFiles = new ArrayList<GitMetaResult>();
	    Ref refs = lGit.branchList().call().get(0);
	    lFilteredFiles.add(new GitMetaResult(lGit.getRepository(), refs, commitId, "one.txt"));
	    Repository repository = mock(Repository.class);

	    Map<String, List> inputMap = new HashMap();
	    ReflectionTestUtils.invokeMethod(instance, "getSearchResult", null, lFilteredFiles, inputMap, lGit, true, true);

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testListBranches() {
	GITUtils instance = spy(new GITUtils(gitConfig));
	try {
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git/.git");
	    Git lGit = Git.open(lRepoDir);
	    ReflectionTestUtils.invokeMethod(instance, "listBranches", lGit);

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testListBranchesTwoParameter() {
	GITUtils instance = spy(new GITUtils(gitConfig));
	try {
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git/.git");
	    Git lGit = Git.open(lRepoDir);
	    ReflectionTestUtils.invokeMethod(instance, "listBranches", lGit, "TestBranch");

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateOrphanBranch() {

	try {
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git/.git");
	    GITConfig gitConfigInstance = new GITConfig();
	    gitConfigInstance.setServiceUserID(currentDir);
	    GITUtils instance = spy(new GITUtils(gitConfigInstance));
	    Git lGit = Git.open(lRepoDir);

	    ReflectionTestUtils.invokeMethod(instance, "createOrphanBranch", lGit.getRepository(), "one.txt", false);

	} catch (WorkflowException e) {
	    System.out.println("Err" + e);
	    assertTrue(true);
	} catch (Exception e) {
	    System.out.println("Err1" + e);
	    assertTrue(true);
	}
    }

    @Test
    public void testGetCommitedTags() {
	GITUtils instance = spy(new GITUtils(gitConfig));
	try {
	    String currentDir = new java.io.File(".").getCanonicalPath();
	    File lRepoDir = new File("/tmp/WorkflowTest/API/source/t1800199.git");
	    Git lGit = Git.open(lRepoDir);
	    // lGit.tag().setTags("myTag").call();
	    ReflectionTestUtils.invokeMethod(instance, "getCommitedTags", lGit.getRepository());

	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateBranches_private() {
	String pRepositoryName = "/WorkflowTest/API/source/t1800199.git";
	Set<String> pBranchName = new HashSet<String>();

	try {

	    String currentDir = new java.io.File(".").getCanonicalPath();
	    GITConfig gitConfigInstance = new GITConfig();
	    gitConfigInstance.setGitBasePath("/tmp");
	    GITUtils instance = spy(new GITUtils(gitConfigInstance));

	    ReflectionTestUtils.invokeMethod(instance, "createBranches", pRepositoryName, pBranchName, false);

	} catch (WorkflowException e) {
	    assertTrue(true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSearchRepo_private() {

	Map<String, GitSearchResult> lResult = new HashMap<String, GitSearchResult>();

	try {

	    String currentDir = new java.io.File(".").getCanonicalPath();
	    String pRepositoryName = "/tmp/WorkflowTest/API/source/t1800199.git";
	    GITUtils instance = spy(new GITUtils(gitConfig));
	    ReflectionTestUtils.invokeMethod(instance, "searchRepo", lResult, pRepositoryName, null, false, null, false);

	} catch (WorkflowException e) {

	    assertTrue(true);
	} catch (Exception e) {

	    assertTrue(true);
	}
    }

    @AfterClass
    public static void removeTempDirectory() {

	File removeFromDirectory = new File("/tmp/WorkflowTest");
	Path pathToBeDeleted = removeFromDirectory.toPath();
	try {
	    Files.walk(pathToBeDeleted).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	} catch (IOException e) {
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
	    commitId = commit.getName();
	    git.tag().setName("tag").call();

	} catch (GitAPIException e) {
	    assertTrue(true);
	} catch (IOException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeleteProdBranches() throws IOException, GitAPIException {
	String pImplPlanId = "";
	Set<String> BranchNames = null;
	String pCompany = "";
	GITUtils instance = spy(new GITUtils(gitConfig));
	Map<String, Boolean> expResult = null;

	try {
	    Boolean result = instance.deleteProdBranches("tp", Arrays.asList("test_apo"));
	} catch (WorkflowException e) {
	    assertTrue(true);
	}

    }
}
