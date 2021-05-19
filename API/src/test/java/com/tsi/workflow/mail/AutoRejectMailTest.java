/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.utils.Constants;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class AutoRejectMailTest {

    public AutoRejectMailTest() {
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
     * Test of getPlanId method, of class AutoRejectMail.
     */
    @Test
    public void testGetPlanId() {
	AutoRejectMail instance = new AutoRejectMail();
	String expResult = "T1700123";
	instance.setPlanId(expResult);
	String result = instance.getPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getDependentPlanId method, of class AutoRejectMail.
     */
    @Test
    public void testGetDependentPlanId() {
	AutoRejectMail instance = new AutoRejectMail();
	String expResult = "T1700123";
	instance.setDependentPlanId(expResult);
	String result = instance.getDependentPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLeadId method, of class AutoRejectMail.
     */
    @Test
    public void testGetLeadId() {
	AutoRejectMail instance = new AutoRejectMail();
	String expResult = "1";
	instance.setLeadId(expResult);
	String result = instance.getLeadId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class AutoRejectMail.
     */
    @Test
    public void testGetStatus() {
	AutoRejectMail instance = new AutoRejectMail();
	String expResult = "1";
	instance.setStatus(expResult);
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class AutoRejectMail.
     */
    @Test
    public void testProcessMessage() {

	AutoRejectMail instance = new AutoRejectMail();
	instance.setLeadId("");
	instance.setStatus("");
	instance.processMessage();
	instance.setStatus(Constants.REJECT_REASON.LOAD_DATE_CHANGE.getValue());
	instance.processMessage();
    }

}
