/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.utils.Constants;
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
public class StagingConfigFileCreationFailureMailTest {

    public StagingConfigFileCreationFailureMailTest() {
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
     * Test of getPlanId method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testGetPlanId() {

	StagingConfigFileCreationFailureMail instance = new StagingConfigFileCreationFailureMail();
	String expResult = null;
	String result = instance.getPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of setPlanId method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testSetPlanId() {

	String planId = "";
	StagingConfigFileCreationFailureMail instance = new StagingConfigFileCreationFailureMail();
	instance.setPlanId(planId);
    }

    /**
     * Test of getLeadId method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testGetLeadId() {

	StagingConfigFileCreationFailureMail instance = new StagingConfigFileCreationFailureMail();
	String expResult = null;
	String result = instance.getLeadId();
	assertEquals(expResult, result);
    }

    /**
     * Test of setLeadId method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testSetLeadId() {

	String leadId = null;
	StagingConfigFileCreationFailureMail instance = new StagingConfigFileCreationFailureMail();
	instance.setLeadId(leadId);
    }

    /**
     * Test of getSystem method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testGetSystem() {

	StagingConfigFileCreationFailureMail instance = new StagingConfigFileCreationFailureMail();
	String expResult = null;
	String result = instance.getSystem();
	assertEquals(expResult, result);
    }

    /**
     * Test of setSystem method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testSetSystem() {

	String system = "";
	StagingConfigFileCreationFailureMail instance = new StagingConfigFileCreationFailureMail();
	instance.setSystem(system);
    }

    /**
     * Test of processMessage method, of class StagingConfigFileCreationFailureMail.
     */
    @Test
    public void testProcessMessage() {

	StagingConfigFileCreationFailureMail realinstance = new StagingConfigFileCreationFailureMail();
	StagingConfigFileCreationFailureMail instance = spy(realinstance);
	instance.setLeadId("");
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	instance.addToAddressUserId("", Constants.MailSenderRole.LEAD);
	doNothing().when(instance).addToAddressUserId("", Constants.MailSenderRole.LEAD);
	instance.processMessage();
    }

}
