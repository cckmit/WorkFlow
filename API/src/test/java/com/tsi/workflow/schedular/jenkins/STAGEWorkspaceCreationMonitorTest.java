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
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.StagingConfigFileCreationFailureMail;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author yeshwanth.shenoy
 */
public class STAGEWorkspaceCreationMonitorTest {

    public STAGEWorkspaceCreationMonitorTest() {
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
     * Test of doMonitor method, of class STAGEWorkspaceCreationMonitor.
     */
    @Test
    public void testDoMonitor() {
	try {
	    STAGEWorkspaceCreationMonitor instance = new STAGEWorkspaceCreationMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingWorkspaceCreationJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingWorkspaceCreationJob);
	    instance.stagingWorkspaceCreationJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingWorkspaceCreationJob.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_CWS)).thenReturn(new Build());
		when(instance.developerManagerService.approveProcessinBPMForPlan(stagingWorkspaceCreationJob.getUser(), DataWareHouse.getPlan().getId(), false)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingConfigFileCreationFailureMail.class)).thenReturn(mock(StagingConfigFileCreationFailureMail.class));
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
	STAGEWorkspaceCreationMonitor instance = new STAGEWorkspaceCreationMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	JenkinsBuild stagingWorkspaceCreationJob = mock(JenkinsBuild.class);
	stagingLoaderJobs.add(stagingWorkspaceCreationJob);
	instance.stagingWorkspaceCreationJobs = stagingLoaderJobs;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenThrow(IOException.class);
	    when(stagingWorkspaceCreationJob.getSystemLoadId()).thenReturn("1");
	    when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	    when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_CWS)).thenReturn(new Build());
	    when(instance.developerManagerService.approveProcessinBPMForPlan(stagingWorkspaceCreationJob.getUser(), DataWareHouse.getPlan().getId(), false)).thenReturn(mock(JSONResponse.class));
	    when(instance.mailMessageFactory.getTemplate(StagingConfigFileCreationFailureMail.class)).thenReturn(mock(StagingConfigFileCreationFailureMail.class));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor3() {
	STAGEWorkspaceCreationMonitor instance = new STAGEWorkspaceCreationMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	JenkinsBuild stagingWorkspaceCreationJob = mock(JenkinsBuild.class);
	stagingLoaderJobs.add(stagingWorkspaceCreationJob);
	instance.stagingWorkspaceCreationJobs = stagingLoaderJobs;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(null);
	    when(stagingWorkspaceCreationJob.getSystemLoadId()).thenReturn("1");
	    when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	    when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_CWS)).thenReturn(new Build());
	    when(instance.developerManagerService.approveProcessinBPMForPlan(stagingWorkspaceCreationJob.getUser(), DataWareHouse.getPlan().getId(), false)).thenReturn(mock(JSONResponse.class));
	    when(instance.mailMessageFactory.getTemplate(StagingConfigFileCreationFailureMail.class)).thenReturn(mock(StagingConfigFileCreationFailureMail.class));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

    @Test
    public void testDoMonitor4() {
	try {
	    STAGEWorkspaceCreationMonitor instance = new STAGEWorkspaceCreationMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingWorkspaceCreationJob = mock(JenkinsBuild.class);
	    stagingLoaderJobs.add(stagingWorkspaceCreationJob);
	    instance.stagingWorkspaceCreationJobs = stagingLoaderJobs;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingWorkspaceCreationJob.getSystemLoadId()).thenReturn("1");
		SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
		systemLoad.getPlanId().setMacroHeader(Boolean.TRUE);
		when(instance.systemLoadDAO.find(1)).thenReturn(systemLoad);
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_CWS)).thenReturn(new Build());
		when(instance.developerManagerService.approveProcessinBPMForPlan(stagingWorkspaceCreationJob.getUser(), DataWareHouse.getPlan().getId(), false)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingConfigFileCreationFailureMail.class)).thenReturn(mock(StagingConfigFileCreationFailureMail.class));
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

    // @Test
    public void testDoMonitor5() {
	STAGEWorkspaceCreationMonitor instance = new STAGEWorkspaceCreationMonitor();
	ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs = new ConcurrentLinkedQueue<>();
	JenkinsBuild stagingWorkspaceCreationJob = mock(JenkinsBuild.class);
	stagingLoaderJobs.add(stagingWorkspaceCreationJob);
	instance.stagingWorkspaceCreationJobs = stagingLoaderJobs;
	try {
	    when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
	    when(stagingWorkspaceCreationJob.getSystemLoadId()).thenReturn("1");
	    SystemLoad systemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	    systemLoad.getPlanId().setMacroHeader(Boolean.FALSE);
	    when(instance.systemLoadDAO.find(1)).thenReturn(systemLoad);
	    when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
	    when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
	    when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_CWS)).thenReturn(new Build());
	    when(instance.developerManagerService.approveProcessinBPMForPlan(stagingWorkspaceCreationJob.getUser(), DataWareHouse.getPlan().getId(), false)).thenReturn(mock(JSONResponse.class));
	    when(instance.mailMessageFactory.getTemplate(StagingConfigFileCreationFailureMail.class)).thenReturn(mock(StagingConfigFileCreationFailureMail.class));
	} catch (IOException ex) {
	    // do nothing
	}
	instance.doMonitor();
    }

}
