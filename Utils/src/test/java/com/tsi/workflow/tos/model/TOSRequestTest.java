/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.model;

import com.tsi.workflow.User;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TOSRequestTest extends TestCase {

    public TOSRequestTest(String testName) {
	super(testName);
    }

    public void testGetSystem() {
	TOSRequest instance = new TOSRequest();
	String expResult = "";
	String system = "";
	instance.setSystem(system);
	String result = instance.getSystem();
	assertEquals(expResult, result);
    }

    public void testGetOldStatus() {
	TOSRequest instance = new TOSRequest();
	String expResult = "";
	String oldStatus = "";
	instance.setOldStatus(oldStatus);
	String result = instance.getOldStatus();
	assertEquals(expResult, result);
    }

    public void testGetUser() {
	TOSRequest instance = new TOSRequest();
	User expResult = null;
	User user = null;
	instance.setUser(user);
	User result = instance.getUser();
	assertEquals(expResult, result);
    }

    public void testGetId() {
	TOSRequest instance = new TOSRequest();
	int expResult = 0;
	int id = 0;
	instance.setId(id);
	int result = instance.getId();
	assertEquals(expResult, result);
    }

    public void testGetPrimaryAddress() {
	TOSRequest instance = new TOSRequest();
	String expResult = "";
	String primaryAddress = "";
	instance.setPrimaryAddress(primaryAddress);
	String result = instance.getPrimaryAddress();
	assertEquals(expResult, result);
    }

    public void testGetSecondaryAddress() {
	TOSRequest instance = new TOSRequest();
	String expResult = "";
	String secondaryAddress = "";
	instance.setSecondaryAddress(secondaryAddress);
	String result = instance.getSecondaryAddress();
	assertEquals(expResult, result);
    }

    public void testGetCommand() {
	TOSRequest instance = new TOSRequest();
	String expResult = "";
	String command = "";
	instance.setCommand(command);
	String result = instance.getCommand();
	assertEquals(expResult, result);
    }

    @Test
    public void testTosRequest() {
	TOSRequest instance = new TOSRequest();
	instance.setRejectReason("Rejected");
	assertEquals("Rejected", instance.getRejectReason());
	instance.setHost("110.102.234.201");
	assertEquals("110.102.234.201", instance.getHost());
    }
}
