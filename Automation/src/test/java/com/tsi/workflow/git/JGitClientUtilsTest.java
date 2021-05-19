/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.interfaces.IGitConfig;
import java.util.List;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class JGitClientUtilsTest {

    public JGitClientUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of SearchAllRepos method, of class JGitClientUtils.
     */
    // @Test
    // public void testSearchAllRepos() throws Exception {
    //
    // String pCompany = "";
    // String pFileFilter = "";
    // Boolean pMacroHeader = null;
    //
    // JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
    // ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
    // when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
    // when(ins.gitConfig.getServiceUserID()).thenReturn("");
    // when(ins.gitConfig.getServiceSecret()).thenReturn("");
    // ins.SearchAllRepos(pCompany, pFileFilter, pMacroHeader,
    // Constants.PRODSearchType.ONLINE_ONLY, null);
    // }

    /**
     * Test of getPlanRepoName method, of class JGitClientUtils.
     */
    @Test
    public void testGetPlanRepoName() throws Exception {

	String nickName = "";
	String pPlanId = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	ins.getPlanRepoName(nickName, pPlanId);
    }

    /**
     * Test of getPlanRepoFullName method, of class JGitClientUtils.
     */
    @Test
    public void testGetPlanRepoFullName() throws Exception {

	String nickName = "";
	String pPlanId = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	ins.getPlanRepoFullName(nickName, pPlanId);

    }

    /**
     * Test of getPlanLFSRepoFullName method, of class JGitClientUtils.
     */
    @Test
    public void testGetPlanLFSRepoFullName() throws Exception {

	String nickName = "test";
	String pPlanId = "test";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	String result = ins.getPlanLFSRepoFullName(nickName, pPlanId);
    }

    /**
     * Test of getAllBranchList method, of class JGitClientUtils.
     */
    @Test
    public void testGetAllBranchList() throws Exception {

	String nickName = "";
	String pPlanId = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getGitBasePath()).thenReturn("");
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	List<String> expResult = null;
	List<String> result = ins.getAllBranchList(nickName, pPlanId);

    }

    /**
     * Test of createBranches method, of class JGitClientUtils.
     */
    @Test
    public void testCreateBranches() throws Exception {

	String id = "";
	Set<String> lBranchList = null;
	String lNickName = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getGitBasePath()).thenReturn("");
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	ins.createBranches(id, lBranchList, lNickName);

    }

    /**
     * Test of getPlanSSHURL method, of class JGitClientUtils.
     */
    @Test
    public void testGetPlanSSHURL() throws Exception {

	String nickName = "";
	String pPlanid = "";
	String pUserId = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getGitBasePath()).thenReturn("");
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	String result = ins.getPlanSSHURL(nickName, pPlanid, pUserId);

    }

    /**
     * Test of getProductionRepoList method, of class JGitClientUtils.
     */
    @Test
    public void testGetProductionRepoList() throws Exception {

	String nickName = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getGitBasePath()).thenReturn("");
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	List<String> result = ins.getProductionRepoList(nickName);

    }

    /**
     * Test of isRepositoryExist method, of class JGitClientUtils.
     */
    @Test
    public void testIsRepositoryExist() {

	String scmUrl = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	ins.isRepositoryExist(scmUrl);

    }

    /**
     * Test of isBranchExistInRepository method, of class JGitClientUtils.
     */
    @Test
    public void testIsBranchExistInRepository() throws Exception {

	String scmUrl = "";
	String branchName = "";

	JGitClientUtils ins = spy(new JGitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "gitConfig", mock(IGitConfig.class));
	when(ins.gitConfig.getJGitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(ins.gitConfig.getServiceUserID()).thenReturn("");
	when(ins.gitConfig.getServiceSecret()).thenReturn("");
	ins.isBranchExistInRepository(scmUrl, branchName);

    }

}
