package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.User;
import java.util.Date;
import java.util.HashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RejectPlansTest {

    private RejectPlans rejectPlans;

    @Before
    public void setUp() throws Exception {
	rejectPlans = new RejectPlans();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testRejectPlans() {
	rejectPlans.setUser(new User());
	assertNotNull(rejectPlans.getUser());
	rejectPlans.setDependentPlanIds(new HashMap<String, Date>());
	assertNotNull(rejectPlans.getDependentPlanIds());
	rejectPlans.setIsReject(Boolean.TRUE);
	assertTrue(rejectPlans.getIsReject());

    }

}
