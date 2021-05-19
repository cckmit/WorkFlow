dashboard.controller("projNbrDisplayCtrl", function ($rootScope, $state, $stateParams, $scope, $timeout, $location, appSettings, Toaster, $http,
    fImplementationPlanValidate, $mdDialog, apiService, APIFactory, WFLogger, WSService, freezeService, IPService, Access, Paginate) {

    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.sourceData = {};
    var apiBase = appSettings.apiBase;
    Paginate.refreshScrolling();
    vm.searchData = {}
    vm.searchData.searchTextDTN = ""
    vm.searchData.searchField = ""
    vm.searchFieldOptions =
        {
            "projectNumber": "Project Number",
            "managerName": "Project Manager",
            "projectName": "Project Title",
            "lineOfBusiness": "Line of Business",
            "sponsorId": "Project Sponsor"
        }
    var selectedRowId = null;
    /* Pagination Table Starts */
    var columnsToBeSorted = ["projectNumber", "managerId", "projectName", "lineOfBusiness", "sponsorId"]
    var tableAttr;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
        tableAttr.orderBy = {
            "projectNumber": "asc"
        }
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
        loadDTNList(tableAttr)
    }

    $scope.refresh = function () {
        vm.searchData.searchTextDTN = ""
        vm.searchData.searchField = ""
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadDTNList(tableAttr)
    }
    $scope.pageChangeHandler = function (num) {
        if (vm.dtnList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadDTNList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadDTNList(tableAttr)
    }
    /* Pagination Table Ends */
    // On Load API Call
    function loadDTNList(tableAttr) {
        selectedRowId = null;
        if (vm.searchData.searchTextDTN.length > 0 && vm.searchData.searchField.length > 0) {
            tableAttr.filter = vm.searchData.searchTextDTN
            tableAttr.searchField = vm.searchData.searchField
        } else {
            tableAttr.filter = ""
            tableAttr.searchField = ""
        }
        APIFactory.getProjectListForDeltaDTN(tableAttr, function (response) {
            if (response.status && response.data.length > 0) {
                vm.dtnList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                $rootScope.saveformData()
            } else {
                vm.dtnList = []
            }
        });
    }

    /*
   ZTPFM-2232 getting Project Manager list from dev manager user by role
   */
    function projectManagers() {
        $scope.projectManagersMapList = {}
        APIFactory.getUsersByRole({
            "role": "DevManager"
        }, function (response) {
            if (response.status) {
                $scope.projectManagersMapList = response.data;
                _.each($scope.projectManagersMapList, function (pObj) {
                    $scope.projectManagersMapList[pObj.id] = pObj
                })
            } else {
                $scope.projectManagersMapList = []
            }
        })
    }
    projectManagers();
    //Search Call
    $scope.searchDTN = function (data) {
        try {
            if (!data.searchField || data.searchField == '') {
                Toaster.sayWarning("Please select a search field")
                return
            }

            if (!data.searchTextDTN || data.searchTextDTN == '') {
                Toaster.sayWarning("Please enter a search Text")
                return;
            }

            vm.searchParams = {
                "filter": data.searchTextDTN,
                "searchField": data.searchField,
                "offset": 0,
                "limit": $rootScope.paginateDefaultValue,
                "orderBy": { "projectNumber": "asc" }
            }
            tableAttr = vm.searchParams;
            APIFactory.getProjectListForDeltaDTN(vm.searchParams, function (response) {
                if (response.status) {
                    vm.dtnList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    $rootScope.saveformData()
                    // initTableSettings() //doubt
                    // loadDTNList(vm.searchParams) //doubt
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        } catch (err) {

        }
    }

    $scope.exportProjNbr = function () {
        tableAttr.limit = '';
        APIFactory.exportProjNbrDisplay(tableAttr, function (response) {
            if (response.status) {
                var resposeStr = base64ToArrayBuffer(response.data)
                var file = new Blob([resposeStr], {
                    type: response.metaData
                });
                var dateObj = new Date()
                var fileName = "EXPORT_PROJNBRDISPLAY_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
                saveAs(file, fileName)
            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }

})