/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ActivityLog;
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
public class ActivityLogMessageTest {

    public ActivityLogMessageTest() {
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
     * Test of setUser method, of class ActivityLogMessage.
     */
    @Test
    public void testSetUser() {

	User user = null;
	ActivityLogMessage instance = new ActivityLogMessageImpl();
	instance.setUser(user);
	assertNull(user);
    }

    /**
     * Test of getUser method, of class ActivityLogMessage.
     */
    @Test
    public void testGetUser() {

	ActivityLogMessage instance = new ActivityLogMessageImpl();
	User expResult = null;
	User result = instance.getUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class ActivityLogMessage.
     */
    @Test
    public void testProcessMessage() {

	ActivityLogMessage instance = new ActivityLogMessageImpl();
	String expResult = "";
	String result = instance.processMessage();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLogLevel method, of class ActivityLogMessage.
     */
    @Test
    public void testGetLogLevel() {

	ActivityLogMessage instance = new ActivityLogMessageImpl();
	Priority expResult = null;
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    /**
     * Test of setArguments method, of class ActivityLogMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	ActivityLogMessage instance = new ActivityLogMessageImpl();
	instance.setArguments(beans);
	assertNull(beans);
    }

    /**
     * Test of getActivityLog method, of class ActivityLogMessage.
     */
    @Test
    public void testGetActivityLog() throws Exception {

	ActivityLogMessage instance = new ActivityLogMessageImpl();
	ActivityLog result = instance.getActivityLog();
	assertEquals("DEBUG", result.getLogLevel());
    }

    public class ActivityLogMessageImpl extends ActivityLogMessage {

	public ActivityLogMessageImpl() {
	    super(null, null);
	}

	public String processMessage() {
	    return "";
	}

	public Priority getLogLevel() {
	    return Priority.DEBUG;
	}

	public void setArguments(IBeans[] beans) {
	}
    }

}
