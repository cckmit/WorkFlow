/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import java.lang.reflect.Field;
import java.util.concurrent.BlockingQueue;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author yeshwanth.shenoy
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

    /**
     * Test of LDAPAuthenticatorImpl method, of class AppConfig.
     */
    @Test
    public void testLDAPAuthenticatorImpl() {
	AppConfig instance = new AppConfig();
	LDAPAuthenticatorImpl result = instance.LDAPAuthenticatorImpl();
	assertNotNull(result);
    }

    /**
     * Test of GITConfig method, of class AppConfig.
     */
    @Test
    public void testGITConfig() {
	AppConfig instance = new AppConfig();
	GITConfig result = instance.GITConfig();
	assertNotNull(result);
    }

    /**
     * Test of WFConfig method, of class AppConfig.
     */
    @Test
    public void testWFConfig() {
	AppConfig instance = new AppConfig();
	WFConfig result = instance.WFConfig();
	assertNotNull(result);
    }

    /**
     * Test of TOSConfig method, of class AppConfig.
     */
    @Test
    public void testTOSConfig() {
	AppConfig instance = new AppConfig();
	TOSConfig result = instance.TOSConfig();
	assertNotNull(result);
    }

    /**
     * Test of JenkinsConfig method, of class AppConfig.
     */
    @Test
    public void testJenkinsConfig() {
	AppConfig instance = new AppConfig();
	JenkinsConfig result = instance.JenkinsConfig();
	assertNotNull(result);
    }

    /**
     * Test of BPMConfig method, of class AppConfig.
     */
    @Test
    public void testBPMConfig() {
	AppConfig instance = new AppConfig();
	BPMConfig result = instance.BPMConfig();
	assertNotNull(result);
    }

    /**
     * Test of MailConfig method, of class AppConfig.
     */
    @Test
    public void testMailConfig() {
	AppConfig instance = new AppConfig();
	MailConfig result = instance.MailConfig();
	assertNotNull(result);
    }

    /**
     * Test of GITUtils method, of class AppConfig.
     */
    @Test
    public void testGITUtils() {
	AppConfig instance = new AppConfig();
	JGitClientUtils result = instance.GITUtils();
	assertNotNull(result);
    }

    // /**
    // * Test of JenkinsClient method, of class AppConfig.
    // */
    // @Test
    // public void testJenkinsClient() throws Exception {
    //
    // AppConfig instance = new AppConfig();
    // JenkinsClient expResult = null;
    // JenkinsClient result = instance.JenkinsClient();
    // assertEquals(expResult, result);
    // }
    /**
     * Test of BPMClientUtils method, of class AppConfig.
     */
    @Test
    public void testBPMClientUtils() {

	AppConfig instance = new AppConfig();
	BPMClientUtils result = instance.BPMClientUtils();
	assertNotNull(result);
    }

    /**
     * Test of GitBlitClientUtils method, of class AppConfig.
     */
    @Test
    public void testGitBlitClientUtils() {

	AppConfig instance = new AppConfig();
	GitBlitClientUtils result = instance.GitBlitClientUtils();
	assertNotNull(result);
    }

    // /**
    // * Test of MailUtil method, of class AppConfig.
    // */
    // @Test
    // public void testMailUtil() {
    //
    // AppConfig instance = new AppConfig();
    // MailUtil expResult = null;
    // MailUtil result = instance.MailUtil();
    // assertEquals(expResult, result);
    // }
    /**
     * Test of MailMessageFactory method, of class AppConfig.
     */
    @Test
    public void testMailMessageFactory() {

	AppConfig instance = new AppConfig();
	MailMessageFactory result = instance.MailMessageFactory();
	assertNotNull(result);
    }

    /**
     * Test of mailQueue method, of class AppConfig.
     */
    @Test
    public void testMailQueue() {

	AppConfig instance = new AppConfig();
	BlockingQueue<MailMessage> result = instance.mailQueue();
	assertNotNull(result);
    }

    // /**
    // * Test of connectionFactory method, of class AppConfig.
    // */
    // @Test
    // public void testConnectionFactory() {
    //
    // AppConfig instance = new AppConfig();
    // ActiveMQConnectionFactory expResult = null;
    // ActiveMQConnectionFactory result = instance.connectionFactory();
    // assertEquals(expResult, result);
    // }
    // /**
    // * Test of jmsListenerContainerFactory method, of class AppConfig.
    // */
    // @Test
    // public void testJmsListenerContainerFactory() {
    //
    // AppConfig instance = new AppConfig();
    // DefaultJmsListenerContainerFactory expResult = null;
    // DefaultJmsListenerContainerFactory result =
    // instance.jmsListenerContainerFactory();
    // assertEquals(expResult, result);
    // }
    // /**
    // * Test of AppToTOSQueue method, of class AppConfig.
    // */
    // @Test
    // public void testAppToTOSQueue() {
    //
    // AppConfig instance = new AppConfig();
    // JmsTemplate expResult = null;
    // JmsTemplate result = instance.AppToTOSQueue();
    // assertEquals(expResult, result);
    // }
    // /**
    // * Test of AppToTOSIPQueue method, of class AppConfig.
    // */
    // @Test
    // public void testAppToTOSIPQueue() {
    //
    // AppConfig instance = new AppConfig();
    // JmsTemplate expResult = null;
    // JmsTemplate result = instance.AppToTOSIPQueue();
    // assertEquals(expResult, result);
    // }
    /**
     * Test of environmentVariablesConfiguration method, of class AppConfig.
     */
    @Test
    public void testEnvironmentVariablesConfiguration() {

	AppConfig instance = new AppConfig();
	EnvironmentStringPBEConfig result = instance.environmentVariablesConfiguration();
	assertEquals("PBEWithMD5AndDES", result.getAlgorithm());
    }

    /**
     * Test of stringEncryptor method, of class AppConfig.
     */
    @Test
    public void testStringEncryptor() throws Exception {

	AppConfig instance = new AppConfig();
	PooledPBEStringEncryptor result = instance.stringEncryptor();
	Field field = ReflectionUtils.findField(PooledPBEStringEncryptor.class, "config");
	field.setAccessible(true);
	EnvironmentStringPBEConfig config = (EnvironmentStringPBEConfig) field.get(result);
	assertEquals("PBEWithMD5AndDES", config.getAlgorithm());

    }

    /**
     * Test of propertyConfigurer method, of class AppConfig.
     */
    @Test
    public void testPropertyConfigurer() {

	AppConfig instance = new AppConfig();
	PropertyPlaceholderConfigurer result = instance.propertyConfigurer();
	assertNotNull(result);
    }

}
