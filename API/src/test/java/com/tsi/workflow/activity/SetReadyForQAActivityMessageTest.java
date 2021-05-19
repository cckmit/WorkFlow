/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

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
 * @author USER
 */
public class SetReadyForQAActivityMessageTest {

    SetReadyForQAActivityMessage instance;

    public SetReadyForQAActivityMessageTest() {
	instance = new SetReadyForQAActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {
	instance.setUser(DataWareHouse.getUserWithDelagated());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {
	User usr = DataWareHouse.getUserWithDelagated();
	usr.setCurrentDelegatedUser(null);
	instance.setUser(usr);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of setArguments method, of class SetReadyForQAActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	instance.setArguments(beans);
    }

}
