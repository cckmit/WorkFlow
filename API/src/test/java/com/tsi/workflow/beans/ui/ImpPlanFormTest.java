/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.tsi.workflow.beans.dao.ImpPlan;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class ImpPlanFormTest {

    public ImpPlanFormTest() {
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

    @Test
    public void testConstructor() {
	ImpPlanForm instance = new ImpPlanForm(null, null);
    }

    /**
     * Test of getImpPlan method, of class ImpPlanForm.
     */
    @Test
    public void testGetImpPlan() {

	ImpPlanForm instance = new ImpPlanForm();
	ImpPlan expResult = null;
	ImpPlan result = instance.getImpPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of setImpPlan method, of class ImpPlanForm.
     */
    @Test
    public void testSetImpPlan() {

	ImpPlan impPlan = null;
	ImpPlanForm instance = new ImpPlanForm();
	instance.setImpPlan(impPlan);
	assertNull(instance.getImpPlan());
    }

    /**
     * Test of getBlockedSystems method, of class ImpPlanForm.
     */
    @Test
    public void testGetBlockedSystems() {

	ImpPlanForm instance = new ImpPlanForm();
	Set<String> expResult = null;
	Set<String> result = instance.getBlockedSystems();
	assertEquals(expResult, result);
    }

    /**
     * Test of setBlockedSystems method, of class ImpPlanForm.
     */
    @Test
    public void testSetBlockedSystems() {

	Set<String> blockedSystems = null;
	ImpPlanForm instance = new ImpPlanForm();
	instance.setBlockedSystems(blockedSystems);
	assertNull(instance.getBlockedSystems());
    }

}
