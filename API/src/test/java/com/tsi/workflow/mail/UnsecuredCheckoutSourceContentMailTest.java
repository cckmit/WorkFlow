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
public class UnsecuredCheckoutSourceContentMailTest {

    public UnsecuredCheckoutSourceContentMailTest() {
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
     * Test of setDependent method, of class UnsecuredCheckoutSourceContentMail.
     */
    @Test
    public void testSetDependent() {
	boolean dependent = false;
	UnsecuredCheckoutSourceContentMail instance = new UnsecuredCheckoutSourceContentMail();
	instance.setDependent(dependent);
    }

    /**
     * Test of setOtherPlanLoadType method, of class
     * UnsecuredCheckoutSourceContentMail.
     */
    @Test
    public void testSetOtherPlanLoadType() {
	String otherPlanLoadType = "";
	UnsecuredCheckoutSourceContentMail instance = new UnsecuredCheckoutSourceContentMail();
	instance.setOtherPlanLoadType(otherPlanLoadType);
    }

    /**
     * Test of setOtherPlanId method, of class UnsecuredCheckoutSourceContentMail.
     */
    @Test
    public void testSetOtherPlanId() {
	String otherPlanId = "";
	UnsecuredCheckoutSourceContentMail instance = new UnsecuredCheckoutSourceContentMail();
	instance.setOtherPlanId(otherPlanId);
    }

    /**
     * Test of setSourceArtifact method, of class
     * UnsecuredCheckoutSourceContentMail.
     */
    @Test
    public void testSetSourceArtifact() {
	String sourceArtifact = "";
	UnsecuredCheckoutSourceContentMail instance = new UnsecuredCheckoutSourceContentMail();
	instance.setSourceArtifact(sourceArtifact);
    }

    /**
     * Test of setToPlanId method, of class UnsecuredCheckoutSourceContentMail.
     */
    @Test
    public void testSetToPlanId() {
	String toPlanId = "";
	UnsecuredCheckoutSourceContentMail instance = new UnsecuredCheckoutSourceContentMail();
	instance.setToPlanId(toPlanId);
    }

    /**
     * Test of processMessage method, of class UnsecuredCheckoutSourceContentMail.
     */
    @Test
    public void testProcessMessage() {
	UnsecuredCheckoutSourceContentMail instance = new UnsecuredCheckoutSourceContentMail();
	instance.processMessage();
	instance.setDependent(true);
	instance.processMessage();
    }
}
