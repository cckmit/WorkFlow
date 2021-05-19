/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.tos.model.TOSResult;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TOSActivityMessageTest {

    TOSActivityMessage instance;

    public TOSActivityMessageTest() {
	instance = new TOSActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), DataWareHouse.getPlan().getProductionLoadsList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @Test
    public void testGettOSResult() {
	TOSResult expResult = null;
	TOSResult tOSResult = null;
	instance.settOSResult(tOSResult);
	TOSResult result = instance.gettOSResult();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetLogLevel() {
	instance.lPriority = Priority.DEBUG;
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	ProductionLoads productionLoads = DataWareHouse.getProductionLoads();
	if (productionLoads == null) {
	    System.out.println("Null occurs");
	}
	productionLoads.setLastActionStatus("SUCCESS");
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	productionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	instance.settOSResult(tos);
	instance.setArguments(productionLoads);
	String result = instance.processMessage();
	assertNotNull(result);

	instance.settOSResult(null);
	productionLoads.setStatus("FALLBACK");
	instance.setArguments(productionLoads);
	result = instance.processMessage();
	assertNotNull(result);

    }

    @Test
    public void testProcessMessage1() {
	ProductionLoads productionLoads = DataWareHouse.getProductionLoads();
	productionLoads.setLastActionStatus("");
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	productionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	instance.settOSResult(tos);
	instance.setArguments(productionLoads);
	String result = instance.processMessage();
	assertNotNull(result);

	instance.settOSResult(null);
	productionLoads.setCpuId(null);
	productionLoads.setStatus("FALLBACK");
	instance.setArguments(productionLoads);
	result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {
	ProductionLoads productionLoads = DataWareHouse.getProductionLoads();
	productionLoads.setLastActionStatus("INPROGRESS");
	SystemCpu cpu = new SystemCpu();
	cpu.setCpuCode("test");
	productionLoads.setCpuId(cpu);
	TOSResult tos = new TOSResult();
	tos.setMessage("message");
	tos.setReturnValue(1);
	instance.settOSResult(tos);
	instance.setArguments(productionLoads);
	String result = instance.processMessage();
	assertNotNull(result);

	instance.settOSResult(null);
	productionLoads.setStatus("FALLBACK");
	instance.setArguments(productionLoads);
	result = instance.processMessage();
	assertNotNull(result);
    }
}
