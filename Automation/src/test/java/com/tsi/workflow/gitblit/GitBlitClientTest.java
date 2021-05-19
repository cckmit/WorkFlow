/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class GitBlitClientTest {

    public GitBlitClientTest() {
    }

    @Test
    public void testGetInstance() {
	GitBlitClient expResult = null;
	GitBlitClient result = GitBlitClient.getInstance();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetJGitAPI() throws IOException {
	GitBlitClient lClient = new GitBlitClient.Builder().connect("http://localhost:80", "", "").build();
	assertNotNull(lClient);
	GitBlitAPI jGitAPI = lClient.getGitBlitAPI();
	assertNotNull(jGitAPI);
    }

}
