/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.schedular;

import com.tsi.workflow.audit.beans.dao.ApiActions;
import com.tsi.workflow.audit.beans.dao.LinuxServers;
import com.tsi.workflow.audit.dao.ApiActionsDAO;
import com.tsi.workflow.audit.dao.LinuxServersDAO;
import com.tsi.workflow.cache.CacheClient;
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
public class ApiActionsCache {

    private static final Logger LOG = Logger.getLogger(ApiActionsCache.class.getName());

    @Autowired
    CacheClient cacheClient;

    @Autowired
    ApiActionsDAO apiActionsDAO;

    @Autowired
    LinuxServersDAO linuxServersDAO;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_HOUR)
    @Transactional
    public void doUpdateApiActionCache() {
	updateApiActionsCache();
	updateLinuxServersCache();
    }

    private void updateApiActionsCache() {
	cacheClient.getAuditApiActionsMap().clear();
	List<ApiActions> lActions = apiActionsDAO.findAllActive();
	if (lActions != null) {
	    lActions.stream().forEach(t -> cacheClient.getAuditApiActionsMap().put(t.getActionUrl(), t));
	}
    }

    private void updateLinuxServersCache() {
	cacheClient.getAuditLinuxServersMap().clear();
	List<LinuxServers> lLinuxServers = linuxServersDAO.findAll();
	if (lLinuxServers != null) {
	    lLinuxServers.stream().forEach(t -> cacheClient.getAuditLinuxServersMap().put(t.getDnsName(), t));
	}
    }
}
