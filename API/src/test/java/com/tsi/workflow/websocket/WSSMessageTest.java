package com.tsi.workflow.websocket;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.utils.Constants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSSMessageTest {

    private WSSMessage wSSMessage;

    @Before
    public void setUp() throws Exception {
	wSSMessage = new WSSMessage(Constants.Channels.AUTO_REJECT, DataWareHouse.getUser().getId(), DataWareHouse.getPlan().getId(), new Object());
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testWSSMessage() {
	wSSMessage.setBroadCastPath("Broadcastpath");
	assertEquals("Broadcastpath", wSSMessage.getBroadCastPath());
	wSSMessage.setChannel(Constants.Channels.AUTO_REJECT);
	assertEquals(Constants.Channels.AUTO_REJECT, wSSMessage.getChannel());
	wSSMessage.setFinalMessage(Boolean.TRUE);
	assertTrue(wSSMessage.isFinalMessage());
	wSSMessage.setMessage(new Object());
	assertNotNull(wSSMessage.getMessage());
	wSSMessage.setPlanId(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), wSSMessage.getPlanId());
	wSSMessage.setUserId(DataWareHouse.getUser().getId());
	assertEquals(DataWareHouse.getUser().getId(), wSSMessage.getUserId());
	wSSMessage.setUserIdAndChannel("UserIdandChannel");
	assertEquals("UserIdandChannel", wSSMessage.getUserIdAndChannel());
	wSSMessage.switchChannel();
	WSSMessage WSSMessage1 = new WSSMessage(wSSMessage.getChannel(), wSSMessage.getUserId(), wSSMessage.getPlanId(), wSSMessage.getMessage(), wSSMessage.isFinalMessage());
    }

}
