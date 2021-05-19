/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.ProjectDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.helper.ExceptionLoadNotificationHelper;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.mail.DevManagerAssignmentMail;
import com.tsi.workflow.mail.RejectMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.utils.Constants;
import com.workflow.ssh.SSHClientUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DeveloperManagerServiceTest {

    DeveloperManagerService instance;

    public DeveloperManagerServiceTest() {
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
	    DeveloperManagerService realInstance = new DeveloperManagerService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, BuildDAO.class);

	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, GitBlitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, JenkinsClient.class);
	    TestCaseMockService.doMockDAO(instance, LdapGroupConfig.class);
	    TestCaseMockService.doMockDAO(instance, LDAPAuthenticatorImpl.class);
	    TestCaseMockService.doMockDAO(instance, ExceptionLoadNotificationHelper.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);

	} catch (Exception ex) {
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperManagerService() throws Exception {
	TestCaseExecutor.doTest(instance, DeveloperManagerService.class);
	instance.setGitHelper(new GITHelper());
    }

    @Test
    public void testGetApproveStatusByPlan() throws Exception {
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	instance.getApproveStatusByPlan((Arrays.asList(DataWareHouse.getPlan().getId())));
    }

    @Test
    public void testdoStagingBuild() {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setLoadType(Constants.LoadTypes.STANDARD.name());
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_BOTH.name());
	Map<String, String> lJobParams = new HashMap<>();
	lJobParams.put("ImplementationID", lPlan.getImplementationList().get(0).getId().toLowerCase() + "_" + lPlan.getSystemLoadList().get(0).getSystemId().getName().toLowerCase());
	JenkinsBuild lExecuteJob = new JenkinsBuild();

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "stagingWorkspaceCreationJobs", mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "lPlanUpdateStatusMap", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.impPlanDAO.doPlanAudit(lPlan.getId(), Constants.PlanStatus.APPROVED.name())).thenReturn("");
	when(instance.implementationDAO.findByImpPlan(lPlan.getId())).thenReturn(lPlan.getImplementationList());
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.lJenkinsClient.executeJob(lUser, Constants.STAGING.CWS_STG_.name() + lPlan.getSystemLoadList().get(0).getSystemId().getName(), lJobParams)).thenReturn(lExecuteJob);

	try {
	    instance.doStagingBuild(lUser, lPlan.getId());
	} catch (Exception ex) {
	    System.out.println(ex);
	}
    }

    @Test
    public void testApprovePlan() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", lPlan.getMacroHeader()));
	lTaskVars.add(new TaskVariable("Approver", lUser.getDisplayName()));
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(lUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.ldapGroupConfig.getLoadsControlGroups()).thenReturn(Arrays.asList(lLDAPGroup));
	when(instance.lBPMClientUtils.assignTaskToGroup(lUser, lPlan.getProcessId(), lGroupNames.toString(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.authenticator.getUserDetails(lUser.getId())).thenReturn(lUser);
	when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));

	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
	statusChangeToDependentPlanMail.setOldStatus(Constants.PlanStatus.APPROVED.name());
	statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.DEV_MGR_APPROVED.name());

	try {
	    instance.approvePlan(lUser, lPlan.getId(), "Test Comment");
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testApprovePlan1() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setLeadId(lUser.getId());

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.authenticator.getUserDetails(lUser.getId())).thenReturn(lUser);

	try {
	    instance.approvePlan(lUser, lPlan.getId(), "Test Comment");
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testApprovePlan2() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", lPlan.getMacroHeader()));
	lTaskVars.add(new TaskVariable("Approver", lUser.getDisplayName()));
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(lUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.ldapGroupConfig.getServiceDeskGroups()).thenReturn(Arrays.asList(lLDAPGroup));
	when(instance.lBPMClientUtils.assignTaskToGroup(lUser, lPlan.getProcessId(), lGroupNames.toString(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.authenticator.getUserDetails(lUser.getId())).thenReturn(lUser);
	when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));

	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
	statusChangeToDependentPlanMail.setOldStatus(Constants.PlanStatus.APPROVED.name());
	statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name());

	try {
	    instance.approvePlan(lUser, lPlan.getId(), "Test Comment");
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    // Positive case
    @Test
    public void testApproveProcessinBPMForPlan() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name());
	List<com.tsi.workflow.beans.dao.System> systemList = new ArrayList<>();
	systemList.add(lPlan.getSystemLoadList().get(0).getSystemId());
	Build lBuild = lPlan.getBuildList().get(0);
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Sumbitted", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", false));
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	DeveloperManagerService instance = new DeveloperManagerService();

	ReflectionTestUtils.setField(instance, "wssPlanIdAndUserId", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.systemLoadDAO.findPlanByQATestingStatus(lPlan.getId(), Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()))).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_CWS)).thenReturn(Arrays.asList(lBuild));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new ArrayList());
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new ArrayList());
	when(instance.lBPMClientUtils.assignTaskToGroup(lUser, lPlan.getProcessId(), lGroupNames.toString(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.ldapGroupConfig.getQAGroups()).thenReturn(Arrays.asList(lLDAPGroup));

	try {
	    instance.approveProcessinBPMForPlan(lUser, lPlan.getId(), false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    // MacroHeader type of Plan
    @Test
    public void testApproveProcessinBPMForPlan1() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setMacroHeader(Boolean.TRUE);
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name());
	List<com.tsi.workflow.beans.dao.System> systemList = new ArrayList<>();
	systemList.add(lPlan.getSystemLoadList().get(0).getSystemId());
	Build lBuild = lPlan.getBuildList().get(0);
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Sumbitted", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));

	DeveloperManagerService instance = new DeveloperManagerService();

	ReflectionTestUtils.setField(instance, "wssPlanIdAndUserId", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_CWS)).thenReturn(Arrays.asList(lBuild));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new ArrayList());
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new ArrayList());
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(lUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(lUser, lPlan.getProcessId(), lPlan.getDevManager(), new ArrayList())).thenReturn(Boolean.TRUE);
	when(instance.mailMessageFactory.getTemplate(DevManagerAssignmentMail.class)).thenReturn(mock(DevManagerAssignmentMail.class));

	DevManagerAssignmentMail devManagerAssignmentMail = new DevManagerAssignmentMail();
	devManagerAssignmentMail.setLeadName(lPlan.getLeadName());
	devManagerAssignmentMail.setPlanId(lPlan.getId());
	devManagerAssignmentMail.addToAddressUserId(lPlan.getDevManager(), Constants.MailSenderRole.DEV_MANAGER);

	try {
	    instance.approveProcessinBPMForPlan(lUser, lPlan.getId(), false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    // ByPassRegression Test Case
    @Test
    public void testApproveProcessinBPMForPlan2() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name());
	List<com.tsi.workflow.beans.dao.System> systemList = new ArrayList<>();
	systemList.add(lPlan.getSystemLoadList().get(0).getSystemId());
	Build lBuild = lPlan.getBuildList().get(0);
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Sumbitted", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", true));
	lTaskVars.add(new TaskVariable("MacroHeader", "local", "boolean", false));
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	DeveloperManagerService instance = new DeveloperManagerService();

	ReflectionTestUtils.setField(instance, "wssPlanIdAndUserId", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_CWS)).thenReturn(Arrays.asList(lBuild));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new ArrayList());
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new ArrayList());
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(lUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTaskToGroup(lUser, lPlan.getProcessId(), lGroupNames.toString(), new ArrayList())).thenReturn(Boolean.TRUE);
	when(instance.ldapGroupConfig.getSystemSupportGroups()).thenReturn(Arrays.asList(lLDAPGroup));

	try {
	    instance.approveProcessinBPMForPlan(lUser, lPlan.getId(), true);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    // Negative Case
    @Test
    public void testApproveProcessinBPMForPlan3() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.getSystemLoadList().get(0).setSystemId(DataWareHouse.systemList.get(0));
	List<com.tsi.workflow.beans.dao.System> systemList = new ArrayList<>();
	systemList.add(lPlan.getSystemLoadList().get(0).getSystemId());
	Build lBuild = lPlan.getBuildList().get(0);
	lBuild.setJobStatus("F");
	String lRepoName = DataWareHouse.RepoName;
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lPlan.getId()));
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	DeveloperManagerService instance = new DeveloperManagerService();

	ReflectionTestUtils.setField(instance, "wssPlanIdAndUserId", mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.systemLoadDAO.findPlanByQATestingStatus(lPlan.getId(), Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()))).thenReturn(Arrays.asList(lPlan.getSystemLoadList().get(0)));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_CWS)).thenReturn(Arrays.asList(lBuild));
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new ArrayList());
	when(instance.buildDAO.findLastBuild(lPlan.getId(), systemList, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new ArrayList());
	when(instance.lBPMClientUtils.assignTask(lUser, lPlan.getProcessId(), lPlan.getLeadId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.ldapGroupConfig.getQAGroups()).thenReturn(Arrays.asList(lLDAPGroup));
	when(instance.lGitUtils.getPlanRepoName("tp", lPlan.getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.sSHClientUtils.removeTag(lRepoName, Constants.TagStatus.SECURED)).thenReturn(Boolean.FALSE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lPlan.getLeadId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.FALSE);
	try {
	    instance.approveProcessinBPMForPlan(lUser, lPlan.getId(), false);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetAssignedPlans() throws Exception {
	instance.getAssignedPlans(DataWareHouse.getUser(), 0, 10, new HashMap<>(), null, null);
    }

    /**
     * Test of getMyPlanTasks method, of class DeveloperManagerService.
     */
    @Test
    public void testGetMyPlanTasks() {
	User pCurrentUser = DataWareHouse.getUser();
	Integer offset = 0;
	Integer limit = 10;
	HashMap<String, String> lOrderBy = null;
	DeveloperManagerService instance = new DeveloperManagerService();
	instance.impPlanDAO = mock(ImpPlanDAO.class);
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("planStatus", Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name());
	pFilter.put("devManager", pCurrentUser.getId());
	when(instance.impPlanDAO.count(pFilter)).thenReturn(Long.valueOf(1));
	instance.getMyPlanTasks(pCurrentUser, offset, limit, lOrderBy);
    }

    /**
     * Test of getMyPlanTasks method, of class DeveloperManagerService.
     */
    @Test
    public void testGetMyPlanTasks2() {
	User pCurrentUser = DataWareHouse.getUser();
	Integer offset = 0;
	Integer limit = 10;
	HashMap<String, String> lOrderBy = new HashMap<>();
	lOrderBy.put("id", "asc");
	DeveloperManagerService instance = new DeveloperManagerService();
	instance.impPlanDAO = mock(ImpPlanDAO.class);
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("planStatus", Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name());
	pFilter.put("devManager", pCurrentUser.getId());
	when(instance.impPlanDAO.count(pFilter)).thenReturn(Long.valueOf(1));
	instance.getMyPlanTasks(pCurrentUser, offset, limit, lOrderBy);
    }

    @Test
    public void testGetAssignedPlans2() throws Exception {
	HashMap<String, String> lOrderBy = new HashMap<>();
	lOrderBy.put("id", "asc");
	DeveloperManagerService instance = new DeveloperManagerService();
	instance.impPlanDAO = mock(ImpPlanDAO.class);
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("devManager", DataWareHouse.getUser().getId());
	when(instance.impPlanDAO.count(pFilter)).thenReturn(Long.valueOf(1));
	instance.getAssignedPlans(DataWareHouse.getUser(), 0, 10, lOrderBy, null, null);
    }

    @Test
    public void testGetAssignedPlans3() throws Exception {
	HashMap<String, String> lOrderBy = null;
	DeveloperManagerService instance = new DeveloperManagerService();
	instance.impPlanDAO = mock(ImpPlanDAO.class);
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("devManager", DataWareHouse.getUser().getId());
	when(instance.impPlanDAO.count(pFilter)).thenReturn(Long.valueOf(1));
	instance.getAssignedPlans(DataWareHouse.getUser(), 0, 10, lOrderBy, null, null);
    }

    @Test
    public void testRejectPlan() throws Exception {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	Implementation lImpl = lPlan.getImplementationList().get(0);
	lPlan.setImplementationList(Arrays.asList(lImpl));
	List<SystemLoad> lSystemLoadList = lPlan.getSystemLoadList();
	String pComments = "test";
	Boolean builFailure = false;
	String lRepoName = DataWareHouse.RepoName;
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Submitted", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	List<TaskVariable> lTaskVars1 = new ArrayList<>();
	lTaskVars1.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lPlan.getId()));
	List<TaskVariable> lTaskVars2 = new ArrayList<>();
	lTaskVars2.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_ID, lImpl.getId()));

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lSystemLoadList.get(0)));
	when(instance.lGitUtils.getPlanRepoName("tp", lPlan.getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.sSHClientUtils.removeTag(lRepoName, Constants.TagStatus.SECURED)).thenReturn(Boolean.FALSE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lPlan.getLeadId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lImpl.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(pUser, lPlan.getProcessId(), lPlan.getLeadId(), lTaskVars1)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(pUser, lImpl.getProcessId(), lImpl.getDevId(), lTaskVars2)).thenReturn(Boolean.TRUE);
	Mockito.doNothing().when(instance.rejectHelper).deleteBuilds(pUser, lPlan);
	Mockito.doNothing().when(instance.rejectHelper).updatePlanAndImplementationStatus((User) pUser, lPlan, pComments, null, Boolean.FALSE, builFailure);
	when(instance.mailMessageFactory.getTemplate(RejectMail.class)).thenReturn(mock(RejectMail.class));

	RejectMail lRejectMail = new RejectMail();
	lRejectMail.setLeadId(lPlan.getLeadId());
	lRejectMail.setBuildFailure(builFailure);
	lRejectMail.setCurrentUser((User) pUser);
	lRejectMail.setPlanId(lPlan.getId());

	try {
	    instance.rejectPlan(pUser, lPlan.getId(), pComments, builFailure, Boolean.TRUE);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testRejectPlan1() throws Exception {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setProcessId(null);
	Implementation lImpl = lPlan.getImplementationList().get(0);
	lPlan.setImplementationList(Arrays.asList(lImpl));
	List<SystemLoad> lSystemLoadList = lPlan.getSystemLoadList();
	String pComments = "test";
	Boolean builFailure = false;
	String lRepoName = DataWareHouse.RepoName;
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Submitted", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	List<TaskVariable> lTaskVars1 = new ArrayList<>();
	lTaskVars1.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lPlan.getId()));
	List<TaskVariable> lTaskVars2 = new ArrayList<>();
	lTaskVars2.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_ID, lImpl.getId()));

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lSystemLoadList.get(0)));
	when(instance.lGitUtils.getPlanRepoName("tp", lPlan.getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.sSHClientUtils.removeTag(lRepoName, Constants.TagStatus.SECURED)).thenReturn(Boolean.FALSE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lPlan.getLeadId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lImpl.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(pUser, lPlan.getProcessId(), lPlan.getLeadId(), lTaskVars1)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(pUser, lImpl.getProcessId(), lImpl.getDevId(), lTaskVars2)).thenReturn(Boolean.TRUE);
	Mockito.doNothing().when(instance.rejectHelper).deleteBuilds(pUser, lPlan);
	Mockito.doNothing().when(instance.rejectHelper).updatePlanAndImplementationStatus((User) pUser, lPlan, pComments, null, Boolean.FALSE, builFailure);
	when(instance.mailMessageFactory.getTemplate(RejectMail.class)).thenReturn(mock(RejectMail.class));

	RejectMail lRejectMail = new RejectMail();
	lRejectMail.setLeadId(lPlan.getLeadId());
	lRejectMail.setBuildFailure(builFailure);
	lRejectMail.setCurrentUser((User) pUser);
	lRejectMail.setPlanId(lPlan.getId());

	try {
	    instance.rejectPlan(pUser, lPlan.getId(), pComments, builFailure, Boolean.TRUE);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testRejectPlan2() throws Exception {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	lPlan.setProcessId(null);
	Implementation lImpl = lPlan.getImplementationList().get(0);
	lPlan.setImplementationList(new ArrayList());
	List<SystemLoad> lSystemLoadList = lPlan.getSystemLoadList();
	String pComments = "test";
	Boolean builFailure = false;
	String lRepoName = DataWareHouse.RepoName;
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable("Submitted", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("Approved", "local", "boolean", false));
	lTaskVars.add(new TaskVariable("ByPassRegression", "local", "boolean", false));
	List<TaskVariable> lTaskVars1 = new ArrayList<>();
	lTaskVars1.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lPlan.getId()));
	List<TaskVariable> lTaskVars2 = new ArrayList<>();
	lTaskVars2.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_ID, lImpl.getId()));

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.lBPMClientUtils.setTaskAsCompletedWithVariables(pUser, lPlan.getProcessId(), lTaskVars)).thenReturn(Boolean.TRUE);
	when(instance.systemLoadDAO.findByImpPlan(lPlan.getId())).thenReturn(Arrays.asList(lSystemLoadList.get(0)));
	when(instance.lGitUtils.getPlanRepoName("tp", lPlan.getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.sSHClientUtils.removeTag(lRepoName, Constants.TagStatus.SECURED)).thenReturn(Boolean.FALSE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lPlan.getLeadId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lImpl.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(pUser, lPlan.getProcessId(), lPlan.getLeadId(), lTaskVars1)).thenReturn(Boolean.TRUE);
	when(instance.lBPMClientUtils.assignTask(pUser, lImpl.getProcessId(), lImpl.getDevId(), lTaskVars2)).thenReturn(Boolean.TRUE);
	Mockito.doNothing().when(instance.rejectHelper).deleteBuilds(pUser, lPlan);
	Mockito.doNothing().when(instance.rejectHelper).updatePlanAndImplementationStatus((User) pUser, lPlan, pComments, null, Boolean.FALSE, builFailure);
	when(instance.mailMessageFactory.getTemplate(RejectMail.class)).thenReturn(mock(RejectMail.class));

	RejectMail lRejectMail = new RejectMail();
	lRejectMail.setLeadId(lPlan.getLeadId());
	lRejectMail.setBuildFailure(builFailure);
	lRejectMail.setCurrentUser((User) pUser);
	lRejectMail.setPlanId(lPlan.getId());

	try {
	    instance.rejectPlan(pUser, lPlan.getId(), pComments, builFailure, Boolean.TRUE);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetApproveStatusByPlan1() throws Exception {
	List<String> lPlanList = new ArrayList();
	lPlanList.add(DataWareHouse.getPlan().getId());
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	when(instance.getBuildDAO().getBuildInProgressPlan(Arrays.asList(DataWareHouse.getPlan().getId()), new ArrayList(Constants.BUILD_TYPE.getStagingBuildType().keySet()))).thenReturn(lPlanList);
	instance.getApproveStatusByPlan((Arrays.asList(DataWareHouse.getPlan().getId())));
    }

    @Test
    public void testGetProjectList() {
	User pUser = DataWareHouse.getUser();
	Integer pOffset = 0;
	Integer pLimit = 10;
	String filter = "test";
	LinkedHashMap pOrderBy = new LinkedHashMap();
	String searchField = "Test";
	Project lProject = new Project();

	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "projectDAO", mock(ProjectDAO.class));

	when(instance.projectDAO.findFiltered(filter, pOffset, pLimit, pOrderBy, searchField, Boolean.TRUE)).thenReturn(new ArrayList());
	when(instance.projectDAO.countBy(filter, Boolean.TRUE, searchField)).thenReturn(Long.MIN_VALUE);

	instance.getProjectList(pUser, pOffset, pLimit, filter, pOrderBy, searchField);
	instance.saveProject(pUser, lProject);
	instance.deleteProject(pUser, lProject);
	instance.updateProject(pUser, lProject);
    }

    @Test
    public void testGetProductionRepoList() {
	User pUser = DataWareHouse.getUser();
	DeveloperManagerService instance = new DeveloperManagerService();
	ReflectionTestUtils.setField(instance, "cacheClient", mock(CacheClient.class));

	when(instance.cacheClient.getFilteredRepositoryMap()).thenReturn(DataWareHouse.getFilteredRepositoryMap());

	try {
	    instance.getProductionRepoList(pUser);
	} catch (Exception ex) {
	    System.out.println(ex);
	}
    }
}
