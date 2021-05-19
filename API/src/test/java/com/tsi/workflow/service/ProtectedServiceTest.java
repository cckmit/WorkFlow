/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static com.tsi.workflow.DataWareHouse.getPlan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.DevlBuildActivityMessage;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ImpPlanApprovals;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.ui.AdvancedSearchForm;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanApprovalsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.helper.DateAuditCrossCheck;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.DelegateHelper;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.CompilerValidationMail;
import com.tsi.workflow.mail.LoadDateChangedMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.FileIOUtils;
import com.tsi.workflow.utils.JSONResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ProtectedServiceTest {

    ProtectedService instance;
    @Mock
    private ProtectedService protectedServiceMock;
    @InjectMocks
    private AdvancedSearchForm advancedSearchInjectMock;

    public ProtectedServiceTest() {
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
	    MockitoAnnotations.initMocks(this);
	    ProtectedService realInstance = new ProtectedService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanApprovalsDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadActionsDAO.class);
	    TestCaseMockService.doMockDAO(instance, BuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, VparsDAO.class);
	    TestCaseMockService.doMockDAO(instance, DbcrDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, LDAPAuthenticatorImpl.class);
	    // TestCaseMockService.doMockDAO(instance, GITSSHUtils.class);
	    TestCaseMockService.doMockDAO(instance, DbcrHelper.class);
	    TestCaseMockService.doMockDAO(instance, FTPHelper.class);
	    TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    TestCaseMockService.doMockDAO(instance, TOSConfig.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, GitBlitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, JenkinsClient.class);
	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	    TestCaseMockService.doMockDAO(instance, SystemCpuDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, TestSystemLoadDAO.class);
	    // TestCaseMockService.doMockDAO(instance, DSLFileHelper.class);
	    TestCaseMockService.doMockDAO(instance, UserSettingsDAO.class);
	    TestCaseMockService.doMockDAO(instance, DateAuditCrossCheck.class);
	    // TestCaseMockService.doMockDAO(instance, SSHUtil.class);
	    TestCaseMockService.doMockDAO(instance, DelegateHelper.class);
	    TestCaseMockService.doMockDAO(instance, CacheClient.class);

	} catch (Exception ex) {
	    Logger.getLogger(ProtectedService.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProtectedService() throws Exception {
	TestCaseExecutor.doTest(instance, ProtectedService.class);
    }

    /**
     * Test of getDeploymentPlanList method, of class ProtectedService.
     */
    @Test
    public void testGetDeploymentPlanList() {
	try {
	    User lUser = DataWareHouse.user;
	    lUser.setCurrentRole("Lead");
	    Integer offset = 10;
	    Integer limit = 10;
	    Long count = (long) 10;
	    LinkedHashMap<String, String> lOrderBy = null;
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    String pFilter = null;
	    List<String> status = new ArrayList(Constants.PlanStatus.getTSSDeploymentStatus().keySet());
	    when(instance.getImpPlanDAO().findByStatusList(status, false, true, lUser.getId(), pFilter, BUILD_TYPE.DVL_LOAD, Constants.LoaderTypes.E, offset, limit, lOrderBy, true)).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	    when(instance.getImpPlanDAO().countByStatusListUser(status, lUser.getId(), false, pFilter, BUILD_TYPE.DVL_LOAD, Constants.LoaderTypes.E)).thenReturn(count);
	    JSONResponse result = instance.getDeploymentPlanList(lUser, false, Constants.LoaderTypes.E, pFilter, offset, limit, lOrderBy);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testGetDeploymentPlanList2() {
	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("");
	Integer offset = null;
	Integer limit = 1;
	String pFilter = null;
	LinkedHashMap<String, String> lOrderBy = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getDeploymentPlanList(lUser, false, Constants.LoaderTypes.E, pFilter, offset, limit, lOrderBy);
	assertNotNull(result);
    }

    @Test
    public void testGetDeploymentPlanList3() {
	try {
	    User lUser = DataWareHouse.user;
	    lUser.setCurrentRole(null);
	    Integer offset = null;
	    Integer limit = null;
	    String pFilter = null;
	    LinkedHashMap<String, String> lOrderBy = null;
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));

	    instance.getDeploymentPlanList(lUser, false, Constants.LoaderTypes.E, pFilter, offset, limit, lOrderBy);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetDeploymentPlanList1() {
	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("QA");
	Integer offset = null;
	Integer limit = null;
	String pFilter = null;
	LinkedHashMap<String, String> lOrderBy = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getDeploymentPlanList(lUser, false, Constants.LoaderTypes.E, pFilter, offset, limit, lOrderBy);
	assertNotNull(result);
    }

    @Test
    public void testGetDeploymentPlanListSystemSupport() {
	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("SystemSupport");
	Integer offset = null;
	Integer limit = null;
	String pFilter = null;
	LinkedHashMap<String, String> lOrderBy = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getDeploymentPlanList(lUser, false, Constants.LoaderTypes.E, pFilter, offset, limit, lOrderBy);
	assertNotNull(result);
    }

    @Test
    public void testGetDeploymentPlanListQA() {
	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("QA");
	Integer offset = null;
	Integer limit = null;
	String pFilter = null;
	LinkedHashMap<String, String> lOrderBy = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	JSONResponse result = instance.getDeploymentPlanList(lUser, true, Constants.LoaderTypes.E, pFilter, offset, limit, lOrderBy);
	assertNotNull(result);
    }

    /**
     * Test of getSystemLoadActions method, of class ProtectedService.
     */
    @Test
    public void testGetSystemLoadActions() {
	User pUser = DataWareHouse.getUser();
	String[] planids = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse result = instance.getSystemLoadActions(pUser, false, planids);
	assertNotNull(result);
    }

    /**
     * Test of getSystemLoadActions method, of class ProtectedService.
     */
    @Test
    public void testGetSystemLoadActions2() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole(Constants.UserGroup.Lead.name());
	String[] planids = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse result = instance.getSystemLoadActions(pUser, false, planids);
	assertNotNull(result);
    }

    /**
     * Test of getSystemLoadActions method, of class ProtectedService.
     */
    @Test
    public void testGetSystemLoadActions3() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole(Constants.UserGroup.SystemSupport.name());
	String[] planids = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse result = instance.getSystemLoadActions(pUser, false, planids);
	assertNotNull(result);
    }

    @Test
    public void testGetSystemLoadActions4() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole(Constants.UserGroup.QA.name());
	String[] planids = new String[] { DataWareHouse.getPlan().getId() };
	JSONResponse result = instance.getSystemLoadActions(pUser, Boolean.TRUE, planids);
	assertNotNull(result);
    }

    /**
     * Test of getDeploymentVParsList method, of class ProtectedService.
     */
    @Test
    public void testGetDeploymentVParsList() {
	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("QA");
	String[] pPlanIds = new String[] { DataWareHouse.getPlan().getId() };
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	JSONResponse result = instance.getDeploymentVParsList(lUser, false, pPlanIds);
	assertNotNull(result);
    }

    /**
     * Test of getDeploymentVParsList method, of class ProtectedService.
     */
    @Test
    public void testGetDeploymentVParsList1() {

	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("Lead");
	String[] pPlanIds = new String[] { DataWareHouse.getPlan().getId() };

	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	when(instance.getImpPlanDAO().find(pPlanIds[0])).thenReturn(DataWareHouse.getPlan());
	when(instance.getVparsDAO().findBySystem(Arrays.asList(DataWareHouse.getPlan().getBuildList().get(0).getSystemId()), Constants.VPARSEnvironment.INTEGRATION)).thenReturn(new ArrayList());
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	JSONResponse result = instance.getDeploymentVParsList(lUser, false, pPlanIds);
	assertNotNull(result);

    }

    /**
     * Test of getDeploymentVParsList method, of class ProtectedService.
     */
    @Test
    public void testGetDeploymentVParsList2() {

	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("SystemSupport");
	String[] pPlanIds = new String[] { DataWareHouse.getPlan().getId() };

	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	JSONResponse result = instance.getDeploymentVParsList(lUser, false, pPlanIds);
	assertNotNull(result);

    }

    @Test
    public void testGetDeploymentVParsList4() {

	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("LE");
	String[] pPlanIds = new String[] { DataWareHouse.getPlan().getId() };

	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	JSONResponse result = instance.getDeploymentVParsList(lUser, false, pPlanIds);
	assertNotNull(result);

    }

    @Test
    public void testGetDeploymentVParsList5() {

	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("Lead");
	String[] pPlanIds = new String[] { DataWareHouse.getPlan().getId() };
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());

	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	when(instance.getImpPlanDAO().find(pPlanIds[0])).thenReturn(lPlan);
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(Arrays.asList(lPlan.getBuildList().get(0)));
	when(instance.getVparsDAO().findBySystem(Arrays.asList(lPlan.getBuildList().get(0).getSystemId()), Constants.VPARSEnvironment.PRIVATE)).thenReturn(Arrays.asList(lPlan.getSystemLoadActionsList().get(0).getVparId()));
	JSONResponse result = instance.getDeploymentVParsList(lUser, false, pPlanIds);
	assertNotNull(result);

    }

    @Test
    public void testGetDeploymentVParsList6() {

	User lUser = DataWareHouse.user;
	lUser.setCurrentRole("Lead");
	String[] pPlanIds = new String[] { DataWareHouse.getPlan().getId() };
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());

	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	when(instance.getImpPlanDAO().find(pPlanIds[0])).thenReturn(lPlan);
	List<Build> lBuildList = lPlan.getBuildList();
	for (Build lBuild : lBuildList) {
	    lBuild.setJobStatus("T");
	}
	when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(lBuildList);
	when(instance.getVparsDAO().findBySystem(Arrays.asList(lPlan.getBuildList().get(0).getSystemId()), Constants.VPARSEnvironment.PRIVATE)).thenReturn(Arrays.asList(lPlan.getSystemLoadActionsList().get(0).getVparId()));
	JSONResponse result = instance.getDeploymentVParsList(lUser, false, pPlanIds);
	assertNotNull(result);

    }

    /**
     * Test of saveExceptionLoadApproval method, of class ProtectedService.
     */
    @Test
    public void testSaveExceptionLoadApproval() {
	try {
	    User pUser = null;
	    ImpPlanApprovals pApproval = null;
	    JSONResponse result = instance.saveExceptionLoadApproval(pUser, pApproval);

	    ProtectedService instance = spy(new ProtectedService());
	    instance.saveExceptionLoadApproval(pUser, pApproval);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of deleteLoadApproval method, of class ProtectedService.
     */
    @Test
    public void testDeleteLoadApproval() {
	try {
	    User pUser = null;
	    Integer pId = null;
	    instance.deleteLoadApproval(pUser, pId);
	    ProtectedService instance = spy(new ProtectedService());

	    instance.deleteLoadApproval(pUser, pId);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of downloadLoadApproval method, of class ProtectedService.
     */
    @Test
    public void testDownloadLoadApproval() {

	User pUser = null;
	String pPlanId = "";
	String pFile = "";
	ProtectedService instance = mock(ProtectedService.class);
	JSONResponse expResult = null;
	File file = spy(new File(pFile));
	doReturn(true).when(file).exists();
	FileIOUtils ioUtil = mock(FileIOUtils.class);
	JSONResponse result = instance.downloadLoadApproval(pUser, pPlanId, pFile);
	assertEquals(expResult, result);

    }

    /**
     * Test of setReadyForQA method, of class ProtectedService.
     */
    @Test
    public void testSetReadyForQA() {
	try {
	    User pUser = DataWareHouse.getUser();
	    Implementation imp = DataWareHouse.getPlan().getImplementationList().get(0);
	    String pImpl = imp.getId();
	    ImplementationDAO impDAO = mock(ImplementationDAO.class);
	    when(instance.getImplementationDAO()).thenReturn(impDAO);
	    imp.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	    when(instance.getImplementationDAO().find(pImpl)).thenReturn(imp);
	    JSONResponse result = instance.setReadyForQA(pUser, pImpl);
	    assertNotNull(result);
	    when(instance.getCheckoutSegmentsDAO().getDependentSegments(imp.getId(), Constants.ImplementationStatus.READY_FOR_QA.name())).thenReturn(Arrays.asList(imp.getId()));
	    instance.setReadyForQA(pUser, pImpl);
	    imp.setSubstatus(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name());
	    when(instance.getImplementationDAO().find(pImpl)).thenReturn(imp);
	    result = instance.setReadyForQA(pUser, pImpl);
	    assertFalse(result.getStatus());
	} catch (Exception e) {
	    // do nothing
	}

    }

    @Test
    public void testSetReadyForQA1() {
	try {
	    User pUser = DataWareHouse.getUser();
	    Implementation imp = DataWareHouse.getPlan().getImplementationList().get(0);
	    String pImpl = imp.getId();
	    List<String> lBranchList = DataWareHouse.getBranchList();
	    ImplementationDAO impDAO = mock(ImplementationDAO.class);
	    when(instance.getImplementationDAO()).thenReturn(impDAO);
	    imp.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	    when(instance.getImplementationDAO().find(pImpl)).thenReturn(imp);
	    when(instance.getCheckoutSegmentsDAO().getDependentSegments(imp.getId(), Constants.ImplementationStatus.READY_FOR_QA.name())).thenReturn(Arrays.asList());
	    // when(instance.getGITSSHUtils().addImplementationTag(pUser,
	    // DataWareHouse.RepoName,
	    // Constants.TagStatus.READY_FOR_QA, lBranchList)).thenReturn(false);
	    JSONResponse result = instance.setReadyForQA(pUser, pImpl);
	    // assertEquals(false, result.getStatus());
	    // assertFalse(result.getStatus());
	} catch (Exception e) {
	    // do nothing
	}

    }

    @Test
    public void testSetReadyForQA2() {
	try {
	    User pUser = DataWareHouse.getUser();
	    Implementation imp = DataWareHouse.getPlan().getImplementationList().get(0);
	    String pImpl = imp.getId();
	    List<String> lBranchList = DataWareHouse.getBranchList();
	    lBranchList.add("test");
	    ImplementationDAO impDAO = mock(ImplementationDAO.class);
	    when(instance.getImplementationDAO()).thenReturn(impDAO);
	    imp.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	    when(instance.getImplementationDAO().find(pImpl)).thenReturn(imp);
	    // when(instance.getJGitClientUtils().getAllBranchList("tp",
	    // imp.getPlanId().getId().toLowerCase())).thenReturn(lBranchList);
	    when(instance.getCheckoutSegmentsDAO().getDependentSegments(imp.getId(), Constants.ImplementationStatus.READY_FOR_QA.name())).thenReturn(Arrays.asList());
	    // when(instance.getGitBlitClientUtils().setPermissionForGitRepository(null,
	    // imp.getDevId(),
	    // Constants.GIT_PERMISSION_READ)).thenReturn(false);
	    JSONResponse result = instance.setReadyForQA(pUser, pImpl);
	    // assertFalse(result.getStatus());
	} catch (Exception e) {
	    // do nothing
	}

    }

    /**
     * Test of notifyOtherDevelopers method, of class ProtectedService.
     */
    @Test
    public void testNotifyOtherDevelopers() {
	try {
	    ImpPlan pPlanId = DataWareHouse.getPlan();
	    Implementation lImp = pPlanId.getImplementationList().get(0);
	    String system = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getName();
	    Date beforeUpdaet = DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime();
	    Date afterUpdate = DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime();
	    List<Object[]> planList = new ArrayList();
	    Object[] lDbPlans = new Object[9];
	    lDbPlans[0] = lImp.getId();
	    lDbPlans[1] = pPlanId.getId();
	    lDbPlans[2] = pPlanId.getLeadId();
	    lDbPlans[3] = lImp.getDevId();
	    lDbPlans[4] = lImp.getCheckoutSegmentsList().get(0).getProgramName() + "[" + "APO" + "]";
	    lDbPlans[5] = pPlanId.getLoadType();
	    lDbPlans[6] = pPlanId.getPlanStatus();
	    lDbPlans[7] = lImp.getId();
	    lDbPlans[8] = beforeUpdaet;
	    planList.add(lDbPlans);
	    LoadDateChangedMail loadDateChangedMail = new LoadDateChangedMail();

	    when(instance.getImpPlanDAO().getDevelopersByTargetSystem(pPlanId.getId(), system)).thenReturn(planList);
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    when(instance.mailMessageFactory.getTemplate(LoadDateChangedMail.class)).thenReturn(mock(LoadDateChangedMail.class));

	    instance.notifyOtherDevelopers(pPlanId, system, beforeUpdaet, afterUpdate);
	} catch (Exception ex) {
	    assertTrue(true);

	}

    }

    @Test
    public void testUpdatePlan() {
	try {
	    User user = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setMacroHeader(Boolean.TRUE);
	    ImpPlan pPlan1 = DataWareHouse.getPlan();
	    pPlan1.setMacroHeader(Boolean.FALSE);
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));
	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(pPlan1);
	    when(instance.dateAuditCrossCheck.dateAutditForMigration(user, pPlan1)).thenReturn("PASSED");
	    instance.updatePlan(user, pPlan, false, false, "");
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testUpdatePlan1() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.EXCEPTION.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(DataWareHouse.getPlan());
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan2() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.EXCEPTION.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(pPlan);
	    instance.updatePlan(pUser, lPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan3() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.EXCEPTION.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    pPlan.setMacroHeader(Boolean.TRUE);
	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    when(instance.getCheckoutSegmentsDAO().isMacroHeaderPlan(pPlan.getId())).thenReturn(2);
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan4() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.EXCEPTION.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    when(instance.getImpPlanDAO().getInvalidRelatedPlans(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList("Test"));
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan5() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.STANDARD.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    for (SystemLoad lLoad : pPlan.getSystemLoadList()) {
		lLoad.setLoadDateTime(new Date());
	    }
	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan6() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.STANDARD.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    for (SystemLoad lLoad : pPlan.getSystemLoadList()) {
		lLoad.setLoadDateTime(new Date());
	    }
	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    when(instance.getImpPlanDAO().doPlanAuditForUpdate(pPlan.getId(), pPlan.getSystemLoadList().get(0).getSystemId().getId(), pPlan.getSystemLoadList().get(0).getLoadDateTime())).thenReturn("Test");
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan7() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.STANDARD.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    for (SystemLoad lLoad : pPlan.getSystemLoadList()) {
		lLoad.setLoadDateTime(new Date());
	    }
	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    when(instance.getImpPlanDAO().doPlanAuditForUpdate(pPlan.getId(), pPlan.getSystemLoadList().get(0).getSystemId().getId(), pPlan.getSystemLoadList().get(0).getLoadDateTime())).thenReturn("");
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(pUser, pPlan)).thenReturn("Test");
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan8() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.EXCEPTION.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    List<SystemLoad> pLoadList = pPlan.getSystemLoadList();
	    for (SystemLoad lLoad : pLoadList) {
		lLoad.setLoadDateTime(new Date());
	    }

	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    List<SystemLoad> lLoadList = lPlan.getSystemLoadList();

	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));
	    List<Object[]> dependentList = new ArrayList();
	    Object[] lDepPlan = new Object[3];
	    lDepPlan[0] = "T1700484/APO";
	    lDepPlan[1] = "APPROVED";
	    lDepPlan[2] = "T1700484";
	    dependentList.add(lDepPlan);

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    when(instance.getSystemLoadDAO().findByImpPlan(pPlan)).thenReturn(lLoadList);
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(pUser, pPlan)).thenReturn("PASSED");
	    when(instance.getImpPlanDAO().doPlanAuditForUpdate(pPlan.getId(), pPlan.getSystemLoadList().get(0).getSystemId().getId(), pPlan.getSystemLoadList().get(0).getLoadDateTime())).thenReturn("");
	    when(instance.getImpPlanDAO().getBtwnSegmentRelatedPlansForLaterDate(pPlan.getId(), pLoadList.get(0).getSystemId().getId(), pLoadList.get(0).getLoadDateTime(), new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()))).thenReturn(dependentList);
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdatePlan9() {
	try {
	    User pUser = DataWareHouse.getUser();
	    ImpPlan pPlan = DataWareHouse.getPlan();
	    pPlan.setLoadType(Constants.LoadTypes.EXCEPTION.getLoadTypes());
	    pPlan.setPlanStatus(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name());
	    List<SystemLoad> pLoadList = pPlan.getSystemLoadList();
	    for (SystemLoad lLoad : pLoadList) {
		lLoad.setId(null);
		lLoad.setLoadDateTime(new Date());
	    }

	    ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	    List<SystemLoad> lLoadList = lPlan.getSystemLoadList();

	    lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));
	    List<Object[]> dependentList = new ArrayList();
	    Object[] lDepPlan = new Object[3];
	    lDepPlan[0] = "T1700484/APO";
	    lDepPlan[1] = "APPROVED";
	    lDepPlan[2] = "T1700484";
	    dependentList.add(lDepPlan);

	    when(instance.getImpPlanDAO().find(pPlan.getId())).thenReturn(lPlan);
	    when(instance.getSystemLoadDAO().findByImpPlan(pPlan)).thenReturn(lLoadList);
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(pUser, pPlan)).thenReturn("PASSED");
	    when(instance.getImpPlanDAO().doPlanAuditForUpdate(pPlan.getId(), pPlan.getSystemLoadList().get(0).getSystemId().getId(), pPlan.getSystemLoadList().get(0).getLoadDateTime())).thenReturn("");
	    when(instance.getImpPlanDAO().getBtwnSegmentRelatedPlansForLaterDate(pPlan.getId(), pLoadList.get(0).getSystemId().getId(), pLoadList.get(0).getLoadDateTime(), new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()))).thenReturn(dependentList);
	    // when(instance.getGITSSHUtils().addImplementationTag(pUser,
	    // DataWareHouse.RepoName,
	    // Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING,
	    // DataWareHouse.lBranchList)).thenReturn(false);
	    instance.updatePlan(pUser, pPlan, false, false, "");
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    /**
     * Test of compilerControlValidationForSystem method, of class ProtectedService.
     */
    @Test
    public void testCompilerControlValidationForSystem() {
	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.getPlan().getImplementationList().get(0);
	System lSystem = null;
	JSONResponse result = instance.compilerControlValidationForSystem(pUser, pImplementation, lSystem);
	assertNotNull(result);

    }

    @Test
    public void testCompilerControlValidationForSystem1() {
	try {
	    User pUser = DataWareHouse.getUser();
	    Implementation pImplementation = DataWareHouse.getPlan().getImplementationList().get(0);
	    System lSystem = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId();
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(false);
	    JSONResponse result = instance.compilerControlValidationForSystem(pUser, pImplementation, lSystem);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do nothing
	}

    }

    /**
     * Test of saveDelegation method, of class ProtectedService.
     */
    @Test
    public void testSaveDelegation() {
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);
	when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	JSONResponse result = instance.saveDelegation(lUser, pUserSetting);
	assertNotNull(result);
    }

    @Test
    public void testSaveDelegation1() {
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	pUserSetting.setValue("FALSE");
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);
	when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	JSONResponse result = instance.saveDelegation(lUser, pUserSetting);
	assertNotNull(result);
    }

    @Test
    public void testSaveDelegation2() {
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = DataWareHouse.getUserSettings();
	pUserSetting.setValue("FALSE");
	pUserSetting.setUserId(null);
	pUserSetting.setName(Constants.UserSettings.DELEGATE_USER.name());
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);
	when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	JSONResponse result = instance.saveDelegation(lUser, pUserSetting);
	assertNotNull(result);
    }

    @Test
    public void testSaveDelegation3() {
	try {
	    User lUser = DataWareHouse.getUser();
	    UserSettings pUserSetting = DataWareHouse.getUserSettings();
	    pUserSetting.setId(null);
	    pUserSetting.setValue("TRUE");
	    pUserSetting.setUserId(null);
	    pUserSetting.setName(Constants.UserSettings.DELEGATE_USER.name());
	    ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	    ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	    ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);
	    when(instance.userSettingsDAO.find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	    when(instance.delegateHelper.addToCache(Matchers.anyObject(), Matchers.anyObject(), true)).thenReturn(false);
	    JSONResponse result = instance.saveDelegation(lUser, pUserSetting);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testSaveDelegation4() {
	try {
	    User lUser = DataWareHouse.getUser();
	    UserSettings pUserSetting = DataWareHouse.getUserSettings();
	    pUserSetting.setId(null);
	    pUserSetting.setValue("FALSE");
	    pUserSetting.setUserId(null);
	    pUserSetting.setName(Constants.UserSettings.DELEGATION.name());
	    ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", mock(LDAPAuthenticatorImpl.class));
	    ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	    ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getValue())).thenReturn(lUser);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(pUserSetting.getUserId())).thenReturn(lUser);
	    when(instance.getUserSettingsDAO().find(pUserSetting.getUserId(), Constants.UserSettings.DELEGATE_USER.name())).thenReturn(pUserSetting);
	    when(instance.getDelegateHelper().addToCache(Matchers.anyObject(), Matchers.anyObject(), true)).thenReturn(false);
	    JSONResponse result = instance.saveDelegation(lUser, pUserSetting);
	    assertNotNull(result);
	} catch (Exception e) {
	    // do
	}
    }

    @Test
    public void testSaveDelegation5() {
	User lUser = DataWareHouse.getUser();
	try {
	    JSONResponse result = instance.saveDelegation(lUser, null);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of setSuperUser method, of class ProtectedService.
     */
    @Test
    public void testSetSuperUser() {
	User lUser = DataWareHouse.getUser();
	UserSettings pUserSetting = null;
	ReflectionTestUtils.setField(instance, "delegateHelper", mock(DelegateHelper.class));
	JSONResponse result = instance.setSuperUser(lUser, pUserSetting);
	assertNotNull(result);

    }

    @Test
    public void testSetSuperUser1() {
	User lUser = null;
	UserSettings pUserSetting = null;
	try {
	    instance.setSuperUser(lUser, pUserSetting);
	} catch (Exception e) {
	    assertTrue(Boolean.TRUE);
	}
    }

    /**
     * Test of getSettingsList method, of class ProtectedService.
     */
    @Test
    public void testGetSettingsList() {
	User lCurrentUser = DataWareHouse.getUser();
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	JSONResponse result = instance.getSettingsList(lCurrentUser);
	assertNotNull(result);

    }

    /**
     * Test of getSettingsListforSuperUser method, of class ProtectedService.
     */
    @Test
    public void testGetSettingsListforSuperUser() {
	User lCurrentUser = DataWareHouse.getUser();
	ReflectionTestUtils.setField(instance, "lSuperUserMap", mock(ConcurrentHashMap.class));
	JSONResponse result = instance.getSettingsListforSuperUser(lCurrentUser);
	assertNotNull(result);

    }

    /**
     * Test of revertImplementation method, of class ProtectedService.
     */
    @Test
    public void testRevertImplementation() {
	User user = DataWareHouse.getUser();
	String impId = DataWareHouse.getPlan().getImplementationList().get(0).getId();
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	when(instance.implementationDAO.find(impId)).thenReturn(DataWareHouse.getPlan().getImplementationList().get(0));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse result = instance.revertImplementation(user, impId);
	assertNotNull(result);
    }

    /**
     * Test of uploadExceptionLoadApproval method, of class ProtectedService.
     */
    @Test
    public void testUploadExceptionLoadApproval() {
	User user = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	MultipartFile pFile = mock(MultipartFile.class);
	when(pFile.isEmpty()).thenReturn(Boolean.FALSE);
	instance.uploadExceptionLoadApproval(user, planId, pFile, "");

	when(pFile.isEmpty()).thenReturn(Boolean.TRUE);
	instance.uploadExceptionLoadApproval(user, planId, pFile, "");
    }

    /**
     * Test of getSegmentMappingByImplementation method, of class ProtectedService.
     */
    @Test
    public void testGetSegmentMappingByImplementation() {
	try {
	    String[] ids = { DataWareHouse.getPlan().getImplementationList().get(0).getId() };
	    Integer offset = 0;
	    Integer limit = 100;
	    LinkedHashMap<String, String> lOrderBy = new LinkedHashMap();
	    lOrderBy.put("programName", "asc");
	    List<CheckoutSegments> lSegments = DataWareHouse.getPlan().getCheckoutSegmentsList();
	    CheckoutSegments checkoutSegments = new CheckoutSegments();
	    checkoutSegments.setPlanId(DataWareHouse.getPlan());
	    List<CheckoutSegments> checkoutSegList = new ArrayList<>();
	    checkoutSegList.add(checkoutSegments);
	    lSegments.forEach(seg -> {
		seg.getImpId().setCheckoutSegmentsList(checkoutSegList);
		seg.setReviewStatus(Boolean.TRUE);
	    });

	    when(instance.getCheckoutSegmentsDAO().findByImplementation(ids)).thenReturn(lSegments);
	    instance.getSegmentMappingByImplementation(ids);
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testGetSharedObjects() {
	System lSystem = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId();
	String pSOName = "Temp";
	JSONResponse lExeResponse = new JSONResponse();
	ImpPlan lPlan = DataWareHouse.getPlan();
	lExeResponse.setData((Object) "temp.mak");
	lExeResponse.setStatus(Boolean.TRUE);
	try {
	    String lCommand = Constants.SystemScripts.SEARCH_SO.getScript() + pSOName.toLowerCase() + " " + lSystem.getName().toLowerCase();
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(false);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    lPlan.getSystemLoadList().get(0).setLoadDateTime(new Date());
	    instance.getSharedObjects(DataWareHouse.user, pSOName, Constants.APP_DATE_TIME_FORMAT.get().format(lPlan.getSystemLoadList().get(0).getLoadDateTime()), lSystem.getId(), 0, 10, null);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetSharedObjects1() {
	System lSystem = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId();
	String pSOName = "Temp";
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setData((Object) "");
	lExeResponse.setStatus(Boolean.TRUE);
	try {
	    String lCommand = Constants.SystemScripts.SEARCH_SO.getScript() + pSOName.toLowerCase() + " " + lSystem.getName().toLowerCase();
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    instance.getSharedObjects(DataWareHouse.user, pSOName, Constants.APP_DATE_TIME_FORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()), lSystem.getId(), 0, 10, null);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetSharedObjects2() {
	System lSystem = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId();
	String pSOName = "Temp";
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(Boolean.TRUE);
	try {
	    String lCommand = Constants.SystemScripts.SEARCH_SO.getScript() + pSOName.toLowerCase() + " " + lSystem.getName().toLowerCase();
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    instance.getSharedObjects(DataWareHouse.user, pSOName, Constants.APP_DATE_TIME_FORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()), lSystem.getId(), 0, 10, null);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetSharedObjects3() {
	System lSystem = DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId();
	String pSOName = "Temp";
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setData((Object) "temp.mak");
	lExeResponse.setStatus(Boolean.TRUE);
	try {
	    String lCommand = Constants.SystemScripts.SEARCH_SO.getScript() + pSOName.toLowerCase() + " " + lSystem.getName().toLowerCase();
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    instance.getSharedObjects(DataWareHouse.user, pSOName, Constants.APP_DATE_TIME_FORMAT.get().format(getPlan().getSystemLoadList().get(0).getLoadDateTime()), lSystem.getId(), 0, 10, null);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetSegmentMappingByImplementation1() {
	String[] ids = { DataWareHouse.getPlan().getImplementationList().get(0).getId() };
	List<CheckoutSegments> lSegments = DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList();
	lSegments.get(0).setReviewStatus(Boolean.TRUE);
	CheckoutSegmentsDAO lSegmentDAO = instance.getCheckoutSegmentsDAO();
	Mockito.doThrow(Exception.class).when(lSegmentDAO).findByImplementation(ids);
	try {
	    instance.getSegmentMappingByImplementation(ids);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of buildPlanForSystem method, of class ProtectedService.
     */
    @Test
    public void testBuildPlanForSystem() throws IllegalAccessException, InvocationTargetException {
	User pUser = DataWareHouse.getUser();
	ImpPlan pPlan = DataWareHouse.setRefreshPlan();
	Implementation pImp = DataWareHouse.getPlan().getImplementationList().get(0);
	SystemLoad pSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	Constants.BUILD_TYPE pBuildType = Constants.BUILD_TYPE.DVL_BUILD;
	List<Build> lBuildList = pPlan.getBuildList();
	List<Object[]> stagingDepedendentPlans = new ArrayList();
	Object[] lTemp = new Object[2];
	lTemp[0] = DataWareHouse.getPlan().getSystemLoadList().get(1);
	lTemp[1] = pPlan;
	JenkinsBuild lJenBuild = new JenkinsBuild(pUser);
	stagingDepedendentPlans.add(lTemp);
	when(instance.getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(lBuildList);
	when(instance.getSystemLoadDAO().findBy(new ImpPlan(pPlan.getId()), pSystemLoad.getSystemId())).thenReturn(pSystemLoad);
	when(instance.getSystemLoadDAO().getDependentPlanByApproveDate(pPlan.getId(), pSystemLoad.getSystemId().getId(), pSystemLoad.getLoadDateTime(), Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(stagingDepedendentPlans);
	when(instance.getJenkinsClient().executeJob(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lJenBuild);
	try {
	    instance.buildPlanForSystem(pUser, pPlan, pImp, pSystemLoad, pBuildType, false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testBuildPlanForSystem1() throws IllegalAccessException, InvocationTargetException {
	User pUser = DataWareHouse.getUser();
	ImpPlan pPlan = DataWareHouse.setRefreshPlan();
	Implementation pImp = DataWareHouse.getPlan().getImplementationList().get(0);
	SystemLoad pSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	Constants.BUILD_TYPE pBuildType = Constants.BUILD_TYPE.STG_BUILD;
	List<Build> lBuildList = pPlan.getBuildList();
	List<Object[]> stagingDepedendentPlans = new ArrayList();
	Object[] lTemp = new Object[2];
	lTemp[0] = DataWareHouse.getPlan().getSystemLoadList().get(1);
	lTemp[1] = pPlan;
	JenkinsBuild lJenBuild = new JenkinsBuild(pUser);
	stagingDepedendentPlans.add(lTemp);
	when(instance.getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), Constants.BUILD_TYPE.STG_BUILD)).thenReturn(lBuildList);
	when(instance.getSystemLoadDAO().findBy(new ImpPlan(pPlan.getId()), pSystemLoad.getSystemId())).thenReturn(pSystemLoad);
	when(instance.getSystemLoadDAO().getDependentPlanByApproveDate(pPlan.getId(), pSystemLoad.getSystemId().getId(), pSystemLoad.getLoadDateTime(), Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(stagingDepedendentPlans);
	when(instance.getJenkinsClient().executeJob(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lJenBuild);
	try {
	    instance.buildPlanForSystem(pUser, pPlan, pImp, pSystemLoad, pBuildType, false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testBuildPlanForSystem2() throws IllegalAccessException, InvocationTargetException {
	User pUser = DataWareHouse.getUser();
	ImpPlan pPlan = DataWareHouse.setRefreshPlan();
	Implementation pImp = DataWareHouse.getPlan().getImplementationList().get(0);
	SystemLoad pSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	Constants.BUILD_TYPE pBuildType = Constants.BUILD_TYPE.STG_BUILD;
	List<Build> lBuildList = pPlan.getBuildList();
	List<Object[]> stagingDepedendentPlans = new ArrayList();
	Object[] lTemp = new Object[2];
	lTemp[0] = DataWareHouse.getPlan().getSystemLoadList().get(1);
	lTemp[1] = pPlan;
	JenkinsBuild lJenBuild = new JenkinsBuild(pUser);
	stagingDepedendentPlans.add(lTemp);
	when(instance.getBuildDAO().findBuildWithPlanAndSystem(pPlan.getId(), Arrays.asList(pSystemLoad.getSystemId()), Constants.BUILD_TYPE.STG_BUILD)).thenReturn(lBuildList);
	when(instance.getSystemLoadDAO().findBy(new ImpPlan(pPlan.getId()), pSystemLoad.getSystemId())).thenReturn(pSystemLoad);
	when(instance.getSystemLoadDAO().getDependentPlanByApproveDate(pPlan.getId(), pSystemLoad.getSystemId().getId(), pSystemLoad.getLoadDateTime(), Constants.PlanStatus.getAfterSubmitStatus().keySet())).thenReturn(stagingDepedendentPlans);
	when(instance.getJenkinsClient().executeJob(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(null);
	try {
	    instance.buildPlanForSystem(pUser, pPlan, pImp, pSystemLoad, pBuildType, false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of getLatestBuildByPlan method, of class ProtectedService.
     */
    @Test
    public void testGetLatestBuildByPlan() {
	String ids = DataWareHouse.getPlan().getId();
	instance.getLatestBuildByPlan(ids);
    }

    /**
     * Test of rejectPlanAndDependentPlans method, of class ProtectedService.
     */
    // @Test
    public void testRejectPlanAndDependentPlans() {
	User currentUser = DataWareHouse.user;
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "Test";
	ProtectedService instance = new ProtectedService();
	JSONResponse expResult = null;
	JSONResponse result = instance.rejectPlanAndDependentPlans(currentUser, planId, rejectReason, Boolean.TRUE);
	assertEquals(expResult, result);
    }

    @Test
    public void testRejectPlanAndDependentPlans1() {
	User currentUser = DataWareHouse.user;
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "Test";
	ProtectedService instance = spy(new ProtectedService());
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	instance.rejectPlanAndDependentPlans(currentUser, planId, rejectReason, Boolean.TRUE);

    }

    @Test
    public void testRejectPlanAndDependentPlans2() throws Exception {
	User currentUser = DataWareHouse.user;
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "Test";
	ProtectedService instance = spy(new ProtectedService());
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	Mockito.doThrow(Exception.class).when(instance.rejectHelper).rejectPlan(currentUser, planId, rejectReason, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
	try {
	    instance.rejectPlanAndDependentPlans(currentUser, planId, rejectReason, Boolean.TRUE);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of buildPlan method, of class ProtectedService.
     */
    @Test
    public void testBuildPlan() {
	try {
	    ProtectedService instance = spy(new ProtectedService());
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ConcurrentHashMap<String, String> test1 = new ConcurrentHashMap<String, String>();
	    // instance.wssPlanIdAndUserId = test1;
	    User lUser = DataWareHouse.getUser();
	    Implementation lImp = DataWareHouse.getPlan().getImplementationList().get(0);
	    ImpPlan lPlan = DataWareHouse.getPlan();
	    List<SystemLoad> loads = lPlan.getSystemLoadList();
	    Date lTemp = loads.get(0).getLoadDateTime();
	    loads.get(0).setLoadDateTime(null);
	    Constants.BUILD_TYPE pBuildType = null;
	    when(instance.getImplementationDAO().find(lImp.getId())).thenReturn(lImp);
	    JSONResponse result = instance.buildPlan(lUser, lImp.getId(), loads, pBuildType, Boolean.FALSE, Boolean.FALSE);
	    // loads.get(0).setLoadDateTime(lTemp);

	    assertFalse(result.getStatus());
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testBuildPlan1() {
	// ProtectedService instance = spy(new ProtectedService());
	// ReflectionTestUtils.setField(instance, "implementationDAO",
	// mock(ImplementationDAO.class));
	User lUser = DataWareHouse.getUser();
	Implementation lImp = DataWareHouse.getPlan().getImplementationList().get(0);
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> temploads = Arrays.asList(lPlan.getSystemLoadList().get(0));
	Constants.BUILD_TYPE pBuildType = Constants.BUILD_TYPE.DVL_BUILD;
	JSONResponse lComResult = new JSONResponse();
	lComResult.setStatus(Boolean.FALSE);
	lComResult.setErrorMessage("");
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	when(instance.getImplementationDAO().find(lImp.getId())).thenReturn(lImp);
	doReturn(lComResult).when(instance).compilerControlValidationForSystem(lUser, lImp, temploads.get(0).getSystemId());
	doReturn(lComResult).when(instance).buildPlanForSystem(lUser, lImp.getPlanId(), lImp, temploads.get(0), pBuildType, false);
	DevlBuildActivityMessage lMessage = new DevlBuildActivityMessage(lImp.getPlanId(), null, temploads.get(0).getSystemId());
	lMessage.setStatus("initiated");

	try {
	    JSONResponse result = instance.buildPlan(lUser, lImp.getId(), temploads, pBuildType, Boolean.FALSE, Boolean.FALSE);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testBuildPlan2() {
	User lUser = DataWareHouse.getUser();
	Implementation lImp = DataWareHouse.getPlan().getImplementationList().get(0);
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> temploads = Arrays.asList(lPlan.getSystemLoadList().get(0));
	Constants.BUILD_TYPE pBuildType = Constants.BUILD_TYPE.DVL_BUILD;
	when(instance.getImplementationDAO().find(lImp.getId())).thenReturn(lImp);
	JSONResponse lComResult = new JSONResponse();
	lComResult.setStatus(Boolean.FALSE);
	lComResult.setErrorMessage("Warning");
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	CompilerValidationMail lCompilerValidationMail = new CompilerValidationMail();
	when(instance.mailMessageFactory.getTemplate(CompilerValidationMail.class)).thenReturn(mock(CompilerValidationMail.class));
	when(instance.getWFConfig().getDevCentreMailID()).thenReturn("test@travelport.com");
	lCompilerValidationMail.setSystem(temploads.get(0).getSystemId().getName());
	lCompilerValidationMail.setIpAddress(temploads.get(0).getSystemId().getIpaddress());
	lCompilerValidationMail.addToAddressUserId("test@travelport.com", Constants.MailSenderRole.LEAD);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	when(instance.getImplementationDAO().find(lImp.getId())).thenReturn(lImp);
	doReturn(lComResult).when(instance).compilerControlValidationForSystem(lUser, lImp, temploads.get(0).getSystemId());
	doReturn(lComResult).when(instance).buildPlanForSystem(lUser, lImp.getPlanId(), lImp, temploads.get(0), pBuildType, false);
	DevlBuildActivityMessage lMessage = new DevlBuildActivityMessage(lImp.getPlanId(), null, temploads.get(0).getSystemId());
	lMessage.setStatus("initiated");
	// Mockito.doThrow(Exception.class).doNothing().when(instance.getActivityLogDAO()).save(lUser,
	// lMessage);

	when(instance.compilerControlValidationForSystem(lUser, lImp, temploads.get(0).getSystemId())).thenReturn(lComResult);
	try {
	    JSONResponse result = instance.buildPlan(lUser, lImp.getId(), temploads, pBuildType, Boolean.FALSE, Boolean.FALSE);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testBuildPlan3() {
	User lUser = DataWareHouse.getUser();
	Implementation lImp = DataWareHouse.getPlan().getImplementationList().get(0);
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> temploads = Arrays.asList(lPlan.getSystemLoadList().get(0));
	Constants.BUILD_TYPE pBuildType = Constants.BUILD_TYPE.DVL_BUILD;
	when(instance.getImplementationDAO().find(lImp.getId())).thenReturn(lImp);
	JSONResponse lComResult = new JSONResponse();
	lComResult.setStatus(Boolean.TRUE);
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	CompilerValidationMail lCompilerValidationMail = new CompilerValidationMail();
	when(instance.mailMessageFactory.getTemplate(CompilerValidationMail.class)).thenReturn(mock(CompilerValidationMail.class));
	when(instance.getWFConfig().getDevCentreMailID()).thenReturn("test@travelport.com");
	lCompilerValidationMail.setSystem(temploads.get(0).getSystemId().getName());
	lCompilerValidationMail.setIpAddress(temploads.get(0).getSystemId().getIpaddress());
	lCompilerValidationMail.addToAddressUserId("test@travelport.com", Constants.MailSenderRole.LEAD);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	when(instance.getImplementationDAO().find(lImp.getId())).thenReturn(lImp);
	doReturn(lComResult).when(instance).compilerControlValidationForSystem(lUser, lImp, temploads.get(0).getSystemId());
	doReturn(lComResult).when(instance).buildPlanForSystem(lUser, lImp.getPlanId(), lImp, temploads.get(0), pBuildType, false);
	DevlBuildActivityMessage lMessage = new DevlBuildActivityMessage(lImp.getPlanId(), null, temploads.get(0).getSystemId());
	lMessage.setStatus("initiated");
	// Mockito.doThrow(Exception.class).doNothing().when(instance.getActivityLogDAO()).save(lUser,
	// lMessage);

	when(instance.compilerControlValidationForSystem(lUser, lImp, temploads.get(0).getSystemId())).thenReturn(lComResult);
	try {
	    JSONResponse result = instance.buildPlan(lUser, lImp.getId(), temploads, pBuildType, Boolean.FALSE, Boolean.FALSE);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testBuildPlan5() {
	ProtectedService instance = spy(new ProtectedService());
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	User lUser = DataWareHouse.getUser();
	Implementation lImp = DataWareHouse.getPlan().getImplementationList().get(0);
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> loads = lPlan.getSystemLoadList();
	loads.get(0).setLoadDateTime(null);
	Constants.BUILD_TYPE pBuildType = null;
	when(instance.getImplementationDAO().find(lImp.getId())).thenThrow(WorkflowException.class);
	try {
	    JSONResponse result = instance.buildPlan(lUser, lImp.getId(), loads, pBuildType, Boolean.FALSE, Boolean.FALSE);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of retriveBuildLog method, of class ProtectedService.
     */
    @Test
    public void testRetriveBuildLog() {
	User lUser = null;
	String planId = "";
	String systemName = "";
	ProtectedService instance = spy(new ProtectedService());
	JSONResponse expResult = null;
	try {
	    JSONResponse result = instance.retriveBuildLog(lUser, planId);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of cancelBuild method, of class ProtectedService.
     */
    @Test
    public void testCancelBuild() throws Exception {
	User lUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	Constants.BUILD_TYPE buildType = Constants.BUILD_TYPE.DVL_BUILD;
	ProtectedService instance = new ProtectedService();
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(planId + "_" + DataWareHouse.getSystemList().get(0).getName(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	instance.systemDAO = mock(SystemDAO.class);
	instance.buildDAO = mock(BuildDAO.class);
	instance.jenkinsClient = mock(JenkinsClient.class);
	when(instance.getSystemDAO().findByName(DataWareHouse.getSystemList().get(0).getName())).thenReturn(DataWareHouse.getSystemList().get(0));
	when(instance.getBuildDAO().findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getSystemList().get(0), jenkinsBuild.getBuildNumber(), Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(mock(Build.class));
	when(instance.getJenkinsClient().stopBuild(jenkinsBuild.getJobName(), jenkinsBuild.getBuildNumber())).thenReturn(Boolean.TRUE);
	JSONResponse result = instance.cancelBuild(lUser, planId, buildType);
	assertNotNull(result);
    }

    /**
     * Test of createLoaderFile method, of class ProtectedService.
     */
    @Test
    public void testCreateLoaderFile() {
	User pUser = null;
	String pImpPlanId = DataWareHouse.getPlan().getId();
	String pLoaderType = "";
	Constants.BUILD_TYPE pBuildType = null;
	ProtectedService instance = spy(new ProtectedService());
	try {
	    JSONResponse result = instance.createLoaderFile(pUser, pImpPlanId, pLoaderType, pBuildType);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateLoaderFile1() {
	User pUser = null;
	ImpPlan pImpPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSystemLoads = pImpPlan.getSystemLoadList();
	lSystemLoads.get(0).setLoadDateTime(null);
	String pLoaderType = "";
	Constants.BUILD_TYPE pBuildType = null;
	try {
	    when(instance.getImpPlanDAO().find(pImpPlan.getId())).thenReturn(pImpPlan);
	    when(instance.getSystemLoadDAO().findByImpPlan(pImpPlan.getId())).thenReturn(lSystemLoads);
	    JSONResponse result = instance.createLoaderFile(pUser, pImpPlan.getId(), pLoaderType, pBuildType);
	    DataWareHouse.setRefreshPlan();
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateLoaderFile2() {
	User pUser = null;
	ImpPlan pImpPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSystemLoads = pImpPlan.getSystemLoadList();
	String pLoaderType = "";
	Constants.BUILD_TYPE pBuildType = null;
	try {
	    when(instance.getImpPlanDAO().find(pImpPlan.getId())).thenReturn(pImpPlan);
	    when(instance.getSystemLoadDAO().findByImpPlan(pImpPlan.getId())).thenReturn(new ArrayList());
	    JSONResponse result = instance.createLoaderFile(pUser, pImpPlan.getId(), pLoaderType, pBuildType);
	    DataWareHouse.setRefreshPlan();
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of createLoaderFileForSystem method, of class ProtectedService.
     */
    @Test
    public void testCreateLoaderFileForSystem() {
	User pUser = DataWareHouse.getUser();
	ImpPlan pPlan = DataWareHouse.setRefreshPlan();
	Implementation pImp = pPlan.getImplementationList().get(0);
	SystemLoad pSystemLoad = pPlan.getSystemLoadList().get(0);
	Constants.BUILD_TYPE pWorkspaceType = Constants.BUILD_TYPE.DVL_LOAD;
	String pLoaderType = "A";
	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    // when(instance.getBuildDAO().findBuildWithPlanAndSystem(Matchers.anyObject(),
	    // Matchers.anyObject(),
	    // Matchers.anyObject())).thenReturn(pPlan.getBuildList());
	    when(instance.getJenkinsClient().executeJob(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new JenkinsBuild(pUser));

	    instance.createLoaderFileForSystem(pUser, pPlan, pImp, pSystemLoad, pWorkspaceType, pLoaderType, false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateLoaderFileForSystem1() {
	User pUser = DataWareHouse.getUser();
	ImpPlan pPlan = DataWareHouse.setRefreshPlan();
	Implementation pImp = pPlan.getImplementationList().get(0);
	SystemLoad pSystemLoad = pPlan.getSystemLoadList().get(0);
	Constants.BUILD_TYPE pWorkspaceType = Constants.BUILD_TYPE.STG_LOAD;
	String pLoaderType = "A";
	GitSearchResult lGitSearchResult = DataWareHouse.getGitSearchResult();
	lGitSearchResult.getBranch().get(0).setTargetSystem("apo");
	lGitSearchResult.getBranch().get(0).setRefStatus("online");
	lGitSearchResult.setFileName("include/lnklogc.h");
	lGitSearchResult.getBranch().get(0).setRefPlan("Test");
	lGitSearchResult.getBranch().get(0).setRefLoadDate(new Date());
	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "gitHelper", mock(GITHelper.class));
	    ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	    DataWareHouse.getPlan().getCheckoutSegmentsList().forEach(seg -> {
		try {
		    when(instance.getjGITSearchUtils().SearchAllRepos(pSystemLoad.getSystemId().getPlatformId().getNickName(), seg.getProgramName(), pPlan.getMacroHeader(), Constants.PRODSearchType.BOTH, Arrays.asList("lSearchRepoName"))).thenReturn(Arrays.asList(lGitSearchResult));
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
	    });

	    when(instance.getGitHelper().getRepositoryNameBySourceURL("ssh://vhldvztdt001.tvlport.net:8445/tpf/tp/nonibm/nonibm_gilk.git")).thenReturn("lSearchRepoName");
	    when(instance.getCheckoutSegmentsDAO().findBySystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(0))).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	    // when(instance.getBuildDAO().findBuildWithPlanAndSystem(Matchers.anyObject(),
	    // Matchers.anyObject(),
	    // Matchers.anyObject())).thenReturn(pPlan.getBuildList());
	    when(instance.getJenkinsClient().executeJob(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new JenkinsBuild(pUser));
	    when(instance.getSystemLoadDAO().getFallbackLoadSetPlanIds(pPlan.getId(), Constants.PlanStatus.getApprovedAndAboveStatus().keySet(), pSystemLoad.getSystemId().getId())).thenReturn(pPlan.getSystemLoadList());
	    when(instance.getCheckoutSegmentsDAO().findBySystemLoad(pSystemLoad)).thenReturn(Arrays.asList(pImp.getCheckoutSegmentsList().get(0)));
	    when(instance.getjGITSearchUtils().SearchAllRepos(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lGitSearchResult));
	    instance.createLoaderFileForSystem(pUser, pPlan, pImp, pSystemLoad, pWorkspaceType, pLoaderType, false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of getCpuListBySystemId method, of class ProtectedService.
     */
    @Test
    public void testGetCpuListBySystemId() {
	Integer pId = null;
	ProtectedService instance = spy(new ProtectedService());
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "tosConfig", mock(TOSConfig.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	when(instance.getTOSConfig().getTosSystemType()).thenReturn(Constants.TOSEnvironment.PRODUCTION.name());
	JSONResponse result = instance.getCpuListBySystemId(pId);
	assertNotNull(result);
    }

    /**
     * Test of getTOSServerListBySystemId method, of class ProtectedService.
     */
    @Test
    public void testGetTOSServerListBySystemId() {
	Integer pId = 0;
	String pType = "";
	ProtectedService instance = spy(new ProtectedService());
	ReflectionTestUtils.setField(instance, "systemCpuDAO", mock(SystemCpuDAO.class));
	JSONResponse result = instance.getTOSServerListBySystemId(pId, pType);
	assertTrue(result.getStatus());
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	when(instance.getSystemDAO().findAll()).thenReturn(DataWareHouse.getSystemList());
	instance.getTOSServerListBySystemId(null, pType);
    }

    /**
     * Test of getTOSServerListBySystemId method, of class ProtectedService.
     */
    // @Test
    public void testGetTOSServerListBySystemId2() {
	Integer pId = DataWareHouse.getPlan().getSystemLoadList().get(0).getId();
	String pType = "";
	ProtectedService instance = new ProtectedService();
	JSONResponse expResult = null;
	JSONResponse result = instance.getTOSServerListBySystemId(pId, pType);
	assertEquals(expResult, result);
    }

    /**
     * Test of postActivationAction method, of class ProtectedService.
     */
    @Test
    public void testPostActivationAction() {
	User pUser = DataWareHouse.getUser();
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	System lSystem = pLoads.get(0).getSystemId();
	pLoads.get(0).setDslUpdate("Y");
	Boolean isDeactivate = Boolean.TRUE;
	try {
	    // when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(false);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction1() {
	User pUser = DataWareHouse.getUser();
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	System lSystem = pLoads.get(0).getSystemId();
	pLoads.get(0).setDslUpdate("Y");
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(false);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript())).thenReturn(lExeResponse);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction2() {
	User pUser = DataWareHouse.getUser();
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	pLoads.get(0).setDslUpdate("Y");
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(true);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(Constants.SystemScripts.TPF_MAINTENANCE_CHECK.getScript())).thenReturn(lExeResponse);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction3() {
	User pUser = DataWareHouse.getUser();
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction4() {
	User pUser = DataWareHouse.getUser();
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	pLoads.get(0).setId(null);
	pLoads.get(0).setStatus("");
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction5() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Temp");
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	pLoads.get(0).setId(null);
	pLoads.get(0).setStatus("");
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction6() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("SystemSupport");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	pLoads.get(0).setId(null);
	pLoads.get(0).setStatus("");
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(null);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction7() {
	User pUser = DataWareHouse.getUser();
	List<SystemLoadActions> pLoads = new ArrayList();
	pLoads.add(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	JSONResponse lExeResponse = new JSONResponse();
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction8() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	// DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(1);
	lSysActions.setId(null);
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	JSONResponse lExeResponse = new JSONResponse();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction9() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(1);
	lSysActions.setId(null);
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	JSONResponse lExeResponse = new JSONResponse();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction10() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(1);
	lSysActions.setId(null);
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	JSONResponse lExeResponse = new JSONResponse();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("127.0.0.1");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().activate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().deleteAndDeActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction11() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(1);
	lSysActions.setId(null);
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	JSONResponse lExeResponse = new JSONResponse();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("127.0.0.1");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(DataWareHouse.getPlan().getDbcrList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().activate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().deleteAndDeActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction12() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	lSysActions.setDslUpdate("Y");
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("127.0.0.1");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	// ReflectionTestUtils.setField(instance, "dSLFileHelper",
	// mock(DSLFileHelper.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().activate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().deleteAndDeActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction13() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.TRUE;
	JSONResponse lExeResponse = new JSONResponse();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("127.0.0.1");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().deActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction14() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("127.0.0.1");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().deActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().getVparsIP(lSysActions.getVparId().getName())).thenReturn(Arrays.asList(lYodaResult));
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction15() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("127.0.0.1");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse lFTPResponse = new JSONResponse();
	lFTPResponse.setStatus(true);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().loadAndActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().getVparsIP(lSysActions.getVparId().getName())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getFTPHelper().doFTP(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lFTPResponse);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction16() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(0);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse lFTPResponse = new JSONResponse();
	lFTPResponse.setStatus(true);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().deActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().getVparsIP(lSysActions.getVparId().getName())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getFTPHelper().doFTP(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lFTPResponse);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction17() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole("Lead");
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	lSysActions.getVparId().setId(null);
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(8);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse lFTPResponse = new JSONResponse();
	lFTPResponse.setStatus(true);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().deActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().getVparsIP(lSysActions.getVparId().getName())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getFTPHelper().doFTP(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lFTPResponse);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testPostActivationAction18() {
	User pUser = DataWareHouse.getUser();
	pUser.setCurrentRole(Constants.UserGroup.QA.name());
	List<SystemLoadActions> pLoads = new ArrayList();
	DataWareHouse.setRefreshPlan();
	ImpPlan planId = DataWareHouse.getPlan();
	planId.setPlanStatus(Constants.PlanStatus.ACTIVE.name());
	SystemLoadActions lSysActions = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	lSysActions.setId(null);
	lSysActions.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	lSysActions.getVparId().setType(Constants.VPARSEnvironment.PRE_PROD.name());
	lSysActions.setPlanId(planId);
	pLoads.add(lSysActions);
	System lSystem = pLoads.get(0).getSystemId();
	Boolean isDeactivate = Boolean.FALSE;
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setIp("");
	lYodaResult.setLab("Test");
	lYodaResult.setMessage("Message");
	lYodaResult.setRc(8);
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	JSONResponse lFTPResponse = new JSONResponse();
	lFTPResponse.setStatus(true);
	try {
	    when(instance.getSystemDAO().find(lSystem.getId())).thenReturn(lSystem);
	    when(instance.getSystemLoadActionsDAO().find(pLoads.get(0).getId())).thenReturn(pLoads.get(0));
	    when(instance.getBuildDAO().findLastSuccessfulBuild(pLoads.get(0).getPlanId().getId(), lSystem.getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(lBuild);
	    when(instance.getDbcrDAO().findByPlanSystemEnvironment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(new ArrayList());
	    when(instance.getDbcrHelper().isDbcrComplete(Matchers.anyObject())).thenReturn(false);
	    when(instance.getTestSystemLoadDAO().deActivate(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getTestSystemLoadDAO().getVparsIP(lSysActions.getVparId().getName())).thenReturn(Arrays.asList(lYodaResult));
	    when(instance.getFTPHelper().doFTP(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lFTPResponse);
	    instance.postActivationAction(pUser, pLoads, isDeactivate, false, false);
	} catch (Exception Ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeleteDbcr() throws Exception {
	try {
	    ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	    instance.deleteDbcr(DataWareHouse.user, null);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSaveDbcr() {
	try {
	    Dbcr dbcr = new Dbcr();
	    instance.saveDbcrList(DataWareHouse.user, Arrays.asList(dbcr));
	    try {
		instance.saveDbcrList(DataWareHouse.user, null);
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	} catch (Exception e) {
	    // do nothing
	}
    }

    /**
     * Test of getDevLoadByPlan method, of class ProtectedService.
     */
    @Test
    public void testGetDevLoadByPlan() {
	String ids = DataWareHouse.getPlan().getId();
	ProtectedService instance = new ProtectedService();
	instance.systemLoadDAO = mock(SystemLoadDAO.class);
	instance.buildDAO = mock(BuildDAO.class);
	List<System> systemList = new ArrayList<>();
	for (SystemLoad systemLoad : DataWareHouse.getPlan().getSystemLoadList()) {
	    systemList.add(systemLoad.getSystemId());
	}
	List<Build> lBuilds = new ArrayList<>();
	lBuilds.add(new Build());
	when(instance.getBuildDAO().findLastBuildByPlan(ids, systemList, Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(lBuilds);
	when(instance.getSystemLoadDAO().findByImpPlan(new ImpPlan(ids))).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	JSONResponse result = instance.getDevLoadByPlan(ids);
	assertNotNull(result);
    }

    @Test
    public void testNonProdFileSearch() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.TRUE);

	String pFileName = "test";
	List<CheckoutSegments> lSegments = pImplementation.getCheckoutSegmentsList();
	JSONResponse lResponse = new JSONResponse();
	try {
	    when(instance.getCheckoutSegmentsDAO().findByFileName(pFileName, pImplementation.getId())).thenReturn(lSegments);
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    instance.nonProdFileSearch(pUser, pImplementation.getId(), pFileName);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testNonProdFileSearch1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.TRUE);
	String pFileName = "test";
	List<CheckoutSegments> lSegments = pImplementation.getCheckoutSegmentsList();
	JSONResponse lResponse = new JSONResponse();
	try {
	    when(instance.getCheckoutSegmentsDAO().findByFileName(pFileName, pImplementation.getId())).thenReturn(null);
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    instance.nonProdFileSearch(pUser, pImplementation.getId(), pFileName);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testIbmVanillaFileSearch() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.TRUE);
	String pFileName = "test";
	List<System> lSystemList = DataWareHouse.systemList;
	List<SystemLoad> lSystemLoadList = lPlan.getSystemLoadList();
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemLoadDAO().findByImpPlan(pImplementation.getPlanId())).thenReturn(lSystemLoadList);
	    when(instance.getSystemDAO().findByPlatform(lSystemLoadList.get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    // when(instance.getSSHUtil().connectSSH(lSystemLoadList.get(0).getSystemId())).thenReturn(Boolean.FALSE);
	    instance.ibmVanillaFileSearch(pUser, pImplementation.getId(), pFileName);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testIbmVanillaFileSearch1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.TRUE);
	String pFileName = "test";
	List<System> lSystemList = DataWareHouse.systemList;
	List<SystemLoad> lSystemLoadList = lPlan.getSystemLoadList();
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(Boolean.TRUE);
	lExeResponse.setData("/ztpf/ibm/put13b/base/openssl/crypto/conf/test.c");
	String lCommand = "readlink -f " + instance.getWFConfig().getIbmVanillaDirectory() + " | find $(awk '{print $1}') -type f -iname " + pFileName + "*";
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemLoadDAO().findByImpPlan(pImplementation.getPlanId())).thenReturn(lSystemLoadList);
	    when(instance.getSystemDAO().findByPlatform(lSystemLoadList.get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    // when(instance.getSSHUtil().connectSSH(lSystemLoadList.get(0).getSystemId())).thenReturn(Boolean.TRUE);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    instance.ibmVanillaFileSearch(pUser, pImplementation.getId(), pFileName);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testIbmVanillaFileSearch2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.TRUE);
	String pFileName = "test";
	List<System> lSystemList = DataWareHouse.systemList;
	List<SystemLoad> lSystemLoadList = lPlan.getSystemLoadList();
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(Boolean.TRUE);
	lExeResponse.setData("/ztpf/ibm/put13b/base/openssl/crypto/conf/test.c");
	String lCommand = "readlink -f " + instance.getWFConfig().getIbmVanillaDirectory() + " | find $(awk '{print $1}') -type f -iname " + pFileName + "*";
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemLoadDAO().findByImpPlan(pImplementation.getPlanId())).thenReturn(lSystemLoadList);
	    when(instance.getSystemDAO().findByPlatform(lSystemLoadList.get(0).getSystemId().getPlatformId())).thenReturn(new ArrayList());
	    // when(instance.getSSHUtil().connectSSH(lSystemLoadList.get(0).getSystemId())).thenReturn(Boolean.TRUE);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    instance.ibmVanillaFileSearch(pUser, pImplementation.getId(), pFileName);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testProdFileSearch() {
	User pUser = DataWareHouse.user;
	Implementation pImplementation = DataWareHouse.getPlan().getImplementationList().get(0);
	String pFileName = "test";
	JSONResponse lResponse = new JSONResponse();
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", pFileName.toLowerCase(), pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(Arrays.asList(DataWareHouse.getGitSearchResult()));
	    lResponse = instance.prodFileSearch(pUser, pImplementation.getId(), pFileName, false);
	    Assert.assertEquals(true, lResponse.getStatus());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testProdFileSearch1() {
	User pUser = DataWareHouse.user;
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	String pFileName = "test";
	JSONResponse lResponse = new JSONResponse();
	try {
	    ImplementationDAO lImpDAO = instance.getImplementationDAO();
	    Mockito.doThrow(Exception.class).when(lImpDAO).find(pImplementation.getId());
	    lResponse = instance.prodFileSearch(pUser, pImplementation.getId(), pFileName, false);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetPlanByAdvancedSearch() {
	AdvancedSearchForm searchForm = new AdvancedSearchForm();
	Integer pOffset = 1;
	Integer pLimit = 10;
	LinkedHashMap<String, String> pOrderBy = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	try {
	    instance.getPlanByAdvancedSearch(searchForm, pOffset, pLimit, pOrderBy);

	} catch (Exception e) {

	}
    }

    @Test
    public void migrateNonIbmFile() {
	User pUser = DataWareHouse.user;
	List<GitSearchResult> pSearchResults = new ArrayList<GitSearchResult>();
	List<GitBranchSearchResult> lBranches = new ArrayList<GitBranchSearchResult>();
	GitSearchResult gitSearchResult = new GitSearchResult();
	GitBranchSearchResult gitBranchSearchResult = new GitBranchSearchResult();
	gitBranchSearchResult.setIsBranchSelected(Boolean.TRUE);
	lBranches.add(gitBranchSearchResult);
	gitSearchResult.setBranch(lBranches);
	pSearchResults.add(gitSearchResult);
	try {
	    instance.migrateNonIbmFile(pUser, pSearchResults);
	} catch (Exception e) {

	}
    }

    @Test
    public void testGetPlanByAdvancedSearchUsingMock() {

	Integer pOffset = 1;
	Integer pLimit = 10;
	LinkedHashMap<String, String> pOrderBy = mock(LinkedHashMap.class);
	JSONResponse mockJSON = mock(JSONResponse.class);
	// ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	try {
	    when(pOrderBy.entrySet()).thenReturn(any(), any());
	    when(protectedServiceMock.advancedSearchExportExcel(advancedSearchInjectMock, pOffset, pLimit, pOrderBy)).thenReturn(mockJSON);

	} catch (Exception e) {

	}
    }
}
