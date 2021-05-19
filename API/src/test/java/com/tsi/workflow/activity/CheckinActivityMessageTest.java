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
import com.tsi.workflow.base.IBeans;
import java.util.regex.Pattern;
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
public class CheckinActivityMessageTest {

    public CheckinActivityMessageTest() {
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
     * Test of setComments method, of class CheckinActivityMessage.
     */
    @Test
    public void testSetComments() {

	String comments = "1";
	CheckinActivityMessage instance = new CheckinActivityMessage(null, null);
	instance.setComments(comments);
	assertEquals(comments, instance.comments);
    }

    /**
     * Test of setError method, of class CheckinActivityMessage.
     */
    @Test
    public void testSetError() {

	boolean error = false;
	CheckinActivityMessage instance = new CheckinActivityMessage(null, null);
	instance.setError(error);
	assertEquals(error, instance.error);
    }

    /**
     * Test of setArguments method, of class CheckinActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	CheckinActivityMessage instance = new CheckinActivityMessage(null, null);
	instance.setArguments(beans);
    }

    /**
     * Test of processMessage method, of class CheckinActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	CheckinActivityMessage instance = new CheckinActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
	instance.setError(false);
	String result = instance.processMessage();
	assertTrue(Pattern.matches(".* has checked-in Implementation - .*", result));
    }

    @Test
    public void testProcessMessage1() {

	CheckinActivityMessage instance = new CheckinActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
	instance.setError(true);
	String result = instance.processMessage();
	assertTrue(Pattern.matches(".* has initiated check-in for implementation.*", result));
	assertNotNull(result);
    }

    /**
     * Test of getLogLevel method, of class CheckinActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	CheckinActivityMessage instance = new CheckinActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetLogLevel1() {

	CheckinActivityMessage instance = new CheckinActivityMessage(null, null);
	Priority expResult = Priority.ERROR;
	instance.setError(true);
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

}
