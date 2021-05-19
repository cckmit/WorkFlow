/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.interfaces.IGitConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
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
public class GitBlitClientUtilsTest {

    public GitBlitClientUtilsTest() {
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
     * Test of createGitRepository method, of class GitBlitClientUtils.
     */
    @Test
    public void testCreateGitRepository() throws Exception {

	String pRepoName = "";
	String pRepoDesc = "";
	GitBlitClientUtils instance = spy(new GitBlitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(instance, "gitConfig", mock(IGitConfig.class));
	when(instance.gitConfig.getGitblitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(instance.gitConfig.getServiceUserID()).thenReturn("");
	when(instance.gitConfig.getServiceSecret()).thenReturn("");
	SortedSet<String> pOwners = new TreeSet();
	pOwners.add("test");
	Boolean result = instance.createGitRepository(pRepoName, pRepoDesc, pOwners);

    }

    /**
     * Test of setPermissionForGitRepository method, of class GitBlitClientUtils.
     */
    @Test
    public void testSetPermissionForGitRepository() throws Exception {

	String pRepoName = "";
	String pDeveloperId = "";
	String pPermission = "";
	GitBlitClientUtils instance = spy(new GitBlitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(instance, "gitConfig", mock(IGitConfig.class));
	when(instance.gitConfig.getGitblitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(instance.gitConfig.getServiceUserID()).thenReturn("");
	when(instance.gitConfig.getServiceSecret()).thenReturn("");
	// instance.setPermissionForGitRepository(pRepoName, pDeveloperId, pPermission);

    }

    /**
     * Test of freezeRepository method, of class GitBlitClientUtils.
     */
    // @Test
    // public void testFreezeRepository() throws Exception {
    //
    // String pRepoName = "";
    // GitBlitClientUtils instance = spy(new
    // GitBlitClientUtils(mock(IGitConfig.class)));
    // ReflectionTestUtils.setField(instance, "gitConfig", mock(IGitConfig.class));
    // when(instance.gitConfig.getGitblitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/repositories/");
    // when(instance.gitConfig.getServiceUserID()).thenReturn("");
    // when(instance.gitConfig.getServiceSecret()).thenReturn("");
    // Boolean expResult = null;
    // Boolean result = instance.freezeRepository(pRepoName);
    //
    //
    //
    // }
    /**
     * Test of unFreezeRepository method, of class GitBlitClientUtils.
     */

    // @Test
    // public void testUnFreezeRepository() throws Exception {
    //
    // String pRepoName = "";
    // GitBlitClientUtils instance = spy(new
    // GitBlitClientUtils(mock(IGitConfig.class)));
    // ReflectionTestUtils.setField(instance, "gitConfig", mock(IGitConfig.class));
    // when(instance.gitConfig.getGitblitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/repositories/");
    // when(instance.gitConfig.getServiceSecret()).thenReturn("");
    // when(instance.gitConfig.getServiceUserID()).thenReturn("");
    // Boolean expResult = null;
    // Boolean result = instance.unFreezeRepository(pRepoName);
    //
    //
    //
    // }
    /**
     * Test of getImplementationTicketURL method, of class GitBlitClientUtils.
     */
    @Test
    public void testGetImplementationTicketURL() throws Exception {

	String pCompany = "";
	String pImplPlanId = "";
	String pTicket = "";
	GitBlitClientUtils instance = spy(new GitBlitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(instance, "gitConfig", mock(IGitConfig.class));
	when(instance.gitConfig.getGitblitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(instance.gitConfig.getServiceUserID()).thenReturn("");

	String result = instance.getImplementationTicketURL(pCompany, pImplPlanId, pTicket);

    }

    /**
     * Test of createGitRepository method, of class GitBlitClientUtils.
     */
    @Test
    public void testdeleteGitRepository() throws Exception {

	String pRepoName = "";
	String pRepoDesc = "";
	GitBlitClientUtils instance = spy(new GitBlitClientUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(instance, "gitConfig", mock(IGitConfig.class));
	when(instance.gitConfig.getGitblitRestUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/gitblit/");
	when(instance.gitConfig.getServiceUserID()).thenReturn("");
	when(instance.gitConfig.getServiceSecret()).thenReturn("");
	List<String> pOwners = new ArrayList();
	pOwners.add("test");
	Boolean result = instance.deleteGitRepository(pRepoName);

    }

}
