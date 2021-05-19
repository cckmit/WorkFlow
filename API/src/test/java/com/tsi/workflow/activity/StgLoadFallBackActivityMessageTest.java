package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class StgLoadFallBackActivityMessageTest {

    private StgLoadFallBackActivityMessage stgLoadFallBackActivityMessage;

    @Before
    public void setUp() throws Exception {
	stgLoadFallBackActivityMessage = new StgLoadFallBackActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStgLoadFallBackActivityMessage() {
	stgLoadFallBackActivityMessage.setArguments(Mockito.any(IBeans.class));
	stgLoadFallBackActivityMessage.setlPlanList(getPlanList());
	assertNotNull(stgLoadFallBackActivityMessage.getlPlanList());
	stgLoadFallBackActivityMessage.setUser(DataWareHouse.getUser());
	stgLoadFallBackActivityMessage.processMessage();
	assertNotNull(stgLoadFallBackActivityMessage.getLogLevel());
	stgLoadFallBackActivityMessage = new StgLoadFallBackActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), Mockito.any(IBeans.class));
    }

    private Set<String> getPlanList() {
	Set<String> set = new HashSet<>();
	set.add(DataWareHouse.getPlan().getId());
	return set;
    }

}
