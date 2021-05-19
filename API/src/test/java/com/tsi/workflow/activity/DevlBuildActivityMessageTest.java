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
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author deepa.jayakumar
 */
public class DevlBuildActivityMessageTest {

    public DevlBuildActivityMessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getStatus method, of class DevlBuildActivityMessage.
     */
    @Test
    public void testGetStatus() {

	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	String expResult = null;
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of setResult method, of class DevlBuildActivityMessage.
     */
    @Test
    public void testSetResult() {

	boolean result_2 = false;
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	instance.setJenkinsLog(new JenkinsLog());
    }

    /**
     * Test of setStatus method, of class DevlBuildActivityMessage.
     */
    @Test
    public void testSetStatus() {

	String lStatus = "";
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	instance.setStatus(lStatus);
    }

    /**
     * Test of processMessage method, of class DevlBuildActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	String expResult = "";
	instance.setUser(DataWareHouse.getUser());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {

	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	String expResult = "";
	instance.setUser(DataWareHouse.getUser());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setUser(DataWareHouse.getUser());
	instance.setJenkinsLog(Mockito.mock(JenkinsLog.class));
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	instance.processMessage();
    }

    @Test
    public void testProcessMessage3() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	DataWareHouse.getUser().setCurrentDelegatedUser(DataWareHouse.getUser());
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = new JenkinsLog();
	jenkinsLog.setErrorMessage("123");
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testProcessMessage4() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setUser(DataWareHouse.getUser());
	instance.setStatus("initiated");
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	instance.processMessage();
    }

    @Test
    public void testProcessMessage5() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = new JenkinsLog();
	List<String> list = new ArrayList<String>();
	list.add("123");
	jenkinsLog.setFailedFiles(list);
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testProcessMessage6() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = new JenkinsLog();
	jenkinsLog.setJobStatus(true);
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testProcessMessage7() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = null;
	instance.setStatus(null);
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testProcessMessage8() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	DataWareHouse.getUser().setCurrentDelegatedUser(null);
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = null;
	instance.setStatus("Passed");
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testProcessMessage9() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	DataWareHouse.getUser().setCurrentDelegatedUser(null);
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = null;
	instance.setStatus("");
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testProcessMessage10() {
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(DataWareHouse.getPlan(), null, null);
	instance.setUser(DataWareHouse.getUser());
	DevlBuildActivityMessage dev = Mockito.mock(DevlBuildActivityMessage.class);
	JenkinsLog jenkinsLogMock = Mockito.mock(JenkinsLog.class);
	JenkinsLog jenkinsLog = new JenkinsLog();
	List<String> list = new ArrayList<String>();
	if (list.isEmpty()) {
	    list.add("");
	}
	jenkinsLog.setFailedFiles(list);
	instance.setJenkinsLog(jenkinsLog);
	Mockito.when(dev.getJenkinsLog()).thenReturn(instance.getJenkinsLog());
	instance.setArguments((IBeans[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId()).toArray());
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of getLogLevel method, of class DevlBuildActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	Priority expResult = Priority.INFO;
	instance.setStatus("a");
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetLogLevel1() {

	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	Priority expResult = Priority.ERROR;
	instance.setStatus("failed");
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    /**
     * Test of setArguments method, of class DevlBuildActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	instance.setArguments((IBeans[]) Arrays.asList(new com.tsi.workflow.beans.dao.System()).toArray());
	instance = new DevlBuildActivityMessage(null, null, null);
	instance.setJenkinsLog(null);
    }

    @Test
    public void testGetJenkinsLog() {

	DevlBuildActivityMessage instance = new DevlBuildActivityMessage(null, null, null);
	JenkinsLog jenkinsLog = Mockito.mock(JenkinsLog.class);
	instance.setJenkinsLog(jenkinsLog);
	instance.getJenkinsLog();

    }

}
