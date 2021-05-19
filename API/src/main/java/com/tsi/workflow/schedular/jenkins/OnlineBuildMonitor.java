package com.tsi.workflow.schedular.jenkins;

import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.ExceptionOccurOnlineActivityMessage;
import com.tsi.workflow.activity.MergeOnlineActivityMessage;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.bpm.model.TaskVariable;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.mail.StatusChangeToDependentPlanMail;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.WFLOGGER;
import com.tsi.workflow.websocket.WSMessagePublisher;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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
 * @author USER
 */
@Component
public class OnlineBuildMonitor {

    private static final Logger LOG = Logger.getLogger(OnlineBuildMonitor.class.getName());

    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired
    @Qualifier("onlineBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> onlineBuildJobs;
    @Autowired
    CheckoutSegmentsDAO checkoutSegmentsDAO;
    @Autowired
    OnlineBuildDAO onlineBuildDAO;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    SystemLoadDAO systemLoadDAO;
    @Autowired
    ActivityLogDAO activityLogDAO;
    @Autowired
    MailMessageFactory mailMessageFactory;
    @Autowired
    CacheClient cacheClient;
    @Autowired
    WSMessagePublisher wsserver;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    FallbackHelper fallbackHelper;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    BPMClientUtils bPMClientUtils;
    @Autowired
    PRNumberHelper prStatusUpdateinNAS;
    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;
    @Autowired
    AuditCommonHelper auditCommonHelper;

    public BPMClientUtils getBPMClientUtils() {
	return bPMClientUtils;
    }

    public FallbackHelper getFallbackHelper() {
	return fallbackHelper;
    }

    public PlanHelper getPlanHelper() {
	return planHelper;
    }

    public PRNumberHelper getPRStatusUpdateinNAS() {
	return prStatusUpdateinNAS;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (JenkinsBuild onlineBuild : onlineBuildJobs) {

	    ImpPlan lPlan = null;
	    try {
		BuildResult jobResult = null;
		try {
		    if (onlineBuild.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(onlineBuild.getQueueUrl());
			LOG.info(onlineBuild.getPlanId() + " -- " + onlineBuild.getSystemLoadId() + " : QueueURL is " + onlineBuild.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + onlineBuild.getBuildNumber());
			if (actualBuildNumber != null) {
			    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(onlineBuild.getSystemLoadId()));
			    OnlineBuild buildData = onlineBuildDAO.findByBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId(), onlineBuild.getBuildNumber(), Constants.BUILD_TYPE.ONL_BUILD);
			    if (buildData != null && buildData.getBuildType().equals(Constants.BUILD_TYPE.ONL_BUILD.name())) {
				buildData.setBuildNumber(actualBuildNumber.intValue());
				onlineBuild.setBuildNumber(actualBuildNumber.intValue());
				onlineBuildDAO.update(onlineBuild.getUser(), buildData);
			    }
			}
		    }
		    if (onlineBuild.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(onlineBuild.getJobName(), onlineBuild.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + onlineBuild.getJobName() + " Online Build " + onlineBuild.getBuildNumber());
		}
		if ((jobResult != null) && !(jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN)) {
		    LOG.info(" Online Build " + onlineBuild.getJobName() + "/ QueueURL is " + onlineBuild.getQueueUrl() + "/ Build Number is " + onlineBuild.getBuildNumber());
		    JobDetails lWssMessage = new JobDetails();
		    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(onlineBuild.getSystemLoadId()));
		    lPlan = lSystemLoad.getPlanId();
		    cacheClient.getSocketUserMap().put(lPlan.getId(), onlineBuild.getUser().getId());
		    com.tsi.workflow.beans.dao.System lSystem = lSystemLoad.getSystemId();
		    lWssMessage.setPlanId(lPlan.getId());
		    lWssMessage.setSystemName(lSystem.getName());

		    if (jobResult.equals(BuildResult.SUCCESS)) {
			OnlineBuild buildData = onlineBuildDAO.findByBuild(lPlan.getId(), lSystem, onlineBuild.getBuildNumber(), Constants.BUILD_TYPE.ONL_BUILD);
			// Plan status update online
			if (buildData != null && buildData.getBuildType().equals(Constants.BUILD_TYPE.ONL_BUILD.name())) {
			    // Writing online log file
			    String consoleLog = lJenkinsClient.getConsoleLog(onlineBuild.getJobName(), onlineBuild.getBuildNumber());
			    File lLogFile = new File(wFConfig.getONLINEBuildLogDir(), lPlan.getId() + "_" + lSystem.getName() + "_ONLINE_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
			    FileUtils.writeStringToFile(lLogFile, consoleLog, false);
			    LOG.warn("Check the log file " + lLogFile.getAbsolutePath());
			    String lOldStatus = lSystemLoad.getPlanId().getPlanStatus();

			    if (lSystemLoad.getPlanId().getMacroHeader()) {
				lSystemLoad.getPlanId().setApproveMailFlag(Boolean.FALSE);
			    }
			    impPlanDAO.update(onlineBuild.getUser(), lSystemLoad.getPlanId());

			    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lSystemLoad.getPlanId(), null);
			    planStatusActivityMessage.setStatus("Online");
			    planStatusActivityMessage.setlSystem(lSystemLoad.getSystemId().getName());
			    activityLogDAO.save(onlineBuild.getUser(), planStatusActivityMessage);

			    try {
				List<TaskVariable> lTaskVars = new ArrayList<>();
				lTaskVars.add(new TaskVariable("Plan Status", "local", "string", Constants.PROD_SCRIPT_PARAMS.ONLINE_ACCEPT.name()));
				lTaskVars.add(new TaskVariable(Constants.BPM_IMPLEMENTATION_PLAN_ID, lSystemLoad.getPlanId().getId()));
				getBPMClientUtils().setTaskAsCompletedWithVariables(onlineBuild.getUser(), lSystemLoad.getPlanId().getProcessId(), lTaskVars);
			    } catch (Exception ex) {
				LOG.error("BPM Exception occur " + ex);
			    }

			    getPRStatusUpdateinNAS().writeFileInNAS(lSystemLoad.getPlanId());
			    StatusChangeToDependentPlanMail statusChangeToDependentPlanMail = (StatusChangeToDependentPlanMail) mailMessageFactory.getTemplate(StatusChangeToDependentPlanMail.class);
			    statusChangeToDependentPlanMail.addToAddressUserId(lSystemLoad.getPlanId().getLeadId(), Constants.MailSenderRole.LEAD);
			    statusChangeToDependentPlanMail.setImpPlanId(lSystemLoad.getPlanId().getId());
			    statusChangeToDependentPlanMail.setOldStatus(lOldStatus);
			    statusChangeToDependentPlanMail.setActivityTime(lSystemLoad.getPlanId().getAcceptedDateTime());
			    statusChangeToDependentPlanMail.setNewStatus(Constants.PlanStatus.ONLINE.name());
			    mailMessageFactory.push(statusChangeToDependentPlanMail);

			}

			if (buildData == null) {
			    onlineBuildJobs.remove(onlineBuild);
			    auditCommonHelper.saveApiTransaction(onlineBuild.getUser(), onlineBuild.getBuildType(), onlineBuild.getStartedDate(), onlineBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(onlineBuild.getBuildType()));

			    lWssMessage.setStatus("F");
			    wsserver.sendMessage(Constants.Channels.ONLINE_BUILD, onlineBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			} else if (buildData != null) {
			    if (jobResult.equals(BuildResult.SUCCESS)) {
				buildData.setJobStatus("S");
				buildData.setBuildStatus("FULL");
				lWssMessage.setStatus(lPlan.getId() + ", System -  " + lSystemLoad.getSystemId().getName() + ": GIT Operation processed successfully");
				lWssMessage.setPlanId(lSystemLoad.getPlanId().getId());
				lWssMessage.setSystemName(lSystemLoad.getSystemId().getName());
				wsserver.sendMessage(Constants.Channels.ONLINE_BUILD, onlineBuild.getUser().getId(), lPlan.getId(), lWssMessage);

			    } else {
				buildData.setJobStatus("F");
				lWssMessage.setStatus(lPlan.getId() + ", System -  " + lSystemLoad.getSystemId().getName() + ": Error occured in GIT Operation process, Please contact Devops Support Team");
				wsserver.sendMessage(Constants.Channels.ONLINE_BUILD, onlineBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				onlineBuildJobs.remove(onlineBuild);
				auditCommonHelper.saveApiTransaction(onlineBuild.getUser(), onlineBuild.getBuildType(), onlineBuild.getStartedDate(), onlineBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(onlineBuild.getBuildType()));
			    }
			    onlineBuildDAO.update(onlineBuild.getUser(), buildData);
			    MergeOnlineActivityMessage lMergeOnlineActivityMessage = new MergeOnlineActivityMessage(lSystemLoad.getPlanId(), null);
			    lMergeOnlineActivityMessage.setSystem(lSystemLoad.getSystemId().getName());
			    lMergeOnlineActivityMessage.setJobInfo(buildData);
			    activityLogDAO.save(onlineBuild.getUser(), lMergeOnlineActivityMessage);

			}
			LOG.info("Online Build Completed for Plan - " + lSystemLoad.getPlanId().getId() + " " + lSystemLoad.getSystemId().getName());

		    } else {
			Map<String, Date> lSystemAndDate = getPlanHelper().getSytemandLoadDate(lSystemLoad.getPlanId());
			getFallbackHelper().exceptionOccurMail(lSystemLoad.getPlanId().getId(), lSystemAndDate);
			// Activity Log update if jenkins job fail
			ExceptionOccurOnlineActivityMessage exceptionOccurOnlineActivityMessage = new ExceptionOccurOnlineActivityMessage(lSystemLoad.getPlanId(), null);
			activityLogDAO.save(onlineBuild.getUser(), exceptionOccurOnlineActivityMessage);

			OnlineBuild buildData = onlineBuildDAO.findByBuild(lPlan.getId(), lSystem, onlineBuild.getBuildNumber(), Constants.BUILD_TYPE.ONL_BUILD);
			if (buildData == null) {
			    onlineBuildJobs.remove(onlineBuild);
			    auditCommonHelper.saveApiTransaction(onlineBuild.getUser(), onlineBuild.getBuildType(), onlineBuild.getStartedDate(), onlineBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(onlineBuild.getBuildType()));
			    lWssMessage.setStatus(lPlan.getId() + ", System -  " + lSystemLoad.getSystemId().getName() + ": Error occured in GIT Operation process, Please contact Devops Support Team");
			    wsserver.sendMessage(Constants.Channels.ONLINE_BUILD, onlineBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			} else {
			    buildData.setJobStatus("F");
			    onlineBuildDAO.update(onlineBuild.getUser(), buildData);
			    lWssMessage.setStatus(lPlan.getId() + ", System -  " + lSystemLoad.getSystemId().getName() + ": Error occured in GIT Operation process, Please contact Devops Support Team");
			    wsserver.sendMessage(Constants.Channels.ONLINE_BUILD, onlineBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			}
		    }

		    Long lOnllineBuildCnt = onlineBuildDAO.findAllSuccessBuildForPlan(lPlan, Constants.BUILD_TYPE.ONL_BUILD);
		    Long lSysCnt = systemLoadDAO.countByImpPlan(lPlan);
		    LOG.info("Online Job Count - " + lOnllineBuildCnt);
		    if (Objects.equals(lOnllineBuildCnt, lSysCnt)) {
			LOG.info(lSystemLoad.getPlanId().getId() + " " + lSystemLoad.getSystemId().getName() + " Accept Completed");
			lPlan.setAcceptedDateTime(new Date());
			lPlan.setPlanStatus(Constants.PlanStatus.ONLINE.name());
			lPlan.setInprogressStatus(Constants.PlanInProgressStatus.NONE.name());
			impPlanDAO.update(onlineBuild.getUser(), lPlan);
			cacheClient.getOnlineAcceptPlanMap().remove(lPlan.getId());
		    }

		    onlineBuildJobs.remove(onlineBuild);
		    auditCommonHelper.saveApiTransaction(onlineBuild.getUser(), onlineBuild.getBuildType(), onlineBuild.getStartedDate(), onlineBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(onlineBuild.getBuildType()));

		    if (lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + Constants.BUILD_TYPE.ONL_BUILD.name())) {
			WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/markAuxAsOnline" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lPlan.getId() + "-" + Constants.BUILD_TYPE.ONL_BUILD.name())) + " ms, )");
			lAsyncPlansStartTimeMap.remove(lPlan.getId() + "-" + Constants.BUILD_TYPE.ONL_BUILD.name());
		    }
		}
	    } catch (Exception ex) {
		LOG.error("Error in Loader Job monitor process", ex);
		onlineBuildJobs.remove(onlineBuild);
		auditCommonHelper.saveApiTransaction(onlineBuild.getUser(), onlineBuild.getBuildType(), onlineBuild.getStartedDate(), onlineBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(onlineBuild.getBuildType()));
	    }

	}

    }

}
