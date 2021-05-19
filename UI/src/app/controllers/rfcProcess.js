dashboard.controller("rfcProcessCtrl", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Upload, Paginate, WSService, Access) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    $rootScope.prevSaveButton = false;
    $templateCache.removeAll();
    // vm.developerName = vm.currentUser = getUserData("displayName")
    Paginate.refreshScrolling();
    vm.currentUser = $rootScope.home_menu
    $scope.initialSort = true
    vm.implList = []
    vm.approvalsList = []
    vm.rfcDetails = {}
    // vm.saveRFC = false;
    $scope.enableRecentPage = true
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

    vm.planStatusList = Access.getAllPlanStatus()

    $scope.statusFilter = function (status) {
        var selectstatus = _.findKey(vm.planStatusList, function (item) { return item == status });
        vm.filterText = selectstatus ? selectstatus : "";
        $scope.refresh()
    }
   
    
    // $scope.updateRFC = function(rfcDetails){
    //     vm.rfcDetails = rfcDetails;
    // 
	vm.rfcDataDetails = []
    $scope.saveRFC = function (rfcDetails,planObj, e) {
		_.each(planObj.systemLoadList, function(Obj) {
			if(e === Obj.systemId.name) {
				vm.rfcDataDetails = Obj
			}
		})
        var params = {
            "rfcNumber": rfcDetails[e].rfcNumber,
            "rfcDesc": rfcDetails[e].rfcDesc,
            "impactLevel": rfcDetails[e].impactLevel,
            "configItem": rfcDetails[e].configItem,
            "vsFlag": rfcDetails[e].vsFlag,
            "vsArea": rfcDetails[e].vsArea,
            "vsTestFlag": rfcDetails[e].vsTestFlag,
            "vsChangeFlag": rfcDetails[e].vsChangeFlag,
            "planId": planObj,
			"systemLoadId": vm.rfcDataDetails
        }
        APIFactory.getupdateRFCDetail(params, function (response) {
            if (response.status) {
                $

            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }

    // Get-call

    sysDet = [];
		sys = [];
        $scope.rfcProcessList = function (planId) {
            $scope.plan_id = planId.id;
			if(planId.rfcFlag){
            APIFactory.getRFCProcessList({ "planIds": $scope.plan_id }, function (l_response) {
                if (l_response.status) {
                  vm.rfcDetails = l_response.data;
				}else{ 
                    Toaster.sayError(response.errorMessage);    
                }
            })
			}
			
        }

})
