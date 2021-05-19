/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.tsi.workflow.beans.dao.ImpPlan;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author User
 */
public class ImpPlanViewTest {

    public ImpPlanViewTest() {
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
     * Test of getPlan method, of class ImpPlanView.
     */
    @Test
    public void testGetPlan() {
	ImpPlanView instance = new ImpPlanView();
	ImpPlan lPlan = new ImpPlan();
	ImpPlan expResult = lPlan;
	instance.setPlan(lPlan);
	ImpPlan result = instance.getPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of setPlan method, of class ImpPlanView.
     */
    // @Test
    public void testSetPlan() {
	ImpPlan plan = null;
	ImpPlanView instance = new ImpPlanView();
	instance.setPlan(plan);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    /**
     * Test of getIsSubmitReady method, of class ImpPlanView.
     */
    @Test
    public void testGetIsSubmitReady() {
	ImpPlanView instance = new ImpPlanView();
	instance.setIsSubmitReady(Boolean.TRUE);
	Boolean expResult = Boolean.TRUE;
	Boolean result = instance.getIsSubmitReady();
	assertEquals(expResult, result);
    }

    /**
     * Test of setIsSubmitReady method, of class ImpPlanView.
     */
    // @Test
    public void testSetIsSubmitReady() {
	Boolean isSubmitReady = null;
	ImpPlanView instance = new ImpPlanView();
	instance.setIsSubmitReady(isSubmitReady);
	// TODO review the generated test code and remove the default call to fail.
	fail("The test case is a prototype.");
    }

    @Test
    public void testSetIsSubmitProgress() {
	ImpPlanView instance = new ImpPlanView();
	instance.setIsInProgress("INPROGRESS");
	instance.getIsInProgress();
    }

    @Test
    public void testGetIsDeleteAllowed() {
	ImpPlanView instance = new ImpPlanView();
	instance.setIsDeleteAllowed(Boolean.FALSE);
	instance.getIsDeleteAllowed();
    }

    @Test
    public void testGetImpDeleteStatus() {
	ImpPlanView instance = new ImpPlanView();
	instance.setImpDeleteStatus(new HashMap());
	instance.getImpDeleteStatus();
    }
}
