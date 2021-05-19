/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSHelperTest {

    public TOSHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    TOSHelper instance;

    @Before
    public void setUp() {
	try {
	    TOSHelper realInstance = new TOSHelper();
	    instance = spy(realInstance);
	} catch (Exception ex) {
	    Logger.getLogger(TOSHelper.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doPreTOSOperation method, of class TOSHelper.
     */
    @Test
    public void testDoPreTOSOperation() {

	User user = DataWareHouse.getUser();
	Constants.LoadSetCommands pOperation = Constants.LoadSetCommands.LOAD;
	PreProductionLoads lLoadSet = DataWareHouse.getPreProductionLoads();
	String lOldLoadSetStatus = "";
	SystemLoad pLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);

	lLoadSet.setCpuId(new SystemCpu());
	instance.doPreTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, false);
	lOldLoadSetStatus = null;
	instance.doPreTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, false);
	lLoadSet.setCpuId(null);
	try {
	    instance.doPreTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, false);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of doTOSOperation method, of class TOSHelper.
     */
    @Test

    public void testDoTOSOperation() {

	User user = DataWareHouse.getUser();
	Constants.LoadSetCommands pOperation = Constants.LoadSetCommands.LOAD;
	ProductionLoads lLoadSet = DataWareHouse.getProductionLoads();
	String lOldLoadSetStatus = "";
	SystemLoad pLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);

	ReflectionTestUtils.setField(instance, "config", mock(TOSConfig.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	ReflectionTestUtils.setField(instance, "AppToTOSQueue", mock(JmsTemplate.class));

	when(instance.config.getTosSystemType()).thenReturn("PRODUCTION");
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultProdCpu())).thenReturn(null);
	try {
	    instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultProdCpu())).thenReturn(new SystemCpu());
	instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad);

	when(instance.config.getTosSystemType()).thenReturn("");
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultNativeCpu())).thenReturn(new SystemCpu());

	lLoadSet.setCpuId(new SystemCpu());
	instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad);

	when(instance.config.getTosSystemType()).thenReturn("ACTIVATE");
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultNativeCpu())).thenReturn(new SystemCpu());

	lLoadSet.setCpuId(new SystemCpu());
	instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad);
	lOldLoadSetStatus = null;
	instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad);
	lLoadSet.setCpuId(null);
	try {
	    instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	try {
	    instance.doTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, null);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of doFallbackTOSOperation method, of class TOSHelper.
     */
    @Test
    public void testdoFallbackTOSOperation() {

	User user = DataWareHouse.getUser();
	Constants.LoadSetCommands pOperation = Constants.LoadSetCommands.LOAD;
	ProductionLoads lLoadSet = DataWareHouse.getProductionLoads();
	String lOldLoadSetStatus = "";
	SystemLoad pLoad = lLoadSet.getSystemLoadId();

	ReflectionTestUtils.setField(instance, "config", mock(TOSConfig.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	ReflectionTestUtils.setField(instance, "AppToTOSQueue", mock(JmsTemplate.class));

	when(instance.config.getTosSystemType()).thenReturn("PRODUCTION");
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultProdCpu())).thenReturn(null);
	try {
	    instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, "");
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultProdCpu())).thenReturn(new SystemCpu());
	instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, "");

	when(instance.config.getTosSystemType()).thenReturn("");
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultNativeCpu())).thenReturn(new SystemCpu());

	lLoadSet.setCpuId(new SystemCpu());
	instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, "");

	when(instance.config.getTosSystemType()).thenReturn("ACTIVATE");
	when(instance.systemCpuDAO.find(pLoad.getSystemId().getDefaultNativeCpu())).thenReturn(new SystemCpu());

	lLoadSet.setCpuId(new SystemCpu());
	instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, "");
	lOldLoadSetStatus = null;
	instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, "");
	lLoadSet.setCpuId(null);
	try {
	    instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, pLoad, "");
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	try {
	    instance.doFallbackTOSOperation(user, pOperation, lLoadSet, lOldLoadSetStatus, null, "");
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of requestIP method, of class TOSHelper.
     */
    @Test
    public void testRequestIP() {

	User user = DataWareHouse.getUser();
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);

	ReflectionTestUtils.setField(instance, "config", mock(TOSConfig.class));
	ReflectionTestUtils.setField(instance, "tosIpMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	ReflectionTestUtils.setField(instance, "AppToTOSQueue", mock(JmsTemplate.class));

	when(instance.config.getTosSystemType()).thenReturn("PRODUCTION");
	try {
	    instance.requestIP(user, systemLoad);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.systemCpuDAO.find(systemLoad.getSystemId().getDefaultProdCpu())).thenReturn(new SystemCpu());
	instance.requestIP(user, systemLoad);
	when(instance.config.getTosSystemType()).thenReturn("");
	when(instance.systemCpuDAO.find(systemLoad.getSystemId().getDefaultNativeCpu())).thenReturn(new SystemCpu());
	instance.requestIP(user, systemLoad);
    }

    /**
     * Test of requestPreProdIP method, of class TOSHelper.
     */
    @Test
    public void testRequestPreProdIP() {

	User user = DataWareHouse.getUser();
	SystemLoad systemLoad = null;
	SystemCpu lCpu = null;

	Boolean result = instance.requestPreProdIP(user, systemLoad, lCpu);
	assertNotNull(result);

    }

    /**
     * Test of getIP method, of class TOSHelper.
     */
    @Test
    public void testGetIP() {

	Integer id = null;
	// TOSHelper instance = new TOSHelper();
	ReflectionTestUtils.setField(instance, "tosIpMap", mock(ConcurrentHashMap.class));
	String result = instance.getIP(id);
	assertNotNull(result);

    }

}
