/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular.jenkins;

import static com.tsi.workflow.utils.Constants.JENKINS_ERROR_LOG_PATTERN;

import com.google.gson.Gson;
import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.StageBuildActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.ImpPlanAndUserDetail;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import com.tsi.workflow.mail.StagingBuildFailureMail;
import com.tsi.workflow.schedular.PlanSubmissionGitOperation;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.io.File;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import javax.management.timer.Timer;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class STAGELoaderMonitor {

    private static final Logger LOG = Logger.getLogger(STAGELoaderMonitor.class.getName());

    // Should be local one, on Cache implementation
    // we need to Set Keys in Cache and
    // need to change the isSubmitInprogress API from Cache
    /*
     * @Autowired
     * 
     * @Qualifier("lPlanUpdateStatusMap") ConcurrentHashMap<String, User>
     * lPlanUpdateStatusMap;
     */
    @Autowired
    @Qualifier("stagingLoaderJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingLoaderJobs;
    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    DeveloperManagerService developerManagerService;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    @Qualifier("dslFileGenerationList")
    ConcurrentLinkedQueue<ImpPlanAndUserDetail> dslFileGenerationList;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    AuditCommonHelper auditCommonHelper;
    @Autowired
    PlanSubmissionGitOperation planGitOperation;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (JenkinsBuild StagingLoaderJob : stagingLoaderJobs) {
	    ImpPlan lPlan = null;
	    Boolean isException = false;
	    try {
		BuildResult jobResult = null;
		try {
		    if (StagingLoaderJob.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(StagingLoaderJob.getQueueUrl());
			LOG.info(StagingLoaderJob.getPlanId() + " -- " + StagingLoaderJob.getSystemLoadId() + " : QueueURL is " + StagingLoaderJob.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + StagingLoaderJob.getBuildNumber());
			if (actualBuildNumber != null) {
			    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(StagingLoaderJob.getSystemLoadId()));
			    Build buildData = buildDAO.findByBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId(), StagingLoaderJob.getBuildNumber(), Constants.BUILD_TYPE.STG_LOAD);
			    buildData.setBuildNumber(actualBuildNumber.intValue());
			    StagingLoaderJob.setBuildNumber(actualBuildNumber.intValue());
			    buildDAO.update(StagingLoaderJob.getUser(), buildData);
			}
		    }
		    if (StagingLoaderJob.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(StagingLoaderJob.getJobName(), StagingLoaderJob.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + StagingLoaderJob.getJobName() + " Build " + StagingLoaderJob.getBuildNumber());
		}
		if (jobResult == null) {
		    continue;
		} else if (jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN) {
		} else {
		    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(StagingLoaderJob.getSystemLoadId()));
		    lPlan = lSystemLoad.getPlanId();
		    System lSystem = lSystemLoad.getSystemId();

		    String consoleLog = lJenkinsClient.getConsoleLog(StagingLoaderJob.getJobName(), StagingLoaderJob.getBuildNumber());
		    File lLogFile = new File(wFConfig.getSTGBuildLogDir(), lPlan.getId() + "_" + lSystem.getName() + "_LDR_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
		    FileUtils.writeStringToFile(lLogFile, consoleLog, false);

		    JobDetails lWssMessage = new JobDetails();
		    cacheClient.getSocketUserMap().put(lPlan.getId(), StagingLoaderJob.getUser().getId());
		    lWssMessage.setPlanId(lSystemLoad.getPlanId().getId());
		    lWssMessage.setSystemName(lSystemLoad.getSystemId().getName());
		    lWssMessage.setStatus("F");

		    Build buildData = buildDAO.findByBuild(lPlan.getId(), lSystem, StagingLoaderJob.getBuildNumber(), Constants.BUILD_TYPE.STG_LOAD);
		    if (buildData != null) {
			if (jobResult.equals(BuildResult.SUCCESS)) {
			    // ZTPFM-2704 Move Derived Artifacts into Git Server
			    if (!lPlan.getMacroHeader() && !lPlan.getLoadType().equalsIgnoreCase(Constants.LoadTypes.STANDARD.name())) {
				planGitOperation.movePlanDerivedArtifactsToGit(lSystemLoad);
			    }

			    StageBuildActivityMessage lMessage = new StageBuildActivityMessage(lPlan, null, lSystemLoad.getSystemId());
			    lMessage.setStatus("success");
			    activityLogDAO.save(StagingLoaderJob.getUser(), lMessage);
			    buildData.setJobStatus("S");
			    lWssMessage.setStatus("S");
			    JSONResponse lResult = developerManagerService.approveProcessinBPMForPlan(StagingLoaderJob.getUser(), lPlan.getId(), StagingLoaderJob.getByPassRegression());
			    if (lResult.getStatus()) {
				LOG.info("Plan Approved Successfully After Staging Load Creation");
				// DSL file generation and Auto load set generations for WSP system
				// TODO: JenkinsBuild obj has to be optimized
				if (!lPlan.getMacroHeader()) {
				    ImpPlanAndUserDetail planAndUserDetail = new ImpPlanAndUserDetail();
				    planAndUserDetail.setUser(StagingLoaderJob.getUser());
				    planAndUserDetail.setImpPlan(lPlan);
				    dslFileGenerationList.add(planAndUserDetail);
				}
			    }

			} else {
			    Matcher matcher = JENKINS_ERROR_LOG_PATTERN.matcher(consoleLog);
			    String lLogResult = "";
			    if (matcher.find()) {
				lLogResult = matcher.group(1);
			    }
			    buildData.setJobStatus("F");
			    StageBuildActivityMessage lMessage = new StageBuildActivityMessage(lPlan, null, lSystemLoad.getSystemId());
			    lMessage.setStatus("failed");
			    if (!lLogResult.isEmpty()) {
				JenkinsLog lResult = new Gson().fromJson(lLogResult, JenkinsLog.class);
				if ((lResult.getErrorMessage() != null) && (!lResult.getErrorMessage().isEmpty())) {
				    lMessage.setErrorMessage(lResult.getErrorMessage());
				}
			    }
			    activityLogDAO.save(StagingLoaderJob.getUser(), lMessage);
			    planHelper.revertSubmittedPlanToActive(StagingLoaderJob.getUser(), lPlan.getId());
			    // Notify ADL
			    StagingBuildFailureMail lRejectMail = (StagingBuildFailureMail) mailMessageFactory.getTemplate(StagingBuildFailureMail.class);
			    lRejectMail.setLeadId(lPlan.getLeadId());
			    lRejectMail.setPlanId(lPlan.getId());
			    lRejectMail.setSystem(lSystemLoad.getSystemId().getName());
			    mailMessageFactory.push(lRejectMail);
			    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
			}
			buildDAO.update(StagingLoaderJob.getUser(), buildData);
		    }
		    wsserver.sendMessage(Constants.Channels.STAGE_LOAD, StagingLoaderJob.getUser().getId(), lPlan.getId(), lWssMessage);
		    stagingLoaderJobs.remove(StagingLoaderJob);
		    auditCommonHelper.saveApiTransaction(StagingLoaderJob.getUser(), StagingLoaderJob.getBuildType(), StagingLoaderJob.getStartedDate(), StagingLoaderJob.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(StagingLoaderJob.getBuildType()));
		    LOG.info("Oldr file creation completed for plan - " + lPlan.getId() + " System - " + lSystem.getName());
		}
	    } catch (Exception ex) {
		LOG.error("Error in Loader Job monitor process", ex);
		if (lPlan != null) {
		    isException = true;
		    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
		}
		stagingLoaderJobs.remove(StagingLoaderJob);
		auditCommonHelper.saveApiTransaction(StagingLoaderJob.getUser(), StagingLoaderJob.getBuildType(), StagingLoaderJob.getStartedDate(), StagingLoaderJob.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(StagingLoaderJob.getBuildType()));
	    }
	    if (isException) {
		try {
		    planHelper.revertSubmittedPlanToActive(StagingLoaderJob.getUser(), lPlan.getId());
		} catch (Exception ex) {
		    LOG.error("Error in Loader Job monitor process", ex);
		}
	    }
	}
    }
}
