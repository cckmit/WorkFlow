package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TSDDependentPlanMailTest {

    private TSDDependentPlanMail tSDDependentPlanMail;

    @Before
    public void setUp() throws Exception {
	tSDDependentPlanMail = new TSDDependentPlanMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testTSDDependentPlanMail() {
	tSDDependentPlanMail.setDependentPlan(getDependentPlan());
	assertNotNull(tSDDependentPlanMail.getDependentPlan());
	tSDDependentPlanMail.setLoadedPlan(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), tSDDependentPlanMail.getLoadedPlan());
	tSDDependentPlanMail.processMessage();
	assertEquals("T1700484 loaded to Production having same source artifacts as your [T1700484] Please take below actions:  <br><br>  1. Please make sure your plan has latest changes and update load date if you are still planning to go to PROD with all your changes.  <br><br>  2. If your plan is no longer valid then please delete Implementation(s)/Implementation Plan to avoid any dependency for others . ", tSDDependentPlanMail.getMessage());
    }

    private Set<String> getDependentPlan() {
	Set<String> set = new HashSet<>();
	set.add(DataWareHouse.getPlan().getId());
	return set;
    }

}
