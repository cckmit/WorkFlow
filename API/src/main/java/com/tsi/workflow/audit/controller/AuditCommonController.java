/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.controller;

import com.tsi.workflow.audit.beans.dao.ApiActions;
import com.tsi.workflow.audit.service.AuditCommonService;
import com.tsi.workflow.audit.uiform.GiTransactionForm;
import com.tsi.workflow.audit.uiform.TransactionForm;
import com.tsi.workflow.audit.uiform.TransactionViewForm;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.utils.JSONResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Radha.Adhimoolam
 */
@Controller
@RequestMapping("/audit/common")
public class AuditCommonController extends BaseController {

    private static final Logger LOG = Logger.getLogger(AuditCommonController.class.getName());

    @Autowired
    AuditCommonService auditCommonService;

    public AuditCommonService getAuditCommonService() {
	return auditCommonService;
    }

    @RequestMapping(value = "/updateAuditSettings", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse updateAuditSettings(HttpServletRequest request, HttpServletResponse response, @RequestParam boolean systemInfo, @RequestParam(required = false) boolean transactionInfo) {
	return getAuditCommonService().updateAuditSettings(systemInfo, transactionInfo);
    }

    @RequestMapping(value = "/getAuditSettings", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAuditSettings(HttpServletRequest request, HttpServletResponse response) {
	return getAuditCommonService().getAuditSettings();
    }

    @RequestMapping(value = "/saveTransaction", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveTransaction(HttpServletRequest request, HttpServletResponse response, @RequestBody GiTransactionForm giTransaction) {
	return getAuditCommonService().saveTransaction(giTransaction);
    }

    @RequestMapping(value = "/getTransaction", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTransaction(HttpServletRequest request, HttpServletResponse response) {
	return getAuditCommonService().getTransaction();
    }

    @RequestMapping(value = "/extractSysViewReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse extractSysViewReport(HttpServletRequest request, HttpServletResponse response, @RequestBody TransactionForm searchForm) {
	return getAuditCommonService().extractSysViewReport(searchForm);
    }

    @RequestMapping(value = "/extractTxnViewReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse extractTxnViewReport(HttpServletRequest request, HttpServletResponse response, @RequestBody TransactionViewForm searchForm) {
	return getAuditCommonService().extractTxnViewReport(searchForm);
    }

    @RequestMapping(value = "/getApiActions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getApiActions(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String filter, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) {
	return getAuditCommonService().getApiActions(filter, limit, offset);
    }

    @RequestMapping(value = "/saveApiActions", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveApiActions(HttpServletRequest request, HttpServletResponse response, @RequestBody ApiActions apiActions) {
	return getAuditCommonService().saveOrUpdateApiActions(apiActions);
    }

    @RequestMapping(value = "/removeApiActions", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse removeApiActions(HttpServletRequest request, HttpServletResponse response, @RequestBody ApiActions apiActions) {
	return getAuditCommonService().removeApiActions(apiActions);
    }

    @RequestMapping(value = "/getLinuxServers", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLinuxServers(HttpServletRequest request, HttpServletResponse response) {
	return getAuditCommonService().getLinuxServers();
    }

    @RequestMapping(value = "/getLinuxServersProfile", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLinuxServersProfile(HttpServletRequest request, HttpServletResponse response) {
	return getAuditCommonService().getLinuxServersProfiles();
    }

    @RequestMapping(value = "/getActionNameList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActionNameList(HttpServletRequest request, HttpServletResponse response) {
	return getAuditCommonService().getActionNameList();
    }

    @RequestMapping(value = "/clearAuditCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse clearAuditCache(@RequestParam String cacheType) {
	return getAuditCommonService().clearAuditCache(cacheType);
    }

    @RequestMapping(value = "/getAuditCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAuditCache(@RequestParam String cacheType) {
	return getAuditCommonService().getAuditCacheData(cacheType);
    }

}
