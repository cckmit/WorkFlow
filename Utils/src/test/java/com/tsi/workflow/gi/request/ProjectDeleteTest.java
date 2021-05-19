package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectDeleteTest {

    private ProjectDelete projectDelete;

    @Before
    public void setUp() throws Exception {
	projectDelete = new ProjectDelete();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProjectDelete() {
	projectDelete.setProjName("ProjName");
	assertEquals("ProjName", projectDelete.getProjName());
	assertNotNull(projectDelete);
    }

}
