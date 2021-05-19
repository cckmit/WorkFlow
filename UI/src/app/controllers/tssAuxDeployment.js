dashboard.controller("tssAuxDeploymentCtrl", function($rootScope, $scope, $state, $location, $timeout, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger, WSService, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.auxDeploymentList = []
    $scope.userRole = getUserData('userRole')
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    Paginate.refreshScrolling();
    $scope.tssAuxScreen = true;
    $scope.isYoda = false
    $scope.isTos = false

    $scope.doYoda = function() {
        var getBase = IPService.initPlan($scope, vm)
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)

        $timeout(function() {
            $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
        }, 1000)

        WSService.initEAuxDeployStatus(function(sResponse) {
            Toaster.sayStatus(sResponse.status);
            $rootScope.saveformData()
        })

        $scope.addRow = function(plan) {
            if (!plan.actionList) {
                plan.actionList = []
            }
            plan.actionList.push({})
        }

        $scope.deleteRow = function(plan) {
            plan.actionList.splice(-1)
        }

        $scope.clearTestSystem = function(actionObj) {
            if (actionObj.vparId.inputTestSystemText) {
                actionObj.vparId.name = '';
                actionObj.vparId.inputTestSystemText = false;
            }
        }

        $scope.testSystemTextField = function(planAction) {
            if (planAction.id === "Other") {
                planAction.inputTestSystemText = true;
            }
        }
        $scope.clearTestSystemTextField = function(planAction) {
            planAction.id = '';
            planAction.name = '';
            planAction.inputTestSystemText = false;
        }

        $scope.getLoadDT = function(plan, systemId) {
            //{{sObj.loadDateTime | formattedDateTimeWithoutSeconds}}
            var dt;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    dt = plan.systemLoadList[sIndex].loadDateTime
                }
            }
            return dt ? dt : "-"
        }

        $scope.getLoadSetName = function(plan, systemId) {
            var st;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    st = plan.systemLoadList[sIndex].loadSetName
                }
            }
            return st ? st : "-"
        }

        $scope.applyPass_Fail = function(planId, systemId, vparId, type) {
            var paramObj = {
                "planId": planId,
                "systemId": systemId,
                "vparId": vparId,
                "status": type,
                "vparsId": vm.uniqVPARList
            }
            APIFactory.planTestStatus(paramObj, function(response) {
                if (response.status) {
                    Toaster.saySuccess(type + " action done for " + planId)
                    $scope.refresh()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }
        $scope.getVparType = function(vparList, selectedId) {
            for (index in vparList) {
                if (vparList[index].id == selectedId) {
                    return vparList[index].type;
                }
            }
            return "-"
        }

        $scope.deleteTestSystemLoad = function(index, plan, id) {
            if (!id) {
                plan.actionList.splice(index, 1)
                return
            }
            APIFactory.deleteTestSystemLoad({ "id": id }, function(response) {
                if (response.status) {
                    $scope.refresh()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        // Do FTP Call
        $scope.doFTP = function(actionobject, pObject) {
            var deploymentListObject = angular.copy(vm.auxDeploymentList)
            var loadActionObj = [];
            for (dIndex in deploymentListObject) {
                if (deploymentListObject[dIndex].actionList && deploymentListObject[dIndex].actionList.length > 0) {
                    var systemIdList = _.pluck(_.pluck(deploymentListObject[dIndex].actionList, "systemId"), "id")
                    var vparIdList = []
                    var vparObj = _.pluck(deploymentListObject[dIndex].actionList, "vparId")
                    for (vObj in vparObj) {
                        if (vparObj[vObj].id) {
                            if (vparObj[vObj].id === 'Other') {
                                vparIdList.push({
                                    "systemName": _.where(_.pluck(deploymentListObject[dIndex].systemLoadList, "systemId"), { "id": parseInt(systemIdList[systemIdList.length - 1]) })[0].name,
                                    "vparName": vparObj[vObj].name,
                                    "vparType": "PRIVATE"
                                })
                            } else {
                                var vObj = _.findWhere(deploymentListObject[dIndex].vparList, { id: parseInt(vparObj[vObj].id) });
                                vparIdList.push({
                                    "systemName": vObj.systemId.name,
                                    "vparName": vObj.name,
                                    "vparType": vObj.type
                                })
                            }
                        }
                    }
                    for (i in systemIdList) {
                        systemIdList[i] = parseInt(systemIdList[i])
                    }
                    // Remove duplicate object
                    var vpar_uniqueList = []
                    _.each(vparIdList, function(root_obj) {
                        var matchExists = false
                        _.each(vpar_uniqueList, function(uniq_obj) {
                            if (uniq_obj.systemName == root_obj.systemName && uniq_obj.vparName == root_obj.vparName && uniq_obj.vparType == root_obj.vparType) {
                                matchExists = true
                            }
                        })
                        if (!matchExists) {
                            vpar_uniqueList.push(root_obj)
                        }
                    })
                    if (systemIdList.length != _.uniq(systemIdList).length && vparIdList.length != vpar_uniqueList.length) { // Check for duplicate
                        Toaster.sayWarning("Duplicate loadset found for " + deploymentListObject[dIndex].id)
                        return
                    }
                }
            }

            // for (aIndex in deploymentListObject[dIndex].actionList) {
            // pObject - Plan Obj
            var actionObj = angular.copy(actionobject)
            actionObj.planId = pObject.systemLoadList[0].planId
            var dslUpdateFlag;
            for (slIndex in pObject.systemLoadList) {
                if (actionObj.systemId.id == pObject.systemLoadList[slIndex].systemId.id) {
                    actionObj.systemId = pObject.systemLoadList[slIndex].systemId
                    actionObj.systemLoadId = pObject.systemLoadList[slIndex]
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

            delete actionObj.vparId.inputTestSystemText;

            if (actionObj.vparId.id === "Other") {
                actionObj.vparId.id = null;
            } else {
                actionObj.vparId = _.where(pObject.vparList, { "id": parseInt(actionObj.vparId.id) })[0]
            }
            actionObj.status = "LOADED"
            delete actionObj.deactivateanddelete
            delete actionObj.disabled
                // if (!actionobject.id || actionobject.id == actionObj.id) {
            loadActionObj.push(actionObj)
                // }
                // loadActionObj.push(actionObj)
                // }
            APIFactory.doFTP(loadActionObj, function(response) {
                if (response.status) {
                    $scope.refresh($scope.recentPage)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })

        }
    }
    $scope.doTos = function() {
        var getBase = IPService.initPlan($scope, vm)
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)

        vm.enableAddBtn = {}

        $timeout(function() {
            $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
        }, 1000)
        
        WSService.initEAuxDeployStatus(function(sResponse) {
            Toaster.sayStatus(sResponse.status);
            $rootScope.saveformData()
        })

        $scope.addRow = function(plan) {
            if (!plan.actionList) {
                plan.actionList = []
            }
            vm.enableAddBtn[plan.id] = false
            plan.actionList.push({})
        }
        $scope.deleteRow = function(plan) {
            plan.actionList.splice(-1)
        }

        $scope.getLoadDT = function(plan, systemId) {
            //{{sObj.loadDateTime | formattedDateTimeWithoutSeconds}}
            var dt;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    dt = plan.systemLoadList[sIndex].loadDateTime
                }
            }
            return dt ? dt : "-"
        }

        $scope.getLoadSetName = function(plan, systemId) {
            var st;
            for (sIndex in plan.systemLoadList) {
                if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                    st = plan.systemLoadList[sIndex].loadSetName
                }
            }
            return st ? st : "-"
        }


        $scope.getVparType = function(vparList, selectedId) {
            for (index in vparList) {
                if (vparList[index].id == selectedId) {
                    return vparList[index].cpuType;
                }
            }
            return "-"
        }

        WSService.initPreProductionLoad(function(response) {
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

        $scope.submitChanges = function(deploymentListObject) {
            var deploymentListObject = angular.copy(deploymentListObject)
            var loadActionObj = {};
            // if (deploymentListObject.actionList.length == 0) {
            //     Toaster.sayWarning("Add some system for " + deploymentListObject.id)
            //     return
            // }
            // for (dIndex in deploymentListObject) {
            if (deploymentListObject.actionList && deploymentListObject.actionList.length > 0) {
                var systemIdList = _.pluck(_.pluck(deploymentListObject.actionList, "systemId"), "id")
                var vparIdList = []
                var vparObj = _.pluck(deploymentListObject.actionList, "cpuId")
                    // if (vparObj.indexOf(undefined) >= 0) {
                    //     Toaster.sayWarning("Choose system and vpar for " + deploymentListObject.id)
                    //     return
                    // }
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
                _.each(vparIdList, function(root_obj) {
                    var matchExists = false
                    _.each(vpar_uniqueList, function(uniq_obj) {
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
                if (deploymentListObject.actionList[aIndex]) {
                    var actionObj = deploymentListObject.actionList[aIndex]
                    actionObj.planId = deploymentListObject.systemLoadList[0].planId
                        // if (!actionObj.systemId || !actionObj.systemId.id) {
                        //     Toaster.sayWarning("Choose system for " + actionObj.planId.id)
                        //     return
                        // }
                        // if (!actionObj.cpuId || !actionObj.cpuId.id) {
                        //     Toaster.sayWarning("Choose test system for selected system in " + actionObj.planId.id)
                        //     return
                        // }
                        // if (!actionObj.id && (!actionObj.loadandactivate && !actionObj.activate && !actionObj.deactivate && !actionObj.deactivateanddelete)) {
                        //     Toaster.sayWarning("Choose action for " + actionObj.planId.id)
                        //     return
                        // }
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

                    // for (slIndex in deploymentListObject.systemLoadList) {
                    //     if (actionObj.systemId && actionObj.systemId.id == deploymentListObject.systemLoadList[slIndex].systemId.id) {
                    //         actionObj.systemId = deploymentListObject.systemLoadList[slIndex].systemId
                    //     }
                    // }
                    // if (actionObj.activate && actionObj.deactivateanddelete) {
                    //     Toaster.sayWarning("Activate/Deactivate/Delete cannot happen at same time")
                    //     return
                    // }
                    // if (actionObj.loadandactivate) {
                    //     actionObj.status = "LOADED"
                    // }
                    // delete actionObj.loadandactivate
                    // if (actionObj.activate) {
                    //     actionObj.status = "ACTIVATED"
                    // }
                    // delete actionObj.activate
                    // if (actionObj.deactivate) {
                    //     actionObj.status = "DEACTIVATED"
                    // }
                    // delete actionObj.deactivate
                    // if (actionObj.deactivateanddelete) {
                    //     actionObj.status = "DELETED"
                    // }
                    actionObj.status = "LOADED"
                    delete actionObj.deactivateanddelete
                    delete actionObj.disabled
                    loadActionObj = actionObj
                }
            }
            // }
            APIFactory.tssPostPreProdSystemAuxLoad(loadActionObj, function(response) {
                if (response.status) {
                    $rootScope.saveformData()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
            $rootScope.saveformData()
        }

        // Validation for do  one action at a time
        $scope.makeCheck = function(plan, actionObj) {
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

        $scope.deleteTestSystemLoad = function(index, plan, id) {
            if (!id) {
                vm.enableAddBtn[plan.id] = true
                plan.actionList.splice(index, 1)
                return
            }
            APIFactory.deletePreProductionLoad({
                "id": id
            }, function(response) {
                if (response.status) {
                    $scope.refresh()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }
    }


    function initSwitch() {

        if ($state.current.name == 'app.tssAuxDeploymentYoda') {
            $scope.isYoda = true
            $scope.isTos = false
            $scope.doYoda()
        }
        if ($state.current.name == 'app.tssAuxDeploymentTos') {
            $scope.isYoda = false
            $scope.isTos = true
            $scope.doTos()
        }
    }
    initSwitch()

});