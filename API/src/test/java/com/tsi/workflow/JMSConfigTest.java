/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.utils.Constants;
import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class JMSConfigTest {

    public JMSConfigTest() {
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
     * Test of jmsListenerContainerFactory method, of class JMSConfig.
     */
    @Test
    public void testJmsListenerContainerFactory() throws Exception {

	JMSConfig instance = new JMSConfig();
	DefaultJmsListenerContainerFactory jmsListenerContainerFactory = instance.jmsListenerContainerFactory();
	Field field = ReflectionUtils.findField(DefaultJmsListenerContainerFactory.class, "concurrency");
	field.setAccessible(true);
	assertEquals("1-1", field.get(jmsListenerContainerFactory));

    }

    /**
     * Test of AppToTOSQueue method, of class JMSConfig.
     */
    @Test
    public void testAppToTOSQueue() throws Exception {

	JMSConfig instance = new JMSConfig();
	JmsTemplate result = instance.AppToTOSQueue();
	Field field = ReflectionUtils.findField(JmsTemplate.class, "defaultDestination");
	field.setAccessible(true);
	assertEquals(Constants.TOS_QUEUE_APP_TOS, field.get(result));

    }

    /**
     * Test of AppToTOSIPQueue method, of class JMSConfig.
     */
    @Test
    public void testAppToTOSIPQueue() throws Exception {

	JMSConfig instance = new JMSConfig();

	JmsTemplate result = instance.AppToTOSIPQueue();
	Field field = ReflectionUtils.findField(JmsTemplate.class, "defaultDestination");
	field.setAccessible(true);
	assertNotNull(Constants.TOS_QUEUE_APP_TOS, field.get(result));

    }

}
