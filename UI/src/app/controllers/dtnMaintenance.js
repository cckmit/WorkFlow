dashboard.controller("dtnMaintenanceCtrl", function ($rootScope, $templateCache, appSettings, $scope, $timeout, $state,
    $location, Toaster, $mdDialog, $http, $filter, apiService, APIFactory, WFLogger, Paginate, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    var apiBase = appSettings.apiBase;
    vm.searchData = {}
    Paginate.refreshScrolling();
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

    $scope.changeSearchData = function () {
        if (vm.searchData.searchTextDTN.length > 0 && vm.searchData.searchField.length > 0) {
            vm.searchData.searchTextDTN = ""
            initTableSettings()
            loadDTNList(tableAttr)
        }
    }
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
                "limit": 10,
                "orderBy": { "projectNumber": "asc" }
            }
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

    /*
    ZTPFM-2232 getting Project Manager list from dev manager user by role
    */
    function projectManagers() {
        try {
            APIFactory.getUsersByRole({
                "role": "DevManager"
            }, function (response) {
                if (response.status) {
                    $scope.projectManagersMapList = response.data;
                    _.each($scope.projectManagersMapList, function (pObj) {
                        $scope.projectManagersMapList[pObj.id] = pObj
                    })
                    $rootScope.saveformData()
                } else {
                    $scope.projectManagersMapList = []
                }

            })
        } catch (err) { }
    }
    projectManagers();


    // Insert DTN
    $scope.showInsertDTNDialog = function (ev) {
        try {
            $mdDialog.show({
                controller: insertDTNDialogCtrl,
                templateUrl: 'html/templates/insertDTN.template.html',
                controllerAs: 'vmid',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });

        } catch (err) { }
    }

    function insertDTNDialogCtrl($scope, $mdDialog) {
        try {
            var vmid = this;
            vmid.form = {}
            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };
            vmid.loadDevManagerName = function () {

                try {

                    APIFactory.getUsersByRole({ "role": "DevManager" }, function (response) {
                        if (response.status) {
                            vmid.devManagerList = response.data;
                            $rootScope.saveformData()
                        } else {
                            vmid.devManagerList = []

                        }

                        initMultipleSelect2("#approver")
                        $timeout(function () {
                            $("#approver").select2({
                                maximumSelectionLength: 1
                            });
                        }, 50)
                    })
                } catch (err) { }
            }
            vmid.loadDevManagerName();
            vmid.form.maintanceFlag = "No";
            vmid.submitDTNDetails = function (data) {
                try {

                    if (Object.keys(data).length == 0) {
                        Toaster.sayWarning("No data entered");
                        return;
                    }
                    if (!vmid.form.projectNumber) {
                        Toaster.sayWarning("Please enter Project Number");
                        return;
                    }
                    if (!vmid.form.managerId) {
                        Toaster.sayWarning("Please enter Project Manager");
                        return;
                    }
                    if (!vmid.form.projectName) {
                        Toaster.sayWarning("Please enter Project Title");
                        return;
                    }
                    var pattern = new RegExp(/(?:(?=(^([0-9]){5}_([0-9]){2}$))|(?=(^([0-9]){6}_([0-9]){2}?$)))/g)
                    if (!pattern.test($.trim(vmid.form.projectNumber))) {
                        Toaster.sayWarning("Please Enter Valid Project Number");
                        return;
                    }




                    vmid.post = {
                        "projectNumber": vmid.form.projectNumber,
                        "managerId": vmid.form.managerId,
                        "managerName": vmid.form.managerName ? vmid.form.managerName : "",
                        "projectName": vmid.form.projectName,
                        "lineOfBusiness": vmid.form.lineOfBusiness ? vmid.form.lineOfBusiness : "",
                        "sponsorId": vmid.form.sponsorId ? vmid.form.sponsorId : "",
                        "maintanceFlag": vmid.form.maintanceFlag ? vmid.form.maintanceFlag : ""
                    }
                    if (Array.isArray(vmid.post.managerId)) {
                        vmid.post.managerId = vmid.post.managerId ? (vmid.post.managerId > 1 ? vmid.post.managerId.join(",") : vmid.post.managerId[0]) : ""
                    }
                    APIFactory.saveDTNDetails({}, vmid.post, function (response) {
                        if (response.status) {
                            Toaster.saySuccess("Delta CSR created successfully");
                            $mdDialog.hide();
                            // initTableSettings()
                            loadDTNList(tableAttr)
                        } else {
                            Toaster.sayError(response.errorMessage);
                        }
                    })
                } catch (err) { }
            };
        } catch (err) { }
    }

    // Update DTN
    $scope.showUpdateDTNDialog = function (ev, rowObj) {
        try {
            selectedRowId = rowObj
            $mdDialog.show({
                controller: updateDTNDialogCtrl,
                templateUrl: 'html/templates/updateDTN.template.html',
                controllerAs: 'vmid',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });

        } catch (err) { }

    }

    function updateDTNDialogCtrl($scope, $mdDialog, appSettings) {

        try {
            var vmid = this;
            vmid.form = angular.copy(selectedRowId)
            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            vmid.loadDevManagerName = function () {
                try {
                    APIFactory.getUsersByRole({ "role": "DevManager" }, function (response) {
                        if (response.status) {
                            vmid.devManagerList = response.data;
                            $rootScope.saveformData()
                        } else {
                            vmid.devManagerList = []
                        }
                        initMultipleSelect2("#approver")
                        $timeout(function () {
                            $("#approver").select2({
                                maximumSelectionLength: 1
                            });
                        }, 50)
                    })
                } catch (err) { }
            }
            vmid.loadDevManagerName();

            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };

            vmid.updateDTNDetails = function (data) {
                try {
                    if (Object.keys(data).length == 0) {
                        Toaster.sayWarning("No data entered");
                        return;
                    }
                    if (!vmid.form.projectNumber) {
                        Toaster.sayWarning("Please enter Project Number");
                        return;
                    }
                    if (!vmid.form.managerId || vmid.form.managerId.length == 0) {
                        Toaster.sayWarning("Please enter Project Manager");
                        return;
                    }
                    if (!vmid.form.projectName) {
                        Toaster.sayWarning("Please enter Project Title");
                        return;
                    }
                    var pattern = new RegExp(/(?:(?=(^([0-9]){5}_([0-9]){2}$))|(?=(^([0-9]){6}_([0-9]){2}?$)))/g)

                    if (!pattern.test($.trim(vmid.form.projectNumber))) {
                        Toaster.sayWarning("Please Enter Valid Project Number");
                        return;
                    }

                    vmid.post = angular.copy(data)
                    // vmid.post = {
                    //     "projectNumber" : vmid.form.projectNumber,
                    //     "managerId" : vmid.form.managerId,
                    //     "projectName" : vmid.form.projectName,
                    //     "lineOfBusiness" : vmid.form.lineOfBusiness ? vmid.form.lineOfBusiness : "",
                    //     "sponsorId" : vmid.form.sponsorId ? vmid.form.sponsorId : "",
                    //     "createdBy" : vmid.form.createdBy
                    // }
                    if (Array.isArray(vmid.post.managerId)) {
                        vmid.post.managerId = vmid.post.managerId ? (vmid.post.managerId > 1 ? vmid.post.managerId.join(",") : vmid.post.managerId[0]) : ""
                    }
                    APIFactory.updateDTNDetails({}, vmid.post, function (response) {
                        if (response.status) {
                            Toaster.saySuccess("Delta CSR updated successfully");
                            $mdDialog.hide();
                            // initTableSettings()
                            loadDTNList(tableAttr)
                        } else {
                            Toaster.sayError(response.errorMessage);
                        }
                    })

                } catch (err) { }


            };

        } catch (err) { }

    }

    //Delete DTN
    $scope.confirmDeleteDTNDetails = function (ev, id) {
        try {
            var confirm = $mdDialog.confirm()
                .title('Would you like to delete ?')
                .ariaLabel('Confirm delete DTN Record')
                .targetEvent(ev)
                .ok('Yes')
                .cancel('No');
            $mdDialog.show(confirm).then(function () {
                vm.deleteDTNDetails(id)
            }, function () {

            });

        } catch (err) { }
    };



    vm.deleteDTNDetails = function (id) {
        try {
            selectedRowId = id
            APIFactory.deleteDTNDetails({}, selectedRowId, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Delta CSR deleted successfully")
                    // initTableSettings()
                    loadDTNList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })

        } catch (err) { }


    }


})