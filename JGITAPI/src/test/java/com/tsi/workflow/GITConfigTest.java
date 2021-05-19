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
 * @author USER
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

    @Test
    public void testGetGitBasePath() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String gitBasePath = "";
	instance.setGitBasePath(gitBasePath);
	String result = instance.getGitBasePath();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitProdPath() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String gitProdPath = "";
	instance.setGitProdPath(gitProdPath);
	String result = instance.getGitProdPath();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitSourcePath() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String gitSourcePath = "";
	instance.setGitSourcePath(gitSourcePath);
	String result = instance.getGitSourcePath();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitDerivedPath() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String gitDerivedPath = "";
	instance.setGitDerivedPath(gitDerivedPath);
	String result = instance.getGitDerivedPath();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitHost() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String gitHost = "";
	instance.setGitHost(gitHost);
	String result = instance.getGitHost();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitDataPort() {
	GITConfig instance = new GITConfig();
	Integer expResult = null;
	Integer gitDataPort = null;
	instance.setGitDataPort(gitDataPort);
	Integer result = instance.getGitDataPort();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitblitRestUrl() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String gitblitRestUrl = "";
	instance.setGitblitRestUrl(gitblitRestUrl);
	String result = instance.getGitblitRestUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetJGitRestUrl() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String JGitRestUrl = "";
	instance.setJGitRestUrl(JGitRestUrl);
	String result = instance.getJGitRestUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetServiceUserID() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String serviceUserID = "";
	instance.setServiceUserID(serviceUserID);
	String result = instance.getServiceUserID();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetServiceSecret() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String serviceSecret = "";
	instance.setServiceSecret(serviceSecret);
	String result = instance.getServiceSecret();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitSshPort() {
	GITConfig instance = new GITConfig();
	Integer expResult = null;
	Integer gitSshPort = null;
	instance.setGitSshPort(gitSshPort);
	Integer result = instance.getGitSshPort();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSSHKeysPath() {
	GITConfig instance = new GITConfig();
	String expResult = "";
	String SSHKeysPath = "";
	instance.setSSHKeysPath(SSHKeysPath);
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
