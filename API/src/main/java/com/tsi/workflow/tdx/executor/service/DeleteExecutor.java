/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tdx.executor.service;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.CheckoutActivityMessage;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.tdx.executor.models.TdxShellExecutorModel;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 *
 * @author vamsi.krishnarao
 */
@Component
public class DeleteExecutor {

    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    ImplementationDAO impDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Autowired
    GITConfig gITConfig;

    @Autowired
    BuildDAO buildDAO;

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GITConfig getgITConfig() {
	return gITConfig;
    }

    private static final Logger LOG = Logger.getLogger(DeleteExecutor.class.getName());

    public Future<TdxShellExecutorModel> executeDelete(TdxShellExecutorModel executorModel) {
	LOG.info("System: " + executorModel.getSystem().getName() + " DateAndTime : " + new Date());
	try {
	    Implementation impl = executorModel.getImpl();
	    com.tsi.workflow.beans.dao.System lSystem = executorModel.getSystem();
	    User pUser = executorModel.getUser();
	    ImpPlan plan = executorModel.getPlan();
	    List<CheckoutSegments> segments = executorModel.getSegmentList();
	    List<String> segList = segments.stream().map(t -> t.getFileName()).collect(Collectors.toList());
	    String lArguments = impl.getId().toLowerCase() + " " + lSystem.getName().toLowerCase() + " " + String.join(",", segList);
	    String lCommand = Constants.SystemScripts.DELETE_FILE.getScript() + lArguments;

	    JSONResponse sshResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lCommand);
	    if (sshResponse.getStatus()) {
		executorModel.setSegmentsToBeDeleted(segments);
		for (CheckoutSegments lSegment : segments) {
		    CheckoutActivityMessage lMessage = new CheckoutActivityMessage(plan, impl, lSegment);
		    lMessage.setDeleteFile(true);
		    getActivityLogDAO().save(pUser, lMessage);
		}
		// Delete build for the plan
		if (impl.getPlanId().getRejectedDateTime() == null) { // 2413
		    List<Build> lBuildList = getBuildDAO().findByImpPlan(impl.getPlanId());
		    for (Build lbuild : lBuildList) {
			getBuildDAO().delete(pUser, lbuild);
		    }
		}
	    }
	    List<CheckoutSegments> newFileList = segments.stream().filter(t -> (t.getRefStatus() != null && t.getRefStatus().equalsIgnoreCase("newfile") && t.getPlanId().getRejectedDateTime() != null)).collect(Collectors.toList());
	    if (newFileList != null && !newFileList.isEmpty()) {
		String url = "ssh://" + gITConfig.getWfLoadBalancerHost() + ":" + gITConfig.getGitDataPort() + "/";
		String reposAndFileName = String.join("|", newFileList.stream().map(t -> t.getSourceUrl().replace(url, "") + "," + t.getFileName()).collect(Collectors.toList()));
		String lArgument = lSystem.getName().toLowerCase() + " " + plan.getId().toLowerCase() + " " + reposAndFileName;
		LOG.info("Final Argument : " + lArgument);
		String lScript = Constants.SystemScripts.DELETE_NEW_SOURCE_ARTIFACT.getScript() + lArgument;
		JSONResponse lResponse = getsSHClientUtils().executeCommand(pUser, lSystem, lScript);
		if (!lResponse.getStatus()) {
		    LOG.info(" ERROR WHILE DELETING NEW FILE : " + lResponse.getErrorMessage());
		}
	    }
	} catch (Exception ex) {
	    LOG.info("Exception on child", ex);
	}
	return new AsyncResult<>(executorModel);
    }
}
