/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class MailConfigTest {

    public MailConfigTest() {
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
     * Test of getAzureId method, of class MailConfig.
     */
    @Test
    public void testGetAzureId() {

	MailConfig instance = new MailConfig();
	String expResult = null;
	String result = instance.getAzureId();
	assertEquals(expResult, result);

    }

    /**
     * Test of setAzureId method, of class MailConfig.
     */
    @Test
    public void testSetAzureId() {

	String azureId = "";
	MailConfig instance = new MailConfig();
	instance.setAzureId(azureId);

    }

    /**
     * Test of getAzureURL method, of class MailConfig.
     */
    @Test
    public void testGetAzureURL() {

	MailConfig instance = new MailConfig();
	String expResult = null;
	String result = instance.getAzureURL();
	assertNull(expResult, result);

    }

    /**
     * Test of setAzureURL method, of class MailConfig.
     */
    @Test
    public void testSetAzureURL() {

	String azureURL = "";
	MailConfig instance = new MailConfig();
	instance.setAzureURL(azureURL);

    }

    /**
     * Test of getAzureKey method, of class MailConfig.
     */
    @Test
    public void testGetAzureKey() {

	MailConfig instance = new MailConfig();
	String expResult = null;
	String result = instance.getAzureKey();
	assertEquals(expResult, result);

    }

    /**
     * Test of setAzureKey method, of class MailConfig.
     */
    @Test
    public void testSetAzureKey() {

	String azureKey = "";
	MailConfig instance = new MailConfig();
	instance.setAzureKey(azureKey);
    }

    /**
     * Test of getFromMailId method, of class MailConfig.
     */
    @Test
    public void testGetFromMailId() {
	MailConfig instance = new MailConfig();
	;

	String result = instance.getFromMailId();
	assertNull(result);

    }

    /**
     * Test of setFromMailId method, of class MailConfig.
     */
    @Test
    public void testSetFromMailId() {

	String fromMailId = "";
	MailConfig instance = new MailConfig();
	instance.setFromMailId(fromMailId);

    }

}
