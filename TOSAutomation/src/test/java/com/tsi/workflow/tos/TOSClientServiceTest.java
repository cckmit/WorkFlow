/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import static org.mockito.Mockito.spy;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSClientServiceTest {

    public TOSClientServiceTest() {
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
     * Test of startClient method, of class TOSClientService.
     */
    // @Test
    public void testStartClient() throws Exception {
	String[] args = null;
	TOSClientService tOSClientService = spy(new TOSClientService());
	tOSClientService.startClient(args);
    }

    /**
     * Test of stopClient method, of class TOSClientService.
     */
    @Test
    public void testStopClient() throws Exception {
	try {
	    String[] args = null;
	    TOSClientService tOSClientService = spy(new TOSClientService());
	    tOSClientService.stopClient(args);
	} catch (Exception e) {
	    System.out.println("");
	}
    }

}
