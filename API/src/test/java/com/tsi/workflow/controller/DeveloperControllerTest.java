/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.service.DeveloperService;
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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeveloperControllerTest {

    DeveloperController instance;

    public DeveloperControllerTest() {
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
	    DeveloperController realInstance = new DeveloperController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, DeveloperService.class);
	    ReflectionTestUtils.setField(instance, "developerService", mock(DeveloperService.class));
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperController() throws Exception {
	TestCaseExecutor.doTest(instance, DeveloperController.class);
    }

    @Test
    public void testgetImplementation() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.getImplementation(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", null)).thenReturn(lResponse);
    }

    @Test
    public void testlistImplementations() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.listImplementations(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testdeleteFile() throws Exception {
	User lUser = DataWareHouse.getUser();
	Integer[] ids = { 1 };
	JSONResponse lResponse = new JSONResponse();
	when(instance.getDeveloperService().deleteFile(lUser, ids)).thenReturn(lResponse);
	JSONResponse lRetResponse = instance.deleteFile(null, null, ids);
	assertEquals(lResponse, lRetResponse);
    }

    @Test
    public void testgetActivityLogList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.getActivityLogList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "")).thenReturn(lResponse);
    }

    @Test
    public void testcreateWorkspace() throws Exception {
	String lImpId = "t1800123_001";
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	JSONResponse lLatestResponse = new JSONResponse();
	lLatestResponse.setStatus(Boolean.FALSE);
	System.out.println("Testing in progress");
	when(instance.getDeveloperService().createWorkspace(DataWareHouse.getUser(), lImpId)).thenReturn(lResponse);
	when(instance.getDeveloperService().createWorkspace(DataWareHouse.getUser(), lImpId)).thenReturn(lLatestResponse);
	when(instance.getDeveloperService().getLatest(DataWareHouse.getUser(), lImpId, "origin")).thenReturn(lLatestResponse);
	// when(instance.createWorkspace(null, null, lImpId)).thenReturn(lResponse);
	instance.createWorkspace(null, null, lImpId);
    }

    @Test
    public void testcreateWorkspace1() throws Exception {
	String lImpId = "t1800123_001";
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.FALSE);
	JSONResponse l1Response = new JSONResponse();
	l1Response.setStatus(Boolean.TRUE);
	System.out.println("Testing in progress");
	when(instance.getDeveloperService().createWorkspace(DataWareHouse.getUser(), lImpId)).thenReturn(l1Response);
	when(instance.getDeveloperService().getLatest(DataWareHouse.getUser(), lImpId, "origin")).thenReturn(lResponse);
	when(instance.createWorkspace(null, null, lImpId)).thenReturn(lResponse);
    }

}
