/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tos.client;

import com.tsi.workflow.tos.model.TOSResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSClientImplTest {

    public TOSClientImplTest() {
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
     * Test of configure method, of class TOSClientImpl.
     */
    // @Test
    public void testConfigure() throws Exception {
	TOSClientImpl instance = new TOSClientImpl();
	instance.configure();
    }

    /**
     * Test of sendResult method, of class TOSClientImpl.
     */
    @Test
    public void testSendResult() {
	String system = "";
	TOSResult pResult = new TOSResult();
	pResult.setCommand("");
	pResult.setLoadset("");
	TOSClientImpl instance = new TOSClientImpl();
	instance.sendResult(system, pResult);
    }

}
