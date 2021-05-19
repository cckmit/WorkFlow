dashboard.controller("myTasks", function ($rootScope, $scope, $state, $location, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger, WSService, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.myTasksList = []
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    vm.currentUser = $rootScope.home_menu
    vm.tsdSelectAll = {}
    vm.loadsetDeactivateChangeComment = ""
    vm.pendingFallBackStatusChangeComment = ""

    Paginate.refreshScrolling();
    var keys = "enable_planReport"

    $scope.RFCClick = function (pObj) {
        var getBase1 = IPService.initRFC($scope, vm, pObj)
    }
    /* Developer Area */
    $scope.loadDevTaskList = function () {
        var getBase = IService.initImpl($scope, vm, "mytasks")
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)
    }

    /* Loads Control Area */
    $scope.loadLCTaskList = function () {
        $scope.role = "LoadsControl"
        var getBase = IPService.initPlan($scope, vm)
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)
    }

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

    /* vm.ipProcessed = false;
    var ipProcessedObj = []
    $scope.getIP = function(readyProDep) {
        var planToSend = []
        ipProcessedObj = []
        for (key in readyProDep) {
            if (readyProDep[key]) {
                planToSend.push(key)
            }
        }
        if (!planToSend || planToSend.length == 0) {
            Toaster.sayWarning("Choose some plans")
            return
        }
        var receivedPlanDataLength = 0;
        var actualLength = 0


    } */

    $scope.validateIPStatus = function (ipAddr) {
        if (!ipAddr) {
            return false;
        }
        if (ipAddr.indexOf(".") >= 0) {
            return true
        }
        return false;
    }


    // ZTPFM-1984
    $scope.enableCheckBox = function (loadList) {
        var flag = includes(loadList.toLowerCase(), 'zoldr load action is not allowed')
        if (flag) {
            return true;
        } else {
            return false;
        }
    }

    function includes(container, value) {
        var returnValue = false;
        var pos = container.indexOf(value);
        if (pos >= 0) {
            returnValue = true;
        }
        return returnValue;
    }
    $scope.confirmReadyForProDep = function (ev, planId) {

        var planToSend = []
        planToSend.push(planId)
        /* for (key in readyProDep) {
            if (readyProDep[key]) {

            }
        }
        if (!planToSend || planToSend.length == 0) {
            Toaster.sayWarning("Choose some plans")
            return
        } */
        var confirm = $mdDialog.confirm()
            .title('Would you like to proceed ?')
            .textContent('Selected Plan : ' + planId)
            .ariaLabel('Confirm delete zTPF Level deployment details')
            .targetEvent(ev)
            .ok('Yes')
            .cancel('No');
        $mdDialog.show(confirm).then(function () {
            var ipList = {}
            $(".loadScreen").toggle().fadeIn('slow')
            var ftpStatusInitialized = false
            WSService.initProdFTPIP(function (response) {
                if (response.status) {
                    var r_data = response.data
                    _.each(vm.myTasksList, function (planObj) {
                        if (planToSend.indexOf(planObj.id) >= 0) {
                            for (index in planObj.systemLoadList) {
                                if (planObj.systemLoadList[index].id == r_data.id) {
                                    planObj.systemLoadList[index].ip_addr = r_data.ipAddress
                                }
                            }
                        }
                    })
                    $scope.$apply()
                    $rootScope.saveformData()
                } else {
                    $rootScope.saveformData()
                    _.each(vm.myTasksList, function (planObj) {
                        if (planToSend.indexOf(planObj.id) >= 0) {
                            for (index in planObj.systemLoadList) {
                                planObj.systemLoadList[index].ip_addr = false
                            }
                        }
                    })
                    if (response.errorMessage) {
                        Toaster.sayStatus(response.errorMessage)
                        $rootScope.saveformData()
                    }
                }
            })
            var dataObj = planToSend
            /* var dataObj = {
                "plans": _.uniq(_.pluck(ipProcessedObj, "id")),
                "ips": ipList
            } */
            APIFactory.readyForProdDeploy(dataObj, function (response) {
                $(".loadScreen").toggle().fadeOut('slow')
                if (response.status) {
                    $scope.loadLCTaskList()
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        }, function () { });
    }

    // Loads Control Reject
    // $scope.rejectPlanLC = function(ev, planId) {
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

    //New Reject Starts..
    $scope.rejectPlanLC = function (ev, planId) {
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

    /* TSD My Tasks Starts here */
    $scope.loadTSDTaskList = function () {
        IPService.getExpandView($scope, vm)
        APIFactory.getSystemList(function (response) {
            if (response.status) {
                vm.systemList = _.sortBy(response.data, "name")
                $scope.loadPlanListForSystem(vm.systemList[0].id, vm.systemList[0].name)
            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }


    //Deactivate Loadset Commnets logic started

    $scope.loadSetDeactivateChange = function (action, actionObj, systemLoadObj, e) {
        if (actionObj[action] && action === actionList[2]) {
            $mdDialog.show({
                controller: loadsetDeactivateChangeCommentCtrl,
                controllerAs: "ld",
                templateUrl: 'html/templates/loadSetDeactivateChangeComment.template.html',
                parent: angular.element(document.body),
                targetEvent: e,
                clickOutsideToClose: false,
                locals: {
                    // "id": planId
                }
            })
                .then(function (answer) {
                    //  $scope.refresh()

                }, function () {

                });

            function loadsetDeactivateChangeCommentCtrl($scope) {
                var ld = this;
                ld.showMessageError = false
                $scope.proceedSaveComment = function () {
                    if (!ld.loadsetDeactivateChangeComment) {
                        ld.showMessageError = true
                        return
                    } else {
                        ld.showMessageError = false
                    }
                    vm.loadsetDeactivateChangeComment = ld.loadsetDeactivateChangeComment.replace(/["“”<>']+/g, '')
                    $mdDialog.hide();
                }
                $scope.cancel = function () {
                    actionObj[action] = false
                    oneActionSelected[systemLoadObj.planId.id] = false
                    $mdDialog.cancel();
                };
            }
        }
    }


    //ZTPFM-2286 Deployment Hold Text Box added

    vm.deploymentHoldPending = function (e, planId) {

        $mdDialog.show({
            controller: deploymentHoldForPendingLoad,
            controllerAs: "ld",
            templateUrl: 'html/templates/pendingStatusUpdate.template.html',
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

        function deploymentHoldForPendingLoad($scope) {
            var ld = this;
            ld.showMessageError = false
            $scope.proceedSaveComment = function () {
                if (!ld.pendingFallBackStatusChangeComment) {
                    ld.showMessageError = true
                    return
                } else {
                    ld.showMessageError = false
                }
                vm.pendingFallBackStatusChangeComment = ld.pendingFallBackStatusChangeComment;


                var paramObj = {
                    "planId": planId.id,
                    "comments": ld.pendingFallBackStatusChangeComment.replace(/["“”<>']+/g, '')
                }
                APIFactory.setPlanasPendingFallback(paramObj, function (response) {
                    if (response.status) {
                        Toaster.sayStatus("Plan status updated pending fallback  " + planId.id)
                        $mdDialog.hide();
                        // $scope.refresh();
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

    var actionList = ["loadandactivate", "activate", "deactivate", "deactivateanddelete"]
    var multiselectAction = "";

    // ZTPFM-1547 - Multiselect Impl Plans - Logic - Starts

    vm.tsdSelectAllAndSubmit = function (action, prodList) {
        localStorage.removeItem(keys);
        if (_.keys(action).length <= 0 || _.values(action).indexOf(true) < 0) {
            Toaster.sayWarning("Choose Something")
            return
        }


        _.each(prodList, function (slObj) {
            _.each(slObj.actionObj.productionLoadsList, function (actionObj) {
                if (actionObj.loadandactivate) {
                    actionObj.status = "LOADED"
                }
                // delete actionObj.loadandactivate
                if (actionObj.activate) {
                    actionObj.status = "ACTIVATED"
                }
                // delete actionObj.activate
                if (actionObj.deactivate) {
                    actionObj.status = "DEACTIVATED"
                }
                // delete actionObj.deactivate
                if (actionObj.deactivateanddelete) {
                    actionObj.status = "DELETED"
                }
                // delete actionObj.deactivateanddelete
                // delete actionObj.disabled
                // delete actionObj.enableVparForSubmittedWSP
            })
            // delete pObj.ltcustLoaded
        })
        var postAction;
        _.each(vm.tsdSelectAll, function (value, key) {
            if (value == true) { postAction = key }
        })
        var postDataObj = {
            action: "",
            prodLoads: []
        }
        _.each(prodList, function (slObj) {
            _.each(slObj.actionObj.productionLoadsList, function (actionObj) {
                if (actionObj.id && actionObj.id != null && actionObj.id != "") { // If Plan has already been loaded
                    postDataObj.prodLoads.push(actionObj)
                }
                else {
                    actionObj["planId"] = {} // If plan is loaded for the 1st time
                    actionObj["planId"] = angular.copy(slObj.actionObj.plan)
                    actionObj["systemId"] = angular.copy(slObj.systemId)
                    postDataObj.prodLoads.push(actionObj)
                }
            })
        })
        if (postAction == "load") {
            postDataObj.action = "LOADED"
            postDataObj.prodLoads = _.where(postDataObj.prodLoads, { "status": "LOADED", "loadandactivate": true })
            multiselectAction = "LOADED"

        } else if (postAction == "activate") {
            postDataObj.action = "ACTIVATED"
            postDataObj.prodLoads = _.where(postDataObj.prodLoads, { "status": "ACTIVATED", "activate": true })
            multiselectAction = "ACTIVATED"
        } else if (postAction == "deactivate") {
            postDataObj.action = "DEACTIVATED"
            postDataObj.prodLoads = _.where(postDataObj.prodLoads, { "status": "DEACTIVATED", "deactivate": true })
        } else if (postAction == "delete") {
            postDataObj.action = "DELETED"
            postDataObj.prodLoads = _.where(postDataObj.prodLoads, { "status": "DELETED", "deactivateanddelete": true })
        }

        localStorage.setItem(keys, JSON.stringify(multiselectAction));

        _.each(postDataObj.prodLoads, function (pObj) {
            delete pObj.loadandactivate
            delete pObj.activate
            delete pObj.deactivate
            delete pObj.deactivateanddelete
        })
        postDataObj.prodLoads = _.filter(postDataObj.prodLoads, function (preObj) {
            return preObj.lastActionStatus != "INPROGRESS"
        });
        if (postDataObj.prodLoads.length <= 0) {
            Toaster.sayWarning("No Plans selected to " + postAction)
            return;
        }
        if (postAction == "activate") {
            _.each(postDataObj.prodLoads, function (aObj) {
                if (aObj.cpuId && aObj.cpuId.displayName == "ALL") {
                    aObj.cpuId = null
                }
            })
        }

        APIFactory.tsdLoadAndActivateInTOS(postDataObj, function (response) {
            if (response.status) {
                $scope.refresh()
            } else {
                multiselectAction = " ";
                $scope.refresh()
                Toaster.sayError(response.errorMessage)
            }
        })


        // TSD SelectAll API call to be hit here..
    }


    var lPlanList = [];
    var systemName = '';
    var actionStatus = '';
    vm.updateCheckbox = function (actionTriggered, prodList) {
        var selectAllActions = ["load", "activate", "deactivate", "delete"]
        _.each(selectAllActions, function (action) {
            if (action != actionTriggered) {
                vm.tsdSelectAll[action] = false

            }
        })
        _.each(prodList, function (slObj) {
            // getting  selected PlanList and SystemName
            if (vm.tsdSelectAll[actionTriggered]) {
                lPlanList.push(slObj.planId.id);
                systemName = slObj.systemId.name;
                actionStatus = actionTriggered;
            } else {
                lPlanList = [];
            }

        })

        //ZTPFM-2500 Multiselect TSD allowed Plans
        if (vm.tsdSelectAll[actionTriggered]) {
            if (actionStatus == 'load') {
                actionStatus = 'LOADED';
            } else {
                actionStatus = 'ACTIVATED';
            }
            var paramObj = {
                "ids": lPlanList,
                "systemName": systemName,
                "action": actionStatus

            }
            APIFactory.getTSDAllowedPlans(paramObj, function (response) {
                if (response.status) {
                    vm.allowedPlanObj = response.data;
                    updateAllPlans(actionTriggered, prodList, vm.allowedPlanObj)
                }
            })

        } else {
            updateAllPlans(actionTriggered, prodList, " ")
        }
    }

    function updateAllPlans(actionDone, prodList, alowedPlanObj) {
        _.each(prodList, function (slObj) {
            _.each(slObj.actionObj.productionLoadsList, function (itObj) {
                if ((itObj.status == null) && !slObj.actionObj.isAnyLoadsInProgress) {
                    if (vm.tsdSelectAll[actionDone] && actionDone == 'load') {
                        itObj["loadandactivate"] = true
                    } else {
                        delete itObj["loadandactivate"]
                        slObj.actionObj.showLoadAndActivateButtonEnable = false;
                    }
                } else
                    if ((!slObj.actionObj.isAnyLoadsDeleted) && ((itObj.status == 'LOADED' || itObj.status == 'DEACTIVATED')) && itObj.lastActionStatus != "INPROGRESS" && !slObj.actionObj.isAnyLoadsInProgress) { // || (itObj.cpuId.displayName == 'ALL' && itObj.status == 'ACTIVATED' && !slObj.actionObj.selectActivateAll)
                        if (vm.tsdSelectAll[actionDone] && actionDone == 'activate') {
                            itObj["activate"] = true
                            if (itObj.cpuId && !itObj.cpuId.displayName) {
                                if (itObj.systemLoadId.loadCategoryId.name == "C" || itObj.systemLoadId.loadCategoryId.name == "F") {
                                    itObj.cpuId = { "id": itObj.systemLoadId.systemId.defaultNativeCpu }
                                } else {
                                    itObj.cpuId = { "displayName": "ALL" }
                                }
                            }

                        } else {
                            delete itObj["activate"]
                            itObj.cpuId = null
                            slObj.actionObj.showLoadAndActivateButtonEnable = false;
                        }
                    }
                if (vm.tsdSelectAll[actionDone] && actionDone == 'load') {
                    _.each(alowedPlanObj, function (planObj) {
                        if (slObj.planId.id == planObj) {
                            itObj["loadandactivate"] = false
                            slObj.actionObj.showLoadAndActivateButtonEnable = true;
                        }
                    })
                } else
                    if (vm.tsdSelectAll[actionDone] && actionDone == 'activate') {
                        _.each(alowedPlanObj, function (planObj) {
                            if (slObj.planId.id == planObj) {
                                itObj["activate"] = false
                                slObj.actionObj.showLoadAndActivateButtonEnable = true;
                            }
                        })
                    }
            })
        })

    }


    $scope.addActionRow = function (rowObject, systemLoadObj, addDeleteRow) {
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
            rowDuplicate.status = "LOADED"
        }
        if (addDeleteRow) {
            rowDuplicate.status = "ACTIVATED"
            vm.showAddDeActivateActionButton[systemLoadObj.planId.id] = false
        } else {
            // rowDuplicate.status = "LOADED"
            vm.showAddActivateActionButton[systemLoadObj.planId.id] = false
        }
        rowObject.push(rowDuplicate)

    }

    $scope.removeCPUaction = function (rowObject, systemLoadObj, addDeleteRow) {
        var ev = null;
        currentSystemLoadObj = systemLoadObj
        showCpuDialog(ev, systemLoadObj.systemId.id)
        function showCpuDialog($event, id) {
            $mdDialog.show({
                parent: angular.element(document.body),
                targetEvent: $event,
                templateUrl: 'html/templates/cpulist.template.html',
                locals: {
                    systemId: id,
                    // action: actionObj
                },
                controller: cpuCtrl,
                clickOutsideToClose: false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            });

            function cpuCtrl($scope, $mdDialog, systemId) {
                $scope.cpuList = currentSystemLoadObj.actionObj.deActivationSystemCpusList;

                $scope.showSelectionError = false
                $scope.selectedCPU = "empty"
                $scope.save = function () {
                    if ($scope.selectedCPU == "empty") {
                        $scope.showSelectionError = true
                        return
                    }
                    // } else if ($scope.selectedCPU == "") {
                    //     rowObject.cpuId = {
                    //         "displayName": "ALL"
                    //     }
                    // } else {
                    // $scope.showSelectionError = true
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
                        rowDuplicate.status = "LOADED"
                    }
                    if (addDeleteRow) {
                        rowDuplicate.status = "ACTIVATED"
                        vm.showAddDeActivateActionButton[systemLoadObj.planId.id] = false
                    } else {
                        // rowDuplicate.status = "LOADED"
                        vm.showAddActivateActionButton[systemLoadObj.planId.id] = false
                    }
                    rowDuplicate.cpuId = _.where($scope.cpuList, { "id": parseInt($scope.selectedCPU) })[0];
                    rowObject.push(rowDuplicate)
                    // }
                    $mdDialog.hide();
                }

                $scope.cancel = function () {
                    $mdDialog.hide();
                }
            }
        }
    }

    $scope.refreshTSD = function () {
        $scope.refreshData()
    }

    /* $scope.deleteActionRow = function(rowObject) {
        // var rowDuplicate = angular.copy(rowObject[0]);
        if (rowObject && rowObject.length > 0 && rowObject[rowObject.length - 1].id == null) {
            rowObject.splice(-1)
        }
    } */

    $scope.validateDeleteAction = function (actionObj) {
        var allAreDeactivated = true
        _.each(actionObj.productionLoadsList, function (plObj) {
            if (plObj.status != "DEACTIVATED") {
                allAreDeactivated = false
            }
        })
        var ifAnyOneActivated = false
        _.each(actionObj.productionLoadsList, function (plObj) {
            if (plObj.status == "ACTIVATED") {
                ifAnyOneActivated = true
            }
        })
        if (ifAnyOneActivated) {
            return false
        }
        var ifAnyOneDeleted = false
        _.each(actionObj.productionLoadsList, function (plObj) {
            if (plObj.status == "DELETED") {
                ifAnyOneDeleted = true
            }
        })
        if (ifAnyOneDeleted) {
            return false
        }
        var ifAnyOneNull = false
        _.each(actionObj.productionLoadsList, function (plObj) {
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
    $scope.loadPlanListForSystem = function (id, name) {
        vm.plansOfSystemList[name] = []
        vm.searchPlanData = ""
        paginateTSDTable(id, name)
    }

    WSService.initProductionLoad(function (response) {
        if (response.message) {
            Toaster.sayStatus(response.message)
            $rootScope.saveformData()
        } else {
            if (response.errorMessage.length != 0) {
                Toaster.sayStatus(response.errorMessage)
                $rootScope.saveformData()
            }
        }
        $scope.refresh()
    })

    // ZTPFM-2275 Plan Status Check
    $scope.getDeploymentStatusByPlan = function (planStatus) {
        if ((planStatus !== 'DEPLOYED_IN_PRODUCTION' && planStatus !== 'PENDING_FALLBACK') && getDeploumentStatus().indexOf(planStatus) <= getDeploumentStatus().indexOf("READY_FOR_PRODUCTION_DEPLOYMENT")) {
            return true;
        }
    }
    // ZTPFM-2275 Plan Status Check
    $scope.getProductionLoadStatus = function (loadStatus, deploymentFlag) {

        if ((loadStatus === null || loadStatus === undefined || loadStatus === 'LOADED' || loadStatus === 'ACTIVATED') && deploymentFlag === 'STOP_DEPLOYMENT') {
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
                        // $scope.refresh();
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


    $scope.applyActions = function (selectedPlanObj) {
        var loadActionObj = null
        var returnOut = false
        var pObj = angular.copy(selectedPlanObj)
        var isForceActivate = false
        // _.each(planOfSystem, function(pObj) {
        if (!pObj.actionObj.productionLoadsList) {
            return;
        }



        if (pObj.loadCategoryId.name == "C" || pObj.loadCategoryId.name == "F") {
            if (pObj.actionObj.productionLoadsList[0].deactivateanddelete) {
                _.each(pObj.actionObj.productionLoadsList, function (slObj) {
                    slObj.status = "DELETED"
                })
            }
            var allExists = false
            var action_obj = []
            _.each(pObj.actionObj.productionLoadsList, function (aObj) {
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
        copy_actionObj = _.filter(copy_actionObj, function (caObj) {
            return caObj != null;
        })
        if (copy_actionObj.length == 0) {
            Toaster.sayWarning("No action performed")
            return
        }
        _.each(copy_actionObj, function (actionObj) {
            if (actionObj.activate && actionObj.deactivateanddelete) {
                returnOut = true
                Toaster.sayWarning("Activate/Deactivate/Delete cannot happen at same time")
                return
            }
            if (actionObj.loadandactivate) {
                actionObj.status = "LOADED"
            }
            delete actionObj.loadandactivate
            if (actionObj.activate) {
                actionObj.status = "ACTIVATED"
            }
            delete actionObj.activate
            if (actionObj.deactivate) {
                actionObj.status = "DEACTIVATED"
            }
            delete actionObj.deactivate
            if (actionObj.deactivateanddelete) {
                actionObj.status = "DELETED"
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
            if (actionObj.forceActivate) {
                isForceActivate = true
            }
            delete actionObj.forceActivate
            loadActionObj = actionObj
        })
        // })
        if (returnOut) {
            return
        }
        if (loadActionObj.status !== 'DEACTIVATED') {
            vm.loadsetDeactivateChangeComment = ""
        }
        APIFactory.tsdPostProdSystemLoad({ "forceActivate": isForceActivate, "loadsetDeactivateChangeComment": vm.loadsetDeactivateChangeComment }, loadActionObj, function (response) {
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
        APIFactory.getSystemLoadListBySystemId(tableAttrs, function (response) {
            if (response.status) {
                var rSystemList = response.data
                vm.plansOfSystemList[tableAttrs.name] = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                var planids = []
                _.filter(vm.plansOfSystemList[tableAttrs.name], function (lObj) {
                    planids.push(lObj.planId.id)
                })
                if (planids.length == 0) {
                    return
                }

                APIFactory.getDbcrList({
                    "planIds": planids
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
                    }
                })
                APIFactory.getProductionLoads({ "ids": planids, "systemName": tableAttrs.name }, function (response) {
                    if (response.status) {
                        vm.tsdSelectAll = {}
                        var productionLoadIds = _.pluck(response.data, "id")
                        _.each(response.data, function (prodListObj) {
                            _.each(vm.plansOfSystemList[tableAttrs.name], function (slObj) {
                                if (prodListObj.plan.id == slObj.planId.id) {
                                    vm.showAddActivateActionButton[prodListObj.plan.id] = true
                                    vm.showAddDeActivateActionButton[prodListObj.plan.id] = true
                                    slObj.actionObj = prodListObj
                                    slObj.showEditBtn = true;
                                    _.each(slObj.actionObj.productionLoadsList, function (prodLoadsObj) {
                                        if (((prodLoadsObj.status == 'DEACTIVATED' && prodLoadsObj.cpuId == null) || (prodLoadsObj.status == 'ACTIVATED' && prodLoadsObj.cpuId == null)) && slObj.actionObj.isMultipleCPUAllowed) {
                                            prodLoadsObj.cpuId = {
                                                "displayName": "ALL"
                                            }
                                        }
                                        if (prodLoadsObj.status == 'DELETED') {
                                            slObj.showEditBtn = false;
                                        }
                                        // Logic to revert back the state - when any zOLDR action fails
                                        if (prodLoadsObj.status == "LOADED" && prodLoadsObj.lastActionStatus == "FAILED") {
                                            prodLoadsObj.status = "LOADED"
                                        } else if (prodLoadsObj.status == "ACTIVATED" && prodLoadsObj.lastActionStatus == "FAILED") {
                                            prodLoadsObj.status = "ACTIVATED"
                                        } else if (prodLoadsObj.status == "DEACTIVATED" && prodLoadsObj.lastActionStatus == "FAILED") {
                                            prodLoadsObj.status = "DEACTIVATED"
                                        } else if (prodLoadsObj.status == "DELETED" && prodLoadsObj.lastActionStatus == "FAILED") {
                                            // prodLoadsObj.status="DEACTIVATED"
                                            prodLoadsObj.status = null
                                            slObj.actionObj.isAnyLoadsDeleted = false;
                                        }
                                    })
                                }
                            })
                        })

                        _.each(vm.plansOfSystemList[tableAttrs.name], function (lObj) {
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

    $scope.refreshProdLoad = function (id) {
        APIFactory.tsdProdLoadRefresh(id, function (response) {
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

    $scope.chooseAction = function (systemLoadObj, systemName, action, ev, actionObj) {

        currentSystemLoadObj = systemLoadObj
        if (!oneActionSelected[systemLoadObj.planId.id]) {
            //ZTPFM-2303
            $scope.loadSetDeactivateChange(action, actionObj, systemLoadObj);
        }
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
            if (actionObj.cpuId && (actionObj.cpuId.displayName == 'ALL' || actionObj.cpuId.hasOwnProperty('id')) && action == 'activate' && actionObj.status != 'DEACTIVATED' && actionObj.status != 'ACTIVATED') {
                actionObj.cpuId = null
            }
            return
        }


        // CSS Validation
        validateCheck(systemLoadObj)

        // if(action=='activate' && !actionObj[action] && !actionObj.id){
        //     actionObj.cpuId = null
        // }

        // CPU Selection
        if ((((action == 'activate' && actionObj.activate)) && (actionObj.cpuId == null || !actionObj.id)) && (systemLoadObj.actionObj.activationSystemCpusList.length > 0 || systemLoadObj.actionObj.deActivationSystemCpusList.length > 0)) {
            showCpuDialog(ev, systemLoadObj.systemId.id, actionObj)
        }
    }

    function validateCheck(sLoadObj) {
        if (sLoadObj.load && sLoadObj.activate) {
            sLoadObj.deactivate = false
            sLoadObj.delete = false
            setTimeout(function () {
                $("." + sLoadObj.planId.id + " .action_deactivate").removeClass("md-checked")
                $("." + sLoadObj.planId.id + " .action_delete").removeClass("md-checked")
            }, 1000)
        }
        if (sLoadObj.deactivate && sLoadObj.delete) {
            sLoadObj.load = false
            sLoadObj.activate = false
            setTimeout(function () {
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
            $scope.save = function () {
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

            $scope.cancel = function () {
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
        var initTableSettings = function () {
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

        $scope.refresh = function () {
            initTableSettings()
            if (vm.searchPlanData) {
                tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
            }
            tableAttr.offset = $rootScope.paginateValue
            loadSystemPlan(tableAttr)
        }
        $scope.refreshData = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            vm.searchPlanData = ""
            loadSystemPlan(tableAttr)
        }
        $scope.searchPlanData = function (searchData) {
            initTableSettings()
            tableAttr.filter = searchData ? searchData : ""
            tableAttr.offset = 0;
            loadSystemPlan(tableAttr)
        }
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
            loadSystemPlan(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.plansOfSystemList[tableAttr.name] && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadSystemPlan(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadSystemPlan(tableAttr)
        }

        /* Pagination Table Ends */

    }

    /**
        *  TOS Response Report
        */

    $scope.showTOSResponseLog = function (ev, sysName) {
        // Read item:
        var actionStatus = [];
        actionStatus.push(JSON.parse(localStorage.getItem(keys)));
        if (actionStatus.length > 0) {
            APIFactory.getProdTOSActionReport({
                "action": actionStatus,
                "systemName": sysName
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
                                    "systemName": sysName
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
                    localStorage.removeItem(keys)
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
        $scope.tosDataClear = function (ev, planInfo) {
            var confirm = $mdDialog.confirm()
                .title("Clear previous TPF action history")
                .textContent('This action would clear the previous TPF action history and wouldnt be available for furture reference. Do you want clear previous TPF action history? ')
                .ariaLabel('tosResponse')
                .targetEvent(ev)
                .ok('Proceed')
                .cancel('Cancel');

            $mdDialog.show(confirm).then(function () {
                APIFactory.clearProdTOSLoadActCache({
                    "action": planInfo.action,
                    "systemName": planInfo.systemName
                }, function (response) {
                    if (response.status) {
                        jk.logContent = response.data;
                    } else {
                        Toaster.sayError(response.errorMessage)
                    }
                })
            }, function () {

            });

        };
    }




    /* TSD My tasks Ends here */
    function initSwitch() {
        vm.currentUser = $rootScope.home_menu
        vm.currentRole = getUserData('userRole')
        switch (getUserData('userRole')) {
            case "Developer":
                $scope.loadDevTaskList()
                break;
            case "LoadsControl":
                $scope.loadLCTaskList()
                break;
            case "TechnicalServiceDesk":
                $scope.loadTSDTaskList()
                break;
        }
    }
    initSwitch()


});