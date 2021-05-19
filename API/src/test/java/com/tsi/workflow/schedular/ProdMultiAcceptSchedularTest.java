package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.tos.ProdMessageProcessor;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class ProdMultiAcceptSchedularTest {

    ProdMultiAcceptSchedular instance;
    ConcurrentHashMap<User, Set<TosActionQueue>> prodMultiAcceptPlans;
    User user;
    ImpPlan lPlan;
    List<SystemLoad> lSystemLoads;
    SystemLoad systemLoad;
    List<Object[]> segmentRelatedPlans;
    ProductionLoads lProdLoad;
    System system;

    @Before
    public void setUp() throws Exception {
	ProdMultiAcceptSchedular prodMultiAcceptSchedular = new ProdMultiAcceptSchedular();
	instance = Mockito.spy(prodMultiAcceptSchedular);

	ReflectionTestUtils.setField(instance, "prodMultiAcceptPlans", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "systemDAO", Mockito.mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "lAsyncPlansStartTimeMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "fallbackHelper", Mockito.mock(FallbackHelper.class));
	ReflectionTestUtils.setField(instance, "prodMessageProcessor", Mockito.mock(ProdMessageProcessor.class));
	ReflectionTestUtils.setField(instance, "planHelper", Mockito.mock(PlanHelper.class));

	prodMultiAcceptPlans = new ConcurrentHashMap<>();
	user = DataWareHouse.getUser();
	prodMultiAcceptPlans.put(user, new HashSet());
	// instance.prodMultiAcceptPlans = prodMultiAcceptPlans;
	lPlan = DataWareHouse.getPlan();
	lSystemLoads = new ArrayList<>();
	systemLoad = Mockito.mock(SystemLoad.class);
	lSystemLoads.add(systemLoad);
	segmentRelatedPlans = new ArrayList<>();
	lProdLoad = Mockito.mock(ProductionLoads.class);
	system = DataWareHouse.getSystemList().get(0);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testAccpetLoadsetInSystem2() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(lPlan);
	Mockito.when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoads);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId())).thenReturn(segmentRelatedPlans);
	Mockito.when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	Mockito.when(systemLoad.getSystemId()).thenReturn(system);
	Mockito.when(instance.getProductionLoadsDAO().findByPlanId(lPlan, system)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	Mockito.when(instance.getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.ACCEPT, lProdLoad, Constants.LOAD_SET_STATUS.ACTIVATED.name(), systemLoad)).thenReturn(true);
	Mockito.when(instance.getProductionLoadsDAO().getAllAccepted(lPlan.getId())).thenReturn((Long.valueOf(lSystemLoads.size())));
	// instance.accpetLoadsetInSystem();
    }

    @Test
    public void testAccpetLoadsetInSystem1() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(lPlan);
	Mockito.when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoads);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId())).thenReturn(segmentRelatedPlans);
	Mockito.when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	Mockito.when(systemLoad.getSystemId()).thenReturn(system);
	Mockito.when(instance.getProductionLoadsDAO().findByPlanId(lPlan, system)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	Mockito.when(instance.getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.ACCEPT, lProdLoad, Constants.LOAD_SET_STATUS.ACTIVATED.name(), systemLoad)).thenReturn(false);
	Mockito.when(instance.getProductionLoadsDAO().getAllAccepted(lPlan.getId())).thenReturn((Long.valueOf(lSystemLoads.size())));
	// instance.accpetLoadsetInSystem();
    }

    @Test
    public void testAccpetLoadsetInSystem() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(lPlan);
	Mockito.when(instance.getSystemLoadDAO().findByImpPlan(lPlan)).thenReturn(lSystemLoads);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lPlan.getId())).thenReturn(segmentRelatedPlans);
	lPlan.setPlanStatus(Constants.PlanStatus.ONLINE.name());
	lPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
	Mockito.when(instance.getImpPlanDAO().find(lPlan.getId())).thenReturn(lPlan);
	Mockito.when(instance.getPlanHelper().getFailedOnlineJobCnt(lPlan.getId())).thenReturn(2);
	Mockito.when(systemLoad.getSystemId()).thenReturn(system);
	Mockito.when(instance.getProductionLoadsDAO().findByPlanId(lPlan, system)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn(Constants.LOAD_SET_STATUS.ACTIVATED.name());

	Mockito.when(instance.getTOSHelper().doTOSOperation(user, Constants.LoadSetCommands.ACCEPT, lProdLoad, Constants.LOAD_SET_STATUS.ACTIVATED.name(), systemLoad)).thenReturn(true);
	// instance.accpetLoadsetInSystem();
	assertNotNull(instance.getSystemDAO());

    }

}
