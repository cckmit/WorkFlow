/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.mockito.Mockito.mock;

import com.tsi.workflow.TestCaseExecutor;
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
public class ProdLoadsetActivationMailTest {

    public ProdLoadsetActivationMailTest() {
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

    @Test
    public void testMail() {
	ProdLoadsetActivationMail instance = new ProdLoadsetActivationMail();
	TestCaseExecutor.doTest(instance, PreProdLoadsetActivationMail.class);
    }

    /**
     * Test of processMessage method, of class ProdLoadsetActivationMail.
     */
    @Test
    public void testProcessMessage() {

	ProdLoadsetActivationMail instance = new ProdLoadsetActivationMail();
	instance.setActivationDateTime(new Date());
	instance.setLoadsetName("");
	instance.setMessage("");
	instance.setPlanId("");
	instance.setActivationDateTime(new Date());
	instance.getLoadsetName();
	instance.getActivationDateTime();
	instance.getMessage();
	instance.getPlanId();
	instance.setProdSystemName("");
	instance.getProdSystemName();
	instance.setStatus("");
	instance.getStatus();
	instance.getFallbackActivity();
	instance.setFallbackActivity(Boolean.FALSE);
	instance.getCpuName();
	instance.setCpuName("wsp");

	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	instance.processMessage();
	instance.setStatus("FALLBACK");
	instance.processMessage();
	instance.setStatus("ACTIVATED");
	instance.processMessage();

    }

}
