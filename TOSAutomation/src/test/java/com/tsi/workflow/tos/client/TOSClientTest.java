/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.ibm.tpf.etos.api.ETOSClient;
import com.ibm.tpf.etos.comm.CommunicationBlock;
import com.tsi.workflow.tos.model.TOSResult;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TOSClientTest {

    TOSClient instance;

    public TOSClientTest() {
	instance = new TOSClient(new TOSClientImpl());
    }

    @Test
    public void testGetOverflow() {
	boolean expResult = false;
	boolean overflow = false;
	instance.setOverflow(overflow);
	boolean result = instance.getOverflow();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCommunicationBlock() {
	String ipAddress = "0.0.0.0:0";
	String tA = "";
	String maxTa = "";
	String system = "";
	CommunicationBlock result = instance.getCommunicationBlock(ipAddress, tA, maxTa, system);
	assertNotNull(result);
    }

    @Test
    public void testvalidateLogin() {
	boolean result = instance.validateLogin(ETOSClient.ETOS_SUCCESS);
	assertEquals(true, result);
	result = instance.validateLogin(ETOSClient.ETOS_ERROR);
	assertEquals(false, result);
	result = instance.validateLogin(-1);
	assertEquals(false, result);
    }

    @Test
    public void testsetListener() {
	instance.setListener("", "");
	instance.close();
	assertTrue(true);
    }

    @Test
    public void testValidateCommands() {
	TOSResult result_2 = new TOSResult();
	boolean expResult = false;
	boolean result = instance.validateCommands(ETOSClient.ETOS_SUCCESS, result_2);
	assertEquals(true, result);
	result = instance.validateCommands(ETOSClient.ETOS_BELOW_1052, result_2);
	assertEquals(expResult, result);
	result = instance.validateCommands(ETOSClient.ETOS_INVALID_ROUTE, result_2);
	assertEquals(expResult, result);
	result = instance.validateCommands(ETOSClient.ETOS_NO_OPEN, result_2);
	assertEquals(expResult, result);
	result = instance.validateCommands(ETOSClient.ETOS_RECONN_IN_PROGRESS, result_2);
	assertEquals(expResult, result);
	result = instance.validateCommands(ETOSClient.ETOS_SERVER_CMD_REJECT, result_2);
	assertEquals(expResult, result);
	result = instance.validateCommands(ETOSClient.ETOS_TPF_CMD_REJECT, result_2);
	assertEquals(expResult, result);
	instance.setOverflow(true);
	result = instance.validateCommands(ETOSClient.ETOS_NO_OPEN, result_2);
	assertEquals(expResult, result);
	result = instance.validateCommands(-1, result_2);
	assertEquals(expResult, result);
    }

}
