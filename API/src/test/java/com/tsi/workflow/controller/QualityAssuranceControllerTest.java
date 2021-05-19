package com.tsi.workflow.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.service.QualityAssuranceService;
import com.tsi.workflow.utils.JSONResponse;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QualityAssuranceControllerTest {

    QualityAssuranceController instance;

    public QualityAssuranceControllerTest() {
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
	    QualityAssuranceController realInstance = new QualityAssuranceController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, QualityAssuranceService.class);
	    TestCaseMockService.doMockController(instance, ProtectedService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testQualityAssuranceController() throws Exception {
	TestCaseExecutor.doTest(instance, QualityAssuranceController.class);
    }

    @Test
    public void testSetQualityAssuranceService() throws Exception {
	QualityAssuranceService qualityAssuranceService = mock(QualityAssuranceService.class);
	ProtectedService protectedService = mock(ProtectedService.class);
	instance.setQualityAssuranceService(qualityAssuranceService);
	instance.setProtectedService(protectedService);
    }

    @Test
    public void testgetRegressionDeploymentPlanList() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getRegressionDeploymentPlanList(null, null, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}");
    }

    @Test
    public void testgetRegressionDeploymentPlanList1() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getRegressionDeploymentPlanList(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "");
    }

    @Test
    public void testUpdatePlanTestStatus() {
	Integer[] vparId = new Integer[] { 20 };
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.updatePlanTestStatus(request, response, 0, vparId, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", vparId)).thenReturn(lResponse);
    }

    @Test
    public void testgetRegressionAuxDeploymentPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getRegressionAuxDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);

    }

    @Test
    public void testgetRegressionAuxDeploymentPlanList1() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getRegressionAuxDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);

    }

    @Test
    public void testgetRegressionDeploymentVParsList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	when(instance.getRegressionDeploymentVParsList(request, response, planId)).thenReturn(lResponse);
    }

    @Test
    public void testgetRegressionSystemLoadActions() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	when(instance.getRegressionSystemLoadActions(request, response, planId)).thenReturn(lResponse);
    }

    @Test
    public void testgetFunctionalDeploymentPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getFunctionalDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetFunctionalDeploymentPlanList1() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getFunctionalDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetFunctionalAuxDeploymentPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getFunctionalAuxDeploymentPlanList(request, response, 0, 10, null, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetFunctionalAuxDeploymentPlanList1() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getFunctionalAuxDeploymentPlanList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetFunctionalDeploymentVParsList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getFunctionalDeploymentVParsList(request, response, planId)).thenReturn(lResponse);
    }

    @Test
    public void testgetFunctionalSystemLoadActions() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	String[] planId = new String[] { DataWareHouse.getPlan().getId() };
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getFunctionalSystemLoadActions(request, response, planId)).thenReturn(lResponse);
    }

}
