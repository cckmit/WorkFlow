/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import java.util.Date;
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
public class LoadDateChangedMailTest {

    public LoadDateChangedMailTest() {
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
     * Test of getLDAPAuthenticatorImpl method, of class LoadDateChangedMail.
     */
    @Test
    public void testGetLDAPAuthenticatorImpl() {

	LoadDateChangedMail instance = new LoadDateChangedMail();
	LDAPAuthenticatorImpl expResult = null;
	LDAPAuthenticatorImpl result = instance.getLDAPAuthenticatorImpl();
	assertEquals(expResult, result);
    }

    /**
     * Test of setPlanId method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetPlanId() {

	String planId = "";
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setPlanId(planId);
    }

    /**
     * Test of setDeveloperId method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetDeveloperId() {

	String developerId = "";
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setDeveloperId(developerId);
    }

    /**
     * Test of setSystem method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetSystem() {

	String system = "";
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setSystem(system);
    }

    /**
     * Test of setBeforeUpdate method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetBeforeUpdate() {

	Date beforeUpdate = null;
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setBeforeUpdate(beforeUpdate);
    }

    /**
     * Test of setAfterUpdate method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetAfterUpdate() {

	Date afterUpdate = null;
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setAfterUpdate(afterUpdate);
    }

    /**
     * Test of processMessage method, of class LoadDateChangedMail.
     */
    @Test
    public void testProcessMessage() {

	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setDeveloperId("");
	instance.setLeadId("");
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	instance.setAfterUpdate(new Date());
	instance.setBeforeUpdate(new Date());
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails("")).thenReturn(new User());
	instance.processMessage();
    }

    /**
     * Test of processMessage method, of class LoadDateChangedMail.
     */
    // @Test
    public void testProcessMessage2() {
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setDeveloperId("");
	instance.setLeadId("");
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	Date after = new Date();
	Date before = new Date();
	instance.setAfterUpdate(after);
	instance.setBeforeUpdate(before);
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails("")).thenReturn(new User());
	instance.processMessage();
    }

    /**
     * Test of setPlanLoadType method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetPlanLoadType() {
	String planLoadType = "";
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setPlanLoadType(planLoadType);
    }

    /**
     * Test of setToPlanId method, of class LoadDateChangedMail.
     */
    @Test
    public void testSetToPlanId() {
	String toPlanId = "";
	LoadDateChangedMail instance = new LoadDateChangedMail();
	instance.setToPlanId(toPlanId);
    }

}
