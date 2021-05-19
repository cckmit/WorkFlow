/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tdx.executor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.CheckoutActivityMessage;
import com.tsi.workflow.activity.CommitActivityMessage;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.tdx.executor.models.TdxShellExecutorModel;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

/**
 *
 * @author vamsi.krishnarao
 */
@Component
public class CommitExecutor {

    private static final Logger LOG = Logger.getLogger(CommitExecutor.class.getName());

    @Autowired
    ImplementationDAO implementationDAO;

    @Autowired
    SSHClientUtils sSHClientUtils;

    @Autowired
    ImplementationDAO impDAO;

    @Autowired
    ActivityLogDAO activityLogDAO;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public ImplementationDAO getImplementationDAO() {
	return implementationDAO;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public Future<TdxShellExecutorModel> executeCommit(TdxShellExecutorModel executorModel) {
	LOG.info("System: " + executorModel.getSystem().getName() + " DateAndTime : " + new Date());
	JSONResponse lReturn = new JSONResponse();
	Map<String, String> lResult = new HashMap();
	try {
	    Implementation implementaion = executorModel.getImpl();
	    com.tsi.workflow.beans.dao.System lSystem = executorModel.getSystem();
	    User lUser = executorModel.getUser();
	    String implId = implementaion.getId();
	    String lSystemName = lSystem.getName();
	    List<CheckoutSegments> segList = executorModel.getSegmentList();
	    ArrayList<String> lList = new ArrayList<>();
	    Implementation imp = getImplementationDAO().find(implId);
	    segList.stream().forEach(t -> lList.add(t.getFileName()));

	    if (!lList.isEmpty()) {
		String lCommand = Constants.SystemScripts.COMMIT.getScript() + " \"" + (implId + "_" + lSystemName).toLowerCase() + "\" \"" + String.join(",", lList) + "\" \"" + executorModel.getCommitMessage() + "\"";
		JSONResponse lResponse = getsSHClientUtils().executeCommand(lUser, lSystem, lCommand);

		String lErrMsg = "";
		String lResponseData = "";
		LOG.info("Shell Script Message " + lResponse.getData() + " Error  " + lResponse.getErrorMessage() + " Display   " + lResponse.getDisplayErrorMessage());
		if (!lResponse.getStatus()) {
		    lErrMsg = lResponse.getDisplayErrorMessage();
		    CommitActivityMessage lErrorMessage = new CommitActivityMessage(imp.getPlanId(), imp);
		    lErrorMessage.setComments(lErrMsg);
		    lErrorMessage.setCommit(true);
		    getActivityLogDAO().save(lUser, lErrorMessage);
		} else {
		    lResponseData = lResponse.getData().toString();
		    for (CheckoutSegments segment : segList) {
			CheckoutActivityMessage lMessage = new CheckoutActivityMessage(imp.getPlanId(), imp, segment);
			lMessage.setCommit(true);
			getActivityLogDAO().save(lUser, lMessage);
		    }
		}
		executorModel.setReturnData(lResponseData);
		lResult.put(lSystem.getName(), lErrMsg);
	    }
	    lReturn.setStatus(Boolean.TRUE);
	} catch (Exception ex) {
	    LOG.info("Exception on child", ex);
	    lReturn.setStatus(Boolean.FALSE);
	}
	// Try to convert map to string
	try {
	    ObjectMapper objectMapper = new ObjectMapper();
	    lReturn.setData(objectMapper.writeValueAsString(lResult));
	} catch (Exception ex) {
	    LOG.info("Exception on Object Mapper", ex);
	}
	executorModel.setJsonResponse(lReturn);
	return new AsyncResult<>(executorModel);
    }

}
