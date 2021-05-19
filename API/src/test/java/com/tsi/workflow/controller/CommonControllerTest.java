/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.CommonService;
import com.tsi.workflow.service.LDAPService;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommonControllerTest {

    CommonController instance;

    public CommonControllerTest() {
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
	    CommonController realInstance = new CommonController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, CommonService.class);
	    TestCaseMockService.doMockController(instance, LDAPService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCommonController() throws Exception {
	TestCaseExecutor.doTest(instance, CommonController.class);
    }

    @Test
    public void testGtFileSyncInfo() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
	    instance.getFileSyncInfo(request, response, "", "", "");
	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGtFileSyncInfo2() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    when(response.getWriter()).thenThrow(new IOException());
	    instance.getFileSyncInfo(request, response, "", "", "");
	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetTAPDetailsByPlan() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
	    instance.getTAPDetailsByPlan(request, response, "");
	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetTAPDetailsByPlan2() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    when(response.getWriter()).thenThrow(new IOException());
	    instance.getTAPDetailsByPlan(request, response, "");

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetTAPDetails() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
	    instance.getTAPDetails(request, response, "", "", "");

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetTAPDetails2() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    when(response.getWriter()).thenThrow(new IOException());
	    instance.getTAPDetails(request, response, "", "", "");

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetLocalConfigDetails() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
	    instance.getLocalConfigDetails(request, response, "", "");

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetLocalConfigDetails2() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    when(response.getWriter()).thenThrow(new IOException());
	    instance.getLocalConfigDetails(request, response, "", "");

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetPutDeployDate() {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
	    instance.getPutDeployDate(request, response, "", "");
	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testGetPutDeployDate2() {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    when(response.getWriter()).thenThrow(new IOException());
	    instance.getPutDeployDate(request, response, "", "");
	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @Test
    public void testListNonIBMFilePaths() {
	instance.commonService = mock(CommonService.class);
	JSONResponse lResponse = instance.listNonIBMFilePaths();
	Assert.assertNotNull(lResponse);

    }

    @Test
    public void testGetFutureSecured() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    when(response.getWriter()).thenThrow(new IOException());
	    instance.getFutureSecured(request, response, "", "", "", false);

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    @Test
    public void testGetFutureSecured1() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    PrintWriter writer = mock(PrintWriter.class);
	    when(response.getWriter()).thenReturn(writer);
	    instance.getFutureSecured(request, response, "", "", "", false);

	} catch (IOException ex) {
	    Logger.getLogger(CommonControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

}
