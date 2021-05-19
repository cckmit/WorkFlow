package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
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

public class ProdTOSOperationListenerTest {

    ProdTOSOperationListener instance;
    ConcurrentHashMap<User, Set<TosActionQueue>> lProdTOSOperationMap;
    Set<TosActionQueue> actionQueues;
    TosActionQueue tosActionQueue;
    ProductionLoads lProdLoad;
    ImpPlan impPlan;
    com.tsi.workflow.beans.dao.System system;
    SystemLoad systemLoad;
    List<String> split;
    List<Object[]> relatedPlanDetails;
    List<Object[]> segmentRelatedPlans;
    List<ProductionLoads> lProdLoads;

    @Before
    public void setUp() throws Exception {
	ProdTOSOperationListener prodTOSOperationListener = new ProdTOSOperationListener();
	instance = Mockito.spy(prodTOSOperationListener);

	ReflectionTestUtils.setField(instance, "lProdTOSOperationMap", Mockito.mock(ConcurrentHashMap.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", Mockito.mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", Mockito.mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "tOSHelper", Mockito.mock(TOSHelper.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(instance, "systemDAO", Mockito.mock(SystemDAO.class));

	lProdTOSOperationMap = new ConcurrentHashMap<>();
	actionQueues = new HashSet();
	tosActionQueue = Mockito.mock(TosActionQueue.class);
	actionQueues.add(tosActionQueue);
	lProdTOSOperationMap.put(DataWareHouse.getUser(), actionQueues);
	// instance.lProdTOSOperationMap = lProdTOSOperationMap;
	lProdLoad = Mockito.mock(ProductionLoads.class);
	impPlan = DataWareHouse.getPlan();
	systemLoad = impPlan.getSystemLoadList().get(0);
	system = systemLoad.getSystemId();
	split = Arrays.asList(impPlan.getId().split(","));
	relatedPlanDetails = new ArrayList<>();
	segmentRelatedPlans = new ArrayList<>();
	lProdLoads = new ArrayList();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoTOSOperation() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	instance.doTOSOperation();
    }

    @Test
    public void testDoTOSOperation1() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/LOADED";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);

	instance.doTOSOperation();
    }

    @Test
    public void testDoTOSOperation2() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/ONLINE";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);

	instance.doTOSOperation();
    }

    @Test
    public void testDoTOSOperation3() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/READY_FOR_PRODUCTION_DEPLOYMENT";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	lProdLoads.add(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getLastActionStatus()).thenReturn("FAILED");
	Mockito.when(instance.getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(impPlan.getId()), new com.tsi.workflow.beans.dao.System(system.getId()))).thenReturn(lProdLoads);

	instance.doTOSOperation();
    }

    @Test
    public void testDoTOSOperation4() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/READY_FOR_PRODUCTION_DEPLOYMENT";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	lProdLoads.add(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getLastActionStatus()).thenReturn("INPROGRESS");
	Mockito.when(instance.getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(impPlan.getId()), new com.tsi.workflow.beans.dao.System(system.getId()))).thenReturn(lProdLoads);
	instance.doTOSOperation();

    }

    @Test
    public void testDoTOSOperation5() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/READY_FOR_PRODUCTION_DEPLOYMENT";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	lProdLoads.add(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getLastActionStatus()).thenReturn("SUCCESS");
	Mockito.when(instance.getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(impPlan.getId()), new com.tsi.workflow.beans.dao.System(system.getId()))).thenReturn(lProdLoads);
	Mockito.when(tosActionQueue.getUser()).thenReturn(DataWareHouse.getUser());
	Mockito.when(tosActionQueue.getOldStatus()).thenReturn("OldStatus");
	Mockito.when(instance.getTOSHelper().doTOSOperation(DataWareHouse.getUser(), Constants.LoadSetCommands.LOAD, lProdLoad, "OldStatus", systemLoad)).thenReturn(true);

	instance.doTOSOperation();

    }

    @Test
    public void testDoTOSOperation6() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/READY_FOR_PRODUCTION_DEPLOYMENT";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	lProdLoads.add(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("ACTIVATED");
	Mockito.when(lProdLoad.getLastActionStatus()).thenReturn("INPROGRESS");
	Mockito.when(instance.getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(impPlan.getId()), new com.tsi.workflow.beans.dao.System(system.getId()))).thenReturn(lProdLoads);

	instance.doTOSOperation();
    }

    @Test
    public void testDoTOSOperation7() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/READY_FOR_PRODUCTION_DEPLOYMENT";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	lProdLoads.add(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getLastActionStatus()).thenReturn("ACTIVE");
	Mockito.when(instance.getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(impPlan.getId()), new com.tsi.workflow.beans.dao.System(system.getId()))).thenReturn(lProdLoads);
	instance.doTOSOperation();

    }

    @Test
    public void testDoTOSOperation8() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("LOADED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/READY_FOR_PRODUCTION_DEPLOYMENT";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	lProdLoads.add(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("ACTIVATED");
	Mockito.when(lProdLoad.getLastActionStatus()).thenReturn("ACTIVE");
	Mockito.when(instance.getProductionLoadsDAO().findInfoByPlanAndSystemId(new ImpPlan(impPlan.getId()), new com.tsi.workflow.beans.dao.System(system.getId()))).thenReturn(lProdLoads);

	instance.doTOSOperation();

    }

    @Test
    public void testDoTOSOperation10() {
	instance.getTestSystemSupportService();
	instance.getBuildDAO();
	instance.getFTPHelper();
    }

    @Test
    public void testDoTOSOperation9() {
	Mockito.when(instance.getImpPlanDAO().find(DataWareHouse.getPlan().getId())).thenReturn(impPlan);
	Mockito.when(tosActionQueue.getTosRecId()).thenReturn(1);
	Mockito.when(instance.getProductionLoadsDAO().find(1)).thenReturn(lProdLoad);
	Mockito.when(lProdLoad.getStatus()).thenReturn("ACTIVATED");
	Mockito.when(lProdLoad.getSystemId()).thenReturn(system);
	Mockito.when(lProdLoad.getPlanId()).thenReturn(impPlan);
	Mockito.when(instance.getSystemLoadDAO().find(impPlan, system)).thenReturn(systemLoad);
	Mockito.when(instance.getSystemDAO().find(systemLoad.getSystemId().getId())).thenReturn(system);
	Object[] obj = new Object[3];
	obj[0] = impPlan.getId();
	obj[1] = "APO/ONLINE";

	relatedPlanDetails.add(obj);
	segmentRelatedPlans.add(obj);
	Mockito.when(instance.getImpPlanDAO().getAllRelatedPlanDetail(lProdLoad.getPlanId().getId(), split)).thenReturn(relatedPlanDetails);
	Mockito.when(instance.getImpPlanDAO().getPreSegmentRelatedPlans(lProdLoad.getPlanId().getId())).thenReturn(segmentRelatedPlans);

	instance.doTOSOperation();
    }

}
