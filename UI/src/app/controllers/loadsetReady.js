dashboard.controller("loadsetReadyCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, WSService, $mdDialog) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var scopeDuplicate = $scope
    vm.loader = {};
    var acceptplanList = [];
    var acceptRCatplanList = [];
    IPService.getExpandView($scope, vm)
    Paginate.refreshScrolling();
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
        "_ETYPE": "E-Type Load",
        "_AUXTYPE": "Aux-Type Load",
        "_MACHEADER": "Macro/Header"
    }
    vm.currentLoadType = "_ETYPE";
    $scope.showETYPETab = true;
    $scope.loadTypePlans = function (type) {
        vm.searchPlanData = ""
        $scope.macroInitSort = true;
        $scope.eTypeInitSort = true;
        $scope.auxTypeInitSort = true;
        vm.currentLoadType = type;
        if (type == "_AUXTYPE") {
            $scope.showETYPETab = false;
            $scope.showAUXTYPETab = true;
            // $scope.showMACHEADERTab= false;
        }
        // else if(type == "_MACHEADER"){
        //     $scope.showETYPETab = false;
        //     $scope.showAUXTYPETab = false;
        //     // $scope.showMACHEADERTab= true;
        // }
        else {
            $scope.showETYPETab = true;
            $scope.showAUXTYPETab = false;
            // $scope.showMACHEADERTab= false;
        }
        $scope.refresh()
    }

    /* Pagination Table Starts */


    var tableAttr, columnsToBeSorted;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        // $scope.tableConfig.pageSize = 20 // Default page Size for TSD
        // $scope.tableDefaultValue = Paginate.defaultPageValue()
        // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for TSD
        // $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        if (vm.currentLoadType == "_ETYPE") {
            //Not a standard field, if sorting affects plz check in productionloadsdao.findTobeLoaded
            columnsToBeSorted = ["loads.planId.id", "loads.planId.loadType", "sysload.loadDateTime", "loads.activatedDateTime"]
        } else if (vm.currentLoadType == "_AUXTYPE") {
            columnsToBeSorted = ["id", "loadType", "systemLoadList.loadDateTime"]
        } else {
            columnsToBeSorted = ["id", "loadType", "systemLoadList.loadDateTime"]
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
        } else if (vm.currentLoadType == "_AUXTYPE") {
            loadsetAuxTypeList(tableAttr)
        } else {
            loadMacroHeadersList(tableAttr)
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

    /* Pagination Table Ends */
    vm.loadsetReadyList = {}
    var key = "plans";
    var keys = "enable_id"
    var plan_keys = "enable_planAcceptReport"
    // localStorage.removeItem(key)
    function loadsetETypeList(tableAttr) {
        if ($scope.eTypeInitSort) {
            tableAttr.orderBy = {
                "sysload.loadDateTime": "asc"
            }
            $scope.sortColumn["sysload.loadDateTime"]["asc"] = true;
            $scope.eTypeInitSort = false;
        }
        vm.isAllSelected = false;
        vm.isAllRCatSelected = false;
        APIFactory.tsdGetLoadsToAccept(tableAttr, function (response) {

            if (response.status) {
                if (Object.keys(response.data).length > 0) {
                    vm.loadsetReadyList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    acceptplanList = JSON.parse(localStorage.getItem("plans"));
                    $scope.getEnableForAcceptPlanList(vm.loadsetReadyList)
                    // acceptRCatplanList = JSON.parse(localStorage.getItem("enable_id"))
                    if (acceptplanList || acceptRCatplanList) {
                        // if (acceptplanList.length > 0 || acceptRCatplanList.length > 0) {

                        var planIds = _.uniq(acceptplanList, function (a) {
                            return a;
                        })
                        planIds = _.uniq(acceptRCatplanList, function (a) {
                            return a;
                        })

                        $scope.getinProgressPlan(planIds)
                        // }
                        acceptplanList = [];
                    }
                    // var emptyList = [];
                    // _.each(_.keys(vm.loadsetReadyList), function(data) {
                    // var tempId = data.split("-")[1]
                    // emptyList.push(tempId);
                    // })
                    APIFactory.getSystemLoadByPlan({
                        "ids": _.keys(vm.loadsetReadyList)
                    }, function (l_response) {
                        if (l_response.status) {
                            _.map(vm.loadsetReadyList, function (sourceObj_in) {
                                sourceObj_in.isChecked = false;
                                sourceObj_in.isCheckedRCat = false;
                                vm.isValidCheck = false;
                                _.each(sourceObj_in.productionLoadsList, function (pObj) {
                                    if (pObj.status === 'ACCEPTED' && pObj.planId.inprogressStatus === 'ACCEPT') {
                                        sourceObj_in.isChecked = true;
                                        vm.isValidCheck = true;
                                        sourceObj_in.isAcceptInProgress = true;
                                    }
                                })
                                var acceptAllRPlanList = $scope.getAllEnableForAcceptPlanList(vm.loadsetReadyList);
                                if (acceptAllRPlanList != undefined && acceptAllRPlanList.length == planIds.length) {
                                    vm.isAllRCatSelected = true;
                                    sourceObj_in.isCheckedRCat = true;
                                }
                                //sourceObj_in.isSelected = false;
                                _.map(sourceObj_in.productionLoadsList, function (in_obj) {
                                    _.each(l_response.data, function (sLoad_in) {
                                        if (in_obj.planId.id == sLoad_in.systemLoad.planId.id && in_obj.systemId.id == sLoad_in.systemLoad.systemId.id) {
                                            in_obj.systemLoadId = sLoad_in.systemLoad
                                        }
                                    })
                                })
                                _.map(sourceObj_in.productionLoadsList, function (in_obj) {
                                    _.each(l_response.data, function (sLoad_in) {
                                        if (IPService.getExpandView($scope, vm)) {
                                            $scope.planStatusModal(sLoad_in.systemLoad.planId)
                                        }
                                    })
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


    // TRACKER STARTS

    $scope.planStatusModal = function (planId) {
        var current = false;
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
                $rootScope.saveformData()
            }
        })
    }

    // TRACKER ENDS

    function loadsetAuxTypeList(tableAttr) {
        if ($scope.auxTypeInitSort) {
            /* tableAttr.orderBy = {
                "systemLoadList.loadDateTime": "asc"
            }
            $scope.sortColumn["systemLoadList.loadDateTime"]["asc"] = true;
            $scope.auxTypeInitSort = false; */
        }
        vm.loadsetReadyList = []
        APIFactory.tsdGetAuxLoads(tableAttr, function (response) {
            if (response.status) {
                vm.loadsetReadyList = _.uniq(response.data, "id")
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                if (vm.loadsetReadyList.length > 0) {
                    var systemAttr = typeof tableAttr.orderBy == "undefined" ? {} : tableAttr.orderBy
                    if (JSON.stringify(systemAttr).indexOf("systemLoadList") >= 0) {
                        systemAttr = JSON.parse(JSON.stringify(systemAttr).replace(/systemloadlist./gi, ""))
                    } else {
                        systemAttr = {
                            "loadDateTime": "asc"
                        }
                    }
                    APIFactory.getSystemLoadByPlan({
                        "ids": _.pluck(vm.loadsetReadyList, "id"),
                        "orderBy": systemAttr
                    }, function (response) {
                        if (response.status && response.data.length > 0) {
                            var rSystemList = response.data
                            _.each(vm.loadsetReadyList, function (pObj) {
                                pObj.systemLoadList = []
                                _.each(rSystemList, function (rsObj) {
                                    if (rsObj.systemLoad.planId.id == pObj.id) {
                                        pObj.systemLoadList.push(rsObj.systemLoad)
                                    }
                                })
                            })
                            $rootScope.saveformData()
                        }
                    })
                    APIFactory.tsdGetAuxPlanOpStatus({}, function (response) {
                        if (response.status) {
                            _.each(response.data, function (value, key) {
                                if (value == 'ONLINE') {
                                    $scope.auxOnlineLoader[key] = true
                                }
                                if (value == 'FALLBACK') {
                                    $scope.auxFallbackLoader[key] = true
                                }
                            })
                            $rootScope.saveformData()
                        } else {
                            // Toaster.sayError(response.errorMessage)
                        }
                    })
                }
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    function loadMacroHeadersList(tableAttr) {
        if ($scope.macroInitSort) {
            tableAttr.orderBy = {
                "systemLoadList.loadDateTime": "asc"
            }
            $scope.sortColumn["systemLoadList.loadDateTime"]["asc"] = true;
            $scope.macroInitSort = false;
        }
        vm.loadsetReadyList = []
        APIFactory.tsdMacroHeaderList(tableAttr, function (response) {
            if (response.status) {
                vm.loadsetReadyList = _.uniq(response.data, "id")
                if (vm.loadsetReadyList.length > 0) {
                    var systemAttr = typeof tableAttr.orderBy == "undefined" ? {} : tableAttr.orderBy
                    if (JSON.stringify(systemAttr).indexOf("systemLoadList") >= 0) {
                        systemAttr = JSON.parse(JSON.stringify(systemAttr).replace(/systemloadlist./gi, ""))
                    } else {
                        systemAttr = {
                            "loadDateTime": "asc"
                        }
                    }
                    APIFactory.getSystemLoadByPlan({
                        "ids": _.pluck(vm.loadsetReadyList, "id"),
                        "orderBy": systemAttr
                    }, function (response) {
                        if (response.status && response.data.length > 0) {
                            var rSystemList = response.data
                            _.each(vm.loadsetReadyList, function (pObj) {
                                pObj.systemLoadList = []
                                _.each(rSystemList, function (rsObj) {
                                    if (rsObj.systemLoad.planId.id == pObj.id) {
                                        pObj.systemLoadList.push(rsObj.systemLoad)
                                    }
                                })
                            })
                            $rootScope.saveformData()
                        }
                    })
                }
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    $scope.isFTP = function (prodSysObj) { // To show FTP only if any system has 'R' category
        var showFTP = false;
        _.each(prodSysObj, function (psObj) {
            if (psObj.systemLoadId.loadCategoryId.name == 'R') {
                showFTP = true;
            }
        })
        return showFTP
    }

    $scope.ftpTrigger = function (id) {
        // var idSplit = id.split("-")[1];
        vm.loadsetReadyList[id].isFTPInProgress = true
        APIFactory.tsdDoFTP({
            "planId": id
        }, function (response) {
            if (response.status) {
                Toaster.saySuccess("FTP Success")
            } else {
                Toaster.sayError(response.errorMessage)
            }
            vm.loadsetReadyList[id].isFTPInProgress = false
            $scope.refresh()
        })
    }

    // Accept Trigger Each system and Load Category R

    $scope.acceptTriggerRCategory = function (id, lSystemName) {
        // var idSplit = id.split("-")[1];
        vm.isAllRCatSelected = true;
        APIFactory.tsdSetOnlineRCategory({
            "planId": id,
            "systemName": lSystemName
        }, function (response) {
            if (response.status) {
                Toaster.sayStatus(id + " Plan Accepted  for the System " + lSystemName)
                onlineBuildRCategory(id);

            } else {

                Toaster.sayError(response.errorMessage)
            }
            $scope.refresh()
        })
    }

    function onlineBuildRCategory(id) {
        WSService.initProductionLoad(function (response) {
            if (response.status) {
                Toaster.sayStatus(response.status.message)
                $rootScope.saveformData()
            } else {
                Toaster.sayStatus(response.errorMessage)
                $rootScope.saveformData()

            }

            $scope.refresh()
        })
        WSService.initOnlineBuildProcess(function (response) {
            if (response.status) {
                localStorage.removeItem(keys)
                Toaster.sayStatus(response.status)
                $rootScope.saveformData()
                $scope.refresh()

            } else {
                Toaster.sayStatus(response.errorMessage)
            }
            $scope.refresh()

        })
    }


    $scope.acceptTrigger = function (id) {
        vm.loadsetReadyList[id].isAcceptInProgress = true;
        APIFactory.tsdSetOnline({
            "planId": id
        }, function (response) {
            if (response.status) {
                Toaster.sayStatus("Set Online In-Progress")
                onlineBuild(id);
                // Toaster.saySuccess("Loadset marked as ONLINE Successfully")
            } else {
                vm.loadsetReadyList[id].isAcceptInProgress = false;
                Toaster.sayError(response.errorMessage)
            }
            // $scope.refresh()
        })
    }

    function onlineBuild(id) {
        WSService.initProductionLoad(function (response) {
            if (response.status) {
                $scope.refresh()
                $rootScope.saveformData()
                vm.isValidCheck = false;
                Toaster.sayStatus(response.status.message)
            } else {
                $rootScope.saveformData()
                $scope.refresh()
                Toaster.sayStatus("Online Failed:" + "" + response.data.loadset)
            }
            vm.loadsetReadyList[id].isAcceptInProgress = false;

        })
        WSService.initOnlineBuildProcess(function (response) {
            if (response.status) {
                vm.loadsetReadyList[id].isAcceptInProgress = false;
                Toaster.sayStatus(response.status)
                $rootScope.saveformData()
                localStorage.removeItem(key);
            } else {
                $rootScope.saveformData()
                Toaster.sayStatus(response.errorMessage)
            }
            $scope.refresh()

        })
    }


    $scope.acceptMacroHeader = function (id) {
        vm.loader[id] = {};
        vm.loader[id].markasOnline = true;
        APIFactory.tsdMarkOnline({
            "id": id
        }, function (response) {
            vm.loader[id].markasOnline = false;
            if (response.status) {
                $scope.refresh()
                Toaster.saySuccess("Plan marked as Online")
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    $scope.auxOnlineLoader = {}
    $scope.markAuxAsOnline = function (id) {
        $scope.auxOnlineLoader[id] = true
        APIFactory.tsdMarkAuxAsOnline({
            "id": id
        }, function (response) {
            if (response.status) {
                Toaster.sayStatus("Set Online In-Progress");
            } else {
                $scope.auxOnlineLoader[id] = false
                Toaster.sayError(response.errorMessage)
            }
        })
        onlineAuxBuild(id);
    }
    function onlineAuxBuild(id) {
        WSService.initPlanAuxOnline(function (response) {
            if (response.status) {
                Toaster.sayStatus(response.status.message)
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
            $scope.auxOnlineLoader[id] = false
            $scope.refresh()
        })
        WSService.initOnlineBuildProcess(function (response) {
            if (response.status) {
                $scope.auxOnlineLoader[id] = false
                Toaster.sayStatus(response.status)
                $rootScope.saveformData()
            }
            $scope.refresh()

        })
    }


    $scope.makeCheck = function (e, planList, actionObj) {
        // var idSplit = planList.split("-")[1]
        localStorage.removeItem(key);
        if (actionObj.isChecked) {
            acceptplanList = acceptplanList || [];
            acceptplanList.push(planList);
        } else {
            actionObj.plan = planList;
            acceptplanList.splice(acceptplanList.indexOf(planList), 1);

        }

    }

    $scope.updateCheckbox = function (actionTriggered, isAllSelected) {
        if (isAllSelected) {
            _.map(actionTriggered, function (sourceObj_in) {
                _.each(sourceObj_in.productionLoadsList, function (Obj) {
                    acceptplanList = acceptplanList || [];
                    var rCat = $scope.getLoadCategoryR(sourceObj_in.productionLoadsList)
                    if (rCat) {
                        sourceObj_in.isChecked = false;
                    } else {
                        acceptplanList.push(Obj.planId.id);
                        sourceObj_in.isChecked = true;
                    }

                })
            })
        } else {
            _.map(vm.loadsetReadyList, function (sourceObj_in) {
                _.each(sourceObj_in.productionLoadsList, function (Obj) {
                    acceptplanList = [];
                    sourceObj_in.isChecked = false;
                })
            })
        }

    }
    $scope.isChecked = false;
    var multiselectAction = "";
    $scope.submitPlan = function () {
        localStorage.removeItem(key);
        localStorage.removeItem(plan_keys);
        var planIds;
        var isAcceptChecked;
        isAcceptChecked = _.filter(vm.loadsetReadyList, function (pObj) {
            return pObj.isChecked

        })
        if (isAcceptChecked.length > 0) {
            _.each(acceptplanList, function (id) {
                planIds = _.uniq(acceptplanList, function (a) {
                    return a;
                })

                localStorage.setItem(key, JSON.stringify(planIds));

                // Read item:
                var item = [];
                item.push(JSON.parse(localStorage.getItem(key)));

            })

            var paramObj = {
                "planIds": planIds
            }

            $scope.getinProgressPlan(paramObj.planIds)
            APIFactory.tsdAcceptMultiPlans(paramObj, function (response) {
                multiselectAction = 'ACCEPTED'
                if (response.status) {
                    localStorage.setItem(plan_keys, JSON.stringify(multiselectAction));
                    _.each(paramObj.planIds, function (id) {
                        vm.loadsetReadyList[id].isAcceptInProgress = true;
                        vm.isValidCheck = true;
                        onlineBuild(id);
                        // $scope.refresh();
                    })
                } else {
                    vm.isValidCheck = false;
                    Toaster.sayError(response.errorMessage + "Failed")
                    $scope.refresh();
                }
            })
        }
        else {
            vm.isValidCheck = true;
            Toaster.sayError("No Action Performed")
        }

    }

    $scope.getinProgressPlan = function (planList) {


        _.each(planList, function (planId) {

            if (vm.loadsetReadyList[planId].productionLoadsList[0].planId.inprogressStatus == 'ACCEPT') {
                vm.loadsetReadyList[planId].isAcceptInProgress = true;
                onlineBuild(planId);
            } else if (vm.loadsetReadyList[planId].productionLoadsList[0].planId.isAcceptEnabled) {
                vm.loadsetReadyList[planId].isAcceptInProgress = true;
            }
            else {
                vm.loadsetReadyList[planId].isAcceptInProgress = false;
            }
        })


    }

    // TSD Accept MultiSelect Logic End

    //  ZTPFM-TSD Enabled for ACCEPT by target system MultiSelect R Category Logic Started


    // Accept Button enabled for Each System based on Enable for Accept Flag
    // Function Started

    $scope.isCheckedRCat = false;
    var planIds = [];
    $scope.applyEnabledAccept = function () {
        localStorage.removeItem(keys)
        planIds = _.uniq(acceptRCatplanList, function (a) {
            return a;
        })
        localStorage.setItem(keys, JSON.stringify(planIds));

        // Read item:
        var item = [];
        item.push(JSON.parse(localStorage.getItem(keys)));

        if (Object.keys(planIds).length == 0) {
            Toaster.sayWarning("No plans are selected for Enable for Accept operation , please select plans and try again");
            return;
        }

        APIFactory.tsdApplyEnabledAccept({
            "planIds": planIds
        }, function (response) {
            if (response.status) {
                _.each(planIds, function (planId) {

                    vm.loadsetReadyList[planId].isAcceptInProgress = true;
                    // Toaster.sayStatus("Apply button enabled")
                })
            } else {
                Toaster.sayError(response.errorMessage)
            }
            $scope.refresh()
        })
    }

    // Function Ended.

    // Get Accept Plan List from DB
    // Function Started
    var acceptPlanIds = [];
    var planId;
    $scope.getloadSetReadyList = function (loadSet) {
        if (acceptRCatplanList != null && acceptRCatplanList.length > 0) {
            _.each(acceptRCatplanList, function (id) {
                acceptPlanIds = _.uniq(acceptRCatplanList, function (a) {
                    return a;
                })
            })

            _.each(acceptPlanIds, function (id) {
                planId = id;
                vm.loadsetReadyList[id].isAcceptInProgress = true;
            })
            return vm.loadsetReadyList[planId].isAcceptInProgress = true;
        }
    }
    // Function Ended

    // Each Plan Enable for Accept Checkbox Plan store

    $scope.makeCheckRcategory = function (e, planList, actionObj) {
        // var idSplit = planList.split("-")[1]
        if (actionObj.isCheckedRCat) {
            actionObj.plan = planList;
            acceptRCatplanList = acceptRCatplanList || [];
            acceptRCatplanList.push(actionObj.plan);


        } else {
            actionObj.plan = planList;
            acceptRCatplanList.splice(acceptRCatplanList.indexOf(planList), 1);


        }

    }
    // Multiple Plan Enable for Accpet
    $scope.updateCheckboxForRCat = function (actionTriggered, isAllRCatSelected) {
        // acceptRCatplanList = [];
        if (isAllRCatSelected) {
            _.map(actionTriggered, function (sourceObj_in) {
                _.each(sourceObj_in.productionLoadsList, function (Obj) {
                    acceptRCatplanList = acceptRCatplanList || [];
                    var rCat = $scope.getLoadCategoryR(sourceObj_in.productionLoadsList)
                    if (rCat) {
                        acceptRCatplanList.push(Obj.planId.id);
                        sourceObj_in.isCheckedRCat = true;

                    } else {
                        sourceObj_in.isCheckedRCat = false;
                    }


                })
            })
        } else {
            _.map(vm.loadsetReadyList, function (sourceObj_in) {
                _.each(sourceObj_in.productionLoadsList, function (Obj) {

                    acceptRCatplanList = [];
                    sourceObj_in.isCheckedRCat = false;

                })
            })
        }

    }

    // TSD Accept MultiSelect Logic start
    $scope.refreshTSDAccept = function () {
        $scope.refreshData()
    }

    // LoadCategory Name R Multiselect option hide

    var categoryList = [];
    $scope.showRCategoryFlag = false;
    $scope.getLoadCategoryAcceptR = function (productionAcceptLoadList) {
        var showRCatIt = false
        var uniqCategory;
        _.each(productionAcceptLoadList, function (prodObj) {
            _.each(prodObj.productionLoadsList, function (Obj) {
                if (Obj.systemLoadId.loadCategoryId.name == 'R') {

                    categoryList.push(Obj.systemLoadId.loadCategoryId.name);

                }
            })
        })

        uniqCategory = _.uniq(categoryList, function (a) {
            return a;
        })

        _.each(uniqCategory, function (prodObj) {
            if (prodObj == 'R') {
                showRCatIt = true;
                $scope.showRCategoryFlag = true;
            }
        })
        return showRCatIt;

    }

    var categoryNotList = [];
    $scope.showNotRCategoryFlag = false;
    $scope.getLoadCategoryAcceptNotR = function (productionNotRAcceptLoadList) {
        var showRCatIt = false
        var uniqNotRCategory;
        _.each(productionNotRAcceptLoadList, function (prodObj) {
            _.each(prodObj.productionLoadsList, function (Obj) {
                categoryNotList.push(Obj.systemLoadId.loadCategoryId.name);
            })
        })

        uniqNotRCategory = _.uniq(categoryNotList, function (a) {
            return a;
        })

        _.each(uniqNotRCategory, function (prodObj) {
            if (prodObj != 'R') {
                showRCatIt = true;
                $scope.showNotRCategoryFlag = true;
            }
        })
        return showRCatIt;

    }


    // get all R category  Accept Plan List

    $scope.getEnableForAcceptPlanList = function (productionData) {
        _.each(productionData, function (Obj) {
            _.each(Obj.productionLoadsList, function (prodPlanObj) {
                if (prodPlanObj.planId.isAcceptEnabled && prodPlanObj.systemLoadId.loadCategoryId.name == 'R') {
                    acceptRCatplanList.push(prodPlanObj.planId.id);
                }
            })
        })

    }

    var acceptRCatAllowedPlanList = [];
    $scope.getAllEnableForAcceptPlanList = function (productionData) {
        var acceptAllRPlanList = [];
        _.each(productionData, function (Obj) {
            _.each(Obj.productionLoadsList, function (prodPlanObj) {
                if (prodPlanObj.systemLoadId.loadCategoryId.name == 'R') {
                    acceptAllRPlanList.push(prodPlanObj.planId.id);
                }
            })
        })
        acceptRCatAllowedPlanList = _.uniq(acceptAllRPlanList, function (a) {
            return a;
        })

        return acceptRCatAllowedPlanList;
    }


    // Get All Accept Plan In-Progress Disable
    $scope.getAcceptAllInProgressDisable = function (produtionValue, id) {
        var planId = id.split("-")[1];
        var statusList = [];
        var acceptStatusList = [];
        _.each(produtionValue.productionLoadsList, function (Obj) {
            if (planId == Obj.planId.id && Obj.systemLoadId.loadCategoryId.name == 'R') {
                statusList.push(Obj.status)
            }
            if (planId == Obj.planId.id && Obj.systemLoadId.loadCategoryId.name == 'R' && Obj.status == 'ACCEPTED' && (Obj.lastActionStatus == 'INPROGRESS')) {
                acceptStatusList.push(Obj.status)
            }
        })
        if (statusList.length == acceptStatusList.length) {
            return true;
        } else {
            return false;
        }
    }

    // get Inprogress System Details for Disable:

    $scope.getInprogressSystemDiable = function (productionLoadList, systemLoadId) {
        var arrayList = [];
        arrayList = _.filter(productionLoadList, function (Obj) {
            return Obj.systemLoadId.loadCategoryId.name == 'R' && systemLoadId == Obj.systemLoadId.id && Obj.status === 'ACCEPTED' && (Obj.lastActionStatus == 'INPROGRESS')
        })

        return arrayList.length > 0;


    }

    $scope.getValue = function (progressStatus, productionLoadList, id) {
        var planId = id.split("-")[1];
        var arrayList = [];
        arrayList = _.filter(productionLoadList, function (Obj) {
            return Obj.planId.id == planId && Obj.status === 'ACCEPTED' && Obj.planId.inprogressStatus === 'ACCEPT'
            $scope.refresh();
        })
        return arrayList.length > 0;
    }

    $scope.getInprogressPlanDiable = function (productionLoadList, systemLoadId) {
        var arrayList = [];
        arrayList = _.filter(productionLoadList, function (Obj) {
            return Obj.systemLoadId.loadCategoryId.name == 'R' && systemLoadId == Obj.systemLoadId.id && Obj.status === 'ACCEPTED' && (Obj.lastActionStatus == 'INPROGRESS' || Obj.lastActionStatus == 'SUCCESS')
        })

        return arrayList.length > 0;


    }

    $scope.getDefaultSystemEnable = function (productionLoadList) {
        var arrayList = [];
        arrayList = _.filter(productionLoadList, function (Obj) {
            return Obj.systemLoadId.loadCategoryId.name == 'R' && !Obj.planId.isAcceptEnabled;
        })

        return arrayList.length > 0;


    }


    // System Level Disable once status changed
    var allowedSystems = [];
    $scope.systemLevelDisable = function (productionLoads, systemName) {
        var systemLevelDisable = true

        allowedSystems = _.uniq(productionLoads.allowedSystems, function (a) {
            return a;
        })

        _.each(productionLoads.productionLoadsList, function (Obj) {

            _.each(allowedSystems, function (system) {
                if (system === Obj.systemLoadId.systemId.name && Obj.systemLoadId.loadCategoryId.name == 'R') {
                    systemLevelDisable = false;
                }
            })
        })
        return systemLevelDisable;

    }

    /**
       *  TOS Response Report
       */

    $scope.showTOSResponseLog = function (ev) {
        // Read item:
        var actionStatus = [];
        actionStatus.push(JSON.parse(localStorage.getItem(plan_keys)));
        if (actionStatus.length > 0) {
            APIFactory.getProdTOSActionReport({
                "action": actionStatus,
                "systemName": " "
            }, function (response) {
                if (response.status) {
                    if (response.data != null && response.data.length > 0) {
                        $mdDialog.show({
                            controller: tosResponseLogCtrl,
                            controllerAs: "jk",
                            templateUrl: 'html/templates/tosResponselog.template.html',
                            parent: angular.element(document.body),
                            targetEvent: ev,
                            clickOutsideToClose: false,
                            locals: {
                                "planInfo": {
                                    "logContent": response.data,
                                    "action": actionStatus,
                                    "systemName": " "
                                }
                            }
                        })
                            .then(function (answer) {

                            }, function () {

                            });
                    }
                    else {
                        Toaster.sayStatus("No TOS Response data avaliable");

                    }
                } else {
                    Toaster.sayError(response.errorMessage)
                    localStorage.removeItem(plan_keys)
                }
            })
        } else {
            Toaster.sayStatus("No Action happened !");

        }
    };


    function tosResponseLogCtrl($scope, $mdDialog, planInfo) {
        var jk = this;
        jk.planInfo = planInfo
        jk.logContent = jk.planInfo.logContent
        $scope.cancel = function () {
            $mdDialog.cancel();
        };
    }


    // Based on Load Cat R Display Checkobox Ebable for Accpet
    $scope.getLoadCategoryR = function (productionLoadList) {
        var showIt = false
        _.each(productionLoadList, function (Obj) {
            if (Obj.systemLoadId.loadCategoryId.name == 'R') {
                showIt = true
            } else {
                showIt = false
            }
        })
        return showIt;

    }


    // logcic End for R category

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
                        Toaster.sayStatus("Set Fallback In-Progress")
                    } else {
                        scopeDuplicate.auxFallbackLoader[id] = false
                        // Toaster.sayError(response.errorMessage)
                    }
                })
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
            onlineAuxFallbackBuild(id);
        }
    }
    //New fallback ends..
    function onlineAuxFallbackBuild(id) {
        WSService.initPlanAuxFallback(function (response) {
            if (response.status) {
                Toaster.sayStatus(response.message.message)
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
            // scopeDuplicate.auxFallbackLoader[id] = false;
            $scope.refresh()
        })
        WSService.initFallbackBuildProcess(function (response) {
            scopeDuplicate.auxFallbackLoader[id] = false;
            if (response.status) {
                Toaster.sayStatus("Fallback Success");
                $rootScope.saveformData()
                scopeDuplicate.refresh();
                Toaster.sayStatus(response.status.message)
            } else {
                Toaster.sayError(response.errorMessage)
                $rootScope.saveformData()
            }
        })

    }

})     //New fallback ends..
