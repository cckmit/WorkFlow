package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectAddDirRuleTest {

    private ProjectAddDirRule projectAddDirRule;

    @Before
    public void setUp() throws Exception {
	projectAddDirRule = new ProjectAddDirRule();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProjectAddDirRule() {
	projectAddDirRule.setDirectory(new Directory());
	assertNotNull(projectAddDirRule.getDirectory());
	projectAddDirRule.setMask("Mask");
	assertEquals("Mask", projectAddDirRule.getMask());
	projectAddDirRule.setProjName("Project Name");
	assertEquals("Project Name", projectAddDirRule.getProjName());
	projectAddDirRule.setRecurse("Recurse");
	assertEquals("Recurse", projectAddDirRule.getRecurse());
    }

}
