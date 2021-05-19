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
import com.tsi.workflow.activity.CommonActivityMessage;
import com.tsi.workflow.activity.RejectionActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.DependentPlanRejectDetail;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import com.tsi.workflow.mail.StagingBuildFailureMail;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.service.ProtectedService;
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
public class STAGEBuildMonitor {

    private static final Logger LOG = Logger.getLogger(STAGEBuildMonitor.class.getName());

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
    JenkinsClient lJenkinsClient;
    @Autowired
    @Qualifier("stagingBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingBuildJobs;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    ProtectedService protectedService;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    DeveloperManagerService developerManagerService;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (JenkinsBuild stagingBuild : stagingBuildJobs) {
	    ImpPlan lPlan = null;
	    Boolean isException = false;
	    try {
		BuildResult jobResult = null;
		try {
		    if (stagingBuild.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(stagingBuild.getQueueUrl());
			LOG.info(stagingBuild.getPlanId() + " -- " + stagingBuild.getSystemLoadId() + " : QueueURL is " + stagingBuild.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + stagingBuild.getBuildNumber());
			if (actualBuildNumber != null) {
			    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(stagingBuild.getSystemLoadId()));
			    Build buildData = buildDAO.findByBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId(), stagingBuild.getBuildNumber(), Constants.BUILD_TYPE.STG_BUILD);
			    if (buildData != null) {
				buildData.setBuildNumber(actualBuildNumber.intValue());
				stagingBuild.setBuildNumber(actualBuildNumber.intValue());
				if (actualBuildNumber.intValue() > 0) {
				    buildData.setBuildDateTime(new Date());
				}
				buildDAO.update(stagingBuild.getUser(), buildData);
			    }
			}
		    }
		    if (stagingBuild.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(stagingBuild.getJobName(), stagingBuild.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + stagingBuild.getJobName() + " Build " + stagingBuild.getBuildNumber());
		}
		if ((jobResult != null) && !(jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN)) {
		    JobDetails lWssMessage = new JobDetails();
		    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(stagingBuild.getSystemLoadId()));
		    lPlan = lSystemLoad.getPlanId();
		    cacheClient.getSocketUserMap().put(lPlan.getId(), stagingBuild.getUser().getId());
		    com.tsi.workflow.beans.dao.System lSystem = lSystemLoad.getSystemId();
		    lWssMessage.setPlanId(lPlan.getId());
		    lWssMessage.setSystemName(lSystem.getName());

		    String consoleLog = lJenkinsClient.getConsoleLog(stagingBuild.getJobName(), stagingBuild.getBuildNumber());
		    File lLogFile = new File(wFConfig.getSTGBuildLogDir(), lPlan.getId() + "_" + lSystem.getName() + "_BLD_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
		    FileUtils.writeStringToFile(lLogFile, consoleLog, false);
		    LOG.warn("Check the log file " + lLogFile.getAbsolutePath());

		    Matcher matcher = JENKINS_ERROR_LOG_PATTERN.matcher(consoleLog);
		    String lLogResult = "";
		    if (matcher.find()) {
			lLogResult = matcher.group(1);
		    }

		    if (jobResult.equals(BuildResult.SUCCESS)) {
			LOG.info("Build Log: " + lLogResult);
			Build buildData = buildDAO.findByBuild(lPlan.getId(), lSystem, stagingBuild.getBuildNumber(), Constants.BUILD_TYPE.STG_BUILD);
			if (buildData == null) {
			    stagingBuildJobs.remove(stagingBuild);
			    auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));

			    lWssMessage.setStatus("F");
			    wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			} else {
			    buildData.setJobStatus("S");
			    if (!lLogResult.isEmpty()) {
				JenkinsLog lResult = new Gson().fromJson(lLogResult, JenkinsLog.class);
				LOG.info("Total Files Count - " + lResult.getTotalCount() + ", Failed Files Count - " + lResult.getFailedFiles());
				long dbSegmentCount = checkoutSegmentsDAO.uniqueSegmentCount(lPlan.getId(), lSystem.getName());
				if (lResult.getTotalCount() < dbSegmentCount) {
				    buildData.setBuildStatus("PARTIAL");
				} else {
				    buildData.setBuildStatus("FULL");
				    lWssMessage.setStatus("S");
				    lWssMessage.setPlanId(lSystemLoad.getPlanId().getId());
				    lWssMessage.setSystemName(lSystemLoad.getSystemId().getName());
				    wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				}
			    } else {
				buildData.setJobStatus("F");
				lWssMessage.setStatus("F");
				wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				stagingBuildJobs.remove(stagingBuild);
				auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));
			    }
			    buildData.setTdxRunningStatus(Constants.TDXRunningStatus.COMPLETED.getTDXRunningStatus());
			    buildDAO.update(stagingBuild.getUser(), buildData);

			    LOG.info("Staging Build Completed for Plan - " + lSystemLoad.getPlanId().getId() + " " + lSystemLoad.getSystemId().getName());

			    Build lBuild = buildDAO.findLastSuccessfulBuild(lPlan.getId(), lSystem.getId(), Constants.BUILD_TYPE.DVL_LOAD);
			    if (lBuild == null) {
				LOG.error("No DEVL LOAD Found, Rejecting plan " + lSystemLoad.getPlanId().getId());
				buildFailureProcess(stagingBuild, lPlan, lSystemLoad, "No DEVL loadset found", "Devl Loadset not created");
				lWssMessage.setStatus("F");
				wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				stagingBuildJobs.remove(stagingBuild);
				auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));
				continue;
			    }

			    if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(lPlan.getId())) {
				DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(lPlan.getId());
				RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(lPlan, null);
				if (rejectDetail.getAutoRejectReason() != null) {
				    rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
				}
				planHelper.revertSubmittedPlanToActive(rejectDetail.getUser(), lPlan.getId());
				activityLogDAO.save(rejectDetail.getUser(), rejectActivityMessage);
			    } else {
				JSONResponse lJobResponse = protectedService.createLoaderFileForSystem(stagingBuild.getUser(), lSystemLoad.getPlanId(), lSystemLoad.getPlanId().getImplementationList().get(0), lSystemLoad, Constants.BUILD_TYPE.STG_LOAD, lBuild.getLoadSetType(), stagingBuild.getByPassRegression());

				if (lJobResponse.getStatus().equals(Boolean.FALSE)) {
				    LOG.error("Error in creating staging Loader File, Rejecting plan " + lSystemLoad.getPlanId().getId());
				    buildFailureProcess(stagingBuild, lPlan, lSystemLoad, "Loader file creation error", "Staging Loadset Creation error");
				    lWssMessage.setStatus("F");
				    wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				    stagingBuildJobs.remove(stagingBuild);
				    auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));
				    continue;
				}
			    }
			}
		    } else {
			Build buildData = buildDAO.findByBuild(lPlan.getId(), lSystem, stagingBuild.getBuildNumber(), Constants.BUILD_TYPE.STG_BUILD);
			if (buildData == null) {
			    stagingBuildJobs.remove(stagingBuild);
			    auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));

			    lWssMessage.setStatus("F");
			    wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			} else {
			    buildData.setJobStatus("F");
			    buildData.setTdxRunningStatus(Constants.TDXRunningStatus.COMPLETED.getTDXRunningStatus());
			    buildDAO.update(stagingBuild.getUser(), buildData);
			    lWssMessage.setStatus("F");
			    wsserver.sendMessage(Constants.Channels.STAGE_BUILD, stagingBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			    if (!lLogResult.isEmpty()) {
				JenkinsLog lResult = new Gson().fromJson(lLogResult, JenkinsLog.class);
				buildFailureProcess(stagingBuild, lPlan, lSystemLoad, lResult.getErrorMessage(), "Staging Build error");
			    } else {
				buildFailureProcess(stagingBuild, lPlan, lSystemLoad, "No Error Message from Jenkins", "Staging Build error");
			    }
			}
		    }
		    stagingBuildJobs.remove(stagingBuild);
		    auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));

		}
	    } catch (Exception ex) {
		LOG.error("Error in Loader Job monitor process", ex);
		if (lPlan != null) {
		    isException = true;
		    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
		}
		stagingBuildJobs.remove(stagingBuild);
		auditCommonHelper.saveApiTransaction(stagingBuild.getUser(), stagingBuild.getBuildType(), stagingBuild.getStartedDate(), stagingBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(stagingBuild.getBuildType()));

	    }
	    if (isException) {
		try {
		    planHelper.revertSubmittedPlanToActive(stagingBuild.getUser(), lPlan.getId());
		} catch (Exception ex) {
		    LOG.error("Unable to revert the plan status - " + lPlan.getId(), ex);
		}
	    }
	}
    }

    private void buildFailureProcess(JenkinsBuild stagingBuild, ImpPlan lPlan, SystemLoad lSystemLoad, String pComments, String pReason) {
	JobDetails lwssBuildFail = new JobDetails();
	cacheClient.getSocketUserMap().put(lPlan.getId(), stagingBuild.getUser().getId());
	lwssBuildFail.setStatus(lPlan.getId() + ": Auto Reject has started");
	wsserver.sendMessage(Constants.Channels.AUTO_REJECT, stagingBuild.getUser().getId(), lPlan.getId(), lwssBuildFail);
	try {
	    StringBuilder sb; // 2311
	    sb = new StringBuilder("STG build failed for ");
	    sb.append(lSystemLoad.getSystemId().getName());
	    if (!pComments.contains("No Error Message")) {
		sb.append(" due to the following errors: ");
	    }
	    sb.append(pComments);

	    CommonActivityMessage lMessage = new CommonActivityMessage(lPlan, null);
	    lMessage.setMessage(sb.toString());
	    lMessage.setStatus("Fail");
	    activityLogDAO.save(stagingBuild.getUser(), lMessage);

	    // 2311 StageBuildActivityMessage lMessage = new
	    // StageBuildActivityMessage(lPlan, null, lSystemLoad.getSystemId());
	    // lMessage.setStatus("failed");
	    // lMessage.setErrorMessage(pReason);
	    // activityLogDAO.save(stagingBuild.getUser(), lMessage);
	    planHelper.revertSubmittedPlanToActive(stagingBuild.getUser(), lPlan.getId());
	    // Notify ADL
	    StagingBuildFailureMail lRejectMail = (StagingBuildFailureMail) mailMessageFactory.getTemplate(StagingBuildFailureMail.class);
	    lRejectMail.setLeadId(lPlan.getLeadId());
	    lRejectMail.setPlanId(lPlan.getId());
	    lRejectMail.setSystem(lSystemLoad.getSystemId().getName());
	    mailMessageFactory.push(lRejectMail);
	    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
	} catch (Exception Ex) {
	    LOG.error("Error in Build Failure process " + Ex);
	    throw new WorkflowException("Error occurs in Build Failure Process");
	}
    }

}
