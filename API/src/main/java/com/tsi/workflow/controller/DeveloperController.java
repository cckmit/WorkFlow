/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.service.DeveloperService;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author USER
 */
@Controller
@RequestMapping("/developer")
public class DeveloperController extends BaseController {

    @Autowired
    DeveloperService developerService;

    public DeveloperService getDeveloperService() {
	return developerService;
    }

    @RequestMapping(value = "/getImplementationList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementation(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperService().getImplementationList(lUser, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/getInboxImplementationList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getInboxImplementationList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperService().getInboxImplementationList(lUser, offset, limit, lOrderBy, filter);
    }

    @RequestMapping(value = "/saveImplementation", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveImplementation(HttpServletRequest request, HttpServletResponse response, @RequestBody Implementation pImplementation) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", pImplementation.getPlanId().getId());

	JSONResponse lReturn = getDeveloperService().saveImplementation(lUser, pImplementation);
	if (lReturn.getStatus()) {
	    request.setAttribute("impId", lReturn.getMetaData());
	}
	return lReturn;
    }

    @RequestMapping(value = "/updateImplementation", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateImplementation(HttpServletRequest request, HttpServletResponse response, @RequestBody Implementation pImplementation) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", pImplementation.getId());
	return getDeveloperService().updateImplementation(lUser, pImplementation);
    }

    // @RequestMapping(value = "/searchFile", method = RequestMethod.GET)
    // @ResponseBody
    // public JSONResponse fileSearch(HttpServletRequest request,
    // HttpServletResponse response,
    // @RequestParam String fileName, @RequestParam String flag, @RequestParam
    // String implId) {
    // User lUser = this.getCurrentUser(request, response);
    // if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_PRODFLAG)) {
    // return protectedService.prodMigNonIBMSearchFile(lUser, fileName,
    // Constants.FILE_MIG_NONIBM);
    // } else if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_NONPRODFLAG)) {
    // return protectedService.nonProdFileSearch(lUser, implId, fileName);
    // } else if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_IBMVANILLA)) {
    // return protectedService.ibmVanillaFileSearch(lUser, implId, fileName);
    // } else {
    // throw new WorkflowException("Not a valid option");
    // }
    // }
    @RequestMapping(value = "/checkoutFile", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse checkoutFile(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pSearchResult, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);
	return getDeveloperService().checkoutFile(lUser, implId, pSearchResult);
    }

    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse commit(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pSearchResult, @RequestParam String implId, @RequestParam String commitMessage) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return getDeveloperService().commit(lUser, implId, pSearchResult, commitMessage);
    }

    @RequestMapping(value = "/checkin", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse checkin(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return getDeveloperService().checkin(lUser, implId);
    }

    @RequestMapping(value = "/createSourceArtifact", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse createSourceArtifact(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pFileList, @RequestParam String implId) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return this.getDeveloperService().createSourceArtifact(lUser, implId, pFileList);
    }

    @RequestMapping(value = "/requestPeerReview", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse requestPeerReview(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return this.getDeveloperService().requestPeerReview(lUser, implId);
    }

    @RequestMapping(value = "/listTestCases", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse listTestCases(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String implId) {
	return getDeveloperService().listTestCases(planId, implId);
    }

    @RequestMapping(value = "/deleteTestCase", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deleteTestCase(HttpServletRequest request, @RequestParam String implId, @RequestParam String planId, @RequestParam String testFile) {
	return getDeveloperService().deleteTestCase(planId, implId, testFile);
    }

    @RequestMapping(value = "/downloadTestCase", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse downloadTestCase(HttpServletRequest request, @RequestParam String implId, @RequestParam String planId, @RequestParam String testFile) throws Exception {
	return getDeveloperService().downloadTestCase(planId, implId, testFile);
    }

    @RequestMapping(value = "/uploadTestCase", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse uploadTestCase(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId, @RequestParam String planId, @RequestParam List<MultipartFile> file) {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);
	request.setAttribute("planId", planId);

	return getDeveloperService().uploadTestCase(lCurrentUser, planId, implId, file);
    }

    @RequestMapping(value = "/myTasks", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse listImplementations(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	HashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return this.getDeveloperService().getUserTaskList(lCurrentUser, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/populateIBMSegment", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse populateIBMSegment(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId, @RequestBody List<GitSearchResult> pSegmentList) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return this.getDeveloperService().populateIBMSegment(lUser, implId, pSegmentList);
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteFile(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer[] ids) {
	User lUser = this.getCurrentUser(request, response);
	return getDeveloperService().deleteFile(lUser, ids);
    }

    @RequestMapping(value = "/getActivityLogList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getActivityLogList(@RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam String implId) throws IOException, WorkflowException {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getDeveloperService().getActivityLogList(implId, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/createWorkspace", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse createWorkspace(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	JSONResponse lResponse = getDeveloperService().createWorkspace(lUser, implId);
	if (lResponse.getStatus()) {
	    lResponse = getDeveloperService().getLatest(lUser, implId, "origin");
	    if (!lResponse.getStatus()) {
		lResponse.setErrorMessage("Workspace created successfully but unable to get the latest segments");
	    }
	}
	return lResponse;
    }

    @RequestMapping(value = "/validateMakFile", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse validateMakFile(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pSearchResult, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return getDeveloperService().validateMakFile(lUser, implId, pSearchResult);
    }

    @RequestMapping(value = "/developerGitRestore", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse developerGitRestore(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	JSONResponse lResponse = getDeveloperService().developerGitRestore(lUser, implId);
	return lResponse;
    }

    @RequestMapping(value = "/getGitRevision", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getGitRevision(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pSearchResult, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return getDeveloperService().getGitRevision(lUser, implId, pSearchResult);
    }

    @RequestMapping(value = "/getGIXML", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getGIXML(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	return getDeveloperService().createXMLforGIProject(lUser, implId);
    }

}
