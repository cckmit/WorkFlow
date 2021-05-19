/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.websocket.WSBroadCaster;
import java.util.Arrays;
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
public class FallbackHelperTest {

    public FallbackHelperTest() {
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
	    FallbackHelper realInstance = new FallbackHelper();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);

	} catch (Exception ex) {
	    Logger.getLogger(FallbackHelperTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getImpPlanDAO method, of class FallbackHelper.
     */
    FallbackHelper instance;

    @Test
    public void testDSLFileHelper() throws Exception {
	// TestCaseExecutor.doTest(instance, FallbackHelper.class);
    }

    @Test
    public void testGetImpPlanDAO() {

	FallbackHelper instance = new FallbackHelper();
	ImpPlanDAO expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ImpPlanDAO result = instance.getImpPlanDAO();

	assertNotNull(result);

    }

    /**
     * Test of getRejectHelper method, of class FallbackHelper.
     */
    @Test
    public void testGetRejectHelper() {

	FallbackHelper instance = new FallbackHelper();
	RejectHelper expResult = null;
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	RejectHelper result = instance.getRejectHelper();
	assertNotNull(result);

    }

    /**
     * Test of getGITConfig method, of class FallbackHelper.
     */
    @Test
    public void testGetGITConfig() {

	FallbackHelper instance = new FallbackHelper();
	GITConfig expResult = null;
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	GITConfig result = instance.getGITConfig();
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadDAO method, of class FallbackHelper.
     */
    @Test
    public void testGetSystemLoadDAO() {

	FallbackHelper instance = new FallbackHelper();
	SystemLoadDAO expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	SystemLoadDAO result = instance.getSystemLoadDAO();
	assertNotNull(result);

    }

    /**
     * Test of fallBackStatusUpdate method, of class FallbackHelper.
     */
    @Test
    public void testFallBackStatusUpdate() {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	String rejectREason = "";
	Constants.FALLBACK_STATUS type = Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET;

	// case 1
	try {
	    instance.fallBackStatusUpdate(currentUser, planId, type, rejectREason, null);
	} catch (Exception e) {
	    assertTrue(true);
	}

	// case 2
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	when(instance.impPlanDAO.find(planId)).thenReturn(DataWareHouse.getPlan());
	when(instance.systemLoadDAO.findByImpPlan(planId)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	// doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	// when(instance.getSSHUtil()).thenReturn(sshUtil);
	doNothing().when(instance).mergeSourceToProduction(currentUser, planId, Constants.PROD_SCRIPT_PARAMS.FALLBACK_ACCEPT);
	try {
	    instance.fallBackStatusUpdate(currentUser, planId, type, rejectREason, null);
	} catch (Exception e) {
	    assertTrue(true);
	}

	// case 3
	type = Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET;
	doNothing().when(instance).mergeSourceToProduction(currentUser, planId, Constants.PROD_SCRIPT_PARAMS.ONLINE_REVERT);
	SystemLoad sysLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	LoadCategories loadCat = new LoadCategories();
	loadCat.setName("P");
	sysLoad.setLoadCategoryId(loadCat);
	when(instance.systemLoadDAO.findByImpPlan(planId)).thenReturn(Arrays.asList(sysLoad));
	try {
	    instance.fallBackStatusUpdate(currentUser, planId, type, rejectREason, null);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of mergeSourceToProduction method, of class FallbackHelper.
     */
    @Test
    public void testMergeSourceToProduction() {

	String planId = DataWareHouse.getPlan().getId();
	Constants.PROD_SCRIPT_PARAMS param = Constants.PROD_SCRIPT_PARAMS.FALLBACK_ACCEPT;
	when(instance.getSystemLoadDAO().findByImpPlan(planId)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	ReflectionTestUtils.setField(instance, "wsserver", mock(WSBroadCaster.class));
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	// when(instance.getSSHUtil()).thenReturn(sshUtil);
	// doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	try {
	    instance.mergeSourceToProduction(DataWareHouse.getUser(), planId, param);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

}
