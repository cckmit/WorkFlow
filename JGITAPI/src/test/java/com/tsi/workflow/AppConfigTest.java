/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertNotNull;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 *
 * @author USER
 */
public class AppConfigTest {

    public AppConfigTest() {
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
    public void testGITConfig() {
	AppConfig instance = new AppConfig();
	GITConfig result = instance.GITConfig();
	assertNotNull(result);
    }

    @Test
    public void testEnvironmentVariablesConfiguration() {
	AppConfig instance = new AppConfig();
	EnvironmentStringPBEConfig result = instance.environmentVariablesConfiguration();
	assertNotNull(result);
    }

    @Test
    public void testStringEncryptor() {
	AppConfig instance = new AppConfig();
	PooledPBEStringEncryptor result = instance.stringEncryptor();
	assertNotNull(result);
    }

    @Test
    public void testPropertyConfigurer() {
	AppConfig instance = new AppConfig();
	PropertyPlaceholderConfigurer result = instance.propertyConfigurer();
	assertNotNull(result);
    }

}
