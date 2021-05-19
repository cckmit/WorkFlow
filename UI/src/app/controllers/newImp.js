dashboard.controller("newImpCtrl", function($rootScope, $http, $scope, $state, $stateParams, $location, $window, $timeout, Toaster, appSettings, fImplementationPlanValidate, apiService, APIFactory, WFLogger, Paginate) {
    var vm = this;
    vm.planId = $stateParams.planId
    vm.imp = {}
    vm.imp.planId = {}
    $rootScope.titleHeading = $state.current.data.pageTitle + " > Create Implementation"
    $rootScope.prevSaveButton = false;
    $scope.userRole = getUserData("userRole")
    Paginate.refreshScrolling();
    vm.loader = { readyforQA: false, createImp: false };

    $('.select2-input').on("keydown", function(e) {
        if (e.keyCode == 13) {
            $('#developersList').focus();
            e.preventDefault();
        }
    });

    $timeout(function() {
        $('[aria-label="impdesc"]').focus()
    }, 1000)

    vm.planList = [{ id: 1, value: 'T1700001' }, { id: 2, value: 'T1700002' }, { id: 1, value: 'T1700002' }]
        //        vm.impStatus = ['In-Progress', 'DONE','Ready for QA', 'Deployed'];
    vm.impStatus = ['In-Progress', 'Ready for QA', 'Deployed'];
    vm.prodList = ['Pass', 'Fail'];
    var apiBase = appSettings.apiBase;
    $scope.fImplementationPlanValidate = fImplementationPlanValidate

    var loadStatusList = function() {
        APIFactory.getImplementationStatusList({}, function(response) {
            if (response.status) {
                vm.impStatusList = response.data
            } else {
                vm.impStatusList = []
            }
        })
    }
    loadStatusList()

    var loadPlan = function() {
        APIFactory.getPlan({ "id": vm.planId }, function(response) {
            if (response.status) {
				$scope.rfcDataFlag = response.data.impPlan.rfcFlag;
                $scope.planLoadType = response.data.impPlan.loadType
            } else {}
        })
    }
    loadPlan()

    vm.submitImpForm = function(data) {
        try {
            if (data.devId)
                if (typeof data.devId != 'undefined' && data.devId.length > 0) {
                    data.devId = data.devId.toString();
                } else {
                    data.devId = null
                }
            if (data.peerReviewers && data.peerReviewers.indexOf(data.devId) > -1) {
                Toaster.sayWarning("Peer Reviewer and Developer can't be same")
                return;
            }
            if (vm.impForm.developerContact.$invalid && vm.impForm.developerContact.$touched) {
                Toaster.sayWarning("Provide Valid Developer phone number")
                return;
            }
            if (Array.isArray(data.peerReviewers)) {
                data.peerReviewers = data.peerReviewers ? (data.peerReviewers.length > 1 ? data.peerReviewers.join(",") : data.peerReviewers[0]) : ""
            }
            data.impStatus = "IN_PROGRESS"
            data.prTktNum = $("#iprobTktNo").val()
            data.devName = data.devId ? _.where(vm.developerList, { "id": data.devId })[0].displayName : ""
            if (data.bypassPeerReview) {
                data.peerReviewers = ""
            } else {
                data.bypassPeerReview = false
            }
            if (fImplementationPlanValidate.validateImpMandatoryFields(data)) {
                vm.loader.createImp = true;
                APIFactory.saveImplementation(data, function(response) {
                    vm.loader.createImp = false;
                    if (response.status) {
                        Toaster.saySuccess("Implementation Created Successfully");
                        $timeout(function() {
                            if ($rootScope.previousState != undefined) {
                                $state.go($rootScope.previousState)
                            } else {
                                $state.go("app.impPlan")
                            }
                        }, 1000)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            }
            //        $window.location.href= "#/app/impPlan";
        } catch (err) { console.log(err) }

    }

    var loadDeveloperName = function() {
        try {

            APIFactory.getUsersByRole({ "role": "Developer" }, function(response) {
                if (response.status) {
                    vm.developerList = response.data
                } else {
                    vm.developerList = []
                }
                initMultipleSelect2("#developersList")
                $timeout(function() {
                    // $("#developersList").val(vm.imp.devId).trigger("change");
                    $("#developersList").select2({
                        maximumSelectionLength: 1
                    });
                }, 50)
            })
        } catch (err) {}
    }
    loadDeveloperName();

    var loadPeerReviewers = function() {
        try {

            APIFactory.getUsersByRole({ "role": "Reviewer" }, function(response) {
                if (response.status) {
                    vm.peerReviewers = response.data
                } else {
                    vm.peerReviewers = []
                }
                initMultipleSelect2("#peerReviewers")
            })
        } catch (err) {}
    }
    loadPeerReviewers();

    vm.developerLocation = [{ id: 1, value: "Onsite" }, { id: 2, value: "Offshore" }, { id: 3, value: "On-Call" }]

    /* Ticket Number */

    vm.showImpTtktNumberError = false;

    function itktAdded(itkt) {
        itkt = itkt.toUpperCase()
        vm.showImpTtktNumberError = false;

        var removeStatus = ["Closed", "Closed Unresolved", "Cancelled", "Close Requested"]
           var planId = vm.imp.planId.id;
           if(planId){
            APIFactory.getSystemListByPlan({
                "planId": planId
            }, function(response) {
                if (response.status) {
                   vm.platformById = response.data;
                   var platformDetails = _.filter(vm.platformById,function(pObj){
                    return pObj.platformId.name == "Travelport";
                   });
                   if(platformDetails.length > 0){
                    APIFactory.getProblemTicket({ "ticketNumber": itkt }, function(response) {
                        if (response.status && response.data.length > 0) {
                            vm.showImpTtktNumberError = false
                            var specifiedTicket = _.where(response.data, { "refNum": itkt })
                            if (specifiedTicket.length > 0) {
                                if (removeStatus.indexOf($.trim(specifiedTicket[0].status)) >= 0) {
                                    vm.showImpTtktNumberError = true;
                                    vm.iticketErrorMessage = itkt + " is " + $.trim(specifiedTicket[0].status)
                                    $("#iprobTktNo").tagit("removeTagByLabel", itkt);
                                }
                            } else {
                                $("#iprobTktNo").tagit("removeTagByLabel", itkt);
                            }
                        } else {
                            $("#iprobTktNo").tagit("removeTagByLabel", itkt);
                            Toaster.sayError(response.errorMessage)
                        }
                    })
                }
                } else {
                    vm.platformById = []
                }
            })
           }
    }

    function tktRemoved(itkt) {
        vm.showTtktNumberError = false;
    }

    $("#iprobTktNo").tagit({
        singleField: true,
        caseSensitive: false,
        singleFieldNode: $('#iprobTktNo'),
        afterTagAdded: function(event, ui) {
            itktAdded($(ui.tag).find("span").html())
        }
    })

    $scope.cancel = function() {
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


});