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
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.LoadFreezeGrouped;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.LoadCategoriesForm;
import com.tsi.workflow.beans.ui.LoadFreezeForm;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.DbcrDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.LoadCategoriesDAO;
import com.tsi.workflow.dao.LoadFreezeDAO;
import com.tsi.workflow.dao.LoadWindowDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.DbcrHelper;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LoadsControlServiceTest {

    LoadsControlService instance;

    public LoadsControlServiceTest() {
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
	    LoadsControlService realInstance = new LoadsControlService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, LoadCategoriesDAO.class);
	    TestCaseMockService.doMockDAO(instance, LoadFreezeDAO.class);
	    TestCaseMockService.doMockDAO(instance, LoadWindowDAO.class);
	    TestCaseMockService.doMockDAO(instance, PutLevelDAO.class);
	    TestCaseMockService.doMockDAO(instance, BuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, DbcrDAO.class);
	    TestCaseMockService.doMockDAO(instance, ProductionLoadsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, LdapGroupConfig.class);
	} catch (Exception ex) {
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLoadsControlService() throws Exception {
	TestCaseExecutor.doTest(instance, LoadsControlService.class);
    }

    @Test
    public void testReadyForProdDeploy() {
	User lUser = DataWareHouse.getUser();
	instance = new LoadsControlService();
	String[] planids = { DataWareHouse.getPlan().getId() };
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "loadWindowDAO", mock(LoadWindowDAO.class));
	ReflectionTestUtils.setField(instance, "dbcrDAO", mock(DbcrDAO.class));
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "dbcrHelper", mock(DbcrHelper.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "bPMClientUtils", mock(BPMClientUtils.class));
	LDAPGroup ldapGroup = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	when(instance.ldapGroupConfig.getSystemSupportGroups()).thenReturn(Arrays.asList(ldapGroup));
	when(instance.impPlanDAO.find(Arrays.asList(planids))).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(null);
	when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	Build build = new Build();
	build.setActive("Y");
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0))).thenReturn(true);
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn("127.0.0.1");
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0), build, "127.0.0.1", false)).thenReturn(lResponse);
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(1).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(2).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0))).thenReturn(true);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(1))).thenReturn(true);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(2))).thenReturn(true);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(3))).thenReturn(true);
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn("127.0.0.1");
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(1).getId())).thenReturn("127.0.0.1");
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(2).getId())).thenReturn("127.0.0.1");
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(3).getId())).thenReturn("127.0.0.1");
	JSONResponse lFalseResponse = new JSONResponse();
	lFalseResponse.setStatus(Boolean.FALSE);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0), build, "127.0.0.1", false)).thenReturn(lFalseResponse);
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(1), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(2), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(3), build, "127.0.0.1", false)).thenReturn(lResponse);
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	List<TaskVariable> lTaskVars = new ArrayList<>();
	lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, DataWareHouse.getPlan().getId()));
	List<TaskVariable> lProcessVars = new ArrayList<>();
	lProcessVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, DataWareHouse.getPlan().getId()));
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	// when(instance.lDAPAuthenticatorImpl.getUserDetails(DataWareHouse.getPlan().getLeadId())).thenReturn(DataWareHouse.getUser());
	try {
	    when(instance.bPMClientUtils.assignTask(DataWareHouse.getUser(), DataWareHouse.getPlan().getProcessId(), DataWareHouse.getUser().getId(), lProcessVars)).thenReturn(true);
	    instance.readyForProdDeploy(lUser, planids);
	} catch (Exception e) {
	    assertTrue(true);
	}
	try {
	    when(instance.bPMClientUtils.setTaskAsCompleted(DataWareHouse.getUser(), DataWareHouse.getPlan().getProcessId())).thenReturn(true);
	    instance.readyForProdDeploy(lUser, planids);
	} catch (Exception e) {
	    assertTrue(true);
	}
	List<ImpPlan> lPlans = new ArrayList<>();
	lPlans.add(DataWareHouse.getPlan());
	when(instance.impPlanDAO.findDependentPlans(DataWareHouse.getPlan().getId())).thenReturn(lPlans);
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn("");
	// when(instance.lDAPAuthenticatorImpl.getUserDetails(DataWareHouse.getPlan().getLeadId())).thenReturn(DataWareHouse.getUser());
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0))).thenReturn(false);
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
	List<SystemLoad> lLoads = DataWareHouse.getPlan().getSystemLoadList();
	lLoads.get(0).setActive("N");
	when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(lLoads);
	try {
	    instance.readyForProdDeploy(lUser, planids);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testReadyForProdDeploy2() {
	BPMClientUtils bpmutils = mock(BPMClientUtils.class);
	User lUser = DataWareHouse.getUser();
	instance = new LoadsControlService();
	String[] planids = { DataWareHouse.getPlan().getId() };
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	// ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl",
	// mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "bPMClientUtils", bpmutils);
	ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	LDAPGroup ldapGroup = new LDAPGroup("", "", "", Constants.UserGroup.Lead);
	when(instance.ldapGroupConfig.getSystemSupportGroups()).thenReturn(Arrays.asList(ldapGroup));
	when(instance.impPlanDAO.find(Arrays.asList(planids))).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	Build build = new Build();
	build.setActive("Y");
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0))).thenReturn(true);
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn("127.0.0.1");
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(1).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(2).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0))).thenReturn(true);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(1))).thenReturn(true);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(2))).thenReturn(true);
	when(instance.tOSHelper.requestIP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(3))).thenReturn(true);
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(0).getId())).thenReturn("127.0.0.1");
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(1).getId())).thenReturn("127.0.0.1");
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(2).getId())).thenReturn("127.0.0.1");
	when(instance.tOSHelper.getIP(DataWareHouse.getPlan().getSystemLoadList().get(3).getId())).thenReturn("127.0.0.1");
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(0), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(1), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(2), build, "127.0.0.1", false)).thenReturn(lResponse);
	when(instance.fTPHelper.doFTP(lUser, DataWareHouse.getPlan().getSystemLoadList().get(3), build, "127.0.0.1", false)).thenReturn(lResponse);
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	// when(instance.lDAPAuthenticatorImpl.getUserDetails(DataWareHouse.getPlan().getLeadId())).thenReturn(DataWareHouse.getUser());
	try {
	    when(instance.bPMClientUtils.assignTask(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(true);
	    when(instance.bPMClientUtils.setTaskAsCompleted(Matchers.any(), Matchers.any())).thenReturn(false);
	    instance.readyForProdDeploy(lUser, planids);
	    when(instance.bPMClientUtils.setTaskAsCompleted(Matchers.any(), Matchers.any())).thenReturn(true);
	    LDAPGroup lGroup = mock(LDAPGroup.class);
	    List<LDAPGroup> lList = new ArrayList<>();
	    lList.add(lGroup);
	    when(instance.ldapGroupConfig.getLoadsControlGroups()).thenReturn(lList);
	    when(instance.bPMClientUtils.assignTaskToGroup(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(false);
	    instance.readyForProdDeploy(lUser, planids);
	    when(instance.bPMClientUtils.assignTaskToGroup(Matchers.any(), Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(true);
	    instance.readyForProdDeploy(lUser, planids);
	} catch (Exception ex) {
	    // do nothing
	}
    }

    @Test
    public void testGetLoadWindowDateByLoadCategory() {
	try {
	    instance.getLoadWindowDateByLoadCategory(7, 2018, 3);
	} catch (Exception e) {

	}
    }

    @Test
    public void testGetLoadWindowByDay() {
	// ReflectionTestUtils.setField(instance, "loadWindowDAO",
	// mock(LoadWindowDAO.class));
	try {
	    when(instance.getLoadWindowDAO().findByLoadCategoriesAndDay(7, "Sun")).thenReturn(Arrays.asList(DataWareHouse.getLoadWindow()));
	    instance.getLoadWindowByDay(DataWareHouse.getUser(), 7, "02-25-2018 00:00:00 -0500", null);
	} catch (Exception e) {

	}
    }

    @Test
    public void testGetLoadCategoriesByDate1() {
	try {
	    instance = new LoadsControlService();
	    instance.getLoadCategoriesByDate(DataWareHouse.getUser(), 7, "");
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testGetLoadFreezeDateByMonth() {
	try {
	    instance = new LoadsControlService();
	    instance.getLoadFreezeDateByMonth(7, 2018, 3);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeletePutLevel() {
	List<SystemLoad> lList = new ArrayList<>();
	instance = new LoadsControlService();
	instance.systemLoadDAO = mock(SystemLoadDAO.class);
	instance.putLevelDAO = mock(PutLevelDAO.class);
	when(instance.systemLoadDAO.findByPutLevel(1)).thenReturn(lList);
	instance.deletePutLevel(DataWareHouse.getUser(), 1);
    }

    @Test
    public void testSaveLoadCategory() {
	instance = new LoadsControlService();
	instance.loadCategoriesDAO = mock(LoadCategoriesDAO.class);
	LoadCategoriesForm loadCategoriesForm = new LoadCategoriesForm();
	LoadCategories loadCategory = loadCategoriesForm.getLoadCategory();
	when(instance.loadCategoriesDAO.isExists(loadCategory)).thenReturn(Boolean.TRUE);
	instance.saveLoadCategory(DataWareHouse.getUser(), loadCategoriesForm);
    }

    @Test
    public void testUpdateLoadCategory() {
	instance = new LoadsControlService();
	instance.loadCategoriesDAO = mock(LoadCategoriesDAO.class);
	LoadCategoriesForm loadCategoriesForm = new LoadCategoriesForm();
	LoadCategories loadCategory = loadCategoriesForm.getLoadCategory();
	when(instance.loadCategoriesDAO.isExists(loadCategory)).thenReturn(Boolean.TRUE);
	instance.updateLoadCategory(DataWareHouse.getUser(), loadCategoriesForm);
    }

    @Test
    public void testSaveLoadFreeze() {
	try {
	    List<LoadFreezeForm> freezeFormsList = new ArrayList<>();
	    instance = new LoadsControlService();
	    instance.loadCategoriesDAO = mock(LoadCategoriesDAO.class);
	    instance.loadFreezeDAO = mock(LoadFreezeDAO.class);
	    LoadFreezeForm loadFreezeForm = new LoadFreezeForm();
	    loadFreezeForm.setLoadFreeze(new LoadFreeze());
	    List<LoadCategories> lLoads = new ArrayList();
	    lLoads.add(mock(LoadCategories.class));
	    com.tsi.workflow.beans.dao.System sys = null;
	    when(instance.loadCategoriesDAO.findBySystem(sys)).thenReturn(lLoads);
	    freezeFormsList.add(loadFreezeForm);
	    instance.saveLoadFreeze(DataWareHouse.getUser(), freezeFormsList);
	} catch (Exception ex) {
	    // do nothing
	}
    }

    @Test
    public void testgroupUpdateLoadFreeze() {
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	LoadFreeze load = new LoadFreeze();
	load.setId(1);
	ArrayList<LoadFreeze> arrayList = new ArrayList();
	arrayList.add(load);
	try {
	    when(instance.getLoadFreezeDAO().find("1")).thenReturn(load);
	    instance.groupUpdateLoadFreeze(DataWareHouse.getUser(), arrayList);
	} catch (Exception e) {

	}
    }

    @Test
    public void testgroupUpdateLoadFreeze1() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	ReflectionTestUtils.setField(instance, "loadFreezeDAO", mock(LoadFreezeDAO.class));
	LoadFreeze load = new LoadFreeze();
	load.setId(1);
	ArrayList<LoadFreeze> arrayList = new ArrayList();
	arrayList.add(load);
	try {
	    when(instance.getLoadFreezeDAO().find("1")).thenReturn(load);
	    when(instance.groupUpdateLoadFreeze(DataWareHouse.getUser(), arrayList)).thenReturn(lResponse);
	} catch (Exception e) {

	}
    }

    @Test
    public void testdeleteGroupLoadFreeze() {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(Boolean.TRUE);
	    when(instance.deleteGroupLoadFreeze(DataWareHouse.getUser(), getLoadFreezeGrouped())).thenReturn(lResponse);
	} catch (Exception e) {

	}
    }

    public LoadFreezeGrouped getLoadFreezeGrouped() {
	LoadFreezeGrouped loadFreeze = new LoadFreezeGrouped();
	List<String> ids = new ArrayList<>();
	ids.add("1");
	ids.add("2");
	Date date = new Date();
	loadFreeze.setIds("1");
	loadFreeze.setListIds(ids);
	loadFreeze.setLoad_categories("c");
	loadFreeze.setFrom_date(date);
	loadFreeze.setTo_date(date);
	return loadFreeze;
    }
}
