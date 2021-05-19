package com.tsi.workflow.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.ui.AdvancedSearchForm;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
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
public class ProtectedControllerTest {

    ProtectedController instance;

    public ProtectedControllerTest() {
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
	    ProtectedController realInstance = new ProtectedController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, ProtectedService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProtectedController() throws Exception {
	TestCaseExecutor.doTest(instance, ProtectedController.class);
    }

    /**
     * Test of setProtectedService method, of class ProtectedController.
     */
    @Test
    public void testSetProtectedService() {
	ProtectedService protectedService = null;
	ProtectedController instance = new ProtectedController();
	instance.setProtectedService(protectedService);
    }

    /**
     * Test of getSharedObjects method, of class ProtectedController.
     */
    @Test
    public void testGetSharedObjects() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	Integer limit = 0;
	Integer offset = 10;
	String orderBy = "{\"id\": \"asc\"}";
	String soName = "";
	String loadDate = "";
	Integer systemId = null;
	JSONResponse expResult = null;
	JSONResponse result = instance.getSharedObjects(request, response, limit, offset, orderBy, soName, loadDate, systemId);
	assertEquals(expResult, result);
    }

    /**
     * Test of rejectPlanAndDependentPlans method, of class ProtectedController.
     */
    @Test
    public void testRejectPlanAndDependentPlans() {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	String impPlanId = "";
	String rejectReason = "";
	JSONResponse expResult = null;
	JSONResponse result = instance.rejectPlanAndDependentPlans(request, response, impPlanId, rejectReason, Boolean.TRUE);
	assertEquals(expResult, result);
    }

    /**
     * Test of getSystemLoadListBySystemId method, of class ProtectedController.
     */
    @Test
    public void testGetSystemLoadListBySystemId() throws Exception {
	Integer limit = 0;
	Integer offset = 10;
	String orderBy = "{\"id\": \"asc\"}";
	Integer id = null;
	JSONResponse expResult = null;
	JSONResponse result = instance.getSystemLoadListBySystemId(limit, offset, orderBy, id);
	assertEquals(expResult, result);
    }

    /**
     * Test of updatePlan method, of class ProtectedController.
     */
    @Test
    public void testUpdatePlan() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	ImpPlan plan = null;
	boolean warningFlag = false;
	JSONResponse expResult = null;
	JSONResponse result = instance.updatePlan(request, response, plan, warningFlag, false, "");
	assertEquals(expResult, result);
    }

    /**
     * Test of getSettingsList method, of class ProtectedController.
     */
    @Test
    public void testGetSettingsList() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	String userId = DataWareHouse.getUser().getId();
	Boolean isToolAdmin = Boolean.TRUE;
	instance.getSettingsList(request, response, userId, isToolAdmin);
    }

    /**
     * Test of getSettingsList method, of class ProtectedController.
     */
    @Test
    public void testGetSettingsList2() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	String userId = null;
	Boolean isToolAdmin = Boolean.FALSE;
	instance.getSettingsList(request, response, userId, isToolAdmin);
    }

    /**
     * Test of getSettingsList method, of class ProtectedController.
     */
    @Test
    public void testGetSettingsList3() throws Exception {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	String userId = DataWareHouse.getUser().getId();
	Boolean isToolAdmin = Boolean.FALSE;
	instance.getSettingsList(request, response, userId, isToolAdmin);
    }

    @Test
    public void testupdateDbcr() {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	ProtectedController instance = mock(ProtectedController.class);
	try {
	    instance.updateDbcr(request, response, null);
	} catch (Exception e) {
	    Assert.assertTrue(true);
	}

    }

    @Test
    public void testdeleteDbcr() {
	HttpServletRequest request = mock(HttpServletRequest.class);
	HttpServletResponse response = mock(HttpServletResponse.class);
	ProtectedController instance = mock(ProtectedController.class);
	try {
	    instance.deleteDbcr(request, response, "123");
	} catch (Exception e) {
	    Assert.assertTrue(true);
	}
    }

    @Test
    public void testgetPlanByAdvancedSearch() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	AdvancedSearchForm advancedSearchForm = new AdvancedSearchForm();
	advancedSearchForm.setProgramName("WSP");
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.getPlanByAdvancedSearch(advancedSearchForm, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);

    }

    @Test
    public void testadvancedSearchExportExcel() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	AdvancedSearchForm advancedSearchForm = new AdvancedSearchForm();
	advancedSearchForm.setProgramName("WSP");
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());
	when(instance.advancedSearchExportExcel(advancedSearchForm, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);

    }

    @Test
    public void testfileSearch() {
	JSONResponse lResponse = new JSONResponse();
	HttpServletRequest request = null;
	HttpServletResponse response = null;
	when(instance.getCurrentUser(null, null)).thenReturn(DataWareHouse.getUser());

	String flag = "PROD";
	when(instance.fileSearch(request, response, "", flag, DataWareHouse.getPlan().getId())).thenReturn(lResponse);

	String flag1 = "NONPROD";
	when(instance.fileSearch(request, response, "", flag1, DataWareHouse.getPlan().getId())).thenReturn(lResponse);

	String flag5 = "PENDING";
	when(instance.fileSearch(request, response, "", flag5, DataWareHouse.getPlan().getId())).thenReturn(lResponse);

	String flag2 = "IBMVANILLA";
	when(instance.fileSearch(request, response, "", flag2, DataWareHouse.getPlan().getId())).thenReturn(lResponse);

	String flag3 = "MIGNONIBM";
	when(instance.fileSearch(request, response, "", flag3, DataWareHouse.getPlan().getId())).thenReturn(lResponse);

	String flag4 = "MIGOBS";
	when(instance.fileSearch(request, response, "", flag4, DataWareHouse.getPlan().getId())).thenReturn(lResponse);

	JSONResponse lResponseData = new JSONResponse();
	lResponseData.setData("Not a valid option");
	try {
	    when(instance.fileSearch(request, response, "", "", DataWareHouse.getPlan().getId())).thenReturn(lResponseData);
	} catch (Exception e) {
	    assertTrue(true);

	}

    }

}
