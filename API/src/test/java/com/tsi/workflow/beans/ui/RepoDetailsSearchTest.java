package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepoDetailsSearchTest {

    private RepoDetailsSearch repoDetailsSearch;

    @Before
    public void setUp() throws Exception {
	repoDetailsSearch = new RepoDetailsSearch();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRepoDetailsSearch() {
	repoDetailsSearch.setDescription("Descripition");
	assertEquals("Descripition", repoDetailsSearch.getDescription());
	repoDetailsSearch.setFunctionalArea("ASD");
	assertEquals("ASD", repoDetailsSearch.getFunctionalArea());
	repoDetailsSearch.setRepoOwners("Repo Owners");
	assertEquals("Repo Owners", repoDetailsSearch.getRepoOwners());
	assertNotNull(repoDetailsSearch.toString());
    }

}
