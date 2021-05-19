/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class PreProductionLoadsFormTest {

    public PreProductionLoadsFormTest() {
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
     * Test of getPreProductionLoadsList method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetPreProductionLoadsList() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	List<PreProductionLoads> expResult = null;
	List<PreProductionLoads> result = instance.getPreProductionLoadsList();
	assertEquals(expResult, result);
    }

    /**
     * Test of setPreProductionLoadsList method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetPreProductionLoadsList() {

	List<PreProductionLoads> preProductionLoadsList = null;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setPreProductionLoadsList(preProductionLoadsList);
	assertNull(instance.getPreProductionLoadsList());
    }

    /**
     * Test of getShowAddActivateButton method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetShowAddActivateButton() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	Boolean expResult = false;
	Boolean result = instance.getShowAddActivateButton();
	assertEquals(expResult, result);
    }

    /**
     * Test of setShowAddActivateButton method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetShowAddActivateButton() {

	Boolean showAddActivateButton = Boolean.TRUE;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setShowAddActivateButton(showAddActivateButton);
	assertEquals(true, instance.getShowAddActivateButton());

    }

    /**
     * Test of getShowAddDeActivateButton method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetShowAddDeActivateButton() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	Boolean expResult = false;
	Boolean result = instance.getShowAddDeActivateButton();
	assertEquals(expResult, result);
    }

    /**
     * Test of setShowAddDeActivateButton method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetShowAddDeActivateButton() {

	Boolean showAddDeActivateButton = Boolean.FALSE;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setShowAddDeActivateButton(showAddDeActivateButton);
	assertEquals(false, instance.getShowAddDeActivateButton());
    }

    /**
     * * Test of getIsMultipleCPUAllowed method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetIsMultipleCPUAllowed() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	Boolean expResult = false;
	Boolean result = instance.getIsMultipleCPUAllowed();
	assertEquals(expResult, result);
    }

    /**
     * Test of setIsMultipleCPUAllowed method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetIsMultipleCPUAllowed() {

	Boolean isMultipleCPUAllowed = Boolean.FALSE;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setIsMultipleCPUAllowed(isMultipleCPUAllowed);
	assertEquals(false, instance.getIsMultipleCPUAllowed());

    }

    /**
     * Test of getPlan method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetPlan() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	ImpPlan expResult = null;
	ImpPlan result = instance.getPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of setPlan method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetPlan() {

	ImpPlan plan = null;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setPlan(plan);
	assertNull(instance.getPlan());
    }

    /**
     * Test of getActivationSystemCpusList method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetActivationSystemCpusList() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	List<SystemCpu> expResult = null;
	List<SystemCpu> result = instance.getActivationSystemCpusList();
	assertNotNull(result);
    }

    /**
     * Test of setActivationSystemCpusList method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetActivationSystemCpusList() {

	List<SystemCpu> activationSystemCpusList = null;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setActivationSystemCpusList(activationSystemCpusList);
	assertNull(instance.getActivationSystemCpusList());
    }

    /**
     * Test of getDeActivationSystemCpusList method, of class
     * PreProductionLoadsForm.
     */
    @Test
    public void testGetDeActivationSystemCpusList() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	List<SystemCpu> expResult = null;
	List<SystemCpu> result = instance.getDeActivationSystemCpusList();
	assertNotNull(result);
    }

    /**
     * Test of setDeActivationSystemCpusList method, of class
     * PreProductionLoadsForm.
     */
    @Test
    public void testSetDeActivationSystemCpusList() {

	List<SystemCpu> deActivationSystemCpusList = null;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setDeActivationSystemCpusList(deActivationSystemCpusList);
	assertNull(instance.getDeActivationSystemCpusList());
    }

    /**
     * Test of getIsAnyLoadsInProgress method, of class PreProductionLoadsForm.
     */
    @Test
    public void testGetIsAnyLoadsInProgress() {

	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	Boolean expResult = null;
	Boolean result = instance.getIsAnyLoadsInProgress();
	assertEquals(expResult, result);
    }

    /**
     * Test of setIsAnyLoadsInProgress method, of class PreProductionLoadsForm.
     */
    @Test
    public void testSetIsAnyLoadsInProgress() {

	Boolean isAnyLoadsInProgress = Boolean.FALSE;
	PreProductionLoadsForm instance = new PreProductionLoadsForm();
	instance.setIsAnyLoadsInProgress(isAnyLoadsInProgress);
	assertEquals(false, instance.getIsAnyLoadsInProgress());
    }

}
