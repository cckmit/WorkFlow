package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectSetTargetTest {

    private ProjectSetTarget projectSetTarget;

    @Before
    public void setUp() throws Exception {
	projectSetTarget = new ProjectSetTarget();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProjectSetTarget() {
	projectSetTarget.setProjName("Proj Name");
	assertEquals("Proj Name", projectSetTarget.getProjName());
	projectSetTarget.setTarget("XYZ");
	assertEquals("XYZ", projectSetTarget.getTarget());
	assertNotNull(projectSetTarget);
    }

}
