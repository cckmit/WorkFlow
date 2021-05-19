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
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.git.GitBranchSearchResult;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.mail.DeveloperNotificationMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class ReviewerServiceTest {

    ReviewerService instance;

    public ReviewerServiceTest() {
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
	    ReviewerService realInstance = new ReviewerService();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);

	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, GITSSHUtils.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);

	} catch (Exception ex) {
	    Logger.getLogger(ReviewerServiceTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReviewerService() throws Exception {
	// TestCaseExecutor.doTest(instance, ReviewerService.class);
    }

    @Test
    public void testReviewSegments() {

	ReviewerService instance = new ReviewerService();
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", Mockito.mock(CheckoutSegmentsDAO.class));
	List<GitSearchResult> pSegments = new ArrayList<>();
	GitSearchResult gitSearchResult = new GitSearchResult();
	gitSearchResult.setFileName("123");

	pSegments.add(gitSearchResult);

	List<GitBranchSearchResult> lBranch = new LinkedList<>();
	gitSearchResult.setBranch(lBranch);
	GitBranchSearchResult git = new GitBranchSearchResult();
	git.setFuncArea("123");
	git.setTargetSystem("pgr");
	lBranch.add(git);

	// Mockito.when(gitSearchResult.getBranch()).thenReturn(lBranch);
	CheckoutSegments checkoutSegments = new CheckoutSegments();
	checkoutSegments.setTargetSystem("pgr");

	Mockito.when(instance.getCheckoutSegmentsDAO().findByFileName(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(checkoutSegments);
	try {
	    instance.reviewSegments(null, "", pSegments);

	} catch (Exception e) {
	}

    }

    @Test
    public void testReviewSegments1() {

	ReviewerService instance = new ReviewerService();
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", Mockito.mock(CheckoutSegmentsDAO.class));
	List<GitSearchResult> pSegments = new ArrayList<>();
	GitSearchResult gitSearchResult = new GitSearchResult();
	gitSearchResult.setFileName("123");

	pSegments.add(gitSearchResult);

	List<GitBranchSearchResult> lBranch = new LinkedList<>();
	gitSearchResult.setBranch(lBranch);
	GitBranchSearchResult git = new GitBranchSearchResult();
	git.setFuncArea("123");
	git.setTargetSystem("pgr");
	lBranch.add(git);

	// Mockito.when(gitSearchResult.getBranch()).thenReturn(lBranch);
	CheckoutSegments checkoutSegments = new CheckoutSegments();
	checkoutSegments.setTargetSystem("pgr");

	Mockito.when(instance.getCheckoutSegmentsDAO().findByFileName(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(null);
	try {
	    instance.reviewSegments(null, "", pSegments);

	} catch (Exception e) {
	}

    }

    @Test
    public void testApproveReview() {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(Boolean.TRUE);
	    ReviewerService instance = new ReviewerService();
	    ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", Mockito.mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "jGitClientUtils", Mockito.mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    Mockito.reset(instance.getsSHClientUtils());
	    String lRepoName = DataWareHouse.RepoName;
	    List<String> lBranchList = DataWareHouse.getBranchList();
	    when(instance.implementationDAO.find(DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getImplementationList().get(0));
	    when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	    when(instance.jGitClientUtils.getPlanRepoName("tp", DataWareHouse.getPlan().getId().toLowerCase())).thenReturn(lRepoName);
	    when(instance.jGitClientUtils.getAllBranchList("tp", DataWareHouse.getPlan().getId())).thenReturn(lBranchList);
	    when(instance.getsSHClientUtils().addImplementationTag(lRepoName, Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED, lBranchList)).thenReturn(Boolean.TRUE);
	    DeveloperNotificationMail mail = new DeveloperNotificationMail();
	    when(instance.mailMessageFactory.getTemplate(DeveloperNotificationMail.class)).thenReturn(mock(DeveloperNotificationMail.class));
	    mail.setImplementationId(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    Implementation lImplementation = new Implementation();
	    lImplementation.setPeerReview("Y");
	    lImplementation.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	    instance.approveReview(DataWareHouse.getUser(), DataWareHouse.getPlan().getImplementationList().get(0).getId());
	} catch (Exception e) {
	    System.out.println(e);
	}

    }

    @Test
    public void testApproveReview1() {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(Boolean.TRUE);
	    ReviewerService instance = new ReviewerService();
	    ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", Mockito.mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "jGitClientUtils", Mockito.mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    Mockito.reset(instance.getsSHClientUtils());
	    String lRepoName = DataWareHouse.RepoName;
	    List<String> lBranchList = DataWareHouse.getBranchList();
	    when(instance.implementationDAO.find(DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getImplementationList().get(0));
	    when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	    when(instance.jGitClientUtils.getPlanRepoName("tp", DataWareHouse.getPlan().getId().toLowerCase())).thenReturn("");
	    when(instance.jGitClientUtils.getAllBranchList("tp", DataWareHouse.getPlan().getId())).thenReturn(lBranchList);
	    when(instance.getsSHClientUtils().addImplementationTag("", Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED, lBranchList)).thenReturn(Boolean.FALSE);
	    DeveloperNotificationMail mail = new DeveloperNotificationMail();
	    when(instance.mailMessageFactory.getTemplate(DeveloperNotificationMail.class)).thenReturn(mock(DeveloperNotificationMail.class));
	    mail.setImplementationId(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    Implementation lImplementation = new Implementation();
	    lImplementation.setPeerReview("Y");
	    lImplementation.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	    instance.approveReview(DataWareHouse.getUser(), DataWareHouse.getPlan().getImplementationList().get(0).getId());
	} catch (Exception e) {
	    System.out.println(e);
	}

    }

    @Test
    public void testApproveReview2() {
	try {
	    JSONResponse lResponse = new JSONResponse();
	    lResponse.setStatus(Boolean.TRUE);
	    ReviewerService instance = new ReviewerService();
	    ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "sSHClientUtils", Mockito.mock(SSHClientUtils.class));
	    ReflectionTestUtils.setField(instance, "jGitClientUtils", Mockito.mock(JGitClientUtils.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    Mockito.reset(instance.getsSHClientUtils());
	    String lRepoName = DataWareHouse.RepoName;
	    List<String> lBranchList = DataWareHouse.getBranchList();
	    when(instance.implementationDAO.find(DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(DataWareHouse.getPlan().getImplementationList().get(0));
	    when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	    when(instance.jGitClientUtils.getPlanRepoName("tp", DataWareHouse.getPlan().getId().toLowerCase())).thenReturn("");
	    when(instance.jGitClientUtils.getAllBranchList("tp", DataWareHouse.getPlan().getId())).thenReturn(lBranchList);
	    when(instance.getsSHClientUtils().addImplementationTag("", Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED, lBranchList)).thenReturn(Boolean.FALSE);
	    DeveloperNotificationMail mail = new DeveloperNotificationMail();
	    when(instance.mailMessageFactory.getTemplate(DeveloperNotificationMail.class)).thenReturn(mock(DeveloperNotificationMail.class));
	    mail.setImplementationId(DataWareHouse.getPlan().getImplementationList().get(0).getId());
	    Implementation lImplementation = new Implementation();
	    lImplementation.setPeerReview("Y");
	    lImplementation.setSubstatus(Constants.ImplementationSubStatus.PEER_REVIEW_COMPLETED.name());
	    instance.approveReview(DataWareHouse.getUser(), "test");
	} catch (Exception e) {
	    System.out.println(e);
	}

    }

    @Test
    public void testGetUserTaskList() {
	ReviewerService instance = new ReviewerService();
	ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(user);
	LinkedHashMap<String, String> pOrderBy = new LinkedHashMap();
	try {
	    when(instance.implementationDAO.countByReviewer(user.getCurrentOrDelagateUser().getId())).thenReturn(new Long(0));
	    when(instance.implementationDAO.findByReviewer(user.getCurrentOrDelagateUser().getId(), 0, 10, pOrderBy)).thenReturn(DataWareHouse.getPlan().getImplementationList());
	    instance.getUserTaskList(user, 0, 10, pOrderBy);

	    when(instance.implementationDAO.countByReviewerHistory(user.getCurrentOrDelagateUser().getId())).thenReturn(new Long(0));
	    when(instance.implementationDAO.findByReviewerHistory(user.getCurrentOrDelagateUser().getId(), 0, 10, pOrderBy)).thenReturn(DataWareHouse.getPlan().getImplementationList());
	    instance.getUserTaskListHistory(user, 0, 10, pOrderBy);

	} catch (Exception ex) {
	    System.out.println(ex);
	}

    }

}
