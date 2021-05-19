package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;
import org.mockito.Mock;

public class PeerReviewActivityMessageTest {

    @Mock
    Implementation implementation;

    @Test
    public void test() throws Throwable {
	PeerReviewActivityMessage peerReviewActivityMessage = new PeerReviewActivityMessage(DataWareHouse.getPlan(), implementation);
	peerReviewActivityMessage.setStatus(Boolean.TRUE);
	peerReviewActivityMessage.setMessage("message");
	peerReviewActivityMessage.setUser(DataWareHouse.getUser());
	assertEquals(DataWareHouse.getUser(), peerReviewActivityMessage.getUser());
	assertEquals("message", peerReviewActivityMessage.getMessage());
	assertEquals(Boolean.TRUE, peerReviewActivityMessage.isStatus());
	peerReviewActivityMessage.processMessage();
    }

    @Test
    public void testSetArguments() throws Throwable {
	IBeans[] beans = null;
	PeerReviewActivityMessage instance = new PeerReviewActivityMessage(null, null);
	instance.setArguments(beans);
    }

}
