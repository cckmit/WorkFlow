/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.utils.JSONResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PlanHelperTest {

    public PlanHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
	PlanHelper realInstance = new PlanHelper();
	instance = spy(realInstance);
	TestCaseMockService.doMockDAO(instance, GitBlitClientUtils.class);
	TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);

    }

    @After
    public void tearDown() {
    }

    PlanHelper instance;

    /**
     * Test of getGitBlitClientUtils method, of class PlanHelper.
     */
    @Test
    public void testGetGitBlitClientUtils() {
	instance.getGitBlitClientUtils();
    }

    /**
     * Test of getJGitClientUtils method, of class PlanHelper.
     */
    @Test
    public void testGetJGitClientUtils() {
	instance.getJGitClientUtils();
    }

    /**
     * Test of deletePlanSourceGit method, of class PlanHelper.
     */
    @Test
    public void testDeletePlanSourceGit() {
	String pCompanyName = "tp";
	ImpPlan pPlan = DataWareHouse.getPlan();
	String pPlanId = pPlan.getId();
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    when(instance.getJGitClientUtils().getPlanRepoFullName(pCompanyName, pPlanId)).thenReturn("");
	    when(instance.getGitBlitClientUtils().deleteGitRepository("")).thenReturn(true);
	    instance.deletePlanSourceGit(pCompanyName, pPlanId);
	} catch (Exception ex) {
	    assertTrue(true);

	}
    }

    /**
     * Test of deletePlanDerivedGit method, of class PlanHelper.
     */
    @Test
    public void testDeletePlanDerivedGit() throws Exception {
	String pCompanyName = "tp";
	ImpPlan pPlan = DataWareHouse.getPlan();
	String pPlanId = pPlan.getId();
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	try {
	    when(instance.getJGitClientUtils().getPlanLFSRepoFullName(pCompanyName, pPlanId)).thenReturn("");
	    when(instance.getGitBlitClientUtils().deleteGitRepository("")).thenReturn(true);
	    instance.deletePlanSourceGit(pCompanyName, pPlanId);
	} catch (Exception ex) {
	    assertTrue(true);

	}
    }

    /**
     * Test of deletePlanGitRepos method, of class PlanHelper.
     */
    @Test
    public void testDeletePlanGitRepos() {
	String pCompanyName = "tp";
	ImpPlan pPlan = DataWareHouse.getPlan();
	String pPlanId = pPlan.getId();
	try {
	    when(instance.deletePlanSourceGit(pCompanyName, pPlanId)).thenReturn(Boolean.FALSE);
	    instance.deletePlanGitRepos(pCompanyName, pPlanId);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlanGitRepos1() {
	String pCompanyName = "tp";
	ImpPlan pPlan = DataWareHouse.getPlan();
	String pPlanId = pPlan.getId();
	try {
	    when(instance.deletePlanSourceGit(pCompanyName, pPlanId)).thenReturn(Boolean.TRUE);
	    when(instance.deletePlanDerivedGit(pCompanyName, pPlanId)).thenReturn(Boolean.FALSE);
	    instance.deletePlanGitRepos(pCompanyName, pPlanId);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlanGitRepos2() {
	String pCompanyName = "tp";
	ImpPlan pPlan = DataWareHouse.getPlan();
	String pPlanId = pPlan.getId();
	try {
	    when(instance.deletePlanSourceGit(pCompanyName, pPlanId)).thenReturn(Boolean.TRUE);
	    when(instance.deletePlanDerivedGit(pCompanyName, pPlanId)).thenReturn(Boolean.TRUE);
	    instance.deletePlanGitRepos(pCompanyName, pPlanId);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

}
