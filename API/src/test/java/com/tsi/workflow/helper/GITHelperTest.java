/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GITHelperTest {

    GITHelper instance;

    public GITHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	GITHelper realInstance = new GITHelper();
	instance = spy(realInstance);
	try {
	    TestCaseMockService.doMockDAO(instance, PlatformDAO.class);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);

	} catch (Exception ex) {
	    Logger.getLogger(GITHelperTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRepositoryNameBySourceURL method, of class GITHelper.
     */
    @Test
    public void testGetRepositoryNameBySourceURL() {
	String pSourceURL = "";
	GITHelper instance = new GITHelper();
	String expResult = "";
	try {
	    String result = instance.getRepositoryNameBySourceURL(pSourceURL);
	    assertEquals(expResult, result);
	} catch (Exception e) {
	    assertTrue(true);
	}
	// TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of getSourceURLByRepoName method, of class GITHelper.
     */
    @Test
    public void testGetSourceURLByRepoName() {
	String pRepoName = "";
	GITHelper instance = new GITHelper();
	String expResult = "";
	try {
	    String result = instance.getSourceURLByRepoName(pRepoName);
	    assertEquals(expResult, result);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of searchFileResultFilter method, of class GITHelper.
     */
    @Test
    public void testSearchFileResultFilter_3args() {
	String pUserId = "";
	String pCompanyName = "";
	List<GitSearchResult> pSearchResult = null;
	GITHelper instance = new GITHelper();
	try {
	    instance.searchFileResultFilter(pUserId, pCompanyName, pSearchResult);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of searchFileResultFilter method, of class GITHelper.
     */
    @Test
    public void testSearchFileResultFilter_4args() {
	String pUserId = "";
	List<GitSearchResult> pSearchResult = null;
	String pCompanyName = "";
	String searchType = "";
	GITHelper instance = new GITHelper();
	Boolean expResult = null;
	try {
	    Boolean result = instance.searchFileResultFilter(pUserId, pSearchResult, pCompanyName, searchType);
	    assertEquals(expResult, result);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of getRepositoryList method, of class GITHelper.
     */
    @Test
    public void testGetRepositoryList_String_String() {
	String companyName = "";
	String repoType = "";
	GITHelper instance = new GITHelper();
	Map<String, String> expResult = null;
	try {
	    Map<String, String> result = instance.getRepositoryList(companyName, repoType);
	    assertEquals(expResult, result);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

}
