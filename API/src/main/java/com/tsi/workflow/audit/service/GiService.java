/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.service;

import com.tsi.workflow.audit.beans.dao.GiTransaction;
import com.tsi.workflow.audit.dao.ApiActionsDAO;
import com.tsi.workflow.audit.dao.GiTransactionDAO;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.audit.uiform.TransactionInputParamForm;
import com.tsi.workflow.audit.uiform.TransactionResponseForm;
import com.tsi.workflow.audit.utils.Constants;
import com.tsi.workflow.cache.CacheClient;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Radha.Adhimoolam
 */
@Service
public class GiService {

    private static final Logger LOG = Logger.getLogger(GiService.class.getName());

    @Autowired
    GiTransactionDAO giTransactionDAO;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Autowired
    ApiActionsDAO apiActionsDAO;

    public GiTransactionDAO getGiTransactionDAO() {
	return giTransactionDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public AuditCommonHelper getAuditCommonHelper() {
	return auditCommonHelper;
    }

    public ApiActionsDAO getApiActionsDAO() {
	return apiActionsDAO;
    }

    public List<TransactionResponseForm> getGITransactionForSystemView(TransactionInputParamForm searchForm) {
	List<TransactionResponseForm> lReturnData = new ArrayList();

	List<GiTransaction> giTranx = new ArrayList();
	giTranx = getGiTransactionDAO().getGiTransactionInfo(searchForm.getStartDate(), searchForm.getEndDate(), searchForm.getPlanId(), searchForm.getUserName(), searchForm.getUserAction());
	List<GiTransaction> giCacheTranx = getCacheTransactionForSystemView(searchForm);
	if (!giCacheTranx.isEmpty()) {
	    giTranx.addAll(giCacheTranx);
	}
	giTranx.stream().forEach(tranx -> {
	    TransactionResponseForm lResponse = new TransactionResponseForm();
	    lResponse.setStartDateTime(tranx.getStartedDt());
	    lResponse.setEndDateTime(tranx.getEndDt());
	    lResponse.setPlanId(tranx.getPlanId());
	    lResponse.setImpId(tranx.getImplId());
	    lResponse.setUserName(tranx.getUserName());
	    lResponse.setUserAction(tranx.getAction());
	    lResponse.setUserRole(tranx.getUserRole());
	    lResponse.setTdx(tranx.getTargetSystem());
	    lResponse.setResponseTimeMs(!getAuditCommonHelper().isObjectNullAndEmpty(tranx.getResponseTime()) ? tranx.getResponseTime() : (long) 0);
	    lResponse.setResponseTimeSec(!getAuditCommonHelper().isObjectNullAndEmpty(tranx.getResponseTime()) ? Constants.decimalFormat.format((double) tranx.getResponseTime() / 1000) : "");
	    lResponse.setResponseTimeMin(!getAuditCommonHelper().isObjectNullAndEmpty(tranx.getResponseTime()) ? Constants.decimalFormat.format((double) tranx.getResponseTime() / 1000 / 60) : "");
	    lResponse.setzOs("No");
	    lReturnData.add(lResponse);
	});
	return lReturnData;
    }

    public List<GiTransaction> getCacheTransactionForSystemView(TransactionInputParamForm searchForm) {
	List<GiTransaction> giTranx = new ArrayList();
	giTranx = getCacheClient().getAuditGiTransactionMap().values().stream().filter(t -> t.getStartedDt().after(searchForm.getStartDate()) && t.getStartedDt().before(searchForm.getEndDate())).collect(Collectors.toList());
	if (giTranx != null && !giTranx.isEmpty()) {
	    if (searchForm.getUserName() != null && !searchForm.getUserName().isEmpty()) {
		giTranx = giTranx.stream().filter(t -> searchForm.getUserName().contains(t.getUserId())).collect(Collectors.toList());
	    }

	    if (!giTranx.isEmpty() && searchForm.getPlanId() != null && !searchForm.getPlanId().isEmpty()) {
		giTranx = giTranx.stream().filter(t -> t.getPlanId() != null && !t.getPlanId().isEmpty() && t.getPlanId().equalsIgnoreCase(searchForm.getPlanId())).collect(Collectors.toList());
	    }
	    if (!giTranx.isEmpty() && searchForm.getUserAction() != null && !searchForm.getUserAction().isEmpty()) {
		List<Integer> actionIds = getApiActionsDAO().findByActionUrl(searchForm.getUserAction());
		if (actionIds == null || actionIds.isEmpty()) {
		    giTranx = new ArrayList();
		} else {
		    giTranx = giTranx.stream().filter(t -> t.getActionsId() != null && actionIds.contains(t.getActionsId().getId())).collect(Collectors.toList());
		}
	    }

	} else {
	    giTranx = new ArrayList();
	}
	return giTranx;
    }

}
