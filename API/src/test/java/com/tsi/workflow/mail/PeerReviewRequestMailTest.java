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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
public class PeerReviewRequestMailTest {

    public PeerReviewRequestMailTest() {
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
     * Test of getReviewerList method, of class PeerReviewRequestMail.
     */
    @Test
    public void testGetReviewerList() {
	PeerReviewRequestMail instance = new PeerReviewRequestMail();
	List<String> expResult = new ArrayList();
	instance.setReviewerList(expResult);
	List<String> result = instance.getReviewerList();
	assertEquals(expResult, result);
    }

    /**
     * Test of getDeveloper method, of class PeerReviewRequestMail.
     */
    @Test
    public void testGetDeveloper() {
	PeerReviewRequestMail instance = new PeerReviewRequestMail();
	String expResult = "1";
	instance.setDeveloper(expResult);
	String result = instance.getDeveloper();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTicketUrl method, of class PeerReviewRequestMail.
     */
    @Test
    public void testGetTicketUrl() {
	PeerReviewRequestMail instance = new PeerReviewRequestMail();
	String expResult = "1";
	instance.setTicketUrl(expResult);
	String result = instance.getTicketUrl();
	assertEquals(expResult, result);
    }

    /**
     * Test of getImplementationId method, of class PeerReviewRequestMail.
     */
    @Test
    public void testGetImplementationId() {
	PeerReviewRequestMail instance = new PeerReviewRequestMail();
	String expResult = "T1700123_001";
	instance.setImplementationId(expResult);
	String result = instance.getImplementationId();
	assertEquals(expResult, result);
    }

    /**
     * Test of getAuthenticator method, of class PeerReviewRequestMail.
     */
    @Test
    public void testGetAuthenticator() {
	PeerReviewRequestMail instance = new PeerReviewRequestMail();
	LDAPAuthenticatorImpl expResult = new LDAPAuthenticatorImpl();
	instance.setAuthenticator(expResult);
	LDAPAuthenticatorImpl result = instance.getAuthenticator();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class PeerReviewRequestMail.
     */
    @Test
    public void testProcessMessage() {

	PeerReviewRequestMail instance = new PeerReviewRequestMail();

	instance.setReviewerList(Arrays.asList(DataWareHouse.getPlan().getImplementationList().get(0).getPeerReviewers()));
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);

	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails(DataWareHouse.getPlan().getImplementationList().get(0).getPeerReviewers())).thenReturn(new User());
	instance.processMessage();
    }

}
