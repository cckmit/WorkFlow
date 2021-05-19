/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import java.util.Arrays;
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
public class StgLoadActivityMessageTest {

    public StgLoadActivityMessageTest() {
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
     * Test of getStatus method, of class StgLoadActivityMessage.
     */
    @Test
    public void testGetStatus() {

	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	String expResult = null;
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of setOldrType method, of class StgLoadActivityMessage.
     */
    @Test
    public void testSetOldrType() {

	String oldrType = "";
	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	instance.setOldrType(oldrType);
    }

    /**
     * Test of setResult method, of class StgLoadActivityMessage.
     */
    @Test
    public void testSetResult() {

	boolean result_2 = false;
	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	instance.setResult(result_2);
    }

    /**
     * Test of setStatus method, of class StgLoadActivityMessage.
     */
    @Test
    public void testSetStatus() {

	String lStatus = "";
	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	instance.setStatus(lStatus);
    }

    /**
     * Test of processMessage method, of class StgLoadActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	StgLoadActivityMessage instance = new StgLoadActivityMessage(DataWareHouse.getPlan(), null);
	String expResult = "";
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	instance.setUser(user);
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {

	StgLoadActivityMessage instance = new StgLoadActivityMessage(DataWareHouse.getPlan(), null);
	String expResult = "";
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	instance.setUser(user);
	instance.setOldrType(expResult);
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of getLogLevel method, of class StgLoadActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	instance.setStatus("pass");
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetLogLevel1() {

	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	Priority expResult = Priority.ERROR;
	instance.setStatus("failed");
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    /**
     * Test of setArguments method, of class StgLoadActivityMessage.
     */
    @Test
    public void testSetArguments() {

	StgLoadActivityMessage instance = new StgLoadActivityMessage(null, null);
	instance.setArguments((IBeans[]) Arrays.asList(new com.tsi.workflow.beans.dao.System()).toArray());
    }

}
