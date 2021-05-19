/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
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
public class PlanStatusActivityMessageTest {

    public PlanStatusActivityMessageTest() {
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
     * Test of setStatus method, of class PlanStatusActivityMessage.
     */
    @Test
    public void testSetStatus() {

	String status = "";
	PlanStatusActivityMessage instance = new PlanStatusActivityMessage(null, null);
	instance.setStatus(status);
    }

    /**
     * Test of getLogLevel method, of class PlanStatusActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	PlanStatusActivityMessage instance = new PlanStatusActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    /**
     * Test of setArguments method, of class PlanStatusActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	PlanStatusActivityMessage instance = new PlanStatusActivityMessage(null, null);
	instance.setArguments(beans);
	assertTrue(true);
    }

    /**
     * Test of processMessage method, of class PlanStatusActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	PlanStatusActivityMessage instance = new PlanStatusActivityMessage(DataWareHouse.getPlan(), null);
	String expResult = "";
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	instance.setUser(user);
	String result = instance.processMessage();
	assertNotNull(result);
	instance.setReason("123");
	String result1 = instance.processMessage();
	assertNotNull(result1);
    }

    @Test
    public void testProcessMessage1() {

	PlanStatusActivityMessage instance = new PlanStatusActivityMessage(DataWareHouse.getPlan(), null);
	String expResult = "";
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUser(user);
	String result = instance.processMessage();
	assertNotNull(result);
	instance.setReason("123");
	String result1 = instance.processMessage();
	assertNotNull(result1);
    }

}
