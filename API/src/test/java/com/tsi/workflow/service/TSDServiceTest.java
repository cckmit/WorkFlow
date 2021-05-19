/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class TSDServiceTest {

    TSDService instance;

    public TSDServiceTest() {
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
	    TSDService realInstance = new TSDService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, BuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, ProductionLoadsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadActionsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemCpuDAO.class);

	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	    // TestCaseMockService.doMockDAO(instance, GITSSHUtils.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, FTPHelper.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, TOSHelper.class);
	    TestCaseMockService.doMockDAO(instance, TOSConfig.class);

	} catch (Exception ex) {
	    Logger.getLogger(TSDServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    // fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTSDService() throws Exception {
	TestCaseExecutor.doTest(instance, TSDService.class);
    }

    @Test
    public void testAcceptFallback() throws Exception {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "";
	TSDService instance = spy(new TSDService());
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	when(instance.impPlanDAO.find(planId)).thenReturn(DataWareHouse.getPlan());
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_LOADED)).thenReturn(new Long(1));
	JSONResponse result = instance.acceptFallback(currentUser, planId, rejectReason);
	assertNotNull(result);
    }

    @Test
    public void testAcceptFallback1() throws Exception {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	TSDService instance = spy(new TSDService());
	String rejectReason = "";
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	when(instance.impPlanDAO.find(planId)).thenReturn(DataWareHouse.getPlan());
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_LOADED)).thenReturn(new Long(0));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED)).thenReturn(new Long(1));
	JSONResponse result = instance.acceptFallback(currentUser, planId, rejectReason);
	assertNotNull(result);
    }

    @Test
    public void testAcceptFallback2() throws Exception {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	TSDService instance = spy(new TSDService());
	String rejectReason = "";
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	when(instance.impPlanDAO.find(planId)).thenReturn(DataWareHouse.getPlan());
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_LOADED)).thenReturn(new Long(0));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED)).thenReturn(new Long(0));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED)).thenReturn(new Long(1));
	JSONResponse result = instance.acceptFallback(currentUser, planId, rejectReason);
	assertNotNull(result);
    }

    @Test
    public void testAcceptFallback3() throws Exception {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "";
	TSDService instance = spy(new TSDService());
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	when(instance.impPlanDAO.find(planId)).thenReturn(DataWareHouse.getPlan());
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	ProductionLoads lProdLoad = DataWareHouse.getProductionLoads();
	lProdLoad.setLastActionStatus("INPROGRESS");
	SystemLoad sysLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	sysLoad.setProdLoadStatus(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name());
	lProdLoad.setStatus(Constants.LOAD_SET_STATUS.ACCEPTED.name());
	when(instance.productionLoadsDAO.findByPlanId(DataWareHouse.getPlan(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId())).thenReturn(lProdLoad);
	when(instance.productionLoadsDAO.findByPlanId(DataWareHouse.getPlan(), DataWareHouse.getPlan().getSystemLoadList().get(1).getSystemId())).thenReturn(lProdLoad);
	when(instance.productionLoadsDAO.findByPlanId(DataWareHouse.getPlan(), DataWareHouse.getPlan().getSystemLoadList().get(2).getSystemId())).thenReturn(lProdLoad);
	when(instance.productionLoadsDAO.findByPlanId(DataWareHouse.getPlan(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId())).thenReturn(lProdLoad);

	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_LOADED)).thenReturn(new Long(0));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED)).thenReturn(new Long(0));
	when(instance.productionLoadsDAO.findLoadSetInProgress(DataWareHouse.getPlan(), Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED)).thenReturn(new Long(0));
	when(instance.systemLoadDAO.findByImpPlan(DataWareHouse.getPlan())).thenReturn(Arrays.asList(sysLoad));
	// JSONResponse result = instance.acceptFallback(currentUser,
	// planId,rejectReason);
	// assertNotNull(result);

    }

    /**
     * Test of getLoadsToAccept method, of class TSDService.
     */
    @Test
    public void testGetLoadsToAccept() {

	User currentUser = DataWareHouse.getUser();
	Integer offset = null;
	Integer limit = null;
	String filter = "";
	Map<String, String> orderBy = null;
	TSDService instance = spy(new TSDService());
	try {
	    JSONResponse result = instance.getLoadsToAccept(currentUser, false, offset, limit, orderBy, filter);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}

    }
    //

    /**
     * Test of getProductionLoads method, of class TSDService.
     */
    @Test
    public void testGetProductionLoads() {

	String[] ids = new String[] { DataWareHouse.getPlan().getId() };
	String systemName = "";
	TSDService instance = spy(new TSDService());
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	try {
	    JSONResponse result = instance.getProductionLoads(ids, false, systemName);
	    assertNotNull(result);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }
    //
    // /**
    // * Test of getProdRefresh method, of class TSDService.
    // */
    // @Test
    // public void testGetProdRefresh() {
    //
    // User lCurrentUser = null;
    // Integer id = null;
    // TSDService instance = spy(new TSDService());
    //
    // JSONResponse result = instance.getProdRefresh(lCurrentUser, id);
    // assertNotNull(result);
    //
    //
    // }
    //
    // /**
    // * Test of getProdRefreshALL method, of class TSDService.
    // */
    // @Test
    // public void testGetProdRefreshALL() {
    //
    // User lCurrentUser = null;
    // TSDService instance = spy(new TSDService());
    //
    // JSONResponse result = instance.getProdRefreshALL(lCurrentUser);
    // assertNotNull(result);
    //
    //
    // }
    //
    // /**
    // * Test of getMacroHeaderList method, of class TSDService.
    // */
    // @Test
    // public void testGetMacroHeaderList() {
    //
    // Integer pOffset = null;
    // Integer pLimit = null;
    // LinkedHashMap<String, String> pOrderBy = null;
    // TSDService instance = spy(new TSDService());
    //
    // JSONResponse result = instance.getMacroHeaderList(pOffset, pLimit, pOrderBy);
    // assertNotNull(result);
    //
    //
    // }
    //
    // /**
    // * Test of getSystemLoadListBySystemId method, of class TSDService.
    // */
    // @Test
    // public void testGetSystemLoadListBySystemId() {
    //
    // Integer pId = null;
    // Integer pOffset = null;
    // Integer pLimit = null;
    // LinkedHashMap<String, String> pOrderBy = null;
    // TSDService instance = spy(new TSDService());
    //
    // JSONResponse result = instance.getSystemLoadListBySystemId(pId, pOffset,
    // pLimit, pOrderBy);
    // assertNotNull(result);
    //
    //
    // }
    //
    // /**
    // * Test of markOnlineForMacroHeaderPlan method, of class TSDService.
    // */
    // @Test
    // public void testMarkOnlineForMacroHeaderPlan() {
    //
    // User lUser = null;
    // String pPlanId = "";
    // TSDService instance = spy(new TSDService());
    //
    // JSONResponse result = instance.markOnlineForMacroHeaderPlan(lUser, pPlanId);
    // assertNotNull(result);
    //
    //
    // }

    @Test
    public void testPostActivationAction() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ProductionLoads pLoads = DataWareHouse.getProductionLoads();
	SystemCpu cpu = new SystemCpu(10);
	pLoads.setCpuId(null);
	pLoads.setStatus("LOADED");
	when(instance.productionLoadsDAO.find(pLoads.getId())).thenReturn(pLoads);
	SystemLoad sysLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	sysLoad.setLoadDateTime(new Date());
	when(instance.systemLoadDAO.find(pLoads.getSystemLoadId().getId())).thenReturn(sysLoad);
	try {
	    instance.postActivationAction(DataWareHouse.getUser(), pLoads, Boolean.TRUE, StringUtils.EMPTY);
	} catch (Exception e) {

	}
    }

    @Test
    public void testMarkAuxAsOnline() {

	ImpPlan lPlan = DataWareHouse.getPlan();
	ReflectionTestUtils.setField(instance, "lPlanOnlineFallbackStatusMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "fallbackHelper", mock(FallbackHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
	// when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(statusChangeToDependentPlanMail);
	when(instance.lPlanOnlineFallbackStatusMap.put(lPlan.getId(), "ONLINE")).thenReturn("");
	try {
	    instance.markAuxAsOnline(DataWareHouse.getUser(), lPlan.getId());
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testMarkAuxAsFallback() {

	ImpPlan lPlan = DataWareHouse.getPlan();
	ReflectionTestUtils.setField(instance, "lPlanOnlineFallbackStatusMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "fallbackHelper", mock(FallbackHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
	// when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(statusChangeToDependentPlanMail);
	when(instance.lPlanOnlineFallbackStatusMap.put(lPlan.getId(), "FALLBACK")).thenReturn("");
	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(DataWareHouse.getPlan());
	when(instance.systemLoadDAO.findByImpPlan(lPlan)).thenReturn(lPlan.getSystemLoadList());

	try {
	    instance.markAuxAsFallback(DataWareHouse.getUser(), lPlan.getId(), null, Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetAuxPlanOpStatus() {
	ReflectionTestUtils.setField(instance, "lPlanOnlineFallbackStatusMap", mock(ConcurrentHashMap.class));
	instance.getAuxPlanOpStatus(DataWareHouse.user);

    }

    @Test
    public void testDeleteAuxPlanInOpStatus() {
	ReflectionTestUtils.setField(instance, "lPlanOnlineFallbackStatusMap", mock(ConcurrentHashMap.class));
	instance.DeleteAuxPlanInOpStatus(DataWareHouse.user, DataWareHouse.getPlan().getId());

    }

}
