dashboard.controller("syncImplementationPlanCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, WSService, $mdDialog) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var scopeDuplicate = $scope
    vm.loader = {};
    $scope.macroInitSort = true;
    $scope.eTypeInitSort = true;
    $scope.auxTypeInitSort = true;
    $scope.keyLength = function (obj) {
        return Object.keys(obj).length;
    }
    Paginate.refreshScrolling();

    // $scope.initOnlineFallback();
    WSService.initProductionLoad(function (response) {
        // if (response.status) {
        //     if (response.data.last) {
        //         Toaster.sayStatus(response.data.command + " (" + response.data.loadset + ") Success")
        //     }
        // } else {
        //     if (response.errorMessage.length != 0) {
        //         Toaster.sayStatus(response.errorMessage)
        //     }
        // }
        if(response.message){
            Toaster.sayStatus(response.message.message)
            $rootScope.saveformData()
        }
        $scope.refresh()
    })
    WSService.initPlanAuxOnline(function (response) {
        if (response.status) {
            $scope.auxOnlineLoader[response.metaData] = false
            $rootScope.saveformData()
        } else {
            if (response.errorMessage.length != 0) {
                if ($rootScope.currentState == "app.loadsetReady" || typeof $rootScope.currentState == "undefined") {
                    Toaster.sayError(response.errorMessage)
                    $rootScope.saveformData()
                } else {
                    Toaster.sayStatus(response.errorMessage)
                    $rootScope.saveformData()
                }
            }
        }
        $scope.refresh()
    })
    WSService.initOnlineBuildProcess(function(response){
        if(response.status){
            Toaster.saySuccess("Plan Marked Online Successfully")
            $rootScope.saveformData()
        }
        else{
            Toaster.sayError(response.errorMessage)
            $rootScope.saveformData()
        }
        $scope.refresh();
    })

    WSService.initPlanAuxFallback(function (response) {
        if (response.status) {
           // $scope.auxFallbackLoader[response.metaData] = false
           $rootScope.saveformData()
        } else {
            if (response.errorMessage.length != 0) {
                if ($rootScope.currentState == "app.loadsetReady" || typeof $rootScope.currentState == "undefined") {
                    Toaster.sayError(response.errorMessage)
                    $rootScope.saveformData()
                } else {
                    Toaster.sayStatus(response.errorMessage)
                    $rootScope.saveformData()
                }
            }
        }
      $scope.refresh()
    })
    WSService.initFallbackBuildProcess(function(response){
        if(response.status){
            Toaster.saySuccess("Plan Fallback Successfully")
            $rootScope.saveformData()
        }
        else{
            Toaster.sayError(response.errorMessage)
            $rootScope.saveformData()
        }
       $scope.refresh();
    })

    vm.loadTypeList = {
        "_ETYPE": "E-Type Load",
        "_AUXTYPE": "Aux-Type Load",
    }
    vm.currentLoadType = "_ETYPE";

    $scope.loadTypePlans = function (type) {
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
        // $scope.tableDefaultValue = Paginate.defaultPageValue()
		// $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for RFC
        $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        if (vm.currentLoadType == "_ETYPE") {
            //Not a standard field, if sorting affects plz check in productionloadsdao.findTobeLoaded
            columnsToBeSorted = ["id",  "systemLoadList.loadDateTime"]
        } else if (vm.currentLoadType == "_AUXTYPE") {
            columnsToBeSorted = ["id", "systemLoadList.loadDateTime"]
        }
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
        loadList(tableAttr)
    }

    $scope.refresh = function () {
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadList(tableAttr)
    }

    function loadList(tableAttr) {
        if (vm.currentLoadType == "_ETYPE") {
            loadsetETypeList(tableAttr)
        } else if (vm.currentLoadType == "_AUXTYPE") {
            loadsetAuxTypeList(tableAttr)
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

    IPService.getExpandView($scope, vm)

    /* Pagination Table Ends */
    vm.loadsetReadyList = {}

    function loadsetETypeList(tableAttr) {
        if ($scope.eTypeInitSort) {
            tableAttr.orderBy = {
                // "systemLoadList.loadDateTime": "asc"
            }
            // $scope.sortColumn["systemLoadList.loadDateTime"]["asc"] = true;
            $scope.eTypeInitSort = false;
        }
        APIFactory.tsdGetSyncPlan(tableAttr, function (response) {
            if (response.status) {
                if (Object.keys(response.data).length > 0) {
                    vm.loadsetReadyList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    _.each(vm.loadsetReadyList, function (pObj) {
                        $scope.getinProgressPlan(pObj);
                    })
                    APIFactory.getSystemLoadByPlan({ "ids": _.pluck(vm.loadsetReadyList, 'id') }, function (l_response) {
                        if (l_response.status) {
                            var rSystemList = l_response.data
                            _.each(vm.loadsetReadyList, function (pObj) {
                                pObj.systemLoadList = []
                                _.each(rSystemList, function (rsObj) {
                                    if (rsObj.systemLoad.planId.id == pObj.id) {
                                        pObj.systemLoadList.push(rsObj.systemLoad)
                                    }
                                })
                            })  
                            $rootScope.saveformData()
                        } else {
                            Toaster.sayError(response.errorMessage)
                        }
                    })
                } else {
                    vm.loadsetReadyList = {}
                }
            } else {
                vm.loadsetReadyList = {}
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    // Online Plan Inprogress Status

    $scope.getinProgressPlan = function (planList) {
        if (planList.inprogressStatus == 'ONLINE') {
            $scope.OnlineActionsDisable[planList.id] = true;

        }
        else {
            $scope.OnlineActionsDisable[planList.id] = false;
        }

    }

    // function loadsetAuxTypeList(tableAttr) {
    //     if ($scope.auxTypeInitSort) {
    //         /* tableAttr.orderBy = {
    //             "systemLoadList.loadDateTime": "asc"
    //         }
    //         $scope.sortColumn["systemLoadList.loadDateTime"]["asc"] = true;
    //         $scope.auxTypeInitSort = false; */
    //     }
    //     vm.loadsetAuxTypeList = []
    //     APIFactory.tsdGetAuxLoads(tableAttr, function (response) {
    //         if (response.status) {
    //             vm.loadsetAuxTypeList = _.uniq(response.data, "id")
    //             if (vm.loadsetAuxTypeList.length > 0) {
    //                 var systemAttr = typeof tableAttr.orderBy == "undefined" ? {} : tableAttr.orderBy
    //                 if (JSON.stringify(systemAttr).indexOf("systemLoadList") >= 0) {
    //                     systemAttr = JSON.parse(JSON.stringify(systemAttr).replace(/systemloadlist./gi, ""))
    //                 } else {
    //                     systemAttr = {
    //                         "loadDateTime": "asc"
    //                     }
    //                 }
    //                 APIFactory.getSystemLoadByPlan({
    //                     "ids": _.pluck(vm.loadsetAuxTypeList, "id"),
    //                     "orderBy": systemAttr
    //                 }, function (response) {
    //                     if (response.status && response.data.length > 0) {
    //                         var rSystemList = response.data
    //                         _.each(vm.loadsetAuxTypeList, function (pObj) {
    //                             pObj.systemLoadList = []
    //                             _.each(rSystemList, function (rsObj) {
    //                                 if (rsObj.planId.id == pObj.id) {
    //                                     pObj.systemLoadList.push(rsObj)
    //                                 }
    //                             })
    //                         })
    //                     }
    //                 })
    //                 APIFactory.tsdGetAuxPlanOpStatus({}, function (response) {
    //                     if (response.status) {
    //                         _.each(response.data, function (value, key) {
    //                             if (value == 'ONLINE') {
    //                                 $scope.auxOnlineLoader[key] = true
    //                             }
    //                             if (value == 'FALLBACK') {
    //                                 $scope.auxFallbackLoader[key] = true
    //                             }
    //                         })
    //                     } else {
    //                         // Toaster.sayError(response.errorMessage)
    //                     }
    //                 })
    //             }
    //         } else {
    //             Toaster.sayError(response.errorMessage)
    //         }
    //     })
    // }
    $scope.OnlineActionsDisable = {}
    $scope.OnlineActions = function (id) {
        $scope.OnlineActionsDisable[id] = true;
        APIFactory.tsdMarkAuxAsOnline({ "id": id }, function (response) {
            if (response.status) {
                Toaster.sayStatus("Online In-Progress")
                // Toaster.saySuccess("Loadset marked as ONLINE Successfully")
            } else {
                $scope.OnlineActionsDisable[id] = false;
                Toaster.sayError(response.errorMessage)
            }
        })
    }


    $scope.FallbackActions = function (ev,id) {
        $mdDialog.show({
            controller: rejectMessageCtrl,
            controllerAs: "fb",
            templateUrl: 'html/templates/fallbackMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "id": id
            }
        })
        .then(function(answer) {
            //$scope.refresh();
        }, function() {

        });
        $scope.auxFallbackLoader = {}
    function rejectMessageCtrl($scope, id) {
        var fb = this;
        fb.impPlanId = id;
        fb.showMessageError = false
        $scope.proceedFallback = function() {
            if (!fb.fallbackMessage) {
                fb.showMessageError = true
                return
            } else {
                fb.showMessageError = false
            }
            var paramObj = {
                "id": id,
                "rejectReason": fb.fallbackMessage
            }
            $mdDialog.hide();
            scopeDuplicate.auxFallbackLoader[id] = true
            APIFactory.tsdMarkAuxAsFallback(paramObj, function(response) {
                                //scopeDuplicate.auxFallbackLoader[id] = false;
                if (response.status) {
                    Toaster.sayStatus("Fallback In-Progress")
                    $mdDialog.cancel();
                    initTableSettings()
                    // $scope.refresh();
                } else {
                    scopeDuplicate.auxFallbackLoader[id] = false;
                    Toaster.sayError(response.errorMessage)
                }
            })
        }
        $scope.cancel = function() {
            $mdDialog.cancel();
        };
    }
    }




    // $scope.acceptTrigger = function (id) {
    //     vm.loadsetReadyList[id].isAcceptInProgress = true;
    //     APIFactory.tsdSetOnline({ "planId": id }, function (response) {
    //         if (response.status) {
    //             Toaster.sayStatus("Set Online In-Progress")
    //             // Toaster.saySuccess("Loadset marked as ONLINE Successfully")
    //         } else {
    //             Toaster.sayError(response.errorMessage)
    //         }
    //         $scope.refresh()
    //     })
    // }


    // $scope.acceptMacroHeader = function (id) {
    //     vm.loader[id] = {};
    //     vm.loader[id].markasOnline = true;
    //     APIFactory.tsdMarkOnline({ "id": id }, function (response) {
    //         vm.loader[id].markasOnline = false;
    //         if (response.status) {
    //             $scope.refresh()
    //             Toaster.saySuccess("Plan marked as Online")
    //         } else {
    //             Toaster.sayError(response.errorMessage)
    //         }
    //     })
    // }

    // $scope.auxOnlineLoader = {}
    // $scope.markAuxAsOnline = function (id) {
    //     $scope.auxOnlineLoader[id] = true
    //     APIFactory.tsdMarkAuxAsOnline({ "id": id }, function (response) {
    //         if (response.status) { } else {
    //             $scope.auxOnlineLoader[id] = false
    //             // Toaster.sayError(response.errorMessage)
    //         }
    //     })
    // }
    $scope.auxFallbackLoader = {}
   
    //New Fallback starts..
    $scope.markAuxAsFallback = function (ev, id) {
        scopeDuplicate = $scope
        $mdDialog.show({
            controller: fallbackMessageCtrl,
            controllerAs: "fb",
            templateUrl: 'html/templates/fallbackMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "id": id
            }
        }).then(function (answer) {

        }, function () {

        });
        function fallbackMessageCtrl($scope, id) {
            var fb = this;
            fb.impPlanId = id;
            fb.showMessageError = false
            $scope.proceedFallback = function () {
                if (!fb.fallbackMessage) {
                    fb.showMessageError = true
                    return
                } else {
                    fb.showMessageError = false
                }
                var paramObj = {
                    "id": id,
                    "rejectReason": fb.fallbackMessage
                }
                // $scope.auxFallbackLoader[id] = true
                scopeDuplicate.auxFallbackLoader[id] = true
                $mdDialog.hide()
                APIFactory.tsdMarkAuxAsFallback(paramObj, function (response) {
                    if (response.status) {
						
                    } else {
                        scopeDuplicate.auxFallbackLoader[id] = false
                        // Toaster.sayError(response.errorMessage)
                    }
                })
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }
    }
    //New fallback ends..
})