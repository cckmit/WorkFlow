package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TSDLoadSetDeactivateActivityMessageTest {

    private TSDLoadSetDeactivateActivityMessage tSDLoadSetDeactivateActivityMessage;

    @Before
    public void setUp() throws Exception {
	tSDLoadSetDeactivateActivityMessage = new TSDLoadSetDeactivateActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTSDLoadSetDeactivateActivityMessage() {
	tSDLoadSetDeactivateActivityMessage.setArguments(Mockito.any(IBeans.class));
	tSDLoadSetDeactivateActivityMessage.setLoadSetComment("Comments");
	assertEquals("Comments", tSDLoadSetDeactivateActivityMessage.getLoadSetComment());
	assertNotNull(tSDLoadSetDeactivateActivityMessage.getLogLevel());
	// CurrentDelegatedUser not null
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(DataWareHouse.getUser());
	tSDLoadSetDeactivateActivityMessage.setUser(user);
	tSDLoadSetDeactivateActivityMessage.processMessage();

    }

}
