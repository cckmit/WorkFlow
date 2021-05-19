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
public class GITConfigTest {

    public GITConfigTest() {
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
     * Test of getGitBasePath method, of class GITConfig.
     */
    @Test
    public void testGetGitBasePath() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setGitBasePath(expResult);
	String result = instance.getGitBasePath();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitProdPath method, of class GITConfig.
     */
    @Test
    public void testGetGitProdPath() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setGitProdPath(expResult);
	String result = instance.getGitProdPath();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitSourcePath method, of class GITConfig.
     */
    @Test
    public void testGetGitSourcePath() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setGitSourcePath(expResult);
	String result = instance.getGitSourcePath();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitDerivedPath method, of class GITConfig.
     */
    @Test
    public void testGetGitDerivedPath() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setGitDerivedPath(expResult);
	String result = instance.getGitDerivedPath();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitHost method, of class GITConfig.
     */
    @Test
    public void testGetGitHost() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setGitHost(expResult);
	String result = instance.getGitHost();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitDataPort method, of class GITConfig.
     */
    @Test
    public void testGetGitDataPort() {
	GITConfig instance = new GITConfig();
	Integer expResult = 1;
	instance.setGitDataPort(expResult);
	Integer result = instance.getGitDataPort();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitblitRestUrl method, of class GITConfig.
     */
    @Test
    public void testGetGitblitRestUrl() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setGitblitRestUrl(expResult);
	String result = instance.getGitblitRestUrl();
	assertEquals(expResult, result);
    }

    /**
     * Test of getJGitRestUrl method, of class GITConfig.
     */
    @Test
    public void testGetJGitRestUrl() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setJGitRestUrl(expResult);
	String result = instance.getJGitRestUrl();
	assertEquals(expResult, result);
    }

    /**
     * Test of getServiceUserID method, of class GITConfig.
     */
    @Test
    public void testGetServiceUserID() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setServiceUserID(expResult);
	String result = instance.getServiceUserID();
	assertEquals(expResult, result);
    }

    /**
     * Test of getServiceSecret method, of class GITConfig.
     */
    @Test
    public void testGetServiceSecret() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setServiceSecret(expResult);
	String result = instance.getServiceSecret();
	assertEquals(expResult, result);
    }

    /**
     * Test of getGitSshPort method, of class GITConfig.
     */
    @Test
    public void testGetGitSshPort() {
	GITConfig instance = new GITConfig();
	Integer expResult = 1;
	instance.setGitSshPort(expResult);
	Integer result = instance.getGitSshPort();
	assertEquals(expResult, result);
    }

    /**
     * Test of getSSHKeysPath method, of class GITConfig.
     */
    @Test
    public void testGetSSHKeysPath() {
	GITConfig instance = new GITConfig();
	String expResult = "1";
	instance.setSSHKeysPath(expResult);
	String result = instance.getSSHKeysPath();
	assertEquals(expResult, result);
    }

    @Test
    public void testgetGitblitTicketUrl() {
	GITConfig instance = new GITConfig();
	String gitblit = instance.getGitblitTicketUrl();
	instance.setGitblitTicketUrl(gitblit);
	String loadBalance = instance.getWfLoadBalancerHost();
	instance.setWfLoadBalancerHost(loadBalance);
    }

}
