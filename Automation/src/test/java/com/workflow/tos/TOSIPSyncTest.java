/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.tos;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.interfaces.ITOSConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSIPSyncTest {

    public TOSIPSyncTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	TOSIPSync realinstance = new TOSIPSync(mock(ITOSConfig.class));
	instance = spy(realinstance);
    }

    @After
    public void tearDown() {
    }

    TOSIPSync instance;

    /**
     * Test of getData method, of class TOSIPSync.
     */
    @Test
    public void testGetData() {
	instance.getData();

    }

}
