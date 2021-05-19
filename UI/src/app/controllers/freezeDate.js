dashboard.controller("freezeDate", function ($rootScope, $templateCache, appSettings, $scope, $state,
    $location, Toaster, $mdDialog, $http, $filter, apiService, APIFactory, WFLogger, Paginate, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    var apiBase = appSettings.apiBase;

    var selectedRowId = null;
    Paginate.refreshScrolling();
    vm.freezeDataList = []
    vm.existingFreezeList = [];
    /* Pagination Table Starts */

    var columnsToBeSorted = ["name", "from_date"]
    var tableAttr;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        if (tableAttr != null) {
            var oldFilter = tableAttr.filter
        }
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        if (oldFilter != undefined || oldFilter != null) {
            tableAttr.filter = oldFilter
        }
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
        tableAttr.orderBy = {
            /* "loadCategoryId.systemId.name": "asc",
            "fromDate": "asc" */
        }
        /* $scope.sortColumn["loadCategoryId.systemId.name"]["asc"] = true
        $scope.sortColumn["fromDate"]["asc"] = true */
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
        loadFreezeList(tableAttr)
    }

    $scope.refresh = function () {
        initTableSettings()
        tableAttr.filter = ""
        tableAttr.offset = $rootScope.paginateValue
        loadFreezeList(tableAttr)

    }
    $scope.pageChangeHandler = function (num) {
        if (vm.freezeDataList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
        tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
        $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadFreezeList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadFreezeList(tableAttr)
    }

    /* Pagination Table Ends */


    $scope.showAction = function () {
        if (getUserData('userRole') === 'LoadsControl') {
            return true;
        } else {
            return false;
        }

    }
    $scope.systemList = []

    APIFactory.getSystemList(function (response) {
        if (response.status) {
            $scope.systemList = response.data;
            $rootScope.saveformData()
        } else {
            Toaster.sayError(response.errorMessage)
        }
    })


    // Target System Search Filter
    $scope.filterSelectedSystem = function (systemId) {
        initTableSettings()
        tableAttr.filter = systemId
        loadFreezeList(tableAttr)
    }
    function loadFreezeList(tableAttr) {
        selectedRowId = null;
        APIFactory.getLoadFreezeList(tableAttr, function (response) {
            if (response.status && response.data.length > 0) {
                vm.freezeDataList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                $rootScope.saveformData()
            } else {
                vm.freezeDataList = []
            }
        });
    }

    // $scope.getDateAlone = function(data) {
    //     var start_dt = $filter("formattedDateTimeWithoutSeconds")(data)
    //     var startEnd = start_dt.split(" ")[0]
    //     return startEnd
    // }

    $scope.confirmDeleteFreezeDate = function (ev, id) {
        try {
            var confirm = $mdDialog.confirm()
                .title('Would you like to delete ?') //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Confirm delete load Category')
                .targetEvent(ev)
                .ok('Yes')
                .cancel('No');
            $mdDialog.show(confirm).then(function () {
                vm.deleteFreeze(id)
            }, function () {

            });

        } catch (err) { }
    };



    vm.deleteFreeze = function (id) {
        try {
            selectedRowId = id;
            var deleteFree = {
                listIds: selectedRowId.listIds
            };
            APIFactory.deleteLoadFreeze(deleteFree, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Load Freeze Deleted Successfully")
                    initTableSettings()
                    loadFreezeList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })

        } catch (err) { }


    }


    /**************  Insert Dialog - Starts  ********************/
    $scope.showInsertDialog = function (ev) {
        try {

            $mdDialog.show({
                controller: insertDialogCtrl,
                templateUrl: 'html/templates/insertFreezeDate.template.html',
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

    function insertDialogCtrl($scope, $mdDialog, $state) {

        try {

            var vmid = this;

            vmid.form = {}
            vmid.form.loadCategoryId = {}
            vmid.form.loadCategoryId.systemId = {}

            vmid.startDate = moment().format('MM/DD/YYYY')
            APIFactory.getSystemList(function (response) {
                if (response.status && response.data.length > 0) {
                    vmid.systemsList = response.data
                    $rootScope.saveformData()
                } else {
                    vmid.systemsList = []
                }
            })


            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
                $state.reload();
            };

            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };
            $scope.selectSystem = function (systemid) {
                APIFactory.getLoadCategoryBySystem({ "ids": systemid }, function (response) {
                    $("#loadCategoryIdData").select2();
                    if (response.status && response.data.length > 0) {
                        vmid.loadCategory = []
                        for (var i = 0; i < response.data.length; i++) {
                            if (response.data[i].active == "Y" && response.data[i].name != "M") {
                                vmid.loadCategory.push(response.data[i]);
                            }
                        }
                    } else {
                        vmid.loadCategory = []
                    }
                })
            }




            vmid.submitFreezeDates = function (data) {


                try {

                    if (!data) {
                        Toaster.sayWarning("No data entered");
                        return;
                    }

                    if (!data.loadCategoryId.systemId.id) {
                        Toaster.sayWarning("Please choose Target system");
                        return;
                    }
                    if (!data.loadCategoryId.id) {
                        Toaster.sayWarning("Please choose Load category");
                        return;
                    }
                    if (!data.reason) {
                        Toaster.sayWarning("Please enter reason for freeze date");
                        return;
                    }

                    if (!data.fDate) {
                        Toaster.sayWarning("Please select from date");
                        return;
                    }
                    // if (!data.fTime) {
                    //     Toaster.sayWarning("Please select from time");
                    //     return;
                    // }
                    if (!data.tDate) {
                        Toaster.sayWarning("Please select to date");
                        return;
                    }

                    // if (!data.tTime) {
                    //     Toaster.sayWarning("Please select to time");
                    //     return;
                    // }


                    var from = moment(data.fDate + " ", "MM-DD-YYYY HH:mm")
                    var to = moment(data.tDate + " ", "MM-DD-YYYY HH:mm")
                    // if (to.diff(from, "minutes") === 0) {
                    //     Toaster.sayWarning("From time and To time should not be same");
                    //     return;
                    // }
                    // if (to.diff(from, "minutes") < 0) {
                    //     Toaster.sayWarning("From date should be lesser than To date");
                    //     return;
                    // }

                    vmid.form.fromDate = data.fDate + " " + "00:00:00 " + moment(data.fDate + " ", appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ")
                    vmid.form.toDate = data.tDate + " " + "00:00:00 " + moment(data.tDate + " ", appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ")

                    /* if (vmid.form.loadCategoryId.id == 0) {
                         vmid.post = {
                             "loadFreeze": {
                                 "reason": vmid.form.reason,
                                 "fromDate": vmid.form.fromDate,
                                 "toDate": vmid.form.toDate
                             },
                             "system": {
                                 "id": vmid.form.loadCategoryId.systemId.id
                             }
                         }
                     } else {
                         vmid.post = {
                             "loadFreeze": {
                                 "loadCategoryId": {
                                     "id": vmid.form.loadCategoryId.id,
                                     "systemId": {
                                         "id": vmid.form.loadCategoryId.systemId.id
                                     }
                                 },
                                 "reason": vmid.form.reason,
                                 "fromDate": vmid.form.fromDate,
                                 "toDate": vmid.form.toDate
                             }
                         }
                     }*/
                    var vmidPostList = [];
                    if (vmid.form.loadCategoryId.id.length != null && vmid.form.loadCategoryId.id.length > 0) {
                        for (var i = 0; i < vmid.form.loadCategoryId.id.length; i++) {
                            var objectLoadFreeze = {
                                "loadFreeze": {
                                    "loadCategoryId": {
                                        "id": vmid.form.loadCategoryId.id[i],
                                        "systemId": {
                                            "id": vmid.form.loadCategoryId.systemId.id
                                        }
                                    },
                                    "reason": vmid.form.reason,
                                    "fromDate": vmid.form.fromDate,
                                    "toDate": vmid.form.toDate
                                }
                            };

                            vmidPostList.push(objectLoadFreeze);
                        }
                    } else {
                        Toaster.sayWarning("Please choose Load category Id");
                        return;
                    }

                    APIFactory.saveLoadFreeze(vmidPostList, function (response) {
                        if (response.status) {
                            Toaster.saySuccess("Freeze Date Created Successfully");
                            $mdDialog.hide();
                            initTableSettings()
                            loadFreezeList(tableAttr)
                        } else {
                            Toaster.sayError(response.errorMessage);
                        }
                    })
                } catch (err) { }

            };

        } catch (err) { }


    }



    /**************  Update Dialog - Starts  ********************/
    $scope.showUpdateDialog = function (ev, rowObj) {
        $("#updateLoadCategoryIdData").select2();

        try {
            selectedRowId = rowObj
            vm.existingFreezeList = [];

            for (var i = 0; i < selectedRowId.listIds.length; i++) {
                var freezeExting = {
                    id: selectedRowId.listIds[i],
                    loadCategoryId: selectedRowId.loadCategoryIdList[i],
                    reason: selectedRowId.reason,
                    fromDate: selectedRowId.from_date,
                    toDate: selectedRowId.to_date,
                    systemId: selectedRowId.systemId,
                    active: "Y"
                };
                vm.existingFreezeList.push(freezeExting);
            }

            $mdDialog.show({
                controller: updateDialogCtrl,
                templateUrl: 'html/templates/updateFreezeDate.template.html',
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

    function updateDialogCtrl($scope, $mdDialog, appSettings) {

        try {
            var vmid = this;
            vmid.form = selectedRowId;
            APIFactory.getLoadCategoryBySystem({ "ids": selectedRowId.systemId }, function (response) {
                if (response.status && response.data.length > 0) {
                    vmid.loadCategory = []
                    // for (var i = 0; i < response.data.length; i++) {
                    //     if (response.data[i].active == "Y" && response.data[i].name != "M" ) {
                    //         vmid.loadCategory.push(response.data[i]);
                    //     }
                    // }
                    for (var i = 0; i < response.data.length; i++) {
                        if (selectedRowId.load_categories != "M") {
                            if (response.data[i].active == "Y" && response.data[i].name != "M") {
                                vmid.loadCategory.push(response.data[i]);
                            }
                        } else {
                            if (response.data[i].active == "Y") {
                                vmid.loadCategory.push(response.data[i]);
                            }
                        }

                    }
                    var select2 = $("#updateLoadCategoryIdData").select2({ width: '350px' });
                    select2.data('select2').$selection.css('height', '38px');
                    $('.select2').on('select2:open', function () {
                        $('.select2-selection__choice__remove').addClass('select2-remove-right');
                    });

                    setTimeout(function () {
                        $("#updateLoadCategoryIdData").val(selectedRowId.loadCategoryIdList).trigger("change")
                    }, 1000)
                } else {
                    vmid.loadCategory = []
                }
            });

            vmid.formExtra = {}
            var start_dt = $filter("formattedDateTimeWithoutSeconds")(selectedRowId.from_date)
            vmid.formExtra.fDate = start_dt.split(" ")[0]
            vmid.formExtra.fTime = start_dt.split(" ")[1]

            var to_dt = $filter("formattedDateTimeWithoutSeconds")(selectedRowId.to_date)
            vmid.formExtra.tDate = to_dt.split(" ")[0]
            vmid.formExtra.tTime = to_dt.split(" ")[1]

            vmid.momentfDate = _.clone(vmid.formExtra.fDate)
            if (moment(vmid.momentfDate, appSettings.dateFormat).diff(moment().format(appSettings.dateFormat)) >= 0) {
                vmid.momentfDate = moment().format(appSettings.dateFormat)
            }
            vmid.momenttDate = _.clone(vmid.formExtra.tDate)
            if (moment(vmid.momenttDate, appSettings.dateFormat).diff(moment().format(appSettings.dateFormat)) >= 0) {
                vmid.momenttDate = moment().format(appSettings.dateFormat)
            }
            vmid.isFromSelectable = function (date, type) {
                return type != 'day' || date.format(appSettings.dateFormat) == vmid.momentfDate || moment(date.format(appSettings.dateFormat)).diff(moment().format("MM-DD-YYYY")) >= 0;
            };

            vmid.isToSelectable = function (date, type) {
                return type != 'day' || date.format(appSettings.dateFormat) == vmid.momenttDate || moment(date.format(appSettings.dateFormat)).diff(moment().format("MM-DD-YYYY")) >= 0;
            };

            // APIFactory.getSystemList(function(response) {
            //     if (response.status && response.data.length > 0) {
            //         vmid.systemsList = response.data
            //         $scope.selectSystem(vmid.form.loadCategoryId.systemId.id)
            //     } else {
            //         vmid.systemsList = []
            //     }
            // })

            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
                initTableSettings()
                loadFreezeList(tableAttr)
            };

            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };
            // $scope.selectSystem = function(systemid) {
            //     APIFactory.getLoadCategoryBySystem({ "ids": systemid }, function(response) {
            //         if (response.status && response.data.length > 0) {
            //             vmid.loadCategory = response.data
            //         } else {
            //             vmid.loadCategory = []
            //         }
            //     })
            // }


            vmid.submitUpdateFreezeDates = function (data) {
                try {

                    // if (!data) {
                    //     Toaster.sayWarning("No data entered");
                    //     return;
                    // }

                    // if (!data.loadCategoryId.systemId.id) {
                    //     Toaster.sayWarning("Please choose Target system");
                    //     return;
                    // }
                    // if (!data.loadCategoryId.id) {
                    //     Toaster.sayWarning("Please choose Load category Id");
                    //     return;
                    // }
                    // if (!data.reason) {
                    //     Toaster.sayWarning("Please enter reason for freeze date");
                    //     return;
                    // }

                    // if (!vmid.formExtra) {
                    //     Toaster.sayWarning("Provide freeze information");
                    //     return;
                    // }
                    if (vmid.form.loadCategoryIdList.length == 0) {
                        Toaster.sayWarning("Please choose Load category ");
                        return;
                    }
                    if (!vmid.formExtra.fDate) {
                        Toaster.sayWarning("Please select from date");
                        return;
                    }
                    if (!vmid.formExtra.fTime) {
                        Toaster.sayWarning("Please select from time");
                        return;
                    }
                    if (!vmid.formExtra.tDate) {
                        Toaster.sayWarning("Please select to date");
                        return;
                    }

                    if (!vmid.formExtra.tTime) {
                        Toaster.sayWarning("Please select to time");
                        return;
                    }


                    /* var frmDt = Date.parse(data.fromDate);
                    var toDt = Date.parse(data.toDate);
                    if (frmDt > toDt) {
                        Toaster.sayWarning("From date should be lesser than To date");
                        return;
                    } */
                    vm.existingFreezeListnew = [];
                    for (var j = 0; j < vm.existingFreezeList.length; j++) {
                        vm.existingFreezeListnew.push(vm.existingFreezeList[j]);
                    }
                    var from = moment(vmid.formExtra.fDate + " ", "MM-DD-YYYY HH:mm")
                    var to = moment(vmid.formExtra.tDate + " ", "MM-DD-YYYY HH:mm")
                    if (to.diff(from, "minutes") === 0) {
                        Toaster.sayWarning("From time and To time should not be same");
                        return;
                    }
                    if (to.diff(from, "minutes") < 0) {
                        Toaster.sayWarning("From date should be lesser than To date");
                        return;
                    }

                    vmid.form.fromDate = vmid.formExtra.fDate + " " + "00:00:00 " + moment(vmid.formExtra.fDate + " ", appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ")
                    vmid.form.toDate = vmid.formExtra.tDate + " " + "00:00:00 " + moment(vmid.formExtra.tDate + " ", appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ")

                    vmid.form.from_date = vmid.form.fromDate
                    vmid.form.to_date = vmid.form.toDate
                    var updateFreezeData = {}
                    angular.copy(vmid.form, updateFreezeData)
                    updateFreezeData = _.omit(updateFreezeData, ['fromDate', 'toDate'])
                    vm.newFreezeList = [];
                    vm.allFreezeList = [];
                    for (var i = 0; i < updateFreezeData.loadCategoryIdList.length; i++) {
                        var falg = true;
                        for (var j = 0; j < vm.existingFreezeListnew.length; j++) {
                            if (vm.existingFreezeListnew[j].loadCategoryId == updateFreezeData.loadCategoryIdList[i]) {
                                falg = false;
                                vm.existingFreezeListnew[j].fromDate = vmid.form.fromDate;
                                vm.existingFreezeListnew[j].toDate = vmid.form.toDate;
                                vm.allFreezeList.push(vm.existingFreezeListnew[j]);
                            }
                        }
                        if (falg) {
                            var freezenew = {
                                id: null,
                                loadCategoryId: updateFreezeData.loadCategoryIdList[i],
                                reason: updateFreezeData.reason,
                                fromDate: vmid.form.fromDate,
                                toDate: vmid.form.toDate,
                                systemId: updateFreezeData.systemId,
                                active: "Y"
                            };
                            vm.newFreezeList.push(freezenew);
                        }
                    }
                    for (var i = 0; i < vm.existingFreezeListnew.length; i++) {
                        if (vm.allFreezeList && vm.allFreezeList.length > 0) {
                            var falg = true;
                            for (var j = 0; j < vm.allFreezeList.length; j++) {
                                if (vm.existingFreezeListnew[i].loadCategoryId == vm.allFreezeList[j].loadCategoryId) {
                                    falg = false;
                                }
                            }
                            if (falg) {
                                vm.existingFreezeListnew[i].active = "N";
                            }
                        } else {
                            vm.existingFreezeListnew[i].active = "N";
                        }
                    }
                    if (vm.newFreezeList != null && vm.newFreezeList.length > 0) {
                        for (var j = 0; j < vm.newFreezeList.length; j++) {
                            vm.existingFreezeListnew.push(vm.newFreezeList[j]);
                        }
                    }
                    var allFinalObject = [];
                    for (var k = 0; k < vm.existingFreezeListnew.length; k++) {

                        var objectData = {
                            id: vm.existingFreezeListnew[k].id,
                            loadCategoryId: {
                                id: vm.existingFreezeListnew[k].loadCategoryId,
                                systemId: {
                                    id: vm.existingFreezeListnew[k].systemId
                                }
                            },
                            reason: vm.existingFreezeListnew[k].reason,
                            fromDate: vm.existingFreezeListnew[k].fromDate,
                            toDate: vm.existingFreezeListnew[k].toDate,
                            active: vm.existingFreezeListnew[k].active
                        }
                        allFinalObject.push(objectData);
                    }
                    APIFactory.updateLoadFreeze(allFinalObject, function (response) {
                        if (response.status) {
                            Toaster.saySuccess("Freeze Date Updated Successfully");
                            $mdDialog.hide();
                            initTableSettings()
                            loadFreezeList(tableAttr)
                        } else {
                            Toaster.sayError(response.errorMessage);
                        }
                    })

                } catch (err) { }


            };

        } catch (err) { }

    }

});