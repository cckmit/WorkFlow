/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.external.CSRNumberDAO;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
public class CSRNumberPopulateTest {

    public CSRNumberPopulateTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	CSRNumberPopulate realInstance = new CSRNumberPopulate();
	instance = spy(realInstance);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doMonitor method, of class CSRNumberPopulate.
     */
    CSRNumberPopulate instance;

    @Test
    public void testDoMonitor() {
	try {
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "projectDAO", mock(ProjectDAO.class));
	    ReflectionTestUtils.setField(instance, "cSRNumberDAO", mock(CSRNumberDAO.class));
	    when(instance.wFConfig.getServiceUserID()).thenReturn("");
	    when(instance.cSRNumberDAO.findAll()).thenReturn(Arrays.asList(DataWareHouse.getPlan().getProjectId()));
	    Map<String, Project> lMap = new HashMap<>();
	    lMap.put(DataWareHouse.getPlan().getProjectId().getProjectNumber(), DataWareHouse.getPlan().getProjectId());
	    // when(instance.projectDAO.findActiveProjectNumbers()).thenReturn(lMap);
	    when(instance.projectDAO.findNonActiveProjectNumbers()).thenReturn(lMap);
	    instance.doMonitor();
	} catch (Exception e) {

	}
    }

}
