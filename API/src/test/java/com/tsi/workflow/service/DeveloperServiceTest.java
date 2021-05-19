/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.service;

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
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.PutLevelDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.mail.CheckInMail;
import com.tsi.workflow.mail.CheckoutDeleteMail;
import com.tsi.workflow.mail.CheckoutSourceContentMail;
import com.tsi.workflow.mail.DeveloperReassignmentMail;
import com.tsi.workflow.mail.ImplementationStatusCompletionMail;
import com.tsi.workflow.mail.NewTargetSystemMail;
import com.tsi.workflow.mail.PeerReviewRequestMail;
import com.tsi.workflow.mail.ReviewerAssignmentMail;
import com.tsi.workflow.mail.ReviewerReassignmentMail;
import com.tsi.workflow.mail.UnsecuredCheckoutSourceContentMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.utils.SequenceGenerator;
import com.workflow.ssh.SSHClientUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class DeveloperServiceTest {

    DeveloperService instance;

    public DeveloperServiceTest() {
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
	    DeveloperService realInstance = new DeveloperService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, PutLevelDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);

	    TestCaseMockService.doMockDAO(instance, SequenceGenerator.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, GitBlitClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, SSHClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, LDAPAuthenticatorImpl.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    // TestCaseMockService.doMockDAO(instance, SSHUtil.class);

	} catch (Exception ex) {
	    Logger.getLogger(DeveloperService.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperService() throws Exception {
	TestCaseExecutor.doTest(instance, DeveloperService.class);
    }

    /**
     * Test of saveImplementation method, of class DeveloperService.
     */
    @Test
    public void testSaveImplementation() throws IOException {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.getPlan().getImplementationList().get(0);
	DeveloperService instance = new DeveloperService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	when(instance.impPlanDAO.find(pImplementation.getPlanId().getId())).thenReturn(DataWareHouse.getPlan());
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lGenerator", mock(SequenceGenerator.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.lBPMClientUtils.createDeveloperProcess(pUser, "")).thenReturn(null);
	when(instance.authenticator.getUserDetails(pImplementation.getDevId())).thenReturn(pUser);
	when(instance.lGenerator.getNewImplementationId(DataWareHouse.getPlan().getId())).thenReturn("");
	when(instance.systemLoadDAO.findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());

	try {
	    JSONResponse result = instance.saveImplementation(pUser, pImplementation);
	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testSaveImplementation1() throws IOException {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.getPlan().getImplementationList().get(0);
	DeveloperService instance = new DeveloperService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	when(instance.impPlanDAO.find(pImplementation.getPlanId().getId())).thenReturn(DataWareHouse.getPlan());
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lGenerator", mock(SequenceGenerator.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	ReviewerAssignmentMail rev = new ReviewerAssignmentMail();
	when(instance.mailMessageFactory.getTemplate(ReviewerAssignmentMail.class)).thenReturn(mock(ReviewerAssignmentMail.class));
	rev.setImplementation(pImplementation);
	rev.setUserDetails(pUser);
	Mockito.reset(instance.lBPMClientUtils);
	when(instance.lBPMClientUtils.createDeveloperProcess(pUser, "")).thenReturn("1");
	when(instance.authenticator.getUserDetails(pImplementation.getDevId())).thenReturn(pUser);
	when(instance.lGenerator.getNewImplementationId(DataWareHouse.getPlan().getId())).thenReturn("");
	when(instance.systemLoadDAO.findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.setRefreshPlan().getSystemLoadList());
	try {
	    pImplementation.setBypassPeerReview(Boolean.FALSE);
	    JSONResponse result = instance.saveImplementation(pUser, pImplementation);
	    assertNotNull(result);
	    Mockito.reset(instance.lBPMClientUtils);
	    when(instance.lBPMClientUtils.createDeveloperProcess(pUser, "")).thenReturn("");
	    result = instance.saveImplementation(pUser, pImplementation);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of updateImplementation method, of class DeveloperService.
     */
    public void testUpdateImplementation() throws IOException {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.getPlan().getImplementationList().get(0);
	DeveloperService instance = new DeveloperService();

	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	when(instance.authenticator.getUserDetails(DataWareHouse.getUser().getId())).thenReturn(pUser);
	when(instance.impPlanDAO.find(pImplementation.getPlanId().getId())).thenReturn(DataWareHouse.getPlan());
	pImplementation.setSubstatus("test");

	Implementation lImplementation = new Implementation();
	lImplementation.setDevId(pImplementation.getDevId());
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(DataWareHouse.getUser().getId());
	lImplementation.setSubstatus(null);
	when(instance.implementationDAO.find(pImplementation.getId())).thenReturn(lImplementation);
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	ReviewerReassignmentMail rev = new ReviewerReassignmentMail();
	when(instance.getMailMessageFactory().getTemplate(ReviewerReassignmentMail.class)).thenReturn(mock(ReviewerReassignmentMail.class));
	JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	assertNotNull(result);

    }

    @Test
    public void testUpdateImplementation1() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	DeveloperService instance = new DeveloperService();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	pImplementation.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	ImpPlan lPlan = pImplementation.getPlanId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId("abcdef");
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(pImplementation.getPeerReviewers());
	lImplementation.setSubstatus(pImplementation.getSubstatus());
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	Mockito.reset(instance.lGitBlitClientUtils);
	when(instance.impPlanDAO.find(lPlan.getId())).thenReturn(lPlan);
	when(instance.implementationDAO.find(pImplementation.getId())).thenReturn(lImplementation);
	when(instance.lGitUtils.getPlanRepoName("tp", pImplementation.getPlanId().getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.lGitUtils.getAllBranchList("tp", lPlan.getId())).thenReturn(lBranchList);
	when(instance.implementationDAO.countBy(lPlan, lImplementation.getDevId())).thenReturn(new Long(0));
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// lImplementation.getDevId(),
	// "")).thenReturn(Boolean.TRUE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(lRepoName,
	// pImplementation.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.lGitBlitClientUtils.setPermissionForGitRepository(null,
	// pImplementation.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);

	// when(instance.getGITSSHUtils().addImplementationTag(pUser, lRepoName,
	// Constants.ImplementationSubStatus.INTERGRATION_TESTING_COMPLETED,
	// lBranchList)).thenReturn(true);
	when(instance.getMailMessageFactory().getTemplate(DeveloperReassignmentMail.class)).thenReturn(mock(DeveloperReassignmentMail.class));
	DeveloperReassignmentMail developerReassignmentMail = new DeveloperReassignmentMail();
	developerReassignmentMail.setImplementation(lImplementation);
	developerReassignmentMail.setOldDeveloperName(lImplementation.getDevId());
	developerReassignmentMail.setNewDeveloperName(pImplementation.getDevId());
	developerReassignmentMail.setUserDetails(pUser);

	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdateImplementation2() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	DeveloperService instance = new DeveloperService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	when(instance.impPlanDAO.find(pImplementation.getPlanId().getId())).thenReturn(DataWareHouse.getPlan());
	pImplementation.setSubstatus(Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name());
	when(instance.lGitUtils.getPlanRepoName("tp", pImplementation.getPlanId().getId().toLowerCase())).thenReturn("");
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId("abc");
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(pImplementation.getPeerReviewers());
	lImplementation.setSubstatus("test1");
	lImplementation.setBypassPeerReview(Boolean.TRUE);
	when(instance.implementationDAO.find(pImplementation.getId())).thenReturn(lImplementation);
	when(instance.implementationDAO.countBy(pImplementation.getPlanId(), lImplementation.getDevId())).thenReturn(new Long(0));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	DeveloperReassignmentMail rev = new DeveloperReassignmentMail();
	when(instance.getMailMessageFactory().getTemplate(DeveloperReassignmentMail.class)).thenReturn(mock(DeveloperReassignmentMail.class));
	rev.setImplementation(pImplementation);
	rev.setUserDetails(pUser);
	rev.setOldDeveloperName(pImplementation.getDevId());
	rev.setNewDeveloperName("abc");
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Mockito.reset(instance.sSHClientUtils);
	when(instance.sSHClientUtils.addImplementationTag(lRepoName, Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED, lBranchList)).thenReturn(Boolean.TRUE);
	when(instance.sSHClientUtils.addImplementationTag("", Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED, new ArrayList())).thenReturn(Boolean.TRUE);
	pImplementation.setBypassPeerReview(Boolean.TRUE);
	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdateImplementation3() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	DeveloperService instance = new DeveloperService();
	JSONResponse expResult = null;
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	pImplementation.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ImpPlan lPlan = pImplementation.getPlanId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId("abc");
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(pImplementation.getPeerReviewers());
	lImplementation.setSubstatus("test1");

	when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.getJGitClientUtils().getAllBranchList("tp", lPlan.getId())).thenReturn(lBranchList);
	when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(lImplementation);
	when(instance.getImplementationDAO().countBy(lPlan, lImplementation.getDevId())).thenReturn(new Long(1));
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// lImplementation.getDevId(),
	// "")).thenReturn(Boolean.TRUE);
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// pImplementation.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.getGITSSHUtils().addImplementationTag(pUser, lRepoName,
	// Constants.ImplementationSubStatus.INTERGRATION_TESTING_COMPLETED,
	// lBranchList)).thenReturn(false);
	when(instance.getMailMessageFactory().getTemplate(ImplementationStatusCompletionMail.class)).thenReturn(mock(ImplementationStatusCompletionMail.class));
	ImplementationStatusCompletionMail statusChangeNotification = new ImplementationStatusCompletionMail();
	statusChangeNotification.setImpPlan(lPlan);
	statusChangeNotification.setImplementation(lImplementation);
	statusChangeNotification.setStatus("Integration Testing ");
	Mockito.reset(instance.sSHClientUtils);
	when(instance.sSHClientUtils.addImplementationTag(lRepoName, Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED, lBranchList)).thenReturn(Boolean.TRUE);
	when(instance.sSHClientUtils.addImplementationTag("", Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED, new ArrayList())).thenReturn(Boolean.TRUE);

	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdateImplementation4() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	pImplementation.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	ImpPlan lPlan = pImplementation.getPlanId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId("abc");
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(pImplementation.getPeerReviewers());
	lImplementation.setSubstatus("test1");

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lGitUtils", mock(JGitClientUtils.class));
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "lBPMClientUtils", mock(BPMClientUtils.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));

	when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.getJGitClientUtils().getAllBranchList("tp", lPlan.getId())).thenReturn(lBranchList);
	when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(lImplementation);
	when(instance.getImplementationDAO().countBy(lPlan, lImplementation.getDevId())).thenReturn(new Long(1));
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// lImplementation.getDevId(),
	// "")).thenReturn(Boolean.TRUE);
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// pImplementation.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.getGITSSHUtils().addImplementationTag(pUser, lRepoName,
	// Constants.ImplementationSubStatus.INTERGRATION_TESTING_COMPLETED,
	// lBranchList)).thenReturn(true);
	when(instance.getMailMessageFactory().getTemplate(ImplementationStatusCompletionMail.class)).thenReturn(mock(ImplementationStatusCompletionMail.class));
	ImplementationStatusCompletionMail statusChangeNotification = new ImplementationStatusCompletionMail();
	statusChangeNotification.setImpPlan(lPlan);
	statusChangeNotification.setImplementation(lImplementation);
	statusChangeNotification.setStatus("Integration Testing ");

	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdateImplementation5() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	pImplementation.setSubstatus(Constants.ImplementationSubStatus.INTEGRATION_TESTING_COMPLETED.name());
	ImpPlan lPlan = pImplementation.getPlanId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	lBranchList.add(pImplementation.getId() + "test");
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId("abc");
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(pImplementation.getPeerReviewers());
	lImplementation.setSubstatus("test1");

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId().toLowerCase())).thenReturn(lRepoName);
	when(instance.getJGitClientUtils().getAllBranchList("tp", lPlan.getId())).thenReturn(lBranchList);
	when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(lImplementation);
	when(instance.getImplementationDAO().countBy(lPlan, lImplementation.getDevId())).thenReturn(new Long(1));
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// lImplementation.getDevId(),
	// "")).thenReturn(Boolean.TRUE);
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// pImplementation.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	// when(instance.getGITSSHUtils().addImplementationTag(pUser, lRepoName,
	// Constants.ImplementationSubStatus.INTERGRATION_TESTING_COMPLETED,
	// lBranchList)).thenReturn(true);
	when(instance.getMailMessageFactory().getTemplate(ImplementationStatusCompletionMail.class)).thenReturn(mock(ImplementationStatusCompletionMail.class));
	ImplementationStatusCompletionMail statusChangeNotification = new ImplementationStatusCompletionMail();
	statusChangeNotification.setImpPlan(lPlan);
	statusChangeNotification.setImplementation(lImplementation);
	statusChangeNotification.setStatus("Integration Testing ");

	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdateImplementation6() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	ImpPlan lPlan = pImplementation.getPlanId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId("abc");
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers(pImplementation.getPeerReviewers());
	lImplementation.setSubstatus(pImplementation.getSubstatus());

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(lImplementation);
	when(instance.getImplementationDAO().countBy(lPlan, lImplementation.getDevId())).thenReturn(new Long(1));
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// lImplementation.getDevId(),
	// "")).thenReturn(Boolean.TRUE);
	// when(instance.getGitBlitClientUtils().setPermissionForGitRepository(lRepoName,
	// pImplementation.getDevId(),
	// Constants.GIT_PERMISSION_READWRITE)).thenReturn(Boolean.TRUE);
	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    @Test
    public void testUpdateImplementation7() throws IOException, Exception {

	User pUser = DataWareHouse.getUser();
	Implementation pImplementation = DataWareHouse.setRefreshPlan().getImplementationList().get(0);
	ImpPlan lPlan = pImplementation.getPlanId();
	String lRepoName = DataWareHouse.RepoName;
	List<String> lBranchList = DataWareHouse.getBranchList();
	Implementation lImplementation = new Implementation();
	lImplementation.setDevId(pImplementation.getDevId());
	lImplementation.setPlanId(pImplementation.getPlanId());
	lImplementation.setPeerReviewers("");
	lImplementation.setSubstatus(pImplementation.getSubstatus());

	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(lImplementation);
	ReviewerReassignmentMail reviewerReassignmentMail = new ReviewerReassignmentMail();
	when(instance.getMailMessageFactory().getTemplate(ReviewerReassignmentMail.class)).thenReturn(mock(ReviewerReassignmentMail.class));

	try {
	    JSONResponse result = instance.updateImplementation(pUser, pImplementation);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

    /**
     * Test of listTestCases method, of class DeveloperService.
     */
    @Test
    public void testListTestCases() {

	String pPlanId = "";
	String pImplementationId = "";
	DeveloperService instance = new DeveloperService();
	JSONResponse expResult = null;
	File file = spy(new File(""));
	doReturn(true).when(file).exists();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	JSONResponse result = instance.listTestCases(pPlanId, pImplementationId);
	assertNotNull(result);
    }

    /**
     * Test of isMakFileExistInUserInput method, of class DeveloperService.
     */
    @Test
    public void testIsMakFileExistInUserInput() {

	DataWareHouse.getPlan().getCheckoutSegmentsList().get(0).setTargetSystem("wsp");
	DataWareHouse.getPlan().getCheckoutSegmentsList().get(0).setFileName("abc.mak");
	Boolean result = instance.isMakFileExistInUserInput(DataWareHouse.getPlan().getCheckoutSegmentsList(), "abc.mak", "wsp");
	Assert.assertEquals(Boolean.TRUE, result);
	Boolean resultFalse = instance.isMakFileExistInUserInput(DataWareHouse.getPlan().getCheckoutSegmentsList(), " ", " ");
	Assert.assertEquals(Boolean.FALSE, resultFalse);
    }

    /**
     * Test of isMakFileExistInUserInputForCheckout method, of class
     * DeveloperService.
     */
    @Test
    public void testIsMakFileExistInUserInputForCheckout() {

	DataWareHouse.getPlan().getCheckoutSegmentsList().get(0).setTargetSystem("wsp");
	DataWareHouse.getPlan().getCheckoutSegmentsList().get(0).setFileName("abc.mak");
	Boolean result = instance.isMakFileExistInUserInputForCheckout(DataWareHouse.getPlan().getCheckoutSegmentsList(), "abc.mak", "wsp");
	Assert.assertEquals(Boolean.TRUE, result);
	Boolean resultFalse = instance.isMakFileExistInUserInputForCheckout(DataWareHouse.getPlan().getCheckoutSegmentsList(), "", "");
	Assert.assertEquals(Boolean.FALSE, resultFalse);
    }

    @Test
    public void testGetUserTaskList() {
	try {
	    HashMap<String, String> lOrderBy = new HashMap<String, String>();
	    lOrderBy.put("wsp", "wsp");
	    instance.getUserTaskList(DataWareHouse.getUser(), 0, 10, lOrderBy);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    /**
     * Test of sendNotificationOnCheckIn method, of class DeveloperService.
     */
    @Test
    public void testSendNotificationOnCheckIn() {
	ReflectionTestUtils.invokeMethod(instance, "sendNotificationOnCheckIn", "101");

    }

    @Test
    public void testCreateSourceArtifact() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.refreshGitSearchResult());

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);

	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pSearchResults);
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact3() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);

	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);
	    CheckoutSegmentsDAO checkoutSegmentsDAO = instance.getCheckoutSegmentsDAO();
	    Mockito.reset(checkoutSegmentsDAO);
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pSearchResults);
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    when(instance.getCheckoutSegmentsDAO().findByFileName(lGitSearchResult.getFileName(), pImplementation.getId(), lSysLoads.get(2).getSystemId().getName(), "nonibm_gilk.git")).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact4() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);

	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pSearchResults);
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact5() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);
	List<String> lBranch = new ArrayList();
	lBranch.add(pImplementation.getId().toLowerCase() + "_" + lSysLoads.get(2).getSystemId().getName().toLowerCase());

	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pSearchResults);
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(lBranch);
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lSysLoads.get(2).getSystemId())).thenReturn(false);
	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact6() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);
	pSearchResults.add(lGitSearchResult);
	List<String> lBranch = new ArrayList();
	lBranch.add(pImplementation.getId().toLowerCase() + "_" + lSysLoads.get(2).getSystemId().getName().toLowerCase());
	String lRepoName = DataWareHouse.RepoName;
	String lWrkSpaceCmd = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + String.join(",", lBranch);
	JSONResponse lWrkspaceRes = new JSONResponse();
	lWrkspaceRes.setStatus(Boolean.FALSE);
	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);
	    // SSHUtil sshUtil = instance.getSSHUtil();
	    // Mockito.reset(sshUtil);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pSearchResults);
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(lBranch);
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lSysLoads.get(2).getSystemId())).thenReturn(true);
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(lWrkSpaceCmd)).thenReturn(lWrkspaceRes);

	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact7() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);
	pSearchResults.add(lGitSearchResult);
	List<String> lBranch = new ArrayList();
	lBranch.add(pImplementation.getId().toLowerCase() + "_" + lSysLoads.get(2).getSystemId().getName().toLowerCase());
	String lRepoName = DataWareHouse.RepoName;
	String lWrkSpaceCmd = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + String.join(",", lBranch);
	JSONResponse lWrkspaceRes = new JSONResponse();
	lWrkspaceRes.setStatus(Boolean.TRUE);
	List<String> lRepoList = new ArrayList();
	lRepoList.add("tp/nonibm/nonibm_nonibm_gilk.git.git");
	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);
	    // SSHUtil sshUtil = instance.getSSHUtil();
	    // Mockito.reset(sshUtil);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(lBranch);
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lSysLoads.get(2).getSystemId())).thenReturn(true);
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId())).thenReturn(lRepoName);
	    when(instance.getJGitClientUtils().getProductionRepoList("tp")).thenReturn(lRepoList);
	    // when(instance.getSSHUtil().executeCommand(lWrkSpaceCmd)).thenReturn(lWrkspaceRes);

	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact8() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);
	pSearchResults.add(lGitSearchResult);
	List<String> lBranch = new ArrayList();
	lBranch.add(pImplementation.getId().toLowerCase() + "_" + lSysLoads.get(2).getSystemId().getName().toLowerCase());
	String lRepoName = DataWareHouse.RepoName;
	String lWrkSpaceCmd = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + String.join(",", lBranch);
	JSONResponse lWrkspaceRes = new JSONResponse();
	lWrkspaceRes.setStatus(Boolean.TRUE);
	List<String> lRepoList = new ArrayList();
	lRepoList.add("tp/nonibm/nonibm_nonibm_gilk.git.git");
	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);
	    // SSHUtil sshUtil = instance.getSSHUtil();
	    // Mockito.reset(sshUtil);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(lBranch);
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lSysLoads.get(2).getSystemId())).thenReturn(true);
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(lWrkSpaceCmd)).thenReturn(lWrkspaceRes);

	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact9() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);
	List<String> lBranch = new ArrayList();
	lBranch.add(pImplementation.getId().toLowerCase() + "_" + lSysLoads.get(2).getSystemId().getName().toLowerCase());
	String lRepoName = DataWareHouse.RepoName;
	String lWrkSpaceCmd = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + String.join(",", lBranch);
	JSONResponse lWrkspaceRes = new JSONResponse();
	lWrkspaceRes.setStatus(Boolean.TRUE);
	String lCommand = Constants.SystemScripts.CREATE_SOURCE_ARTIFACT.getScript() + pImplementation.getId().toLowerCase() + " " + lSysLoads.get(2).getSystemId().getName().toLowerCase() + " " + "tp/nonibm/nonibm_nonibm_gilk.git.git" + " " + lGitSearchResult.getFileName();
	List<String> lRepoList = new ArrayList();
	lRepoList.add("tp/nonibm/nonibm_nonibm_gilk.git.git");
	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);
	    // SSHUtil sshUtil = instance.getSSHUtil();
	    // Mockito.reset(sshUtil);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(lBranch);
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lSysLoads.get(2).getSystemId())).thenReturn(true);
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId())).thenReturn(lRepoName);
	    when(instance.getJGitClientUtils().getProductionRepoList("tp")).thenReturn(lRepoList);
	    // when(instance.getSSHUtil().executeCommand(lWrkSpaceCmd)).thenReturn(lWrkspaceRes);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lWrkspaceRes);

	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateSourceArtifact10() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.setRefreshPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	for (GitBranchSearchResult get : lGitSearchResult.getBranch()) {
	    String replace = get.getTargetSystem().replace("master_", "");
	    get.setTargetSystem(replace);
	}
	pSearchResults.add(lGitSearchResult);
	List<String> lBranch = new ArrayList();
	lBranch.add(pImplementation.getId().toLowerCase() + "_" + lSysLoads.get(2).getSystemId().getName().toLowerCase());
	String lRepoName = DataWareHouse.RepoName;
	String lWrkSpaceCmd = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + String.join(",", lBranch);
	JSONResponse lWrkspaceRes = new JSONResponse();
	lWrkspaceRes.setStatus(Boolean.TRUE);
	String lCommand = Constants.SystemScripts.CREATE_SOURCE_ARTIFACT.getScript() + pImplementation.getId().toLowerCase() + " " + lSysLoads.get(2).getSystemId().getName().toLowerCase() + " " + "tp/nonibm/nonibm_nonibm_gilk.git.git" + " " + lGitSearchResult.getFileName();
	List<String> lRepoList = new ArrayList();
	lRepoList.add("tp/nonibm/nonibm_nonibm_gilk.git.git");
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.FALSE);

	try {
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.reset(jGitClientUtils);
	    SystemDAO systemDAO = instance.getSystemDAO();
	    Mockito.reset(systemDAO);
	    // SSHUtil sshUtil = instance.getSSHUtil();
	    // Mockito.reset(sshUtil);

	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(1).getSystemId().getName().toLowerCase())).thenReturn(null);
	    when(instance.getSystemDAO().findByName(lSysLoads.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoads.get(2).getSystemId());
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(new ArrayList());
	    when(instance.getJGitClientUtils().getAllBranchList("tp", pImplementation.getPlanId().getId())).thenReturn(lBranch);
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lSysLoads.get(2).getSystemId())).thenReturn(true);
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", pImplementation.getPlanId().getId())).thenReturn(lRepoName);
	    when(instance.getJGitClientUtils().getProductionRepoList("tp")).thenReturn(lRepoList);
	    // when(instance.getSSHUtil().executeCommand(lWrkSpaceCmd)).thenReturn(lWrkspaceRes);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdRes);

	    instance.createSourceArtifact(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment() {
	try {
	    List<GitSearchResult> pFileList = new ArrayList<GitSearchResult>();
	    instance.populateIBMSegment(DataWareHouse.getUser(), "t1800345", pFileList);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testRequestPeerReview() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.setTktNum("");
	System pSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().getPlanSSHURL("tp", lPlan.getId(), pUser.getId())).thenReturn("lRepoUrl");
	    when(instance.getJGitClientUtils().getPlanRepoFullName("tp", lPlan.getId())).thenReturn("repositoryName");
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // pSystem)).thenReturn(Boolean.FALSE);
	    instance.requestPeerReview(pUser, pImplementation.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testRequestPeerReview1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.setTktNum("");
	System pSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	String lCommand = Constants.GitScripts.CREATE_TICKET.getScript() + "lRepoUrl" + " " + pImplementation.getId().toLowerCase() + " " + pImplementation.getPeerReviewers();
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(Boolean.TRUE);
	lExeResponse.setData("lTicket");

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance.getMailMessageFactory(), "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().getPlanSSHURL("tp", lPlan.getId(), pUser.getId())).thenReturn("lRepoUrl");
	    when(instance.getJGitClientUtils().getPlanRepoFullName("tp", lPlan.getId())).thenReturn("repositoryName");
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // pSystem)).thenReturn(Boolean.TRUE);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    when(instance.getGitBlitClientUtils().getImplementationTicketURL("tp", lPlan.getId(), "lTicket")).thenReturn("lTicketURL");
	    PeerReviewRequestMail peerReviewRequestMail = new PeerReviewRequestMail();
	    when(instance.getMailMessageFactory().getTemplate(PeerReviewRequestMail.class)).thenReturn(mock(PeerReviewRequestMail.class));
	    JSONResponse sshResponse = new JSONResponse();
	    sshResponse.setStatus(true);
	    sshResponse.setData("ticket");
	    when(instance.getsSHClientUtils().executeCommand(pUser, pImplementation.getPlanId().getSystemLoadList().get(0).getSystemId(), Constants.GitScripts.CREATE_TICKET.getScript() + "lRepoUrl" + " " + pImplementation.getId().toLowerCase() + " " + pImplementation.getPeerReviewers())).thenReturn(sshResponse);

	    instance.requestPeerReview(pUser, pImplementation.getId());

	    // If sshResponse status if false
	    sshResponse.setStatus(false);
	    when(instance.getsSHClientUtils().executeCommand(pUser, pImplementation.getPlanId().getSystemLoadList().get(0).getSystemId(), Constants.GitScripts.CREATE_TICKET.getScript() + "lRepoUrl" + " " + pImplementation.getId().toLowerCase() + " " + pImplementation.getPeerReviewers())).thenReturn(sshResponse);
	    instance.requestPeerReview(pUser, pImplementation.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testRequestPeerReview2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.setTktNum("");
	System pSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	String lCommand = Constants.GitScripts.CREATE_TICKET.getScript() + "lRepoUrl" + " " + pImplementation.getId().toLowerCase() + " " + pImplementation.getPeerReviewers();
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(Boolean.TRUE);
	lExeResponse.setData("lTicket");

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance.getMailMessageFactory(), "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().getPlanSSHURL("tp", lPlan.getId(), pUser.getId())).thenReturn("lRepoUrl");
	    when(instance.getJGitClientUtils().getPlanRepoFullName("tp", lPlan.getId())).thenReturn("repositoryName");
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // pSystem)).thenReturn(Boolean.TRUE);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    when(instance.getGitBlitClientUtils().getImplementationTicketURL("tp", lPlan.getId(), "lTicket")).thenReturn("lTicketURL");
	    PeerReviewRequestMail peerReviewRequestMail = new PeerReviewRequestMail();
	    when(instance.getMailMessageFactory().getTemplate(PeerReviewRequestMail.class)).thenReturn(mock(PeerReviewRequestMail.class));
	    JSONResponse sshResponse = new JSONResponse();
	    sshResponse.setData("ticket");

	    // If sshResponse status if false
	    sshResponse.setStatus(false);
	    when(instance.getsSHClientUtils().executeCommand(pUser, pImplementation.getPlanId().getSystemLoadList().get(0).getSystemId(), Constants.GitScripts.CREATE_TICKET.getScript() + "lRepoUrl" + " " + pImplementation.getId().toLowerCase() + " " + pImplementation.getPeerReviewers())).thenReturn(sshResponse);
	    instance.requestPeerReview(pUser, pImplementation.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testRequestPeerReview3() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.setTktNum("");
	System pSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	String lCommand = Constants.GitScripts.CREATE_TICKET.getScript() + "lRepoUrl" + " " + pImplementation.getId().toLowerCase() + " " + pImplementation.getPeerReviewers();
	JSONResponse lExeResponse = new JSONResponse();
	lExeResponse.setStatus(Boolean.FALSE);
	lExeResponse.setData("lTicket");

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().getPlanSSHURL("tp", lPlan.getId(), pUser.getId())).thenReturn("lRepoUrl");
	    when(instance.getJGitClientUtils().getPlanRepoFullName("tp", lPlan.getId())).thenReturn("repositoryName");
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // pSystem)).thenReturn(Boolean.TRUE);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lExeResponse);
	    when(instance.getGitBlitClientUtils().getImplementationTicketURL("tp", lPlan.getId(), "lTicket")).thenReturn("lTicketURL");
	    when(instance.getMailMessageFactory().getTemplate(PeerReviewRequestMail.class)).thenReturn(mock(PeerReviewRequestMail.class));

	    instance.requestPeerReview(pUser, pImplementation.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeleteFile() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<CheckoutSegments> lSegments = pImplementation.getCheckoutSegmentsList();
	Integer[] ids = new Integer[1];
	CheckoutSegments lSegment = lSegments.get(0);
	ids[0] = lSegment.getId();
	String lMtpService = "mtpservice";
	System lSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase();
	String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase();
	String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + pUser.getId() + "/projects/" + (lSegment.getImpId().getId() + "/" + lSegment.getTargetSystem()).toLowerCase() + " \"" + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase() + "\" \"" + lMtpService + "\"";
	JSONResponse lCheckinResponse = new JSONResponse();
	JSONResponse lDevlCmd = new JSONResponse();
	JSONResponse lExpCmd = new JSONResponse();
	lCheckinResponse.setStatus(Boolean.FALSE);
	lDevlCmd.setStatus(Boolean.FALSE);
	lExpCmd.setStatus(Boolean.FALSE);
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));

	try {
	    when(instance.getGITConfig().getServiceUserID()).thenReturn(lMtpService);
	    when(instance.systemDAO.findByName(lSegment.getTargetSystem())).thenReturn(lSystem);
	    when(instance.getCheckoutSegmentsDAO().find(lSegments.get(0).getId())).thenReturn(lSegments.get(0));
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCheckInCommand)).thenReturn(lCheckinResponse);
	    when(instance.sSHClientUtils.executeCommand(lSystem, lDevlWorkspaceCommand)).thenReturn(lDevlCmd);
	    when(instance.sSHClientUtils.executeCommand(lSystem, lExportCommand)).thenReturn(lExpCmd);

	    instance.deleteFile(pUser, ids);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testDeleteFile1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<CheckoutSegments> lSegments = pImplementation.getCheckoutSegmentsList();
	Integer[] ids = new Integer[1];
	CheckoutSegments lSegment = lSegments.get(0);
	ids[0] = lSegment.getId();
	String lMtpService = "mtpservice";
	System lSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase();
	String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase();
	String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + pUser.getId() + "/projects/" + (lSegment.getImpId().getId() + "/" + lSegment.getTargetSystem()).toLowerCase() + " \"" + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase() + "\" \"" + lMtpService + "\"";
	JSONResponse lCheckinResponse = new JSONResponse();
	JSONResponse lDevlCmd = new JSONResponse();
	JSONResponse lExpCmd = new JSONResponse();
	JSONResponse lDelCmd = new JSONResponse();
	lCheckinResponse.setStatus(Boolean.TRUE);
	lDevlCmd.setStatus(Boolean.TRUE);
	lExpCmd.setStatus(Boolean.TRUE);
	lDelCmd.setStatus(Boolean.TRUE);
	String lArguments = lSegment.getImpId().getId().toLowerCase() + " " + lSegment.getTargetSystem().toLowerCase() + " " + lSegment.getFileName();
	String lDelCommand = Constants.SystemScripts.DELETE_FILE.getScript() + lArguments;
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));

	try {
	    when(instance.getGITConfig().getServiceUserID()).thenReturn(lMtpService);
	    when(instance.getCheckoutSegmentsDAO().find(lSegments.get(0).getId())).thenReturn(lSegments.get(0));
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCheckInCommand)).thenReturn(lCheckinResponse);
	    when(instance.sSHClientUtils.executeCommand(lSystem, lDevlWorkspaceCommand)).thenReturn(lDevlCmd);
	    when(instance.sSHClientUtils.executeCommand(lSystem, lExportCommand)).thenReturn(lExpCmd);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lDelCommand)).thenReturn(lExpCmd);

	    instance.deleteFile(pUser, ids);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testDeleteFile2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<CheckoutSegments> lSegments = pImplementation.getCheckoutSegmentsList();
	Integer[] ids = new Integer[1];
	CheckoutSegments lSegment = lSegments.get(0);
	ids[0] = lSegment.getId();
	String lMtpService = "mtpservice";
	System lSystem = lPlan.getSystemLoadList().get(0).getSystemId();

	String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase();
	String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase();
	String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + pUser.getId() + "/projects/" + (lSegment.getImpId().getId() + "/" + lSegment.getTargetSystem()).toLowerCase() + " \"" + (lSegment.getImpId().getId() + "_" + lSegment.getTargetSystem()).toLowerCase() + "\" \"" + lMtpService + "\"";
	JSONResponse lCheckinResponse = new JSONResponse();
	JSONResponse lDevlCmd = new JSONResponse();
	JSONResponse lExpCmd = new JSONResponse();
	JSONResponse lDelCmd = new JSONResponse();
	lCheckinResponse.setStatus(Boolean.TRUE);
	lDevlCmd.setStatus(Boolean.TRUE);
	lExpCmd.setStatus(Boolean.TRUE);
	lDelCmd.setStatus(Boolean.TRUE);
	String lArguments = lSegment.getImpId().getId().toLowerCase() + " " + lSegment.getTargetSystem().toLowerCase() + " " + lSegment.getFileName();
	String lDelCommand = Constants.SystemScripts.DELETE_FILE.getScript() + lArguments;
	List<Object[]> planList = new ArrayList();
	Object[] lDbPlans = new Object[8];
	lDbPlans[0] = pImplementation.getId();
	lDbPlans[1] = lPlan.getId();
	lDbPlans[2] = lPlan.getLeadId();
	lDbPlans[3] = pImplementation.getDevId();
	lDbPlans[4] = pImplementation.getCheckoutSegmentsList().get(0).getProgramName() + "[" + "APO" + "]";
	lDbPlans[5] = lPlan.getLoadType();
	lDbPlans[6] = lPlan.getPlanStatus();
	lDbPlans[7] = pImplementation.getId();
	planList.add(lDbPlans);
	CheckoutDeleteMail checkoutDeleteMail = new CheckoutDeleteMail();
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));

	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutDeleteMail.class)).thenReturn(mock(CheckoutDeleteMail.class));

	    when(instance.getGITConfig().getServiceUserID()).thenReturn(lMtpService);
	    when(instance.getCheckoutSegmentsDAO().find(lSegments.get(0).getId())).thenReturn(lSegments.get(0));
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCheckInCommand)).thenReturn(lCheckinResponse);
	    when(instance.sSHClientUtils.executeCommand(lSystem, lDevlWorkspaceCommand)).thenReturn(lDevlCmd);
	    when(instance.sSHClientUtils.executeCommand(lSystem, lExportCommand)).thenReturn(lExpCmd);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lDelCommand)).thenReturn(lExpCmd);
	    when(instance.getImpPlanDAO().getDevelopersBySegmentForDelete(lSegment.getPlanId().getId(), new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()), lSegment.getProgramName(), Arrays.asList(lSegment.getTargetSystem()))).thenReturn(planList);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    instance.deleteFile(pUser, ids);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCreateWorkspace() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	String lRepoName = DataWareHouse.RepoName;
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lPlan.getSystemLoadList().get(1).getSystemId().getName().toLowerCase();
	String l3Command = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lPlan.getSystemLoadList().get(2).getSystemId().getName().toLowerCase();

	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	JSONResponse l3CmdResponse = new JSONResponse();
	l3CmdResponse.setStatus(Boolean.FALSE);

	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.getSystemLoadDAO().getSystemLoadsFromImp(pImplementation.getId())).thenReturn(lPlan.getSystemLoadList());
	    // when(instance.getSSHUtil().connectSSH(pUser,
	    // lPlan.getSystemLoadList().get(0).getSystemId())).thenReturn(false);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(l3CmdResponse);
	    instance.createWorkspace(pUser, pImplementation.getId());

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCreateWorkspace1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);

	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(null);
	    instance.createWorkspace(pUser, pImplementation.getId());

	} catch (Exception ex) {
	    java.lang.System.out.println(ex);
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(0).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().connectSSH()).thenReturn(Boolean.FALSE);
	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.FALSE);
	try {
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCommand)).thenReturn(lCmdRes);
	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile3() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("");
	List<Object[]> planList = new ArrayList();
	Object[] lDbPlans = new Object[9];
	lDbPlans[0] = pImplementation.getId();
	lDbPlans[1] = lPlan.getId();
	lDbPlans[2] = lPlan.getLeadId();
	lDbPlans[3] = pImplementation.getDevId();
	lDbPlans[4] = pImplementation.getCheckoutSegmentsList().get(0).getProgramName() + "[" + "APO" + "]";
	lDbPlans[5] = lPlan.getLoadType();
	lDbPlans[6] = lPlan.getPlanStatus();
	lDbPlans[7] = pImplementation.getId();
	lDbPlans[8] = lPlan.getSystemLoadList().get(1).getLoadDateTime();
	planList.add(lDbPlans);

	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lCmdRes);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getImpPlanDAO().getDevelopersBySegment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(planList);
	    when(instance.getImpPlanDAO().getAllDependentDevelopersBySegment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(planList);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile4() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("");
	List<Object[]> planList = new ArrayList();
	Object[] lDbPlans = new Object[9];
	lDbPlans[0] = pImplementation.getId();
	lDbPlans[1] = lPlan.getId();
	lDbPlans[2] = lPlan.getLeadId();
	lDbPlans[3] = pImplementation.getDevId();
	lDbPlans[4] = pImplementation.getCheckoutSegmentsList().get(0).getProgramName() + "[" + "APO" + "]";
	lDbPlans[5] = lPlan.getLoadType();
	lDbPlans[6] = lPlan.getPlanStatus();
	lDbPlans[7] = pImplementation.getId();
	lDbPlans[8] = lPlan.getSystemLoadList().get(1).getLoadDateTime();
	planList.add(lDbPlans);

	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lCmdRes);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile5() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("");
	List<Object[]> planList = new ArrayList();
	Object[] lDbPlans = new Object[9];
	lDbPlans[0] = pImplementation.getId();
	lDbPlans[1] = lPlan.getId();
	lDbPlans[2] = lPlan.getLeadId();
	lDbPlans[3] = pImplementation.getDevId();
	lDbPlans[4] = pImplementation.getCheckoutSegmentsList().get(0).getProgramName() + "[" + "APO" + "]";
	lDbPlans[5] = lPlan.getLoadType();
	lDbPlans[6] = lPlan.getPlanStatus();
	lDbPlans[7] = pImplementation.getId();
	lDbPlans[8] = lPlan.getSystemLoadList().get(1).getLoadDateTime();
	planList.add(lDbPlans);

	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lCmdRes);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getImpPlanDAO().getAllDependentDevelopersBySegment(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(planList);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile6() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("lnaj.mak");
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(DataWareHouse.getGitSearchResult(), lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	lTempGitSearchResult1.getBranch().get(0).setRefStatus("online");
	lTempGitSearchResult1.getBranch().get(1).setRefStatus("online");
	pTempSearchResults1.add(lTempGitSearchResult1);

	try {
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lCmdRes);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile7() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("lnaj.mak");
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(DataWareHouse.getGitSearchResult(), lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	lTempGitSearchResult1.getBranch().get(0).setRefStatus("online");
	lTempGitSearchResult1.getBranch().get(1).setRefStatus("online");
	pTempSearchResults1.add(lTempGitSearchResult1);
	// SSHUtil sshUtil = instance.getSSHUtil();

	try {
	    // Mockito.reset(sshUtil);
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "gitHelper", mock(GITHelper.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lCmdRes);
	    // when(instance.getSSHUtil().connectSSH(Matchers.anyObject(),
	    // Matchers.anyObject())).thenReturn(false);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(sshUtil);

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile8() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("lnaj.mak");
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(DataWareHouse.getGitSearchResult(), lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	lTempGitSearchResult1.getBranch().get(0).setRefStatus("online");
	lTempGitSearchResult1.getBranch().get(1).setRefStatus("online");
	pTempSearchResults1.add(lTempGitSearchResult1);
	// SSHUtil sshUtil = instance.getSSHUtil();
	String lSearchCmd = "";// Constants.SystemScripts.SEARCH_MAK_FILE.getScript() + "lnaj.asm" + " " +
			       // lSystem.getName().toLowerCase();
	JSONResponse lSearchCmdres = new JSONResponse();
	lSearchCmdres.setStatus(Boolean.FALSE);
	ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	try {
	    Mockito.reset(instance.sSHClientUtils);
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "gitHelper", mock(GITHelper.class));

	    // when(instance.gitHelper.)
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCommand)).thenReturn(lCmdRes);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lSearchCmd)).thenReturn(lSearchCmdres);
	    // when(instance.getSSHUtil().connectSSH(Matchers.anyObject(),
	    // Matchers.anyObject())).thenReturn(true);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(sshUtil);

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile9() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("lnaj.mak");
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(DataWareHouse.getGitSearchResult(), lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	lTempGitSearchResult1.getBranch().get(0).setRefStatus("online");
	lTempGitSearchResult1.getBranch().get(1).setRefStatus("online");
	pTempSearchResults1.add(lTempGitSearchResult1);
	// SSHUtil sshUtil = instance.getSSHUtil();
	String lSearchCmd = "";// Constants.SystemScripts.SEARCH_MAK_FILE.getScript() + "lnaj.asm" + " " +
			       // lSystem.getName().toLowerCase();
	JSONResponse lSearchCmdres = new JSONResponse();
	lSearchCmdres.setStatus(Boolean.TRUE);
	lSearchCmdres.setData("notfound");
	try {
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    Mockito.reset(instance.sSHClientUtils);
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCommand)).thenReturn(lCmdRes);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lSearchCmd)).thenReturn(lSearchCmdres);
	    // when(instance.getSSHUtil().connectSSH(Matchers.anyObject(),
	    // Matchers.anyObject())).thenReturn(true);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(pImplementation.getCheckoutSegmentsList().get(0));
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(sshUtil);

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckoutFile10() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	System lSystem = lPlan.getSystemLoadList().get(1).getSystemId();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	String lRepoName = DataWareHouse.RepoName;
	Map<String, Boolean> lReturn = new HashMap();
	lReturn.put("test", false);
	String lCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(Boolean.TRUE);
	lCmdRes.setData("lnaj.mak");
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(DataWareHouse.getGitSearchResult(), lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	lTempGitSearchResult1.getBranch().get(0).setRefStatus("online");
	lTempGitSearchResult1.getBranch().get(1).setRefStatus("online");
	pTempSearchResults1.add(lTempGitSearchResult1);
	// SSHUtil sshUtil = instance.getSSHUtil();
	String sourceURL = DataWareHouse.getGitSearchResult().getBranch().get(0).getSourceUrl();
	String lSearchCmd = "";// Constants.SystemScripts.SEARCH_MAK_FILE.getScript() + "lnaj.asm" + " " +
			       // lSystem.getName().toLowerCase();
	JSONResponse lSearchCmdres = new JSONResponse();
	lSearchCmdres.setStatus(Boolean.TRUE);
	lSearchCmdres.setData("lnaj.mak");
	String lCheckoutCmd = "${MTP_ENV}/mtpgitcmdchkout t1700484_001_pre lnaj.mak 2e2c8d1a3ddf45f51d0ba67c18352dc9ba099b3d db60d0f197dfaf7af2386355ad2b22ce232da3d9 ssh://vhldvztdt001.tvlport.net:8445/tpf/tp/nonibm/nonibm_gilk.git t1700484";
	try {
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "gitHelper", mock(GITHelper.class));
	    Mockito.reset(instance.sSHClientUtils);
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.gitHelper.getRepositoryNameBySourceURL(sourceURL)).thenReturn("test");
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getJGitClientUtils().createBranches(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(lReturn);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getMailMessageFactory().getTemplate(UnsecuredCheckoutSourceContentMail.class)).thenReturn(mock(UnsecuredCheckoutSourceContentMail.class));
	    when(instance.getMailMessageFactory().getTemplate(CheckoutSourceContentMail.class)).thenReturn(mock(CheckoutSourceContentMail.class));

	    when(instance.getJGitClientUtils().getPlanRepoName("tp", lPlan.getId())).thenReturn(lRepoName);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCommand)).thenReturn(lCmdRes);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lCheckoutCmd)).thenReturn(lCmdRes);
	    when(instance.sSHClientUtils.executeCommand(pUser, lSystem, lSearchCmd)).thenReturn(lSearchCmdres);
	    // when(instance.getSSHUtil().connectSSH(Matchers.anyObject(),
	    // Matchers.anyObject())).thenReturn(true);
	    when(instance.getCheckoutSegmentsDAO().findByFileName(Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject(), Matchers.anyObject())).thenReturn(null);
	    when(instance.getSystemLoadDAO().find(lPlan, lSystem)).thenReturn(null);
	    when(instance.getLDAPAuthenticatorImpl().getUserDetails(lPlan.getLeadId())).thenReturn(pUser);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, Arrays.asList("test"))).thenReturn(pTempSearchResults1);

	    instance.checkoutFile(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(sshUtil);

	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCommit() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	List<SystemLoad> lSysLoadList = lPlan.getSystemLoadList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	List<GitSearchResult> lGitSearchResultList = new ArrayList();
	lGitSearchResultList.add(lGitSearchResult);
	CheckoutSegments lSegment = pImplementation.getCheckoutSegmentsList().get(0);
	lSegment.setReviewStatus(Boolean.TRUE);
	List<CheckoutSegments> lSegments = new ArrayList();
	lSegments.add(lSegment);
	String pCommitMessage = "test";
	try {
	    when(instance.getSystemDAO().findByName(lSysLoadList.get(1).getSystemId().getName().toLowerCase())).thenReturn(lSysLoadList.get(1).getSystemId());
	    when(instance.getSystemDAO().findByName(lSysLoadList.get(2).getSystemId().getName().toLowerCase())).thenReturn(lSysLoadList.get(2).getSystemId());
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(pImplementation.getId())).thenReturn(lSegments);
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemLoadDAO().findByImpPlan(pImplementation.getPlanId())).thenReturn(lSysLoadList);
	    instance.commit(pUser, pImplementation.getId(), lGitSearchResultList, pCommitMessage);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testCheckin() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	CheckoutSegments lSegment = pImplementation.getCheckoutSegmentsList().get(0);
	lSegment.setReviewStatus(Boolean.TRUE);
	List<CheckoutSegments> lSegments = new ArrayList();
	lSegments.add(lSegment);
	String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (pImplementation.getId() + "_" + "apo").toLowerCase();
	String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (pImplementation.getId() + "_" + "apo").toLowerCase();
	String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + pUser.getId() + "/projects/" + (pImplementation.getId() + "/" + "apo").toLowerCase() + " \"" + (pImplementation.getId() + "_" + "apo").toLowerCase() + "\" \"" + instance.getGITConfig().getServiceUserID() + "\"";
	JSONResponse lCmdRes = new JSONResponse();
	JSONResponse lDvlRes = new JSONResponse();
	JSONResponse lExpRes = new JSONResponse();
	lCmdRes.setStatus(true);
	lDvlRes.setStatus(true);
	lExpRes.setStatus(true);

	// DeveloperService instance = spy(new DeveloperService());
	// ReflectionTestUtils.setField(instance, "implementationDAO",
	// mock(ImplementationDAO.class));
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(pImplementation.getId())).thenReturn(lSegments);
	    // when(instance.getSSHUtil().executeCommand(lCheckInCommand)).thenReturn(lCmdRes);
	    // when(instance.getSSHUtil().executeCommand(lDevlWorkspaceCommand)).thenReturn(lCmdRes);
	    // when(instance.getSSHUtil().executeCommand(lExportCommand)).thenReturn(lCmdRes);

	    instance.checkin(pUser, pImplementation.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testCheckin1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	CheckoutSegments lSegment = pImplementation.getCheckoutSegmentsList().get(0);
	lSegment.setReviewStatus(Boolean.TRUE);
	List<CheckoutSegments> lSegments = new ArrayList();
	lSegments.add(lSegment);
	String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (pImplementation.getId() + "_" + "apo").toLowerCase();
	String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (pImplementation.getId() + "_" + "apo").toLowerCase();
	String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + pUser.getId() + "/projects/" + (pImplementation.getId() + "/" + "apo").toLowerCase() + " \"" + (pImplementation.getId() + "_" + "apo").toLowerCase() + "\" \"" + instance.getGITConfig().getServiceUserID() + "\"";
	JSONResponse lCmdRes = new JSONResponse();
	JSONResponse lDvlRes = new JSONResponse();
	JSONResponse lExpRes = new JSONResponse();
	lCmdRes.setStatus(false);
	lDvlRes.setStatus(false);
	lExpRes.setStatus(false);
	lCmdRes.setErrorMessage("test");
	lDvlRes.setErrorMessage("test");
	lExpRes.setErrorMessage("test");

	// DeveloperService instance = spy(new DeveloperService());
	// ReflectionTestUtils.setField(instance, "implementationDAO",
	// mock(ImplementationDAO.class));
	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(pImplementation.getId())).thenReturn(lSegments);
	    // when(instance.getSSHUtil().executeCommand(lCheckInCommand)).thenReturn(lCmdRes);
	    // when(instance.getSSHUtil().executeCommand(lDevlWorkspaceCommand)).thenReturn(lCmdRes);
	    // when(instance.getSSHUtil().executeCommand(lExportCommand)).thenReturn(lCmdRes);

	    instance.checkin(pUser, pImplementation.getId());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testGetLatest() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	CheckoutSegments lSegment = pImplementation.getCheckoutSegmentsList().get(0);
	lSegment.setReviewStatus(Boolean.TRUE);
	List<CheckoutSegments> lSegments = new ArrayList();
	lSegments.add(lSegment);
	String pType = "test";
	String lCommand = Constants.SystemScripts.GET_LATEST.getScript() + " " + (pImplementation.getId() + "_" + "apo").toLowerCase() + " " + pType + " " + pImplementation.getPlanId().getId().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(false);
	lCmdRes.setErrorMessage("temp ERROR: test");

	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(pImplementation.getId())).thenReturn(lSegments);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdRes);

	    instance.getLatest(pUser, pImplementation.getId(), pType);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testGetLatest1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	CheckoutSegments lSegment = pImplementation.getCheckoutSegmentsList().get(0);
	lSegment.setReviewStatus(Boolean.TRUE);
	List<CheckoutSegments> lSegments = new ArrayList();
	lSegments.add(lSegment);
	String pType = "test";
	String lCommand = Constants.SystemScripts.GET_LATEST.getScript() + " " + (pImplementation.getId() + "_" + "apo").toLowerCase() + " " + pType + " " + pImplementation.getPlanId().getId().toLowerCase();
	JSONResponse lCmdRes = new JSONResponse();
	lCmdRes.setStatus(true);
	lCmdRes.setErrorMessage("ERROR: test");

	try {
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(pImplementation.getId())).thenReturn(lSegments);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdRes);

	    instance.getLatest(pUser, pImplementation.getId(), pType);
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.getGitSearchResult();
	SortedSet<String> lTargetList = new TreeSet();
	lTargetList.add("APO");
	lTargetList.add("PRE");
	lTargetList.add("PGR");
	lGitSearchResult.setTargetSystems(lTargetList);
	pSearchResults.add(DataWareHouse.getGitSearchResult());
	List<System> lSystemList = DataWareHouse.systemList;
	PutLevel lPutLevel = lSysLoads.get(0).getPutLevelId();
	System lSystem = lSysLoads.get(0).getSystemId();
	// src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000
	String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + "src/lnaj.asm" + " " + lPutLevel.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setData("lnaj.mak");
	String lRepoName = DataWareHouse.RepoName;
	String lWorkspaceCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lWkResponse = new JSONResponse();
	lWkResponse.setStatus(Boolean.TRUE);
	List<GitSearchResult> pTempSearchResults = new ArrayList();
	GitSearchResult lTempGitSearchResult = new GitSearchResult();
	BeanUtils.copyProperties(lGitSearchResult, lTempGitSearchResult);
	GitBranchSearchResult lBranch = lTempGitSearchResult.getBranch().get(0);
	lBranch.setFuncArea("ibm_put12a.git");
	lBranch.setTargetSystem("apo");
	lBranch.setRefStatus("online");
	pTempSearchResults.add(lTempGitSearchResult);
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(lTempGitSearchResult, lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	pTempSearchResults1.add(lTempGitSearchResult1);
	Set<String> lBranchList = new HashSet<>();
	lBranchList.add("master" + "_" + lSystem.getName().toLowerCase());
	lBranchList.add(pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase());
	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", mock(SSHClientUtils.class));
	    Map<String, Boolean> lBranchStatus = new HashMap<>();
	    lBranchStatus.put("branchKey", true);
	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByPlatform(lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(0).getSystemId().getDefalutPutLevel())).thenReturn(lPutLevel);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(1).getSystemId().getDefalutPutLevel())).thenReturn(null);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(Matchers.anyObject())).thenReturn(lCmdResponse);
	    when(instance.getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);
	    when(instance.getSystemLoadDAO().find(pImplementation.getPlanId(), lSystem)).thenReturn(null);
	    when(instance.getsSHClientUtils().executeCommand(lSystem, "${MTP_ENV}/mtpgitpopulate src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000")).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().createBranches(lPlan.getId(), lBranchList, "tp")).thenReturn(lBranchStatus);

	    String lCommand1 = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + lPlan.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	    when(instance.getsSHClientUtils().executeCommand(pUser, lSystem, "${MTP_ENV}/mtpgitcreateworkspace /tpf/tp/source/t1700484.git _apo")).thenReturn(lCmdResponse);
	    instance.populateIBMSegment(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(instance.getSSHUtil());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	SortedSet<String> lTargetList = new TreeSet();
	lTargetList.add("APO");
	lTargetList.add("PRE");
	lTargetList.add("PGR");
	lGitSearchResult.setTargetSystems(lTargetList);
	pSearchResults.add(lGitSearchResult);
	List<System> lSystemList = DataWareHouse.getSystemList();
	PutLevel lPutLevel = lSysLoads.get(1).getPutLevelId();
	System lSystem = lSysLoads.get(1).getSystemId();
	// src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000
	String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + "src/lnaj.asm" + " " + lPutLevel.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setData("lnaj.mak");
	String lRepoName = DataWareHouse.RepoName;
	String lWorkspaceCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lWkResponse = new JSONResponse();
	lWkResponse.setStatus(Boolean.TRUE);
	List<GitSearchResult> pTempSearchResults = new ArrayList();
	GitSearchResult lTempGitSearchResult = new GitSearchResult();
	BeanUtils.copyProperties(lGitSearchResult, lTempGitSearchResult);
	GitBranchSearchResult lBranch = lTempGitSearchResult.getBranch().get(0);
	lBranch.setFuncArea("ibm_put12a.git");
	lBranch.setTargetSystem("apo");
	lBranch.setRefStatus("online");
	pTempSearchResults.add(lTempGitSearchResult);
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(lTempGitSearchResult, lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	pTempSearchResults1.add(lTempGitSearchResult1);

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByPlatform(lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(0).getSystemId().getDefalutPutLevel())).thenReturn(null);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(1).getSystemId().getDefalutPutLevel())).thenReturn(lPutLevel);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(Matchers.anyObject())).thenReturn(lCmdResponse);
	    when(instance.getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);
	    when(instance.getSystemLoadDAO().find(pImplementation.getPlanId(), lSystem)).thenReturn(null);
	    instance.populateIBMSegment(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(instance.getSSHUtil());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment3() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	SortedSet<String> lTargetList = new TreeSet();
	lTargetList.add("APO");
	lTargetList.add("PRE");
	lTargetList.add("PGR");
	lGitSearchResult.setTargetSystems(lTargetList);
	pSearchResults.add(lGitSearchResult);
	List<System> lSystemList = DataWareHouse.getSystemList();
	PutLevel lPutLevel = lSysLoads.get(1).getPutLevelId();
	System lSystem = lSysLoads.get(1).getSystemId();
	// src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000
	String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + "src/lnaj.asm" + " " + lPutLevel.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setData("lnaj.mak");
	String lRepoName = DataWareHouse.RepoName;
	String lWorkspaceCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lWkResponse = new JSONResponse();
	lWkResponse.setStatus(Boolean.TRUE);
	List<GitSearchResult> pTempSearchResults = new ArrayList();
	GitSearchResult lTempGitSearchResult = new GitSearchResult();
	BeanUtils.copyProperties(lGitSearchResult, lTempGitSearchResult);
	GitBranchSearchResult lBranch = lTempGitSearchResult.getBranch().get(0);
	lBranch.setFuncArea("ibm_put12a.git");
	lBranch.setTargetSystem("apo");
	lBranch.setRefStatus("online");
	pTempSearchResults.add(lTempGitSearchResult);
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(lTempGitSearchResult, lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	pTempSearchResults1.add(lTempGitSearchResult1);

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByPlatform(lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(0).getSystemId().getDefalutPutLevel())).thenReturn(null);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(1).getSystemId().getDefalutPutLevel())).thenReturn(lPutLevel);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(Matchers.anyObject())).thenReturn(lCmdResponse);
	    when(instance.getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);
	    when(instance.getSystemLoadDAO().find(pImplementation.getPlanId(), lSystem)).thenReturn(null);
	    JGitClientUtils jGitClientUtils = instance.getJGitClientUtils();
	    Mockito.doThrow(Exception.class).when(jGitClientUtils).isRepositoryExist(lPutLevel.getScmUrl());
	    instance.populateIBMSegment(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(instance.getSSHUtil());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment4() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	SortedSet<String> lTargetList = new TreeSet();
	lTargetList.add("APO");
	lTargetList.add("PRE");
	lTargetList.add("PGR");
	lGitSearchResult.setTargetSystems(lTargetList);
	pSearchResults.add(lGitSearchResult);
	List<System> lSystemList = DataWareHouse.getSystemList();
	PutLevel lPutLevel = lSysLoads.get(1).getPutLevelId();
	System lSystem = lSysLoads.get(1).getSystemId();
	// src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000
	String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + "src/lnaj.asm" + " " + lPutLevel.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setData("lnaj.mak");
	String lRepoName = DataWareHouse.RepoName;
	String lWorkspaceCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lWkResponse = new JSONResponse();
	lWkResponse.setStatus(Boolean.TRUE);
	List<GitSearchResult> pTempSearchResults = new ArrayList();
	GitSearchResult lTempGitSearchResult = new GitSearchResult();
	BeanUtils.copyProperties(lGitSearchResult, lTempGitSearchResult);
	GitBranchSearchResult lBranch = lTempGitSearchResult.getBranch().get(0);
	lBranch.setFuncArea("ibm_put12a.git");
	lBranch.setTargetSystem("apo");
	lBranch.setRefStatus("online");
	pTempSearchResults.add(lTempGitSearchResult);
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(lTempGitSearchResult, lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	pTempSearchResults1.add(lTempGitSearchResult1);

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByPlatform(lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(0).getSystemId().getDefalutPutLevel())).thenReturn(null);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(1).getSystemId().getDefalutPutLevel())).thenReturn(lPutLevel);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(Matchers.anyObject())).thenReturn(lCmdResponse);
	    when(instance.getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);
	    when(instance.getSystemLoadDAO().find(pImplementation.getPlanId(), lSystem)).thenReturn(null);
	    when(instance.getJGitClientUtils().isRepositoryExist(lPutLevel.getScmUrl())).thenReturn(false);
	    instance.populateIBMSegment(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(instance.getSSHUtil());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment5() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	SortedSet<String> lTargetList = new TreeSet();
	lTargetList.add("APO");
	lTargetList.add("PRE");
	lTargetList.add("PGR");
	lGitSearchResult.setTargetSystems(lTargetList);
	pSearchResults.add(lGitSearchResult);
	List<System> lSystemList = DataWareHouse.getSystemList();
	PutLevel lPutLevel = lSysLoads.get(1).getPutLevelId();
	System lSystem = lSysLoads.get(1).getSystemId();
	// src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000
	String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + "src/lnaj.asm" + " " + lPutLevel.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.TRUE);
	lCmdResponse.setData("lnaj.mak");
	String lRepoName = DataWareHouse.RepoName;
	String lWorkspaceCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lWkResponse = new JSONResponse();
	lWkResponse.setStatus(Boolean.TRUE);
	List<GitSearchResult> pTempSearchResults = new ArrayList();
	GitSearchResult lTempGitSearchResult = new GitSearchResult();
	BeanUtils.copyProperties(lGitSearchResult, lTempGitSearchResult);
	GitBranchSearchResult lBranch = lTempGitSearchResult.getBranch().get(0);
	lBranch.setFuncArea("ibm_put12a.git");
	lBranch.setTargetSystem("apo");
	lBranch.setRefStatus("online");
	pTempSearchResults.add(lTempGitSearchResult);
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(lTempGitSearchResult, lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	pTempSearchResults1.add(lTempGitSearchResult1);

	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByPlatform(lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(0).getSystemId().getDefalutPutLevel())).thenReturn(null);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(1).getSystemId().getDefalutPutLevel())).thenReturn(lPutLevel);
	    // when(instance.getSSHUtil().connectSSH(lSystem)).thenReturn(false);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId())).thenReturn(lRepoName);
	    // when(instance.getSSHUtil().executeCommand(Matchers.anyObject())).thenReturn(lCmdResponse);
	    when(instance.getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);
	    when(instance.getSystemLoadDAO().find(pImplementation.getPlanId(), lSystem)).thenReturn(null);
	    instance.populateIBMSegment(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(instance.getSSHUtil());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testPopulateIBMSegment6() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	List<SystemLoad> lSysLoads = lPlan.getSystemLoadList();
	Implementation pImplementation = lPlan.getImplementationList().get(0);
	pImplementation.getPlanId().setMacroHeader(Boolean.FALSE);
	List<GitSearchResult> pSearchResults = new ArrayList();
	GitSearchResult lGitSearchResult = DataWareHouse.refreshGitSearchResult();
	SortedSet<String> lTargetList = new TreeSet();
	lTargetList.add("APO");
	lTargetList.add("PRE");
	lTargetList.add("PGR");
	lGitSearchResult.setTargetSystems(lTargetList);
	pSearchResults.add(lGitSearchResult);
	List<System> lSystemList = DataWareHouse.getSystemList();
	PutLevel lPutLevel = lSysLoads.get(1).getPutLevelId();
	System lSystem = lSysLoads.get(1).getSystemId();
	// src/lnaj.asm tpf/tp/ibm/ibm_put12a.git master_apo 20170829042000
	String lCommand = Constants.GitScripts.IBM_POPULATE.getScript() + "src/lnaj.asm" + " " + lPutLevel.getScmUrl() + " " + Constants.BRANCH_MASTER + lSystem.getName().toLowerCase() + " " + Constants.JENKINS_DATEFORMAT.get().format(lPutLevel.getPutDateTime());
	JSONResponse lCmdResponse = new JSONResponse();
	lCmdResponse.setStatus(Boolean.FALSE);
	lCmdResponse.setErrorMessage("already");
	String lRepoName = DataWareHouse.RepoName;
	String lWorkspaceCommand = Constants.SystemScripts.CREATE_WORKSPACE.getScript() + lRepoName + " " + pImplementation.getId().toLowerCase() + "_" + lSystem.getName().toLowerCase();
	JSONResponse lWkResponse = new JSONResponse();
	lWkResponse.setStatus(Boolean.TRUE);
	List<GitSearchResult> pTempSearchResults = new ArrayList();
	GitSearchResult lTempGitSearchResult = new GitSearchResult();
	BeanUtils.copyProperties(lGitSearchResult, lTempGitSearchResult);
	GitBranchSearchResult lBranch = lTempGitSearchResult.getBranch().get(0);
	lBranch.setFuncArea("ibm_put12a.git");
	lBranch.setTargetSystem("apo");
	lBranch.setRefStatus("online");
	pTempSearchResults.add(lTempGitSearchResult);
	List<GitSearchResult> pTempSearchResults1 = new ArrayList();
	GitSearchResult lTempGitSearchResult1 = new GitSearchResult();
	BeanUtils.copyProperties(lTempGitSearchResult, lTempGitSearchResult1);
	lTempGitSearchResult1.setFileName("lnaj.mak");
	pTempSearchResults1.add(lTempGitSearchResult1);
	Set<String> lBranches = new HashSet();
	lBranches.add("_pre");
	lBranches.add("master_pre");
	Map<String, Boolean> lReturnBranch = new HashMap();
	lReturnBranch.put("PRE", false);
	try {
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));

	    when(instance.getMailMessageFactory().getTemplate(NewTargetSystemMail.class)).thenReturn(mock(NewTargetSystemMail.class));
	    when(instance.getImplementationDAO().find(pImplementation.getId())).thenReturn(pImplementation);
	    when(instance.getSystemDAO().findByPlatform(lPlan.getSystemLoadList().get(0).getSystemId().getPlatformId())).thenReturn(lSystemList);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(0).getSystemId().getDefalutPutLevel())).thenReturn(null);
	    when(instance.getPutLevelDAO().find(lSysLoads.get(1).getSystemId().getDefalutPutLevel())).thenReturn(lPutLevel);
	    // when(instance.getSSHUtil().executeCommand(lCommand)).thenReturn(lCmdResponse);
	    when(instance.getJGitClientUtils().getPlanRepoName(lSystem.getPlatformId().getNickName(), lPlan.getId())).thenReturn(lRepoName);
	    when(instance.getjGITSearchUtils().SearchAllRepos(lSystem.getPlatformId().getNickName().toLowerCase(), "lnaj.asm", pImplementation.getPlanId().getMacroHeader(), Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults);
	    when(instance.getjGITSearchUtils().SearchAllRepos("tp", "lnaj.mak", Boolean.FALSE, Constants.PRODSearchType.ONLINE_ONLY, null)).thenReturn(pTempSearchResults1);
	    when(instance.getSystemLoadDAO().find(pImplementation.getPlanId(), lSystem)).thenReturn(null);
	    when(instance.getJGitClientUtils().createBranches(lPlan.getId(), lBranches, lSystem.getPlatformId().getNickName())).thenReturn(lReturnBranch);
	    instance.populateIBMSegment(pUser, pImplementation.getId(), pSearchResults);
	    // Mockito.reset(instance.getSSHUtil());
	} catch (Exception ex) {
	    assertTrue(true);
	}

    }

    @Test
    public void testSendNotificationOnCheckIn1() {
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.getMailMessageFactory(), "mailQueue", mock(BlockingQueue.class));
	when(instance.getImplementationDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getImplementationList().get(0));
	when(instance.getCheckoutSegmentsDAO().getSameSegmentDevelopersByImpId(DataWareHouse.getPlan().getId(), new ArrayList<>(Constants.PlanStatus.getNonProdStatusMap().keySet()))).thenReturn(DataWareHouse.getPlan().getCheckoutSegmentsList());
	when(instance.getMailMessageFactory().getTemplate(CheckInMail.class)).thenReturn(mock(CheckInMail.class));

	try {
	    ReflectionTestUtils.invokeMethod(instance, "sendNotificationOnCheckIn", DataWareHouse.getPlan().getId());
	} catch (Exception e) {

	}
    }

}
