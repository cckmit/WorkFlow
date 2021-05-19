/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.service.DeveloperLeadService;
import com.tsi.workflow.service.ProtectedService;
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
 * @author USER
 */
@Controller
@RequestMapping("/developerLead")
public class DeveloperLeadController extends BaseController {

    @Autowired
    DeveloperLeadService developerLeadService;
    @Autowired
    private ProtectedService protectedService;

    public DeveloperLeadService getDeveloperLeadService() {
	return developerLeadService;
    }

    public void setDeveloperLeadService(DeveloperLeadService developerLeadService) {
	this.developerLeadService = developerLeadService;
    }

    public ProtectedService getProtectedService() {
	return protectedService;
    }

    public void setProtectedService(ProtectedService protectedService) {
	this.protectedService = protectedService;
    }

    @RequestMapping(value = "/savePlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse savePlan(HttpServletRequest request, HttpServletResponse response, @RequestBody ImpPlan plan) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	JSONResponse lReturn = getDeveloperLeadService().savePlan(lUser, plan);
	if (lReturn.getStatus()) {
	    request.setAttribute("planId", lReturn.getMetaData());
	}
	return lReturn;
    }

    // @RequestMapping(value = "/myTasks", method = RequestMethod.GET)
    // @ResponseBody
    // public JSONResponse getUserTaskList(HttpServletRequest request,
    // HttpServletResponse response,
    // @RequestParam(required = false, defaultValue = "0") Integer limit,
    // @RequestParam(required = false, defaultValue = "0") Integer offset,
    // @RequestParam(value = "orderBy", required = false) String orderBy) throws
    // Exception {
    // HashMap lOrderBy = null;
    // if (orderBy != null) {
    // lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
    // }
    // User lUser = this.getCurrentUser(request, response);
    // return this.getDeveloperLeadService().getUserTaskList(lUser, offset, limit,
    // lOrderBy);
    // }
    @RequestMapping(value = "/isSubmitReady", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse isSubmitReady(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws Exception {
	return getDeveloperLeadService().isSubmitReady(planId);
    }

    @RequestMapping(value = "/planSubmit", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse planSubmit(HttpServletRequest request, HttpServletResponse response, @RequestBody ImpPlan planId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId.getId());

	return getDeveloperLeadService().planSubmit(lUser, planId);
    }

    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProjectList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = true) String platform) throws Exception {
	HashMap lOrderBy = null;
	JSONResponse lResponse;
	User lUser = this.getCurrentUser(request, response);
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	if (platform.equalsIgnoreCase("Travelport")) {
	    lResponse = getDeveloperLeadService().getProjectList(lUser, offset, limit, filter, lOrderBy);
	} else {
	    lResponse = getDeveloperLeadService().getDeltaProjectList(lUser, offset, limit, filter, lOrderBy);
	}
	return lResponse;
    }

    @RequestMapping(value = "/getActivityLogList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActivityLogList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(value = "filter", defaultValue = "") String filter, @RequestParam String planId) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getDeveloperLeadService().getActivityLogList(planId, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/validateDbcr", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse validateDbcr(HttpServletRequest request, HttpServletResponse response, @RequestParam String dbcrName, @RequestParam String systemId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperLeadService().validateDbcr(lUser, dbcrName, systemId);
    }

    @RequestMapping(value = "/getDbcrList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDbcrList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperLeadService().getDbcrList(lUser, planIds);
    }

    @RequestMapping(value = "/getPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "") String planFilter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User currentUser = getCurrentUser(request, response);
	return getDeveloperLeadService().getPlanList(currentUser, offset, limit, filter, lOrderBy, planFilter);
    }

    @RequestMapping(value = "/getInboxPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getInboxPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "") String planFilter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User currentUser = getCurrentUser(request, response);
	return getDeveloperLeadService().getInboxPlanList(currentUser, offset, limit, filter, lOrderBy, planFilter);
    }

    @RequestMapping(value = "/checkForCSV", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse isCheckForCSV(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws Exception {
	return getDeveloperLeadService().isCheckForCSV(planId);
    }

    @RequestMapping(value = "/checkForDvlBuild", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse checkForDvlBuild(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws Exception {
	return getDeveloperLeadService().checkForDvlBuild(planId);
    }

    @RequestMapping(value = "/checkForBuildInProgress", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse checkForBuildInProgress(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws Exception {
	return getDeveloperLeadService().checkForDvlBuildInProgress(planId);
    }

    @RequestMapping(value = "/getPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlan(@RequestParam("id") String id) {
	return getDeveloperLeadService().getPlan(id);
    }

    @RequestMapping(value = "/deleteImp", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteImps(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) {
	User currentUser = getCurrentUser(request, response);
	return getDeveloperLeadService().deleteImplementations(currentUser, id);
    }

    @RequestMapping(value = "/deletePlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deletePlan(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String id) {
	User currentUser = getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getDeveloperLeadService().deletePlan(currentUser, id);
    }

    @RequestMapping(value = "/getPdddsLibrary", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPdddsLibrary(@RequestParam("systemName") String pSystemName) {
	return getDeveloperLeadService().getPdddsLibrary(pSystemName);

    }

    @RequestMapping(value = "/getDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, false, Constants.LoaderTypes.E, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getAuxDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAuxDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, false, Constants.LoaderTypes.A, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getDeploymentVParsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDeploymentVParsList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentVParsList(lCurrentUser, false, ids);
    }

    @RequestMapping(value = "/getSystemLoadActions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActions(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getSystemLoadActions(lCurrentUser, false, planIds);

    }

    @RequestMapping(value = "/getDepencyPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDepencyPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam String planId) throws IOException, WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getDeveloperLeadService().getDepencyPlanList(lCurrentUser, planId, filter, offset, limit);
    }

    // ZTPFM-1771
    @RequestMapping(value = "/isPrivateVpars", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse isPrivateVpars(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer systemId, @RequestParam String vparsName) throws IOException, WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getDeveloperLeadService().isPrivateVpars(lCurrentUser, systemId, vparsName);
    }

    /**
     * ZTPFM-2533 get plan status by implementation
     *
     * @author vinoth.ponnurangan
     * @param request
     * @param response
     * @param impId
     * @return planStatus JSON Object
     */
    @RequestMapping(value = "/getPlanStatusByImp", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deleteFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String impId) {
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperLeadService().getPlanStatusByImp(lUser, impId);
    }

    // ZTPFM-1771
    @RequestMapping(value = "/removePlanFromSubmissionCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse removePlanFromSubmissionCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws IOException, WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getDeveloperLeadService().removePlanFromSubmissionCache(lCurrentUser, planId);
    }

}
