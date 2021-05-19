package com.tsi.workflow.controller;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.ToolAdminService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ToolAdminControllerTest {

    ToolAdminController instance;

    public ToolAdminControllerTest() {
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
	    ToolAdminController realInstance = new ToolAdminController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, ToolAdminService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testToolAdminController() throws Exception {
	TestCaseExecutor.doTest(instance, ToolAdminController.class);
    }

    // @Test
    // public void testupdateRepository() {
    // when(instance.getCurrentUser(null,
    // null)).thenReturn(DataWareHouse.getUser());
    // instance.updateRepository(null, null, DataWareHouse.getRepositoryDetails());
    // }

    @Test
    public void testgetRepoList() throws Exception {
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.getRepoList(1, 1);
    }

    @Test
    public void testToolAdminService() {
	ToolAdminController obj = new ToolAdminController();
	obj.setToolAdminService(obj.getToolAdminService());

    }

    @Test
    public void testJSONResponse() {

	JSONResponse response = new JSONResponse();
	response.setUserId(DataWareHouse.getUser().getId());
	assertNotNull(response.getUserId());

    }
}
