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
public class RejectMailTest {

    public RejectMailTest() {
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
     * Test of getPlanId method, of class RejectMail.
     */
    @Test
    public void testGetPlanId() {
	RejectMail instance = new RejectMail();
	String expResult = "T1700123";
	instance.setPlanId(expResult);
	String result = instance.getPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getCurrentUser method, of class RejectMail.
     */
    @Test
    public void testGetCurrentUser() {
	RejectMail instance = new RejectMail();
	User expResult = DataWareHouse.getUser();
	instance.setCurrentUser(expResult);
	User result = instance.getCurrentUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLeadId method, of class RejectMail.
     */
    @Test
    public void testGetLeadId() {
	RejectMail instance = new RejectMail();
	String expResult = "1";
	instance.setLeadId(expResult);
	String result = instance.getLeadId();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class RejectMail.
     */
    @Test
    public void testProcessMessage() {

	RejectMail instance = new RejectMail();
	instance.setCurrentUser(DataWareHouse.getUser());
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	instance.setLeadId("");
	when(instance.authenticator.getUserDetails("")).thenReturn(instance.getCurrentUser());
	instance.processMessage();
    }

    @Test
    public void testGetComment() {
	RejectMail instance = new RejectMail();
	instance.setComment("Reject");
	assertEquals("Reject", instance.getComment());
	instance.setCurrentUser(DataWareHouse.getUser());
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	instance.setLeadId("");
	when(instance.authenticator.getUserDetails("")).thenReturn(instance.getCurrentUser());
	instance.processMessage();
    }

    @Test
    public void testProcessMessageCondition() {
	RejectMail instance = new RejectMail();
	instance.setBuildFailure(Boolean.TRUE);
	instance.getLDAPAuthenticatorImpl();
	instance.processMessage();
    }
}
