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
public class DeveloperReassignmentMailTest {

    public DeveloperReassignmentMailTest() {
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
     * Test of getOldDeveloperName method, of class DeveloperReassignmentMail.
     */
    @Test
    public void testGetOldDeveloperName() {
	DeveloperReassignmentMail instance = new DeveloperReassignmentMail();
	String expResult = "1";
	instance.setOldDeveloperName(expResult);
	String result = instance.getOldDeveloperName();
	assertEquals(expResult, result);
    }

    /**
     * Test of getNewDeveloperName method, of class DeveloperReassignmentMail.
     */
    @Test
    public void testGetNewDeveloperName() {
	DeveloperReassignmentMail instance = new DeveloperReassignmentMail();
	String expResult = "1";
	instance.setNewDeveloperName(expResult);
	String result = instance.getNewDeveloperName();
	assertEquals(expResult, result);
    }

    /**
     * Test of getImplementation method, of class DeveloperReassignmentMail.
     */
    @Test
    public void testGetImplementation() {
	DeveloperReassignmentMail instance = new DeveloperReassignmentMail();
	Implementation expResult = new Implementation("T1700123_001");
	instance.setImplementation(expResult);
	Implementation result = instance.getImplementation();
	assertEquals(expResult, result);
    }

    /**
     * Test of getUserDetails method, of class DeveloperReassignmentMail.
     */
    @Test
    public void testGetUserDetails() {
	DeveloperReassignmentMail instance = new DeveloperReassignmentMail();
	User expResult = DataWareHouse.getUser();
	instance.setUserDetails(expResult);
	User result = instance.getUserDetails();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class DeveloperReassignmentMail.
     */
    @Test
    public void testProcessMessage() {

	DeveloperReassignmentMail instance = new DeveloperReassignmentMail();
	instance.setUserDetails(DataWareHouse.getUser());
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	instance.setOldDeveloperName("");
	instance.setNewDeveloperName("");
	instance.setImplementation(DataWareHouse.getPlan().getImplementationList().get(0));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails("")).thenReturn(new User());
	instance.processMessage();
    }

    /**
     * Test of processMessage method, of class DeveloperReassignmentMail.
     */
    @Test
    public void testProcessMessage1() {

	DeveloperReassignmentMail instance = new DeveloperReassignmentMail();
	instance.setUserDetails(DataWareHouse.getUser());
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	instance.setOldDeveloperName(null);
	instance.setNewDeveloperName("");
	instance.setImplementation(DataWareHouse.getPlan().getImplementationList().get(0));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails("")).thenReturn(new User());
	instance.processMessage();
    }

}
