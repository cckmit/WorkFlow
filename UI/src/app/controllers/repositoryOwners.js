dashboard.controller("repositoryOwnersCtrl", function ($rootScope, $templateCache, appSettings, $scope,
  $state, $location, Toaster, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate) {
  var vm = this;
  $rootScope.titleHeading = $state.current.data.pageTitle
  var apiBase = appSettings.apiBase;
  vm.currentUser = getUserData("user")
  var selectedRowId = null;
  Paginate.refreshScrolling();
  $scope.repoOwnersList = []
  var tableAttr;
  // var columnsToBeSorted = ["trimmedName"]
  $scope.tableConfig = Paginate.tableConfig()
  // $scope.tableDefaultValue = Paginate.defaultPageValue()
  // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Repo Owners Page Size
  // $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
  $scope.pageSizeList = Paginate.pageSizeList()
  tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
  //	$scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)

  /* On Load Function Starts */
  function loadRepoTableList(tableAttr) {
    APIFactory.getRepoList(tableAttr, function (response) {
      if (response.status) {
        $scope.repoTableList = response.data;
        $scope.tableConfig.totalItems = response.count
        vm.totalNumberOfItem = response.count
        $rootScope.saveformData()
      } else {
        Toaster.sayError(response.errorMessage)
      }
    })


  }
  loadRepoTableList(tableAttr)

  $scope.refresh = function () {
    tableAttr.offset = $rootScope.paginateValue
    loadRepoTableList(tableAttr)
  }

  $scope.switchPageSize = function () {
    // tableAttr.offset = 0
    // tableAttr.limit = $scope.tableConfig.pageSize;
    // $scope.tableConfig.currentPage = 1
    // Paginate.switchPageSize($scope, tableAttr)
    tableAttr.offset = 0
		tableAttr.limit = $scope.tableConfig.pageSize;
		$scope.tableConfig.currentPage = 1
		localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
		tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
		$rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
    loadRepoTableList(tableAttr)
  }

  $scope.pageChangeHandler = function (num) {
    if ($scope.repoTableList && $scope.tableConfig.lastLoadedPage === num) {
      return;
    }
    $scope.tableConfig.lastLoadedPage = num
    tableAttr.offset = num - 1
    localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
    tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
    $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
    loadRepoTableList(tableAttr)
  };

  // $scope.sort = function (columnName) {
  // var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
  // tableAttr = lSort.tableAttr
  // $scope.tableConfig = lSort.tableConfig
  // $scope.sortColumn = lSort.sortColumn
  // loadRepoTableList(tableAttr)
  // }
  /************ Hide Action column start  *****************/

  $scope.showAction = function () {
    if (getUserData('userRole') === 'ToolAdmin') {
      return true;
    } else {
      return false;
    }

  }
  /************  Hide Action column ends *****************/
  /************ Hyperlink enable start  *****************/
  // $scope.showHyper = function () {
  // if (getUserData('userRole') != 'ToolAdmin') {
  // return true;
  // } else {
  // return false;
  // }
  // }
  /************ Hyperlink enable ends   *****************/


  /************ repoName Dialog - Starts  *****************/

  $scope.openModelrepoName = function (ev, rowObj) {
    try {
      $mdDialog.show({
        controller: dataFuntionalPartner,
        templateUrl: 'html/templates/dataFunctionalPartner.template.html',
        controllerAs: 'vmid',
        parent: angular.element(document.body),
        targetEvent: ev,
        clickOutsideToClose: false,
        fullscreen: $scope.customFullscreen, // Only for -xs, -sm breakpoints.
        locals: {
          "rowObj": rowObj
        }
      })
    } catch (err) { }
  }

  function dataFuntionalPartner($scope, $mdDialog, rowObj) {
    var repoNameObj = {
      "repoName": rowObj.repository.trimmedName,
    }
    APIFactory.getFuncPackSrcArtifacts(repoNameObj, function (response) {
      if (response.status) {
        $scope.repoNameList = response;
        $scope.tableConfig.totalItems = response.count
      } else {
        Toaster.sayError(response.errorMessage)
      }
    })
    $scope.doExportRepoNameData = function () {
      APIFactory.exportFuncPackSrcArtifacts(repoNameObj, function (response) {
        if (response.status) {
          var resposeStr = base64ToArrayBuffer(response.data)
          var file = new Blob([resposeStr], {
            type: response.metaData
          });
          var dateObj = new Date()
          var fileName = "REPOSITORY_NAME_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
          saveAs(file, fileName)
        } else {
          Toaster.sayError(response.errorMessage);
        }
      })
    }

    $scope.cancel = function () {
      $mdDialog.cancel();
    };

    var columnsToBeSorted = ["filename", "progname", "fileext", "targetsystem"]
    var tableAttr;
    var initTableSettings = function () {

      $scope.tableConfig = Paginate.tableConfig()
      $scope.tableConfig.pageSize = 100 // Default page Size for TSD
	
      $scope.paginationPageSize = $scope.tableConfig.pageSize;
      $scope.pageSizeList = Paginate.repoNamePageSizeList()
	  
      tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
	  
      tableAttr.repoName = repoNameObj.repoName
      tableAttr.progName = $scope.activitySearchText ? $scope.activitySearchText : ""
      $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
    }
    initTableSettings()

    $scope.switchPageSize = function () {
      tableAttr.offset = 0
      tableAttr.limit = $scope.tableConfig.pageSize;
      tableAttr.progName = $scope.activitySearchText ? $scope.activitySearchText : ""
      //tableAttr.limit = $scope.repoNameList.length
      $scope.tableConfig.currentPage = 1
      loadSystemPlan(tableAttr)
    }

    $scope.pageChangeHandler = function (num) {
      if ($scope.repoNameList && $scope.tableConfig.lastLoadedPage === num) {
        return;
      }
      $scope.tableConfig.lastLoadedPage = num
      tableAttr.offset = num - 1
      //tableAttr.filter = $scope.activitySearchText ? $scope.activitySearchText : ""
      tableAttr.progName = $scope.activitySearchText ? $scope.activitySearchText : ""
      // tableAttr.limit = "";
      localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
      tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
      $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
      loadSystemPlan(tableAttr)
    };

    $scope.sort = function (columnName) {
      var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
      tableAttr = lSort.tableAttr
      tableAttr.progName = $scope.activitySearchText ? $scope.activitySearchText : ""
      $scope.tableConfig = lSort.tableConfig
      $scope.sortColumn = lSort.sortColumn
      loadSystemPlan(tableAttr)
    }

    function loadSystemPlan(tableAttr) {
      APIFactory.getFuncPackSrcArtifacts(tableAttr, function (response) {
        if (response.status) {
          $scope.repoNameList = response;
          $scope.tableConfig.totalItems = response.count
        } else {
          Toaster.sayError(response.errorMessage)
          //$scope.repoNameList = [];
        }
      })
    }

    function loadActivity(tableAttr) {
      APIFactory.getFuncPackSrcArtifacts(tableAttr, function (response) {
        if (response.status && response.data.length > 0) {
          $scope.repoNameList = response;
          $scope.tableConfig.totalItems = response.count
        } else {
          $scope.repoNameList = [];
        }
      })

    }
    $scope.activityFilterText = function (plan) {
      initTableSettings()
      repoNameObj.progName = plan;
      loadActivity(repoNameObj);
    }
  }

  /************ repoName Dialog - ends  *****************/



  //Repository Owners List
  function repoOwners() {
    $scope.repoOwnersMapList = {}
    APIFactory.getUsersByRole({
      "role": "DevManager"
    }, function (response) {
      if (response.status) {
        $scope.repoOwnersList = response.data;
        _.each($scope.repoOwnersList, function (rObj) {
          $scope.repoOwnersMapList[rObj.id] = rObj
        })
      } else {
        $scope.repoOwnersList = []
      }
    })
  }
  repoOwners();





  /**************  Update Dialog - Starts  ********************/
  $scope.updateRepoInfo = function (ev, rowObj) {
    try {
      $mdDialog.show({
        controller: updateDialogCtrl,
        templateUrl: 'html/templates/updateRepositoryOwners.template.html',
        controllerAs: 'vmid',
        parent: angular.element(document.body),
        targetEvent: ev,
        clickOutsideToClose: false,
        fullscreen: $scope.customFullscreen, // Only for -xs, -sm breakpoints.
        locals: {
          "repoList": $scope.repoOwnersList,
          "repoMap": $scope.repoOwnersMapList,
          "rowObj": rowObj
        }
      })
        .then(function (answer) {
          $scope.status = 'You said the information was "' + answer + '".';
        }, function () {
          $scope.status = 'You cancelled the dialog.';
        });

    } catch (err) { }

  }

  function updateDialogCtrl($scope, $mdDialog, appSettings, repoList, repoMap, rowObj) {

    var vmid = this;
    vmid.repoInfo = {}
    vmid.repoInfo = angular.copy(rowObj)
    vmid.repoOwnersList = repoList;

    vmid.notFoundList = []
    vmid.defaultUserList = []
    var ignore_users = ["mtpservice"]
    _.each(vmid.repoInfo.repository.owners, function (eachUser) {
      if (!repoMap[eachUser] && ignore_users.indexOf(eachUser) < 0) {
        vmid.notFoundList.push(eachUser)
      }
      if (ignore_users.indexOf(eachUser) >= 0) {
        vmid.defaultUserList.push(eachUser)
      }
    })

    setTimeout(function () {
      $("#repositoryOwners").select2()
      $("#repositoryOwners").val(vmid.repoInfo.repository.owners).trigger("change");
    }, 1000)

    $scope.hide = function () {
      $mdDialog.hide();
    };

    $scope.cancel = function () {
      $mdDialog.cancel();
    };

    $scope.answer = function (answer) {
      $mdDialog.hide(answer);
    };

    //Repo Owners List


    vmid.submitUpdateRepositoryOwners = function (data) {
      if (vmid.defaultUserList.length == 0 && data.repository.owners.length == 0) {
        Toaster.sayWarning("Kindly choose atleast one repository owner")
        return
      }
      if (!data.repository.description) {
        Toaster.sayWarning("Provide Description")
        return
      }
      _.each(ignore_users, function (eachUser) {
        if (rowObj.repository.owners.indexOf(eachUser) >= 0) {
          data.repository.owners.push(eachUser)
        }
      })
      APIFactory.repoUpdateRepository(data, function (response) {
        if (response.status) {
          Toaster.saySuccess("Updated Successfully");
          loadRepoTableList()
          $mdDialog.hide();
        } else {
          Toaster.sayError(response.errorMessage);
        }
      })
    };
  }

  /**************  Update Dialog - Ends  ********************/



})