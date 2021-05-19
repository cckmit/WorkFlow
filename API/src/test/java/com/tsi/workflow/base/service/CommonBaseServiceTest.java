/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ImpPlanApprovals;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.LoadWindow;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanApprovalsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.LoadCategoriesDAO;
import com.tsi.workflow.dao.LoadFreezeDAO;
import com.tsi.workflow.dao.LoadWindowDAO;
import com.tsi.workflow.dao.PlatformDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.SystemPdddsMappingDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.ProblemTicketDAO;
import com.tsi.workflow.utils.JSONResponse;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CommonBaseServiceTest {

    public CommonBaseServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    CommonBaseService instance;

    @Before
    public void setUp() {
	try {
	    CommonBaseService realInstance = new CommonBaseService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, ProductionLoadsDAO.class);
	    TestCaseMockService.doMockDAO(instance, PutLevelDAO.class);
	    TestCaseMockService.doMockDAO(instance, ProjectDAO.class);

	} catch (Exception ex) {
	    Logger.getLogger(CommonBaseServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCommonService() throws Exception {
	TestCaseExecutor.doTest(instance, CommonBaseService.class);
    }

    /**
     * Test of getActivityLogDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetActivityLogDAO() {

	CommonBaseService instance = new CommonBaseService();
	ActivityLogDAO expResult = null;
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ActivityLogDAO result = instance.getActivityLogDAO();
	assertNotNull(result);
    }

    /**
     * Test of setActivityLogDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetActivityLogDAO() {

	ActivityLogDAO activityLogDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setActivityLogDAO(activityLogDAO);
	assertNull(instance.activityLogDAO);
    }

    /**
     * Test of getBuildDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetBuildDAO() {

	CommonBaseService instance = new CommonBaseService();
	BuildDAO expResult = null;
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	BuildDAO result = instance.getBuildDAO();
	assertNotNull(result);
    }

    /**
     * Test of setBuildDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetBuildDAO() {

	BuildDAO buildDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setBuildDAO(buildDAO);
	assertNull(instance.buildDAO);
    }

    /**
     * Test of getImpPlanApprovalsDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetImpPlanApprovalsDAO() {

	CommonBaseService instance = new CommonBaseService();
	ImpPlanApprovalsDAO expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanApprovalsDAO", mock(ImpPlanApprovalsDAO.class));
	ImpPlanApprovalsDAO result = instance.getImpPlanApprovalsDAO();
	assertNotNull(result);
    }

    /**
     * Test of setImpPlanApprovalsDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetImpPlanApprovalsDAO() {

	ImpPlanApprovalsDAO impPlanApprovalsDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setImpPlanApprovalsDAO(impPlanApprovalsDAO);
	assertNull(instance.impPlanApprovalsDAO);
    }

    /**
     * Test of getImpPlanDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetImpPlanDAO() {

	CommonBaseService instance = new CommonBaseService();
	ImpPlanDAO expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ImpPlanDAO result = instance.getImpPlanDAO();
	assertNotNull(result);
    }

    /**
     * Test of setImpPlanDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetImpPlanDAO() {

	ImpPlanDAO impPlanDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setImpPlanDAO(impPlanDAO);
	assertNull(instance.impPlanDAO);
    }

    /**
     * Test of getCheckoutSegmentsDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetCheckoutSegmentsDAO() {

	CommonBaseService instance = new CommonBaseService();
	CheckoutSegmentsDAO expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	CheckoutSegmentsDAO result = instance.getCheckoutSegmentsDAO();
	assertNotNull(result);
    }

    /**
     * Test of setCheckoutSegmentsDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetCheckoutSegmentsDAO() {

	CheckoutSegmentsDAO checkoutSegmentsDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setCheckoutSegmentsDAO(checkoutSegmentsDAO);
	assertNull(instance.checkoutSegmentsDAO);
    }

    /**
     * Test of getImplementationDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetImplementationDAO() {

	CommonBaseService instance = new CommonBaseService();
	ImplementationDAO expResult = null;
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ImplementationDAO result = instance.getImplementationDAO();
	assertNotNull(result);
    }

    /**
     * Test of setImplementationDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetImplementationDAO() {

	ImplementationDAO implementationDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setImplementationDAO(implementationDAO);
	assertNull(instance.impPlanApprovalsDAO);
    }

    /**
     * Test of getLoadCategoriesDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadCategoriesDAO() {

	CommonBaseService instance = new CommonBaseService();
	LoadCategoriesDAO expResult = null;
	ReflectionTestUtils.setField(instance, "loadCategoriesDAO", mock(LoadCategoriesDAO.class));
	LoadCategoriesDAO result = instance.getLoadCategoriesDAO();
	assertNotNull(result);
    }

    /**
     * Test of setLoadCategoriesDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetLoadCategoriesDAO() {

	LoadCategoriesDAO loadCategoriesDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setLoadCategoriesDAO(loadCategoriesDAO);
	assertNull(instance.loadCategoriesDAO);
    }

    /**
     * Test of getLoadFreezeDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadFreezeDAO() {

	CommonBaseService instance = new CommonBaseService();
	LoadFreezeDAO expResult = null;
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	LoadFreezeDAO result = instance.getLoadFreezeDAO();
	assertNotNull(result);
    }

    /**
     * Test of setLoadFreezeDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetLoadFreezeDAO() {

	LoadFreezeDAO loadFreezeDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setLoadFreezeDAO(loadFreezeDAO);
	assertNull(instance.getLoadFreezeDAO());
    }

    /**
     * Test of getLoadWindowDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadWindowDAO() {

	CommonBaseService instance = new CommonBaseService();
	LoadWindowDAO expResult = null;
	ReflectionTestUtils.setField(instance, "loadWindowDAO", mock(LoadWindowDAO.class));
	LoadWindowDAO result = instance.getLoadWindowDAO();
	assertNotNull(result);
    }

    /**
     * Test of setLoadWindowDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetLoadWindowDAO() {

	LoadWindowDAO loadWindowDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setLoadWindowDAO(loadWindowDAO);
	assertNull(instance.getLoadWindowDAO());
    }

    /**
     * Test of getPlatformDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetPlatformDAO() {

	CommonBaseService instance = new CommonBaseService();
	PlatformDAO expResult = null;
	ReflectionTestUtils.setField(instance, "platformDAO", mock(PlatformDAO.class));
	PlatformDAO result = instance.getPlatformDAO();
	assertNotNull(result);
    }

    /**
     * Test of setPlatformDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetPlatformDAO() {

	PlatformDAO platformDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setPlatformDAO(platformDAO);
	assertNull(instance.getPlatformDAO());
    }

    /**
     * Test of getProjectDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetProjectDAO() {

	CommonBaseService instance = new CommonBaseService();
	ProjectDAO expResult = null;
	ReflectionTestUtils.setField(instance, "projectDAO", mock(ProjectDAO.class));
	ProjectDAO result = instance.getProjectDAO();
	assertNotNull(result);

    }

    /**
     * Test of setProjectDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetProjectDAO() {

	ProjectDAO projectDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setProjectDAO(projectDAO);
	assertNull(instance.getProjectDAO());

    }

    /**
     * Test of getPutLevelDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetPutLevelDAO() {

	CommonBaseService instance = new CommonBaseService();
	PutLevelDAO expResult = null;
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	PutLevelDAO result = instance.getPutLevelDAO();
	assertNotNull(result);

    }

    /**
     * Test of setPutLevelDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetPutLevelDAO() {

	PutLevelDAO putLevelDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setPutLevelDAO(putLevelDAO);
	assertNull(instance.getPutLevelDAO());

    }

    /**
     * Test of getSystemDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemDAO() {

	CommonBaseService instance = new CommonBaseService();
	SystemDAO expResult = null;
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	SystemDAO result = instance.getSystemDAO();
	assertNotNull(result);

    }

    /**
     * Test of setSystemDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetSystemDAO() {

	SystemDAO systemDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setSystemDAO(systemDAO);
	assertNull(instance.getSystemDAO());

    }

    /**
     * Test of getSystemLoadDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadDAO() {

	CommonBaseService instance = new CommonBaseService();
	SystemLoadDAO expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	SystemLoadDAO result = instance.getSystemLoadDAO();
	assertNotNull(result);
    }

    /**
     * Test of setSystemLoadDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetSystemLoadDAO() {

	SystemLoadDAO systemLoadDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setSystemLoadDAO(systemLoadDAO);
	assertNull(instance.getSystemDAO());

    }

    /**
     * Test of getVparsDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetVparsDAO() {

	CommonBaseService instance = new CommonBaseService();
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	VparsDAO expResult = null;
	VparsDAO result = instance.getVparsDAO();
	assertNotNull(result);
    }

    /**
     * Test of setVparsDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetVparsDAO() {

	VparsDAO vparsDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setVparsDAO(vparsDAO);
	assertNull(instance.getVparsDAO());

    }

    /**
     * Test of getProblemTicketDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetProblemTicketDAO() {

	CommonBaseService instance = new CommonBaseService();
	ProblemTicketDAO expResult = null;
	ReflectionTestUtils.setField(instance, "problemTicketDAO", mock(ProblemTicketDAO.class));
	ProblemTicketDAO result = instance.getProblemTicketDAO();
	assertNotNull(result);
    }

    /**
     * Test of setProblemTicketDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetProblemTicketDAO() {

	ProblemTicketDAO problemTicketDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setProblemTicketDAO(problemTicketDAO);
	assertNull(instance.getProblemTicketDAO());

    }

    /**
     * Test of getSystemLoadActionsDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActionsDAO() {

	CommonBaseService instance = new CommonBaseService();
	SystemLoadActionsDAO expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	SystemLoadActionsDAO result = instance.getSystemLoadActionsDAO();
	assertNotNull(result);
    }

    /**
     * Test of setSystemLoadActionsDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetSystemLoadActionsDAO() {

	SystemLoadActionsDAO systemLoadActionsDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setSystemLoadActionsDAO(systemLoadActionsDAO);
	assertNull(instance.getSystemLoadActionsDAO());

    }

    /**
     * Test of getDbcrDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetDbcrDAO() {

	CommonBaseService instance = new CommonBaseService();
	DbcrDAO expResult = null;
	ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	DbcrDAO result = instance.getDbcrDAO();
	assertNotNull(result);
    }

    /**
     * Test of setDbcrDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetDbcrDAO() {

	DbcrDAO dbcrDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setDbcrDAO(dbcrDAO);
	assertNull(instance.getDbcrDAO());

    }

    /**
     * Test of getProductionLoadsDAO method, of class CommonBaseService.
     */
    @Test
    public void testGetProductionLoadsDAO() {

	CommonBaseService instance = new CommonBaseService();
	ProductionLoadsDAO expResult = null;
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	ProductionLoadsDAO result = instance.getProductionLoadsDAO();
	assertNotNull(result);
    }

    /**
     * Test of setProductionLoadsDAO method, of class CommonBaseService.
     */
    @Test
    public void testSetProductionLoadsDAO() {

	ProductionLoadsDAO productionLoadsDAO = null;
	CommonBaseService instance = new CommonBaseService();
	instance.setProductionLoadsDAO(productionLoadsDAO);
	assertNull(instance.getProductionLoadsDAO());

    }

    /**
     * Test of getVpars method, of class CommonBaseService.
     */
    @Test
    public void testGetVpars() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	JSONResponse result = instance.getVpars(id);
	assertNotNull(result);

    }

    @Test
    public void testGetVpars1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	when(instance.vparsDAO.find(id)).thenReturn(new Vpars());
	JSONResponse result = instance.getVpars(id);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoad method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoad() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	JSONResponse result = instance.getSystemLoad(id);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoad method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoad1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	when(instance.systemLoadDAO.find(id)).thenReturn(new SystemLoad());
	JSONResponse result = instance.getSystemLoad(id);
	assertNotNull(result);
    }

    /**
     * Test of getPutLevel method, of class CommonBaseService.
     */
    @Test
    public void testGetPutLevel() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	when(instance.putLevelDAO.find(id)).thenReturn(new PutLevel());
	JSONResponse result = instance.getPutLevel(id);
	assertNotNull(result);

    }

    /**
     * Test of getPutLevel method, of class CommonBaseService.
     */
    @Test
    public void testGetPutLevel1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	JSONResponse result = instance.getPutLevel(id);
	assertNotNull(result);

    }

    /**
     * Test of getSystem method, of class CommonBaseService.
     */
    @Test
    public void testGetSystem() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	JSONResponse result = instance.getSystem(id);
	assertNotNull(result);

    }

    /**
     * Test of getSystem method, of class CommonBaseService.
     */
    @Test
    public void testGetSystem1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	when(instance.systemDAO.find(id)).thenReturn(new com.tsi.workflow.beans.dao.System());
	JSONResponse result = instance.getSystem(id);
	assertNotNull(result);

    }

    /**
     * Test of getProject method, of class CommonBaseService.
     */
    @Test
    public void testGetProject() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "projectDAO", mock(ProjectDAO.class));
	JSONResponse result = instance.getProject(id);
	assertNotNull(result);
    }

    /**
     * Test of getProject method, of class CommonBaseService.
     */
    @Test
    public void testGetProject1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "projectDAO", mock(ProjectDAO.class));
	when(instance.projectDAO.find(id)).thenReturn(new Project());
	JSONResponse result = instance.getProject(id);
	assertNotNull(result);
    }

    /**
     * Test of getPlatform method, of class CommonBaseService.
     */
    @Test
    public void testGetPlatform1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "platformDAO", mock(PlatformDAO.class));
	when(instance.platformDAO.find(id)).thenReturn(new Platform());
	JSONResponse result = instance.getPlatform(id);
	assertNotNull(result);
    }

    @Test
    public void testGetPlatform() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "platformDAO", mock(PlatformDAO.class));
	JSONResponse result = instance.getPlatform(id);
	assertNotNull(result);
    }

    /**
     * Test of getLoadWindow method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadWindow() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadWindowDAO", mock(LoadWindowDAO.class));
	when(instance.loadWindowDAO.find(id)).thenReturn(new LoadWindow());
	JSONResponse result = instance.getLoadWindow(id);
	assertNotNull(result);
    }

    /**
     * Test of getLoadFreeze method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadFreeze() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	when(instance.loadFreezeDAO.find(id)).thenReturn(new LoadFreeze());
	JSONResponse result = instance.getLoadFreeze(id);
	assertNotNull(result);
    }

    /**
     * Test of getLoadCategory method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadCategory() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadCategoriesDAO", mock(LoadCategoriesDAO.class));
	when(instance.loadCategoriesDAO.find(id)).thenReturn(new LoadCategories());
	JSONResponse result = instance.getLoadCategory(id);
	assertNotNull(result);
    }

    /**
     * Test of getImplementation method, of class CommonBaseService.
     */
    @Test
    public void testGetImplementation() {

	String id = "";
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	when(instance.implementationDAO.find(id)).thenReturn(new Implementation());
	JSONResponse result = instance.getImplementation(id);
	assertNotNull(result);
    }

    @Test
    public void testGetLoadWindow1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadWindowDAO", mock(LoadWindowDAO.class));
	JSONResponse result = instance.getLoadWindow(id);
	assertNotNull(result);
    }

    /**
     * Test of getLoadFreeze method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadFreeze1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	JSONResponse result = instance.getLoadFreeze(id);
	assertNotNull(result);
    }

    /**
     * Test of getLoadCategory method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadCategory1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadCategoriesDAO", mock(LoadCategoriesDAO.class));
	JSONResponse result = instance.getLoadCategory(id);
	assertNotNull(result);
    }

    /**
     * Test of getImplementation method, of class CommonBaseService.
     */
    @Test
    public void testGetImplementation1() {

	String id = "";
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	JSONResponse result = instance.getImplementation(id);
	assertNotNull(result);
    }

    /**
     * Test of getSegmentMapping method, of class CommonBaseService.
     */
    @Test
    public void testGetSegmentMapping() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	when(instance.checkoutSegmentsDAO.find(id)).thenReturn(new CheckoutSegments());
	JSONResponse result = instance.getSegmentMapping(id);
	assertNotNull(result);

    }

    @Test
    public void testGetSegmentMapping1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	JSONResponse result = instance.getSegmentMapping(id);
	assertNotNull(result);

    }

    /**
     * Test of getPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetPlan() {

	String id = "";
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	when(instance.impPlanDAO.find(id)).thenReturn(new ImpPlan());
	JSONResponse result = instance.getPlan(id);
	assertNotNull(result);
    }

    /**
     * Test of getPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetPlan1() {

	String id = "";
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getPlan(id);
	assertNotNull(result);
    }

    /**
     * Test of getPlanApprovals method, of class CommonBaseService.
     */
    @Test
    public void testGetPlanApprovals() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanApprovalsDAO", mock(ImpPlanApprovalsDAO.class));
	when(instance.impPlanApprovalsDAO.find(id)).thenReturn(new ImpPlanApprovals());
	JSONResponse result = instance.getPlanApprovals(id);
	assertNotNull(result);
    }

    @Test
    public void testGetPlanApprovals1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanApprovalsDAO", mock(ImpPlanApprovalsDAO.class));
	JSONResponse result = instance.getPlanApprovals(id);
	assertNotNull(result);
    }

    /**
     * Test of getBuild method, of class CommonBaseService.
     */
    @Test
    public void testGetBuild() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	when(instance.buildDAO.find(id)).thenReturn(new Build());
	JSONResponse result = instance.getBuild(id);
	assertNotNull(result);
    }

    /**
     * Test of getBuild method, of class CommonBaseService.
     */
    @Test
    public void testGetBuild1() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	JSONResponse result = instance.getBuild(id);
	assertNotNull(result);
    }

    /**
     * Test of getPlanApprovalsList method, of class CommonBaseService.
     */
    @Test
    public void testGetPlanApprovalsList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanApprovalsDAO", mock(ImpPlanApprovalsDAO.class));
	JSONResponse result = instance.getPlanApprovalsList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getBuildList method, of class CommonBaseService.
     */
    @Test
    public void testGetBuildList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	JSONResponse result = instance.getBuildList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getSegmentMappingList method, of class CommonBaseService.
     */
    @Test
    public void testGetSegmentMappingList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	JSONResponse result = instance.getSegmentMappingList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getLoadFreezeList method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadFreezeList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	JSONResponse result = instance.getLoadFreezeList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getLoadCategoryList method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadCategoryList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadCategoriesDAO", mock(LoadCategoriesDAO.class));
	JSONResponse result = instance.getLoadCategoryList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of testGetGroupLoadFreezeList method, of class CommonBaseService.
     */
    @Test
    public void testGetGroupLoadFreezeList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	JSONResponse result = instance.getGroupLoadFreezeList(pOffset, pLimit, pOrderBy, null);
	assertNotNull(result);

    }

    /**
     * Test of testgetSystemPdddsMapping method, of class CommonBaseService.
     */
    @Test
    public void testgetSystemPdddsMapping() {
	CommonBaseService instance = new CommonBaseService();
	ReflectionTestUtils.setField(instance, "systemPdddsMappingDAO", mock(SystemPdddsMappingDAO.class));
	JSONResponse result = instance.getSystemPdddsMapping(1);
	assertNotNull(result);

    }

    /**
     * Test of testgetSystemPdddsMapping method, of class CommonBaseService.
     */
    @Test
    public void testgetSystemPdddsMappingList() {
	Integer[] idsList = new Integer[] { 1 };
	CommonBaseService instance = new CommonBaseService();
	ReflectionTestUtils.setField(instance, "systemPdddsMappingDAO", mock(SystemPdddsMappingDAO.class));
	JSONResponse result = instance.getSystemPdddsMappingList(idsList);
	assertNotNull(result);

    }

    /**
     * Test of getLoadWindowList method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadWindowList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadWindowDAO", mock(LoadWindowDAO.class));
	JSONResponse result = instance.getLoadWindowList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getPlatformList method, of class CommonBaseService.
     */
    @Test
    public void testGetPlatformList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "platformDAO", mock(PlatformDAO.class));
	JSONResponse result = instance.getPlatformList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getProjectList method, of class CommonBaseService.
     */
    @Test
    public void testGetProjectList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "projectDAO", mock(ProjectDAO.class));
	JSONResponse result = instance.getProjectList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getPutLevelList method, of class CommonBaseService.
     */
    @Test
    public void testGetPutLevelList() {

	Integer pOffset = null;
	Integer pLimit = null;
	String filter = "";
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	JSONResponse result = instance.getPutLevelList(pOffset, pLimit, pOrderBy, filter);
	assertNotNull(result);

    }

    /**
     * Test of getPutLevelList method, of class CommonBaseService.
     */
    @Test
    public void testGetPutLevelList2() {

	Integer pOffset = null;
	Integer pLimit = null;
	String filter = "1";
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	JSONResponse result = instance.getPutLevelList(pOffset, pLimit, pOrderBy, filter);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadList method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	JSONResponse result = instance.getSystemLoadList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getSystemList method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	JSONResponse result = instance.getSystemList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getVparsList method, of class CommonBaseService.
     */
    @Test
    public void testGetVparsList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	JSONResponse result = instance.getVparsList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getPlanList method, of class CommonBaseService.
     */
    @Test
    public void testGetPlanList() {

	Integer pOffset = null;
	Integer pLimit = null;
	String filter = "";
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getPlanList(pOffset, pLimit, filter, pOrderBy);
	assertNotNull(result);

    }

    @Test
    public void testGetPlanList1() {

	Integer pOffset = null;
	Integer pLimit = null;
	String filter = "id";
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getPlanList(pOffset, pLimit, filter, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getImplementationList method, of class CommonBaseService.
     */
    @Test
    public void testGetImplementationList() {

	Integer pOffset = null;
	Integer pLimit = null;
	String filter = "";
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	JSONResponse result = instance.getImplementationList(pOffset, pLimit, filter, pOrderBy);
	assertNotNull(result);

    }

    @Test
    public void testGetImplementationList1() {

	Integer pOffset = null;
	Integer pLimit = null;
	String filter = "filter";
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	JSONResponse result = instance.getImplementationList(pOffset, pLimit, filter, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getBuildByPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetBuildByPlan() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	JSONResponse result = instance.getBuildByPlan(ids);
	assertNotNull(result);

    }

    /**
     * Test of getBuildBySystem method, of class CommonBaseService.
     */
    @Test
    public void testGetBuildBySystem() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	JSONResponse result = instance.getBuildBySystem(ids);
	assertNotNull(result);

    }

    /**
     * Test of getImplementationByPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetImplementationByPlan() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	JSONResponse result = instance.getImplementationByPlan(ids);
	assertNotNull(result);

    }

    /**
     * Test of getPlanByProject method, of class CommonBaseService.
     */
    @Test
    public void testGetPlanByProject() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getPlanByProject(ids);
	assertNotNull(result);

    }

    /**
     * Test of getPlanApprovalsByPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetPlanApprovalsByPlan() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanApprovalsDAO", mock(ImpPlanApprovalsDAO.class));
	JSONResponse result = instance.getPlanApprovalsByPlan(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSegmentMappingByPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetSegmentMappingByPlan() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	JSONResponse result = instance.getSegmentMappingByPlan(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSegmentMappingByImplementation method, of class CommonBaseService.
     */
    @Test
    public void testGetSegmentMappingByImplementation() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	JSONResponse result = instance.getSegmentMappingByImplementation(ids);
	assertNotNull(result);

    }

    /**
     * Test of getLoadCategoriesBySystem method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadCategoriesBySystem() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadCategoriesDAO", mock(LoadCategoriesDAO.class));
	JSONResponse result = instance.getLoadCategoriesBySystem(ids);
	assertNotNull(result);

    }

    /**
     * Test of getLoadFreezeByLoadCategories method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadFreezeByLoadCategories() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	JSONResponse result = instance.getLoadFreezeByLoadCategories(ids);
	assertNotNull(result);

    }

    /**
     * Test of getLoadWindowByLoadCategories method, of class CommonBaseService.
     */
    @Test
    public void testGetLoadWindowByLoadCategories() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "loadWindowDAO", mock(LoadWindowDAO.class));
	JSONResponse result = instance.getLoadWindowByLoadCategories(ids);
	assertNotNull(result);

    }

    /**
     * Test of getPutLevelBySystem method, of class CommonBaseService.
     */
    @Test
    public void testGetPutLevelBySystem() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	JSONResponse result = instance.getPutLevelBySystem(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemByPlatform method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemByPlatform() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	JSONResponse result = instance.getSystemByPlatform(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadByLoadCategories method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadByLoadCategories() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	JSONResponse result = instance.getSystemLoadByLoadCategories(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadByPutLevel method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadByPutLevel() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	JSONResponse result = instance.getSystemLoadByPutLevel(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadByPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadByPlan() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	JSONResponse result = instance.getSystemLoadByPlan(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadBySystem method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadBySystem() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	JSONResponse result = instance.getSystemLoadBySystem(ids);
	assertNotNull(result);

    }

    /**
     * Test of getVparsBySystem method, of class CommonBaseService.
     */
    @Test
    public void testGetVparsBySystem() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	JSONResponse result = instance.getVparsBySystem(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSegmentMappingBySystemLoad method, of class CommonBaseService.
     */
    @Test
    public void testGetSegmentMappingBySystemLoad() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	JSONResponse result = instance.getSegmentMappingBySystemLoad(ids);
	assertNotNull(result);

    }

    /**
     * Test of getProblemTicket method, of class CommonBaseService.
     */
    @Test
    public void testGetProblemTicket() {

	String ticketNumber = "";
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "problemTicketDAO", mock(ProblemTicketDAO.class));
	JSONResponse result = instance.getProblemTicket(ticketNumber);
	assertNotNull(result);

    }

    /**
     * Test of getSystemListByPlan method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemListByPlan() {

	String planId = DataWareHouse.getPlan().getId();
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	instance.impPlanDAO = mock(ImpPlanDAO.class);
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	when(instance.impPlanDAO.find(planId)).thenReturn(DataWareHouse.getPlan());
	// ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getSystemListByPlan(planId);
	assertNotNull(result);
    }

    /**
     * Test of getActivityLogByPlanId method, of class CommonBaseService.
     */
    @Test
    public void testGetActivityLogByPlanId() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse result = instance.getActivityLogByPlanId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getActivityLogByImpId method, of class CommonBaseService.
     */
    @Test
    public void testGetActivityLogByImpId() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse result = instance.getActivityLogByImpId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getActivityLogList method, of class CommonBaseService.
     */
    @Test
    public void testGetActivityLogList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse result = instance.getActivityLogList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getActivityLog method, of class CommonBaseService.
     */
    @Test
    public void testGetActivityLog() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse result = instance.getActivityLog(id);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActionsBySystemId method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActionsBySystemId() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	JSONResponse result = instance.getSystemLoadActionsBySystemId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActionsByPlanId method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActionsByPlanId() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	JSONResponse result = instance.getSystemLoadActionsByPlanId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActionsBySystemLoadId method, of class
     * CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActionsBySystemLoadId() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	JSONResponse result = instance.getSystemLoadActionsBySystemLoadId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActionsByVparId method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActionsByVparId() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	JSONResponse result = instance.getSystemLoadActionsByVparId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActionsList method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActionsList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	JSONResponse result = instance.getSystemLoadActionsList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActions method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemLoadActions() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	JSONResponse result = instance.getSystemLoadActions(id);
	assertNotNull(result);

    }

    /**
     * Test of getDbcrByPlanId method, of class CommonBaseService.
     */
    @Test
    public void testGetDbcrByPlanId() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	JSONResponse result = instance.getDbcrByPlanId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getDbcrBySystemId method, of class CommonBaseService.
     */
    @Test
    public void testGetDbcrBySystemId() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	JSONResponse result = instance.getDbcrBySystemId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getDbcrList method, of class CommonBaseService.
     */
    @Test
    public void testGetDbcrList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	JSONResponse result = instance.getDbcrList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getDbcr method, of class CommonBaseService.
     */
    @Test
    public void testGetDbcr() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	JSONResponse expResult = null;
	JSONResponse result = instance.getDbcr(id);
	assertNotNull(result);

    }

    /**
     * Test of getProductionLoadsByPlanId method, of class CommonBaseService.
     */
    @Test
    public void testGetProductionLoadsByPlanId() {

	String[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	JSONResponse result = instance.getProductionLoadsByPlanId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getProductionLoadsBySystemId method, of class CommonBaseService.
     */
    @Test
    public void testGetProductionLoadsBySystemId() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	JSONResponse result = instance.getProductionLoadsBySystemId(ids);
	assertNotNull(result);

    }
    //

    /**
     * Test of getProductionLoadsBySystemLoadId method, of class CommonBaseService.
     */
    @Test
    public void testGetProductionLoadsBySystemLoadId() {

	Integer[] ids = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	JSONResponse result = instance.getProductionLoadsBySystemLoadId(ids);
	assertNotNull(result);

    }

    /**
     * Test of getProductionLoadsList method, of class CommonBaseService.
     */
    @Test
    public void testGetProductionLoadsList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	JSONResponse result = instance.getProductionLoadsList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }

    /**
     * Test of getProductionLoads method, of class CommonBaseService.
     */
    @Test
    public void testGetProductionLoads() {

	Integer id = null;
	CommonBaseService instance = new CommonBaseService();
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	JSONResponse result = instance.getProductionLoads(id);
	assertNotNull(result);

    }

    /**
     * Test of getSystemCpuList method, of class CommonBaseService.
     */
    @Test
    public void testGetSystemCpuList() {

	Integer pOffset = null;
	Integer pLimit = null;
	LinkedHashMap<String, String> pOrderBy = null;
	CommonBaseService instance = new CommonBaseService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	JSONResponse result = instance.getSystemCpuList(pOffset, pLimit, pOrderBy);
	assertNotNull(result);

    }
    //
}
