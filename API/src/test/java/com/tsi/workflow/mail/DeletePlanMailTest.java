/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DeletePlanMailTest {

    public DeletePlanMailTest() {
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
     * Test of getlPlan method, of class DeletePlanMail.
     */
    @Test
    public void testGetlPlan() {
	DeletePlanMail instance = new DeletePlanMail();
	ImpPlan expResult = DataWareHouse.getPlan();
	instance.setlPlan(expResult);
	ImpPlan result = instance.getlPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of getlImp method, of class DeletePlanMail.
     */
    @Test
    public void testGetlImp() {
	DeletePlanMail instance = new DeletePlanMail();
	List<Implementation> expResult = DataWareHouse.getPlan().getImplementationList();
	instance.setlImp(expResult);
	List<Implementation> result = instance.getlImp();
	assertEquals(expResult, result);
    }

    /**
     * Test of getDeletedBy method, of class DeletePlanMail.
     */
    @Test
    public void testGetDeletedBy() {
	DeletePlanMail instance = new DeletePlanMail();
	String expResult = "";
	instance.setDeletedBy(expResult);
	String result = instance.getDeletedBy();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class DeletePlanMail.
     */
    @Test
    public void testProcessMessage() {
	DeletePlanMail instance = new DeletePlanMail();
	instance.setDeletedBy("");
	instance.setlImp(DataWareHouse.getPlan().getImplementationList());
	instance.setlPlan(DataWareHouse.getPlan());
	instance.processMessage();
    }

}
