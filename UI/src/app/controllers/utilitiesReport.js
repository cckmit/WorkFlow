dashboard.controller("utilitiesReportCtrl", function ($rootScope, $state, $stateParams, $scope, $timeout, $location, appSettings, Toaster, $http,
	fImplementationPlanValidate, $mdDialog, apiService, APIFactory, WFLogger, WSService, freezeService, IPService, Access, Paginate) {

	var vm = this;
	$rootScope.titleHeading = $state.current.data.pageTitle;
	vm.sourceData = {};
	var apiBase = appSettings.apiBase;
	var tableAttr;
	tableAttr = Paginate.initTableAttr(1000)
	$scope.searchKeyClickedOnScreen = false;
	var searchsys = [];
	$scope.filteredPackageName = []
	Paginate.refreshScrolling();
	vm.source = [
		{ id: 1, name: 'Deployment details by manager' },
		{ id: 2, name: 'Deployment details by Functional Package' },
		{ id: 3, name: 'Source artifact update frequency' },
		{ id: 4, name: 'QA Effectiveness' }
	];
	$scope.sourceNames = vm.source[0].name

	vm.role = [
		{ id: 1, name: 'DevManager' },
		{ id: 2, name: 'Lead' },
	];
	vm.sourceData.role = vm.role[0].name;

	initiateSourceDetailsByManager()

	// On clicking on each tab function
	$scope.sourceNameList = function (id, name) {
		if (name == 'Deployment details by Functional Package') {
			initiateSourceDetailsByPackage();
		} else if (name == 'Deployment details by manager') {
			vm.sourceData.role = vm.role[0].name;
			vm.sourceDataUserRole()
			initiateSourceDetailsByManager()
		} else if (name == 'Source artifact update frequency') {
			initiateSourceDetailsByFrequency();
			$scope.searchKeyClickedOnScreen = false;
		} else if (name == 'QA Effectiveness') {
			$scope.searchKeyClickedOnScreen = false;
			initiateSourceDetailsByQAeffect()
		}
		$scope.sourceNames = name;

	}

	//on initial loading
	$scope.onLoadName = function () {
		setTimeout(function () {
			$("#sourceDataUserName").select2()

			$("#sourceDataUserName").on("select2:select", function (evt) {
				$scope.selectedUserName = $("#sourceDataUserName").select2("val")
				initSourceDataUserName()
			})
			$("#sourceDataUserName").val("0").trigger("change")
			$scope.selectedUserName = ["0"]
			initSourceDataUserName()
			$("#sourceDataUserName").on("select2:unselect", function () {
				$scope.selectedUserName = $("#sourceDataUserName").select2("val")
				$scope.selectedUserName = $scope.selectedUserName ? $scope.selectedUserName : []
				if ($scope.selectedUserName.indexOf("0") < 0) {
					_.each(vm.sourceDataRoles, function (sObj) {
						$("#sourceDataUserName option[value='" + sObj.id + "']").prop('disabled', false)
					})
					$("#sourceDataUserName").select2()
				}
				if ($scope.selectedUserName.length == 0) { // When User Unselects last selected systems
					$("#sourceDataUserName option[value='0']").prop('disabled', false)
					$("#sourceDataUserName").select2()
				}
				$scope.$digest()
			})
		}, 1000)
	}


	vm.sourceDataUserRole = function () {
		APIFactory.getUsersByRole({
			"role": vm.sourceData.role
		}, function (response) {
			if (response.status) {
				vm.sourceDataRoles = response.data;

				setTimeout(function () {
					$("#sourceDataUserName").select2()

					$("#sourceDataUserName").on("select2:select", function (evt) {
						$scope.selectedUserName = $("#sourceDataUserName").select2("val")
						initSourceDataUserName()
					})
					$("#sourceDataUserName").val("0").trigger("change")
					$scope.selectedUserName = ["0"]
					initSourceDataUserName()
					$("#sourceDataUserName").on("select2:unselect", function () {
						$scope.selectedUserName = $("#sourceDataUserName").select2("val")
						$scope.selectedUserName = $scope.selectedUserName ? $scope.selectedUserName : []
						if ($scope.selectedUserName.indexOf("0") < 0) {
							_.each(vm.sourceDataRoles, function (sObj) {
								$("#sourceDataUserName option[value='" + sObj.id + "']").prop('disabled', false)
							})
							$("#sourceDataUserName").select2()
						}
						if ($scope.selectedUserName.length == 0) { // When User Unselects last selected systems
							$("#sourceDataUserName option[value='0']").prop('disabled', false)
							$("#sourceDataUserName").select2()
						}
						$scope.$digest()
					})
				}, 1000)
			}
		})
	}

	// validating function on clicking on search button.
	$scope.searchFilter = function () {

		//Common field validation.

		if (!vm.sourceData.startDateTime) {
			Toaster.sayWarning("Provide Start Load Date");
			return;
		}
		if (!vm.sourceData.endDateTime) {
			Toaster.sayWarning("Provide End Load Date");
			return;
		}
		var from = moment(vm.sourceData.startDateTime, "MM-DD-YYYY")
		var to = moment(vm.sourceData.endDateTime, "MM-DD-YYYY")
		// if (to.diff(from, "minutes") === 0) {
		// Toaster.sayWarning("From time and To time should not be same");
		// return;
		// }
		if (to.diff(from, "minutes") < 0) {
			Toaster.sayWarning("Start Load Date should be lesser than End Load Date");
			return;
		}
		if ($scope.selectedSystems.length == 0) {
			Toaster.sayWarning("provide Target System");
			return;
		}

		// based on tab switch validation
		if ($scope.sourceNames == 'Deployment details by manager') {
			if (!vm.sourceData.role) {
				Toaster.sayWarning("Provide Role");
				return;
			}
			if ($scope.selectedUserName.length == 0) {
				Toaster.sayWarning("Provide Dev Manager Name");
				return;
			}
			// target system multiple select
			// var searchsys = []
			searchsys = $scope.selectedSystems

			if (searchsys[0] == 0) {
				searchsys = _.pluck(vm.sourceDetailsList, "name")
			}

			for (s in searchsys) {
				searchsys[s] = (searchsys[s])
			}

			// User Name multiple select
			var userNameSourceData = []

			userNameSourceData = $scope.selectedUserName

			if (userNameSourceData[0] == 0) {
				userNameSourceData = _.pluck(vm.sourceDataRoles, "id")
			}

			for (s in userNameSourceData) {
				userNameSourceData[s] = userNameSourceData[s]
			}
			var advSearchForm = {
				// "startDate": vm.sourceData.startDateTime + ":00 " + moment(vm.sourceData.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				// "endDate": vm.sourceData.endDateTime + ":00 " + moment(vm.sourceData.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				"startDate": vm.sourceData.startDateTime + " " + "00:00:00 " + moment(vm.sourceData.startDateTime).tz(getTimeZone()).format("ZZ"),
				"endDate": vm.sourceData.endDateTime + " " + "23:59:59 " + moment(vm.sourceData.endDateTime).tz(getTimeZone()).format("ZZ"),
				"systems": (searchsys),
				"reportType": "USER_DEPLOYMENT",
				"role": vm.sourceData.role,
				"userIds": (userNameSourceData)
			}
			$scope.searchKeyClickedOnScreen = true;

			searchData(advSearchForm)

		} else if ($scope.sourceNames == 'Deployment details by Functional Package') {
			if (vm.sourceData.functionalPackage == undefined || vm.sourceData.functionalPackage.length == 0) {
				Toaster.sayWarning("provide Functional Package");
				return;
			}
			// target system multiple select
			// var searchsys = []
			searchsys = $scope.selectedSystems

			if (searchsys[0] == 0) {
				searchsys = _.pluck(vm.sourceDetailsList, "name")
			}

			for (s in searchsys) {
				searchsys[s] = (searchsys[s])
			}

			// functional Package multiple select
			var functionalPackageSourceData = []
			functionalPackageSourceData = $scope.selectedFunctionalPackage

			if (functionalPackageSourceData != undefined) {
				if (functionalPackageSourceData[0] == 0) {
					// _.uniq($scope.sourcePackage.flat())
					functionalPackageSourceData = [];
				} else {
					for (s in functionalPackageSourceData) {
						functionalPackageSourceData[s] = functionalPackageSourceData[s]
					}
				}
			}

			var advSearchForm = {
				// "startDate": vm.sourceData.startDateTime + ":00 " + moment(vm.sourceData.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				// "endDate": vm.sourceData.endDateTime + ":00 " + moment(vm.sourceData.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				"startDate": vm.sourceData.startDateTime + " " + "00:00:00 " + moment(vm.sourceData.startDateTime).tz(getTimeZone()).format("ZZ"),
				"endDate": vm.sourceData.endDateTime + " " + "23:59:59 " + moment(vm.sourceData.endDateTime).tz(getTimeZone()).format("ZZ"),
				"systems": (searchsys),
				"reportType": "FUNC_PACKAGE",
				"funcAreas": (functionalPackageSourceData),
			}
			$scope.searchKeyClickedOnScreen = true;

			searchData(advSearchForm)
			functionalPackageSourceData = [];
		} else if ($scope.sourceNames == 'QA Effectiveness') {
			if ($scope.selectedUserName.length == 0) {
				Toaster.sayWarning("Provide User Name");
				return;
			}

			// User Name multiple select
			var userNameSourceData = []

			userNameSourceData = $scope.selectedUserName

			if (userNameSourceData[0] == 0) {
				userNameSourceData = _.pluck(vm.sourceDataRoles, "id")
			}

			for (s in userNameSourceData) {
				userNameSourceData[s] = userNameSourceData[s]
			}
			var advSearchForm = {
				// "startDate": vm.sourceData.startDateTime + ":00 " + moment(vm.sourceData.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				// "endDate": vm.sourceData.endDateTime + ":00 " + moment(vm.sourceData.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				"startDate": vm.sourceData.startDateTime + " " + "00:00:00 " + moment(vm.sourceData.startDateTime).tz(getTimeZone()).format("ZZ"),
				"endDate": vm.sourceData.endDateTime + " " + "23:59:59 " + moment(vm.sourceData.endDateTime).tz(getTimeZone()).format("ZZ"),
				"reportType": "USER_DEPLOYMENT",
				"role": "DevManager",
				"userIds": (userNameSourceData)
			}
			$scope.searchKeyClickedOnScreen = true;

			searchData(advSearchForm)
		} else if ($scope.sourceNames == 'Source artifact update frequency') {
			if (!vm.sourceData.startDateTime) {
				Toaster.sayWarning("Provide Start Load Date");
				return;
			}
			if (!vm.sourceData.endDateTime) {
				Toaster.sayWarning("Provide End Load Date");
				return;
			}
			var from = moment(vm.sourceData.startDateTime, "MM-DD-YYYY")
			var to = moment(vm.sourceData.endDateTime, "MM-DD-YYYY")
			// if (to.diff(from, "minutes") === 0) {
			// Toaster.sayWarning("From time and To time should not be same");
			// return;
			// }
			if (to.diff(from, "minutes") < 0) {
				Toaster.sayWarning("Start Load Date should be lesser than End Load Date");
				return;
			}
			if ($scope.selectedSystems.length == 0) {
				Toaster.sayWarning("provide Target System");
				return;
			}
			if ($scope.selectedFileType.length == 0) {
				Toaster.sayWarning("provide File Type");
				return;
			}
			searchsys = $scope.selectedSystems
			vm.allSysDat = $scope.selectedSystems
			if (searchsys[0] == 0) {
				searchsys = []
			} else {
				searchsys = $scope.selectedSystems
			}

			for (s in searchsys) {
				searchsys[s] = (searchsys[s])
			}
			vm.fileExtenData = [];
			vm.isTrue = $scope.selectedFileType;
			if ($scope.selectedFileType == "0") {
				vm.fileExtenData = []
			} else {
				vm.fileExtenData = $scope.selectedFileType;
			}
			var advSearchForm = {
				// "startDate": vm.sourceData.startDateTime + ":00 " + moment(vm.sourceData.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				// "endDate": vm.sourceData.endDateTime + ":00 " + moment(vm.sourceData.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
				"startDate": vm.sourceData.startDateTime + " " + "00:00:00 " + moment(vm.sourceData.startDateTime).tz(getTimeZone()).format("ZZ"),
				"endDate": vm.sourceData.endDateTime + " " + "23:59:59 " + moment(vm.sourceData.endDateTime).tz(getTimeZone()).format("ZZ"),
				"systems": (searchsys),
				"fileExten": (vm.fileExtenData),
			}
			$scope.searchKeyClickedOnScreen = true;
			saSearch(advSearchForm)

		}

	}

	// initial loading function when page load.
	function initiateSourceDetailsByManager() {

		vm.sourceData.startDateTime = moment().subtract(1, 'months').format("MM-DD-YYYY");
		vm.sourceData.endDateTime = moment(2359, 'HH:mm').format("MM-DD-YYYY");
		APIFactory.getSystemList(function (response) {
			if (response.status) {
				vm.sourceDetailsList = response.data;
				$scope.searchKeyClickedOnScreen = false;
				setTimeout(function () {
					$("#sourceTargetSystems").select2()

					$("#sourceTargetSystems").on("select2:select", function (evt) {
						$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
						initTargetSystemFieldByManager()
					})
					$("#sourceTargetSystems").val("0").trigger("change")

					$scope.selectedSystems = ["0"]
					initTargetSystemFieldByManager()
					$("#sourceTargetSystems").on("select2:unselect", function () {
						$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
						$scope.selectedSystems = $scope.selectedSystems ? $scope.selectedSystems : []
						if ($scope.selectedSystems.indexOf("0") < 0) {
							_.each(vm.sourceDetailsList, function (sObj) {
								$("#sourceTargetSystems option[value='" + sObj.name + "']").prop('disabled', false)
							})
							$("#sourceTargetSystems").select2()
						}
						if ($scope.selectedSystems.length == 0) { // When User Unselects last selected systems
							$("#sourceTargetSystems option[value='0']").prop('disabled', false)
							$("#sourceTargetSystems").select2()
						}
						$scope.$digest()
					})
				}, 1000)
			}
		})
	}

	// by functional Package

	function initiateSourceDetailsByPackage() {
		vm.sourceData.startDateTime = moment().subtract(6, 'months').format("MM-DD-YYYY");
		vm.sourceData.endDateTime = moment(2359, 'HH:mm').format("MM-DD-YYYY");
		$scope.sourcePackage = [];
		APIFactory.getSystemList(function (response) {
			if (response.status) {
				vm.sourceDetailsList = response.data;
				$scope.searchKeyClickedOnScreen = false;
				APIFactory.getAllFuncAreaListBySysName({
					"systems": _.pluck(vm.sourceDetailsList, "name")
				}, function (response) {
					$scope.repoTableList = response.data;
					_.each($scope.repoTableList, function (Obj) {
						$scope.sourcePackage.push(Obj);
					})

					setTimeout(function () {
						$("#sourceTargetSystems").select2()
						$("#sourceFunctionalPackage").select2()

						$("#sourceTargetSystems").on("select2:select", function (evt) {
							$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
							initTargetSystemFieldByPackage()
						})

						$("#sourceFunctionalPackage").on("select2:select", function (evt) {
							$scope.selectedFunctionalPackage = $("#sourceFunctionalPackage").select2("val")
							initSourceDataFunctionalPackage()
						})

						$("#sourceTargetSystems").val("0").trigger("change")
						$scope.selectedSystems = ["0"]
						initTargetSystemFieldByPackage()

						$("#sourceFunctionalPackage").val("0").trigger("change")
						$scope.selectedFunctionalPackage = ["0"]
						initSourceDataFunctionalPackage()

						$("#sourceTargetSystems").on("select2:unselect", function () {
							$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
							$scope.selectedSystems = $scope.selectedSystems ? $scope.selectedSystems : []
							if ($scope.selectedSystems.indexOf("0") < 0) {
								_.each(vm.sourceDetailsList, function (sObj) {
									$("#sourceTargetSystems option[value='" + sObj.name + "']").prop('disabled', false)
								})
								$("#sourceTargetSystems").select2()
							}

							if ($scope.selectedSystems.length == 0) { // When User Unselects last selected systems
								$("#sourceTargetSystems option[value='0']").prop('disabled', false)
								$("#sourceTargetSystems").select2()
							}
							vm.sourceData.functionalPackage = []
							$scope.filteredPackageName = filterPackageList($scope.selectedSystems)
							$scope.filteredPackageName.sort()
							$scope.$digest()
						})

						$("#sourceFunctionalPackage").on("select2:unselect", function () {
							$scope.selectedFunctionalPackage = $("#sourceFunctionalPackage").select2("val")
							$scope.selectedFunctionalPackage = $scope.selectedFunctionalPackage ? $scope.selectedFunctionalPackage : []
							if ($scope.selectedFunctionalPackage.indexOf("0") < 0) {
								_.each($scope.sourcePackage, function (sObj) {
									_.each(sObj, function (obj) {
										$("#sourceFunctionalPackage option[value='" + obj + "']").prop('disabled', false)
									})
								})
								$("#sourceFunctionalPackage").select2()
							}
							if ($scope.selectedFunctionalPackage.length == 0) { // When User Unselects last selected systems
								$("#sourceFunctionalPackage option[value='0']").prop('disabled', false)
								$("#sourceFunctionalPackage").select2()
							}
							$scope.$digest()
						})
					}, 1000)
				})
			}
		})

	}

	function initiateSourceDetailsByFrequency() {
		vm.sourceData.startDateTime = moment().subtract(6, 'months').format("MM-DD-YYYY HH:mm");
		vm.sourceData.endDateTime = moment(2359, 'HH:mm').add(6, 'months').format("MM-DD-YYYY HH:mm");
		APIFactory.listSourceArtifactExtenstions({}, function (response) {
			var sbtExten = "sbt";
			$scope.fileTypeListDetails = response.data["IBM"].concat(response.data["NON_IBM"])
			$scope.fileTypeListDetails.push(sbtExten);
			_.each($scope.fileTypeListDetails, function (id) {
				vm.planIds = _.uniq($scope.fileTypeListDetails, function (a) {
					return a;
				})
			})
			$scope.searchKeyClickedOnScreen = false;
			setTimeout(function () {
				$("#sourceTargetSystems").select2()
				$("#fileTypeList").select2()

				$("#sourceTargetSystems").on("select2:select", function (evt) {
					$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
					initTargetSystemFieldByPackage()
				})

				$("#fileTypeList").on("select2:select", function (evt) {
					$scope.selectedFileType = $("#fileTypeList").select2("val")
					initFileTypeExtension()
				})

				$("#sourceTargetSystems").val("0").trigger("change")
				$scope.selectedSystems = ["0"]
				initTargetSystemFieldByPackage()

				$("#fileTypeList").val("0").trigger("change")
				$scope.selectedFileType = ["0"]
				initFileTypeExtension()

				$("#sourceTargetSystems").on("select2:unselect", function () {
					$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
					$scope.selectedSystems = $scope.selectedSystems ? $scope.selectedSystems : []
					if ($scope.selectedSystems.indexOf("0") < 0) {
						_.each(vm.sourceDetailsList, function (sObj) {
							$("#sourceTargetSystems option[value='" + sObj.name + "']").prop('disabled', false)
						})
						$("#sourceTargetSystems").select2()
					}

					if ($scope.selectedSystems.length == 0) { // When User Unselects last selected systems
						$("#sourceTargetSystems option[value='0']").prop('disabled', false)
						$("#sourceTargetSystems").select2()
					}
					vm.sourceData.functionalPackage = []
					$scope.filteredPackageName = filterPackageList($scope.selectedSystems)
					$scope.filteredPackageName.sort()
					$scope.$digest()
				})

				$("#fileTypeList").on("select2:unselect", function () {
					$scope.selectedFileType = $("#fileTypeList").select2("val")
					$scope.selectedFileType = $scope.selectedFileType ? $scope.selectedFileType : []
					if ($scope.selectedFileType.indexOf("0") < 0) {
						_.each($scope.fileTypeListDetails, function (sObj) {
							$("#fileTypeList option[value='" + sObj + "']").prop('disabled', false)
						})
						$("#fileTypeList").select2()
					}
					if ($scope.selectedFileType.length == 0) { // When User Unselects last selected systems
						$("#fileTypeList option[value='0']").prop('disabled', false)
						$("#fileTypeList").select2()
					}
					$scope.$digest()
				})
			}, 1000)
		})
	}


	// QA Effectiveness

	function initiateSourceDetailsByQAeffect() {
		vm.sourceData.startDateTime = moment().subtract(1, 'months').format("MM-DD-YYYY");
		vm.sourceData.endDateTime = moment(2359, 'HH:mm').format("MM-DD-YYYY");
		APIFactory.getSystemList(function (response) {
			if (response.status) {
				vm.sourceDetailsList = response.data;
				$scope.searchKeyClickedOnScreen = false;
				setTimeout(function () {
					$("#sourceTargetSystems").select2()

					$("#sourceTargetSystems").on("select2:select", function (evt) {
						$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
						initTargetSystemFieldByManager()
					})
					$("#sourceTargetSystems").val("0").trigger("change")

					$scope.selectedSystems = ["0"]
					initTargetSystemFieldByManager()
					$("#sourceTargetSystems").on("select2:unselect", function () {
						$scope.selectedSystems = $("#sourceTargetSystems").select2("val")
						$scope.selectedSystems = $scope.selectedSystems ? $scope.selectedSystems : []
						if ($scope.selectedSystems.indexOf("0") < 0) {
							_.each(vm.sourceDetailsList, function (sObj) {
								$("#sourceTargetSystems option[value='" + sObj.name + "']").prop('disabled', false)
							})
							$("#sourceTargetSystems").select2()
						}
						if ($scope.selectedSystems.length == 0) { // When User Unselects last selected systems
							$("#sourceTargetSystems option[value='0']").prop('disabled', false)
							$("#sourceTargetSystems").select2()
						}
						$scope.$digest()
					})
				}, 1000)
			}
		})
	}


	// Function to filter out package name based on selected target system(s)
	var filterPackageList = function (selectedsys) {
		var result = []
		if (selectedsys.length == 0) {
			return result
		} else if (selectedsys.indexOf("0") >= 0) {
			result = _.unique(_.flatten(_.values($scope.repoTableList)))
		} else {
			_.each(selectedsys, function (elem) {
				result = _.uniq(result.concat($scope.repoTableList[(_.findWhere(vm.sourceDetailsList, {
					"name": (elem)
				})).name]));
			})
		}
		return result;
	}

	// based on role name multi select dropdown load function.
	vm.sourceDataRole = function (role) {
		APIFactory.getUsersByRole({
			"role": role
		}, function (response) {
			if (response.status) {
				vm.sourceDataRoles = response.data;

				setTimeout(function () {
					$("#sourceDataUserName").select2()

					$("#sourceDataUserName").on("select2:select", function (evt) {
						$scope.selectedUserName = $("#sourceDataUserName").select2("val")
						initSourceDataUserName()
					})
					$("#sourceDataUserName").val("0").trigger("change")
					$scope.selectedUserName = ["0"]
					initSourceDataUserName()
					$("#sourceDataUserName").on("select2:unselect", function () {
						$scope.selectedUserName = $("#sourceDataUserName").select2("val")
						$scope.selectedUserName = $scope.selectedUserName ? $scope.selectedUserName : []
						if ($scope.selectedUserName.indexOf("0") < 0) {
							_.each(vm.sourceDataRoles, function (sObj) {
								$("#sourceDataUserName option[value='" + sObj.id + "']").prop('disabled', false)
							})
							$("#sourceDataUserName").select2()
						}
						if ($scope.selectedUserName.length == 0) { // When User Unselects last selected systems
							$("#sourceDataUserName option[value='0']").prop('disabled', false)
							$("#sourceDataUserName").select2()
						}
						$scope.$digest()
					})
				}, 1000)
			}
		})
	}

	// by manager target system
	function initTargetSystemFieldByManager() {
		if ($scope.selectedSystems.indexOf("0") >= 0) { // When User Selects "All" option
			_.each(vm.sourceDetailsList, function (sObj) {
				$("#sourceTargetSystems option[value='" + sObj.name + "']").prop('disabled', true)
			})
		} else { // When User Selects Any one system
			$("#sourceTargetSystems option[value='0']").prop('disabled', true)
		}
		$scope.$digest()
		$("#sourceTargetSystems").select2()
	}

	// by functional package target system
	function initTargetSystemFieldByPackage() {
		if ($scope.selectedSystems.indexOf("0") >= 0) { // When User Selects "All" option
			_.each(vm.sourceDetailsList, function (sObj) {
				$("#sourceTargetSystems option[value='" + sObj.name + "']").prop('disabled', true)
			})
		} else { // When User Selects Any one system
			$("#sourceTargetSystems option[value='0']").prop('disabled', true)
		}
		// vm.sourceData.functionalPackage = []
		$scope.filteredPackageName = filterPackageList($scope.selectedSystems)
		$scope.filteredPackageName.sort()
		$scope.$digest()
		$("#sourceTargetSystems").select2()
	}

	function initSourceDataFunctionalPackage() {
		if ($scope.selectedFunctionalPackage.indexOf("0") >= 0) { // When User Selects "All" option
			_.each($scope.sourcePackage, function (sObj) {
				_.each(sObj, function (obj) {
					$("#sourceFunctionalPackage option[value='" + obj + "']").prop('disabled', true)
				})
			})
		} else { // When User Selects Any one system
			$("#sourceFunctionalPackage option[value='0']").prop('disabled', true)
		}
		$scope.$digest()
		$("#sourceFunctionalPackage").select2()
	}

	function initFileTypeExtension() {
		if ($scope.selectedFileType.indexOf("0") >= 0) { // When User Selects "All" option
			_.each(vm.planIds, function (sObj) {
				$("#fileTypeList option[value='" + sObj + "']").prop('disabled', true)
			})
		} else { // When User Selects Any one system
			$("#fileTypeList option[value='0']").prop('disabled', true)
		}
		$scope.$digest()
		$("#fileTypeList").select2()
	}


	// user name function for selecting all and other data and disable corresponding target system content.
	function initSourceDataUserName() {
		if ($scope.selectedUserName.indexOf("0") >= 0) { // When User Selects "All" option
			_.each(vm.sourceDataRoles, function (sObj) {
				$("#sourceDataUserName option[value='" + sObj.id + "']").prop('disabled', true)
			})
		} else { // When User Selects Any one system
			$("#sourceDataUserName option[value='0']").prop('disabled', true)
		}
		$scope.$digest()
		$("#sourceDataUserName").select2()
	}


	// call backend to get data and display it in table.
	function searchData(advObj) {
		vm.searchFlag = false;
		if ($scope.sourceNames == 'Deployment details by Functional Package') {
			APIFactory.reportByManager({}, advObj, function (response) {
				if (response.status && response.data.reportTable.length != 0) {
					vm.searchFlag = true;
					vm.noDataFound = false;
					vm.reportPackage = response.data;
					$scope.startDate1 = moment(vm.reportPackage.reportForm.startDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.startDate = $scope.startDate1.split(" ")[0];
					$scope.endDate1 = moment(vm.reportPackage.reportForm.endDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.endDate = $scope.endDate1.split(" ")[0];
					$scope.funcAreas = []
					_.each(vm.reportPackage.reportForm.funcAreas, function (area) {
						$scope.funcAreas.push(area);
					})
				} else if (!response.status) {
					vm.noDataFound = true;
					Toaster.sayError(response.errorMessage)
				} else {
					vm.noDataFound = true;
				}
			})
		} else if ($scope.sourceNames == 'Deployment details by manager') {
			APIFactory.reportByManager({}, advObj, function (response) {
				if (response.status && response.data.reportTable.length != 0) {
					vm.searchFlag = true;
					vm.noDataFound = false;
					vm.reportManager = response.data;
					$scope.startDate1 = moment(vm.reportManager.reportForm.startDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.startDate = $scope.startDate1.split(" ")[0];
					$scope.endDate1 = moment(vm.reportManager.reportForm.endDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.endDate = $scope.endDate1.split(" ")[0];
					$scope.role = vm.reportManager.reportForm.role
				} else if (!response.status) {
					vm.noDataFound = true;
					Toaster.sayError(response.errorMessage)
				} else {
					vm.noDataFound = true;
				}
			})
		} else if ($scope.sourceNames == 'QA Effectiveness') {
			APIFactory.generateQAReport({}, advObj, function (response) {
				if (response.status && response.data.detailData.length != 0) {
					vm.searchFlag = true;
					vm.noDataFound = false;
					vm.generaeQAReport = response.data;
					$scope.startDate1 = moment(vm.generaeQAReport.reportForm.startDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.startDate = $scope.startDate1.split(" ")[0];
					$scope.endDate1 = moment(vm.generaeQAReport.reportForm.endDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.endDate = $scope.endDate1.split(" ")[0];
				} else if (!response.status) {
					vm.noDataFound = true;
					Toaster.sayError(response.errorMessage)
				} else {
					vm.noDataFound = true;
				}
			})
		}
	}

	$scope.exportReport = function () {
		if ($scope.sourceNames == 'Deployment details by manager') {
			APIFactory.exportReportByManager({}, vm.reportManager, function (response) {
				if (response.status) {
					var resposeStr = base64ToArrayBuffer(response.data)
					var file = new Blob([resposeStr], {
						type: response.metaData
					});
					var dateObj = new Date()
					var fileName = "EXPORT_TARGETSYSTEM_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
					saveAs(file, fileName)
				} else {
					Toaster.sayError(response.errorMessage);
				}
			})
		} else if ($scope.sourceNames == 'Deployment details by Functional Package') {
			APIFactory.exportReportByManager({}, vm.reportPackage, function (response) {
				if (response.status) {
					var resposeStr = base64ToArrayBuffer(response.data)
					var file = new Blob([resposeStr], {
						type: response.metaData
					});
					var dateObj = new Date()
					var fileName = "EXPORT_TARGET_SYSTEM_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
					saveAs(file, fileName)
				} else {
					Toaster.sayError(response.errorMessage);
				}
			})
		} else if ($scope.sourceNames == 'QA Effectiveness') {
			APIFactory.exportQAReport({}, vm.generaeQAReport, function (response) {
				if (response.status) {
					var resposeStr = base64ToArrayBuffer(response.data)
					var file = new Blob([resposeStr], {
						type: response.metaData
					});
					var dateObj = new Date()
					var fileName = "EXPORT_QA_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
					saveAs(file, fileName)
				} else {
					Toaster.sayError(response.errorMessage);
				}
			})
		}
		else if ($scope.sourceNames == 'Source artifact update frequency') {
			APIFactory.exportRepoReport({}, vm.reportFrequency, function (response) {
				if (response.status) {
					var resposeStr = base64ToArrayBuffer(response.data)
					var file = new Blob([resposeStr], {
						type: response.metaData
					});
					var dateObj = new Date()
					var fileName = "EXPORT_TARGET_SYSTEM_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
					saveAs(file, fileName)
				} else {
					Toaster.sayError(response.errorMessage);
				}
			})
		}
	}

	function saSearch(saObj) {
		IPService.getExpandView($scope, vm)

		/* Pagination Table Starts */
		var columnsToBeSorted = []
		var tableAttr;
		var initTableSettings = function () {
			$scope.tableConfig = Paginate.tableConfig()
			$scope.pageSizeList = Paginate.pageSizeList()
			tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
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
			saPagination(tableAttr)
		}

		$scope.refresh = function () {
			initTableSettings()
			tableAttr.offset = $rootScope.paginateValue
			saPagination(tableAttr)
		}

		$scope.pageChangeHandler = function (num) {
			if (vm.paginateData && $scope.tableConfig.lastLoadedPage === num) {
				return;
			}
			$scope.tableConfig.lastLoadedPage = num
			tableAttr.offset = num - 1
			localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
			tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
			$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
			saPagination(tableAttr)
		};
		$scope.pageChangeHandler($scope.tableConfig.currentPage)
		$scope.sort = function (columnName) {
			var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
			tableAttr = lSort.tableAttr
			$scope.tableConfig = lSort.tableConfig
			$scope.sortColumn = lSort.sortColumn
			saPagination(tableAttr)
		}

		function saPagination(tableAttr) {
			APIFactory.generateRepoReport(tableAttr, saObj, function (response) {
				if (response.status && response.data.length != 0) {
					vm.searchFlag = true;
					vm.noDataFound = false;
					vm.reportFrequency = response.data;
					vm.paginateData = vm.reportFrequency.systemAndUserDetails
					vm.saData = vm.reportFrequency.fileExtnReportForm
					$scope.tableConfig.totalItems = response.count
					vm.totalNumberOfItem = response.count
					$scope.startDate1 = moment(vm.reportFrequency.fileExtnReportForm.startDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.startDate = $scope.startDate1.split(" ")[0];
					$scope.endDate1 = moment(vm.reportFrequency.fileExtnReportForm.endDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
					$scope.endDate = $scope.endDate1.split(" ")[0];
					$rootScope.saveformData()
				} else if (!response.status) {
					vm.noDataFound = true;
					Toaster.sayError(response.errorMessage)
				} else {
					vm.noDataFound = true;
				}
			})

		}
	}

})