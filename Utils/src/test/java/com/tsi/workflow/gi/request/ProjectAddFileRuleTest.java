package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectAddFileRuleTest {

    private ProjectAddFileRule projectAddFileRule;

    @Before
    public void setUp() throws Exception {
	projectAddFileRule = new ProjectAddFileRule();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProjectAddFileRule() {
	projectAddFileRule.setFile(new File());
	assertNotNull(projectAddFileRule.getFile());
	projectAddFileRule.setProjName("ProjName");
	assertEquals("ProjName", projectAddFileRule.getProjName());

    }

}
