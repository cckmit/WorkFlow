package com.tsi.workflow.schedular;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class PlanStagingSchedularTest {

    public PlanStagingSchedularTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test1() throws Exception {
	PlanStagingSchedular instance = spy(new PlanStagingSchedular());
	ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ConcurrentHashMap<String, User> mockPlanUPdate = new ConcurrentHashMap<>();
	mockPlanUPdate.put(DataWareHouse.getUser().getId(), DataWareHouse.getUser());
	ImpPlan newPlan = DataWareHouse.getPlan();
	newPlan.setPlanStatus(Constants.PlanStatus.SUBMITTED.name());
	HashMap<String, Serializable> pFilter = new HashMap();
	pFilter.put("planStatus", Constants.PlanStatus.SUBMITTED.name());
	List<ImpPlan> plans = new ArrayList<ImpPlan>();
	plans.add(newPlan);
	when(instance.impPlanDAO.findAll(pFilter)).thenReturn(plans);
	when(instance.cacheClient.getPlanUpdateStatusMap().get(newPlan.getId())).thenReturn(DataWareHouse.getUser());
	when(instance.cacheClient.getPlanUpdateStatusMap().containsKey(newPlan.getId())).thenReturn(Boolean.TRUE);
	instance.doApprove();
    }

}
