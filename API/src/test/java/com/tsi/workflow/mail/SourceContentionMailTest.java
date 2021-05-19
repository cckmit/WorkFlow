/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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
public class SourceContentionMailTest {

    public SourceContentionMailTest() {
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
     * Test of getDependentPlan method, of class SourceContentionMail.
     */
    @Test
    public void testGetDependentPlan() {

	SourceContentionMail instance = spy(new SourceContentionMail());
	String expResult = "";
	String result = instance.getDependentPlan();
	assertNull(result);

    }

    /**
     * Test of setDependentPlan method, of class SourceContentionMail.
     */
    @Test
    public void testSetDependentPlan() {

	String dependentPlan = "";
	SourceContentionMail instance = spy(new SourceContentionMail());
	instance.setDependentPlan(dependentPlan);
	assertNotNull(instance.getDependentPlan());

    }

    /**
     * Test of getOnlinePlan method, of class SourceContentionMail.
     */
    @Test
    public void testGetOnlinePlan() {

	SourceContentionMail instance = spy(new SourceContentionMail());
	String expResult = "";
	String result = instance.getOnlinePlan();
	assertNull(result);

    }

    /**
     * Test of setOnlinePlan method, of class SourceContentionMail.
     */
    @Test
    public void testSetOnlinePlan() {

	String onlinePlan = "";
	SourceContentionMail instance = spy(new SourceContentionMail());
	instance.setOnlinePlan(onlinePlan);
	assertNotNull(onlinePlan);
    }

    /**
     * Test of getLeadId method, of class SourceContentionMail.
     */
    @Test
    public void testGetLeadId() {

	SourceContentionMail instance = spy(new SourceContentionMail());
	String expResult = "";
	String result = instance.getLeadId();
	assertNull(result);

    }

    /**
     * Test of setLeadId method, of class SourceContentionMail.
     */
    @Test
    public void testSetLeadId() {

	String leadId = "";
	SourceContentionMail instance = spy(new SourceContentionMail());
	instance.setLeadId(leadId);

	assertNotNull(instance.getLeadId());

    }

    /**
     * Test of processMessage method, of class SourceContentionMail.
     */
    @Test
    public void testProcessMessage() {

	SourceContentionMail instance = spy(new SourceContentionMail());
	instance.setLeadId(DataWareHouse.user.getId());
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	instance.processMessage();
    }

}
