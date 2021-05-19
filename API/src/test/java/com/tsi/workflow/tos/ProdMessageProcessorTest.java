package com.tsi.workflow.tos;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TOSConfig;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.TOSActivityMessage;
import com.tsi.workflow.base.MailMessage;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemCpuDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.mail.ExceptionSourceContentionMail;
import com.tsi.workflow.mail.ProdLoadsetActivationMail;
import com.tsi.workflow.mail.StackHolderLoadsetMail;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class ProdMessageProcessorTest {

    ProdMessageProcessor instance;

    @Before
    public void setUp() throws Exception {
	try {
	    ProdMessageProcessor ProdMessageProcessor = new ProdMessageProcessor();
	    instance = Mockito.spy(ProdMessageProcessor);
	    TestCaseMockService.doMockDAO(instance, TOSConfig.class);
	    // TestCaseMockService.doMockDAO(instance, SystemCpuDAO.class);
	    TestCaseMockService.doMockDAO(instance, ProductionLoadsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    // TestCaseMockService.doMockDAO(instance, WSMessagePublisher.class);
	    TestCaseMockService.doMockDAO(instance, RejectHelper.class);
	    TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    TestCaseMockService.doMockDAO(instance, FallbackHelper.class);
	    TestCaseMockService.doMockDAO(instance, PRNumberHelper.class);
	    TestCaseMockService.doMockDAO(instance, BPMClientUtils.class);
	    TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	    TestCaseMockService.doMockDAO(instance, JGitClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, SSHClientUtils.class);
	    // TestCaseMockService.doMockDAO(instance, WFConfig.class);
	    TestCaseMockService.doMockDAO(instance, PlanHelper.class);
	    TestCaseMockService.doMockDAO(instance, JenkinsClient.class);
	    TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	    TestCaseMockService.doMockDAO(instance, OnlineBuildDAO.class);
	    TestCaseMockService.doMockDAO(instance, MailMessageFactory.class);
	    // TestCaseMockService.doMockDAO(instance, TSDService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProdMessageProcessor.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testprocessProductionTOSMessage() throws Exception {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", Mockito.mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "lProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "systemCpuDAO", Mockito.mock(SystemCpuDAO.class));

	ProductionLoads ProductionLoads = new ProductionLoads();
	ProductionLoads.setPlanId(DataWareHouse.getPlan());
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_LOADED.name());
	ProductionLoads.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(0));
	Mockito.when(instance.productionLoadsDAO.find(Mockito.anyInt())).thenReturn(ProductionLoads);
	Mockito.doNothing().when(instance.productionLoadsDAO).update(Mockito.any(User.class), Mockito.any(ProductionLoads.class));
	Mockito.doNothing().when(instance.productionLoadsDAO).updateAllActivated(Mockito.any(User.class), Mockito.any(ProductionLoads.class));
	Mockito.doNothing().when(instance.systemLoadDAO).update(Mockito.any(User.class), Mockito.any());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new TOSActivityMessage(null, null, null));
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PROD_LOAD, DataWareHouse.getUser(), new JobDetails());
	Mockito.when(instance.lAsyncPlansStartTimeMap.containsKey(Mockito.any())).thenReturn(true);
	Mockito.when(instance.lAsyncPlansStartTimeMap.remove(Mockito.any())).thenReturn(new Long(1));
	Mockito.when(instance.lAsyncPlansStartTimeMap.get(Mockito.any())).thenReturn(new Long(1));
	TOSResult TOSResult = new TOSResult();
	TOSResult = DataWareHouse.getTOSResult();
	TOSResult.setCommand("AUTOLOAD");
	// Test Case 1: TOSResult.Command : AUTOLOAD , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:FALLBACK_LOADED
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 1.1: TOSResult.Command : AUTOLOAD , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:FALLBACK_LOADED
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 2: TOSResult.Command : AUTOLOAD , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:LOADED
	// ProducationLoad.plan.loadType : Exception
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	ProductionLoads.getPlanId().setLoadType(Constants.LoadTypes.EXCEPTION.name());
	Mockito.doNothing().when(instance.rejectHelper).rejectDependentPlans(DataWareHouse.getUser(), DataWareHouse.getPlan().getId(), Constants.REJECT_REASON.ONLINE.getValue(), Constants.AUTOREJECT_COMMENT.ONLINE.getValue(), Boolean.TRUE, Boolean.TRUE, Boolean.FALSE, null, Boolean.TRUE);
	List<Object[]> list = new ArrayList<Object[]>();
	List<Object> objectList = new ArrayList();
	objectList.add(new String());
	objectList.add(new String("ACTIVE"));
	objectList.add(new String(DataWareHouse.getPlan().getId()));
	list.add(objectList.toArray());
	Mockito.when(instance.impPlanDAO.getPostSegmentRelatedPlans(DataWareHouse.getPlan().getId(), Boolean.FALSE)).thenReturn(list);
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	Mockito.when(instance.mailMessageFactory.getTemplate(ExceptionSourceContentionMail.class)).thenReturn(new ExceptionSourceContentionMail());
	BlockingQueue<MailMessage> blockingQueue = new LinkedBlockingQueue<MailMessage>();
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 3: TOSResult.Command : LOADACT , ProducationLoad.LastAction
	// :Sucess, ACTIVATED
	TOSResult.setCommand("LOADACT");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 3.1: TOSResult.Command : LOADACT , ProducationLoad.LastAction
	// :Sucess, ACTIVATED
	TOSResult.setCommand("LOADACT");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 4: TOSResult.Command : LOADACT , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: LOADED

	LoadCategories categories = new LoadCategories();
	ProductionLoads.setCpuId(new SystemCpu());
	categories.setName("C");
	ProductionLoads.getSystemLoadId().setLoadCategoryId(categories);
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	ProductionLoads.setSystemId(DataWareHouse.getSystemList().get(0));
	List<Object[]> list1 = new ArrayList<Object[]>();
	List<Object> objectList1 = new ArrayList();
	objectList1.add(new String("ACTIVATED"));
	objectList1.add(new String("2"));
	List<Object> objectList2 = new ArrayList();
	objectList2.add(new String("DEACTIVATED"));
	objectList2.add(new String("1"));
	list1.add(objectList1.toArray());
	// list1.add(objectList2.toArray());
	Mockito.when(instance.productionLoadsDAO.getLoadsCountByLoadStatus(ProductionLoads.getPlanId().getId(), ProductionLoads.getSystemId().getId())).thenReturn(list1);
	Mockito.when(instance.productionLoadsDAO.getAllLoadsCountByLoadStatus(ProductionLoads.getPlanId().getId(), ProductionLoads.getSystemId().getId())).thenReturn(new ArrayList<>());
	Mockito.when(instance.systemCpuDAO.findCpusOtherthan(ProductionLoads.getSystemId(), new ArrayList(), Constants.TOSEnvironment.PRODUCTION.name(), false)).thenReturn(new ArrayList());
	Mockito.when(instance.systemCpuDAO.findCpusOtherthan(ProductionLoads.getSystemId(), new ArrayList(), Constants.TOSEnvironment.NATIVE_CPU.name(), false)).thenReturn(new ArrayList());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	list1.add(objectList2.toArray());
	Mockito.when(instance.productionLoadsDAO.getAllLoadsCountByLoadStatus(ProductionLoads.getPlanId().getId(), ProductionLoads.getSystemId().getId())).thenReturn(list1);
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 5: TOSResult.Command : LOADACT , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:FALLBACK_ACTIVATED
	TOSResult.setCommand("LOADACT");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 6: TOSResult.Command : LOADDEA , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:DEACTIVATED
	TOSResult.setCommand("LOADDEA");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 6.1: TOSResult.Command : LOADDEA , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:DEACTIVATED
	TOSResult.setCommand("LOADDEA");
	ProductionLoads.setSystemId(null);
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 6.2: TOSResult.Command : LOADDEA , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status:DEACTIVATED
	TOSResult.setCommand("LOADDEA");
	ProductionLoads.setSystemId(DataWareHouse.getSystemList().get(0));
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 7: TOSResult.Command : LOADDEA , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: FALLBACK_DEACTIVATED
	TOSResult.setCommand("LOADDEA");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_DEACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 8: TOSResult.Command : LOADDEL , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: DELETED
	TOSResult.setCommand("LOADDEL");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.DELETED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 9: TOSResult.Command : LOADDEL , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: FALLBACK_DELETED
	TOSResult.setCommand("LOADDEL");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 9.1: TOSResult.Command : LOADDEL , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: FALLBACK_DELETED
	TOSResult.setCommand("LOADDEL");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_DELETED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 10: TOSResult.Command : LOADACC , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: ACCEPTED
	TOSResult.setCommand("LOADACC");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 10.1: TOSResult.Command : LOADACC , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: ACCEPTED
	TOSResult.setCommand("LOADACC");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACCEPTED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 11: TOSResult.Command : LOADACC , ProducationLoad.LastAction
	// :Sucess, ProducationLoad.Status: FALLBACK_ACCEPTED
	TOSResult.setCommand("LOADACC");
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.FALLBACK_ACCEPTED.name());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

	// Test Case 12:
	TOSResult.setReturnValue(1);
	ProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACCEPTED.name());
	ProductionLoads.setSystemId(DataWareHouse.getSystemList().get(0));
	// Mockito.when(instance.lProdTOSOperationMap.containsKey(DataWareHouse.getPlan().getId()
	// + "_" + ProductionLoads.getSystemId().getId())).thenReturn(true);
	// Mockito.when(instance.lProdTOSOperationMap.get(Mockito.anyString())).thenReturn(new
	// HashSet<TosActionQueue>());
	// Mockito.when(instance.lProdTOSOperationMap.remove(Mockito.any())).thenReturn(new
	// HashSet<TosActionQueue>());
	// Mockito.when(instance.lProdTOSOperationMap.replace(Mockito.any(),
	// Mockito.any())).thenReturn(new HashSet<TosActionQueue>());
	assertNotNull(instance.processProductionTOSMessage(TOSResult));

    }

    @Test
    public void testCheckAllDeleted() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "tsdService", Mockito.mock(TSDService.class));
	ReflectionTestUtils.setField(instance, "fallbackHelper", Mockito.mock(FallbackHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ProductionLoads lLoads = DataWareHouse.getProductionLoads();
	Mockito.when(instance.productionLoadsDAO.find(92)).thenReturn(lLoads);
	List<ProductionLoads> lProdLoads = new ArrayList<ProductionLoads>();
	lProdLoads.add(DataWareHouse.getProductionLoads());
	SystemLoad systemLoad1 = new SystemLoad();
	systemLoad1.setLoadSetName("Sample");
	lProdLoads.get(0).setSystemLoadId(systemLoad1);
	lProdLoads.get(0).setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	lProdLoads.get(0).setId(93);
	Mockito.when(instance.productionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lProdLoads);
	Mockito.when(instance.productionLoadsDAO.findAllLastSuccessfulBuild(lLoads.getPlanId().getId())).thenReturn(lProdLoads);
	Mockito.doNothing().when(instance.productionLoadsDAO).delete(DataWareHouse.getUser(), DataWareHouse.getPlan().getId());
	Mockito.doNothing().doThrow(new RuntimeException()).when(instance.impPlanDAO).update(DataWareHouse.getUser(), DataWareHouse.getPlan());
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	Mockito.when(instance.tsdService.postActivationAction(DataWareHouse.getUser(), DataWareHouse.getProductionLoads(), false, StringUtils.EMPTY)).thenReturn(new JSONResponse());
	Mockito.doNothing().when(instance.fallbackHelper).fallBackStatusUpdate(DataWareHouse.getUser(), DataWareHouse.getProductionLoads().getPlanId().getId(), Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET, null, null);
	instance.checkAllDeleted(DataWareHouse.getUser(), new Integer(92));

	Mockito.when(instance.productionLoadsDAO.findByPlanId((lLoads.getPlanId().getId()))).thenReturn(lProdLoads);
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PROD_LOAD, DataWareHouse.getUser(), new JSONResponse());
	instance.checkAllDeleted(DataWareHouse.getUser(), new Integer(92));
	Mockito.when(instance.productionLoadsDAO.findAllLastSuccessfulBuild(lLoads.getPlanId().getId())).thenReturn(new ArrayList<ProductionLoads>());
	instance.checkAllDeleted(DataWareHouse.getUser(), new Integer(92));

    }

    @Test
    public void testCheckFallbackDeleted() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ProductionLoads lLoads = DataWareHouse.getProductionLoads();
	Mockito.when(instance.productionLoadsDAO.find(92)).thenReturn(lLoads);
	List<ProductionLoads> lProdLoads = new ArrayList<ProductionLoads>();
	lProdLoads.add(DataWareHouse.getProductionLoads());
	SystemLoad systemLoad1 = new SystemLoad();
	systemLoad1.setLoadSetName("Sample");
	lProdLoads.get(0).setSystemLoadId(systemLoad1);
	lProdLoads.get(0).setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	lProdLoads.get(0).setId(93);
	Mockito.when(instance.productionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lProdLoads);
	Mockito.when(instance.productionLoadsDAO.findAllLastSuccessfulBuild(lLoads.getPlanId().getId())).thenReturn(lProdLoads);
	instance.checkFallbackDeleted(DataWareHouse.getUser(), new Integer(92));

	Mockito.when(instance.productionLoadsDAO.findAllLastSuccessfulBuild(lLoads.getPlanId().getId())).thenReturn(new ArrayList<ProductionLoads>());
	Mockito.doNothing().when(instance.impPlanDAO).update(DataWareHouse.getUser(), lLoads.getPlanId());
	instance.checkFallbackDeleted(DataWareHouse.getUser(), new Integer(92));

    }

    @Test
    public void testCheckAllAccepted() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "fallbackHelper", Mockito.mock(FallbackHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance, "wFConfig", Mockito.mock(WFConfig.class));
	ProductionLoads lLoads = DataWareHouse.getProductionLoads();
	lLoads.getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
	lLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	Mockito.when(instance.productionLoadsDAO.find(92)).thenReturn(lLoads);
	Mockito.when(instance.productionLoadsDAO.getAllAccepted(lLoads.getPlanId().getId())).thenReturn(new Long(90));
	Mockito.when(instance.systemLoadDAO.countByImpPlan(lLoads.getPlanId())).thenReturn(new Long(90));
	Mockito.doNothing().when(instance.fallbackHelper).onlineJenkinsJob(DataWareHouse.getUser(), lLoads.getPlanId());
	ProdLoadsetActivationMail lProdLoadsetActivationMail = new ProdLoadsetActivationMail();
	Mockito.when(instance.mailMessageFactory.getTemplate(ProdLoadsetActivationMail.class)).thenReturn(lProdLoadsetActivationMail);
	Mockito.when(instance.mailMessageFactory.getTemplate(StackHolderLoadsetMail.class)).thenReturn(new StackHolderLoadsetMail());
	Mockito.when(instance.wFConfig.getIsDeltaApp()).thenReturn(true);
	Mockito.when(instance.wFConfig.getDeltaFallbackOnloneMailId()).thenReturn("String");
	Mockito.when(instance.wFConfig.getDeltaFallbackOnloneMailId()).thenReturn("String");
	Mockito.when(instance.systemLoadDAO.findByImpPlan(lLoads.getPlanId().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	BlockingQueue<MailMessage> blockingQueue = new LinkedBlockingQueue<MailMessage>();
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	lLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	lLoads.setCpuId(DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0));
	lLoads.getPlanId().setStackHolderEmail("STring");
	SystemLoad load = new SystemLoad();
	load.setActive("Y");
	load.setSystemId(DataWareHouse.getSystemList().get(0));
	List<SystemLoad> systemLoadlist = new ArrayList<SystemLoad>();
	systemLoadlist.add(load);
	lLoads.getPlanId().setSystemLoadList(systemLoadlist);
	List<Dbcr> dbcrList = new ArrayList<>();
	Dbcr dbcr = new Dbcr();
	dbcr.setDbcrName("String");
	dbcrList.add(dbcr);
	lLoads.getPlanId().setDbcrList(dbcrList);
	// ProductionLoads.Status= DEACTIVATED
	instance.checkAllAccepted(DataWareHouse.getUser(), new Integer(92), new String());

	// ProductionLoads.Status= ACCEPTED
	lLoads.setStatus(Constants.LOAD_SET_STATUS.ACCEPTED.name());
	instance.checkAllAccepted(DataWareHouse.getUser(), new Integer(92), new String());

	// ImpPlan Count are not Equal
	Mockito.when(instance.systemLoadDAO.countByImpPlan(lLoads.getPlanId())).thenReturn(new Long(95));
	instance.checkAllAccepted(DataWareHouse.getUser(), new Integer(92), new String());

    }

    @Test
    public void testCheckAllFallbackAccepted() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "fallbackHelper", Mockito.mock(FallbackHelper.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", Mockito.mock(MailMessageFactory.class));
	ProductionLoads lLoads = DataWareHouse.getProductionLoads();
	lLoads.getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
	lLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	Mockito.when(instance.productionLoadsDAO.find(92)).thenReturn(lLoads);
	Mockito.when(instance.productionLoadsDAO.getAllFallBackAccepted(lLoads.getPlanId().getId())).thenReturn(new Long(90));
	Mockito.when(instance.systemLoadDAO.findByImpPlan(lLoads.getPlanId().getId())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	Mockito.doNothing().when(instance.fallbackHelper).fallBackStatusUpdate(DataWareHouse.getUser(), lLoads.getPlanId().getId(), Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET, "rejectReason", lLoads);
	Mockito.when(instance.mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class)).thenReturn(new StatusChangeToDependentPlanMail());
	instance.checkAllFallbackAccepted(DataWareHouse.getUser(), new Integer(92), "String", "rejectReason");
	// Count Equals
	BlockingQueue<MailMessage> blockingQueue = new LinkedBlockingQueue<MailMessage>();
	Mockito.when(instance.mailMessageFactory.getMailQueue()).thenReturn(blockingQueue);
	Mockito.when(instance.productionLoadsDAO.getAllFallBackAccepted(lLoads.getPlanId().getId())).thenReturn(new Long(4));
	instance.checkAllFallbackAccepted(DataWareHouse.getUser(), new Integer(92), "String", "rejectReason");

    }

    @Test
    public void testcheckAllActivated() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ProductionLoads lLoads = DataWareHouse.getProductionLoads();
	lLoads.getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
	lLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	Mockito.when(instance.productionLoadsDAO.find(92)).thenReturn(lLoads);
	Mockito.when(instance.productionLoadsDAO.getAllActivated(lLoads.getPlanId().getId())).thenReturn(new Long(90));
	Mockito.when(instance.systemLoadDAO.countByImpPlan(lLoads.getPlanId())).thenReturn(new Long(6));
	Mockito.doNothing().when(instance.impPlanDAO).update(DataWareHouse.getUser(), lLoads.getPlanId());
	instance.checkAllActivated(DataWareHouse.getUser(), new Integer(92));

	// Count equals
	Mockito.when(instance.systemLoadDAO.countByImpPlan(lLoads.getPlanId())).thenReturn(new Long(90));
	instance.checkAllActivated(DataWareHouse.getUser(), new Integer(92));
    }

    @Test
    public void checkAllFallbackActivated() {
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ProductionLoads lLoads = DataWareHouse.getProductionLoads();
	lLoads.getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
	lLoads.getPlanId().setPlanStatus(Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name());
	lLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	Mockito.when(instance.productionLoadsDAO.find(92)).thenReturn(lLoads);
	Mockito.when(instance.productionLoadsDAO.getAllFallBackActivated(lLoads.getPlanId().getId())).thenReturn(new Long(90));
	Mockito.when(instance.systemLoadDAO.countByImpPlan(lLoads.getPlanId())).thenReturn(new Long(6));
	Mockito.doNothing().when(instance.impPlanDAO).update(DataWareHouse.getUser(), lLoads.getPlanId());
	instance.checkAllFallbackActivated(DataWareHouse.getUser(), new Integer(92));

	// Count equals
	Mockito.when(instance.systemLoadDAO.countByImpPlan(lLoads.getPlanId())).thenReturn(new Long(90));
	instance.checkAllFallbackActivated(DataWareHouse.getUser(), new Integer(92));

    }

    @Test
    public void test() {
	assertNotNull(instance.getActivityLogDAO());
	assertNotNull(instance.getBPMClientUtils());
	assertNotNull(instance.getFallbackHelper());
	assertNotNull(instance.getImplementationDAO());
	assertNotNull(instance.getImpPlanDAO());
	assertNotNull(instance.getJenkinsClient());
	assertNotNull(instance.getJGitClientUtils());
	assertNotNull(instance.getMailMessageFactory());
	assertNotNull(instance.getOnlineBuildDAO());
	assertNotNull(instance.getPlanHelper());
	assertNotNull(instance.getProductionLoadsDAO());
	assertNotNull(instance.getPRStatusUpdateinNAS());
	assertNotNull(instance.getRejectHelper());
	assertNotNull(instance.getSystemLoadDAO());
	assertNotNull(instance.getTOSConfig());
	instance.setActivityLogDAO(new ActivityLogDAO());
	instance.setFallbackHelper(new FallbackHelper());
	instance.setImpPlanDAO(new ImpPlanDAO());
	instance.setJGitClientUtils(new JGitClientUtils(null));
	instance.setMailMessageFactory(new MailMessageFactory());
	instance.setProductionLoadsDAO(new ProductionLoadsDAO());
	instance.setPRStatusUpdateinNAS(new PRNumberHelper());
	instance.setRejectHelper(new RejectHelper());
	instance.setSystemLoadDAO(new SystemLoadDAO());
	instance.setTOSConfig(new TOSConfig());

    }

}
