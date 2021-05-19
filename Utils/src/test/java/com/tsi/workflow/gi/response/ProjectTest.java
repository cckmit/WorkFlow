package com.tsi.workflow.gi.response;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectTest {

    private Project project;

    @Before
    public void setUp() throws Exception {
	project = new Project();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProject() {
	project.setConfigFile(new ConfigFile());
	assertNotNull(project.getConfigFile());
	project.setPlatform("Platform");
	assertEquals("Platform", project.getPlatform());
	project.setProcessingType("ProcessingType");
	assertEquals("ProcessingType", project.getProcessingType());
	project.setProjName("Proj Name");
	assertEquals("Proj Name", project.getProjName());
	project.setTarget("XYZ");
	assertEquals("XYZ", project.getTarget());
	assertNotNull(project.getDirRule());
	assertNotNull(project.getFileRule());
    }

}
