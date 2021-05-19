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
public class TOSResultTest extends TestCase {

    public TOSResultTest(String testName) {
	super(testName);
    }

    public void testConstructors() {
	TOSResult tosResult = new TOSResult();
	assertNotNull(tosResult);
	tosResult = new TOSResult("", null, 0, null, null, null, true);
	assertNotNull(tosResult);
	tosResult = new TOSResult(null, 0, null, null, null, true);
	assertNotNull(tosResult);
	tosResult = new TOSResult(null, null, 0, null, null, null, true);
	assertNotNull(tosResult);
    }

    public void testIsLast() {
	TOSResult instance = new TOSResult();
	boolean expResult = false;
	boolean last = false;
	instance.setLast(last);
	boolean result = instance.isLast();
	assertEquals(expResult, result);
    }

    public void testGetIpAddress() {
	TOSResult instance = new TOSResult();
	String expResult = "";
	String ipAddress = "";
	instance.setIpAddress(ipAddress);
	String result = instance.getIpAddress();
	assertEquals(expResult, result);
    }

    public void testGetOldStatus() {
	TOSResult instance = new TOSResult();
	String expResult = "";
	String oldStatus = "";
	instance.setOldStatus(oldStatus);
	String result = instance.getOldStatus();
	assertEquals(expResult, result);
    }

    public void testGetId() {
	TOSResult instance = new TOSResult();
	Integer expResult = null;
	Integer id = null;
	instance.setId(id);
	Integer result = instance.getId();
	assertEquals(expResult, result);
    }

    public void testGetUser() {
	TOSResult instance = new TOSResult();
	User expResult = null;
	User user = null;
	instance.setUser(user);
	User result = instance.getUser();
	assertEquals(expResult, result);
    }

    public void testGetCommand() {
	TOSResult instance = new TOSResult();
	String expResult = "";
	String command = "";
	instance.setCommand(command);
	String result = instance.getCommand();
	assertEquals(expResult, result);
    }

    public void testGetLoadset() {
	TOSResult instance = new TOSResult();
	String expResult = "";
	String loadset = "";
	instance.setLoadset(loadset);
	String result = instance.getLoadset();
	assertEquals(expResult, result);
    }

    public void testGetReturnValue() {
	TOSResult instance = new TOSResult();
	int expResult = 0;
	int returnValue = 0;
	instance.setReturnValue(returnValue);
	int result = instance.getReturnValue();
	assertEquals(expResult, result);
    }

    public void testGetMessage() {
	TOSResult instance = new TOSResult();
	String expResult = "";
	String message = "";
	instance.setMessage(message);
	String result = instance.getMessage();
	assertEquals(expResult, result);
    }

    @Test
    public void testgetRejectReason() {
	TOSResult instance = new TOSResult();
	String expResult = "Test";
	instance.setRejectReason(expResult);
	instance.setHost(expResult);
	assertEquals(expResult, instance.getRejectReason());
	assertEquals(expResult, instance.getHost());
    }

    @Test
    public void testEquals() {
	TOSResult instance = new TOSResult();

	TOSResult DummyResult = new TOSResult();
	instance.setCommand("test");
	DummyResult.setCommand("Test");
	assertEquals(false, instance.equals(DummyResult));
	instance.equals("");

    }

    @Test
    public void testEquals1() {
	TOSResult instance = new TOSResult();

	TOSResult DummyResult = new TOSResult();
	instance.setCommand("test");
	instance.setLoadset("loadset");
	DummyResult.setCommand("test");
	DummyResult.setLoadset("loadset");
	assertEquals(true, instance.equals(DummyResult));
	assertEquals(3556789, instance.hashCode());
    }

}
