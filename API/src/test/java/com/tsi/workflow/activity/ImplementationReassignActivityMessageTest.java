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
public class ImplementationReassignActivityMessageTest {

    public ImplementationReassignActivityMessageTest() {
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
     * Test of setOldUser method, of class ImplementationReassignActivityMessage.
     */
    @Test
    public void testSetOldUser() {

	String oldUser = "";
	ImplementationReassignActivityMessage instance = new ImplementationReassignActivityMessage(null, null);
	instance.setOldUser(oldUser);
    }

    /**
     * Test of processMessage method, of class
     * ImplementationReassignActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	User usr = DataWareHouse.getUser();
	usr.setCurrentDelegatedUser(usr);
	ImplementationReassignActivityMessage instance = new ImplementationReassignActivityMessage(null, DataWareHouse.getPlan().getImplementationList().get(0));
	String expResult = "";
	instance.setUser(usr);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of getLogLevel method, of class ImplementationReassignActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	ImplementationReassignActivityMessage instance = new ImplementationReassignActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    @Test
    public void testProcessMessage2() {
	User usr = DataWareHouse.getUser();
	usr.setCurrentDelegatedUser(null);
	ImplementationReassignActivityMessage instance = new ImplementationReassignActivityMessage(null, DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(usr);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of setArguments method, of class ImplementationReassignActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	ImplementationReassignActivityMessage instance = new ImplementationReassignActivityMessage(null, null);
	instance.setArguments(beans);
    }

}
