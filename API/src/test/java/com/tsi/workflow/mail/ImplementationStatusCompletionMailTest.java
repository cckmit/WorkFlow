/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ImplementationStatusCompletionMailTest {

    public ImplementationStatusCompletionMailTest() {
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
     * Test of getImpPlan method, of class ImplementationStatusCompletionMail.
     */
    @Test
    public void testGetImpPlan() {
	ImplementationStatusCompletionMail instance = new ImplementationStatusCompletionMail();
	ImpPlan expResult = new ImpPlan("T1700123");
	instance.setImpPlan(expResult);
	ImpPlan result = instance.getImpPlan();
	assertEquals(expResult, result);
    }

    /**
     * Test of getImplementation method, of class
     * ImplementationStatusCompletionMail.
     */
    @Test
    public void testGetImplementation() {
	ImplementationStatusCompletionMail instance = new ImplementationStatusCompletionMail();
	Implementation expResult = new Implementation("T1700123_001");
	instance.setImplementation(expResult);
	Implementation result = instance.getImplementation();
	assertEquals(expResult, result);
    }

    /**
     * Test of getStatus method, of class ImplementationStatusCompletionMail.
     */
    @Test
    public void testGetStatus() {
	ImplementationStatusCompletionMail instance = new ImplementationStatusCompletionMail();
	String expResult = "1";
	instance.setStatus(expResult);
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class ImplementationStatusCompletionMail.
     */
    @Test
    public void testProcessMessage() {

	ImplementationStatusCompletionMail instance = new ImplementationStatusCompletionMail();

	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	instance.setImpPlan(DataWareHouse.getPlan());
	instance.setImplementation(DataWareHouse.getPlan().getImplementationList().get(0));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails(DataWareHouse.getPlan().getLeadId())).thenReturn(new User());
	when(instance.authenticator.getUserDetails(DataWareHouse.getPlan().getImplementationList().get(0).getDevId())).thenReturn(new User());
	instance.processMessage();
    }

}
