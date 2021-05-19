dashboard.controller("devManagerCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, $mdDialog, WSService, $timeout) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    $rootScope.prevSaveButton = false;
    $templateCache.removeAll();
    vm.mandatoryComment = false;
    vm.comment = {}
    vm.ar_status = {}
    vm.progress_status = {}
    vm.loadsetReadyList = []

    Paginate.refreshScrolling();
    vm.currentUser = $rootScope.home_menu
    vm.implList = []
    vm.approvalsList = []
    var selectedPlan;
    $scope.showAUXTYPETab = true;

    $scope.RFCClick = function (pObj) {
		var getBase1 = IPService.initRFC($scope, vm, pObj)
	}

    vm.isMacroHeader = false
    $scope.initApproveReject = function () { // Initialize from IPService
        $(".dropdown-menu.devManager li a").click(function () {
            selectedPlan = JSON.parse($(this).attr("pId"))

            if ($(this).text() === "Approve") {
                vm.comment.mandatoryComment = false;
                vm.comment.statusComment = "Approve";
                vm.comment.scope = this;
            } else if ($(this).text() === "Reject") {
                vm.comment.mandatoryComment = true;
                vm.comment.statusComment = "Reject";
                vm.comment.scope = this;
            }

            vm.isMacroHeader = selectedPlan.macroHeader
            $scope.$apply()
            $("#commentModal").modal("show")
        });
    }


    vm.comment.commentText = ""
    $scope.dmApprove = function () {


        if (vm.comment.mandatoryComment && (!vm.comment.commentText || vm.comment.commentText.length == 0)) {
            Toaster.sayWarning("Manager Comments are mandatory")
            return
        }

        vm.ar_status[selectedPlan.id] = true
        var paramObj = {
            "planId": selectedPlan.id,
            "comments": vm.comment.commentText
        }

        APIFactory.dmApprovePlan(paramObj, {}, function (response) {
            $("#commentModal").modal("hide")
            if (response.status) {
                Toaster.saySuccess("Plan (" + selectedPlan.id + ") approved successfully")
                vm.comment.commentText = ""
            } else {
                vm.ar_status[selectedPlan.id] = false
                Toaster.sayError(response.errorMessage)
            }
            $scope.refresh()
        })
    }

    $scope.loadTypePlans = function (type) {
        if (type == "_AUXTYPE") {
            $scope.showAUXTYPETab = true;
            $scope.showMACHEADERTab = false;
            $scope.refresh()
        } else {
            $scope.showAUXTYPETab = false;
            $scope.showMACHEADERTab = true;
            $scope.refreshMacro();
        }
    }

    $scope.dmReject = function () {
        if (vm.comment.commentText.length == 0) {
            Toaster.sayWarning("Comment is mandatory")
            return
        }
        vm.ar_status[selectedPlan.id] = true
        var paramObj = {
            "id": selectedPlan.id,
            "comments": vm.comment.commentText
        }
        $("#commentModal").modal("hide")
        APIFactory.dmRejectPlan(paramObj, function (response) {
            if (response.status) {
                // $scope.refresh()
                vm.comment.commentText = ""
            } else {
                vm.ar_status[selectedPlan.id] = false
                Toaster.sayError(response.errorMessage)
            }
            $scope.refresh()
        })
    }
    $scope.refreshMacro = function () {
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadList(tableAttr)
    }
    var tableAttr, columnsToBeSorted;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        columnsToBeSorted = ["id", "loadType", "loaddatetime"]
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
    }
    initTableSettings()
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
        loadList(tableAttr)
    }
    $scope.pageChangeHandler = function (num) {
        if (vm.loadsetReadyList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.macroSort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadList(tableAttr)
    }
    function loadList(tableAttr) {
        loadMacroHeadersList(tableAttr)
    }

    function loadMacroHeadersList(tableAttr) {
        if ($scope.macroInitSort) {
            tableAttr.orderBy = {
                "systemLoadList.loadDateTime": "asc"
            }
            $scope.sortColumn["systemLoadList.loadDateTime"]["asc"] = true;
            $scope.macroInitSort = false;
        }
        APIFactory.tsdMacroHeaderList(tableAttr, function (response) {
            if (response.status) {
                vm.loadsetReadyList = _.uniq(response.data, "id")
                if (vm.loadsetReadyList.length > 0) {
					$scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    var systemAttr = typeof tableAttr.orderBy == "undefined" ? {} : tableAttr.orderBy
                    if (JSON.stringify(systemAttr).indexOf("systemLoadList") >= 0) {
                        systemAttr = JSON.parse(JSON.stringify(systemAttr).replace(/systemloadlist./gi, ""))
                    } else {
                        systemAttr = {
                            "loadDateTime": "asc"
                        }
                    }
                    APIFactory.getSystemLoadByPlan({
                        "ids": _.pluck(vm.loadsetReadyList, "id"),
                        "orderBy": systemAttr
                    }, function (response) {
                        if (response.status && response.data.length > 0) {
                            var rSystemList = response.data
                            _.each(vm.loadsetReadyList, function (pObj) {
                                pObj.systemLoadList = []
                                _.each(rSystemList, function (rsObj) {
                                    if (rsObj.systemLoad.planId.id == pObj.id) {
                                        pObj.systemLoadList.push(rsObj.systemLoad)
                                    }
                                })
                            })
                            $rootScope.saveformData()
                        }
                    })
                }
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    $scope.rejectMacroHeader = function (ev, id) {
        $mdDialog.show({
            controller: rejectMessageCtrl,
            controllerAs: "rj",
            templateUrl: 'html/templates/rejectMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            vm:vm,
            locals: {
                "id": id
            }
        })
            .then(function (answer) {
                $scope.refreshMacro()
            }, function () {

            });

        function rejectMessageCtrl($scope, id,vm) {
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
                    "id": id,
                    "rejectReason": rj.rejectMessage
                }
                vm.showProgressMacro = true;
                vm.progress_status[id] = true;
                $mdDialog.hide();
                APIFactory.devmanagerRejectMacro(paramObj, function (response) {
                    if (response.status) {
                        vm.showProgressMacro = false;
                        vm.progress_status[id] = false;
                        Toaster.saySuccess("Plan Rejected Successfully")
                        // $mdDialog.hide();
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                    initTableSettings()
                    loadList(tableAttr)
                })
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }
    }



    $scope.acceptMacroHeader = function (id) {
        // vm.loader[id] = {};
        // vm.loader[id].markasOnline = true;
        vm.progress_status[id] = true;
        APIFactory.tsdMarkOnline({ "id": id }, function (response) {
            // vm.loader[id].markasOnline = false;
            if (response.status) {
                Toaster.sayStatus("Set Online In-Progress")
                // $scope.refreshMacro()
            } else {
                vm.progress_status[id] = false;
                Toaster.sayError(response.errorMessage)
            }
        })
        onlineMacroHeader(id);
    }
    function onlineMacroHeader(id){
        WSService.initOnlineBuildProcess(function(response) {
            var dataFlag = include(response.status.toLowerCase(),'successfully')
            if(dataFlag){
            vm.progress_status[id] = false;
                Toaster.sayStatus(response.status)
                Toaster.saySuccess(response.status)
                $rootScope.saveformData()
            }
            else{
                vm.progress_status[id] = false;
				Toaster.sayStatus(response.status)
                Toaster.sayError(response.status)
                $rootScope.saveformData()
			}
            $scope.refreshMacro() 
            
        })
    }
	
	function include(container,value){
		var returnValue = false;
		var pos = container.indexOf(value)
		if(pos > 0){
			returnValue = true;
		}
		return returnValue
		
	}

    $scope.role = "devManager"
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

});

function updateImplementation(planId, impId) {
    location.href = "#/app/updateImp/" + planId + "/" + impId
}