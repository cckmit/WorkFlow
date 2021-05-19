/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author yeshwanth.shenoy
 */
public class LoadSetUtilsTest {

    public LoadSetUtilsTest() {
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
     * Test of getLoadSetName method, of class LoadSetUtils.
     */
    @Test
    public void testGetLoadSetName() {

	ImpPlan pPlan = DataWareHouse.getPlan();
	SystemLoad pSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	String result = LoadSetUtils.getLoadSetName(pPlan, pSystemLoad);
	assertNotNull(result);
    }

    /**
     * Test of getFallbackLoadSetName method, of class LoadSetUtils.
     */
    @Test
    public void testGetFallbackLoadSetName() {

	LoadSetUtils lLoadSetUtils = new LoadSetUtils();

	ImpPlan pPlan = DataWareHouse.getPlan();
	SystemLoad pSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	String result = LoadSetUtils.getFallbackLoadSetName(pPlan, pSystemLoad);
	assertNotNull(result);
    }

}
