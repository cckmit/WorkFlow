/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.ui.PreProdTOSForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.service.TestSystemSupportService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
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
@RequestMapping("/tss")
public class TestSystemSupportController extends BaseController {

    @Autowired
    private TestSystemSupportService testSystemSupportService;
    @Autowired
    private ProtectedService protectedService;

    public TestSystemSupportService getTestSystemSupportService() {
	return testSystemSupportService;
    }

    public ProtectedService getProtectedService() {
	return protectedService;
    }

    public void setProtectedService(ProtectedService protectedService) {
	this.protectedService = protectedService;
    }

    @RequestMapping(value = "/getPreProductionLoads", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPreProductionLoads(@RequestParam String[] ids) throws WorkflowException {
	return getTestSystemSupportService().getPreProductionLoads(ids);
    }

    @RequestMapping(value = "/deletePreProductionLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteTestSystemLoad(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id) {
	User lUser = this.getCurrentUser(request, response);
	return getTestSystemSupportService().deleteActivationAction(lUser, id);
    }

    @RequestMapping(value = "/getTosDeploymentVParsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTosDeploymentVParsList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getTestSystemSupportService().getDeploymentVParsList(lCurrentUser, ids);
    }

    @RequestMapping(value = "/getTosDeploymentPreProdVParsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTosDeploymentPreProdVParsList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids, @RequestParam(required = false, defaultValue = "false") Boolean isDeployedInPreProdLoads) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getTestSystemSupportService().getTosDeploymentPreProdVParsList(lCurrentUser, ids, isDeployedInPreProdLoads);
    }

    @RequestMapping(value = "/getTosDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTosDeploymentPlanList(@RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) throws IOException {
	return getTestSystemSupportService().getDeploymentPlanList(filter, offset, limit);
    }

    @RequestMapping(value = "/getTosAuxDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTosAuxDeploymentPlanList(@RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) throws IOException {
	return getTestSystemSupportService().getAuxDeploymentPlanList(filter, offset, limit);
    }

    @RequestMapping(value = "/postPreProdSystemLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse postPreProdSystemLoad(HttpServletRequest request, HttpServletResponse response, @RequestBody PreProductionLoads lLoadSet) throws WorkflowException {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", lLoadSet.getPlanId().getId());

	return getTestSystemSupportService().postActivationAction(lUser, lLoadSet, false);
    }

    @RequestMapping(value = "/postPreProdSystemAuxLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse postPreProdSystemAuxLoad(HttpServletRequest request, HttpServletResponse response, @RequestBody PreProductionLoads lLoadSet) throws WorkflowException {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", lLoadSet.getPlanId().getId());

	return getTestSystemSupportService().postActivationAction(lUser, lLoadSet, true);
    }

    @RequestMapping(value = "/getYodaDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getYodaDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, false, Constants.LoaderTypes.E, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getYodaAuxDeploymentPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getYodaAuxDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentPlanList(lCurrentUser, false, Constants.LoaderTypes.A, filter, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getYodaDeploymentVParsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getYodaDeploymentVParsList(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] ids) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentVParsList(lCurrentUser, false, ids);
    }

    @RequestMapping(value = "/getYodaSystemLoadActions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getYodaSystemLoadActions(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] planIds) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getSystemLoadActions(lCurrentUser, false, planIds);
    }

    // ZTPFM-1548
    @RequestMapping(value = "/getPlansDeployedInPreProdList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlansDeployedInPreProdList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) throws IOException {
	return getTestSystemSupportService().getPlansDeployedInPreProdList(filter, offset, limit);
    }

    // ZTPFM-1547
    @RequestMapping(value = "/loadAndActivateInTOS", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse loadAndActivateInTOS(HttpServletRequest request, HttpServletResponse response, @RequestBody PreProdTOSForm preProductionLoads) {
	User lUser = this.getCurrentUser(request, response);
	return getTestSystemSupportService().postTOSOperationProcess(lUser, preProductionLoads);
    }

    // ZTPFM-1547 - DEBUGG
    @RequestMapping(value = "/removeLoadAndActivateInTOS", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse removeLoadAndActivateInTOS(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String systemName, @RequestParam Integer preProdId) {
	User lUser = this.getCurrentUser(request, response);
	return getTestSystemSupportService().removeLoadAndActivateInTOS(lUser, planId, systemName, preProdId);
    }

    // ZTPFM-1547 - DEBUGG
    @RequestMapping(value = "/getLoadAndActivateInTOS", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadAndActivateInTOS(HttpServletRequest request, HttpServletResponse response) {
	User lUser = this.getCurrentUser(request, response);
	return getTestSystemSupportService().getLoadAndActivateInTOS(lUser);
    }

}
