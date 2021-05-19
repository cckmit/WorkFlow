package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepoSearchFormTest {

    private RepoSearchForm repoSearchForm;

    @Before
    public void setUp() throws Exception {
	repoSearchForm = new RepoSearchForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRepoSearchForm() {
	repoSearchForm.setSegment("Segment");
	assertEquals("Segment", repoSearchForm.getSegment());
	repoSearchForm.setTargetSys(new ArrayList<Integer>());
	assertNotNull(repoSearchForm.getTargetSys());
	assertNotNull(repoSearchForm.toString());
    }

}
