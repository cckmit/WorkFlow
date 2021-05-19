dashboard.controller("buildQueueCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, WSService, $mdDialog) {
    $scope.buildScreenType = "ONLINE_BUILD"
    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    IPService.getExpandView($scope, vm)
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


    /* Pagination Table Starts */


    var tableAttr, columnsToBeSorted;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.tableDefaultValue = Paginate.defaultPageValue()
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
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
        loadBuildQueueList(tableAttr)
    }
    $scope.searchPlanData = function (searchData) {
        initTableSettings()
        tableAttr.filter = searchData ? searchData : ""
        tableAttr.offset = 0;
        loadBuildQueueList(tableAttr)
    }

    $scope.searchServerName = function (searchData) {
        tableAttr.filter = searchData ? searchData : ""
        vm.searchPlanData = ""

    }


    $scope.refreshData = function () {
        initTableSettings()
        if (vm.searchPlanData) {
            tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
        }
        tableAttr.offset = $rootScope.paginateValue
        loadBuildQueueList(tableAttr)
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
        loadBuildQueueList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadBuildQueueList(tableAttr)
    }

    // TSD Accept MultiSelect Logic start
    $scope.refreshBuild = function () {
        $scope.refreshData()
    }

    /* Pagination Table Ends */
    if ($scope.buildScreenType === 'ONLINE_BUILD') {
        $rootScope.interval = setInterval(function () {
            loadBuildQueueList(tableAttr);
            // every 20Sec this function calling
        }, 20000)
    }
    vm.buildQueueList = {}
    function loadBuildQueueList(tableAttr) {
        APIFactory.getBuildQueueByPlan(tableAttr, function (response) {
            if (response.status) {
                if (Object.keys(response.data).length > 0) {
                    vm.buildQueueList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    _.each(vm.buildQueueList, function (rsObj) {
                        if (IPService.getExpandView($scope, vm)) {
                            $scope.planStatusModal(rsObj.plan)
                        }

                    })
                }
                else {
                    vm.buildQueueList = {}
                    $scope.searchServerName("");

                }
            } else {
                vm.buildQueueList = {}
                clearInterval($rootScope.interval);
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
