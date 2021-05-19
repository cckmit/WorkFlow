/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import com.tsi.workflow.activity.TOSActivityMessage;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.ui.TosActionQueue;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ProductionLoadsDAO;
import com.tsi.workflow.tos.TOSHelper;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
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
public class ProdTOSCommandListener {

    private static final Logger LOG = Logger.getLogger(ProdTOSCommandListener.class.getName());

    @Autowired
    CacheClient cacheClient;

    @Autowired
    ProductionLoadsDAO prodLoadsDAO;

    @Autowired
    TOSHelper tOSHelper;

    @Autowired
    ImpPlanDAO impPlanDAO;

    @Autowired
    ConcurrentHashMap<String, Long> lAsyncPlansStartTimeMap;

    @Autowired
    ActivityLogDAO activityLogDAO;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void monitor() {
	List<String> lRemovePlans = new ArrayList();
	cacheClient.getProdTosCommandProcessingMap().entrySet().stream().forEach(planCommand -> {
	    TosActionQueue lTosQueue = planCommand.getValue();
	    String lastAction = lTosQueue.getOldStatus();
	    LOG.info("Tos last Action --> " + lastAction);

	    ProductionLoads lProdLoads = prodLoadsDAO.find(lTosQueue.getTosRecId());
	    if (lProdLoads == null || !lProdLoads.getLastActionStatus().equalsIgnoreCase("INPROGRESS")) {
		return;
	    }
	    Constants.LOAD_SET_STATUS lActions = Constants.LOAD_SET_STATUS.valueOf(lProdLoads.getStatus());
	    boolean lResult = false;
	    switch (lActions) {
	    case LOADED:
		lResult = tOSHelper.doTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.LOAD, lProdLoads, lastAction, lProdLoads.getSystemLoadId());
		break;
	    case ACTIVATED:
		lResult = tOSHelper.doTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.ACTIVATE, lProdLoads, lastAction, lProdLoads.getSystemLoadId());
		break;
	    case DEACTIVATED:
		lResult = tOSHelper.doTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.DEACTIVATE, lProdLoads, lastAction, lProdLoads.getSystemLoadId());
		break;
	    case DELETED:
		lResult = tOSHelper.doTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.DELETE, lProdLoads, lastAction, lProdLoads.getSystemLoadId());
		break;
	    case FALLBACK_LOADED:
		lResult = tOSHelper.doFallbackTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.LOAD, lProdLoads, lastAction, lProdLoads.getSystemLoadId(), null);
		break;
	    case FALLBACK_ACTIVATED:
		lResult = tOSHelper.doFallbackTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.ACTIVATE, lProdLoads, lastAction, lProdLoads.getSystemLoadId(), null);
		break;
	    case FALLBACK_DEACTIVATED:
		lResult = tOSHelper.doFallbackTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.DEACTIVATE, lProdLoads, lastAction, lProdLoads.getSystemLoadId(), null);
		break;
	    case FALLBACK_DELETED:
		lResult = tOSHelper.doFallbackTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.DELETE, lProdLoads, lastAction, lProdLoads.getSystemLoadId(), null);
		break;
	    case ACCEPTED:
		lResult = tOSHelper.doTOSOperation(lTosQueue.getUser(), Constants.LoadSetCommands.ACCEPT, lProdLoads, lastAction, lProdLoads.getSystemLoadId());
		break;
	    default:
		break;
	    }
	    if (!lResult) {
		lProdLoads.setLastActionStatus("FAILED");
		lProdLoads.setStatus(lastAction);
		prodLoadsDAO.update(lTosQueue.getUser(), lProdLoads);
	    } else {
		if (!lAsyncPlansStartTimeMap.containsKey(lProdLoads.getPlanId().getId() + "-" + lProdLoads.getSystemId().getName() + "-" + lProdLoads.getStatus())) {
		    lAsyncPlansStartTimeMap.put(lProdLoads.getPlanId().getId() + "-" + lProdLoads.getSystemId().getName() + "-" + lProdLoads.getStatus(), java.lang.System.currentTimeMillis());
		}
	    }
	    activityLogDAO.save(lTosQueue.getUser(), new TOSActivityMessage(lProdLoads.getPlanId(), null, lProdLoads));
	    lRemovePlans.add(planCommand.getKey());
	});
	lRemovePlans.stream().forEach(t -> cacheClient.getProdTosCommandProcessingMap().remove(t));
    }
}
