package com.tsi.workflow.service;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.BPMConfig;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.interfaces.IGitConfig;
import com.tsi.workflow.ldap.config.LDAPGroup;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.utils.Constants;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JLayeredPane;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class QualityAssuranceServiceTest {

    QualityAssuranceService instance;

    public QualityAssuranceServiceTest() {
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
	    QualityAssuranceService realInstance = new QualityAssuranceService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, SystemLoadActionsDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, VparsDAO.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	} catch (Exception ex) {
	    Logger.getLogger(QualityAssuranceServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testQualityAssuranceService() throws Exception {
	TestCaseExecutor.doTest(instance, QualityAssuranceService.class);
    }

    @Test
    public void testUpdatePlanTestStatus() throws Exception {
	User lUser = DataWareHouse.getUser();
	String lPlanId = DataWareHouse.getPlan().getId();
	Integer lSystemId = DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getId();
	Integer[] lVparsId = new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() };
	String lFail = "FAIL";
	Integer[] lVparsIds = new Integer[] { DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getId() };
	ImpPlan lPlan = DataWareHouse.getPlan();

	List<SystemLoadActions> lLoadActions = DataWareHouse.getPlan().getSystemLoadActionsList();
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "planHelper", mock(PlanHelper.class));

	when(instance.getSystemDAO().find(lSystemId)).thenReturn(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId());
	when(instance.getVparsDAO().findByVpars(lVparsId)).thenReturn(Arrays.asList(DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId()));
	when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(new ArrayList());
	PlanHelper lPlanHelper = instance.getPlanHelper();
	Mockito.doThrow(Exception.class).doNothing().when(lPlanHelper).sendMailNotificationQAFunTesterPassFail(lPlan, lUser, "REJECTED");

	String rejectReason = DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getSystemId().getName() + "-" + DataWareHouse.getPlan().getSystemLoadActionsList().get(0).getVparId().getName() + " " + Constants.REJECT_REASON.QA_FAIL.getValue();
	Mockito.doThrow(Exception.class).doNothing().when(instance.rejectHelper).rejectPlan(lUser, lPlanId, rejectReason, Constants.AUTOREJECT_COMMENT.REJECT.getValue(), Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
	try {
	    instance.updatePlanTestStatus(lUser, lPlanId, lSystemId, lVparsId, lFail, lVparsIds);
	} catch (Exception ex) {
	    System.out.println("" + ex);
	}
    }

    @Test
    public void testUpdatePlanTestStatus1() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	String lPlanId = lPlan.getId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Integer lSystemId = lPlan.getSystemLoadActionsList().get(0).getSystemId().getId();
	Integer[] lVparsId = new Integer[] { lPlan.getSystemLoadActionsList().get(0).getVparId().getId() };
	String lStatus = "PASS";
	Integer[] lVparsIds = new Integer[] { lPlan.getSystemLoadActionsList().get(0).getVparId().getId() };
	List<SystemLoadActions> lLoadActions = lPlan.getSystemLoadActionsList();
	String lByPassedType = DataWareHouse.getPlan().getPlanStatus().equals(Constants.PlanStatus.APPROVED.name()) ? Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name() : Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name();
	Constants.PlanStatus lPlanStatus = Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING;
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name());
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	QualityAssuranceService instance = new QualityAssuranceService();

	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));

	Mockito.reset(instance.mailMessageFactory);

	when(instance.impPlanDAO.find(lPlanId)).thenReturn(lPlan);
	when(instance.systemLoadActionsDAO.findByPlanId(lPlanId)).thenReturn(lPlan.getSystemLoadActionsList());
	when(instance.getSystemDAO().find(lSystemId)).thenReturn(lPlan.getSystemLoadActionsList().get(0).getSystemId());
	when(instance.getVparsDAO().findByVpars(lVparsId)).thenReturn(Arrays.asList(lPlan.getSystemLoadActionsList().get(0).getVparId()));
	when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lPlan.getSystemLoadList());
	when(instance.getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	when(instance.getSystemLoadActionsDAO().findBy(new String[] { lPlanId }, lVparsIds)).thenReturn(lLoadActions.stream().filter(lLoadSet -> lLoadSet.getTestStatus() != null && lLoadSet.getTestStatus().equals(lStatus)).collect(Collectors.toList()));
	when(instance.getSystemLoadDAO().findPlanByQATestingStatus(DataWareHouse.getPlan().getId(), Arrays.asList("NONE", lByPassedType))).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	when(instance.lGitUtils.getPlanRepoName("tp", lPlanId.toLowerCase())).thenReturn(lRepoName);
	when(instance.lGitUtils.getAllBranchList("tp", lPlanId)).thenReturn(lBranchList);
	when(instance.sSHClientUtils.addImplementationTag(lRepoName, lPlanStatus, lBranchList)).thenReturn(Boolean.TRUE);
	when(instance.getSystemLoadDAO().findPlanByQATestingStatus(lPlanId, Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()))).thenReturn(lPlan.getSystemLoadList());
	when(instance.getBPMClientUtils().assignTaskToGroup(lUser, DataWareHouse.getPlan().getProcessId(), lGroupNames.toString(), new ArrayList())).thenReturn(Boolean.TRUE);
	when(instance.getLdapGroupConfig().getSystemSupportGroups()).thenReturn(Arrays.asList(lLDAPGroup));

	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
	statusChangeToDependentPlanMail.setOldStatus(Constants.PlanStatus.APPROVED.name());
	statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name());

	try {
	    instance.updatePlanTestStatus(lUser, lPlanId, lSystemId, lVparsId, lStatus, lVparsIds);
	} catch (Exception ex) {
	}
    }

    @Test
    public void testUpdatePlanTestStatus2() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name());
	String lPlanId = lPlan.getId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Integer lSystemId = lPlan.getSystemLoadActionsList().get(0).getSystemId().getId();
	Integer[] lVparsId = new Integer[] { lPlan.getSystemLoadActionsList().get(0).getVparId().getId() };
	String lStatus = "PASS";
	Integer[] lVparsIds = new Integer[] { lPlan.getSystemLoadActionsList().get(0).getVparId().getId() };
	List<SystemLoadActions> lLoadActions = lPlan.getSystemLoadActionsList();
	String lByPassedType = lPlan.getPlanStatus().equals(Constants.PlanStatus.APPROVED.name()) ? Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name() : Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name();
	Constants.PlanStatus lPlanStatus = Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING;
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name());
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	QualityAssuranceService instance = new QualityAssuranceService();

	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));

	Mockito.reset(instance.mailMessageFactory);

	when(instance.impPlanDAO.find(lPlanId)).thenReturn(lPlan);
	when(instance.systemLoadActionsDAO.findByPlanId(lPlanId)).thenReturn(lPlan.getSystemLoadActionsList());
	when(instance.getSystemDAO().find(lSystemId)).thenReturn(lPlan.getSystemLoadActionsList().get(0).getSystemId());
	when(instance.getVparsDAO().findByVpars(lVparsId)).thenReturn(Arrays.asList(lPlan.getSystemLoadActionsList().get(0).getVparId()));
	when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lPlan.getSystemLoadList());
	when(instance.getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	when(instance.getSystemLoadActionsDAO().findBy(new String[] { lPlanId }, lVparsIds)).thenReturn(lLoadActions.stream().filter(lLoadSet -> lLoadSet.getTestStatus() != null && lLoadSet.getTestStatus().equals(lStatus)).collect(Collectors.toList()));
	when(instance.getSystemLoadDAO().findPlanByQATestingStatus(DataWareHouse.getPlan().getId(), Arrays.asList("NONE", lByPassedType))).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	when(instance.lGitUtils.getPlanRepoName("tp", lPlanId.toLowerCase())).thenReturn(lRepoName);
	when(instance.lGitUtils.getAllBranchList("tp", lPlanId)).thenReturn(lBranchList);
	when(instance.sSHClientUtils.addImplementationTag(lRepoName, lPlanStatus, lBranchList)).thenReturn(Boolean.TRUE);
	when(instance.getSystemLoadDAO().findPlanByQATestingStatus(lPlanId, Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()))).thenReturn(lPlan.getSystemLoadList());
	when(instance.getBPMClientUtils().assignTaskToGroup(lUser, DataWareHouse.getPlan().getProcessId(), lGroupNames.toString(), new ArrayList())).thenReturn(Boolean.TRUE);
	when(instance.getLdapGroupConfig().getSystemSupportGroups()).thenReturn(Arrays.asList(lLDAPGroup));

	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
	statusChangeToDependentPlanMail.setOldStatus(Constants.PlanStatus.APPROVED.name());
	statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name());

	try {
	    instance.updatePlanTestStatus(lUser, lPlanId, lSystemId, lVparsId, lStatus, lVparsIds);
	} catch (Exception ex) {
	}
    }

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	User user0 = new User();
	Integer integer0 = JLayeredPane.POPUP_LAYER;
	Integer[] integerArray0 = new Integer[8];
	try {
	    qualityAssuranceService0.updatePlanTestStatus(user0, "QW gvl,@#.gio5", integer0, integerArray0, "2.16.840.1.113730.3.4.16", integerArray0);
	    fail("Expecting exception: RuntimeException");

	} catch (RuntimeException e) {
	    //
	    // Unable to update Implementation Plan Test Status
	    //

	}
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ActivityLogDAO activityLogDAO0 = new ActivityLogDAO();
	SSHClientUtils sSHClientUtils0 = new SSHClientUtils((IGitConfig) null);
	qualityAssuranceService0.sSHClientUtils = sSHClientUtils0;
	SSHClientUtils sSHClientUtils1 = qualityAssuranceService0.getsSHClientUtils();
	assertSame(sSHClientUtils1, sSHClientUtils0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ActivityLogDAO activityLogDAO0 = new ActivityLogDAO();
	VparsDAO vparsDAO0 = new VparsDAO();
	SessionFactory sessionFactory1 = activityLogDAO0.getSessionFactory();
	qualityAssuranceService0.vparsDAO = vparsDAO0;
	VparsDAO vparsDAO1 = qualityAssuranceService0.getVparsDAO();
	assertSame(vparsDAO1, vparsDAO0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ActivityLogDAO activityLogDAO0 = new ActivityLogDAO();
	SystemLoadDAO systemLoadDAO0 = new SystemLoadDAO();
	SessionFactory sessionFactory1 = activityLogDAO0.getSessionFactory();
	qualityAssuranceService0.systemLoadDAO = systemLoadDAO0;
	SystemLoadDAO systemLoadDAO1 = qualityAssuranceService0.getSystemLoadDAO();
	assertSame(systemLoadDAO1, systemLoadDAO0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	LdapGroupConfig ldapGroupConfig0 = new LdapGroupConfig();
	qualityAssuranceService0.ldapGroupConfig = ldapGroupConfig0;
	LdapGroupConfig ldapGroupConfig1 = qualityAssuranceService0.getLdapGroupConfig();
	assertSame(ldapGroupConfig1, ldapGroupConfig0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ImpPlanDAO impPlanDAO0 = qualityAssuranceService0.getImpPlanDAO();
	assertNull(impPlanDAO0);
    }

    // @Test(timeout = 4000)
    public void test07() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	BPMConfig bPMConfig0 = new BPMConfig();
	BPMClientUtils bPMClientUtils0 = new BPMClientUtils(bPMConfig0);
	BPMClientUtils bPMClientUtils1 = qualityAssuranceService0.getBPMClientUtils();
	assertSame(bPMClientUtils1, bPMClientUtils0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ActivityLogDAO activityLogDAO0 = qualityAssuranceService0.getActivityLogDAO();
	assertNull(activityLogDAO0);
    }

    // @Test(timeout = 4000)
    public void test09() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ImpPlanDAO impPlanDAO0 = new ImpPlanDAO();
	ImpPlanDAO impPlanDAO1 = qualityAssuranceService0.getImpPlanDAO();
	assertSame(impPlanDAO1, impPlanDAO0);
    }

    // @Test(timeout = 4000)
    public void test10() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	ActivityLogDAO activityLogDAO0 = new ActivityLogDAO();
	ActivityLogDAO activityLogDAO1 = qualityAssuranceService0.getActivityLogDAO();
	assertSame(activityLogDAO1, activityLogDAO0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	MailMessageFactory mailMessageFactory0 = qualityAssuranceService0.getMailMessageFactory();
	assertNull(mailMessageFactory0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	VparsDAO vparsDAO0 = qualityAssuranceService0.getVparsDAO();
	assertNull(vparsDAO0);
    }

    // @Test(timeout = 4000)
    public void test13() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	GITConfig gITConfig1 = qualityAssuranceService0.getGITConfig();
	assertNull(gITConfig1.getSSHKeysPath());
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	BPMClientUtils bPMClientUtils0 = qualityAssuranceService0.getBPMClientUtils();
	assertNull(bPMClientUtils0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	SSHClientUtils sSHClientUtils0 = qualityAssuranceService0.getsSHClientUtils();
	assertNull(sSHClientUtils0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	JGitClientUtils jGitClientUtils0 = qualityAssuranceService0.getJGitClientUtils();
	assertNull(jGitClientUtils0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	SystemDAO systemDAO0 = qualityAssuranceService0.getSystemDAO();
	assertNull(systemDAO0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	SystemLoadDAO systemLoadDAO0 = qualityAssuranceService0.getSystemLoadDAO();
	assertNull(systemLoadDAO0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	RejectHelper rejectHelper0 = qualityAssuranceService0.getRejectHelper();
	assertNull(rejectHelper0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	QualityAssuranceService qualityAssuranceService0 = new QualityAssuranceService();
	LdapGroupConfig ldapGroupConfig0 = qualityAssuranceService0.getLdapGroupConfig();
	assertNull(ldapGroupConfig0);
    }

    @Test
    public void testUpdatePlanTestStatus3() throws Exception {
	User lUser = DataWareHouse.getUser();
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.DEPLOYED_IN_QA_FUNCTIONAL.name());
	String lPlanId = lPlan.getId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Integer lSystemId = lPlan.getSystemLoadActionsList().get(0).getSystemId().getId();
	Integer[] lVparsId = new Integer[] { lPlan.getSystemLoadActionsList().get(0).getVparId().getId() };
	String lStatus = "FAIL";
	Integer[] lVparsIds = new Integer[] { lPlan.getSystemLoadActionsList().get(0).getVparId().getId() };
	List<SystemLoadActions> lLoadActions = lPlan.getSystemLoadActionsList();
	String lByPassedType = lPlan.getPlanStatus().equals(Constants.PlanStatus.APPROVED.name()) ? Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name() : Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_FUNCTIONAL_TESTING.name();
	Constants.PlanStatus lPlanStatus = Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING;
	lPlan.setQaBypassStatus(Constants.PLAN_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name());
	StringBuilder lGroupNames = new StringBuilder();
	LDAPGroup lLDAPGroup = new LDAPGroup("a", "b", "c", Constants.UserGroup.DevManager);
	List<LDAPGroup> lGroups = Arrays.asList(lLDAPGroup);
	lGroups.forEach((lLDAPGroup1) -> {
	    lGroupNames.append(lLDAPGroup1.getLdapGroupName()).append(",");
	});
	lGroupNames.setLength(lGroupNames.length() - 1);

	QualityAssuranceService instance = new QualityAssuranceService();

	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	ReflectionTestUtils.setField(instance, "planHelper", mock(PlanHelper.class));

	Mockito.reset(instance.mailMessageFactory);

	when(instance.impPlanDAO.find(lPlanId)).thenReturn(lPlan);
	when(instance.systemLoadActionsDAO.findByPlanId(lPlanId)).thenReturn(lPlan.getSystemLoadActionsList());
	when(instance.getSystemDAO().find(lSystemId)).thenReturn(lPlan.getSystemLoadActionsList().get(0).getSystemId());
	when(instance.getVparsDAO().findByVpars(lVparsId)).thenReturn(Arrays.asList(lPlan.getSystemLoadActionsList().get(0).getVparId()));
	when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lPlan.getSystemLoadList());
	when(instance.getMailMessageFactory().getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(mock(StatusChangeToDependentPlanMail.class));
	when(instance.getSystemLoadActionsDAO().findBy(new String[] { lPlanId }, lVparsIds)).thenReturn(lLoadActions.stream().filter(lLoadSet -> lLoadSet.getTestStatus() != null && lLoadSet.getTestStatus().equals(lStatus)).collect(Collectors.toList()));
	when(instance.getSystemLoadDAO().findPlanByQATestingStatus(DataWareHouse.getPlan().getId(), Arrays.asList("NONE", lByPassedType))).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	when(instance.lGitUtils.getPlanRepoName("tp", lPlanId.toLowerCase())).thenReturn(lRepoName);
	when(instance.lGitUtils.getAllBranchList("tp", lPlanId)).thenReturn(lBranchList);
	when(instance.sSHClientUtils.addImplementationTag(lRepoName, lPlanStatus, lBranchList)).thenReturn(Boolean.TRUE);
	when(instance.getSystemLoadDAO().findPlanByQATestingStatus(lPlanId, Arrays.asList(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name(), Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()))).thenReturn(lPlan.getSystemLoadList());
	when(instance.getBPMClientUtils().assignTaskToGroup(lUser, DataWareHouse.getPlan().getProcessId(), lGroupNames.toString(), new ArrayList())).thenReturn(Boolean.TRUE);
	when(instance.getLdapGroupConfig().getSystemSupportGroups()).thenReturn(Arrays.asList(lLDAPGroup));

	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail.addToAddressUserId(lPlan.getLeadId(), Constants.MailSenderRole.LEAD);
	statusChangeToDependentPlanMail.setImpPlanId(lPlan.getId());
	statusChangeToDependentPlanMail.setOldStatus(Constants.PlanStatus.APPROVED.name());
	statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.PASSED_FUNCTIONAL_TESTING.name());

	try {
	    instance.updatePlanTestStatus(lUser, lPlanId, lSystemId, lVparsId, lStatus, lVparsIds);
	} catch (Exception ex) {
	}
    }
}
