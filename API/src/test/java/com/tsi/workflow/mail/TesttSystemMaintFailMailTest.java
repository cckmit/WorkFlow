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
public class TesttSystemMaintFailMailTest {

    public TesttSystemMaintFailMailTest() {
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
     * Test of getSystemName method, of class TesttSystemMaintFailMail.
     */
    @Test
    public void testGetSystemName() {
	TesttSystemMaintFailMail instance = new TesttSystemMaintFailMail();
	String expResult = "APO";
	instance.setSystemName(expResult);
	String result = instance.getSystemName();
	assertEquals(expResult, result);
    }

    /**
     * Test of getIpAddress method, of class TesttSystemMaintFailMail.
     */
    // @Test
    // public void testGetIpAddress() {
    // TesttSystemMaintFailMail instance = new TesttSystemMaintFailMail();
    // String expResult = "1";
    // instance.setIpAddress(expResult);
    // String result = instance.getIpAddress();
    // assertEquals(expResult, result);
    // }

    /**
     * Test of processMessage method, of class TesttSystemMaintFailMail.
     */
    @Test
    public void testProcessMessage() {

	TesttSystemMaintFailMail instance = new TesttSystemMaintFailMail();
	instance.processMessage();
    }

    /**
     * Test of setSystemName method, of class TesttSystemMaintFailMail.
     */
    @Test
    public void testSetSystemName() {

	String systemName = "";
	TesttSystemMaintFailMail instance = new TesttSystemMaintFailMail();
	instance.setSystemName(systemName);
    }

    /**
     * Test of setIpAddress method, of class TesttSystemMaintFailMail.
     */
    // @Test
    // public void testSetIpAddress() {
    //
    // String ipAddress = "";
    // TesttSystemMaintFailMail instance = new TesttSystemMaintFailMail();
    // instance.setIpAddress(ipAddress);
    // }

}
