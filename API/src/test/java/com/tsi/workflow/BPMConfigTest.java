/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class BPMConfigTest {

    public BPMConfigTest() {
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
     * Test of getBpmRestUrl method, of class BPMConfig.
     */
    @Test
    public void testGetBpmRestUrl() {

	BPMConfig instance = new BPMConfig();
	String expResult = "1";
	instance.setBpmRestUrl(expResult);
	String result = instance.getBpmRestUrl();
	assertEquals(expResult, result);
    }

    /**
     * Test of getAdlProcessKey method, of class BPMConfig.
     */
    @Test
    public void testGetAdlProcessKey() {

	BPMConfig instance = new BPMConfig();
	String expResult = "1";
	instance.setAdlProcessKey(expResult);
	String result = instance.getAdlProcessKey();
	assertEquals(expResult, result);
    }

    /**
     * Test of getDlProcessKey method, of class BPMConfig.
     */
    @Test
    public void testGetDlProcessKey() {

	BPMConfig instance = new BPMConfig();
	String expResult = "1";
	instance.setDlProcessKey(expResult);
	String result = instance.getDlProcessKey();
	assertEquals(expResult, result);
    }

}
