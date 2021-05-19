dashboard.controller("acceptFallbackCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, WSService, $mdDialog) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var scopeDuplicate = $scope
    vm.loader = {};
    $scope.macroInitSort = true;
    $scope.eTypeInitSort = true;
    $scope.auxTypeInitSort = true;
    var acceptplanList = [];
    Paginate.refreshScrolling();
	// var acceptedPlanList = [];
    $scope.keyLength = function (obj) {
        return Object.keys(obj).length;
    }

    $scope.RFCClick = function (pObj) {
		var getBase1 = IPService.initRFC($scope, vm, pObj)
	}

    var tableAttr, columnsToBeSorted;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        // $scope.tableDefaultValue = Paginate.defaultPageValue()
		// $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for TSD
		// $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        //Not a standard field, if sorting affects plz check in productionloadsdao.findTobeLoaded
        columnsToBeSorted = ["loads.planId.id", "loads.planId.loadType", "sysload.loadDateTime", "loads.fallbackActivatedDateTime"]
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
        loadsetETypeList(tableAttr)
    }

    $scope.refresh = function () {
        initTableSettings()
         tableAttr.offset = $rootScope.paginateValue
        loadsetETypeList(tableAttr)
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

        loadsetETypeList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
		// Paginate.tableSort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        loadsetETypeList(tableAttr)
    }

    IPService.getExpandView($scope, vm)

    /* Pagination Table Ends */
    vm.loadsetReadyList = {}
	vm.loadSetReadyListUpdate = {}


    function loadsetETypeList(tableAttr) {
        if ($scope.eTypeInitSort) {
            tableAttr.orderBy = {
                "sysload.loadDateTime": "asc"
            }
            $scope.sortColumn["sysload.loadDateTime"]["asc"] = true;
            $scope.eTypeInitSort = false;
        }
        $scope.isAllSelected = false;
        APIFactory.tsdGetFallBackLoadsToAccept(tableAttr, function (response) {
            if (response.status) {
                if (Object.keys(response.data).length > 0) {
                    vm.loadsetReadyList = response.data
                    getEnableForAcceptPlanList(vm.loadsetReadyList);
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
					 // _.each(vm.loadsetReadyList, function (value, key) {
						// acceptedPlanList = [];
						// acceptedPlanList.push(key) 
					// })
					// var emptyList = [];
					// _.each(_.keys(vm.loadsetReadyList), function(data) {
						// var tempId = data.split("-")[1]
						// emptyList.push(tempId);
					// })
                    APIFactory.getSystemLoadByPlan({ "ids": _.keys(vm.loadsetReadyList) }, function (l_response) {
                        if (l_response.status) {
                            _.map(vm.loadsetReadyList, function (sourceObj_in) {
                                sourceObj_in.isChecked = false;
                                _.map(sourceObj_in.productionLoadsList, function (in_obj) {
                                    _.each(l_response.data, function (sLoad_in) {
                                        if (in_obj.planId.id == sLoad_in.systemLoad.planId.id && in_obj.systemId.id == sLoad_in.systemLoad.systemId.id) {
                                            in_obj.systemLoadId = sLoad_in.systemLoad
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

    // TSD Accept MultiSelect Logic start
    $scope.refreshTSDAccept = function () {
        $scope.refresh()
    }

    // LoadCategory Name R Multiselect option hide
    var categoryList = [];
    $scope.getLoadCategoryAcceptR = function (productionAcceptLoadList) {
        var showRCatIt = false
        var uniqCategory;
        _.each(productionAcceptLoadList, function (prodObj) {
            _.each(prodObj.productionLoadsList, function (Obj) {
                categoryList.push(Obj.systemLoadId.loadCategoryId.name);
            })
        })

        uniqCategory = _.uniq(categoryList, function (a) {
            return a;
        })
        var flag = includes(uniqCategory, 'R')
        if (flag) {
            showRCatIt = true;
        }
        else {
            showRCatIt = false;
        }
        return showRCatIt;
    }

    function includes(container, value) {
        var returnValue = false;
        var pos = container.indexOf(value);
        if (pos >= 0) {
            returnValue = true;
        }
        return returnValue;
    }

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


    // get all R category  Accept Plan List

    getEnableForAcceptPlanList = function (productionData) {
        _.each(productionData, function (Obj) {
            _.each(Obj.productionLoadsList, function (prodPlanObj) {
                if (prodPlanObj.planId.isAcceptEnabled && prodPlanObj.systemLoadId.loadCategoryId.name == 'R') {
                    acceptplanList.push(prodPlanObj.planId.id);
                }
            })
        })
    }

    // Get All Accept Plan In-Progress Disable
    $scope.getAcceptAllInProgressDisable = function (produtionValue, planId) {
        var statusList = [];
        var acceptStatusList = [];
        _.each(produtionValue.productionLoadsList, function (Obj) {
            if (planId == Obj.planId.id && Obj.systemLoadId.loadCategoryId.name == 'R') {
                statusList.push(Obj.status)
            }
            if (planId == Obj.planId.id && Obj.systemLoadId.loadCategoryId.name == 'R' && Obj.status == 'FALLBACK_ACCEPTED' && (Obj.lastActionStatus == 'INPROGRESS')) {
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
            return Obj.systemLoadId.loadCategoryId.name == 'R' && systemLoadId == Obj.systemLoadId.id && Obj.status === 'FALLBACK_ACCEPTED' && (Obj.lastActionStatus == 'INPROGRESS')
        })

        return arrayList.length > 0;


    }
    $scope.getInprogressPlanDiable = function (productionLoadList, systemLoadId) {
        var arrayList = [];
        arrayList = _.filter(productionLoadList, function (Obj) {
            return Obj.systemLoadId.loadCategoryId.name == 'R' && systemLoadId == Obj.systemLoadId.id && Obj.status === 'FALLBACK_ACCEPTED' && (Obj.lastActionStatus == 'INPROGRESS' || Obj.lastActionStatus == 'SUCCESS')
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


    var planIds = [];
    var planId;
    $scope.getloadSetReadyList = function (loadSet) {
        if (acceptplanList != null && acceptplanList.length > 0) {
            _.each(acceptplanList, function (id) {
                planIds = _.uniq(acceptplanList, function (a) {
                    return a;
                })
            })
            _.each(planIds, function (id) {
                planId = id;
				vm.loadsetReadyList[id].isAcceptInProgress = true;
            })
				return vm.loadsetReadyList[planId].isAcceptInProgress = true;            
        }
    }

    // Accept  Multiple Fallback

    $scope.makeCheck = function (e, planList, actionObj) {
        if (actionObj.isChecked) {
            actionObj.plan = planList;
            acceptplanList.push(actionObj.plan);
        } else {
            actionObj.plan = planList;
            acceptplanList.splice(acceptplanList.indexOf(planList), 1);

        }

    }

    $scope.updateCheckbox = function (actionTriggered, isAllSelected) {
        //acceptplanList = [];
        if (isAllSelected) {
            _.map(actionTriggered, function (sourceObj_in) {
                _.each(sourceObj_in.productionLoadsList, function (Obj) {
                    var rCat = $scope.getLoadCategoryR(sourceObj_in.productionLoadsList)
                    if (rCat) {
                        acceptplanList.push(Obj.planId.id);
                        sourceObj_in.isChecked = true;
                    } else {

                        sourceObj_in.isChecked = false;
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


    // FallBack Trigger Each system and Load Category R

    $scope.fallBackAcceptTriggerRCategory = function (id, lSystemName) {

        APIFactory.tsdSetFallBackRCategory({
            "planId": id,
            "systemName": lSystemName
        }, function (response) {
            if (response.status) {
                Toaster.sayStatus(id + " Plan Accepted  for the System " + lSystemName)
                fallBackBuildRCategory(id);

            } else {

                Toaster.sayError(response.errorMessage)
            }
            $scope.refresh()
        })
    }

    function fallBackBuildRCategory(id) {
        WSService.initProductionLoad(function (response) {
            if (response.status) {
                Toaster.sayStatus(response.data.message)
                $rootScope.saveformData()
            } else {
                if (response.errorMessage.length != 0) {
                    if ($rootScope.currentState == "app.acceptFallback" || typeof $rootScope.currentState == "undefined") {
                        Toaster.sayStatus("Fallback Failed")
                        $scope.refresh()
                        $rootScope.saveformData()
                    } else {
                        Toaster.sayStatus("Fallback Failed")
                        $scope.refresh()
                        $rootScope.saveformData()
                    }
                }

            }

            $scope.refresh()
        })
        WSService.initFallbackBuildProcess(function (response) {
            if (response.status) {
                Toaster.sayStatus(response.status)
                $scope.refresh();
                $rootScope.saveformData()
            }
            else {
                Toaster.sayStatus(response.errorMessage)
                $rootScope.saveformData()

            }
            $scope.refresh();
        })

    }

    // Plan Inprogress Status based Disable  checkbox
    var inProgressPlanList = [];
    $scope.getInprogressStatus = function (productionAcceptLoadList) {
        var showEnabeAcceptButton = false;
        _.each(productionAcceptLoadList, function (prodObj) {
            _.each(prodObj.productionLoadsList, function (Obj) {
                if (Obj.systemLoadId.loadCategoryId.name == 'R' && Obj.status == 'FALLBACK_ACCEPTED' && Obj.lastActionStatus == 'INPROGRESS') {
                    inProgressPlanList.push(Obj.planId.id)
                }
            })
        })

        if (inProgressPlanList.length > 0) {
            showEnabeAcceptButton = true;
        }
        return showEnabeAcceptButton;
    }


    $scope.acceptTrigger = function (ev, id) {
		// var idSplit = id.split("-")[1]
        $mdDialog.show({
            controller: messageCtrl,
            controllerAs: "me",
            templateUrl: 'html/templates/promptMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "id": id,
            }
        })
            .then(function (answer) {
                $scope.refresh()
            }, function () {

            });

        function messageCtrl($scope, id) {
            var me = this;
            me.impPlanId = id;
            me.showMessageError = false
            me.title = "Accept Reason for " + id
            me.btnProceed = "Accept"
            $scope.proceed = function () {
                if (!me.meReason) {
                    me.showMessageError = true
                    return
                } else {
                    me.showMessageError = false
                }
                var paramObj = {
                    "planId": id,
                    "rejectReason": me.meReason
                }
                vm.loadsetReadyList[id].isAcceptInProgress = true;
                APIFactory.tsdAcceptFallback(paramObj, function (response) {
                    if (response.status) {

                        Toaster.sayStatus("Set Fallback In-Progress")
                        // Toaster.saySuccess("Loadset marked as ONLINE Successfully")
                    } else {
                        Toaster.sayError(response.errorMessage)
                    }

                    $mdDialog.cancel();
                    initTableSettings()


                    // $scope.refresh()
                })

            }
            loadsetETypeList(tableAttr)
            onlineFallback(id);
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }

    }
    function onlineFallback(id) {
        WSService.initProductionLoad(function (response) {
            if (response.status) {
                Toaster.sayStatus(response.data.message)
                $rootScope.saveformData()
            } else {
                if (response.errorMessage.length != 0) {
                    if ($rootScope.currentState == "app.acceptFallback" || typeof $rootScope.currentState == "undefined") {
                        Toaster.sayStatus("Fallback Failed")
                        $rootScope.saveformData()
                    } else {
                        Toaster.sayStatus("Fallback Failed")
                        $rootScope.saveformData()
                    }
                }
            }
            //vm.loadsetReadyList[id].isAcceptInProgress = false;
            //$scope.refresh()
        })
        WSService.initFallbackBuildProcess(function (response) {
            if (response.status) {
                //Toaster.sayStatus("Fallback Success");
                Toaster.sayStatus(response.status)
                $scope.refresh();
                $rootScope.saveformData()
            }
            else {
                Toaster.sayStatus(response.errorMessage)
                vm.loadsetReadyList[id].isAcceptInProgress = false;
                $rootScope.saveformData()
            }

        })

    }
    var tableatrr = {
        offset: 0, limit: 20, filter: ""
    }
    // loadsetETypeList(tableatrr)


    //New fallback ends..
})