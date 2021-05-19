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
public class ImplementationStatusRevertActivityMessageTest {

    public ImplementationStatusRevertActivityMessageTest() {
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
     * Test of setStatus method, of class ImplementationStatusRevertActivityMessage.
     */
    @Test
    public void testSetStatus() {

	String status = "";
	ImplementationStatusRevertActivityMessage instance = new ImplementationStatusRevertActivityMessage(null, null);
	instance.setStatus(status);
    }

    /**
     * Test of processMessage method, of class
     * ImplementationStatusRevertActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	ImplementationStatusRevertActivityMessage instance = new ImplementationStatusRevertActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	String expResult = "";
	User usr = DataWareHouse.getUserWithDelagated();
	usr.setCurrentDelegatedUser(usr);
	instance.setUser(usr);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {

	ImplementationStatusRevertActivityMessage instance = new ImplementationStatusRevertActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	String expResult = "";
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUser(user);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of getLogLevel method, of class
     * ImplementationStatusRevertActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	ImplementationStatusRevertActivityMessage instance = new ImplementationStatusRevertActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    /**
     * Test of setArguments method, of class
     * ImplementationStatusRevertActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	ImplementationStatusRevertActivityMessage instance = new ImplementationStatusRevertActivityMessage(null, null);
	instance.setArguments(beans);
    }

}
