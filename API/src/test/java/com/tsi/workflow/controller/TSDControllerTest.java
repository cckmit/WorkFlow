package com.tsi.workflow.controller;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.TSDService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TSDControllerTest {

    TSDController instance;

    public TSDControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    TSDController realInstance = new TSDController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, TSDService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTSDController() throws Exception {
	TestCaseExecutor.doTest(instance, TSDController.class);
    }

    @Test
    public void testacceptFallback() {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.acceptFallback(null, null, DataWareHouse.getPlan().getId(), "Test");
    }

    @Test
    public void testloadsetsToAccept() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.loadsetsToAccept(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "");
    }

    @Test
    public void testpostProdSystemLoad() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.postProdSystemLoad(null, null, DataWareHouse.getProductionLoads(), Boolean.TRUE, StringUtils.EMPTY);
    }

    @Test
    public void testgetSystemLoadListBySystemId() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getSystemLoadListBySystemId(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", 0, "");
    }

    @Test
    public void testgetAuxLoads() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getAuxLoads(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", null);
    }

    @Test
    public void testgetFallBackLoadsToAccept() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getFallBackLoadsToAccept(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}");
    }

    @Test
    public void testgetFallBackSystemLoadListBySystemId() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getFallBackSystemLoadListBySystemId(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", 1);
    }

    @Test
    public void testgetImplementationPlanSyncEType() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getImplementationPlanSyncEType(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}");
    }

}
