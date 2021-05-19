dashboard.controller("passedQARegressionCtrl", function($rootScope, $scope, $state, $location, Toaster, WFLogger, apiService,
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
    $scope.qaScreenType = "REGRESSION"
    $scope.isPassedRegressionScreen = true
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

    $scope.RFCClick = function (pObj) {
		var getBase1 = IPService.initRFC($scope, vm, pObj)
    }

    $timeout(function() {
        $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
    }, 1000)


    // $scope.qaType = function(type) {
    //     $scope.qaScreenType = type
    //     $scope.refreshForDeployment()
    // }



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

    // $scope.applyPass_Fail = function(planObj, systemId, vparId, type, all, ev) {
    //     var paramObj = {
    //         "planId": planObj.id,
    //         "systemId": systemId,
    //         "vparId": vparId,
    //         "status": type,
    //         "vparsId": vm.uniqVPARList
    //     }
    //     if (all) {
    //         var populatedActions = _.where(planObj.extraParams.qaTemplate, {
    //             "systemId": parseInt(systemId)
    //         })[0].prevDeploymentActions;
    //         paramObj.vparId = _.pluck(_.pluck(populatedActions, "vparId"), "id")
    //     }

    //     if (type == "PASS") {
    //         proceedWithPostTestLoad(paramObj)
    //     } else {
    //         var confirm = $mdDialog.confirm()
    //             .title('Are you sure?')
    //             .textContent('Would you like to proceed with reject test load for ' + paramObj.planId + " ?")
    //             .ariaLabel('reject')
    //             .targetEvent(ev)
    //             .ok('Proceed')
    //             .cancel('Cancel');

    //         $mdDialog.show(confirm).then(function() {
    //             $mdDialog.hide()
    //             proceedWithPostTestLoad(paramObj)
    //         }, function() {

    //         });
    //     }
    // }

    // function proceedWithPostTestLoad(paramObj) {
    //     APIFactory.planTestStatus(paramObj, function(response) {
    //         if (response.status) {
    //             Toaster.saySuccess(paramObj.status + " action done for " + paramObj.planId)
    //             $scope.refresh()
    //         } else {
    //             Toaster.sayError(response.errorMessage)
    //         }
    //     })
    // }
    $scope.getVparType = function(vparList, selectedId) {
        for (index in vparList) {
            if (vparList[index].id == selectedId) {
                return vparList[index].type;
            }
        }
        return "-"
    }

    $scope.submitChanges = function() {
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
            _.each(rootList, function(rootObj) {
                if (rootObj.prevDeploymentActions.length > 0) {
                    _.each(rootObj.prevDeploymentActions, function(prevDeploymentObj) {
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
        APIFactory.postTestSystemLoad( {"skipPassed" : true}, loadActionObj, function(response) {
            if (response.status) {
                $scope.refresh($scope.recentPage)
                $rootScope.saveformData()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
        $rootScope.saveformData()
    }

    $scope.deleteTestSystemLoad = function(index, plan, id) {
        if (!id) {
            plan.actionList.splice(index, 1)
            return
        }
        APIFactory.deleteTestSystemLoad({
            "id": id
        }, function(response) {
            if (response.status) {
                $scope.refresh()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

}).directive("multiSelect", function() {
    return function(scope, element, attr) {
        $.fn.select2.amd.require(['select2/selection/search'], function(Search) {
            var oldRemoveChoice = Search.prototype.searchRemoveChoice;
            Search.prototype.searchRemoveChoice = function() {
                oldRemoveChoice.apply(this, arguments);
                this.$search.val('');
            };
            $(element).select2({
                tags: "true"
            });
        });
    }
})