/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.ui.ProdTOSForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author
 */
@Controller
@RequestMapping("/tsd")

public class TSDController extends BaseController {

    @Autowired
    TSDService tsdService;

    public TSDService getTSDService() {
	return tsdService;
    }

    @RequestMapping(value = "/setOnline", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse setOnline(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return this.getTSDService().setOnline(lCurrentUser, planId);
    }

    @RequestMapping(value = "/acceptFallback", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse acceptFallback(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String rejectReason) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return this.getTSDService().acceptFallback(lCurrentUser, planId, rejectReason);
    }

    @RequestMapping(value = "/getLoadsToAccept", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse loadsetsToAccept(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getTSDService().getLoadsToAccept(lCurrentUser, false, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/getFallBackLoadsToAccept", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFallBackLoadsToAccept(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	String filter = "";
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getTSDService().getLoadsToAccept(lCurrentUser, true, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/postProdSystemLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse postProdSystemLoad(HttpServletRequest request, HttpServletResponse response, @RequestBody ProductionLoads lLoadSet, @RequestParam(defaultValue = "false") Boolean forceActivate, @RequestParam(defaultValue = "") String loadsetDeactivateChangeComment) throws WorkflowException {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", lLoadSet.getPlanId().getId());

	return getTSDService().postActivationAction(lUser, lLoadSet, forceActivate, loadsetDeactivateChangeComment);
    }

    @RequestMapping(value = "/getFallbackProductionLoads", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFallbackProductionLoads(@RequestParam String[] ids, @RequestParam String systemName) throws WorkflowException {
	return getTSDService().getProductionLoads(ids, true, systemName);
    }

    @RequestMapping(value = "/getProductionLoads", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoads(@RequestParam String[] ids, @RequestParam String systemName) throws WorkflowException {
	return getTSDService().getProductionLoads(ids, false, systemName);
    }

    @RequestMapping(value = "/getSystemLoadListBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadListBySystemId(@RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, value = "orderBy") String orderBy, @RequestParam Integer id, @RequestParam(required = false, defaultValue = "") String filter) throws IOException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	return getTSDService().getSystemLoadListBySystemId(id, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/getFallBackSystemLoadListBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFallBackSystemLoadListBySystemId(@RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, value = "orderBy") String orderBy, @RequestParam Integer id) throws IOException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	return getTSDService().getFallBackSystemLoadListBySystemId(id, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getAuxLoads", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAuxLoads(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getTSDService().getAuxLoads(lCurrentUser, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/markAuxAsOnline", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse markAuxAsOnline(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getTSDService().markAuxAsOnline(lCurrentUser, id);
    }

    @RequestMapping(value = "/markAuxAsFallback", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse markAuxAsFallback(HttpServletRequest request, HttpServletResponse response, @RequestParam String id, @RequestParam String rejectReason) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getTSDService().markAuxAsFallback(lCurrentUser, id, rejectReason, Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET);
    }

    @RequestMapping(value = "/markAuxAsOnlineRevert", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse markAuxAsOnlineRevert(HttpServletRequest request, HttpServletResponse response, @RequestParam String id, @RequestParam String rejectReason) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getTSDService().markAuxAsFallback(lCurrentUser, id, rejectReason, Constants.FALLBACK_STATUS.DELETE_ALL_LOADSET);
    }

    @RequestMapping(value = "/getAuxPlanOpStatus", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAuxPlanOpStatus(HttpServletRequest request, HttpServletResponse response) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getTSDService().getAuxPlanOpStatus(lCurrentUser);
    }

    @RequestMapping(value = "/DeleteAuxPlanInOpStatus", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse DeleteAuxPlanInOpStatus(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getTSDService().DeleteAuxPlanInOpStatus(lCurrentUser, id);
    }

    @RequestMapping(value = "/ImpPlanSyncEType", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementationPlanSyncEType(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getTSDService().getImplementationPlanSync(lCurrentUser, Constants.LoaderTypes.E, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/uploadFallbackLoadset", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse uploadFallbackLoadset(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return getTSDService().uploadFallbackLoadset(lCurrentUser, planId);
    }

    @RequestMapping(value = "/getFallBackMacroHeaderPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFallBackMacroHeaderPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, value = "orderBy") String orderBy) throws IOException {
	LinkedHashMap lOrderBy = null;
	User lUser = this.getCurrentUser(request, response);
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	return getTSDService().getFallBackMacroHeaderPlan(lUser, offset, limit, lOrderBy);
    }

    // ZTPFM-1547 Multi Select for Production Load and Activate
    @RequestMapping(value = "/loadAndActivateInTOS", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse loadAndActivateInTOS(HttpServletRequest request, HttpServletResponse response, @RequestBody ProdTOSForm productionLoads) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().postTOSOperationProcess(lUser, productionLoads);
    }

    // ZTPFM-1547 - DEBUGG
    @RequestMapping(value = "/removeLoadAndActivateInTOS", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse removeLoadAndActivateInTOS(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String systemName, @RequestParam Integer preProdId) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().removeLoadAndActivateInTOS(lUser, planId, systemName, preProdId);
    }

    // ZTPFM-1547 - DEBUGG
    @RequestMapping(value = "/getLoadAndActivateInTOS", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadAndActivateInTOS(HttpServletRequest request, HttpServletResponse response) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().getLoadAndActivateInTOS(lUser);
    }

    // ZTPFM-891 - Multi Select for Accept
    @RequestMapping(value = "/acceptMultiPlans", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse acceptMultiPlans(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().acceptMultiPlans(lUser, planIds);
    }

    // ZTPFM-891 - InProgress Call
    @RequestMapping(value = "/isAcceptInProgress", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse isAcceptInProgress(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planId) throws Exception {
	return getTSDService().isAcceptInProgress(planId);
    }

    // ZTPFM-1980
    @RequestMapping(value = "/acceptLoadset", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse acceptLoadset(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String systemName) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return getTSDService().acceptLoadsetForSystem(lUser, planId, systemName);
    }

    // ZTPFM-1980
    @RequestMapping(value = "/acceptFallbackLoadset", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse acceptFallbackLoadset(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String systemName) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return getTSDService().acceptFallbackLoadsetForSystem(lUser, planId, systemName);
    }

    // ZTPFM-1980 - R Category Changes
    @RequestMapping(value = "/enablePlanForAccept", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse enablePlanForAccept(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().enablePlanForAccept(lUser, planIds);
    }

    // ZTPFM-2286 - Update the plan status as Pending Fallback
    @RequestMapping(value = "/setPlanasPendingFallback", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse setPlanasPendingFallback(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String comments) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return getTSDService().setPlanasPendingFallback(lUser, planId, comments);
    }

    // ZTPFM-2500 Get the Allowed Plan which are ready for Load and Activate
    @RequestMapping(value = "/getActionNotAllowedPlans", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActionNotAllowedPlans(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids, @RequestParam String systemName, @RequestParam String action) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().getActionNotAllowedPlans(lUser, ids, systemName, action);
    }

    // ZTPFM-2500 Get the Allowed Plan which are ready for Load and Activate
    @RequestMapping(value = "/getProdTOSActionReport", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProdTOSActionReport(HttpServletRequest request, HttpServletResponse response, @RequestParam String action, @RequestParam(required = false) String systemName) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().getProdTOSActionReport(lUser, action, systemName);
    }

    // 2502 Get Online Feedback Running plans
    @RequestMapping(value = "/getAcceptedPlans", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAcceptedPlans(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, value = "orderBy") String[] orderBy, @RequestParam(required = false) String filter) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().getAcceptedPlans(lUser, offset, limit, orderBy, filter);
    }

    // 2502 Get Online Feedback Running plans
    @RequestMapping(value = "/removeAcceptInprogressPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse removeAcceptInprogressPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().removeAcceptInprogressPlan(lUser, planId);
    }

    // 2502 Get Online Feedback Running plans
    @RequestMapping(value = "/getAcceptProcessingPlans", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAcceptProcessingPlans(HttpServletRequest request, HttpServletResponse response) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().getAcceptProcessingPlans(lUser);
    }

    // 2502 Get Online Feedback Running plans
    @RequestMapping(value = "/getTosLoadActProcessingPlans", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTosLoadActProcessingPlans(HttpServletRequest request, HttpServletResponse response) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().getTosLoadActProcessingPlans(lUser);
    }

    // ZTPFM-2502
    @RequestMapping(value = "/clearProdTOSLoadActCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse clearProdTOSLoadActCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String action, @RequestParam(required = false) String systemName) {
	User lUser = this.getCurrentUser(request, response);
	return getTSDService().clearProdTOSLoadActCache(lUser, action, systemName);
    }

}
