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
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.utils.Constants;
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
public class MergeOnlineActivityMessageTest {

    public MergeOnlineActivityMessageTest() {
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
     * Test of getLogLevel method, of class MergeOnlineActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	MergeOnlineActivityMessage instance = new MergeOnlineActivityMessage(DataWareHouse.getPlan(), null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);

    }

    /**
     * Test of setArguments method, of class MergeOnlineActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	MergeOnlineActivityMessage instance = new MergeOnlineActivityMessage(DataWareHouse.getPlan(), null);
	instance.setArguments(beans);

    }

    /**
     * Test of processMessage method, of class MergeOnlineActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	MergeOnlineActivityMessage instance = new MergeOnlineActivityMessage(DataWareHouse.getPlan(), null);
	String expResult = "";
	instance.setUser(DataWareHouse.getUser());
	String result = instance.processMessage();
	assertNotNull(result);

    }

    @Test
    public void testProcessMessage1() {

	ImpPlan impPlan = DataWareHouse.getPlan();
	impPlan.setPlanStatus(Constants.PlanStatus.ONLINE.name());
	MergeOnlineActivityMessage instance = new MergeOnlineActivityMessage(impPlan, null);
	String expResult = "";
	instance.setUser(DataWareHouse.getUser());
	String result = instance.processMessage();
	assertNotNull(result);

    }

    @Test
    public void testProcessMessage2() {

	ImpPlan impPlan = DataWareHouse.getPlan();
	impPlan.setPlanStatus(Constants.PlanStatus.ONLINE.name());
	MergeOnlineActivityMessage instance = new MergeOnlineActivityMessage(impPlan, null);
	String expResult = "";
	User userObj = new User();
	userObj.setDisplayName("admin");
	userObj.setCurrentDelegatedUser(userObj);
	instance.setUser(userObj);

	String result = instance.processMessage();
	assertNotNull(result);

    }

}
