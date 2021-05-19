dashboard.controller("impPlanCtrl", function($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, IPService, Upload, Paginate, WSService, Access) {

    var apiBase = appSettings.apiBase;
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    $rootScope.prevSaveButton = false;
    $templateCache.removeAll();
    // vm.developerName = vm.currentUser = getUserData("displayName")
    vm.currentUser = $rootScope.home_menu
    $scope.initialSort = true
    vm.implList = []
    vm.approvalsList = []
    vm.rfcDetails={}
    $scope.enableRecentPage = true
    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)
	var saveFlag  = false;
    vm.planStatusList = Access.getAllPlanStatus()
    Paginate.refreshScrolling();
    $scope.statusFilter = function(status) {
        var selectstatus = _.findKey(vm.planStatusList, function(item) { return item == status });
        vm.filterText = selectstatus ? selectstatus : "";
        $scope.refresh()
    }

    $scope.RFCClick = function (pObj) {
		var getBase1 = IPService.initRFC($scope, vm, pObj)
    }
	
	// $scope.rfcProcessList = function(pObj, system) {
	// }
    
    var rfcSystemDetails;
    $scope.downloadTestCaseImpl = function(fileName, implementation) {
        try {
            var params = {
                "implId": implementation.id,
                "planId": implementation.planId.id,
                "testFile": fileName
            }
            APIFactory.downloadTestCase(params, function(response) {
                if (response.status) {
                    var resposeStr = base64ToArrayBuffer(response.data)
                    var file = new Blob([resposeStr], { type: response.metaData });
                    saverAs(file, fileName)
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        } catch (err) {}

    }

}).directive("rejectStatus", function() {
    return function(scope, element, attr, model) {
        var status = ["ACTIVE", "SUBMITTED", "APPROVED", "DEPLOYED_IN_QA_FUNCTIONAL", "PASSED_FUNCTIONAL_TESTING", "DEPLOYED_IN_QA_REGRESSION", "PASSED_REGRESSION_TESTING", "DEPLOYED_IN_PRE_PRODUCTION", "PASSED_ACCEPTANCE_TESTING", "READY_FOR_PRODUCTION_DEPLOYMENT","PARTIALLY_DEPLOYED_IN_PRODUCTION", "ONLINE", "FALLBACK"]
        if (status.indexOf(attr.rejectStatus) >= 1 && status.indexOf(attr.rejectStatus) < 9) {
            $(element).show()
        } else {
            $(element).hide()
        }
    }
})

function updateImplementation(planId, impId) {
    location.href = "#/app/updateImp/" + planId + "/" + impId
}

function expandCollapse(e) {
    if ($(e).html() === "+") {
        $(e).html("-")
    } else {
        $(e).html("+")
    }
}
