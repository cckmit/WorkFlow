package com.tsi.workflow.gi.response;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectsTest {

    private Projects projects;

    @Before
    public void setUp() throws Exception {
	projects = new Projects();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
	assertNotNull(projects.getProject());

    }

}
