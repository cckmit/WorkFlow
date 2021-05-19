app.service('RService', function ($http, $q, appSettings, Flash, $location, WFLogger, $timeout,
    APIFactory, apiService, $mdDialog, Paginate, Toaster, $rootScope) {

    var RService = {};
    Paginate.refreshScrolling();
    var initReview = function ($scope, vm, callType) {
        function loadReviewTaskList(tableAttr) {
            vm.hideAction = false;
            if (callType == "history") {
                vm.hideAction = true;
                APIFactory.reviewerHistory(tableAttr, function (response) {
                    if (response.status && response.data.length > 0) {
                        vm.reviewTasksList = response.data
                        $scope.tableConfig.totalItems = response.count
                        vm.totalNumberOfItem = response.count
                        $rootScope.saveformData()
                    } else {
                        vm.reviewTasksList = []
                    }
                })
            } else {
                vm.hideAction = false;
                APIFactory.reviewerMyTasks(tableAttr, function (response) {
                    if (response.status && response.data.length > 0) {
                        vm.reviewTasksList = response.data
                        $scope.tableConfig.totalItems = response.count
                        vm.totalNumberOfItem = response.count
                        $rootScope.saveformData()
                    } else {
                        vm.reviewTasksList = []
                    }
                })
            }
        }

        /* Pagination Table Starts */
        var columnsToBeSorted = ["id"]
        var tableAttr;
        var initTableSettings = function () {
            $scope.tableConfig = Paginate.tableConfig()
            $scope.pageSizeList = Paginate.pageSizeList()
            tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
            $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
        }
        initTableSettings()
        $scope.switchPageSize = function () {
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
            loadReviewTaskList(tableAttr)
        }
        $scope.pageChangeHandler = function (num) {
            if (vm.reviewTasksList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            tableAttr.filter = vm.filterText ? vm.filterText : ""
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
		tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
		$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadReviewTaskList(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadReviewTaskList(tableAttr)
        }

        /* Pagination Table Ends */

        $scope.loadSystemsAndSegmentsList = function (implementation) {
            APIFactory.getSystemLoadByPlan({ "ids": implementation.planId.id }, function (response) {
                if (response.status) {
                    implementation.systemLoadList = response.data
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
            APIFactory.getSegmentList({ "ids": implementation.id }, function (response) {
                if (response.status && response.data.length > 0) {
                    implementation.segments = response.data
                } else {
                    implementation.segments = []
                }
            })
            APIFactory.listTestCases({
                "implId": implementation.id,
                "planId": implementation.planId.id
            }, function (response) {
                if (response.status && response.data.length > 0) {
                    implementation.uploadedFiles = response.data
                } else {
                    implementation.uploadedFiles = [];
                }
            })
        }

        $scope.reviewCompletedDisable = {}
        $scope.submitReview = function (id) {
            $scope.reviewCompletedDisable[id] = true;
            APIFactory.approveReview({ "implId": id }, function (response) {
                if (response.status) {
                    $scope.reviewCompletedDisable[id] = false;
                    Toaster.saySuccess("Peer Review Ticket Updated")
                    if (response.data != null) {
                        $timeout(function () {
                            Toaster.sayWarning(response.data);
                        }, 1000)

                    }
                    initTableSettings()
                    loadReviewTaskList(tableAttr)
                } else {
                    $scope.reviewCompletedDisable[id] = false;
                    Toaster.sayError(response.errorMessage);
                }
            })
        }

        $scope.downloadTestCaseImpl = function (fileName, implementation) {
            try {
                var params = {
                    "implId": implementation.id,
                    "planId": implementation.planId.id,
                    "testFile": fileName
                }
                APIFactory.downloadTestCase(params, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], { type: response.metaData });
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }

        }

        return {
            "scope": $scope,
            "vm": vm
        }
    }
    RService.initReview = initReview;

    return RService;

});