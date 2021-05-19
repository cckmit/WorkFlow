/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular.jenkins;

import static com.tsi.workflow.utils.Constants.JENKINS_ERROR_LOG_PATTERN;

import com.google.gson.Gson;
import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.DevlBuildActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.WFLOGGER;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.management.timer.Timer;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author USER
 */
@Component
public class DEVLBuildMonitor {

    private static final Logger LOG = Logger.getLogger(DEVLBuildMonitor.class.getName());

    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired // Constants.BUILD_TYPE.DEVL
    ConcurrentHashMap<String, JenkinsBuild> develBuildJob;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    SystemDAO systemDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (Map.Entry<String, JenkinsBuild> entrySet : develBuildJob.entrySet()) {
	    String key = entrySet.getKey();
	    boolean isJobExecutionFailed = false;
	    List<String> changedFiles = new ArrayList<>();
	    String planId = key.split("_")[0];
	    String systemName = key.split("_")[1];
	    JenkinsBuild build = entrySet.getValue();
	    try {
		JobDetails lWssMessage = new JobDetails();
		lWssMessage.setPlanId(planId);
		lWssMessage.setSystemName(systemName);
		cacheClient.getSocketUserMap().put(planId, build.getUser().getId());
		BuildResult jobResult = null;
		changedFiles = build.getChangedFiles();
		try {
		    if (build.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(build.getQueueUrl());
			LOG.info(key + " : QueueURL is " + build.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + build.getBuildNumber());
			if (actualBuildNumber != null) {
			    com.tsi.workflow.beans.dao.System lSystem = systemDAO.findByName(systemName);
			    Build buildData = buildDAO.findByBuild(planId, lSystem, build.getBuildNumber(), Constants.BUILD_TYPE.DVL_BUILD);
			    if (buildData != null) {
				buildData.setBuildNumber(actualBuildNumber.intValue());
				if (actualBuildNumber.intValue() > 0) {
				    buildData.setBuildDateTime(new Date());
				}
				build.setBuildNumber(actualBuildNumber.intValue());
				buildDAO.update(build.getUser(), buildData);
			    }
			}
		    }
		    if (build.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(build.getJobName(), build.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + build.getJobName() + " Build " + build.getBuildNumber());
		}
		if (jobResult == null) {
		    continue;
		}
		LOG.info("JOB Result : " + jobResult);
		if (jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN) {
		} else {
		    String consoleLog = lJenkinsClient.getConsoleLog(build.getJobName(), build.getBuildNumber());
		    File lFile = new File(wFConfig.getDVLBuildLogDir());
		    if (!lFile.exists()) {
			boolean mkdirs = lFile.mkdirs();
		    }
		    File lLogFile = new File(lFile, planId + "_" + systemName + "_BLD_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
		    FileUtils.writeStringToFile(lLogFile, consoleLog, false);
		    Matcher matcher = JENKINS_ERROR_LOG_PATTERN.matcher(consoleLog);
		    String lLogResult = "";
		    if (matcher.find()) {
			lLogResult = matcher.group(1);
		    }
		    LOG.info("Build Log: " + lLogResult);
		    com.tsi.workflow.beans.dao.System lSystem = systemDAO.findByName(systemName);
		    Build buildData = buildDAO.findByBuild(planId, lSystem, build.getBuildNumber(), Constants.BUILD_TYPE.DVL_BUILD);
		    if (buildData == null) {
			isJobExecutionFailed = true;
			develBuildJob.remove(key);
			auditCommonHelper.saveApiTransaction(build.getUser(), build.getBuildType(), build.getStartedDate(), build.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(build.getBuildType()));

			lWssMessage.setStatus("F");
			wsserver.sendMessage(Constants.Channels.DEVEL_BUILD, build.getUser().getId(), planId, lWssMessage);
			if (lAsyncPlansStartTimeMap.containsKey(planId + "-" + Constants.BUILD_TYPE.DVL_BUILD.name())) {
			    List<String> keyList = develBuildJob.keySet().stream().filter(k -> Arrays.asList(k.split("_")).get(0).equals(planId)).collect(Collectors.toList());
			    if (!keyList.stream().filter(k -> !k.equals(key)).findAny().isPresent()) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/DevlBuild" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(planId + "-" + Constants.BUILD_TYPE.DVL_BUILD.name())) + " ms, )");
				lAsyncPlansStartTimeMap.remove(planId + "-" + Constants.BUILD_TYPE.DVL_BUILD.name());
			    }
			}
		    } else {
			ImpPlan impPlan = impPlanDAO.find(planId);
			DevlBuildActivityMessage lMessage = new DevlBuildActivityMessage(impPlan, null, lSystem);
			if (!lLogResult.isEmpty()) {
			    JenkinsLog lResult = new Gson().fromJson(lLogResult, JenkinsLog.class);

			    if (lResult.getJobStatus()) {
				long dbSegmentCount = checkoutSegmentsDAO.uniqueSegmentCount(planId, systemName);
				LOG.info("Total Files Count - " + lResult.getTotalCount() + ", Failed Files Count - " + lResult.getFailedCount() + ", DB Files Count - " + dbSegmentCount);
				if (lResult.getTotalCount() < dbSegmentCount) {
				    buildData.setBuildStatus("PARTIAL");
				} else {
				    buildData.setBuildStatus("FULL");
				}
				if (jobResult == BuildResult.FAILURE) {
				    buildData.setJobStatus("F");
				    isJobExecutionFailed = true;
				    lMessage.setStatus("failed");
				    lMessage.setJenkinsLog(lResult);
				    activityLogDAO.save(build.getUser(), lMessage);
				    // lWssMessage.setPercentageCompleted(100);
				    lWssMessage.setStatus("F");
				} else if (jobResult == BuildResult.CANCELLED || jobResult == BuildResult.ABORTED) {
				    LOG.info("Job Status " + jobResult);
				    buildData.setJobStatus("C");
				    isJobExecutionFailed = true;
				    lMessage.setStatus("cancelled");
				    activityLogDAO.save(build.getUser(), lMessage);
				    lWssMessage.setStatus("C");
				} else {
				    buildData.setJobStatus("S");
				    lMessage.setJenkinsLog(lResult);
				    lMessage.setStatus("success");
				    activityLogDAO.save(build.getUser(), lMessage);
				    lWssMessage.setStatus("S");
				}
				// Update Build Start Time
				buildData.setBuildDateTime(Constants.JENKINS_DATEFORMAT.get().parse(lResult.getStartTime()));
			    } else {
				buildData.setJobStatus("F");
				isJobExecutionFailed = true;
				if (lResult.getFailedFileNameList() != null && !lResult.getFailedFileNameList().isEmpty()) {
				    changedFiles = lResult.getFailedFileNameList();
				}

				lMessage.setJenkinsLog(lResult);
				activityLogDAO.save(build.getUser(), lMessage);
				lWssMessage.setStatus("F");
			    }
			} else {
			    if (jobResult == BuildResult.CANCELLED || jobResult == BuildResult.ABORTED) {
				LOG.info("Job Status " + jobResult);
				buildData.setJobStatus("C");
				isJobExecutionFailed = true;
				lMessage.setStatus("cancelled");
				activityLogDAO.save(build.getUser(), lMessage);
				lWssMessage.setStatus("C");
			    } else {
				buildData.setJobStatus("F");
				lMessage.setJenkinsLog(new JenkinsLog());
				activityLogDAO.save(build.getUser(), lMessage);
				lWssMessage.setStatus("F");
				isJobExecutionFailed = true;
			    }
			}
			if (lAsyncPlansStartTimeMap.containsKey(planId + "-" + Constants.BUILD_TYPE.DVL_BUILD.name())) {
			    List<String> keyList = develBuildJob.keySet().stream().filter(k -> Arrays.asList(k.split("_")).get(0).equals(planId)).collect(Collectors.toList());
			    if (!keyList.stream().filter(k -> !k.equals(key)).findAny().isPresent()) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/DevlBuild" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(planId + "-" + Constants.BUILD_TYPE.DVL_BUILD.name())) + " ms, )");
				lAsyncPlansStartTimeMap.remove(planId + "-" + Constants.BUILD_TYPE.DVL_BUILD.name());
			    }
			}
			develBuildJob.remove(key);
			auditCommonHelper.saveApiTransaction(build.getUser(), build.getBuildType(), build.getStartedDate(), planId, null, Constants.BUILD_TYPE.getBuildTypeActionUrl(build.getBuildType()));
			wsserver.sendMessage(Constants.Channels.DEVEL_BUILD, build.getUser().getId(), planId, lWssMessage);
			lWssMessage.setMessage(planId + ": Devl build has completed for " + lSystem.getName());
			wsserver.sendMessage(Constants.Channels.DEVEL_BUILD_STATUS, build.getUser().getId(), planId, lWssMessage);
			buildData.setTdxRunningStatus(Constants.TDXRunningStatus.COMPLETED.getTDXRunningStatus());
			buildDAO.update(build.getUser(), buildData);

		    }
		}
	    } catch (Exception ex) {
		auditCommonHelper.saveApiTransaction(build.getUser(), build.getBuildType(), build.getStartedDate(), build.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(build.getBuildType()));
		develBuildJob.remove(key);
		isJobExecutionFailed = true;
		LOG.error("Error in JOB monitor process", ex);
	    } finally {
		if (isJobExecutionFailed) {
		    List<String> failedFileNames = new ArrayList<>();
		    if (changedFiles != null && !changedFiles.isEmpty()) {
			failedFileNames.addAll(changedFiles);
		    }
		    List<CheckoutSegments> checkoutSegs = checkoutSegmentsDAO.findSystemBasedSegs(planId, systemName, failedFileNames);
		    checkoutSegs.forEach(seg -> {
			seg.setLastChangedTime(new Date());
			checkoutSegmentsDAO.update(build.getUser(), seg);
		    });
		}
	    }
	}
    }
}
