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
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.StagingBuildFailureMail;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.service.ProtectedService;
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
 * @author deepa.jayakumar
 */
public class STAGEBuildMonitorTest {

    public STAGEBuildMonitorTest() {
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

    @Test
    public void testDoMonitor() {
	try {
	    STAGEBuildMonitor instance = new STAGEBuildMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develBuildJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingBuild = mock(JenkinsBuild.class);
	    develBuildJob.add(stagingBuild);
	    instance.stagingBuildJobs = develBuildJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingBuild.getUser(), DataWareHouse.getPlan().getId(), "No DEVL loadset found", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
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
	    STAGEBuildMonitor instance = new STAGEBuildMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develBuildJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingBuild = mock(JenkinsBuild.class);
	    develBuildJob.add(stagingBuild);
	    instance.stagingBuildJobs = develBuildJob;
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingBuild.getSystemLoadId()).thenReturn("1");
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"2\" , \"failedCount\" : \"0\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingBuild.getUser(), DataWareHouse.getPlan().getId(), "", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
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
	    STAGEBuildMonitor instance = new STAGEBuildMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "protectedService", mock(ProtectedService.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develBuildJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingBuild = mock(JenkinsBuild.class);
	    when(stagingBuild.getUser()).thenReturn(DataWareHouse.getUser());
	    develBuildJob.add(stagingBuild);
	    instance.stagingBuildJobs = develBuildJob;
	    JSONResponse jSONResponse = mock(JSONResponse.class);
	    jSONResponse.setStatus(Boolean.FALSE);
	    // when(jSONResponse.getStatus()).thenReturn(Boolean.FALSE);
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingBuild.getSystemLoadId()).thenReturn("1");
		DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		// when(instance.systemLoadDAO.find(1).getPlanId().getImplementationList()).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().getImplementationList());
		// when(instance.systemLoadDAO.find(1).getPlanId().getImplementationList().get(0)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().getImplementationList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("JSON RESULT --> { \"totalCount\" :\"-1\" , \"failedCount\" : \"1\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList().get(2));
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new Build());
		when(instance.developerManagerService.rejectPlan(stagingBuild.getUser(), DataWareHouse.getPlan().getId(), "Loader file creation error", true, Boolean.TRUE)).thenReturn(mock(JSONResponse.class));
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
		when(instance.protectedService.createLoaderFileForSystem(DataWareHouse.getUser(), DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), DataWareHouse.getPlan().getSystemLoadList().get(0), Constants.BUILD_TYPE.STG_LOAD, "E", Boolean.FALSE)).thenReturn(jSONResponse);
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
	    STAGEBuildMonitor instance = new STAGEBuildMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "protectedService", mock(ProtectedService.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develBuildJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingBuild = mock(JenkinsBuild.class);
	    when(stagingBuild.getUser()).thenReturn(DataWareHouse.getUser());
	    develBuildJob.add(stagingBuild);
	    instance.stagingBuildJobs = develBuildJob;
	    JSONResponse jSONResponse = mock(JSONResponse.class);
	    jSONResponse.setStatus(Boolean.FALSE);
	    // when(jSONResponse.getStatus()).thenReturn(Boolean.FALSE);
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.SUCCESS);
		when(stagingBuild.getSystemLoadId()).thenReturn("1");
		DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		// when(instance.systemLoadDAO.find(1).getPlanId().getImplementationList()).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().getImplementationList());
		// when(instance.systemLoadDAO.find(1).getPlanId().getImplementationList().get(0)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().getImplementationList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("TEST RESULT --> { \"totalCount\" :\"-1\" , \"failedCount\" : \"1\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList().get(2));
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new Build());
		JSONResponse lResult = new JSONResponse();
		lResult.setStatus(Boolean.TRUE);
		when(instance.developerManagerService.rejectPlan(stagingBuild.getUser(), DataWareHouse.getPlan().getId(), "Loader file creation error", true, Boolean.TRUE)).thenReturn(lResult);
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
		when(instance.protectedService.createLoaderFileForSystem(DataWareHouse.getUser(), DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), DataWareHouse.getPlan().getSystemLoadList().get(0), Constants.BUILD_TYPE.STG_LOAD, "E", Boolean.FALSE)).thenReturn(lResult);
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
	    STAGEBuildMonitor instance = new STAGEBuildMonitor();
	    ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
	    ReflectionTestUtils.setField(instance, "protectedService", mock(ProtectedService.class));
	    ReflectionTestUtils.setField(instance, "developerManagerService", mock(DeveloperManagerService.class));
	    ReflectionTestUtils.setField(instance, "buildDAO", mock(BuildDAO.class));
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));
	    ReflectionTestUtils.setField(instance, "lJenkinsClient", mock(JenkinsClient.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	    ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	    ConcurrentLinkedQueue<JenkinsBuild> develBuildJob = new ConcurrentLinkedQueue<>();
	    JenkinsBuild stagingBuild = mock(JenkinsBuild.class);
	    when(stagingBuild.getUser()).thenReturn(DataWareHouse.getUser());
	    develBuildJob.add(stagingBuild);
	    instance.stagingBuildJobs = develBuildJob;
	    JSONResponse jSONResponse = mock(JSONResponse.class);
	    jSONResponse.setStatus(Boolean.FALSE);
	    // when(jSONResponse.getStatus()).thenReturn(Boolean.FALSE);
	    try {
		when(instance.lJenkinsClient.getJobResult(null, 0)).thenReturn(BuildResult.FAILURE);
		when(stagingBuild.getSystemLoadId()).thenReturn("1");
		DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().setImplementationList(DataWareHouse.getPlan().getImplementationList());
		when(instance.systemLoadDAO.find(1)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0));
		// when(instance.systemLoadDAO.find(1).getPlanId().getImplementationList()).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().getImplementationList());
		// when(instance.systemLoadDAO.find(1).getPlanId().getImplementationList().get(0)).thenReturn(DataWareHouse.getPlan().getSystemLoadList().get(0).getPlanId().getImplementationList().get(0));
		when(instance.lJenkinsClient.getConsoleLog(null, 0)).thenReturn("TEST RESULT --> { \"totalCount\" :\"-1\" , \"failedCount\" : \"1\", \"failedFiles\" : [] , \"startTime\" : \"20180223020221\" , \"endTime\" : \"20180223020230\" , \"errorMessage\" : \"\", \"jobStatus\" : \"TRUE\" }");
		when(instance.wFConfig.getSTGBuildLogDir()).thenReturn("abc.txt");
		when(instance.buildDAO.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId(), Constants.BUILD_TYPE.DVL_LOAD)).thenReturn(DataWareHouse.getPlan().getBuildList().get(2));
		when(instance.buildDAO.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId(), 0, Constants.BUILD_TYPE.STG_BUILD)).thenReturn(new Build());
		JSONResponse lResult = new JSONResponse();
		lResult.setStatus(Boolean.TRUE);
		when(instance.developerManagerService.rejectPlan(stagingBuild.getUser(), DataWareHouse.getPlan().getId(), "No Error Message from Jenkins", true, Boolean.TRUE)).thenReturn(lResult);
		when(instance.mailMessageFactory.getTemplate(StagingBuildFailureMail.class)).thenReturn(mock(StagingBuildFailureMail.class));
		when(instance.protectedService.createLoaderFileForSystem(DataWareHouse.getUser(), DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0), DataWareHouse.getPlan().getSystemLoadList().get(0), Constants.BUILD_TYPE.STG_LOAD, "E", Boolean.FALSE)).thenReturn(lResult);
	    } catch (IOException ex) {
		// do nothing
	    }
	    instance.doMonitor();
	} catch (Exception e) {
	    // do nothing
	}
    }

}
