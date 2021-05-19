/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

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
public class JenkinsConfigTest {

    public JenkinsConfigTest() {
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
     * Test of getURL method, of class JenkinsConfig.
     */
    @Test
    public void testGetURL() {

	JenkinsConfig instance = new JenkinsConfig();
	String expResult = "1";
	instance.setURL(expResult);
	String result = instance.getURL();
	assertEquals(expResult, result);
    }

    /**
     * Test of getServiceUser method, of class JenkinsConfig.
     */
    @Test
    public void testGetServiceUser() {

	JenkinsConfig instance = new JenkinsConfig();
	String expResult = "1";
	instance.setServiceUser(expResult);
	String result = instance.getServiceUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getServicePassword method, of class JenkinsConfig.
     */
    @Test
    public void testGetServicePassword() {

	JenkinsConfig instance = new JenkinsConfig();
	String expResult = "1";
	instance.setServicePassword(expResult);
	String result = instance.getServicePassword();
	assertEquals(expResult, result);
    }
}
