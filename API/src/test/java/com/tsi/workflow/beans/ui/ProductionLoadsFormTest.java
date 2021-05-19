/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ProductionLoadsFormTest {

    public ProductionLoadsFormTest() {
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
     * Test of getProductionLoadsList method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetProductionLoadsList() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	List<ProductionLoads> expResult = new ArrayList();
	instance.setProductionLoadsList(expResult);
	List<ProductionLoads> result = instance.getProductionLoadsList();
	assertEquals(expResult, result);
    }

    /**
     * Test of getShowAddActivateButton method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetShowAddActivateButton() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = true;
	instance.setShowAddActivateButton(expResult);
	Boolean result = instance.getShowAddActivateButton();
	assertEquals(expResult, result);
    }

    /**
     * Test of getShowAddDeActivateButton method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetShowAddDeActivateButton() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = true;
	instance.setShowAddDeActivateButton(expResult);
	Boolean result = instance.getShowAddDeActivateButton();
	assertEquals(expResult, result);
    }

    /**
     * Test of getIsMultipleCPUAllowed method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetIsMultipleCPUAllowed() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = true;
	instance.setIsMultipleCPUAllowed(expResult);
	Boolean result = instance.getIsMultipleCPUAllowed();
	assertEquals(expResult, result);
    }

    /**
     * Test of getPlan method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetPlan() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	ImpPlan expResult = new ImpPlan("T1700123");
	instance.setPlan(expResult);
	ImpPlan result = instance.getPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of getActivationSystemCpusList method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetActivationSystemCpusList() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	List<SystemCpu> expResult = new ArrayList();
	instance.setActivationSystemCpusList(expResult);
	List<SystemCpu> result = instance.getActivationSystemCpusList();
	assertEquals(expResult, result);
    }

    /**
     * Test of getDeActivationSystemCpusList method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetDeActivationSystemCpusList() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	List<SystemCpu> expResult = new ArrayList();
	instance.setDeActivationSystemCpusList(expResult);
	List<SystemCpu> result = instance.getDeActivationSystemCpusList();
	assertEquals(expResult, result);
    }

    /**
     * Test of getIsAnyLoadsInProgress method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetIsAnyLoadsInProgress() {

	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = true;
	instance.setIsAnyLoadsInProgress(expResult);
	Boolean result = instance.getIsAnyLoadsInProgress();
	assertEquals(expResult, result);
    }

    /**
     * Test of getIsAnyLoadsDeleted method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetIsAnyLoadsDeleted() {
	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = null;
	Boolean result = instance.getIsAnyLoadsDeleted();
	assertEquals(expResult, result);

    }

    /**
     * Test of setIsAnyLoadsDeleted method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetIsAnyLoadsDeleted() {
	Boolean isAnyLoadsDeleted = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setIsAnyLoadsDeleted(isAnyLoadsDeleted);

    }

    /**
     * Test of setProductionLoadsList method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetProductionLoadsList() {
	List<ProductionLoads> productionLoadsList = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setProductionLoadsList(productionLoadsList);

    }

    /**
     * Test of setShowAddActivateButton method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetShowAddActivateButton() {
	Boolean showAddActivateButton = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setShowAddActivateButton(showAddActivateButton);

    }

    /**
     * Test of setShowAddDeActivateButton method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetShowAddDeActivateButton() {
	Boolean showAddDeActivateButton = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setShowAddDeActivateButton(showAddDeActivateButton);

    }

    /**
     * Test of setIsMultipleCPUAllowed method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetIsMultipleCPUAllowed() {
	Boolean isMultipleCPUAllowed = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setIsMultipleCPUAllowed(isMultipleCPUAllowed);

    }

    /**
     * Test of setPlan method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetPlan() {
	ImpPlan plan = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setPlan(plan);

    }

    /**
     * Test of setActivationSystemCpusList method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetActivationSystemCpusList() {
	List<SystemCpu> activationSystemCpusList = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setActivationSystemCpusList(activationSystemCpusList);

    }

    /**
     * Test of setDeActivationSystemCpusList method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetDeActivationSystemCpusList() {
	List<SystemCpu> deActivationSystemCpusList = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setDeActivationSystemCpusList(deActivationSystemCpusList);

    }

    /**
     * Test of setIsAnyLoadsInProgress method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetIsAnyLoadsInProgress() {
	Boolean isAnyLoadsInProgress = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setIsAnyLoadsInProgress(isAnyLoadsInProgress);

    }

    /**
     * Test of getSelectDeActivateAll method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetSelectDeActivateAll() {
	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = null;
	Boolean result = instance.getSelectDeActivateAll();
    }

    /**
     * Test of setSelectDeActivateAll method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetSelectDeActivateAll() {
	Boolean selectDeActivateAll = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setSelectDeActivateAll(selectDeActivateAll);

    }

    /**
     * Test of getSelectActivateAll method, of class ProductionLoadsForm.
     */
    @Test
    public void testGetSelectActivateAll() {
	ProductionLoadsForm instance = new ProductionLoadsForm();
	Boolean expResult = null;
	Boolean result = instance.getSelectActivateAll();
    }

    /**
     * Test of setSelectActivateAll method, of class ProductionLoadsForm.
     */
    @Test
    public void testSetSelectActivateAll() {
	Boolean selectActivateAll = null;
	ProductionLoadsForm instance = new ProductionLoadsForm();
	instance.setSelectActivateAll(selectActivateAll);
    }

}
