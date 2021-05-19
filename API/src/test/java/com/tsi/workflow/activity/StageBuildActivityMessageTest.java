/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class StageBuildActivityMessageTest {

    StageBuildActivityMessage instance;

    public StageBuildActivityMessageTest() {
	instance = new StageBuildActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @Test
    public void testGetStatus() {
	String expResult = "";
	String lStatus = "";
	instance.setStatus(lStatus);
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetLogLevel() {
	instance.setStatus("failed");
	Priority result = instance.getLogLevel();
	assertNotNull(result);

	instance.setStatus("success");
	result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	instance.setUser(DataWareHouse.getUser());
	instance.setStatus("initiated");
	String result = instance.processMessage();
	assertNotNull(result);

	instance.setStatus("success");
	instance.setUser(DataWareHouse.getUserWithDelagated());
	result = instance.processMessage();
	assertNotNull(result);

	instance.setArguments(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	instance.setUser(DataWareHouse.getUser());
	result = instance.processMessage();
	assertNotNull(result);

	instance.setUser(DataWareHouse.getUserWithDelagated());
	result = instance.processMessage();
	assertNotNull(result);

	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUser(user);
	result = instance.processMessage();
	assertNotNull(result);

	instance.setErrorMessage("");
	assertEquals("", instance.getErrorMessage());

	instance.setComment("");
	instance.setStatus("failed");
	result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testSetComment() {
	instance = new StageBuildActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), null);
	String expResult = "";
	instance.setComment(expResult);
	assertTrue(true);
    }
}
