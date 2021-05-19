/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.helper;

import com.tsi.workflow.User;
import com.tsi.workflow.audit.config.AuditConfig;
import com.tsi.workflow.audit.models.RequestQueueModel;
import com.tsi.workflow.audit.utils.Constants;
import com.tsi.workflow.cache.CacheClient;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author Radha.Adhimoolam
 */
@Component
public class AuditCommonHelper {

    private static final Logger LOG = Logger.getLogger(AuditCommonHelper.class.getName());

    @Autowired
    AuditConfig auditConfig;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    ConcurrentLinkedQueue<RequestQueueModel> apiRequestQueue;

    @Autowired
    ConcurrentLinkedQueue<RequestQueueModel> apiResponseQueue;

    public void setPlanandImplId(HttpServletRequest httpRequest, Object reqeustBody) {
	try {

	} catch (Exception ex) {
	    LOG.info("Unable to set Imp Plan id and implementation id", ex);
	}
    }

    public Boolean isAuditNeeded() {
	return (getAuditSystem() || getAuditTransaction());
    }

    public Boolean isAuditNeeded(HttpServletRequest request) {
	return ((getAuditSystem() || getAuditTransaction()) && auditConfig.getAuditMethodsAsList().contains(request.getMethod()));
    }

    public void addApiTransaction(User user, String buildType, Date date) {
	if (!isAuditNeeded()) {
	    return;
	}
	RequestQueueModel lRequestWrapper = new RequestQueueModel();
	lRequestWrapper.setStartDateTime(date.getTime());
	lRequestWrapper.setUser(user);
	lRequestWrapper.setPathinfo(buildType);
	apiRequestQueue.add(lRequestWrapper);
    }

    public void saveApiTransaction(User user, String buildType, Date startDate, String planId, String impId, String parentActionUrl) {
	if (!isAuditNeeded()) {
	    return;
	}

	RequestQueueModel lRequestWrapper = new RequestQueueModel();
	lRequestWrapper.setEndDateTime(System.currentTimeMillis());
	lRequestWrapper.setStartDateTime(startDate.getTime());
	lRequestWrapper.setPathinfo(buildType);
	lRequestWrapper.setPlanId(planId);
	lRequestWrapper.setImpId(impId);
	lRequestWrapper.setUser(user);
	lRequestWrapper.setAsyncParentUrl(parentActionUrl);
	apiResponseQueue.add(lRequestWrapper);
    }

    public void removeApiTransaction(User user, String buildType, Date date) {
	RequestQueueModel lRequestWrapper = new RequestQueueModel();
	lRequestWrapper.setStartDateTime(date.getTime());
	lRequestWrapper.setUser(user);
	lRequestWrapper.setPathinfo(buildType);
	String cacheKey = generateKey(lRequestWrapper);
	cacheClient.getAuditApiTransactioMap().remove(cacheKey);
    }

    public String generateKey(RequestQueueModel requestModel) {
	String cacheKey = requestModel.getStartDateTime() + "|" + requestModel.getAuthorizationCode() + "|" + requestModel.getPathinfo();
	return cacheKey;
    }

    public Boolean getAuditSystem() {
	return cacheClient.getAuditConfig().get(Constants.AuditConfiguration.SystemView.name());
    }

    public Boolean getAuditTransaction() {
	return cacheClient.getAuditConfig().get(Constants.AuditConfiguration.TransactionView.name());
    }

    public <T> boolean isObjectNullAndEmpty(T object) {
	if (object == null) {
	    return true;
	}
	if (object instanceof String && ((String) object).isEmpty()) {
	    return true;
	}
	if (object instanceof List && ((List) object).isEmpty()) {
	    return true;
	}
	if (object instanceof Set && ((Set) object).isEmpty()) {
	    return true;
	}
	if (object instanceof Map && ((Map) object).isEmpty()) {
	    return true;
	}
	if (object instanceof Long && ((Long) object).toString().isEmpty()) {
	    return true;
	}
	return false;
    }

}
