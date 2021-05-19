/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.controller;

import com.tsi.workflow.audit.beans.dao.GiTransaction;
import com.tsi.workflow.audit.service.ApiService;
import com.tsi.workflow.audit.uiform.TransactionForm;
import com.tsi.workflow.audit.uiform.TransactionViewForm;
import com.tsi.workflow.utils.JSONResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Radha.Adhimoolam
 */
@Controller
@RequestMapping("/audit/api")
public class ApiController {

    private static final Logger LOG = Logger.getLogger(ApiController.class.getName());

    @Autowired
    ApiService apiService;

    public ApiService getApiService() {
	return apiService;
    }

    @RequestMapping(value = "/saveTransaction", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveTransaction(HttpServletRequest request, HttpServletResponse response, @RequestBody GiTransaction giTransaction) {
	JSONResponse lReturn = new JSONResponse();
	lReturn.setStatus(Boolean.TRUE);
	return lReturn;
    }

    @RequestMapping(value = "/getTransactionInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getTransactionInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody TransactionForm transactionForm) {
	return getApiService().getTransactionInfo(transactionForm);
    }

    @RequestMapping(value = "/getTransactionViewInfo", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getTransactionViewInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody TransactionViewForm transactionForm) {
	return getApiService().getTransactionViewInfo(transactionForm);
    }
}
