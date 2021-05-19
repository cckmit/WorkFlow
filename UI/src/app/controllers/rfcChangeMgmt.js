dashboard.controller("rfcChangeMgmtCtrl", function ($rootScope, $scope, $state, $location, appSettings,
	Toaster, apiService, APIFactory, $mdDialog, Upload, IPService, IService, Paginate, WFLogger, WSService, Access) {
	var vm = this;
	$rootScope.titleHeading = $state.current.data.pageTitle;
	vm.rfcInboxList = []
	var pObj = [];
	var apiBase = appSettings.apiBase;
	vm.currentUser = $rootScope.home_menu

	Paginate.refreshScrolling();


	var columnsToBeSorted = ["planid", "rfcnumber", "targetsystem", "loaddatetime", "rfcstatus"]
	initialLoading();
	function initialLoading() {
		initTableSettings()
		APIFactory.getRFCInboxPlans(tableAttr, function (response) {
			if (response.status && response.data != null) {
				if (Object.keys(response.data).length > 0) {
					vm.rfcInboxList = response.data
					$scope.tableConfig.totalItems = response.count
					vm.totalNumberOfItem = response.count
					$rootScope.saveformData()
				} else {
					vm.rfcInboxList = []
				}

			} else {
				vm.rfcInboxList = []
			}
		})
	}

	function initTableSettings() {
		$scope.tableConfig = Paginate.tableConfig()
		// $scope.tableConfig.rfcPageSize = 10;
		// $scope.tableDefaultValue = Paginate.defaultPageValue()
		// $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for RFC
		$scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
		$scope.pageSizeList = Paginate.pageSizeList();
		tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize);

		$scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)

	}

	$scope.switchPageSize = function () {
		// tableAttr.offset = 0
		// tableAttr.limit = $scope.tableConfig.rfcPageSize
		// $scope.tableConfig.currentPage = 1
		// Paginate.switchPageSize($scope, tableAttr)
		tableAttr.offset = 0
		tableAttr.limit = $scope.tableConfig.pageSize;
		$scope.tableConfig.currentPage = 1
		localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
		tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
		$rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
		initialApiRFCMgnt(tableAttr)
	}

	$scope.pageChangeHandler = function (num) {
		if (vm.rfcInboxList[tableAttr.name] && $scope.tableConfig.lastLoadedPage === num) {
			return;
		}
		$scope.tableConfig.lastLoadedPage = num
		tableAttr.offset = num - 1
		tableAttr.filter = vm.filterText ? vm.filterText : ""
		localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
		tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
		$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
		initialApiRFCMgnt(tableAttr)
	};

	$scope.searchPlanData = function (searchData) {
		$scope.enableRecentPage = false;
		initTableSettings()
		$scope.enableRecentPage = true;
		// if (recentPage) {
		//     $scope.tableConfig.currentPage = recentPage + 1
		//     tableAttr.offset = recentPage
		// }
		isPopupShown = false;
		yesChanged = false;
		tableAttr.filter = searchData ? searchData : ""
		tableAttr.offset = 0;
		initialApiRFCMgnt(tableAttr)
	}

	$scope.refreshData = function (recentPage) {
		$scope.enableRecentPage = false;
		initTableSettings()
		$scope.enableRecentPage = true;
		if (recentPage) {
			$scope.tableConfig.currentPage = recentPage + 1
			tableAttr.offset = recentPage
		}
		tableAttr.offset = $rootScope.paginateValue
		isPopupShown = false;
		yesChanged = false;
		tableAttr.filter = vm.filterText ? vm.filterText : ""
		vm.searchPlanData = ""
		initialApiRFCMgnt(tableAttr)
	}

	$scope.sort = function (columnName) {
		var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
		tableAttr = lSort.tableAttr
		$scope.tableConfig = lSort.tableConfig
		$scope.sortColumn = lSort.sortColumn
		initialApiRFCMgnt(tableAttr)
	}

	function initialApiRFCMgnt(tableAttr) {
		APIFactory.getRFCInboxPlans(tableAttr, function (response) {
			if (response.status && response.data != null) {
				if (Object.keys(response.data).length > 0) {
					vm.rfcInboxList = response.data
					$scope.tableConfig.totalItems = response.count
					vm.totalNumberOfItem = response.count
					$rootScope.saveformData()
				} else {
					vm.rfcInboxList = []
				}

			} else {
				vm.rfcInboxList = []
			}
		})
	}

	// RFC Expand view 

	$scope.rfcExpand = function (pObj, id, data) {
		APIFactory.getPlan({ "id": id }, function (response) {
			pObj.planObj = response.data.impPlan;
			var getBase = IPService.getExpandView($scope, vm)
			$scope.loadSystemImplApproverList(pObj.planObj, '', data)
			var getBase1 = IPService.initRFC($scope, vm, pObj, data)
		})
	}

	// $scope.RFCClick = function(pObj) {
	// var getBase1 = IPService.initRFC($scope, vm, pObj)
	// }

});