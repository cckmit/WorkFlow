/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import junit.framework.TestCase;

/**
 *
 * @author USER
 */
public class JSONResponseTest extends TestCase {

    public JSONResponseTest(String testName) {
	super(testName);
    }

    public void testGetMetaData() {
	JSONResponse instance = new JSONResponse();
	Object expResult = null;
	Object pMetaData = null;
	instance.setMetaData(pMetaData);
	Object result = instance.getMetaData();
	assertEquals(expResult, result);
    }

    public void testGetCount() {
	JSONResponse instance = new JSONResponse();
	Long expResult = 0L;
	Long pCount = 0L;
	Integer pCount1 = 0;
	instance.setCount(pCount);
	Long result = instance.getCount();
	assertEquals(expResult, result);
	instance.setCount(pCount1);
	result = instance.getCount();
	assertEquals(expResult, result);
    }

    public void testGetErrorMessage() {
	JSONResponse instance = new JSONResponse();
	String expResult = "";
	String pErrorMessage = "";
	instance.setErrorMessage(pErrorMessage);
	String result = instance.getErrorMessage();
	assertEquals(expResult, result);
    }

    public void testGetStatus() {
	JSONResponse instance = new JSONResponse();
	Boolean expResult = null;
	Boolean status = null;
	instance.setStatus(status);
	Boolean result = instance.getStatus();
	assertEquals(expResult, result);
    }

    public void testGetData() {
	JSONResponse instance = new JSONResponse();
	Object expResult = null;
	Object data = null;
	instance.setData(data);
	Object result = instance.getData();
	assertEquals(expResult, result);
    }

    public void testGetDisplayErrorMessage() {

	JSONResponse instance = new JSONResponse();
	instance.getDisplayErrorMessage();
	instance.setErrorMessage(" ERROR: ");
	String result = instance.getDisplayErrorMessage();
	instance.setErrorMessage(" ERROR: " + System.lineSeparator());
	instance.getDisplayErrorMessage();
    }

    public void testHavingErrorMessage() {
	JSONResponse instance = new JSONResponse();
	boolean expResult = false;
	boolean result = instance.havingErrorMessage();
	assertEquals(expResult, result);
    }

    public void testClearErrorMessage() {
	JSONResponse instance = new JSONResponse();
	instance.clearErrorMessage();
	assertTrue(true);
    }
}
