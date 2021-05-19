/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.User;
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
public class FallBackRejectUnSecMailTest {

    public FallBackRejectUnSecMailTest() {
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
     * Test of getFileName method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testGetFileName() {
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.getFileName();
    }

    /**
     * Test of setFileName method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testSetFileName() {
	String fileName = "";
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.setFileName(fileName);

    }

    /**
     * Test of getFallbackPlan method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testGetFallbackPlan() {
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.getFallbackPlan();
    }

    /**
     * Test of setFallbackPlan method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testSetFallbackPlan() {
	String fallbackPlan = "";
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.setFallbackPlan(fallbackPlan);

    }

    /**
     * Test of getDependentPlan method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testGetDependentPlan() {
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.getDependentPlan();
    }

    /**
     * Test of setDependentPlan method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testSetDependentPlan() {
	String dependentPlan = "";
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.setDependentPlan(dependentPlan);
    }

    /**
     * Test of getDepPlanLeadId method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testGetDepPlanLeadId() {
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	String result = instance.getDepPlanLeadId();
    }

    /**
     * Test of setDepPlanLeadId method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testSetDepPlanLeadId() {
	String depPlanLeadId = "";
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.setDepPlanLeadId(depPlanLeadId);

    }

    /**
     * Test of processMessage method, of class FallBackRejectUnSecMail.
     */
    @Test
    public void testProcessMessage() {
	FallBackRejectUnSecMail instance = new FallBackRejectUnSecMail();
	instance.setDepPlanLeadId("");
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails("")).thenReturn(mock(User.class));

	instance.processMessage();

    }

}
