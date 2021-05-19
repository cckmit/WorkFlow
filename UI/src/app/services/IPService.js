app.service('IPService', function ($http, $q, $rootScope, appSettings, Flash, $location, $timeout, WFLogger, APIFactory, apiService,
  Upload, Toaster, Paginate, $mdDialog, WSService, IService, Access, $window, $state) {

  var IPService = {};
  var vm = {}
  vm.loaderFlag = false;
  vm.approvalsList = []
  vm.impPlanList = []
  vm.impPlan = {}
  vm.implementationList = []
  var apiBase = appSettings.apiBase;
  var onloadDeploymentList;
  Paginate.refreshScrolling();
  var wrapperPlanSave = function (planObj, vm) {
    var lPlanObj = angular.copy(planObj)
    lPlanObj = _.omit(lPlanObj, ["system", "systems", "projectName", "lobName", "projectManagerId", "isDeltaFlag", "projectManagerName", "projectSponsorId", "platformId", "platformName"])
    lPlanObj.systemLoadList = []
    for (systemId in planObj.system) {
      var sObj = {}
      sObj = _.extend(sObj, _.where(vm.allSystemInformationWithAdditionalParameters, {
        "id": planObj.system[systemId].id
      })[0])
      sObj = _.extend(sObj, {
        "id": planObj.system[systemId].id,
        "active": planObj.system[systemId].active,
        "planId": planObj.system[Object.keys(planObj.system)[0]].planId,
        "preload": planObj.system[systemId].preload,
        "iplRequired": planObj.system[systemId].iplRequired,
        "preloadDesc": planObj.system[systemId].preloadDesc ? planObj.system[systemId].preloadDesc : "",
        "preloadJust": planObj.system[systemId].preloadJust ? planObj.system[systemId].preloadJust : "",
        "loadAttendee": planObj.system[systemId].loadAttendee,
        "loadAttendeeContact": planObj.system[systemId].loadAttendeeContact,
        "loadAttendeeId": planObj.system[systemId].loadAttendeeId,
        "loadAttendeeContact": planObj.system[systemId].loadAttendeeContact ? planObj.system[systemId].loadAttendeeContact : "",
        "qaFunctionalTesters": planObj.system[systemId].qaFunctionalTesters,
        "loadInstruction": planObj.system[systemId].loadInstruction,
        "specialInstructionQA": planObj.system[systemId].specialInstructionQA,
        // "loadDateTime": moment(planObj.system[systemId].loadDateTime, appSettings.uiDTFormat).valueOf(),
        "loadDateTime": planObj.system[systemId].loadDateTime,
        "loadCategoryId": _.where(vm.loadWindowCategories[systemId], {
          "id": parseInt(planObj.system[systemId].loadCategoryId)
        })[0],

        "putLevelId": _.where(vm.putLevels[systemId], {
          "id": parseInt(planObj.system[systemId].putLevelId)
        })[0],
        "systemId": _.where(vm.systemsList, {
          "id": parseInt(systemId)
        })[0]
      })
      // var tempObj = {sytemLoad:null};
      // tempObj.sytemLoad = sObj;
      lPlanObj.systemLoadList.push(sObj)
    }
    return lPlanObj;
  }

  var wrapOnLoad = function (planObj, cbk) {
    var lPlanObj = angular.copy(planObj)
    lPlanObj = _.omit(lPlanObj, ["systemLoadList", "projectId"])

    lPlanObj.projectId = planObj.projectId.id
    lPlanObj.projectName = planObj.projectId.projectName
    lPlanObj.lobName = planObj.projectId.lineOfBusiness
    lPlanObj.projectManagerId = planObj.projectId.managerId
    lPlanObj.projectManagerName = planObj.projectId.managerName
    lPlanObj.isDeltaFlag = planObj.projectId.isDelta ? planObj.projectId.isDelta : false
    lPlanObj.projectSponsorId = planObj.projectId.sponsorId
    lPlanObj.maintanceFlag = planObj.projectId.maintanceFlag
    lPlanObj.system = {}
    lPlanObj.systems = {}
    angular.forEach(planObj.systemLoadList, function (sObj) {
      try {
        lPlanObj.system[sObj.systemLoad.systemId.id] = {
          "id": sObj.systemLoad.id,
          "active": sObj.systemLoad.active,
          "planId": sObj.systemLoad.planId,
          "loadCategoryId": sObj.systemLoad.loadCategoryId ? sObj.systemLoad.loadCategoryId.id : "",
          "loadAttendee": sObj.systemLoad.loadAttendee,
          "loadAttendeeId": sObj.systemLoad.loadAttendeeId,
          "qaFunctionalTesters": sObj.systemLoad.qaFunctionalTesters,
          "putLevelId": sObj.systemLoad.putLevelId.id,
          "preload": sObj.systemLoad.preload,
          "iplRequired": sObj.systemLoad.iplRequired,
          "preloadDesc": sObj.systemLoad.preloadDesc,
          "preloadJust": sObj.systemLoad.preloadJust,
          "loadInstruction": sObj.systemLoad.loadInstruction,
          "specialInstructionQA": sObj.systemLoad.specialInstructionQA,
          "loadAttendeeContact": sObj.systemLoad.loadAttendeeContact,
          "loadDateTime": sObj.systemLoad.loadDateTime ? Access.formatUIDate(moment(sObj.systemLoad.loadDateTime, appSettings.apiDTFormat)) : null,
          "allowPutLevelChange": sObj.allowPutLevelChange
        }
      } catch (err) { }
      lPlanObj.systems[sObj.systemLoad.systemId.id] = true
      lPlanObj.platformId = sObj.systemLoad.systemId.platformId.id
    })

    cbk(lPlanObj)
  }



  var structure = {
    "standardTypeByDate": [
      "loaddate",
      "loadCategoryId",
      "loadtime"
    ],
    "standardTypeByCategory": [
      "loadCategoryId",
      "loadCategoryDate",
      "loadtime"
    ],
    "exceptionType": [
      "exceptionLoaddate",
      "loadCategoryId",
      "exceptionLoadtime"
    ]
  }
  var selectBox = {
    "standardTypeByDate": [
      "loadWindowCategories",
      "loadTime"
    ],
    "standardTypeByCategory": [
      "loadWindowCategories",
      "loadTime"
    ],
    "exceptionType": [
      "loadWindowCategories"
    ]
  }
  var showHide = {
    "standardTypeByDate": [
      "showLoadDate",
      "showLoadCategory",
      "showLoadTime"
    ],
    "standardTypeByCategory": [
      "showLoadCategory",
      "showCategoryLoadDate",
      "showLoadTime"
    ],
    "exceptionType": [
      "showExceptionLoadDate",
      "showLoadCategory",
      "showExceptionLoadTime"
    ]
  }

  var clearFieldsOnChange = function (vm, systemId, key) {


    var clearModel = {
      "standardTypeByDate": [
        "loadCategoryId",
        "loadtime"
      ],
      "standardTypeByCategory": [
        "loadCategoryDate",
        "loadtime"
      ],
      "exceptionType": [
        "loadCategoryId",
      ],
      "standardTypeByLoadWindowChange": [
        "loadCategoryId",
        "loadCategoryDate",
        "loadDateTime"
      ]
    }
    var clearOnScreenValues = {
      "standardTypeByDate": [
        "loadWindowCategories",
        "loadTime"
      ],
      "standardTypeByCategory": [
        "loadTime"
      ],
      "exceptionType": [
        "loadWindowCategories"
      ]
    }

    for (scrVal in clearOnScreenValues[key]) {
      if (vm[clearOnScreenValues[key][scrVal]] && vm[clearOnScreenValues[key][scrVal]][systemId]) {
        vm[clearOnScreenValues[key][scrVal]][systemId] = []
      }
    }
    for (mdl in clearModel[key]) {
      if (vm.impPlan.system[systemId] && vm.impPlan.system[systemId][clearModel[key][mdl]]) {
        vm.impPlan.system[systemId][clearModel[key][mdl]] = ""
      }
    }
  }
  var alignStructure = function (vm, systemId, key, clearException, clearAllFields) {

    if (clearException) {
      for (vals in showHide[key]) {
        if (!vm[showHide[key][vals]]) {
          vm[showHide[key][vals]] = []
        }
        vm[showHide[key][vals]][systemId] = false;
      }
      return;
    }
    //Hide all Fields
    for (shKey in showHide) {
      for (vals in showHide[shKey]) {
        if (!vm[showHide[shKey][vals]]) {
          vm[showHide[shKey][vals]] = []
        }
        vm[showHide[shKey][vals]][systemId] = false;
      }
    }
    //Show selected fields
    for (vals in showHide[key]) {
      if (!vm[showHide[key][vals]]) {
        vm[showHide[key][vals]] = []
      }
      vm[showHide[key][vals]][systemId] = true;
    }

    if (clearAllFields) {
      //Clear fields Data
      for (kV in structure[key]) {
        if (vm.impPlan.system[systemId] && vm.impPlan.system[systemId][structure[key][kV]]) {
          vm.impPlan.system[systemId][structure[key][kV]] = ""
        }
      }
      //Clear select box list
      for (sB in selectBox[key]) {
        if (vm[selectBox[key][sB]] && vm[selectBox[key][sB]][systemId]) {
          vm[selectBox[key][sB]][systemId] = []
        }
      }
    }
  }

  var tableAttr = {};

  var getExpandView = function ($scope, vm) {
    expandViewObj = initExpandView($scope, vm)
    initVariables($scope, vm)
    _.extend($scope, expandViewObj.scope)
    _.extend(vm, expandViewObj.vm)
  }
  IPService.getExpandView = getExpandView

  function initVariables($scope, vm) {
    vm.buildInProgress = {}
    vm.buildResult = {}
    vm.stageBuildResult = {}
    vm.buildTriggered = {}
    vm.buildStatus = {}
    vm.loaderList = {}
    vm.loaderFileStatus = {}
    vm.disableOLDR = {}
    /* Activity Log Init */
    $scope.sortColumnV2 = {}
    $scope.tableConfigV2 = {}
    $scope.pageSizeListV2 = {}
    /* Dependancy Log Init */
    $scope.sortColumnV3 = {}
    $scope.tableConfigV3 = {}
    $scope.pageSizeListV3 = {}
    vm.rebuildAllFlag = true;
    vm.disbaleDevlBuild = true;
    vm.rebuildDisableFlag = true;
  }

  var initPlan = function ($scope, vm) {


    initVariables($scope, vm)

    /*  */
    expandViewObj = initExpandView($scope, vm)
    _.extend($scope, expandViewObj.scope)
    _.extend(vm, expandViewObj.vm)

    /* Pagination Table Starts */
    var columnsToBeSorted = ["id", "loadType", "planStatus", "planDesc", "loaddatetime", "createdBy"]
    var tableAttr;
    $scope.recentPage = 0
    var initTableSettings = function () {
      $scope.tableConfig = Paginate.tableConfig()
      if ($scope.role == "LoadsControl" || $scope.role == "SystemSupport" || $scope.userRole == 'SystemSupport') {
        // $scope.tableDefaultValue = Paginate.defaultPageValue()
        $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for LC
        $scope.pageSizeList = Paginate.pageSizeList()
      } else {
        $scope.pageSizeList = Paginate.pageSizeList()
      }
      tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
      $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
      if (Access.getSearchData().recentPage && $scope.enableRecentPage) {
        $scope.tableConfig.currentPage = Access.getSearchData().recentPage + 1
      }
    }
    $rootScope.initTableSettings = function () {
      $scope.tableConfig = Paginate.tableConfig()
      if ($scope.role == "LoadsControl" || $scope.role == "SystemSupport" || $scope.userRole == 'SystemSupport') {
        // $scope.tableDefaultValue = Paginate.defaultPageValue()
        // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for LC
        $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for LC
        $scope.pageSizeList = Paginate.pageSizeList()
      } else {
        $scope.pageSizeList = Paginate.pageSizeList()
      }
      tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
      $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
      if (Access.getSearchData().recentPage && $scope.enableRecentPage) {
        $scope.tableConfig.currentPage = Access.getSearchData().recentPage + 1
      }
    }
    initTableSettings()
    var isPopupShown = false;
    var yesChanged = false;
    $scope.refresh = function (recentPage) {
      $scope.enableRecentPage = false;
      initTableSettings()
      $scope.enableRecentPage = true;
      if (recentPage) {
        $scope.tableConfig.currentPage = recentPage + 1
        tableAttr.offset = recentPage
      }
      isPopupShown = false;
      yesChanged = false;
      tableAttr.filter = vm.filterText ? vm.filterText : ""
      if (vm.searchPlanData) {
        tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
      }
      if (vm.searchPlanDataforLead) {
        tableAttr.planFilter = vm.searchPlanDataforLead ? vm.searchPlanDataforLead : ""
      }
      tableAttr.offset = $rootScope.paginateValue
      loadPlanList(tableAttr)
    }

    $scope.refreshForDeployment = function (recentPage) {
      $scope.enableRecentPage = false;
      initTableSettings()
      $scope.enableRecentPage = true;
      if (recentPage) {
        $scope.tableConfig.currentPage = recentPage + 1
        tableAttr.offset = recentPage
      }
      isPopupShown = false;
      yesChanged = false;
      tableAttr.filter = vm.filterText ? vm.filterText : ""
      if (vm.searchPlanData) {
        tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
      }
      if (vm.searchPlanDataforLead) {
        tableAttr.planFilter = vm.searchPlanDataforLead ? vm.searchPlanDataforLead : ""
      }
      tableAttr.offset = 0
      loadPlanList(tableAttr)
    }

    $rootScope.refresh = function (recentPage) {
      $scope.enableRecentPage = false;
      initTableSettings()
      $scope.enableRecentPage = true;
      if (recentPage) {
        $scope.tableConfig.currentPage = recentPage + 1
        tableAttr.offset = recentPage
      }
      isPopupShown = false;
      yesChanged = false;
      tableAttr.filter = vm.filterText ? vm.filterText : ""
      if (vm.searchPlanData) {
        tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
      }
      tableAttr.offset = $rootScope.paginateValue
      loadPlanList(tableAttr)
    }
    $scope.refreshData = function (recentPage) {
      $scope.enableRecentPage = false;
      initTableSettings()
      $scope.enableRecentPage = true;
      if (recentPage) {
        $scope.tableConfig.currentPage = recentPage + 1
        tableAttr.offset = recentPage
      }
      isPopupShown = false;
      yesChanged = false;
      tableAttr.filter = vm.filterText ? vm.filterText : ""
      vm.searchPlanData = ""
      vm.searchPlanDataforLead = ""
      tableAttr.offset = $rootScope.paginateValue
      // loadPlanList(tableAttr)
    }

    $scope.refreshDatas = function () {
      $rootScope.refresh()
      vm.searchPlanDataforLead = ""
    }

    $scope.searchPlanData = function (searchData) {
      $scope.enableRecentPage = false;
      initTableSettings()
      $scope.enableRecentPage = true;
      // if (recentPage) {
      //     $scope.tableConfig.currentPage = recentPage + 1
      //     tableAttr.offset = recentPage
      // }
      isPopupShown = false;
      yesChanged = false;
      tableAttr.filter = searchData ? searchData : ""
      tableAttr.offset = 0;
      loadPlanList(tableAttr)
    }
    $scope.searchPlanDataforLead = function (searchData) {
      $scope.enableRecentPage = false;
      initTableSettings()
      $scope.enableRecentPage = true;
      isPopupShown = false;
      yesChanged = false;
      tableAttr.planFilter = searchData ? searchData : ""
      if (vm.filterText) {
        tableAttr.filter = vm.filterText ? vm.filterText : ""
      }
      // vm.searchPlanDataforLead = ""
      vm.searchPlanData = ""
      vm.searchStatus = ""
      tableAttr.offset = 0;
      loadPlanList(tableAttr)
    }

    $scope.switchPageSize = function () {
      /** if ($scope.qaDeploypmentScreen) {
         if (!angular.equals(onloadDeploymentList, vm.deploymentList)) {
           isPopupShown = true
           if (!UnsavedChangesConfirm()) {
             $scope.tableConfig.pageSize = tableAttr.limit;
             return
           }
         }
       }
  */
      // tableAttr.offset = 0
      // tableAttr.limit = $scope.tableConfig.pageSize
      // $scope.tableConfig.currentPage = 1
      // Paginate.switchPageSize($scope, tableAttr)
      tableAttr.offset = 0
      tableAttr.limit = $scope.tableConfig.pageSize;
      $scope.tableConfig.currentPage = 1
      localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
      tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
      $rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
      // loadPlanList(tableAttr)
    }


    var isTriggerd = false;
    // var tempLastPage;
    $scope.pageChangeHandler = function (num) {
      if (vm.loadCategoryList && $scope.tableConfig.lastLoadedPage === num) {
        return;
      }
      /**
            if ($scope.qaDeploypmentScreen) {
              if (isTriggerd) {
                if (onloadDeploymentList)
                  if (!angular.equals(onloadDeploymentList, vm.deploymentList) && !yesChanged && !isPopupShown) {
                    yesChanged = true;
                    if (!confirm("Unsaved changes are present. Would you like to proceed?")) {
                      if ($scope.tableConfig.lastLoadedPage == null) {
                        $scope.tableConfig.currentPage = 1;
                      } else {
                        $scope.tableConfig.currentPage = $scope.tableConfig.lastLoadedPage;
                      }
                      return
                    } else {
                      yesChanged = false;
                      isPopupShown = false;
                    }
                  }
                if (yesChanged) {
                  yesChanged = false;
                  isPopupShown = false;
                  return;
                }
              } else {
                isTriggerd = true;
              }
            }
      */
      $scope.tableConfig.lastLoadedPage = num
      tableAttr.offset = num - 1
      $scope.recentPage = tableAttr.offset
      Access.getSearchData().recentPage = tableAttr.offset

      localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
      tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
      $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))

      tableAttr.filter = vm.filterText ? vm.filterText : ""
      if (vm.searchPlanData) {
        tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
      }
      loadPlanList(tableAttr)
    };

    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
      var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
      tableAttr = lSort.tableAttr
      var previousSortImpPlan = {};
      previousSortImpPlan = tableAttr.orderBy;
      Access.getSearchData().previousSortImpPlan = previousSortImpPlan;
      $scope.tableConfig = lSort.tableConfig
      $scope.sortColumn = lSort.sortColumn
      loadPlanList(tableAttr)
    }

    function UnsavedChangesConfirm() {
      return confirm("Unsaved changes are present. Would you like to proceed?")
    }

    $scope.getSelectedVPARS = function (vpList, savedDActions) {
      var returnList = []
      _.each(savedDActions, function (sAct) {
        _.each(vpList, function (vpObj) {
          if (sAct.vparId.id == vpObj.id) {
            returnList.push(vpObj.name)
          }
        })
      })
      return returnList.join(",")
    }

    $scope.getSelectedLoadDT = function (sId, systemLoadList) {
      var selectedLoadDT = "-"
      _.each(systemLoadList, function (slObj) {
        if (slObj.systemId.id == sId) {
          selectedLoadDT = slObj.loadDateTime
        }
      })
      return selectedLoadDT
    }

    function buildDeploymentRecords(vm) {
      _.each(vm.deploymentList, function (dObj) {
        if (!dObj.extraParams) {
          dObj.extraParams = {}
        }
        dObj.extraParams.qaTemplate = []
        dObj.extraParams.qaTemplate[dObj.id] = []
        var eachSysVpar = []
        var deploymentActions = []
        var systemDetailsObj = {}
        var tempVpar = []
        var slInfo = {}
        if (dObj.systemLoadList != null) {
          _.each(dObj.systemLoadList, function (sObj) {
            slInfo[sObj.systemId.name] = sObj
            eachSysVpar = _.filter(dObj.vparList, function (vObj) {
              return vObj.systemId.id == sObj.systemId.id
            })
            if (dObj.actionList && dObj.actionList.length > 0) {
              deploymentActions = _.filter(dObj.actionList, function (aObj) {
                return aObj.systemId.id == sObj.systemId.id
              })
              if (eachSysVpar.length > 0) {
                var preSelectedVpars = _.pluck(_.pluck(deploymentActions, "vparId"), "id")
                _.each(eachSysVpar, function (v) {
                  if (preSelectedVpars.indexOf(v.id) < 0) {
                    tempVpar.push(v)
                  }
                })
              }
              eachSysVpar = tempVpar
            }
            systemDetailsObj = {
              "systemVparList": eachSysVpar,
              "systemId": sObj.systemId.id,
              "system_id": "",
              "vpar_id": "",
              "systemLoad": slInfo,
              "systemName": sObj.systemId.name,
              "prevDeploymentActions": deploymentActions,
              "qaBypass": sObj.qaBypassStatus
            }
            dObj.extraParams.qaTemplate.push(systemDetailsObj)

          })
        } else {
          // $scope.refreshData();

        }
      })
      vm.updateFilteredVparList = function (planObj, systemId) {
        // planObj = angular.copy(planObj)
        planObj.vpar_id = ""
        planObj.loadandactivate = ""
        if (systemId && planObj.extraParams.qaTemplate.length > 0) {
          planObj.selectVparList = _.where(planObj.extraParams.qaTemplate, {
            "systemId": parseInt(systemId)
          })[0].systemVparList
          if (getUserData("userRole") == "QA") {
            try {
              var ltvustId = '#ltcustVpar_' + planObj.id + '_' + systemId;
              initMultipleSelect2(ltvustId)
              $timeout(function () {
                $(ltvustId).select2({
                  maximumSelectionLength: 1
                });
              }, 50)
            } catch (err) { }
          }

        } else {
          planObj.selectVparList = []
        }
      }
      vm.selectAll = function (actionRootObj, action) {
        if (action == "loadandactivate") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if ((actionObj.status == null || actionObj.status == "DELETED") && actionObj.disableLoadActionDueToTSSDeploy != true) {
              actionObj[action] = actionRootObj[action]
            }
          })
        }
        if (action == "activate") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if (actionObj.disableLoadActionDueToTSSDeploy != true && (actionObj.status == "LOADED" || actionObj.status == "DEACTIVATED")) {
              actionObj[action] = actionRootObj[action]
            }
          })
        }
        if (action == "deactivate") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if (actionObj.disableLoadActionDueToTSSDeploy != true && actionObj.status == "ACTIVATED") {
              actionObj[action] = actionRootObj[action]
            }
          })
        }
        if (action == "deactivateanddelete") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if (actionObj.disableLoadActionDueToTSSDeploy != true && (actionObj.status == "ACTIVATED" || actionObj.status == "DEACTIVATED")) {
              actionObj[action] = actionRootObj[action]
            }
          })
        }
      }

      vm.showCheckbox = function (actionRootObj, action) {
        var showIt = false
        if (action == "loadandactivate") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if ((actionObj.status == null || actionObj.status == "DELETED") && actionObj.disableLoadActionDueToTSSDeploy != true) {
              showIt = true
            }
          })
        }
        if (action == "activate") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if ((actionObj.status == "LOADED" || actionObj.status == "DEACTIVATED") && actionObj.disableLoadActionDueToTSSDeploy != true) {
              showIt = true
            }
          })
        }
        if (action == "deactivate") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if (actionObj.status == "ACTIVATED" && actionObj.disableLoadActionDueToTSSDeploy != true) {
              showIt = true
            }
          })
        }
        if (action == "deactivateanddelete") {
          _.each(actionRootObj.prevDeploymentActions, function (actionObj) {
            if ((actionObj.status == "ACTIVATED" || actionObj.status == "DEACTIVATED") && actionObj.disableLoadActionDueToTSSDeploy != true) {
              showIt = true
            }
          })
        }
        return showIt
      }
    }

    /* Pagination Table Ends */

    function loadPlanList(tableAttr) {
      yesChanged = false;
      if ($scope.role == "devManager") {
        $scope.tableConfig.totalItems = 0
        APIFactory.getMyPlanTasks(tableAttr, function (response) {
          if (response.status && response.data.length > 0) {
            vm.impPlanList = response.data;
            var systemAttr = {
              "loadDateTime": "asc"
            }
            APIFactory.getSystemLoadByPlan({
              "ids": _.pluck(vm.impPlanList, "id"),
              "orderBy": systemAttr
            }, function (response) {
              if (response.status && response.data.length > 0) {
                var rSystemList = response.data
                _.each(vm.impPlanList, function (pObj) {
                  pObj.systemLoadList = []
                  _.each(rSystemList, function (rsObj) {
                    if (rsObj.systemLoad.planId.id == pObj.id) {
                      pObj.systemLoadList.push(rsObj.systemLoad)
                    }
                  })

                })
              }
            })
            APIFactory.getApproveStatusByPlan({
              "ids": _.pluck(vm.impPlanList, "id")
            }, function (response) {
              if (response.status) {
                vm.ar_status = response.data
                $rootScope.saveformData()
              } else {
                vm.ar_status = {}
              }
            })
            $scope.tableConfig.totalItems = response.count
            vm.totalNumberOfItem = response.count

            $timeout(function () {
              $scope.initApproveReject()
            }, 100)
          } else {
            vm.impPlanList = []
          }
        })
      } else if ($scope.role == "LoadsControl") {
        APIFactory.getLoadsControlMyTasks(tableAttr, function (response) {
          if (response.status) {
            if (response.data.length > 0) {
              vm.myTasksList = response.data
              $scope.tableConfig.totalItems = response.count
              vm.totalNumberOfItem = response.count
              APIFactory.getSystemLoadByPlan({
                "ids": _.pluck(vm.myTasksList, "id")
              }, function (response) {
                if (response.status && response.data.length > 0) {
                  var rSystemList = response.data
                  _.each(vm.myTasksList, function (pObj) {
                    pObj.systemLoadList = []
                    _.each(rSystemList, function (rsObj) {
                      if (rsObj.systemLoad.planId.id == pObj.id) {
                        pObj.systemLoadList.push(rsObj.systemLoad)
                      }
                    })
                  })
                  APIFactory.getDbcrList({
                    "planIds": _.pluck(vm.myTasksList, "id")
                  }, function (response) {
                    if (response.status) {
                      if (response.data && response.data.length > 0) {
                        var dbcrList = response.data
                        _.each(rSystemList, function (sysObj) {
                          sysObj.systemLoad.systemId.dbcrList = []
                          _.each(dbcrList, function (dbcrObj) {
                            if (dbcrObj.planId.id == sysObj.systemLoad.planId.id && dbcrObj.systemId.id == sysObj.systemLoad.systemId.id) {
                              sysObj.systemLoad.systemId.dbcrList.push(dbcrObj)
                            }
                          })
                        })
                        $rootScope.saveformData()
                      }
                    }
                  })
                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })

            } else {
              vm.myTasksList = []
            }
            // $rootScope.saveformData()
          } else {
            Toaster.sayError(response.errorMessage);
          }
        })
      } else if ($scope.leadDeploypmentScreen) {
        APIFactory.leadGetDeploymentPlanList(tableAttr, function (response) {
          if (response.status) {
            if (response.data.length > 0) {
              vm.deploymentList = response.data
              $scope.tableConfig.totalItems = response.count
              vm.totalNumberOfItem = response.count
              APIFactory.getSystemLoadByPlan({
                "ids": _.pluck(vm.deploymentList, "id")
              }, function (response) {
                if (response.status && response.data.length > 0) {
                  var rSystemList = response.data
                  angular.forEach(vm.deploymentList, function (pObj) {
                    pObj.systemLoadList = []
                    angular.forEach(rSystemList, function (rsObj) {
                      if (rsObj.systemLoad.planId.id == pObj.id) {
                        pObj.systemLoadList.push(rsObj.systemLoad)
                      }
                    })
                  })
                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })
              APIFactory.leadGetDeploymentVParsList({
                "ids": _.pluck(vm.deploymentList, "id")
              }, function (response) {
                if (response.status) {
                  var vpList = response.data
                  vm.uniqVPARList = []
                  _.each(vm.deploymentList, function (pObj) {
                    pObj.vparList = vpList[pObj.id]
                    vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                  })
                  vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                  if (Object.keys(vpList).length > 0) {
                    APIFactory.leadGetSystemLoadActions({
                      "planIds": _.pluck(vm.deploymentList, "id")
                    }, function (response) {
                      if (response.status) {
                        for (dIndex in vm.deploymentList) {
                          vm.deploymentList[dIndex].actionList = []
                          for (index in response.data) {
                            if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                              response.data[index].disabled = true
                              if (response.data[index].dslUpdate == "Y") {
                                response.data[index].dslUpdate = true
                              } else {
                                response.data[index].dslUpdate = false
                              }
                              vm.deploymentList[dIndex].actionList.push(response.data[index])
                            }
                          }
                        }

                        if (vm.deploymentList.length > 100) {
                          setTimeout(function () {
                            buildDeploymentRecords(vm)
                            $scope.$apply(); //this triggers a $digest
                          }, 3000);
                        } else {
                          setTimeout(function () {
                            buildDeploymentRecords(vm)
                            $scope.$apply(); //this triggers a $digest
                          }, 1000);
                        }

                        onloadDeploymentList = angular.copy(vm.deploymentList);
                        $rootScope.saveformData()
                      } else {
                        // Toaster.sayError(response.errorMessage)
                      }
                    })
                  }
                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })

            } else {
              vm.deploymentList = []
            }
          } else {
            Toaster.sayError(response.errorMessage);
          }
        })
      } else if ($scope.leadAuxDeploymentScreen) {
        APIFactory.leadGetAuxDeploymentPlanList(tableAttr, function (response) {
          if (response.status) {
            if (response.data.length > 0) {
              vm.auxDeploymentList = response.data
              $scope.tableConfig.totalItems = response.count
              vm.totalNumberOfItem = response.count
              APIFactory.getSystemLoadByPlan({
                "ids": _.pluck(vm.auxDeploymentList, "id")
              }, function (response) {
                if (response.status && response.data.length > 0) {
                  var rSystemList = response.data
                  angular.forEach(vm.auxDeploymentList, function (pObj) {
                    pObj.systemLoadList = []
                    angular.forEach(rSystemList, function (rsObj) {
                      if (rsObj.systemLoad.planId.id == pObj.id) {
                        pObj.systemLoadList.push(rsObj.systemLoad)
                      }
                    })
                  })

                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })
              APIFactory.leadGetDeploymentVParsList({
                "ids": _.pluck(vm.auxDeploymentList, "id")
              }, function (response) {
                if (response.status) {
                  var vpList = response.data
                  vm.uniqVPARList = []
                  _.each(vm.auxDeploymentList, function (pObj) {
                    pObj.vparList = vpList[pObj.id]
                    vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                  })
                  vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                  if (Object.keys(vpList).length > 0) {
                    APIFactory.leadGetSystemLoadActions({
                      "planIds": _.pluck(vm.auxDeploymentList, "id")
                    }, function (response) {
                      if (response.status) {
                        for (dIndex in vm.auxDeploymentList) {
                          vm.auxDeploymentList[dIndex].actionList = []
                          for (index in response.data) {
                            if (response.data[index].planId.id == vm.auxDeploymentList[dIndex].id) {
                              response.data[index].disabled = true
                              if (response.data[index].dslUpdate == "Y") {
                                response.data[index].dslUpdate = true
                              } else {
                                response.data[index].dslUpdate = false
                              }
                              vm.auxDeploymentList[dIndex].actionList.push(response.data[index])
                            }
                          }
                        }
                        $rootScope.saveformData()
                      } else {
                        // Toaster.sayError(response.errorMessage)
                      }
                    })
                  }
                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })
              // $rootScope.saveformData()
            } else {
              vm.auxDeploymentList = []
            }
          } else {
            Toaster.sayError(response.errorMessage);
          }
        })
      } else if ($scope.qaDeploypmentScreen) {
        WSService.initEAuxDeployStatus(function (sResponse) {
          Toaster.sayStatus(sResponse.status);
          $rootScope.saveformData()
        })
        if ($scope.qaScreenType == "FUNCTIONAL") {
          APIFactory.qaGetFunctionalDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.qaGetFunctionalDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.qaGetFunctionalSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].vparId.tssDeploy == true) {
                                  response.data[index].disableLoadActionDueToTSSDeploy = true
                                } else {
                                  response.data[index].disableLoadActionDueToTSSDeploy = false
                                }
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          if (vm.deploymentList.length > 100) {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 3000);
                          } else {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 1000);
                          }
                          onloadDeploymentList = angular.copy(vm.deploymentList);
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }
        if ($scope.qaScreenType == "REGRESSION" && $scope.isPassedRegressionScreen != true) {
          APIFactory.qaGetRegressionDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.qaGetRegressionDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.qaGetRegressionSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].vparId.tssDeploy == true) {
                                  response.data[index].disableLoadActionDueToTSSDeploy = true
                                } else {
                                  response.data[index].disableLoadActionDueToTSSDeploy = false
                                }
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          if (vm.deploymentList.length > 100) {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 3000);
                          } else {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 1000);
                          }
                          onloadDeploymentList = angular.copy(vm.deploymentList);
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }
        if ($scope.qaScreenType == "REGRESSION" && $scope.isPassedRegressionScreen == true) {
          APIFactory.qaGetPassedRegressionDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.qaGetRegressionDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.qaGetRegressionSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].vparId.tssDeploy == true) {
                                  response.data[index].disableLoadActionDueToTSSDeploy = true
                                } else {
                                  response.data[index].disableLoadActionDueToTSSDeploy = false
                                }
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          if (vm.deploymentList.length > 100) {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 3000);
                          } else {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 1000);
                          }
                          onloadDeploymentList = angular.copy(vm.deploymentList);
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }

        if ($scope.qaScreenType == "QATASKS") {
          APIFactory.qaGetmyTaskDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.qaGetFunctionalDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.qaGetFunctionalSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].vparId.tssDeploy == true) {
                                  response.data[index].disableLoadActionDueToTSSDeploy = true
                                } else {
                                  response.data[index].disableLoadActionDueToTSSDeploy = false
                                }
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          if (vm.deploymentList.length > 100) {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 3000);
                          } else {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 1000);
                          }
                          onloadDeploymentList = angular.copy(vm.deploymentList);
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }
      } else if ($scope.qaAuxDeploymentScreen) {
        if ($scope.qaScreenType == "FUNCTIONAL") {
          APIFactory.qaGetFunctionalAuxDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.qaGetFunctionalDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.qaGetFunctionalSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }
            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }
        if ($scope.qaScreenType == "REGRESSION") {
          APIFactory.qaGetRegressionAuxDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.deploymentREGList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentREGList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentREGList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.qaGetRegressionDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentREGList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentREGList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.qaGetRegressionSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentREGList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentREGList) {
                            vm.deploymentREGList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentREGList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentREGList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentREGList = []
              }
            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }

      } else if ($scope.tssScreen) {
        WSService.initEAuxDeployStatus(function (sResponse) {
          Toaster.sayStatus(sResponse.status);
          $rootScope.saveformData()
        })
        if ($scope.isYoda) {
          APIFactory.tssGetYodaDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              vm.tssSelectAllYoda = {}
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.tssGetYodaDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.tssGetYodaSystemLoadActions({
                        "planIds": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.deploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.deploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          if (vm.deploymentList.length > 100) {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 3000);
                          } else {
                            setTimeout(function () {
                              buildDeploymentRecords(vm)
                              $scope.$apply(); //this triggers a $digest
                            }, 1000);
                          }
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        } else {
          APIFactory.tssGetTosDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              vm.tssSelectAll = {}
              if (response.data.length > 0) {
                vm.deploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.deploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.tssGetTosDeploymentVParsList({
                  "ids": _.pluck(vm.deploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.deploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.getPreProductionLoads({
                        "ids": _.pluck(vm.deploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.deploymentList) {
                            vm.deploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              _.each(response.data[index].preProductionLoadsList, function (preProdObj) {
                                if (preProdObj.planId.id == vm.deploymentList[dIndex].id) {
                                  preProdObj.disabled = true
                                  if (preProdObj.planId.planStatus == "PASSED_REGRESSION_TESTING" || preProdObj.planId.planStatus == "BYPASSED_REGRESSION_TESTING" || preProdObj.planId.planStatus == "PARTIAL_REGRESSION_TESTING" || preProdObj.planId.planStatus == "DEPLOYED_IN_PRE_PRODUCTION") {
                                    vm.deploymentList[dIndex].ltcustLoaded = false
                                  } else {
                                    vm.deploymentList[dIndex].ltcustLoaded = true
                                  }
                                  if (preProdObj.planId.planStatus == "APPROVED") {
                                    preProdObj.enableVparForSubmittedWSP = true
                                  }
                                  if (preProdObj.dslUpdate == "Y") {
                                    preProdObj.dslUpdate = true
                                  } else {
                                    preProdObj.dslUpdate = false
                                  }
                                  // if (preProdObj.systemLoadId.dslUpdate == "Y") {
                                  //     preProdObj.dslUpdate = true
                                  // } else {
                                  //     preProdObj.dslUpdate = false
                                  // }
                                  // if(preProdObj.status=="LOADED" && preProdObj.lastActionStatus== "FAILED"){
                                  //     preProdObj.status=null
                                  // }else if(preProdObj.status=="ACTIVATED" && preProdObj.lastActionStatus== "FAILED"){
                                  //     preProdObj.status="ACTIVATED"
                                  // }else if(preProdObj.status=="DEACTIVATED" && preProdObj.lastActionStatus== "FAILED"){
                                  //     preProdObj.status="DEACTIVATED"
                                  // }else if(preProdObj.status=="DELETED" && preProdObj.lastActionStatus== "FAILED"){
                                  //     preProdObj.status="DEACTIVATED"
                                  // }
                                  // if(preProdObj.status=="LOADED"){
                                  //     if(preProdObj.lastActionStatus== "SUCCESS"){
                                  //         preProdObj.status="LOADED"
                                  //     }else if(preProdObj.lastActionStatus== "FAILED"){
                                  //         preProdObj.status=null
                                  //     }
                                  // }
                                  // else if(preProdObj.status=="ACTIVATED"){
                                  //     if(preProdObj.lastActionStatus== "SUCCESS"){
                                  //         preProdObj.status="ACTIVATED"
                                  //     }else if(preProdObj.lastActionStatus== "FAILED"){
                                  //         preProdObj.status="LOADED"
                                  //     }
                                  // }
                                  // else if(preProdObj.status=="DEACTIVATED"){
                                  //     if(preProdObj.lastActionStatus== "SUCCESS"){
                                  //         preProdObj.status="DEACTIVATED"
                                  //     }else if(preProdObj.lastActionStatus== "FAILED"){
                                  //         preProdObj.status="ACTIVATED"
                                  //     }
                                  // }
                                  // else if(preProdObj.status=="DELETED"){
                                  //     if(preProdObj.lastActionStatus== "SUCCESS"){
                                  //         preProdObj.status="DELETED"
                                  //     }else if(preProdObj.lastActionStatus== "FAILED"){

                                  //     }
                                  // }






                                  vm.deploymentList[dIndex].actionList.push(preProdObj)
                                }
                              })
                              _.each(vm.deploymentList, function (dObj) {
                                if (dObj.id == response.data[index].plan.id) {
                                  dObj.isAnyLoadsInProgress = response.data[index].isAnyLoadsInProgress
                                }
                              })
                            }
                          }
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.deploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }

      } else if ($scope.tssAuxScreen) {
        if ($scope.isYoda) {
          APIFactory.tssGetYodaAuxDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.auxDeploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.auxDeploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.auxDeploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.tssGetYodaDeploymentVParsList({
                  "ids": _.pluck(vm.auxDeploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.auxDeploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.tssGetYodaSystemLoadActions({
                        "planIds": _.pluck(vm.auxDeploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.auxDeploymentList) {
                            vm.auxDeploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              if (response.data[index].planId.id == vm.auxDeploymentList[dIndex].id) {
                                response.data[index].disabled = true
                                if (response.data[index].dslUpdate == "Y") {
                                  response.data[index].dslUpdate = true
                                } else {
                                  response.data[index].dslUpdate = false
                                }
                                vm.auxDeploymentList[dIndex].actionList.push(response.data[index])
                              }
                            }
                          }
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.auxDeploymentList = []
              }
            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        } else {
          APIFactory.tssGetTosAuxDeploymentPlanList(tableAttr, function (response) {
            if (response.status) {
              if (response.data.length > 0) {
                vm.auxDeploymentList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                APIFactory.getSystemLoadByPlan({
                  "ids": _.pluck(vm.auxDeploymentList, "id")
                }, function (response) {
                  if (response.status && response.data.length > 0) {
                    var rSystemList = response.data
                    angular.forEach(vm.auxDeploymentList, function (pObj) {
                      pObj.systemLoadList = []
                      angular.forEach(rSystemList, function (rsObj) {
                        if (rsObj.systemLoad.planId.id == pObj.id) {
                          pObj.systemLoadList.push(rsObj.systemLoad)
                        }
                      })
                    })
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                APIFactory.tssGetTosDeploymentVParsList({
                  "ids": _.pluck(vm.auxDeploymentList, "id")
                }, function (response) {
                  if (response.status) {
                    var vpList = response.data
                    vm.uniqVPARList = []
                    _.each(vm.auxDeploymentList, function (pObj) {
                      pObj.vparList = vpList[pObj.id]
                      vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                    })
                    vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                    if (Object.keys(vpList).length > 0) {
                      APIFactory.getPreProductionLoads({
                        "ids": _.pluck(vm.auxDeploymentList, "id")
                      }, function (response) {
                        if (response.status) {
                          for (dIndex in vm.auxDeploymentList) {
                            vm.auxDeploymentList[dIndex].actionList = []
                            for (index in response.data) {
                              _.each(response.data[index].preProductionLoadsList, function (preProdObj) {
                                if (preProdObj.planId.id == vm.auxDeploymentList[dIndex].id) {
                                  preProdObj.disabled = true
                                  if (preProdObj.dslUpdate == "Y") {
                                    preProdObj.dslUpdate = true
                                  } else {
                                    preProdObj.dslUpdate = false
                                  }
                                  // if (preProdObj.systemLoadId.dslUpdate == "Y") {
                                  //     preProdObj.dslUpdate = true
                                  // } else {
                                  //     preProdObj.dslUpdate = false
                                  // }
                                  vm.auxDeploymentList[dIndex].actionList.push(preProdObj)
                                }
                              })
                            }
                          }
                          $rootScope.saveformData()
                        } else {
                          // Toaster.sayError(response.errorMessage)
                        }
                      })
                    }
                  } else {
                    // Toaster.sayError(response.errorMessage)
                  }
                })
                // $rootScope.saveformData()
              } else {
                vm.auxDeploymentList = []
              }

            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
        }

      } else if ($scope.tssPlanDeployedInPreProdScreen) {
        APIFactory.getPlansDeployedInPreProdList(tableAttr, function (response) {
          if (response.status) {
            if (response.data.length > 0) {
              vm.planDeployedInPreProdList = response.data
              $scope.tableConfig.totalItems = response.count
              vm.totalNumberOfItem = response.count
              APIFactory.getSystemLoadByPlan({
                "ids": _.pluck(vm.planDeployedInPreProdList, "id")
              }, function (response) {
                if (response.status && response.data.length > 0) {
                  var rSystemList = response.data
                  angular.forEach(vm.planDeployedInPreProdList, function (pObj) {
                    pObj.systemLoadList = []
                    angular.forEach(rSystemList, function (rsObj) {
                      if (rsObj.systemLoad.planId.id == pObj.id) {
                        pObj.systemLoadList.push(rsObj.systemLoad)
                      }
                    })
                  })
                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })
              APIFactory.tssGetTosPreProdDeploymentVParsList({
                "ids": _.pluck(vm.planDeployedInPreProdList, "id"),
                "isDeployedInPreProdLoads": true
              }, function (response) {
                if (response.status) {
                  var vpList = response.data
                  vm.uniqVPARList = []
                  _.each(vm.planDeployedInPreProdList, function (pObj) {
                    pObj.vparList = vpList[pObj.id]
                    // vm.uniqVPARList = vm.uniqVPARList.concat(_.pluck(pObj.vparList, "id"))
                  })
                  // vm.uniqVPARList = _.uniq(vm.uniqVPARList)
                  // if (Object.keys(vpList).length > 0) {
                  //     APIFactory.getPreProductionLoads({
                  //         "ids": _.pluck(vm.planDeployedInPreProdList, "id")
                  //     }, function(response) {
                  //         if (response.status) {
                  //             for (dIndex in vm.planDeployedInPreProdList) {
                  //                 vm.planDeployedInPreProdList[dIndex].actionList = []
                  //                 for (index in response.data) {
                  //                     _.each(response.data[index].preProductionLoadsList, function(preProdObj) {
                  //                         if (preProdObj.planId.id == vm.planDeployedInPreProdList[dIndex].id) {
                  //                             preProdObj.disabled = true
                  //                             if(preProdObj.dslUpdate == "Y"){
                  //                                 preProdObj.dslUpdate = true
                  //                             }else{
                  //                                 preProdObj.dslUpdate = false
                  //                             }
                  //                             // if (preProdObj.systemLoadId.dslUpdate == "Y") {
                  //                             //     preProdObj.dslUpdate = true
                  //                             // } else {
                  //                             //     preProdObj.dslUpdate = false
                  //                             // }
                  //                             vm.planDeployedInPreProdList[dIndex].actionList.push(preProdObj)
                  //                         }
                  //                     })
                  //                 }
                  //             }
                  //         } else {
                  //             // Toaster.sayError(response.errorMessage)
                  //         }
                  //     })
                  // }
                  $rootScope.saveformData()
                } else {
                  // Toaster.sayError(response.errorMessage)
                }
              })
              // $rootScope.saveformData()
            } else {
              vm.planDeployedInPreProdList = []
            }
          } else {
            Toaster.sayError(response.errorMessage);
          }
        })
      } else if ($scope.planSearch) {
        if ($scope.onceSearchTriggered) {
          tableAttr.orderBy = {
            "id": "desc"
          }
          tableAttr.offset = 0
          localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
          tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
          $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
          $scope.sortColumn["id"]["desc"] = true
          $scope.onceSearchTriggered = false
        }
        if (Access.getSearchData().recentPage && Access.getSearchData().recentPage != 0 && $scope.enableRecentPage) {
          // tableAttr.offset = Access.getSearchData().recentPage;
          $scope.tableConfig.currentPage = tableAttr.offset + 1;
        }
        if (Access.getSearchData().previousSortImpPlan) {
          if (Object.keys(Access.getSearchData().previousSortImpPlan).length > 0) {
            tableAttr.orderBy = Access.getSearchData().previousSortImpPlan;
            $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
            var values = Object.keys(tableAttr.orderBy).map(function (e) {
              return (tableAttr.orderBy)[e]
            })
            $scope.sortColumn[Object.keys(tableAttr.orderBy)[0]][values[0]] = true
          }
        }
        tableAttr.planFilter = vm.planFilter
        tableAttr.filter = ""
        vm.totalNumberOfItem = ""
        APIFactory.getCommonPlanList(tableAttr, function (response) {
          if (response.status) {
            $scope.isLoading = false
            vm.implementationPlanExport = true;
            $scope.tableConfig.totalItems = response.count
            vm.totalNumberOfItem = response.count
            vm.impPlanList = _.pluck(response.data, "planView")
            vm.systemListforLead = _.pluck(response.data, "systemLoadDetails")
            _.each(vm.impPlanList, function (pObj) {
              pObj.systemLoadList = []
              _.each(vm.systemListforLead, function (sObj) {
                _.each(sObj, function (lObj) {
                  if (pObj.plan.id == lObj.systemLoad.planId.id) {
                    pObj.systemLoadList.push(lObj.systemLoad)
                  }
                })
              })
            })
          } else {
            vm.implementationPlanExport = false;
            $scope.isLoading = false
            vm.impPlanList = []
          }
        })
      } else {
        if ($scope.initialSort) {
          tableAttr.orderBy = {
            "id": "desc"
          }
          $scope.sortColumn["id"]["desc"] = true
          $scope.initialSort = false
        }
        APIFactory.getInboxPlanList(tableAttr, function (response) {
          if (response.status) {
            vm.totalNumberOfItem = response.count
            $scope.tableConfig.totalItems = response.count
            vm.impPlanList = _.pluck(response.data, "planView")
            vm.systemListforLead = _.pluck(response.data, "systemLoadDetails")
            _.each(vm.impPlanList, function (pObj) {
              pObj.systemLoadList = []
              _.each(vm.systemListforLead, function (sObj) {
                _.each(sObj, function (lObj) {
                  if (pObj.plan.id == lObj.systemLoad.planId.id) {
                    pObj.systemLoadList.push(lObj.systemLoad)
                  }
                })
              })
            })
          } else {
            vm.impPlanList = []
          }
        })


      }

    }

    vm.functionalActions = {}
    vm.regressionActions = {}
    var selectedPlan;
    vm.all = {
      functionalComment: "",
      regressionComment: "",
      isFunctional: false,
      isRegression: false,
      functional: function () {
        _.each(vm.functionalActions, function (fObj) {
          fObj.isSelected = vm.all.isFunctional
        })
      },
      regression: function () {
        _.each(vm.regressionActions, function (fObj) {
          fObj.isSelected = vm.all.isRegression
        })
      },
      fChange: function () {
        _.each(vm.functionalActions, function (fObj) {
          fObj.systemComment = vm.all.functionalComment
        })
      },
      rChange: function () {
        _.each(vm.regressionActions, function (fObj) {
          fObj.systemComment = vm.all.regressionComment
        })
      }
    }
    $scope.validateSingleFAction = function () {
      var fCount = 0;
      _.each(selectedPlan.systemLoadList, function (slObj) {
        if (vm.functionalActions[slObj.systemId.name].isSelected) {
          fCount++
        }
      })
      if (selectedPlan.systemLoadList.length == fCount) {
        vm.all.isFunctional = true
      } else {
        vm.all.isFunctional = false
      }
    }
    $scope.validateSingleRAction = function () {
      var rCount = 0;
      _.each(selectedPlan.systemLoadList, function (slObj) {
        if (vm.regressionActions[slObj.systemId.name].isSelected) {
          rCount++
        }
      })
      if (selectedPlan.systemLoadList.length == rCount) {
        vm.all.isRegression = true
      } else {
        vm.all.isRegression = false
      }
    }
    var commentsValidator = function () {
      var anyChecked = true
      if (vm.all.isFunctional) {
        anyChecked = vm.all.functionalComment && vm.all.functionalComment.length > 0 ? true : false
      }
      if (vm.all.isRegression) {
        anyChecked = vm.all.regressionComment && vm.all.regressionComment.length > 0 ? true : false
      }
      if (vm.all.isFunctional || vm.all.isRegression) {
        return anyChecked
      }
      for (var fKey in vm.functionalActions) {
        if (vm.functionalActions[fKey].isSelected) {
          anyChecked = vm.functionalActions[fKey].systemComment && vm.functionalActions[fKey].systemComment.length > 0 ? true : false
        }
      }
      for (var fKey in vm.regressionActions) {
        if (vm.regressionActions[fKey].isSelected) {
          anyChecked = vm.regressionActions[fKey].systemComment && vm.regressionActions[fKey].systemComment.length > 0 ? true : false
        }
      }
      return anyChecked
    }
    var submit_pObj;
    $scope.submitPlan = function (rootObj) {
      submit_pObj = rootObj
      selectedPlan = rootObj.plan
      vm.functionalActions = {}
      vm.regressionActions = {}
      vm.all.functionalComment = "";
      vm.all.regressionComment = "";
      vm.all.isFunctional = false
      vm.all.isRegression = false
      APIFactory.getSystemLoadByPlan({
        "ids": rootObj.plan.id
      }, function (response) {
        if (response.status) {
          rootObj.plan.systemLoadList = _.map(response.data, function (data) {
            return data.systemLoad;
          })
          _.each(rootObj.plan.systemLoadList, function (slObj) {
            vm.functionalActions[slObj.systemId.name] = {
              "systemName": slObj.systemId.name,
              "isSelected": false,
              "systemComment": ""
            }
            vm.regressionActions[slObj.systemId.name] = {
              "systemName": slObj.systemId.name,
              "isSelected": false,
              "systemComment": ""
            }
          })
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
      if (rootObj.plan.macroHeader) {
        asyncSysttemLoad(rootObj).then(function (data) {
          if (data.length > 0) {
            submit_pObj.plan.systemLoadList = _.map(data, function (sysobj) {
              return sysobj.systemLoad;
            });
            if (submit_pObj.plan.systemLoadList) {
              $scope.proceedToSubmit();
            }
          } else {
            Toaster.sayError(data)
          }
        });
      } else {
        $("#submitModal").modal("show")
      }
    }

    function asyncSysttemLoad(rootObj) {
      return new Promise(function (resolve, reject) {
        APIFactory.getSystemLoadByPlan({
          "ids": rootObj.plan.id
        }, function (response, err) {
          if (response.status) {
            // Toaster.sayError(response.errorMessage)
            var systemLoadList = [];
            _.each(response.data, function (data) {
              systemLoadList.push(data)
            })
            resolve(systemLoadList);
            // resolve(response.data);
          } else {
            reject(response.errorMessage);

          }
        })
      })
    }
    $scope.proceedToSubmit = function () {
      if (!commentsValidator()) {
        Toaster.sayWarning("Comments are mandatory for the selected system(s)")
        return
      }

      let selectedPlan = submit_pObj.plan
      if (vm.all.isFunctional && vm.all.isRegression) {
        selectedPlan.qaBypassStatus = "BYPASSED_BOTH"
      } else if (vm.all.isFunctional && !vm.all.isRegression) {
        selectedPlan.qaBypassStatus = "BYPASSED_FUNCTIONAL_TESTING"
      } else if (!vm.all.isFunctional && vm.all.isRegression) {
        selectedPlan.qaBypassStatus = "BYPASSED_REGRESSION_TESTING"
      } else {
        selectedPlan.qaBypassStatus = "NONE"
      }
      if (!selectedPlan.macroHeader) {
        _.each(selectedPlan.systemLoadList, function (slObj) {
          if (vm.functionalActions[slObj.systemId.name].isSelected && vm.regressionActions[slObj.systemId.name].isSelected) {
            slObj.qaBypassStatus = "BYPASSED_BOTH"
            slObj.qaFunctionalBypassComment = vm.functionalActions[slObj.systemId.name].systemComment
            slObj.qaRegressionBypassComment = vm.regressionActions[slObj.systemId.name].systemComment
          } else if (vm.functionalActions[slObj.systemId.name].isSelected && !vm.regressionActions[slObj.systemId.name].isSelected) {
            slObj.qaBypassStatus = "BYPASSED_FUNCTIONAL_TESTING"
            slObj.qaFunctionalBypassComment = vm.functionalActions[slObj.systemId.name].systemComment
            slObj.qaRegressionBypassComment = ""
          } else if (!vm.functionalActions[slObj.systemId.name].isSelected && vm.regressionActions[slObj.systemId.name].isSelected) {
            slObj.qaBypassStatus = "BYPASSED_REGRESSION_TESTING"
            slObj.qaFunctionalBypassComment = ""
            slObj.qaRegressionBypassComment = vm.regressionActions[slObj.systemId.name].systemComment
          } else {
            slObj.qaBypassStatus = "NONE"
            slObj.qaFunctionalBypassComment = ""
            slObj.qaRegressionBypassComment = ""
          }
        })
      } else {
        _.each(selectedPlan.systemLoadList, function (slObj) {
          slObj.qaBypassStatus = "NONE"
          slObj.qaFunctionalBypassComment = ""
          slObj.qaRegressionBypassComment = ""
        });
      }
      submit_pObj.isInProgress = true
      // selectedPlan.systemLoadList = _.map(selectedPlan.systemLoadList,function(data){
      //     return data.systemLoad;
      // })
      APIFactory.planSubmit({}, selectedPlan, function (response) {
        if (response.status) { } else {
          Toaster.sayError(response.errorMessage)
        }
        submit_pObj.isInProgress = false
      })
      if (!submit_pObj.plan.macroHeader) {
        $("#submitModal").modal("hide")
      }
    }

    WSService.initPlanSubmit(function (response) {
      // Submit Plan Block
      var planIds = _.pluck(_.pluck(vm.impPlanList, "plan"), "id")
      if (planIds && planIds.indexOf(response.metaData) >= 0) {
        // vm.checkSubmitPlanStatus()
        if (response.status) {
          if (response.errorMessage.length > 0) {
            //                        Toaster.sayStatus(response.errorMessage);
          } else {
            Toaster.sayStatus(response.metaData + ": Submitted succesfully");
          }
        } else {
          if (response.errorMessage != undefined && response.errorMessage.length > 0) {
            Toaster.sayError(response.errorMessage);
          }
        }
        $scope.refresh()
      }
      $rootScope.saveformData()
    })

    WSService.initDateAuditStatus(function (sResponse) {
      Toaster.sayStatus(sResponse.status)
      $scope.refresh()
      $rootScope.saveformData()
    })

    WSService.initAutoReject(function (sResponse) {
      Toaster.sayStatus(sResponse.status)
      setTimeout(function () {
        $scope.refresh()
        $rootScope.saveformData()
      }, 2000)
    })

    WSService.initStagingCreateWorkspaceStatus(function (sResponse) {
      if (sResponse.status == "S") {
        Toaster.sayStatus("Staging Workspace Creation SUCCESS for " + sResponse.systemName + " (" + sResponse.planId + ")")
        $rootScope.saveformData()
      }
      if (sResponse.status == "F") {
        Toaster.sayStatus("Staging Workspace Creation FAILED for " + sResponse.systemName + " (" + sResponse.planId + ")")
        $rootScope.saveformData()
      }
      setTimeout(function () {
        // Need to add Validation for screen
        $scope.refresh()
        $rootScope.saveformData()
      }, 2000)
    })
    WSService.initStagingBuildStatus(function (sResponse) {
      if (sResponse.status == "S") {
        Toaster.sayStatus(sResponse.planId + ": Staging build has completed successfully for " + sResponse.systemName)
      }
      if (sResponse.status == "F") {
        Toaster.sayStatus(sResponse.planId + ": Staging build has failed for " + sResponse.systemName)
      }
      setTimeout(function () {
        $scope.refresh()
        $rootScope.saveformData()
      }, 2000)
    })
    WSService.initStagingLoadStatus(function (sResponse) {
      if (sResponse.status == "S") {
        Toaster.sayStatus(sResponse.planId + " Loadset generated successfully for " + sResponse.systemName)
      }
      if (sResponse.status == "F") {
        Toaster.sayStatus(sResponse.planId + " Loadset generated failed for " + sResponse.systemName)
      }
      $timeout(function () {
        $scope.refresh()
        $rootScope.saveformData()
      }, 2000)
    })

    WSService.initStageDevlBuildMessage(function (sResponse) {
      Toaster.sayStatus(sResponse.status)
      $scope.refresh()
      $rootScope.saveformData()
    })

    $scope.checkDeleteAccess = function (planId, implId) {
      var impCheck = false
      _.each(vm.impPlanList, function (planObj) {
        if (planObj.plan.id == planId) {
          if (planObj.impDeleteStatus[implId] == 0) {
            impCheck = true
          }
        }
      })
      return impCheck
    }

    $scope.deletePlan = function (ev, planId) {
      var confirm = $mdDialog.confirm()
        .title("Are you sure to delete " + planId + " ?")
        .textContent('Would you like to proceed with delete action')
        .ariaLabel('deleteplan')
        .targetEvent(ev)
        .ok('Proceed')
        .cancel('Cancel');

      $mdDialog.show(confirm).then(function () {
        $mdDialog.hide()
        APIFactory.leadDeletePlan({
          "id": planId
        }, function (response) {
          if (response.status) {
            Toaster.saySuccess("Plan deleted Successfully")
            $scope.refresh()
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })
      }, function () {

      });

    }

    $scope.deleteImplementation = function (ev, planObj, impId) {
      var confirm = $mdDialog.confirm()
        .title("Are you sure to delete " + impId + " ?")
        .textContent('Would you like to proceed with delete action')
        .ariaLabel('deleteimpl')
        .targetEvent(ev)
        .ok('Proceed')
        .cancel('Cancel');

      $mdDialog.show(confirm).then(function () {
        $mdDialog.hide()
        APIFactory.leadDeleteImplementation({
          "id": impId
        }, function (response) {
          if (response.status) {
            Toaster.saySuccess("Implementation deleted Successfully")
            $scope.getImplementationByPlan(planObj)
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })
      }, function () {

      });

    }

    //New Reject Plan Starts..
    $scope.rejectPlan = function (ev, planId) {
      $mdDialog.show({
        controller: rejectMessageCtrl,
        controllerAs: "rj",
        templateUrl: 'html/templates/rejectMessage.template.html',
        parent: angular.element(document.body),
        targetEvent: ev,
        clickOutsideToClose: false,
        locals: {
          "id": planId
        }
      })
        .then(function (answer) {
          $scope.refresh();
        }, function () {

        });

      function rejectMessageCtrl($scope, id) {
        var rj = this;
        rj.impPlanId = id;
        rj.showMessageError = false
        if ($rootScope.currentRole == "Lead") {
          rj.deleteLoadSetFlag = "Yes";
          rj.currentRole = $rootScope.currentRole;
        }
        $scope.proceedReject = function () {
          if (!rj.rejectMessage) {
            rj.showMessageError = true
            return
          } else {
            rj.showMessageError = false
          }
          var paramObj = {
            "impPlanId": id,
            "rejectReason": rj.rejectMessage.replace(/["“”<>']+/g, '')
          }
          if ($rootScope.currentRole == "Lead") {
            paramObj.deleteLoadSetFlag = rj.deleteLoadSetFlag === "Yes" ? true : false;
          }
          APIFactory.rejectPlan(paramObj, function (response) {
            if (response.status) {
              Toaster.saySuccess("Plan Rejected Successfully")
              $mdDialog.hide();
            } else {
              Toaster.sayError(response.errorMessage)
            }
          })
        }
        $scope.cancel = function () {
          $mdDialog.cancel();
        };
        if ($rootScope.currentRole == "Lead") {
          $scope.keepLoadSetValue = function () {
            Toaster.sayWarning("Keeping loadset in test systems is not recommended as it will cause issue to reload loadset if previous loadset is activated and not yet deactivated or accepted")
          };
        }
      }

    }

    //New Reject Plan Ends..

    $scope.getActionLeadAccess = function (currentUserId, pObj) {
      var currentRole = getUserData("userRole");
      return (pObj.leadId == currentUserId && currentRole == 'Lead');
    }

    return {
      "scope": $scope,
      "vm": vm
    }
  }

  function initExpandView($scope, vm) {

    $scope.loadSystemImplApproverList = function (plan, impId, data) {
      var currentRole = getUserData("userRole");
      var targetSystem = [];
      var allObjects = [];
      if (!plan.extraParams) {
        plan.extraParams = {}
      }
      plan.extraParams.randomNo = parseInt(Math.random() * 1000)
      plan.extraParams.pddds = {};
      $scope.loadActivityLog(plan)
      $scope.loadDependecies(plan)
      $scope.planStatusModal(plan)
      $rootScope.expandPlanData = plan
      // $scope.loadApprovalsList(plan)

      vm.systemListRFC = [];
      var buildRoles = ["Lead", "Developer"]
      var rfcFlag = ["Lead", "DLCoreChangeTeam", "Developer"]
      plan.extraParams.buildTabStatus = {
        "checkPlan": true,
        "checkImplementation": true
      }

      if (buildRoles.indexOf(getUserData("userRole")) < 0 || implementationPlanStatus().indexOf(plan.planStatus) >= implementationPlanStatus().indexOf('ONLINE') || plan.macroHeader || $rootScope.currentActiveUserId != plan.leadId) {
        plan.extraParams.buildTabStatus.checkPlan = false
        plan.extraParams.buildTabStatus.checkImplementation = false
      }

      // if (rfcFlag.indexOf(getUserData("userRole")) < 0 || implementationPlanStatus().indexOf(plan.planStatus) >= 1 || plan.rfcFlag || $rootScope.currentActiveUserId != plan.leadId) {
      // plan.rfcFlag = true
      // }
      APIFactory.getLoadsetStatusList({}, function (response) {
        if (response.status) {
          vm.loadset_status_list = response.data
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })

      if (currentRole === 'DLCoreChangeTeam') {

        _.each(data, function (system) {
          targetSystem.push(system.targetSystem)
        })

        APIFactory.getSystemLoadByPlan({
          "ids": plan.id
        }, function (response) {
          if (response.status) {
            // plan.systemLoadList = response.data
            plan.systemLoadList = _.map(response.data, function (data) {
              return data.systemLoad;
            })
            _.each(plan.systemLoadList, function (Objects) {
              _.each(targetSystem, function (sys) {
                if (sys == Objects.systemId.name) {
                  allObjects.push(Objects)
                  plan.systemLoadList = [];
                  if (allObjects.length > 0) {
                    _.each(allObjects, function (datas) {
                      plan.systemLoadList.push(datas)
                    })
                  }
                }
              })
            })
            loadSystemDBCR(plan)
            if (isDeltaSL(plan.systemLoadList)) {
              loadDeltaSystemList(plan)
            }
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })

        APIFactory.getFileTypeByPlan({
          "ids": plan.id
        }, function (response) {
          if (response.status) {
            plan.fileType = response.data[plan.id];
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })
        APIFactory.checkForDvlBuild({
          "planId": plan.id
        }, function (response) {
          if (response.status) {
            plan.extraParams.csvExists = typeof response.data.isCSVFileExist != "undefined" ? response.data.isCSVFileExist : "";
            plan.extraParams.dvlBuild = typeof response.data.enableBuildButton != "undefined" ? response.data.enableBuildButton : "";
            plan.extraParams.dvlBuildErrors = response.data
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })

        $scope.loadApprovalsList(plan)

        $scope.checkIsDeltaPlan = function (sysList) {
          return isDeltaSL(sysList);
        }

        $scope.getQAStatus = function (qaStatus, fComment, rComment) {
          var statusList = []
          if (fComment != null && fComment != "" && fComment.indexOf("*PASSED*") < 0) {
            statusList.push("BYPASSED FUNCTIONAL TESTING")
          } else if (fComment.indexOf("*PASSED*") >= 0) {
            statusList.push("PASSED FUNCTIONAL TESTING")
          }
          if (statusList.length != 0) {
            if (rComment != null && rComment != "" && rComment.indexOf("*PASSED*") < 0) {
              statusList.push("BYPASSED REGRESSION TESTING")
            } else if (rComment.indexOf("*PASSED*") >= 0) {
              statusList.push("PASSED REGRESSION TESTING")
            }
          }
          return statusList.join(" / ")
        }

        $scope.getImplementationByPlan = function (plan) {
          APIFactory.getImplementationByPlan({
            "ids": plan.id
          }, function (response) {
            if (response.status) {
              plan.implementationList = response.data
              var devAccessForBuild = false
              if (plan) {
                var temparr = []
                _.each(plan.implementationList, function (impObj) {
                  impObj.showImpl = {};
                  if (impObj.id == impId) {
                    impObj.showImpl[impObj.id] = true;
                    $scope.loadSegmentByDeveloperList(impObj, impId);
                  } else {
                    impObj.showImpl[impObj.id] = false;
                  }
                  temparr.push(impObj);
                })
              }
              _.each(plan.implementationList, function (iObj) {
                // Check for current user or active delegated user
                if (getUserData("userRole") == "Developer" && (iObj.devId == getUserData("userName") || (getUserData("user").currentDelegatedUser && Object.keys(getUserData("user").currentDelegatedUser).length > 0 && getUserData("user").currentDelegatedUser.id == iObj.devId))) {
                  devAccessForBuild = true
                }
              })
              if (getUserData("userRole") == "Developer" && !plan.macroHeader && implementationPlanStatus().indexOf(plan.planStatus) == 0) {
                plan.extraParams.buildTabStatus.checkImplementation = devAccessForBuild
              }
              if (getUserData("userName") == plan.leadId || (getUserData("user").currentDelegatedUser && Object.keys(getUserData("user").currentDelegatedUser).length > 0 && getUserData("user").currentDelegatedUser.id == plan.leadId) || _.pluck(plan.implementationList, "devId").indexOf(getUserData("userName")) >= 0) {
                $scope.initBuild(plan);
                $scope.loadOLDTLDRList(plan)
                $scope.buildPlanList(plan.id)
                $scope.fullBuildStatus(plan.id)
                $scope.loadLastBuild(plan.id)
                $scope.loadLastStageBuild(plan.id)
                vm.buildTriggered[plan.id] = null
              }
            } else {
              Toaster.sayError(response.errorMessage)
            }
          })
        }
        $scope.getImplementationByPlan(plan)
        IService.initImplActions($scope, vm, plan, impId)

      } else {
        APIFactory.getSystemLoadByPlan({
          "ids": plan.id
        }, function (response) {
          if (response.status) {
            // plan.systemLoadList = response.data
            plan.systemLoadList = _.map(response.data, function (data) {
              return data.systemLoad;
            })
            // vm.systemListRFC.push(plan.systemLoadList)

            loadSystemDBCR(plan)
            if (isDeltaSL(plan.systemLoadList)) {
              loadDeltaSystemList(plan)
            }

          } else {
            Toaster.sayError(response.errorMessage)
          }
        })


        APIFactory.getFileTypeByPlan({
          "ids": plan.id
        }, function (response) {
          if (response.status) {
            plan.fileType = response.data[plan.id];
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })
        APIFactory.checkForDvlBuild({
          "planId": plan.id
        }, function (response) {
          if (response.status) {
            plan.extraParams.csvExists = typeof response.data.isCSVFileExist != "undefined" ? response.data.isCSVFileExist : "";
            plan.extraParams.dvlBuild = typeof response.data.enableBuildButton != "undefined" ? response.data.enableBuildButton : "";
            plan.extraParams.dvlBuildErrors = response.data
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })

        $scope.loadApprovalsList(plan)

        $scope.checkIsDeltaPlan = function (sysList) {
          return isDeltaSL(sysList);
        }

        $scope.getQAStatus = function (qaStatus, fComment, rComment) {
          var statusList = []
          if (fComment != null && fComment != "" && fComment.indexOf("*PASSED*") < 0) {
            statusList.push("BYPASSED FUNCTIONAL TESTING")
          } else if (fComment.indexOf("*PASSED*") >= 0) {
            statusList.push("PASSED FUNCTIONAL TESTING")
          }
          if (statusList.length != 0) {
            if (rComment != null && rComment != "" && rComment.indexOf("*PASSED*") < 0) {
              statusList.push("BYPASSED REGRESSION TESTING")
            } else if (rComment.indexOf("*PASSED*") >= 0) {
              statusList.push("PASSED REGRESSION TESTING")
            }
          }
          return statusList.join(" / ")
        }

        $scope.getImplementationByPlan = function (plan) {
          APIFactory.getImplementationByPlan({
            "ids": plan.id
          }, function (response) {
            if (response.status) {
              plan.implementationList = response.data
              var devAccessForBuild = false
              if (plan) {
                var temparr = []
                _.each(plan.implementationList, function (impObj) {
                  impObj.showImpl = {};
                  if (impObj.id == impId) {
                    impObj.showImpl[impObj.id] = true;
                    $scope.loadSegmentByDeveloperList(impObj, impId);
                  } else {
                    impObj.showImpl[impObj.id] = false;
                  }
                  temparr.push(impObj);
                })
              }
              _.each(plan.implementationList, function (iObj) {
                // Check for current user or active delegated user
                if (getUserData("userRole") == "Developer" && (iObj.devId == getUserData("userName") || (getUserData("user").currentDelegatedUser && Object.keys(getUserData("user").currentDelegatedUser).length > 0 && getUserData("user").currentDelegatedUser.id == iObj.devId))) {
                  devAccessForBuild = true
                }
              })
              if (getUserData("userRole") == "Developer" && !plan.macroHeader && implementationPlanStatus().indexOf(plan.planStatus) == 0) {
                plan.extraParams.buildTabStatus.checkImplementation = devAccessForBuild
              }
              if (getUserData("userName") == plan.leadId || (getUserData("user").currentDelegatedUser && Object.keys(getUserData("user").currentDelegatedUser).length > 0 && getUserData("user").currentDelegatedUser.id == plan.leadId) || _.pluck(plan.implementationList, "devId").indexOf(getUserData("userName")) >= 0) {
                $scope.initBuild(plan);
                $scope.loadOLDTLDRList(plan)
                $scope.buildPlanList(plan.id)
                $scope.fullBuildStatus(plan.id)
                $scope.loadLastBuild(plan.id)
                $scope.loadLastStageBuild(plan.id)
                vm.buildTriggered[plan.id] = null
              }
            } else {
              Toaster.sayError(response.errorMessage)
            }
          })
        }
        $scope.getImplementationByPlan(plan)
        IService.initImplActions($scope, vm, plan, impId)
      }
    }

    function loadSystemDBCR(plan) {
      APIFactory.getDbcrList({
        "planIds": plan.id
      }, function (response) {
        if (response.status && response.data.length > 0) {
          var dbcrList = response.data
          _.each(plan.systemLoadList, function (sysObj) {
            sysObj.systemId.dbcrList = []
            _.each(dbcrList, function (dbcrObj) {
              if (dbcrObj.planId.id == sysObj.planId.id && dbcrObj.systemId.id == sysObj.systemId.id) {
                sysObj.systemId.dbcrList.push(dbcrObj)
              }
            })
          })
        }
      })
    }

    var combineSystemList = ["RES", "AIR"];

    function loadDeltaSystemList(plan) {
      var flag = true;
      var systemIds = [];
      plan.extraParams.pddds.systemLists = _.map(plan.systemLoadList, function (systems) {
        return systems.systemId.name;
      })
      _.each(plan.systemLoadList, function (elem) {
        systemIds.push(elem.systemId);
        if (combineSystemList.indexOf(elem.systemId.name) >= 0 && flag) {
          flag = false;
          APIFactory.getLoadBuildSystemList({
            systemName: elem.systemId.name
          }, function (response) {
            if (response.status) {
              plan.extraParams.pddds.Res_Air_systemList = response.data;
            } else {
              Toaster.sayError(response.errorMessage)
            }
          })
        } else if (combineSystemList.indexOf(elem.systemId.name) < 0) {
          APIFactory.getLoadBuildSystemList({
            systemName: elem.systemId.name
          }, function (response) {
            if (response.status) {
              plan.extraParams.pddds.Oss_systemlist = response.data
            } else {
              Toaster.sayError(response.errorMessage)
            }
          })
        }

      })
      loadPdddsSystemMapping(plan.id, plan);
    }

    function loadPdddsSystemMapping(planId, planObj) {
      var plan;
      if ($rootScope.currentRole == "Lead") {
        if (vm.impPlanList) {
          plan = _.where(_.pluck(vm.impPlanList, "plan"), {
            "id": planId
          })[0]
        }
      } else {
        plan = planObj;
      }
      if (plan && plan.extraParams) {
        // var systemLoadId = _.pluck(plan.systemLoadList, 'id');
        var systemLoadId = _.map(plan.systemLoadList, function (systemObj) {
          return systemObj.id;
        })
        APIFactory.getSystemPdddsMappingList({
          id: systemLoadId
        }, function (response) {
          var systemResAirList = []
          var systemOssList = []
          _.each(plan.systemLoadList, function (elem) {
            elem.systemPdddsMappingList = [];
            _.each(response.data, function (each) {
              if (elem.id == each.systemLoadId.id) {
                if (combineSystemList.indexOf(each.pdddsLibraryId.systemId.name) >= 0) {
                  systemResAirList.push(each.pdddsLibraryId.id.toString());
                } else if (combineSystemList.indexOf(elem.systemId.name) < 0) {
                  systemOssList.push(each.pdddsLibraryId.id.toString())
                }
                elem.systemPdddsMappingList.push(each);
              }

            })
          })
          plan.extraParams.pddds.choosePds = false;
          if (systemResAirList.length > 0 || systemOssList.length > 0) {
            plan.extraParams.pddds.choosePds = true;
          }
          plan.extraParams.pddds.loadResAir = _.uniq(systemResAirList);
          plan.extraParams.pddds.loadOSS = _.uniq(systemOssList);
          initMultipleSelect2("#loadResAir");
          initMultipleSelect2("#loadOSS");


          $timeout(function () {
            $("#loadResAir").val(plan.extraParams.pddds.loadResAir).trigger("change");
            $("#loadOSS").val(plan.extraParams.pddds.loadOSS).trigger("change");
          }, 500);
          $timeout(function () {
            $("#loadResAir").on("select2:select", function (evt) {
              var element = evt.params.data.element;
              var $element = $(element);
              $element.detach();
              $(this).append($element);
              $("#loadResAir").val(plan.extraParams.pddds.loadResAir).trigger("change");
            });
            $("#loadOSS").on("select2:select", function (evt) {
              var element = evt.params.data.element;
              var $element = $(element);

              $element.detach();
              $(this).append($element);
              $("#loadOSS").val(plan.extraParams.pddds.loadOSS).trigger("change");
            });

          }, 500);

        })
      }

    }

    $scope.loadApprovalsList = function (plan) {
      APIFactory.getPlanApprovalsByPlan({
        "ids": plan.id
      }, function (response) {
        if (response.status) {
          plan.approvalsList = response.data
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }
    /*
    ZTPFM-2328 and 2329 stage build by plan
    */

    $scope.loadLastStageBuild = function (planId) {
      vm.stageBuildResult[planId] = {
        "stageBuildDateTime": 0,
        "stageBuildStatus": "FULL",
        "showPreviousStageBuildLog": false
      }

      APIFactory.getStageBuildByPlan({
        "id": planId
      }, function (response) {
        if (response.status) {
          if (response.data.length > 0) {
            vm.stageBuildResult[planId].stageBuildDateTime = response.data[0].buildDateTime
            for (index in response.data) {
              if (moment(response.data[index].buildDateTime, appSettings.uiDTFormat).diff(moment(vm.stageBuildResult[planId].stageBuildDateTime, appSettings.uiDTFormat)) > 0) {
                vm.stageBuildResult[planId].stageBuildDateTime = response.data[index].buildDateTime
              }
              if (response.data[index].stageBuildStatus == "PARTIAL") {
                vm.buildResult[planId].stageBuildStatus = response.data[index].buildStatus
              }

            }

            vm.stageBuildResult[planId].showPreviousStageBuildLog = true;
          } else {
            vm.stageBuildResult[planId] = {
              "showPreviousStageBuildLog": false
            }
          }
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }

    $scope.loadLastBuild = function (planId) {
      vm.buildResult[planId] = {
        "buildDateTime": 0,
        "buildStatus": "FULL",
        "showPreviousBuildLog": false
      }
      vm.disableOLDR[planId] = true
      APIFactory.getDevBuildByPlan({
        "id": planId
      }, function (response) {
        if (response.status) {
          if (response.data.length > 0) {
            var jobStatus = "S"
            vm.buildResult[planId].buildDateTime = response.data[0].buildDateTime
            for (index in response.data) {
              if (moment(response.data[index].buildDateTime, appSettings.uiDTFormat).diff(moment(vm.buildResult[planId].buildDateTime, appSettings.uiDTFormat)) > 0) {
                vm.buildResult[planId].buildDateTime = response.data[index].buildDateTime
              }
              if (response.data[index].buildStatus == "PARTIAL") {
                vm.buildResult[planId].buildStatus = response.data[index].buildStatus
              }
              if (response.data[index].jobStatus == "F") {
                jobStatus = "F"
              }
            }
            if (jobStatus == "F") {
              vm.buildResult[planId].buildDateTime = null
              vm.disableOLDR[planId] = true
            } else {
              vm.disableOLDR[planId] = false
            }
            vm.buildResult[planId].showPreviousBuildLog = true;
          } else {
            vm.buildResult[planId] = {
              "showPreviousBuildLog": false
            }
          }
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }

    $scope.loadSetType = function (plan) {
      // Setting default loader type based on previous selection
      APIFactory.getDevLoadByPlan({
        "id": plan.id
      }, function (response) {
        if (response.status && response.data.length > 0) {
          plan.loaderType = response.data[0].loadSetType;
        }
      })
    }

    $scope.loadOLDTLDRList = function (plan) {
      APIFactory.getLoaderTypes({}, function (response) {
        if (response.status) {
          vm.loaderList[plan.id] = response.data
          plan.loaderType = "E"
          $scope.loadSetType(plan)
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }

    /**
     * ZTPFM-2447 getting Rebuild Details Based on Plan Id
     * created: vinoth ponnurangan
     *
     */
    $scope.buildPlanList = function (plan) {
      APIFactory.getRebuildStatus({
        "pPlanId": plan
      }, function (response) {
        if (response.status) {
          var reBuild = response.data.allowRebuild;
          var devlBuild = response.data.allowDevlBuild;
          if (reBuild) {
            vm.rebuildAllFlag = false;
            vm.rebuildDisableFlag = false;
            vm.disbaleDevlBuild = true;
          }
          if (devlBuild) {
            vm.disbaleDevlBuild = false;
          }
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })

    }
    vm.loadSetGenerateFlag = false;
    $scope.fullBuildStatus = function (plan) {
      APIFactory.getFullBuildStatus({
        "pPlanId": plan
      }, function (response) {
        if (response.status && response.data) {
          vm.loadSetGenerateFlag = response.data
        }
      })

    }
    $scope.changeRebuildFlag = function (rebuildFlag) {
      if (rebuildFlag) {
        vm.disbaleDevlBuild = false;
      } else {
        vm.disbaleDevlBuild = true;
      }
    }

    $scope.initBuild = function (plan) {
      vm.buildInProgress[plan.id] = {}
      vm.buildStatus[plan.id] = {}
      var inProgressSystemList = [];
      APIFactory.getInProgressBuildByPlan({
        "planId": plan.id
      }, function (response) {
        if (response.status) {
          inProgressSystemList.push(response.data);
          _.filter(plan.systemLoadList, function (systemObj) {
            var flag = includes(inProgressSystemList[0], systemObj.systemId.name);
            if (flag) {
              vm.buildInProgress[plan.id][systemObj.systemId.name] = true
            } else {
              vm.buildInProgress[plan.id][systemObj.systemId.name] = false
            }
          })
        } else {
          _.filter(plan.systemLoadList, function (systemObj) {
            vm.buildInProgress[plan.id][systemObj.systemId.name] = false
          })
        }
      })
      WSService.initBuildStatus(function (response) {
        if (Object.keys(response).length == 0) {
          return
        }

        vm.buildTriggered[response.planId] = true
        vm.buildInProgress[response.planId][response.systemName] = true

        // $scope.updateProgressBar(response.planId, response.systemName, response.percentageCompleted)
        if (response.status && (response.status == "S" || response.status == "C" || response.status == "F")) {
          $scope.checkBuildCompletion(response)
          vm.buildStatus[response.planId][response.systemName] = response.status
        }

        // setTimeout(function() {
        $scope.$apply()
        $rootScope.saveformData()
        // }, 2000)
      })

    }

    function includes(container, value) {
      var returnValue = false;
      var pos = container.indexOf(value);
      if (pos >= 0) {
        returnValue = true;
      }
      return returnValue;
    }

    $scope.checkValidateDateBuild = function (ev, plan) {
      $scope.checkValidateFlag = false;
      if ($scope.checkLoadDate(plan.systemLoadList) && $rootScope.currentRole == "Lead") {
        $scope.checkValidateFlag = true;
        plan.systemRemoveFlag = false;
        var system = [];
        _.each(plan.systemLoadList, function (data) {
          if (data.loadDateTime == null) {
            system.push(data.systemId.name)
          }
        })
        try {
          var confirm = $mdDialog.confirm()
            .title('Load date for these System' + " " + system.toString() + " " + 'are Null')
            .textContent('Do you want to continue by removing these system ?')

            .targetEvent(ev)
            .ok('Yes')
            .cancel('No');

          $mdDialog.show(confirm).then(function () {
            plan.systemRemoveFlag = true;
            $scope.triggerBuild(plan);
          }, function () { });
        } catch (err) { }
      } else {
        $scope.triggerBuild(plan);
      }
    }

    $scope.triggerBuild = function (plan) {
      if (isDeltaSL(plan.systemLoadList)) {
        var combineSystemList = ["RES", "AIR"];
        var systemResAirPdddsMappingList = [];
        var systemOssPdddsMappingList = [];
        _.each(plan.systemLoadList, function (systemLoad) {
          systemLoad.systemPdddsMappingList = systemLoad.systemPdddsMappingList ? systemLoad.systemPdddsMappingList : []
          var lSystemPdddsMappingList = []
          if (combineSystemList.indexOf(systemLoad.systemId.name) >= 0) {
            var selectedAirResValues = plan.extraParams.pddds.loadResAir.map(Number)
            _.each(selectedAirResValues, function (sAirResEachValue) {
              // let derivedAirRes = _.where(systemLoad.systemPdddsMappingList, { "pdddsLibraryId.id": sAirResEachValue })
              let derivedAirRes = _.find(systemLoad.systemPdddsMappingList, function (mapValue) {
                return mapValue.pdddsLibraryId.id == sAirResEachValue;
              })
              if (derivedAirRes == undefined) { // For new values
                lSystemPdddsMappingList.push({
                  "id": null,
                  "systemLoadId": systemLoad.id,
                  "planId": plan.id,
                  "pdddsLibraryId": sAirResEachValue,
                  "active": "Y",
                  "createdBy": null,
                  "createdDt": null,
                  "modifiedBy": null,
                  "modifiedDt": null
                })
              } else {
                lSystemPdddsMappingList.push(derivedAirRes)
              }
            })
            if (!selectedAirResValues || selectedAirResValues == null || selectedAirResValues.length == 0) {
              // If nothing is selected
              systemLoad.systemPdddsMappingList = []
            } else {
              systemLoad.systemPdddsMappingList = lSystemPdddsMappingList
            }
          } else if (combineSystemList.indexOf(systemLoad.systemId.name) < 0) {
            var lSystemOssPdddsMappingList = []
            var selectedOssValues = plan.extraParams.pddds.loadOSS.map(Number)
            _.each(selectedOssValues, function (sOssEachValue) {
              // let derivedOss = _.where(systemLoad.systemPdddsMappingList, { "id": sOssEachValue })
              let derivedOss = _.find(systemLoad.systemPdddsMappingList, function (mapValue) {
                return mapValue.pdddsLibraryId.id == sOssEachValue;
              })
              if (derivedOss == undefined) { // For new values
                lSystemOssPdddsMappingList.push({
                  "id": null,
                  "systemLoadId": systemLoad.id,
                  "planId": plan.id,
                  "pdddsLibraryId": sOssEachValue,
                  "active": "Y",
                  "createdBy": null,
                  "createdDt": null,
                  "modifiedBy": null,
                  "modifiedDt": null
                })
              } else {
                lSystemOssPdddsMappingList.push(derivedOss)
              }
            })
            if (!selectedOssValues || selectedOssValues == null || selectedOssValues.length == 0) {
              // If nothing is selected
              systemLoad.systemPdddsMappingList = []
            } else {
              systemLoad.systemPdddsMappingList = lSystemOssPdddsMappingList
            }
          }
        })
      }


      // plan.systemLoadList[0].systemPdddsMappingList = systemPdddsMappingList;
      if (plan.implementationList.length === 0) {
        Toaster.sayWarning("Atleast 1 implementation is needed to trigger build")
        return;
      }
      vm.buildTriggered[plan.id] = true
      _.filter(plan.systemLoadList, function (systemObj) {
        vm.buildInProgress[plan.id][systemObj.systemId.name] = true
      })

      var systemLoadList = _.map(plan.systemLoadList, function (sysObj) {
        return sysObj;
      })

      APIFactory.buildPlan({
        "implId": plan.id + "_001",
        systemRemoveFlag: plan.systemRemoveFlag,
        rebuildAll: vm.rebuildAllFlag
      }, systemLoadList, function (response) {
        if (response.status) {
          vm.buildTriggered[plan.id] = true
          _.filter(plan.systemLoadList, function (systemObj) {
            vm.buildInProgress[plan.id][systemObj.systemId.name] = true
            // $scope.updateProgressBar(plan.id, systemObj.systemId.name, 0)
          })
          if ($scope.checkValidateFlag) {
            $scope.systemLoadData();
          }
        } else {
          vm.buildTriggered[plan.id] = false
          _.filter(plan.systemLoadList, function (systemObj) {
            vm.buildInProgress[plan.id][systemObj.systemId.name] = false
          })
          if ($scope.checkValidateFlag) {
            vm.buildTriggered[plan.id] = null
            Toaster.sayError(response.errorMessage)
            return
          } else {
            Toaster.sayError(response.errorMessage)
          }

        }
      })
      $scope.systemLoadData = function () {
        APIFactory.getSystemLoadByPlan({
          "ids": plan.id
        }, function (response) {
          if (response.status) {
            // plan.systemLoadList = response.data
            plan.systemLoadList = _.map(response.data, function (data) {
              return data.systemLoad;
            })
            vm.tempBuildInProgress = {}
            vm.tempBuildInProgress[plan.id] = {}
            _.filter(plan.systemLoadList, function (systemObj) {
              vm.tempBuildInProgress[plan.id][systemObj.systemId.name] = true;
            })
            vm.buildInProgress = vm.tempBuildInProgress;
            loadSystemDBCR(plan)
            if (isDeltaSL(plan.systemLoadList)) {
              loadDeltaSystemList(plan)
            }
          } else {
            Toaster.sayError(response.errorMessage)
          }
        })
      }
    }


    WSService.initBuildMessage(function (response) {
      Toaster.sayStatus(response.message)
      $rootScope.saveformData()
      WSService.initStageDevlBuildMessage(function (response) {
        Toaster.sayStatus(response.status)
        $rootScope.saveformData()
      })
    })


    $scope.checkBuildCompletion = function (rObj) {
      var systemsInQueue = Object.keys(vm.buildInProgress[rObj.planId]).length
      var successCount = 0;
      vm.buildInProgress[rObj.planId][rObj.systemName] = false
      for (buildProg in vm.buildInProgress[rObj.planId]) {
        if (!vm.buildInProgress[rObj.planId][buildProg]) {
          successCount++
        }
      }
      if (successCount == systemsInQueue) {
        $scope.loadLastBuild(rObj.planId)
        $scope.buildPlanList(rObj.planId)
        $scope.fullBuildStatus(rObj.planId)
        loadPdddsSystemMapping(rObj.planId);
        vm.buildTriggered[rObj.planId] = false
      }
    }

    $scope.cancelBuild = function (plan) {
      APIFactory.cancelBuild({
        "planId": plan.id
      }, function (response) {
        if (response.status) {
          vm.buildTriggered[plan.id] = false;
          for (buildProg in vm.buildInProgress[plan.id]) {
            vm.buildInProgress[plan.id][buildProg] = false
            vm.buildStatus[plan.id][buildProg] = "C"
          }
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }


    $scope.updateProgressBar = function (planId, systemName, progress) {
      var $pb = $('.PG_' + planId + '_' + systemName + ' .bar');
      $pb.attr('data-transitiongoal', progress).progressbar({
        display_text: 'fill'
      });
    }

    $scope.updateBuildButton = function (progressObj, planId) {
      if (progressObj && progressObj[planId] && JSON.stringify(progressObj[planId]).indexOf("true") >= 0) {
        return true;
      } else {
        return false;
      }
    }

    $scope.checkLoadDate = function (systemObj) {
      var systemLoadList = _.map(systemObj, function (sysObj) {
        return sysObj;
      })
      if (_.findWhere(systemLoadList, {
        loadDateTime: null
      })) {
        return true;
      } else {
        return false;
      }
    }
    /*
ZTPFM-2328 Build Log
        */

    vm.devLog = [{
      id: 1,
      name: 'Build Log',
      type: 'DVL_BUILD'
    },
    {
      id: 2,
      name: 'Loader Log',
      type: 'DVL_LOAD'
    }
    ];


    vm.devLogDefault = vm.devLog[0].name;
    vm.devLogDefaultType = vm.devLog[0].type;
    var keys = "jsonData";
    $scope.defaultBuildFlag = false;
    $scope.onChangeBuildFlag = false;
    localStorage.removeItem(keys)
    $scope.showJenkinsLog = function (ev, planId, systemName) {

      APIFactory.getLatestBuildLog({
        "planId": planId,
        "systemName": systemName,
        "buildType": vm.devLogDefaultType
      }, function (response) {
        if (response.status) {
          vm.historicalLog = response.data;
          if (vm.historicalLog.buildFileNameList.length > 0 || vm.historicalLog.loaderFileNameList.length > 0) {
            APIFactory.getBuildLog({
              "fileName": vm.historicalLog.buildFileNameList[0]
            }, function (response) {
              if (response.status) {
                localStorage.setItem(keys, JSON.stringify(response.data));

                var item = [];
                item.push(JSON.parse(localStorage.getItem("jsonData")));
                vm.fileNameContent = item[0];
                if (vm.fileNameContent != null) {
                  $mdDialog.show({
                    controller: jenkinLogCtrl,
                    controllerAs: "jk",
                    templateUrl: 'html/templates/jenkinlog.template.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                      "planInfo": {
                        "planId": planId,
                        "systemName": systemName,
                        "logContent": vm.fileNameContent,
                        "devLogs": vm.devLog,
                        "defaultBuildLog": vm.devLogDefault,
                        "defaultHistoricalLog": vm.historicalLog.buildFileNameList[0],
                        "defaultHistoricalLoaderLog": vm.historicalLog.loaderFileNameList[0],
                        "historicalBuildLog": vm.historicalLog
                      }
                    }

                  })


                    .then(function (answer) {

                    }, function () {

                    });
                }
              }
            })
          } else {
            Toaster.sayStatus("No build log avaliable for this plan " + planId);
          }

        } else {
          Toaster.sayError(response.errorMessage)
        }
      })

    };

    /*
         ZTPFM-2329,ZTPFM-2328  Build Log and Loadset log need to display drop down list
         created_by: Vinoth Ponnurangan
         Deployment :18.0

        */
    function jenkinLogCtrl($scope, $mdDialog, planInfo) {
      var jk = this;
      var defaultHisFileName;
      jk.planInfo = planInfo;
      $scope.defaultBuildFlag = true;
      $scope.onChangeBuildFlag = false;

      jk.logContent = jk.planInfo.logContent.replace(/\\r\\n/g, "<br/>")

      $scope.cancel = function () {
        $mdDialog.cancel();
      };
      $scope.selectedBuild = function (buildName) {
        if (buildName === 'Build Log') {
          vm.devLogDefaultType = 'DVL_BUILD';
        } else {
          vm.devLogDefaultType = 'DVL_LOAD';
        }
        APIFactory.getLatestBuildLog({
          "planId": jk.planInfo.planId,
          "systemName": jk.planInfo.systemName,
          "buildType": vm.devLogDefaultType
        }, function (response) {
          if (response.status) {
            vm.historicalLog = response.data;
            if ((vm.historicalLog.buildFileNameList != null && vm.historicalLog.buildFileNameList != "") ||
              (vm.historicalLog.loaderFileNameList != null && vm.historicalLog.loaderFileNameList != "")) {
              if (vm.devLogDefaultType === 'DVL_BUILD') {
                defaultHisFileName = vm.historicalLog.buildFileNameList[0];
                $scope.fileNameContent(defaultHisFileName);
              } else {
                defaultHisFileName = vm.historicalLog.loaderFileNameList[0];
                $scope.fileNameContent(defaultHisFileName);
              }
            } else {
              $scope.fileNameContent("");
            }

          }


        })
      }


      $scope.fileNameContent = function (fileName) {
        APIFactory.getBuildLog({
          "fileName": fileName
        }, function (response) {
          if (response.status) {
            if (response.data != null) {
              $scope.onChangeBuildFlag = true;
              $scope.defaultBuildFlag = false;
              $scope.fileContent = response.data.replace(/\\r\\n/g, "<br/>");
            }
          } else {
            $scope.onChangeBuildFlag = true;
            $scope.defaultBuildFlag = false;
            $scope.fileContent = [];
          }


        })


      }


    }

    /*
            ZTPFM-2328 Stage Build Log
    */

    vm.stageLog = [{
      id: 1,
      name: 'Build Log',
      type: 'STG_BUILD'
    },
    {
      id: 2,
      name: 'Loader Log',
      type: 'STG_LOAD'
    }
    ];


    vm.stageLogDefault = vm.stageLog[0].name;
    vm.stageLogDefaultType = vm.stageLog[0].type;
    var stageKeys = "jsonStageData";
    $scope.defaultStageBuildFlag = false;
    $scope.onChangeStageBuildFlag = false;
    localStorage.removeItem(stageKeys)
    $scope.showStageJenkinsLog = function (ev, planId, systemName) {

      APIFactory.getLatestBuildLog({
        "planId": planId,
        "systemName": systemName,
        "buildType": vm.stageLogDefaultType
      }, function (response) {
        if (response.status) {
          vm.stageHistoricalLog = response.data;
          if (vm.stageHistoricalLog.buildFileNameList.length > 0 || vm.stageHistoricalLog.loaderFileNameList.length > 0) {
            APIFactory.getStageBuildLog({
              "fileName": vm.stageHistoricalLog.buildFileNameList[0]
            }, function (response) {
              if (response.status) {
                localStorage.setItem(stageKeys, JSON.stringify(response.data));

                var item = [];
                item.push(JSON.parse(localStorage.getItem("jsonStageData")));
                vm.stageFileNameContent = item[0];
                if (vm.stageFileNameContent != null) {
                  $mdDialog.show({
                    controller: jenkinStageLogCtrl,
                    controllerAs: "jks",
                    templateUrl: 'html/templates/jenkinStagelog.template.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose: false,
                    locals: {
                      "planInfo": {
                        "planId": planId,
                        "systemName": systemName,
                        "logContent": vm.stageFileNameContent,
                        "stageLogs": vm.stageLog,
                        "defaultStageBuildLog": vm.stageLogDefault,
                        "defaultStageHistoricalLog": vm.stageHistoricalLog.buildFileNameList[0],
                        "defaultStageHistoricalLoaderLog": vm.stageHistoricalLog.loaderFileNameList[0],
                        "historicalStageBuildLog": vm.stageHistoricalLog
                      }
                    }

                  })


                    .then(function (answer) {

                    }, function () {

                    });
                }
              }
            })
          } else {
            Toaster.sayStatus("No build log avaliable for this plan " + planId);
          }

        } else {
          Toaster.sayError(response.errorMessage)
        }
      })

    };

    /*
         ZTPFM-2329,ZTPFM-2328   Stage Build Log and Loadset log need to display drop down list
         created_by: Vinoth Ponnurangan
         Deployment :18.0

        */
    function jenkinStageLogCtrl($scope, $mdDialog, planInfo) {
      var jks = this;
      var defaultHisFileName;
      jks.planInfo = planInfo;
      $scope.defaultStageBuildFlag = true;
      $scope.onChangeStageBuildFlag = false;

      jks.logContent = jks.planInfo.logContent.replace(/\\r\\n/g, "<br/>")

      $scope.cancel = function () {
        $mdDialog.cancel();
      };
      $scope.selectedBuild = function (buildName) {
        if (buildName === 'Build Log') {
          vm.stageLogDefaultType = 'STG_BUILD';
        } else {
          vm.stageLogDefaultType = 'STG_LOAD';
        }
        APIFactory.getLatestBuildLog({
          "planId": jks.planInfo.planId,
          "systemName": jks.planInfo.systemName,
          "buildType": vm.stageLogDefaultType
        }, function (response) {
          if (response.status) {
            vm.stageHistoricalLog = response.data;
            if ((vm.stageHistoricalLog.buildFileNameList != null && vm.stageHistoricalLog.buildFileNameList != "") ||
              (vm.stageHistoricalLog.loaderFileNameList != null && vm.stageHistoricalLog.loaderFileNameList != "")) {
              if (vm.stageLogDefaultType === 'STG_BUILD') {
                defaultHisFileName = vm.stageHistoricalLog.buildFileNameList[0];
                $scope.fileNameContent(defaultHisFileName);
              } else {
                defaultHisFileName = vm.stageHistoricalLog.loaderFileNameList[0];
                $scope.fileNameContent(defaultHisFileName);
              }
            } else {
              $scope.fileNameContent("");
            }

          }


        })
      }


      $scope.fileNameContent = function (fileName) {
        APIFactory.getStageBuildLog({
          "fileName": fileName
        }, function (response) {
          if (response.status) {
            if (response.data != null) {
              $scope.onChangeStageBuildFlag = true;
              $scope.defaultStageBuildFlag = false;
              $scope.fileContent = response.data.replace(/\\r\\n/g, "<br/>");
            }
          } else {
            $scope.onChangeStageBuildFlag = true;
            $scope.defaultStageBuildFlag = false;
            $scope.fileContent = [];
          }


        })


      }


    }






    $scope.saveApprover = function (plan) {
      var dataObj;
      // Load Approvals-Validation
      if (!vm.exceptionApproavls || vm.exceptionApproavls.approvedBy == "" || !vm.exceptionApproavls.approvedBy) {
        Toaster.sayWarning("Provide Load Approver Name for " + plan.id)
        return
      }
      /* if (!vm.exceptionApproavls || vm.exceptionApproavls.comments == "" || !vm.exceptionApproavls.comments) {
       Toaster.sayWarning("Provide Comments for " + plan.id)
       return
       }
       if (!vm.exceptionApproavls || !vm.exceptionApproavls.fileName) {
       Toaster.sayWarning("Upload Email for " + plan.id)
       return
       }*/
      if (vm.exceptionApproavls.fileName == undefined) {
        vm.exceptionApproavls.approvalType = "EXCEPTION";
        vm.exceptionApproavls.planId = plan
        APIFactory.saveLoadApprovals(vm.exceptionApproavls, function (response) {
          if (response.status) {
            $scope.loadApprovalsList(plan)
            vm.exceptionApproavls = {}
          } else {
            Toaster.sayError(response.errorMessage)
          }
          // $scope.refresh()
        })

      } else {
        dataObj = {
          "planId": plan.id,
          "file": vm.exceptionApproavls.fileName,
          "approvalCmt": vm.exceptionApproavls.comments
        }

        APIFactory.uploadLoadApprovals(dataObj, function (response) {
          if (response.status) {
            vm.exceptionApproavls.planId = {
              "id": plan.id
            }
            vm.exceptionApproavls.approvalType = "EXCEPTION";
            vm.exceptionApproavls.fileName = vm.exceptionApproavls.fileName.name;
            APIFactory.saveLoadApprovals(vm.exceptionApproavls, function (response) {
              if (response.status) {
                $scope.loadApprovalsList(plan)
                vm.exceptionApproavls = {}
              } else {
                Toaster.sayError(response.errorMessage)
              }
            })
          } else {
            Toaster.sayError(response.errorMessage)
          }
          // $scope.refresh()
        })
      }

    }


    // Approval tab showing based on roles Lead, Developer, Reviewer, Load attendee, Dev Manager, QA Functional tester

    $scope.checkApprovalDisable = function () {
      var currentRole = getUserData("userRole");
      if (currentRole == 'Lead' || currentRole == 'Developer' || currentRole == 'Reviewer' || currentRole == 'QA' || currentRole == 'DLCoreChangeTeam') {
        return false
      }
      return true
    }

    $scope.checkApprovalDeleteDisable = function (status) {
      var currentRole = getUserData("userRole");
      if (status == 'delete' && currentRole == 'Lead' || currentRole == 'Developer' || currentRole == 'Reviewer' || currentRole == 'QA' || currentRole == 'DevManager' || currentRole == 'DLCoreChangeTeam') {
        return false
      }
      return true
    }


    $rootScope.loadPlanApprovalsList = function (plan) {
      APIFactory.getPlanApprovalsByPlan({
        "ids": plan.id
      }, function (response) {
        if (response.status) {
          plan.approvalsList = response.data
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }

    /**************  Update Dialog - Starts  ********************/
    $scope.showUpdateDialog = function (ev, updatedata) {
      selectedRowData = updatedata
      if (selectedRowData) {
        $mdDialog.show({
          controller: updateDialogCtrl,
          templateUrl: 'html/templates/updateApproval.template.html',
          controllerAs: 'vmid',
          parent: angular.element(document.body),
          targetEvent: ev,
          clickOutsideToClose: false,
          options: selectedRowData.options,
          fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
          .then(function (answer) {
            $scope.status = 'You said the information was "' + answer + '".';
          }, function () {
            $scope.status = 'You cancelled the dialog.';
          });
      } else {
        Toaster.sayError("Something went wront in updating");
        return false;
      }
    }

    var updateData = {}

    function updateDialogCtrl($scope, $mdDialog, options) {
      var vmid = this;
      // Insert the record if it is true
      forceInsert = false;
      vmid.forceInsertMsg = false;
      //Initializing ng model for form
      vmid.updateData = {}
      vmid.updateData.approvedBy = selectedRowData.approvedBy
      vmid.updateData.comments = selectedRowData.comments
      vmid.updateData.active = selectedRowData.active
      vmid.updateData.id = selectedRowData.id
      vmid.updateData.planId = selectedRowData.planId
      vmid.updateData.createdBy = selectedRowData.createdBy
      vmid.updateData.fileName = selectedRowData.fileName
      vmid.updateData.createdDt = selectedRowData.createdDt

      $scope.cancel = function () {
        $mdDialog.cancel();
      };

      vmid.updateApprover = function (data) {
        if (vmid.updateData.approvedBy == "" || !vmid.updateData.approvedBy) {
          Toaster.sayWarning("Provide Load Approver Name for " + vmid.updateData.planId.id)
          return
        }
        if ((vmid.updateData.fileName == null) || !vmid.updateData.fileName.size) {
          vmid.updateData.fileName = vmid.updateData.fileName;
        }
        var dData = angular.copy(data)

        if (vmid.updateData.fileName == undefined || !vmid.updateData.fileName.size) {
          dData.approvalType = "EXCEPTION";
          dData.fileName = vmid.updateData.fileName;
          APIFactory.updateLoadApprovals(dData, function (response) {
            if (response.status) {
              $mdDialog.hide();
              Toaster.saySuccess("Data Updated successfully " + dData.planId.id)
              $rootScope.loadPlanApprovalsList($rootScope.expandPlanData)
              vmid.updateData = {}
            } else {
              Toaster.sayError(response.errorMessage)
            }
            // $rootScope.refresh()
          })

        } else {
          dataObj = {
            "planId": dData.planId.id,
            "file": vmid.updateData.fileName,
            "approvalCmt": vmid.updateData.comments
          }

          APIFactory.uploadLoadApprovals(dataObj, function (response) {
            if (response.status) {
              vmid.updateData.planId = {
                "planId": data.planId.id
              }
              dData.fileName = vmid.updateData.fileName.name;
              APIFactory.updateLoadApprovals(dData, function (response) {
                if (response.status) {
                  $mdDialog.hide();
                  Toaster.saySuccess("Data Updated successfully " + dData.planId.id)
                  $rootScope.loadPlanApprovalsList($rootScope.expandPlanData)
                  vmid.updateData = {}
                } else {
                  Toaster.sayError(response.errorMessage)
                }
              })
            } else {
              Toaster.sayError(response.errorMessage)
            }
            // $rootScope.refresh()
          })
        }
      }

      vmid.deleteApprvlDocFile = function (file) {
        try {
          var params = {
            "planId": vmid.updateData.planId.id,
            "testFile": file
          }
          APIFactory.deleteAttachedFile(params, function (response) {
            if (response.status) {
              Toaster.saySuccess("File Deleted Successfully " + vmid.updateData.planId.id);
              vmid.updateData.fileName = ""
              vmid.updateData.createdDt = null
            } else {
              Toaster.sayError(response.errorMessage);
            }

          })
        } catch (err) { }
      }
    }

    // ZTPFM-2275 Plan Status Check
    vm.getDeploymentStatusByPlan = function (planStatus) {
      if ((planStatus !== 'DEPLOYED_IN_PRODUCTION' && planStatus !== 'PENDING_FALLBACK') && getDeploumentStatus().indexOf(planStatus) <= getDeploumentStatus().indexOf("READY_FOR_PRODUCTION_DEPLOYMENT")) {
        return true;

      }


    }

    //ZTPFM-2275 Commont box added
    vm.deploymentStatus = function (e, planId) {

      $mdDialog.show({
        controller: deploymentStopAndStartCtrl,
        controllerAs: "ld",
        templateUrl: 'html/templates/deploymentStartAndStart.template.html',
        parent: angular.element(document.body),
        targetEvent: e,
        clickOutsideToClose: false,
        locals: {
          // "id": planId
        }
      })
        .then(function (answer) {
          //  $scope.refresh()
        }, function () { });

      function deploymentStopAndStartCtrl($scope) {
        var ld = this;
        ld.showMessageError = false
        $scope.proceedSaveComment = function () {
          if (!ld.deploymentStatusChangeComment) {
            ld.showMessageError = true
            return
          } else {
            ld.showMessageError = false
          }
          var paramObj = {
            "planId": planId,
            "deploymentStartAndStopReason": ld.deploymentStatusChangeComment.replace(/["“”<>']+/g, '')
          }
          APIFactory.deploymentStatusChange(paramObj, function (response) {
            if (response.status) {
              Toaster.sayStatus("Deployment status changed " + planId)
              $mdDialog.hide();
              $rootScope.refresh();
            } else {
              Toaster.sayError(response.errorMessage);
            }
          })
          $rootScope.refresh();
        }
        $scope.cancel = function () {
          $mdDialog.cancel();
        };
      }

    }


    $scope.deleteApprover = function (plan, appId) {
      APIFactory.deleteLoadApproval({
        "approvalId": appId
      }, function (response) {
        if (response.status) {
          $scope.loadApprovalsList(plan)
        } else {
          Toaster.sayError(response.errorMessage)
        }
        // $scope.refresh()
      })
    }


    $scope.downloadLoadApproverFile = function (planId, fileName) {

      var postDataObj = {
        planId: planId,
        fileName: fileName
      }
      APIFactory.downloadLoadApproval(postDataObj, function (response) {
        if (response.status) {
          var resposeStr = base64ToArrayBuffer(response.data)
          var file = new Blob([resposeStr], {
            type: response.metaData
          });
          saveAs(file, fileName)
        } else {
          Toaster.sayError(response.errorMessage)
        }
      })
    }

    $scope.generateOLDRTLDR = function (plan) {
      //Do not go for generate oldr, if any plan contains csv file

      if (plan.extraParams.csvExists == "Y") {
        Toaster.sayWarning("CSV file included, so generate TLDR and FTP manually.")
        return
      }
      vm.loaderFileStatus[plan.id] = true
      vm.loaderFlag = true;
      paramObj = {
        "planId": plan.id,
        "loaderType": plan.loaderType
      }
      APIFactory.createLoaderFile(paramObj, function (response) {
        if (response.status) {
          // vm.loaderFileStatus[plan.id] = true
          WSService.initLoaderStatus(function (response) {
            if (response.status == "S") {
              Toaster.sayStatus(response.planId + ": Devl Loadset generated for " + response.systemName)
              vm.loaderFileStatus[response.planId] = false
              vm.loaderFlag = false;
              $scope.$apply()
            }
            if (response.status == "F") {
              Toaster.sayStatus(response.planId + ": Devl Loadset generation failed for " + response.systemName)
              vm.loaderFileStatus[response.planId] = false
              vm.loaderFlag = false;
              $scope.$apply()
            }
            $rootScope.saveformData()
          })
        } else {
          vm.loaderFileStatus[plan.id] = false
          vm.loaderFlag = false;
          Toaster.sayError(response.errorMessage)
          $rootScope.saveformData()
        }
      })
    }

    $scope.loadActivityLog = function (plan) {
      // return
      /* Pagination Table Starts */
      var columnsToBeSorted = ["createdDt", "createdBy"]

      var initTableSettings = function () {
        $scope.tableConfigV2[plan.id + '_' + plan.extraParams.randomNo] = Paginate.tableConfig()
        $scope.pageSizeListV2[plan.id + '_' + plan.extraParams.randomNo] = Paginate.pageSizeList()
        tableAttr[plan.id] = Paginate.initTableAttr($scope.tableConfigV2[plan.id + '_' + plan.extraParams.randomNo].pageSize)
        tableAttr[plan.id].planId = plan.id
        $scope.sortColumnV2[plan.id + '_' + plan.extraParams.randomNo] = Paginate.resetSortColumn(columnsToBeSorted)
        $scope.tableConfigV2[plan.id + '_' + plan.extraParams.randomNo].currentPage = 0;
      }
      initTableSettings()
      $scope.switchPageSizeV2 = function (planId, plan) {
        tableAttr[planId].offset = 0
        tableAttr[planId].limit = $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo].pageSize
        $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo].currentPage = 1

        localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr[planId].limit));
        tableAttr[planId].limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
        $rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))

        loadActivity(tableAttr[planId], plan)
      }

      /* $scope.refresh = function() {
       initTableSettings()
       loadActivity(tableAttr[plan.id])
       } */

      $scope.pageChangeHandlerV2 = function (num, planId, plan) {
        if (plan.activityLogList && $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo] && $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo].lastLoadedPage && $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo].lastLoadedPage === num) {
          return;
        }
        if ($scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo]) {
          $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo].lastLoadedPage = num

          // tableAttr[planId].offset = num - 1

          var key = 'ActivityLog-' + planId
          if ((JSON.parse(localStorage.getItem(key)) == null || JSON.parse(localStorage.getItem(key)) == undefined) && (num == 0)) {

            localStorage.setItem(key, num);
          } else if (num > 0) {
            localStorage.setItem(key, num - 1);
          }
          $scope.tableConfigV2[plan.id + '_' + plan.extraParams.randomNo].currentPage = JSON.parse(localStorage.getItem(key)) + 1
          tableAttr[planId].offset = JSON.parse(localStorage.getItem(key));

          loadActivity(tableAttr[planId], plan)
        }
      };
      $scope.pageChangeHandlerV2($scope.tableConfigV2[plan.id + '_' + plan.extraParams.randomNo].currentPage, plan.id, plan)
      $scope.sortV2 = function (columnName, planId, plan) {
        var lSort = Paginate.sort(columnName, tableAttr[planId], $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo], $scope.sortColumnV2[planId + '_' + plan.extraParams.randomNo], columnsToBeSorted)
        tableAttr[planId] = lSort.tableAttr
        $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo] = lSort.tableConfig
        $scope.sortColumnV2[planId + '_' + plan.extraParams.randomNo] = lSort.sortColumn
        loadActivity(tableAttr[planId], plan)
      }

      /* Pagination Table Ends */

      function loadActivity(tableParam, plan) {
        selectedRowId = null;

        APIFactory.dlGetActivityLogList(tableParam, function (response) {
          if (response.status && response.data.length > 0) {
            plan.activityLogList = response.data
            $scope.tableConfigV2[plan.id + '_' + plan.extraParams.randomNo].totalItems = response.count
          } else {
            plan.activityLogList = []
          }
        })

      }
      $scope.activityFilterText = function (plan) {
        initTableSettings()
        tableAttr[plan.id].filter = plan.extraParams.activitySearchText
        loadActivity(tableAttr[plan.id], plan)
      }
    }

    $scope.loadDependecies = function (plan) {
      // return
      /* Pagination Table Starts */

      var columnsToBeSorted = ["createdDt", "createdBy"]

      var initTableSettings = function () {
        $scope.tableConfigV3[plan.id + '_' + plan.extraParams.randomNo] = Paginate.tableConfig()
        $scope.pageSizeListV3[plan.id + '_' + plan.extraParams.randomNo] = Paginate.pageSizeList()
        tableAttr[plan.id] = Paginate.initTableAttr($scope.tableConfigV3[plan.id + '_' + plan.extraParams.randomNo].pageSize)
        tableAttr[plan.id].planId = plan.id
        $scope.sortColumnV3[plan.id + '_' + plan.extraParams.randomNo] = Paginate.resetSortColumn(columnsToBeSorted)
        $scope.tableConfigV3[plan.id + '_' + plan.extraParams.randomNo].currentPage = 0
      }
      initTableSettings()
      $scope.dependencyswitchPageSizeV2 = function (planId, plan) {
        tableAttr[planId].offset = 0
        tableAttr[planId].limit = $scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo].pageSize
        $scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo].currentPage = 1

        localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr[planId].limit));
        tableAttr[planId].limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
        $rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))

        loadDependecies(tableAttr[planId], plan)
      }

      /* $scope.refresh = function() {
       initTableSettings()
       loadActivity(tableAttr[plan.id])
       } */

      $scope.dependencypageChangeHandlerV2 = function (num, planId, plan) {
        if (plan.dependenciesList && $scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo] && $scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo].lastLoadedPage && $scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo].lastLoadedPage === num) {
          return;
        }
        if ($scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo]) {
          $scope.tableConfigV3[planId + '_' + plan.extraParams.randomNo].lastLoadedPage = num
          // tableAttr[planId].offset = num - 1
          // localStorage.setItem('Pagination_number', JSON.stringify(tableAttr[planId].offset));
          // tableAttr[planId].offset = JSON.parse(localStorage.getItem("Pagination_number"))
          // $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))

          var key = 'DepLog-' + planId
          if ((JSON.parse(localStorage.getItem(key)) == null || JSON.parse(localStorage.getItem(key)) == undefined) && (num == 0)) {
            localStorage.setItem(key, num);
          } else if (num > 0) {
            localStorage.setItem(key, num - 1);
          }
          $scope.tableConfigV3[plan.id + '_' + plan.extraParams.randomNo].currentPage = JSON.parse(localStorage.getItem(key)) + 1
          tableAttr[planId].offset = JSON.parse(localStorage.getItem(key));

          loadDependecies(tableAttr[planId], plan)
        }
      };
      $scope.dependencypageChangeHandlerV2($scope.tableConfigV3[plan.id + '_' + plan.extraParams.randomNo].currentPage, plan.id, plan)
      // $scope.sortV2 = function (columnName, planId, plan) {
      //     var lSort = Paginate.sort(columnName, tableAttr[planId], $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo], $scope.sortColumnV2[planId + '_' + plan.extraParams.randomNo], columnsToBeSorted)
      //     tableAttr[planId] = lSort.tableAttr
      //     $scope.tableConfigV2[planId + '_' + plan.extraParams.randomNo] = lSort.tableConfig
      //     $scope.sortColumnV2[planId + '_' + plan.extraParams.randomNo] = lSort.sortColumn
      //     loadDependecies(tableAttr[planId], plan)
      // }

      /* Pagination Table Ends */

      function loadDependecies(tableParam, plan) {
        selectedRowId = null;
        // tableParam
        APIFactory.dependenciesList(tableParam, function (response) {
          if (response.status && response.data.length > 0) {
            plan.dependenciesList = response.data
            $scope.tableConfigV3[plan.id + '_' + plan.extraParams.randomNo].totalItems = response.count
          } else {
            plan.dependenciesList = []
          }
        })
      }

      // $scope.activityFilterText = function (plan) {
      //     initTableSettings()
      //     tableAttr[plan.id].filter = plan.extraParams.activitySearchText
      //     loadActivity(tableAttr[plan.id], plan)
      // }

      // loadDependecies(tableAttr[planId], plan)

    }

    // TRACKER STARTS

    $scope.planStatusModal = function (planId) {
      var current = false;
      $scope.plan_id = planId.id;
      planId.implementationMessage = [];
      planId.implementationStatus = [];
      planId.implementationId = [];
      APIFactory.getPlanTrackStatus({
        "planId": $scope.plan_id
      }, function (l_response) {
        if (l_response.status) {
          planId.trackerImplementationPlan = l_response.data;
          _.each(planId.trackerImplementationPlan.stages, function (stages) {
            if (stages.currentStatus == 'IN_PROGRESS') {
              planId.currentStatus = stages.currentStatus;
              planId.currentStatusId = stages.id;
              planId.implementationPlanMessage = stages.messages;
            }
          })
          _.each(planId.trackerImplementationPlan.implementations, function (message) {
            planId.implementationMessage.push(message.messages);
            planId.implementationStatus.push(message.currentStage.status);
            planId.implementationId.push(message.currentStage.id);
          })
          if ($rootScope.currentRole == 'Developer') {
            planId.step_indicator = true;
          } else {
            planId.step_indicator = false;
          }
        }
      })
    }

    // TRACKER ENDS

    // RFC Starts
    return {
      "scope": $scope,
      "vm": vm
    }
  }

  // $scope.RFCClick = function(plan) {
  // var getBase = IPService.initRFC($scope, vm, pObj)
  // }

  var initRFC = function ($scope, vm, planId, data) {
    planStatus = [];
    $scope.saveRFCLoader = false;
    // planStatus.push(vm.planStatusList)

    // _.each(vm.planStatusList,function(Obj){
    // planStatus.push(Obj)
    // })

    var disable = false;
    $scope.disableFields = false;
    var currentRole = getUserData("userRole");


    var systemDBId;
    var active;
    var createdBy;
    var createdDt;
    var modifiedBy;
    var modifiedDt;
    var rfcSystemDetails;

    var systemId;
    responseDetails = []
    $scope.targetSystem = [];
    if (currentRole === 'DLCoreChangeTeam' || currentRole === 'Lead') {
      $scope.disableFields = false;
    } else {
      $scope.disableFields = true;
    }
    // if (implementationPlanStatus().indexOf(planStatus) >= implementationPlanStatus().indexOf("DEPLOYED_IN_PRODUCTION")) {
    //   $scope.disableFields = true;
    // }

    $scope.disableOnlyRFC = function () {
      if (currentRole === 'DLCoreChangeTeam') {
        return false;
      } else
        return true;
    }

    vm.rfcProcessList = function (planId, system) {
      $rootScope.planDetails = planId
      if (currentRole === 'DLCoreChangeTeam') {
        vm.rfcNumber = {};
        vm.systemTarget = system;
        // if (implementationPlanStatus().indexOf(planId.planStatus) >= implementationPlanStatus().indexOf("DEPLOYED_IN_PRODUCTION")) {
        //   $scope.disableFields = true;
        // }
        APIFactory.getConfigList({}, function (l_response) {
          if (l_response.status) {
            vm.configList = l_response.data;
          }
        })
        $scope.impactLevel = ["Minimal Impact", "Medium Impact", "High Impact", "None"]
        $rootScope.plan_id = planId.id;
        $rootScope.systemId = system;

        var params = {
          "planIds": $rootScope.plan_id,
          "systemName": $rootScope.systemId
        }

        if (planId.rfcFlag) {
          APIFactory.getRFCProcessList(params, function (l_response) {
            if (l_response.status) {
              planId.rfcDetails = l_response.data;
              if (planId.rfcDetails.length > 0) {
                systemDBId = planId.rfcDetails[0].rfcDetails.id;
                active = planId.rfcDetails[0].rfcDetails.active;
                createdBy = planId.rfcDetails[0].rfcDetails.createdBy;
                createdDt = planId.rfcDetails[0].rfcDetails.createdDt;
                modifiedBy = planId.rfcDetails[0].rfcDetails.modifiedBy;
                modifiedDt = planId.rfcDetails[0].rfcDetails.modifiedDt;
              }

            }
          })
        }
      } else {
        vm.systemTarget = system;
        if (implementationPlanStatus().indexOf(planId.planStatus) >= implementationPlanStatus().indexOf("PASSED_ACCEPTANCE_TESTING")) {
          $scope.disableFields = true;
        }
        APIFactory.getConfigList({}, function (l_response) {
          if (l_response.status) {
            vm.configList = l_response.data;
          }
        })
        $scope.impactLevel = ["Minimal Impact", "Medium Impact", "High Impact", "None"]
        $rootScope.plan_id = planId.id;
        $rootScope.systemId = system;
        var params = {
          "planIds": $rootScope.plan_id,
          "systemName": $rootScope.systemId
        }
        if (planId.rfcFlag) {
          APIFactory.getRFCProcessList(params, function (l_response) {
            if (l_response.status) {
              planId.rfcDetails = l_response.data;
              if (planId.rfcDetails.length > 0) {
                systemDBId = planId.rfcDetails[0].rfcDetails.id;
                active = planId.rfcDetails[0].rfcDetails.active;
                createdBy = planId.rfcDetails[0].rfcDetails.createdBy;
                createdDt = planId.rfcDetails[0].rfcDetails.createdDt;
                modifiedBy = planId.rfcDetails[0].rfcDetails.modifiedBy;
                modifiedDt = planId.rfcDetails[0].rfcDetails.modifiedDt;
              }

            }
          })
        }
      }

    }

    if (currentRole == 'DLCoreChangeTeam') {

      $scope.saveRFC = function (pObj, e) {
        if (!pObj.rfcDetails.rfcNumber) {
          Toaster.sayWarning("Provide RFC Number");
          return;
        }
        if (!pObj.rfcDetails.rfcDesc) {
          Toaster.sayWarning("Provide Break-Fix/Expedited justification");
          return;
        }
        if (!pObj.rfcDetails.impactLevel) {
          Toaster.sayWarning("Provide TPF Resources Impact");
          return;
        }
        if (!pObj.rfcDetails.configItem) {
          Toaster.sayWarning("Provide Configuration Item");
          return;
        }
        if (!pObj.rfcDetails.vsFlag) {
          Toaster.sayWarning("Provide Does VS use this Process");
          return;
        }
        if (!pObj.rfcDetails.isTestScriptAttached) {
          Toaster.sayWarning("Provide Test script attached on approvals tab?");
          return;
        }
        if (!pObj.rfcDetails.isBusinessApprovalAttached) {
          Toaster.sayWarning("Provide Business approval attached on approvals tab?");
          return;
        }
        if (!pObj.rfcDetails.readyToSchedule) {
          Toaster.sayWarning("Provide Ready to schedule an RFC?");
          return;
        }
        if (pObj.rfcDetails.vsFlag == 'true' && !pObj.rfcDetails.vsArea) {
          Toaster.sayWarning("Provide What area of VS does this affect");
          return;
        }
        if (pObj.rfcDetails.vsFlag == 'true' && pObj.rfcDetails.vsTestFlag == null) {
          Toaster.sayWarning("Provide Can VS test this?");
          return;
        }
        if (pObj.rfcDetails.vsFlag == 'true' && !pObj.rfcDetails.vsDesc) {
          Toaster.sayWarning("Provide What will VS see with this change");
          return;
        }
        _.each(pObj.systemLoadList, function (Obj) {
          if ($rootScope.systemId == Obj.systemId.name) {
            rfcSystemDetails = Obj
          }
        })
        var params = {
          "rfcNumber": pObj.rfcDetails.rfcNumber[e + 1],
          "rfcDesc": pObj.rfcDetails.rfcDesc,
          "impactLevel": pObj.rfcDetails.impactLevel,
          "configItem": pObj.rfcDetails.configItem,
          "vsFlag": pObj.rfcDetails.vsFlag,
          "isTestScriptAttached": pObj.rfcDetails.isTestScriptAttached,
          "isBusinessApprovalAttached": pObj.rfcDetails.isBusinessApprovalAttached,
          "readyToSchedule": pObj.rfcDetails.readyToSchedule,
          "vsArea": pObj.rfcDetails.vsArea,
          "vsTestFlag": pObj.rfcDetails.vsTestFlag,
          "vsDesc": pObj.rfcDetails.vsDesc,
          "planId": pObj,
          "systemLoadId": rfcSystemDetails,
        }

        var dataSet = {
          "planIds": $rootScope.plan_id,
          "systemName": $rootScope.systemId
        }

        $scope.saveRFCLoader = true;
        APIFactory.getupdateRFCDetail(params, function (response) {
          if (response.status) {
            Toaster.saySuccess("RFC Details Saved Successfully");
            APIFactory.getRFCProcessList(dataSet, function (l_response) {
              if (l_response.status) {
                pObj.rfcDetails = l_response.data;
                $scope.saveRFCLoader = false;
              }
            })
          } else {
            $scope.saveRFCLoader = false;
            Toaster.sayError(response.errorMessage);
          }
        })
      }
      $scope.updateRFC = function (planId, e) {
        if (!planId.rfcDetails[0].rfcDetails.rfcDesc) {
          Toaster.sayWarning("Provide Break-Fix/Expedited justification");
          return;
        }
        if (!planId.rfcDetails[0].rfcDetails.impactLevel) {
          Toaster.sayWarning("TPF Resources Impact");
          return;
        }
        if (!planId.rfcDetails[0].rfcDetails.configItem) {
          Toaster.sayWarning("Provide Configuration Item");
          return;
        }
        if (planId.rfcDetails[0].rfcDetails.vsFlag == true && !planId.rfcDetails[0].rfcDetails.vsArea) {
          Toaster.sayWarning("Provide What area of VS does this affect");
          return;
        }
        if (planId.rfcDetails[0].rfcDetails.vsFlag == true && planId.rfcDetails[0].rfcDetails.vsTestFlag == null) {
          Toaster.sayWarning("Provide Can VS test this?");
          return;
        }
        if (planId.rfcDetails[0].rfcDetails.vsFlag == true && !planId.rfcDetails[0].rfcDetails.vsDesc) {
          Toaster.sayWarning("Provide What will VS see with this change");
          return;
        }
        _.each(planId.systemLoadList, function (Obj) {
          if ($rootScope.systemId == Obj.systemId.name) {
            rfcSystemDetails = Obj
          }
        })
        var params = {
          "rfcNumber": planId.rfcDetails[0].rfcDetails.rfcNumber,
          "rfcDesc": planId.rfcDetails[0].rfcDetails.rfcDesc,
          "impactLevel": planId.rfcDetails[0].rfcDetails.impactLevel,
          "configItem": planId.rfcDetails[0].rfcDetails.configItem,
          "vsFlag": planId.rfcDetails[0].rfcDetails.vsFlag,
          "isTestScriptAttached": planId.rfcDetails[0].rfcDetails.isTestScriptAttached,
          "isBusinessApprovalAttached": planId.rfcDetails[0].rfcDetails.isBusinessApprovalAttached,
          "readyToSchedule": planId.rfcDetails[0].rfcDetails.readyToSchedule,
          "vsArea": planId.rfcDetails[0].rfcDetails.vsArea,
          "vsTestFlag": planId.rfcDetails[0].rfcDetails.vsTestFlag,
          "vsDesc": planId.rfcDetails[0].rfcDetails.vsDesc,
          "planId": planId,
          "systemLoadId": rfcSystemDetails,
          "id": planId.rfcDetails[0].rfcDetails.id,
          "active": planId.rfcDetails[0].rfcDetails.active,
          "createdBy": planId.rfcDetails[0].rfcDetails.createdBy,
          "createdDt": planId.rfcDetails[0].rfcDetails.createdDt,
          "modifiedBy": planId.rfcDetails[0].rfcDetails.modifiedBy,
          "modifiedDt": planId.rfcDetails[0].rfcDetails.modifiedDt
        }

        $scope.saveRFCLoader = true;
        APIFactory.getupdateRFCDetail(params, function (response) {
          if (response.status) {
            Toaster.saySuccess("RFC Details Updated Successfully");
            setTimeout(function () {
              $scope.saveRFCLoader = false;
              $scope.$apply();
            }, 2000)
          } else {
            $scope.saveRFCLoader = false;
            Toaster.sayError(response.errorMessage);
          }
        })
      }
    } else {
      $scope.saveRFC = function (pObj, e) {
        if (!pObj.rfcDetails.rfcDesc) {
          Toaster.sayWarning("Provide Break-Fix/Expedited justification");
          return;
        }
        if (!pObj.rfcDetails.impactLevel) {
          Toaster.sayWarning("Provide TPF Resources Impact");
          return;
        }
        if (!pObj.rfcDetails.configItem) {
          Toaster.sayWarning("Provide Configuration Item");
          return;
        }
        if (!pObj.rfcDetails.vsFlag) {
          Toaster.sayWarning("Provide Does VS use this process?");
          return;
        }
        if (!pObj.rfcDetails.isTestScriptAttached) {
          Toaster.sayWarning("Provide Test script attached on approvals tab?");
          return;
        }
        if (!pObj.rfcDetails.isBusinessApprovalAttached) {
          Toaster.sayWarning("Provide Business approval attached on approvals tab?");
          return;
        }
        if (!pObj.rfcDetails.readyToSchedule) {
          Toaster.sayWarning("Provide Ready to schedule an RFC?");
          return;
        }
        if (pObj.rfcDetails.vsFlag == 'true' && !pObj.rfcDetails.vsArea) {
          Toaster.sayWarning("Provide What area of VS does this affect");
          return;
        }
        if (pObj.rfcDetails.vsFlag == 'true' && pObj.rfcDetails.vsTestFlag == null) {
          Toaster.sayWarning("Provide Can VS test this?");
          return;
        }
        if (pObj.rfcDetails.vsFlag == 'true' && !pObj.rfcDetails.vsDesc) {
          Toaster.sayWarning("Provide What will VS see with this change");
          return;
        }

        _.each(pObj.systemLoadList, function (Obj) {
          if ($rootScope.systemId == Obj.systemId.name) {
            rfcSystemDetails = Obj
          }
        })
        var params = {
          "rfcNumber": pObj.rfcDetails.rfcNumber,
          "rfcDesc": pObj.rfcDetails.rfcDesc,
          "impactLevel": pObj.rfcDetails.impactLevel,
          "configItem": pObj.rfcDetails.configItem,
          "vsFlag": pObj.rfcDetails.vsFlag,
          "isTestScriptAttached": pObj.rfcDetails.isTestScriptAttached,
          "isBusinessApprovalAttached": pObj.rfcDetails.isBusinessApprovalAttached,
          "readyToSchedule": pObj.rfcDetails.readyToSchedule,
          "vsArea": pObj.rfcDetails.vsArea,
          "vsTestFlag": pObj.rfcDetails.vsTestFlag,
          "vsDesc": pObj.rfcDetails.vsDesc,
          "planId": pObj,
          "systemLoadId": rfcSystemDetails

        }
        var dataSet = {
          "planIds": $rootScope.plan_id,
          "systemName": $rootScope.systemId
        }

        $scope.saveRFCLoader = true;
        APIFactory.getupdateRFCDetail(params, function (response) {
          if (response.status) {
            Toaster.saySuccess("RFC Details Saved Successfully");
            APIFactory.getConfigList({}, function (l_response) {
              if (l_response.status) {
                vm.configList = l_response.data;
              }
            })
            APIFactory.getRFCProcessList(dataSet, function (l_response) {
              if (l_response.status) {
                pObj.rfcDetails = l_response.data;
                $scope.saveRFCLoader = false;
              }
            })
          } else {
            $scope.saveRFCLoader = false;
            Toaster.sayError(response.errorMessage);
          }
        })
      }
      $scope.updateRFC = function (planId, e) {
        if (!planId.rfcDetails[0].rfcDetails.rfcDesc) {
          Toaster.sayWarning("Provide Break-Fix/Expedited justification");
          return;
        }
        if (!planId.rfcDetails[0].rfcDetails.impactLevel) {
          Toaster.sayWarning("TPF Resources Impact");
          return;
        }
        if (!planId.rfcDetails[0].rfcDetails.configItem) {
          Toaster.sayWarning("Provide Configuration Item");
          return;
        }
        if (planId.rfcDetails[0].rfcDetails.vsFlag && !planId.rfcDetails[0].rfcDetails.vsArea) {
          Toaster.sayWarning("Provide What area of VS does this affect");
          return;
        }
        if (planId.rfcDetails[0].rfcDetails.vsFlag == true && planId.rfcDetails[0].rfcDetails.vsTestFlag == null) {
          Toaster.sayWarning("Provide Can VS use this");
          return;
        }
        if (planId.rfcDetails[0].rfcDetails.vsFlag && !planId.rfcDetails[0].rfcDetails.vsDesc) {
          Toaster.sayWarning("Provide What will VS see with this change");
          return;
        }
        _.each(planId.systemLoadList, function (Obj) {
          if ($rootScope.systemId == Obj.systemId.name) {
            rfcSystemDetails = Obj
          }
        })
        var params = {
          "rfcNumber": planId.rfcDetails[0].rfcDetails.rfcNumber,
          "rfcDesc": planId.rfcDetails[0].rfcDetails.rfcDesc,
          "impactLevel": planId.rfcDetails[0].rfcDetails.impactLevel,
          "configItem": planId.rfcDetails[0].rfcDetails.configItem,
          "vsFlag": planId.rfcDetails[0].rfcDetails.vsFlag,
          "isTestScriptAttached": planId.rfcDetails[0].rfcDetails.isTestScriptAttached,
          "isBusinessApprovalAttached": planId.rfcDetails[0].rfcDetails.isBusinessApprovalAttached,
          "readyToSchedule": planId.rfcDetails[0].rfcDetails.readyToSchedule,
          "vsArea": planId.rfcDetails[0].rfcDetails.vsArea,
          "vsTestFlag": planId.rfcDetails[0].rfcDetails.vsTestFlag,
          "vsDesc": planId.rfcDetails[0].rfcDetails.vsDesc,
          "planId": planId,
          "systemLoadId": rfcSystemDetails,
          "id": planId.rfcDetails[0].rfcDetails.id,
          "active": planId.rfcDetails[0].rfcDetails.active,
          "createdBy": planId.rfcDetails[0].rfcDetails.createdBy,
          "createdDt": planId.rfcDetails[0].rfcDetails.createdDt,
          "modifiedBy": planId.rfcDetails[0].rfcDetails.modifiedBy,
          "modifiedDt": planId.rfcDetails[0].rfcDetails.modifiedDt
        }

        $scope.saveRFCLoader = true;
        APIFactory.getupdateRFCDetail(params, function (response) {
          if (response.status) {
            Toaster.saySuccess("RFC Details Updated Successfully");
            setTimeout(function () {
              $scope.saveRFCLoader = false;
              $scope.$apply()
            }, 2000)
          } else {
            $scope.saveRFCLoader = false;
            Toaster.sayError(response.errorMessage);
          }
        })
      }
    }
  }

  var isDelta = function () {
    return appSettings.isDeltaApp == "true"
  }

  var isTravelport = function () {
    return appSettings.isTravelportApp == "true"
  }

  var isDeltaSL = function (systemLoad) {
    if (systemLoad)
      return systemLoad[0].systemId.platformId.name == "Delta"
  }

  var isTravelportSL = function (systemLoad) {
    return systemLoad[0].systemId.platformId.name == "Travelport"
  }



  IPService.isTravelport = isTravelport;
  IPService.isDelta = isDelta;
  IPService.isTravelportSL = isTravelportSL;
  IPService.isDeltaSL = isDeltaSL;
  IPService.initPlan = initPlan;
  IPService.initRFC = initRFC;
  IPService.wrapOnLoad = wrapOnLoad;
  IPService.wrapperPlanSave = wrapperPlanSave;
  IPService.alignStructure = alignStructure;
  IPService.clearFieldsOnChange = clearFieldsOnChange;
  return IPService;

});

function switchArrow(e) {
  if ($(e).find("i").hasClass("fa-arrow-circle-down")) {
    $(e).find("i").switchClass("fa-arrow-circle-down", "fa-arrow-circle-right")
  } else {
    $(e).find("i").switchClass("fa-arrow-circle-right", "fa-arrow-circle-down")
  }
}