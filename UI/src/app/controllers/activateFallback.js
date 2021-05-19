dashboard.controller("activateFallbackCtrl", function($rootScope, $scope, $state, $location, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger, WSService, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.myTasksList = []
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    vm.currentUser = $rootScope.home_menu
    Paginate.refreshScrolling();
    $scope.loadTSDTaskList = function() {
        IPService.getExpandView($scope, vm)
        APIFactory.getSystemList(function(response) {
            if (response.status) {
                vm.systemList = _.sortBy(response.data, "name")
                $scope.loadPlanListForSystem(vm.systemList[0].id, vm.systemList[0].name)
            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }

    $scope.RFCClick = function (pObj) {
		var getBase1 = IPService.initRFC($scope, vm, pObj)
    }
    
    $scope.loadTSDTaskList()

    var actionList = ["loadandactivate", "activate", "deactivate", "deactivateanddelete"]

    $scope.addActionRow = function(rowObject, systemLoadObj, addDeleteRow) {
        // var rowDuplicate = angular.copy(rowObject[0]);
        /* var noActionPerformedPreviously = true
        var productionLoadList = systemLoadObj.actionObj.productionLoadsList
        for (plIndex in productionLoadList) {
            if (productionLoadList[plIndex]["activate"] && productionLoadList[plIndex].cpuId != null) {
                noActionPerformedPreviously = false
            }
        }
        if (noActionPerformedPreviously) {
            Toaster.sayWarning("No action performed previously")
            return
        } */
        var rowDuplicate = {};
        rowDuplicate.activatedDateTime = null
        rowDuplicate.deActivatedDateTime = null
        rowDuplicate.lastActionStatus = null
        rowDuplicate.cpuId = null
        rowDuplicate.status = null
        rowDuplicate.systemLoadId = {
            "id": systemLoadObj.id
        }
        if (rowObject.length > 0) {
            rowDuplicate.status = "FALLBACK_LOADED"
        }
        if (addDeleteRow) {
            rowDuplicate.status = "FALLBACK_ACTIVATED"
            vm.showAddDeActivateActionButton[systemLoadObj.planId.id] = false
        } else {
            // rowDuplicate.status = "LOADED"
            vm.showAddActivateActionButton[systemLoadObj.planId.id] = false
        }
        rowObject.push(rowDuplicate)

    }

    /* $scope.deleteActionRow = function(rowObject) {
        // var rowDuplicate = angular.copy(rowObject[0]);
        if (rowObject && rowObject.length > 0 && rowObject[rowObject.length - 1].id == null) {
            rowObject.splice(-1)
        }
    } */

    $scope.validateDeleteAction = function(actionObj) {
        var allAreDeactivated = true
        _.each(actionObj.productionLoadsList, function(plObj) {
            if (plObj.status != "FALLBACK_DEACTIVATED") {
                allAreDeactivated = false
            }
        })
        var ifAnyOneActivated = false
        _.each(actionObj.productionLoadsList, function(plObj) {
            if (plObj.status == "FALLBACK_ACTIVATED") {
                ifAnyOneActivated = true
            }
        })
        if (ifAnyOneActivated) {
            return false
        }
        var ifAnyOneDeleted = false
        _.each(actionObj.productionLoadsList, function(plObj) {
            if (plObj.status == "FALLBACK_DELETED") {
                ifAnyOneDeleted = true
            }
        })
        if (ifAnyOneDeleted) {
            return false
        }
        var ifAnyOneNull = false
        _.each(actionObj.productionLoadsList, function(plObj) {
            if (plObj.status == null) {
                ifAnyOneNull = true
            }
        })
        if (ifAnyOneNull) {
            return false
        }
        return allAreDeactivated || actionObj.isAnyLoadsDeleted
    }

    vm.plansOfSystemList = {}
    vm.allActions = {}
    $scope.loadPlanListForSystem = function(id, name) {
        vm.plansOfSystemList[name] = []
        paginateTSDTable(id, name)
    }
    WSService.initProductionLoad(function(response) {
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
        $rootScope.saveformData()
    })



    $scope.applyActions = function(selectedPlanObj) {
        var loadActionObj = null
        var returnOut = false
        var pObj = angular.copy(selectedPlanObj)
            // _.each(planOfSystem, function(pObj) {
        if (!pObj.actionObj.productionLoadsList) {
            return;
        }
        if (pObj.loadCategoryId.name == "C" || pObj.loadCategoryId.name == "F") {
            if (pObj.actionObj.productionLoadsList[0].deactivateanddelete) {
                _.each(pObj.actionObj.productionLoadsList, function(slObj) {
                    slObj.status = "FALLBACK_DELETED"
                })
            }
            var allExists = false
            var action_obj = []
            _.each(pObj.actionObj.productionLoadsList, function(aObj) {
                if (aObj.activate) {
                    if (aObj.cpuId && aObj.cpuId.displayName == "") {
                        allExists = true
                        aObj.cpuId = null
                        action_obj.push(aObj)
                    }
                }
            })
            if (allExists) {
                pObj.actionObj.productionLoadsList = action_obj
            }
        }
        var copy_actionObj = angular.copy(pObj.actionObj.productionLoadsList)

        // Remove empty objects
        for (aoIndex in copy_actionObj) {
            var emptyObj = true;
            for (alIndex in actionList) {
                if (copy_actionObj[aoIndex][actionList[alIndex]]) {
                    emptyObj = false
                }
            }
            if (emptyObj) {
                copy_actionObj[aoIndex] = null
            }
        }
        copy_actionObj = _.filter(copy_actionObj, function(caObj) {
            return caObj != null;
        })
        if (copy_actionObj.length == 0) {
            Toaster.sayWarning("No action performed")
            return
        }
        _.each(copy_actionObj, function(actionObj) {
                if (actionObj.activate && actionObj.deactivateanddelete) {
                    returnOut = true
                    Toaster.sayWarning("Activate/Deactivate/Delete cannot happen at same time")
                    return
                }
                if (actionObj.loadandactivate) {
                    actionObj.status = "FALLBACK_LOADED"
                }
                delete actionObj.loadandactivate
                if (actionObj.activate) {
                    actionObj.status = "FALLBACK_ACTIVATED"
                }
                delete actionObj.activate
                if (actionObj.deactivate) {
                    actionObj.status = "FALLBACK_DEACTIVATED"
                }
                delete actionObj.deactivate
                if (actionObj.deactivateanddelete) {
                    actionObj.status = "FALLBACK_DELETED"
                }
                delete actionObj.deactivateanddelete
                actionObj.planId = pObj.planId
                actionObj.systemId = pObj.systemId
                    // actionObj.systemLoadId = pObj
                if (!actionObj.cpuId) {
                    actionObj.cpuId = null
                }
                if (actionObj && actionObj.cpuId) {
                    delete actionObj.cpuId.disabled
                    if (actionObj.cpuId.displayName == "ALL") {
                        actionObj.cpuId = null
                    }
                }
                if (actionObj.planId) {
                    delete actionObj.planId.approvalsList
                    delete actionObj.planId.extraParams
                    delete actionObj.planId.activityLogList
                    delete actionObj.planId.fileType
                    delete actionObj.planId.implementationList
                }
                loadActionObj = actionObj
            })
            // })
        if (returnOut) {
            return
        }
        APIFactory.tsdPostProdSystemLoad({}, loadActionObj, function(response) {
            if (response.status) {
               $scope.refresh()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
       
    }

    var oneActionSelected = {}

    function loadSystemPlan(tableAttrs) {
        vm.showAddActivateActionButton = {}
        vm.showAddDeActivateActionButton = {}
        oneActionSelected = {}
        APIFactory.getFallBackSystemLoadListBySystemId(tableAttrs, function(response) {
            if (response.status) {
                vm.plansOfSystemList[tableAttrs.name] = response.data
				$scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                var planids = []
                _.filter(vm.plansOfSystemList[tableAttrs.name], function(lObj) {
                    planids.push(lObj.planId.id)
                })
                if (planids.length == 0) {
                    return
                }
                APIFactory.getFallbackProductionLoads({ "ids": planids, "systemName": tableAttrs.name }, function(response) {
                    if (response.status) {
                        var productionLoadIds = _.pluck(response.data, "id")
                        _.each(response.data, function(prodListObj) {
                            _.each(vm.plansOfSystemList[tableAttrs.name], function(slObj) {
                                if (prodListObj.plan.id == slObj.planId.id) {
                                    vm.showAddActivateActionButton[prodListObj.plan.id] = true
                                    vm.showAddDeActivateActionButton[prodListObj.plan.id] = true
                                    slObj.actionObj = prodListObj
                                    slObj.showEditBtn = true;
                                    _.each(slObj.actionObj.productionLoadsList, function(prodLoadsObj) {
                                        if (((prodLoadsObj.status == 'FALLBACK_DEACTIVATED' && prodLoadsObj.cpuId == null) || (prodLoadsObj.status == 'FALLBACK_ACTIVATED' && prodLoadsObj.cpuId == null)) && slObj.actionObj.isMultipleCPUAllowed) {
                                            prodLoadsObj.cpuId = {
                                                "displayName": "ALL"
                                            }
                                        }
                                        if (prodLoadsObj.status != 'FALLBACK_LOADED') {
                                            slObj.showEditBtn = false;
                                        }
                                    })
                                }
                            })
                        })

                        _.each(vm.plansOfSystemList[tableAttrs.name], function(lObj) {
                            if (!lObj.actionObj) {
                                lObj.actionObj = {}
                                lObj.actionObj.productionLoadsList = []
                            }
                            if (lObj.actionObj.productionLoadsList == null || lObj.actionObj.productionLoadsList.length == 0) {
                                lObj.actionObj.productionLoadsList = []
                                $scope.addActionRow(lObj.actionObj.productionLoadsList, lObj)
                            }
                        })
                        $rootScope.saveformData()
                    } else {
                        Toaster.sayError(response.errorMessage)
                    }
                })
            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }

    $scope.refreshProdLoad = function(id) {
        APIFactory.tsdProdLoadRefresh(id, function(response) {
            if (response.status) {
                $scope.refresh()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }



    //Sharing between cpu controller
    var currentSystemLoadObj;

    vm.defaultDisabledChecked = true

    $scope.chooseAction = function(systemLoadObj, systemName, action, ev, actionObj) {
        currentSystemLoadObj = systemLoadObj
        if (actionObj[action]) {
            //Allow only on action at a time
            if (oneActionSelected[systemLoadObj.planId.id]) {
                Toaster.sayWarning("Only one action are allowed at a time per plan")
                actionObj[action] = false
                return
            } else {
                oneActionSelected[systemLoadObj.planId.id] = true
            }
        } else {
            //Allow only on action at a time
            var somethingSelected = false
            for (plObj in systemLoadObj.actionObj.productionLoadsList) {
                for (actionItem in actionList) {
                    if (systemLoadObj.actionObj.productionLoadsList[plObj][actionList[actionItem]]) {
                        somethingSelected = true
                    }
                }
            }
            if (!somethingSelected) {
                oneActionSelected[systemLoadObj.planId.id] = false
            }
            //--
            /* if (actionObj.status != "DEACTIVATED" && actionObj.status != "ACTIVATED") {
                actionObj.cpuId = null
            } */
            return
        }


        // CSS Validation
        validateCheck(systemLoadObj)

        // CPU Selection
        if ((((action == 'activate' && actionObj.activate) || (action == 'deactivate' && actionObj.deactivate)) && actionObj.cpuId == null) && (systemLoadObj.actionObj.activationSystemCpusList.length > 0 || systemLoadObj.actionObj.deActivationSystemCpusList.length > 0)) {
            showCpuDialog(ev, systemLoadObj.systemId.id, actionObj)
        }
    }

    function validateCheck(sLoadObj) {
        if (sLoadObj.load && sLoadObj.activate) {
            sLoadObj.deactivate = false
            sLoadObj.delete = false
            setTimeout(function() {
                $("." + sLoadObj.planId.id + " .action_deactivate").removeClass("md-checked")
                $("." + sLoadObj.planId.id + " .action_delete").removeClass("md-checked")
            }, 1000)
        }
        if (sLoadObj.deactivate && sLoadObj.delete) {
            sLoadObj.load = false
            sLoadObj.activate = false
            setTimeout(function() {
                $("." + sLoadObj.planId.id + " .action_load").removeClass("md-checked")
                $("." + sLoadObj.planId.id + " .action_activate").removeClass("md-checked")
            }, 2000)
        }

    }

    function showCpuDialog($event, id, actionObj) {
        $mdDialog.show({
            parent: angular.element(document.body),
            targetEvent: $event,
            templateUrl: 'html/templates/cpulist.template.html',
            locals: {
                systemId: id,
                action: actionObj
            },
            controller: cpuCtrl,
            clickOutsideToClose: false,
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        });

        function cpuCtrl($scope, $mdDialog, systemId, action) {

            if (action.activate) {
                $scope.cpuList = currentSystemLoadObj.actionObj.activationSystemCpusList;
            } else {
                $scope.cpuList = currentSystemLoadObj.actionObj.deActivationSystemCpusList;
            }

            $scope.showSelectionError = false
            $scope.selectedCPU = "empty"
            $scope.save = function() {
                if ($scope.selectedCPU == "empty") {
                    $scope.showSelectionError = true
                    return
                } else if ($scope.selectedCPU == "") {
                    action.cpuId = {
                        "displayName": "ALL"
                    }
                } else {
                    $scope.showSelectionError = false
                    action.cpuId = _.where($scope.cpuList, { "id": parseInt($scope.selectedCPU) })[0];
                }
                $mdDialog.hide();
            }

            $scope.cancel = function() {
                if (action.activate) {
                    action.activate = false
                } else {
                    action.deactivate = false
                }
                var somethingSelected = false
                for (plObj in currentSystemLoadObj.actionObj.productionLoadsList) {
                    for (actionItem in actionList) {
                        if (currentSystemLoadObj.actionObj.productionLoadsList[plObj][actionList[actionItem]]) {
                            somethingSelected = true
                        }
                    }
                }
                if (!somethingSelected) {
                    oneActionSelected[currentSystemLoadObj.planId.id] = false
                }
                $mdDialog.hide();
            }
        }
    }



    function paginateTSDTable(id, name) {
        /* Pagination Table Starts */
        var columnsToBeSorted = []
        var tableAttr;
        var initTableSettings = function() {
            $scope.tableConfig = Paginate.tableConfig()
            // $scope.tableConfig.pageSize = 20 // Default page Size for TSD
			// $scope.tableDefaultValue = Paginate.defaultPageValue()
            // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for TSD
            // $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
            $scope.pageSizeList = Paginate.pageSizeList()
            tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
            tableAttr.id = id
            tableAttr.name = name
            $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
        }
        initTableSettings()

        $scope.refresh = function() {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadSystemPlan(tableAttr)
        }
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
            loadSystemPlan(tableAttr)
        }

        $scope.pageChangeHandler = function(num) {
            if (vm.plansOfSystemList[tableAttr.name] && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            tableAttr.filter = vm.filterText ? vm.filterText : ""
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadSystemPlan(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function(columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadSystemPlan(tableAttr)
        }

        /* Pagination Table Ends */
    }


})