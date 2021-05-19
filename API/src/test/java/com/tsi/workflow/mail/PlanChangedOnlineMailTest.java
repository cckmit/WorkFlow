package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class PlanChangedOnlineMailTest {

    @Test
    public void test00() throws Throwable {
	PlanChangedOnlineMail planChangedOnlineMail = new PlanChangedOnlineMail();
	Map<String, Date> loadDateTargetSys = new HashMap<String, Date>();
	loadDateTargetSys.put("WSP", new Date());
	loadDateTargetSys.put("APO", new Date());
	planChangedOnlineMail.setLoadDateTargetSys(loadDateTargetSys);
	assertNotNull(planChangedOnlineMail.getLoadDateTargetSys());
	planChangedOnlineMail.setPlanId("T1900111");
	assertEquals("T1900111", planChangedOnlineMail.getPlanId());
	planChangedOnlineMail.setTosServerId("tosServerId");
	assertEquals("tosServerId", planChangedOnlineMail.getTosServerId());
	planChangedOnlineMail.processMessage();

    }

}
