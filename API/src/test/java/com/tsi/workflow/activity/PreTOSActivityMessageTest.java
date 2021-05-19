/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.tos.model.TOSResult;
import java.util.Arrays;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class PreTOSActivityMessageTest {

    SystemCpu cpu = new SystemCpu();

    public PreTOSActivityMessageTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	cpu.setCpuCode("test");
	cpu.setCpuName("A");
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of gettOSResult method, of class PreTOSActivityMessage.
     */
    @Test
    public void testGettOSResult() {

	PreTOSActivityMessage instance = new PreTOSActivityMessage(null, null, null);
	TOSResult expResult = null;
	TOSResult result = instance.gettOSResult();
	assertEquals(expResult, result);
    }

    /**
     * Test of settOSResult method, of class PreTOSActivityMessage.
     */
    @Test
    public void testSettOSResult() {

	TOSResult tOSResult = null;
	PreTOSActivityMessage instance = new PreTOSActivityMessage(null, null, null);
	instance.settOSResult(tOSResult);
    }

    /**
     * Test of processMessage method, of class PreTOSActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	PreProductionLoads preProductionLoads = DataWareHouse.getPreProductionLoads();
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	preProductionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	PreTOSActivityMessage instance = new PreTOSActivityMessage(DataWareHouse.getPlan(), null, preProductionLoads);
	instance.settOSResult(tos);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage_() {

	PreProductionLoads preProductionLoads = DataWareHouse.getPreProductionLoads();
	preProductionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	PreTOSActivityMessage instance = new PreTOSActivityMessage(DataWareHouse.getPlan(), null, preProductionLoads);
	instance.settOSResult(tos);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {

	PreProductionLoads preProductionLoads = DataWareHouse.getPreProductionLoads();
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	preProductionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	preProductionLoads.setLastActionStatus("INPROGRESS");
	PreTOSActivityMessage instance = new PreTOSActivityMessage(DataWareHouse.getPlan(), null, preProductionLoads);
	instance.settOSResult(tos);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1_() {

	PreProductionLoads preProductionLoads = DataWareHouse.getPreProductionLoads();
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	preProductionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	preProductionLoads.setLastActionStatus("INPROGRESS");
	PreTOSActivityMessage instance = new PreTOSActivityMessage(DataWareHouse.getPlan(), null, preProductionLoads);
	instance.settOSResult(null);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {

	PreProductionLoads preProductionLoads = DataWareHouse.getPreProductionLoads();
	preProductionLoads.setLastActionStatus("");
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	preProductionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	PreTOSActivityMessage instance = new PreTOSActivityMessage(DataWareHouse.getPlan(), null, preProductionLoads);
	instance.settOSResult(tos);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2_() {

	PreProductionLoads preProductionLoads = DataWareHouse.getPreProductionLoads();
	preProductionLoads.setLastActionStatus("");
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	preProductionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	PreTOSActivityMessage instance = new PreTOSActivityMessage(DataWareHouse.getPlan(), null, preProductionLoads);
	instance.settOSResult(tos);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of getLogLevel method, of class PreTOSActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	PreTOSActivityMessage instance = new PreTOSActivityMessage(null, null, DataWareHouse.getPreProductionLoads());
	Priority expResult = null;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    /**
     * Test of setArguments method, of class PreTOSActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	PreTOSActivityMessage instance = new PreTOSActivityMessage(null, null, null);
	instance.setArguments((IBeans[]) Arrays.asList(new PreProductionLoads()).toArray());
    }

}
