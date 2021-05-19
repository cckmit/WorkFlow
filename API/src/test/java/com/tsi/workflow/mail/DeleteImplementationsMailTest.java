/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DeleteImplementationsMailTest {

    public DeleteImplementationsMailTest() {
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
     * Test of getlPlan method, of class DeleteImplementationsMail.
     */
    @Test
    public void testGetlPlan() {
	DeleteImplementationsMail instance = new DeleteImplementationsMail();
	instance.setlPlan(DataWareHouse.getPlan());
	ImpPlan result = instance.getlPlan();
    }

    /**
     * Test of getlImp method, of class DeleteImplementationsMail.
     */
    @Test
    public void testGetlImp() {
	DeleteImplementationsMail instance = new DeleteImplementationsMail();
	Implementation expResult = null;
	instance.setlImp(DataWareHouse.getPlan().getImplementationList().get(0));
	Implementation result = instance.getlImp();
    }

    /**
     * Test of getDeletedBy method, of class DeleteImplementationsMail.
     */
    @Test
    public void testGetDeletedBy() {
	DeleteImplementationsMail instance = new DeleteImplementationsMail();
	instance.setDeletedBy("");
	String result = instance.getDeletedBy();
    }

    /**
     * Test of processMessage method, of class DeleteImplementationsMail.
     */
    @Test
    public void testProcessMessage() {
	DeleteImplementationsMail instance = new DeleteImplementationsMail();
	instance.setDeletedBy(" ");
	instance.setlPlan(DataWareHouse.getPlan());
	instance.setlImp(DataWareHouse.getPlan().getImplementationList().get(0));
	instance.processMessage();
    }

}
