/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.dao.BuildDAO;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.helper.MaintenanceHelper;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import com.workflow.ssh.SSHClientUtils;
import java.util.List;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class MaintenanceMonitor {

    private static final Logger LOG = Logger.getLogger(MaintenanceMonitor.class.getName());

    @Autowired
    GITConfig gITConfig;
    @Autowired
    WFConfig wFConfig;
    @Autowired
    MaintenanceHelper maintenanceHelper;
    @Autowired
    SSHClientUtils sSHClientUtils;
    @Autowired
    BuildDAO buildDAO;
    @Autowired
    SystemDAO systemDAO;

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public BuildDAO getBuildDAO() {
	return buildDAO;
    }

    public SSHClientUtils getsSHClientUtils() {
	return sSHClientUtils;
    }

    public GITConfig getGITConfig() {
	return gITConfig;
    }

    public WFConfig getWFConfig() {
	return wFConfig;
    }

    public MaintenanceHelper getMaintenanceHelper() {
	return maintenanceHelper;
    }

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_MINUTE * 30)
    @Transactional
    public void doMonitor() {
	// Date lDate = new Date();
	JSONResponse lResponse = new JSONResponse();
	List<System> lSystemList = getSystemDAO().findAll();
	// List<Build> buildInProgress = new ArrayList<>();
	// LOG.info("lSystemList : " + lSystemList);
	// List<Build> lPendingBuild = getBuildDAO().findBuildInProgress();
	// lPendingBuild.forEach((pBuild) -> {
	// long Diff = (lDate.getTime() - pBuild.getCreatedDt().getTime()) / (60 * 60 *
	// 1000);
	// if (Diff < 24) {
	// buildInProgress.add(pBuild);
	// }
	// });
	// if (buildInProgress.isEmpty()) {
	for (System lSystem : lSystemList) {

	    String command = Constants.SystemScripts.TPF_TEST_SYSTEM_MAINTENANCE_OLDR_CREATION.getScript() + " " + lSystem.getName().toLowerCase();
	    LOG.info(command);
	    lResponse = getsSHClientUtils().executeCommand(lSystem, command);
	    LOG.info("lResponse : " + lResponse.getStatus());

	    if (!lResponse.getStatus()) {
		LOG.error("Nightly Maintenance process is failed for System - " + lSystem.getName().toLowerCase() + ", Error Occurs in Oldr File Creation");
		getMaintenanceHelper().notifyToolAdminOnProcessFails(lSystem.getName());
	    }
	}
    }

}
// }
