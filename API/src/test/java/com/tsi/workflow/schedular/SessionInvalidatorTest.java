package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.CacheConfig;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class SessionInvalidatorTest {

    SessionInvalidator instance;
    CacheClient lCacheClient;
    LocalDateTime localDateTime;
    WFConfig wFConfig;

    @Before
    public void setUp() throws Exception {
	SessionInvalidator sessionInvalidator = new SessionInvalidator();
	instance = Mockito.spy(sessionInvalidator);
	lCacheClient = Mockito.spy(new CacheClient());
	ReflectionTestUtils.setField(lCacheClient, "cacheConfig", Mockito.mock(CacheConfig.class));
	ReflectionTestUtils.setField(instance, "lCacheClient", Mockito.mock(CacheClient.class));
	ReflectionTestUtils.setField(instance, "wsserver", Mockito.mock(WSMessagePublisher.class));
	ReflectionTestUtils.setField(lCacheClient, "wFConfig", Mockito.mock(WFConfig.class));
	localDateTime = LocalDateTime.now();
	lCacheClient = Mockito.mock(CacheClient.class);
	wFConfig = Mockito.mock(WFConfig.class);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testInvalidateSession() {

	instance.invalidateSession();
    }

}
