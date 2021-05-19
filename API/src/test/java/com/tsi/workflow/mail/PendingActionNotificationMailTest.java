package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.WFConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PendingActionNotificationMailTest {

    @Mock
    WFConfig wfConfig;

    @InjectMocks
    PendingActionNotificationMail pendingActionNotificationMail;

    @Before
    public void setUp() throws Exception {
	MockitoAnnotations.initMocks(this);

    }

    @Test
    public void test00() throws Throwable {

	pendingActionNotificationMail.setAction("action");
	assertEquals("action", pendingActionNotificationMail.getAction());
	List<String> implementation = new ArrayList<String>();
	implementation.add("T1900111_002");
	implementation.add("T1900111_001");
	pendingActionNotificationMail.setImplementation(implementation);
	assertEquals(implementation, pendingActionNotificationMail.getImplementation());
	pendingActionNotificationMail.setTicketUrl("ticketUrl");
	assertEquals("ticketUrl", pendingActionNotificationMail.getTicketUrl());

	wfConfig.setProfileName("DL_delta");

	pendingActionNotificationMail.processMessage();
    }

}
