/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.service.QualityAssuranceService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author USER
 */
@Controller
@RequestMapping("/qualityAssurance")
public class QualityAssuranceController extends BaseController {

    @Autowired
    QualityAssuranceService qualityAssuranceService;
    @Autowired
    private ProtectedService protectedService;

    public QualityAssuranceService getQualityAssuranceService() {
	return qualityAssuranceService;
    }

    public void setQualityAssuranceService(QualityAssuranceService qualityAssuranceService) {
	this.qualityAssuranceService = qualityAssuranceService;
    }

    public ProtectedService getProtectedService() {
	return protectedService;
    }

    public void setProtectedService(ProtectedService protectedService) {
	this.protectedService = protectedService;
    }

    @RequestMapping(value = "/planTestStatus", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updatePlanTestStatus(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer systemId, @RequestParam Integer[] vparId, @RequestParam String planId, @RequestParam String status, @RequestParam Integer[] vparsId) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return this.getQualityAssuranceService().updatePlanTestStatus(lCurrentUser, planId, systemId, vparId, status, vparsId);
    }

    @RequestMapping(value = "/getRegressionDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRegressionDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, false, Constants.LoaderTypes.E, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getRegressionAuxDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRegressionAuxDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, false, Constants.LoaderTypes.A, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getRegressionDeploymentVParsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRegressionDeploymentVParsList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentVParsList(lCurrentUser, false, ids);
    }

    @RequestMapping(value = "/getRegressionSystemLoadActions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRegressionSystemLoadActions(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getSystemLoadActions(lCurrentUser, false, planIds);
    }

    @RequestMapping(value = "/getFunctionalDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFunctionalDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, true, Constants.LoaderTypes.E, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getFunctionalAuxDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFunctionalAuxDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, true, Constants.LoaderTypes.A, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getFunctionalDeploymentVParsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFunctionalDeploymentVParsList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentVParsList(lCurrentUser, true, ids);
    }

    @RequestMapping(value = "/getFunctionalSystemLoadActions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFunctionalSystemLoadActions(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getSystemLoadActions(lCurrentUser, true, planIds);
    }

    @RequestMapping(value = "/getPassedRegressionDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPassedRegressionDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getPlansPassedRegTesting(lCurrentUser, false, Constants.LoaderTypes.E, filter, offset, limit, lOrderBy);
    }

    // ZTPFM-1455 QA Functional Tester

    @RequestMapping(value = "/getQAFunctionalDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getQAFunctionalDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getQualityAssuranceService().getQAFunDeploymentPlanList(lCurrentUser, true, Constants.LoaderTypes.E, filter, offset, limit, lOrderBy);
    }
}
