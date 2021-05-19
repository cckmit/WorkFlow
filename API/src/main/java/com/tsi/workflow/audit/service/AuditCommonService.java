/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.service;

import com.tsi.workflow.User;
import com.tsi.workflow.audit.beans.dao.ApiActions;
import com.tsi.workflow.audit.beans.dao.GiTransaction;
import com.tsi.workflow.audit.config.AuditConfig;
import com.tsi.workflow.audit.dao.ApiActionsDAO;
import com.tsi.workflow.audit.dao.ApiTransactionDAO;
import com.tsi.workflow.audit.dao.GiTransactionDAO;
import com.tsi.workflow.audit.helper.AuditCommonHelper;
import com.tsi.workflow.audit.report.SysViewReportCreator;
import com.tsi.workflow.audit.report.TxnViewReportCreator;
import com.tsi.workflow.audit.uiform.GiTransactionForm;
import com.tsi.workflow.audit.uiform.TransactionForm;
import com.tsi.workflow.audit.uiform.TransactionInputParamForm;
import com.tsi.workflow.audit.uiform.TransactionResponseForm;
import com.tsi.workflow.audit.uiform.TransactionViewForm;
import com.tsi.workflow.audit.uiform.TransactionViewResponseForm;
import com.tsi.workflow.audit.utils.Constants;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.cache.CacheClient;
import com.tsi.workflow.dao.SystemDAO;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.JSONResponse;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class AuditCommonService {

    private static final Logger LOG = Logger.getLogger(AuditCommonService.class.getName());

    @Autowired
    GiTransactionDAO giTransactionDAO;

    @Autowired
    LDAPAuthenticatorImpl ldapInfo;

    @Autowired
    ApiTransactionDAO apiTransactionDAO;

    @Autowired
    AuditConfig auditConfig;

    @Autowired
    ApiService apiService;

    @Autowired
    CacheClient cacheClient;

    @Autowired
    ApiActionsDAO apiActionsDAO;

    @Autowired
    AuditCommonHelper auditCommonHelper;

    @Autowired
    SystemDAO systemDAO;

    public ApiService getApiService() {
	return apiService;
    }

    public ApiTransactionDAO getApiTransactionDAO() {
	return apiTransactionDAO;
    }

    public void setApiTransactionDAO(ApiTransactionDAO apiTransactionDAO) {
	this.apiTransactionDAO = apiTransactionDAO;
    }

    public LDAPAuthenticatorImpl getLdapInfo() {
	return ldapInfo;
    }

    public GiTransactionDAO getGiTransactionDAO() {
	return giTransactionDAO;
    }

    public CacheClient getCacheClient() {
	return cacheClient;
    }

    public ApiActionsDAO getApiActionsDAO() {
	return apiActionsDAO;
    }

    public AuditConfig getAuditConfig() {
	return auditConfig;
    }

    public AuditCommonHelper getAuditCommonHelper() {
	return auditCommonHelper;
    }

    public SystemDAO getSystemDAO() {
	return systemDAO;
    }

    public JSONResponse updateAuditSettings(Boolean systemInfo, Boolean transactionInfo) {
	JSONResponse lReturn = new JSONResponse();
	getCacheClient().getAuditConfig().put(Constants.AuditConfiguration.SystemView.name(), systemInfo);
	getCacheClient().getAuditConfig().put(Constants.AuditConfiguration.TransactionView.name(), transactionInfo);

	if (!systemInfo) {
	    getCacheClient().getAuditGiTransactionMap().clear();
	    getCacheClient().getAuditApiTransactioMap().clear();
	}

	JSONResponse lResponse = getAuditSettings();
	lReturn.setStatus(Boolean.TRUE);
	lReturn.setData(getAuditCommonHelper().getAuditSystem() + "" + getAuditCommonHelper().getAuditTransaction());
	return lReturn;
    }

    public JSONResponse getAuditSettings() {
	JSONResponse lReturn = new JSONResponse();
	Map<String, Boolean> lReturnMap = new HashMap();
	lReturnMap.put(Constants.AuditConfiguration.SystemView.name(), getAuditCommonHelper().getAuditSystem());
	lReturnMap.put(Constants.AuditConfiguration.TransactionView.name(), getAuditCommonHelper().getAuditTransaction());
	lReturn.setStatus(Boolean.TRUE);
	lReturn.setData(lReturnMap);
	return lReturn;
    }

    @Transactional
    public JSONResponse saveTransaction(GiTransactionForm giTransactionForm) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    GiTransaction giTranx = new GiTransaction();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    String cacheKey = giTransactionForm.getUserId() + "|" + giTransactionForm.getStartDateTime() + "|" + giTransactionForm.getAction();
	    BeanUtils.copyProperties(giTranx, giTransactionForm);
	    LOG.info("Started Date Time " + giTransactionForm.getStartDateTime());
	    LOG.info("End Date Time " + giTransactionForm.getEndDateTime());
	    LOG.info("Action " + giTransactionForm.getAction());
	    if (giTransactionForm.getEndDateTime() == null || giTransactionForm.getEndDateTime().isEmpty()) {
		User lUser = getLdapInfo().getUserDetails(giTransactionForm.getUserId());
		if (lUser.getId() == null) {
		    throw new WorkflowException("Error: Unable to find the user information " + lUser.getId());
		}
		giTranx.setActionsId(getApiActions(giTransactionForm.getAction()));
		giTranx.setUserId(lUser.getId());
		giTranx.setUserName(lUser.getDisplayName());
		giTranx.setUserRole(lUser.getCurrentRole());
		giTranx.setStartedDt(dateFormat.parse(giTransactionForm.getStartDateTime()));
		getCacheClient().getAuditGiTransactionMap().put(cacheKey, giTranx);
	    } else {
		giTranx = getCacheClient().getAuditGiTransactionMap().get(cacheKey);
		if (giTranx == null) {
		    throw new WorkflowException("Previous Transaction was not saved in Workflow");
		}
		giTranx.setEndDt(dateFormat.parse(giTransactionForm.getEndDateTime()));
		giTranx.setResponseTime(dateFormat.parse(giTransactionForm.getEndDateTime()).getTime() - giTranx.getStartedDt().getTime());
		System lSystem = getSystemDAO().findByName(giTranx.getTargetSystem());
		if (lSystem != null) {
		    giTranx.setTargetSystem(lSystem.getAliasName());
		}
		getGiTransactionDAO().save(giTranx);
		getCacheClient().getAuditGiTransactionMap().remove(cacheKey);
	    }
	    lReturn.setData(giTransactionForm);
	} catch (WorkflowException ex) {
	    throw ex;
	} catch (Exception ex) {
	    LOG.info("Unable to save GI/Shell Script Transaction");
	    throw new WorkflowException("Unable to save GI/Shell Script Transaction");
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    private ApiActions getApiActions(String action) {
	// Check the action, available in api_actions. If not and add new entry
	ApiActions apiAction = new ApiActions();
	if (getCacheClient().getAuditApiActionsMap().containsKey(action)) {
	    apiAction = getCacheClient().getAuditApiActionsMap().get(action);
	} else {
	    apiAction = new ApiActions();
	    apiAction.setActionUrl(action);
	    apiAction.setActionName(action);
	    apiAction.setActive("Y");
	    apiAction.setInfoLevel("NONE");
	    apiAction.setIsSchedular(Boolean.FALSE);
	    getApiActionsDAO().save(apiAction);
	    getCacheClient().getAuditApiActionsMap().put(apiAction.getActionUrl(), apiAction);
	}

	return apiAction;
    }

    @Transactional
    public JSONResponse getTransaction() {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(Boolean.TRUE);
	GiTransaction lForm = new GiTransaction();
	lReturn.setData(lForm);
	return lReturn;
    }

    @Transactional
    public JSONResponse extractSysViewReport(TransactionForm searchFormparent) {

	TransactionInputParamForm searchForm = searchFormparent.getInputParam();
	JSONResponse lResponse = new JSONResponse();
	try {
	    SysViewReportCreator sysViewReportCreator = new SysViewReportCreator();
	    List<TransactionResponseForm> lResponseForm = searchFormparent.getResponseParam();

	    if (lResponseForm != null && !lResponseForm.isEmpty()) {
		sysViewReportCreator.addSystemViewDetails(lResponseForm, searchForm.getHostName(), searchForm.getUserName(), searchForm.getPlanId(), searchForm.getStartDate(), searchForm.getEndDate(), searchForm.getUserAction());

		ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
		sysViewReportCreator.getExcelAttachmentCreator().write(lExcelStream);
		sysViewReportCreator.getWorkBook().close();
		lResponse.setData(lExcelStream.toByteArray());
		lExcelStream.close();
		lResponse.setMetaData("application/vnd.ms-excel");
		lResponse.setStatus(Boolean.TRUE);
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("No Data from UI");
	    }
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lResponse.setErrorMessage("Error in Downloading Report");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }

    public JSONResponse getApiActions(String actionUrl, Integer limit, Integer offset) {
	JSONResponse lReturn = new JSONResponse();
	List<ApiActions> lReturnList = new ArrayList();
	int startRecord = (offset * limit);
	int endRecord = (offset * limit) + limit;
	if (actionUrl != null && !actionUrl.isEmpty()) {
	    lReturnList = getCacheClient().getAuditApiActionsMap().values().stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId())).filter(t -> t.getActionUrl().toLowerCase().contains(actionUrl.toLowerCase())).collect(Collectors.toList());
	    lReturn.setCount(lReturnList.size());
	} else {
	    lReturnList = getCacheClient().getAuditApiActionsMap().values().stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId())).collect(Collectors.toList());
	    lReturn.setCount(getCacheClient().getAuditApiActionsMap().values().size());
	}
	lReturn.setData(lReturnList.stream().sorted((o1, o2) -> o1.getId().compareTo(o2.getId())).skip(startRecord).limit(endRecord).collect(Collectors.toList()));
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse saveOrUpdateApiActions(ApiActions actions) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    if (actions.getId() == null) {
		if (getCacheClient().getAuditApiActionsMap().containsKey(actions.getActionUrl())) {
		    lReturn.setStatus(Boolean.FALSE);
		    lReturn.setErrorMessage("URL " + actions.getActionUrl() + " is already exists");
		    return lReturn;
		}
		actions.setActive("Y");
		getApiActionsDAO().save(actions);
	    } else {
		actions.setActive("Y");
		getApiActionsDAO().update(actions);
	    }
	    getCacheClient().getAuditApiActionsMap().put(actions.getActionUrl(), actions);
	    lReturn.setData(getCacheClient().getAuditApiActionsMap().get(actions.getActionUrl()));
	} catch (Exception ex) {
	    LOG.info("Unable to save/Update APIActions", ex);
	    throw new WorkflowException("Unable to save/ApiActions", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse removeApiActions(ApiActions actions) {
	JSONResponse lReturn = new JSONResponse();
	try {
	    if (actions.getId() != null) {
		actions.setActive("N");
		getApiActionsDAO().update(actions);
		getCacheClient().getAuditApiActionsMap().remove(actions.getActionUrl());
	    }

	} catch (Exception ex) {
	    LOG.info("Unable to Delete APIActions", ex);
	    throw new WorkflowException("Unable to Delete", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;

    }

    @Transactional
    public JSONResponse getLinuxServers() {
	JSONResponse lReturn = new JSONResponse();
	try {
	    lReturn.setData(getCacheClient().getAuditLinuxServersMap().values());
	} catch (Exception ex) {
	    LOG.info("Unable to get the Linux servers", ex);
	    throw new WorkflowException("Unable to get the Linux Servers information", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse getLinuxServersProfiles() {
	JSONResponse lReturn = new JSONResponse();
	try {
	    lReturn.setData(getCacheClient().getAuditLinuxServersMap().values().stream().map(t -> t.getHostProfile()).collect(Collectors.toSet()));
	} catch (Exception ex) {
	    LOG.info("Unable to get the Linux servers", ex);
	    throw new WorkflowException("Unable to get the Linux Servers information", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse getActionNameList() {
	JSONResponse lReturn = new JSONResponse();
	try {
	    lReturn.setData(getCacheClient().getAuditApiActionsMap().values().stream().filter(t -> t.getActionName() != null).map(t -> t.getActionName()).sorted().collect(Collectors.toSet()));
	} catch (Exception ex) {
	    LOG.info("Unable to get the Action List", ex);
	    throw new WorkflowException("Unable to get the Action List", ex);
	}
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @Transactional
    public JSONResponse extractTxnViewReport(TransactionViewForm searchFormparent) {
	TransactionInputParamForm searchForm = searchFormparent.getInputParam();
	JSONResponse lResponse = new JSONResponse();
	try {
	    TxnViewReportCreator txnViewReportCreator = new TxnViewReportCreator();
	    List<TransactionViewResponseForm> lResponseForm = searchFormparent.getResponseParam();
	    if (lResponseForm != null && !lResponseForm.isEmpty()) {
		txnViewReportCreator.addSystemViewDetails(lResponseForm, searchForm.getHostName(), searchForm.getUserName(), searchForm.getPlanId(), searchForm.getStartDate(), searchForm.getEndDate(), searchForm.getUserAction());

		ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
		txnViewReportCreator.getExcelAttachmentCreator().write(lExcelStream);
		txnViewReportCreator.getWorkBook().close();
		lResponse.setData(lExcelStream.toByteArray());
		lExcelStream.close();
		lResponse.setMetaData("application/vnd.ms-excel");
		lResponse.setStatus(Boolean.TRUE);
	    } else {
		lResponse.setStatus(Boolean.FALSE);
		lResponse.setErrorMessage("No Data from UI");
	    }
	} catch (Exception ex) {
	    LOG.error("Error in Excel Creation ", ex);
	    lResponse.setErrorMessage("Error in Downloading Report");
	    lResponse.setStatus(Boolean.FALSE);
	}
	return lResponse;
    }

    public JSONResponse clearAuditCache(String type) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(Boolean.TRUE);
	switch (type) {
	case "GI":
	    getCacheClient().getAuditGiTransactionMap().clear();
	    break;
	case "API":
	    getCacheClient().getAuditApiTransactioMap().clear();
	    break;
	default:
	    LOG.info("Audit Cache not found" + type);
	}

	return lReturn;
    }

    public JSONResponse getAuditCacheData(String type) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(Boolean.TRUE);
	switch (type) {
	case "GI":
	    lReturn.setData(getCacheClient().getAuditGiTransactionMap());
	    lReturn.setCount(getCacheClient().getAuditGiTransactionMap().size());
	    break;
	case "API":
	    lReturn.setData(getCacheClient().getAuditApiTransactioMap());
	    lReturn.setCount(getCacheClient().getAuditApiTransactioMap().size());
	    break;
	default:
	    LOG.info("Audit Cache not found" + type);
	}

	return lReturn;
    }
}
