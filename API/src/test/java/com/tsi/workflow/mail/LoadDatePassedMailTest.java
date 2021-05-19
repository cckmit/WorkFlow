/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
public class LoadDatePassedMailTest {

    public LoadDatePassedMailTest() {
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
     * Test of getPlanId method, of class LoadDatePassedMail.
     */
    @Test
    public void testGetPlanId() {
	LoadDatePassedMail instance = new LoadDatePassedMail();
	String expResult = "T1700123";
	instance.setPlanId(expResult);
	String result = instance.getPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getCurrentUser method, of class LoadDatePassedMail.
     */
    @Test
    public void testGetCurrentUser() {
	LoadDatePassedMail instance = new LoadDatePassedMail();
	User expResult = DataWareHouse.getUser();
	instance.setCurrentUser(expResult);
	User result = instance.getCurrentUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLeadId method, of class LoadDatePassedMail.
     */
    @Test
    public void testGetLeadId() {
	LoadDatePassedMail instance = new LoadDatePassedMail();
	String expResult = "1";
	instance.setLeadId(expResult);
	String result = instance.getLeadId();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class LoadDatePassedMail.
     */
    @Test
    public void testProcessMessage() {

	LoadDatePassedMail instance = new LoadDatePassedMail();
	instance.setLeadId("");
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	instance.processMessage();
    }

}
