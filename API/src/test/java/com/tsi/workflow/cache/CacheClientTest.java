package com.tsi.workflow.cache;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hazelcast.core.HazelcastInstance;
import com.tsi.workflow.CacheConfig;
import com.tsi.workflow.WFConfig;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

public class CacheClientTest {

    @Mock
    private HazelcastInstance lClient;

    @Test(expected = NullPointerException.class)
    public void cacheClientTest() throws Exception {

	CacheClient instance = new CacheClient();
	ReflectionTestUtils.setField(instance, "cacheConfig", mock(CacheConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "cacheConfig", mock(CacheConfig.class));

	when(instance.getCacheInstance()).thenReturn(lClient);

    }
}
