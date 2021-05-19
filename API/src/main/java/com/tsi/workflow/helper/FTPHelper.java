/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.activity.YodaResponseActivityMessage;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.YodaResult;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author USER
 */
@Component
public class FTPHelper {

    private static final Logger LOG = Logger.getLogger(FTPHelper.class.getName());

    @Autowired
    SystemDAO systemDAO;
    @Autowired
    GITConfig gITConfig;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    ActivityLogDAO activityLogDAO;

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public ActivityLogDAO getActivityLogDAO() {
	return activityLogDAO;
    }

    public JSONResponse doFTP(User lUser, SystemLoad load, Build lBuild, String ipAddress, Boolean isfallback) {
	com.tsi.workflow.beans.dao.System system = getSystemDAO().find(load.getSystemId().getId());

	String fileExtension = "";
	if (lBuild.getLoadSetType().equalsIgnoreCase(Constants.LoaderTypes.A.name())) {
	    fileExtension = Constants.LOAD_FILE_EXTENSION.TLDR_EXTN.getValue();
	} else if (lBuild.getLoadSetType().equalsIgnoreCase(Constants.LoaderTypes.E.name())) {
	    fileExtension = Constants.LOAD_FILE_EXTENSION.OLDR_EXTN.getValue();
	}
	String command = "";
	if (!isfallback) {
	    command = Constants.SystemScripts.FTP.getScript() + lBuild.getPlanId().getImplementationList().get(0).getId().toLowerCase() + "_" + lBuild.getSystemId().getName().toLowerCase() + " " + ipAddress + " " + lBuild.getLoadSetType() + " " + load.getLoadSetName() + fileExtension + " " + lBuild.getBuildType().substring(0, 3);
	} else {
	    command = Constants.SystemScripts.FTP.getScript() + lBuild.getPlanId().getImplementationList().get(0).getId().toLowerCase() + "_" + lBuild.getSystemId().getName().toLowerCase() + " " + ipAddress + " " + lBuild.getLoadSetType() + " " + load.getFallbackLoadSetName() + fileExtension + " " + lBuild.getBuildType().substring(0, 3);
	}

	JSONResponse lResponse = getsSHClientUtils().executeCommand(system, command);
	return lResponse;
    }

    public void getYodaLoadSetPath(User pUser, ImpPlan lPlan, JSONResponse lSSHResponse) {
	String lYodaResponse = lSSHResponse.getData().toString();
	LOG.info("Yoda Response data " + lYodaResponse);
	YodaResult lYodaResult = new YodaResult();
	lYodaResult.setRc(0);
	lYodaResult.setMessage(lYodaResponse);
	YodaResponseActivityMessage responseActivityMessage = new YodaResponseActivityMessage(lPlan, null);
	responseActivityMessage.setFtpFlag(Boolean.TRUE);
	responseActivityMessage.setlYodaResult(lYodaResult);
	responseActivityMessage.setYodaActivity(Constants.YodaActivtiyMessage.FTP);
	getActivityLogDAO().save(pUser, responseActivityMessage);
    }
}
