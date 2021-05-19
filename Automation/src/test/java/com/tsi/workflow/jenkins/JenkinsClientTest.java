/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;
import com.offbytwo.jenkins.model.QueueReference;
import com.tsi.workflow.User;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.interfaces.IJenkinsConfig;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class JenkinsClientTest {

    public JenkinsClientTest() {
    }

    JenkinsClient instance = spy(new JenkinsClient());

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	ReflectionTestUtils.setField(instance, "lServer", mock(JenkinsServer.class));
	ReflectionTestUtils.setField(instance, "jenkinsConfig", mock(IJenkinsConfig.class));
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getJobXMLByName method, of class JenkinsClient.
     */
    @Test
    public void testGetJobXMLByName() {

	String pJobName = "";
	instance.getJobXMLByName(pJobName);
	pJobName = null;
	instance.getJobXMLByName(pJobName);

    }

    @Test
    public void testGetAllJobs() {
	ReflectionTestUtils.invokeMethod(instance, "getAllJobs");
	ReflectionTestUtils.invokeMethod(instance, "getJobByName", "");
	ReflectionTestUtils.setField(instance, "lServer", mock(JenkinsServer.class));
	ReflectionTestUtils.invokeMethod(instance, "getAllJobs");
	ReflectionTestUtils.invokeMethod(instance, "getJobByName", "");
    }

    @Test
    public void testJenkinsClient() {
	try {

	    String pJobName = "";
	    JenkinsClient jClient = spy(new JenkinsClient(mock(IJenkinsConfig.class)));
	} catch (IOException ex) {
	    Logger.getLogger(JenkinsClientTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of createJobByXML method, of class JenkinsClient.
     */
    @Test
    public void testCreateJobByXML() {

	String pJobName = "";
	String pXML = "";

	instance.createJobByXML(pJobName, pXML);
	try {
	    when(instance.lServer.getJob(pJobName)).thenReturn(mock(JobWithDetails.class));
	    instance.createJobByXML(pJobName, pXML);
	} catch (Exception e) {
	    Assert.assertTrue(true);
	}
    }

    /**
     * Test of validateJob method, of class JenkinsClient.
     */
    @Test
    public void testValidateJob() {

	String pJobName = "";
	try {
	    instance.validateJob(pJobName);

	} catch (Exception e) {
	    Assert.assertTrue(true);
	}
	JobWithDetails jobWithDetails = mock(JobWithDetails.class);
	try {
	    when(instance.lServer.getJob(pJobName)).thenReturn(jobWithDetails);
	    instance.validateJob(pJobName);
	} catch (Exception e) {
	    Assert.assertTrue(true);
	}
	when(jobWithDetails.isBuildable()).thenReturn(true);
	instance.validateJob(pJobName);

	try {
	    when(jobWithDetails.isInQueue()).thenReturn(true);
	    instance.validateJob(pJobName);
	} catch (WorkflowException e) {
	    Assert.assertTrue(true);
	}
    }

    /**
     * Test of executeJob method, of class JenkinsClient.
     */
    @Test
    public void testExecuteJob() {

	User user = null;
	String pJobName = "";
	Map<String, String> pParams = null;
	try {
	    when(instance.lServer.getJob(pJobName)).thenReturn(mock(JobWithDetails.class));
	    JenkinsBuild result = instance.executeJob(user, pJobName, pParams);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

    /**
     * Test of stopBuild method, of class JenkinsClient.
     */
    @Test
    public void testStopBuild() throws Exception {

	String pJobName = "";
	Integer pBuildNumber = 0;
	JobWithDetails jobWithDetails = mock(JobWithDetails.class);
	when(instance.lServer.getJob(pJobName)).thenReturn(jobWithDetails);
	Build build = mock(Build.class);
	BuildWithDetails buildDetails = mock(BuildWithDetails.class);
	when(jobWithDetails.getBuildByNumber(0)).thenReturn(build);
	when(build.details()).thenReturn(buildDetails);
	when(buildDetails.getClient()).thenReturn(mock(JenkinsHttpClient.class));
	when(buildDetails.isBuilding()).thenReturn(true);
	Boolean result = instance.stopBuild(pJobName, pBuildNumber);
	assertTrue(result);
	when(buildDetails.isBuilding()).thenReturn(false);
	result = instance.stopBuild(pJobName, pBuildNumber);
	assertFalse(result);

    }

    /**
     * Test of getJobResult method, of class JenkinsClient.
     */
    @Test
    public void testGetJobResult() throws Exception {

	String pJobName = "";
	Integer pBuildNumber = 0;
	ReflectionTestUtils.setField(instance, "lServer", mock(JenkinsServer.class));
	JobWithDetails jd = mock(JobWithDetails.class);
	when(instance.lServer.getJob(pJobName)).thenReturn(jd);
	when(jd.getBuildByNumber(0)).thenReturn(null);
	BuildResult expResult = null;
	BuildResult result = instance.getJobResult(pJobName, pBuildNumber);
	assertEquals(expResult, result);

	Build build = mock(Build.class);
	BuildWithDetails buildDetails = mock(BuildWithDetails.class);
	when(jd.getBuildByNumber(0)).thenReturn(build);
	when(build.details()).thenReturn(buildDetails);
	instance.getJobResult(pJobName, pBuildNumber);

    }

    /**
     * Test of getPercentCompleted method, of class JenkinsClient.
     */
    // @Test
    // public void testGetPercentCompleted_String_Integer() throws Exception {
    //
    // String pJobName = "";
    // Integer pBuildNumber = 0;
    //
    // Long expResult = 0L;
    // ReflectionTestUtils.setField(instance, "lServer", mock(JenkinsServer.class));
    // JobWithDetails jd = mock(JobWithDetails.class);
    // when(instance.lServer.getJob(pJobName)).thenReturn(jd);
    // when(jd.getBuildByNumber(0)).thenReturn(null);
    // Long result = instance.getPercentCompleted(pJobName, pBuildNumber);
    // assertEquals(expResult, result);
    //
    // Build build = mock(Build.class);
    // BuildWithDetails buildDetails = mock(BuildWithDetails.class);
    // when(jd.getBuildByNumber(0)).thenReturn(build);
    // when(build.details()).thenReturn(buildDetails);
    // when(buildDetails.isBuilding()).thenReturn(true);
    // when(buildDetails.getDuration()).thenReturn(10L);
    // when(buildDetails.getEstimatedDuration()).thenReturn(10L);
    // instance.getPercentCompleted(pJobName, pBuildNumber);
    //
    // }

    /**
     * Test of getPercentCompleted method, of class JenkinsClient.
     */
    // @Test
    // public void testGetPercentCompleted_String() throws Exception {
    //
    // String pQueueUrl = "";
    //
    // Long expResult = null;
    // Long result = instance.getPercentCompleted(pQueueUrl);
    // assertEquals(expResult, result);
    //
    // }

    /**
     * Test of getBuildNumber method, of class JenkinsClient.
     */
    // @Test
    public void testGetBuildNumber() throws Exception {

	String pQueueUrl = "";
	instance = spy(new JenkinsClient());
	Integer expResult = null;
	ReflectionTestUtils.setField(instance, "lServer", mock(JenkinsServer.class));
	QueueItem qu = new QueueItem();
	QueueReference qref = new QueueReference(pQueueUrl);
	qu.setCancelled(true);
	when(instance.lServer.getQueueItem(qref)).thenReturn(qu);
	Integer result = instance.getBuildNumber(pQueueUrl);
	assertEquals(expResult, result);

    }

    /**
     * Test of getConsoleLog method, of class JenkinsClient.
     */
    @Test
    public void testGetConsoleLog() throws Exception {

	String jobName = "";
	int buildNumber = 0;

	String expResult = "";
	ReflectionTestUtils.setField(instance, "lServer", mock(JenkinsServer.class));
	JobWithDetails jd = mock(JobWithDetails.class);
	when(instance.lServer.getJob(jobName)).thenReturn(jd);
	String result = instance.getConsoleLog(jobName, buildNumber);
	assertEquals(expResult, result);

	Build build = mock(Build.class);
	BuildWithDetails buildDetails = mock(BuildWithDetails.class);
	when(jd.getBuildByNumber(0)).thenReturn(build);
	when(build.details()).thenReturn(buildDetails);
	when(buildDetails.getConsoleOutputText()).thenReturn("");
	instance.getConsoleLog(jobName, buildNumber);

    }

    /**
     * Test of isInQueue method, of class JenkinsClient.
     */
    @Test
    public void testIsInQueue() {

	try {
	    String pJobName = "";

	    Boolean expResult = false;
	    Boolean result = instance.isInQueue(pJobName);
	    assertEquals(expResult, result);

	    JobWithDetails jd = mock(JobWithDetails.class);
	    when(instance.lServer.getJob(pJobName)).thenReturn(jd);
	    when(jd.isInQueue()).thenReturn(true);
	    instance.isInQueue(pJobName);
	} catch (IOException ex) {
	    Logger.getLogger(JenkinsClientTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

    /**
     * Test of isBuildable method, of class JenkinsClient.
     */
    @Test
    public void testIsBuildable() {

	try {
	    String pJobName = "";

	    Boolean expResult = false;
	    Boolean result = instance.isBuildable(pJobName);
	    assertEquals(expResult, result);

	    JobWithDetails jd = mock(JobWithDetails.class);
	    when(instance.lServer.getJob(pJobName)).thenReturn(jd);
	    when(jd.isBuildable()).thenReturn(true);
	    instance.isBuildable(pJobName);
	} catch (IOException ex) {
	    Logger.getLogger(JenkinsClientTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    /**
     * Test of getLastBuild method, of class JenkinsClient.
     */
    @Test
    public void testGetLastBuild() {

	try {
	    String pJobName = "";

	    Build expResult = null;
	    Build result = instance.getLastBuild(pJobName);
	    assertEquals(expResult, result);

	    JobWithDetails jd = mock(JobWithDetails.class);
	    when(instance.lServer.getJob(pJobName)).thenReturn(jd);
	    when(jd.getLastBuild()).thenReturn(mock(Build.class));
	    instance.getLastBuild(pJobName);
	} catch (IOException ex) {
	    Logger.getLogger(JenkinsClientTest.class.getName()).log(Level.SEVERE, null, ex);
	}
    }

}
