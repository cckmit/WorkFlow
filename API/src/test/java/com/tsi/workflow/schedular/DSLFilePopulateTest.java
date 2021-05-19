package com.tsi.workflow.schedular;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.beans.ui.ImpPlanAndUserDetail;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemLoadActionsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.dao.VparsDAO;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.BUILD_TYPE;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class DSLFilePopulateTest {
    DSLFilePopulate instance;

    @Before
    public void setUp() throws Exception {
	DSLFilePopulate dslFilePopulate = new DSLFilePopulate();
	instance = Mockito.spy(dslFilePopulate);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoDSlFilePopulate() {

	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "dslFileGenerationList", Mockito.mock(ConcurrentLinkedQueue.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> dslFileGenerationList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	dslFileGenerationList.add(pObject);
	instance.dslFileGenerationList = dslFileGenerationList;

	Mockito.when(instance.systemLoadDAO.findByImpPlan(pObject.getImpPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	// instance.doDSlFilePopulate();

    }

    @Test
    public void testDoDSlFilePopulate1() {

	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadActionsDAO", mock(SystemLoadActionsDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "dslFileGenerationList", Mockito.mock(ConcurrentLinkedQueue.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> dslFileGenerationList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	pObject.setUser(DataWareHouse.getUser());
	dslFileGenerationList.add(pObject);
	instance.dslFileGenerationList = dslFileGenerationList;

	Mockito.when(instance.systemLoadDAO.findByImpPlan(pObject.getImpPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	Build build = new Build();
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	List<Vpars> vparsList = new ArrayList();
	vparsList.add(new Vpars());
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(vparsList);
	// Mockito.doThrow(new
	// Exception()).when(instance.getSystemLoadActionsDAO()).save(pObject.getUser(),
	// new SystemLoadActions());
	instance.doDSlFilePopulate();

    }

    @Test
    public void testDoDSlFilePopulate2() {

	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "dslFileGenerationList", Mockito.mock(ConcurrentLinkedQueue.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> dslFileGenerationList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	dslFileGenerationList.add(pObject);
	instance.dslFileGenerationList = dslFileGenerationList;

	Mockito.when(instance.systemLoadDAO.findByImpPlan(pObject.getImpPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	Build build = null;
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	instance.doDSlFilePopulate();

    }

    @Test
    public void testDoDSlFilePopulate3() {

	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "vparsDAO", mock(VparsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "dslFileGenerationList", Mockito.mock(ConcurrentLinkedQueue.class));
	ConcurrentLinkedQueue<ImpPlanAndUserDetail> dslFileGenerationList = new ConcurrentLinkedQueue<ImpPlanAndUserDetail>();
	ImpPlanAndUserDetail pObject = new ImpPlanAndUserDetail();
	pObject.setImpPlan(DataWareHouse.getPlan());
	dslFileGenerationList.add(pObject);
	instance.dslFileGenerationList = dslFileGenerationList;

	Mockito.when(instance.systemLoadDAO.findByImpPlan(pObject.getImpPlan())).thenReturn(DataWareHouse.getPlan().getSystemLoadList());
	Build build = new Build();
	Mockito.when(instance.getBuildDAO().findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId().getId(), BUILD_TYPE.STG_LOAD)).thenReturn(build);
	List<System> systemIds = new ArrayList<>();
	systemIds.add(DataWareHouse.getPlan().getSystemLoadList().get(3).getSystemId());
	Mockito.when(instance.getVparsDAO().findBySystem(systemIds, Boolean.TRUE, Constants.VPARSEnvironment.QA_FUCTIONAL)).thenReturn(new ArrayList<>());
	instance.doDSlFilePopulate();

    }

}
