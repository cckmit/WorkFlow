dashboard.controller("putLevelDD", function ($rootScope, $templateCache, appSettings, $scope, $filter, $state, $location, Toaster, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    var apiBase = appSettings.apiBase;
    $scope.userRole = getUserData('userRole')
    var selectedRowId = null;
    Paginate.refreshScrolling();
    vm.putLevelDataList = []


    /* Pagination Table Starts */
    var columnsToBeSorted = ["systemId.name", "putLevel"]
    var tableAttr;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        if (tableAttr != null) { var oldFilter = tableAttr.filter }
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        if (oldFilter != undefined || oldFilter != null) { tableAttr.filter = oldFilter }
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
        loadPutList(tableAttr)
    }

    $scope.systemList = []

    APIFactory.getSystemList(function (response) {
        if (response.status) {
            $scope.systemList = response.data;
        } else {
            Toaster.sayError(response.errorMessage)
        }
    })

    $scope.refresh = function () {
        initTableSettings()
        tableAttr.filter = ""
        tableAttr.offset = $rootScope.paginateValue
        loadPutList(tableAttr)
    }

    $scope.pageChangeHandler = function (num) {
        if (vm.loadCategoryList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
        tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
        $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadPutList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadPutList(tableAttr)
    }
    /* Pagination Table Ends */

    function loadPutList(tableAttr) {
        selectedRowId = null;
        APIFactory.getPutLevelList(tableAttr, function (response) {
            if (response.status && response.data.length > 0) {
                vm.putLevelDataList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                $rootScope.saveformData()
            } else {
                vm.putLevelDataList = []
            }
        })
    }

    // Target System Search Filter
    $scope.filterSelectedSystem = function (systemId) {
        initTableSettings()
        tableAttr.filter = systemId
        loadPutList(tableAttr)
    }


    $scope.confirmDeletePutLevel = function (ev, id) {
        // Appending dialog to document.body to cover sidenav in docs app
        var confirm = $mdDialog.confirm()
            .title('Would you like to delete ?') //.textContent('All of the banks have agreed to forgive you your debts.')
            .ariaLabel('Confirm delete zTPFM Level deployment details')
            .targetEvent(ev)
            .ok('Yes')
            .cancel('No');
        $mdDialog.show(confirm).then(function () {
            vm.deletePutLevel(id);
        }, function () { });
    };


    vm.deletePutLevel = function (id) {
        APIFactory.deletePutLevel({
            "id": id
        }, function (response) {
            if (response.status) {
                Toaster.saySuccess("zTPFM Level Deleted Successfully")
                initTableSettings()
                loadPutList(tableAttr)
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }


    /**************  Insert Dialog - Starts  ********************/
    var forceInsert = false;
    $scope.showInsertDialog = function (ev) {
        $mdDialog.show({
            controller: insertDialogCtrl,
            templateUrl: 'html/templates/insertPutLevelDD.template.html',
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
    }

    function insertDialogCtrl($scope, $mdDialog) {
        var vmid = this;

        // Insert the record if it is true
        forceInsert = false;
        vmid.forceInsertMsg = false;
        //Initializing ng model for form
        vmid.putData = {}
        vmid.putData.status = "INITIAL";
        //Start date for calendar
        vmid.startDate = moment().format('MM/DD/YYYY')

        //Default Selection of 'Default' Field
        // vmid.putData.defaultPut = "N"
        vmid.loadOwnersList = function () {
            try {

                APIFactory.getLoadAttendeeList({}, function (response) {
                    if (response.status) {
                        vmid.loadOwnersList = response.data;
                    } else {
                        vmid.loadOwnersList = []
                    }
                    initMultipleSelect2("#owners")

                })
            } catch (err) { }
        }
        vmid.loadOwnersList();

        vmid.statusList = ["INITIAL", "DEVLOPMENT", "LOCKDOWN", "PRODUCTION", "BACKUP", "ARCHIVE", "INACTIVE"]



        // Get Systems List
        APIFactory.getSystemList(function (response) {
            if (response.status && response.data.length > 0) {
                vmid.systemsList = response.data
            } else {
                vmid.systemsList = []
            }
        })

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        vmid.submitPutLevelDD = function (data) {
            var dData = angular.copy(data)
            if (!data) {
                Toaster.sayWarning("No data entered");
                return;
            }

            if (!data.systemId) {
                Toaster.sayWarning("Please choose Target system");
                return;
            }
            // if (!data.scmUrl && data.defaultPut == "Y") {
            //     Toaster.sayWarning("Provide SCM URL");
            //     return;
            // }
            if (!data.putLevel) {
                Toaster.sayWarning("Provide zTPF Level");
                return;
            }

            if (!data.ownerids || data.ownerids.length == 0) {
                Toaster.sayWarning("Provide Owners");
                return;
            }
            if (!data.putDate) {
                Toaster.sayWarning("Please choose Load Date");
                return;
            }

            if (!data.putTime) {
                Toaster.sayWarning("Please choose Load Time");
                return;
            }

            if (data.defaultPut == "Y") {
                dData.scmUrl = (dData.scmUrl.charAt(0) == "/") ? dData.scmUrl.substr(1) : dData.scmUrl
                dData.scmUrl = (dData.scmUrl.slice(-4) == ".git") ? dData.scmUrl : dData.scmUrl + ".git"
            }
            var lSystem = _.where(vmid.systemsList, {
                "id": parseInt(dData.systemId)
            })
            dData.systemId = lSystem[0]

            dData.putDateTime = Access.formatAPIDate(data.putDate + " " + $.trim(data.putTime) + ":00 " + moment(data.putDate + " " + $.trim(data.putTime)).tz(getTimeZone()).format("z"))
            dData = _.omit(dData, ["putDate", "putTime", "defaultPut"])
            if (data.defaultPut == "Y") {
                if (lSystem.length > 0 && lSystem[0].defalutPutLevel != null && !vmid.forceInsertMsg) {
                    Toaster.sayWarning("Default put already exists for the selected system")
                    vmid.forceInsertMsg = true;
                    return;
                }
            }
            if (Array.isArray(dData.ownerids)) {
                dData.ownerids = dData.ownerids ? (dData.ownerids.length > 1 ? dData.ownerids.join(",") : dData.ownerids[0]) : ""
            }

            var pultLevel = dData.putLevel;
            dData.putLevel = pultLevel.toUpperCase();

            APIFactory.savePutLevel(dData, function (response) {
                if (response.status) {
                    Toaster.saySuccess("zTPF Level created successfully")
                    $mdDialog.hide();
                    initTableSettings()
                    loadPutList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })

        };
    }



    /**************  Update Dialog - Starts  ********************/
    $scope.showUpdateDialog = function (ev, updatedata) {
        selectedRowData = updatedata
        if (selectedRowData) {
            $mdDialog.show({
                controller: updateDialogCtrl,
                templateUrl: 'html/templates/updatePutLevelDD.template.html',
                controllerAs: 'vmid',
                parent: angular.element(document.body),
                targetEvent: ev,
                canUpdateOthers: selectedRowData.canUpdateOthers,
                canUpdateStatus: selectedRowData.canUpdateStatus,
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

    function updateDialogCtrl($scope, $mdDialog, options, canUpdateOthers, canUpdateStatus) {
        var vmid = this;
        // Insert the record if it is true
        forceInsert = false;
        vmid.forceInsertMsg = false;
        vmid.canUpdateOthers = canUpdateOthers;
        vmid.canUpdateStatus = canUpdateStatus;
        //Initializing ng model for form
        vmid.putData = {}

        //Start date for calendar
        vmid.startDate = moment().format(appSettings.dateFormat)
        _.extend(vmid.putData, selectedRowData.putLevel)
        vmid.putData.putLevel = selectedRowData.putLevel.putLevel
        vmid.putData.scmUrl = selectedRowData.putLevel.scmUrl
        vmid.putData.startingSequence = selectedRowData.putLevel.startingSequence
        vmid.putData.endingSequence = selectedRowData.putLevel.endingSequence
        vmid.putData.ownerids = selectedRowData.putLevel.ownerids
        vmid.putData.systemId = selectedRowData.putLevel.systemId.id
        // vmid.putData.defaultPut = (selectedRowData.putLevel.id == selectedRowData.putLevel.systemId.defalutPutLevel) ? "Y" : "N"
        vmid.putData.active = selectedRowData.putLevel.active
        vmid.putData.status = selectedRowData.putLevel.status

        vmid.loadOwnersList = function () {
            try {

                APIFactory.getLoadAttendeeList({}, function (response) {
                    if (response.status) {
                        vmid.loadOwnersList = response.data;
                    } else {
                        vmid.loadOwnersList = []
                    }
                    initMultipleSelect2("#owners")

                })
            } catch (err) { }
        }
        vmid.loadOwnersList();

        // vmid.statusList = [ "INITIAL","DEVLOPMENT","LOCKDOWN","PRODUCTION","BACKUP","ARCHIVE","INACTIVE" ]
        vmid.statusList = options;



        var put_dt = $filter("formattedDateTimeWithoutSeconds")(selectedRowData.putLevel.putDateTime)
        vmid.putData.putDate = put_dt.split(" ")[0]
        vmid.putData.putTime = put_dt.split(" ")[1]
        vmid.putData.putDateTime = vmid.putData.putDate + " " + vmid.putData.putTime

        vmid.putData.id = selectedRowData.putLevel.id

        vmid.momentdeploymentDate = _.clone(vmid.putData.putDate)
        if (moment(vmid.momentdeploymentDate, appSettings.dateFormat).diff(moment().format(appSettings.dateFormat)) >= 0) {
            vmid.momentdeploymentDate = moment().format(appSettings.dateFormat)
        }

        vmid.isDeploymentSelectable = function (date, type) {
            return type != 'day' || date.format(appSettings.dateFormat) == vmid.momentdeploymentDate || moment(date.format(appSettings.dateFormat)).diff(moment().format("MM-DD-YYYY")) >= 0;
        };

        // Get Systems List
        APIFactory.getSystemList(function (response) {
            if (response.status && response.data.length > 0) {
                vmid.systemsList = response.data
            } else {
                vmid.systemsList = []
            }
        })


        $scope.cancel = function () {
            $mdDialog.cancel();
        };

        vmid.updatePutLevelDD = function (data) {
            var dData = angular.copy(data)
            if (!data) {
                Toaster.sayWarning("No data entered");
                return;
            }

            if (!data.systemId) {
                Toaster.sayWarning("Please choose Target system");
                return;
            }

            if (data.status == "") {
                Toaster.sayWarning("Provide Status");
                return;
            }

            // if (!data.scmUrl && data.defaultPut == "Y") {
            //     Toaster.sayWarning("Provide SCM URL");
            //     return;
            // }
            if (!data.ownerids || data.ownerids.length == 0) {
                Toaster.sayWarning("Provide Owners");
                return;
            }

            if (!data.putDate) {
                Toaster.sayWarning("Please choose Load Date");
                return;
            }

            if (!data.putTime) {
                Toaster.sayWarning("Please choose Load Time");
                return;
            }

            if (!data.putLevel) {
                Toaster.sayWarning("Provide zTPF Level");
                return;
            }

            if (data.defaultPut == "Y") {
                dData.scmUrl = (dData.scmUrl.charAt(0) == "/") ? dData.scmUrl.substr(1) : dData.scmUrl
                dData.scmUrl = (dData.scmUrl.slice(-4) == ".git") ? dData.scmUrl : dData.scmUrl + ".git"
            }
            var lSystem = _.where(vmid.systemsList, {
                "id": parseInt(dData.systemId)
            })
            dData.systemId = lSystem[0]
            dData.putDateTime = Access.formatAPIDate(data.putDate + " " + $.trim(data.putTime) + ":00 " + moment(data.putDate + " " + $.trim(data.putTime)).tz(getTimeZone()).format("z"))
            dData = _.omit(dData, ["putDate", "putTime", "defaultPut"])
            if (data.defaultPut == "Y") {
                if (lSystem.length > 0 && lSystem[0].defalutPutLevel != null && !vmid.forceInsertMsg) {
                    Toaster.sayWarning("Default put already exists for the selected system")
                    vmid.forceInsertMsg = true;
                    return;
                }
            }

            if (Array.isArray(dData.ownerids)) {
                dData.ownerids = dData.ownerids ? (dData.ownerids.length > 1 ? dData.ownerids.join(",") : dData.ownerids[0]) : ""
            }
            var pultLevel = dData.putLevel;
            dData.putLevel = pultLevel.toUpperCase();
            // {"defaultPut": (data.defaultPut == "Y") ? true : false},
            APIFactory.savePutLevel(dData, function (response) {
                if (response.status) {
                    Toaster.saySuccess("zTPF Level created successfully")
                    $mdDialog.hide();
                    initTableSettings()
                    loadPutList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })

        };
    }

});