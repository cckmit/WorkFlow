package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TSDPlanDeactivateMailTest {

    private TSDPlanDeactivateMail tSDPlanDeactivateMail;

    @Before
    public void setUp() throws Exception {
	tSDPlanDeactivateMail = new TSDPlanDeactivateMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTSDPlanDeactivateMail() {
	tSDPlanDeactivateMail.setDependentPlan(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), tSDPlanDeactivateMail.getDependentPlan());
	tSDPlanDeactivateMail.setLoadDateTime(DataWareHouse.getPlan().getApproveDateTime());
	assertEquals(DataWareHouse.getPlan().getApproveDateTime(), tSDPlanDeactivateMail.getLoadDateTime());
	tSDPlanDeactivateMail.setParentPlan(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), tSDPlanDeactivateMail.getParentPlan());
	tSDPlanDeactivateMail.setTargetSystem(getTargetSystemSet());
	assertNotNull(tSDPlanDeactivateMail.getTargetSystem());
	tSDPlanDeactivateMail.setTsdDeactivateFlag(true);
	assertTrue(tSDPlanDeactivateMail.getTsdDeactivateFlag());
	// TSDDeactivate Flag : True
	tSDPlanDeactivateMail.processMessage();

	// TSDDeactivate Flag : False
	tSDPlanDeactivateMail.setTsdDeactivateFlag(false);
	tSDPlanDeactivateMail.processMessage();

    }

    private Set<String> getTargetSystemSet() {
	Set<String> set = new HashSet<>();
	set.add(DataWareHouse.getSystemList().get(0).getName());
	return set;
    }

}
