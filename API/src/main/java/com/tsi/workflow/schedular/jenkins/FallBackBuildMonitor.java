package com.tsi.workflow.schedular.jenkins;

import com.offbytwo.jenkins.model.BuildResult;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.activity.ExceptionOccurOnlineActivityMessage;
import com.tsi.workflow.activity.PlanStatusActivityMessage;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.JobDetails;
import com.tsi.workflow.bpm.BPMClientUtils;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.OnlineBuildDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.git.JGitClientUtils;
import com.tsi.workflow.helper.FallbackHelper;
import com.tsi.workflow.helper.PRNumberHelper;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.jenkins.JenkinsClient;
import com.tsi.workflow.jenkins.model.JenkinsBuild;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.WFLOGGER;
import com.tsi.workflow.websocket.WSMessagePublisher;
import com.workflow.ssh.SSHClientUtils;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class FallBackBuildMonitor {

    private static final Logger LOG = Logger.getLogger(FallBackBuildMonitor.class.getName());

    @Autowired
    JenkinsClient lJenkinsClient;
    @Autowired
    @Qualifier("fallBackBuildJobs")
    ConcurrentLinkedQueue<JenkinsBuild> fallBackBuildJobs;
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
    SSHClientUtils sSHClientUtils;
    @Autowired
    JGitClientUtils lGitUtils;
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

    public JenkinsClient getlJenkinsClient() {
	return lJenkinsClient;
    }

    public JGitClientUtils getJGitClientUtils() {
	return lGitUtils;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (JenkinsBuild fallbackBuild : fallBackBuildJobs) {
	    ImpPlan lPlan = null;
	    try {
		BuildResult jobResult = null;
		try {
		    if (fallbackBuild.getBuildNumber() < 0) {
			Long actualBuildNumber = lJenkinsClient.getActualBuildNumber(fallbackBuild.getQueueUrl());
			LOG.info(fallbackBuild.getPlanId() + " -- " + fallbackBuild.getSystemLoadId() + " : QueueURL is " + fallbackBuild.getQueueUrl() + " Actual Build Number is " + (actualBuildNumber != null ? actualBuildNumber : " Null") + " previous Build Number - " + fallbackBuild.getBuildNumber());
			if (actualBuildNumber != null) {
			    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(fallbackBuild.getSystemLoadId()));
			    OnlineBuild fallBackBuildData = onlineBuildDAO.findByBuild(lSystemLoad.getPlanId().getId(), lSystemLoad.getSystemId(), fallbackBuild.getBuildNumber(), Constants.BUILD_TYPE.FAL_BUILD);
			    if (fallBackBuildData != null && fallBackBuildData.getBuildType().equals(Constants.BUILD_TYPE.FAL_BUILD.name())) {
				fallBackBuildData.setBuildNumber(actualBuildNumber.intValue());
				fallbackBuild.setBuildNumber(actualBuildNumber.intValue());
				onlineBuildDAO.update(fallbackBuild.getUser(), fallBackBuildData);
			    }
			}
		    }
		    if (fallbackBuild.getBuildNumber() > 0) {
			jobResult = lJenkinsClient.getJobResult(fallbackBuild.getJobName(), fallbackBuild.getBuildNumber());
		    }
		} catch (HttpResponseException e) {
		    LOG.error("Error in Jeknins Communication, Retrying after sometime for the job " + fallbackBuild.getJobName() + " Fallback Build " + fallbackBuild.getBuildNumber());
		}
		if ((jobResult != null) && !(jobResult == BuildResult.BUILDING || jobResult == BuildResult.NOT_BUILT || jobResult == BuildResult.REBUILDING || jobResult == BuildResult.UNKNOWN)) {
		    JobDetails lWssMessage = new JobDetails();
		    SystemLoad lSystemLoad = systemLoadDAO.find(Integer.parseInt(fallbackBuild.getSystemLoadId()));
		    lPlan = lSystemLoad.getPlanId();
		    cacheClient.getSocketUserMap().put(lPlan.getId(), fallbackBuild.getUser().getId());
		    com.tsi.workflow.beans.dao.System lSystem = lSystemLoad.getSystemId();
		    lWssMessage.setPlanId(lPlan.getId());
		    lWssMessage.setSystemName(lSystem.getName());
		    if (jobResult.equals(BuildResult.SUCCESS)) {
			OnlineBuild fallBackBuildData = onlineBuildDAO.findByBuild(lPlan.getId(), lSystem, fallbackBuild.getBuildNumber(), Constants.BUILD_TYPE.FAL_BUILD);
			// Plan status update Fallback
			if (fallBackBuildData != null && fallBackBuildData.getBuildType().equals(Constants.BUILD_TYPE.FAL_BUILD.name())) {
			    String consoleLog = lJenkinsClient.getConsoleLog(fallbackBuild.getJobName(), fallbackBuild.getBuildNumber());
			    File lLogFile = new File(wFConfig.getFALLBACKBuildLogDir(), lPlan.getId() + "_" + lSystem.getName() + "_FALLBACK_" + Constants.JENKINS_DATEFORMAT.get().format(new Date()) + ".txt");
			    FileUtils.writeStringToFile(lLogFile, consoleLog, false);
			    LOG.warn("Check the log file " + lLogFile.getAbsolutePath());
			    LOG.info(lSystemLoad.getPlanId().getId() + " " + lSystemLoad.getSystemId().getName() + " FallBack Completed");
			    lSystemLoad.getPlanId().setFallbackDateTime(new Date());
			    lSystemLoad.getPlanId().setPlanStatus(Constants.PlanStatus.FALLBACK.name());
			    impPlanDAO.update(fallbackBuild.getUser(), lSystemLoad.getPlanId());

			    PlanStatusActivityMessage planStatusActivityMessage = new PlanStatusActivityMessage(lSystemLoad.getPlanId(), null);
			    planStatusActivityMessage.setStatus(Constants.PlanStatus.FALLBACK.name());
			    planStatusActivityMessage.setlSystem(lSystemLoad.getSystemId().getName());
			    if (fallbackBuild.getReason() != null) {
				planStatusActivityMessage.setReason(fallbackBuild.getReason());
			    }
			    activityLogDAO.save(fallbackBuild.getUser(), planStatusActivityMessage);

			    try {
				// AddTag - FALLBACK
				ImpPlan lImpPlan = impPlanDAO.find(lSystemLoad.getPlanId().getId());
				String lCompanyName = lImpPlan.getSystemLoadList().get(0).getSystemId().getPlatformId().getNickName();
				String lRepoName = getJGitClientUtils().getPlanRepoName(lCompanyName, lImpPlan.getId().toLowerCase());
				List<String> lBranchList = getJGitClientUtils().getAllBranchList(lCompanyName, lImpPlan.getId().toLowerCase());
				if (!getsSHClientUtils().addImplementationTag(lRepoName, Constants.PlanStatus.FALLBACK, lBranchList)) {
				    LOG.error("Unable to add tag-" + Constants.PlanStatus.FALLBACK.name() + " to Plan-" + lImpPlan.getId());
				}
			    } catch (Exception ex) {
				LOG.error("Exception Occur in BPM update " + ex);
			    }

			}

			if (fallBackBuildData == null) {
			    fallBackBuildJobs.remove(fallbackBuild);
			    auditCommonHelper.saveApiTransaction(fallbackBuild.getUser(), fallbackBuild.getBuildType(), fallbackBuild.getStartedDate(), fallbackBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(fallbackBuild.getBuildType()));

			    lWssMessage.setStatus("F");
			    wsserver.sendMessage(Constants.Channels.FALLBACK_BUILD, fallbackBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			} else if (fallBackBuildData != null) {
			    if (jobResult.equals(BuildResult.SUCCESS)) {
				fallBackBuildData.setJobStatus("S");
				fallBackBuildData.setBuildStatus("FULL");
				lWssMessage.setStatus(lPlan.getId() + ": Fallback Success");
				lWssMessage.setPlanId(lSystemLoad.getPlanId().getId());
				lWssMessage.setSystemName(lSystemLoad.getSystemId().getName());
				wsserver.sendMessage(Constants.Channels.FALLBACK_BUILD, fallbackBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			    } else {
				fallBackBuildData.setJobStatus("F");
				lWssMessage.setStatus(lPlan.getId() + ": Fallback Failed");
				wsserver.sendMessage(Constants.Channels.FALLBACK_BUILD, fallbackBuild.getUser().getId(), lPlan.getId(), lWssMessage);
				fallBackBuildJobs.remove(fallbackBuild);
				auditCommonHelper.saveApiTransaction(fallbackBuild.getUser(), fallbackBuild.getBuildType(), fallbackBuild.getStartedDate(), fallbackBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(fallbackBuild.getBuildType()));

			    }
			    onlineBuildDAO.update(fallbackBuild.getUser(), fallBackBuildData);
			}

			LOG.info("FallBack Build Completed for Plan - " + lSystemLoad.getPlanId().getId() + " " + lSystemLoad.getSystemId().getName());

		    } else {
			Map<String, Date> lSystemAndDate = getPlanHelper().getSytemandLoadDate(lSystemLoad.getPlanId());
			getFallbackHelper().exceptionOccurMail(lSystemLoad.getPlanId().getId(), lSystemAndDate);
			// Activity Log update if jenkins job fail
			ExceptionOccurOnlineActivityMessage exceptionOccurOnlineActivityMessage = new ExceptionOccurOnlineActivityMessage(lSystemLoad.getPlanId(), null);
			activityLogDAO.save(fallbackBuild.getUser(), exceptionOccurOnlineActivityMessage);

			OnlineBuild buildData = onlineBuildDAO.findByBuild(lPlan.getId(), lSystem, fallbackBuild.getBuildNumber(), Constants.BUILD_TYPE.FAL_BUILD);
			if (buildData == null) {
			    fallBackBuildJobs.remove(fallbackBuild);
			    auditCommonHelper.saveApiTransaction(fallbackBuild.getUser(), fallbackBuild.getBuildType(), fallbackBuild.getStartedDate(), fallbackBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(fallbackBuild.getBuildType()));

			    lWssMessage.setStatus(lPlan.getId() + ": Fallback Failed");
			    wsserver.sendMessage(Constants.Channels.FALLBACK_BUILD, fallbackBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			} else {
			    buildData.setJobStatus("F");
			    onlineBuildDAO.update(fallbackBuild.getUser(), buildData);
			    lWssMessage.setStatus(lPlan.getId() + ": Fallback Failed");
			    wsserver.sendMessage(Constants.Channels.FALLBACK_BUILD, fallbackBuild.getUser().getId(), lPlan.getId(), lWssMessage);
			}
		    }
		    fallBackBuildJobs.remove(fallbackBuild);
		    auditCommonHelper.saveApiTransaction(fallbackBuild.getUser(), fallbackBuild.getBuildType(), fallbackBuild.getStartedDate(), fallbackBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(fallbackBuild.getBuildType()));

		    if (lAsyncPlansStartTimeMap.containsKey(lPlan.getId() + "-" + Constants.BUILD_TYPE.FAL_BUILD.name())) {
			WFLOGGER.LOG(DEVLBuildMonitor.class, Level.INFO, "<-- 200 " + "https://workflowApi/tsd/markAuxAsFallback(markAuxAsOnlineRevert)" + " (" + (java.lang.System.currentTimeMillis() - lAsyncPlansStartTimeMap.get(lPlan.getId() + "-" + Constants.BUILD_TYPE.FAL_BUILD.name())) + " ms, )");
			lAsyncPlansStartTimeMap.remove(lPlan.getId() + "-" + Constants.BUILD_TYPE.FAL_BUILD.name());
		    }
		}
	    } catch (Exception ex) {
		LOG.error("Error in Loader Job monitor process", ex);
		if (lPlan != null) {
		    cacheClient.getPlanUpdateStatusMap().remove(lPlan.getId());
		}
		fallBackBuildJobs.remove(fallbackBuild);
		auditCommonHelper.saveApiTransaction(fallbackBuild.getUser(), fallbackBuild.getBuildType(), fallbackBuild.getStartedDate(), fallbackBuild.getPlanId(), null, Constants.BUILD_TYPE.getBuildTypeActionUrl(fallbackBuild.getBuildType()));
	    }

	}

    }

}
