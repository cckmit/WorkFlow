app.factory('APIFactory', function (apiService) {

    var APIFactory = {};




    /*----------------------------------*/
    /*---------Definition---------------*/
    /*----------------------------------*/

    /*             Commons              */
    var setRole = function (paramObj, cbk) {
        var apiModule = "/setRole";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.setRole = setRole;

    var setDelegate = function (paramObj, cbk) {
        var apiModule = "/setDelegate";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.setDelegate = setDelegate;

    var saveDelegation = function (dataObj, cbk) {
        var apiModule = "/access/saveDelegation";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveDelegation = saveDelegation;

    var setSuperUser = function (dataObj, cbk) {
        var apiModule = "/access/setSuperUser";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.setSuperUser = setSuperUser;

    var getSettingsList = function (paramObj, cbk) {
        var apiModule = "/access/getSettingsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSettingsList = getSettingsList;

    var getPlanTrackStatus = function (paramObj, cbk) {
        var apiModule = "/common/getTrackerData";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanTrackStatus = getPlanTrackStatus;



    var getRepoList = function (paramObj, cbk) {
        var apiModule = "/toolAdmin/getRepoList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRepoList = getRepoList;

    var repoUpdateRepository = function (dataObj, cbk) {
        var apiModule = "/toolAdmin/updateRepository";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.repoUpdateRepository = repoUpdateRepository;

    var getRepositoryInfo = function (paramObj, cbk) {
        var apiModule = "/toolAdmin/getRepositoryInfo";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRepositoryInfo = getRepositoryInfo;

    var createRepo = function (paramObj, dataObj, cbk) {
        var apiModule = "/toolAdmin/createRepo";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.createRepo = createRepo;

    var getDelegationToUsersList = function (cbk) {
        var apiModule = "/getDelegationToUsersList";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getDelegationToUsersList = getDelegationToUsersList;

    var getDelegationFromUsersList = function (cbk) {
        var apiModule = "/getDelegationFromUsersList";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getDelegationFromUsersList = getDelegationFromUsersList;

    var getSuperUserFromUsersList = function (cbk) {
        var apiModule = "/getSuperUserFromUsersList";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSuperUserFromUsersList = getSuperUserFromUsersList;

    var getSystemList = function (cbk) {
        var apiModule = "/common/getSystemList";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemList = getSystemList;

    var updateSystem = function (dataObj, cbk) {
        var apiModule = "/access/updateSystem";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateSystem = updateSystem;

    var getSystemListByPlan = function (paramObj, cbk) {
        var apiModule = "/common/getSystemListByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemListByPlan = getSystemListByPlan;

    var getUsersByRole = function (paramObj, cbk) {
        var apiModule = "/common/getUsersByRole";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getUsersByRole = getUsersByRole;

    var getLoadAttendeeList = function (paramObj, cbk) {
        var apiModule = "/common/getLoadAttendeeUsers";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadAttendeeList = getLoadAttendeeList;

    var getSystemByPlanUrl = function () {
        return "/system/getSystemNameList";
    };

    /*      Implementation Plan         */

    var getPlanList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanList = getPlanList;

    var getInboxPlanList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getInboxPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getInboxPlanList = getInboxPlanList;

    var getCommonPlanList = function (paramObj, cbk) {
        var apiModule = "/common/getSearchViewPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getCommonPlanList = getCommonPlanList;

    var getCommonPlanListforImpl = function (paramObj, cbk) {
        var apiModule = "/common/getSearchViewImplementationList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getCommonPlanListforImpl = getCommonPlanListforImpl;

    var getStatusListForPlanUpdate = function (paramObj, cbk) {
        var apiModule = "/common/getProdLoadsForUpdateplan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getStatusListForPlanUpdate = getStatusListForPlanUpdate;

    var getSystemLoadByPlan = function (paramObj, cbk) {
        var apiModule = "/loadsControl/getSystemLoadByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemLoadByPlan = getSystemLoadByPlan;

    var getPlan = function (paramObj, cbk) {
        var apiModule = "/developerLead/getPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlan = getPlan;

    var getFileTypeByPlan = function (paramObj, cbk) {
        var apiModule = "/common/getFileTypeByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getFileTypeByPlan = getFileTypeByPlan;

    var getPlanApprovalsByPlan = function (paramObj, cbk) {
        var apiModule = "/common/getPlanApprovalsByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanApprovalsByPlan = getPlanApprovalsByPlan;

    var getPlatformList = function (paramObj, cbk) {
        var apiModule = "/common/getPlatformList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlatformList = getPlatformList;

    var getSystemByPlatform = function (paramObj, cbk) {
        var apiModule = "/common/getSystemByPlatform";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemByPlatform = getSystemByPlatform;

    var dlGetActivityLogList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getActivityLogList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.dlGetActivityLogList = dlGetActivityLogList;

    var dependenciesList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getDepencyPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.dependenciesList = dependenciesList;

    var checkForDvlBuild = function (paramObj, cbk) {
        var apiModule = "/developerLead/checkForDvlBuild";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.checkForDvlBuild = checkForDvlBuild;

    var getLoadBuildSystemList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getPdddsLibrary";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadBuildSystemList = getLoadBuildSystemList;

    var getSystemPdddsMappingList = function (paramObj, cbk) {
        var apiModule = "/common/getSystemPdddsMappingList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemPdddsMappingList = getSystemPdddsMappingList;

    var getProjectList = function (cbk) {
        cbk("/developerLead/getProjectList")
    };
    APIFactory.getProjectListUrl = getProjectList;

    var getProjectListForSearch = function (paramObj, cbk) {
        var apiModule = "/developerLead/getProjectList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProjectListForSearch = getProjectListForSearch;

    var getProjectListForDeltaDTN = function (paramObj, cbk) {
        var apiModule = "/developerManager/getProjectList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProjectListForDeltaDTN = getProjectListForDeltaDTN;

    var saveDTNDetails = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerManager/saveProject";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveDTNDetails = saveDTNDetails;

    var updateDTNDetails = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerManager/updateProject";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateDTNDetails = updateDTNDetails;

    var deleteDTNDetails = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerManager/deleteProject";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteDTNDetails = deleteDTNDetails;

    var getPlanStatusList = function (paramObj, cbk) {
        var apiModule = "/common/getPlanStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanStatusList = getPlanStatusList;

    var getPlanStatusListForLabel = function (paramObj, cbk) {
        var apiModule = "/common/getAllPlanStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanStatusListForLabel = getPlanStatusListForLabel;

    var getPlanStatusListForLabelForAdvancedSearch = function (paramObj, cbk) {
        var apiModule = "/common/getAdvancedSearchPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanStatusListForLabelForAdvancedSearch = getPlanStatusListForLabelForAdvancedSearch;

    var getSystemQATestingStatusList = function (paramObj, cbk) {
        var apiModule = "/common/getSystemQATestingStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemQATestingStatusList = getSystemQATestingStatusList;

    var savePlan = function (dataObj, cbk) {
        var apiModule = "/developerLead/savePlan";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.savePlan = savePlan;

    var leadDeletePlan = function (paramObj, cbk) {
        var apiModule = "/developerLead/deletePlan";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.leadDeletePlan = leadDeletePlan;

    var leadDeleteImplementation = function (paramObj, cbk) {
        var apiModule = "/developerLead/deleteImp";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.leadDeleteImplementation = leadDeleteImplementation;

    var updatePlan = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/updatePlan";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updatePlan = updatePlan;

    var getImplementationPlanFindUrl = function () {
        return "/common/getPlan";
    };


    var getProblemTicket = function (paramObj, cbk) {
        var apiModule = "/common/getProblemTicket";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProblemTicket = getProblemTicket;

    var uploadLoadApprovals = function (dataObj, cbk) {
        var apiModule = "/access/uploadLoadApprovals";
        var apiCall = apiService.upload(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.uploadLoadApprovals = uploadLoadApprovals;

    var saveLoadApprovals = function (dataObj, cbk) {
        var apiModule = "/access/saveLoadApprovals";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveLoadApprovals = saveLoadApprovals

    var updateLoadApprovals = function (dataObj, cbk) {
        var apiModule = "/access/updateLoadApprovals";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateLoadApprovals = updateLoadApprovals

    var deleteLoadApproval = function (paramObj, cbk) {
        var apiModule = "/access/deleteLoadApproval";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteLoadApproval = deleteLoadApproval

    var downloadLoadApproval = function (dataObj, cbk) {
        var apiModule = "/access/downloadLoadApproval";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.downloadLoadApproval = downloadLoadApproval

    var isSubmitReady = function (paramObj, cbk) {
        var apiModule = "/developerLead/isSubmitReady";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.isSubmitReady = isSubmitReady;

    var getSystemLoadListBySystemId = function (paramObj, cbk) {
        var apiModule = "/tsd/getSystemLoadListBySystemId";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemLoadListBySystemId = getSystemLoadListBySystemId;

    var getFallBackSystemLoadListBySystemId = function (paramObj, cbk) {
        var apiModule = "/tsd/getFallBackSystemLoadListBySystemId";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getFallBackSystemLoadListBySystemId = getFallBackSystemLoadListBySystemId;

    var tsdGetAuxLoads = function (paramObj, cbk) {
        var apiModule = "/tsd/getAuxLoads";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdGetAuxLoads = tsdGetAuxLoads;

    var tsdGetAuxPlanOpStatus = function (paramObj, cbk) {
        var apiModule = "/tsd/getAuxPlanOpStatus";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdGetAuxPlanOpStatus = tsdGetAuxPlanOpStatus;

    var planSubmit = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerLead/planSubmit";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.planSubmit = planSubmit

    var getProductionRepoList = function (paramObj, cbk) {
        var apiModule = "/developerManager/getProductionRepoList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProductionRepoList = getProductionRepoList;


    var getRepoPermission = function (paramObj, cbk) {
        var apiModule = "/common/getRepoPermission";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRepoPermission = getRepoPermission;

    var getAllRepoPermission = function (paramObj, cbk) {
        var apiModule = "/common/getAllRepoPermission";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAllRepoPermission = getAllRepoPermission;

    var updateRepoPermission = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerManager/updateRepoPermission";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateRepoPermission = updateRepoPermission;

    var setRepoOwnersPermission = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerManager/setRepoOwnersPermission";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.setRepoOwnersPermission = setRepoOwnersPermission;


    var specialPrivilegedUsers = function (paramObj, cbk) {
        var apiModule = "/developerManager/getProdRepoUsers";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.specialPrivilegedUsers = specialPrivilegedUsers;

    var tsdPostProdSystemLoad = function (paramObj, dataObj, cbk) {
        var apiModule = "/tsd/postProdSystemLoad";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdPostProdSystemLoad = tsdPostProdSystemLoad;

    var tsdMarkAuxAsOnline = function (paramObj, cbk) {
        var apiModule = "/tsd/markAuxAsOnline";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdMarkAuxAsOnline = tsdMarkAuxAsOnline;

    var tsdMarkAuxAsFallback = function (paramObj, cbk) {
        // var apiModule = "/tsd/markAuxAsFallback";
        var apiModule = "/tsd/markAuxAsOnlineRevert";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdMarkAuxAsFallback = tsdMarkAuxAsFallback;

    var tsdMarkOnline = function (paramObj, cbk) {
        var apiModule = "/developerManager/markOnline";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdMarkOnline = tsdMarkOnline;

    var macroHeaderFallback = function (paramObj, cbk) {
        var apiModule = "/access/macroHeaderFallback";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.macroHeaderFallback = macroHeaderFallback;

    var devmanagerRejectMacro = function (paramObj, cbk) {
        var apiModule = "/developerManager/macroHeaderReject";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.devmanagerRejectMacro = devmanagerRejectMacro;

    var tsdMacroHeaderList = function (paramObj, cbk) {
        var apiModule = "/developerManager/macroHeaderList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdMacroHeaderList = tsdMacroHeaderList;

    var devManagerfallbackMacroList = function (paramObj, cbk) {
        var apiModule = "/developerManager/getFallBackMacroHeaderPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.devManagerfallbackMacroList = devManagerfallbackMacroList;

    var fallbackMacroHeaderPlans = function (paramObj, cbk) {
        var apiModule = "/tsd/getFallBackMacroHeaderPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.fallbackMacroHeaderPlans = fallbackMacroHeaderPlans;

    var tssPostPreProdSystemLoad = function (paramObj, dataObj, cbk) {
        var apiModule = "/tss/postPreProdSystemLoad";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssPostPreProdSystemLoad = tssPostPreProdSystemLoad;

    var tssPostPreProdSystemAuxLoad = function (dataObj, cbk) {
        var apiModule = "/tss/postPreProdSystemAuxLoad";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssPostPreProdSystemAuxLoad = tssPostPreProdSystemAuxLoad;

    var getProductionLoads = function (paramObj, cbk) {
        var apiModule = "/tsd/getProductionLoads";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProductionLoads = getProductionLoads;

    var getFallbackProductionLoads = function (paramObj, cbk) {
        var apiModule = "/tsd/getFallbackProductionLoads";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getFallbackProductionLoads = getFallbackProductionLoads;

    var getPreProductionLoads = function (paramObj, cbk) {
        var apiModule = "/tss/getPreProductionLoads";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPreProductionLoads = getPreProductionLoads;

    var tsdGetLoadsToAccept = function (paramObj, cbk) {
        var apiModule = "/tsd/getLoadsToAccept";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdGetLoadsToAccept = tsdGetLoadsToAccept;

    var tsdGetFallBackLoadsToAccept = function (paramObj, cbk) {
        var apiModule = "/tsd/getFallBackLoadsToAccept";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdGetFallBackLoadsToAccept = tsdGetFallBackLoadsToAccept;

    var tsdGetFallBackLoads = function (paramObj, cbk) {
        var apiModule = "/tsd/getFallBackLoads";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdGetFallBackLoads = tsdGetFallBackLoads;

    var tsdSetOnline = function (paramObj, cbk) {
        var apiModule = "/tsd/setOnline";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdSetOnline = tsdSetOnline

    var tsdSetOnlineRCategory = function (paramObj, cbk) {
        var apiModule = "/tsd/acceptLoadset";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdSetOnlineRCategory = tsdSetOnlineRCategory

    var tsdSetFallBackRCategory = function (paramObj, cbk) {
        var apiModule = "/tsd/acceptFallbackLoadset";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdSetFallBackRCategory = tsdSetFallBackRCategory

    var tsdDoFTP = function (paramObj, cbk) {
        var apiModule = "/tsd/uploadFallbackLoadset";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdDoFTP = tsdDoFTP

    var tsdProdLoadRefresh = function (dataObj, cbk) {
        var apiModule = "/tsd/prodLoadRefresh";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdProdLoadRefresh = tsdProdLoadRefresh

    var tsdAcceptFallback = function (paramObj, cbk) {
        var apiModule = "/tsd/acceptFallback";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdAcceptFallback = tsdAcceptFallback

    var tsdAcceptMultiPlans = function (paramObj, cbk) {
        var apiModule = "/tsd/acceptMultiPlans";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdAcceptMultiPlans = tsdAcceptMultiPlans

    var isAcceptInProgress = function (paramObj, cbk) {
        var apiModule = "/tsd/isAcceptInProgress";
        var apiCall = apiService.get(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.isAcceptInProgress = isAcceptInProgress

    var tsdDeActivateFallback = function (paramObj, cbk) {
        var apiModule = "/tsd/deActivateFallback";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdDeActivateFallback = tsdDeActivateFallback

    var tsdGetSyncPlan = function (paramObj, cbk) {
        var apiModule = "/tsd/ImpPlanSyncEType";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdGetSyncPlan = tsdGetSyncPlan;


    var getSystemSyncByPlan = function (paramObj, cbk) {
        var apiModule = "/common/getSystemSyncByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemSyncByPlan = getSystemSyncByPlan;

    var setFallback = function (paramObj, cbk) {
        var apiModule = "/tsd/setFallback";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.setFallback = setFallback


    var tsdApplyEnabledAccept = function (paramObj, cbk) {
        var apiModule = "/tsd/enablePlanForAccept";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdApplyEnabledAccept = tsdApplyEnabledAccept

    var setPlanasPendingFallback = function (paramObj, cbk) {
        var apiModule = "/tsd/setPlanasPendingFallback";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.setPlanasPendingFallback = setPlanasPendingFallback

    var buildPlan = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/buildPlan";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.buildPlan = buildPlan;

    var getBuildByPlan = function (paramObj, cbk) {
        var apiModule = "/access/getBuildByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getBuildByPlan = getBuildByPlan;

    /**
     *
     *ZTPFM-2447 getRebuildStatus based on planId
     *
     */
    var getRebuildStatus = function (paramObj, cbk) {
        var apiModule = "/access/getRebuildStatus";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRebuildStatus = getRebuildStatus;

    /**
    *
    *ZTPFM-2447 getFullBuildStatus based on planId
    *
    */
    var getFullBuildStatus = function (paramObj, cbk) {
        var apiModule = "/access/getFullBuildStatus";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getFullBuildStatus = getFullBuildStatus;

    /**
     *
     *ZTPFM-2328 ,ZTPFM-2329 Dev Build Details Display
     *
     */
    var getDevBuildByPlan = function (paramObj, cbk) {
        var apiModule = "/access/getDevBuildByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getDevBuildByPlan = getDevBuildByPlan;
    /*
    ZTPFM-2328 get staging build by plan
    */

    var getStageBuildByPlan = function (paramObj, cbk) {
        var apiModule = "/access/getStageBuildByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getStageBuildByPlan = getStageBuildByPlan;

    var getInProgressBuildByPlan = function (paramObj, cbk) {
        var apiModule = "/developerLead/checkForBuildInProgress";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getInProgressBuildByPlan = getInProgressBuildByPlan;

    var getLoadsetStatusList = function (paramObj, cbk) {
        var apiModule = "/common/getLoadsetStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadsetStatusList = getLoadsetStatusList;

    var getDevLoadByPlan = function (paramObj, cbk) {
        var apiModule = "/access/getDevLoadByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getDevLoadByPlan = getDevLoadByPlan;

    var getVparsList = function (paramObj, cbk) {
        var apiModule = "/common/getVparsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getVparsList = getVparsList;

    var getIsPrivate = function (paramObj, cbk) {
        var apiModule = "/developerLead/isPrivateVpars";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getIsPrivate = getIsPrivate;

    var getSystemCpuList = function (paramObj, cbk) {
        var apiModule = "/common/getSystemCpuList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemCpuList = getSystemCpuList;
    /*
    ZTPFM-2329 , ZTPFM-2328 DevBuild log based on fileName
    */

    var getBuildLog = function (paramObj, cbk) {
        var apiModule = "/access/getBuildLog";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getBuildLog = getBuildLog;
    /*
       ZTPFM-2329 , ZTPFM-2328 StageBuild log based on fileName
       */
    var getStageBuildLog = function (paramObj, cbk) {
        var apiModule = "/access/getStageBuildLog";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getStageBuildLog = getStageBuildLog;

    var getLatestBuildLog = function (paramObj, cbk) {
        var apiModule = "/access/getLatestBuildLog";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLatestBuildLog = getLatestBuildLog;

    var cancelBuild = function (paramObj, cbk) {
        var apiModule = "/access/cancelBuild";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.cancelBuild = cancelBuild;

    var createLoaderFile = function (paramObj, cbk) {
        var apiModule = "/access/createLoaderFile";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.createLoaderFile = createLoaderFile;

    var rejectPlan = function (paramObj, cbk) {
        var apiModule = "/access/rejectPlan";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.rejectPlan = rejectPlan;

    // LEAD
    var leadGetDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.leadGetDeploymentPlanList = leadGetDeploymentPlanList;

    var leadGetAuxDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getAuxDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.leadGetAuxDeploymentPlanList = leadGetAuxDeploymentPlanList;

    var leadGetDeploymentVParsList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getDeploymentVParsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.leadGetDeploymentVParsList = leadGetDeploymentVParsList;

    var leadGetSystemLoadActions = function (paramObj, cbk) {
        var apiModule = "/developerLead/getSystemLoadActions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.leadGetSystemLoadActions = leadGetSystemLoadActions;

    // QA - Regression
    var qaGetRegressionDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getRegressionDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetRegressionDeploymentPlanList = qaGetRegressionDeploymentPlanList;

    // QA - mytask
    var qaGetmyTaskDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getQAFunctionalDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetmyTaskDeploymentPlanList = qaGetmyTaskDeploymentPlanList;

    var qaGetPassedRegressionDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getPassedRegressionDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetPassedRegressionDeploymentPlanList = qaGetPassedRegressionDeploymentPlanList;

    var qaGetRegressionAuxDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getRegressionAuxDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetRegressionAuxDeploymentPlanList = qaGetRegressionAuxDeploymentPlanList;

    var qaGetRegressionDeploymentVParsList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getRegressionDeploymentVParsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetRegressionDeploymentVParsList = qaGetRegressionDeploymentVParsList;

    var qaGetRegressionSystemLoadActions = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getRegressionSystemLoadActions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetRegressionSystemLoadActions = qaGetRegressionSystemLoadActions;

    // QA - Functional
    var qaGetFunctionalDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getFunctionalDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetFunctionalDeploymentPlanList = qaGetFunctionalDeploymentPlanList;

    var qaGetFunctionalAuxDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getFunctionalAuxDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetFunctionalAuxDeploymentPlanList = qaGetFunctionalAuxDeploymentPlanList;

    var qaGetFunctionalDeploymentVParsList = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getFunctionalDeploymentVParsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetFunctionalDeploymentVParsList = qaGetFunctionalDeploymentVParsList;

    var qaGetFunctionalSystemLoadActions = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/getFunctionalSystemLoadActions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.qaGetFunctionalSystemLoadActions = qaGetFunctionalSystemLoadActions;

    // TSS - YODA
    var tssGetYodaDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/tss/getYodaDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetYodaDeploymentPlanList = tssGetYodaDeploymentPlanList;

    var tssGetYodaAuxDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/tss/getYodaAuxDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetYodaAuxDeploymentPlanList = tssGetYodaAuxDeploymentPlanList;

    var tssGetYodaDeploymentVParsList = function (paramObj, cbk) {
        var apiModule = "/tss/getYodaDeploymentVParsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetYodaDeploymentVParsList = tssGetYodaDeploymentVParsList;

    var tssGetYodaSystemLoadActions = function (paramObj, cbk) {
        var apiModule = "/tss/getYodaSystemLoadActions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetYodaSystemLoadActions = tssGetYodaSystemLoadActions;

    // TSS - TOS
    var tssGetTosDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/tss/getTosDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetTosDeploymentPlanList = tssGetTosDeploymentPlanList;

    var tssGetTosAuxDeploymentPlanList = function (paramObj, cbk) {
        var apiModule = "/tss/getTosAuxDeploymentPlanList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetTosAuxDeploymentPlanList = tssGetTosAuxDeploymentPlanList;

    var tssGetTosDeploymentVParsList = function (paramObj, cbk) {
        var apiModule = "/tss/getTosDeploymentVParsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetTosDeploymentVParsList = tssGetTosDeploymentVParsList;

    var tssGetTosPreProdDeploymentVParsList = function (paramObj, cbk) {
        var apiModule = "/tss/getTosDeploymentPreProdVParsList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssGetTosPreProdDeploymentVParsList = tssGetTosPreProdDeploymentVParsList;

    //TSS - COMMON
    var getPlansDeployedInPreProdList = function (paramObj, cbk) {
        var apiModule = "/tss/getPlansDeployedInPreProdList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlansDeployedInPreProdList = getPlansDeployedInPreProdList;



    // Deployment screen end

    var getMyPlanTasks = function (paramObj, cbk) {
        var apiModule = "/developerManager/getMyPlanTasks";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getMyPlanTasks = getMyPlanTasks;

    var getApproveStatusByPlan = function (paramObj, cbk) {
        var apiModule = "/developerManager/getApproveStatusByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getApproveStatusByPlan = getApproveStatusByPlan;

    var getAssignedPlansAndSysLoad = function (paramObj, cbk) {
        var apiModule = "/developerManager/getAssignedPlansAndSysLoad";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAssignedPlansAndSysLoad = getAssignedPlansAndSysLoad;

    var getLoaderTypes = function (paramObj, cbk) {
        var apiModule = "/common/getLoaderTypes";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoaderTypes = getLoaderTypes;

    var getLoadTypeList = function (cbk) {
        var apiModule = "/common/getLoadTypeList";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadTypeList = getLoadTypeList;

    var getSystemLoadActionsByPlanIdAndVparId = function (paramObj, cbk) {
        var apiModule = "/access/getSystemLoadActions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSystemLoadActionsByPlanIdAndVparId = getSystemLoadActionsByPlanIdAndVparId;

    var doFTP = function (dataObj, cbk) {
        var apiModule = "/access/postTestSystemAuxLoad";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.doFTP = doFTP

    var postTestSystemLoad = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/postTestSystemLoad";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.postTestSystemLoad = postTestSystemLoad

    var postPreProdSystemLoad = function (dataObj, cbk) {
        var apiModule = "/tss/postPreProdSystemLoad";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.postPreProdSystemLoad = postPreProdSystemLoad

    var tssLoadAndActivateInTOS = function (dataObj, cbk) {
        var apiModule = "/tss/loadAndActivateInTOS";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tssLoadAndActivateInTOS = tssLoadAndActivateInTOS

    var tsdLoadAndActivateInTOS = function (dataObj, cbk) {
        var apiModule = "/tsd/loadAndActivateInTOS";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.tsdLoadAndActivateInTOS = tsdLoadAndActivateInTOS


    var deleteTestSystemLoad = function (paramObj, cbk) {
        var apiModule = "/access/deleteTestSystemLoad";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteTestSystemLoad = deleteTestSystemLoad;

    var deletePreProductionLoad = function (paramObj, cbk) {
        var apiModule = "/tss/deletePreProductionLoad";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.deletePreProductionLoad = deletePreProductionLoad;

    var dmApprovePlan = function (paramObj, dataObj, cbk) {
        var apiModule = "/developerManager/approvePlan";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.dmApprovePlan = dmApprovePlan

    var dmRejectPlan = function (paramObj, cbk) {
        var apiModule = "/developerManager/rejectPlan";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.dmRejectPlan = dmRejectPlan

    var approveProcessinBPMForPlan = function (paramObj, cbk) {
        var apiModule = "/developerManager/approveProcessinBPMForPlan";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.approveProcessinBPMForPlan = approveProcessinBPMForPlan

    var validateDbcr = function (paramObj, cbk) {
        var apiModule = "/developerLead/validateDbcr";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.validateDbcr = validateDbcr

    var saveDbcrList = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/saveDbcrList";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveDbcrList = saveDbcrList

    var deleteDbcr = function (paramObj, cbk) {
        var apiModule = "/access/deleteDbcr";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteDbcr = deleteDbcr

    var getDbcrList = function (paramObj, cbk) {
        var apiModule = "/developerLead/getDbcrList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getDbcrList = getDbcrList

    /*      Implementation              */
    var getImplementationList = function (paramObj, cbk) {
        var apiModule = "/developer/getImplementationList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getImplementationList = getImplementationList;

    var getInboxImplementationList = function (paramObj, cbk) {
        var apiModule = "/developer/getInboxImplementationList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getInboxImplementationList = getInboxImplementationList;

    var developerCommit = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/commit";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.developerCommit = developerCommit;

    var developerCheckin = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/checkin";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.developerCheckin = developerCheckin;

    var developerGetLatest = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/getLatest";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.developerGetLatest = developerGetLatest;

    var developerGitRestore = function (paramObj, cbk) {
        var apiModule = "/developer/developerGitRestore";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.developerGitRestore = developerGitRestore;

    var getGitRevision = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/getGitRevision";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getGitRevision = getGitRevision;

    var getSearchImplementationList = function (paramObj, cbk) {
        var apiModule = "/common/getImplementationList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSearchImplementationList = getSearchImplementationList;


    var getSegmentList = function (paramObj, cbk) {
        var apiModule = "/access/getSegmentMappingByImplementation";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSegmentList = getSegmentList;

    var getRFCProcessList = function (paramObj, cbk) {
        var apiModule = "/rfc/getRFCDetailsByPlanIds";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRFCProcessList = getRFCProcessList;

    var getConfigList = function (paramObj, cbk) {
        var apiModule = "/rfc/getRFCConfigValues";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getConfigList = getConfigList;

    var getupdateRFCDetail = function (paramObj, cbk) {
        var apiModule = "/rfc/updateRFCDetail";
        var apiCall = apiService.post(apiModule, {}, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getupdateRFCDetail = getupdateRFCDetail;

    var getRFCInboxPlans = function (paramObj, cbk) {
        var apiModule = "/rfc/getRFCInboxPlans";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRFCInboxPlans = getRFCInboxPlans;

    var rfcDetailsExportExcel = function (paramObj,dataObj, cbk) {
        var apiModule = "/rfc/exportRFCReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.rfcDetailsExportExcel = rfcDetailsExportExcel;

    var getRFCReport = function (paramObj, dataObj, cbk) {
        var apiModule = "/rfc/getRFCReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getRFCReport = getRFCReport;

    // var getRFCInboxPlans = function (paramObj, cbk) {
    //     var apiModule = "/rfc/getRFCInboxPlans";
    //     var apiCall = apiService.get(apiModule, paramObj, function (response) {
    //         cbk(response)
    //     })
    // };
    // APIFactory.getRFCInboxPlans = getRFCInboxPlans;

    var getImplementationByPlan = function (paramObj, cbk) {
        var apiModule = "/common/getImplementationByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getImplementationByPlan = getImplementationByPlan;

    var getImplementation = function (paramObj, cbk) {
        var apiModule = "/common/getImplementation";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getImplementation = getImplementation;

    var listTestCases = function (paramObj, cbk) {
        var apiModule = "/developer/listTestCases";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.listTestCases = listTestCases;

    var uploadTestCase = function (dataObj, cbk) {
        var apiModule = "/developer/uploadTestCase";
        var apiCall = apiService.upload(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.uploadTestCase = uploadTestCase;

    var deleteTestCase = function (paramObj, cbk) {
        var apiModule = "/developer/deleteTestCase";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteTestCase = deleteTestCase;

    var deleteAttachedFile = function (paramObj, cbk) {
        var apiModule = "/access/deleteAttachedFile";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteAttachedFile = deleteAttachedFile;

    var downloadTestCase = function (paramObj, cbk) {
        var apiModule = "/developer/downloadTestCase";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.downloadTestCase = downloadTestCase;

    var searchExportExcel = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/exportExcel";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.searchExportExcel = searchExportExcel;

    var searchDeploymentExportExcel = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/getDeploymentActivityInExcel";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.searchDeploymentExportExcel = searchDeploymentExportExcel;

    var devCheckoutFile = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/checkoutFile";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.devCheckoutFile = devCheckoutFile

    var clearValidateMakFileCacheutFile = function (paramObj, cbk) {
        var apiModule = "/common/clearValidateMakFileCache";
        var apiCall = apiService.get(apiModule, paramObj,function (response) {
            cbk(response)
        })
    };
    APIFactory.clearValidateMakFileCacheutFile = clearValidateMakFileCacheutFile
    
    var devGetMakFileList = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/validateMakFile";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.devGetMakFileList = devGetMakFileList

    var migrateNonIbmFile = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/migrateNonIbmFile";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.migrateNonIbmFile = migrateNonIbmFile

    var isOBS = function (cbk) {
        var apiModule = "/access/obsRepoInfo/";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.isOBS = isOBS

    var saveImplementation = function (dataObj, cbk) {
        var apiModule = "/developer/saveImplementation";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveImplementation = saveImplementation

    var updateImplementation = function (dataObj, cbk) {
        var apiModule = "/developer/updateImplementation";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateImplementation = updateImplementation;

    var requestPeerReview = function (paramObj, cbk) {
        var apiModule = "/developer/requestPeerReview";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.requestPeerReview = requestPeerReview;

    var revertImplementation = function (paramObj, cbk) {
        var apiModule = "/access/revertImplementation";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.revertImplementation = revertImplementation;

    var searchFile = function (paramObj, cbk) {
        var apiModule = "/access/searchFile";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.searchFile = searchFile;

    var planTestStatus = function (paramObj, cbk) {
        var apiModule = "/qualityAssurance/planTestStatus";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.planTestStatus = planTestStatus;

    var setReadyForQA = function (paramObj, cbk) {
        var apiModule = "/access/setReadyForQA";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.setReadyForQA = setReadyForQA

    var getAddTagUrl = function () {
        return "/addTag";
    };

    var deleteFile = function (paramObj, cbk) {
        var apiModule = "/developer/deleteFile";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteFile = deleteFile;

    var getCreateWorkspaceUrl = function (paramObj, cbk) {
        var apiModule = "/developer/createWorkspace";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getCreateWorkspaceUrl = getCreateWorkspaceUrl;

    var populateIBMSegment = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/populateIBMSegment";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.populateIBMSegment = populateIBMSegment;

    var createSourceArtifact = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/createSourceArtifact";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.createSourceArtifact = createSourceArtifact;

    var listSourceArtifactExtenstions = function (paramObj, cbk) {
        var apiModule = "/common/listSourceArtifactExtenstions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.listSourceArtifactExtenstions = listSourceArtifactExtenstions

    var listNonIBMFilePaths = function (paramObj, cbk) {
        var apiModule = "/common/listNonIBMFilePaths";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.listNonIBMFilePaths = listNonIBMFilePaths;

    var getImplementationSubStatusList = function (paramObj, cbk) {
        var apiModule = "/common/getImplementationSubStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getImplementationSubStatusList = getImplementationSubStatusList

    var getImplementationStatusList = function (paramObj, cbk) {
        var apiModule = "/common/getImplementationStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getImplementationStatusList = getImplementationStatusList

    var getTagStatusList = function (paramObj, cbk) {
        var apiModule = "/common/getTagStatusList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getTagStatusList = getTagStatusList

    /*          Reviewer          */


    var reviewerMyTasks = function (paramObj, cbk) {
        var apiModule = "/reviewer/myTasks";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.reviewerMyTasks = reviewerMyTasks;

    var reviewerHistory = function (paramObj, cbk) {
        var apiModule = "/reviewer/reviewerHistory";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.reviewerHistory = reviewerHistory;

    var approveReview = function (paramObj, cbk) {
        var apiModule = "/reviewer/approveReview";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.approveReview = approveReview;

    /*      Load Category               */
    var getLoadCategoriesList = function (paramObj, cbk) {
        var apiModule = "/loadsControl/getLoadCategoryList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadCategoriesList = getLoadCategoriesList;

    var getLoadFreezeDateByMonth = function (paramObj, cbk) {
        var apiModule = "/loadsControl/getLoadFreezeDateByMonth";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadFreezeDateByMonth = getLoadFreezeDateByMonth;

    var getLoadCategoriesByDate = function (paramObj, cbk) {
        var apiModule = "/loadsControl/getLoadCategoriesByDate";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadCategoriesByDate = getLoadCategoriesByDate;

    var getLoadWindowByDay = function (paramObj, cbk) {
        var apiModule = "/loadsControl/getLoadWindowByDay";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadWindowByDay = getLoadWindowByDay;

    var lcSetOnline = function (paramObj, cbk) {
        var apiModule = "/loadsControl/setOnline";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.lcSetOnline = lcSetOnline;

    var getLoadWindowList = function (paramObj, cbk) {
        var apiModule = "/common/getLoadWindowByLoadCategories";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadWindowList = getLoadWindowList;

    var getCpuListBySystemId = function (paramObj, cbk) {
        var apiModule = "/access/getCpuListBySystemId";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getCpuListBySystemId = getCpuListBySystemId;

    var getTOSServerListBySystemId = function (paramObj, cbk) {
        var apiModule = "/access/getTOSServerListBySystemId";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getTOSServerListBySystemId = getTOSServerListBySystemId;

    var saveLoadCategory = function (dataObj, cbk) {
        var apiModule = "/loadsControl/saveLoadCategory";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveLoadCategory = saveLoadCategory;

    var updateLoadCategory = function (dataObj, cbk) {
        var apiModule = "/loadsControl/updateLoadCategory";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateLoadCategory = updateLoadCategory;

    var deleteLoadCategory = function (paramObj, cbk) {
        var apiModule = "/loadsControl/deleteLoadCategory";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteLoadCategory = deleteLoadCategory;

    /*      Load FreezeDate             */
    var getLoadFreezeList = function (paramObj, cbk) {
        var apiModule = "/common/getGroupLoadFreezeList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadFreezeList = getLoadFreezeList;

    var getLoadCategoryBySystem = function (paramObj, cbk) {
        var apiModule = "/common/getLoadCategoriesBySystem";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadCategoryBySystem = getLoadCategoryBySystem;

    var getLoadFreezeByLoadCategories = function (paramObj, cbk) {
        var apiModule = "/common/getLoadFreezeByLoadCategories";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadFreezeByLoadCategories = getLoadFreezeByLoadCategories;

    var saveLoadFreeze = function (dataObj, cbk) {
        var apiModule = "/loadsControl/saveLoadFreeze";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveLoadFreeze = saveLoadFreeze;

    var updateLoadFreeze = function (dataObj, cbk) {
        var apiModule = "/loadsControl/groupUpdateLoadFreeze";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateLoadFreeze = updateLoadFreeze;

    var deleteLoadFreeze = function (dataObj, cbk) {
        var apiModule = "/loadsControl/deleteGroupLoadFreeze";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deleteLoadFreeze = deleteLoadFreeze;


    var getLoadsControlMyTasks = function (paramObj, cbk) {
        var apiModule = "/loadsControl/myTasks";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadsControlMyTasks = getLoadsControlMyTasks;

    var getLoadHistory = function (paramObj, cbk) {
        var apiModule = "/loadsControl/loadHistory";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getLoadHistory = getLoadHistory;

    var readyForProdDeploy = function (dataObj, cbk) {
        var apiModule = "/loadsControl/readyForProdDeploy";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.readyForProdDeploy = readyForProdDeploy;

    /*        My Tasks                  */

    var getDevTasks = function (paramObj, cbk) {
        var apiModule = "/developer/myTasks";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getDevTasks = getDevTasks;
    /*        zTPF Level                  */

    var getPutLevelList = function (paramObj, cbk) {
        var apiModule = "/loadsControl/getPutLevelList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPutLevelList = getPutLevelList;

    var getProdFTPIP = function (dataObj, cbk) {
        var apiModule = "/loadsControl/getProdFTPIP";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProdFTPIP = getProdFTPIP;

    var savePutLevel = function (dataObj, cbk) {
        var apiModule = "/loadsControl/savePutLevel";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.savePutLevel = savePutLevel;

    var updatePutLevel = function (dataObj, cbk) {
        var apiModule = "/loadsControl/updatePutLevel";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updatePutLevel = updatePutLevel;

    var deletePutLevel = function (paramObj, cbk) {
        var apiModule = "/loadsControl/deletePutLevel";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.deletePutLevel = deletePutLevel;

    var getPutLevelBySystem = function (paramObj, cbk) {
        var apiModule = "/common/getPutLevelBySystem";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPutLevelBySystem = getPutLevelBySystem;

    var getNonIBMPutLevelBySystem = function (paramObj, cbk) {
        var apiModule = "/access/getFuncAreaList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getNonIBMPutLevelBySystem = getNonIBMPutLevelBySystem;


    var getIBMPutLevelBySystem = function (paramObj, cbk) {
        var apiModule = "/access/getIBMFuncAreaList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getIBMPutLevelBySystem = getIBMPutLevelBySystem;

    /* get all FuncAreaList  */

    var getAllFuncAreaList = function (paramObj, cbk) {
        var apiModule = "/access/getAllFuncAreaList";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAllFuncAreaList = getAllFuncAreaList;

    /* get all FuncAreaList for report  */

    var getAllFuncAreaListBySysName = function (paramObj, cbk) {
        var apiModule = "/access/getAllFuncAreaListBySysName";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAllFuncAreaListBySysName = getAllFuncAreaListBySysName;



    /*    Manager                  */
    var getPlanTasksUrl = function () {
        return "/myPlanTasks";
    };

    /*  SO File Search              */

    var searchSharedObject = function (paramObj, cbk) {
        var apiModule = "/access/searchSharedObject";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.searchSharedObject = searchSharedObject;

    var advancedSearch = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/advancedSearch";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.advancedSearch = advancedSearch;

    var getSegmentActivityDetails = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/getSegmentActivityDetails";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSegmentActivityDetails = getSegmentActivityDetails;

    var getSourceArtifactSearch = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/sourceArtifactSearch";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSourceArtifactSearch = getSourceArtifactSearch;

    var getSegmentRepository = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/segmentRepoSearch";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getSegmentRepository = getSegmentRepository;

    var reviewAsCompleted = function (paramObj, dataObj, cbk) {
        var apiModule = "/reviewer/reviewSegments";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.reviewAsCompleted = reviewAsCompleted

    var loginURL = function () {
        return "/login";
    };
    APIFactory.loginURL = loginURL;

    var ssoLoginURL = function () {
        return "/ssoLogin";
    };
    APIFactory.ssoLoginURL = ssoLoginURL;

    var doLogout = function (cbk) {
        var apiModule = "/logout";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.doLogout = doLogout;

    var getAppInfo = function (paramObj, cbk) {
        var apiModule = "/WFInfo";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAppInfo = getAppInfo;

    var getFuncPackSrcArtifacts = function (paramObj, cbk) {
        var apiModule = "/common/getFuncPackSrcArtifacts";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.getFuncPackSrcArtifacts = getFuncPackSrcArtifacts


    var exportFuncPackSrcArtifacts = function (paramObj, cbk) {
        var apiModule = "/common/exportFuncPackSrcArtifacts";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.exportFuncPackSrcArtifacts = exportFuncPackSrcArtifacts

    //API for GI

    var getGIXML = function (paramObj, dataObj, cbk) {
        var apiModule = "/developer/getGIXML";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getGIXML = getGIXML;

    var getGIPort = function (paramObj, cbk) {
        var apiModule = "/common/apiports";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getGIPort = getGIPort;

    //ZTPFM-1692,1693 generate api.
    var exportReportByManager = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/exportUserReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.exportReportByManager = exportReportByManager

    //ZTPFM-1692,1693 generate api.
    var reportByManager = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/generateUserReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.reportByManager = reportByManager

    // ZTPFM - 2038 generate QA report
    var generateQAReport = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/generateQAReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.generateQAReport = generateQAReport

    // ZTPFM-2038 exportQA report
    var exportQAReport = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/exportQAReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.exportQAReport = exportQAReport

    var exportRepoReport = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/exportRepoReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.exportRepoReport = exportRepoReport

    var generateRepoReport = function (paramObj, dataObj, cbk) {
        var apiModule = "/access/generateRepoReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.generateRepoReport = generateRepoReport


    //ZTPFM-2275 Deployment Start and Stop Logic
    var deploymentStatusChange = function (paramObj, cbk) {
        var apiModule = "/access/deploymentStatusChange";
        var apiCall = apiService.post(apiModule, paramObj, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.deploymentStatusChange = deploymentStatusChange;

    var getLoginUserDetails = function (paramObj, cbk) {
        var apiModule = "/access/getLoginUserDetails";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.getLoginUserDetails = getLoginUserDetails;

    //ZTPFM-2224 story export
    var exportProjNbrDisplay = function (paramObj, cbk) {
        var apiModule = "/developerManager/exportDTNReport";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.exportProjNbrDisplay = exportProjNbrDisplay;

    //ZTPFM-2376 story
    var getSystemLoadByPlanAndSystem = function (paramObj, cbk) {
        var apiModule = "/access/getSystemLoadByPlanAndSystem";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.getSystemLoadByPlanAndSystem = getSystemLoadByPlanAndSystem;


    //ZTPFM-2500 story
    var getTSDAllowedPlans = function (paramObj, cbk) {
        var apiModule = "/tsd/getActionNotAllowedPlans";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.getTSDAllowedPlans = getTSDAllowedPlans;
    //ZTPFM-2533
    var getPlanStatusByImp = function (paramObj, cbk) {
        var apiModule = "/developerLead/getPlanStatusByImp";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getPlanStatusByImp = getPlanStatusByImp;

    //ZTPFM-2502
    var getAcceptedPlans = function (paramObj, cbk) {
        var apiModule = "/tsd/getAcceptedPlans";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAcceptedPlans = getAcceptedPlans;

    //ZTPFM-2502 TOS Report
    var getProdTOSActionReport = function (paramObj, cbk) {
        var apiModule = "/tsd/getProdTOSActionReport";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getProdTOSActionReport = getProdTOSActionReport;

    //ZTPFM-2502 TOS Report Catch clear
    var clearProdTOSLoadActCache = function (paramObj, cbk) {
        var apiModule = "/tsd/clearProdTOSLoadActCache";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.clearProdTOSLoadActCache = clearProdTOSLoadActCache;

    //ZTPFM-2502
    var getBuildQueueByPlan = function (paramObj, cbk) {
        var apiModule = "/access/getBuildQueueByPlan";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getBuildQueueByPlan = getBuildQueueByPlan;

    //ZTPFM-2599 System View

    var getTransactionInfo = function (dataObj, cbk) {
        var apiModule = "/audit/api/getTransactionInfo";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getTransactionInfo = getTransactionInfo;

    //ZTPFM-2599 user action API
    var getuserAcionAPI = function (cbk) {
        var apiModule = "/audit/common/getActionNameList";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getuserAcionAPI = getuserAcionAPI;

    //ZTPFM-2599 Update Switch ON/OFF
    var updateAuditSettings = function (paramObj, cbk) {
        var apiModule = "/audit/common/updateAuditSettings";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.updateAuditSettings = updateAuditSettings;

    //ZTPFM-2599 GET Switch ON/OFF
    var getAuditSettings = function (cbk) {
        var apiModule = "/audit/common/getAuditSettings";
        var apiCall = apiService.get(apiModule, {}, function (response) {
            cbk(response)
        })
    };
    APIFactory.getAuditSettings = getAuditSettings;

    //ZTPFM-2599 export system view performance.
    var exportSystemViewPerformance = function (paramObj, dataObj, cbk) {
        var apiModule = "/audit/common/extractSysViewReport";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response);
        })
    };
    APIFactory.exportSystemViewPerformance = exportSystemViewPerformance

    //ZTPFM-2599 CRUD Operation (GET)
    var getApiActions = function (paramObj, cbk) {
        var apiModule = "/audit/common/getApiActions";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.getApiActions = getApiActions;

    //ZTPFM-2599 CRUD Operation (CREATE, UPDATE)
    var saveApiActions = function (dataObj, cbk) {
        var apiModule = "/audit/common/saveApiActions";
        var apiCall = apiService.post(apiModule, {}, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.saveApiActions = saveApiActions;

    //ZTPFM-2599 CRUD Operation (REMOVE)
    var removeApiActions = function (paramObj, dataObj, cbk) {
        var apiModule = "/audit/common/removeApiActions";
        var apiCall = apiService.post(apiModule, paramObj, dataObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.removeApiActions = removeApiActions;

    //ZTPFM-2541
    var inprogressUserActioncheck = function (paramObj, cbk) {
        var apiModule = "/access/inprogressUserActioncheck";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.inprogressUserActioncheck = inprogressUserActioncheck;

    //ZTPFM-2755 check new file create flag in the catch
    var checkNewFileCreateFlag = function (paramObj, cbk) {
        var apiModule = "/developerManager/checkNewFileCreateFlag";
        var apiCall = apiService.get(apiModule, paramObj, function (response) {
            cbk(response)
        })
    };
    APIFactory.checkNewFileCreateFlag = checkNewFileCreateFlag;

    return APIFactory;

});