dashboard.controller("leadDeploymentCtrl", function ($rootScope, $scope, $state, $location, Toaster, WFLogger, apiService, APIFactory, Upload, appSettings, IPService, $filter, $timeout, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.deploymentList = []
    $scope.userRole = getUserData('userRole')
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    Paginate.refreshScrolling();
    $scope.leadDeploypmentScreen = true
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

    $timeout(function () {
        $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
    }, 1000)

    $scope.RFCClick = function (pObj) {
        var getBase1 = IPService.initRFC($scope, vm, pObj)
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

    $scope.submitChanges = function () {
        var deploymentListObject = angular.copy(vm.deploymentList)
        var loadActionObj = []
        for (dIndex in deploymentListObject) {
            var rootList = deploymentListObject[dIndex].extraParams.qaTemplate
            // If it is new entry
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
                            dslUpdate: deploymentListObject[dIndex].dsl_update,
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
                                actionObj.systemLoadId = angular.copy(deploymentListObject[dIndex].systemLoadList[slIndex])
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
                                var selectedVparList = _.where(deploymentListObject[dIndex].vparList, {
                                    "id": parseInt(vparCopy[eachVpar])
                                })
                                if (selectedVparList.length == 0) {
                                    tempActionObj.vparId = {
                                        "name": vparCopy[eachVpar]
                                    }
                                } else {
                                    tempActionObj.vparId = selectedVparList[0]
                                }
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

    $scope.onSelect = function (pId, sysId, vparList, valueEntered) {
        var selectedVparVal;
        selectedVparVal = valueEntered;
        if (selectedVparVal && _.where(vparList, { "id": parseInt(selectedVparVal) }).length <= 0) {
            try {
                APIFactory.getIsPrivate({ "systemId": sysId, "vparsName": selectedVparVal }, function (response) {
                    if (response && response.status) {

                    } else {
                        $timeout(function () {
                            Toaster.sayError(response.errorMessage)
                            var selectedVparsListInPlan
                            selectedVparsListInPlan = angular.copy($("#Leadvpar_" + pId).val())
                            if (selectedVparsListInPlan != null && selectedVparsListInPlan.indexOf(selectedVparVal) != -1) {
                                selectedVparsListInPlan.splice(selectedVparsListInPlan.indexOf(selectedVparVal), 1)
                                $("#Leadvpar_" + pId).val(selectedVparsListInPlan).trigger("change")
                            }
                            $scope.$apply()
                        }, 0)

                    }
                })
            } catch (err) { }
        }
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