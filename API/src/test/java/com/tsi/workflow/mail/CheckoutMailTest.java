/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class CheckoutMailTest {

    public CheckoutMailTest() {
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
     * Test of setSourceArtifact method, of class CheckoutMail.
     */
    @Test
    public void testSetSourceArtifact() {
	String sourceArtifact = "1";
	CheckoutMail instance = new CheckoutMail();
	instance.setSourceArtifact(sourceArtifact);
    }

    /**
     * Test of setPlanId method, of class CheckoutMail.
     */
    @Test
    public void testSetPlanId() {
	String planId = "1";
	CheckoutMail instance = new CheckoutMail();
	instance.setPlanId(planId);
    }

    /**
     * Test of setToPlanId method, of class CheckoutMail.
     */
    @Test
    public void testSetToPlanId() {
	String toPlanId = "1";
	CheckoutMail instance = new CheckoutMail();
	instance.setToPlanId(toPlanId);
    }

    /**
     * Test of setImpId method, of class CheckoutMail.
     */
    @Test
    public void testSetImpId() {
	String impId = "1";
	CheckoutMail instance = new CheckoutMail();
	instance.setImpId(impId);
    }

    /**
     * Test of setToImpId method, of class CheckoutMail.
     */
    @Test
    public void testSetToImpId() {
	String toImpId = "1";
	CheckoutMail instance = new CheckoutMail();
	instance.setToImpId(toImpId);
    }

    /**
     * Test of processMessage method, of class CheckoutMail.
     */
    @Test
    public void testProcessMessage() {

	CheckoutMail instance = new CheckoutMail();
	instance.processMessage();
    }

}
