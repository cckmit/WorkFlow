/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Level;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class WFLOGGERTest {

    public WFLOGGERTest() {
    }

    @Test
    public void testLOG_3args() {
	Class pClass = WFLOGGERTest.class;
	Level pLevel = Level.INFO;
	String pMessage = "";
	WFLOGGER.LOG(pClass, pLevel, pMessage);
	assertTrue(true);
    }

    @Test
    public void testLOG_4args() {
	Class pClass = WFLOGGERTest.class;
	Level pLevel = Level.INFO;
	String pMessage = "";
	Throwable exception = new Exception();
	WFLOGGER.LOG(pClass, pLevel, pMessage, exception);
	assertTrue(true);
    }

    @Test
    public void testEncode() {
	String message = "test";
	String expResult = "test";
	String result = WFLOGGER.encode(message);
	assertEquals(expResult, result);
	WFLOGGER wfLogger = new WFLOGGER();
    }

}
