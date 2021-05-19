/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DeleteImplementationsActivityMessageTest {

    public DeleteImplementationsActivityMessageTest() {
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
     * Test of processMessage method, of class DeleteImplementationsActivityMessage.
     */
    @Test
    public void testProcessMessage() {
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation lImp = lPlan.getImplementationList().get(0);

	DeleteImplementationsActivityMessage instance = new DeleteImplementationsActivityMessage(lPlan, lImp);
	instance.setUser(DataWareHouse.user);
	instance.processMessage();
    }

    /**
     * Test of getLogLevel method, of class DeleteImplementationsActivityMessage.
     */
    @Test
    public void testGetLogLevel() {
	DeleteImplementationsActivityMessage instance = new DeleteImplementationsActivityMessage(null, null);
	Priority expResult = null;
	Priority result = instance.getLogLevel();
    }

    /**
     * Test of setArguments method, of class DeleteImplementationsActivityMessage.
     */
    @Test
    public void testSetArguments() {
	IBeans[] beans = null;
	DeleteImplementationsActivityMessage instance = new DeleteImplementationsActivityMessage(null, null);
	instance.setArguments(beans);
    }

}
