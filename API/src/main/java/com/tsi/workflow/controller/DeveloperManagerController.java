/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.ui.RepoUserView;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.service.DeveloperManagerService;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.Arrays;
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
@RequestMapping("/developerManager")
public class DeveloperManagerController extends BaseController {

    @Autowired
    DeveloperManagerService developerManagerService;

    @Autowired
    GITHelper gitHelper;

    public DeveloperManagerService getDeveloperManagerService() {
	return developerManagerService;
    }

    public void setDeveloperManagerService(DeveloperManagerService developerManagerService) {
	this.developerManagerService = developerManagerService;
    }

    @RequestMapping(value = "/getMyPlanTasks", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getMyPlanTasks(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getDeveloperManagerService().getMyPlanTasks(lCurrentUser, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getAssignedPlans", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAssignedPlans(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false) String planId, @RequestParam(required = false) String planStatus, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getDeveloperManagerService().getAssignedPlans(lCurrentUser, offset, limit, lOrderBy, planId, planStatus);
    }

    @RequestMapping(value = "/getAssignedPlansAndSysLoad", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAssignedPlansAndSysLoad(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false) String planId, @RequestParam(required = false) String planStatus, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getDeveloperManagerService().getAssignedPlansAndSysLoad(lCurrentUser, offset, limit, lOrderBy, planId, planStatus);
    }

    @RequestMapping(value = "/approvePlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse approvePlan(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String comments) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);

	return getDeveloperManagerService().approvePlan(lUser, planId, comments);
    }

    @RequestMapping(value = "/rejectPlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse rejectPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam String id, @RequestParam String comments, @RequestParam(defaultValue = "true") boolean deleteLoadSetFlag) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getDeveloperManagerService().rejectPlan(lUser, id, comments, false, deleteLoadSetFlag);
    }

    @RequestMapping(value = "/getApproveStatusByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildByPlan(@RequestParam String[] ids) {
	return getDeveloperManagerService().getApproveStatusByPlan(Arrays.asList(ids));
    }

    @RequestMapping(value = "/getProductionRepoList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProductionRepoList(HttpServletRequest request, HttpServletResponse response) {
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperManagerService().getProductionRepoList(lUser);
    }

    @RequestMapping(value = "/updateRepoPermission", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateRepoPermission(HttpServletRequest request, HttpServletResponse response, @RequestBody RepositoryView pReopsitory) {
	return getDeveloperManagerService().updateRepoPermission(pReopsitory);
    }

    // @RequestMapping(value = "/getRepositoryByName", method = RequestMethod.GET)
    // @ResponseBody
    // public JSONResponse getRepoListByRepoName(@RequestParam String repoName) {
    // return getDeveloperManagerService().getRepositoryByName(repoName);
    // }
    @RequestMapping(value = "/getProdRepoUsers", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProdRepoUsers(@RequestParam String repoName) {
	return getDeveloperManagerService().getProdRepoUsers(repoName);
    }

    @RequestMapping(value = "/setRepoOwnersPermission", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse setRepoOwnersPermission(HttpServletRequest request, HttpServletResponse response, @RequestParam String repoName, @RequestParam String access, @RequestBody RepoUserView repoUsers) {

	User lUser = this.getCurrentUser(request, response);
	return getDeveloperManagerService().setProdRepoUsers(lUser, repoName, access, repoUsers);

    }

    @RequestMapping(value = "/getProjectList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getProjectList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "") String searchField) throws Exception {
	LinkedHashMap lOrderBy = null;
	User lUser = this.getCurrentUser(request, response);
	JSONResponse lResponse;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	lResponse = getDeveloperManagerService().getProjectList(lUser, offset, limit, filter, lOrderBy, searchField);

	return lResponse;
    }

    /*
     * Created By :Ramkumar Seenivasan Date:08/09/2019 JIRA :2224 Exporting Data For
     * Project NBR Display Screen
     */
    @RequestMapping(value = "/exportDTNReport", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse exportDTNReport(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(required = false, defaultValue = "") String searchField) throws Exception {
	LinkedHashMap lOrderBy = null;
	User lUser = this.getCurrentUser(request, response);
	JSONResponse lResponse;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getDeveloperManagerService().exportDTNReport(lUser, offset, limit, filter, lOrderBy, searchField);
    }

    @RequestMapping(value = "/updateProject", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateProject(HttpServletRequest request, HttpServletResponse response, @RequestBody Project project) throws Exception {
	User lUser = this.getCurrentUser(request, response);

	project.setIsDelta(Boolean.TRUE);
	return getDeveloperManagerService().updateProject(lUser, project);
    }

    @RequestMapping(value = "/saveProject", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveProject(HttpServletRequest request, HttpServletResponse response, @RequestBody Project project) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	project.setIsDelta(Boolean.TRUE);
	return getDeveloperManagerService().saveProject(lUser, project);
    }

    @RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteProject(HttpServletRequest request, HttpServletResponse response, @RequestBody Project project) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	project.setIsDelta(Boolean.TRUE);
	return getDeveloperManagerService().deleteProject(lUser, project);
    }

    @RequestMapping(value = "/macroHeaderList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse macroHeaderList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	User lUser = this.getCurrentUser(request, response);
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getDeveloperManagerService().getMacroHeaderList(lUser, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/markOnline", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse markOnlineForMacroHeaderPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getDeveloperManagerService().markOnlineForMacroHeaderPlan(lCurrentUser, id);
    }

    @RequestMapping(value = "/macroHeaderReject", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse macroHeaderReject(HttpServletRequest request, HttpServletResponse response, @RequestParam String id, @RequestParam String rejectReason) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getDeveloperManagerService().macroHeaderReject(lCurrentUser, id, rejectReason);
    }

    @RequestMapping(value = "/getFallBackMacroHeaderPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFallBackMacroHeaderPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, value = "orderBy") String orderBy) throws IOException {
	LinkedHashMap lOrderBy = null;
	User lUser = this.getCurrentUser(request, response);

	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	return getDeveloperManagerService().getFallBackMacroHeaderPlan(lUser, offset, limit, lOrderBy);
    }

    @ResponseBody
    @RequestMapping(value = "/checkNewFileCreateFlag", method = RequestMethod.GET)
    public JSONResponse checkNewFileCreateFlag(HttpServletRequest request, HttpServletResponse response, @RequestParam String repoName) throws IOException {
	return getDeveloperManagerService().checkNewFileCreateFlag(repoName);
    }
}
