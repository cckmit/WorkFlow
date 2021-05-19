package com.tsi.workflow.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.LoadsControlService;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author USER
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoadsControlControllerTest {

    LoadsControlController instance;

    public LoadsControlControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    LoadsControlController realInstance = new LoadsControlController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, LoadsControlService.class);
	} catch (Exception ex) {
	    Logger.getLogger(LoadsControlControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoadsControlController() throws Exception {
	TestCaseExecutor.doTest(instance, LoadsControlController.class);

    }

    @Test
    public void testsetLoadsControlService() throws Exception {
	LoadsControlService loadsControlService = mock(LoadsControlService.class);
	instance.setLoadsControlService(loadsControlService);
    }

    @Test
    public void testgetLoadCategoryList() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getLoadCategoryList(0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}");
    }

}
