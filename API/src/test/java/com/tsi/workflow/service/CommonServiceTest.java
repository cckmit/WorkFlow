/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class CommonServiceTest {

    CommonService instance;

    public CommonServiceTest() {
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
	    CommonService realInstance = new CommonService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);

	} catch (Exception ex) {
	    Logger.getLogger(CommonServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCommonService() throws Exception {
	TestCaseExecutor.doTest(instance, CommonService.class);
    }

    @Test
    public void testGetFileSyncInfo() throws Exception {
	// instance = new CommonService();
	String expResult = "test|NODATA";
	String result = instance.getFileSyncInfo("test", "test", "test");
	Assert.assertEquals(expResult, result);
	instance.getFileSyncInfo("", "test", "test");
	instance.getFileSyncInfo("test", "", "test");
	instance.getFileSyncInfo(null, "test", "test");
	instance.getFileSyncInfo("test", null, "test");

	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	when(instance.getCheckoutSegmentsDAO().findCommonFilesStatus("test", "test", "test")).thenReturn(Arrays.asList("a"));
	instance.getFileSyncInfo("test", "test", "test");
	when(instance.getCheckoutSegmentsDAO().findCommonFiles("test", "test")).thenReturn(Arrays.asList("a"));
	instance.getFileSyncInfo("test", "test", "test");
    }

    @Test
    public void testGetPutDeployDate() {
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	when(instance.getSystemDAO().findByName("")).thenReturn(DataWareHouse.getSystemList().get(0));
	instance.getPutDeployDate("", "");
	ReflectionTestUtils.setField(instance, "putLevelDAO", mock(PutLevelDAO.class));
	when(instance.getPutLevelDAO().findBySystemAndPutName("", DataWareHouse.getSystemList().get(0).getId())).thenReturn((PutLevel) DataWareHouse.getPutLevelList().toArray()[0]);
	instance.getPutDeployDate("", "");
    }

    @Test
    public void testGetTAPDetails() {
	String planId = DataWareHouse.getPlan().getId();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	when(instance.getSystemDAO().findByName("")).thenReturn(DataWareHouse.getSystemList().get(0));
	when(instance.getSystemLoadDAO().findBy(new ImpPlan(planId), DataWareHouse.getSystemList().get(0))).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
	List<SystemLoad> lLoads = DataWareHouse.getPlan().getSystemLoadList();
	when(instance.getSystemLoadDAO().getStagingDepedendentPlansWithDate(planId, DataWareHouse.getSystemList().get(0).getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime(), Constants.PlanStatus.getApprovedAndAboveStatus().keySet(), true)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	instance.getTAPDetails(planId, "", null, true, false);
	when(instance.getSystemLoadDAO().getStagingDepedendentPlansWithDate("", DataWareHouse.getSystemList().get(0).getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime(), Constants.PlanStatus.getApprovedAndAboveStatus().keySet(), true)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	instance.getTAPDetails("", "", Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
	List<String> lPlanList = new ArrayList<>();
	lLoads.stream().forEach(t -> lPlanList.add(t.getPlanId().getId()));
	HashMap<String, List<String>> planMap = new HashMap();
	planMap.put(planId, Constants.lNONIBMTemplate);
	when(instance.getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]))).thenReturn(planMap);
	instance.getTAPDetails("", "", Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
	planMap.put(planId, Constants.lIBMTemplate);
	when(instance.getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]))).thenReturn(planMap);
	instance.getTAPDetails("", "", Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
	instance.getTAPDetails(planId, null, null, true, false);
	instance.getTAPDetails(null, DataWareHouse.getSystemList().get(0).getName(), Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
    }

    @Test
    public void testGetTAPDetails1() {
	String planId = DataWareHouse.getPlan().getId();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	when(instance.getImpPlanDAO().findByPlanStatus(planId, new ArrayList(Constants.PlanStatus.getApprovedAndAboveStatus().keySet()))).thenReturn(DataWareHouse.getPlan());
	when(instance.getSystemDAO().findByName("")).thenReturn(DataWareHouse.getSystemList().get(0));
	when(instance.getSystemLoadDAO().findBy(new ImpPlan(planId), DataWareHouse.getSystemList().get(0))).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
	List<SystemLoad> lLoads = DataWareHouse.getPlan().getSystemLoadList();
	when(instance.getSystemLoadDAO().getStagingDepedendentPlansWithDate(planId, DataWareHouse.getSystemList().get(0).getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime(), Constants.PlanStatus.getApprovedAndAboveStatus().keySet(), true)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	instance.getTAPDetails(planId, "", null, true, false);
	when(instance.getSystemLoadDAO().getStagingDepedendentPlansWithDate("", DataWareHouse.getSystemList().get(0).getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime(), Constants.PlanStatus.getApprovedAndAboveStatus().keySet(), true)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	instance.getTAPDetails("", "", Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
	List<String> lPlanList = new ArrayList<>();
	lLoads.stream().forEach(t -> lPlanList.add(t.getPlanId().getId()));
	HashMap<String, List<String>> planMap = new HashMap();
	planMap.put(planId, Constants.lNONIBMTemplate);
	when(instance.getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]))).thenReturn(planMap);
	instance.getTAPDetails("", "", Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
	when(instance.getCheckoutSegmentsDAO().getFileTypesByPlan(lPlanList.toArray(new String[0]))).thenReturn(planMap);
	instance.getTAPDetails("", "", Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
	instance.getTAPDetails(planId, null, null, true, false);
	instance.getTAPDetails(null, DataWareHouse.getSystemList().get(0).getName(), Constants.JENKINS_DATEFORMAT.get().format(DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime()), true, false);
    }

    @Test
    public void testGetLocalConfigDetails2() {
	when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(null);
	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), "APO");
    }

    @Test
    public void testGetLocalConfigDetails3() {
	when(instance.getSystemDAO().findByName("APO")).thenReturn(null);
	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), "APO");
    }

    @Test
    public void testGetLocalConfigDetails4() {
	when(instance.getSystemLoadDAO().findBy(DataWareHouse.getPlan(), DataWareHouse.getSystemList().get(0))).thenReturn(null);
	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), "APO");
    }

    @Test
    public void testGetLocalConfigDetails5() {
	SystemLoad systemLoad = new SystemLoad();
	when(instance.getSystemLoadDAO().findBy(DataWareHouse.getPlan(), DataWareHouse.getSystemList().get(0))).thenReturn(systemLoad);
	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), "APO");
    }

    @Test
    public void testGetLocalConfigDetails() {
	instance = new CommonService();
	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), null);
    }

    @Test
    public void testGetLocalConfigDetails1() {
	instance = new CommonService();
	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), "");
    }

    @Test
    public void testGetLocalConfigDetailsPlan() {
	instance = new CommonService();
	instance.getLocalConfigDetails(null, DataWareHouse.getSystemList().get(0).getName());
    }

    @Test
    public void testGetLocalConfigDetailsPlanEmpty() {
	instance = new CommonService();
	instance.getLocalConfigDetails("", DataWareHouse.getSystemList().get(0).getName());
    }

    @Test
    public void testGetLocalConfigDetails6() {
	ImpPlan lPlan = DataWareHouse.getPlan();
	SystemLoad systemLoad = lPlan.getSystemLoadList().get(0);
	Object[] object = new Object[2];
	object[0] = systemLoad;
	object[1] = DataWareHouse.getPlan();
	List<Object[]> lList = new ArrayList();
	lList.add(object);
	when(instance.getSystemLoadDAO().findBy(DataWareHouse.getPlan(), DataWareHouse.getSystemList().get(0))).thenReturn(systemLoad);
	when(instance.getSystemLoadDAO().getDependentPlanByApproveDate(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), systemLoad.getLoadDateTime(), Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(lList);

	instance.getLocalConfigDetails(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getName());
    }

}
