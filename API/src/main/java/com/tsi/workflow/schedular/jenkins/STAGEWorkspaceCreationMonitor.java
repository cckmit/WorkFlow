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
import com.tsi.workflow.activity.RejectionActivityMessage;
import com.tsi.workflow.activity.StageWorkSpaceActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.DependentPlanRejectDetail;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.jenkins.model.JenkinsLog;
import com.tsi.workflow.jenkins.model.JenkinsNewSegmentInfo;
import com.tsi.workflow.mail.StagingConfigFileCreationFailureMail;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import javax.management.timer.Timer;
import org.apache.commons.beanutils.BeanUtils;
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
public class STAGEWorkspaceCreationMonitor {

    private static final Logger LOG = Logger.getLogger(STAGEWorkspaceCreationMonitor.class.getName());
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
    @Qualifier("stagingWorkspaceCreationJobs")
    ConcurrentLinkedQueue<JenkinsBuild> stagingWorkspaceCreationJobs;
    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    ProtectedService protectedService;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    DeveloperManagerService developerManagerService;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    AuditCommonHelper auditCommonHelper;

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (JenkinsBuild lBuild : stagingWorkspaceCreationJobs) {
	    ImpPlan lPlan = null;
	    Boolean lCWSJobStatus = Boolean.TRUE;
	    try {
		BuildResult jobResult = null;
		try {
		    if (lBuild.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(lBuild.getQueueUrl());
			LOG.info(lBuild.getPlanId() + " -- " + lBuild.getSystemLoadId() + " : QueueURL is " + lBuild.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + lBuild.getBuildNumber());

			if (actualBuildNumber != null) {
			    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(lBuild.getSystemLoadId()));
			    Build buildData = buildDAO.findByBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId(), lBuild.getBuildNumber(), Constants.BUILD_TYPE.STG_CWS);
			    if (buildData != null) {
				buildData.setBuildNumber(actualBuildNumber.intValue());
				lBuild.setBuildNumber(actualBuildNumber.intValue());
				buildDAO.update(lBuild.getUser(), buildData);
			    }
			}
		    }
		    if (lBuild.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(lBuild.getJobName(), lBuild.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + lBuild.getJobName() + " Build " + lBuild.getBuildNumber());
		}
		if (jobResult == null) {
		    continue;
		} else if (jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN) {

		} else {
		    JobDetails lWssMessage = new JobDetails();

		    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(lBuild.getSystemLoadId()));
		    lPlan = lSystemLoad.getPlanId();
		    cacheClient.getSocketUserMap().put(lPlan.getId(), lBuild.getUser().getId());
		    com.tsi.workflow.beans.dao.System lSystem = lSystemLoad.getSystemId();
		    lWssMessage.setPlanId(lPlan.getId());
		    lWssMessage.setSystemName(lSystem.getName());

		    File lFile = new File(wFConfig.getSTGBuildLogDir());
		    if (!lFile.exists()) {
			boolean mkdirs = lFile.mkdirs();
		    }
		    String consoleLog = lJenkinsClient.getConsoleLog(lBuild.getJobName(), lBuild.getBuildNumber());
		    File lLogFile = new File(wFConfig.getSTGBuildLogDir(), lPlan.getId() + "_" + lSystem.getName() + "_WRKSPC_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
		    FileUtils.writeStringToFile(lLogFile, consoleLog, false);
		    Matcher matcher = JENKINS_ERROR_LOG_PATTERN.matcher(consoleLog);
		    String lLogResult = "";
		    if (matcher.find()) {
			lLogResult = matcher.group(1);
		    }
		    JenkinsLog lResult = new JenkinsLog();
		    if (!lLogResult.isEmpty()) {
			lResult = new Gson().fromJson(lLogResult, JenkinsLog.class);
			List<CheckoutSegments> lSegmentList = checkoutSegmentsDAO.findPlanBySystem(lPlan.getId(), lSystem.getName().toUpperCase());
			List<JenkinsNewSegmentInfo> newSegmentInfos = lResult.getNewSegmentInfo();
			for (JenkinsNewSegmentInfo lNewSegmentInfo : newSegmentInfos) {
			    for (CheckoutSegments lSeg : lSegmentList) {
				if (lNewSegmentInfo.getFileName().equalsIgnoreCase(lSeg.getFileName())) {
				    BeanUtils.copyProperties(lSeg, lNewSegmentInfo);
				    lSeg.setRefStatus("newfile");
				    checkoutSegmentsDAO.update(lBuild.getUser(), lSeg);
				}
			    }
			}
		    }

		    Build buildData = buildDAO.findByBuild(lPlan.getId(), lSystem, lBuild.getBuildNumber(), Constants.BUILD_TYPE.STG_CWS);
		    if (buildData == null) {
			lWssMessage.setStatus("F");
			stagingWorkspaceCreationJobs.remove(lBuild);
			auditCommonHelper.saveApiTransaction(lBuild.getUser(), lBuild.getBuildType(), lBuild.getStartedDate(), lBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(lBuild.getBuildType()));
			wsserver.sendMessage(Constants.Channels.CREATE_WORKSPACE, lBuild.getUser().getId(), lPlan.getId(), lWssMessage);
		    } else {
			if (jobResult.equals(BuildResult.SUCCESS)) {
			    buildData.setJobStatus("S");
			    if (lPlan.getMacroHeader()) {
				lWssMessage.setStatus("S");
				wsserver.sendMessage(Constants.Channels.CREATE_WORKSPACE, lBuild.getUser().getId(), lPlan.getId(), lWssMessage);

				JSONResponse lResult1 = developerManagerService.approveProcessinBPMForPlan(lBuild.getUser(), lPlan.getId(), lBuild.getByPassRegression());
				if (lResult1.getStatus()) {
				    LOG.info("Plan Approved Successfully for Macro/Header/Include Type");
				}
			    } else {
				if (cacheClient.getInProgressRelatedPlanMap() != null && cacheClient.getInProgressRelatedPlanMap().containsKey(lPlan.getId())) {
				    DependentPlanRejectDetail rejectDetail = cacheClient.getInProgressRelatedPlanMap().get(lPlan.getId());
				    RejectionActivityMessage rejectActivityMessage = new RejectionActivityMessage(lPlan, null);
				    if (rejectDetail.getAutoRejectReason() != null) {
					rejectActivityMessage.setComments(rejectDetail.getAutoRejectReason());
				    }
				    getActivityLogDAO().save(rejectDetail.getUser(), rejectActivityMessage);
				    lWssMessage.setStatus("F");
				} else {
				    JSONResponse lJobResponse = protectedService.buildPlanForSystem(lBuild.getUser(), lSystemLoad.getPlanId(), lSystemLoad.getPlanId().getImplementationList().get(0), lSystemLoad, Constants.BUILD_TYPE.STG_BUILD, lBuild.getByPassRegression());
				    if (lJobResponse.getStatus().equals(Boolean.FALSE)) {
					lWssMessage.setStatus("F");
					wsserver.sendMessage(Constants.Channels.CREATE_WORKSPACE, lBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				    }
				}

			    }
			} else {
			    if (!lLogResult.isEmpty()) {
				StageWorkSpaceActivityMessage lMessage = new StageWorkSpaceActivityMessage(lPlan, null, lSystemLoad.getSystemId());
				lMessage.setStatus("failed");
				lMessage.setErrorMessage(lResult.getErrorMessage());
				activityLogDAO.save(lBuild.getUser(), lMessage);
			    }
			    // Notify ADL
			    StagingConfigFileCreationFailureMail failureMail = (StagingConfigFileCreationFailureMail) mailMessageFactory.getTemplate(StagingConfigFileCreationFailureMail.class);
			    failureMail.setLeadId(lPlan.getLeadId());
			    failureMail.setPlanId(lPlan.getId());
			    failureMail.setSystem(lSystemLoad.getSystemId().getName());
			    lWssMessage.setStatus("F");
			    wsserver.sendMessage(Constants.Channels.CREATE_WORKSPACE, lBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			    buildData.setJobStatus("F");
			}
			buildDAO.update(lBuild.getUser(), buildData);
			if (lWssMessage.getStatus() != null && lWssMessage.getStatus().equalsIgnoreCase("F")) {
			    planHelper.revertSubmittedPlanToActive(lBuild.getUser(), lPlan.getId());
			    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
			}
		    }
		    stagingWorkspaceCreationJobs.remove(lBuild);
		    LOG.info("Plan Id for response processing " + lBuild.getPlanId());
		    auditCommonHelper.saveApiTransaction(lBuild.getUser(), lBuild.getBuildType(), lBuild.getStartedDate(), lBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(lBuild.getBuildType()));
		}
	    } catch (Exception ex) {
		LOG.error("Error in Loader Job monitor process", ex);
		stagingWorkspaceCreationJobs.remove(lBuild);
		auditCommonHelper.saveApiTransaction(lBuild.getUser(), lBuild.getBuildType(), lBuild.getStartedDate(), lBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(lBuild.getBuildType()));
		if (lPlan != null) {
		    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
		}
	    }
	}
    }
}
