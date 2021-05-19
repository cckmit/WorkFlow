dashboard.controller("plansDeployedInPreProdCtrl", function($rootScope, $scope, $state, $location, $timeout, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    $scope.userRole = getUserData('userRole')
    $scope.tssPlanDeployedInPreProdScreen = true;
    vm.planDeployedInPreProdList = []

    var getBase = IPService.initPlan($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)
    Paginate.refreshScrolling();
    $timeout(function() {
        $(".deploymentHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
    }, 1000)
    

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

    $scope.getLoadSetName = function(plan, systemId) {
        var st;
        for (sIndex in plan.systemLoadList) {
            if (plan.systemLoadList[sIndex].systemId.id == systemId) {
                st = plan.systemLoadList[sIndex].loadSetName
            }
        }
        return st ? st : "-"
    }
    
});