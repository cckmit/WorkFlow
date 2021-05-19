/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class SystemAddActivityMessageTest {

    SystemAddActivityMessage instance;

    public SystemAddActivityMessageTest() {
	instance = new SystemAddActivityMessage(new ImpPlan(), new Implementation(), DataWareHouse.getPlan().getSystemLoadList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @Test
    public void testProcessMessage() {
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testSetArguments() {
	instance.setArguments(DataWareHouse.getPlan().getSystemLoadList().get(0));
	assertTrue(true);
    }

}
