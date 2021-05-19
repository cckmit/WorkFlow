/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class JGitClientTest {

    public JGitClientTest() {
    }

    @Test
    public void testGetInstance() {
	JGitClient expResult = null;
	JGitClient result = JGitClient.getInstance();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetJGitAPI() throws IOException {
	JGitClient lClient = new JGitClient.Builder().connect("http://localhost:80", "", "").build();
	assertNotNull(lClient);
	JGitAPI jGitAPI = lClient.getJGitAPI();
	assertNotNull(jGitAPI);
    }

}
