package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.mail.PendingActionNotificationMail;
import com.tsi.workflow.service.ReviewerService;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class ReviewAndApprovePlanMonitorTest {

    ReviewAndApprovePlanMonitor instance;
    List<String> lImplementationList;
    String string;
    Implementation lImpl;
    List<Object[]> lpendingReview;
    Object[] obj;
    PendingActionNotificationMail reviewerMail;
    BlockingQueue<MailMessage> blockingQueue;
    List<ImpPlan> devList;
    List<Object[]> lapproveList;

    @Before
    public void setUp() throws Exception {
	ReviewAndApprovePlanMonitor reviewAndApprovePlanMonitor = new ReviewAndApprovePlanMonitor();
	instance = Mockito.spy(reviewAndApprovePlanMonitor);

	ReflectionTestUtils.setField(instance, "reviewerService", Mockito.mock(ReviewerService.class));
	ReflectionTestUtils.setField(instance, "lLdapUserMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", Mockito.mock(LDAPAuthenticatorImpl.class));

	lImplementationList = new ArrayList<String>();
	string = DataWareHouse.getPlan().getId();
	lImplementationList.add(string);
	lImpl = DataWareHouse.getPlan().getImplementationList().get(0);
	lpendingReview = new ArrayList<>();
	obj = new Object[1];
	obj[0] = DataWareHouse.getPlan().getImplementationList().get(0).getId();
	lpendingReview.add(obj);
	reviewerMail = Mockito.mock(PendingActionNotificationMail.class);
	blockingQueue = Mockito.mock(BlockingQueue.class);
	devList = new ArrayList<>();
	devList.add(DataWareHouse.getPlan());
	lapproveList = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoMonitor() {
	Mockito.when(instance.getImplementationDAO().findAllPlansAndReviewers()).thenReturn(lImplementationList);
	Mockito.when(instance.getImplementationDAO().find(string)).thenReturn(lImpl);
	Mockito.when(instance.lDAPAuthenticatorImpl.getServiceUser()).thenReturn(DataWareHouse.getUser());
	Mockito.when(instance.getImplementationDAO().findByReviewerBasedOnTime(lImpl.getPeerReviewers(), Boolean.TRUE)).thenReturn(lpendingReview);
	Mockito.when(instance.getImplementationDAO().find(DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(lImpl);
	Mockito.when(instance.getMailMessageFactory().getTemplate(PendingActionNotificationMail.class)).thenReturn(reviewerMail);
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	Mockito.doNothing().when(instance.implementationDAO).update(DataWareHouse.getUser(), lImpl);
	Mockito.when(instance.getImpPlanDAO().passedAcceptanceList(Mockito.anyList())).thenReturn(devList);
	Object[] obj1 = new Object[1];
	obj1[0] = DataWareHouse.getPlan().getId();
	lapproveList.add(obj1);
	Mockito.when(instance.getImpPlanDAO().getToBeApprovedList(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name(), "prabhu.prabhakaran", Boolean.TRUE)).thenReturn(lapproveList);
	Mockito.when(instance.getImpPlanDAO().getPlansByMacroHeaderBasedOnTime("prabhu.prabhakaran", Constants.PlanStatus.APPROVED.name(), Boolean.TRUE, Boolean.TRUE)).thenReturn(lapproveList);
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	Mockito.when(instance.getImplementationDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor1() {
	Mockito.when(instance.getImplementationDAO().findAllPlansAndReviewers()).thenReturn(lImplementationList);
	lImpl.setPeerReviewers("abcd,efgs");
	Mockito.when(instance.getImplementationDAO().find(string)).thenReturn(lImpl);
	Mockito.when(instance.lDAPAuthenticatorImpl.getServiceUser()).thenReturn(DataWareHouse.getUser());
	Mockito.when(instance.getImplementationDAO().findByReviewerBasedOnTime(lImpl.getPeerReviewers(), Boolean.TRUE)).thenReturn(lpendingReview);
	Mockito.when(instance.getImplementationDAO().find(DataWareHouse.getPlan().getImplementationList().get(0).getId())).thenReturn(lImpl);
	Mockito.when(instance.getMailMessageFactory().getTemplate(PendingActionNotificationMail.class)).thenReturn(reviewerMail);
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	Mockito.doNothing().when(instance.implementationDAO).update(DataWareHouse.getUser(), lImpl);
	Mockito.when(instance.getImpPlanDAO().passedAcceptanceList(Mockito.anyList())).thenReturn(devList);
	Object[] obj1 = new Object[1];
	obj1[0] = DataWareHouse.getPlan().getId();
	lapproveList.add(obj1);
	Mockito.when(instance.getImpPlanDAO().getToBeApprovedList(Constants.PlanStatus.PASSED_ACCEPTANCE_TESTING.name(), "prabhu.prabhakaran", Boolean.TRUE)).thenReturn(lapproveList);
	Mockito.when(instance.getImpPlanDAO().getPlansByMacroHeaderBasedOnTime("prabhu.prabhakaran", Constants.PlanStatus.APPROVED.name(), Boolean.TRUE, Boolean.TRUE)).thenReturn(lapproveList);
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	Mockito.when(instance.getImplementationDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	instance.doMonitor();
    }

    @Test
    public void test() {
	assertNotNull(instance.getSystemLoadDAO());
	assertNotNull(instance.getReviewerService());
    }
}
