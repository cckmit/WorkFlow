/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class QATestingActivityMessageTest {

    QATestingActivityMessage instance;

    public QATestingActivityMessageTest() {
	instance = new QATestingActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	instance.setUser(user);
	String result = instance.processMessage();
	instance.setArguments(null);
	assertNotNull(result);
    }

}
