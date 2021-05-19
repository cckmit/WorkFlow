/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSConfigTest {

    public TOSConfigTest() {
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
     * Test of getTosFilePath method, of class TOSConfig.
     */
    @Test
    public void testGetTosFilePath() {

	TOSConfig instance = new TOSConfig();
	String expResult = null;
	String result = instance.getTosFilePath();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTosSystemType method, of class TOSConfig.
     */
    @Test
    public void testGetTosSystemType() {

	TOSConfig instance = new TOSConfig();
	String expResult = null;
	String result = instance.getTosSystemType();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTosFileDomain method, of class TOSConfig.
     */
    @Test
    public void testGetTosFileDomain() {

	TOSConfig instance = new TOSConfig();
	String expResult = null;
	String result = instance.getTosFileDomain();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTosFileUser method, of class TOSConfig.
     */
    @Test
    public void testGetTosFileUser() {

	TOSConfig instance = new TOSConfig();
	String expResult = null;
	String result = instance.getTosFileUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTosFilePassword method, of class TOSConfig.
     */
    @Test
    public void testGetTosFilePassword() {

	TOSConfig instance = new TOSConfig();
	String expResult = null;
	String result = instance.getTosFilePassword();
	assertEquals(expResult, result);
    }

    /**
     * Test of setTosSystemType method, of class TOSConfig.
     */
    @Test
    public void testSetTosSystemType() {

	String tosSystemType = "";
	TOSConfig instance = new TOSConfig();
	instance.setTosSystemType(tosSystemType);
	assertNotNull(instance.getTosSystemType());
    }

    /**
     * Test of setTosFileDomain method, of class TOSConfig.
     */
    @Test
    public void testSetTosFileDomain() {

	String tosFileDomain = "";
	TOSConfig instance = new TOSConfig();
	instance.setTosFileDomain(tosFileDomain);
	assertNotNull(instance.getTosFileDomain());
    }

    /**
     * Test of setTosFileUser method, of class TOSConfig.
     */
    @Test
    public void testSetTosFileUser() {

	String tosFileUser = "";
	TOSConfig instance = new TOSConfig();
	instance.setTosFileUser(tosFileUser);
	assertNotNull(instance.getTosFileUser());
    }

    /**
     * Test of setTosFilePassword method, of class TOSConfig.
     */
    @Test
    public void testSetTosFilePassword() {

	String tosFilePassword = "";
	TOSConfig instance = new TOSConfig();
	instance.setTosFilePassword(tosFilePassword);
	assertNotNull(instance.getTosFilePassword());
    }

    /**
     * Test of setTosFilePath method, of class TOSConfig.
     */
    @Test
    public void testSetTosFilePath() {

	String tosFilePath = "";
	TOSConfig instance = new TOSConfig();
	instance.setTosFilePath(tosFilePath);
	assertNotNull(instance.getTosFilePath());
    }

    /**
     * Test of getTosServerId method, of class TOSConfig.
     */
    @Test
    public void testGetTosServerId() {
	TOSConfig instance = new TOSConfig();
	String expResult = null;
	String result = instance.getTosServerId();
	assertEquals(expResult, result);
    }

    /**
     * Test of setTosServerId method, of class TOSConfig.
     */
    @Test
    public void testSetTosServerId() {
	String tosServerId = "";
	TOSConfig instance = new TOSConfig();
	instance.setTosServerId(tosServerId);
    }

}
