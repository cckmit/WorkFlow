/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class StagingBuildFailureMailTest {

    public StagingBuildFailureMailTest() {
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
     * Test of getPlanId method, of class StagingBuildFailureMail.
     */
    @Test
    public void testGetPlanId() {

	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	String expResult = null;
	String result = instance.getPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of setPlanId method, of class StagingBuildFailureMail.
     */
    @Test
    public void testSetPlanId() {

	String planId = "";
	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	instance.setPlanId(planId);
    }

    /**
     * Test of getLeadId method, of class StagingBuildFailureMail.
     */
    @Test
    public void testGetLeadId() {

	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	String expResult = null;
	String result = instance.getLeadId();
	assertEquals(expResult, result);
    }

    /**
     * Test of setLeadId method, of class StagingBuildFailureMail.
     */
    @Test
    public void testSetLeadId() {

	String leadId = "";
	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	instance.setLeadId(leadId);
    }

    /**
     * Test of getSystem method, of class StagingBuildFailureMail.
     */
    @Test
    public void testGetSystem() {

	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	String expResult = null;
	String result = instance.getSystem();
	assertEquals(expResult, result);
    }

    /**
     * Test of setSystem method, of class StagingBuildFailureMail.
     */
    @Test
    public void testSetSystem() {

	String system = "";
	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	instance.setSystem(system);
    }

    /**
     * Test of processMessage method, of class StagingBuildFailureMail.
     */
    @Test
    public void testProcessMessage() {

	StagingBuildFailureMail instance = new StagingBuildFailureMail();
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	instance.setLeadId("");
	instance.processMessage();
    }

}
