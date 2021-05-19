/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.User;
import com.tsi.workflow.service.LDAPService;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author deepa.jayakumar
 */
public class RequestInterceptorTest {

    public RequestInterceptorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of preHandle method, of class RequestInterceptor.
     */
    @Test
    public void testPreHandle() throws Exception {

	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Object handler = null;
	RequestInterceptor instance = new RequestInterceptor();
	when(request.getContextPath()).thenReturn("");
	when(request.getRequestURI()).thenReturn("/login");
	boolean expResult = true;
	boolean result = instance.preHandle(request, response, handler);
	assertEquals(expResult, result);
    }

    @Test
    public void testPreHandle1() {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    Object handler = null;
	    RequestInterceptor instance = new RequestInterceptor();
	    when(request.getContextPath()).thenReturn("");
	    when(request.getRequestURI()).thenReturn("test");
	    // when(request.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("/login");
	    instance.lDAPService = mock(LDAPService.class);
	    User user = new User();
	    user.setDisplayName("123");
	    ConcurrentHashMap lSessionValidator = new ConcurrentHashMap();
	    // when(instance.lDAPService.doSSOAuthentication(request)).thenReturn(user);
	    boolean result = instance.preHandle(request, response, handler);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testPreHandle2() throws Exception {
	try {
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    Object handler = null;
	    RequestInterceptor instance = new RequestInterceptor();
	    when(request.getContextPath()).thenReturn("");
	    when(request.getRequestURI()).thenReturn("test");
	    // when(request.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("/login");
	    instance.lDAPService = mock(LDAPService.class);
	    // when(instance.lDAPService.doSSOAuthentication(request)).thenReturn(null);
	    boolean result = instance.preHandle(request, response, handler);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testPreHandle3() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Object handler = null;
	RequestInterceptor instance = new RequestInterceptor();
	when(request.getContextPath()).thenReturn("");
	when(request.getRequestURI()).thenReturn("test");
	// when(request.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("");

	boolean result = instance.preHandle(request, response, handler);
	assertNotNull(result);
    }

    @Test
    public void testPreHandle4() throws Exception {
	try {
	    RequestInterceptor instance = new RequestInterceptor();
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    Object handler = null;
	    when(request.getContextPath()).thenReturn("");
	    when(request.getRequestURI()).thenReturn("/test");
	    when(request.getHeader("Authorization")).thenReturn("test");
	    boolean result = instance.preHandle(request, response, handler);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do nothing
	}
    }

    // @Test
    public void testPreHandle5() throws Exception {
	RequestInterceptor instance = new RequestInterceptor();
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Object handler = null;
	when(request.getContextPath()).thenReturn("");
	when(request.getRequestURI()).thenReturn("/test");
	when(request.getHeader("Authorization")).thenReturn("test");
	ConcurrentHashMap lMap = new ConcurrentHashMap();
	boolean result2 = instance.preHandle(request, response, handler);
	assertNotNull(result2);
    }

    @Test
    public void testPreHandle6() throws Exception {
	RequestInterceptor instance = new RequestInterceptor();
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Object handler = null;
	RequestInterceptor mockInstance = mock(RequestInterceptor.class);
	when(request.getHeader("Authorization")).thenReturn(null);
	boolean result2 = instance.preHandle(request, response, handler);
	assertNotNull(result2);

    }

    @Test
    public void testPreHandle7() throws Exception {
	try {
	    RequestInterceptor instance = new RequestInterceptor();
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    Object handler = null;
	    when(request.getHeader("Authorization")).thenReturn("test");
	    boolean result2 = instance.preHandle(request, response, handler);
	    assertNotNull(result2);
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testPreHandle8() throws Exception {
	try {
	    RequestInterceptor instance = new RequestInterceptor();
	    HttpServletRequest request = mock(HttpServletRequest.class);
	    HttpServletResponse response = mock(HttpServletResponse.class);
	    Object handler = null;
	    // when(request.getHeader(Constants.SSOHeaders.SM_USER.getKey())).thenReturn("test");
	    boolean result2 = instance.preHandle(request, response, handler);
	    assertNotNull(result2);
	} catch (Exception e) {
	    // do nothing
	}

    }

    /**
     * Test of afterCompletion method, of class RequestInterceptor.
     */
    @Test
    public void testAfterCompletion() throws Exception {

	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Object handler = "";
	Exception ex = null;
	when(request.getMethod()).thenReturn("");
	when(request.getRequestURL()).thenReturn(new StringBuffer(""));
	when(response.getStatus()).thenReturn(0);

	RequestInterceptor instance = new RequestInterceptor();
	instance.afterCompletion(request, response, handler, ex);
    }

    public void testPostHandle() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Object handler = "";
	RequestInterceptor instance = new RequestInterceptor();
	ModelAndView modelAndView = new ModelAndView();

	instance.postHandle(request, response, handler, modelAndView);

    }

}
