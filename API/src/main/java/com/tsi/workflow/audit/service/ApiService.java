/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.service;

import com.tsi.workflow.audit.beans.dao.ApiTransaction;
import com.tsi.workflow.audit.beans.dao.PlanDetails;
import com.tsi.workflow.audit.dao.ApiTransactionDAO;
import com.tsi.workflow.audit.dao.PlanDetailsDAO;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.audit.uiform.TransactionForm;
import com.tsi.workflow.audit.uiform.TransactionInfoForm;
import com.tsi.workflow.audit.uiform.TransactionInputParamForm;
import com.tsi.workflow.audit.uiform.TransactionResponseForm;
import com.tsi.workflow.audit.uiform.TransactionViewBean;
import com.tsi.workflow.audit.uiform.TransactionViewForm;
import com.tsi.workflow.audit.uiform.TransactionViewResponseForm;
import com.tsi.workflow.audit.utils.Constants;
import com.tsi.workflow.beans.dao.PlanPerformanceViewDAO;
import com.tsi.workflow.beans.ui.PlanPerformanceView;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Radha.Adhimoolam
 */
@Service
public class ApiService {

    private static final Logger LOG = Logger.getLogger(ApiService.class.getName());

    @Autowired
    ApiTransactionDAO apiTransactionDAO;

    @Autowired
    PlanPerformanceViewDAO planPerfViewDAO;

    @Autowired
    PlanDetailsDAO planDetailsDAO;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Autowired
    GiService giService;

    public ApiTransactionDAO getApiTransactionDAO() {
	return apiTransactionDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public PlanPerformanceViewDAO getPlanPerfViewDAO() {
	return planPerfViewDAO;
    }

    public PlanDetailsDAO getPlanDetailsDAO() {
	return planDetailsDAO;
    }

    public AuditCommonHelper getAuditCommonHelper() {
	return auditCommonHelper;
    }

    public GiService getGiService() {
	return giService;
    }

    @Transactional
    public JSONResponse saveTransaction(TransactionInfoForm tranxInfo) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    ApiTransaction lApiTranx = tranxInfo.getApiTransaction();
	    getApiTransactionDAO().save(lApiTranx);
	    List<PlanPerformanceView> lPlanView = tranxInfo.getPlanInfo();
	    if (lPlanView != null && !lPlanView.isEmpty()) {
		for (PlanPerformanceView lPlanInfo : lPlanView) {
		    PlanDetails lPlanDetails = new PlanDetails();
		    BeanUtils.copyProperties(lPlanDetails, lPlanInfo);
		    lPlanDetails.setTranxId(lApiTranx);
		    getPlanDetailsDAO().save(lPlanDetails);
		}
	    }
	} catch (Exception ex) {
	    LOG.info("Exception occurs in Saving the Transaction ", ex);
	    throw new WorkflowException("Errors occurs on saving the Transaction ", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse getTransactionInfo(TransactionForm transactionInfo) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    String hostProfile = transactionInfo.getInputParam().getHostName();
	    Date startDate = transactionInfo.getInputParam().getStartDate();
	    Date endDate = transactionInfo.getInputParam().getEndDate();
	    List<String> userId = transactionInfo.getInputParam().getUserName() != null && !transactionInfo.getInputParam().getUserName().isEmpty() ? transactionInfo.getInputParam().getUserName() : new ArrayList<>();
	    String planId = transactionInfo.getInputParam().getPlanId() != null && !transactionInfo.getInputParam().getPlanId().isEmpty() ? transactionInfo.getInputParam().getPlanId() : "";
	    List<String> userAction = transactionInfo.getInputParam().getUserAction() != null && !transactionInfo.getInputParam().getUserAction().isEmpty() ? transactionInfo.getInputParam().getUserAction() : new ArrayList<>();
	    if (getAuditCommonHelper().isObjectNullAndEmpty(hostProfile) || getAuditCommonHelper().isObjectNullAndEmpty(startDate) || getAuditCommonHelper().isObjectNullAndEmpty(endDate)) {
		throw new WorkflowException("Host Name/Start Date/End Date parameters has invalid data, please do verify");
	    }

	    List<ApiTransaction> lApiTransactions = new ArrayList();
	    lApiTransactions = getApiTransactionDAO().getApiTransactionInfo(hostProfile, userId, planId, startDate, endDate, userAction);
	    lApiTransactions.addAll(getCacheDeatils(transactionInfo.getInputParam()));

	    // Sort by start date and time
	    lApiTransactions.stream().sorted((o1, o2) -> o1.getStartedDt().compareTo(o2.getStartedDt()));

	    List<TransactionResponseForm> lResponseForm = new ArrayList();
	    for (ApiTransaction lApiTransaction : lApiTransactions) {
		TransactionResponseForm lResponse = new TransactionResponseForm();
		lResponse.setStartDateTime(lApiTransaction.getStartedDt());
		lResponse.setEndDateTime(lApiTransaction.getEndDt());
		lResponse.setPlanId(lApiTransaction.getPlanId());
		lResponse.setImpId(lApiTransaction.getImplId());
		lResponse.setUserName(lApiTransaction.getUserName());
		lResponse.setUserAction(lApiTransaction.getActionsId().getActionName());
		lResponse.setUserRole(lApiTransaction.getUserRole());
		lResponse.setResponseTimeMs(!getAuditCommonHelper().isObjectNullAndEmpty(lApiTransaction.getResponseTime()) ? lApiTransaction.getResponseTime() : (long) 0);
		lResponse.setResponseTimeSec(!getAuditCommonHelper().isObjectNullAndEmpty(lApiTransaction.getResponseTime()) ? Constants.decimalFormat.format((double) lApiTransaction.getResponseTime() / 1000) : "");
		lResponse.setResponseTimeMin(!getAuditCommonHelper().isObjectNullAndEmpty(lApiTransaction.getResponseTime()) ? Constants.decimalFormat.format((double) lApiTransaction.getResponseTime() / 1000 / 60) : "");
		lResponse.setHostName(lApiTransaction.getId() != null ? lApiTransaction.getLnxServerId() != null ? lApiTransaction.getLnxServerId().getDnsName() : "" : null);
		lResponse.setzOs("No");
		if (lApiTransaction.getPlanDetailsList() != null) {
		    lResponse.setTdx(lApiTransaction.getPlanDetailsList().stream().map(t -> t.getTdx()).distinct().collect(Collectors.joining(",")));
		}
		lResponseForm.add(lResponse);
	    }
	    lResponseForm.addAll(getGiService().getGITransactionForSystemView(transactionInfo.getInputParam()));
	    transactionInfo.setResponseParam(lResponseForm.stream().sorted(Comparator.comparing(TransactionResponseForm::getStartDateTime)).collect(Collectors.toList()));

	    if (lResponseForm == null || lResponseForm.isEmpty()) {
		lReturn.setStatus(Boolean.FALSE);
		lReturn.setErrorMessage("No Transaction Found");
		return lReturn;
	    }

	    lReturn.setData(transactionInfo);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to get Transaction Information", ex);
	    throw new WorkflowException("Unable to get Transaction Information");
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    private List<ApiTransaction> getCacheDeatils(TransactionInputParamForm searchForm) {

	List<ApiTransaction> cacheTranxList = new ArrayList<>();

	if (!getCacheClient().getAuditApiTransactioMap().isEmpty()) {
	    cacheTranxList = !getCacheClient().getAuditApiTransactioMap().values().stream().filter(t -> t.getLnxServerId().getHostProfile().equals(searchForm.getHostName())).filter(t -> t.getStartedDt().after(searchForm.getStartDate())).filter(t -> t.getStartedDt().before(searchForm.getEndDate())).collect(Collectors.toList()).isEmpty()
		    ? cacheClient.getAuditApiTransactioMap().values().stream().filter(t -> t.getStartedDt().after(searchForm.getStartDate())).filter(t -> t.getStartedDt().before(searchForm.getEndDate())).collect(Collectors.toList())
		    : new ArrayList<>();
	}
	if (!cacheTranxList.isEmpty()) {
	    if (searchForm.getUserName() != null && !searchForm.getUserName().isEmpty()) {
		cacheTranxList = !cacheTranxList.stream().filter(t -> t.getUserId() != null && !t.getUserId().isEmpty()).filter(t -> searchForm.getUserName().contains(t.getUserId())).collect(Collectors.toList()).isEmpty() ? cacheTranxList.stream().filter(t -> searchForm.getUserName().contains(t.getUserId())).collect(Collectors.toList()) : new ArrayList<>();
	    }
	    if (searchForm.getUserAction() != null && !searchForm.getUserAction().isEmpty()) {
		cacheTranxList = !cacheTranxList.stream().filter(t -> t.getActionsId().getActionName() != null && !t.getActionsId().getActionName().isEmpty()).filter(t -> searchForm.getUserAction().contains(t.getActionsId().getActionName())).collect(Collectors.toList()).isEmpty() ? cacheTranxList.stream().filter(t -> searchForm.getUserAction().contains(t.getActionsId().getActionName())).collect(Collectors.toList()) : new ArrayList<>();
	    }
	    if (searchForm.getPlanId() != null && !searchForm.getPlanId().isEmpty()) {
		cacheTranxList = !cacheTranxList.stream().filter(t -> t.getPlanId() != null && !t.getPlanId().isEmpty()).filter(t -> searchForm.getPlanId().contains(t.getPlanId())).collect(Collectors.toList()).isEmpty() ? cacheTranxList.stream().filter(t -> searchForm.getPlanId().contains(t.getPlanId())).collect(Collectors.toList()) : new ArrayList<>();
	    }
	}

	return cacheTranxList;

    }

    @Transactional
    public JSONResponse getTransactionViewInfo(TransactionViewForm transactionInfo) {
	JSONResponse lReturn = new JSONResponse();
	Map<String, TransactionViewResponseForm> lResponseMap = new HashMap<>();
	try {
	    String hostProfile = transactionInfo.getInputParam().getHostName();
	    Date startDate = transactionInfo.getInputParam().getStartDate();
	    Date endDate = transactionInfo.getInputParam().getEndDate();
	    List<String> userAction = transactionInfo.getInputParam().getUserAction() != null && !transactionInfo.getInputParam().getUserAction().isEmpty() ? transactionInfo.getInputParam().getUserAction() : new ArrayList<>();
	    if (getAuditCommonHelper().isObjectNullAndEmpty(hostProfile) || getAuditCommonHelper().isObjectNullAndEmpty(startDate) || getAuditCommonHelper().isObjectNullAndEmpty(endDate)) {
		lReturn.setStatus(Boolean.FALSE);
		lReturn.setErrorMessage("Error in HostProfile");
		// return lReturn;
	    }

	    List<TransactionViewBean> lApiTransactions = new ArrayList();
	    lApiTransactions = getApiTransactionDAO().getTransactionViewInfo(hostProfile, startDate, endDate, userAction);
	    lApiTransactions.addAll(getCacheDeatilsForTxnView(transactionInfo.getInputParam()));

	    if (getAuditCommonHelper().isObjectNullAndEmpty(lApiTransactions)) {
		lReturn.setStatus(Boolean.FALSE);
		lReturn.setErrorMessage("No Transaction Found");
		// return lReturn;
	    }

	    lApiTransactions.forEach((t) -> {

		String key = t.getPlanid() + "_" + t.getStartdate();
		if (!lResponseMap.containsKey(key)) {
		    TransactionViewResponseForm transactionViewResponseForm = new TransactionViewResponseForm();
		    transactionViewResponseForm.setStartDateTime(t.getStartdate());
		    transactionViewResponseForm.setEndDateTime(t.getEnddate());
		    transactionViewResponseForm.setPlanId(t.getPlanid());
		    transactionViewResponseForm.setImpId(t.getImplid());
		    transactionViewResponseForm.setUserName(t.getInitiatedby());
		    transactionViewResponseForm.setUserRole(t.getUserrole());
		    transactionViewResponseForm.setUserAction(t.getUseraction());
		    transactionViewResponseForm.setInitiatedBy(t.getInitiatedby());
		    transactionViewResponseForm.setAsmcount(Integer.parseInt(t.getAsmcount().toString()));
		    transactionViewResponseForm.setSbtcount(Integer.parseInt(t.getSbtcount().toString()));
		    transactionViewResponseForm.setCcppcount(Integer.parseInt(t.getCcppcount().toString()));
		    transactionViewResponseForm.setHeadercount(Integer.parseInt(t.getHeadercount().toString()));
		    transactionViewResponseForm.setMakcount(Integer.parseInt(t.getMakcount().toString()));

		    transactionViewResponseForm.setRepocount(Integer.parseInt(t.getRepocount().toString()));
		    transactionViewResponseForm.setReponamelist(t.getReponamelist());
		    transactionViewResponseForm.setResponseTimeMs(Long.parseLong(t.getResponsetime().toString()));
		    transactionViewResponseForm.setResponseTimeSec((double) Long.parseLong(t.getResponsetime().toString()) / 1000);
		    transactionViewResponseForm.setResponseTimeMin(Constants.decimalFormat.format((double) Long.parseLong(t.getResponsetime().toString()) / 1000 / 60));
		    transactionViewResponseForm.setHostName(t.getHostname());
		    transactionViewResponseForm.setTdx(t.getTdx());
		    transactionViewResponseForm.setzOs("No");

		    Map<String, Integer> countBySystem = new HashMap<>();
		    countBySystem.put(t.getTargetsystem(), Integer.parseInt(t.getTotalcount().toString()));
		    transactionViewResponseForm.setTotalCountBySystem(countBySystem);

		    Map<String, Integer> soCountBySystem = new HashMap<>();
		    soCountBySystem.put(t.getTargetsystem(), Integer.parseInt(t.getSocount() != null ? t.getSocount().toString() : "0"));
		    transactionViewResponseForm.setSocount(soCountBySystem);

		    lResponseMap.put(key, transactionViewResponseForm);
		} else {
		    TransactionViewResponseForm lTransactionViewResponseForm = lResponseMap.get(key);
		    Map<String, Integer> lcountBySystem = lTransactionViewResponseForm.getTotalCountBySystem();
		    if (!lcountBySystem.containsKey(t.getTargetsystem())) {
			lcountBySystem.put(t.getTargetsystem(), Integer.parseInt(t.getTotalcount().toString()));
		    }
		    lTransactionViewResponseForm.setTotalCountBySystem(lcountBySystem);

		    Map<String, Integer> lSocountBySystem = lTransactionViewResponseForm.getSocount();
		    if (!lSocountBySystem.containsKey(t.getTargetsystem())) {
			lSocountBySystem.put(t.getTargetsystem(), Integer.parseInt(t.getSocount() != null ? t.getSocount().toString() : "0"));
		    }
		    lTransactionViewResponseForm.setSocount(lSocountBySystem);

		}

	    });

	    lReturn.setData(lResponseMap.values());

	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to get Transaction Information", ex);
	    throw new WorkflowException("Unable to get Transaction Information");
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    private List<TransactionViewBean> getCacheDeatilsForTxnView(TransactionInputParamForm searchForm) {

	List<TransactionViewBean> cacheTranxList = new ArrayList<>();
	//
	// if (!getCacheClient().getAuditApiTransactioMap().isEmpty()) {
	// cacheTranxList =
	// !getCacheClient().getAuditApiTransactioMap().values().stream().filter(t ->
	// t.getStartedDt().after(searchForm.getStartDate())).filter(t ->
	// t.getStartedDt().before(searchForm.getEndDate())).collect(Collectors.toList()).isEmpty()
	// ? cacheClient.getAuditApiTransactioMap().values().stream().filter(t ->
	// t.getStartedDt().after(searchForm.getStartDate())).filter(t ->
	// t.getStartedDt().before(searchForm.getEndDate())).collect(Collectors.toList())
	// : new ArrayList<>();
	// }
	// if (!cacheTranxList.isEmpty()) {
	// if (searchForm.getUserName() != null && !searchForm.getUserName().isEmpty())
	// {
	// cacheTranxList = !cacheTranxList.stream().filter(t -> t.getUserId() != null
	// && !t.getUserId().isEmpty()).filter(t ->
	// searchForm.getUserName().contains(t.getUserId())).collect(Collectors.toList()).isEmpty()
	// ? cacheTranxList.stream().filter(t ->
	// searchForm.getUserName().contains(t.getUserId())).collect(Collectors.toList())
	// : new ArrayList<>();
	// }
	// if (searchForm.getUserAction() != null &&
	// !searchForm.getUserAction().isEmpty()) {
	// cacheTranxList = !cacheTranxList.stream().filter(t ->
	// t.getActionsId().getActionName() != null &&
	// !t.getActionsId().getActionName().isEmpty()).filter(t ->
	// searchForm.getUserAction().contains(t.getActionsId().getActionName())).collect(Collectors.toList()).isEmpty()
	// ? cacheTranxList.stream().filter(t ->
	// searchForm.getUserAction().contains(t.getActionsId().getActionName())).collect(Collectors.toList())
	// : new ArrayList<>();
	// }
	// if (searchForm.getPlanId() != null && !searchForm.getPlanId().isEmpty()) {
	// cacheTranxList = !cacheTranxList.stream().filter(t -> t.getPlanId() != null
	// && !t.getPlanId().isEmpty()).filter(t ->
	// searchForm.getPlanId().contains(t.getPlanId())).collect(Collectors.toList()).isEmpty()
	// ? cacheTranxList.stream().filter(t ->
	// searchForm.getPlanId().contains(t.getPlanId())).collect(Collectors.toList())
	// : new ArrayList<>();
	// }
	// }

	return cacheTranxList;

    }
}
