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
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.StagingBuildFailureMail;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class STAGELoaderMonitorTest {

    public STAGELoaderMonitorTest() {
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
     * Test of doMonitor method, of class STAGELoaderMonitor.
     */
    @Test
    public void testDoMonitor() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.approveProcessinBPMForPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), false)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor2() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor3() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenThrow(new IOException());
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
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
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(null);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenThrow(new IOException());
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
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
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(null);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenThrow(new IOException());
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor6() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"test\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}

    }

    @Test
    public void testDoMonitor7() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"test\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
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
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"test\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.approveProcessinBPMForPlan(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor9() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(new Build());
		when(instance.developerManagerService.approveProcessinBPMForPlan(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    @Test
    public void testDoMonitor10() {
	try {
	    STAGELoaderMonitor instance = new STAGELoaderMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingLoaderJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingLoaderJob);
	    instance.stagingLoaderJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingLoaderJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_LOAD)).thenReturn(null);
		when(instance.developerManagerService.approveProcessinBPMForPlan(Matchers.any(), Matchers.any(), Matchers.any())).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.developerManagerService.rejectPlan(stagingLoaderJob.getUser(), DataWareHouse.getPlan().getId(), "Staging Load Creation Failed", true, Boolean.TRUE)).thenReturn(DataWareHouse.getPositiveResponse());
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.ABORTED);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.BUILDING);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.CANCELLED);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.NOT_BUILT);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.REBUILDING);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.UNKNOWN);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.UNSTABLE);
	    } catch (IOException ex) {

	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

}
