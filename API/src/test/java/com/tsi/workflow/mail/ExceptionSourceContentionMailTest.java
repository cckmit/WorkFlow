/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
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
public class ExceptionSourceContentionMailTest {

    public ExceptionSourceContentionMailTest() {
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
     * Test of getDependentPlan method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testGetDependentPlan() {
	ExceptionSourceContentionMail instance = new ExceptionSourceContentionMail();

	String result = instance.getDependentPlan();

    }

    /**
     * Test of setDependentPlan method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testSetDependentPlan() {
	String dependentPlan = "";
	ExceptionSourceContentionMail instance = new ExceptionSourceContentionMail();
	instance.setDependentPlan(dependentPlan);

    }

    /**
     * Test of getOnlinePlan method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testGetOnlinePlan() {
	ExceptionSourceContentionMail instance = new ExceptionSourceContentionMail();

	String result = instance.getOnlinePlan();

    }

    /**
     * Test of setOnlinePlan method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testSetOnlinePlan() {
	String onlinePlan = "";
	ExceptionSourceContentionMail instance = new ExceptionSourceContentionMail();
	instance.setOnlinePlan(onlinePlan);

    }

    /**
     * Test of getLeadId method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testGetLeadId() {

	ExceptionSourceContentionMail instance = new ExceptionSourceContentionMail();

	String result = instance.getLeadId();

    }

    /**
     * Test of setLeadId method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testSetLeadId() {

	String leadId = "";
	ExceptionSourceContentionMail instance = new ExceptionSourceContentionMail();
	instance.setLeadId(leadId);

    }

    /**
     * Test of processMessage method, of class ExceptionSourceContentionMail.
     */
    @Test
    public void testProcessMessage() {

	ExceptionSourceContentionMail instance = spy(new ExceptionSourceContentionMail());
	instance.setLeadId(DataWareHouse.user.getId());
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails(instance.getLeadId())).thenReturn(DataWareHouse.user);
	instance.processMessage();

    }

}
