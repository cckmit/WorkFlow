dashboard.controller("updateImpCtrl", function ($rootScope, $http, $scope, $state, $stateParams, $location, $window,
    Toaster, appSettings, fImplementationPlanValidate, $timeout, apiService, IService, $mdDialog, Upload, APIFactory, WFLogger, $q, $filter, Paginate) {
    var vm = this;
    vm.planId = $stateParams.planId
    vm.imp = {}
    vm.imp.planId = {}
    vm.imp.planId.id = vm.planId
    // var segmentsToPopulateSummary = []
    // vm.segmentsToPopulate = []
    // vm.selectedVanillaSegments = [];
    // $rootScope.segmentsToPopulate = [];
    $rootScope.titleHeading = $state.current.data.pageTitle + " > Edit"
    $rootScope.prevSaveButton = false;
    vm.prodList = ['Pass', 'Fail'];
    var apiBase = appSettings.apiBase;
    $scope.fImplementationPlanValidate = fImplementationPlanValidate
    $scope.segmentsList = []
    vm.userAccess = true
    vm.checkoutInProgress = false
    vm.flagTrueFalse = false;
    vm.workspaceList = []
    var key = "checkin-enable";
    vm.commitdataList = JSON.parse(localStorage.getItem(key));
    vm.currentUser = $rootScope.home_menu
    var stockImpEditObj = {}
    vm.updatedDBData = {}
    $scope.userRole = getUserData("userRole")
    vm.loader = {
        unitTest: false,
        updateImp: false,
        integration: false,
        peerReview: false
    };
    if (getUserData("userRole") == "Lead") {
        vm.userAccess = false;
    }
    Paginate.refreshScrolling();
    vm.showCheckoutMakLoading = true
    vm.showMakCheckoutLoading = true
    vm.showMakInfo = false;
    vm.makFileLoading = false;
    $scope.enableReadyForQA = false
    $scope.partialBuildStatus = false
    $scope.impDescritonOldData = "";
    $scope.impDevLocationOldData = "";
    $scope.impDevIdOldData = "";
    $scope.imppeerReviewersOldData = "";
    $scope.impdevContactOldData = "";
    $scope.loadImpData = function (cbk) {
        APIFactory.getImplementation({
            "id": $stateParams.impId
        }, function (response) {
            if (response.status) {
                vm.imp = response.data;
                if (localStorage.getItem(key) == null) {
                    localStorage.setItem(key, "true");
                }
                $scope.impDescritonOldData = vm.imp.impDesc
                $scope.impDevLocationOldData = vm.imp.devLocation
                $scope.impDevIdOldData = vm.imp.devId
                $scope.imppeerReviewersOldData = vm.imp.peerReviewers
                $scope.impdevContactOldData = vm.imp.devContact
                if (vm.imp.prTktNum !== null) {
                    $("#iprobTktNo").val(vm.imp.prTktNum)
                    $("#iprobTktNo").tagit({
                        singleField: true,
                        caseSensitive: false,
                        singleFieldNode: $('#iprobTktNo'),
                        afterTagAdded: function (event, ui) {
                            itktAdded($(ui.tag).find("span").html())
                        },
                        afterTagRemoved: function (event, ui) {
                            tktRemoved()
                        }
                    })
                }


                if (response.data.peerReviewers !== null) {
                    vm.imp.peerReviewers = response.data.peerReviewers.split(",");
                }
                if (response.data.segments) {
                    vm.workspaceList = response.data.segments
                    vm.imp.segments = vm.workspaceList
                    $scope.workspaceLength = vm.workspaceList.length
                }
                // loadPeerReviewers();
                if (response.data.substatus === "INTEGRATION_TESTING_COMPLETED") {
                    $scope.enableReadyForQA = true;
                } else {
                    $scope.enableReadyForQA = false;
                }
                if (response.data.substatus === "PEER_REVIEW_COMPLETED" || vm.imp.substatus == 'BYPASSED_PEER_REVIEW') {
                    getFullBuildStatus()
                }
                angular.copy(vm.imp, stockImpEditObj)
                angular.copy(vm.imp, vm.updatedDBData)
                $scope.loadPlanStatusByimpPlan();
                if (cbk && Object.keys(vm.imp).length > 0) {
                    cbk(true);
                }
            } else {
                Toaster.sayError("Error in fetching implementation data");
            }
        })
    }
    $scope.loadImpData();

    var getFullBuildStatus = function () {
        APIFactory.getBuildByPlan({
            "id": vm.planId
        }, function (response) {
            if (response.status && response.data.length > 0) {
                for (index in response.data) {
                    // response.data[index].jobStatus == "S" &&
                    if (response.data[index].buildStatus != "FULL" || response.data[index].jobStatus != "S") {
                        $scope.partialBuildStatus = true
                    }
                }
            } else {
                $scope.partialBuildStatus = true
            }
            if (vm.imp.planId.macroHeader) {
                $scope.partialBuildStatus = false
            }
            getFullLoadsetStatus()
        })
    }

    var getFullLoadsetStatus = function () {
        APIFactory.getDevLoadByPlan({
            "id": vm.planId
        }, function (response) {
            if (response.status && response.data.length > 0) {
                for (eachSystem in response.data) {
                    if (response.data[eachSystem].jobStatus != "S") {
                        $scope.partialBuildStatus = true
                    }
                }
            } else {
                $scope.partialBuildStatus = true
            }
            if (vm.imp.planId.macroHeader) {
                $scope.partialBuildStatus = false
            }
        }

        )
    }


    $timeout(function () {
        $('[aria-label="impdesc"]').focus()
    }, 1000)

    var loadStatusList = function () {
        APIFactory.getImplementationStatusList({}, function (response) {
            if (response.status) {
                vm.impStatusList = response.data
            } else {
                vm.impStatusList = []
            }
        })
        APIFactory.getImplementationSubStatusList({}, function (response) {
            if (response.status) {
                vm.impSubStatusList = response.data
            } else {
                vm.impSubStatusList = []
            }
        })
    }
    loadStatusList()


    $scope.loadSegmentList = vm.loadSegmentMappingList = function () {
        paramObj = {
            "ids": $stateParams.impId
        }
        APIFactory.getSegmentList(paramObj, function (response) {
            if (response.status && response.data.length > 0) {
                vm.workspaceList = response.data
                vm.segmentSelection.singleSelect = {}
            } else {
                vm.workspaceList = []
                vm.segmentSelection.singleSelect = {}
            }
        })
    }
    $scope.loadSegmentList()

    $scope.numberToDisplay = 20;

    // $scope.logEvents = [];
    // for (var i = 0; i < 1000; i++) {
    //     $scope.logEvents.push({
    //         name: "Hello, my name is " + i
    //     });
    // }

    // $scope.loadMore = function() {
    //     if ($scope.numberToDisplay + 5 < $scope.logEvents.length) {
    //         $scope.numberToDisplay += 5;
    //     } else {
    //         $scope.numberToDisplay = $scope.logEvents.length;
    //     }
    // };

    //ZTPFM-2533 getplanstatus
    $scope.planStausData = "";
    $scope.loadPlanStatusByimpPlan = function () {
        var impPlanId = vm.imp.id;
        APIFactory.getPlanStatusByImp({
            "impId": impPlanId
        }, function (response) {
            if (response.status) {
                $scope.planStausData = response.data;
            }
        })
    }

    var platformDetails;
    $scope.loadPlatformDetails = function () {
        var planId = vm.imp.planId.id;
        APIFactory.getSystemListByPlan({
            "planId": planId
        }, function (response) {
            if (response.status) {
                platformDetails = _.filter(response.data, function (pObj) {
                    return pObj.platformId.name == "Travelport";
                });
            } else {
                platformDetails = []
            }
        })
    }
    $scope.loadPlatformDetails();
    /* Ticket Number */

    vm.showImpTtktNumberError = false;

    function itktAdded(itkt) {
        itkt = itkt.toUpperCase()
        vm.showImpTtktNumberError = false;

        var removeStatus = ["Closed", "Closed Unresolved", "Cancelled", "Close Requested"]
        if (platformDetails && platformDetails.length > 0) {
            APIFactory.getProblemTicket({
                "ticketNumber": itkt
            }, function (response) {
                if (response.status && response.data.length > 0) {
                    vm.showImpTtktNumberError = false
                    var specifiedTicket = _.where(response.data, {
                        "refNum": itkt
                    })
                    if (specifiedTicket.length > 0) {
                        if (removeStatus.indexOf($.trim(specifiedTicket[0].status)) >= 0) {
                            vm.showImpTtktNumberError = true;
                            vm.iticketErrorMessage = itkt + " is " + $.trim(specifiedTicket[0].status)
                            $("#iprobTktNo").tagit("removeTagByLabel", itkt);
                            return;
                        }
                        vm.imp.prTktNum = $("#iprobTktNo").val()
                        $scope.enableUpdate = true;

                    } else {
                        $("#iprobTktNo").tagit("removeTagByLabel", itkt);
                    }
                } else {
                    $("#iprobTktNo").tagit("removeTagByLabel", itkt);
                    Toaster.sayError(response.errorMessage)
                }
            })
        } else {
            $scope.enableUpdate = true;
            var tempPrtkNum = vm.imp.prTktNum;
            if (tempPrtkNum.toUpperCase() == $("#iprobTktNo").val().toUpperCase()) {
                $scope.enableUpdate = false;
            }
            $scope.$apply()
        }

    }

    function tktRemoved(itkt) {
        if (platformDetails.length > 0) {
            vm.imp.prTktNum = $("#iprobTktNo").val()
            $scope.$digest()
        } else {
            var tempUpperPrtkNum = vm.imp.prTktNum.toUpperCase();
            if (tempUpperPrtkNum == $("#iprobTktNo").val().toUpperCase()) {
                $scope.enableUpdate = false;
            } else {
                $scope.enableUpdate = true;
            }
            $scope.$apply()
        }
        // vm.imp.prTktNum = $("#iprobTktNo").val()
        // $scope.enableUpdate = false;
        // $scope.$digest()
        vm.showImpTtktNumberError = false;
    }


    $scope.goBackImpl = function () {
        if ($rootScope.previousState != undefined) {
            $state.go($rootScope.previousState)
        } else {
            if (getUserData("userRole") == "Lead") {
                $state.go('app.impPlan');
            } else {
                $state.go('app.impl')
            }
        }
    }

    var loadDeveloperName = function (cbk) {
        $("#developersList").select2({
            maximumSelectionLength: 1
        });
        try {
            APIFactory.getUsersByRole({
                "role": "Developer"
            }, function (response) {
                if (response.status) {
                    vm.developerList = response.data
                    if (cbk) {
                        cbk(true)
                    }
                } else {
                    vm.developerList = []
                    if (cbk) {
                        cbk(false)
                    }
                }
                //  initMultipleSelect2("#developersList")
                // $timeout(function () {
                //     // $("#developersList").val(vm.imp.devId).trigger("change");
                //     $("#developersList").select2({
                //         maximumSelectionLength: 1
                //     });
                //     // if(vm.developerList)
                //     // if(_.where(vm.developerList, {"id" : vm.imp.devId}).length == 0){
                //     //     $("#developersList").val("").trigger("change");
                //     //     Toaster.sayWarning("Pre selected Developer has been removed from LDAP. Please Contact Administrator")
                //     // }
                // }, 500);
            })
        } catch (err) { }
    }
    // loadDeveloperName();


    var loadPeerReviewers = function (cbk) {
        try {

            APIFactory.getUsersByRole({
                "role": "Reviewer"
            }, function (response) {
                if (response.status) {
                    vm.peerReviewers = response.data
                    if (cbk) {
                        cbk(true)
                    }
                    /*if(vm.imp.reviewTktNumber){
                        $("#peerReviewers").select2("enable", false)
                    }*/
                } else {
                    vm.peerReviewers = []
                    if (cbk) {
                        cbk(false)
                    }
                }
                initMultipleSelect2("#peerReviewers");
                // $timeout(function () {
                // var checkExistingPeerReviewer = false
                // if(vm.peerReviewers)
                // for(i in vm.imp.peerReviewers){
                //     if(_.where(vm.peerReviewers, {"id" : vm.imp.peerReviewers[i]}).length == 0){
                //         checkExistingPeerReviewer = true
                //     }
                // }
                // if(checkExistingPeerReviewer && vm.imp.bypassPeerReview == false){
                //     $("#peerReviewers").val("").trigger("change");
                //     Toaster.sayWarning("Pre selected Peer Reviewer has been removed from LDAP. Please Contact Administrator")
                // }else{
                //     $("#peerReviewers").val(vm.imp.peerReviewers).trigger("change");
                // }
                // }, 500);
            })
        } catch (err) { }
    }
    // loadPeerReviewers();

    // Promise Logic

    var updateImpDataPromise = new Promise(function (resolve, reject) {
        $scope.loadImpData(function () {
            Object.keys(vm.imp).length > 0 ? resolve(vm.imp) : reject("plan")
        })

    })

    var peerReviewerPromise = new Promise(function (resolve, reject) {
        //reviewers
        loadPeerReviewers(function (isDataReady) {
            isDataReady == true ? resolve(vm.peerReviewers) : reject("reviewer")
        });
    })

    var developerPromise = new Promise(function (resolve, reject) {
        //developer
        loadDeveloperName(function (isDataReady) {
            isDataReady == true ? resolve(vm.developerList) : reject("developer")
        });
    })

    Promise.all([updateImpDataPromise, peerReviewerPromise, developerPromise]).then(function (values) {
        //Peer Reviewer Part
        var checkExistingPeerReviewer = false
        for (i in vm.imp.peerReviewers) {
            if (_.where(vm.peerReviewers, {
                "id": vm.imp.peerReviewers[i]
            }).length == 0) {
                checkExistingPeerReviewer = true
            }
        }
        if (checkExistingPeerReviewer && vm.imp.bypassPeerReview == false) {
            $("#peerReviewers").val("").trigger("change");
            Toaster.sayWarning("Pre selected Peer Reviewer has been removed from LDAP. Please Contact Administrator")
        } else {
            $timeout(function () {
                $("#peerReviewers").val(vm.imp.peerReviewers).trigger("change");
            }, 500);
        }

        //Developer Part


        if (_.where(vm.developerList, {
            "id": vm.imp.devId
        }).length == 0) {
            $("#developersList").val("").trigger("change");
            Toaster.sayWarning("Pre selected Developer has been removed from LDAP. Please Contact Administrator")
        } else {
            $timeout(function () {
                $("#developersList").val(vm.imp.devId).trigger("change");
            }, 500);
        }
    }, function (values) {
        switch (values) {
            case "reviewer":
                Toaster.sayWarning("Peer Reviewer List has not been received correctly")
                break;
            case "developer":
                Toaster.sayWarning("Developer List has not been received correctly")
                break;
        }
        return
    });

    // Update Status, Update Implementation Function
    $scope.saveStatusAndSubmitChange = function (data, cbk, changedSubStatus, changedImpStatus) {
        try {
            if (typeof data.devId != 'undefined' && data.devId.length > 0) {
                data.devId = data.devId.toString();
            }
            if (data.peerReviewers.indexOf(data.devId) > -1) {
                Toaster.sayWarning("Peer Reviewer and Developer should not be same")
                return;
            }
            if ((!data.devContact && data.substatus == "INTEGRATION_TESTING_COMPLETED") || (vm.impForm.developerContact.$invalid && vm.impForm.developerContact.$touched)) {
                Toaster.sayWarning("Provide Valid Developer phone number")
                return;
            }
            if (Array.isArray(data.peerReviewers)) {
                data.peerReviewers = data.peerReviewers ? (data.peerReviewers.length > 1 ? data.peerReviewers.join(",") : data.peerReviewers[0]) : ""
            }
            data.segments = []
            data.prTktNum = _.pluck(data.prTktNum, "text")
            data.prTktNum = $("#iprobTktNo").val()
            if (data.bypassPeerReview) {
                data.peerReviewers = ""
            } else {
                data.bypassPeerReview = false
            }
            if (fImplementationPlanValidate.validateImpMandatoryFields(data)) {

                angular.copy(data, stockImpEditObj)
                angular.copy(data, vm.updatedDBData)
                data = _.omit(data, ["peerreviewers", "segments"])
                var datas = angular.copy(data);
                if (changedSubStatus) {
                    datas.substatus = changedSubStatus
                    datas.impStatus = changedImpStatus
                }
                datas.devName = _.where(vm.developerList, {
                    "id": datas.devId
                })[0].displayName
                if (changedSubStatus == "UNIT_TESTING_COMPLETED") {
                    vm.loader.unitTest = true;
                } else if (changedSubStatus == "INTEGRATION_TESTING_COMPLETED") {
                    vm.loader.integration = true;
                } else {
                    vm.loader.updateImp = true;
                }
                APIFactory.updateImplementation(datas, function (response) {
                    vm.loader.unitTest = false;
                    vm.loader.integration = false;
                    vm.loader.updateImp = false;
                    if (response.status) {
                        if (changedSubStatus) {
                            data.substatus = changedSubStatus
                            data.impStatus = changedImpStatus
                            if (data.substatus == "UNIT_TESTING_COMPLETED") {
                                Toaster.saySuccess("Unit Testing Completed Successfully");
                            }
                        } else {
                            if (cbk) {
                                cbk()
                            } else {
                                if (response.errorMessage && response.errorMessage != "") {
                                    Toaster.sayError(response.errorMessage);
                                } else {
                                    Toaster.saySuccess("Implementation Updated Successfully");
                                }
                            }
                        }
                        $scope.loadImpData()
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })

            }
        } catch (err) { }
    }

    //Peer Review Request
    $scope.peerReviewRequest = function (implementationObj) {
        vm.loader.peerReview = true;
        $scope.saveStatusAndSubmitChange(vm.imp, function () {
            APIFactory.requestPeerReview({
                "implId": implementationObj.id
            }, function (response) {
                vm.loader.peerReview = false;
                if (response.status) {
                    Toaster.saySuccess("Peer review ticket created successfully")
                    $scope.loadImpData()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        })
    }


    $scope.enableUpdate = false
    $scope.$watch("vm.imp", function (impObj) {
        if (typeof impObj.devId != 'undefined' && impObj.devId.length > 0) {
            impObj.devId = impObj.devId.toString()
        }
        if (!angular.equals(impObj, stockImpEditObj)) {
            $scope.enableUpdate = true;
        } else {
            $scope.enableUpdate = false;
        }
        if (vm.imp.impStatus == "READY_FOR_QA") {
            $scope.enableUpdate = false;
        }



        if (getUserData("userRole") == "Lead" && vm.imp.impStatus == "READY_FOR_QA" &&
            implementationPlanStatus().indexOf($scope.planStausData) <= implementationPlanStatus().indexOf('DEPLOYED_IN_PRE_PRODUCTION') &&
            vm.imp.impDesc != undefined &&
            vm.imp.devLocation == $scope.impDevLocationOldData &&
            vm.imp.devId == $scope.impDevIdOldData &&
            vm.imp.peerReviewers == $scope.imppeerReviewersOldData &&
            vm.imp.devContact == $scope.impdevContactOldData &&
            vm.imp.impDesc != $scope.impDescritonOldData) {
            $scope.enableUpdate = true;
        }
    }, true)
    $scope.confirmReadyForQA = function (ev, data) {

        try {
            var confirm = $mdDialog.confirm()
                .title('Are you sure?')
                .textContent('Please make sure you are done with the integration testing')
                .ariaLabel('Ready for QA')
                .targetEvent(ev)
                .ok('Proceed')
                .cancel('Cancel');

            $mdDialog.show(confirm).then(function () {
                $scope.readyForQA(data)
            }, function () {

            });
        } catch (err) { }
    }
    $scope.readyForQA = function (data) {
        if (fImplementationPlanValidate.validateImpFields(data)) {
            if (vm.workspaceList.length == 0) {
                Toaster.sayWarning("Checkout segments")
                return
            }
            // if (!angular.equals(vm.updatedDBData, data)) {
            //     Toaster.sayWarning("Update before setting implementation as Ready for QA")
            //     return;
            // }
            vm.loader.readyForQA = true;
            $scope.saveStatusAndSubmitChange(data, function () {
                APIFactory.setReadyForQA({
                    "implId": $stateParams.impId
                }, function (response) {
                    vm.loader.readyForQA = false;
                    if (response.status) {
                        Toaster.saySuccess("Implementation is Ready for QA")
                        $timeout(function () {
                            //    $state.go($rootScope.previousState);
                            if ($rootScope.previousState != undefined) {
                                $state.go($rootScope.previousState)
                            } else {
                                if ($stateParams.redirect || getUserData("userRole") == "Developer") {
                                    $state.go("app.impl")
                                } else {
                                    $state.go("app.impPlan")
                                }
                            }

                        }, 1000)
                    } else {
                        Toaster.sayError(response.errorMessage);
                        vm.loader.readyForQA = false;
                    }
                })
            })
        }
    }




    vm.developerLocation = [{
        id: 1,
        value: "Onsite"
    }, {
        id: 2,
        value: "Offshore"
    }, {
        id: 3,
        value: "On-Call"
    }]

    // Check any segments selected or Not
    $scope.checkSegments = function () {
        if (Object.keys(vm.segmentSelection.singleSelect).length > 0) {
            if (_.contains(vm.segmentSelection.singleSelect, true)) {
                return false;
            }
            return true;
        } else {
            return true;
        }
    }
    /**
     * Info Message
     */
    $scope.checkinButtonDisableInfoMesssage = function () {
        var message = 'Check-in is disabled due to one of the following reasons: ' +
            '1.There is no change in any  source artifacts. ' +
            '2.Implementation is Ready for QA status. ' +
            '3.No source artifacts checked out yet.'
        return message;
    }



    /* Git Operations - Start */

    $scope.addCommit = function (ev, implementaionId) {
        $mdDialog.show({
            controller: commitMessageCtrl,
            controllerAs: "cm",
            templateUrl: 'html/templates/commitMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "id": implementaionId
            }
        })
            .then(function (answer) {

            }, function () {

            });

        function commitMessageCtrl($scope, id) {
            var cm = this;
            cm.impId = id;
            cm.showMessageError = false
            var dataObj = []
            $scope.date = new Date();
            for (index in vm.segmentSelection.singleSelect) {
                if (vm.segmentSelection.singleSelect[index] && vm.workspaceList[index]) {
                    dataObj.push(vm.workspaceList[index])
                }
            }
            cm.showCommitBtn = true;
            //ZTPFM-2135 Default commit message
            var commitDate = $filter("formattedDateTimeWithoutSeconds")($scope.date)
            cm.commitMessage = id + "   " + vm.imp.impDesc + "   " + commitDate;

            APIFactory.getGitRevision({
                implId: implementaionId
            }, dataObj, function (response) {
                if (response) {
                    cm.showCommitBtn = false;
                    if (response.status) {
                        cm.revisionMsg = response.data;
                    } else {
                        Toaster.sayError(response.errorMessage)
                    }
                }
            })
            $scope.proceedCommit = function () {
                if (!cm.commitMessage) {
                    cm.showMessageError = true
                    return
                } else {
                    cm.showMessageError = false
                }
                var paramObj = {
                    "implId": id,
                    "commitMessage": cm.commitMessage.replace(/["“”<>']+/g, '')
                }
                var dataObj = []
                for (index in vm.segmentSelection.singleSelect) {
                    if (vm.segmentSelection.singleSelect[index] && vm.workspaceList[index]) {
                        dataObj.push(vm.workspaceList[index])
                    }
                }
                APIFactory.developerCommit(paramObj, dataObj, function (response) {
                    if (response.status) {

                        vm.segmentSelection.selectAll = {}
                        vm.segmentSelection.singleSelect = {}
                        var errMsg = ""
                        _.each(response.data, function (value, key) {
                            if (value != "") {
                                errMsg += key + " - " + value + "<br/>"
                            }
                        })
                        if (errMsg != "") {
                            Toaster.sayError(errMsg)
                            localStorage.setItem(key, "true");
                            vm.commitdataList = JSON.parse(localStorage.getItem(key));
                        } else {
                            if (response.metaData == "") {
                                Toaster.saySuccess("Commit done successfully")
                                localStorage.setItem(key, "false");
                                vm.commitdataList = JSON.parse(localStorage.getItem(key));
                            }
                        }
                        if (response.metaData == "Nothing-to-Commit") {
                            Toaster.saySuccess("Commit done successfully. There is no change in any source artifacts to check-in.")
                            localStorage.setItem(key, "true");
                            vm.commitdataList = JSON.parse(localStorage.getItem(key));
                        }
                        vm.loadSegmentMappingList()
                        $mdDialog.hide();
                    } else {
                        Toaster.sayError(response.errorMessage)
                    }
                })
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }

    }

    $scope.getLatest = function (type) {
        APIFactory.developerGetLatest({
            "implId": vm.imp.id,
            "type": type
        }, {}, function (response) {
            if (response.status) {
                var failedActions = []
                _.each(response.data, function (value, key) {
                    if (value != "true") {
                        failedActions.push(value)
                    }
                })
                if (failedActions.length == 0) {
                    $scope.loadImpData()
                    Toaster.saySuccess("Latest code updated successfully")
                    return;
                }
                // Toaster.sayError("Get latest failed while " + failedActions.join(", "))
                Toaster.sayError(failedActions[0])
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    $scope.checkIn = function () {
        $scope.checkinLoader = true;
        APIFactory.developerCheckin({
            "implId": vm.imp.id
        }, {}, function (response) {
            if (response.status) {
                validateCheckInResult(response.data)
                $scope.checkinLoader = false;
            } else {
                Toaster.sayError(response.errorMessage)
                $scope.checkinLoader = false;
                localStorage.setItem(key, "false");
                vm.commitdataList = JSON.parse(localStorage.getItem(key));
            }
        })
    }

    function validateCheckInResult(data) {
        var failedActions = []
        //var warningActions = []
        //        var firststr;
        _.each(data, function (obj, key) {
            if (obj.CHECK_IN != "true" && obj.CHECK_IN_ERROR_MESSAGE) {
                failedActions.push(key + " - Check In <br/> " + obj.CHECK_IN_ERROR_MESSAGE)
            }
            if (obj.DEVL_WORKSPACE != "true" && obj.DEVL_WORKSPACE_ERROR_MESSAGE) {
                failedActions.push(key + " - Devl Workspace - " + obj.DEVL_WORKSPACE_ERROR_MESSAGE)
            }
            if (obj.EXPORT_WORKSPACE != "true" && obj.EXPORT_WORKSPACE_ERROR_MESSAGE) {
                failedActions.push(key + " - Export Workspace - " + obj.EXPORT_WORKSPACE_ERROR_MESSAGE)
            }
            //            if (obj.GIT_REVISION != "true" && obj.GIT_REVISION_ERROR_MESSAGE) {
            //                var str = obj.GIT_REVISION_ERROR_MESSAGE;
            //                var index = str.indexOf(':',str.indexOf(':') + 1);
            //                 firststr = str.substr(0,index);
            //                var secondstr = str.substr(index+1);
            //                warningActions.push(secondstr)
            //            }

        })
        $scope.loadImpData()
        //        if(warningActions.length > 0){
        //            Toaster.sayStatus(firststr + warningActions.join("<br/><br/>"))
        //        }
        if (failedActions.length == 0) {
            Toaster.saySuccess("Check-in Success")
            $scope.checkinLoader = false;
            localStorage.setItem(key, "true");
            vm.commitdataList = JSON.parse(localStorage.getItem(key));

            return;
        }
        if (failedActions.length > 0) {
            Toaster.sayError(failedActions.join("<br/><br/>"))
            $scope.checkinLoader = false;
            localStorage.setItem(key, "false");
            vm.commitdataList = JSON.parse(localStorage.getItem(key));
        }

    }

    /* Git Operations - End */


    //Segments
    /*********************************************************************************/
    /*********************************************************************************/

    var productionMetaData = {
        "pageStartCount": 0,
        "pageEndCount": 10,
        "selectedSegmentCount": 0,
        "selectedMakSegmentCount": 0,
        "totalCount": 0,
        "pageNo": 0,
        "pageCount": 10,
        "segSearchProcess": true,
        "selectedData": [],
        "selectedMakData": [],
        "searchText": "",
        "metaData": null,
        "searchList": [],
        "segmentsList": []
    }
    var nonProductionMetaData = {
        "pageStartCount": 0,
        "pageEndCount": 10,
        "selectedSegmentCount": 0,
        "selectedMakSegmentCount": 0,
        "totalCount": 0,
        "pageNo": 0,
        "pageCount": 10,
        "segSearchProcess": true,
        "selectedData": [],
        "selectedMakData": [],
        "searchText": "",
        "metaData": null,
        "searchList": [],
        "segmentsList": []
    }
    var legacyMetaData = {
        "pageStartCount": 0,
        "pageEndCount": 10,
        "selectedSegmentCount": 0,
        "selectedMakSegmentCount": 0,
        "totalCount": 0,
        "pageNo": 0,
        "pageCount": 10,
        "segSearchProcess": true,
        "selectedData": [],
        "selectedMakData": [],
        "searchText": "",
        "metaData": null,
        "searchList": [],
        "segmentsList": []
    }
    var ibmVanillaMetaData = {
        "pageStartCount": 0,
        "pageEndCount": 10,
        "selectedSegmentCount": 0,
        "selectedMakSegmentCount": 0,
        "totalCount": 0,
        "pageNo": 0,
        "pageCount": 10,
        "segSearchProcess": true,
        "selectedData": [],
        "selectedMakData": [],
        "searchText": "",
        "metaData": null,
        "searchList": [],
        "segmentsList": []
    }

    $scope.productionMetaData = productionMetaData
    $scope.nonProductionMetaData = nonProductionMetaData
    $scope.legacyMetaData = legacyMetaData
    $scope.ibmVanillaMetaData = ibmVanillaMetaData
    $scope.initPNP = function () {
        productionMetaData.searchList = [];
        productionMetaData.logprodSegmentsEvents = [];
        productionMetaData.numberToDisplay = 2;
        productionMetaData.totalCount = 0;
        nonProductionMetaData.searchList = [];
        nonProductionMetaData.totalCount = 0;
        legacyMetaData.searchList = [];
        legacyMetaData.totalCount = 0;
        ibmVanillaMetaData.searchList = [];
        ibmVanillaMetaData.totalCount = 0;
    }

    $scope.openSegments = function () {
        $scope.initPNP()

        $scope.searchText = ""
        $scope.vanillaSearchText = ""
        productionMetaData.selectedData = [];
        nonProductionMetaData.selectedData = [];
        ibmVanillaMetaData.selectedData = [];
        legacyMetaData.selectedData = [];
        productionMetaData.selectedMakData = [];
        nonProductionMetaData.selectedMakData = [];
        ibmVanillaMetaData.selectedMakData = [];
        legacyMetaData.selectedMakData = [];
        productionMetaData.searchText = "";
        nonProductionMetaData.searchText = "";
        ibmVanillaMetaData.searchText = "";
        legacyMetaData.searchText = "";
        productionMetaData.segmentsList = [];
        nonProductionMetaData.segmentsList = [];
        ibmVanillaMetaData.segmentsList = [];
        legacyMetaData.segmentsList = [];
        productionMetaData.selectedSegmentCount = 0;
        nonProductionMetaData.selectedSegmentCount = 0;
        ibmVanillaMetaData.selectedSegmentCount = 0;
        legacyMetaData.selectedSegmentCount = 0;

        productionMetaData.selectedMakSegmentCount = 0;
        nonProductionMetaData.selectedMakSegmentCount = 0;
        ibmVanillaMetaData.selectedMakSegmentCount = 0;
        legacyMetaData.selectedMakSegmentCount = 0;

        $("#segmentSearch").modal({
            backdrop: 'static',
            keyboard: true
        });
        if (vm.checkoutInProgress == false) {
            $('#segmentSearch').on('hidden', function () {
                $(this).data('modal', null);
            });
            $(".pnpTabs").css('display', 'block')
            $(".populateSCMArea").css('display', 'none')
            $(".populateReportTemplate").css('display', 'none')
            $(".checkoutReportTemplate").css('display', 'none')
            $(".checkoutSegments").css('display', 'none')
            $(".checkoutSegmentsTitle").css('display', 'none')
            $(".makCheckoutSegments").css('display', 'none')
            $(".makCheckoutSegmentsTitle").css('display', 'none')
            $(".chkCheckout").css('display', 'none')
            $(".makCheckoutSegmentsLoop").css('display', 'none')
            $(".makCheckoutSegmentsLoop1").css('display', 'none')
            $(".makCheckoutSegmentsLoop2").css('display', 'none')
            $(".makCheckoutSegmentsTitle1").css('display', 'none')
            $(".makCheckoutSegmentsTitle2").css('display', 'none')
            $(".makCheckoutSegmentsTitle3").css('display', 'none')
            $(".populateMakCheckoutSegments").css('display', 'none')
            $(".populateChkMakSelected").css('display', 'none')
            $(".populateMakCheckoutSegmentsTitle").css('display', 'none')
            $(".populateSummaryTemplate").css('display', 'none')
            $(".populateSummaryReport").css('display', 'none')
            $(".chooseSCMArea").css('display', 'none')


            $(".searchSegments").css('display', 'block')
            $(".chkSelected").css('display', 'block')
            $(".chkMakSelected").css('display', 'none')
            $(".chkMakSelected1").css('display', 'none')
            $(".chkMakSelected2").css('display', 'none')
            $(".chkMakSelected3").css('display', 'none')
            $timeout(function () {
                document.getElementById('srchtext').focus();
            }, 1000);
        } else {
            $(".searchSegments").css('display', 'none')
            $(".checkoutSegments").css('display', 'none')
            $(".makCheckoutSegments").css('display', 'none')
            $(".checkoutSegmentsTitle").css('display', 'none')
            $(".makCheckoutSegmentsTitle").css('display', 'none')
            $(".makCheckoutSegmentsLoop").css('display', 'none')
            $(".makCheckoutSegmentsLoop1").css('display', 'none')
            $(".makCheckoutSegmentsLoop2").css('display', 'none')
            $(".makCheckoutSegmentsTitle1").css('display', 'none')
            $(".makCheckoutSegmentsTitle2").css('display', 'none')
            $(".makCheckoutSegmentsTitle3").css('display', 'none')
            $(".chkSelected").css('display', 'none')
            $(".chkMakSelected").css('display', 'none')
            $(".chkMakSelected1").css('display', 'none')
            $(".chkMakSelected2").css('display', 'none')
            $(".chkMakSelected3").css('display', 'none')
            $(".populateMakCheckoutSegments").css('display', 'none')
            $(".populateChkMakSelected").css('display', 'none')
            $(".populateMakCheckoutSegmentsTitle").css('display', 'none')
            $(".populateSummaryTemplate").css('display', 'none')
            $(".populateSummaryReport").css('display', 'none')
            $(".chooseSCMArea").css('display', 'none')
        }
    }

    $scope.searchSegment = function (searchText, pnp, pageCount, cbk) {
        var params = {
            implId: vm.imp.id,
            fileName: $.trim(searchText).replace("*", ""),
            offset: pageCount,
            limit: 10,
            flag: pnp
        }
        APIFactory.searchFile(params, function (response) {
            cbk(response)
        })
    }
    // var prod = ["productionMetaData"];
    var PNP = ["productionMetaData", "nonProductionMetaData", "legacyMetaData"]
    // var PNP = ["productionMetaData"]
    vm.showCheckoutLoading = false
    $scope.searchSegmentPNP = function (searchText) {
        $scope.initPNP()
        if ($.trim(searchText) == "") {
            Toaster.sayWarning("Segment name cannot be empty");
            return false;
        }
        if ($.trim(searchText).length <= 1) {
            Toaster.sayWarning("Minimum two characters required");
            return false;
        }

        // var defer = $q.defer();
        var pnpErrorList = []
        vm.showCheckoutLoading = true
        $q.when()
            .then(function () {
                var deferred = $q.defer();
                var searchStatus = 0;
                angular.forEach(PNP, function (repo) {
                    $scope[repo].pageCount = 0
                    $scope[repo].pageNo = 0;
                    // var prodFlag = (repo == "productionMetaData") ? "PROD" : "NONPROD"
                    var prodFlag;
                    if (repo == "productionMetaData") {
                        prodFlag = "COMMON"
                    } else if (repo == "nonProductionMetaData") {
                        prodFlag = "NONPROD"
                    } else if (repo == "legacyMetaData") {
                        prodFlag = "PENDING"
                    }

                    $scope.searchSegment(searchText, prodFlag, $scope[repo].pageCount, function (response) {
                        searchStatus++;
                        $('#srchtext').focus()
                        if (response.status) {
                            if (response.data && response.data.length > 0) {
                                $scope[repo].segSearchProcess = true;
                                $scope[repo].pageStartCount = 1;
                                $scope[repo].pageEndCount = $scope[repo].pageCount;
                                $scope[repo].totalCount = response.data.length;
                                var searchedList = response.data;
                                // if(prodFlag === "PROD"){
                                //     prodFlag = "PENDING"
                                // }
                                if (prodFlag === "COMMON") {
                                    // Check whether the file is already checked out and set the "isCheckout" status
                                    var workspaceList = _.groupBy(vm.workspaceList, "fileName");
                                    var fileSearchList = [];
                                    for (searchedIndex in searchedList) {
                                        var searchedFileObj = searchedList[searchedIndex];
                                        var workspaceFileObj = workspaceList[searchedFileObj.fileName];
                                        var branchList = [];
                                        var isCheckedout = false;
                                        for (searchedFileObjBranchIndex in searchedFileObj.branch) {
                                            var branch = searchedFileObj.branch[searchedFileObjBranchIndex];
                                            for (workspaceFileObjIndex in workspaceFileObj) {
                                                if (!branch['isCheckedout']) {
                                                    for (workspaceFileObjBranchIndex in workspaceFileObj[workspaceFileObjIndex].branch) {
                                                        if (branch.targetSystem.split("_")[1] === workspaceFileObj[workspaceFileObjIndex].branch[workspaceFileObjBranchIndex].targetSystem.toLowerCase() && branch.funcArea == workspaceFileObj[workspaceFileObjIndex].branch[workspaceFileObjBranchIndex].funcArea && branch.refStatus == workspaceFileObj[workspaceFileObjIndex].branch[workspaceFileObjBranchIndex].refStatus) { // Removing Filter due to repo separation changes
                                                            branch['isCheckedout'] = true;
                                                            break;
                                                        } else {
                                                            branch['isCheckedout'] = false;
                                                        }
                                                    }
                                                }
                                            }
                                            branchList.push(branch);
                                        }
                                        searchedFileObj.branch = branchList;
                                        fileSearchList.push(searchedFileObj);
                                    }
                                    //--
                                    $scope[repo].searchList = fileSearchList;
                                    $scope[repo].metaData = response.metaData;
                                    $scope[repo].logprodSegmentsEvents = $scope[repo].searchList
                                } else if (prodFlag === "PENDING") {
                                    // Check whether the file is already checked out and set the "isCheckout" status
                                    var workspaceList = _.groupBy(vm.workspaceList, "fileName");
                                    var fileSearchList = [];
                                    for (searchedIndex in searchedList) {
                                        var searchedFileObj = searchedList[searchedIndex];
                                        var workspaceFileObj = workspaceList[searchedFileObj.fileName];
                                        var branchList = [];
                                        // searchedFileObj.prodFlag = "PENDING";
                                        var isCheckedout = false;
                                        for (searchedFileObjBranchIndex in searchedFileObj.branch) {
                                            var branch = searchedFileObj.branch[searchedFileObjBranchIndex];
                                            for (workspaceFileObjIndex in workspaceFileObj) {
                                                if (!branch['isCheckedout']) {
                                                    for (workspaceFileObjBranchIndex in workspaceFileObj[workspaceFileObjIndex].branch) {
                                                        if (branch.targetSystem.split("_")[1] === workspaceFileObj[workspaceFileObjIndex].branch[workspaceFileObjBranchIndex].targetSystem.toLowerCase() && branch.funcArea == workspaceFileObj[workspaceFileObjIndex].branch[workspaceFileObjBranchIndex].funcArea && branch.refStatus == workspaceFileObj[workspaceFileObjIndex].branch[workspaceFileObjBranchIndex].refStatus) {
                                                            branch['isCheckedout'] = true;
                                                            break;
                                                        } else {
                                                            branch['isCheckedout'] = false;
                                                        }
                                                    }
                                                }
                                            }
                                            branchList.push(branch);
                                        }
                                        searchedFileObj.branch = branchList;
                                        fileSearchList.push(searchedFileObj);
                                    }
                                    //--
                                    $scope[repo].searchList = fileSearchList;

                                } else {
                                    $scope[repo].searchList = searchedList;
                                    $scope[repo].searchList = _.filter($scope[repo].searchList, function (slObj) {
                                        return slObj.additionalInfo.planId != vm.imp.planId.id
                                    })
                                }

                            } else {
                                $scope[repo].segSearchProcess = false;
                                $scope[repo].searchList = [];
                                if (prodFlag === "PROD") {
                                    $scope[repo].metaData = null;
                                }
                                //                              Toaster.sayError("Source artifact not found in any of the "+prodFlag+" libraries");
                            }
                        } else {
                            pnpErrorList.push(response.errorMessage)
                        }
                    })
                })
                setInterval(function () {
                    if (searchStatus == 3) {
                        deferred.resolve();
                    }
                }, 100);
                return deferred.promise;
            })
            .then(function () {
                vm.showCheckoutLoading = false
                $timeout(function () {
                    _.each(PNP, function (type, index) {
                        _.each($scope[type].selectedData, function (sObj) {
                            if (index == 0) { // Prod
                                $(".class_PROD_" + sObj.fileNameWithHash).addClass('ulHighligt')
                            } else if (index == 2) {
                                $(".class_PENDING_" + sObj.fileNameWithHash).addClass('ulHighligt')
                            } else { // NonProd
                                $(".class_NONPROD_" + sObj.fileNameWithHash + "_" + sObj.additionalInfo.planId).addClass('ulHighligt')
                            }
                        })
                    })
                }, 100)
                $timeout(function () {
                    _.each(PNP, function (type, index) {
                        _.each($scope[type].selectedMakData, function (sObj) {
                            if (index == 0) { // Prod
                                $(".class_PROD_" + sObj.fileNameWithHash).addClass('ulHighligt')
                            } else if (index == 2) {
                                $(".class_PENDING_" + sObj.fileNameWithHash).addClass('ulHighligt')
                            } else { // NonProd
                                $(".class_NONPROD_" + sObj.fileNameWithHash + "_" + sObj.additionalInfo.planId).addClass('ulHighligt')
                            }
                        })
                    })
                }, 100)
                pnpErrorList = _.compact(pnpErrorList)
                if (pnpErrorList.length > 0) {
                    // Right now, not error message is returned from the backend
                    // Toaster.sayError(pnpErrorList.join(","))
                }
            });
    }

    $scope.loadMore = function () {
        var repo = "productionMetaData";
        if ($scope[repo].logprodSegmentsEvents) {
            if ($scope[repo].numberToDisplay + 5 < $scope[repo].logprodSegmentsEvents.length) {
                $scope[repo].numberToDisplay += 5;
            } else {
                $scope[repo].numberToDisplay = $scope[repo].logprodSegmentsEvents.length;
            }

        }

    };

    $scope.checkoutPlanShowPopup = function (segmentObj) {
        if (segmentObj.additionalInfo.isPlanDeactivatedInProd) {
            vm.warningMessage = segmentObj.additionalInfo.isPlanDeactivatedInProd
            $('#warningDeactivateCheckoutPopup').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
    }

    $(".warnPlanCheckoutNo").click(function () {
        selectedRow = ".class_NONPROD_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
        if ($(selectedRow).hasClass("ulHighligt"))
            $scope.selectSegmentNonProd(lSegmentObj)
        $scope.$apply()
    })

    $(".warnPlanCheckoutYes").click(function () {
        validate_non_prod_deactivate_segment = true
        selectedRow = ".class_NONPROD_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
        if (!$(selectedRow).hasClass("ulHighligt"))
            $scope.selectSegmentNonProd(lSegmentObj)
    })

    /// main segment selection.

    $scope.selectSegmentPNP = function (data) {
        try {
            var repo;
            var selectedRow;
            var isLegacy = false;
            var legacySegments = _.filter(data.branch, function (elem) {
                return elem.refStatus == "Pending";
            })
            if (legacySegments.length > 0) {
                isLegacy = true;
            }
            if (data.hasOwnProperty("prodFlag") && (data.prodFlag == "PROD" && !isLegacy) || (data.prodFlag == "PROD" && isLegacy)) {
                prodFlag = "PROD"
                repo = PNP[0]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

                // Check for already selected segment between prod and nonprod
                var selectedListObj = _.filter(angular.copy($scope["nonProductionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeSegment(selectedListObj[0])
                }

                // Check for already selected segment between legacy and prod
                var legacySelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (legacySelectedListObj.length > 0) {
                    $scope.removeSegment(legacySelectedListObj[0])
                }

                // Check for already selected segment within prod itself
                var refStatus = []
                var prodSelectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    refStatus = _.pluck(data.branch, "refStatus")
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                    var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                    if ($(preSelectedRow).hasClass('ulHighligt')) {
                        $(preSelectedRow).removeClass('ulHighligt');
                    }
                    $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                        "fileName": prodSelectedListObj[0].fileName,
                        "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                    });
                }
            } else if (data.hasOwnProperty("prodFlag") && data.prodFlag != "NONPROD" && isLegacy) {
                prodFlag = "PENDING"
                repo = PNP[2]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

                // Check for already selected segment between legacy and nonprod
                var selectedListObj = _.filter(angular.copy($scope["nonProductionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeSegment(selectedListObj[0], isLegacy)
                }

                // Check for already selected segment between legacy and prod
                var selectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeSegment(selectedListObj[0], isLegacy)
                }

                // Check for already selected segment within legacy itself
                var refStatus = []
                var prodSelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    refStatus = _.pluck(data.branch, "refStatus")
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                    var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                    if ($(preSelectedRow).hasClass('ulHighligt')) {
                        $(preSelectedRow).removeClass('ulHighligt');
                    }
                    $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                        "fileName": prodSelectedListObj[0].fileName,
                        "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                    });
                }
            } else {
                prodFlag = "NONPROD"
                repo = PNP[1]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash + "_" + data.additionalInfo.planId
            }

            var arrayData = [];

            if ($(selectedRow).hasClass('ulHighligt') || $(selectedRow).hasClass('ulHighligt1')) {
                $(selectedRow).removeClass('ulHighligt');
                $(selectedRow).removeClass('ulHighligt1');
                $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                    "fileName": data.fileName,
                    "fileNameWithHash": data.fileNameWithHash
                });
                $scope[repo].selectedMakData = _.reject($scope[repo].selectedMakData, {
                    "fileName": data.fileName,
                    "fileNameWithHash": data.fileNameWithHash
                });
                $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
            } else {
                if ($(selectedRow).length != 0) {
                    if (prodFlag === "PROD" && vm.workspaceList.length) {
                        if (_.pluck(vm.workspaceList, "fileName").indexOf(data.fileName) >= 0) {
                            var checkoutList = _.groupBy(vm.workspaceList, "fileName");
                            var ckFile = checkoutList[data.fileName];
                            var count = 0;
                            for (var j = 0; j < data.branch.length; j++) {
                                var branch = data.branch[j];
                                for (var k = 0; k < ckFile.length; k++) {
                                    var checkedOutBranch = _.pluck(ckFile[k].branch, "targetSystem")
                                    // checkedOutBranch = checkedOutBranch.map(ts => ts.toLowerCase())
                                    for (chkB in checkedOutBranch) {
                                        checkedOutBranch[chkB] = checkedOutBranch[chkB].toLowerCase()
                                    }
                                    if (checkedOutBranch.indexOf(branch.targetSystem.split("_")[1]) > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count == data.branch.length) {
                                Toaster.sayError("Segments already selected or checked out");
                                return
                            }
                        }
                    }

                    //If branch length and branch which are not selected are not same
                    // Third condition check for single branch
                    if (prodFlag === "PROD" && (data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                        "isCheckoutAllowed": true
                    }).length || data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                    }).length ||
                        (data.branch.length === _.where(data.branch, {
                            "isCheckedout": true,
                            "isCheckoutAllowed": true
                        }).length + _.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length) && (_.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length > 1))) {
                        Toaster.sayError("Please choose some systems");
                        return;
                    }

                    //Legacy
                    if (prodFlag === "PENDING" && vm.workspaceList.length) {
                        if (_.pluck(vm.workspaceList, "fileName").indexOf(data.fileName) >= 0) {
                            var checkoutList = _.groupBy(vm.workspaceList, "fileName");
                            var ckFile = checkoutList[data.fileName];
                            var count = 0;
                            for (var j = 0; j < data.branch.length; j++) {
                                var branch = data.branch[j];
                                for (var k = 0; k < ckFile.length; k++) {
                                    var checkedOutBranch = _.pluck(ckFile[k].branch, "targetSystem")
                                    // checkedOutBranch = checkedOutBranch.map(ts => ts.toLowerCase())
                                    for (chkB in checkedOutBranch) {
                                        checkedOutBranch[chkB] = checkedOutBranch[chkB].toLowerCase()
                                    }
                                    if (checkedOutBranch.indexOf(branch.targetSystem.split("_")[1]) > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count == data.branch.length) {
                                Toaster.sayError("Segments already selected or checked out");
                                return
                            }
                        }
                    }
                    //Legacy
                    //If branch length and branch which are not selected are not same
                    // Third condition check for single branch
                    if (prodFlag === "PENDING" && (data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                        "isCheckoutAllowed": true
                    }).length ||
                        data.branch.length === _.where(data.branch, {
                            "isCheckedout": true,
                            "isCheckoutAllowed": true
                        }).length + _.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length) && (_.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length > 1)) {
                        Toaster.sayError("Please choose some systems");
                        return;
                    }
                    $(selectedRow).addClass('ulHighligt');
                    $scope[repo].selectedData.push(data);
                }

            }
            $scope[repo].selectedSegmentCount = $scope[repo].selectedData.length;
            try {
                $scope.$digest()
            } catch (err) { }
        } catch (err) {
            console.log(err)
        }
    }


    $scope.selectSegmentPNPBack = function (data) {
        try {
            var repo;
            var selectedRow;
            var isLegacy = false;
            var legacySegments = _.filter(data.branch, function (elem) {
                return elem.refStatus == "Pending";
            })
            if (legacySegments.length > 0) {
                isLegacy = true;
            }
            if (data.hasOwnProperty("prodFlag") && (data.prodFlag == "PROD" && !isLegacy) || (data.prodFlag == "PROD" && isLegacy)) {
                prodFlag = "PROD"
                repo = PNP[0]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

                // Check for already selected segment between prod and nonprod
                var selectedListObj = _.filter(angular.copy($scope["nonProductionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeSegment(selectedListObj[0])
                }

                // Check for already selected segment between legacy and prod
                var legacySelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (legacySelectedListObj.length > 0) {
                    $scope.removeSegment(legacySelectedListObj[0])
                }

                // Check for already selected segment within prod itself
                var refStatus = []
                var prodSelectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    refStatus = _.pluck(data.branch, "refStatus")
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                    var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                    if ($(preSelectedRow).hasClass('ulHighligt')) {
                        $(preSelectedRow).removeClass('ulHighligt');
                    }
                    $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                        "fileName": prodSelectedListObj[0].fileName,
                        "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                    });
                }
            } else if (data.hasOwnProperty("prodFlag") && data.prodFlag != "NONPROD" && isLegacy) {
                prodFlag = "PENDING"
                repo = PNP[2]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

                // Check for already selected segment between legacy and nonprod
                var selectedListObj = _.filter(angular.copy($scope["nonProductionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeSegment(selectedListObj[0], isLegacy)
                }

                // Check for already selected segment between legacy and prod
                var selectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeSegment(selectedListObj[0], isLegacy)
                }

                // Check for already selected segment within legacy itself
                var refStatus = []
                var prodSelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    refStatus = _.pluck(data.branch, "refStatus")
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                    var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                    if ($(preSelectedRow).hasClass('ulHighligt')) {
                        $(preSelectedRow).removeClass('ulHighligt');
                    }
                    $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                        "fileName": prodSelectedListObj[0].fileName,
                        "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                    });
                }
            } else {
                prodFlag = "NONPROD"
                repo = PNP[1]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash + "_" + data.additionalInfo.planId
            }

            var arrayData = [];

            if ($(selectedRow).hasClass('ulHighligt') || $(selectedRow).hasClass('ulHighligt1')) {
                $(selectedRow).removeClass('ulHighligt');
                $(selectedRow).removeClass('ulHighligt1');
                $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                    "fileName": data.fileName,
                    "fileNameWithHash": data.fileNameWithHash
                });
                $scope[repo].selectedMakData = _.reject($scope[repo].selectedMakData, {
                    "fileName": data.fileName,
                    "fileNameWithHash": data.fileNameWithHash
                });
                $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
            } else {
                if ($(selectedRow).length != 0) {
                    if (prodFlag === "PROD" && vm.workspaceList.length) {
                        if (_.pluck(vm.workspaceList, "fileName").indexOf(data.fileName) >= 0) {
                            var checkoutList = _.groupBy(vm.workspaceList, "fileName");
                            var ckFile = checkoutList[data.fileName];
                            var count = 0;
                            for (var j = 0; j < data.branch.length; j++) {
                                var branch = data.branch[j];
                                for (var k = 0; k < ckFile.length; k++) {
                                    var checkedOutBranch = _.pluck(ckFile[k].branch, "targetSystem")
                                    // checkedOutBranch = checkedOutBranch.map(ts => ts.toLowerCase())
                                    for (chkB in checkedOutBranch) {
                                        checkedOutBranch[chkB] = checkedOutBranch[chkB].toLowerCase()
                                    }
                                    if (checkedOutBranch.indexOf(branch.targetSystem.split("_")[1]) > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count == data.branch.length) {
                                Toaster.sayError("Segments already selected or checked out");
                                return
                            }
                        }
                    }

                    //If branch length and branch which are not selected are not same
                    // Third condition check for single branch
                    if (prodFlag === "PROD" && (data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                        "isCheckoutAllowed": true
                    }).length || data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                    }).length ||
                        (data.branch.length === _.where(data.branch, {
                            "isCheckedout": true,
                            "isCheckoutAllowed": true
                        }).length + _.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length) && (_.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length > 1))) {
                        Toaster.sayError("Please choose some systems");
                        return;
                    }

                    //Legacy
                    if (prodFlag === "PENDING" && vm.workspaceList.length) {
                        if (_.pluck(vm.workspaceList, "fileName").indexOf(data.fileName) >= 0) {
                            var checkoutList = _.groupBy(vm.workspaceList, "fileName");
                            var ckFile = checkoutList[data.fileName];
                            var count = 0;
                            for (var j = 0; j < data.branch.length; j++) {
                                var branch = data.branch[j];
                                for (var k = 0; k < ckFile.length; k++) {
                                    var checkedOutBranch = _.pluck(ckFile[k].branch, "targetSystem")
                                    // checkedOutBranch = checkedOutBranch.map(ts => ts.toLowerCase())
                                    for (chkB in checkedOutBranch) {
                                        checkedOutBranch[chkB] = checkedOutBranch[chkB].toLowerCase()
                                    }
                                    if (checkedOutBranch.indexOf(branch.targetSystem.split("_")[1]) > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count == data.branch.length) {
                                Toaster.sayError("Segments already selected or checked out");
                                return
                            }
                        }
                    }
                    //Legacy
                    //If branch length and branch which are not selected are not same
                    // Third condition check for single branch
                    if (prodFlag === "PENDING" && (data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                        "isCheckoutAllowed": true
                    }).length ||
                        data.branch.length === _.where(data.branch, {
                            "isCheckedout": true,
                            "isCheckoutAllowed": true
                        }).length + _.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length) && (_.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length > 1)) {
                        Toaster.sayError("Please choose some systems");
                        return;
                    }
                    $(selectedRow).addClass('ulHighligt');
                    $scope[repo].selectedData.push(data);
                }

            }
            $scope[repo].selectedSegmentCount = $scope[repo].selectedData.length;

            if (vm.flagTrueFalse) {
                $(selectedRow).addClass('ulHighligt1');

                _.each($scope[repo].logprodSegmentsEvents, function (Obj) {
                    _.each($rootScope.segmentData, function (dataObj) {
                        if (Obj.fileNameWithHash == dataObj.fileNameWithHash) {
                            _.each(Obj.branch, function (branc) {
                                try {
                                    _.each(dataObj.branch, function (dataBranc) {
                                        if (branc.targetSystem == dataBranc.targetSystem) {
                                            branc.isBranchSelected = true
                                            throw new Error();
                                        } else {
                                            branc.isBranchSelected = false
                                        }
                                    })
                                } catch (e) { }
                            })
                        }
                    })
                })

                $scope[repo].selectedMakData = $rootScope.segmentData

                $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
            }
            if (repo == "productionMetaData") {
                prodFlag = "PROD"
                if (data.prodFlag != prodFlag) {
                    $scope[repo].selectedMakData = []
                    $scope[repo].selectedMakData.length = 0;
                    $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
                }
            } else if (repo == "nonproductionMetaData") {
                prodFlag = "NONPROD"
                if (data.prodFlag != prodFlag) {
                    $scope[repo].selectedMakData = []
                    $scope[repo].selectedMakData.length = 0;
                    $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
                }
            } else {
                prodFlag = "LEGACY"
                if (data.prodFlag != prodFlag) {
                    $scope[repo].selectedMakData = []
                    $scope[repo].selectedMakData.length = 0;
                    $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
                }
            }
            try {
                $scope.$digest()
            } catch (err) { }
        } catch (err) {
            console.log(err)
        }
    }

    /// mak file segment selection.
    $scope.selectMakSegmentPNP = function (data) {
        try {
            var repo;
            var selectedRow;
            var isLegacy = false;
            var legacySegments = _.filter(data.branch, function (elem) {
                return elem.refStatus == "Pending";
            })
            if (legacySegments.length > 0) {
                isLegacy = true;
            }
            if (data.hasOwnProperty("prodFlag") && (data.prodFlag == "PROD" && !isLegacy) || (data.prodFlag == "PROD" && isLegacy)) {
                prodFlag = "PROD"
                repo = PNP[0]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

                // Check for already selected segment between prod and nonprod
                var selectedListObj = _.filter(angular.copy($scope["nonProductionMetaData"].selectedMakData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeMakSegment(selectedListObj[0])
                }

                // Check for already selected segment between legacy and prod
                var legacySelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedMakData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (legacySelectedListObj.length > 0) {
                    $scope.removeMakSegment(legacySelectedListObj[0])
                }

                // Check for already selected segment within prod itself
                var refStatus = []
                var prodSelectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedMakData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    refStatus = _.pluck(data.branch, "refStatus")
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                    var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                    if ($(preSelectedRow).hasClass('ulHighligt1')) {
                        $(preSelectedRow).removeClass('ulHighligt1');
                    }
                    $scope[repo].selectedMakData = _.reject($scope[repo].selectedMakData, {
                        "fileName": prodSelectedListObj[0].fileName,
                        "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                    });
                }
            } else if (data.hasOwnProperty("prodFlag") && data.prodFlag != "NONPROD" && isLegacy) {
                prodFlag = "PENDING"
                repo = PNP[2]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

                // Check for already selected segment between legacy and nonprod
                var selectedListObj = _.filter(angular.copy($scope["nonProductionMetaData"].selectedMakData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeMakSegment(selectedListObj[0], isLegacy)
                }

                // Check for already selected segment between legacy and prod
                var selectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedMakData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (selectedListObj.length > 0) {
                    $scope.removeMakSegment(selectedListObj[0], isLegacy)
                }

                // Check for already selected segment within legacy itself
                var refStatus = []
                var prodSelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedMakData), function (nObj) {
                    var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                    var branches_selected = _.pluck(data.branch, "targetSystem")
                    var branches_matched = _.intersection(branches_iteration, branches_selected)
                    refStatus = _.pluck(data.branch, "refStatus")
                    return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
                })
                if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                    var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                    if ($(preSelectedRow).hasClass('ulHighligt1')) {
                        $(preSelectedRow).removeClass('ulHighligt1');
                    }
                    $scope[repo].selectedMakData = _.reject($scope[repo].selectedMakData, {
                        "fileName": prodSelectedListObj[0].fileName,
                        "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                    });
                }
            } else {
                prodFlag = "NONPROD"
                repo = PNP[1]
                selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash + "_" + data.additionalInfo.planId
            }

            var arrayData = [];
            if ($(selectedRow).hasClass('ulHighligt1')) {
                $(selectedRow).removeClass('ulHighligt1');
                $scope[repo].selectedMakData = _.reject($scope[repo].selectedMakData, {
                    "fileName": data.fileName,
                    "fileNameWithHash": data.fileNameWithHash
                });
            } else {
                if ($(selectedRow).length != 0) {
                    if (prodFlag === "PROD" && vm.workspaceList.length) {
                        if (_.pluck(vm.workspaceList, "fileName").indexOf(data.fileName) >= 0) {
                            var checkoutList = _.groupBy(vm.workspaceList, "fileName");
                            var ckFile = checkoutList[data.fileName];
                            var count = 0;
                            for (var j = 0; j < data.branch.length; j++) {
                                var branch = data.branch[j];
                                for (var k = 0; k < ckFile.length; k++) {
                                    var checkedOutBranch = _.pluck(ckFile[k].branch, "targetSystem")
                                    // checkedOutBranch = checkedOutBranch.map(ts => ts.toLowerCase())
                                    for (chkB in checkedOutBranch) {
                                        checkedOutBranch[chkB] = checkedOutBranch[chkB].toLowerCase()
                                    }
                                    if (checkedOutBranch.indexOf(branch.targetSystem.split("_")[1]) > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count == data.branch.length) {
                                Toaster.sayError("Segments already selected or checked out");
                                return
                            }
                        }
                    }

                    //If branch length and branch which are not selected are not same
                    // Third condition check for single branch
                    if (prodFlag === "PROD" && (data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                        "isCheckoutAllowed": true
                    }).length || data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                    }).length ||
                        (data.branch.length === _.where(data.branch, {
                            "isCheckedout": true,
                            "isCheckoutAllowed": true
                        }).length + _.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length) && (_.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length > 1))) {
                        Toaster.sayError("Please choose some systems");
                        return;
                    }

                    //Legacy
                    if (prodFlag === "PENDING" && vm.workspaceList.length) {
                        if (_.pluck(vm.workspaceList, "fileName").indexOf(data.fileName) >= 0) {
                            var checkoutList = _.groupBy(vm.workspaceList, "fileName");
                            var ckFile = checkoutList[data.fileName];
                            var count = 0;
                            for (var j = 0; j < data.branch.length; j++) {
                                var branch = data.branch[j];
                                for (var k = 0; k < ckFile.length; k++) {
                                    var checkedOutBranch = _.pluck(ckFile[k].branch, "targetSystem")
                                    // checkedOutBranch = checkedOutBranch.map(ts => ts.toLowerCase())
                                    for (chkB in checkedOutBranch) {
                                        checkedOutBranch[chkB] = checkedOutBranch[chkB].toLowerCase()
                                    }
                                    if (checkedOutBranch.indexOf(branch.targetSystem.split("_")[1]) > 0) {
                                        count++;
                                    }
                                }
                            }
                            if (count == data.branch.length) {
                                Toaster.sayError("Segments already selected or checked out");
                                return
                            }
                        }
                    }
                    //Legacy
                    //If branch length and branch which are not selected are not same
                    // Third condition check for single branch
                    if (prodFlag === "PENDING" && (data.branch.length === _.where(data.branch, {
                        "isBranchSelected": false,
                        "isCheckoutAllowed": true
                    }).length ||
                        data.branch.length === _.where(data.branch, {
                            "isCheckedout": true,
                            "isCheckoutAllowed": true
                        }).length + _.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length) && (_.where(data.branch, {
                            "isBranchSelected": false,
                            "isCheckoutAllowed": true
                        }).length > 1)) {
                        Toaster.sayError("Please choose some systems");
                        return;
                    }
                    $(selectedRow).addClass('ulHighligt1');
                    $scope[repo].selectedMakData.push(data);
                }
            }
            $rootScope.segmentData = $scope[repo].selectedMakData
            $rootScope.segmentIBMData = $scope[repo].selectedMakData
            $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length;
            try {
                $scope.$digest()
            } catch (err) { }
        } catch (err) {
            console.log(err)
        }
    }


    var lSegmentObj, lBranchObj;
    $scope.divergenceWarning = function (segmentObj, branchObj) { // Prod selection
        lSegmentObj = segmentObj;
        lBranchObj = branchObj;

        // Online and Fallback cannot be selected at the same time
        if (branchObj.isBranchSelected && branchObj.refStatus == "Fallback") {
            var selectedOnlineExists = false
            _.each(segmentObj.branch, function (bObj) {
                if (bObj.isBranchSelected && bObj.refStatus == "Online" && !bObj.isCheckedout) {
                    selectedOnlineExists = true
                }
            })
            if (selectedOnlineExists) {
                Toaster.sayWarning("Online and Fallback segment cannot be selected at the same time");
                branchObj.isBranchSelected = false;
                return
            }
        }
        if (branchObj.isBranchSelected && branchObj.refStatus == "Online") {
            var selectedOnlineExists = false
            _.each(segmentObj.branch, function (bObj) {
                if (bObj.isBranchSelected && bObj.refStatus == "Fallback" && !bObj.isCheckedout) {
                    selectedOnlineExists = true
                }
            })
            if (selectedOnlineExists) {
                Toaster.sayWarning("Online and Fallback segment cannot be selected at the same time");
                branchObj.isBranchSelected = false;
                return
            }
        }

        if (lSegmentObj.prodFlag == "PROD") {
            selectedRow = ".class_" + lSegmentObj.prodFlag + "_" + lSegmentObj.fileNameWithHash
        } else if (lSegmentObj.prodFlag == "PENDING") {
            selectedRow = ".class_" + lSegmentObj.prodFlag + "_" + lSegmentObj.fileNameWithHash
        } else {
            selectedRow = ".class_" + lSegmentObj.prodFlag + "_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
        }

        // While unselecting the last system for a segment, do not show divergence warning
        if (_.where(segmentObj.branch, {
            "isBranchSelected": true
        }).length == 0) {
            if ($(selectedRow).hasClass("ulHighligt")) {
                $scope.selectSegmentPNP(lSegmentObj)
            }
            return
        }

        if (!branchObj.isBranchSelected) {
            $("#warnFileName").html(segmentObj.fileName)
            $("#warnSystemName").html($filter("systemFilter")(branchObj.targetSystem))

            $('#warningModal').modal({
                backdrop: 'static',
                keyboard: true
            })
        }
        if (branchObj.isBranchSelected) {
            if (!$(selectedRow).hasClass("ulHighligt")) {
                $scope.selectSegmentPNP(lSegmentObj)
            }
        }
    }


    var category_P_exists = false;
    var validate_non_prod_same_segment = false
    var validate_non_prod_deactivate_segment = false
    $scope.selectSegmentNonProd = function (data) { // Category P validation for non prod
        category_P_exists = false;
        lSegmentObj = data
        lSegmentObj.prodFlag = "NONPROD"
        var selectedRow = ".class_NONPROD_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
        if ($(selectedRow).hasClass("ulHighligt")) {
            $scope.selectSegmentPNP(data)
            return
        }
        var prodSelectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedData), function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (pObj.fileName == data.fileName) && (branches_matched.length > 0);
        })
        if (prodSelectedListObj.length > 0) {
            $scope.removeSegment(prodSelectedListObj[0])
        }

        var legacySelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedData), function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (pObj.fileName == data.fileName) && (branches_matched.length > 0);
        })
        if (legacySelectedListObj.length > 0) {
            $scope.removeSegment(legacySelectedListObj[0])
        }

        var nonProductionSelected = angular.copy($scope["nonProductionMetaData"].selectedData)
        var nonProdSelectedListObj = _.filter(nonProductionSelected, function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (pObj.fileName == data.fileName) && (branches_matched.length > 0);
        })
        if (nonProdSelectedListObj.length > 0) {
            $scope.removeSegment(nonProdSelectedListObj[0])
        }

        var alreadyCheckedOut = false
        _.each(vm.workspaceList, function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(lSegmentObj.branch, "targetSystem")
            _.each(branches_selected, function (bValue, index) {
                branches_selected[index] = bValue.split("_")[1].toUpperCase()
            })
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            var pFuncArea = _.uniq(_.pluck(pObj.branch, "funcArea"))[0]
            var lFuncArea = _.uniq(_.pluck(lSegmentObj.branch, "funcArea"))[0]
            if ((pObj.fileName == lSegmentObj.fileName) && (branches_matched.length > 0) && pFuncArea == lFuncArea) {
                alreadyCheckedOut = true
            }
        })
        if (!validate_non_prod_same_segment && alreadyCheckedOut == true && !validate_non_prod_deactivate_segment) {
            $('#warningConfirmationModal').modal({
                backdrop: 'static',
                keyboard: true
            })
            return;
        }
        validate_non_prod_same_segment = false
        validate_non_prod_deactivate_segment = false
        _.each(data.branch, function (bObj) {
            if (bObj.additionalInfo.categoryName && bObj.additionalInfo.categoryName == "P") {
                category_P_exists = true
            }
        })
        if (category_P_exists) {
            $('#categoryPModal').modal({
                backdrop: 'static',
                keyboard: true
            })
            return true;
        } else {
            $scope.selectSegmentPNP(data)
        }
    }

    //non prod mak segment
    $scope.selectMakSegmentNonProd = function (data) { // Category P validation for non prod
        category_P_exists = false;
        lSegmentObj = data
        lSegmentObj.prodFlag = "NONPROD"
        var selectedRow = ".class_NONPROD_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
        if ($(selectedRow).hasClass("ulHighligt1")) {
            $scope.selectMakSegmentPNP(data)
            return
        }
        var prodSelectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedMakData), function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (pObj.fileName == data.fileName) && (branches_matched.length > 0);
        })
        if (prodSelectedListObj.length > 0) {
            $scope.removeMakSegment(prodSelectedListObj[0])
        }

        var legacySelectedListObj = _.filter(angular.copy($scope["legacyMetaData"].selectedMakData), function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (pObj.fileName == data.fileName) && (branches_matched.length > 0);
        })
        if (legacySelectedListObj.length > 0) {
            $scope.removeMakSegment(legacySelectedListObj[0])
        }

        var nonProductionSelected = angular.copy($scope["nonProductionMetaData"].selectedMakData)
        var nonProdSelectedListObj = _.filter(nonProductionSelected, function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (pObj.fileName == data.fileName) && (branches_matched.length > 0);
        })
        if (nonProdSelectedListObj.length > 0) {
            $scope.removeMakSegment(nonProdSelectedListObj[0])
        }

        var alreadyCheckedOut = false
        _.each(vm.workspaceList, function (pObj) {
            var branches_iteration = _.pluck(pObj.branch, "targetSystem")
            var branches_selected = _.pluck(lSegmentObj.branch, "targetSystem")
            _.each(branches_selected, function (bValue, index) {
                branches_selected[index] = bValue.split("_")[1].toUpperCase()
            })
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            var pFuncArea = _.uniq(_.pluck(pObj.branch, "funcArea"))[0]
            var lFuncArea = _.uniq(_.pluck(lSegmentObj.branch, "funcArea"))[0]
            if ((pObj.fileName == lSegmentObj.fileName) && (branches_matched.length > 0) && pFuncArea == lFuncArea) {
                alreadyCheckedOut = true
            }
        })
        if (!validate_non_prod_same_segment && alreadyCheckedOut == true && !validate_non_prod_deactivate_segment) {
            $('#warningConfirmationModal').modal({
                backdrop: 'static',
                keyboard: true
            })
            return;
        }
        validate_non_prod_same_segment = false
        validate_non_prod_deactivate_segment = false
        _.each(data.branch, function (bObj) {
            if (bObj.additionalInfo.categoryName && bObj.additionalInfo.categoryName == "P") {
                category_P_exists = true
            }
        })
        if (category_P_exists) {
            $('#categoryPModal').modal({
                backdrop: 'static',
                keyboard: true
            })
            return true;
        } else {
            $scope.selectMakSegmentPNP(data)
        }
    }



    $scope.deleteFromWorkspace = function (branches) {
        var branchIds = {
            "ids": _.pluck(branches, "id")
        }
        APIFactory.deleteFile(branchIds, function (response) {
            if (response.status) {
                Toaster.saySuccess("Deleted from workspace successfully")
                vm.segmentSelection.selectAll = false;
                $scope.saveRFCLoader = false;
            } else {
                $scope.saveRFCLoader = false;
                vm.segmentSelection.selectAll = false;
                if (response.data) {
                    validateCheckInResult(response.data)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            }
            $scope.loadImpData()
            $scope.loadSegmentList()
        })
    }
    $scope.closeCheckoutDialog = function () {
        APIFactory.clearValidateMakFileCacheutFile({ "impl": $stateParams.impId }, function (response) {
            if (response.status) {
            }
        })
        $scope.productionMetaData.selectedData = [];
        $scope.productionMetaData.selectedMakData = [];
        $scope.productionMetaData.selectedSegmentCount = 0;
        $scope.productionMetaData.selectedMakSegmentCount = 0;
        $scope.productionMetaData.metaData = null;
        $scope.ibmVanillaMetaData.metaData = null;
        segmentsToPopulateSummary = [];
        vm.segmentsToPopulate = [];
        vm.selectedVanillaSegments = [];
        vm.populatedVanillaFileList = [];
        $rootScope.segmentsToPopulate = [];
        $scope.loadImpData()
    }

    $(document).ready(function () {
        $(".warnProceed").click(function () {
            if (lSegmentObj.prodFlag == "PROD") {
                selectedRow = ".class_" + lSegmentObj.prodFlag + "_" + lSegmentObj.fileNameWithHash
            } else if (lSegmentObj.prodFlag == "LEGACY") {
                selectedRow = ".class_" + lSegmentObj.prodFlag + "_" + lSegmentObj.fileNameWithHash
            } else {
                selectedRow = ".class_" + lSegmentObj.prodFlag + "_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
            }

            var totalBranches = lSegmentObj.branch.length
            var unselectedBranchAlongWithCheckoutBranch = _.where(lSegmentObj.branch, {
                "isBranchSelected": false
            }).length + _.where(lSegmentObj.branch, {
                "isCheckedout": true
            }).length
            if (unselectedBranchAlongWithCheckoutBranch === totalBranches) {
                if ($(selectedRow).hasClass("ulHighligt"))
                    $scope.selectSegmentPNP(lSegmentObj)
                return
            }
            //If none of the systems are selected, perform un-select
            if (_.where(lSegmentObj.branch, {
                "isBranchSelected": false
            }).length === totalBranches) {
                $scope.selectSegmentPNP(lSegmentObj)
            } else {
                if (!$(selectedRow).hasClass("ulHighligt"))
                    $scope.selectSegmentPNP(lSegmentObj)
            }
        })
        $(".warnCancel").click(function () {
            lBranchObj.isBranchSelected = true;
            $scope.$apply()
        })

        $(".confirmProceed").click(function () {
            selectedRow = ".class_NONPROD_" + lSegmentObj.fileNameWithHash + "_" + lSegmentObj.additionalInfo.planId
            if (!$(selectedRow).hasClass("ulHighligt"))
                $scope.selectSegmentPNP(lSegmentObj)
        })

        $(".warnSelectionCancel").click(function () {

        })

        $(".confirmSelectionsProceed").click(function () {
            validate_non_prod_same_segment = true
            $scope.selectSegmentNonProd(lSegmentObj)
        })
    })
    // var productionBackup; // production - Added for sending production data alone
    $scope.removeSegment = function (data, isLegacy) {
        $scope.selectSegmentPNP(data)
        var preSelectedRow = ".class_PROD_" + data.fileNameWithHash
        if (isLegacy) {
            preSelectedRow = ".class_PENDING_" + data.fileNameWithHash
        }
        if ($(preSelectedRow).hasClass('ulHighligt') || $(preSelectedRow).hasClass('ulHighligt1')) {
            $(preSelectedRow).removeClass('ulHighligt');
            $(preSelectedRow).removeClass('ulHighligt1');
        }
        $scope.segmentsList = _.reject($scope.segmentsList, function (obj) {
            var branches_iteration = _.pluck(obj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (obj.fileName === data.fileName && branches_matched.length > 0 && obj.prodFlag === data.prodFlag);
        });

        _.each(PNP, function (repo) {
            $scope[repo].selectedData = _.reject($scope[repo].selectedData, function (obj) {
                var branches_iteration = _.pluck(obj.branch, "targetSystem")
                var branches_selected = _.pluck(data.branch, "targetSystem")
                var branches_matched = _.intersection(branches_iteration, branches_selected)
                return (obj.fileName === data.fileName && branches_matched.length > 0 && obj.prodFlag === data.prodFlag);
            });
            $scope[repo].selectedSegmentCount = $scope[repo].selectedData.length
        })
        // productionBackup = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData)
    }

    // remove mak file segment
    $scope.removeMakSegment = function (data, isLegacy) {
        $scope.selectMakSegmentPNP(data)
        var preSelectedRow = ".class_PROD_" + data.fileNameWithHash
        if (isLegacy) {
            preSelectedRow = ".class_PENDING_" + data.fileNameWithHash
        }
        if ($(preSelectedRow).hasClass('ulHighligt1')) {
            $(preSelectedRow).removeClass('ulHighligt1');
        }
        $scope.segmentsList = _.reject($scope.segmentsList, function (obj) {
            var branches_iteration = _.pluck(obj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (obj.fileName === data.fileName && branches_matched.length > 0 && obj.prodFlag === data.prodFlag);
        });

        _.each(PNP, function (repo) {
            $scope[repo].selectedMakData = _.reject($scope[repo].selectedMakData, function (obj) {
                var branches_iteration = _.pluck(obj.branch, "targetSystem")
                var branches_selected = _.pluck(data.branch, "targetSystem")
                var branches_matched = _.intersection(branches_iteration, branches_selected)
                return (obj.fileName === data.fileName && branches_matched.length > 0 && obj.prodFlag === data.prodFlag);
            });
            $scope[repo].selectedMakSegmentCount = $scope[repo].selectedMakData.length
        })
        // productionBackup = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData)
    }


    $scope.checkLegacy = function (segObj) {
        var ischeckLegacy = false;
        var legacySegments = _.filter(segObj.branch, function (elem) {
            return elem.refStatus == "Pending";
        })
        if (legacySegments.length > 0) {
            ischeckLegacy = true;
        }
        return ischeckLegacy;
    }

    $scope.checkNonProd = function (segObj) {
        var ischeckNonProd = false;
        var legacySegments = _.filter(segObj.branch, function (elem) {
            return (elem.refStatus == "Pending" || elem.refStatus == "Online" || elem.refStatus == "Fallback") && segObj.additionalInfo.planId;
        })
        if (legacySegments.length > 0) {
            ischeckNonProd = true;
        }
        return ischeckNonProd;
    }

    $scope.chooseSegment = function () {
        try {
            $(".searchSegments").fadeToggle('fast')
            $(".chkSelected").fadeToggle('fast')
            $(".checkoutSegments, .checkoutSegmentsTitle").css('display', 'none')
            vm.showMakCheckoutLoading = true
            $timeout(function () {
                $(".makCheckoutSegments").fadeToggle('fast')
                $(".chkMakSelected").fadeToggle('fast')
                $(".makCheckoutSegmentsTitle").css('display', 'block')
            }, 200)
            // var legacySegments = _.each($scope[PNP[2]].selectedData,function(elem){
            //     _.each(elem.branch,function(bObj){
            //         bObj.refStatus = "PENDING";
            //     })
            // })

            var prod = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData, $scope[PNP[0]].selectedMakData)
            var nonProd = _.union($scope[PNP[1]].segmentsList, $scope[PNP[1]].selectedData, $scope[PNP[1]].selectedMakData)
            var legacy = _.union($scope[PNP[2]].segmentsList, $scope[PNP[2]].selectedData, $scope[PNP[2]].selectedMakData)
            // $scope.productionMetaData.metaData = null;
            productionBackup = prod
            var chosenLegacySegments = prod.concat(nonProd);
            $scope.segmentsList = chosenLegacySegments.concat(legacy);
            $scope.segmentsList = angular.copy($scope.segmentsList)
            var listOfSegments = $scope.segmentsList;
            for (sL in $scope.segmentsList) {
                if ($scope.segmentsList[sL].branch.length == 1) { // Segments with single system
                    $scope.segmentsList[sL].branch[0].isBranchSelected = true
                }
                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isBranchSelected": false
                })
                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isCheckedout": true
                })
                // $scope.segmentsList[sL].branch = _.filter($scope.segmentsList[sL].branch, function (branchObj) {
                // return branchObj.refStatus == "newfile" || branchObj.refStatus == "Online" || branchObj.refStatus == "Fallback" || branchObj.refStatus == "Pending" || branchObj.refStatus == null;
                // })
            }

            APIFactory.devGetMakFileList({
                "implId": $stateParams.impId
            }, listOfSegments, function (response) {
                if (response && response.status && response.data) {
                    vm.showMakCheckoutLoading = false
                    vm.makFileErrorMsg = response.errorMessage;
                    $scope.makFileMessage = response.metaData
                    if (response.count > 0) {
                        $scope.makFileLength = response.count;
                        var devMakFileList = []
                        devMakFileList.push(response.data)
                        vm.showMakInfo = true;
                        $scope.segmentsList[0].associatedMakFiles = devMakFileList
                        // for (segIndex in $scope.segmentsList) {
                        // for (makResIndex in devMakFileList) {
                        // for (fHash in devMakFileList[makResIndex]) {
                        // if (fHash == $scope.segmentsList[segIndex].fileNameWithHash) {
                        // $scope.segmentsList[segIndex].associatedMakFiles = devMakFileList[makResIndex][fHash]
                        // }
                        // }
                        // }
                        // }
                    } else {
                        $(".makCheckoutSegments").css('display', 'none')
                        $(".makCheckoutSegmentsTitle").css('display', 'none')
                        $(".chkCheckout").css('display', 'none')
                        $(".makCheckoutSegmentsLoop").css('display', 'none')
                        $(".makCheckoutSegmentsLoop1").css('display', 'none')
                        $(".makCheckoutSegmentsLoop2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle1").css('display', 'none')
                        $(".makCheckoutSegmentsTitle2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle3").css('display', 'none')
                        $(".chkMakSelected").css('display', 'none')
                        $(".chkMakSelected1").css('display', 'none')
                        $(".chkMakSelected2").css('display', 'none')
                        $(".chkMakSelected3").css('display', 'none')

                        vm.showCheckoutMakLoading = true
                        $timeout(function () {
                            $(".checkoutSegments").fadeToggle('fast')
                            $(".checkoutSegmentsTitle").fadeToggle('fast')
                            $(".chkCheckout").fadeToggle('fast')
                        }, 200)

                    }
                    vm.showCheckoutMakLoading = false
                } else {
                    vm.showCheckoutMakLoading = false
                    vm.showMakCheckoutLoading = false
                    Toaster.sayError(response.errorMessage)
                }

            })


        } catch (err) { }
    }

    var selectedListOfMakFile = [];
    $scope.chooseMakSegment = function () {
        try {
            $(".checkoutSegments, .checkoutSegmentsTitle").css('display', 'none')
            $(".makCheckoutSegments").fadeToggle('fast')
            $(".chkMakSelected").fadeToggle('fast')

            vm.showMakCheckoutLoading = true
            $timeout(function () {
                $(".makCheckoutSegments").fadeToggle('fast')
                $(".chkMakSelected").fadeToggle('fast')
                $(".makCheckoutSegmentsTitle").css('display', 'block')
            }, 200)

            var prod = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData, $scope[PNP[0]].selectedMakData)
            var nonProd = _.union($scope[PNP[1]].segmentsList, $scope[PNP[1]].selectedData, $scope[PNP[1]].selectedMakData)
            var legacy = _.union($scope[PNP[2]].segmentsList, $scope[PNP[2]].selectedData, $scope[PNP[2]].selectedMakData)
            // $scope.productionMetaData.metaData = null;
            productionBackup = prod
            var chosenLegacySegments = prod.concat(nonProd);
            $scope.segmentsList = chosenLegacySegments.concat(legacy);
            $scope.segmentsList = angular.copy($scope.segmentsList)
            var listOfSegments = $scope.segmentsList;
            for (sL in $scope.segmentsList) {
                if ($scope.segmentsList[sL].branch.length == 1) { // Segments with single system
                    $scope.segmentsList[sL].branch[0].isBranchSelected = true
                }

                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isBranchSelected": false
                })
                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isCheckedout": true
                })
                // $scope.segmentsList[sL].branch = _.filter($scope.segmentsList[sL].branch, function (branchObj) {
                // return branchObj.refStatus == "newfile" || branchObj.refStatus == "Online" || branchObj.refStatus == "Fallback" || branchObj.refStatus == "Pending" || branchObj.refStatus == null;
                // })
            }

            APIFactory.devGetMakFileList({
                "implId": $stateParams.impId
            }, listOfSegments, function (response) {
                if (response && response.status && response.data) {
                    vm.showMakCheckoutLoading = false
                    vm.makFileErrorMsg = response.errorMessage;
                    $scope.makFileMessage = response.metaData
                    if (response.count > 0) {
                        $scope.makFileLength = response.count;
                        var devMakFileList = []
                        devMakFileList.push(response.data)
                        vm.showMakInfo = true;
                        $scope.segmentsList[0].associatedMakFiles = devMakFileList

                    } else {
                        $(".makCheckoutSegments").css('display', 'none')
                        $(".makCheckoutSegmentsTitle").css('display', 'none')
                        $(".chkCheckout").css('display', 'none')
                        $(".makCheckoutSegmentsLoop").css('display', 'none')
                        $(".makCheckoutSegmentsLoop1").css('display', 'none')
                        $(".makCheckoutSegmentsLoop2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle1").css('display', 'none')
                        $(".makCheckoutSegmentsTitle2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle3").css('display', 'none')
                        $(".chkMakSelected").css('display', 'none')
                        $(".chkMakSelected1").css('display', 'none')
                        $(".chkMakSelected2").css('display', 'none')
                        $(".chkMakSelected3").css('display', 'none')

                        vm.showCheckoutMakLoading = true
                        $timeout(function () {
                            $(".checkoutSegments").fadeToggle('fast')
                            $(".checkoutSegmentsTitle").fadeToggle('fast')
                            $(".chkCheckout").fadeToggle('fast')
                        }, 200)

                    }
                    vm.showCheckoutMakLoading = false
                } else {
                    vm.showCheckoutMakLoading = false
                    vm.showMakCheckoutLoading = false
                    Toaster.sayError(response.errorMessage)
                }

            })
        } catch (err) { }
    }

    $scope.chooseMakSegmentLoop1 = function () {
        try {

            $(".checkoutSegments, .checkoutSegmentsTitle").css('display', 'none')
            $(".makCheckoutSegmentsLoop").fadeToggle('fast')
            $(".chkMakSelected1").fadeToggle('fast')

            vm.showMakCheckoutLoading1 = true
            $timeout(function () {
                $(".makCheckoutSegmentsLoop1").fadeToggle('fast')
                $(".chkMakSelected2").fadeToggle('fast')
                $(".makCheckoutSegmentsTitle").css('display', 'block')
            }, 200)

            var prod = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData, $scope[PNP[0]].selectedMakData)
            var nonProd = _.union($scope[PNP[1]].segmentsList, $scope[PNP[1]].selectedData, $scope[PNP[1]].selectedMakData)
            var legacy = _.union($scope[PNP[2]].segmentsList, $scope[PNP[2]].selectedData, $scope[PNP[2]].selectedMakData)
            // $scope.productionMetaData.metaData = null;
            productionBackup = prod
            var chosenLegacySegments = prod.concat(nonProd);
            $scope.segmentsList = chosenLegacySegments.concat(legacy);
            $scope.segmentsList = angular.copy($scope.segmentsList)
            var listOfSegments = $scope.segmentsList;
            for (sL in $scope.segmentsList) {
                if ($scope.segmentsList[sL].branch.length == 1) { // Segments with single system
                    $scope.segmentsList[sL].branch[0].isBranchSelected = true
                }

                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isBranchSelected": false
                })
                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isCheckedout": true
                })
                // $scope.segmentsList[sL].branch = _.filter($scope.segmentsList[sL].branch, function (branchObj) {
                // return branchObj.refStatus == "newfile" || branchObj.refStatus == "Online" || branchObj.refStatus == "Fallback" || branchObj.refStatus == "Pending" || branchObj.refStatus == null;
                // })
            }

            APIFactory.devGetMakFileList({
                "implId": $stateParams.impId
            }, listOfSegments, function (response) {
                if (response && response.status && response.data) {
                    vm.showMakCheckoutLoading1 = false
                    vm.makFileErrorMsg = response.errorMessage;
                    $scope.makFileMessage = response.metaData
                    if (response.count > 0) {
                        $scope.makFileLength = response.count;
                        var devMakFileList = []
                        devMakFileList.push(response.data)
                        vm.showMakInfo = true;
                        $scope.segmentsList[0].associatedMakFiles = devMakFileList
                        // for (segIndex in $scope.segmentsList) {
                        // for (makResIndex in devMakFileList) {
                        // for (fHash in devMakFileList[makResIndex]) {
                        // if (fHash == $scope.segmentsList[segIndex].fileNameWithHash) {
                        // $scope.segmentsList[segIndex].associatedMakFiles = devMakFileList[makResIndex][fHash]
                        // $scope.makFileLength = $scope.segmentsList[segIndex].associatedMakFiles.length

                        // }
                        // }
                        // }
                        // }

                    } else {

                        $(".makCheckoutSegmentsLoop1").fadeToggle('fast')
                        $(".chkMakSelected2").fadeToggle('fast')

                        $(".makCheckoutSegments").css('display', 'none')
                        $(".makCheckoutSegmentsTitle").css('display', 'none')
                        $(".chkCheckout").css('display', 'none')
                        $(".makCheckoutSegmentsLoop").css('display', 'none')
                        $(".makCheckoutSegmentsLoop1").css('display', 'none')
                        $(".makCheckoutSegmentsLoop2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle1").css('display', 'none')
                        $(".makCheckoutSegmentsTitle2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle3").css('display', 'none')
                        $(".chkMakSelected").css('display', 'none')
                        $(".chkMakSelected1").css('display', 'none')
                        $(".chkMakSelected2").css('display', 'none')
                        $(".chkMakSelected3").css('display', 'none')



                        vm.showCheckoutMakLoading = true
                        $timeout(function () {
                            $(".checkoutSegments").fadeToggle('fast')
                            $(".checkoutSegmentsTitle").fadeToggle('fast')
                            $(".chkCheckout").fadeToggle('fast')
                        }, 200)
                    }
                    vm.showCheckoutMakLoading = false
                } else {
                    vm.showCheckoutMakLoading = false
                    vm.showMakCheckoutLoading1 = false
                    Toaster.sayError(response.errorMessage)
                }

            })
        } catch (err) { }
    }

    $scope.chooseMakSegmentLoop2 = function () {
        try {

            // $(".checkoutSegments, .checkoutSegmentsTitle").css('display', 'none')
            // $(".makCheckoutSegmentsLoop1").fadeToggle('fast')
            // $(".chkMakSelected2").fadeToggle('fast')

            vm.showMakCheckoutLoading2 = true
            $timeout(function () {
                $(".makCheckoutSegmentsLoop2").fadeToggle('fast')
                $(".chkMakSelected3").fadeToggle('fast')
                $(".makCheckoutSegmentsTitle").css('display', 'block')
            }, 200)

            var prod = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData, $scope[PNP[0]].selectedMakData)
            var nonProd = _.union($scope[PNP[1]].segmentsList, $scope[PNP[1]].selectedData, $scope[PNP[1]].selectedMakData)
            var legacy = _.union($scope[PNP[2]].segmentsList, $scope[PNP[2]].selectedData, $scope[PNP[2]].selectedMakData)
            // $scope.productionMetaData.metaData = null;
            productionBackup = prod
            var chosenLegacySegments = prod.concat(nonProd);
            $scope.segmentsList = chosenLegacySegments.concat(legacy);
            $scope.segmentsList = angular.copy($scope.segmentsList)
            var listOfSegments = $scope.segmentsList;
            for (sL in $scope.segmentsList) {
                if ($scope.segmentsList[sL].branch.length == 1) { // Segments with single system
                    $scope.segmentsList[sL].branch[0].isBranchSelected = true
                }
                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isBranchSelected": false
                })
                $scope.segmentsList[sL].branch = _.reject($scope.segmentsList[sL].branch, {
                    "isCheckedout": true
                })
                // $scope.segmentsList[sL].branch = _.filter($scope.segmentsList[sL].branch, function (branchObj) {
                // return branchObj.refStatus == "newfile" || branchObj.refStatus == "Online" || branchObj.refStatus == "Fallback" || branchObj.refStatus == "Pending" || branchObj.refStatus == null;
                // })
            }

            APIFactory.devGetMakFileList({
                "implId": $stateParams.impId
            }, listOfSegments, function (response) {
                if (response && response.status && response.data) {
                    vm.showMakCheckoutLoading2 = false
                    vm.makFileErrorMsg = response.errorMessage;
                    $scope.makFileMessage = response.metaData
                    if (response.count > 0) {
                        $scope.makFileLength = response.count;
                        var devMakFileList = []
                        devMakFileList.push(response.data)
                        vm.showMakInfo = true;
                        $scope.segmentsList[0].associatedMakFiles = devMakFileList
                        // for (segIndex in $scope.segmentsList) {
                        // for (makResIndex in devMakFileList) {
                        // for (fHash in devMakFileList[makResIndex]) {
                        // if (fHash == $scope.segmentsList[segIndex].fileNameWithHash) {
                        // $scope.segmentsList[segIndex].associatedMakFiles = devMakFileList[makResIndex][fHash]
                        // $scope.makFileLength = $scope.segmentsList[segIndex].associatedMakFiles.length
                        // }
                        // }
                        // }
                        // }

                    } else {
                        $(".makCheckoutSegmentsLoop2").fadeToggle('fast')
                        $(".chkMakSelected3").fadeToggle('fast')


                        $(".makCheckoutSegments").css('display', 'none')
                        $(".makCheckoutSegmentsTitle").css('display', 'none')
                        $(".chkCheckout").css('display', 'none')
                        $(".makCheckoutSegmentsLoop").css('display', 'none')
                        $(".makCheckoutSegmentsLoop1").css('display', 'none')
                        $(".makCheckoutSegmentsLoop2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle1").css('display', 'none')
                        $(".makCheckoutSegmentsTitle2").css('display', 'none')
                        $(".makCheckoutSegmentsTitle3").css('display', 'none')
                        $(".chkMakSelected").css('display', 'none')
                        $(".chkMakSelected1").css('display', 'none')
                        $(".chkMakSelected2").css('display', 'none')
                        $(".chkMakSelected3").css('display', 'none')

                        vm.showCheckoutMakLoading = true
                        $timeout(function () {
                            $(".checkoutSegments").fadeToggle('fast')
                            $(".checkoutSegmentsTitle").fadeToggle('fast')
                            $(".chkCheckout").fadeToggle('fast')
                        }, 200)

                    }
                    vm.showCheckoutMakLoading = false
                } else {
                    vm.showCheckoutMakLoading = false
                    vm.showMakCheckoutLoading2 = false
                    Toaster.sayError(response.errorMessage)
                }

            })
        } catch (err) { }
    }

    var selectedCheckFile = [];
    $scope.updateMak = function (selectedMak, segment, selectedFlag, makSelectionList) {

        if (_.indexOf(_.values(makSelectionList), true) == -1) // To disable Logic
        {
            _.each(segment.associatedMakFiles, function (value) {
                $("#check_" + value.fileNameWithHash).removeAttr("checked")
                $("#check_" + value.fileNameWithHash).removeAttr("disabled")
            })
        } else if (_.indexOf(_.values(makSelectionList), true) >= 0) {
            _.each(segment.associatedMakFiles, function (value2) {
                if (selectedMak.targetSystems[0] === value2.targetSystems[0]) {
                    if (selectedMak.fileNameWithHash != value2.fileNameWithHash) {
                        $("#check_" + value2.fileNameWithHash).attr("disabled", 'true')
                    }
                }
            })
        }
        if (selectedFlag) { // To Push/Remove Logic
            segment.selectedMakFiles = [];
            selectedCheckFile.push(selectedMak)
            segment.selectedMakFiles = selectedCheckFile
        } else {
            var selectedSegmentDelete = segment.selectedMakFiles.indexOf(selectedMak)
            segment.selectedMakFiles.splice(selectedSegmentDelete, 1);
        }
    }



    $scope.backToSelection = function () {
        vm.showCheckoutLoading = false
        vm.flagTrueFalse = true
        // $scope[PNP[0]].selectedMakSegmentCount = 0;
        // $scope[PNP[1]].selectedMakSegmentCount = 0;
        // $scope[PNP[2]].selectedMakSegmentCount = 0;
        // $scope[PNP[0]].selectedMakData = [];
        // $scope[PNP[1]].selectedMakData = [];
        // $scope[PNP[2]].selectedMakData = [];

        // if ($("ul").hasClass("ulHighligt1")) {
        // $("ul").removeClass("ulHighligt1")
        // }


        try {
            $(".checkoutSegments").fadeToggle('fast')
            $(".checkoutSegmentsTitle").fadeToggle('fast')
            $(".chkCheckout").fadeToggle('fast')
            $timeout(function () {
                $(".searchSegments").fadeToggle('fast')
                $(".chkSelected").fadeToggle('fast')
            }, 200)

            _.each($rootScope.segmentData, function (Obj) {
                $scope.selectSegmentPNPBack(Obj)
            })

        } catch (err) { }
    }

    var addMakasObj = function () {
        var selectedMakList = [] //(_.flatten(_.pluck(copyFileList, "selectedMakFiles")))
        var copyFileList = angular.copy($scope.segmentsList)
        for (i in copyFileList) {
            if (copyFileList[i].selectedMakFiles) {
                selectedMakList.push(copyFileList[i].selectedMakFiles)
            }
        }
        selectedMakList = _.flatten(selectedMakList)
        if (selectedMakList && selectedMakList.length > 0) {
            copyFileList = _.union(copyFileList, selectedMakList)
        }
        _.each(copyFileList, function (obj) {
            delete obj.associatedMakFiles;
            delete obj.selectedMakFiles
        })
        $scope.segmentsList = angular.copy(copyFileList)
    }

    $scope.checkoutShowPopup = function () {
        if (vm.makFileErrorMsg) {
            $('#warningCheckoutPopup').modal({
                backdrop: 'static',
                keyboard: true
            })
        } else {
            $scope.checkoutSegments();
        }
    }

    $scope.checkoutSegments = function () {
        if ($scope.segmentsList.length === 0) {
            Toaster.sayWarning("Add Files for Checkout");
            return;
        }
        $("#chkoutAnimate").removeClass("aBox").addClass("aBox")
        $(".chkModalBody").css("background", "#455A64")
        $(".checkoutSegments").hide()
        $(".makCheckoutSegments").hide()
        $(".makCheckoutSegmentsLoop").hide()
        $(".makCheckoutSegmentsLoop1").hide()
        $(".makCheckoutSegmentsLoop2").hide()
        $(".chkCheckout").hide()
        $(".chkMakSelected").hide()
        $(".chkMakSelected1").hide()
        $(".chkMakSelected2").hide()
        $(".chkMakSelected3").hide()
        $(".checkoutLoading").show()
        vm.checkoutInProgress = true
        for (pbObj in productionBackup) {
            productionBackup[pbObj].branch = _.reject(productionBackup[pbObj].branch, {
                "isBranchSelected": false
            })
            /* productionBackup[pbObj].branch = _.reject(productionBackup[pbObj].branch, {
                "isCheckedout": true
            }) */
        }
        // var dataObj = angular.copy(productionBackup)
        var dataObj;
        if (vm.showMakInfo) { // if there are any associated mak files response recived
            addMakasObj(); // Convert MAK files selected, as individual objects before checkout
        }
        dataObj = $scope.segmentsList


        var params = {
            "implId": $stateParams.impId
        }

        APIFactory.devCheckoutFile(params, dataObj, function (response) {
            $timeout(function () {
                $(".chkModalBody").css("background", "#fff")
                $(".checkoutLoading").hide()
                vm.checkoutInProgress = false
                $(".checkoutSegmentsTitle").fadeToggle('fast')
                $(".checkoutReportTemplate").fadeToggle('fast')
                if (response.status) {
                    vm.checkoutStatusList = response.data
                    // qxp1, yiia
                    //temp fix -->
                    _.each(vm.checkoutStatusList, function (source_obj) {
                        source_obj.branch = _.uniq(source_obj.branch, "targetSystem")
                    })
                    //<--
                    $scope.loadSegmentList()
                } else {
                    vm.checkoutStatusList = response.data
                    Toaster.sayError(response.errorMessage)
                }
                $scope.$digest()
            }, 1000)
        })
        APIFactory.clearValidateMakFileCacheutFile({ "impl": $stateParams.impId }, function (response) {
            if (response.status) {
            }
        })
    }


    // after selecting segments popup checkout screen

    $(document).ready(function () {
        $(".warnCheckoutYes").click(function () {
            $scope.checkoutSegments();
        })
        $(".warnCheckoutNo").click(function () {
            // $scope.chooseSegment();
            // $scope.$apply()
        })


    })
    /*********************************************************************************/
    /*********************************************************************************/

    $scope.updateWorkspace = function () {
        try {
            var apiModule = APIFactory.getImplementationFindByImpIdUrl();
            var params = {
                "impId": $stateParams.impId
            }
            var apiCall = apiService.get(apiModule, params, function (response) {
                if (response.status) {
                    vm.workspaceList = response.data.segments ? response.data.segments : []
                    vm.imp.segments = response.data.segments ? response.data.segments : []
                    $scope.workspaceLength = vm.workspaceList.length
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        } catch (err) { }

    }

    $scope.confirmDeleteFromWorkspace = function (ev, branches) {
        $scope.saveRFCLoader = true;
        var selectedBranch = [];
        _.each(branches, function (bObj, index) {
            for (var k in vm.segmentSelection.singleSelect) {
                if (k == index && vm.segmentSelection.singleSelect[index]) {
                    selectedBranch.push(bObj)
                }
            }
        })
        try {
            var confirm = $mdDialog.confirm()
                .title('Are you sure?')
                .textContent('Delete action can not be reverted')
                .ariaLabel('Delete from workspace')
                .targetEvent(ev)
                .ok('Proceed')
                .cancel('Cancel');

            $mdDialog.show(confirm).then(function () {
                var branch = [];
                _.each(selectedBranch, function (wObj) {
                    if (wObj.branch) {
                        _.each(wObj.branch, function (bObj) {
                            branch.push(bObj);
                        })
                    }
                })
                if (branch.length > 0) {
                    $scope.deleteFromWorkspace(branch)
                } else {
                    $scope.saveRFCLoader = false;
                }

            }, function () {
                $scope.saveRFCLoader = false;
            });
        } catch (err) { }
    }
    vm.selectedFilesSize = 0
    vm.uploadedFilesSize = 0;
    var fileAlreadyExists = false;
    $scope.uploadFiles = function (files, errFiles) {

        try {
            if (files.length === 0 && errFiles.length == 0) {
                return;
            }
            vm.selectedFilesSize = 0;
            fileAlreadyExists = false;
            for (fileIndex in files) {
                vm.selectedFilesSize += files[fileIndex].size
                if (vm.uploadedFiles.indexOf(files[fileIndex].name) >= 0) {
                    fileAlreadyExists = true;
                }
            }
            if (fileAlreadyExists) {
                vm.selectedFilesSize = 0
                Toaster.sayWarning("Files with same name are not allowed");
                return;
            }
            vm.selectedFilesSize = parseFloat(vm.selectedFilesSize / 1024).toFixed(3) //bytes to kb
            if ((parseFloat(vm.uploadedFilesSize) + parseFloat(vm.selectedFilesSize)) > 5000) {
                vm.selectedFilesSize = 0
                Toaster.sayWarning("Maximum upload size limit reached");
                return;
            }

            if (errFiles.length > 0) {
                Toaster.sayWarning("Individual file size should not be more than 1 MB");
            }
            //            $scope.errFiles = errFiles;
            //            vm.selectedFiles = vm.selectedFiles.concat(files)
            $scope.uploadTestFiles(files)
        } catch (err) { }

    }

    $scope.uploadTestFiles = function (files) {

        try {
            if (files && files.length) {
                var dataObj = {
                    "file": files,
                    "implId": $stateParams.impId,
                    "planId": $stateParams.planId
                }
                APIFactory.uploadTestCase(dataObj, function (response) {
                    if (response.status) {
                        $scope.getTestFiles()
                        vm.selectedFilesSize = 0
                        // Toaster.saySuccess("Test Results uploaded successfully");
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            }
        } catch (err) { }

    }
    vm.folderSize = parseInt(0)
    vm.freeSpace = 100
    $(".progress-bar").css("width", "0%")
    var maxSize = 5000
    $scope.getTestFiles = function () {
        try {

            APIFactory.listTestCases({
                "implId": $stateParams.impId,
                "planId": $stateParams.planId
            }, function (response) {
                if (response.status) {
                    if (response.metaData != null) {
                        var totalSize = (parseFloat(response.metaData) / 1024).toFixed(3)
                        vm.folderSize = convertToPercentage(parseFloat(totalSize))
                        vm.freeSpace = parseFloat(maxSize) - parseFloat(totalSize)
                        $timeout(function () {
                            $(".progress-bar").css("width", vm.folderSize + "%")
                        }, 100)
                        vm.uploadedFilesSize = totalSize
                    }
                    vm.uploadedFiles = response.data
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        } catch (err) { }

    }
    $scope.downloadTestCase = function (fileName) {

        try {
            var params = {
                "implId": $stateParams.impId,
                "planId": $stateParams.planId,
                "testFile": fileName
            }
            APIFactory.downloadTestCase(params, function (response) {
                if (response.status) {
                    var resposeStr = base64ToArrayBuffer(response.data)
                    var file = new Blob([resposeStr], {
                        type: response.metaData
                    });
                    saveAs(file, fileName)
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        } catch (err) { }

    }


    $scope.confirmDeleteTestDoc = function (ev, file) {

        try {
            var confirm = $mdDialog.confirm()
                .title('Would you like to delete ?') //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Confirm delete Test Document')
                .targetEvent(ev)
                .ok('Yes')
                .cancel('No');
            $mdDialog.show(confirm).then(function () {
                $scope.deleteTestCase(file)
            }, function () {

            });
        } catch (err) { }

    };

    $scope.deleteTestCase = function (file) {

        try {
            var params = {
                "implId": $stateParams.impId,
                "planId": $stateParams.planId,
                "testFile": file
            }
            APIFactory.deleteTestCase(params, function (response) {
                if (response.status) {
                    $scope.getTestFiles()
                    Toaster.saySuccess("File Deleted Successfully");
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
            //                $window.location = apiBase + "/downloadTestCase?implId="+$stateParams.impId+"&planId="+$stateParams.planId+"&testFile="+file
        } catch (err) { }


    }
    $scope.completeUnitTest = function () {

        try {
            //            alert($.trim(vm.imp.subStatus))
            if ($.trim(vm.imp.subStatus) == "UnitTestingCompleted") {
                var apiModule = getAddTagUrl();
                var params = {
                    "branchName": $stateParams.impId
                }
                var apiCall = apiService.post(apiModule, params, null, function (response) {
                    if (response.status) {
                        Toaster.saySuccess('"UNIT TESTING COMPLETED" Tag updated successfully')
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            }
        } catch (err) { }
    }
    $scope.getTestFiles()

    vm.segmentSelection = {}
    vm.segmentSelection.singleSelect = {}
    $scope.checkAll = function () {
        if (vm.segmentSelection.selectAll) {
            for (index in vm.workspaceList) {
                vm.segmentSelection.singleSelect[index] = true
            }
        } else {
            for (index in vm.workspaceList) {
                vm.segmentSelection.singleSelect[index] = false
            }
        }
    }

    $scope.singleselectchange = function (select) {
        var selectcount = _.filter(select.singleSelect, function (item) {
            return item == true;
        });
        if (selectcount.length == vm.workspaceList.length) {
            vm.segmentSelection.selectAll = true;
        } else {
            vm.segmentSelection.selectAll = false;
        }
        $scope.checkSegments();
    }

    $scope.togglePopulateSCM = function () {
        $(".pnpTabs").toggle()
        // $(".populateSCMArea").toggle()
        $(".chooseSCMArea").toggle()
        $(".populateMakCheckoutSegments").css('display', 'none')
        $(".populateChkMakSelected").css('display', 'none')
        $(".populateMakCheckoutSegmentsTitle").css('display', 'none')
        //            $scope.searchText = $scope.vanillaSearchText;
        if ($(".chooseSCMArea").is(":visible")) {
            $scope.vanillaSearchText = $scope.searchText
            $scope.productionMetaData.metaData = null
            $(".chkMakSelected").css('display', 'none')
            $(".chkMakSelected1").css('display', 'none')
            $(".chkMakSelected2").css('display', 'none')
            $(".chkMakSelected3").css('display', 'none')
        } else {
            $scope.searchText = $scope.vanillaSearchText;
            $scope.ibmVanillaMetaData.metaData = null
            $(".chkMakSelected").css('display', 'none')
            $(".chkMakSelected1").css('display', 'none')
            $(".chkMakSelected2").css('display', 'none')
            $(".chkMakSelected3").css('display', 'none')
        }
    }

    vm.selectedVanillaSegments = []
    $scope.ibmVanillaList = function (searchText) {
        if ($.trim(searchText) == "") {
            Toaster.sayWarning("Segment name cannot be empty");
            return false;
        }
        if ($.trim(searchText).length <= 1) {
            Toaster.sayWarning("Minimum two characters required");
            return false;
        }
        vm.showCheckoutLoading = true
        //vm.selectedVanillaSegments = []
        // IBMVANILLA
        $scope.searchSegment(searchText, "IBMVANILLA", $scope["ibmVanillaMetaData"].pageCount, function (response) {
            vm.showCheckoutLoading = false
            if (response.status) {
                $scope.ibmVanillaMetaData.searchList = response.data
                $scope.ibmVanillaMetaData.metaData = response.metaData


            } else {
                // Toaster.sayError(response.errorMessage)
            }
        })

    }
    $scope.chooseVanillaSystem = function (fileObject) {

        var selectedRow = ".class_" + fileObject.fileNameWithHash
        var fileObjectSelected = false;
        for (FS in fileObject.selectedSystems) {
            if (fileObject.selectedSystems[FS]) {
                fileObjectSelected = true
            }
        }
        if ($(selectedRow).hasClass("ulHighligt") && !fileObjectSelected) {
            $(selectedRow).removeClass("ulHighligt")
            vm.selectedVanillaSegments = _.reject(vm.selectedVanillaSegments, {
                "fileName": fileObject.fileName
            })
        } else {
            if (_.where(vm.selectedVanillaSegments, {
                "fileName": fileObject.fileName
            }).length === 0) {
                vm.selectedVanillaSegments.push(fileObject)
                $(selectedRow).addClass("ulHighligt")
            }
        }
    }

    $scope.chooseVanillaSegments = function (segmentObject) {
        var selectedRow = ".class_" + segmentObject.fileNameWithHash
        if ($(selectedRow).hasClass("ulHighligt")) {
            $(selectedRow).removeClass("ulHighligt")
            vm.selectedVanillaSegments = _.reject(vm.selectedVanillaSegments, {
                "fileName": segmentObject.fileName
            })
        } else {
            if (_.where(vm.selectedVanillaSegments, {
                "fileName": segmentObject.fileName
            }).length === 0) {
                vm.selectedVanillaSegments.push(segmentObject)
                $(selectedRow).addClass("ulHighligt")
            }
        }
    }

    $scope.chooseVanillaSCM = function () {
        $rootScope.segmentsToPopulate = [];

        try {

            if (vm.selectedVanillaSegments.length === 0) {
                Toaster.sayWarning("Choose some segments to populate")
                return;
            } else {
                vm.segmentsToPopulate = angular.copy(vm.selectedVanillaSegments)
                for (vObj in vm.segmentsToPopulate) {
                    vm.segmentsToPopulate[vObj].targetSystemList = []
                    vm.segmentsToPopulate[vObj].branchList = []
                    for (sObj in vm.segmentsToPopulate[vObj].selectedSystems) {
                        if (vm.segmentsToPopulate[vObj].selectedSystems[sObj]) {
                            vm.segmentsToPopulate[vObj].targetSystemList.push(sObj)
                        }
                    }
                }

                vm.segmentsToPopulate = _.reject(vm.segmentsToPopulate, function (obj) {
                    return obj.targetSystemList.length === 0;
                })

                if (vm.segmentsToPopulate.length === 0) {
                    Toaster.sayWarning("Choose system for the selected segments")
                    return;
                }

                var params = {
                    "implId": vm.imp.id,
                    "searchType": "IBM"
                }
                $(".chooseSCMArea").fadeToggle('fast')
                $(".searchSegments").css('display', 'none')
                // $(".chooseSCMArea").css('display', 'none')
                vm.showMakCheckoutLoading = true
                vm.makFileLoading = true;
                $timeout(function () {
                    $(".populateMakCheckoutSegments").fadeToggle('fast')
                    $(".populateChkMakSelected").fadeToggle('fast')
                    $(".populateMakCheckoutSegmentsTitle").css('display', 'block')
                }, 200)

                var dataObj = []
                angular.forEach(vm.segmentsToPopulate, function (sObj) {
                    // delete sObj.selectedSystems
                    dataObj.push({
                        "fileName": sObj.fileName,
                        "programName": sObj.programName,
                        "targetSystems": _.keys(_.pick(sObj.selectedSystems, _.identity)),
                        "branch": sObj.branch,
                        "prodFlag": sObj.prodFlag,
                        "fileNameWithHash": sObj.fileNameWithHash,
                        "additionalInfo": sObj.additionalInfo,
                        "maxLoadDate": sObj.maxLoadDate,
                        "repoAccess": sObj.repoAccess,
                        "branchMaxLoadDate": sObj.branchMaxLoadDate,
                    })
                    // dataObj.push(sObj)
                })
                $rootScope.segmentsToPopulate = dataObj
                APIFactory.devGetMakFileList(params, dataObj, function (response) {
                    if (response && response.status && response.data) {
                        vm.showMakCheckoutLoading = false
                        vm.makFileErrorMsg = response.errorMessage;
                        $scope.makFileMessage = response.metaData
                        if (response.count > 0) {
                            $scope.makFileLength = response.count;
                            var devMakFileList = []
                            devMakFileList.push(response.data)
                            vm.showMakInfo = true;
                            vm.makFileLoading = false;
                            vm.segmentsToPopulate[0].associatedMakFiles = devMakFileList
                        } else {
                            $(".searchSegments").css('display', 'none')
                            $(".populateMakCheckoutSegments").css('display', 'none')
                            $(".populateChkMakSelected").css('display', 'none')
                            $(".populateMakCheckoutSegmentsTitle").css('display', 'none')
                            vm.showCheckoutMakLoading = true
                            vm.makFileLoading = false;
                            $timeout(function () {
                                $(".populateSummaryTemplate").fadeToggle('fast')
                                $(".populateSummaryReport").fadeToggle('fast')
                                $(".populateSCMArea ").fadeToggle('fast')
                            }, 200)
                            vm.segmentsToPopulate = response.data
                            vm.segmentCount = Object.keys(vm.segmentsToPopulate).length
                        }
                        vm.showCheckoutMakLoading = false
                    } else {
                        vm.showCheckoutMakLoading = false
                        vm.showMakCheckoutLoading = false
                        vm.makFileLoading = false;
                        Toaster.sayError(response.errorMessage)
                    }

                })
            }
        } catch (err) { }
    }

    $scope.populateChooseMakSegment = function () {
        try {
            $(".searchSegments").css('display', 'none')
            $(".populateMakCheckoutSegments").fadeToggle('fast')
            $(".populateChkMakSelected").fadeToggle('fast')
            $(".populateMakCheckoutSegmentsTitle").css('display', 'block')
            vm.showMakCheckoutLoading = true
            vm.makFileLoading = true;
            $timeout(function () {
                // $(".searchSegments").fadeToggle('fast')
                $(".populateMakCheckoutSegments").fadeToggle('fast')
                $(".populateChkMakSelected").fadeToggle('fast')
                $(".populateMakCheckoutSegmentsTitle").css('display', 'block')
            }, 200)
            // var prod = _.union(vm.segmentsToPopulate, $rootScope.segmentIBMData)
            var prod = _.union($rootScope.segmentsToPopulate, $scope[PNP[1]].selectedMakData, $scope[PNP[2]].selectedMakData, $scope[PNP[0]].selectedMakData)

            vm.segmentsToPopulate = prod
            for (vObj in vm.segmentsToPopulate) {
                vm.segmentsToPopulate[vObj].targetSystemList = []
                vm.segmentsToPopulate[vObj].branchList = []
                for (sObj in vm.segmentsToPopulate[vObj].selectedSystems) {
                    if (vm.segmentsToPopulate[vObj].selectedSystems[sObj]) {
                        vm.segmentsToPopulate[vObj].targetSystemList.push(sObj)
                    }
                }
            }

            // vm.segmentsToPopulate = _.reject(vm.segmentsToPopulate, function (obj) {
            // return obj.targetSystemList.length === 0;
            // })

            // if (vm.segmentsToPopulate.length === 0) {
            // Toaster.sayWarning("Choose system for the selected segments")
            // return;
            // }
            var params = {
                "implId": vm.imp.id,
                "searchType": "IBM"
            }
            var dataObj = []
            angular.forEach(vm.segmentsToPopulate, function (sObj) {
                // delete sObj.selectedSystems
                // dataObj.push({
                // "fileName": sObj.fileName,
                // "programName": sObj.programName,
                // "targetSystems": _.keys(_.pick(sObj.selectedSystems, _.identity))
                // })
                dataObj.push({
                    "fileName": sObj.fileName,
                    "programName": sObj.programName,
                    "targetSystems": sObj.targetSystems,
                    "branch": sObj.branch,
                    "prodFlag": sObj.prodFlag,
                    "fileNameWithHash": sObj.fileNameWithHash,
                    "additionalInfo": sObj.additionalInfo,
                    "maxLoadDate": sObj.maxLoadDate,
                    "repoAccess": sObj.repoAccess,
                    "branchMaxLoadDate": sObj.branchMaxLoadDate,
                })
                // dataObj.push(sObj)
            })

            APIFactory.devGetMakFileList(params, dataObj, function (response) {
                if (response && response.status && response.data) {
                    vm.showMakCheckoutLoading = false
                    vm.makFileErrorMsg = response.errorMessage;
                    $scope.makFileMessage = response.metaData
                    if (response.count > 0) {
                        $scope.makFileLength = response.count;
                        var devMakFileList = []
                        devMakFileList.push(response.data)
                        vm.showMakInfo = true;
                        vm.makFileLoading = false;
                        vm.segmentsToPopulate[0].associatedMakFiles = devMakFileList
                    } else {
                        $(".searchSegments").css('display', 'none')
                        $(".populateMakCheckoutSegments").css('display', 'none')
                        $(".populateChkMakSelected").css('display', 'none')
                        $(".populateMakCheckoutSegmentsTitle").css('display', 'none')
                        vm.showCheckoutMakLoading = true
                        vm.makFileLoading = false;
                        $timeout(function () {

                            $(".populateSummaryTemplate").fadeToggle('fast')
                            $(".populateSummaryReport").fadeToggle('fast')
                            $(".populateSCMArea ").fadeToggle('fast')
                            // $(".chkCheckout").fadeToggle('fast')
                        }, 200)
                        vm.segmentsToPopulate = response.data
                        vm.segmentCount = Object.keys(vm.segmentsToPopulate).length
                    }
                    vm.showCheckoutMakLoading = false
                } else {
                    vm.showCheckoutMakLoading = false
                    vm.showMakCheckoutLoading = false
                    vm.makFileLoading = false;
                    Toaster.sayError(response.errorMessage)
                }

            })


        } catch (err) { }
    }

    $scope.removeSegmentIBM = function (data) {
        vm.segmentsToPopulate = _.reject(vm.segmentsToPopulate, function (obj) {
            var branches_iteration = _.pluck(obj[0].branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (obj[0].fileName === data.fileName && branches_matched.length > 0 && obj[0].prodFlag === data.prodFlag);
        });
    }
    var segmentsToPopulateSummary = []
    $scope.populateVanillaSCM = function () {
        // if (vm.selectedVanillaSegments.length === 0) {
        // Toaster.sayWarning("Choose some segments to populate")
        // return;
        // }

        vm.showMakCheckoutLoading = true
        vm.makFileLoading = true;
        // vm.segmentsToPopulate = segmentsToPopulateSummary
        // for (vObj in vm.segmentsToPopulate) {
        // vm.segmentsToPopulate[vObj].targetSystemList = []
        // for (sObj in vm.segmentsToPopulate[vObj].selectedSystems) {
        // if (vm.segmentsToPopulate[vObj].selectedSystems[sObj]) {
        // vm.segmentsToPopulate[vObj].targetSystemList.push(sObj)
        // }
        // }
        // }

        // vm.segmentsToPopulate = _.reject(vm.segmentsToPopulate, function (obj) {
        // return obj.targetSystemList.length === 0;
        // })

        // if (vm.segmentsToPopulate.length === 0) {
        // Toaster.sayWarning("Choose system for the selected segments")
        // return;
        // }

        $(".populateSCMBtn").attr("disabled", "true")
        $(".populateSCMBtn").html('<i class="fa fa-pulse fa-spinner" style="color:#ccc;"></i> Populating In-Progress')

        var params = {
            "implId": vm.imp.id
        }

        angular.forEach(vm.segmentsToPopulate, function (sObj) {
            segmentsToPopulateSummary.push({
                "fileName": sObj[0].fileName,
                "programName": sObj[0].programName,
                "targetSystems": sObj[0].targetSystems,
                "branch": sObj[0].branch,
                "prodFlag": sObj[0].prodFlag,
                "fileNameWithHash": sObj[0].fileNameWithHash,
                "additionalInfo": sObj[0].additionalInfo,
                "maxLoadDate": sObj[0].maxLoadDate,
                "repoAccess": sObj[0].repoAccess,
                "branchMaxLoadDate": sObj[0].branchMaxLoadDate,
            })
        })
        APIFactory.populateIBMSegment(params, segmentsToPopulateSummary, function (response) {
            $(".populateSCMBtn").removeAttr("disabled")
            $(".populateSCMBtn").html('<i class="fa fa-forward"></i> &nbsp; Populate SCM and checkout')
            if (response.status) {
                vm.showMakCheckoutLoading = false
                vm.makFileLoading = false;
                vm.selectedVanillaSegments = []
                segmentsToPopulateSummary = []
                vm.segmentsToPopulate = []
                vm.populatedVanillaFileList = response.data
                $(".searchSegments").hide()
                $(".populateSCMArea").hide()
                $(".populateMakCheckoutSegments").css('display', 'none')
                $(".populateChkMakSelected").css('display', 'none')
                $(".populateMakCheckoutSegmentsTitle").css('display', 'none')
                $(".populateSummaryTemplate").css('display', 'none')
                $(".populateSummaryReport").css('display', 'none')
                $(".populateReportTemplate").fadeToggle('fast')
                $scope.loadSegmentList();
            } else {
                Toaster.sayError(response.errorMessage);
                vm.showMakCheckoutLoading = false
                vm.makFileLoading = false;
            }
        })
    }
    $scope.createNewSourceArtifact = function (ev, impId, planId) {
        IService.initCreateNewSourceArtifact($scope, ev, impId, planId);
    }

    function convertToPercentage(number) {

        try {
            return parseFloat(parseFloat(number) * 100 / parseInt(maxSize)).toFixed(1)
        } catch (err) { }
    }

    /* $(".searchList.prod").scroll(function () {
            if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {

            } else {
                if ($scope[PNP[0]].segSearchProcess) {
                    $scope[PNP[0]].segSearchProcess = false;
                    $scope.loadSegments("PROD", PNP[0]);
                }
            }
        });
        $(".searchList.nonProd").scroll(function () {
            if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {

            } else {
                if ($scope[PNP[1]].segSearchProcess) {
                    $scope[PNP[1]].segSearchProcess = false;
                    $scope.loadSegments("NONPROD", PNP[1]);
                }
            }
        }); */


});


dashboard.filter('dateTime', [function () {
    return function (date) { //1 = January
        var number = moment(new Date(date).getTime()).format("MM/DD/YYYY HH:mm")
        return number;
    }
}]);

dashboard.filter('replaceDot', [function () {
    return function (str) { //1 = January
        return str.replace(".", "");
    }
}]);
dashboard.filter('takeProgramName', [function () {
    return function (str) {
        return str ? str.replace(/\//g, "_").replace(".", "_") : ""
    }
}]);
dashboard.filter('systemFilter', [function () {
    return function (str) {
        if (str.indexOf("_") >= 0) {
            return str ? str.split("_")[1].toUpperCase() : "-";
        }
        return str;
    }
}]);
dashboard.filter('functionalFilter', [function () {
    return function (str) {
        if (str.indexOf(".") >= 0) {
            return str.split(".")[0].toUpperCase().split("_").slice(-1)[0];
        }
    }
}]);
dashboard.filter('iconStatus', [function () {
    return function (str) {
        return "<i class='fa fa-cog fa-spin'></i> " + str;
    }
}]);
dashboard.filter('takeOutSystem', function ($filter) {
    return function (obj) {
        var systemList = _.pluck(obj, "targetSystem")
        for (sL in systemList) {
            systemList[sL] = $filter("systemFilter")(systemList[sL])
        }
        return systemList.length > 0 ? systemList.join(",") : "-";
    }
});

dashboard.filter('systemTargetFilter', [function () {
    return function (str) {
        if (str.indexOf("_") >= 0) {
            return str ? str.split("_")[2].toUpperCase() : "-";
        }
        return str;
    }
}]).directive('disableIfZero', function () {

    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attr) {
            scope.$watch("workspaceLength", function (v) {
                if (v != 0) {
                    $(element).removeAttr("disabled")
                } else {
                    $(element).attr("disabled", "true")
                }
            })

        }
    }
}).directive('selectSegment', function () {
    return {
        restrict: 'EA',
        scope: {
            sObj: '=selectObj',
            sFunction: '&selectCall'
        },
        link: function (scope, element, attr) {
            $(element).click(function () {
                scope.sFunction({
                    segmentObj: scope.sObj
                })
            })
        }
    };
})
    .directive("myNotification", function ($timeout) {
        return {
            restrict: 'EA',
            replace: true,
            scope: {
                message: '@'
            },
            template: '<span>{{message}} <br/></span>',
            link: function (scope, element, attrs) {
                $timeout(function () {
                    element.hide();
                }, 10000);
            }
        }
    });