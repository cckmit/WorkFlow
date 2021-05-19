dashboard.controller("systemViewCtrl", function ($rootScope, $state, $stateParams, $scope, $timeout, $location, appSettings, Toaster, $http,
	fImplementationPlanValidate, $mdDialog, apiService, APIFactory, WFLogger, WSService, freezeService, IPService, Access, Paginate, $filter) {

	var vm = this;
	$rootScope.titleHeading = $state.current.data.pageTitle;
	vm.performanceData = {};
	var apiBase = appSettings.apiBase;
	var searchsys = [];
	var userActionSearch = [];
	$scope.searchKeyClickedOnScreen = false;
	vm.currentUser = $rootScope.home_menu
	$scope.createFlag = false
	$scope.updateFlag = false
	var selectedRowId = null;
		
	vm.loadPlatforms = function () {
        try {
            APIFactory.getPlatformList({}, function (response) {
                if (response.status) {
                    vm.pList = response.data
                    if (vm.pList.length == 1) {
						vm.performanceData.hostName = vm.pList[0].name;
					}
                }
            })
        } catch (err) { }
    }
	
	$scope.performanceSwitchOfOff = function(switchs) {
		var params = {
			"systemInfo" : ((switchs == true) ? true : false),
			"transactionInfo" : false
		}
		APIFactory.updateAuditSettings(params, function(response) {
			if(response.status) {
				
			} else {
				Toaster.sayError(response.errorMessage);
			}
		})
	}
	
	vm.initialLoadPerformanceReport = function() {
		vm.performanceData.startDates = moment(2359, 'HH:mm').format("MM-DD-YYYY");
		vm.performanceData.endDates = moment(2359, 'HH:mm').format("MM-DD-YYYY");
		vm.performanceData.startTimes = "00:00:00";
		vm.performanceData.endTimes = moment().tz(getTimeZone()).format('HH:mm:ss');
		vm.performanceData.planId = null
		
		
		APIFactory.getAuditSettings(function(response) {
			if(response.status) {
				vm.switchOnOff = response.data.SystemView
			} else {
				Toaster.sayError(response.errorMessage);
			}
				
		})
		
		APIFactory.getSuperUserFromUsersList(function (response) {
			if (response.status) {
				vm.userNameList = response.data;
				setTimeout(function () {
					$("#performanceUserName").select2()

					$("#performanceUserName").on("select2:select", function (evt) {
						$scope.selectedSystems = $("#performanceUserName").select2("val")
						userNamePerformanceField()
					})
					$("#performanceUserName").val("0").trigger("change")

					$scope.selectedSystems = ["0"]
					userNamePerformanceField()
					$("#performanceUserName").on("select2:unselect", function () {
						$scope.selectedSystems = $("#performanceUserName").select2("val")
						$scope.selectedSystems = $scope.selectedSystems ? $scope.selectedSystems : []
						if ($scope.selectedSystems.indexOf("0") < 0) {
							_.each(vm.userNameList, function (sObj) {
								$("#performanceUserName option[value='" + sObj.id + "']").prop('disabled', false)
							})
							$("#performanceUserName").select2()
						}
						if ($scope.selectedSystems.length == 0) { // When User Unselects last selected systems
							$("#performanceUserName option[value='0']").prop('disabled', false)
							$("#performanceUserName").select2()
							
						}
						$scope.$digest()
					})
				}, 1000)
			}
		})
	}
	
	vm.initialLoadUserAction = function() {
		APIFactory.getuserAcionAPI(function (response) {
			if (response.status) {
				vm.userActionList = response.data;
				setTimeout(function () {
					$("#performanceUserAction").select2()

					$("#performanceUserAction").on("select2:select", function (evt) {
						$scope.selectedUserAction = $("#performanceUserAction").select2("val")
						userActionPerformanceField()
					})
					$("#performanceUserAction").val("0").trigger("change")

					$scope.selectedUserAction = ["0"]
					userActionPerformanceField()
					$("#performanceUserAction").on("select2:unselect", function () {
						$scope.selectedUserAction = $("#performanceUserAction").select2("val")
						$scope.selectedUserAction = $scope.selectedUserAction ? $scope.selectedUserAction : []
						if ($scope.selectedUserAction.indexOf("0") < 0) {
							_.each(vm.userActionList, function (sObj) {
								$("#performanceUserAction option[value='" + sObj + "']").prop('disabled', false)
							})
							$("#performanceUserAction").select2()
						}
						if ($scope.selectedUserAction.length == 0) { // When User Unselects last selected systems
							$("#performanceUserAction option[value='0']").prop('disabled', false)
							$("#performanceUserAction").select2()
						}
						$scope.$digest()
					})
				}, 1000)
			}
		})
	}
	
	function userNamePerformanceField() {
		if ($scope.selectedSystems.indexOf("0") >= 0) { // When User Selects "All" option
			_.each(vm.userNameList, function (sObj) {
				$("#performanceUserName option[value='" + sObj.id + "']").prop('disabled', true)
			})
		} else { // When User Selects Any one system
			$("#performanceUserName option[value='0']").prop('disabled', true)
		}
		$scope.$digest()
		$("#performanceUserName").select2()
	}
	
	function userActionPerformanceField() {
		if ($scope.selectedUserAction.indexOf("0") >= 0) { // When User Selects "All" option
			_.each(vm.userActionList, function (sObj) {
				$("#performanceUserAction option[value='" + sObj + "']").prop('disabled', true)
			})
		} else { // When User Selects Any one system
			$("#performanceUserAction option[value='0']").prop('disabled', true)
		}
		$scope.$digest()
		$("#performanceUserAction").select2()
	}

	vm.submitPerformanceData = function(data) {
		if(!vm.performanceData.hostName) {
			Toaster.sayWarning("Select Workflow Application");
			return;
		}
		if (!vm.performanceData.startDates) {
			Toaster.sayWarning("Provide Start Date");
			return;
		}
		if (!vm.performanceData.endDates) {
			Toaster.sayWarning("Provide End Date");
			return;
		}
		if (!vm.performanceData.startTimes) {
			Toaster.sayWarning("Provide Start Time");
			return;
		}
		if (!vm.performanceData.endTimes) {
			Toaster.sayWarning("Provide End Time");
			return;
		}
		// if ($scope.selectedUserAction.length == 0) {
			// Toaster.sayWarning("Provide User Action");
			// return;
		// }
		// if ($scope.selectedSystems.length == 0) {
			// Toaster.sayWarning("Provide User Name");
			// return;
		// }
		// if (!vm.performanceData.planId) {
			// Toaster.sayWarning("Provide End Time");
			// return;
		// }
		var fromDate = moment(vm.performanceData.startDates, "MM-DD-YYYY")
		var toDate = moment(vm.performanceData.endDates, "MM-DD-YYYY")
		var fromTime = moment(vm.performanceData.startTimes, "HH:mm")
		var toTime = moment(vm.performanceData.endTimes, "HH:mm")
		
		if (toTime.diff(fromTime, "minutes") < 0) {
			Toaster.sayWarning("From time should be lesser than To time ");
			return;
		}
		if (toTime.diff(fromTime, "minutes") === 0) {
			Toaster.sayWarning("From time and To time should not be same");
			return;
		}
		if (toDate.diff(fromDate, "minutes") < 0) {
			Toaster.sayWarning("Start Date should be lesser than End Date");
			return;
		}
		
		searchsys = $scope.selectedSystems
		vm.allUserName = $scope.selectedSystems
		if (searchsys[0] == 0) {
			// searchsys =	_.pluck(vm.userNameList, "id")
			searchsys = []
		}

		for (s in searchsys) {
			searchsys[s] = (searchsys[s])
		}
		
		userActionSearch = $scope.selectedUserAction
		vm.allUserAction = $scope.selectedUserAction
		if (userActionSearch[0] == 0) {
			// userActionSearch = (vm.userActionList)
			userActionSearch = []
		} 
		for (s in userActionSearch) {
			userActionSearch[s] = (userActionSearch[s])
		}		
		
		
		var performanceParams = {
			"inputParam": {
				// "startDate1": Access.formatAPIDate(vm.performanceData.startDates + " " + $.trim(vm.performanceData.startTimes) + ":00 " + moment(vm.performanceData.startDates + " " + $.trim(vm.performanceData.startTimes)).tz(getTimeZone()).format("ZZ")),
				"startDate": vm.performanceData.startDates + " " + $.trim(vm.performanceData.startTimes) + " " + moment(vm.performanceData.startDates + " " + $.trim(vm.performanceData.startTimes)).tz(getTimeZone()).format("ZZ"),
				// "endDate1": Access.formatAPIDate(vm.performanceData.endDates + " " + $.trim(vm.performanceData.endTimes) + ":00 " + moment(vm.performanceData.endDates + " " + $.trim(vm.performanceData.endTimes)).tz(getTimeZone()).format("ZZ")),
				"endDate": vm.performanceData.endDates + " " + $.trim(vm.performanceData.endTimes) + " " + moment(vm.performanceData.endDates + " " + $.trim(vm.performanceData.endTimes)).tz(getTimeZone()).format("ZZ"),
				"hostName": vm.performanceData.hostName,
				"planId": vm.performanceData.planId,
				"userAction": (userActionSearch),
				"userName": (searchsys)
			}

		}
		
		$scope.searchKeyClickedOnScreen = true;
		performanceSearch(performanceParams)
	}
	
	function performanceSearch(performanceParams) {
		vm.searchFlag = false;
		APIFactory.getTransactionInfo(performanceParams, function (response) {
			if (response.status) {
				vm.performanceSystemReport = response.data;
				vm.noDataFound = false;
				vm.searchFlag =  true;
				vm.startDates = moment(vm.performanceSystemReport.inputParam.startDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
				vm.endDates = moment(vm.performanceSystemReport.inputParam.endDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
				vm.startDate = vm.startDates.split(" ")[0];
				vm.endDate = vm.endDates.split(" ")[0];
				vm.startTime = vm.startDates.split(" ")[1];
				vm.endTime = vm.endDates.split(" ")[1];

			} else if (!response.status) {
					vm.noDataFound = true;
					Toaster.sayError(response.errorMessage)
				} else {
					vm.noDataFound = true;
				}
		})
	}
	
	$scope.exportReport = function () {
		APIFactory.exportSystemViewPerformance({}, vm.performanceSystemReport, function (response) {
			if (response.status) {
				var resposeStr = base64ToArrayBuffer(response.data)
				var file = new Blob([resposeStr], {
					type: response.metaData
				});
				var dateObj = new Date()
				var fileName = "EXPORT_SYSTEMVIEW_PERFORMANCE_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xls"
				saveAs(file, fileName)
			} else {
				Toaster.sayError(response.errorMessage);
			}
		})
	}

	vm.generateUserAction = function(ev) {
		$mdDialog.show({
            controller: userActionCtrl,
            controllerAs: "vm",
            templateUrl: 'html/templates/userAction.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            // locals: {
                // "id": id,
            // }
        }).then(function (answer) {
			// $scope.refresh()
		}, function () {

		});
	}
	
	function userActionCtrl($scope) {
		
		var tableAttr;
		var initTableSettings = function () {

			$scope.tableConfig = Paginate.tableConfig()
			$scope.tableConfig.pageSize = 10 
			$scope.pageSizeList = Paginate.pageSizeList1()
			tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
			// $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
		}
		
		initTableSettings()

		$scope.switchPageSize = function () {
			tableAttr.offset = 0
			tableAttr.limit = $scope.tableConfig.pageSize;
			$scope.tableConfig.currentPage = 1
			getApiActions(tableAttr)
		}

		$scope.pageChangeHandler = function (num) {
			if ($scope.getUserActionData && $scope.tableConfig.lastLoadedPage === num) {
				return;
			}
			$scope.tableConfig.lastLoadedPage = num
			tableAttr.offset = num - 1
			getApiActions(tableAttr)
		};
		
		$scope.pageChangeHandler($scope.tableConfig.currentPage)
		
		$scope.searchUserActionData = function(searchData) {
			initTableSettings()
			tableAttr.filter = searchData;
			tableAttr.offset = 0;
			getApiActions(tableAttr);
		}
		
		function getApiActions(tableAttr) {
			APIFactory.getApiActions(tableAttr, function(response) {
				if (response.status) {
					$scope.getUserActionData = response.data
					$scope.tableConfig.totalItems = response.count
					// $rootScope.saveformData()
				} else {
					$scope.getUserActionData = []
				}
			})
		}
		
		
		
		$scope.cancel = function () {
		  $mdDialog.cancel();
		};
		
		$scope.deleteUserActionData = function(ev, actionID, actionName, action) {
			APIFactory.removeApiActions({}, action, function (response) {
			  if (response.status) {
				Toaster.saySuccess("User Action deleted Successfully")
				// $scope.refresh()
				userActionCtrl($scope)
			  } else {
				Toaster.sayError(response.errorMessage)
			  }
			})
		}
		
		$scope.createUserActionData = function(ev) {
			$mdDialog.show({
				controller: userActionCreateCtrl,
				controllerAs: "vm",
				templateUrl: 'html/templates/userActionCreate.template.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				// locals: {
					// "id": id,
				// }
			}).then(function (answer) {
				// $scope.refresh()
			}, function () {

			});
		}
		
		$scope.editUserActionData = function(ev, Obj) {
			
			selectedRowId = Obj
					
			$mdDialog.show({
				controller: userActionUpdateCtrl,
				controllerAs: "vm",
				templateUrl: 'html/templates/userActionCreate.template.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose: false,
				locals: {
					"id": Obj.id,
				}
			}).then(function (answer) {
				// $scope.refresh()
			}, function () {

			});
		}
		
		
	}
	
	function userActionCreateCtrl($scope) {
		$scope.createFlag = true
		
		var vm = this;
			
		vm.data = {}
		
		$scope.createUserActionList = function(data) {
			 
			 if (!data.actionUrl) {
				Toaster.sayWarning("Provide Action URL");
				return;
			 }
			 if (!data.actionMethod) {
				Toaster.sayWarning("Provide Action Method");
				return;
			 }
			 if (!data.actionName) {
				Toaster.sayWarning("Provide Action Name");
				return;
			 }
			 if (!data.infoLevel) {
				Toaster.sayWarning("Provide Information Level");
				return;
			 }
			 if (!data.isSchedular) {
				Toaster.sayWarning("Provide Schedular");
				return;
			 }
			 
			var params = {
				"actionUrl" : vm.data.actionUrl,
				"actionMethod" : vm.data.actionMethod,
				"actionName" : vm.data.actionName,
				"isSchedular" : vm.data.isSchedular,
				"infoLevel" : vm.data.infoLevel,
				"id": null
			}
			
			APIFactory.saveApiActions(params,  function(response) {
				if (response.status) {
					Toaster.saySuccess("User Action Created Successfully");
					$mdDialog.hide();
				} else {
					Toaster.sayError(response.errorMessage);
				}
			})
		}
		
		$scope.cancelCreateUpdate = function () {
		  $mdDialog.cancel();
		};
	}
	function userActionUpdateCtrl($scope, id) {
		$scope.updateFlag = true
		
		var vm = this;
		vm.formData = selectedRowId

		$scope.updateUserActionList = function(data) {
			
			if (!data.actionUrl) {
				Toaster.sayWarning("Provide Action URL");
				return;
			 }
			 if (!data.actionMethod) {
				Toaster.sayWarning("Provide Action Method");
				return;
			 }
			 if (!data.actionName) {
				Toaster.sayWarning("Provide Action Name");
				return;
			 }
			 if (!data.infoLevel) {
				Toaster.sayWarning("Provide Information Level");
				return;
			 }
			
			var params = {
				"actionUrl" : vm.formData.actionUrl,
				"actionMethod" : vm.formData.actionMethod,
				"actionName" : vm.formData.actionName,
				"isSchedular" : vm.formData.isSchedular,
				"infoLevel" : vm.formData.infoLevel,
				"id": id
				
			}
			
			APIFactory.saveApiActions(params,  function(response) {
				if (response.status) {
					Toaster.saySuccess("User Action Updated Successfully");
					$mdDialog.hide();
					userActionCtrl($scope);
				} else {
					Toaster.sayError(response.errorMessage);
				}
			})
		}
		
		$scope.cancelCreateUpdate = function () {
		  $mdDialog.cancel();
		};
	}

	var callsTriggeredOnLoad = [vm.loadPlatforms(), vm.initialLoadPerformanceReport(), vm.initialLoadUserAction()]

})