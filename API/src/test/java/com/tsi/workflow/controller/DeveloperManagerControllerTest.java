package com.tsi.workflow.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DeveloperManagerControllerTest {

    DeveloperManagerController instance;

    public DeveloperManagerControllerTest() {
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
	    DeveloperManagerController realInstance = new DeveloperManagerController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, DeveloperManagerService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperManagerController() throws Exception {
	TestCaseExecutor.doTest(instance, DeveloperManagerController.class);
    }

    @Test
    public void testSetDeveloperManagerService() throws Exception {
	instance.setDeveloperManagerService(mock(DeveloperManagerService.class));
    }

    @Test
    public void testApprovePlan() throws Exception {
	instance.approvePlan(mock(HttpServletRequest.class), mock(HttpServletResponse.class), DataWareHouse.getPlan().getId(), "Test Comment");
	instance.getBuildByPlan(new String[] { "123" });
    }

    /**
     * Test of getMyPlanTasks method, of class DeveloperManagerController.
     */
    @Test
    public void testGetMyPlanTasks() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Integer limit = 10;
	Integer offset = 0;
	String orderBy = "{ \"id\" : \"asc\" }";
	DeveloperManagerController instance = new DeveloperManagerController();
	instance.developerManagerService = mock(DeveloperManagerService.class);
	instance.getMyPlanTasks(request, response, limit, offset, orderBy);
    }

    /**
     * Test of getAssignedPlans method, of class DeveloperManagerController.
     */
    @Test
    public void testGetAssignedPlans() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Integer limit = 10;
	Integer offset = 0;
	String orderBy = "{ \"id\" : \"asc\" }";
	DeveloperManagerController instance = new DeveloperManagerController();
	instance.developerManagerService = mock(DeveloperManagerService.class);
	instance.getAssignedPlans(request, response, limit, offset, orderBy, "", "");
    }

    @Test
    public void testgetProjectList() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getProjectList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}", "", "")).thenReturn(lResponse);
    }

    @Test
    public void testupdateProject() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	Project project = new Project();
	project.setId(1);
	when(instance.updateProject(request, response, project)).thenReturn(lResponse);
    }

    @Test
    public void testsaveProject() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	Project project = new Project();
	project.setId(1);
	when(instance.saveProject(request, response, project)).thenReturn(lResponse);
    }

    @Test
    public void testdeleteProject() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	Project project = new Project();
	project.setId(1);
	when(instance.deleteProject(request, response, project)).thenReturn(lResponse);
    }

    @Test
    public void testmacroHeaderList() throws Exception {
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	instance.macroHeaderList(request, response, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}");
    }
}
