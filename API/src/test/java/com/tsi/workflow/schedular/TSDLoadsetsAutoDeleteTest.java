package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class TSDLoadsetsAutoDeleteTest {

    TSDLoadsetsAutoDelete instance;
    Date date;
    List<ProductionLoads> prodPlans;
    ProductionLoads productionLoads;
    List<Object[]> segmentRelatedPlans;
    Object[] plan;
    ImpPlan impPlan;

    @Before
    public void setUp() throws Exception {
	TSDLoadsetsAutoDelete tsdLoadsetsAutoDelete = new TSDLoadsetsAutoDelete();
	instance = Mockito.spy(tsdLoadsetsAutoDelete);
	ReflectionTestUtils.setField(instance, "wFConfig", Mockito.mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", Mockito.mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "tsdService", Mockito.mock(TSDService.class));
	ReflectionTestUtils.setField(instance, "lDAPAuthenticatorImpl", Mockito.mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "productionLoadsDAO", Mockito.mock(ProductionLoadsDAO.class));
	date = Date.from(LocalDateTime.now().minusDays(3).atZone(ZoneId.systemDefault()).toInstant());
	prodPlans = new ArrayList();
	productionLoads = DataWareHouse.getPlan().getProductionLoadsList().get(0);
	productionLoads.setStatus(Constants.LOAD_SET_STATUS.LOADED.name());
	productionLoads.setLastActionStatus("FAILED");
	prodPlans.add(productionLoads);
	segmentRelatedPlans = new ArrayList();
	plan = new Object[2];
	plan[0] = "";
	plan[1] = "";
	segmentRelatedPlans.add(plan);
	impPlan = Mockito.mock(ImpPlan.class);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDeleteLoadsetFromTSD() {
	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	Mockito.when(instance.getProductionLoadsDAO().getProdLoadDeactivatedPlans(Mockito.any(Date.class))).thenReturn(prodPlans);
	plan[1] = "READY_FOR_PRODUCTION_DEPLOYMENT";
	plan[0] = "";
	Mockito.when(instance.getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallback(productionLoads.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	instance.deleteLoadsetFromTSD();
    }

    @Test
    public void testDeleteLoadsetFromTSD1() {
	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	Mockito.when(instance.getProductionLoadsDAO().getProdLoadDeactivatedPlans(Mockito.any(Date.class))).thenReturn(prodPlans);
	plan[0] = "READY_FOR_PRODUCTION_DEPLOYMENT";
	plan[1] = "";
	Mockito.when(instance.getImpPlanDAO().getPostSegmentRelatedPlansProdLoadFallback(productionLoads.getPlanId().getId())).thenReturn(segmentRelatedPlans);
	Mockito.when(instance.lDAPAuthenticatorImpl.getServiceUser()).thenReturn(DataWareHouse.getUser());
	Mockito.when(instance.getTSDService().postActivationAction(DataWareHouse.getUser(), productionLoads, false, StringUtils.EMPTY)).thenReturn(new JSONResponse());
	instance.deleteLoadsetFromTSD();
    }

}
