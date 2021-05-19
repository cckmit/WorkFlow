/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular.jenkins;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class DEVLLoaderMonitorTest {

    public DEVLLoaderMonitorTest() {
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
     * Test of doMonitor method, of class DEVLLoaderMonitor.
     */
    @Test
    public void testDoMonitor() {
	DEVLLoaderMonitor instance = new DEVLLoaderMonitor();

	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develLoaderJob.add(jenkinsBuild);
	instance.develLoaderJob = develLoaderJob;
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor2() {
	DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develLoaderJob.add(jenkinsBuild);
	instance.develLoaderJob = develLoaderJob;
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
	try {
	    DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	    develLoaderJob.add(jenkinsBuild);
	    instance.develLoaderJob = develLoaderJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		// when(instance.lJenkinsClient.getPercentCompleted(null,
		// 0)).thenReturn(Long.parseLong("0"));
		when(jenkinsBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor4() {
	try {
	    DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	    develLoaderJob.add(jenkinsBuild);
	    instance.develLoaderJob = develLoaderJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		// when(instance.lJenkinsClient.getPercentCompleted(null,
		// 0)).thenReturn(Long.parseLong("0"));
		when(jenkinsBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("MTPF2713I: loadtpf ended, RC=0.");
		when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(new Build());
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor5() {
	try {
	    DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	    develLoaderJob.add(jenkinsBuild);
	    instance.develLoaderJob = develLoaderJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		// when(instance.lJenkinsClient.getPercentCompleted(null,
		// 0)).thenReturn(Long.parseLong("0"));
		when(jenkinsBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(new Build());
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {

	}
    }

    @Test
    public void testDoMonitor6() {
	DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develLoaderJob.add(jenkinsBuild);
	instance.develLoaderJob = develLoaderJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenThrow(new IOException());
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor7() {
	try {
	    DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	    develLoaderJob.add(jenkinsBuild);
	    instance.develLoaderJob = develLoaderJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		// when(instance.lJenkinsClient.getPercentCompleted(null,
		// 0)).thenReturn(Long.parseLong("0"));
		when(jenkinsBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"errorMessage\" : \"test\" }");
		when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(new Build());
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor8() {
	DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develLoaderJob.add(jenkinsBuild);
	instance.develLoaderJob = develLoaderJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.NOT_BUILT);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor9() {
	DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develLoaderJob.add(jenkinsBuild);
	instance.develLoaderJob = develLoaderJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.REBUILDING);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor10() {
	DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	develLoaderJob.add(jenkinsBuild);
	instance.develLoaderJob = develLoaderJob;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.UNKNOWN);
	    // when(instance.lJenkinsClient.getPercentCompleted(null,
	    // 0)).thenReturn(Long.parseLong("0"));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor11() {
	try {
	    DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	    develLoaderJob.add(jenkinsBuild);
	    instance.develLoaderJob = develLoaderJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		// when(instance.lJenkinsClient.getPercentCompleted(null,
		// 0)).thenReturn(Long.parseLong("0"));
		when(jenkinsBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("J");
		when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(new Build());
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {

	}
    }

    @Test
    public void testDoMonitor12() {
	try {
	    DEVLLoaderMonitor instance = new DEVLLoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild jenkinsBuild = mock(JenkinsBuild.class);
	    develLoaderJob.add(jenkinsBuild);
	    instance.develLoaderJob = develLoaderJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		// when(instance.lJenkinsClient.getPercentCompleted(null,
		// 0)).thenReturn(Long.parseLong("0"));
		when(jenkinsBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getDVLBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(new Build());
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {

	}
    }

}
