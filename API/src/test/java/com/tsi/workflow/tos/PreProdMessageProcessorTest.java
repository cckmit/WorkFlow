package com.tsi.workflow.tos;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.activity.PreTOSActivityMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRStatusUpdateinNASTest;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.mail.PreProdLoadsetActivationMail;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class PreProdMessageProcessorTest {

    PreProdMessageProcessor instance;

    @Before
    public void setUp() throws Exception {
	try {
	    PreProdMessageProcessor preProdMessageProcessor = new PreProdMessageProcessor();
	    instance = Mockito.spy(preProdMessageProcessor);
	    TestCaseMockService.doMockDAO(instance, TOSConfig.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);
	    TestCaseMockService.doMockDAO(instance, SystemCpuDAO.class);
	    TestCaseMockService.doMockDAO(instance, PreProductionLoadsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, WSMessagePublisher.class);
	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	    TestCaseMockService.doMockDAO(instance, TOSHelper.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, BuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, FallbackHelper.class);
	    TestCaseMockService.doMockDAO(instance, PRStatusUpdateinNASTest.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadActionsDAO.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, SSHClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    TestCaseMockService.doMockDAO(instance, PlanHelper.class);
	    TestCaseMockService.doMockDAO(instance, FTPHelper.class);
	    TestCaseMockService.doMockDAO(instance, JenkinsClient.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, OnlineBuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    // TestCaseMockService.doMockDAO(instance, TSDService.class);
	} catch (Exception ex) {
	    Logger.getLogger(PreProdMessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
	    // fail("Fail on Exception " + ex.getMessage());
	}
    }

    @Test
    public void test() {
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", Mockito.mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", Mockito.mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", Mockito.mock(SystemCpuDAO.class));
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));
	JSONResponse response = null;
	PreProductionLoads preProductionLoads = Mockito.mock(PreProductionLoads.class);
	TOSResult tOSResult = DataWareHouse.getTOSResult();
	Mockito.when(instance.preProductionLoadsDAO.find(tOSResult.getId())).thenReturn(preProductionLoads);
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	Mockito.when(instance.implementationDAO.findByImpPlan(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan().getImplementationList());
	Mockito.doNothing().when(instance.preProductionLoadsDAO).update(DataWareHouse.getUser(), DataWareHouse.getPreProductionLoads());
	tOSResult.setCommand("AUTOLOAD");
	assertNotNull(instance.processPreProductionTOSMessage(tOSResult));
	Mockito.when(instance.mailMessageFactory.getTemplate(PreProdLoadsetActivationMail.class)).thenReturn(new PreProdLoadsetActivationMail());
	Set<TosActionQueue> lPPQueue = new HashSet<TosActionQueue>();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	lPPQueue.add(tosActionQueue);
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(null, null, null));

	Integer retvalue = instance.processPreProductionTOSMessage(tOSResult);
    }

}
