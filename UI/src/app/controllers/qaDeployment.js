dashboard.controller("qaDeploymentCtrl", function ($rootScope, $scope, $state, $location, Toaster, WFLogger, apiService,
    APIFactory, Upload, appSettings, IPService, $filter, $timeout, $mdDialog, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.deploymentList = []
    $scope.userRole = getUserData('userRole')
    var apiBase = appSettings.apiBase;
    vm.implList = []
    vm.approvalsList = []
    Paginate.refreshScrolling();
    $scope.qaDeploypmentScreen = true
    $scope.isPassedRegressionScreen = false
    $scope.qaScreenType = "FUNCTIONAL"
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

    $scope.RFCClick = function (pObj) {
        var getBase1 = IPService.initRFC($scope, vm, pObj)
    }

    $timeout(function () {
        $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
    }, 1000)


    $scope.qaType = function (type) {
        $scope.qaScreenType = type
        vm.searchPlanData = ""
        $scope.refreshForDeployment()
    }

    $scope.enablePassBtn = function (vparList) {
        var statusTrue = false
        if (vparList.length > 0) {
            for (var i = 0; i < vparList.length; i++) {
                if (vparList[i].testStatus == "PASS") {
                    statusTrue = true;
                } else {
                    statusTrue = false;
                    break;
                }
            }
        }

        return statusTrue;
    }


    $scope.checkTssDeploy = function (vparList) {
        var isTSSD = false
        if (vparList && vparList.length > 0) {
            var checkLTCUST = _.filter(vparList, function (VparObj) {
                return VparObj.vparId.tssDeploy == true;
            })
        }
        if (vparList) {
            if (checkLTCUST.length == vparList.length) {
                isTSSD = true;
            }
        }

        var activated = _.filter(vparList, function (VparObj) {
            return VparObj.status == "ACTIVATED";
        });
        if (activated && activated.length > 0) {
            isTSSD = false;
        }

        return isTSSD;
    }

    /**
     * ZTPFM-2631 QA Regression Status change
     */
    $scope.checkVparsActivated = function (sysActionList, sysObj) {
        var arrayList = [];
        arrayList = _.filter(sysActionList, function (Obj) {
            return sysObj != undefined && Obj.status == "ACTIVATED" && sysObj.planId.id == Obj.planId.id && sysObj.id == Obj.systemLoadId.id && Obj.isVparActivated
        })
        return arrayList.length > 0;
    }

    $scope.showAcceptForQA = function (selectvpar, vparList) {
        var isTSSD = false
        if (selectvpar && selectvpar.length > 0) {
            var checkLTCUST = _.filter(vparList, function (VparObj) {
                return selectvpar.indexOf(VparObj.id.toString()) >= 0 && VparObj.tssDeploy == true;
            })
        }

        if (checkLTCUST && checkLTCUST.length > 0) {
            isTSSD = true;
        }
        return isTSSD;
    }

    $scope.checkDeploy = function (vparList) {
        var isTSSD = false
        if (vparList && vparList.length > 0) {
            var checkLTCUST = _.filter(vparList, function (VparObj) {
                return VparObj.tssDeploy == true;
            })
        }

        if (checkLTCUST && checkLTCUST.length > 0) {
            isTSSD = true;
        }
        return isTSSD;
    }



    $scope.getLoadDT = function (plan, systemId) {
        var dt;
        for (sIndex in plan.systemLoadList) {
            if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                dt = plan.systemLoadList[sIndex].loadDateTime
            }
        }
        return dt ? dt : "-"
    }

    $scope.applyPass_Fail = function (planObj, systemId, vparId, type, all, ev) {
        var paramObj = {
            "planId": planObj.id,
            "systemId": systemId,
            "vparId": vparId,
            "status": type,
            "vparsId": vm.uniqVPARList
        }
        if (all) {
            var populatedActions = _.where(planObj.extraParams.qaTemplate, {
                "systemId": parseInt(systemId)
            })[0].prevDeploymentActions;
            paramObj.vparId = _.pluck(_.pluck(populatedActions, "vparId"), "id")
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

    $scope.acceptForQA = function (planToAccept) {
        if (!planToAccept.loadandactivate) {
            Toaster.sayWarning("Select Load & Activate checkbox and click on 'Accept for QA' button")
            return
        } else {
            $scope.submitChanges()
        }
    }
    $scope.submitChanges = function () {
        var deploymentListObject = angular.copy(vm.deploymentList)
        var loadActionObj = []
        var checkLoadChoosen = [];
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
            var showError = true
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
                        if (actionObj.loadandactivate || actionObj.activate || actionObj.deactivateanddelete || actionObj.deactivate) {
                            showError = false;
                            checkLoadChoosen.push(showError)
                        } else {
                            checkLoadChoosen.push(showError)
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
                        delete actionObj.disableLoadActionDueToTSSDeploy

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
        if (!_.contains(checkLoadChoosen, false)) {
            Toaster.sayWarning("No action performed")
            return
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
        APIFactory.deleteTestSystemLoad({
            "id": id
        }, function (response) {
            if (response.status) {
                $scope.refresh()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
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