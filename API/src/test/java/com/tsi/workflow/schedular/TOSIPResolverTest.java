/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.dao.SystemCpuDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class TOSIPResolverTest {

    public TOSIPResolverTest() {
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
     * Test of refreshIps method, of class TOSIPResolver.
     */
    @Test
    public void testRefreshIps() {
	TOSIPResolver instance = spy(new TOSIPResolver());
	ReflectionTestUtils.setField(instance, "config", mock(TOSConfig.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	instance.refreshIps();
    }

}
