/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertEquals;

import javax.servlet.http.HttpServletRequest;
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
public class URLUtilsTest {

    public URLUtilsTest() {
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
     * Test of isAjaxRequest method, of class URLUtils.
     */
    @Test
    public void testIsAjaxRequest() {

	HttpServletRequest mockedRequest = Mockito.mock(HttpServletRequest.class);
	boolean expResult = false;
	URLUtils uRLUtils = new URLUtils();
	boolean result = uRLUtils.isAjaxRequest(mockedRequest);
	assertEquals(expResult, result);
    }

}
