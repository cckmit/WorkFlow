dashboard.controller("defaultCPUCtrl", function($rootScope, $templateCache, appSettings, $scope,
    $state, $location, Toaster, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;

    vm.systemCPUList = []
        
    Paginate.refreshScrolling();

    var cpuType = {
        "prod": "PRODUCTION",
        "native_tos": "NATIVE_TOS"
            /* ,
                    "preprod_tos": "PRE_PROD_TOS" */
    }

    vm.cpu_action = {}


    function loadSystemCPUList() {
        APIFactory.getSystemList(function(response) {
            if (response.status && response.data.length > 0) {
                vm.systemCPUList = _.sortBy(response.data, "name")
                _.each(vm.systemCPUList, function(sObj) {
                    vm.cpu_action[sObj.id] = false
                })
                APIFactory.getTOSServerListBySystemId({ "id": "", "type": cpuType["prod"] }, function(response) {
                    vm.system_prodlist = response.data
                    _.each(vm.systemCPUList, function(systemObj) {
                        var defaultCPU = _.where(vm.system_prodlist[systemObj.id], { "id": systemObj.defaultProdCpu })
                        if (defaultCPU.length > 0) {
                            systemObj.defaultProdCpu = defaultCPU[0]
                        }
                    })
                })
                APIFactory.getTOSServerListBySystemId({ "id": "", "type": cpuType["native_tos"] }, function(response) {
                        vm.system_nativelist = response.data
                        _.each(vm.systemCPUList, function(systemObj) {
                            var defaultCPU = _.where(vm.system_nativelist[systemObj.id], { "id": systemObj.defaultNativeCpu })
                            if (defaultCPU.length > 0) {
                                systemObj.defaultNativeCpu = defaultCPU[0]
                            }
                        })
                        $rootScope.saveformData()
                    })
                    /* APIFactory.getTOSServerListBySystemId({ "id": "", "type": cpuType["preprod_tos"] }, function(response) {
                        vm.system_preprodlist = response.data
                        _.each(vm.systemCPUList, function(systemObj) {
                            var defaultCPU = _.where(vm.system_preprodlist[systemObj.id], { "id": systemObj.defaultPreProdCpu })
                            if (defaultCPU.length > 0) {
                                systemObj.defaultPreProdCpu = defaultCPU[0]
                            }
                        })
                    }) */
            } else {
                vmid.systemsList = []
            }
        })
    }
    loadSystemCPUList()


    $scope.editDefault = function(systemId) {
        vm.cpu_action[systemId] = true
    }

    $scope.refreshCPUList = function(systemId) {
        loadSystemCPUList()
    }

    $scope.submitDefault = function(systemObj) {
        if (!_.isObject(systemObj.defaultProdCpu)) {
            systemObj.defaultProdCpu = parseInt(systemObj.defaultProdCpu)
        }
        if (!_.isObject(systemObj.defaultNativeCpu)) {
            systemObj.defaultNativeCpu = parseInt(systemObj.defaultNativeCpu)
        }
        /*  if (!_.isObject(systemObj.defaultPreProdCpu)) {
             systemObj.defaultPreProdCpu = parseInt(systemObj.defaultPreProdCpu)
         } */

        if (_.isObject(systemObj.defaultProdCpu)) {
            systemObj.defaultProdCpu = parseInt(systemObj.defaultProdCpu.id)
        }

        if (_.isObject(systemObj.defaultNativeCpu)) {
            systemObj.defaultNativeCpu = parseInt(systemObj.defaultNativeCpu.id)
        }
        /*  if (_.isObject(systemObj.defaultPreProdCpu)) {
             systemObj.defaultPreProdCpu = parseInt(systemObj.defaultPreProdCpu.id)
         } */

        APIFactory.updateSystem(systemObj, function(response) {
            if (response.status) {
                vm.cpu_action[systemObj.id] = false
                loadSystemCPUList()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

})