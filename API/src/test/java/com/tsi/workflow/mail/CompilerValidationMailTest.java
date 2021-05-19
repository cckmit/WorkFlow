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
public class CompilerValidationMailTest {

    public CompilerValidationMailTest() {
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
     * Test of getIpAddress method, of class CompilerValidationMail.
     */
    @Test
    public void testGetIpAddress() {
	CompilerValidationMail instance = new CompilerValidationMail();
	String expResult = "1";
	instance.setIpAddress(expResult);
	String result = instance.getIpAddress();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSystem method, of class CompilerValidationMail.
     */
    @Test
    public void testGetSystem() {
	CompilerValidationMail instance = new CompilerValidationMail();
	String expResult = "1";
	instance.setSystem(expResult);
	String result = instance.getSystem();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class CompilerValidationMail.
     */
    @Test
    public void testProcessMessage() {

	CompilerValidationMail instance = new CompilerValidationMail();
	instance.processMessage();
    }

}
