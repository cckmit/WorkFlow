/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular.jenkins;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
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
public class DEVLBuildMonitorTest {

    public DEVLBuildMonitorTest() {
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
     * Test of doMonitor method, of class DEVLBuildMonitor.
     */
    @Test
    public void testDoMonitor() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor2() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.BUILDING);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor3() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	    when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild("DVL", null, 0, Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(new Build());
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor4() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	    when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild("DVL", null, 0, Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(new Build());
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor5() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.CANCELLED);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	    when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild("DVL", null, 0, Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(new Build());
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor6() throws IOException {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	// when(instance.lJenkinsClient.getPercentCompleted(null)).thenReturn(Long.parseLong("10"));
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor7() throws IOException {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	// when(instance.lJenkinsClient.getPercentCompleted(null)).thenReturn(Long.parseLong("10"));
	when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.ABORTED);
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor8() throws IOException {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	// when(instance.lJenkinsClient.getPercentCompleted(null)).thenReturn(Long.parseLong("10"));
	when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.ABORTED);
	when(instance.buildDAO.findByBuild("DVL", null, 0, Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(new Build());
	when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"-1\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor9() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"FALSE\" }");
	    when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild("DVL", null, 0, Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(new Build());
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor10() {
	DEVLBuildMonitor instance = new DEVLBuildMonitor();
	ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemDAO", mock(SystemDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentHashMap<String, JenkinsBuild> develBuildJob = new ConcurrentHashMap<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develBuildJob.put(Constants.BUILD_TYPE.DVL_BUILD.name(), jenkinsBuild);
	instance.develBuildJob = develBuildJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSONS");
	    when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild("DVL", null, 0, Constants.BUILD_TYPE.DVL_BUILD)).thenReturn(new Build());
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }
}
