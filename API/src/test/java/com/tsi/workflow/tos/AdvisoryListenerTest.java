/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import javax.jms.JMSException;
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
public class AdvisoryListenerTest {

    public AdvisoryListenerTest() {
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
     * Test of receiveMessage method, of class AdvisoryListener.
     */
    @Test
    public void testReceiveMessage() {
	Message message = mock(Message.class);
	AdvisoryListener realinstance = new AdvisoryListener();
	AdvisoryListener instance = spy(realinstance);
	instance.receiveMessage(message);

    }

    @Test
    public void testReceiveMessageConditionBlock() {
	Message message = mock(Message.class);
	AdvisoryListener realinstance = new AdvisoryListener();
	AdvisoryListener instance = spy(realinstance);
	try {

	    doReturn(1).when(message).getIntProperty(any());

	    instance.receiveMessage(message);
	} catch (JMSException e) {

	}
    }

    @Test
    public void testReceiveMessageException() {
	Message message = mock(Message.class);
	AdvisoryListener realinstance = new AdvisoryListener();
	AdvisoryListener instance = spy(realinstance);
	try {

	    doThrow(new JMSException("", "")).when(message).getIntProperty(any());

	    instance.receiveMessage(message);
	} catch (JMSException e) {

	}
    }
}
