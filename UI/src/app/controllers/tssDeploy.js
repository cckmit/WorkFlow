dashboard.controller("tssDeploymentCtrl", function ($rootScope, $scope, $state, $location, $timeout, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger, WSService, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.deploymentList = []
    $scope.userRole = getUserData('userRole')
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    vm.tssSelectAll = {}
    vm.tssSelectAllYoda = {}
    Paginate.refreshScrolling();
    $scope.tssScreen = true;
    $scope.isYoda = false
    $scope.isTos = false

    $scope.role = "SystemSupport"

    $scope.doYoda = function () { // Yoda for delta

        var getBase = IPService.initPlan($scope, vm)
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)

        $scope.RFCClick = function (pObj) {
            var getBase1 = IPService.initRFC($scope, vm, pObj)
        }

        $timeout(function () {
            $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
        }, 1000)



        $scope.addRow = function (plan) {
            if (!plan.actionList) {
                plan.actionList = []
            }
            plan.actionList.push({})
        }
        $scope.deleteRow = function (plan) {
            plan.actionList.splice(-1)
        }

        $scope.clearTestSystem = function (actionObj) {
            if (actionObj.vparId.inputTestSystemText) {
                actionObj.vparId.name = '';
                actionObj.vparId.inputTestSystemText = false;
            }
        }

        $scope.testSystemTextField = function (planAction) {
            if (planAction.id === "Other") {
                planAction.inputTestSystemText = true;
            }
        }
        $scope.clearTestSystemTextField = function (planAction) {
            planAction.id = '';
            planAction.name = '';
            planAction.inputTestSystemText = false;
        }

        $scope.getLoadDT = function (plan, systemId) {
            //{{sObj.loadDateTime | formattedDateTimeWithoutSeconds}}
            var dt;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    dt = plan.systemLoadList[sIndex].loadDateTime
                }
            }
            return dt ? dt : "-"
        }

        $scope.getLoadSetName = function (plan, systemId) {
            var st;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    st = plan.systemLoadList[sIndex].loadSetName
                }
            }
            return st ? st : "-"
        }

        $scope.applyPass_Fail = function (planId, systemId, vparId, type) {
            var paramObj = {
                "planId": planId,
                "systemId": systemId,
                "vparId": vparId,
                "status": type,
                "vparsId": vm.uniqVPARList
            }
            APIFactory.planTestStatus(paramObj, function (response) {
                if (response.status) {
                    Toaster.saySuccess(type + " action done for " + planId)
                    $scope.refresh()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }
        $scope.getVparType = function (vparList, selectedId) {
            for (index in vparList) {
                if (vparList[index].id == selectedId) {
                    return vparList[index].type;
                }
            }
            return "-"
        }

        //ZTPFM-1547 YODA Select All Action (starts)
        vm.updateCheckbox = function (actionTriggered) {
            var selectAllActions = ["load", "activate"] //"deactivate", "delete"
            _.each(selectAllActions, function (action) {
                if (action != actionTriggered) {
                    vm.tssSelectAllYoda[action] = false
                }
            })
            deleteLoadActionSelections()
            updateAllPlans(actionTriggered)
        }

        function updateAllPlans(actionDone) {
            _.each(vm.deploymentList, function (planObject) {
                //Newly Added row
                if (planObject.system_id && planObject.system_id.length > 0 && planObject.vpar_id && planObject.vpar_id.length > 0) {
                    if (actionDone == 'load') {
                        planObject["loadandactivate"] = vm.tssSelectAllYoda[actionDone]
                    } else {
                        delete planObject["loadandactivate"]
                    }
                }
                //Green Bar
                if (planObject.extraParams.qaTemplate && planObject.extraParams.qaTemplate.length > 0) {
                    _.each(planObject.extraParams.qaTemplate, function (systemSelectAllObject) {
                        if (systemSelectAllObject.prevDeploymentActions && systemSelectAllObject.prevDeploymentActions.length > 0) {
                            if (actionDone == 'load') {
                                if (vm.showCheckbox(systemSelectAllObject, 'loadandactivate')) {
                                    systemSelectAllObject["loadandactivate"] = vm.tssSelectAllYoda[actionDone]
                                }
                                vm.selectAll(systemSelectAllObject, 'loadandactivate')
                            }
                            if (actionDone == 'activate') {
                                if (vm.showCheckbox(systemSelectAllObject, 'activate')) {
                                    systemSelectAllObject["activate"] = vm.tssSelectAllYoda[actionDone]
                                }
                                vm.selectAll(systemSelectAllObject, 'activate')
                            }
                        }
                    })
                }
            })
        }

        function deleteLoadActionSelections() {
            var loadActions = ["loadandactivate", "activate", "deactivate", "deactivateanddelete"]
            _.each(loadActions, function (act) {
                _.each(vm.deploymentList, function (pl) {
                    delete pl[act] // New Row
                    _.each(pl.extraParams.qaTemplate, function (sObj) {
                        delete sObj[act] // Green Bar
                        if (sObj.prevDeploymentActions && sObj.prevDeploymentActions.length > 0) {
                            _.each(sObj.prevDeploymentActions, function (prevDepObj) {
                                delete prevDepObj[act] // Blue Bar
                            })
                        }
                    })
                })
            })

        }

        //ZTPFM-1547 YODA Select All Action (ends)

        $scope.submitChanges = function () {
            var deploymentListObject = angular.copy(vm.deploymentList)
            var loadActionObj = []
            for (dIndex in deploymentListObject) {
                var rootList = deploymentListObject[dIndex].extraParams.qaTemplate
                if (deploymentListObject[dIndex].system_id && deploymentListObject[dIndex].vpar_id && deploymentListObject[dIndex].loadandactivate) {
                    for (rlIndex in rootList) {
                        if (rootList[rlIndex].systemId == deploymentListObject[dIndex].system_id) {
                            rootList[rlIndex].prevDeploymentActions.push({
                                systemId: {
                                    id: deploymentListObject[dIndex].system_id
                                },
                                vparId: {
                                    id: deploymentListObject[dIndex].vpar_id
                                },
                                loadandactivate: true
                            })
                        } else {
                            rootList[rlIndex].prevDeploymentActions = []
                        }
                    }

                }
                _.each(rootList, function (rootObj) {
                    if (rootObj.prevDeploymentActions.length > 0) {
                        _.each(rootObj.prevDeploymentActions, function (prevDeploymentObj) {
                            var actionObj = prevDeploymentObj
                            actionObj.planId = deploymentListObject[dIndex].systemLoadList[0].planId
                            // slTemplate.planId = deploymentListObject[dIndex].systemLoadList[0].planId
                            // slTemplate.systemId = deploymentListObject[dIndex].systemLoadList[0].planId
                            var dslUpdateFlag;
                            for (slIndex in deploymentListObject[dIndex].systemLoadList) {
                                if (actionObj.systemId.id == deploymentListObject[dIndex].systemLoadList[slIndex].systemId.id) {
                                    actionObj.systemId = deploymentListObject[dIndex].systemLoadList[slIndex].systemId
                                    actionObj.systemLoadId = deploymentListObject[dIndex].systemLoadList[slIndex]
                                    if (actionObj.dslUpdate == true) {
                                        // actionObj.systemLoadId.dslUpdate = "Y"
                                        dslUpdateFlag = "Y"
                                    } else {
                                        // actionObj.systemLoadId.dslUpdate = "N"
                                        dslUpdateFlag = "N"
                                    }
                                    delete actionObj.dslUpdate
                                }
                            }
                            actionObj.dslUpdate = dslUpdateFlag;

                            for (slIndex in deploymentListObject[dIndex].systemLoadList) {
                                if (actionObj.systemId && actionObj.systemId.id == deploymentListObject[dIndex].systemLoadList[slIndex].systemId.id) {
                                    actionObj.systemId = deploymentListObject[dIndex].systemLoadList[slIndex].systemId
                                }
                            }
                            if (actionObj.activate && actionObj.deactivateanddelete) {
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
                            delete actionObj.disabled

                            var vparCopy = []
                            vparCopy = actionObj.vparId.id
                            actionObj.vparId = ""
                            if (_.isArray(vparCopy)) {
                                for (eachVpar in vparCopy) {
                                    var tempActionObj = {}
                                    angular.copy(actionObj, tempActionObj)
                                    tempActionObj.vparId = (_.where(deploymentListObject[dIndex].vparList, {
                                        "id": parseInt(vparCopy[eachVpar])
                                    })[0])
                                    loadActionObj.push(tempActionObj)
                                }
                            } else {
                                actionObj.vparId = (_.where(deploymentListObject[dIndex].vparList, {
                                    "id": parseInt(vparCopy)
                                })[0])
                                loadActionObj.push(actionObj)
                            }
                        })
                    }

                })
            }
            APIFactory.postTestSystemLoad({}, loadActionObj, function (response) {
                if (response.status) {
                    $scope.refresh($scope.recentPage)
                    $rootScope.saveformData()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
            $rootScope.saveformData()
        }

        $scope.deleteTestSystemLoad = function (index, plan, id) {
            if (!id) {
                plan.actionList.splice(index, 1)
                return
            }
            APIFactory.deleteTestSystemLoad({ "id": id }, function (response) {
                if (response.status) {
                    $scope.refresh()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

    }

    $scope.doTos = function () { // TOS for travelport
        var getBase = IPService.initPlan($scope, vm)
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)


        vm.enableAddBtn = {}

        $timeout(function () {
            $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
        }, 1000)

        $scope.addRow = function (plan) {
            if (!plan.actionList) {
                plan.actionList = []
            }
            vm.enableAddBtn[plan.id] = false
            plan.actionList.push({})
        }
        $scope.deleteRow = function (plan) {
            plan.actionList.splice(-1)
        }

        $scope.getLoadDT = function (plan, systemId) {
            //{{sObj.loadDateTime | formattedDateTimeWithoutSeconds}}
            var dt;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    dt = plan.systemLoadList[sIndex].loadDateTime
                }
            }
            return dt ? dt : "-"
        }

        $scope.getLoadSetName = function (plan, systemId) {
            var st;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    st = plan.systemLoadList[sIndex].loadSetName
                }
            }
            return st ? st : "-"
        }


        $scope.getVparType = function (vparList, selectedId) {
            for (index in vparList) {
                if (vparList[index].id == selectedId) {
                    return vparList[index].cpuType;
                }
            }
            return "-"
        }

        WSService.initPreProductionLoad(function (response) {
            if (response.status) {
                if (response.data.last) {
                    Toaster.sayStatus(response.data.command + " (" + response.data.loadset + ") Success")
                }
                $rootScope.saveformData()
            } else {
                if (response.errorMessage.length != 0) {
                    Toaster.sayStatus(response.errorMessage)
                    $rootScope.saveformData()
                }
            }
            $scope.refresh($scope.recentPage)
        })

        // ZTPFM-1547 - Multiselect Impl Plans - Logic - Starts
        vm.tssSelectAllAndSubmit = function (action) {
            if (_.keys(action).length <= 0 || _.values(action).indexOf(true) < 0) {
                Toaster.sayWarning("Choose Something")
                return
            }

            _.each(vm.deploymentList, function (pObj) {
                _.each(pObj.actionList, function (actionObj) {
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
                    delete actionObj.disabled
                    delete actionObj.enableVparForSubmittedWSP
                })
                delete pObj.ltcustLoaded
            })
            var postAction;
            _.each(vm.tssSelectAll, function (value, key) {
                if (value == true) { postAction = key }
            })
            var postDataObj = {
                action: "",
                preProdLoads: []
            }
            _.each(vm.deploymentList, function (pObj) {
                _.each(pObj.actionList, function (actionObj) {
                    if (actionObj.id && actionObj.id != null && actionObj.id != "") {
                        postDataObj.preProdLoads.push(actionObj)
                    } else {
                        actionObj["planId"] = {}
                        actionObj["planId"]["id"] = pObj.id // .systemId, {"id" : parseInt(actionObj.systemId.id)}
                        actionObj["dslUpdate"] = "N"
                        _.each(pObj.systemLoadList, function (sysObj) {
                            if (parseInt(actionObj.systemId.id) == sysObj.systemId.id) {
                                actionObj["systemLoadId"] = angular.copy(sysObj)
                                actionObj["systemId"] = angular.copy(sysObj.systemId)
                            }
                        })
                        _.each(pObj.vparList, function (vObj) {
                            if (parseInt(actionObj.cpuId.id) == vObj.id) {
                                actionObj["cpuId"] = angular.copy(vObj)
                            }
                        })
                        if (actionObj.planId.id == pObj.id) {
                            actionObj["planId"] = angular.copy(pObj)
                        }
                        delete actionObj.planId.actionList
                        delete actionObj.planId.systemLoadList
                        delete actionObj.planId.vparList
                        _.each(pObj.actionList, function (aObj) {
                            if (aObj.id) {
                                if (aObj.cpuId.id == actionObj.cpuId.id) {
                                    Toaster.sayWarning("Duplicate Loadset found for plan " + actionObj.planId.id)
                                    return
                                }
                            }
                        })
                        postDataObj.preProdLoads.push(actionObj)
                    }
                })
            })
            if (postAction == "load") {
                postDataObj.action = "LOADED"
                postDataObj.preProdLoads = _.where(postDataObj.preProdLoads, { "status": "LOADED", "loadandactivate": true })
            } else if (postAction == "activate") {
                postDataObj.action = "ACTIVATED"
                postDataObj.preProdLoads = _.where(postDataObj.preProdLoads, { "status": "ACTIVATED", "activate": true })
            } else if (postAction == "deactivate") {
                postDataObj.action = "DEACTIVATED"
                postDataObj.preProdLoads = _.where(postDataObj.preProdLoads, { "status": "DEACTIVATED", "deactivate": true })
            } else if (postAction == "delete") {
                postDataObj.action = "DELETED"
                postDataObj.preProdLoads = _.where(postDataObj.preProdLoads, { "status": "DELETED", "deactivateanddelete": true })
            }
            _.each(postDataObj.preProdLoads, function (pObj) {
                delete pObj.loadandactivate
                delete pObj.activate
                delete pObj.deactivate
                delete pObj.deactivateanddelete
            })
            postDataObj.preProdLoads = _.filter(postDataObj.preProdLoads, function (preObj) { return preObj.lastActionStatus != "INPROGRESS" });
            if (postDataObj.preProdLoads.length <= 0) {
                Toaster.sayWarning("No Plans selected to " + postAction)
                return;
            }
            APIFactory.tssLoadAndActivateInTOS(postDataObj, function (response) {
                if (response.status) {
                    $scope.refresh($scope.recentPage)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })


            // TSS SelectAll API call to be hit here..
        }

        vm.updateCheckbox = function (actionTriggered) {
            var selectAllActions = ["load", "activate", "deactivate", "delete"]
            _.each(selectAllActions, function (action) {
                if (action != actionTriggered) {
                    vm.tssSelectAll[action] = false

                }
            })
            updateAllPlans(actionTriggered)
        }

        function updateAllPlans(actionDone) {

            _.each(vm.deploymentList, function (pObj) {
                _.each(pObj.actionList, function (aLObj) {
                    if ((aLObj.status == null || aLObj.status == 'DELETED' || aLObj.status == 'FALLBACK_ACTIVATED') && !pObj.isAnyLoadsInProgress) {
                        if (vm.tssSelectAll[actionDone] && actionDone == 'load' && aLObj.cpuId && aLObj.systemId && aLObj.cpuId.id && aLObj.systemId.id && aLObj.cpuId.id != '' && aLObj.systemId.id != '') {
                            aLObj["loadandactivate"] = true
                        } else {
                            delete aLObj["loadandactivate"]
                        }
                    }
                    if ((aLObj.status == 'LOADED' || aLObj.status == 'DEACTIVATED') && !pObj.isAnyLoadsInProgress) {
                        if (vm.tssSelectAll[actionDone] && actionDone == 'activate' && aLObj.cpuId && aLObj.systemId && aLObj.cpuId.id && aLObj.systemId.id && aLObj.cpuId.id != '' && aLObj.systemId.id != '') {
                            aLObj["activate"] = true
                        } else {
                            delete aLObj["activate"]
                        }
                    }
                    if (aLObj.status == 'ACTIVATED' && !pObj.isAnyLoadsInProgress) {
                        if (vm.tssSelectAll[actionDone] && actionDone == 'deactivate' && aLObj.cpuId && aLObj.systemId && aLObj.cpuId.id && aLObj.systemId.id && aLObj.cpuId.id != '' && aLObj.systemId.id != '') {
                            aLObj["deactivate"] = true
                        } else {
                            delete aLObj["deactivate"]
                        }
                    }
                    if (aLObj.status == 'DEACTIVATED' && !pObj.isAnyLoadsInProgress) {
                        if (vm.tssSelectAll[actionDone] && actionDone == 'delete' && aLObj.cpuId && aLObj.systemId && aLObj.cpuId.id && aLObj.systemId.id && aLObj.cpuId.id != '' && aLObj.systemId.id != '') {
                            aLObj["deactivateanddelete"] = true
                        } else {
                            delete aLObj["deactivateanddelete"]
                        }
                    }

                })
            })

        }

        vm.updateVPAR = function (aLObj) {
            if (aLObj.systemId && aLObj.systemId.id == "" && aLObj.cpuId) {
                aLObj.cpuId.id = ""
            }
        }
        // ZTPFM-1547 - Multiselect Impl Plans - Logic - Ends

        $scope.submitChanges = function (deploymentListObject) {
            var deploymentListObject = angular.copy(deploymentListObject)
            var loadActionObj = {};
            if (deploymentListObject.actionList.length == 0) {
                Toaster.sayWarning("Add some system for " + deploymentListObject.id)
                return
            }
            // for (dIndex in deploymentListObject) {
            if (deploymentListObject.actionList && deploymentListObject.actionList.length > 0) {
                var systemIdList = _.pluck(_.pluck(deploymentListObject.actionList, "systemId"), "id")
                var vparIdList = []
                var vparObj = _.pluck(deploymentListObject.actionList, "cpuId")
                if (vparObj.indexOf(undefined) >= 0) {
                    Toaster.sayWarning("Choose system and vpar for " + deploymentListObject.id)
                    return
                }
                for (vObj in vparObj) {
                    var vObj = _.findWhere(deploymentListObject.vparList, {
                        id: parseInt(vparObj[vObj].id)
                    });
                    vparIdList.push({
                        "systemName": vObj.systemId.name,
                        "vparName": vObj.cpuName,
                        "vparType": vObj.cpuType
                    })
                }
                for (i in systemIdList) {
                    systemIdList[i] = parseInt(systemIdList[i])
                }
                // Remove duplicate object
                var vpar_uniqueList = []
                _.each(vparIdList, function (root_obj) {
                    var matchExists = false
                    _.each(vpar_uniqueList, function (uniq_obj) {
                        if (uniq_obj.systemName == root_obj.systemName && uniq_obj.vparName == root_obj.vparName && uniq_obj.vparType == root_obj.vparType) {
                            matchExists = true
                        }
                    })
                    if (!matchExists) {
                        vpar_uniqueList.push(root_obj)
                    }
                })
                if (systemIdList.length != _.uniq(systemIdList).length && vparIdList.length != vpar_uniqueList.length) { // Check for duplicate
                    Toaster.sayWarning("Duplicate loadset found for " + deploymentListObject.id)
                    return
                }
            }
            for (aIndex in deploymentListObject.actionList) {
                if (deploymentListObject.actionList[aIndex].deactivate || deploymentListObject.actionList[aIndex].activate || deploymentListObject.actionList[aIndex].loadandactivate || deploymentListObject.actionList[aIndex].deactivateanddelete) {
                    var actionObj = deploymentListObject.actionList[aIndex]
                    actionObj.planId = deploymentListObject.systemLoadList[0].planId
                    if (!actionObj.systemId || !actionObj.systemId.id) {
                        Toaster.sayWarning("Choose system for " + actionObj.planId.id)
                        return
                    }
                    if (!actionObj.cpuId || !actionObj.cpuId.id) {
                        Toaster.sayWarning("Choose test system for selected system in " + actionObj.planId.id)
                        return
                    }
                    if (!actionObj.id && (!actionObj.loadandactivate && !actionObj.activate && !actionObj.deactivate && !actionObj.deactivateanddelete)) {
                        Toaster.sayWarning("Choose action for " + actionObj.planId.id)
                        return
                    }
                    var dslUpdateFlag;
                    for (slIndex in deploymentListObject.systemLoadList) {
                        if (actionObj.systemId.id == deploymentListObject.systemLoadList[slIndex].systemId.id) {
                            actionObj.systemId = deploymentListObject.systemLoadList[slIndex].systemId
                            actionObj.systemLoadId = deploymentListObject.systemLoadList[slIndex]
                            if (actionObj.dslUpdate == true) {
                                // actionObj.systemLoadId.dslUpdate = "Y"
                                dslUpdateFlag = "Y"
                            } else {
                                // actionObj.systemLoadId.dslUpdate = "N"
                                dslUpdateFlag = "N"
                            }
                            delete actionObj.dslUpdate
                        }
                    }
                    actionObj.dslUpdate = dslUpdateFlag;
                    actionObj.cpuId = _.where(deploymentListObject.vparList, {
                        "id": parseInt(actionObj.cpuId.id)
                    })[0]

                    for (slIndex in deploymentListObject.systemLoadList) {
                        if (actionObj.systemId && actionObj.systemId.id == deploymentListObject.systemLoadList[slIndex].systemId.id) {
                            actionObj.systemId = deploymentListObject.systemLoadList[slIndex].systemId
                        }
                    }
                    if (actionObj.activate && actionObj.deactivateanddelete) {
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
                    delete actionObj.disabled
                    delete actionObj.enableVparForSubmittedWSP
                    loadActionObj = actionObj
                }
            }
            // delete deploymentListObject.isAnyLoadsInProgress
            // }
            delete deploymentListObject.ltcustLoaded
            APIFactory.postPreProdSystemLoad(loadActionObj, function (response) {
                if (response.status) {
                    $rootScope.saveformData()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
            $rootScope.saveformData()
        }

        // validation to show +Add only when ltcust and last action status 'success'
        $scope.checkSuccess = function (ltcust, actionList) {
            var status = false;
            if (actionList && actionList.length > 0) {
                status = _.filter(actionList, function (elem) {
                    return elem.lastActionStatus == "SUCCESS" && ltcust == true;
                }).length > 0;
            }
            return status;
        }

        $scope.checkCopySys = function (sys, planStatus) {
            var status = [];
            status = _.pluck(sys, "systemLoadActionsId")
            if (implementationPlanStatus().indexOf(planStatus) >= implementationPlanStatus().indexOf("PASSED_REGRESSION_TESTING")) {
                return false;
            } else if (status.length > 0) {
                return true;
            } else {
                return false;
            }
        }

        vm.defaultDisabledChecked = true
        // Validation for do  one action at a time
        $scope.makeCheck = function (plan, actionObj) {
            if (typeof actionObj.active == "undefined") {
                return
            }
            if (actionObj.loadandactivate || actionObj.activate || actionObj.deactivate || actionObj.deactivateanddelete) {
                if (!vm.enableAddBtn[plan.id] && typeof actionObj.active != "undefined") {
                    Toaster.sayWarning("Only one action allowed at a time")
                    if (actionObj.activate && actionObj.deactivateanddelete) {
                        vm.enableAddBtn[plan.id] = true
                    }
                    actionObj.loadandactivate = false
                    actionObj.activate = false
                    actionObj.deactivate = false
                    actionObj.deactivateanddelete = false
                    return
                }
                vm.enableAddBtn[plan.id] = false
            } else {
                vm.enableAddBtn[plan.id] = true
            }
        }

        $scope.deleteTestSystemLoad = function (index, plan, id) {
            if (!id) {
                vm.enableAddBtn[plan.id] = true
                plan.actionList.splice(index, 1)
                return
            }
            APIFactory.deletePreProductionLoad({
                "id": id
            }, function (response) {
                if (response.status) {
                    $scope.refresh()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }
    }

    function initSwitch() {
        if ($state.current.name == 'app.tssYodaDeployment') {
            $scope.isYoda = true
            $scope.isTos = false
            $scope.doYoda()
        }
        if ($state.current.name == 'app.tssTosDeployment') {
            $scope.isYoda = false
            $scope.isTos = true
            $scope.doTos()
        }
    }
    initSwitch()
});