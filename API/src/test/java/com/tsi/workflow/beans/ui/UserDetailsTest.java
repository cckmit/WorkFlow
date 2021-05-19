package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDetailsTest {

    private UserDetails userDetails;

    @Before
    public void setUp() throws Exception {
	userDetails = new UserDetails();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
	userDetails.setId(757);
	assertEquals(new Integer(757), userDetails.getId());
	userDetails.setPermission("Permission");
	assertEquals("Permission", userDetails.getPermission());
	userDetails.setRepoid(56);
	assertEquals(new Integer(56), userDetails.getRepoid());
	userDetails.setUsername("User Name");
	assertEquals("User Name", userDetails.getUsername());
	userDetails.setRepoName("Repo Name");
	assertEquals("Repo Name", userDetails.getRepoName());
    }

}
