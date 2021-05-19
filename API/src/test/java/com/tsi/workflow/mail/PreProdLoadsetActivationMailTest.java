/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
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
public class PreProdLoadsetActivationMailTest {

    public PreProdLoadsetActivationMailTest() {
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
     * Test of getLDAPAuthenticatorImpl method, of class
     * PreProdLoadsetActivationMail.
     */
    @Test
    public void testMail() {
	PreProdLoadsetActivationMail instance = new PreProdLoadsetActivationMail();
	TestCaseExecutor.doTest(instance, PreProdLoadsetActivationMail.class);
    }

    @Test
    public void testProcessMessage() {
	PreProdLoadsetActivationMail instance = new PreProdLoadsetActivationMail();
	instance.setActivationDateTime(new Date());
	instance.setLoadsetName("");
	instance.setMessage("");
	instance.setPlanId("");

	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails("")).thenReturn(new User());
	instance.setPreProdSystemName("");
	instance.processMessage();

    }
}
