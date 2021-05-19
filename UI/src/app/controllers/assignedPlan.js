dashboard.controller("assignedPlanCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, $mdDialog, WFLogger, IPService, Paginate, WSService, $timeout, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;

    var table = null
    var selectedRowId = null;
    vm.assignedPlanList = []
    $scope.timeZone = Access.refactorTimeZone(moment().tz(getTimeZone()).format("z"));

    Paginate.refreshScrolling();

    $scope.RFCClick = function (pObj) {
        var getBase1 = IPService.initRFC($scope, vm, pObj)
    }

    /* Pagination Table Starts */
    var columnsToBeSorted = ["id", "planDesc", "loaddatetime", "createdBy"]
    var tableAttr;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
    }
    initTableSettings()
    $scope.switchPageSizeTable = function () {
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
        assignedPlanList(tableAttr)
    }
    vm.planStatusList = Access.getAllPlanStatus()

    $scope.statusFilter = function (status) {
        var selectstatus = _.findKey(vm.planStatusList, function (item) { return item == status });
        vm.filterText = selectstatus ? selectstatus : "";
        $scope.refresh()
    }

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
        tableAttr.planStatus = vm.filterText ? vm.filterText : ""
        if (vm.searchPlanData) {
            tableAttr.planId = vm.searchPlanData ? vm.searchPlanData : ""
        }
        assignedPlanList(tableAttr)
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
        tableAttr.planId = searchData ? searchData : ""
        if (vm.filterText) {
            tableAttr.planStatus = vm.filterText ? vm.filterText : ""
        }
        tableAttr.offset = 0;
        assignedPlanList(tableAttr)
    }

    $scope.pageChangeHandlerTable = function (num) {
        if (vm.assignedPlanList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        assignedPlanList(tableAttr)
    };
    $scope.pageChangeHandlerTable($scope.tableConfig.currentPage)
    $scope.sortTable = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        assignedPlanList(tableAttr)
    }
    /* Pagination Table Ends */

    // ZTPFM-2275 Plan Status Check

    $scope.getDeploymentStatusByPlan = function (planStatus) {
        if (planStatus === "DEV_MGR_APPROVED" || planStatus === "READY_FOR_PRODUCTION_DEPLOYMENT") {
            return true;
        }
    }

    //ZTPFM-2275 Commont box added
    $scope.deploymentStatus = function (e, planId) {

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
                $scope.refresh()
            }, function () {
            });
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
                    "deploymentStartAndStopReason": ld.deploymentStatusChangeComment.replace(/["“”']+/g, '')
                }
                APIFactory.deploymentStatusChange(paramObj, function (response) {
                    if (response.status) {
                        Toaster.sayStatus("Deployment status changed " + planId)
                        $mdDialog.hide();
                        // $scope.refresh();
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
                // $scope.refresh();
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }

    }

    function assignedPlanList(tableAttr) {
        APIFactory.getAssignedPlansAndSysLoad(tableAttr, function (response) {
            if (response.status) {
                vm.totalNumberOfItem = response.count
                $scope.tableConfig.totalItems = response.count
                vm.assignedPlanList = _.pluck(response.data, "plan")
                vm.systemListforDM = _.pluck(response.data, "systemLoadDetails")
                _.each(vm.assignedPlanList, function (pObj) {
                    pObj.systemLoadList = []
                    _.each(vm.systemListforDM, function (sObj) {
                        _.each(sObj, function (lObj) {
                            if (pObj.id == lObj.systemLoad.planId.id) {
                                pObj.systemLoadList.push(lObj.systemLoad)
                            }
                        })
                    })
                })
            } else {
                vm.assignedPlanList = []
                vm.totalNumberOfItem = response.count
            }
        })
    }
    IPService.getExpandView($scope, vm)

})