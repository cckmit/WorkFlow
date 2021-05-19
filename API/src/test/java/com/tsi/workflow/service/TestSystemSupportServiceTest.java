/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
 * @author Radha.Adhimoolam
 */
public class TestSystemSupportServiceTest {

    TestSystemSupportService instance;

    public TestSystemSupportServiceTest() {
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
	    TestSystemSupportService realInstance = new TestSystemSupportService();
	    instance = spy(realInstance);
	    // TestCaseMockService.doMockDAO(instance, DbcrDAO.class);
	    // TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    // TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    // TestCaseMockService.doMockDAO(instance, ProjectDAO.class);
	    // TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    // TestCaseMockService.doMockDAO(instance, CSRNumberDAO.class);
	    // TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    // TestCaseMockService.doMockDAO(instance, LDAPAuthenticatorImpl.class);
	    // TestCaseMockService.doMockDAO(instance, DateAuditCrossCheck.class);
	    // TestCaseMockService.doMockDAO(instance, DbcrHelper.class);
	    // TestCaseMockService.doMockDAO(instance,
	    // ExceptionLoadNotificationHelper.class);
	    // TestCaseMockService.doMockDAO(instance, LdapGroupConfig.class);
	    // TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    // TestCaseMockService.doMockDAO(instance, SequenceGenerator.class);
	    // TestCaseMockService.doMockDAO(instance, GitBlitClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    // TestCaseMockService.doMockDAO(instance, ImplementationHelper.class);
	    // TestCaseMockService.doMockDAO(instance, PlanHelper.class);
	    // TestCaseMockService.doMockDAO(instance, ProjectDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);

	} catch (Exception ex) {
	    Logger.getLogger(TestSystemSupportService.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testTestSystemSupportService() throws Exception {
	TestCaseExecutor.doTest(instance, TestSystemSupportService.class);
    }

    @Test
    public void testgetDeploymentVParsList() {

	User lCurrentUser = DataWareHouse.getUser();
	String planIds[] = new String[] { DataWareHouse.getPlan().getId() };
	Boolean isDeployedInPreProdLoads = false;
	TestSystemSupportService instance = new TestSystemSupportService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	List<com.tsi.workflow.beans.dao.System> lSystems = new ArrayList<>();
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	when(instance.getSystemCpuDAO().findBySystem(lSystems, Constants.TOSEnvironment.PRE_PROD_TOS)).thenReturn(new ArrayList<>());
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	JSONResponse result = instance.getDeploymentVParsList(lCurrentUser, planIds);
	assertNotNull(result);
    }

    @Test
    public void testgetDeploymentVParsList1() {

	User lCurrentUser = DataWareHouse.getUser();
	String planIds[] = new String[] { DataWareHouse.getPlan().getId() };
	Boolean isDeployedInPreProdLoads = false;
	List<Build> lBuildList = DataWareHouse.getPlan().getBuildList();
	for (Build lBuild : lBuildList) {
	    lBuild.setJobStatus("D");
	}
	TestSystemSupportService instance = new TestSystemSupportService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	List<com.tsi.workflow.beans.dao.System> lSystems = new ArrayList<>();
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	when(instance.getSystemCpuDAO().findBySystem(lSystems, Constants.TOSEnvironment.PRE_PROD_TOS)).thenReturn(new ArrayList<>());
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuildList);
	when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	JSONResponse result = instance.getDeploymentVParsList(lCurrentUser, planIds);
	assertNotNull(result);
    }

    @Test
    public void testPostActivationAction() {

	User lCurrentUser = DataWareHouse.getUser();
	PreProductionLoads loads = DataWareHouse.getPreProductionLoads();
	TestSystemSupportService instance = spy(new TestSystemSupportService());
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "tOSHelper", mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	// List<com.tsi.workflow.beans.dao.System> lSystems = new ArrayList<>();
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "wsserver", mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	when(instance.getPreProductionLoadsDAO().find(loads.getId())).thenReturn(loads);
	when(instance.systemLoadDAO.find(loads.getSystemLoadId().getId())).thenReturn(loads.getSystemLoadId());
	when(instance.systemDAO.find(loads.getSystemId().getId())).thenReturn(loads.getSystemId());
	ImpPlan impPlan = loads.getPlanId();
	impPlan.setMacroHeader(false);
	impPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	when(instance.getImpPlanDAO().find(loads.getPlanId().getId())).thenReturn(impPlan);
	List<Object[]> temp = new ArrayList<>();
	Object[] tempVal = { loads.getPlanId().getId() + "/" + loads.getSystemId().getName(), loads.getPlanId().getPlanStatus() };
	temp.add(tempVal);
	when(instance.impPlanDAO.getAllRelatedPlanDetail(loads.getPlanId().getId(), Arrays.asList(loads.getPlanId().getRelatedPlans().split(",")))).thenReturn(temp);
	when(instance.getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(loads.getPlanId().getId()), loads.getSystemId())).thenReturn(null);
	// Mockito.doNothing().when(instance.wsserver).sendMessage(Matchers.eq(Constants.Channels.PROD_LOAD),
	// Matchers.anyObject());

	try {
	    instance.postActivationAction(lCurrentUser, loads, false);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	loads.setId(null);
	when(instance.getPreProductionLoadsDAO().find(loads.getId())).thenReturn(loads);
	when(instance.tOSHelper.requestPreProdIP(lCurrentUser, loads.getSystemLoadId(), loads.getCpuId())).thenReturn(false);
	try {
	    instance.postActivationAction(lCurrentUser, loads, false);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.tOSHelper.requestPreProdIP(lCurrentUser, loads.getSystemLoadId(), loads.getCpuId())).thenReturn(true);
	when(instance.tOSHelper.getIP(loads.getSystemLoadId().getId())).thenReturn("");
	try {
	    instance.postActivationAction(lCurrentUser, loads, false);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.tOSHelper.getIP(loads.getSystemLoadId().getId())).thenReturn("11.11.11.11");
	when(instance.buildDAO.findLastSuccessfulBuild(loads.getPlanId().getId(), loads.getSystemLoadId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(null);
	try {
	    instance.postActivationAction(lCurrentUser, loads, false);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.tOSHelper.getIP(loads.getSystemLoadId().getId())).thenReturn("12.11.11.11");
	Build bb = new Build();
	bb.setActive("Y");
	when(instance.buildDAO.findLastSuccessfulBuild(loads.getPlanId().getId(), loads.getSystemLoadId().getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(bb);
	// instance.postActivationAction(lCurrentUser, loads);
	when(instance.fTPHelper.doFTP(lCurrentUser, loads.getSystemLoadId(), bb, "12.11.11.11", Boolean.FALSE)).thenReturn(DataWareHouse.getNegativeResponse());
	instance.postActivationAction(lCurrentUser, loads, false);
	when(instance.fTPHelper.doFTP(lCurrentUser, loads.getSystemLoadId(), bb, "12.11.11.11", Boolean.FALSE)).thenReturn(DataWareHouse.getPositiveResponse());
	when(instance.tOSHelper.doPreTOSOperation(lCurrentUser, Constants.LoadSetCommands.LOAD, loads, "", loads.getSystemLoadId(), false)).thenReturn(true);
	instance.postActivationAction(lCurrentUser, loads, false);

	when(instance.tOSHelper.doPreTOSOperation(lCurrentUser, Constants.LoadSetCommands.ACTIVATE, loads, "", loads.getSystemLoadId(), false)).thenReturn(true);
	loads.setStatus("ACTIVATED");
	instance.postActivationAction(lCurrentUser, loads, false);

	when(instance.tOSHelper.doPreTOSOperation(lCurrentUser, Constants.LoadSetCommands.DEACTIVATE, loads, "", loads.getSystemLoadId(), false)).thenReturn(true);
	loads.setStatus("DEACTIVATED");
	instance.postActivationAction(lCurrentUser, loads, false);

	when(instance.tOSHelper.doPreTOSOperation(lCurrentUser, Constants.LoadSetCommands.DELETE, loads, null, loads.getSystemLoadId(), false)).thenReturn(true);
	loads.setStatus("DELETED");
	instance.postActivationAction(lCurrentUser, loads, false);
    }

    @Test
    public void testGetPreProductionLoads() {
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", mock(PreProductionLoadsDAO.class));
	when(instance.getPreProductionLoadsDAO().findByPlanId(new String[] { DataWareHouse.getPlan().getId() })).thenReturn(Arrays.asList(DataWareHouse.getPreProductionLoads()));

	instance.getPreProductionLoads(new String[] { DataWareHouse.getPlan().getId() });
    }

    @Test
    public void testDeleteActivationAction() {
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", mock(PreProductionLoadsDAO.class));

	instance.deleteActivationAction(DataWareHouse.user, 0);
    }

    @Test
    public void testGetDeploymentPlanList() {
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ArrayList<String> lStatusList = new ArrayList<>();
	lStatusList.add(Constants.PlanStatus.PASSED_REGRESSION_TESTING.name());
	when(instance.impPlanDAO.findByStatusList(lStatusList, false, false, null, null, Constants.BUILD_TYPE.DVL_LOAD, Constants.LoaderTypes.E, 1, 5, null, true)).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	instance.getDeploymentPlanList("", 1, 5);
    }
}
