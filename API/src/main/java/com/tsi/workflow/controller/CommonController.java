/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tsi.workflow.User;
import com.tsi.workflow.WFConfig;
import com.tsi.workflow.base.controller.CommonBaseController;
import com.tsi.workflow.beans.ui.TestSystemMaintenanceMailForm;
import com.tsi.workflow.git.IJGITSearchUtils;
import com.tsi.workflow.helper.GITHelper;
import com.tsi.workflow.helper.MaintenanceHelper;
import com.tsi.workflow.service.CommonService;
import com.tsi.workflow.service.GitHookUpdateService;
import com.tsi.workflow.service.LDAPService;
import com.tsi.workflow.service.ProtectedService;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.SortedSet;
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

@Controller
@RequestMapping("common")
public class CommonController extends CommonBaseController {

    @Autowired
    LDAPService lDAPService;

    @Autowired
    CommonService commonService;

    @Autowired
    GITHelper gitHelper;

    @Autowired
    WFConfig wfConfig;

    @Autowired
    IJGITSearchUtils jGITSearchUtils;

    @Autowired
    GitHookUpdateService gitHookUpdateService;

    @Autowired
    MaintenanceHelper maintenanceHelper;

    public MaintenanceHelper getMaintenanceHelper() {
	return maintenanceHelper;
    }

    @Autowired
    private ProtectedService protectedService;

    public ProtectedService getProtectedService() {
	return protectedService;
    }

    public IJGITSearchUtils getjGITSearchUtils() {
	return jGITSearchUtils;
    }

    public GitHookUpdateService getGitHookUpdateService() {
	return gitHookUpdateService;
    }

    public LDAPService getLDAPService() {
	return lDAPService;
    }

    public CommonService getCommonService() {
	return commonService;
    }

    public GITHelper getGitHelper() {
	return gitHelper;
    }

    @RequestMapping(value = "/getPlanStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getPlanStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.PlanStatus.getStatusMap());
	return response;
    }

    @RequestMapping(value = "/getAllPlanStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAllPlanStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.PlanStatus.getAllStatusMap());
	return response;
    }

    @RequestMapping(value = "/getAdvancedSearchPlanList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAdvancedSearchPlanList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	LinkedHashMap<String, String> planMap = Constants.PlanStatus.getAllStatusMap();
	planMap.put("PENDING", "Pending");
	response.setData(planMap);
	return response;
    }

    @RequestMapping(value = "/getImplementationStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementationStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.ImplementationStatus.getStatusMap());
	return response;
    }

    @RequestMapping(value = "/getImplementationSubStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getImplementationSubStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.ImplementationSubStatus.getStatusMap());
	return response;
    }

    @RequestMapping(value = "/getFileTypeByPlan", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getFileTypeByPlan(@RequestParam String[] ids) {
	return getCommonService().getFileTypeByPlan(ids);
    }

    @RequestMapping(value = "/getTagStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTagStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.TagStatus.getStatusMap());
	return response;
    }

    @RequestMapping(value = "/getUsersByRole", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getUsersByRole(@RequestParam String role) {
	return getLDAPService().getUsersByRole(role);
    }

    @RequestMapping(value = "/listSourceArtifactExtenstions", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSourceArtifactExtnList() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	HashMap<String, SortedSet<String>> lHashMap = new HashMap<>();
	lHashMap.put("IBM", Constants.SourceArtificatExtension.getIBMSourceArtificatExtList());
	lHashMap.put("NON_IBM", Constants.SourceArtificatExtension.getNONIBMSourceArtificatExtList());
	lResponse.setData(lHashMap);
	return lResponse;
    }

    @RequestMapping(value = "/getLoaderTypes", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoaderTypes() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(Constants.LoaderTypes.getLoaderTypesList());
	return lResponse;
    }

    @RequestMapping(value = "/getLocalConfigDetails", method = RequestMethod.GET)
    public void getLocalConfigDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String systemName) {
	String lDetails = getCommonService().getLocalConfigDetails(planId, systemName);
	try {
	    response.getWriter().write(lDetails);
	    response.getWriter().close();
	} catch (IOException ex) {
	    LOG.error("", ex);
	}
    }

    // /**
    // * Layouts :
    // *
    // * getTAPDetails?planId=T1800001
    // *
    // * getTAPDetails?planId=T1800001&systemName=APO
    // *
    // * getTAPDetails?systemName=APO&loadDate=20180101000000
    @RequestMapping(value = "/getTAPDetails", method = RequestMethod.GET)
    public void getTAPDetails(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String planId, @RequestParam(required = false) String systemName, @RequestParam(required = false) String loadDate) {
	String lDetails = getCommonService().getTAPDetails(planId, systemName, loadDate, false, false);
	try {
	    response.getWriter().write(lDetails);
	    response.getWriter().close();
	} catch (IOException ex) {
	    LOG.error("", ex);
	}
    }

    // /**
    // * Layouts :
    // *
    // * getTAPDetailsByPlan?id=T1800001
    @RequestMapping(value = "/getTAPDetailsByPlan", method = RequestMethod.GET)
    public void getTAPDetailsByPlan(HttpServletRequest request, HttpServletResponse response, @RequestParam String id) {
	String lDetails = getCommonService().getTAPDetails(id, null, null, false, false);
	try {
	    response.getWriter().write(lDetails);
	    response.getWriter().close();
	} catch (IOException ex) {
	    LOG.error("", ex);
	}
    }

    // /**
    // * Layouts :
    // *
    // * getFutureSecured?planId=T1800001
    // *
    // * getFutureSecured?planId=T1800001&systemName=APO
    // *
    // * getFutureSecured?systemName=APO&loadDate=20180101000000
    @RequestMapping(value = "/getFutureSecured", method = RequestMethod.GET)
    public void getFutureSecured(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String planId, @RequestParam(required = false) String systemName, @RequestParam(required = false) String loadDate, @RequestParam(required = false) boolean preProdAndAboveOnly) {
	String lDetails = null;
	if (wfConfig.getIsTravelportApp()) {
	    lDetails = getCommonService().getTAPDetails(planId, systemName, loadDate, true, preProdAndAboveOnly);
	} else {
	    lDetails = getCommonService().getTAPDetailsForDelta(planId, systemName, loadDate, true, preProdAndAboveOnly);
	}

	try {
	    response.getWriter().write(lDetails);
	    response.getWriter().close();
	} catch (IOException ex) {
	    LOG.error("", ex);
	}
    }

    @RequestMapping(value = "/getFileSyncInfo", method = RequestMethod.GET)
    public void getFileSyncInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String implId, @RequestParam String file, @RequestParam String systemName) {
	String lDetails = getCommonService().getFileSyncInfo(implId, file, systemName);
	try {
	    response.getWriter().write(lDetails);
	    response.getWriter().close();
	} catch (IOException ex) {
	    LOG.error("", ex);
	}
    }

    @ResponseBody
    @RequestMapping(value = "/FindAllRepos", method = RequestMethod.GET)
    public void FindAllRepos(HttpServletRequest request, HttpServletResponse response, @RequestParam String pCompany, @RequestParam String pFileFilter, @RequestParam String pBranch) {
	try {
	    StringBuilder builder = getCommonService().getAllNonProdSegments(pCompany, pFileFilter, pBranch);
	    if (builder.length() == 0) {
		response.getWriter().write("NOT FOUND");
	    } else {
		response.getWriter().write(builder.toString());
	    }
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("", ex);
	}
    }

    @RequestMapping(value = "/getLoadTypeList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadTypeList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.LoadTypes.getLoadTypesList());
	return response;
    }

    @RequestMapping(value = "/listNonIBMFilePaths", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse listNonIBMFilePaths() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(commonService.getNONIBMSourceArtificatExtListWithFilePath());
	return lResponse;
    }

    @RequestMapping(value = "/getLoadsetStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadsetStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.PROD_LOAD_STATUS.getProdLoadStatusList());
	return response;
    }

    @RequestMapping(value = "/getPutDeployDate", method = RequestMethod.GET)
    public void getPutDeployDate(HttpServletRequest request, HttpServletResponse response, @RequestParam String putName, @RequestParam String systemName) {
	String lDetails = getCommonService().getPutDeployDate(putName, systemName);
	try {
	    response.getWriter().write(lDetails);
	    response.getWriter().close();
	} catch (IOException ex) {
	    LOG.error("", ex);
	}
    }

    @RequestMapping(value = "/getSystemQATestingStatusList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getQATestingStatusList() {
	JSONResponse response = new JSONResponse();
	response.setStatus(Boolean.TRUE);
	response.setData(Constants.SYSTEM_QA_TESTING_STATUS.getSystemQATestingStatusList());
	return response;
    }

    @RequestMapping(value = "/getRepoPermission", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getRepoPermission() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(Constants.RepoPermission.getPermissionList(Boolean.TRUE));
	return lResponse;
    }

    @RequestMapping(value = "/getAllRepoPermission", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getAllRepoPermission() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(Constants.RepoPermission.getAllPermissionList());
	return lResponse;
    }

    @RequestMapping(value = "/getLoadAttendeeUsers", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getLoadAttendeeUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
	User lUser = this.getCurrentUser(request, response);
	return getLDAPService().getLoadAttendeeUsers(lUser);
    }

    @RequestMapping(value = "/publishMessage", method = RequestMethod.GET)
    @ResponseBody
    public void publishMessage(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String topic, @RequestParam(required = true) String message, @RequestParam(required = false, defaultValue = "*") String userId, @RequestParam(required = false, defaultValue = "*") String planId) throws Exception {
	getCommonService().publishMessage(topic, message, userId, planId.toUpperCase());
    }

    @RequestMapping(value = "/searchAllRepos", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse searchRepos(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String pCompany, @RequestParam(required = true) String pFileFilter, @RequestParam(required = false, defaultValue = "false") Boolean pMacroHeader) throws Exception {

	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	lResponse.setData(getjGITSearchUtils().SearchAllRepos(pCompany, pFileFilter, pMacroHeader, null, null));
	return lResponse;

    }

    @RequestMapping(value = "/gitDbUpdate", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse gitDbUpdate(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false, defaultValue = "") String ref_plan, @RequestParam(required = true) String ref_status, @RequestParam(required = false) String ref_load_date_time, @RequestParam(required = true) String source_commit_id, @RequestParam(required = false) String source_commit_date, @RequestParam(required = false, defaultValue = "") String derived_commit_id,
	    @RequestParam(required = false) String derived_commit_date, @RequestParam(required = true) String source_repo, @RequestParam(required = true) String file_name, @RequestParam(required = true) String target_system, @RequestParam(required = true, defaultValue = "false") Boolean isModify) throws Exception {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.FALSE);
	if (wfConfig.getGitSearchType().equals("GIT_DB")) {
	    lResponse = getGitHookUpdateService().gitDbUpdateProcess(ref_plan, ref_status, ref_load_date_time, source_commit_id, source_commit_date, derived_commit_id, derived_commit_date, source_repo, file_name, target_system.trim().toUpperCase(), isModify);
	}
	return lResponse;

    }

    @RequestMapping(value = "/generiAPI", method = RequestMethod.GET)
    public void genericAPI(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = true) String retriveType, @RequestParam(required = true) String targetSystem, @RequestParam(required = false) String impPlan, @RequestParam(required = false) String planStatusids, @RequestParam(required = false) String progName, @RequestParam(required = false) String progType, @RequestParam(required = false) String onlineVersion, @RequestParam(required = false) String fallbackVersion,
	    @RequestParam(required = false) String fromLoadDate, @RequestParam(required = false) String toLoadDate, @RequestParam(required = false) String funcArea, @RequestParam(required = false) String devId, @RequestParam(required = false) String devMangerId, @RequestParam(required = false) String outputPath, @RequestParam(required = false) Boolean planStatusList, @RequestParam(required = false) Boolean ldapIdList, @RequestParam(required = false) String userName,
	    @RequestParam(required = false) String role, @RequestParam(required = false, defaultValue = "N") String Dependent) {

	try {
	    // Parameter Key validation
	    Set<String> lParameterKeys = request.getParameterMap().keySet();
	    Boolean lParamValidation = Boolean.TRUE;

	    for (String lKey : lParameterKeys) {
		LOG.info("Generic API - Input Parameter Key - " + lKey);
		if (!Constants.GenericAPIParameterKeys.getValue().contains(lKey)) {
		    lParamValidation = Boolean.FALSE;
		    StringBuilder lResponseMsg = new StringBuilder();
		    lResponseMsg.append("Return Code: 1\n\n").append(lKey).append(" is not valid parameter.\n").append("Valid Parameters are - ").append(Constants.GenericAPIParameterKeys.getValue().toString());
		    response.getWriter().write(lResponseMsg.toString());
		}
	    }

	    if (lParamValidation) {

		JSONResponse lResponse = getCommonService().getSystemBasedMetadata(retriveType, targetSystem, impPlan, planStatusids, progName, progType, onlineVersion, fallbackVersion, fromLoadDate, toLoadDate, funcArea, devId, devMangerId, outputPath, planStatusList, ldapIdList, userName, role, Dependent);

		if (lResponse.getStatus()) {
		    response.getWriter().write(lResponse.getData().toString());
		} else {
		    response.getWriter().write("Error: " + lResponse.getErrorMessage());
		}
	    }
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("", ex);
	}
    }

    // ZTPFM-1867
    @ResponseBody
    @RequestMapping(value = "/getPutLevelInfo", method = RequestMethod.GET)
    public void getPutLevelInfo(HttpServletRequest request, HttpServletResponse response, @RequestParam String pCompany, @RequestParam String pSystem) {
	try {
	    StringBuilder builder = getCommonService().getPutLevelInfo(pCompany, pSystem);
	    if (builder.length() == 0) {
		response.getWriter().write("NO PUTLEVEL  FOUND");
	    } else {
		response.getWriter().write(builder.toString());
	    }
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("", ex);
	}
    }

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = Logger.getLogger(CommonController.class.getName());

    @RequestMapping(value = "/setMaintanance", method = RequestMethod.GET)
    @ResponseBody
    public void setMaintanance(HttpServletRequest request, HttpServletResponse response, @RequestParam Boolean flag) {
	LOG.info("Marking Maintanance Flag : " + flag);
	wfConfig.setMaintenance(flag);
    }

    @RequestMapping(value = "/setPrimary", method = RequestMethod.GET)
    public void setPrimary(HttpServletRequest request, HttpServletResponse response, @RequestParam Boolean pPrimary) {
	User lUser = this.getCurrentUser(request, response);
	LOG.info(lUser.getDisplayName() + " has changed the server as Primary <<-->> with the value as : " + pPrimary);
	wfConfig.setPrimary(pPrimary);
    }

    @RequestMapping(value = "/searchFileList", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse fileSearch(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileName, @RequestParam String implId, @RequestParam String targetSystem) {
	User lUser = this.getCurrentUser(request, response);
	JSONResponse prodFileSearch = getProtectedService().prodFileSearch(lUser, implId, fileName, false);
	return getCommonService().dofilterTest(prodFileSearch, targetSystem, implId);

    }

    @RequestMapping(value = "/getFuncPackSrcArtifacts", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getSegmentRepoDetails(@RequestParam(required = true) String repoName, @RequestParam(required = false) String progName, @RequestParam(required = false, defaultValue = "100") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonService().getFuncPackSrcArtifacts(repoName, progName, limit, offset, lOrderBy);
    }

    @RequestMapping(value = "/exportFuncPackSrcArtifacts", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse exportFuncPackSrcArtifacts(@RequestParam(required = true) String repoName, @RequestParam(required = false) String progName, @RequestParam(required = false, defaultValue = "0") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset, @RequestParam(value = "orderBy", required = false) String orderBy) throws Exception {
	LinkedHashMap lOrderBy = null;
	if (orderBy != null) {
	    lOrderBy = new ObjectMapper().readValue(orderBy, LinkedHashMap.class);
	}
	return getCommonService().exportFuncPackSrcArtifacts(repoName, progName, limit, offset, lOrderBy);
    }

    @RequestMapping(value = "/getTrackerData", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getTrackerData(@RequestParam String planId) throws Exception {
	return getCommonService().getTrackerData(planId);
    }

    @ResponseBody
    @RequestMapping(value = "/getNewFileList", method = RequestMethod.GET)
    public void getNewFileList(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String pSystem) {
	try {
	    StringBuilder builder = getCommonService().getNewFileList(planId, pSystem);
	    if (builder.length() == 0) {
		response.getWriter().write("NOT_FOUND");
	    } else {
		response.getWriter().write(builder.toString());
	    }
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("", ex);
	}
    }

    @ResponseBody
    @RequestMapping(value = "/getSystemDSL", method = RequestMethod.GET)
    public String getDslDetailBasedOnSystem(HttpServletRequest request, HttpServletResponse response, @RequestParam String systemName) {
	return getCommonService().getDslDetailBasedOnSystem(systemName);
    }

    @RequestMapping(value = "/sendGenericMail", method = RequestMethod.POST)
    @ResponseBody
    public void mnfExecutionMail(HttpServletRequest request, HttpServletResponse response, @RequestBody TestSystemMaintenanceMailForm testSystemMaintenanceMailForm) {
	getMaintenanceHelper().mnfExecutionMail(testSystemMaintenanceMailForm);

    }

    @ResponseBody
    @RequestMapping(value = "/removePlanFromPPRejectCache", method = RequestMethod.GET)
    public JSONResponse removePlanFromPPRejectCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String systemName, @RequestParam String planId) {
	return getCommonService().removePlanFromPPRejectCache(planId);
    }

    @ResponseBody
    @RequestMapping(value = "/planIdCleanupFromCache", method = RequestMethod.POST)
    public JSONResponse planIdCleanupFromCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String progressType, @RequestParam String planId) {
	return getCommonService().planIdCleanupFromCache(progressType, planId);
    }

    @ResponseBody
    @RequestMapping(value = "/apiports", method = RequestMethod.PUT)
    public void updateGIPorts(@RequestParam String userId, @RequestParam Integer portNo, @RequestParam String ipAddr) {
	getCommonService().updateGIPorts(userId, portNo, ipAddr);
    }

    @ResponseBody
    @RequestMapping(value = "/apiports", method = RequestMethod.GET)
    public JSONResponse getGIPorts(@RequestParam String userId) {
	return getCommonService().getGIPorts(userId);
    }

    @ResponseBody
    @RequestMapping(value = "/apiports", method = RequestMethod.DELETE)
    public void deletGIPorts(@RequestParam String userId) {
	getCommonService().deletGIPorts(userId);
    }

    @ResponseBody
    @RequestMapping(value = "/downloadFile", method = RequestMethod.GET)
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, @RequestParam String fileId) {
	getCommonService().downloadFile(fileId, response);
    }

    @ResponseBody
    @RequestMapping(value = "/FindAllProdRepos", method = RequestMethod.GET)
    public void FindAllProdRepos(HttpServletRequest request, HttpServletResponse response, @RequestParam String pCompany, @RequestParam String pFileFilter, @RequestParam String pBranch) {
	try {
	    StringBuilder builder = getCommonService().getAllProdSegments(pCompany, pFileFilter, pBranch);
	    response.getWriter().write(builder.toString());
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("Error in Searching File", ex);
	}
    }

    @ResponseBody
    @RequestMapping(value = "/getSystemBasedSegments", method = RequestMethod.GET)
    public void getSystemBasedSegments(HttpServletRequest request, HttpServletResponse response, @RequestParam String system) {
	try {
	    StringBuilder builder = getCommonService().getSystemBasedSegments(system);
	    response.getWriter().write(builder.toString());
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("Error in Searching File", ex);
	}
    }

    @ResponseBody
    @RequestMapping(value = "/getProdLoadsForUpdateplan", method = RequestMethod.GET)
    public JSONResponse getProdLoadsForUpdateplan(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) {
	return getCommonService().getProdLoadsForUpdateplan(planId);
    }

    @ResponseBody
    @RequestMapping(value = "/getSegListwithFunArea", method = RequestMethod.GET)
    public void getSegmentListwithFunArea(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId, @RequestParam String pSystem) {
	try {
	    StringBuilder builder = getCommonService().getFileList(planId, pSystem);
	    if (builder.length() == 0) {
		response.getWriter().write("NOT_FOUND");
	    } else {
		response.getWriter().write(builder.toString());
	    }
	    response.getWriter().close();
	} catch (Exception ex) {
	    LOG.error("", ex);
	}
    }

    // Only for Debugging
    @ResponseBody
    @RequestMapping(value = "/implIdCleanupFromCache", method = RequestMethod.GET)
    public JSONResponse implIdCleanupFromCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String impId) {
	return getCommonService().implIdCleanupFromCache(impId, "CHECKOUT_INPROGRESS");
    }

    // Only for Debugging
    @ResponseBody
    @RequestMapping(value = "/programCleanupFromCache", method = RequestMethod.GET)
    public JSONResponse programCleanupFromCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String programName) {
	return getCommonService().programCleanupFromCache(programName, "MOVE_ARTIFACT_INPROGRESS");
    }

    @RequestMapping(value = "/removePlanFromJenkinsCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse removePlanFromJenkinsCache(@RequestParam String type, @RequestParam String key, @RequestParam(required = false) String value) {
	return getCommonService().removePlanFromJenkinsCache(type, key, value);
    }

    @RequestMapping(value = "/updateBuildQueueInfo", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse updateBuildQueueInfo(@RequestParam String planId, @RequestParam String runStatus) {
	return getCommonService().updateBuildQueueInfo(planId, runStatus.toUpperCase());
    }

    @RequestMapping(value = "/deletedSegmentIds", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse deletedSegmentIds(@RequestParam String impl) {
	return getCommonService().deleteSegmentids(impl);
    }

    @RequestMapping(value = "/clearValidateMakFileCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse clearValidateMakCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String impl) {

	User lUser = this.getCurrentUser(request, response);
	return getCommonService().clearValidateMakCache(impl, lUser);
    }

    @RequestMapping(value = "/moveDerivedArtifactsToGit", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse moveDerivedArtifactsToGit(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) {

	User lUser = this.getCurrentUser(request, response);
	return getCommonService().moveDerivedArtifactsToGit(lUser, planId);
    }

    @RequestMapping(value = "/getBuildQueueCache", method = RequestMethod.GET)
    @ResponseBody
    public JSONResponse getBuildQueueCache(HttpServletRequest request, HttpServletResponse response, @RequestParam String planId) {

	User lUser = this.getCurrentUser(request, response);
	return getCommonService().getBuildQueueCache();
    }

}
