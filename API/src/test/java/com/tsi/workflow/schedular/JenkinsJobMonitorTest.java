/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static org.mockito.Mockito.mock;

import com.tsi.workflow.schedular.jenkins.DEVLBuildMonitor;
import com.tsi.workflow.schedular.jenkins.DEVLLoaderMonitor;
import com.tsi.workflow.schedular.jenkins.STAGEBuildMonitor;
import com.tsi.workflow.schedular.jenkins.STAGELoaderMonitor;
import com.tsi.workflow.schedular.jenkins.STAGEWorkspaceCreationMonitor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author yeshwanth.shenoy
 */
public class JenkinsJobMonitorTest {

    public JenkinsJobMonitorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of doMonitor method, of class JenkinsJobMonitor.
     */
    @Test
    public void testDoMonitor() {

	JenkinsJobMonitor instance = new JenkinsJobMonitor();
	ReflectionTestUtils.setField(instance, "dEVLBuildMonitor", mock(DEVLBuildMonitor.class));
	ReflectionTestUtils.setField(instance, "dEVLLoaderMonitor", mock(DEVLLoaderMonitor.class));
	ReflectionTestUtils.setField(instance, "sTAGEBuildMonitor", mock(STAGEBuildMonitor.class));
	ReflectionTestUtils.setField(instance, "sTAGELoaderMonitor", mock(STAGELoaderMonitor.class));
	ReflectionTestUtils.setField(instance, "sTAGEWorkspaceCreationMonitor", mock(STAGEWorkspaceCreationMonitor.class));
	instance.doMonitor();
    }

    /**
     * Test of checkIsRunning method, of class JenkinsJobMonitor.
     */
    @Test
    public void testCheckIsRunning() {

	JenkinsJobMonitor instance = new JenkinsJobMonitor();
	ReflectionTestUtils.setField(instance, "dEVLBuildMonitor", mock(DEVLBuildMonitor.class));
	ReflectionTestUtils.setField(instance, "dEVLLoaderMonitor", mock(DEVLLoaderMonitor.class));
	ReflectionTestUtils.setField(instance, "sTAGEBuildMonitor", mock(STAGEBuildMonitor.class));
	ReflectionTestUtils.setField(instance, "sTAGELoaderMonitor", mock(STAGELoaderMonitor.class));
	ReflectionTestUtils.setField(instance, "sTAGEWorkspaceCreationMonitor", mock(STAGEWorkspaceCreationMonitor.class));
	instance.checkIsRunning();
    }

}
