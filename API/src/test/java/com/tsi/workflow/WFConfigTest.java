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
public class WFConfigTest {

    public WFConfigTest() {
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
     * Test of getServiceUserID method, of class WFConfig.
     */
    @Test
    public void testGetServiceUserID() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setServiceUserID(expResult);
	String result = instance.getServiceUserID();
	assertEquals(expResult, result);
    }

    /**
     * Test of getServicePassword method, of class WFConfig.
     */
    @Test
    public void testGetServicePassword() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setServicePassword(expResult);
	String result = instance.getServicePassword();
	assertEquals(expResult, result);
    }

    /**
     * Test of getBuildLogDir method, of class WFConfig.
     */
    @Test
    public void testGetBuildLogDir() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setBuildLogDir(expResult);
	String result = instance.getBuildLogDir();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTestResultsDir method, of class WFConfig.
     */
    @Test
    public void testGetTestResultsDir() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setTestResultsDir(expResult);
	String result = instance.getTestResultsDir();
	assertEquals(expResult, result);
    }

    /**
     * Test of getIbmVanillaDirectory method, of class WFConfig.
     */
    @Test
    public void testGetIbmVanillaDirectory() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setIbmVanillaDirectory(expResult);
	String result = instance.getIbmVanillaDirectory();
	assertEquals(expResult, result);
    }

    /**
     * Test of getAttachmentDirectory method, of class WFConfig.
     */
    @Test
    public void testGetAttachmentDirectory() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setAttachmentDirectory(expResult);
	String result = instance.getAttachmentDirectory();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLoadApprovalDirectory method, of class WFConfig.
     */
    @Test
    public void testGetLoadApprovalDirectory() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setLoadApprovalDirectory(expResult);
	String result = instance.getLoadApprovalDirectory();
	assertEquals(expResult, result);
    }

    /**
     * Test of getDevCentreMailID method, of class WFConfig.
     */
    @Test
    public void testGetDevCentreMailID() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setDevCentreMailID(expResult);
	String result = instance.getDevCentreMailID();
	assertEquals(expResult, result);
    }

    /**
     * Test of getTosJMSUrl method, of class WFConfig.
     */
    @Test
    public void testGetTosJMSUrl() {
	WFConfig instance = new WFConfig();
	String expResult = "1";
	instance.setTosJMSUrl(expResult);
	String result = instance.getTosJMSUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testgetProfileName() {
	WFConfig instance = new WFConfig();
	String profileName = instance.getProfileName();
	instance.setProfileName(profileName);
	instance.getDVLBuildLogDir();
	instance.getSTGBuildLogDir();
    }

}
