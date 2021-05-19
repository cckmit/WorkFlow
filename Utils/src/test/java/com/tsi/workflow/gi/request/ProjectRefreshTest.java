package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProjectRefreshTest {

    private ProjectRefresh projectRefresh;

    @Before
    public void setUp() throws Exception {
	projectRefresh = new ProjectRefresh();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testProjectRefresh() {
	projectRefresh.setProjName("Proj Name");
	assertEquals("Proj Name", projectRefresh.getProjName());
	assertNotNull(projectRefresh);

    }

}
