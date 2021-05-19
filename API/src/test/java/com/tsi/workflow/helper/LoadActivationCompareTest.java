/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class LoadActivationCompareTest {

    public LoadActivationCompareTest() {
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
     * Test of compare method, of class LoadActivationCompare.
     */
    @Test
    public void testCompare() {
	SystemLoadActions o1 = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	SystemLoadActions o2 = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	LoadActivationCompare instance = new LoadActivationCompare(true);
	int expResult = 0;
	int result = instance.compare(o1, o2);
	assertEquals(expResult, result);
    }

    /**
     * Test of compare method, of class LoadActivationCompare.
     */
    @Test
    public void testCompare2() {
	SystemLoadActions o1 = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	SystemLoadActions o2 = DataWareHouse.getPlan().getSystemLoadActionsList().get(1);
	LoadActivationCompare instance = new LoadActivationCompare(false);
	int expResult = 0;
	int result = instance.compare(o1, o2);
	assertEquals(expResult, result);
    }

}
