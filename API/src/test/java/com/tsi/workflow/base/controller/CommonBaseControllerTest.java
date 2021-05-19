/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base.controller;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommonBaseControllerTest {

    CommonBaseController instance;

    public CommonBaseControllerTest() {
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
	    CommonBaseController realInstance = new CommonBaseController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, CommonBaseService.class);
	} catch (Exception ex) {
	    Logger.getLogger(CommonBaseControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCommonBaseController() throws Exception {
	TestCaseExecutor.doTest(instance, CommonBaseController.class);
    }

    @Test
    public void testSetCommonBaseService() throws WorkflowException {
	CommonBaseController instance = new CommonBaseController();
	instance.setCommonBaseService(null);
	assertNull(instance.getCommonBaseService());
    }

    @Test
    public void testGetBuildList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getBuildList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetPlanApprovalsList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getPlanApprovalsList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetPlanList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getPlanList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "T1800123")).thenReturn(lResponse);
    }

    @Test
    public void testgetSegmentMappingList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getSegmentMappingList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetImplementationList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getImplementationList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "t1800123_001")).thenReturn(lResponse);
    }

    @Test
    public void testgetLoadFreezeList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getLoadFreezeList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetLoadCategoryList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getLoadCategoryList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetLoadWindowList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getLoadWindowList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetPlatformList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getPlatformList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetProjectList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getProjectList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetPutLevelList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getPutLevelList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testgetSystemList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getSystemList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetSystemLoadList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getSystemLoadList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetVparsList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getVparsList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetActivityLogList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getActivityLogList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetSystemLoadActionsList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getSystemLoadActionsList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetDbcrList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getDbcrList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetProductionLoadsList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getProductionLoadsList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testgetSystemCpuList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	ReflectionTestUtils.setField(instance, "commonService", mock(CommonBaseService.class));
	when(instance.getSystemCpuList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

}
