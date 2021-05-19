/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tdx.executor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.CheckinActivityMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.tdx.executor.models.TdxShellExecutorModel;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class CheckInExecutor {

    private static final Logger LOG = Logger.getLogger(CheckInExecutor.class.getName());

    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    GITConfig gITConfig;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    ImplementationDAO impDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    private String changedSegs = null;
    private String deletedSegs = null;

    public Future<TdxShellExecutorModel> executeCheckin(TdxShellExecutorModel executorModel) {
	JSONResponse lReturn = new JSONResponse();
	Map<String, String> lReturnResponse = new HashMap();
	Boolean activityStatus = Boolean.TRUE;
	try {
	    Implementation impl = executorModel.getImpl();
	    System system = executorModel.getSystem();
	    User user = executorModel.getUser();
	    ImpPlan plan = executorModel.getPlan();

	    JSONResponse checkinResponse = doCheckInImplementation(impl, system, user, executorModel.getIsSegmentDelete());
	    lReturnResponse.put("CHECK_IN", checkinResponse.getStatus().toString());
	    if (!checkinResponse.getStatus()) {
		LOG.info("Error Message for " + system.getName() + " : " + checkinResponse.getDisplayErrorMessage());
		activityStatus = Boolean.FALSE;
		lReturnResponse.put("CHECK_IN_ERROR_MESSAGE", checkinResponse.getDisplayErrorMessage());
	    } else {
		executorModel.setReturnData(checkinResponse.getData().toString());
		// JSONResponse workspaceResponse = devlWorkspaceImplementation(impl, system,
		// user);
		// lReturnResponse.put("DEVL_WORKSPACE",
		// workspaceResponse.getStatus().toString());
		// if (!workspaceResponse.getStatus()) {
		// LOG.info("Error Message for " + system.getName() + " : " +
		// workspaceResponse.getData());
		// lReturnResponse.put("DEVL_WORKSPACE_ERROR_MESSAGE",
		// workspaceResponse.getDisplayErrorMessage());
		// }
		if ((((changedSegs == null || changedSegs.equalsIgnoreCase("NULL")) && plan.getFullBuildDt() == null) || ((changedSegs != null && !changedSegs.equalsIgnoreCase("NULL")) || (deletedSegs != null && !deletedSegs.equalsIgnoreCase("NULL"))))) {
		    JSONResponse exportResponse = exportImplementation(impl, system, user);
		    lReturnResponse.put("EXPORT_WORKSPACE", exportResponse.getStatus().toString());
		    if (!exportResponse.getStatus()) {
			activityStatus = Boolean.FALSE;
			LOG.info("Error Message for " + system.getName() + " : " + exportResponse.getData());
			lReturnResponse.put("EXPORT_WORKSPACE_ERROR_MESSAGE", exportResponse.getDisplayErrorMessage());
		    }
		}
	    }
	    executorModel.setActivityStatus(activityStatus);
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.info("Exception on child", ex);
	    lReturn.setStatus(Boolean.FALSE);
	}
	// Try to convert map to string
	try {
	    ObjectMapper objectMapper = new ObjectMapper();
	    lReturn.setData(objectMapper.writeValueAsString(lReturnResponse));
	} catch (Exception ex) {
	    LOG.info("Exception on Object Mapper", ex);
	}
	executorModel.setJsonResponse(lReturn);
	return new AsyncResult<>(executorModel);
    }

    private JSONResponse doCheckInImplementation(Implementation impl, com.tsi.workflow.beans.dao.System system, User user, Boolean isSegmentDelete) throws Exception {
	JSONResponse lReturn = new JSONResponse();
	String implId = impl.getId();
	String systemName = system.getName();

	// ZTPFM-2447 Code changes to add Last check in date in checkin script param
	String lastCheckInDateTime = (impl.getCheckinDateTime() != null) ? Constants.JENKINS_DATEFORMAT.get().format(impl.getCheckinDateTime()) : "NULL";

	String lCheckInCommand = Constants.SystemScripts.CHECK_IN.getScript() + (implId + "_" + systemName).toLowerCase() + " " + lastCheckInDateTime + " " + ((!isSegmentDelete || isSegmentDelete == null) ? "checkin" : "delete");
	List<String> changedSegList = new ArrayList<>();

	JSONResponse checkinResponse = sSHClientUtils.executeCommand(user, system, lCheckInCommand);

	// ZTPFM-2447 Add the list of changed segments
	if (checkinResponse.getStatus()) {
	    Optional<String> changedString = Arrays.asList(checkinResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("changedFiles")).findAny();
	    changedSegs = (changedString != null && changedString.isPresent() && !changedString.get().replace("changedFiles:", "").trim().isEmpty()) ? changedString.get().replace("changedFiles:", "") : "NULL";
	    if (changedSegs != null && !changedSegs.isEmpty() && !"NULL".equalsIgnoreCase(changedSegs)) {

		Arrays.asList(changedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
		    changedSegList.add(seg.trim());
		});
	    }

	    Optional<String> deletedString = Arrays.asList(checkinResponse.getData().toString().split("\n")).stream().filter(x -> x.contains("deletedFiles")).findAny();
	    deletedSegs = (deletedString != null && deletedString.isPresent() && !deletedString.get().replace("deletedFiles:", "").trim().isEmpty()) ? deletedString.get().replace("deletedFiles:", "") : "NULL";

	    if (deletedSegs != null && !deletedSegs.isEmpty() && !"NULL".equalsIgnoreCase(deletedSegs)) {
		Arrays.asList(deletedSegs.split(",")).stream().filter(x -> x != null && !x.trim().isEmpty()).forEach(seg -> {
		    changedSegList.add(seg.trim());
		});
	    }
	    lReturn.setStatus(checkinResponse.getStatus());
	    lReturn.setData(String.join(",", changedSegList));
	} else {
	    CheckinActivityMessage lMessage = new CheckinActivityMessage(impl.getPlanId(), impl);
	    lMessage.setError(true);
	    lMessage.setComments(checkinResponse.getDisplayErrorMessage());
	    activityLogDAO.save(user, lMessage);

	    lReturn.setStatus(checkinResponse.getStatus());
	    lReturn.setErrorMessage(checkinResponse.getErrorMessage());
	    lReturn.setData("");
	}

	return lReturn;
    }

    private JSONResponse devlWorkspaceImplementation(Implementation imp, com.tsi.workflow.beans.dao.System system, User user) {
	String lDevlWorkspaceCommand = Constants.SystemScripts.CREATE_DEVL_WORKSPACE.getScript() + (imp.getId() + "_" + system.getName()).toLowerCase();
	JSONResponse lResponse = sSHClientUtils.executeCommand(system, lDevlWorkspaceCommand);
	if (!lResponse.getStatus()) {
	    CheckinActivityMessage lMessage = new CheckinActivityMessage(imp.getPlanId(), imp);
	    lMessage.setError(true);
	    lMessage.setComments(lResponse.getDisplayErrorMessage());
	    activityLogDAO.save(user, lMessage);
	}
	return lResponse;
    }

    private JSONResponse exportImplementation(Implementation imp, com.tsi.workflow.beans.dao.System system, User user) {
	String lExportCommand = Constants.SystemScripts.EXPORT.getScript() + "/home/" + user.getId() + "/projects/" + (imp.getId() + "/" + system.getName()).toLowerCase() + " \"" + (imp.getId() + "_" + system.getName()).toLowerCase() + "\" \"" + gITConfig.getServiceUserID() + "\"" + " \"" + changedSegs + "\"" + " \"" + deletedSegs + "\"";
	JSONResponse lResponse = sSHClientUtils.executeCommand(system, lExportCommand);
	if (!lResponse.getStatus()) {
	    CheckinActivityMessage lMessage = new CheckinActivityMessage(imp.getPlanId(), imp);
	    lMessage.setError(true);
	    lMessage.setComments(lResponse.getDisplayErrorMessage());
	    activityLogDAO.save(user, lMessage);
	}
	return lResponse;
    }

}
