dashboard.controller("qaAuxDeploymentScreenCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, $mdDialog, APIFactory, WFLogger, IPService, Upload, Paginate, WSService, Access, $filter, $timeout) {

    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    vm.deploymentList = []
    $scope.userRole = getUserData('userRole')
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    Paginate.refreshScrolling();
    $scope.qaAuxDeploymentScreen = true
    $scope.qaScreenType = "FUNCTIONAL"
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

    $timeout(function () {
        $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
    }, 1000)

    WSService.initEAuxDeployStatus(function (sResponse) {
        Toaster.sayStatus(sResponse.status);
        $rootScope.saveformData()
    })

    $scope.qaType = function (type) {
        $scope.qaScreenType = type
        vm.searchPlanData = ""
        $scope.refreshForDeployment()
    }

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

    /**
    * ZTPFM-2631 QA Regression Status change
    */
    $scope.checkVparsActivated = function (sysActionList, sysObj) {
        var arrayList = [];
        arrayList = _.filter(sysActionList, function (Obj) {
            return sysObj.planId != undefined && Obj.status == "ACTIVATED" && sysObj.planId.id == Obj.planId.id && sysObj.systemLoadId.id == Obj.systemLoadId.id && Obj.isVparActivated
        })
        return arrayList.length > 0;
    }

    $scope.applyPass_Fail = function (planId, systemId, vparId, type, ev) {
        var paramObj = {
            "planId": planId,
            "systemId": systemId,
            "vparId": vparId,
            "status": type,
            "vparsId": vm.uniqVPARList
        }

        if (type == "PASS") {
            proceedWithPostTestLoad(paramObj)
        } else {
            var confirm = $mdDialog.confirm()
                .title('Are you sure?')
                .textContent('Would you like to proceed with reject test load for ' + paramObj.planId + " ?")
                .ariaLabel('reject')
                .targetEvent(ev)
                .ok('Proceed')
                .cancel('Cancel');

            $mdDialog.show(confirm).then(function () {
                $mdDialog.hide()
                proceedWithPostTestLoad(paramObj)
            }, function () {

            });
        }

    }

    function proceedWithPostTestLoad(paramObj) {
        APIFactory.planTestStatus(paramObj, function (response) {
            if (response.status) {
                Toaster.saySuccess(paramObj.status + " action done for " + paramObj.planId)
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

    // Do FTP Call
    $scope.doFTP = function (actionobject, pObject) {
        var deploymentListObject = angular.copy(vm.deploymentList)
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
        APIFactory.doFTP(loadActionObj, function (response) {
            if (response.status) {
                $scope.refresh($scope.recentPage)
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })

    }

    $scope.submitChanges = function () {
        var deploymentListObject = angular.copy(vm.deploymentList)
        var loadActionObj = []
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
                    Toaster.sayWarning("Duplicate loadset found for " + deploymentListObject[dIndex].id)
                    return
                }
            }
            for (aIndex in deploymentListObject[dIndex].actionList) {
                var actionObj = deploymentListObject[dIndex].actionList[aIndex]
                actionObj.planId = deploymentListObject[dIndex].systemLoadList[0].planId
                if (!actionObj.systemId || !actionObj.systemId.id) {
                    Toaster.sayWarning("Choose system for " + actionObj.planId.id)
                    return
                }
                if (!actionObj.vparId || !actionObj.vparId.id) {
                    Toaster.sayWarning("Choose test system for selected system in " + actionObj.planId.id)
                    return
                }
                if (!actionObj.id && (!actionObj.loadandactivate && !actionObj.activate && !actionObj.deactivate && !actionObj.deactivateanddelete)) {
                    Toaster.sayWarning("Choose action for " + actionObj.planId.id)
                    return
                }
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

                delete actionObj.vparId.inputTestSystemText;

                if (actionObj.vparId.id === "Other") {
                    actionObj.vparId.id = null;

                } else {
                    actionObj.vparId = _.where(deploymentListObject[dIndex].vparList, { "id": parseInt(actionObj.vparId.id) })[0]
                }




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
                loadActionObj.push(actionObj)
            }
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

}).directive("multiSelect", function () {
    return function (scope, element, attr) {
        $.fn.select2.amd.require(['select2/selection/search'], function (Search) {
            var oldRemoveChoice = Search.prototype.searchRemoveChoice;
            Search.prototype.searchRemoveChoice = function () {
                oldRemoveChoice.apply(this, arguments);
                this.$search.val('');
            };
            $(element).select2({
                tags: "true"
            });
        });
    }
})