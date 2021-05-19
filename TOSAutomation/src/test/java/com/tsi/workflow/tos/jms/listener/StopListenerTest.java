/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.jms.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class StopListenerTest {

    public StopListenerTest() {
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
     * Test of onMessage method, of class StopListener.
     */
    @Test
    public void testOnMessage() {
	try {
	    StopListener instance = spy(new StopListener());
	    TextMessage message = mock(TextMessage.class);
	    when(message.getText()).thenReturn("Exit");
	    instance.onMessage(message);
	    when(message.getText()).thenThrow(JMSException.class);
	    instance.onMessage(message);
	} catch (JMSException ex) {
	    Logger.getLogger(StopListenerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
