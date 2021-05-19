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
public class CheckoutDeleteMailTest {

    public CheckoutDeleteMailTest() {
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
     * Test of setSourceArtifact method, of class CheckoutDeleteMail.
     */
    @Test
    public void testSetSourceArtifact() {
	String sourceArtifact = "1";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setSourceArtifact(sourceArtifact);
    }

    /**
     * Test of setPlanId method, of class CheckoutDeleteMail.
     */
    @Test
    public void testSetPlanId() {
	String planId = "1";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setToPlanId(planId);
    }

    /**
     * Test of setToPlanId method, of class CheckoutDeleteMail.
     */
    @Test
    public void testSetToPlanId() {
	String toPlanId = "1";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setToPlanId(toPlanId);
    }

    /**
     * Test of setImpId method, of class CheckoutDeleteMail.
     */
    @Test
    public void testSetImpId() {
	String impId = "1";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setToImpId(impId);
    }

    /**
     * Test of setToImpId method, of class CheckoutDeleteMail.
     */
    @Test
    public void testSetToImpId() {
	String toImpId = "1";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setToImpId(toImpId);
    }

    /**
     * Test of processMessage method, of class CheckoutDeleteMail.
     */
    @Test
    public void testProcessMessage() {

	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.processMessage();
    }

    @Test
    public void testSetOtherPlanLoadType() {
	String planLoadType = "C";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setOtherPlanLoadType(planLoadType);
    }

    @Test
    public void testSetOtherPlanId() {
	String planId = "1";
	CheckoutDeleteMail instance = new CheckoutDeleteMail();
	instance.setOtherPlanId(planId);
    }
}
