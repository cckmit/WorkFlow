/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DelegateActivationMailTest {

    public DelegateActivationMailTest() {
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
     * Test of getAssignee method, of class DelegateActivationMail.
     */
    @Test
    public void testGetAssignee() {
	DelegateActivationMail instance = new DelegateActivationMail();
	String expResult = "1";
	instance.setAssignee(expResult);
	String result = instance.getAssignee();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSuperuser method, of class DelegateActivationMail.
     */
    @Test
    public void testGetSuperuser() {
	DelegateActivationMail instance = new DelegateActivationMail();
	Boolean expResult = true;
	instance.setSuperuser(expResult);
	Boolean result = instance.getSuperuser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getFromUser method, of class DelegateActivationMail.
     */
    @Test
    public void testGetFromUser() {
	DelegateActivationMail instance = new DelegateActivationMail();
	String expResult = "1";
	instance.setFromUser(expResult);
	String result = instance.getFromUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of isActivated method, of class DelegateActivationMail.
     */
    @Test
    public void testIsActivated() {
	DelegateActivationMail instance = new DelegateActivationMail();
	Boolean expResult = true;
	instance.setActivated(expResult);
	Boolean result = instance.isActivated();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class DelegateActivationMail.
     */
    @Test
    public void testProcessMessage() {

	DelegateActivationMail instance = new DelegateActivationMail();
	instance.setActivated(null);

	instance.processMessage();

	assertEquals("User null has assigned you as delegate for null", instance.getMessage());
    }

    @Test
    public void testProcessMessage1() {

	DelegateActivationMail instance = new DelegateActivationMail();
	instance.setActivated(Boolean.TRUE);
	instance.processMessage();
	assertEquals("Dev Manager null has delegated approval authority to null", instance.getMessage());
    }

    @Test
    public void testProcessMessage2() {

	DelegateActivationMail instance = new DelegateActivationMail();
	instance.setSuperuser(Boolean.TRUE);
	instance.processMessage();
	assertEquals("Super User null has got delegation access for null", instance.getMessage());
    }

    @Test
    public void testProcessMessage3() {

	DelegateActivationMail instance = new DelegateActivationMail();
	instance.setActivated(Boolean.FALSE);
	instance.setSuperuser(Boolean.FALSE);
	instance.processMessage();
	assertEquals("Dev Manager null has turned OFF approval authority for null", instance.getMessage());
    }
}
