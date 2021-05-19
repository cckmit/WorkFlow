/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import org.apache.log4j.Priority;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class CheckoutActivityMessageTest {

    CheckoutActivityMessage instance;

    public CheckoutActivityMessageTest() {
	instance = new CheckoutActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), DataWareHouse.getPlan().getCheckoutSegmentsList().get(0));
	instance.setUser(DataWareHouse.getUser());

    }

    @Test
    public void testGetLogLevel() {
	Priority exresult = Priority.INFO;
	Priority result = instance.getLogLevel();
	Assert.assertEquals(exresult, result);
    }

    @Test
    public void testProcessMessage() {

	instance.setNewFile(true);
	String result = instance.processMessage();
	assertNotNull(result);
	instance.setNewFile(true);
	instance.setPopulate(false);
	result = instance.processMessage();
	assertNotNull(result);
	instance.setNewFile(false);
	instance.setPopulate(true);
	result = instance.processMessage();
	assertNotNull(result);
	instance.setNewFile(false);
	instance.setPopulate(false);
	instance.setCommit(true);
	result = instance.processMessage();
	assertNotNull(result);
	instance.setNewFile(false);
	instance.setPopulate(false);
	instance.setCommit(false);
	instance.setDeleteFile(true);
	result = instance.processMessage();
	assertNotNull(result);
	instance.setNewFile(false);
	instance.setPopulate(false);
	instance.setCommit(false);
	instance.setDeleteFile(false);
	result = instance.processMessage();
	assertNotNull(result);
    }

}
