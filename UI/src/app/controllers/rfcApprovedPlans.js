dashboard.controller("rfcApprovedPlansCtrl", function ($rootScope, $scope, $state, $location, appSettings,
    Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger, WSService, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    vm.rfcApproved = []
    var apiBase = appSettings.apiBase;
    vm.currentUser = $rootScope.home_menu
	$rootScope.isApprovedEdit = false;
    Paginate.refreshScrolling();
    // /* Developer Area */
    // $scope.loadDevTaskList = function () {
    //     var getBase = IService.initImpl($scope, vm, "mytasks")
    //     _.extend($scope, getBase.scope)
    //     _.extend(vm, getBase.vm)
    // }
	
	var columnsToBeSorted = ["planid","rfcnumber","targetsystem","loaddatetime"]
	initialLoading();
	function initialLoading() {
		initTableSettings()
		tableAttr.isApprovedPlans = true;
		if(tableAttr.isApprovedPlans){

		}
		 APIFactory.getRFCInboxPlans(tableAttr, function (response) {
			if (response.status && response.data != null) {
				if (Object.keys(response.data).length > 0) {
					vm.rfcApproved = response.data
					$scope.tableConfig.totalItems = response.count
					vm.totalNumberOfItem = response.count
					$rootScope.saveformData()
				} else {
					vm.rfcApproved = []
				}

			} else {
				vm.rfcApproved = []
			}
		})
	}
	
	function initTableSettings()  {
		$scope.tableConfig = Paginate.tableConfig()
		// $scope.tableConfig.rfcApprovedpageSize = 10 // Default page Size for RFC
		// $scope.tableDefaultValue = Paginate.defaultPageValue()
		// $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for RFC
		// $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
		$scope.pageSizeList = Paginate.pageSizeList();
		tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize);
		$scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
	}
	
	
	$scope.switchPageSize = function () {
		// tableAttr.offset = 0
		// tableAttr.limit = $scope.tableConfig.rfcApprovedpageSize
		// $scope.tableConfig.currentPage = 1
		// Paginate.switchPageSize($scope, tableAttr)
		tableAttr.offset = 0
		tableAttr.limit = $scope.tableConfig.pageSize;
		$scope.tableConfig.currentPage = 1
		localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
		tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
		$rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
		initialApiRFCApproved(tableAttr)
	}
	
	$scope.pageChangeHandler = function (num) {
		if (vm.rfcApproved[tableAttr.name] && $scope.tableConfig.lastLoadedPage === num) {
			return;
		}
		$scope.tableConfig.lastLoadedPage = num
		tableAttr.offset = num - 1
		tableAttr.filter = vm.filterText ? vm.filterText : ""
		localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
		tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
		$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
		initialApiRFCApproved(tableAttr)
	};

	$scope.searchPlanData = function (searchData) {
		$scope.enableRecentPage = false;
		initTableSettings()
		initialLoading()
		$scope.enableRecentPage = true;
		// if (recentPage) {
		//     $scope.tableConfig.currentPage = recentPage + 1
		//     tableAttr.offset = recentPage
		// }
		isPopupShown = false;
		yesChanged = false;
		tableAttr.filter = searchData ? searchData : ""
		tableAttr.offset = 0;
		initialApiRFCApproved(tableAttr)
	}
	$scope.refreshData = function () {
		tableAttr.filter = vm.filterText ? vm.filterText : ""
		vm.searchPlanData = ""
		initTableSettings()
		tableAttr.isApprovedPlans = true;
		tableAttr.offset = $rootScope.paginateValue
		initialApiRFCApproved(tableAttr)
	}
	
	$scope.sort = function (columnName) {
		var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
		tableAttr = lSort.tableAttr
		$scope.tableConfig = lSort.tableConfig
		$scope.sortColumn = lSort.sortColumn
		initialApiRFCApproved(tableAttr)
	}
	
	function initialApiRFCApproved(tableAttr) {
		APIFactory.getRFCInboxPlans(tableAttr, function (response) {
			if (response.status && response.data != null) {
				if (Object.keys(response.data).length > 0) {
					vm.rfcApproved = response.data
					$scope.tableConfig.totalItems = response.count
					vm.totalNumberOfItem = response.count
					$rootScope.saveformData()
				} else {
					vm.rfcApproved = []
				}

			} else {
				vm.rfcApproved = []
			}
		})
	}
	
	//RFC Approved Expand
	
	$scope.rfcApprovedExpand = function(pObj, id, data) {
		APIFactory.getPlan({"id": id}, function(response) {
			pObj.planObj = response.data.impPlan;		
			var getBase = IPService.getExpandView($scope, vm)
			// var getBase2 = IPService.initPlan($scope, vm)
			// _.extend($scope, getBase2.scope)
			// _.extend(vm, getBase2.vm)
			$scope.loadSystemImplApproverList(pObj.planObj, '', data)
			var getBase1 = IPService.initRFC($scope, vm, pObj)
		})
	}
	

});