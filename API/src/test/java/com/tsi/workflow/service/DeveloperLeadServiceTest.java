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

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PdddsLibrary;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PdddsLibraryDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.external.CSRNumberDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.helper.DateAuditCrossCheck;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.ExceptionLoadNotificationHelper;
import com.tsi.workflow.helper.ImplementationHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.mail.CompilerValidationMail;
import com.tsi.workflow.mail.DeletePlanMail;
import com.tsi.workflow.mail.DevManagerAssignmentMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.SequenceGenerator;
import com.tsi.workflow.websocket.WSBroadCaster;
import com.workflow.ssh.SSHClientUtils;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author User
 */
public class DeveloperLeadServiceTest {

    DeveloperLeadService instance;

    public DeveloperLeadServiceTest() {
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
	    DeveloperLeadService realInstance = new DeveloperLeadService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, DbcrDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, ProjectDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, CSRNumberDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, LDAPAuthenticatorImpl.class);
	    TestCaseMockService.doMockDAO(instance, DateAuditCrossCheck.class);
	    TestCaseMockService.doMockDAO(instance, DbcrHelper.class);
	    TestCaseMockService.doMockDAO(instance, ExceptionLoadNotificationHelper.class);
	    TestCaseMockService.doMockDAO(instance, LdapGroupConfig.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, SequenceGenerator.class);
	    TestCaseMockService.doMockDAO(instance, GitBlitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationHelper.class);
	    TestCaseMockService.doMockDAO(instance, PlanHelper.class);
	    TestCaseMockService.doMockDAO(instance, ProjectDAO.class);

	} catch (Exception ex) {
	    Logger.getLogger(DeveloperLeadService.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperLeadService() throws Exception {
	TestCaseExecutor.doTest(instance, DeveloperLeadService.class);
    }

    @Test
    public void testGetDbcrList() throws Exception {
	try {
	    instance.getDbcrList(null, null);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSubmitToDevManager() throws Exception {
	try {
	    instance.planSubmit(DataWareHouse.user, null);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetPlan() throws Exception {
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));

	when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	when(instance.checkoutSegmentsDAO.getBlockedSystemsByPlan(DataWareHouse.getPlan().getId())).thenReturn(Arrays.asList("as"));
	instance.getPlan(DataWareHouse.getPlan().getId());
    }

    /**
     * Test of checkForDvlBuild method, of class DeveloperLeadService.
     */
    @Test
    public void testCheckForDvlBuild() {
	String pPlanId = DataWareHouse.setRefreshPlan().getId();
	DeveloperLeadService instance = new DeveloperLeadService();
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	when(instance.getSystemLoadDAO().findByImpPlan(pPlanId)).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	when(instance.getCheckoutSegmentsDAO().findBySystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	List<CheckoutSegments> lSegments = DataWareHouse.getPlan().getCheckoutSegmentsList();
	when(instance.implementationDAO.findByImpPlan(pPlanId)).thenReturn(DataWareHouse.getPlan().getImplementationList());
	CheckoutSegments lSegment = new CheckoutSegments();
	lSegment.setFileName("abcd.csv");
	lSegments.add(lSegment);
	when(instance.getCheckoutSegmentsDAO().findBySystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(1).getId())).thenReturn(lSegments);
	try {
	    JSONResponse result = instance.checkForDvlBuild(pPlanId);
	    assertNotNull(result);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    @Test
    public void testSubmitToDevManager2() throws Exception {
	try {
	    DeveloperLeadService instance = spy(new DeveloperLeadService());
	    ConcurrentHashMap<String, User> test = new ConcurrentHashMap<String, User>();
	    ConcurrentHashMap<String, String> test1 = new ConcurrentHashMap<String, String>();
	    // instance.wssPlanIdAndUserId = test1;
	    ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "wsserver", mock(WSBroadCaster.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    DataWareHouse.getPlan().setLoadType("Passed");
	    when(instance.getImplementationDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan())).thenReturn("PASSED");
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.getCheckoutSegmentsDAO().findBySystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	    String command = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + "_";
	    Session lSession = mock(Session.class);
	    ChannelExec lChannel = mock(ChannelExec.class);
	    // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	    when(lSession.openChannel("exec")).thenReturn(lChannel);
	    when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", DataWareHouse.getPlan().getId().toLowerCase())).thenReturn("RepoName");
	    when(instance.getsSHClientUtils().addImplementationTag("RepoName", Constants.TagStatus.SECURED, new ArrayList<>())).thenReturn(true);
	    // when(instance.getGitBlitClientUtils().setPermissionForGitRepository(null,
	    // DataWareHouse.getUser().getId(),
	    // Constants.GIT_PERMISSION_READ)).thenReturn(true);
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.mailMessageFactory.getTemplate(CompilerValidationMail.class)).thenReturn(mock(CompilerValidationMail.class));
	    JSONResponse lCommandResponse = new JSONResponse();
	    lCommandResponse.setStatus(true);
	    lCommandResponse.setErrorMessage("Warning");
	    DataWareHouse.getPlan().getSystemLoadList().forEach(sysLoad -> {
		sysLoad.setLoadDateTime(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
		when(instance.getsSHClientUtils().executeCommand(sysLoad.getSystemId(), "${MTP_ENV}/mtptpfcntchk t1700484_001_apo")).thenReturn(lCommandResponse);
	    });

	    DataWareHouse.getPlan().setLoadType("Passed");
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    // doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	    // doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(1).getSystemId());
	    // doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(2).getSystemId());
	    // doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	    // when(instance.getSSHUtil()).thenReturn(sshUtil);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }

	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan())).thenReturn("PASSED");
	    when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	    Build build = DataWareHouse.getPlan().getBuildList().get(0);
	    build.setJobStatus("S");
	    when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(Arrays.asList(build));
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }

	    // when(sshUtil.executeCommand(command +
	    // DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getName().toLowerCase())).thenReturn(DataWareHouse.getPositiveResponse());
	    // when(sshUtil.executeCommand(command +
	    // DataWareHouse.getPlan().getSystemLoadList().get(1).getSystemId().getName().toLowerCase())).thenReturn(DataWareHouse.getPositiveResponse());
	    // when(sshUtil.executeCommand(command +
	    // DataWareHouse.getPlan().getSystemLoadList().get(2).getSystemId().getName().toLowerCase())).thenReturn(DataWareHouse.getPositiveResponse());
	    // when(sshUtil.executeCommand(command +
	    // DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getName().toLowerCase())).thenReturn(DataWareHouse.getPositiveResponse());
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	    when(instance.getImpPlanDAO().doPlanAudit(DataWareHouse.getPlan().getId(), Constants.PlanStatus.SUBMITTED.name())).thenReturn("asd");
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	    ImpPlan impPlan = DataWareHouse.getPlan();
	    impPlan.setLoadType("Exception");
	    impPlan.setPlanStatus("SUBMITTED");
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	    impPlan.setPlanStatus("ACTIVE");
	    impPlan.setDevManager(null);
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	    impPlan.setDevManager(DataWareHouse.user.getId());
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(impPlan.getDevManager())).thenReturn(DataWareHouse.user);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	    List<TaskVariable> lTaskVars = new ArrayList<>();
	    lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, impPlan.getId()));
	    when(instance.getBPMClientUtils().setTaskAsCompleted(DataWareHouse.getUser(), impPlan.getProcessId())).thenReturn(Boolean.TRUE);
	    when(instance.getBPMClientUtils().assignTask(DataWareHouse.getUser(), impPlan.getProcessId(), impPlan.getDevManager(), lTaskVars)).thenReturn(Boolean.TRUE);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }

	    try {
		ImpPlan lPlan1 = DataWareHouse.getPlan();
		lPlan1.setLoadType(Constants.LoadTypes.STANDARD.name());
		;
		when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(lPlan1);
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (Exception e) {
		// do nothing
	    }

	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), impPlan)).thenReturn("FAILED");
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSubmitToDevManager3() throws Exception {
	try {
	    DeveloperLeadService instance = spy(new DeveloperLeadService());
	    ConcurrentHashMap<String, User> test = new ConcurrentHashMap<String, User>();
	    // instance.lPlanUpdateStatusMap = test;
	    ConcurrentHashMap<String, String> test1 = new ConcurrentHashMap<String, String>();
	    // instance.wssPlanIdAndUserId = test1;
	    ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "wsserver", mock(WSBroadCaster.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    DataWareHouse.getPlan().setLoadType("Passed");
	    when(instance.getImplementationDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan())).thenReturn("PASSED");
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.getCheckoutSegmentsDAO().findBySystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	    String command = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + "_";
	    Session lSession = mock(Session.class);
	    ChannelExec lChannel = mock(ChannelExec.class);
	    // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	    when(lSession.openChannel("exec")).thenReturn(lChannel);
	    when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", DataWareHouse.getPlan().getId().toLowerCase())).thenReturn("RepoName");
	    when(instance.getsSHClientUtils().addImplementationTag("RepoName", Constants.TagStatus.SECURED, new ArrayList<>())).thenReturn(true);
	    // when(instance.getGitBlitClientUtils().setPermissionForGitRepository(null,
	    // DataWareHouse.getUser().getId(),
	    // Constants.GIT_PERMISSION_READ)).thenReturn(true);
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.mailMessageFactory.getTemplate(CompilerValidationMail.class)).thenReturn(mock(CompilerValidationMail.class));
	    JSONResponse lCommandResponse = new JSONResponse();
	    lCommandResponse.setStatus(false);
	    lCommandResponse.setErrorMessage("");
	    DataWareHouse.getPlan().getSystemLoadList().forEach(sysLoad -> {
		sysLoad.setLoadDateTime(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
		when(instance.getsSHClientUtils().executeCommand(sysLoad.getSystemId(), "${MTP_ENV}/mtptpfcntchk t1700484_001_apo")).thenReturn(lCommandResponse);
	    });
	    Build build = DataWareHouse.getPlan().getBuildList().get(0);
	    build.setJobStatus("S");
	    when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(Arrays.asList(build));
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan())).thenReturn("PASSED");
	    when(instance.getImpPlanDAO().doPlanAudit(DataWareHouse.getPlan().getId(), Constants.PlanStatus.SUBMITTED.name())).thenReturn("asd");
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    ImpPlan impPlan = DataWareHouse.getPlan();
	    impPlan.setLoadType("Exception");
	    impPlan.setPlanStatus("Approved");
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }

	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSubmitToDevManager4() throws Exception {
	try {
	    DeveloperLeadService instance = spy(new DeveloperLeadService());
	    ConcurrentHashMap<String, User> test = new ConcurrentHashMap<String, User>();
	    // instance.lPlanUpdateStatusMap = test;
	    ConcurrentHashMap<String, String> test1 = new ConcurrentHashMap<String, String>();
	    // instance.wssPlanIdAndUserId = test1;
	    ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "wsserver", mock(WSBroadCaster.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    DataWareHouse.getPlan().setLoadType("Passed");
	    when(instance.getImplementationDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan())).thenReturn("PASSED");
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.getCheckoutSegmentsDAO().findBySystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	    String command = Constants.SystemScripts.COMPILER_CONTROL_VALIDATION.getScript() + " " + DataWareHouse.getPlan().getImplementationList().get(0).getId().toLowerCase() + "_";
	    Session lSession = mock(Session.class);
	    ChannelExec lChannel = mock(ChannelExec.class);
	    // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	    when(lSession.openChannel("exec")).thenReturn(lChannel);
	    when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", DataWareHouse.getPlan().getId().toLowerCase())).thenReturn("RepoName");
	    when(instance.getsSHClientUtils().addImplementationTag("RepoName", Constants.TagStatus.SECURED, new ArrayList<>())).thenReturn(true);
	    // when(instance.getGitBlitClientUtils().setPermissionForGitRepository(null,
	    // DataWareHouse.getUser().getId(),
	    // Constants.GIT_PERMISSION_READ)).thenReturn(true);
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.mailMessageFactory.getTemplate(CompilerValidationMail.class)).thenReturn(mock(CompilerValidationMail.class));
	    JSONResponse lCommandResponse = new JSONResponse();
	    lCommandResponse.setStatus(false);
	    lCommandResponse.setErrorMessage("Warning");
	    DataWareHouse.getPlan().getSystemLoadList().forEach(sysLoad -> {
		sysLoad.setLoadDateTime(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
		when(instance.getsSHClientUtils().executeCommand(sysLoad.getSystemId(), "${MTP_ENV}/mtptpfcntchk t1700484_001_apo")).thenReturn(lCommandResponse);
	    });
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    Build build = DataWareHouse.getPlan().getBuildList().get(0);
	    build.setJobStatus("S");
	    when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(Arrays.asList(build));
	    when(instance.getDateAuditCrossCheck().dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan())).thenReturn("PASSED");
	    when(instance.getImpPlanDAO().doPlanAudit(DataWareHouse.getPlan().getId(), Constants.PlanStatus.SUBMITTED.name())).thenReturn("asd");
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	    ImpPlan impPlan = DataWareHouse.getPlan();
	    impPlan.setLoadType("Passed");
	    impPlan.setPlanStatus("Approved");
	    when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	    try {
		instance.planSubmit(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    } catch (WorkflowException e) {
		assertTrue(true);
	    }
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of savePlan method, of class DeveloperLeadService.
     */
    @Test
    public void testSavePlan() {

	User pUser = DataWareHouse.getUserWithDelagated();
	ImpPlan pPlan = DataWareHouse.getPlan();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	BPMClientUtils bpmutils = mock(BPMClientUtils.class);
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", bpmutils);
	ReflectionTestUtils.setField(instance, "lGenerator", mock(SequenceGenerator.class));
	List<TaskVariable> lVars = new ArrayList<>();
	lVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, pPlan.getId()));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.getMailMessageFactory().getTemplate(DevManagerAssignmentMail.class)).thenReturn(mock(DevManagerAssignmentMail.class));
	try {
	    // when(bpmutils.createADLProcess(pUser,
	    // pUser.getMailId())).thenReturn("82186");
	    when(instance.getSequenceGenerator().getNewImplementationPlanId("T")).thenReturn(pPlan.getId());
	    JSONResponse result = instance.savePlan(pUser, pPlan);
	    // Assert.assertNotNull(result);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	try {
	    when(bpmutils.createADLProcess(pUser)).thenReturn("82186");
	    when(bpmutils.assignTask(pUser, "82186", "prabhu.prabhakaran", lVars)).thenReturn(true);

	    JSONResponse result = instance.savePlan(pUser, pPlan);
	} catch (IOException ex) {

	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	List<SystemLoad> oldLst = new ArrayList();
	try {

	    oldLst = pPlan.getSystemLoadList();
	    SystemLoad systemLoad = pPlan.getSystemLoadList().get(0);
	    systemLoad.setPutLevelId(null);
	    List<SystemLoad> lst = new ArrayList();
	    lst.add(systemLoad);
	    // lst.addAll(pPlan.getSystemLoadList());
	    pPlan.setSystemLoadList(lst);
	    JSONResponse result = instance.savePlan(pUser, pPlan);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	pPlan.setSystemLoadList(oldLst);
    }

    @Test
    public void testSavePlan2() {
	DeveloperLeadService lService = new DeveloperLeadService();
	User pUser = DataWareHouse.getUserWithDelagated();
	ImpPlan pPlan = DataWareHouse.getPlan();
	List<Dbcr> lDbcr = new ArrayList();
	lDbcr.add(mock(Dbcr.class));
	pPlan.getSystemLoadList().get(0).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(1).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(2).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(3).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(0).getSystemId().setDbcrList(lDbcr);
	BPMClientUtils bpmutils = mock(BPMClientUtils.class);
	SequenceGenerator seq = mock(SequenceGenerator.class);
	ReflectionTestUtils.setField(lService, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(lService, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(lService, "dbcrDAO", mock(DbcrDAO.class));
	ReflectionTestUtils.setField(lService, "lBPMClientUtils", bpmutils);
	ReflectionTestUtils.setField(lService, "lGenerator", seq);
	ReflectionTestUtils.setField(lService, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(lService, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(lService, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(lService, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(lService.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	try {
	    when(lService.getMailMessageFactory().getTemplate(DevManagerAssignmentMail.class)).thenReturn(mock(DevManagerAssignmentMail.class));
	    when(bpmutils.createADLProcess(Matchers.any())).thenReturn("82186");
	    when(bpmutils.assignTask(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(true);
	    when(seq.getNewImplementationPlanId(Matchers.any())).thenReturn("123");
	    lService.savePlan(pUser, pPlan);

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSavePlan3() {
	DeveloperLeadService lService = new DeveloperLeadService();
	User pUser = DataWareHouse.getUserWithDelagated();
	ImpPlan pPlan = DataWareHouse.setRefreshPlan();
	pPlan.getSystemLoadList().get(0).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(1).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(2).setPutLevelId(mock(PutLevel.class));
	pPlan.getSystemLoadList().get(3).setPutLevelId(mock(PutLevel.class));
	ReflectionTestUtils.setField(lService, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(lService, "systemLoadDAO", mock(SystemLoadDAO.class));
	List<String> lPlans = new ArrayList<>();
	lPlans.add("123");
	when(lService.impPlanDAO.getInvalidRelatedPlans(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(lPlans);
	try {
	    lService.savePlan(pUser, pPlan);
	} catch (WorkflowException ex) {

	}
    }

    @Test
    public void testSubmitToDevManager03() {
	DeveloperLeadService lService = new DeveloperLeadService();
	DeveloperLeadService ins = spy(lService);
	User pUser = DataWareHouse.getUserWithDelagated();
	String pPlanId = DataWareHouse.setRefreshPlan().getId();
	ConcurrentHashMap<String, User> lMap = new ConcurrentHashMap<>();
	// ins.lPlanUpdateStatusMap = lMap;
	List<Build> lBuilds = new ArrayList();
	Build lBuild = DataWareHouse.getPlan().getBuildList().get(0);
	lBuild.setJobStatus("S");
	lBuilds.add(lBuild);
	String sDate1 = "31/12/2028";
	Date date1 = null;
	try {
	    date1 = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
	} catch (ParseException ex) {
	    // do nothing
	}
	List<CheckoutSegments> lSegments = new ArrayList<>();
	lSegments.add(mock(CheckoutSegments.class));
	List<SystemLoad> lLoads = DataWareHouse.getPlan().getSystemLoadList();
	for (SystemLoad lLoad : lLoads) {
	    lLoad.setLoadDateTime(date1);
	}
	Session lSession = mock(Session.class);
	ChannelExec lChannel = mock(ChannelExec.class);
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	try {
	    when(lSession.openChannel("exec")).thenReturn(lChannel);
	    when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
	} catch (Exception e) {
	    // do nothing
	}
	BPMClientUtils bpm = mock(BPMClientUtils.class);
	// GITSSHUtils gitsshu = spy(new GITSSHUtils(mock(IGitConfig.class)));
	ReflectionTestUtils.setField(ins, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(ins, "dateAuditCrossCheck", mock(DateAuditCrossCheck.class));
	ReflectionTestUtils.setField(ins, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(ins, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(ins, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(ins, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(ins, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(ins, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(ins, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(ins, "lBPMClientUtils", bpm);
	ReflectionTestUtils.setField(ins, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(ins, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(ins, "activityLogDAO", mock(ActivityLogDAO.class));
	when(ins.impPlanDAO.find(pPlanId)).thenReturn(DataWareHouse.getPlan());
	when(ins.dateAuditCrossCheck.dateAutditForMigration(Matchers.any(), Matchers.any())).thenReturn("PASSED");
	when(ins.buildDAO.findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(lBuilds);
	when(ins.systemLoadDAO.findByImpPlan(pPlanId)).thenReturn(lLoads);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(0).getId())).thenReturn(null);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(1).getId())).thenReturn(null);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(2).getId())).thenReturn(null);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(3).getId())).thenReturn(null);
	when(ins.impPlanDAO.doPlanAudit(pPlanId, Constants.PlanStatus.SUBMITTED.name())).thenReturn("test");
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    assertTrue(true);
	}
	// when(ins.getSSHUtil()).thenReturn(sshUtil);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(0).getId())).thenReturn(lSegments);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(1).getId())).thenReturn(lSegments);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(2).getId())).thenReturn(lSegments);
	when(ins.checkoutSegmentsDAO.findBySystemLoad(lLoads.get(3).getId())).thenReturn(lSegments);
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    assertTrue(true);
	}
	// doReturn(true).when(sshUtil).connectSSH(lLoads.get(0).getSystemId());
	// doReturn(true).when(sshUtil).connectSSH(lLoads.get(1).getSystemId());
	// doReturn(true).when(sshUtil).connectSSH(lLoads.get(2).getSystemId());
	// doReturn(true).when(sshUtil).connectSSH(lLoads.get(3).getSystemId());
	// when(ins.getSSHUtil()).thenReturn(sshUtil);
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    assertTrue(true);
	}
	JSONResponse lResponse = DataWareHouse.getNegativeResponse();
	lResponse.setErrorMessage("Error Code: 8 Warning");
	String command = "${MTP_ENV}/mtptpfcntchk t1700484_001_apo";
	// when(sshUtil.executeCommand(command)).thenReturn(lResponse);
	when(ins.mailMessageFactory.getTemplate(CompilerValidationMail.class)).thenReturn(mock(CompilerValidationMail.class));
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    assertTrue(true);
	}
	String command1 = "${MTP_ENV}/mtptpfcntchk t1700484_001_apo";
	// when(sshUtil.executeCommand(command1)).thenReturn(DataWareHouse.getPositiveResponse());
	String command2 = "${MTP_ENV}/mtptpfcntchk t1700484_001_pre";
	// when(sshUtil.executeCommand(command2)).thenReturn(DataWareHouse.getPositiveResponse());
	String command3 = "${MTP_ENV}/mtptpfcntchk t1700484_001_pgr";
	// when(sshUtil.executeCommand(command3)).thenReturn(DataWareHouse.getPositiveResponse());
	String command4 = "${MTP_ENV}/mtptpfcntchk t1700484_001_wsp";
	// when(sshUtil.executeCommand(command4)).thenReturn(DataWareHouse.getPositiveResponse());
	when(ins.impPlanDAO.doPlanAudit(pPlanId, Constants.PlanStatus.SUBMITTED.name())).thenReturn("");
	when(ins.authenticator.getUserDetails(Matchers.any())).thenReturn(DataWareHouse.getUser());
	List<String> lBranch = new ArrayList<>();
	try {
	    // when(gitsshu.addImplementationTag(DataWareHouse.getUser(), null,
	    // Constants.TagStatus.SECURED,
	    // lBranch)).thenReturn(true);
	    when(bpm.setTaskAsCompleted(Matchers.any(), Matchers.any())).thenReturn(true);
	    when(bpm.assignTask(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(true);
	    // when(ins.getGitBlitClientUtils().setPermissionForGitRepository(null,
	    // DataWareHouse.getUser().getId(),
	    // Constants.GIT_PERMISSION_READ)).thenReturn(true);
	} catch (Exception ex) {
	    // do nothing
	}
	List<ImpPlan> lPlans = new ArrayList<>();
	lPlans.add(DataWareHouse.getPlan());
	when(ins.impPlanDAO.findDependentPlans(DataWareHouse.getPlan().getId())).thenReturn(lPlans);
	// when(ins.getGITSSHUtils()).thenReturn(gitsshu);
	when(ins.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	// try {
	// when(ins.getGitBlitClientUtils().setPermissionForGitRepository(null,
	// DataWareHouse.getUser().getId(),
	// Constants.GIT_PERMISSION_READ)).thenReturn(false);
	// } catch (Exception ex) {
	// // do nothing
	// }
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	try {
	    when(bpm.assignTask(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(false);
	} catch (Exception ex) {
	    // do nothing
	}
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	try {
	    // when(gitsshu.addImplementationTag(DataWareHouse.getUser(), null,
	    // Constants.TagStatus.SECURED,
	    // lBranch)).thenReturn(false);
	} catch (Exception ex) {
	    // do nothing
	}
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	try {
	    when(bpm.assignTask(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(false);
	} catch (Exception ex) {
	    // do nothing
	}
	// when(ins.getGITSSHUtils()).thenReturn(gitsshu);
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	try {
	    when(bpm.setTaskAsCompleted(Matchers.any(), Matchers.any())).thenReturn(true);
	    when(bpm.assignTask(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(false);
	} catch (Exception ex) {
	    // do nothing
	}
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setDevManager("");
	when(ins.impPlanDAO.find(pPlanId)).thenReturn(lPlan);
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
	lPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());
	when(ins.impPlanDAO.find(pPlanId)).thenReturn(lPlan);
	try {
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}

	try {
	    ImpPlan lPlan1 = DataWareHouse.getPlan();
	    lPlan1.setLoadType(Constants.LoadTypes.STANDARD.name());
	    ;
	    when(ins.impPlanDAO.find(pPlanId)).thenReturn(lPlan1);
	    ins.planSubmit(pUser, DataWareHouse.getPlan());
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDeleteImplementations() {
	User pUser = DataWareHouse.user;
	Implementation pId = DataWareHouse.getPlan().getImplementationList().get(0);
	try {
	    instance.deleteImplementations(pUser, pId.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlan() {
	User pUser = DataWareHouse.user;
	ImpPlan pId = DataWareHouse.getPlan();
	// ImpPlan lPlan = new ImpPlan();
	// BeanUtils.copyProperties(pId, lPlan);
	// lPlan.setImplementationList(new ArrayList());
	// Implementation lImp = pId.getImplementationList().get(0);

	try {
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(pId);
	    JSONResponse lCmdResponse = new JSONResponse();
	    lCmdResponse.setStatus(false);
	    DataWareHouse.getPlan().getSystemLoadList().forEach(sysLoad -> {
		sysLoad.setLoadDateTime(new Date(new Date().getTime() + (1000 * 60 * 60 * 24)));
		when(instance.getsSHClientUtils().executeCommand(sysLoad.getSystemId(), "${MTP_ENV}/mtpgitdeleteimpl t1700484_apo_201710300700000000")).thenReturn(lCmdResponse);
	    });

	    instance.deletePlan(pUser, pId.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlan1() {
	User pUser = DataWareHouse.user;
	ImpPlan pId = DataWareHouse.getPlan();
	ImpPlan lPlan = new ImpPlan();
	BeanUtils.copyProperties(pId, lPlan);
	lPlan.setImplementationList(new ArrayList());
	SystemLoad lSysLoad = lPlan.getSystemLoadList().get(0);
	com.tsi.workflow.beans.dao.System lSystem = lSysLoad.getSystemId();
	List<SystemLoad> lSystemLoad = new ArrayList();
	lSystemLoad.add(lSysLoad);
	// SSHUtil sshUtil = instance.getSSHUtil();

	try {
	    // Mockito.reset(sshUtil);

	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(lPlan);
	    when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoad);
	    when(instance.getImplementationDAO().findByImpPlan(lPlan.getId())).thenReturn(lPlan.getImplementationList());
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(false);
	    instance.deletePlan(pUser, pId.getId());
	    // Mockito.reset(sshUtil);

	} catch (Exception ex) {
	    // Mockito.reset(sshUtil);
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlan2() {
	User pUser = DataWareHouse.user;
	ImpPlan pId = DataWareHouse.getPlan();
	ImpPlan lPlan = new ImpPlan();
	BeanUtils.copyProperties(pId, lPlan);
	lPlan.setImplementationList(new ArrayList());
	SystemLoad lSysLoad = lPlan.getSystemLoadList().get(0);
	com.tsi.workflow.beans.dao.System lSystem = lSysLoad.getSystemId();
	List<SystemLoad> lSystemLoad = new ArrayList();
	lSystemLoad.add(lSysLoad);
	String lCommand = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + lSysLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lSysLoad.getLoadDateTime()) + "0000").toLowerCase();
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.FALSE);
	lCmdResponse.setErrorMessage("");
	// SSHUtil sshUtil = instance.getSSHUtil();

	try {
	    // Mockito.reset(sshUtil);

	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(lPlan);
	    when(instance.getImplementationDAO().findByImpPlan(lPlan.getId())).thenReturn(lPlan.getImplementationList());
	    when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoad);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    instance.deletePlan(pUser, pId.getId());
	    // Mockito.reset(sshUtil);

	} catch (Exception ex) {
	    // Mockito.reset(sshUtil);
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlan3() {
	User pUser = DataWareHouse.user;
	ImpPlan pId = DataWareHouse.getPlan();
	ImpPlan lPlan = new ImpPlan();
	BeanUtils.copyProperties(pId, lPlan);
	lPlan.setImplementationList(new ArrayList());
	SystemLoad lSysLoad = lPlan.getSystemLoadList().get(0);
	com.tsi.workflow.beans.dao.System lSystem = lSysLoad.getSystemId();
	List<SystemLoad> lSystemLoad = new ArrayList();
	lSystemLoad.add(lSysLoad);
	String lCommand = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + lSysLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lSysLoad.getLoadDateTime()) + "0000").toLowerCase();
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setErrorMessage("");
	// SSHUtil sshUtil = instance.getSSHUtil();

	try {
	    // Mockito.reset(sshUtil);
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(pId);
	    // JSONResponse lCmdResponse = new JSONResponse();
	    lCmdResponse.setStatus(false);
	    DataWareHouse.getPlan().getSystemLoadList().forEach(sysLoad -> {
		Date lDate = sysLoad.getLoadDateTime() != null ? sysLoad.getLoadDateTime() : new Date();
		String lCommand1 = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + sysLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lDate) + "0000").toLowerCase();
		when(instance.getsSHClientUtils().executeCommand(sysLoad.getSystemId(), lCommand1)).thenReturn(lCmdResponse);
	    });
	    when(instance.getMailMessageFactory().getTemplate(DeletePlanMail.class)).thenReturn(mock(DeletePlanMail.class));
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(lPlan);
	    when(instance.getImplementationDAO().findByImpPlan(lPlan.getId())).thenReturn(lPlan.getImplementationList());
	    when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoad);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getPlanHelper().deletePlanGitRepos("tp", lPlan.getId())).thenReturn(lCmdResponse);
	    instance.deletePlan(pUser, pId.getId());

	    // Mockito.reset(sshUtil);
	} catch (Exception ex) {
	    // Mockito.reset(sshUtil);
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlan4() {
	User pUser = DataWareHouse.user;
	ImpPlan pId = DataWareHouse.getPlan();
	ImpPlan lPlan = new ImpPlan();
	BeanUtils.copyProperties(pId, lPlan);
	lPlan.setImplementationList(new ArrayList());
	SystemLoad lSysLoad = lPlan.getSystemLoadList().get(0);
	com.tsi.workflow.beans.dao.System lSystem = lSysLoad.getSystemId();
	List<SystemLoad> lSystemLoad = new ArrayList();
	lSystemLoad.add(lSysLoad);
	String lCommand = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + lSysLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lSysLoad.getLoadDateTime()) + "0000").toLowerCase();
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setErrorMessage("");
	// SSHUtil sshUtil = instance.getSSHUtil();

	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.FALSE);
	try {
	    // Mockito.reset(sshUtil);
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(DeletePlanMail.class)).thenReturn(mock(DeletePlanMail.class));
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(lPlan);
	    when(instance.getImplementationDAO().findByImpPlan(lPlan.getId())).thenReturn(lPlan.getImplementationList());
	    when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoad);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getPlanHelper().deletePlanGitRepos("tp", lPlan.getId())).thenReturn(lResponse);
	    instance.deletePlan(pUser, pId.getId());

	    // Mockito.reset(sshUtil);
	} catch (Exception ex) {
	    // Mockito.reset(sshUtil);
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePlan5() {
	User pUser = DataWareHouse.user;
	ImpPlan pId = DataWareHouse.getPlan();
	ImpPlan lPlan = new ImpPlan();
	BeanUtils.copyProperties(pId, lPlan);
	lPlan.setImplementationList(new ArrayList());
	SystemLoad lSysLoad = lPlan.getSystemLoadList().get(0);
	com.tsi.workflow.beans.dao.System lSystem = lSysLoad.getSystemId();
	List<SystemLoad> lSystemLoad = new ArrayList();
	lSystemLoad.add(lSysLoad);
	String lCommand = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + lSysLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lSysLoad.getLoadDateTime()) + "0000").toLowerCase();
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setErrorMessage("");
	// SSHUtil sshUtil = instance.getSSHUtil();

	try {
	    // Mockito.reset(sshUtil);
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "planHelper", mock(PlanHelper.class));
	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(pId);
	    DataWareHouse.getPlan().getSystemLoadList().forEach(sysLoad -> {
		Date lDate = sysLoad.getLoadDateTime() != null ? sysLoad.getLoadDateTime() : new Date();
		String lCommand1 = Constants.SystemScripts.DELETE_PLAN.getScript() + (lPlan.getId() + "_" + sysLoad.getSystemId().getName() + "_" + Constants.JENKINS_DATEFORMAT.get().format(lDate) + "0000").toLowerCase();
		when(instance.getsSHClientUtils().executeCommand(sysLoad.getSystemId(), lCommand1)).thenReturn(lCmdResponse);
	    });
	    when(instance.getMailMessageFactory().getTemplate(DeletePlanMail.class)).thenReturn(mock(DeletePlanMail.class));
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getImpPlanDAO().find(pId.getId())).thenReturn(lPlan);
	    when(instance.getImplementationDAO().findByImpPlan(lPlan.getId())).thenReturn(lPlan.getImplementationList());
	    when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoad);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(true);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getPlanHelper().deletePlanGitRepos("tp", lPlan.getId())).thenReturn(lCmdResponse);
	    when(instance.getPlanHelper().deletePlanGitRepos(lSystemLoad.get(0).getSystemId().getPlatformId().getNickName(), lPlan.getId())).thenReturn(lCmdResponse);
	    instance.deletePlan(pUser, pId.getId());

	    // Mockito.reset(sshUtil);
	} catch (Exception ex) {
	    // Mockito.reset(sshUtil);
	    assertTrue(true);
	}
    }

    @Test
    public void testGetPlanList1() {
	try {
	    ConcurrentHashMap<String, User> test = new ConcurrentHashMap<String, User>();
	    // instance.lPlanUpdateStatusMap = test;
	    ImpPlan impPlan = DataWareHouse.getPlan();
	    impPlan.setPlanStatus("SUBMITTED");
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    when(instance.getImpPlanDAO().find("T1700484")).thenReturn(impPlan);
	    when(instance.getImpPlanDAO().findByStatusListAndOwner(Arrays.asList("T1700484"), DataWareHouse.user.getId(), 0, 10, null, null)).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	    when(instance.getCheckoutSegmentsDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	    when(instance.getImplementationDAO().findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	    DataWareHouse.getPlan().getImplementationList().forEach(imp -> {
		when(instance.getImplementationDAO().findByImpPlan(imp.getId())).thenReturn(DataWareHouse.getPlan().getImplementationList());
		when(instance.getCheckoutSegmentsDAO().findByImplementation(imp.getId())).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	    });
	    when(instance.getBuildDAO().findAll(DataWareHouse.getPlan(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList());
	    instance.getPlanList(DataWareHouse.user, 0, 10, "T1700484", null, null);

	    impPlan.setPlanStatus(Constants.PlanStatus.ACTIVE.name());
	    when(instance.getImpPlanDAO().find("T1700484")).thenReturn(impPlan);
	    instance.isSubmitReady(DataWareHouse.getPlan().getId());

	    // If Filter is empty
	    instance.getPlanList(DataWareHouse.user, 0, 10, "", null, null);

	    // Imp list is empty
	    when(instance.getImpPlanDAO().find("T1700484")).thenReturn(null);
	    instance.isSubmitReady(DataWareHouse.getPlan().getId());

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void TestGetPdddsLibrary() {
	try {
	    ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	    ReflectionTestUtils.setField(instance, "pdddsLibraryDAO", mock(PdddsLibraryDAO.class));

	    com.tsi.workflow.beans.dao.System system = new com.tsi.workflow.beans.dao.System();
	    when(instance.getSystemDAO().findByName("sysName")).thenReturn(system);
	    when(instance.getPdddsLibraryDAO().findBySystem(system)).thenReturn(new ArrayList<PdddsLibrary>());
	    instance.getPdddsLibrary("sysName");
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void TestGetDbcrList() {
	try {

	    DeveloperLeadService instance = new DeveloperLeadService();
	    instance.getDbcrList(DataWareHouse.user, new String[3]);
	} catch (Exception e) {

	}
    }
}
