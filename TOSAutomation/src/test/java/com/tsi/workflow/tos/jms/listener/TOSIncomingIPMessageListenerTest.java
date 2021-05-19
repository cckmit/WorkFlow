/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.jms.listener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.tos.client.TOSClientImpl;
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
public class TOSIncomingIPMessageListenerTest {

    public TOSIncomingIPMessageListenerTest() {
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
     * Test of onMessage method, of class TOSIncomingIPMessageListener.
     */
    @Test
    public void testOnMessage() {
	try {
	    TextMessage message = mock(TextMessage.class);
	    TOSIncomingIPMessageListener instance = spy(new TOSIncomingIPMessageListener(mock(TOSClientImpl.class)));
	    when(message.getText()).thenReturn(
		    "{\"user\":{\"id\":\"yeshwanth.shenoy\",\"password\":\"FAB97854C1FD4B869134FA07D56464CB838B05EAEA301B6E\",\"displayName\":\"Yeshwanth Shenoy\",\"mailId\":\"yeshwanth.shenoy@travelport.com\",\"role\":[\"DevManager\",\"TechnicalServiceDesk\",\"LoadsControl\",\"Reviewer\",\"Lead\",\"Developer\",\"ToolAdmin\"],\"currentRole\":\"TechnicalServiceDesk\",\"timeZone\":\"Etc/GMT\",\"delegated\":false,\"allowDelegateMenu\":false,\"delegations\":{}},\"id\":58,\"primaryAddress\":\"xxx.xx.xx.xx:x\",\"secondaryAddress\":\"xxx.xx.xx.xx:x\",\"command\":\"`05AUTO ACT LOADACT0 GC800125 A\",\"oldStatus\":\"DEACTIVATED\",\"system\":\"WSP\"}");
	    instance.onMessage(message);
	} catch (Exception e) {

	}
    }

}
