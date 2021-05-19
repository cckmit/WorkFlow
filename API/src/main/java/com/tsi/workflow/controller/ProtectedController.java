/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseController;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ImpPlanApprovals;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.ui.AdvancedSearchForm;
import com.tsi.workflow.beans.ui.DownloadApprovalDocumentForm;
import com.tsi.workflow.beans.ui.FileExtnReportForm;
import com.tsi.workflow.beans.ui.RepoReportView;
import com.tsi.workflow.beans.ui.RepoSearchForm;
import com.tsi.workflow.beans.ui.ReportForm;
import com.tsi.workflow.beans.ui.ReportQATestingData;
import com.tsi.workflow.beans.ui.ReportView;
import com.tsi.workflow.beans.ui.SegmentSearchForm;
import com.tsi.workflow.beans.ui.SourceArtifactSearchForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.service.TSDService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.text.ParseException;
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
@RequestMapping("access")
@ResponseBody
public class ProtectedController extends BaseController {

    @Autowired
    private ProtectedService protectedService;

    public ProtectedService getProtectedService() {
	return protectedService;
    }

    public void setProtectedService(ProtectedService protectedService) {
	this.protectedService = protectedService;
    }

    @Autowired
    TSDService tsdService;

    public TSDService getTSDService() {
	return tsdService;
    }

    @RequestMapping(value = "/setSuperUser", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse setSuperUser(HttpServletRequest request, HttpServletResponse response, @RequestBody UserSettings UserSettings) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().setSuperUser(lUser, UserSettings);
    }

    @RequestMapping(value = "/saveDelegation", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveDelegation(HttpServletRequest request, HttpServletResponse response, @RequestBody UserSettings UserSettings) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().saveDelegation(lUser, UserSettings);
    }

    @RequestMapping(value = "/uploadLoadApprovals", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse uploadExceptionLoadApproval(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam(required = false) MultipartFile file, @RequestParam(required = false) String approvalCmt) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().uploadExceptionLoadApproval(lUser, planId, file, approvalCmt);
    }

    @RequestMapping(value = "/saveLoadApprovals", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse saveLoadApprovals(HttpServletRequest request, HttpServletResponse response, @RequestBody ImpPlanApprovals impPlanApprovals) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().saveExceptionLoadApproval(lUser, impPlanApprovals);
    }

    @RequestMapping(value = "/updateLoadApprovals", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateLoadApprovals(HttpServletRequest request, HttpServletResponse response, @RequestBody ImpPlanApprovals impPlanApprovals) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().updateExceptionLoadApproval(lUser, impPlanApprovals);
    }

    @RequestMapping(value = "/deleteAttachedFile", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deleteAttachedFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String testFile) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().deleteAttachedFile(lUser, planId, testFile);
    }

    @RequestMapping(value = "/deleteLoadApproval", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deleteLoadApproval(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer approvalId) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().deleteLoadApproval(lUser, approvalId);
    }

    @RequestMapping(value = "/downloadLoadApproval", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse downloadLoadApproval(HttpServletRequest request, HttpServletResponse response, @RequestBody DownloadApprovalDocumentForm downloadApprovalDocumentForm) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().downloadLoadApproval(lUser, downloadApprovalDocumentForm.getPlanId(), downloadApprovalDocumentForm.getFileName());
    }

    @RequestMapping(value = "/getSegmentMappingByImplementation", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentMappingByImplementation(@RequestParam String[] ids) {
	return getProtectedService().getSegmentMappingByImplementation(ids);
    }

    @RequestMapping(value = "/setReadyForQA", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse setReadyForQA(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().setReadyForQA(lUser, implId);
    }

    @RequestMapping(value = "/searchSharedObject", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSharedObjects(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam("soName") String soName, @RequestParam("loadDate") String loadDate, @RequestParam("systemId") Integer systemId) throws Exception {

	User lUser = this.getCurrentUser(request, response);

	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getProtectedService().getSharedObjects(lUser, soName, loadDate, systemId, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/rejectPlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse rejectPlanAndDependentPlans(HttpServletRequest request, HttpServletResponse response, @RequestParam String impPlanId, @RequestParam(defaultValue = "") String rejectReason, @RequestParam(defaultValue = "true") boolean deleteLoadSetFlag) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().rejectPlanAndDependentPlans(lUser, impPlanId, rejectReason, deleteLoadSetFlag);
    }

    @RequestMapping(value = "/buildPlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse buildPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId, @RequestBody List<SystemLoad> loads, @RequestParam(defaultValue = "false") Boolean systemRemoveFlag, @RequestParam(defaultValue = "true") Boolean rebuildAll) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);
	request.setAttribute("planId", loads.get(0) == null ? "" : (loads.get(0).getPlanId() != null ? loads.get(0).getPlanId().getId() : ""));
	return getProtectedService().buildPlan(lUser, implId, loads, Constants.BUILD_TYPE.DVL_BUILD, systemRemoveFlag, rebuildAll);
    }

    @RequestMapping(value = "/getBuildLog", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildLog(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().retriveBuildLog(lUser, fileName);
    }

    @RequestMapping(value = "/getStageBuildLog", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse retriveStageBuildLog(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().retriveStageBuildLog(lUser, fileName);
    }

    @RequestMapping(value = "/getLatestBuildLog", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLatestFiveBuildLog(HttpServletRequest request, HttpServletResponse response, @RequestParam String systemName, @RequestParam String planId, @RequestParam String buildType) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().retriveLatestFiveBuildLog(lUser, planId, systemName, buildType);
    }

    @RequestMapping(value = "/cancelBuild", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse cancelBuild(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) throws IOException {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().cancelBuild(lUser, planId, Constants.BUILD_TYPE.DVL_BUILD);
    }

    @RequestMapping(value = "/getBuildByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildByPlan(@RequestParam String id) {
	return getProtectedService().getLatestBuildByPlan(id);
    }

    @RequestMapping(value = "/getDevBuildByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDevBuildByPlan(@RequestParam String id) {
	return getProtectedService().getLatestDevBuildByPlan(id);
    }

    @RequestMapping(value = "/getStageBuildByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getStageBuildByPlan(@RequestParam String id) {
	return getProtectedService().getLatestStageBuildByPlan(id);
    }

    @RequestMapping(value = "/createLoaderFile", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse createLoaderFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String loaderType) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);
	return getProtectedService().createLoaderFile(lUser, planId, loaderType, Constants.BUILD_TYPE.DVL_LOAD);
    }

    @RequestMapping(value = "/getSystemLoadListBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadListBySystemId(@RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, value = "orderBy") String orderBy, @RequestParam Integer id) throws IOException {
	// TODO: Need to move to TSD
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	return getProtectedService().getSystemLoadListBySystemId(id, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getCpuListBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getCpuListBySystemId(@RequestParam Integer id) throws IOException {
	return getProtectedService().getCpuListBySystemId(id);
    }

    @RequestMapping(value = "/getTOSServerListBySystemId", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTOSServerListBySystemId(@RequestParam Integer id, @RequestParam String type) throws IOException {
	return getProtectedService().getTOSServerListBySystemId(id, type);
    }

    @RequestMapping(value = "/updateSystem", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateSystem(@RequestBody System pSystem, HttpServletRequest request, HttpServletResponse response) throws IOException {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().updateSystem(lUser, pSystem);
    }

    @RequestMapping(value = "/postTestSystemLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse postTestSystemLoad(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SystemLoadActions> lLoadSets, @RequestParam(defaultValue = "false") Boolean skipPassed) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", lLoadSets.get(0) == null ? "" : (lLoadSets.get(0).getPlanId() != null ? lLoadSets.get(0).getPlanId().getId() : ""));

	JSONResponse deActivationResponse = getProtectedService().postActivationAction(lUser, lLoadSets, true, false, skipPassed);
	if (!deActivationResponse.getStatus()) {
	    return deActivationResponse;
	} else {
	    JSONResponse activationResponse = getProtectedService().postActivationAction(lUser, lLoadSets, false, false, skipPassed);
	    return activationResponse;
	}

    }

    @RequestMapping(value = "/postTestSystemAuxLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse postTestSystemAuxLoad(HttpServletRequest request, HttpServletResponse response, @RequestBody List<SystemLoadActions> lLoadSets) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", lLoadSets.get(0) == null ? "" : (lLoadSets.get(0).getPlanId() != null ? lLoadSets.get(0).getPlanId().getId() : ""));
	JSONResponse deActivationResponse = getProtectedService().postActivationAction(lUser, lLoadSets, false, true, false);
	return deActivationResponse;
    }

    @RequestMapping(value = "/deleteTestSystemLoad", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteTestSystemLoad(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer id) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().deleteActivationAction(lUser, id);
    }

    @RequestMapping(value = "/updatePlan", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updatePlan(HttpServletRequest request, HttpServletResponse response, @RequestBody ImpPlan plan, @RequestParam boolean warningFlag, @RequestParam boolean allowDateChange, @RequestParam(defaultValue = "") String loadTypeChangeComment) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", plan.getId());
	return getProtectedService().updatePlan(lUser, plan, warningFlag, allowDateChange, loadTypeChangeComment);
    }

    @RequestMapping(value = "/getSettingsList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSettingsList(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String userId, @RequestParam(required = false, defaultValue = "false") Boolean isToolAdmin) throws Exception {
	if (isToolAdmin) {
	    User lCurrentUser = new User(userId);
	    return getProtectedService().getSettingsListforSuperUser(lCurrentUser);
	} else if (userId != null) {
	    User lCurrentUser = new User(userId);
	    return getProtectedService().getSettingsList(lCurrentUser);
	} else {
	    User lCurrentUser = this.getCurrentUser(request, response);
	    return getProtectedService().getSettingsList(lCurrentUser);
	}
    }

    @RequestMapping(value = "/revertImplementation", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse revertImplementation(HttpServletRequest request, HttpServletResponse response, @RequestParam String impId) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", impId);
	return getProtectedService().revertImplementation(lUser, impId);
    }

    @RequestMapping(value = "/getDevLoadByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getDevLoadByPlan(@RequestParam String id) {
	return getProtectedService().getDevLoadByPlan(id);
    }

    @RequestMapping(value = "/saveDbcrList", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse updateDbcr(HttpServletRequest request, HttpServletResponse response, @RequestBody List<Dbcr> dbcrList) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().saveDbcrList(lUser, dbcrList);
    }

    @RequestMapping(value = "/deleteDbcr", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deleteDbcr(HttpServletRequest request, HttpServletResponse response, @RequestParam String dbcrId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().deleteDbcr(lUser, dbcrId);
    }

    @RequestMapping(value = "/advancedSearch", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getPlanByAdvancedSearch(@RequestBody AdvancedSearchForm searchForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	if (searchForm.getProgramName() != null) {
	    String programeName = searchForm.getProgramName().replace("*", "").trim().toLowerCase();
	    searchForm.setProgramName(programeName);
	}
	return getProtectedService().getPlanByAdvancedSearch(searchForm, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/exportExcel", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse advancedSearchExportExcel(@RequestBody AdvancedSearchForm searchForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	if (searchForm.getProgramName() != null) {
	    String programeName = searchForm.getProgramName().replace("*", "").trim().toLowerCase();
	    searchForm.setProgramName(programeName);
	}
	return getProtectedService().advancedSearchExportExcel(searchForm, limit, offset, lOrderBy);
    }

    @RequestMapping(value = "/getFuncAreaList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFuncAreaList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer[] ids) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().getFuncAreaList(lUser, ids);
    }

    @RequestMapping(value = "/getIBMFuncAreaList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getIBMFuncAreaList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer[] ids) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().getIBMFuncAreaList(lUser, ids);
    }

    @RequestMapping(value = "/searchFile", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse fileSearch(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName, @RequestParam String flag, @RequestParam String implId) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("impId", implId);

	if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_PRODFLAG)) {
	    return getProtectedService().prodFileSearch(lUser, implId, fileName, false);
	} else if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_PENDINGFLAG)) {
	    return getProtectedService().prodFileSearch(lUser, implId, fileName, true);
	} else if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_NONPRODFLAG)) {
	    return getProtectedService().nonProdFileSearch(lUser, implId, fileName);
	} else if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_IBMVANILLA)) {
	    return getProtectedService().ibmVanillaFileSearch(lUser, implId, fileName);
	} else if (flag.equalsIgnoreCase(Constants.FILE_MIG_NONIBM)) {
	    return getProtectedService().prodMigNonIBMSearchFile(lUser, fileName, flag);
	} else if (flag.equalsIgnoreCase(Constants.FILE_MIG_OBS)) {
	    return getProtectedService().prodMigNonIBMObsSearchFile(lUser, fileName, flag);
	} else if (flag.equalsIgnoreCase(Constants.FILE_SEARCH_COMMONFLAG)) {
	    return getProtectedService().commonFileSearch(lUser, implId, fileName);
	} else {
	    throw new WorkflowException("Not a valid option");
	}
    }

    @RequestMapping(value = "/migrateNonIbmFile", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse migrateNonIbmFile(HttpServletRequest request, HttpServletResponse response, @RequestBody List<GitSearchResult> pSearchResult, @RequestParam String implId) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().migrateNonIbmFile(lUser, pSearchResult);
    }

    @RequestMapping(value = "/obsRepoInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse obsRepoInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().obsRepoInfo(lUser);
    }

    @RequestMapping(value = "/getAllFuncAreaList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAllFuncAreaList(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer[] ids) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().getAllFuncAreaList(lUser, ids);
    }

    @RequestMapping(value = "/macroHeaderFallback", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse macroHeaderFallback(HttpServletRequest request, HttpServletResponse response, @RequestParam String id, @RequestParam String rejectReason) throws WorkflowException {
	User lCurrentUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", id);

	return getTSDService().markAuxAsFallback(lCurrentUser, id, rejectReason, Constants.FALLBACK_STATUS.ACCEPT_FALLBACK_LOADSET);
    }

    @RequestMapping(value = "/getSegmentActivityDetails", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getPassedRegressionDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestBody SegmentSearchForm segmentSearchForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy, @RequestParam(required = false, defaultValue = "") String filter) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getSegmentBasedActivity(lCurrentUser, segmentSearchForm, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/getDeploymentActivityInExcel", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getPassedRegressionDeploymentPlanList(HttpServletRequest request, HttpServletResponse response, @RequestBody SegmentSearchForm segmentSearchForm) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().getDeploymentActivitiesInExcel(lCurrentUser, segmentSearchForm);
    }

    @RequestMapping(value = "/sourceArtifactSearch", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getHistoricalVersionsBySourceArtifact(@RequestBody SourceArtifactSearchForm searchForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}

	return getProtectedService().getHistoricalVersionsBySourceArtifact(searchForm, offset, limit, lOrderBy);
    }

    @RequestMapping(value = "/segmentRepoSearch", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse getSegmentRepoDetails(@RequestBody RepoSearchForm repoForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) throws Exception {

	return getProtectedService().getSegmentRepoDetails(repoForm, limit, offset);
    }

    @RequestMapping(value = "/getLoginUserDetails", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoginUserDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] roles) {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().getLoginUserDetails(lUser, roles);
    }

    // ZTPFM-2275 Deployment Status Change
    @RequestMapping(value = "/deploymentStatusChange", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse deploymentStatusChange(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam(defaultValue = "") String deploymentStartAndStopReason) {
	User lUser = this.getCurrentUser(request, response);
	request.setAttribute("planId", planId);
	return getProtectedService().deploymentStatusChange(lUser, planId, deploymentStartAndStopReason);
    }

    /**
     * Created by : Vinoth Ponnurangan Date :08/12/2019 JIRA : 2037 Retrieving
     * Sorted RepoReport data
     */
    @RequestMapping(value = "/generateRepoReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse generateRepoReport(HttpServletRequest request, HttpServletResponse response, @RequestBody FileExtnReportForm fileExtnReportForm, @RequestParam(required = false, defaultValue = "10") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().generateRepoReport(lCurrentUser, fileExtnReportForm, limit, offset);
    }

    /**
     * Created by : Ramkumar Seenivasan Date :08/12/2019 JIRA : 2037 Exporting
     * RepoReport data
     */
    @RequestMapping(value = "/exportRepoReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse exportRepoReport(HttpServletRequest request, HttpServletResponse response, @RequestBody RepoReportView repoReportView) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().exportRepoReport(lCurrentUser, repoReportView);
    }

    @RequestMapping(value = "/generateUserReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse generateUserReport(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportForm userReportForm) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	if (userReportForm.getReportType().equalsIgnoreCase(Constants.report_type.USER_DEPLOYMENT.name())) {
	    return getProtectedService().generateUserReport(lCurrentUser, userReportForm);
	} else if (userReportForm.getReportType().equalsIgnoreCase(Constants.report_type.FUNC_PACKAGE.name())) {
	    return getProtectedService().generateFuncReport(lCurrentUser, userReportForm);
	} else if (userReportForm.getReportType().equalsIgnoreCase(Constants.report_type.QA_TESTING.name())) {
	    return getProtectedService().generateFuncReport(lCurrentUser, userReportForm);
	}
	return (new JSONResponse());
    }

    @RequestMapping(value = "/exportUserReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse exportUserReport(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportView userReportView) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	if (userReportView.getReportForm().getReportType().equalsIgnoreCase(Constants.report_type.USER_DEPLOYMENT.name())) {
	    return getProtectedService().exportUserReport(lCurrentUser, userReportView);
	} else if (userReportView.getReportForm().getReportType().equalsIgnoreCase(Constants.report_type.FUNC_PACKAGE.name())) {
	    return getProtectedService().exportFuncAreaReport(lCurrentUser, userReportView);
	}
	return (new JSONResponse());
    }

    @RequestMapping(value = "/getAllFuncAreaListBySysName", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAllFuncAreaListBySysName(HttpServletRequest request, HttpServletResponse response, @RequestParam String[] systems) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getProtectedService().getAllFuncAreaListBySysName(lUser, systems);
    }

    @RequestMapping(value = "/generateQAReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse generateQAReport(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportForm userReportForm) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().generateQAReport(lCurrentUser, userReportForm);
    }

    @RequestMapping(value = "/exportQAReport", method = RequestMethod.POST)
    @ResponseBody
    public JSONResponse exportQAReport(HttpServletRequest request, HttpServletResponse response, @RequestBody ReportQATestingData reportData) throws Exception {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().exportQAReport(lCurrentUser, reportData);
    }

    @RequestMapping(value = "/getRebuildStatus", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRebuildStatus(HttpServletRequest request, HttpServletResponse response, @RequestParam String pPlanId) {
	return getProtectedService().isRebuildAllowed(pPlanId);
    }

    @RequestMapping(value = "/getFullBuildStatus", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFullBuildStatus(HttpServletRequest request, HttpServletResponse response, @RequestParam String pPlanId) {
	return getProtectedService().getFullBuildStatus(pPlanId);
    }

    @RequestMapping(value = "/getSystemLoadByPlanAndSystem", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSystemLoadByPlanAndSystem(HttpServletRequest request, HttpServletResponse response, @RequestParam String pPlanId, @RequestParam Integer systemId, @RequestParam String loadDate) throws ParseException {
	return getProtectedService().getSystemLoadByPlanAndSystem(pPlanId, systemId, loadDate);
    }

    @RequestMapping(value = "/getBuildQueueByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildQueueByPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(required = false, defaultValue = "") String filter, @RequestParam(value = "orderBy", required = false) String orderBy) throws IOException, WorkflowException {
	HashMap lOrderBy = null;
	User lUser = this.getCurrentUser(request, response);
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getProtectedService().getBuildQueueByPlan(lUser, offset, limit, filter, lOrderBy);
    }

    @ResponseBody
    @RequestMapping(value = "/inprogressUserActioncheck", method = RequestMethod.GET)
    public JSONResponse inprogressActionUsercheck(HttpServletRequest request, HttpServletResponse response, @RequestParam String user) {
	return getProtectedService().inprogressUserActioncheck(user);
    }

    @RequestMapping(value = "/updateExistingPlanLeadEmail", method = RequestMethod.GET)
    public JSONResponse updateExistingPlanLeadEmail(HttpServletRequest request, HttpServletResponse response) {
	return getProtectedService().updateExistingPlanLeadEmail();
    }

    @ResponseBody
    @RequestMapping(value = "/prcheck", method = RequestMethod.GET)
    public JSONResponse prcheck(HttpServletRequest request, HttpServletResponse response, @RequestParam String prnumber) {
	return getProtectedService().prCheck(prnumber);
    }

    /**
     * Migration Query for Repo Desc update
     */
    @ResponseBody
    @RequestMapping(value = "/repoDescUpdate", method = RequestMethod.GET)
    public JSONResponse repoDescUpdate(HttpServletRequest request, HttpServletResponse response, @RequestParam String company) {
	User lCurrentUser = this.getCurrentUser(request, response);
	return getProtectedService().repoDescUpdate(company, lCurrentUser);
    }

}
