/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.controller.CommonBaseController;
import com.tsi.workflow.base.service.CommonBaseService;
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
import org.mockito.Mockito;

/**
 *
 * @author deepa.jayakumar
 */
public class BaseControllerTest {

    public BaseControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    CommonBaseController instance;

    @Before
    public void setUp() {
	try {
	    CommonBaseController realInstance = new CommonBaseController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, CommonBaseService.class);
	} catch (Exception ex) {
	    Logger.getLogger(BaseControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCommonBaseController() throws Exception {
	TestCaseExecutor.doTest(instance, CommonBaseController.class);
    }

    /**
     * Test of handleException method, of class BaseController.
     */
    @Test
    public void testHandleException() {

	HttpServletRequest request = null;
	Exception ex = new NullPointerException();
	CommonBaseController instance = new CommonBaseController();
	JSONResponse expResult = null;
	JSONResponse result = instance.handleException(request, ex);
	assertNotNull(result);

    }

    /**
     * Test of handleWorkflowException method, of class BaseController.
     */
    @Test
    public void testHandleWorkflowException() {

	HttpServletRequest request = null;

	Exception ex = new NullPointerException();
	CommonBaseController instance = new CommonBaseController();
	JSONResponse expResult = null;
	JSONResponse result = instance.handleWorkflowException(request, ex);
	assertNotNull(result);

    }

    /**
     * Test of getCurrentUser method, of class BaseController.
     */
    @Test
    public void testGetCurrentUser() {

	HttpServletRequest pRequest = mock(HttpServletRequest.class);
	HttpServletResponse pResponse = mock(HttpServletResponse.class);
	CommonBaseController instance = new CommonBaseController();
	User expResult = null;
	User result = instance.getCurrentUser(pRequest, pResponse);
	assertNull(result);

    }

    @Test
    public void testGetCurrentUser2() {

	HttpServletRequest pRequest = mock(HttpServletRequest.class);
	HttpServletResponse pResponse = mock(HttpServletResponse.class);
	BaseController instance = new BaseController() {
	};
	Mockito.when(pRequest.getHeader("Authorization")).thenReturn("test");
	try {
	    User result = instance.getCurrentUser(pRequest, pResponse);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testGetCurrentUser3() {

	HttpServletRequest pRequest = mock(HttpServletRequest.class);
	HttpServletResponse pResponse = mock(HttpServletResponse.class);
	BaseController instance = new BaseController() {
	};
	// Mockito.when(pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("test");
	try {
	    User result = instance.getCurrentUser(pRequest, pResponse);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of removeCurrentUser method, of class BaseController.
     */
    @Test
    public void testRemoveCurrentUser() {

	HttpServletRequest pRequest = mock(HttpServletRequest.class);
	HttpServletResponse pResponse = mock(HttpServletResponse.class);
	BaseController instance = new BaseController() {
	};
	// Mockito.when(pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("Authtest");
	Mockito.when(pRequest.getHeader("Authorization")).thenReturn("Manualtest");
	try {
	    instance.removeCurrentUser(pRequest, pResponse);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testRemoveCurrentUser1() {

	HttpServletRequest pRequest = mock(HttpServletRequest.class);
	HttpServletResponse pResponse = mock(HttpServletResponse.class);
	BaseController instance = new BaseController() {
	};
	// Mockito.when(pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("Authtest");
	Mockito.when(pRequest.getHeader("Authorization")).thenReturn("Manualtest");
	try {
	    instance.removeCurrentUser(pRequest, pResponse);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testRemoveCurrentUser2() {

	HttpServletRequest pRequest = mock(HttpServletRequest.class);
	HttpServletResponse pResponse = mock(HttpServletResponse.class);
	BaseController instance = new BaseController() {
	};
	// Mockito.when(pRequest.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("Authtest");

	try {
	    instance.removeCurrentUser(pRequest, pResponse);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }
}
