/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.schedular;

import com.tsi.workflow.audit.config.AuditConfig;
import com.tsi.workflow.audit.utils.Constants;
import com.tsi.workflow.cache.CacheClient;
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
public class AuditConfigSchedular {

    private static final Logger LOG = Logger.getLogger(AuditConfigSchedular.class.getName());

    @Autowired
    CacheClient cacheClient;

    @Autowired
    AuditConfig auditConfig;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_HOUR)
    @Transactional
    public void doMonintor() {
	updateAuditConfig();
    }

    private void updateAuditConfig() {
	Boolean cacheAuditSystemFlag = cacheClient.getAuditConfig().get(Constants.AuditConfiguration.SystemView.name());
	if (cacheAuditSystemFlag == null) {
	    cacheClient.getAuditConfig().put(Constants.AuditConfiguration.SystemView.name(), auditConfig.getAuditSystem());
	}

	Boolean cacheAuditTransactionFlag = cacheClient.getAuditConfig().get(Constants.AuditConfiguration.TransactionView.name());
	if (cacheAuditTransactionFlag == null) {
	    cacheClient.getAuditConfig().put(Constants.AuditConfiguration.TransactionView.name(), auditConfig.getAuditSystem());
	}
    }
}
