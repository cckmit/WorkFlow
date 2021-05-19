/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.helper.PlanHelper;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.utils.Constants;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.timer.Timer;
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
public class PlanStagingSchedular {

    private static final Logger LOG = Logger.getLogger(PlanStagingSchedular.class.getName());

    // Should be local one, on Cache implementation
    // we need to Set Keys in Cache and
    // need to change the isSubmitInprogress API from Cache
    @Autowired
    @Qualifier("lPlanUpdateStatusMap")
    ConcurrentHashMap<String, User> lPlanUpdateStatusMap;
    @Autowired
    DeveloperManagerService developerManagerService;
    @Autowired
    ImpPlanDAO impPlanDAO;
    @Autowired
    PlanHelper planHelper;
    @Autowired
    CacheClient cacheClient;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doApprove() throws Exception {
	for (String lPlanId : lPlanUpdateStatusMap.keySet()) {
	    if (cacheClient.getPlanUpdateStatusMap().containsKey(lPlanId)) {
		User lUser = cacheClient.getPlanUpdateStatusMap().get(lPlanId);
		Boolean isException = false;
		try {
		    ImpPlan imp = impPlanDAO.find(lPlanId);
		    if (imp.getPlanStatus().equalsIgnoreCase(Constants.PlanStatus.SUBMITTED.name())) {
			developerManagerService.doStagingBuild(lUser, lPlanId);
		    }
		} catch (Exception ex) {
		    LOG.info("Exception occurs");
		    isException = true;
		}

		// Block to revert the status
		try {
		    if (isException) {
			planHelper.revertSubmittedPlanToActive(lUser, lPlanId);
		    }
		} catch (Exception ex) {
		    LOG.error("Unable to do revert the plan status from submitted to active");
		}
	    }
	    lPlanUpdateStatusMap.remove(lPlanId);
	}
    }
}
