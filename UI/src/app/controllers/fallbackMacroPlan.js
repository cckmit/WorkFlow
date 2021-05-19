dashboard.controller("fallbackmacroPlanCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Paginate, $mdDialog, WSService, $timeout) {
    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    $rootScope.prevSaveButton = false;
    $templateCache.removeAll();
    vm.mandatoryComment = false;
    vm.comment = {}
    vm.ar_status = {}

    vm.currentUser = $rootScope.home_menu
    vm.implList = []
    vm.approvalsList = []
    var selectedPlan;
    $scope.showAUXTYPETab = true;

    Paginate.refreshScrolling();
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
    
   
    var tableAttr, columnsToBeSorted;
    var initTableSettings = function () {
        $scope.tableConfig = Paginate.tableConfig()
        $scope.pageSizeList = Paginate.pageSizeList()
        tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
        columnsToBeSorted = ["id", "loadType", "systemLoadList.loadDateTime"]
        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
        loadList(tableAttr)
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
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadList(tableAttr)
    }
    function refreshMacro() {
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadList(tableAttr)
    }

    function loadList(tableAttr) {
        if ($scope.macroInitSort) {
            tableAttr.orderBy = {
                "systemLoadList.loadDateTime": "asc"
            }
            $scope.sortColumn["systemLoadList.loadDateTime"]["asc"] = true;
            $scope.macroInitSort = false;
        }
        vm.loadsetReadyList = []
        if(getUserData("userRole") == "TechnicalServiceDesk"){
            APIFactory.fallbackMacroHeaderPlans(tableAttr, function (response) {
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
        }else{
            APIFactory.devManagerfallbackMacroList(tableAttr, function (response) {
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
        
    }

    //New Fallback starts..

    $scope.macroFallbackLoader = {}
    $scope.markAuxAsFallback = function (ev, id) {
        scopeDuplicate = $scope
        $mdDialog.show({
            controller: fallbackMessageCtrl,
            controllerAs: "fb",
            templateUrl: 'html/templates/fallbackMessage.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "id": id
            }
        }).then(function (answer) {

        }, function () {

        });
        function fallbackMessageCtrl($scope, id) {
            var fb = this;
            fb.impPlanId = id;
            fb.showMessageError = false
            $scope.proceedFallback = function () {
                if (!fb.fallbackMessage) {
                    fb.showMessageError = true
                    return
                } else {
                    fb.showMessageError = false
                }
                var paramObj = {
                    "id": id,
                    "rejectReason": fb.fallbackMessage
                }
                // $scope.auxFallbackLoader[id] = true
                scopeDuplicate.macroFallbackLoader[id] = true
                $mdDialog.hide()
                APIFactory.macroHeaderFallback(paramObj, function (response) {
                    if (response.status) {
						Toaster.sayStatus("Fallback In-progress")
                        //refreshMacro();
                    } else {
                        scopeDuplicate.macroFallbackLoader[id] = false
                         Toaster.sayError(response.errorMessage)
                    }
                })
				onlineMacroFallbackBuild(id);
            }
            $scope.cancel = function () {
                $mdDialog.cancel();
                
        }
    }
    function onlineMacroFallbackBuild(id){
        WSService.initPlanAuxFallback(function(response) {
            if (response.status) {
                Toaster.sayStatus(response.message.message)
                $rootScope.saveformData()
            } else {
                if (response.errorMessage.length != 0) {
                    if ($rootScope.currentState == "app.fallbackMacroPlan" || typeof $rootScope.currentState == "undefined") {
                        Toaster.sayError(response.errorMessage)
                        $rootScope.saveformData()
                    } else {
                        Toaster.sayStatus(response.errorMessage)
                        $rootScope.saveformData()
                    }
                }
            }
            // $scopeDuplicate.macroFallbackLoader[id] = false;
            refreshMacro()
        })
        WSService.initFallbackBuildProcess(function(response){
			scopeDuplicate.macroFallbackLoader[id] = false
            if(response.status){
				Toaster.sayStatus("Fallback Success") 
                refreshMacro();
                $rootScope.saveformData()
                Toaster.sayStatus(response.status.message)               
            }
            else{
                Toaster.sayError(response.errorMessage)
                $rootScope.saveformData()
            }
            
        })
       
    }
            };
        
    




    $scope.role = "devManager"
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

});

function updateImplementation(planId, impId) {
    location.href = "#/app/updateImp/" + planId + "/" + impId
}