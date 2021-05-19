package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.ui.ProdDeactivateResult;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.mail.TSDPlanDeactivateMail;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class TSDPlanDeactivateMonitorTest {

    TSDPlanDeactivateMonitor instance;
    List<ProdDeactivateResult> impPlanAndLoadList;
    ProdDeactivateResult prodDeactivateResult;
    ImpPlan impPlan;
    User user;
    TSDPlanDeactivateMail tsdPlanDeactivateMail;
    BlockingQueue<MailMessage> blockingQueue;

    @Before
    public void setUp() throws Exception {
	TSDPlanDeactivateMonitor tsdPlanDeactivateMonitor = new TSDPlanDeactivateMonitor();
	instance = Mockito.spy(tsdPlanDeactivateMonitor);
	ReflectionTestUtils.setField(instance, "wFConfig", Mockito.mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", Mockito.mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));

	impPlanAndLoadList = new ArrayList();
	prodDeactivateResult = Mockito.mock(ProdDeactivateResult.class);
	impPlan = Mockito.mock(ImpPlan.class);
	user = Mockito.mock(User.class);
	tsdPlanDeactivateMail = Mockito.mock(TSDPlanDeactivateMail.class);
	impPlanAndLoadList.add(prodDeactivateResult);
	blockingQueue = Mockito.mock(BlockingQueue.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPlanFromProductionLoadDeactivateCheckProcess() {
	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	Mockito.when(instance.impPlanDAO.getImpPlanFromProductionLoadDeactivate(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name())).thenReturn(impPlanAndLoadList);
	Mockito.when(prodDeactivateResult.getReviewers()).thenReturn("ABCD,EFGH");
	Mockito.when(prodDeactivateResult.getSysname()).thenReturn("APO,WSP");
	Mockito.when(prodDeactivateResult.getLeadid()).thenReturn(DataWareHouse.getPlan().getLeadId());
	Mockito.when(prodDeactivateResult.getDeveloperid()).thenReturn(DataWareHouse.getPlan().getImplementationList().get(0).getDevId());
	Mockito.when(prodDeactivateResult.getPlanid()).thenReturn(DataWareHouse.getPlan().getId());
	Mockito.when(instance.impPlanDAO.find(prodDeactivateResult.getPlanid())).thenReturn(impPlan);
	Mockito.when(instance.lDAPAuthenticatorImpl.getServiceUser()).thenReturn(user);
	Mockito.when(impPlan.getDeactivateTSDMailFlag()).thenReturn(false);
	Mockito.when(instance.getMailMessageFactory().getTemplate(TSDPlanDeactivateMail.class)).thenReturn(tsdPlanDeactivateMail);
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	Mockito.doNothing().when(instance.impPlanDAO).update(user, impPlan);
	Mockito.when(instance.impPlanDAO.getDependentPlanFromDeactivatePlanList(new ArrayList(Constants.PlanStatus.getPlanStatus().keySet()), prodDeactivateResult.getPlanid())).thenReturn(impPlanAndLoadList);
	instance.planFromProductionLoadDeactivateCheckProcess();

    }

}
