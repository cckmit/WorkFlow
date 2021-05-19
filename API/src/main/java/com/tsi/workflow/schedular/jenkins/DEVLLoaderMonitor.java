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
import com.tsi.workflow.activity.DevlLoadActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.LoadSetUtils;
import com.tsi.workflow.utils.WFLOGGER;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.management.timer.Timer;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpResponseException;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
@Component
public class DEVLLoaderMonitor {

    private static final Logger LOG = Logger.getLogger(DEVLLoaderMonitor.class.getName());

    @Autowired
    @Qualifier("develLoaderJob")
    ConcurrentLinkedQueue<JenkinsBuild> develLoaderJob;
    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    AuditCommonHelper auditCommonHelper;
    @Autowired
    PlanHelper planHelper;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (JenkinsBuild jenkinsBuild : develLoaderJob) {
	    try {
		BuildResult jobResult = null;
		try {
		    if (jenkinsBuild.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(jenkinsBuild.getQueueUrl());
			LOG.info(jenkinsBuild.getPlanId() + " -- " + jenkinsBuild.getSystemLoadId() + " : QueueURL is " + jenkinsBuild.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + jenkinsBuild.getBuildNumber());

			if (actualBuildNumber != null) {
			    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(jenkinsBuild.getSystemLoadId()));
			    Build buildData = buildDAO.findByBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId(), jenkinsBuild.getBuildNumber(), Constants.BUILD_TYPE.DVL_LOAD);
			    if (buildData != null) {
				buildData.setBuildNumber(actualBuildNumber.intValue());
				jenkinsBuild.setBuildNumber(actualBuildNumber.intValue());
				buildDAO.update(jenkinsBuild.getUser(), buildData);
			    }
			}
		    }
		    if (jenkinsBuild.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(jenkinsBuild.getJobName(), jenkinsBuild.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + jenkinsBuild.getJobName() + " Build " + jenkinsBuild.getBuildNumber());
		}
		if (jobResult == null) {
		    continue;
		} else if (jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN) {
		} else {
		    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(jenkinsBuild.getSystemLoadId()));
		    ImpPlan lPlan = lSystemLoad.getPlanId();
		    com.tsi.workflow.beans.dao.System lSystem = lSystemLoad.getSystemId();

		    String consoleLog = lJenkinsClient.getConsoleLog(jenkinsBuild.getJobName(), jenkinsBuild.getBuildNumber());
		    File lLogFile = new File(wFConfig.getDVLBuildLogDir(), lPlan.getId() + "_" + lSystem.getName() + "_LDR_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
		    FileUtils.writeStringToFile(lLogFile, consoleLog, false);

		    JobDetails lWssMessage = new JobDetails();
		    cacheClient.getSocketUserMap().put(lSystemLoad.getPlanId().getId(), jenkinsBuild.getUser().getId());
		    lWssMessage.setPlanId(lSystemLoad.getPlanId().getId());
		    lWssMessage.setSystemName(lSystemLoad.getSystemId().getName());
		    lWssMessage.setStatus("F");
		    DevlLoadActivityMessage lMessage = new DevlLoadActivityMessage(lPlan, null, lSystemLoad.getSystemId());
		    Build buildData = buildDAO.findByBuild(lPlan.getId(), lSystem, jenkinsBuild.getBuildNumber(), Constants.BUILD_TYPE.DVL_LOAD);
		    if (buildData == null) {
			develLoaderJob.remove(jenkinsBuild);
			auditCommonHelper.saveApiTransaction(jenkinsBuild.getUser(), jenkinsBuild.getBuildType(), jenkinsBuild.getStartedDate(), jenkinsBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(jenkinsBuild.getBuildType()));
			lWssMessage.setStatus("F");
			wsserver.sendMessage(Constants.Channels.DEVEL_LOAD, jenkinsBuild.getUser().getId(), lSystemLoad.getPlanId().getId(), lWssMessage);
			if (lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + Constants.BUILD_TYPE.DVL_LOAD.name())) {
			    List<JenkinsBuild> keyList = develLoaderJob.stream().filter(t -> t.getPlanId().equals(lPlan.getId())).collect(Collectors.toList());
			    if (!keyList.stream().filter(t -> !t.equals(jenkinsBuild)).findAny().isPresent()) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/DevlLoader" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lPlan.getId() + "-" + Constants.BUILD_TYPE.DVL_LOAD.name())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lPlan.getId() + "-" + Constants.BUILD_TYPE.DVL_LOAD.name());
			    }
			}
		    } else {
			if (jobResult.equals(BuildResult.SUCCESS)) {
			    buildData.setJobStatus("S");
			    lWssMessage.setStatus("S");
			    lMessage.setStatus("successfully completed ");
			    activityLogDAO.save(jenkinsBuild.getUser(), lMessage);

			    lSystemLoad.setLoadSetName(LoadSetUtils.getLoadSetName(lPlan, lSystemLoad));
			    lSystemLoad.setFallbackLoadSetName(LoadSetUtils.getFallbackLoadSetName(lPlan, lSystemLoad));
			    systemLoadDAO.update(jenkinsBuild.getUser(), lSystemLoad);
			} else {
			    // String consoleLog = lJenkinsClient.getConsoleLog(jenkinsBuild.getJobName(),
			    // jenkinsBuild.getBuildNumber());
			    Matcher matcher = JENKINS_ERROR_LOG_PATTERN.matcher(consoleLog);
			    String lLogResult = "";
			    if (matcher.find()) {
				lLogResult = matcher.group(1);
			    }
			    LOG.info("Build Log: " + lLogResult);
			    if (!lLogResult.isEmpty()) {
				JenkinsLog lResult = new Gson().fromJson(lLogResult, JenkinsLog.class);
				if ((lResult.getErrorMessage() != null) && (!lResult.getErrorMessage().isEmpty())) {
				    lMessage.setErrorMessage(lResult.getErrorMessage());
				}
			    }
			    buildData.setJobStatus("F");
			    lMessage.setStatus("failed");
			    activityLogDAO.save(jenkinsBuild.getUser(), lMessage);
			}
			buildDAO.update(jenkinsBuild.getUser(), buildData);
			wsserver.sendMessage(Constants.Channels.DEVEL_LOAD, jenkinsBuild.getUser().getId(), lSystemLoad.getPlanId().getId(), lWssMessage);
			auditCommonHelper.saveApiTransaction(jenkinsBuild.getUser(), jenkinsBuild.getBuildType(), jenkinsBuild.getStartedDate(), jenkinsBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(jenkinsBuild.getBuildType()));
			LOG.info("Oldr file creation completed for plan -" + lPlan.getId() + "System - " + lSystem.getName());
			if (lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + Constants.BUILD_TYPE.DVL_LOAD.name())) {
			    List<JenkinsBuild> keyList = develLoaderJob.stream().filter(t -> t.getPlanId().equals(lPlan.getId())).collect(Collectors.toList());
			    if (!keyList.stream().filter(t -> !t.equals(jenkinsBuild)).findAny().isPresent()) {
				WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/DevlLoader" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lPlan.getId() + "-" + Constants.BUILD_TYPE.DVL_LOAD.name())) + "ms, )");
				lAsyncPlansStartTimeMap.remove(lPlan.getId() + "-" + Constants.BUILD_TYPE.DVL_LOAD.name());
			    }
			}
			develLoaderJob.remove(jenkinsBuild);
			planHelper.clearPlanActionFromCache(jenkinsBuild.getPlanId(), Constants.PlanActions.DEVL_LOADSET_CREATION.name());

		    }
		}
	    } catch (IOException ex) {
		LOG.error("Error in Loader Job monitor process", ex);
		develLoaderJob.remove(jenkinsBuild);
		auditCommonHelper.saveApiTransaction(jenkinsBuild.getUser(), jenkinsBuild.getBuildType(), jenkinsBuild.getStartedDate(), jenkinsBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(jenkinsBuild.getBuildType()));
	    } finally {
		List<String> builds = buildDAO.getBuildInProgressPlan(Arrays.asList(jenkinsBuild.getPlanId()), Arrays.asList(Constants.BUILD_TYPE.DVL_LOAD.name()));
		if (builds == null && builds.isEmpty()) {
		    planHelper.clearPlanActionFromCache(jenkinsBuild.getPlanId(), Constants.PlanActions.DEVL_LOADSET_CREATION.name());
		}
	    }
	}
    }
}
