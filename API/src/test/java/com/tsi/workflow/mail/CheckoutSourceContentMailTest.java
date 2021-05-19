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
 * @author deepa.jayakumar
 */
public class CheckoutSourceContentMailTest {

    public CheckoutSourceContentMailTest() {
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
     * Test of setOtherPlanLoadType method, of class CheckoutSourceContentMail.
     */
    @Test
    public void testSetOtherPlanLoadType() {
	String otherPlanLoadType = "";
	CheckoutSourceContentMail instance = new CheckoutSourceContentMail();
	instance.setOtherPlanLoadType(otherPlanLoadType);

    }

    /**
     * Test of setOtherPlanId method, of class CheckoutSourceContentMail.
     */
    @Test
    public void testSetOtherPlanId() {
	String otherPlanId = "";
	CheckoutSourceContentMail instance = new CheckoutSourceContentMail();
	instance.setOtherPlanId(otherPlanId);

    }

    @Test
    public void testSetSecured() {
	CheckoutSourceContentMail checkOut = new CheckoutSourceContentMail();
	checkOut.setSecured(true);
	checkOut.processMessage();

    }

    @Test
    public void testSetSecuredFalse() {
	CheckoutSourceContentMail checkOut = new CheckoutSourceContentMail();
	checkOut.setSecured(false);
	checkOut.processMessage();

    }

    /**
     * Test of setSourceArtifact method, of class CheckoutSourceContentMail.
     */
    @Test
    public void testSetSourceArtifact() {
	String sourceArtifact = "";
	CheckoutSourceContentMail instance = new CheckoutSourceContentMail();
	instance.setSourceArtifact(sourceArtifact);

    }

    /**
     * Test of setOtherPlanStatus method, of class CheckoutSourceContentMail.
     */
    @Test
    public void testSetOtherPlanStatus() {
	String otherPlanStatus = "";
	CheckoutSourceContentMail instance = new CheckoutSourceContentMail();
	instance.setOtherPlanStatus(otherPlanStatus);

    }

    /**
     * Test of setToPlanId method, of class CheckoutSourceContentMail.
     */
    @Test
    public void testSetToPlanId() {
	String toPlanId = "";
	CheckoutSourceContentMail instance = new CheckoutSourceContentMail();
	instance.setToPlanId(toPlanId);

    }

    /**
     * Test of processMessage method, of class CheckoutSourceContentMail.
     */
    @Test
    public void testProcessMessage() {
	CheckoutSourceContentMail instance = new CheckoutSourceContentMail();
	instance.processMessage();

    }

}
