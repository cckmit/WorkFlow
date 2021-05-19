/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TestResultsActivityMessageTest {

    TestResultsActivityMessage instance;

    public TestResultsActivityMessageTest() {
	instance = new TestResultsActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	instance.setArguments(null);
	String result = instance.processMessage();
	assertNotNull(result);
    }

}
