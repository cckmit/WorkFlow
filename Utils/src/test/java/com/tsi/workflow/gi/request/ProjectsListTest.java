package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectsListTest {

    private ProjectsList projectsList;

    @Before
    public void setUp() throws Exception {
	projectsList = new ProjectsList();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProjectsList() {
	projectsList.setEMPTY(new EMPTY());
	assertNotNull(projectsList.getEMPTY());
	assertNotNull(projectsList);
    }
}
