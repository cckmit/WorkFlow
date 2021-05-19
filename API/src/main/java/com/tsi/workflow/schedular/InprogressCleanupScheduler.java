package com.tsi.workflow.schedular;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.utils.Constants;
import java.util.Set;
import javax.management.timer.Timer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InprogressCleanupScheduler {

    private static final Logger LOG = Logger.getLogger(InprogressCleanupScheduler.class.getName());

    @Autowired
    CacheClient cacheClient;
    @Autowired
    ImpPlanDAO lImpPlanDAO;

    @Scheduled(initialDelay = Timer.ONE_MINUTE / 2, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	if (cacheClient.getInProgressRelatedPlanMap() != null) {
	    cacheClient.getInProgressRelatedPlanMap().forEach((key, value) -> {
		boolean isPostPlansAvailable = false;
		LOG.info("Scheduler Processing for the Plan: " + key);
		Set<String> postDependentPlans = lImpPlanDAO.getPostDependentPlanList(key);
		ImpPlan lPlan = lImpPlanDAO.find(key);
		if (postDependentPlans.stream().filter(x -> cacheClient.getInProgressRelatedPlanMap().containsKey(x)).findAny().isPresent()) {
		    isPostPlansAvailable = true;
		    LOG.info("Rejection Plan available : " + key);
		}

		if (lPlan != null && Constants.PlanStatus.ACTIVE.name().equals(lPlan.getPlanStatus()) && !isPostPlansAvailable) {
		    cacheClient.getInProgressRelatedPlanMap().remove(key);
		}
	    });
	}
    }
}
