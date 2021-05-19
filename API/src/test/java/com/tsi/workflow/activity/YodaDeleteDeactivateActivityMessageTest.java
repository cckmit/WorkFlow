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
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
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
public class YodaDeleteDeactivateActivityMessageTest {

    public YodaDeleteDeactivateActivityMessageTest() {
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
     * Test of isIsAutoReject method, of class YodaDeleteDeactivateActivityMessage.
     */
    @Test
    public void testIsIsAutoReject() {
	YodaDeleteDeactivateActivityMessage instance = new YodaDeleteDeactivateActivityMessage(DataWareHouse.getPlan(), null, null);
	boolean expResult = false;
	boolean result = instance.isIsAutoReject();
	assertEquals(expResult, result);
    }

    /**
     * Test of setIsAutoReject method, of class YodaDeleteDeactivateActivityMessage.
     */
    @Test
    public void testSetIsAutoReject() {
	boolean isAutoReject = false;
	YodaDeleteDeactivateActivityMessage instance = new YodaDeleteDeactivateActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setIsAutoReject(isAutoReject);
    }

    /**
     * Test of processMessage method, of class YodaDeleteDeactivateActivityMessage.
     */
    @Test
    public void testProcessMessage() {
	YodaDeleteDeactivateActivityMessage instance = new YodaDeleteDeactivateActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setArguments(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	instance.setUser(DataWareHouse.user);
	String result = instance.processMessage();
    }

    /**
     * Test of getLogLevel method, of class YodaDeleteDeactivateActivityMessage.
     */
    @Test
    public void testGetLogLevel() {
	YodaDeleteDeactivateActivityMessage instance = new YodaDeleteDeactivateActivityMessage(DataWareHouse.getPlan(), null, null);
	Priority expResult = Priority.ERROR;
	instance.setArguments(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	instance.setUser(DataWareHouse.user);
	instance.processMessage();
	Priority result = instance.getLogLevel();
	assertNotNull(result);
	SystemLoadActions sysLoadActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	sysLoadActions.setLastActionStatus("Failure");
	instance.setArguments(sysLoadActions);
	instance.processMessage();
    }

    /**
     * Test of setArguments method, of class YodaDeleteDeactivateActivityMessage.
     */
    @Test
    public void testSetArguments() {
	IBeans[] beans = null;
	YodaDeleteDeactivateActivityMessage instance = new YodaDeleteDeactivateActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setArguments(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
    }

    @Test
    public void testProcessMessageDelegateUser() {
	YodaDeleteDeactivateActivityMessage instance = new YodaDeleteDeactivateActivityMessage(DataWareHouse.getPlan(), null, null);
	SystemLoadActions sysLoadActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	sysLoadActions.setLastActionStatus("Failure");
	instance.setIsAutoReject(Boolean.TRUE);
	Vpars vparInfo = new Vpars();
	vparInfo.setId(10);
	sysLoadActions.setVparId(vparInfo);
	instance.setArguments(sysLoadActions);
	User usr = DataWareHouse.getUser();
	usr.setCurrentDelegatedUser(usr);
	instance.setUser(usr);

	instance.processMessage();

    }

}
