package com.tsi.workflow.websocket;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class WSMessagePublisherTest {

    private WSMessagePublisher wSMessagePublisher;
    @Mock
    CacheClient cacheClient;

    @Before
    public void setUp() throws Exception {
	wSMessagePublisher = new WSMessagePublisher();
	wSMessagePublisher = Mockito.spy(new WSMessagePublisher());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWSMessagePublisher() {
	ReflectionTestUtils.setField(wSMessagePublisher, "cacheClient", Mockito.mock(CacheClient.class));
	// Mockito.when(methodCall);XXXXXXX
	wSMessagePublisher.sendMessage(Constants.Channels.AUTO_REJECT, DataWareHouse.getUser().getId(), new Object());
    }

}
