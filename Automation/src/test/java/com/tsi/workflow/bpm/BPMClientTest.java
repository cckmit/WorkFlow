/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class BPMClientTest {

    public BPMClientTest() {
    }

    @Test
    public void testGetInstance() {
	BPMClient expResult = null;
	BPMClient result = BPMClient.getInstance();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetJGitAPI() throws IOException {
	BPMClient lClient = new BPMClient.Builder().connect("http://localhost:80", "", "").build();
	assertNotNull(lClient);
	ActivitiBPMAPI jGitAPI = lClient.getActivitiBPMAPI();
	assertNotNull(jGitAPI);
    }

}
