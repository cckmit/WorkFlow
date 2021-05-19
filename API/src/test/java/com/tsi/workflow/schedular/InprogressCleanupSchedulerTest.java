package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.hazelcast.core.IMap;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ImpPlanDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class InprogressCleanupSchedulerTest {

    InprogressCleanupScheduler instance;

    @Before
    public void setUp() throws Exception {
	InprogressCleanupScheduler inprogressCleanupScheduler = new InprogressCleanupScheduler();
	instance = Mockito.spy(inprogressCleanupScheduler);

	ReflectionTestUtils.setField(instance, "cacheClient", Mockito.mock(CacheClient.class));
	ReflectionTestUtils.setField(instance, "lImpPlanDAO", Mockito.mock(ImpPlanDAO.class));

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDoMonitor() {
	Mockito.when(instance.cacheClient.getAllRepositoryMap()).thenReturn(Mockito.mock(IMap.class));
	instance.doMonitor();
    }

}
