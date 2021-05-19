/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
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
public class ProdDeployTest {

    public ProdDeployTest() {
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
     * Test of getPlans method, of class ProdDeploy.
     */
    @Test
    public void testGetPlans() {

	ProdDeploy instance = new ProdDeploy();
	List<String> expResult = new ArrayList();
	instance.setPlans(expResult);
	List<String> result = instance.getPlans();
	assertEquals(expResult, result);
    }

    /**
     * Test of getIps method, of class ProdDeploy.
     */
    @Test
    public void testGetIps() {

	ProdDeploy instance = new ProdDeploy();
	HashMap<Integer, String> expResult = new HashMap();
	instance.setIps(expResult);
	HashMap<Integer, String> result = instance.getIps();
	assertEquals(expResult, result);
    }

}
