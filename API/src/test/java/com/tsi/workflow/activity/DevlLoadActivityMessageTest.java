/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class DevlLoadActivityMessageTest {

    public DevlLoadActivityMessageTest() {
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
     * Test of getStatus method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testGetStatus() {

	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(null, null);
	String expResult = null;
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of setOldrType method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testSetOldrType() {

	String oldrType = "";
	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(null, null);
	instance.setOldrType(oldrType);
	assertNotNull(instance.oldrType);
    }

    /**
     * Test of setResult method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testSetResult() {

	boolean result_2 = false;
	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(null, null);
	instance.setResult(result_2);
	assertFalse(instance.result);
    }

    /**
     * Test of setStatus method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testSetStatus() {

	String lStatus = "";
	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(null, null);
	instance.setStatus(lStatus);
	assertNotNull(instance.status);
    }

    /**
     * Test of processMessage method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(DataWareHouse.getPlan(), null);
	instance.setUser(DataWareHouse.getUserWithDelagated());
	instance.setArguments((IBeans) DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	instance.setStatus("");
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {

	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(DataWareHouse.getPlan(), null);
	instance.setUser(DataWareHouse.getUserWithDelagated());
	instance.setArguments((IBeans) DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	instance.setStatus("");
	instance.setOldrType("");
	instance.setErrorMessage("");
	assertEquals("", instance.getErrorMessage());
	String result = instance.processMessage();
	assertNotNull(result);
	instance.setStatus("failed");
	result = instance.processMessage();
	assertEquals("failed", instance.status);

    }

    @Test
    public void testProcessMessage2() {
	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(DataWareHouse.getPlan(), null);
	instance.setArguments((IBeans) DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	instance.setStatus("failed");
	DataWareHouse.getUser().setCurrentDelegatedUser(null);
	instance.setUser(DataWareHouse.getUser());
	instance.processMessage();
    }

    /**
     * Test of getLogLevel method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(null, null);
	instance.setStatus("pass");
	Priority result = instance.getLogLevel();
	assertNotNull(result);
	instance.setStatus("failed");
	result = instance.getLogLevel();
	assertNotNull(result);
    }

    /**
     * Test of setArguments method, of class DevlLoadActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = { new com.tsi.workflow.beans.dao.System() };
	DevlLoadActivityMessage instance = new DevlLoadActivityMessage(null, null);
	instance.setArguments(beans);
	assertTrue(true);
    }

}
