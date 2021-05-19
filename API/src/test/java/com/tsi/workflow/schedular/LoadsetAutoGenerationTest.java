package com.tsi.workflow.schedular;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.activity.YodaResponseActivityMessage;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.ImpPlanAndUserDetail;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.dao.external.TestSystemLoadDAO;
import com.tsi.workflow.helper.FTPHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class LoadsetAutoGenerationTest {

    LoadsetAutoGeneration instance;

    @Before
    public void setUp() throws Exception {
	LoadsetAutoGeneration autoGeneration = new LoadsetAutoGeneration();
	instance = Mockito.spy(autoGeneration);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoAutoLoadsetGen() {

	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "qaBypassedLoadsetList", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance, "testSystemLoadDAO", mock(TestSystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	pObject.setSystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(3));
	pObject.setUser((DataWareHouse.getUser()));
	qaBypassedLoadsetList.add(pObject);
	Build build = new Build();
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	List<Vpars> vparsList = new ArrayList();
	Vpars vpars = new Vpars();
	vparsList.add(vpars);
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(vparsList);
	instance.qaBypassedLoadsetList = qaBypassedLoadsetList;
	SystemLoadActions systemLoadActions = new SystemLoadActions();
	systemLoadActions.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(3));
	systemLoadActions.setSystemId(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	Vpars vpars1 = new Vpars();
	vpars1.setName("Vpars");
	systemLoadActions.setVparId(vpars1);
	Mockito.when(instance.getSystemLoadActionsDAO().findByPlanAndVpars(DataWareHouse.getPlan().getId(), vparsList.get(0).getId(), true)).thenReturn(systemLoadActions);
	List<YodaResult> list = new ArrayList<>();
	YodaResult yodaResult = new YodaResult();
	yodaResult.setIp("255.255.255.0");
	yodaResult.setMessage("Message");
	list.add(yodaResult);
	Mockito.when(instance.getTestSystemLoadDAO().getVparsIP(vparsList.get(0).getName())).thenReturn(list);
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new YodaResponseActivityMessage(DataWareHouse.getPlan(), null));
	Mockito.when(instance.getFTPHelper().doFTP(pObject.getUser(), systemLoadActions.getSystemLoadId(), build, yodaResult.getIp().replaceAll("^0+", "").replaceAll("\\.0+", "\\."), false)).thenReturn(Mockito.mock(JSONResponse.class));
	instance.doAutoLoadsetGen();
    }

    @Test
    public void testDoAutoLoadsetGen1() {

	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "qaBypassedLoadsetList", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance, "testSystemLoadDAO", mock(TestSystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	pObject.setSystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(3));
	pObject.setUser((DataWareHouse.getUser()));
	qaBypassedLoadsetList.add(pObject);
	Build build = new Build();
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	List<Vpars> vparsList = new ArrayList();
	Vpars vpars = new Vpars();
	vparsList.add(vpars);
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(vparsList);
	instance.qaBypassedLoadsetList = qaBypassedLoadsetList;
	SystemLoadActions systemLoadActions = new SystemLoadActions();
	systemLoadActions.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(3));
	systemLoadActions.setSystemId(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	Vpars vpars1 = new Vpars();
	vpars1.setName("Vpars");
	systemLoadActions.setVparId(vpars1);
	Mockito.when(instance.getSystemLoadActionsDAO().findByPlanAndVpars(DataWareHouse.getPlan().getId(), vparsList.get(0).getId(), true)).thenReturn(systemLoadActions);
	List<YodaResult> list = new ArrayList<>();
	YodaResult yodaResult = new YodaResult();
	yodaResult.setIp("255.255.255.0");
	yodaResult.setRc(1);
	;
	list.add(yodaResult);
	Mockito.when(instance.getTestSystemLoadDAO().getVparsIP(vparsList.get(0).getName())).thenReturn(list);
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new YodaResponseActivityMessage(DataWareHouse.getPlan(), null));
	Mockito.when(instance.getFTPHelper().doFTP(pObject.getUser(), systemLoadActions.getSystemLoadId(), build, yodaResult.getIp().replaceAll("^0+", "").replaceAll("\\.0+", "\\."), false)).thenReturn(Mockito.mock(JSONResponse.class));
	instance.doAutoLoadsetGen();
    }

    @Test
    public void testDoAutoLoadsetGen3() {

	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "qaBypassedLoadsetList", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance, "testSystemLoadDAO", mock(TestSystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	pObject.setSystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(3));
	pObject.setUser((DataWareHouse.getUser()));
	qaBypassedLoadsetList.add(pObject);
	Build build = new Build();
	build.setPlanId(DataWareHouse.getPlan());
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	List<Vpars> vparsList = new ArrayList();
	Vpars vpars = new Vpars();
	vparsList.add(vpars);
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(vparsList);
	instance.qaBypassedLoadsetList = qaBypassedLoadsetList;
	SystemLoadActions systemLoadActions = new SystemLoadActions();
	systemLoadActions.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(3));
	systemLoadActions.setSystemId(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	Vpars vpars1 = new Vpars();
	vpars1.setName("Vpars");
	systemLoadActions.setVparId(vpars1);
	Mockito.when(instance.getSystemLoadActionsDAO().findByPlanAndVpars(DataWareHouse.getPlan().getId(), vparsList.get(0).getId(), true)).thenReturn(systemLoadActions);
	List<YodaResult> list = new ArrayList<>();
	YodaResult yodaResult = new YodaResult();
	yodaResult.setIp("255.255.255.0");
	yodaResult.setMessage("Message");
	list.add(yodaResult);
	Mockito.when(instance.getTestSystemLoadDAO().getVparsIP(vparsList.get(0).getName())).thenReturn(list);
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new YodaResponseActivityMessage(DataWareHouse.getPlan(), null));
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(true);
	Mockito.when(instance.getFTPHelper().doFTP(pObject.getUser(), systemLoadActions.getSystemLoadId(), build, yodaResult.getIp().replaceAll("^0+", "").replaceAll("\\.0+", "\\."), false)).thenReturn(jsonResponse);
	instance.doAutoLoadsetGen();
    }

    @Test
    public void testDoAutoLoadsetGen4() {

	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "qaBypassedLoadsetList", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance, "testSystemLoadDAO", mock(TestSystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	pObject.setSystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(3));
	pObject.setUser((DataWareHouse.getUser()));
	qaBypassedLoadsetList.add(pObject);
	Build build = new Build();
	build.setPlanId(DataWareHouse.getPlan());
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	List<Vpars> vparsList = new ArrayList();
	Vpars vpars = new Vpars();
	vparsList.add(vpars);
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(vparsList);
	instance.qaBypassedLoadsetList = qaBypassedLoadsetList;
	SystemLoadActions systemLoadActions = new SystemLoadActions();
	systemLoadActions.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(3));
	systemLoadActions.setSystemId(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	Vpars vpars1 = new Vpars();
	vpars1.setName("Vpars");
	systemLoadActions.setVparId(vpars1);
	Mockito.when(instance.getSystemLoadActionsDAO().findByPlanAndVpars(DataWareHouse.getPlan().getId(), vparsList.get(0).getId(), true)).thenReturn(systemLoadActions);
	List<YodaResult> list = new ArrayList<>();
	YodaResult yodaResult = new YodaResult();
	yodaResult.setIp("255.255.255.0");
	yodaResult.setRc(0);
	list.add(yodaResult);
	Mockito.when(instance.getTestSystemLoadDAO().getVparsIP(vparsList.get(0).getName())).thenReturn(list);
	Mockito.doNothing().when(instance.activityLogDAO).save(DataWareHouse.getUser(), new YodaResponseActivityMessage(DataWareHouse.getPlan(), null));
	JSONResponse jsonResponse = new JSONResponse();
	jsonResponse.setStatus(true);
	Mockito.when(instance.getFTPHelper().doFTP(pObject.getUser(), systemLoadActions.getSystemLoadId(), build, yodaResult.getIp().replaceAll("^0+", "").replaceAll("\\.0+", "\\."), false)).thenReturn(jsonResponse);
	Mockito.when(instance.getTestSystemLoadDAO().loadAndActivate(pObject.getUser(), systemLoadActions.getVparId().getName(), systemLoadActions.getSystemLoadId().getLoadSetName())).thenReturn(list);
	instance.doAutoLoadsetGen();
	instance.getWFConfig();
	instance.getImpPlanDAO();
	instance.getSystemLoadDAO();
    }

    @Test
    public void testDoAutoLoadsetGen5() {

	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "qaBypassedLoadsetList", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance, "testSystemLoadDAO", mock(TestSystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "fTPHelper", mock(FTPHelper.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> qaBypassedLoadsetList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	pObject.setSystemLoad(DataWareHouse.getPlan().getSystemLoadList().get(3));
	pObject.setUser((DataWareHouse.getUser()));
	qaBypassedLoadsetList.add(pObject);
	Build build = new Build();
	build.setPlanId(DataWareHouse.getPlan());
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	List<Vpars> vparsList = null;
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(vparsList);
	instance.qaBypassedLoadsetList = qaBypassedLoadsetList;
	SystemLoadActions systemLoadActions = new SystemLoadActions();
	systemLoadActions.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(3));
	systemLoadActions.setSystemId(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	instance.doAutoLoadsetGen();

    }

}
