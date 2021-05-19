/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author yeshwanth.shenoy
 */
public class TOSClientDaemonTest {

    public TOSClientDaemonTest() {
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
     * Test of start method, of class TOSClientDaemon.
     */
    // @Test
    public void testStart() throws Exception {
	String[] args = null;
	TOSClientDaemon.start(args);
    }

}
