/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import java.util.regex.Pattern;
import org.apache.log4j.Priority;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class DbcrValidationMessageTest {

    public DbcrValidationMessageTest() {
    }

    @Test
    public void testSetDbcrName() {
	String dbcrName = "";
	DbcrValidationMessage instance = new DbcrValidationMessage(null, null);
	instance.setDbcrName(dbcrName);
	assertTrue(true);
    }

    @Test
    public void testSetEnvironment() {
	String environment = "";
	DbcrValidationMessage instance = new DbcrValidationMessage(null, null);
	instance.setEnvironment(environment);
	assertTrue(true);
    }

    @Test
    public void testSetSystemName() {
	String systemName = "";
	DbcrValidationMessage instance = new DbcrValidationMessage(null, null);
	instance.setSystemName(systemName);
	assertTrue(true);
    }

    @Test
    public void testProcessMessage() {
	DbcrValidationMessage instance = new DbcrValidationMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	String result = instance.processMessage();
	assertTrue(Pattern.matches("Associated DBCR .* for .* needs to be applied in .* before deploying loadset.", result));
    }

    @Test
    public void testGetLogLevel() {
	DbcrValidationMessage instance = new DbcrValidationMessage(null, null);
	Priority exresult = Priority.INFO;
	Priority result = instance.getLogLevel();
	Assert.assertEquals(exresult, result);
    }

    @Test
    public void testSetArguments() {
	IBeans[] beans = null;
	DbcrValidationMessage instance = new DbcrValidationMessage(null, null);
	instance.setArguments(beans);
	assertTrue(true);
    }

}
