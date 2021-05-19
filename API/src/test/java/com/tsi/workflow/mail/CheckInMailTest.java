/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class CheckInMailTest {

    public CheckInMailTest() {
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
     * Test of setSourceArtifact method, of class CheckInMail.
     */
    @Test
    public void testSetSourceArtifact() {
	String sourceArtifact = "1";
	CheckInMail instance = new CheckInMail();
	instance.setSourceArtifacts(sourceArtifact);
    }

    /**
     * Test of setPlanId method, of class CheckInMail.
     */
    @Test
    public void testSetPlanId() {

	ImpPlan planId = new ImpPlan("1");
	CheckInMail instance = new CheckInMail();
	instance.setDependentPlan(planId);
    }

    /**
     * Test of setToPlanId method, of class CheckInMail.
     */
    @Test
    public void testSetToPlanId() {

	ImpPlan toPlanId = new ImpPlan("1");
	CheckInMail instance = new CheckInMail();
	instance.setSourcePlan(toPlanId);
    }

    /**
     * Test of processMessage method, of class CheckInMail.
     */
    @Test
    public void testProcessMessage() {

	CheckInMail instance = new CheckInMail();
	instance.setDependentPlan(DataWareHouse.getPlan());
	instance.setSourcePlan(DataWareHouse.getPlan());
	instance.setSourceArtifacts("");
	instance.processMessage();
    }

    /**
     * Test of getDependentPlan method, of class CheckInMail.
     */
    @Test
    public void testGetDependentPlan() {
	CheckInMail instance = new CheckInMail();
	ImpPlan expResult = null;
	ImpPlan result = instance.getDependentPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSourcePlan method, of class CheckInMail.
     */
    @Test
    public void testGetSourcePlan() {
	CheckInMail instance = new CheckInMail();
	ImpPlan expResult = null;
	ImpPlan result = instance.getSourcePlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSourceArtifacts method, of class CheckInMail.
     */
    @Test
    public void testGetSourceArtifacts() {
	CheckInMail instance = new CheckInMail();
	String expResult = null;
	String result = instance.getSourceArtifacts();
	assertEquals(expResult, result);
    }

}
