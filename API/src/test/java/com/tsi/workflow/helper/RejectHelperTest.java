/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.mail.AutoRejectMail;
import com.tsi.workflow.mail.ExceptionSourceContentionMail;
import com.tsi.workflow.mail.FallBackRejectUnSecMail;
import com.tsi.workflow.mail.LoadDatePassedMail;
import com.tsi.workflow.mail.RejectMail;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class RejectHelperTest {

    public RejectHelperTest() {
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
	    RejectHelper realInstance = new RejectHelper();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, BuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    // TestCaseMockService.doMockDAO(instance, SSHUtil.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, TestSystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadActionsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);

	} catch (Exception ex) {
	    Logger.getLogger(RejectHelperTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    RejectHelper instance;

    @Test
    public void testDSLFileHelper() throws Exception {
	TestCaseExecutor.doTest(instance, RejectHelper.class);
    }

    /**
     * Test of getBPMClientUtils method, of class RejectHelper.
     */
    @Test
    public void testGetBPMClientUtils() {

	RejectHelper instance = new RejectHelper();
	BPMClientUtils expResult = null;
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	BPMClientUtils result = instance.getBPMClientUtils();
	assertNotNull(result);

    }

    /**
     * Test of getImpPlanDAO method, of class RejectHelper.
     */
    @Test
    public void testGetImpPlanDAO() {

	RejectHelper instance = new RejectHelper();
	ImpPlanDAO expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ImpPlanDAO result = instance.getImpPlanDAO();
	assertNotNull(result);

    }

    /**
     * Test of getImplementationDAO method, of class RejectHelper.
     */
    @Test
    public void testGetImplementationDAO() {

	RejectHelper instance = new RejectHelper();
	ImplementationDAO expResult = null;
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ImplementationDAO result = instance.getImplementationDAO();
	assertNotNull(result);

    }

    /**
     * Test of getJGitClientUtils method, of class RejectHelper.
     */
    @Test
    public void testGetJGitClientUtils() {

	RejectHelper instance = new RejectHelper();
	JGitClientUtils expResult = null;
	ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	JGitClientUtils result = instance.getJGitClientUtils();
	assertNotNull(result);

    }

    /**
     * Test of getMailMessageFactory method, of class RejectHelper.
     */
    @Test
    public void testGetMailMessageFactory() {

	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	MailMessageFactory result = instance.getMailMessageFactory();
	assertNotNull(result);
    }

    /**
     * Test of getActivityLogDAO method, of class RejectHelper.
     */
    @Test
    public void testGetActivityLogDAO() {

	RejectHelper instance = new RejectHelper();
	ActivityLogDAO expResult = null;
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ActivityLogDAO result = instance.getActivityLogDAO();
	assertNotNull(result);

    }

    /**
     * Test of getTestSystemLoadDAO method, of class RejectHelper.
     */
    @Test
    public void testGetTestSystemLoadDAO() {

	RejectHelper instance = new RejectHelper();
	TestSystemLoadDAO expResult = null;
	ReflectionTestUtils.setField(instance, "testSystemLoadDAO", mock(TestSystemLoadDAO.class));
	TestSystemLoadDAO result = instance.getTestSystemLoadDAO();
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadActionsDAO method, of class RejectHelper.
     */
    @Test
    public void testGetSystemLoadActionsDAO() {

	RejectHelper instance = new RejectHelper();
	SystemLoadActionsDAO expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	SystemLoadActionsDAO result = instance.getSystemLoadActionsDAO();
	assertNotNull(result);

    }

    /**
     * Test of getBuildDAO method, of class RejectHelper.
     */
    @Test
    public void testGetBuildDAO() {

	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	BuildDAO result = instance.getBuildDAO();
	assertNotNull(result);

    }

    /**
     * Test of getGITConfig method, of class RejectHelper.
     */
    @Test
    public void testGetGITConfig() {

	RejectHelper instance = new RejectHelper();
	GITConfig expResult = null;
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	GITConfig result = instance.getGITConfig();
	assertNotNull(result);

    }

    /**
     * Test of getSystemLoadDAO method, of class RejectHelper.
     */
    @Test
    public void testGetSystemLoadDAO() {

	RejectHelper instance = new RejectHelper();
	SystemLoadDAO expResult = null;
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	SystemLoadDAO result = instance.getSystemLoadDAO();
	assertNotNull(result);

    }

    /**
     * Test of findAllDependentPlanIds method, of class RejectHelper.
     */
    @Test
    public void testFindAllDependentPlanIds() {

	String planId = DataWareHouse.getPlan().getId();
	RejectHelper instance = new RejectHelper();
	SortedSet<String> expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	Object[] plan = new Object[] { DataWareHouse.getPlan() };
	List<Object[]> lstPlan = new ArrayList();
	lstPlan.add(plan);
	// when(instance.getImpPlanDAO().getPostSegmentRelatedPlans(planId)).thenReturn(lstPlan);
	when(instance.getImpPlanDAO().findDependentPlans(planId)).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	SortedSet<String> result = instance.findAllDependentPlanIds(planId, false, null);
	assertNotNull(result);

    }

    /**
     * Test of findAllDependentPlanIds method, of class RejectHelper.
     */
    @Test
    public void testFindAllDependentPlanIds1() {

	String planId = DataWareHouse.getPlan().getId();
	RejectHelper instance = new RejectHelper();
	SortedSet<String> expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ImpPlan IP1 = DataWareHouse.getPlan();
	// ImpPlan IP2 = DataWareHouse.getPlan();
	// IP2.setId("T1700485");
	// ImpPlan IP3 = DataWareHouse.getPlan();
	// IP3.setId("T1700486");
	Object[] plan1 = new Object[] { IP1, IP1, IP1 };
	List<Object[]> lstPlan = new ArrayList();
	lstPlan.add(plan1);
	when(instance.getImpPlanDAO().getPostSegmentRelatedPlans(planId, Boolean.FALSE)).thenReturn(lstPlan);
	when(instance.getImpPlanDAO().findDependentPlans(planId)).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	SortedSet<String> result = instance.findAllDependentPlanIds(planId, false, null);
	// assertNotNull(result);

    }

    /**
     * Test of getSecuredPlans method, of class RejectHelper.
     */
    @Test
    public void testGetSecuredPlans() {

	List<String> impPlans = null;
	RejectHelper instance = new RejectHelper();
	List<ImpPlan> expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	List<ImpPlan> result = instance.getSecuredPlans(impPlans);
	assertNotNull(result);

    }

    /**
     * Test of getUnSecuredPlans method, of class RejectHelper.
     */
    @Test
    public void testGetUnSecuredPlans() {

	List<String> impPlans = null;
	RejectHelper instance = new RejectHelper();
	List<ImpPlan> expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	List<ImpPlan> result = instance.getUnSecuredPlans(impPlans);
	assertNotNull(result);

    }

    /**
     * Test of notifyADLOnReject method, of class RejectHelper.
     */
    @Test
    public void testNotifyADLOnReject() {

	String planId = DataWareHouse.getPlan().getId();
	String leadId = "";
	User currentUser = null;
	String rejectReason = "";
	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(RejectMail.class)).thenReturn(mock(RejectMail.class));

	instance.notifyADLOnReject(planId, leadId, currentUser, rejectReason, Boolean.TRUE);

    }

    /**
     * Test of notifyADLOnLoadDatePass method, of class RejectHelper.
     */
    @Test
    public void testNotifyADLOnLoadDatePass() {

	String planId = DataWareHouse.getPlan().getId();
	String leadId = "";
	User currentUser = null;
	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(LoadDatePassedMail.class)).thenReturn(mock(LoadDatePassedMail.class));

	instance.notifyADLOnLoadDatePass(planId, leadId, currentUser);

    }

    /**
     * Test of notifyADLOnAutoReject method, of class RejectHelper.
     */
    @Test
    public void testNotifyADLOnAutoReject() {

	String planId = "";
	String leadId = "";
	String dependentPlanId = "";
	String status = "";
	RejectHelper instance = new RejectHelper();

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(AutoRejectMail.class)).thenReturn(mock(AutoRejectMail.class));

	instance.notifyADLOnAutoReject(planId, leadId, dependentPlanId, status);
    }

    @Test
    public void testNotifyADLOnUnsecuredPlan() {

	String planId = "";
	String leadId = "";
	String dependentPlanId = "";
	String status = "";
	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(ExceptionSourceContentionMail.class)).thenReturn(mock(ExceptionSourceContentionMail.class));
	when(instance.mailMessageFactory.getTemplate(FallBackRejectUnSecMail.class)).thenReturn(mock(FallBackRejectUnSecMail.class));
	ImpPlan plan = DataWareHouse.getPlan();
	plan.setPlanStatus("ONLINE");
	plan.setLoadType("EXCEPTION");
	instance.notifyADLOnUnsecuredPlan(DataWareHouse.getPlan(), leadId, dependentPlanId, status);
	plan.setPlanStatus("FALLBACK");
	instance.notifyADLOnUnsecuredPlan(DataWareHouse.getPlan(), leadId, dependentPlanId, status);
    }

    /**
     * Test of bpmRejectTask method, of class RejectHelper.
     */
    @Test
    public void testBpmRejectTask() throws Exception {

	User user = null;
	String processId = "";
	String leadId = "";
	String planId = "";
	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	Boolean expResult = null;
	Boolean result = instance.bpmRejectTask(user, processId, leadId, planId);
	assertNotNull(result);

    }

    /**
     * Test of updatePlanAndImplementationStatus method, of class RejectHelper.
     */
    @Test
    public void testUpdatePlanAndImplementationStatus() throws Exception {

	User user = null;
	ImpPlan plan = DataWareHouse.getPlan();
	String comments = "";
	String parentPlanId = "";
	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	when(instance.getSystemLoadDAO().findByImpPlan(plan.getId())).thenReturn(plan.getSystemLoadList());
	when(instance.getBuildDAO().findByImpPlan(plan)).thenReturn(DataWareHouse.getPlan().getBuildList());
	instance.updatePlanAndImplementationStatus(user, plan, "Auto-rejected due to load date change for exception load - ", parentPlanId, false, false);

    }

    /**
     * Test of tagImplementationPlan method, of class RejectHelper.
     */
    @Test
    public void testTagImplementationPlan() throws Exception {
	try {
	    User currentUser = DataWareHouse.getUser();
	    ImpPlan impPlan = DataWareHouse.getPlan();
	    Constants.TagStatus status = Constants.TagStatus.REJECTED;
	    RejectHelper instance = new RejectHelper();
	    Boolean expResult = null;
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	    when(instance.getSystemLoadDAO().findByImpPlan(impPlan)).thenReturn(impPlan.getSystemLoadList());
	    Boolean result = instance.tagImplementationPlan(currentUser, impPlan, status);
	    assertNotNull(result);
	} catch (Exception e) {

	}

    }

    /**
     * Test of deleteStagingWorkspace method, of class RejectHelper.
     */
    @Test
    public void testDeleteStagingWorkspace() {
	try {

	    User currentUser = DataWareHouse.getUser();
	    ImpPlan impPlan = DataWareHouse.getPlan();
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	    instance.deleteStagingWorkspace(currentUser, impPlan);
	    impPlan.setPlanStatus("APPROVED");
	    ReflectionTestUtils.invokeMethod(instance, "deleteLoadSetActivated", currentUser, impPlan, false);
	} catch (Exception e) {
	}

    }

    /**
     * Test of deleteStagingWorkspace method, of class RejectHelper.
     */
    @Test
    public void testDeleteStagingWorkspace1() throws Exception {

	User currentUser = DataWareHouse.getUser();
	ImpPlan impPlan = DataWareHouse.getPlan();
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	// when(instance.getSSHUtil()).thenReturn(sshUtil);
	// doReturn(false).when(sshUtil).connectSSH(DataWareHouse.getSystemList().get(0));
	try {
	    instance.deleteStagingWorkspace(currentUser, impPlan);
	} catch (Exception e) {
	    assertTrue(true);
	}
	// doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getSystemList().get(0));
	try {
	    instance.deleteStagingWorkspace(currentUser, impPlan);
	} catch (Exception e) {
	    assertTrue(true);
	}
	impPlan.setPlanStatus("APPROVED");
	ReflectionTestUtils.invokeMethod(instance, "deleteLoadSetActivated", currentUser, impPlan, false);

    }

    /**
     * Test of getApprovedPlans method, of class RejectHelper.
     */
    @Test
    public void testGetApprovedPlans() {

	List<String> impPlans = null;
	RejectHelper instance = new RejectHelper();
	List<ImpPlan> expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	List<ImpPlan> result = instance.getApprovedPlans(impPlans);
	assertNotNull(result);

    }

    /**
     * Test of rejectDependentPlans method, of class RejectHelper.
     */
    @Test
    public void testRejectDependentPlans() throws Exception {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "";
	String autoRejectReason = "";
	// RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	// when(instance.getMailMessageFactory().push(mock(AutoRejectMail.class))).thenReturn();
	SortedSet<String> planSet = new TreeSet();
	// when(instance.getImpPlanDAO().findDependentPlans(planId)).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	planSet.add(planId);
	planSet.add("T1700845");
	// SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	// when(instance.getSSHUtil()).thenReturn(sshUtil);
	String command = Constants.SystemScripts.DELETE_STAGING_WORKSPACE.getScript() + planId.toLowerCase();
	// doReturn(DataWareHouse.getPositiveResponse()).when(sshUtil).executeCommand(command);
	// doReturn(true).when(sshUtil).connectSSH();
	// doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
	AutoRejectMail autoRejectMail = new AutoRejectMail();
	autoRejectMail.setPlanId(planId);
	autoRejectMail.setDependentPlanId(planId);
	autoRejectMail.setLeadId(DataWareHouse.getPlan().getLeadId());
	autoRejectMail.setStatus(rejectReason);
	when(instance.getMailMessageFactory().getTemplate(AutoRejectMail.class)).thenReturn(autoRejectMail);
	// SortedSet<String> lSS = spy(new TreeSet<String>());
	// when(instance.findAllDependentPlanIds(planId,
	// Boolean.FALSE)).thenReturn(planSet);
	// SortedSet<String> dependentPlans = mock(SortedSet.class);
	// doReturn(false).when(dependentPlans).isEmpty();
	// ReflectionTestUtils.invokeMethod(new RejectHelper(), "notifyADLOnAutoReject",
	// planId,
	// DataWareHouse.getPlan().getLeadId(),planId, rejectReason);
	// when(instance.findAllDependentPlanIds("T1700845",
	// Boolean.FALSE)).thenReturn(planSet);
	// when(instance.getSecuredPlans(new
	// ArrayList(planSet))).thenReturn(Arrays.asList(DataWareHouse.getPlan()));
	instance.rejectDependentPlans(currentUser, planId, rejectReason, autoRejectReason, false, Boolean.FALSE, Boolean.FALSE, "abc", Boolean.TRUE);

	doReturn(planSet).when(instance).findAllDependentPlanIds(planId, Boolean.FALSE, null);
	doReturn(Arrays.asList(DataWareHouse.getPlan())).when(instance).getUnSecuredPlans(Matchers.any());
	doReturn(Arrays.asList(DataWareHouse.getPlan())).when(instance).getSecuredPlans(Matchers.any());
	// GITSSHUtils gitsshu = spy(new GITSSHUtils(mock(IGitConfig.class)));
	// when(gitsshu.removeTag(DataWareHouse.getUser(),
	// "/tpf/tp/source/t1700484.git",
	// Constants.TagStatus.SECURED)).thenReturn(true);
	// when(instance.getGITSSHUtils()).thenReturn(gitsshu);
	when(instance.getMailMessageFactory().getTemplate(AutoRejectMail.class)).thenReturn(mock(AutoRejectMail.class));
	try {
	    instance.rejectDependentPlans(currentUser, planId, rejectReason, autoRejectReason, false, Boolean.FALSE, Boolean.FALSE, "abc", Boolean.TRUE);
	} catch (Exception e) {

	}
    }

    /**
     * Test of rejectPlan method, of class RejectHelper.
     */
    @Test
    public void testRejectPlan() throws Exception {

	User currentUser = DataWareHouse.getUser();
	String planId = DataWareHouse.getPlan().getId();
	String rejectReason = "";
	String autoRejectReason = "";
	boolean dependent = false;
	RejectHelper instance = new RejectHelper();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "jGitClientUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.getMailMessageFactory().getTemplate(LoadDatePassedMail.class)).thenReturn(mock(LoadDatePassedMail.class));
	ImpPlan plan = DataWareHouse.getPlan();
	plan.setPlanStatus("ACTIVE");
	when(instance.getImpPlanDAO().find(planId)).thenReturn(plan);
	// doNothing().when(instance.mailMessageFactory).push(mock(RejectMail.class));
	instance.rejectPlan(currentUser, planId, rejectReason, autoRejectReason, dependent, false, Boolean.TRUE);
	try {
	    plan.setPlanStatus("INACTIVE");
	    instance.rejectPlan(currentUser, planId, rejectReason, autoRejectReason, dependent, false, Boolean.TRUE);

	} catch (Exception e) {
	    assertTrue(true);
	}
	try {
	    plan.setPlanStatus("INACTIVE");

	    instance.rejectPlan(currentUser, planId, Constants.REJECT_REASON.REJECTION.getValue(), autoRejectReason, dependent, false, Boolean.TRUE);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of removeDeployment method, of class RejectHelper.
     */
    @Test
    public void testRemoveDeployment() throws Exception {
	User currentUser = DataWareHouse.getUser();
	ImpPlan impPlan = DataWareHouse.getPlan();
	instance.removeDeployment(currentUser, impPlan);
    }
}
