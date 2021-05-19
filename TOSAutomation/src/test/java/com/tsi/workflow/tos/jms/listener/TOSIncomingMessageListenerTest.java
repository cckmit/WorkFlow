/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.jms.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import javax.jms.Message;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSIncomingMessageListenerTest {

    public TOSIncomingMessageListenerTest() {
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
     * Test of onMessage method, of class TOSIncomingMessageListener.
     */
    @Test
    public void testOnMessage() {
	Message message = null;
	TOSIncomingMessageListener instance = spy(new TOSIncomingMessageListener(mock(com.tsi.workflow.tos.client.TOSClientImpl.class)));
	instance.onMessage(message);
    }

}
