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
public class ReviewerAssignmentMailTest {

    public ReviewerAssignmentMailTest() {
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
     * Test of getImplementation method, of class ReviewerAssignmentMail.
     */
    @Test
    public void testGetImplementation() {
	ReviewerAssignmentMail instance = new ReviewerAssignmentMail();
	Implementation expResult = new Implementation("T1700123_001");
	instance.setImplementation(expResult);
	Implementation result = instance.getImplementation();
	assertEquals(expResult, result);
    }

    /**
     * Test of getUserDetails method, of class ReviewerAssignmentMail.
     */
    @Test
    public void testGetUserDetails() {
	ReviewerAssignmentMail instance = new ReviewerAssignmentMail();
	User expResult = DataWareHouse.getUser();
	instance.setUserDetails(expResult);
	User result = instance.getUserDetails();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class ReviewerAssignmentMail.
     */
    @Test
    public void testProcessMessage() {

	ReviewerAssignmentMail instance = new ReviewerAssignmentMail();
	instance.setImplementation(DataWareHouse.getPlan().getImplementationList().get(0));
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(user);
	instance.setUserDetails(user);
	when(instance.authenticator.getUserDetails(instance.getImplementation().getPeerReviewers())).thenReturn(user);
	instance.processMessage();
    }

    @Test
    public void testProcessMessage1() {

	ReviewerAssignmentMail instance = new ReviewerAssignmentMail();
	instance.setImplementation(DataWareHouse.getPlan().getImplementationList().get(0));
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUserDetails(user);
	when(instance.authenticator.getUserDetails(instance.getImplementation().getPeerReviewers())).thenReturn(user);
	instance.processMessage();
    }

}
