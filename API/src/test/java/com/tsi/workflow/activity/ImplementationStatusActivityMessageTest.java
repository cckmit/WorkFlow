/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;

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
public class ImplementationStatusActivityMessageTest {

    public ImplementationStatusActivityMessageTest() {
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
     * Test of setStatus method, of class ImplementationStatusActivityMessage.
     */
    @Test
    public void testSetStatus() {

	String status = "";
	ImplementationStatusActivityMessage instance = new ImplementationStatusActivityMessage(null, null);
	instance.setStatus(status);

    }

    /**
     * Test of processMessage method, of class ImplementationStatusActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	ImplementationStatusActivityMessage instance = new ImplementationStatusActivityMessage(null, DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
	String result = instance.processMessage();
	assertNotNull(result);

    }

    /**
     * Test of getLogLevel method, of class ImplementationStatusActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	ImplementationStatusActivityMessage instance = new ImplementationStatusActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertNotNull(result);

    }

    /**
     * Test of setArguments method, of class ImplementationStatusActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	ImplementationStatusActivityMessage instance = new ImplementationStatusActivityMessage(null, null);
	instance.setArguments(beans);

    }

}
