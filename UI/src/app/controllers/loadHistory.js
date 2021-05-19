dashboard.controller("loadsControlHistoryCtrl", function ($rootScope, $scope, $state, $location, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.loadHistoryList = []
    var apiBase = appSettings.apiBase;
    IPService.getExpandView($scope, vm);
    Paginate.refreshScrolling();
    /* Pagination Table Starts */
    var columnsToBeSorted = ["name", "systemId.name"]
    var tableAttr;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        // $scope.tableConfig.pageSize = 20 // Default page Size for LC
        // $scope.tableDefaultValue = Paginate.defaultPageValue()
        // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for LC
        // $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
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
        loadHistoryList(tableAttr)
    }

    $scope.refresh = function () {
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadHistoryList(tableAttr)
    }

    $scope.pageChangeHandler = function (num) {
        if (vm.loadHistoryList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
        tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
        $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadHistoryList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadHistoryList(tableAttr)
    }

    // ZTPFM-2275 Plan Status Check
    $scope.getDeploymentStatusByPlan = function (planStatus) {
        if (planStatus !== 'DEPLOYED_IN_PRODUCTION' && getDeploumentStatus().indexOf(planStatus) <= getDeploumentStatus().indexOf("READY_FOR_PRODUCTION_DEPLOYMENT")) {
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
                    "deploymentStartAndStopReason": ld.deploymentStatusChangeComment.replace(/["“”<>']+/g, '')
                }
                APIFactory.deploymentStatusChange(paramObj, function (response) {
                    if (response.status) {
                        Toaster.sayStatus("Deployment status changed " + planId)
                        $mdDialog.hide();
                        //  $scope.refresh();
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


    /* Pagination Table Ends */

    $scope.checkForActiveDBCR = function (dbcrList) {
        dbcrList = dbcrList ? dbcrList : []
        var activeExists = false
        _.each(dbcrList, function (dbcrObj) {
            if (dbcrObj.mandatory == "Y") {
                activeExists = true
            }
        })
        return activeExists
    }

    function loadHistoryList(tableAttr) {
        selectedRowId = null;
        APIFactory.getLoadHistory(tableAttr, function (response) {
            if (response.status) {
                if (response.data.length > 0) {
                    vm.loadHistoryList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    APIFactory.getSystemLoadByPlan({ "ids": _.pluck(vm.loadHistoryList, "id") }, function (response) {
                        if (response.status && response.data.length > 0) {
                            var rSystemList = _.map(response.data, function (elem) {
                                return elem.systemLoad;
                            })
                            _.each(vm.loadHistoryList, function (pObj) {
                                pObj.systemLoadList = []
                                _.each(rSystemList, function (rsObj) {
                                    if (rsObj.planId.id == pObj.id) {
                                        pObj.systemLoadList.push(rsObj)
                                    }
                                })
                            })
                            APIFactory.getDbcrList({
                                "planIds": _.pluck(vm.loadHistoryList, "id")
                            }, function (response) {
                                if (response.status) {
                                    if (response.data && response.data.length > 0) {
                                        var dbcrList = response.data
                                        _.each(rSystemList, function (sysObj) {
                                            sysObj.systemId.dbcrList = []
                                            _.each(dbcrList, function (dbcrObj) {
                                                if (dbcrObj.planId.id == sysObj.planId.id && dbcrObj.systemId.id == sysObj.systemId.id) {
                                                    sysObj.systemId.dbcrList.push(dbcrObj)
                                                }
                                            })
                                        })
                                    }
                                    $rootScope.saveformData()
                                }
                            })
                        } else {
                            // Toaster.sayError(response.errorMessage)
                        }
                    })
                } else {
                    vm.loadHistoryList = []
                }

            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }

    // Loads Control Reject
    // $scope.rejectSelectedPlan = function(ev, planId) {
    //     var confirm = $mdDialog.confirm()
    //         .title('Would you like to reject ' + planId + ' ?')
    //         .ariaLabel('Confirm Reject')
    //         .targetEvent(ev)
    //         .ok('Yes')
    //         .cancel('No');
    //     $mdDialog.show(confirm).then(function() {
    //         APIFactory.rejectPlan({ "impPlanId": planId }, function(response) {
    //             if (response.status) {
    //                 $scope.refresh()
    //             } else {
    //                 Toaster.sayError(response.errorMessage);
    //             }
    //         })
    //     }, function() {});
    // }

    //New Reject Starts

    $scope.rejectSelectedPlan = function (ev, planId) {
        $mdDialog.show({
            controller: rejectMessageCtrl,
            controllerAs: "rj",
            templateUrl: 'html/templates/rejectMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "id": planId
            }
        })
            .then(function (answer) {
                $scope.refresh()
            }, function () {

            });

        function rejectMessageCtrl($scope, id) {
            var rj = this;
            rj.impPlanId = id;
            rj.showMessageError = false
            $scope.proceedReject = function () {
                if (!rj.rejectMessage) {
                    rj.showMessageError = true
                    return
                } else {
                    rj.showMessageError = false
                }
                var paramObj = {
                    "impPlanId": id,
                    "rejectReason": rj.rejectMessage.replace(/["“”<>']+/g, '')
                }

                APIFactory.rejectPlan(paramObj, function (response) {
                    if (response.status) {
                        Toaster.saySuccess("Plan Rejected Successfully")
                        $mdDialog.hide();
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }

    }

    //New Reject Ends..

    $scope.checkLoadStatus = function (systemList) {
        if (systemList) {
            var loadStatus = []
            _.each(systemList, function (pObj) {
                loadStatus = pObj;
            })
            if (loadStatus.prodLoadStatus == "ACTIVATED" || loadStatus.prodLoadStatus == "LOADED" || loadStatus.prodLoadStatus == null || loadStatus.prodLoadStatus == "DEACTIVATED" || loadStatus.prodLoadStatus == "ACTIVATED_ON_ALL_CPU") {
                return true;
            } else {
                return false;
            }
        }
    }

    $scope.getRejectStatus = function (pObj) {
        var disabledReject = false;
        var statusList = _.filter(pObj.systemLoadList, function (elem) {
            return elem.prodLoadStatus;
        })
        if (statusList.length > 0) {
            disabledReject = true;
        }
        return disabledReject
    }

})