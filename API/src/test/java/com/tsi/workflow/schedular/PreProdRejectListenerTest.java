package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.ui.RejectPlans;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.PreProductionLoadsDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class PreProdRejectListenerTest {

    PreProdRejectListener instance;

    @Before
    public void setUp() throws Exception {
	PreProdRejectListener preProdRejectListener = new PreProdRejectListener();
	instance = Mockito.spy(preProdRejectListener);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoReject() {
	ReflectionTestUtils.setField(instance, "lRejectPlans", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ConcurrentLinkedQueue<RejectPlans> lRejectPlans = new ConcurrentLinkedQueue<>();
	RejectPlans rejectPlans = new RejectPlans();
	rejectPlans.setUser(DataWareHouse.getUser());
	Map<String, Date> dependentPlans = new HashMap<>();
	dependentPlans.put(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime());
	rejectPlans.setDependentPlanIds(dependentPlans);
	lRejectPlans.add(rejectPlans);
	lRejectPlans.add(Mockito.mock(RejectPlans.class));
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	List<PreProductionLoads> lLoadSets = new ArrayList<>();
	lLoadSets = (List<PreProductionLoads>) DataWareHouse.getPlan().getPreProductionLoadsList();
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lLoadSets);
	instance.lRejectPlans = lRejectPlans;
	instance.doReject();
    }

    @Test
    public void testDoReject1() throws Exception {
	ReflectionTestUtils.setField(instance, "lRejectPlans", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", Mockito.mock(RejectHelper.class));
	ConcurrentLinkedQueue<RejectPlans> lRejectPlans = new ConcurrentLinkedQueue<>();
	RejectPlans rejectPlans = new RejectPlans();
	rejectPlans.setUser(DataWareHouse.getUser());
	Map<String, Date> dependentPlans = new HashMap<>();
	dependentPlans.put(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime());
	rejectPlans.setDependentPlanIds(dependentPlans);
	lRejectPlans.add(rejectPlans);
	lRejectPlans.add(Mockito.mock(RejectPlans.class));
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	List<PreProductionLoads> lLoadSets = new ArrayList<>();
	lLoadSets.add(Mockito.mock(PreProductionLoads.class));
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lLoadSets);
	Mockito.doNothing().when(instance.rejectHelper).deleteStagingWorkspace(rejectPlans.getUser(), DataWareHouse.getPlan());
	Mockito.doNothing().when(instance.rejectHelper).deleteBuilds(rejectPlans.getUser(), DataWareHouse.getPlan());
	List<Object[]> objects = new ArrayList<>();
	Object[] lPlanInfo = new Object[3];
	lPlanInfo[1] = DataWareHouse.getPlan().getPlanStatus();
	lPlanInfo[2] = DataWareHouse.getPlan().getId();
	objects.add(lPlanInfo);
	Mockito.when(instance.impPlanDAO.getPostSegmentRelatedPlans(DataWareHouse.getPlan().getId(), Boolean.FALSE)).thenReturn(objects);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(lPlanInfo[2].toString())).thenReturn(lLoadSets);
	instance.lRejectPlans = lRejectPlans;
	instance.doReject();
    }

    @Test
    public void testDoReject2() {
	ReflectionTestUtils.setField(instance, "lRejectPlans", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ConcurrentLinkedQueue<RejectPlans> lRejectPlans = new ConcurrentLinkedQueue<>();
	RejectPlans rejectPlans = new RejectPlans();
	rejectPlans.setUser(DataWareHouse.getUser());
	Map<String, Date> dependentPlans = new HashMap<>();
	dependentPlans.put(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime());
	rejectPlans.setDependentPlanIds(dependentPlans);
	lRejectPlans.add(rejectPlans);
	lRejectPlans.add(Mockito.mock(RejectPlans.class));
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	List<PreProductionLoads> lLoadSets = new ArrayList<>();
	lLoadSets.add(Mockito.mock(PreProductionLoads.class));
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lLoadSets);
	List<Object[]> objects = new ArrayList<>();
	Object[] lPlanInfo = new Object[3];
	lPlanInfo[1] = DataWareHouse.getPlan().getPlanStatus();
	lPlanInfo[2] = DataWareHouse.getPlan().getId();
	objects.add(lPlanInfo);
	Mockito.when(instance.impPlanDAO.getPostSegmentRelatedPlans(DataWareHouse.getPlan().getId(), Boolean.FALSE)).thenReturn(objects);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(lPlanInfo[2].toString())).thenReturn(lLoadSets);
	instance.lRejectPlans = lRejectPlans;
	Map<String, Date> lPlanVsTriggerDate = dependentPlans;
	instance.doReject();
    }

    @Test
    public void testDoReject3() {
	ReflectionTestUtils.setField(instance, "lRejectPlans", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ConcurrentLinkedQueue<RejectPlans> lRejectPlans = new ConcurrentLinkedQueue<>();
	RejectPlans rejectPlans = new RejectPlans();
	rejectPlans.setUser(DataWareHouse.getUser());
	Map<String, Date> dependentPlans = new HashMap<>();
	dependentPlans.put(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime());
	rejectPlans.setDependentPlanIds(dependentPlans);
	lRejectPlans.add(rejectPlans);
	lRejectPlans.add(Mockito.mock(RejectPlans.class));
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	List<PreProductionLoads> lLoadSets = new ArrayList<>();
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setLastActionStatus("ACTIVE");
	preProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	preProductionLoads.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(0));
	lLoadSets.add(preProductionLoads);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lLoadSets);
	List<Object[]> objects = new ArrayList<>();
	Object[] lPlanInfo = new Object[3];
	lPlanInfo[1] = DataWareHouse.getPlan().getPlanStatus();
	lPlanInfo[2] = DataWareHouse.getPlan().getId();
	objects.add(lPlanInfo);
	Mockito.when(instance.impPlanDAO.getPostSegmentRelatedPlans(DataWareHouse.getPlan().getId(), Boolean.FALSE)).thenReturn(objects);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(lPlanInfo[2].toString())).thenReturn(lLoadSets);
	Mockito.when(instance.tOSHelper.doPreTOSOperation(DataWareHouse.getUser(), Constants.LoadSetCommands.DEACTIVATE, preProductionLoads, Constants.LOAD_SET_STATUS.ACTIVATED.name(), preProductionLoads.getSystemLoadId(), rejectPlans.getIsReject())).thenReturn(true);
	instance.lRejectPlans = lRejectPlans;
	Map<String, Date> lPlanVsTriggerDate = dependentPlans;
	instance.doReject();
    }

    @Test
    public void testDoReject4() {
	ReflectionTestUtils.setField(instance, "lRejectPlans", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ConcurrentLinkedQueue<RejectPlans> lRejectPlans = new ConcurrentLinkedQueue<>();
	RejectPlans rejectPlans = new RejectPlans();
	rejectPlans.setUser(DataWareHouse.getUser());
	Map<String, Date> dependentPlans = new HashMap<>();
	dependentPlans.put(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime());
	ImpPlan impPlan = DataWareHouse.getPlan();
	impPlan.setId("D1900185");
	impPlan.getSystemLoadList().get(0).setLoadDateTime(new Date("1970/10/01"));
	dependentPlans.put(impPlan.getId(), impPlan.getSystemLoadList().get(0).getLoadDateTime());
	rejectPlans.setDependentPlanIds(dependentPlans);
	lRejectPlans.add(rejectPlans);
	RejectPlans rejectPlans1 = rejectPlans;
	lRejectPlans.add(rejectPlans1);
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	List<PreProductionLoads> lLoadSets = new ArrayList<>();
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setLastActionStatus("ACTIVE");
	preProductionLoads.setStatus(Constants.LOAD_SET_STATUS.DEACTIVATED.name());
	preProductionLoads.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(0));
	lLoadSets.add(preProductionLoads);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lLoadSets);
	List<Object[]> objects = new ArrayList<>();
	Object[] lPlanInfo = new Object[3];
	lPlanInfo[1] = DataWareHouse.getPlan().getPlanStatus();
	lPlanInfo[2] = DataWareHouse.getPlan().getId();
	objects.add(lPlanInfo);
	Mockito.when(instance.impPlanDAO.getPostSegmentRelatedPlans(DataWareHouse.getPlan().getId(), Boolean.FALSE)).thenReturn(objects);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(lPlanInfo[2].toString())).thenReturn(lLoadSets);
	Mockito.when(instance.tOSHelper.doPreTOSOperation(DataWareHouse.getUser(), Constants.LoadSetCommands.DELETE, preProductionLoads, Constants.LOAD_SET_STATUS.DEACTIVATED.name(), preProductionLoads.getSystemLoadId(), rejectPlans.getIsReject())).thenReturn(false);
	instance.lRejectPlans = lRejectPlans;
	Map<String, Date> lPlanVsTriggerDate = dependentPlans;
	instance.doReject();
	instance.removePlan(DataWareHouse.getPlan().getId());
    }

    @Test
    public void testDoReject5() {
	ReflectionTestUtils.setField(instance, "lRejectPlans", Mockito.mock(ConcurrentLinkedQueue.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "preProductionLoadsDAO", Mockito.mock(PreProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ConcurrentLinkedQueue<RejectPlans> lRejectPlans = new ConcurrentLinkedQueue<>();
	RejectPlans rejectPlans = new RejectPlans();
	rejectPlans.setUser(DataWareHouse.getUser());
	Map<String, Date> dependentPlans = new HashMap<>();
	dependentPlans.put(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getLoadDateTime());
	rejectPlans.setDependentPlanIds(dependentPlans);
	lRejectPlans.add(rejectPlans);
	lRejectPlans.add(Mockito.mock(RejectPlans.class));
	Mockito.when(instance.impPlanDAO.find(DataWareHouse.getPlan().getId())).thenReturn(DataWareHouse.getPlan());
	List<PreProductionLoads> lLoadSets = new ArrayList<>();
	PreProductionLoads preProductionLoads = new PreProductionLoads();
	preProductionLoads.setLastActionStatus("ACTIVE");
	preProductionLoads.setStatus(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	preProductionLoads.setSystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(0));
	lLoadSets.add(preProductionLoads);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(DataWareHouse.getPlan())).thenReturn(lLoadSets);
	List<Object[]> objects = new ArrayList<>();
	Object[] lPlanInfo = new Object[3];
	lPlanInfo[1] = DataWareHouse.getPlan().getPlanStatus();
	lPlanInfo[2] = DataWareHouse.getPlan().getId();
	objects.add(lPlanInfo);
	Mockito.when(instance.impPlanDAO.getPostSegmentRelatedPlans(DataWareHouse.getPlan().getId(), Boolean.FALSE)).thenReturn(objects);
	Mockito.when(instance.preProductionLoadsDAO.findByPlanId(lPlanInfo[2].toString())).thenReturn(lLoadSets);
	Mockito.when(instance.tOSHelper.doPreTOSOperation(DataWareHouse.getUser(), Constants.LoadSetCommands.DEACTIVATE, preProductionLoads, Constants.LOAD_SET_STATUS.ACTIVATED.name(), preProductionLoads.getSystemLoadId(), rejectPlans.getIsReject())).thenReturn(false);
	instance.lRejectPlans = lRejectPlans;
	Map<String, Date> lPlanVsTriggerDate = dependentPlans;
	instance.doReject();
    }

}
