/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.schedular;

import com.tsi.workflow.GITConfig;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.audit.beans.dao.ApiActions;
import com.tsi.workflow.audit.beans.dao.ApiTransaction;
import com.tsi.workflow.audit.beans.dao.LinuxServers;
import com.tsi.workflow.audit.dao.ApiActionsDAO;
import com.tsi.workflow.audit.dao.LinuxServersDAO;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.audit.httpclients.ApiHttpClients;
import com.tsi.workflow.audit.models.RequestQueueModel;
import com.tsi.workflow.audit.service.ApiService;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.cache.CacheClient;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
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
public class ApiTransactionRequestQueue {

    private static final Logger LOG = Logger.getLogger(ApiTransactionRequestQueue.class.getName());

    @Autowired
    ConcurrentLinkedQueue<RequestQueueModel> apiRequestQueue;

    @Autowired
    ApiHttpClients apiHttpClients;

    @Autowired
    ApiService apiService;

    @Autowired
    ApiActionsDAO apiActionsDAO;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    WFConfig wfConfig;

    @Autowired
    GITConfig gitConfig;

    @Autowired
    LDAPAuthenticatorImpl ldapInfo;

    @Autowired
    LinuxServersDAO lnxServerDAO;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Scheduled(initialDelay = Timer.ONE_MINUTE, fixedDelay = Timer.ONE_SECOND)
    @Transactional
    public void doMonitor() {
	for (RequestQueueModel request : apiRequestQueue) {
	    try {
		// apiHttpClients.sendPOST(request);
		String cacheKey = auditCommonHelper.generateKey(request);// request.getStartDateTime() + "|" + request.getAuthorizationCode() + "|" +
									 // request.getPathinfo();
		ApiTransaction lApiTransaction = createApiTransaction(request);
		if (lApiTransaction != null) {
		    lApiTransaction.setStartedDt(new Date(request.getStartDateTime()));
		    lApiTransaction.setParams(request.getQueryString());
		    if (!cacheClient.getAuditApiTransactioMap().containsKey(cacheKey)) {
			cacheClient.getAuditApiTransactioMap().put(cacheKey, lApiTransaction);
			LOG.info("Cache Key " + cacheKey);
		    }
		}
	    } catch (Exception ex) {
		LOG.info("Error Occurs on Calling API", ex);
	    } finally {
		apiRequestQueue.remove(request);
	    }
	}
    }

    private ApiTransaction createApiTransaction(RequestQueueModel requestQueue) throws Exception {
	if (requestQueue.getPathinfo() == null) {
	    LOG.info("Received null value for " + requestQueue.getRequestURL() + " -> " + requestQueue.getPathinfo());
	    return null;
	}

	ApiTransaction lApiTransaction = new ApiTransaction();
	ApiActions lActions = getApiActions(requestQueue);
	lApiTransaction.setActionsId(lActions);

	LinuxServers lLnxServer = getLinuxServerInfo(gitConfig.getGitHost());
	lApiTransaction.setLnxServerId(lLnxServer);

	String lManualAuthorizeCode = requestQueue.getAuthorizationCode();
	User lUser = new User();
	if (requestQueue.getUser() != null) {
	    lUser = requestQueue.getUser();
	    lApiTransaction.setUserRole(lUser.getCurrentRole());
	} else if (lManualAuthorizeCode != null && !lManualAuthorizeCode.isEmpty()) {
	    lUser = cacheClient.getLoginMap().get(lManualAuthorizeCode);
	    if (lUser != null) {
		lApiTransaction.setUserRole(lUser.getCurrentRole());
	    } else {
		lUser = ldapInfo.getServiceUser();
	    }
	} else {
	    lUser = ldapInfo.getServiceUser();
	}
	lApiTransaction.setUserId(lUser.getId());
	lApiTransaction.setUserName(lUser.getDisplayName());

	return lApiTransaction;
    }

    private ApiActions getApiActions(RequestQueueModel requestQueue) throws Exception {
	ApiActions lAction = cacheClient.getAuditApiActionsMap().get(requestQueue.getPathinfo());
	if (lAction == null) {
	    lAction = apiActionsDAO.findByActionUrl(requestQueue.getPathinfo());
	    if (lAction == null) {
		lAction = new ApiActions();
		lAction.setActionUrl(requestQueue.getPathinfo());
		lAction.setActionMethod(requestQueue.getMethod());
		lAction.setActionName(requestQueue.getPathinfo());
		lAction.setActive("Y");
		lAction.setIsSchedular(Boolean.FALSE);
		lAction.setInfoLevel("NONE");
		apiActionsDAO.save(lAction);
	    }
	    cacheClient.getAuditApiActionsMap().put(lAction.getActionUrl(), lAction);
	}
	return lAction;
    }

    private LinuxServers getLinuxServerInfo(String hostName) throws Exception {
	String hostProfile = wfConfig.getIsDeltaApp() ? "Delta" : "Travelport";
	LinuxServers lLnxServer = cacheClient.getAuditLinuxServersMap().get(hostName);
	if (lLnxServer == null) {
	    lLnxServer = lnxServerDAO.findByHostNameAndProfile(hostName, hostProfile);
	    if (lLnxServer == null) {
		lLnxServer = new LinuxServers();
		lLnxServer.setDnsName(hostName);
		lLnxServer.setHostProfile(hostProfile);
		lnxServerDAO.save(lLnxServer);
	    }
	    cacheClient.getAuditLinuxServersMap().put(hostName, lLnxServer);
	}
	return lLnxServer;

    }
}
