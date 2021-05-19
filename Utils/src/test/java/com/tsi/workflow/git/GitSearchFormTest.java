/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class GitSearchFormTest {

    public GitSearchFormTest() {
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
     * Test of getCompanyName method, of class GitSearchForm.
     */
    @Test
    public void testGetCompanyName() {
	GitSearchForm instance = new GitSearchForm();
	Set<String> expResult = new HashSet();
	instance.setCompanyName(expResult);
	Set<String> result = instance.getCompanyName();
	assertEquals(expResult, result);
    }

    /**
     * Test of getFilter method, of class GitSearchForm.
     */
    @Test
    public void testGetFilter() {
	GitSearchForm instance = new GitSearchForm();
	String expResult = "";
	instance.setFilter(expResult);
	String result = instance.getFilter();
	assertEquals(expResult, result);
    }

    /**
     * Test of getMacroHeader method, of class GitSearchForm.
     */
    @Test
    public void testGetMacroHeader() {
	GitSearchForm instance = new GitSearchForm();
	Boolean expResult = Boolean.TRUE;
	instance.setMacroHeader(expResult);
	Boolean result = instance.getMacroHeader();
	assertEquals(expResult, result);
    }

    /**
     * Test of getPendingStatusReq method, of class GitSearchForm.
     */
    @Test
    public void testGetPendingStatusReq() {
	GitSearchForm instance = new GitSearchForm();
	Boolean expResult = Boolean.TRUE;
	instance.setPendingStatusReq(expResult);
	Boolean result = instance.getPendingStatusReq();
	assertEquals(expResult, result);
    }

    /**
     * Test of getRepoType method, of class GitSearchForm.
     */
    @Test
    public void testGetRepoType() {
	GitSearchForm instance = new GitSearchForm();
	String expResult = "";
	instance.setRepoType(expResult);
	String result = instance.getRepoType();
	assertEquals(expResult, result);
    }

    /**
     * Test of getBranchName method, of class GitSearchForm.
     */
    @Test
    public void testGetBranchName() {
	GitSearchForm instance = new GitSearchForm();
	String expResult = "";
	instance.setBranchName(expResult);
	String result = instance.getBranchName();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSearchType method, of class GitSearchForm.
     */
    @Test
    public void testGetSearchType() {
	GitSearchForm instance = new GitSearchForm();
	String expResult = "";
	instance.setSearchType(expResult);
	String result = instance.getSearchType();
	assertEquals(expResult, result);
    }

}
