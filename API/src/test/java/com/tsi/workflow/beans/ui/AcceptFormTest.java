/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.tsi.workflow.beans.dao.ProductionLoads;
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
public class AcceptFormTest {

    public AcceptFormTest() {
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
     * Test of getIsAcceptInProgress method, of class AcceptForm.
     */
    @Test
    public void testGetIsAcceptInProgress() {

	AcceptForm instance = new AcceptForm();
	instance.setIsAcceptInProgress(Boolean.FALSE);
	Boolean expResult = null;
	Boolean result = instance.getIsAcceptInProgress();

	assertEquals(false, result);

    }

    /**
     * Test of setIsAcceptInProgress method, of class AcceptForm.
     */
    @Test
    public void testSetIsAcceptInProgress() {

	Boolean isAcceptInProgress = null;
	AcceptForm instance = new AcceptForm();
	instance.setIsAcceptInProgress(isAcceptInProgress);
	assertNull(instance.getIsAcceptInProgress());

    }

    /**
     * Test of getProductionLoadsList method, of class AcceptForm.
     */
    @Test
    public void testGetProductionLoadsList() {

	AcceptForm instance = new AcceptForm(Boolean.TRUE, null);

	List<ProductionLoads> result = instance.getProductionLoadsList();
	assertNull(result);

    }

    /**
     * Test of setProductionLoadsList method, of class AcceptForm.
     */
    @Test
    public void testSetProductionLoadsList() {

	List<ProductionLoads> productionLoadsList = null;
	AcceptForm instance = new AcceptForm();
	instance.setProductionLoadsList(productionLoadsList);
	assertNull(instance.getProductionLoadsList());

    }

}
