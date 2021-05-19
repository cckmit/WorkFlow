/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.base.service.CommonBaseService;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.service.DeveloperLeadService;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author USER
 */
public class CommonBaseController extends BaseController {

    @Autowired
    private CommonBaseService commonService;
    @Autowired
    DeveloperLeadService developerLeadService;

    public DeveloperLeadService getDeveloperLeadService() {
	return developerLeadService;
    }

    public CommonBaseService getCommonBaseService() {
	return commonService;
    }

    public void setCommonBaseService(CommonBaseService commonService) {
	this.commonService = commonService;
    }

    @RequestMapping(value = "/getBuild", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuild(@RequestParam("id") Integer id) {
	return getCommonBaseService().getBuild(id);
    }

    @RequestMapping(value = "/getPlanApprovals", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanApprovals(@RequestParam("id") Integer id) {
	return getCommonBaseService().getPlanApprovals(id);
    }

    @RequestMapping(value = "/getPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlan(@RequestParam("id") String id) {
	return getCommonBaseService().getPlan(id);
    }

    @RequestMapping(value = "/getSegmentMapping", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentMapping(@RequestParam("id") Integer id) {
	return getCommonBaseService().getSegmentMapping(id);
    }

    @RequestMapping(value = "/getImplementation", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementation(@RequestParam("id") String id) {
	return getCommonBaseService().getImplementation(id);
    }

    @RequestMapping(value = "/getLoadCategory", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadCategory(@RequestParam("id") Integer id) {
	return getCommonBaseService().getLoadCategory(id);
    }

    @RequestMapping(value = "/getLoadFreeze", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadFreeze(@RequestParam("id") Integer id) {
	return getCommonBaseService().getLoadFreeze(id);
    }

    @RequestMapping(value = "/getLoadWindow", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadWindow(@RequestParam("id") Integer id) {
	return getCommonBaseService().getLoadWindow(id);
    }

    @RequestMapping(value = "/getPlatform", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlatform(@RequestParam("id") Integer id) {
	return getCommonBaseService().getPlatform(id);
    }

    @RequestMapping(value = "/getProject", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProject(@RequestParam("id") Integer id) {
	return getCommonBaseService().getProject(id);
    }

    @RequestMapping(value = "/getPutLevel", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPutLevel(@RequestParam("id") Integer id) {
	return getCommonBaseService().getPutLevel(id);
    }

    @RequestMapping(value = "/getSystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystem(@RequestParam("id") Integer id) {
	return getCommonBaseService().getSystem(id);
    }

    @RequestMapping(value = "/getSystemLoad", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoad(@RequestParam("id") Integer id) {
	return getCommonBaseService().getSystemLoad(id);
    }

    @RequestMapping(value = "/getVpars", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getVpars(@RequestParam("id") Integer id) {
	return getCommonBaseService().getVpars(id);
    }

    @RequestMapping(value = "/getBuildList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getBuildList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getPlanApprovalsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanApprovalsList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getPlanApprovalsList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getPlanList(offset, limit, filter, lOrderBy);
    }

    @RequestMapping(value = "/getSegmentMappingList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentMappingList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSegmentMappingList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getImplementationList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementationList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getImplementationList(offset, limit, filter, lOrderBy);
    }

    @RequestMapping(value = "/getLoadFreezeList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadFreezeList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getLoadFreezeList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getGroupLoadFreezeList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getGroupLoadFreezeList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getGroupLoadFreezeList(offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/getLoadCategoryList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadCategoryList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getLoadCategoryList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getLoadWindowList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadWindowList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getLoadWindowList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getPlatformList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlatformList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getPlatformList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProjectList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getProjectList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getPutLevelList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPutLevelList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getPutLevelList(offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/getSystemList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSystemList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getSystemLoadList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSystemLoadList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getVparsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getVparsList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getVparsList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getBuildByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildByPlan(@RequestParam String[] ids) {
	return getCommonBaseService().getBuildByPlan(ids);
    }

    @RequestMapping(value = "/getBuildBySystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildBySystem(@RequestParam Integer[] ids) {
	return getCommonBaseService().getBuildBySystem(ids);
    }

    @RequestMapping(value = "/getImplementationByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementationByPlan(@RequestParam String[] ids) {
	return getCommonBaseService().getImplementationByPlan(ids);
    }

    @RequestMapping(value = "/getPlanByProject", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanByProject(@RequestParam Integer[] ids) {
	return getCommonBaseService().getPlanByProject(ids);
    }

    @RequestMapping(value = "/getPlanApprovalsByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanApprovalsByPlan(@RequestParam String[] ids) {
	return getCommonBaseService().getPlanApprovalsByPlan(ids);
    }

    @RequestMapping(value = "/getSegmentMappingByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentMappingByPlan(@RequestParam String[] ids) {
	return getCommonBaseService().getSegmentMappingByPlan(ids);
    }

    @RequestMapping(value = "/getSegmentMappingByImplementation", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentMappingByImplementation(@RequestParam String[] ids) {
	return getCommonBaseService().getSegmentMappingByImplementation(ids);
    }

    @RequestMapping(value = "/getSegmentMappingBySystemLoad", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentMappingBySystemLoad(@RequestParam Integer[] ids) {
	return getCommonBaseService().getSegmentMappingBySystemLoad(ids);
    }

    @RequestMapping(value = "/getLoadCategoriesBySystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadCategoriesBySystem(@RequestParam Integer[] ids) {
	return getCommonBaseService().getLoadCategoriesBySystem(ids);
    }

    @RequestMapping(value = "/getLoadFreezeByLoadCategories", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadFreezeByLoadCategories(@RequestParam Integer[] ids) {
	return getCommonBaseService().getLoadFreezeByLoadCategories(ids);
    }

    @RequestMapping(value = "/getLoadWindowByLoadCategories", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadWindowByLoadCategories(@RequestParam Integer[] ids) {
	return getCommonBaseService().getLoadWindowByLoadCategories(ids);
    }

    @RequestMapping(value = "/getPutLevelBySystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPutLevelBySystem(@RequestParam Integer[] ids) {
	return getCommonBaseService().getPutLevelBySystem(ids);
    }

    @RequestMapping(value = "/getSystemByPlatform", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemByPlatform(@RequestParam Integer[] ids) {
	return getCommonBaseService().getSystemByPlatform(ids);
    }

    @RequestMapping(value = "/getSystemLoadByLoadCategories", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadByLoadCategories(@RequestParam Integer[] ids) {
	return getCommonBaseService().getSystemLoadByLoadCategories(ids);
    }

    @RequestMapping(value = "/getSystemLoadByPutLevel", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadByPutLevel(@RequestParam Integer[] ids) {
	return getCommonBaseService().getSystemLoadByPutLevel(ids);
    }

    @RequestMapping(value = "/getSystemLoadByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadByPlan(@RequestParam String[] ids) {
	return getCommonBaseService().getSystemLoadByPlan(ids);
    }

    @RequestMapping(value = "/getSystemLoadBySystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadBySystem(@RequestParam Integer[] ids) {
	return getCommonBaseService().getSystemLoadBySystem(ids);
    }

    @RequestMapping(value = "/getVparsBySystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getVparsBySystem(@RequestParam Integer[] ids) {
	return getCommonBaseService().getVparsBySystem(ids);
    }

    @RequestMapping(value = "/getProblemTicket", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getProblemTicket(@RequestParam String ticketNumber) throws WorkflowException {
	return getCommonBaseService().getProblemTicket(ticketNumber);
    }

    @RequestMapping(value = "/getSystemListByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemListByPlan(@RequestParam String planId) {
	return getCommonBaseService().getSystemListByPlan(planId);
    }

    @RequestMapping(value = "/getActivityLogByPlanId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActivityLogByPlanId(@RequestParam String[] ids) throws WorkflowException {
	return getCommonBaseService().getActivityLogByPlanId(ids);
    }

    @RequestMapping(value = "/getActivityLogByImpId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActivityLogByImpId(@RequestParam String[] ids) throws WorkflowException {
	return getCommonBaseService().getActivityLogByImpId(ids);
    }

    @RequestMapping(value = "/getActivityLogList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActivityLogList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getActivityLogList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getActivityLog", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActivityLog(@RequestParam("id") Integer id) {
	return getCommonBaseService().getActivityLog(id);
    }

    @RequestMapping(value = "/getSystemLoadActionsBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActionsBySystemId(@RequestParam Integer[] ids) throws WorkflowException {
	return getCommonBaseService().getSystemLoadActionsBySystemId(ids);
    }

    @RequestMapping(value = "/getSystemLoadActionsByPlanId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActionsByPlanId(@RequestParam String[] ids) throws WorkflowException {
	return getCommonBaseService().getSystemLoadActionsByPlanId(ids);
    }

    @RequestMapping(value = "/getSystemLoadActionsBySystemLoadId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActionsBySystemLoadId(@RequestParam Integer[] ids) throws WorkflowException {
	return getCommonBaseService().getSystemLoadActionsBySystemLoadId(ids);
    }

    @RequestMapping(value = "/getSystemLoadActionsByVparId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActionsByVparId(@RequestParam Integer[] ids) throws WorkflowException {
	return getCommonBaseService().getSystemLoadActionsByVparId(ids);
    }

    @RequestMapping(value = "/getSystemLoadActionsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActionsList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSystemLoadActionsList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getSystemLoadActions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadActions(@RequestParam("id") Integer id) {
	return getCommonBaseService().getSystemLoadActions(id);
    }

    @RequestMapping(value = "/getDbcrByPlanId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDbcrByPlanId(@RequestParam String[] ids) throws WorkflowException {
	return getCommonBaseService().getDbcrByPlanId(ids);
    }

    @RequestMapping(value = "/getDbcrBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDbcrBySystemId(@RequestParam Integer[] ids) throws WorkflowException {
	return getCommonBaseService().getDbcrBySystemId(ids);
    }

    @RequestMapping(value = "/getDbcrList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDbcrList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getDbcrList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getDbcr", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDbcr(@RequestParam("id") Integer id) {
	return getCommonBaseService().getDbcr(id);
    }

    @RequestMapping(value = "/getProductionLoadsByPlanId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoadsByPlanId(@RequestParam String[] ids) throws WorkflowException {
	return getCommonBaseService().getProductionLoadsByPlanId(ids);
    }

    @RequestMapping(value = "/getProductionLoadsBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoadsBySystemId(@RequestParam Integer[] ids) throws WorkflowException {
	return getCommonBaseService().getProductionLoadsBySystemId(ids);
    }

    @RequestMapping(value = "/getProductionLoadsBySystemLoadId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoadsBySystemLoadId(@RequestParam Integer[] ids) throws WorkflowException {
	return getCommonBaseService().getProductionLoadsBySystemLoadId(ids);
    }

    @RequestMapping(value = "/getProductionLoadsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoadsList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getProductionLoadsList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getProductionLoads", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionLoads(@RequestParam("id") Integer id) {
	return getCommonBaseService().getProductionLoads(id);
    }

    @RequestMapping(value = "/getSystemCpuList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemCpuList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSystemCpuList(offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getSystemPdddsMapping", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemPdddsMapping(@RequestParam Integer id) throws IOException, WorkflowException {
	return getCommonBaseService().getSystemPdddsMapping(id);
    }

    @RequestMapping(value = "/getSystemPdddsMappingList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemPdddsMappingList(@RequestParam Integer[] id) throws IOException, WorkflowException {
	return getCommonBaseService().getSystemPdddsMappingList(id);
    }

    @RequestMapping(value = "/getSearchViewPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSearchPlanList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "") String planFilter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSearchViewPlanList(offset, limit, null, lOrderBy, planFilter);
    }

    @RequestMapping(value = "/getSearchViewImplementationList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSearchViewImplementationList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonBaseService().getSearchViewImplementationList(offset, limit, null, lOrderBy, filter);
    }

}
