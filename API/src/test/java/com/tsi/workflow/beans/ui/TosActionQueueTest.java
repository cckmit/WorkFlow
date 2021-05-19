package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TosActionQueueTest {

    private TosActionQueue tosActionQueue;

    @Before
    public void setUp() throws Exception {
	tosActionQueue = new TosActionQueue();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTosActionQueue() {
	tosActionQueue.setOldStatus("OldStatus");
	assertEquals("OldStatus", tosActionQueue.getOldStatus());
	tosActionQueue.setTosRecId(89);
	assertEquals(new Integer(89), tosActionQueue.getTosRecId());
	tosActionQueue.setUser(new User());
	assertNotNull(tosActionQueue.getUser());
	TosActionQueue tosActionQueue1 = new TosActionQueue(tosActionQueue.getUser(), tosActionQueue.getOldStatus(), tosActionQueue.getTosRecId());

    }

}
