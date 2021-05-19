package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectCreateTest {

    private ProjectCreate projectCreate;

    @Before
    public void setUp() throws Exception {
	projectCreate = new ProjectCreate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
	projectCreate.setConfigFile(new ConfigFile());
	assertNotNull(projectCreate.getConfigFile());
	projectCreate.setPlatform("Platform");
	assertEquals("Platform", projectCreate.getPlatform());
	projectCreate.setProcessingType("ProcessingType");
	assertEquals("ProcessingType", projectCreate.getProcessingType());
	projectCreate.setProjName("ProjName");
	assertEquals("ProjName", projectCreate.getProjName());
	projectCreate.setTarget("XYZ");
	assertEquals("XYZ", projectCreate.getTarget());

    }

}
