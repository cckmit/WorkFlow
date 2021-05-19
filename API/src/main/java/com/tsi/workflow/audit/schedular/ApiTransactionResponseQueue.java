/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.schedular;

import com.tsi.workflow.User;
import com.tsi.workflow.audit.beans.dao.ApiActions;
import com.tsi.workflow.audit.beans.dao.ApiTransaction;
import com.tsi.workflow.audit.dao.ApiActionsDAO;
import com.tsi.workflow.audit.dao.ApiTransactionDAO;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.audit.models.RequestQueueModel;
import com.tsi.workflow.audit.service.ApiService;
import com.tsi.workflow.audit.uiform.TransactionInfoForm;
import com.tsi.workflow.audit.utils.Constants;
import com.tsi.workflow.beans.dao.PlanPerformanceViewDAO;
import com.tsi.workflow.beans.ui.PlanPerformanceView;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.utils.JSONResponse;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.management.timer.Timer;
import javax.servlet.http.HttpServletRequest;
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
public class ApiTransactionResponseQueue {

    private static final Logger LOG = Logger.getLogger(ApiTransactionResponseQueue.class.getName());

    @Autowired
    ConcurrentLinkedQueue<RequestQueueModel> apiResponseQueue;

    @Autowired
    ApiService apiService;

    @Autowired
    ApiActionsDAO apiActionsDAO;

    @Autowired
    ApiTransactionDAO apiTransactionDAO;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    PlanPerformanceViewDAO planPerfViewDAO;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	doProcessQueue();
    }

    private void doProcessQueue() {
	for (RequestQueueModel responseQueue : apiResponseQueue) {
	    String cacheKey = auditCommonHelper.generateKey(responseQueue);// responseQueue.getStartDateTime() + "|" + responseQueue.getAuthorizationCode()
	    // + "|" + responseQueue.getPathinfo();
	    try {
		LOG.info("Response Cache Key " + cacheKey);
		ApiTransaction lTranx = cacheClient.getAuditApiTransactioMap().get(cacheKey);
		if (lTranx != null) {
		    lTranx.setEndDt(new Date(responseQueue.getEndDateTime()));
		    lTranx.setResponseTime(responseQueue.getEndDateTime() - responseQueue.getStartDateTime());
		    lTranx.setImplId(responseQueue.getImpId());
		    lTranx.setPlanId(responseQueue.getPlanId());
		    TransactionInfoForm lTranxInfo = new TransactionInfoForm(lTranx);
		    ApiActions apiAction = apiActionsDAO.find(lTranx.getActionsId().getId());
		    if (apiAction.getInfoLevel() != null && !apiAction.getInfoLevel().isEmpty()) {
			if (apiAction.getInfoLevel() != null && Constants.InfoLevel.PLAN.name().equalsIgnoreCase(apiAction.getInfoLevel()) && responseQueue.getPlanId() != null && !responseQueue.getPlanId().isEmpty()) {
			    List<PlanPerformanceView> lPlanView = planPerfViewDAO.findByPlanId(responseQueue.getPlanId());
			    lTranxInfo.setPlanInfo(lPlanView);
			} else if (apiAction.getInfoLevel() != null && Constants.InfoLevel.IMP.name().equalsIgnoreCase(apiAction.getInfoLevel()) && responseQueue.getImpId() != null && !responseQueue.getImpId().isEmpty()) {
			    LOG.info(apiAction.getInfoLevel() + " " + responseQueue.getImpId());
			    List<PlanPerformanceView> lPlanView = planPerfViewDAO.findByImpId(responseQueue.getImpId());
			    lTranxInfo.setPlanInfo(lPlanView);
			}
		    }
		    if (responseQueue.getAsyncParentUrl() != null && !responseQueue.getAsyncParentUrl().isEmpty()) {
			ApiTransaction apiTransaction = apiTransactionDAO.findAsycnTranxInfo(responseQueue.getUser().getId(), responseQueue.getAsyncParentUrl(), responseQueue.getPlanId());
			Long responseTime = responseQueue.getEndDateTime() - apiTransaction.getStartedDt().getTime();
			apiTransaction.setResponseTime(responseTime);
			apiTransaction.setEndDt(new Date(responseQueue.getEndDateTime()));
			apiTransactionDAO.update(apiTransaction);
		    }

		    JSONResponse lReturn = apiService.saveTransaction(lTranxInfo);
		    if (!lReturn.getStatus()) {
			LOG.info("Unable to save into DB");
		    }
		}
	    } catch (Exception ex) {
		LOG.info("Error Occurs on Calling API", ex);
	    } finally {
		cacheClient.getAuditApiTransactioMap().remove(cacheKey);
		apiResponseQueue.remove(responseQueue);
	    }
	}
    }

    private ApiTransaction getApiTransaction(HttpServletRequest request) {
	Long lStartDt = (Long) request.getAttribute("startTime");
	User lUser = null;
	String lManualAuthorizeCode = request.getHeader("Authorization");
	if (lManualAuthorizeCode != null && !lManualAuthorizeCode.isEmpty()) {
	    lUser = cacheClient.getLoginMap().get(lManualAuthorizeCode);
	    if (lUser == null) {
		return null;
	    }
	} else {
	    return null;
	}
	return apiTransactionDAO.find(lStartDt, lUser.getId());
    }
}
