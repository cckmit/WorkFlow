package com.tsi.workflow.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.DeveloperLeadService;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mockito;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeveloperLeadControllerTest {

    DeveloperLeadController instance;

    public DeveloperLeadControllerTest() {
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
	    DeveloperLeadController realInstance = new DeveloperLeadController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, DeveloperLeadService.class);
	    TestCaseMockService.doMockController(instance, ProtectedService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperLeadController() throws Exception {
	TestCaseExecutor.doTest(instance, DeveloperLeadController.class);
    }

    @Test
    public void testSetDeveloperLeadService() {
	DeveloperLeadController developerLeadController = new DeveloperLeadController();
	DeveloperLeadService developerLeadService = new DeveloperLeadService();
	ProtectedService protectedService = mock(ProtectedService.class);
	developerLeadController.setProtectedService(protectedService);
	developerLeadController.setDeveloperLeadService(developerLeadService);

    }

    @Test
    public void testGetProjectList() {

	DeveloperLeadController developerLeadController = new DeveloperLeadController();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	String orderBy = "1";

	try {

	    ObjectMapper o = Mockito.mock(ObjectMapper.class);
	    o.readValue(orderBy, LinkedHashMap.class);
	    developerLeadController.getProjectList(request, response, Integer.SIZE, Integer.SIZE, "", orderBy, "1");

	} catch (Exception e) {
	}

    }

    @Test
    public void testgetProjectList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getProjectList(request, response, 0, 0, "", "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "1")).thenReturn(lResponse);
    }

    @Test
    public void testgetProjectList1() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getProjectList(request, response, 0, 10, "", "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "1")).thenReturn(lResponse);
    }

    @Test
    public void testgetProjectList2() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getProjectList(request, response, 0, 0, "", "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "Travelport")).thenReturn(lResponse);
    }

    @Test
    public void testgetActivityLogList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.getActivityLogList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.getPlanList(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetDeploymentPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetAuxDeploymentPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getAuxDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetDeploymentPlanList1() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetAuxDeploymentPlanList1() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getAuxDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

}
