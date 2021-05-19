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
import java.util.SortedSet;
import java.util.TreeSet;
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
public class NewTargetSystemMailTest {

    public NewTargetSystemMailTest() {
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
     * Test of getUserDetails method, of class NewTargetSystemMail.
     */
    @Test
    public void testGetUserDetails() {
	NewTargetSystemMail instance = new NewTargetSystemMail();
	User expResult = DataWareHouse.getUser();
	instance.setUserDetails(expResult);
	User result = instance.getUserDetails();
	assertEquals(expResult, result);
    }

    /**
     * Test of getAuthenticator method, of class NewTargetSystemMail.
     */
    @Test
    public void testGetAuthenticator() {
	NewTargetSystemMail instance = new NewTargetSystemMail();
	LDAPAuthenticatorImpl expResult = new LDAPAuthenticatorImpl();
	instance.setAuthenticator(expResult);
	LDAPAuthenticatorImpl result = instance.getAuthenticator();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTargetSystem method, of class NewTargetSystemMail.
     */
    @Test
    public void testGetTargetSystem() {
	NewTargetSystemMail instance = new NewTargetSystemMail();
	SortedSet<String> expResult = new TreeSet();
	instance.setTargetSystem(expResult);
	SortedSet<String> result = instance.getTargetSystem();
	assertEquals(expResult, result);
    }

    /**
     * Test of addTargetSystem method, of class NewTargetSystemMail.
     */
    @Test
    public void testAddTargetSystem() {
	String targetSystem = "APO";
	NewTargetSystemMail instance = new NewTargetSystemMail();
	instance.addTargetSystem(targetSystem);
    }

    /**
     * Test of getLeadId method, of class NewTargetSystemMail.
     */
    @Test
    public void testGetLeadId() {
	NewTargetSystemMail instance = new NewTargetSystemMail();
	String expResult = "1";
	instance.setLeadId(expResult);
	String result = instance.getLeadId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getImplementationId method, of class NewTargetSystemMail.
     */
    @Test
    public void testGetImplementationId() {
	NewTargetSystemMail instance = new NewTargetSystemMail();
	String expResult = "T1700123_001";
	instance.setImplementationId(expResult);
	String result = instance.getImplementationId();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class NewTargetSystemMail.
     */
    @Test
    public void testProcessMessage() {

	NewTargetSystemMail instance = new NewTargetSystemMail();
	instance.setLeadId("ll");
	LDAPAuthenticatorImpl lDAPAuthenticatorImpl = mock(LDAPAuthenticatorImpl.class);
	User user = mock(User.class);
	ReflectionTestUtils.setField(instance, "authenticator", lDAPAuthenticatorImpl);
	ReflectionTestUtils.setField(instance, "userDetails", user);
	when(lDAPAuthenticatorImpl.getUserDetails("ll")).thenReturn(mock(User.class));
	when(user.getMailId()).thenReturn("");
	when(user.getDisplayName()).thenReturn("test");
	instance.processMessage();
	// TODO review the generated test code and remove the default call to fail.
	assertEquals(user, instance.getUserDetails());
    }

}
