package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import org.junit.Test;

public class DSLFilePopulateFailureMailTest {

    @Test
    public void test00() throws Throwable {
	DSLFilePopulateFailureMail dSLFilePopulateFailureMail = new DSLFilePopulateFailureMail();
	dSLFilePopulateFailureMail.setLoadSetName("G19002T1");
	assertEquals("G19002T1", dSLFilePopulateFailureMail.getLoadSetName());
	dSLFilePopulateFailureMail.setCalendarFileName("calendarFileName");
	assertEquals("calendarFileName", dSLFilePopulateFailureMail.getCalendarFileName());
	dSLFilePopulateFailureMail.setDurationMins(100);
	assertNotEquals("", dSLFilePopulateFailureMail.getDurationMins());
	dSLFilePopulateFailureMail.setPlanId("T1900111");
	assertEquals("T1900111", dSLFilePopulateFailureMail.getPlanId());
	dSLFilePopulateFailureMail.processMessage();
    }

}
