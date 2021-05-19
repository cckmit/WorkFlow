dashboard.controller("helpCtrl", function($scope, $rootScope, $state,
    APIFactory, Paginate) {
    $rootScope.titleHeading = $state.current.data.pageTitle
    var vm = this;
    vm.currentList = "System List";
    Paginate.refreshScrolling();
    /* Pagination Table Starts */
    // var columnsToBeSorted = ["systemId.name", "putLevel"]
    vm.usersOf = {}
    vm.selectedUserOf = {}
    vm.allRoles = ["DevManager", "TechnicalServiceDesk", "LoadsControl", "Reviewer", "Lead", "Developer", "QA", "SystemSupport", "ToolAdmin"]
    var tableAttr;
    var columnsToBeSorted = ["systemId.name"]
    var initTableSettings = function() {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
    }
    initTableSettings()
    $scope.switchPageSize = function() {
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
        if (vm.currentList == "System Vpars List") {
            systemVParsList(tableAttr)
        }
        if (vm.currentList == "System Cpu list") {
            systemCPUList(tableAttr)
        }
        if (vm.currentList == "System List") {
            systemList()
        }

    }
    $scope.currentList = function(listname) {
        vm.currentList = listname;
        if (vm.currentList == "Role_List") {
            loadRoles()
        } else {
            $scope.refresh()
        }
    }
    $scope.refresh = function() {
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadSpecificList(tableAttr)
    }

    $scope.pageChangeHandler = function(num) {
        /* if (vm.loadCategoryList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        } */
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadSpecificList(tableAttr)
    };

    function loadSpecificList(tableAttr) {
        if (vm.currentList == "System Vpars List") {
            systemVParsList(tableAttr)
        }
        if (vm.currentList == "System Cpu list") {
            systemCPUList(tableAttr)
        }
        if (vm.currentList == "System List") {
            systemList()
        }
    }

    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function(columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadSpecificList(tableAttr)
        }
        /* Pagination Table Ends */


    // APIFactory.getSystemList(function (response) {
    //     vm.systemLists = response.data;
    // })

    function systemList() {
        selectedRowId = null;
        APIFactory.getSystemList(function(response) {
            vm.systemLists = response.data;
        })
    }

    function systemVParsList(tableAttr) {
        selectedRowId = null;
        APIFactory.getVparsList(tableAttr, function(response) {
            if (response.data.length > 0) {
                vm.systemVparsLists = response.data
                $scope.tableConfig.totalItems = response.count
                $rootScope.saveformData()
            } else {
                vm.systemVparsLists = []
            }
        })
    }

    function systemCPUList(tableAttr) {
        selectedRowId = null;
        APIFactory.getSystemCpuList(tableAttr, function(response) {
            if (response.data.length > 0) {
                vm.systemCPULists = response.data
                $scope.tableConfig.totalItems = response.count
                $rootScope.saveformData()
            } else {
                vm.systemCPULists = []
            }
        })
    }

    function loadRoles() {
        angular.forEach(vm.allRoles, function(roleName) {
            APIFactory.getUsersByRole({
                "role": roleName
            }, function(response) {
                if (response.status) {
                    vm.usersOf[roleName] = response.data
                    $rootScope.saveformData()
                } else {
                    vm.usersOf[roleName] = []
                }
            })
        })
    }

    APIFactory.getAppInfo({}, function(response) {
        vm.app_info = response
    })

})