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
import org.mockito.Mockito;

/**
 *
 * @author deepa.jayakumar
 */
public class LoadTypeChangesActivityMessageTest {

    public LoadTypeChangesActivityMessageTest() {
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
     * Test of setOldLoadType method, of class LoadTypeChangesActivityMessage.
     */
    @Test
    public void testSetOldLoadType() {

	String oldLoadType = "";
	LoadTypeChangesActivityMessage instance = new LoadTypeChangesActivityMessage(null, null);
	instance.setOldLoadType(oldLoadType);

    }

    /**
     * Test of processMessage method, of class LoadTypeChangesActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	LoadTypeChangesActivityMessage instance = new LoadTypeChangesActivityMessage(DataWareHouse.getPlan(), null);
	String expResult = "";
	instance.setOldLoadType("");
	instance.setUser(DataWareHouse.user);
	String result = instance.processMessage();
	User user = Mockito.mock(User.class);
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	assertNotNull(result);

    }

    /**
     * Test of getLogLevel method, of class LoadTypeChangesActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	LoadTypeChangesActivityMessage instance = new LoadTypeChangesActivityMessage(DataWareHouse.getPlan(), null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);

    }

    /**
     * Test of setArguments method, of class LoadTypeChangesActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	LoadTypeChangesActivityMessage instance = new LoadTypeChangesActivityMessage(null, null);
	instance.setArguments(beans);

    }

}
