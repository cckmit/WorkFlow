dashboard.controller("onlineFeedbackCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, WSService, $mdDialog) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    IPService.getExpandView($scope, vm)
    $scope.macroInitSort = true;
    $scope.eTypeInitSort = true;
    $scope.auxTypeInitSort = true;
    $scope.keyLength = function (obj) {
        return Object.keys(obj).length;
    }

    WSService.initOnlineProcess(function (response) {
        Toaster.sayStatus(response.status)
        $rootScope.saveformData()
    })

    $scope.RFCClick = function (pObj) {
        var getBase1 = IPService.initRFC($scope, vm, pObj)
    }

    vm.defaultIsChecked = true;
    vm.loadTypeList = {
        "_ETYPE": "E-Type Load"
    }
    vm.currentLoadType = "_ETYPE";
    $scope.showETYPETab = true;
    $scope.loadTypePlans = function (type) {
        vm.searchPlanData = ""
        $scope.macroInitSort = true;
        $scope.eTypeInitSort = true;
        $scope.auxTypeInitSort = true;
        vm.currentLoadType = type;

        $scope.refresh()
    }

    /* Pagination Table Starts */


    var tableAttr, columnsToBeSorted;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        // $scope.tableConfig.pageSize = 20 // Default page Size for TSD
        $scope.tableDefaultValue = Paginate.defaultPageValue()
        // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for TSD
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        if (vm.currentLoadType == "_ETYPE") {
            //Not a standard field, if sorting affects plz check in productionloadsdao.findTobeLoaded
            columnsToBeSorted = ["loads.planId.id", "loads.planId.loadType", "sysload.loadDateTime", "loads.activatedDateTime"]
        }
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
    }
    initTableSettings()
    $scope.switchPageSize = function () {
        tableAttr.offset = 0
		tableAttr.limit = $scope.tableConfig.pageSize;
		$scope.tableConfig.currentPage = 1
		localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
		tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
		$rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
        loadList(tableAttr)
    }
    $scope.searchPlanData = function (searchData) {
        initTableSettings()
        tableAttr.filter = searchData ? searchData : ""
        tableAttr.offset = 0;
        loadList(tableAttr)
    }

    $scope.refresh = function () {
        initTableSettings()
        if (vm.searchPlanData) {
            tableAttr.filter = searchPlanData ? searchPlanData : ""
        }
        tableAttr.offset = $rootScope.paginateValue
        loadList(tableAttr)
    }
    $scope.refreshData = function () {
        initTableSettings()
        vm.searchPlanData = ""
        tableAttr.offset = $rootScope.paginateValue
        loadList(tableAttr)
    }

    function loadList(tableAttr) {
        if (vm.currentLoadType == "_ETYPE") {
            loadsetETypeList(tableAttr)
        }
    }

    $scope.pageChangeHandler = function (num) {
        if (vm.loadsetReadyList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
        tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
        $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadList(tableAttr)
    }

    // TSD Accept MultiSelect Logic start
    $scope.refreshTSDAccept = function () {
        $scope.refreshData()
    }
    /* Pagination Table Ends */
    vm.loadsetReadyList = {}
    function loadsetETypeList(tableAttr) {
        if ($scope.eTypeInitSort) {
            tableAttr.orderBy = {
                "sysload.loadDateTime": "asc"
            }
            $scope.sortColumn["sysload.loadDateTime"]["asc"] = true;
            $scope.eTypeInitSort = false;
        }
        APIFactory.getAcceptedPlans(tableAttr, function (response) {
            if (response.status) {
                if (Object.keys(response.data).length > 0) {
                    vm.loadsetReadyList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    _.each(vm.loadsetReadyList, function (rsObj) {
                        if (IPService.getExpandView($scope, vm)) {
                            $scope.planStatusModal(rsObj.plan.id)
                        }

                    })
                }
                else {
                    vm.loadsetReadyList = {}
                }
            } else {
                vm.loadsetReadyList = {}
                Toaster.sayError(response.errorMessage)
            }
        })
    }


    // TRACKER STARTS

    $scope.planStatusModal = function (planId) {
        $scope.plan_id = planId.id;
        planId.implementationMessage = [];
        planId.implementationStatus = [];
        planId.implementationId = [];
        APIFactory.getPlanTrackStatus({ "planId": $scope.plan_id }, function (l_response) {
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
})     //New fallback ends..
