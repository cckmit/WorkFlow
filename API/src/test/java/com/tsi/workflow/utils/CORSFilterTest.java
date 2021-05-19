/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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
 * @author yeshwanth.shenoy
 */
public class CORSFilterTest {

    public CORSFilterTest() {
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
     * Test of init method, of class CORSFilter.
     */
    @Test
    public void testInit() throws Exception {

	FilterConfig filterConfig = null;
	CORSFilter instance = new CORSFilter();
	instance.init(filterConfig);
    }

    /**
     * Test of doFilter method, of class CORSFilter.
     */
    @Test
    public void testDoFilter() throws Exception {

	HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
	HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
	FilterChain chain = Mockito.mock(FilterChain.class);
	CORSFilter instance = spy(new CORSFilter());
	when(servletRequest.getMethod()).thenReturn("OPTIONS");
	instance.doFilter(servletRequest, servletResponse, chain);

	when(servletRequest.getHeader("Origin")).thenReturn("OPTIONS");
	instance.doFilter(servletRequest, servletResponse, chain);
    }

    /**
     * Test of doFilter method, of class CORSFilter.
     */
    @Test
    public void testDoFilter2() throws Exception {
	HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
	HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
	FilterChain chain = Mockito.mock(FilterChain.class);
	CORSFilter instance = spy(new CORSFilter());
	when(servletRequest.getMethod()).thenReturn("GET");
	instance.doFilter(servletRequest, servletResponse, chain);
	when(servletRequest.getHeader("Origin")).thenReturn("OPTIONS");
	instance.doFilter(servletRequest, servletResponse, chain);
    }

    /**
     * Test of doFilter method, of class CORSFilter.
     */
    @Test
    public void testDoFilter3() throws Exception {
	HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
	HttpServletResponse servletResponse = Mockito.mock(HttpServletResponse.class);
	FilterChain chain = Mockito.mock(FilterChain.class);
	CORSFilter instance = spy(new CORSFilter());
	when(servletRequest.getMethod()).thenReturn("GET");
	instance.doFilter(servletRequest, servletResponse, chain);
	when(servletRequest.getHeader("Origin")).thenReturn("https://vhldvztdt001.tvlport.net:8443");
	instance.doFilter(servletRequest, servletResponse, chain);
    }

    /**
     * Test of destroy method, of class CORSFilter.
     */
    @Test
    public void testDestroy() {

	CORSFilter instance = new CORSFilter();
	instance.destroy();
    }

}
