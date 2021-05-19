package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.activity.PreTOSActivityMessage;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class PreProdTOSOperationListenerTest {

    PreProdTOSOperationListener instance;

    @Before
    public void setUp() throws Exception {
	PreProdTOSOperationListener preProdTOSOperationListener = new PreProdTOSOperationListener();
	instance = Mockito.spy(preProdTOSOperationListener);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testdoTOSOperation() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	instance.doTOSOperation();
    }

    @Test
    public void testdoTOSOperation1() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(false);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation2() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation3() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("255.255.255.0");
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation4() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("255.255.255.0");
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	build = Mockito.mock(Build.class);
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(false);
	Mockito.when(instance.getFTPHelper().doFTP(tosActionQueue.getUser(), systemLoad, build, "255.255.255.0", false)).thenReturn(jsonResponse);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation5() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("255.255.255.0");
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	build = Mockito.mock(Build.class);
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(false);
	Mockito.when(instance.getFTPHelper().doFTP(tosActionQueue.getUser(), systemLoad, build, "255.255.255.0", false)).thenReturn(jsonResponse);
	jsonResponse.setStatus(true);
	Mockito.when(instance.getFTPHelper().doFTP(tosActionQueue.getUser(), systemLoad, build, "255.255.255.0", false)).thenReturn(jsonResponse);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation6() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("255.255.255.0");
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	build = Mockito.mock(Build.class);
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	JSONResponse jsonResponse = new JSONResponse();
	Mockito.when(instance.getFTPHelper().doFTP(tosActionQueue.getUser(), systemLoad, build, "255.255.255.0", false)).thenReturn(jsonResponse);
	jsonResponse.setStatus(true);
	Mockito.when(instance.getTOSHelper().doPreTOSOperation(tosActionQueue.getUser(), Constants.LoadSetCommands.LOAD, preProductionLoads, tosActionQueue.getOldStatus(), systemLoad, false)).thenReturn(true);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation7() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("255.255.255.0");
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	build = Mockito.mock(Build.class);
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	JSONResponse jsonResponse = new JSONResponse();
	Mockito.when(instance.getFTPHelper().doFTP(tosActionQueue.getUser(), systemLoad, build, "255.255.255.0", false)).thenReturn(jsonResponse);
	jsonResponse.setStatus(true);
	Mockito.when(instance.getTOSHelper().doPreTOSOperation(tosActionQueue.getUser(), Constants.LoadSetCommands.LOAD, preProductionLoads, tosActionQueue.getOldStatus(), systemLoad, false)).thenReturn(true);
	preProductionLoads.setStatus("ACTIVATED");
	instance.doTOSOperation();
	Mockito.when(instance.getTOSHelper().doPreTOSOperation(tosActionQueue.getUser(), Constants.LoadSetCommands.ACTIVATE, preProductionLoads, tosActionQueue.getOldStatus(), systemLoad, false)).thenReturn(true);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation8() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "buildDAO", Mockito.mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", Mockito.mock(FTPHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	TosActionQueue tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(tosActionQueue.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(true);
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("");
	Mockito.when(instance.getTOSHelper().getIP(systemLoad.getId())).thenReturn("255.255.255.0");
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	build = Mockito.mock(Build.class);
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), systemLoad.getSystemId().getId(), Constants.BUILD_TYPE.STG_LOAD)).thenReturn(build);
	JSONResponse jsonResponse = new JSONResponse();
	Mockito.when(instance.getFTPHelper().doFTP(tosActionQueue.getUser(), systemLoad, build, "255.255.255.0", false)).thenReturn(jsonResponse);
	jsonResponse.setStatus(true);
	Mockito.when(instance.getTOSHelper().doPreTOSOperation(tosActionQueue.getUser(), Constants.LoadSetCommands.LOAD, preProductionLoads, tosActionQueue.getOldStatus(), systemLoad, false)).thenReturn(true);
	preProductionLoads.setStatus("ACTIVATED");
	Mockito.when(instance.getTOSHelper().doPreTOSOperation(tosActionQueue.getUser(), Constants.LoadSetCommands.ACTIVATE, preProductionLoads, tosActionQueue.getOldStatus(), systemLoad, false)).thenReturn(true);
	instance.doTOSOperation();

    }

    @Test
    public void testdoTOSOperation9() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	String lWSMessage = "Error: Plan - " + DataWareHouse.getPlan().getId() + " Unable to get IP Adress for system " + systemLoad.getSystemId().getName() + " CPU id - " + preProductionLoads.getCpuId();
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PRE_PROD_LOAD, DataWareHouse.getUser(), (Object) lWSMessage);
	List<Object[]> objectList = new ArrayList();
	Object[] object1 = new Object[3];
	object1[0] = DataWareHouse.getPlan().getId();
	object1[1] = "SUBMITTED";
	objectList.add(object1);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(preProductionLoads.getPlanId().getId(), Arrays.asList(preProductionLoads.getPlanId().getRelatedPlans().split(",")))).thenReturn(objectList);
	List<PreProductionLoads> listPreProductionLoads = new ArrayList();
	listPreProductionLoads.add(preProductionLoads);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanId(object1[0].toString().split("/")[0].toUpperCase())).thenReturn(listPreProductionLoads);
	List<Object[]> objectList1 = new ArrayList();
	Object[] object2 = new Object[3];
	object2[0] = (DataWareHouse.getPlan().getId() + "/" + "WSP");
	object2[1] = "SUBMITTED";
	objectList1.add(object2);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(preProductionLoads.getPlanId().getId())).thenReturn(objectList1);
	instance.doTOSOperation();

	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(DataWareHouse.getPlan().getId()), systemLoad.getSystemId())).thenReturn(listPreProductionLoads);
	instance.doTOSOperation();

	preProductionLoads.setLastActionStatus("INPROGRESS");
	instance.doTOSOperation();

	preProductionLoads.setLastActionStatus("ACTIVATED");
	instance.doTOSOperation();

	preProductionLoads.setStatus("ACTIVATED");
	preProductionLoads.setLastActionStatus("INPROGRESS");
	instance.doTOSOperation();

	preProductionLoads.setLastActionStatus("ACTIVATED");
	instance.doTOSOperation();
    }

    @Test
    public void testdoTOSOperation10() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	Mockito.when(instance.getTOSHelper().requestPreProdIP(DataWareHouse.getUser(), systemLoad, preProductionLoads.getCpuId())).thenReturn(false);
	String lWSMessage = "Error: Plan - " + DataWareHouse.getPlan().getId() + " Unable to get IP Adress for system " + systemLoad.getSystemId().getName() + " CPU id - " + preProductionLoads.getCpuId();
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PRE_PROD_LOAD, DataWareHouse.getUser(), (Object) lWSMessage);
	instance.doTOSOperation();
    }

    @Test
    public void testdoTOSOperation11() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	String lWSMessage = "Error: Plan - " + DataWareHouse.getPlan().getId() + " Unable to get IP Adress for system " + systemLoad.getSystemId().getName() + " CPU id - " + preProductionLoads.getCpuId();
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PRE_PROD_LOAD, DataWareHouse.getUser(), (Object) lWSMessage);
	List<Object[]> objectList = new ArrayList();
	Object[] object1 = new Object[3];
	object1[0] = DataWareHouse.getPlan().getId();
	object1[1] = "SUBMITTED";
	objectList.add(object1);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(preProductionLoads.getPlanId().getId(), Arrays.asList(preProductionLoads.getPlanId().getRelatedPlans().split(",")))).thenReturn(objectList);
	List<PreProductionLoads> listPreProductionLoads = new ArrayList();
	listPreProductionLoads.add(preProductionLoads);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanId(object1[0].toString().split("/")[0].toUpperCase())).thenReturn(listPreProductionLoads);
	List<Object[]> objectList1 = new ArrayList();
	Object[] object2 = new Object[3];
	object2[0] = (DataWareHouse.getPlan().getId() + "/" + "WSP");
	object2[1] = "SUBMITTED";
	objectList1.add(object2);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(preProductionLoads.getPlanId().getId())).thenReturn(objectList1);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(DataWareHouse.getPlan().getId()), systemLoad.getSystemId())).thenReturn(listPreProductionLoads);
	instance.doTOSOperation();
    }

    @Test
    public void testdoTOSOperation12() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	String lWSMessage = "Error: Plan - " + DataWareHouse.getPlan().getId() + " Unable to get IP Adress for system " + systemLoad.getSystemId().getName() + " CPU id - " + preProductionLoads.getCpuId();
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PRE_PROD_LOAD, DataWareHouse.getUser(), (Object) lWSMessage);
	List<Object[]> objectList = new ArrayList();
	Object[] object1 = new Object[3];
	object1[0] = DataWareHouse.getPlan().getId();
	object1[1] = "SUBMITTED";
	objectList.add(object1);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(preProductionLoads.getPlanId().getId(), Arrays.asList(preProductionLoads.getPlanId().getRelatedPlans().split(",")))).thenReturn(objectList);
	List<PreProductionLoads> listPreProductionLoads = new ArrayList();
	listPreProductionLoads.add(preProductionLoads);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanId(object1[0].toString().split("/")[0].toUpperCase())).thenReturn(listPreProductionLoads);
	List<Object[]> objectList1 = new ArrayList();
	Object[] object2 = new Object[3];
	object2[0] = (DataWareHouse.getPlan().getId() + "/" + "WSP");
	object2[1] = "SUBMITTED";
	objectList1.add(object2);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(preProductionLoads.getPlanId().getId())).thenReturn(objectList1);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(DataWareHouse.getPlan().getId()), systemLoad.getSystemId())).thenReturn(listPreProductionLoads);
	preProductionLoads.setLastActionStatus("INPROGRESS");
	instance.doTOSOperation();
    }

    @Test
    public void testdoTOSOperation13() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	String lWSMessage = "Error: Plan - " + DataWareHouse.getPlan().getId() + " Unable to get IP Adress for system " + systemLoad.getSystemId().getName() + " CPU id - " + preProductionLoads.getCpuId();
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PRE_PROD_LOAD, DataWareHouse.getUser(), (Object) lWSMessage);
	List<Object[]> objectList = new ArrayList();
	Object[] object1 = new Object[3];
	object1[0] = DataWareHouse.getPlan().getId();
	object1[1] = "SUBMITTED";
	objectList.add(object1);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(preProductionLoads.getPlanId().getId(), Arrays.asList(preProductionLoads.getPlanId().getRelatedPlans().split(",")))).thenReturn(objectList);
	List<PreProductionLoads> listPreProductionLoads = new ArrayList();
	listPreProductionLoads.add(preProductionLoads);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanId(object1[0].toString().split("/")[0].toUpperCase())).thenReturn(listPreProductionLoads);
	List<Object[]> objectList1 = new ArrayList();
	Object[] object2 = new Object[3];
	object2[0] = (DataWareHouse.getPlan().getId() + "/" + "WSP");
	object2[1] = "SUBMITTED";
	objectList1.add(object2);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(preProductionLoads.getPlanId().getId())).thenReturn(objectList1);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(DataWareHouse.getPlan().getId()), systemLoad.getSystemId())).thenReturn(listPreProductionLoads);
	preProductionLoads.setLastActionStatus("ACTIVATED");
	instance.doTOSOperation();
    }

    @Test
    public void testdoTOSOperation14() {
	ReflectionTestUtils.setField(instance, "lPreProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));

	ConcurrentHashMap<String, Set<TosActionQueue>> lPreProdTOSOperationMap = new ConcurrentHashMap<>();
	Set<TosActionQueue> actionQueues = new HashSet();
	actionQueues.add(Mockito.mock(TosActionQueue.class));
	String string = "Sample";
	lPreProdTOSOperationMap.put(string, actionQueues);
	instance.lPreProdTOSOperationMap = lPreProdTOSOperationMap;
	Mockito.when(instance.getImpPlanDAO().find(string)).thenReturn(DataWareHouse.getPlan());
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setStatus("LOADED");
	preProductionLoads.setLastActionStatus("FAILED");
	Mockito.when(instance.getPreProductionLoadsDAO().find(actionQueues.stream().findFirst().get().getTosRecId())).thenReturn(preProductionLoads);
	SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(3);
	Mockito.when(instance.getSystemLoadDAO().find(DataWareHouse.getPlan(), Mockito.mock(PreProductionLoads.class).getSystemId())).thenReturn(systemLoad);
	String[] array = new String[12];
	preProductionLoads.setPlanId(DataWareHouse.getPlan());
	preProductionLoads.setCpuId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getSystemCpuList().get(0));
	preProductionLoads.getPlanId().setRelatedPlans(DataWareHouse.getPlan().getId());
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new PreTOSActivityMessage(preProductionLoads.getPlanId(), null, preProductionLoads));
	String lWSMessage = "Error: Plan - " + DataWareHouse.getPlan().getId() + " Unable to get IP Adress for system " + systemLoad.getSystemId().getName() + " CPU id - " + preProductionLoads.getCpuId();
	Mockito.doNothing().when(instance.wsserver).sendMessage(Constants.Channels.PRE_PROD_LOAD, DataWareHouse.getUser(), (Object) lWSMessage);
	List<Object[]> objectList = new ArrayList();
	Object[] object1 = new Object[3];
	object1[0] = DataWareHouse.getPlan().getId();
	object1[1] = "SUBMITTED";
	objectList.add(object1);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(preProductionLoads.getPlanId().getId(), Arrays.asList(preProductionLoads.getPlanId().getRelatedPlans().split(",")))).thenReturn(objectList);
	List<PreProductionLoads> listPreProductionLoads = new ArrayList();
	listPreProductionLoads.add(preProductionLoads);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanId(object1[0].toString().split("/")[0].toUpperCase())).thenReturn(listPreProductionLoads);
	List<Object[]> objectList1 = new ArrayList();
	Object[] object2 = new Object[3];
	object2[0] = (DataWareHouse.getPlan().getId() + "/" + "WSP");
	object2[1] = "SUBMITTED";
	objectList1.add(object2);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(preProductionLoads.getPlanId().getId())).thenReturn(objectList1);
	Mockito.when(instance.getPreProductionLoadsDAO().findByPlanIdAndSystemId(new ImpPlan(DataWareHouse.getPlan().getId()), systemLoad.getSystemId())).thenReturn(listPreProductionLoads);
	preProductionLoads.setStatus("ACTIVATED");
	preProductionLoads.setLastActionStatus("INPROGRESS");
	instance.doTOSOperation();
    }
}
