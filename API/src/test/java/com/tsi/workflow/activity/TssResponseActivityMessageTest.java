package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TssResponseActivityMessageTest {

    private TssResponseActivityMessage tssResponseActivityMessage;

    @Before
    public void setUp() throws Exception {
	tssResponseActivityMessage = new TssResponseActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTssResponseActivityMessage() {
	tssResponseActivityMessage.setArguments(Mockito.any(IBeans.class));
	tssResponseActivityMessage.setEnv("ENV");
	assertEquals("ENV", tssResponseActivityMessage.getEnv());
	tssResponseActivityMessage.setLoadStatus("LoadStatus");
	assertEquals("LoadStatus", tssResponseActivityMessage.getLoadStatus());
	tssResponseActivityMessage.setSystemName("XYZ");
	assertEquals("XYZ", tssResponseActivityMessage.getSystemName());
	tssResponseActivityMessage.setUser(DataWareHouse.getUser());
	tssResponseActivityMessage.setVparName("VparName");
	assertEquals("VparName", tssResponseActivityMessage.getVparName());
	tssResponseActivityMessage.processMessage();
	assertNotNull(tssResponseActivityMessage.getLogLevel());
    }

}
