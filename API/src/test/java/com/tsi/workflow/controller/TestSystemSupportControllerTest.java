/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.service.TestSystemSupportService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class TestSystemSupportControllerTest {

    public TestSystemSupportControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    TestSystemSupportController instance;

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    TestSystemSupportController realInstance = new TestSystemSupportController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, TestSystemSupportService.class);
	    TestCaseMockService.doMockController(instance, ProtectedService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    @Test
    public void testTSDController() throws Exception {
	TestCaseExecutor.doTest(instance, TestSystemSupportController.class);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getTestSystemSupportService method, of class
     * TestSystemSupportController.
     */
    @Test
    public void testGetTestSystemSupportService() {
	ProtectedService protectedService = new ProtectedService();
	TestSystemSupportController instance = new TestSystemSupportController();
	instance.setProtectedService(protectedService);
	TestSystemSupportService expResult = null;
	TestSystemSupportService result = instance.getTestSystemSupportService();
	assertEquals(expResult, result);
    }

    /**
     * Test of getPreProductionLoads method, of class TestSystemSupportController.
     */
    @Test
    public void testGetPreProductionLoads() {
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getPreProductionLoads(planId)).thenReturn(lResponse);

    }

    /**
     * Test of deleteTestSystemLoad method, of class TestSystemSupportController.
     */
    @Test
    public void testDeleteTestSystemLoad() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	Integer planId = 20;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.deleteTestSystemLoad(request, response, planId)).thenReturn(lResponse);
    }

    /**
     * Test of getDeploymentVParsList method, of class TestSystemSupportController.
     */
    @Test
    public void testGetDeploymentVParsList() {

	HttpServletRequest request = null;
	HttpServletResponse response = null;
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	Boolean isDeployedInPreProdLoads = false;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getTosDeploymentVParsList(request, response, planId)).thenReturn(lResponse);
    }

    /**
     * Test of getDeploymentPlanList method, of class TestSystemSupportController.
     */
    @Test
    public void testGetDeploymentPlanList() throws Exception {

	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getTosDeploymentPlanList("", 0, 10)).thenReturn(lResponse);
    }

    /**
     * Test of postPreProdSystemLoad method, of class TestSystemSupportController.
     */
    @Test
    public void testPostPreProdSystemLoad() {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	JSONResponse lResponse = new JSONResponse();
	when(instance.postPreProdSystemLoad(request, response, DataWareHouse.getPreProductionLoads())).thenReturn(lResponse);
    }

    /**
     * Test of getDeploymentPlanList method, of class TestSystemSupportController.
     */
    @Test
    public void testGetTosAuxDeploymentPlanList() throws Exception {

	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getTosAuxDeploymentPlanList("", 0, 10)).thenReturn(lResponse);
    }

    @Test
    public void testpostPreProdSystemAuxLoad() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.postPreProdSystemAuxLoad(request, response, DataWareHouse.getPreProductionLoads())).thenReturn(lResponse);
    }

    @Test
    public void testgetYodaDeploymentPlanList() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getYodaDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetYodaDeploymentPlanList1() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getYodaDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetYodaAuxDeploymentPlanList() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getYodaAuxDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetYodaAuxDeploymentPlanList1() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getYodaAuxDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetYodaDeploymentVParsList() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getYodaDeploymentVParsList(request, response, planId)).thenReturn(lResponse);
    }

    @Test
    public void testgetYodaSystemLoadActions() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse lResponse = new JSONResponse();
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getYodaSystemLoadActions(request, response, planId)).thenReturn(lResponse);
    }

}
