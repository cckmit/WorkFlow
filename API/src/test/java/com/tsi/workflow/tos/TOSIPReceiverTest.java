/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.tos.model.TOSResult;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.TextMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSIPReceiverTest {

    public TOSIPReceiverTest() {
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
     * Test of receiveMessage method, of class TOSIPReceiver.
     */
    @Test
    public void testReceiveMessage() throws JsonProcessingException {
	try {
	    TextMessage message = mock(TextMessage.class);
	    TOSIPReceiver instance = spy(new TOSIPReceiver());
	    ReflectionTestUtils.setField(instance, "tosConfig", mock(TOSConfig.class));
	    ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	    ReflectionTestUtils.setField(instance, "tosIpMap", mock(ConcurrentHashMap.class));

	    when(message.getText()).thenReturn(new ObjectMapper().writeValueAsString(DataWareHouse.getTOSResult()));
	    instance.receiveMessage(message);

	    when(instance.tosConfig.getTosServerId()).thenReturn("");
	    instance.receiveMessage(message);

	    TOSResult res = DataWareHouse.getTOSResult();
	    res.setReturnValue(1);
	    when(message.getText()).thenReturn(new ObjectMapper().writeValueAsString(res));
	    instance.receiveMessage(message);

	    TOSResult res1 = DataWareHouse.getTOSResult();
	    res1.setLast(false);
	    when(message.getText()).thenReturn(new ObjectMapper().writeValueAsString(res1));
	    instance.receiveMessage(message);

	    res1.setLast(true);
	    res1.setIpAddress(null);
	    when(message.getText()).thenReturn(new ObjectMapper().writeValueAsString(res1));
	    instance.receiveMessage(message);

	    res1.setHost(null);
	    when(message.getText()).thenReturn(new ObjectMapper().writeValueAsString(res1));
	    instance.receiveMessage(message);
	} catch (JMSException ex) {
	    Logger.getLogger(TOSIPReceiverTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
