dashboard.controller("searchCtrl", function ($rootScope, $scope, $state, $location, Toaster,
    $http, appSettings, apiService, APIFactory, IPService, IService, Paginate, Access, $timeout) {
    var vm = this;
    var apiBase = appSettings.apiBase;
    $rootScope.titleHeading = $state.current.data.pageTitle
    // vm.developerName = vm.currentUser = getUserData("user").displayName
    // vm.ADLName = getUserData("user").id

    Paginate.refreshScrolling();
    vm.currentUser = $rootScope.home_menu
    var params = {};
    vm.soSearchData = {};
    vm.sourceArtifactSearchData = {};
    vm.advSearchData = {};
    vm.advSearchObj = {};
    vm.depActObj = {};
    vm.exactSegement = false;
    vm.deploymentActivityData = {}
    var defaultSoFileLength = 4;
    vm.searchType = ""
    $scope.enableRecentPage = true
    $scope.searchKeyClickedOnScreen = false
    vm.advancedSearchSystemList = []
    vm.depActSystemList = []
    // vm.advSearchData.selectedSystems = []
    $scope.selectedSystems = []
    $scope.filteredPackageName = []
    $scope.filteredPackageNameDepAct = []
    $scope.csrList = []
    $scope.userNameList = []
    vm.advSearchTableAttr = {}
    vm.depActTableAttr = []
    vm.advResultCount = ""
    vm.depActResultCount = ""
    $scope.RFCClick = function (pObj) {
        var getBase1 = IPService.initRFC($scope, vm, pObj)
    }

    $timeout(function () {
        $(".searchHeight").height(parseInt($(".content-wrapper").height()) * 0.60)
    }, 1000)

    var searchTypeList = [ // for reference
        "Implementation Plan",
        "Implementation",
        "Shared Object"
    ]

    var searchAccess = {
        "Lead": ["Implementation Plan", "Implementation", "Shared Object", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "Developer": ["Implementation Plan", "Implementation", "Shared Object", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "LoadsControl": ["Implementation Plan", "Implementation", "Shared Object", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "DevManager": ["Implementation Plan", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "TechnicalServiceDesk": ["Implementation Plan", "Shared Object", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "QA": ["Implementation Plan", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "QADeployLead": ["Implementation Plan", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "DLCoreChangeTeam": ["Implementation Plan", "Implementation", "Shared Object", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "SystemSupport": ["Implementation Plan", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"],
        "Reviewer": ["Implementation Plan", "Advanced Search", "Deployment Activity", "Source Artifact Search", "Segment/Repository Search"]
    }

    if (searchAccess[getUserData("userRole")]) {
        vm.searchTypeList = searchAccess[getUserData("userRole")]
    } else {
        vm.searchTypeList = []
    }

    vm.depActEnvironment = ["Production", "Pre-Production"]
    vm.depActions = ["Deployed", "Removed"]

    vm.showClearButton = false;
    vm.impPlanLength = 0
    vm.impPlanList = []
    vm.implementationList = []
    vm.implementationPlanExport = false;
    vm.implementationExport = false;
    $scope.$watch('vm.searchType', function () {
        // $scope.searchKeyClickedOnScreen=false
        Access.getSearchData().recentPage = 0
        vm.impPlanList = []
        vm.implementationList = []
        vm.implementationPlanExport = false;
        vm.implementationExport = false;
        vm.exactSegement = false;
        vm.filterData = null;
        if (vm.searchType === "Implementation") {
            vm.searchImplementation = true
            vm.searchImplementationPlan = false
        } else if (vm.searchType === "Implementation Plan") {
            vm.searchImplementation = false
            vm.searchImplementationPlan = true
        } else if (vm.searchType === "Shared Object") {
            vm.searchImplementation = false
            vm.searchImplementationPlan = false
            vm.searchSharedObject = true
            vm.searchTypeTxt = "Shared Object"
            getSystemInfo()
        } else if (vm.searchType === "Advanced Search") {
            vm.searchAdvanced = true
            initiateAdvancedSearch()
        }
        else if (vm.searchType === "Deployment Activity") {
            vm.searchDepAct = true
            initDeploymentActivity()
        }
        else if (vm.searchType === "Source Artifact Search") {
            vm.sourceArtifactSearch = true
            initSourceArtifactSearch()
        } else if (vm.searchType === "Segment/Repository Search") {
            vm.segmentRepositorySearch = true
            initSegmentRepositorySearch()
        }
    })
    vm.soFileList = []
    vm.advFileList;
    vm.depActFileList;
    vm.sourceArtifactSearchList;
	$scope.sharedObjectSearchTriggered = false
	$scope.advancedSearchTriggered = false
	$scope.deploymentActivitySearchTriggered = false
	$scope.sourceArtifactSearchTriggered = false
	$scope.segmentSearchTriggered = false
    $scope.searchFilter = function () {
        if (vm.searchType === "Implementation Plan" || vm.searchType === "Implementation") {
            if (!vm.filterData) {
                Toaster.sayWarning("Provide Search data");
                return;
            }
            $scope.searchKeyClickedOnScreen = true
            if (vm.searchType === "Implementation") { // || getUserData("userRole") === "Developer"
                searchImplementation(angular.uppercase(vm.filterData))
            } else if (vm.searchType === "Implementation Plan") { //  || getUserData("userRole") === "Lead"
                searchImplementationPlan(angular.uppercase(vm.filterData))
            } else {
                Toaster.sayWarning("Choose Search Types");
                return false;
            }
            Access.getSearchData().param_1 = vm.filterData;
        } else if (vm.searchType === "Shared Object") {
            if (!vm.soSearchData.soName) {
                Toaster.sayWarning("SO Name cannot be empty");
                return;
            }
            if (vm.soSearchData.soName.length > defaultSoFileLength || vm.soSearchData.soName.length < defaultSoFileLength) {
                Toaster.sayWarning("SO Name should be four characters");
                return;
            }
            if (!vm.soSearchData.systemId) {
                Toaster.sayWarning("Choose Target System");
                return;
            }
            $scope.searchKeyClickedOnScreen = true
            Access.getSearchData().param_2 = vm.soSearchData.soName;
            Access.getSearchData().param_3 = vm.soSearchData.loadDate;
            Access.getSearchData().param_4 = vm.soSearchData.systemId;
			$scope.sharedObjectSearchTriggered = true
            searchSharedObject(vm.soSearchData)

        } else if (vm.searchType === "Advanced Search") {
            if (!$scope.selectedSystems || $scope.selectedSystems.length <= 0) {
                Toaster.sayWarning("Select Target System(s)");
                return;
            }
            if (!vm.advSearchData.startDateTime) {
                Toaster.sayWarning("Provide Start Date/Time");
                return;
            }
            if (!vm.advSearchData.endDateTime) {
                Toaster.sayWarning("Provide End Date/Time");
                return;
            }
            var from = moment(vm.advSearchData.startDateTime, "MM-DD-YYYY HH:mm")
            var to = moment(vm.advSearchData.endDateTime, "MM-DD-YYYY HH:mm")
            if (to.diff(from, "minutes") === 0) {
                Toaster.sayWarning("From time and To time should not be same");
                return;
            }
            if (to.diff(from, "minutes") < 0) {
                Toaster.sayWarning("From date should be lesser than To date");
                return;
            }

            var searchsys = []
            searchsys = $scope.selectedSystems

            if (searchsys[0] == 0) {
                searchsys = _.pluck(vm.advancedSearchSystemList, "id")
            }

            for (s in searchsys) {
                searchsys[s] = parseInt(searchsys[s])
            }
            var csrProjectNos = []
            if (vm.advSearchData.csrNo && vm.advSearchData.csrNo.length > 0) {
                _.each(vm.advSearchData.csrNo, function (csr) {
                    csrProjectNos.push(_.findWhere($scope.csrList, { "id": parseInt(csr) }).projectNumber)
                })
            }
            if (vm.advSearchData.userName && vm.advSearchData.userName.length > 0) {
                if (!vm.advSearchData.role || vm.advSearchData.role.length == 0) {
                    Toaster.sayWarning("Choose role for the selected User")
                    return
                }
            }
            var rolesListDuplicate = angular.copy($scope.roleList);

            var searchDataRole = [];
            if (vm.advSearchData.role && vm.advSearchData.role.length >= 0) {
                if (vm.advSearchData.role.indexOf("0") >= 0) {
                    searchDataRole = _.each(rolesListDuplicate, function (elem, key) {
                        if (elem == "LoadsAttendee") {
                            rolesListDuplicate[key] = "LoadsControl"
                        }
                    });
                } else {
                    searchDataRole = _.each(vm.advSearchData.role, function (elem, key) {
                        if (elem == "LoadsAttendee") {
                            vm.advSearchData.role[key] = "LoadsControl"
                        }
                    })
                }
            }


            var advSearchForm = {
                "startDate": vm.advSearchData.startDateTime + ":00 " + moment(vm.advSearchData.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
                "endDate": vm.advSearchData.endDateTime + ":00 " + moment(vm.advSearchData.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
                "targetSystems": (searchsys),
                "programName": (vm.advSearchData.programName && vm.advSearchData.programName.length > 0) ? vm.advSearchData.programName : null,
                "implPlanStatus": (vm.advSearchData.planStatus && vm.advSearchData.planStatus.length > 0) ? vm.advSearchData.planStatus : null,
                "functionalPackages": (vm.advSearchData.packageName && vm.advSearchData.packageName.length > 0) ? vm.advSearchData.packageName : null,
                "csrNumber": (vm.advSearchData.csrNo && vm.advSearchData.csrNo.length > 0) ? csrProjectNos : null,
                "name": (vm.advSearchData.userName && vm.advSearchData.userName.length > 0) ? vm.advSearchData.userName : null,
                "role": searchDataRole,
                "exactSegment": vm.exactSegement
            }
            $scope.searchKeyClickedOnScreen = true
			$scope.advancedSearchTriggered = true
            // Access.getSearchData().param_5 = vm.advSearchData.startDateTime;
            // Access.getSearchData().param_6 = vm.advSearchData.endDateTime;
            // Access.getSearchData().param_7 = vm.advSearchData.systems;
            // Access.getSearchData().param_8 = (vm.advSearchData.developers && vm.advSearchData.developers.length > 0) ? vm.advSearchData.developers : null;
            // Access.getSearchData().param_9 = (vm.advSearchData.programName && vm.advSearchData.programName.length > 0) ? vm.advSearchData.programName : null;
            searchAdvanced(advSearchForm)

        } else if (vm.searchType === "Deployment Activity") {
            if (!vm.deploymentActivityData.environment) {
                Toaster.sayWarning("Choose Environment");
                return;
            }
            if (!vm.deploymentActivityData.depActions || vm.deploymentActivityData.depActions.length < 0) {
                Toaster.sayWarning("Choose Deployment Action");
                return;
            }
            if (!(_.values(vm.deploymentActivityData.depActions).toString().indexOf("true") >= 0)) {
                Toaster.sayWarning("Choose Deployment Action");
                return;
            }
            if (!vm.deploymentActivityData.startDateTime) {
                Toaster.sayWarning("Provide Start Date/Time");
                return;
            }
            if (!vm.deploymentActivityData.endDateTime) {
                Toaster.sayWarning("Provide End Date/Time");
                return;
            }
            var from = moment(vm.deploymentActivityData.startDateTime, "MM-DD-YYYY HH:mm")
            var to = moment(vm.deploymentActivityData.endDateTime, "MM-DD-YYYY HH:mm")
            if (to.diff(from, "minutes") === 0) {
                Toaster.sayWarning("From time and To time should not be same");
                return;
            }
            if (to.diff(from, "minutes") < 0) {
                Toaster.sayWarning("From date should be lesser than To date");
                return;
            }
            if (!$scope.selectedSystemsDepAct || $scope.selectedSystemsDepAct.length <= 0) {
                Toaster.sayWarning("Select Target System(s)");
                return;
            }
            var searchsys = []
            searchsys = $scope.selectedSystemsDepAct

            if (searchsys[0] == 0) {
                searchsys = _.pluck(vm.depActSystemList, "id")
            }

            for (s in searchsys) {
                searchsys[s] = parseInt(searchsys[s])
            }
            var daKeys = Object.keys(vm.deploymentActivityData.depActions);

            var filterDaKeys = daKeys.filter(function (key) {
                return vm.deploymentActivityData.depActions[key]
            });

            vm.deploymentActivityData.programName = $("#depActprogramName").val().split()

            var depActSearchForm = {
                "environment": vm.deploymentActivityData.environment,
                "actionList": filterDaKeys,
                "startDate": vm.deploymentActivityData.startDateTime + ":00 " + moment(vm.deploymentActivityData.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
                "endDate": vm.deploymentActivityData.endDateTime + ":00 " + moment(vm.deploymentActivityData.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
                "targetSys": (searchsys),
                "functionPackage": (vm.deploymentActivityData.packageName && vm.deploymentActivityData.packageName.length > 0) ? vm.deploymentActivityData.packageName : null,
                "programNames": (vm.deploymentActivityData.programName && vm.deploymentActivityData.programName.length > 0 && $("#depActprogramName").val() != "") ? vm.deploymentActivityData.programName : null,
            }
			$scope.deploymentActivitySearchTriggered = true
            $scope.searchKeyClickedOnScreen = true
            searchDepAct(depActSearchForm)

        } else if (vm.searchType === "Source Artifact Search") {
            if (!vm.sourceArtifactSearchData.sourceArtifactName) {
                Toaster.sayWarning("Enter Source Artifact Name");
                return;
            }
            if (!$scope.selectedSystemsSourceArtifactSearch || $scope.selectedSystemsSourceArtifactSearch.length <= 0) {
                Toaster.sayWarning("Select Target System(s)");
                return;
            }
            var searchsys = []
            searchsys = $scope.selectedSystemsSourceArtifactSearch

            if (searchsys[0] == 0) {
                searchsys = _.pluck(vm.sourceArtifactSearchSystemList, "id")
            }

            for (s in searchsys) {
                searchsys[s] = parseInt(searchsys[s])
            }
            var sourceArtifactSearchForm = {
                "sourceArtifactName": vm.sourceArtifactSearchData.sourceArtifactName,
                "fileType": (vm.sourceArtifactSearchData.fileType && vm.sourceArtifactSearchData.fileType.length > 0 ? vm.sourceArtifactSearchData.fileType : null),
                "targetSys": (searchsys),
            }
			$scope.sourceArtifactSearchTriggered = true
            $scope.searchKeyClickedOnScreen = true
            sourceArtifactSearch(sourceArtifactSearchForm)
        } else if (vm.searchType === "Segment/Repository Search") {

            if (!vm.segmentRepositorySearchData.segment) {
                Toaster.sayWarning("Enter Segment/Component Name");
                return;
            }
            if (!$scope.selectedSystemsSegmentRepository || $scope.selectedSystemsSegmentRepository.length <= 0) {
                Toaster.sayWarning("Select Target System(s)");
                return;
            }
            var searchsys = []
            searchsys = $scope.selectedSystemsSegmentRepository

            if (searchsys[0] == 0) {
                searchsys = _.pluck(vm.sourceSegmentRepoSystemList, "id")
            }

            for (s in searchsys) {
                searchsys[s] = parseInt(searchsys[s])
            }

            var segmentRepositorySearchForm = {
                "segment": vm.segmentRepositorySearchData.segment,
                "targetSys": (searchsys),
            }

            $scope.searchKeyClickedOnScreen = true
			$scope.segmentSearchTriggered = true
            segmentRepositorySearch(segmentRepositorySearchForm)
        }
        if (!vm.searchType) {
            Toaster.sayWarning("Choose search Type");
            return;
        }
        Access.getSearchData().searchType = vm.searchType;
    }




    var searchData = Access.getSearchData();
    if (Object.keys(searchData).length > 1) {
        $scope.searchKeyClickedOnScreen = true
        vm.filterData = searchData.param_1 ? searchData.param_1 : "";
        vm.soSearchData.soName = searchData.param_2 ? searchData.param_2 : "";
        vm.soSearchData.loadDate = searchData.param_3 ? searchData.param_3 : "";
        vm.soSearchData.systemId = searchData.param_4 ? searchData.param_4 : "";
        // vm.advSearchData.startDateTime = searchData.param_5 ? searchData.param_5 : "";
        // vm.advSearchData.endDateTime = searchData.param_6 ? searchData.param_6 : "";
        // vm.advSearchData.systems = searchData.param_7.split(",") ? searchData.param_7 : "";
        // vm.advSearchData.developers = searchData.param_8 ? searchData.param_8 : "";
        // $timeout(function() {
        //     $("#searchTargetSystems").val(vm.advSearchData.systems).trigger("change");
        // }, 500);
        // vm.advSearchData.programName = searchData.param_9 ? searchData.param_9 : "";
        vm.searchType = searchData.searchType;
        if (vm.filterData != "") {
            $scope.searchFilter();
        }
    }

    $scope.onceSearchTriggered = false
	$scope.onceImplementationTriggered = false
    $scope.isLoading = false

    function searchImplementationPlan(filterData) {
        vm.planFilter = filterData
        $scope.planSearch = true
        $scope.isLoading = true
        $scope.onceSearchTriggered = true
        var getBase = IPService.initPlan($scope, vm)
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)
    }

    function searchImplementation(filterData) {
        vm.filterText = filterData
        $scope.isLoading = true
		$scope.onceImplementationTriggered = true
        var getBase = IService.initImpl($scope, vm, "search")
        _.extend($scope, getBase.scope)
        _.extend(vm, getBase.vm)
    }

    function searchSharedObject(soObj) {
        IPService.getExpandView($scope, vm)
		
        /* Pagination Table Starts */
        var columnsToBeSorted = ["name", "systemId.name"]
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
            loadSoSearchList(tableAttr)
        }

        $scope.refresh = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadSoSearchList(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.soFileList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadSoSearchList(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadSoSearchList(tableAttr)
        }
		
		if($scope.sharedObjectSearchTriggered) {
			tableAttr.offset = 0
			localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
			tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
			$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
			
			$scope.sharedObjectSearchTriggered = false
		}

        /* Pagination Table Ends */

        function loadSoSearchList(tableAttr) {
            selectedRowId = null;
            _.extend(tableAttr, soObj)
            APIFactory.searchSharedObject(tableAttr, function (response) {
                if (response.status && response.data && response.data.length > 0) {
                    vm.soFileList = response.data
                    vm.totalNumberOfItem = response.count
                    var planIds = _.pluck(_.pluck(vm.soFileList, "planId"), "id")
                    APIFactory.getSystemLoadByPlan({ "ids": planIds }, function (response) {
                        if (response.status && response.data.length > 0) {
                            var rSystemList = response.data
                            _.each(vm.soFileList, function (soObj) {
                                soObj.systemLoadList = []
                                _.each(rSystemList, function (rsObj) {
                                    if (rsObj.systemLoad.planId.id == soObj.planId.id) {
                                        soObj.systemLoadList.push(rsObj.systemLoad)
                                    }
                                })
                            })
                            $rootScope.saveformData()
                        }
                    })
                } else {
                    vm.soFileList = []
                }
            })
        }

    }

    function getSystemInfo() {
        vm.soStartDate = moment().format(appSettings.dateFormat);
        vm.soSearchData.loadDate = vm.soStartDate;
        APIFactory.getSystemList(function (response) {
            if (response.status && response.data.length > 0) {
                vm.systemsList = response.data
            } else {
                vm.systemsList = []
            }
        })
    }



    //For Advanced Search - Starts
    function initTargetSystemField() {
        if ($scope.selectedSystems.indexOf("0") >= 0) { // When User Selects "All" option
            _.each(vm.advancedSearchSystemList, function (sObj) {
                $("#searchTargetSystems option[value='" + sObj.id + "']").prop('disabled', true)
            })
        } else { // When User Selects Any one system
            $("#searchTargetSystems option[value='0']").prop('disabled', true)
        }
        vm.advSearchData.packageName = []
        $scope.filteredPackageName = filterPackageList($scope.selectedSystems)
        $scope.filteredPackageName.sort()
        $scope.$digest()
        $("#searchTargetSystems").select2()
    }
    $scope.allOption = false;

    $(document.body).on("change", "#searchRole", function () {
        $scope.allOption = false;
        setTimeout(function () {
            var selectedValue = $("#searchRole").select2("val");
            if (selectedValue && selectedValue.indexOf("0") >= 0) {
                _.each($scope.roleList, function (sObj) {
                    $("#searchRole option[value='" + sObj + "']").prop('disabled', true)
                })
            } else {
                $scope.allOption = true;
            }

            if (selectedValue == null) {
                _.each($scope.roleList, function (sObj) {
                    $("#searchRole option[value='" + sObj + "']").prop('disabled', false)
                })
                $scope.allOption = false;
            }
            $scope.$digest()
            $("#searchRole").select2()

        }, 100);
    });

    function initiateAdvancedSearch() {
        vm.advSearchData.startDateTime = moment().subtract(1, 'months').format("MM-DD-YYYY HH:mm");
        vm.advSearchData.endDateTime = moment().add(1, 'months').format("MM-DD-YYYY HH:mm");
        $scope.roleList = ['Lead', 'Developer', 'Reviewer', 'LoadsAttendee', 'DevManager']
        APIFactory.getSystemList(function (response) {
            if (response.status) {
                vm.advancedSearchSystemList = response.data
                APIFactory.getAllFuncAreaList({
                    "ids": _.pluck(vm.advancedSearchSystemList, "id")
                }, function (response) {
                    $scope.packageList = response.data


                    setTimeout(function () {
                        $("#searchTargetSystems").select2()

                        $("#searchTargetSystems").on("select2:select", function (evt) {
                            $scope.selectedSystems = $("#searchTargetSystems").select2("val")
                            initTargetSystemField()
                        })
                        $("#searchTargetSystems").val("0").trigger("change")
                        $scope.selectedSystems = ["0"]
                        initTargetSystemField()
                        $("#searchTargetSystems").on("select2:unselect", function () {
                            $scope.selectedSystems = $("#searchTargetSystems").select2("val")
                            $scope.selectedSystems = $scope.selectedSystems ? $scope.selectedSystems : []
                            if ($scope.selectedSystems.indexOf("0") < 0) {
                                _.each(vm.advancedSearchSystemList, function (sObj) {
                                    $("#searchTargetSystems option[value='" + sObj.id + "']").prop('disabled', false)
                                })
                                $("#searchTargetSystems").select2()
                            }
                            if ($scope.selectedSystems.length == 0) { // When User Unselects last selected systems
                                $("#searchTargetSystems option[value='0']").prop('disabled', false)
                                $("#searchTargetSystems").select2()
                            }
                            vm.advSearchData.packageName = []
                            $scope.filteredPackageName = filterPackageList($scope.selectedSystems)
                            $scope.filteredPackageName.sort()
                            $scope.$digest()

                        })
                    }, 100)
                })
            } else {
                vm.advancedSearchSystemList = []
            }
        })
        if (Access.getAllPlanStatus()) {
            $scope.planStatusList = Access.getAllPlanStatus()
        } else {
            $scope.planStatusList = []
        }

    }
    APIFactory.getProjectListForSearch({ "filter": "", "platform": appSettings.isDeltaApp == 'true' ? "Delta" : "Travelport" }, function (response) {
        if (response.status) {
            $scope.csrList = response.data
        } else {
            $scope.csrList = []
        }
    })
    APIFactory.getSuperUserFromUsersList(function (response) {
        if (response.status) {
            $scope.userNameList = response.data
        } else {
            $scope.userNameList = []
        }
    })


    vm.ChangeName = function () {
        vm.advSearchData.role = ["0"];
        $scope.allOption = false;
        if (vm.advSearchData.userName == "" || vm.advSearchData.userName == null) {
            vm.advSearchData.role = '';
        }
        setTimeout(function () {
            var selectedValue = $("#searchRole").select2("val");
            if (selectedValue && selectedValue.indexOf("0") >= 0) {
                _.each($scope.roleList, function (sObj) {
                    $("#searchRole option[value='" + sObj + "']").prop('disabled', true)
                })
            }
            $scope.$digest()
            $("#searchRole").select2()

        }, 100);
    }


    // Function to filter out package name based on selected target system(s)
    var filterPackageList = function (selectedsys) {
        var result = []
        if (selectedsys.length == 0) {
            return result
        } else if (selectedsys.indexOf("0") >= 0) {
            result = _.unique(_.flatten(_.values($scope.packageList)))
        } else {
            _.each(selectedsys, function (elem) {
                result = _.uniq(result.concat($scope.packageList[(_.findWhere(vm.advancedSearchSystemList, { "id": parseInt(elem) })).name]));
            })
        }
        return result;
    }

    // call backend to get data
    function searchAdvanced(advObj) {
        IPService.getExpandView($scope, vm)

        /* Pagination Table Starts */
        var columnsToBeSorted = ["planid", "csrnumber", "loadcategory", "targetsystem", "planstatus", "qastatus", "loaddatetime", "activateddatetime", "fallbackdatetime"]
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
            loadAdvSearchList(tableAttr)
        }

        $scope.refresh = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadAdvSearchList(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.advFileList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadAdvSearchList(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadAdvSearchList(tableAttr)
        }
		
		if($scope.advancedSearchTriggered) {
			tableAttr.offset = 0
			localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
			tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
			$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
			
			$scope.advancedSearchTriggered = false
		}

        /* Pagination Table Ends */

        function loadAdvSearchList(tableAttr) {
            selectedRowId = null;
            vm.advSearchObj = advObj
            vm.advSearchTableAttr = tableAttr
            APIFactory.advancedSearch(tableAttr, advObj, function (response) {
                if (response.status && response.data && Object.keys(response.data).length > 0) {
                    vm.advFileList = response.data
                    vm.advResultCount = Object.keys(response.data).length
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    $rootScope.saveformData()
                    vm.advFileListstatus = true;
                } else {
                    vm.advFileList = {};
                    vm.advFileListstatus = false;
                }
            })
        }

    }


    //IPService.getExpandView($scope, vm)

    $scope.checkIsDeltaPlan = function (sysList) {
        var sss = IPService.isDeltaSL(sysList);
    }

    $scope.advSearchExpand = function (planId, searchObject) {
        APIFactory.getPlan({ "id": planId }, function (plan_response) {
            searchObject.planObj = plan_response.data.impPlan
            $scope.loadSystemImplApproverList(searchObject.planObj)
            // $scope.$apply()
        })
    }

    vm.uniqSegmentList = function (searchList) {
        return _.uniq(searchList, function (eachObj, key, index) {
            return eachObj.programname || eachObj.functionalarea
        })
    }

    // Adv Search Export function has been made common with deployment activity export and placed at the end of code
    // $scope.doExport = function() {}
    //For Advanced Search - Ends

    //For deployment activity
    function initDepActTargetSystemField() {
        if ($scope.selectedSystemsDepAct.indexOf("0") >= 0) { // When User Selects "All" option
            _.each(vm.depActSystemList, function (sObj) {
                $("#searchTargetSystemsDepAct option[value='" + sObj.id + "']").prop('disabled', true)
            })
        } else { // When User Selects Any one system
            $("#searchTargetSystemsDepAct option[value='0']").prop('disabled', true)
        }
        vm.deploymentActivityData.packageName = []
        $scope.filteredPackageNameDepAct = filterPackageListDepAct($scope.selectedSystemsDepAct)
        $scope.filteredPackageNameDepAct.sort()
        $scope.$digest()
        $("#searchTargetSystemsDepAct").select2()
        $("#depActprogramName").tagit({
            // singleField: true,
            caseSensitive: true,
            singleFieldNode: $('#depActprogramName'),
            // placeholderText : "Program Name"
            afterTagAdded: function (event, ui) {
                itktAdded($(ui.tag).find("span").html())
            }
        })
    }

    function itktAdded(itkt) {
    }

    function initDeploymentActivity() {
        vm.deploymentActivityData.startDateTime = moment(00, 'HH:mm').format("MM-DD-YYYY HH:mm");
        vm.deploymentActivityData.endDateTime = moment(2359, 'HH:mm').format("MM-DD-YYYY HH:mm");
        APIFactory.getSystemList(function (response) {
            if (response.status) {
                vm.depActSystemList = response.data
                APIFactory.getAllFuncAreaList({
                    "ids": _.pluck(vm.depActSystemList, "id")
                }, function (response) {
                    $scope.packageListDepAct = response.data


                    setTimeout(function () {
                        $("#searchTargetSystemsDepAct").select2()

                        $("#searchTargetSystemsDepAct").on("select2:select", function (evt) {
                            $scope.selectedSystemsDepAct = $("#searchTargetSystemsDepAct").select2("val")
                            initDepActTargetSystemField()
                        })
                        $("#searchTargetSystemsDepAct").val("0").trigger("change")
                        $scope.selectedSystemsDepAct = ["0"]
                        initDepActTargetSystemField()
                        $("#searchTargetSystemsDepAct").on("select2:unselect", function () {
                            $scope.selectedSystemsDepAct = $("#searchTargetSystemsDepAct").select2("val")
                            $scope.selectedSystemsDepAct = $scope.selectedSystemsDepAct ? $scope.selectedSystemsDepAct : []
                            if ($scope.selectedSystemsDepAct.indexOf("0") < 0) {
                                _.each(vm.depActSystemList, function (sObj) {
                                    $("#searchTargetSystemsDepAct option[value='" + sObj.id + "']").prop('disabled', false)
                                })
                                $("#searchTargetSystemsDepAct").select2()
                            }
                            if ($scope.selectedSystemsDepAct.length == 0) { // When User Unselects last selected systems
                                $("#searchTargetSystemsDepAct option[value='0']").prop('disabled', false)
                                $("#searchTargetSystemsDepAct").select2()
                            }
                            vm.deploymentActivityData.packageName = []
                            $scope.filteredPackageNameDepAct = filterPackageListDepAct($scope.selectedSystemsDepAct)
                            $scope.filteredPackageNameDepAct.sort()
                            $scope.$digest()

                        })
                    }, 100)
                })
            } else {
                vm.depActSystemList = []
            }
        })

    }


    // Function to filter out package name based on selected target system(s)
    var filterPackageListDepAct = function (selectedsys) {
        var result = []
        if (selectedsys.length == 0) {
            return result
        } else if (selectedsys.indexOf("0") >= 0) {
            result = _.unique(_.flatten(_.values($scope.packageListDepAct)))
        } else {
            _.each(selectedsys, function (elem) {
                result = _.uniq(result.concat($scope.packageListDepAct[(_.findWhere(vm.depActSystemList, { "id": parseInt(elem) })).name]));
            })
        }
        return result;
    }

    // call backend to get data
    function searchDepAct(daObj) {
        IPService.getExpandView($scope, vm)

        /* Pagination Table Starts */
        var columnsToBeSorted = []// ["planid", "csrnumber", "loadcategory", "targetsystem", "planstatus", "qastatus", "loaddatetime", "activateddatetime", "fallbackdatetime"]
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
            loadDepActList(tableAttr)
        }

        $scope.refresh = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadDepActList(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.depActFileList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadDepActList(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadDepActList(tableAttr)
        }
		
		if($scope.deploymentActivitySearchTriggered) {
			tableAttr.offset = 0
			localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
			tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
			$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
			
			$scope.deploymentActivitySearchTriggered = false
		}

        /* Pagination Table Ends */

        function loadDepActList(tableAttr) {
            selectedRowId = null;
            vm.depActObj = daObj
            vm.depActTableAttr = tableAttr
            APIFactory.getSegmentActivityDetails(tableAttr, daObj, function (response) {
                if (response && response.status && response.data && Object.keys(response.data).length > 0) {
                    vm.depActFileList = response.data
                    vm.depActResultCount = Object.keys(response.data).length
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    $rootScope.saveformData()
                    vm.depActFileListstatus = true;
                } else {
                    vm.depActFileList = {};
                    vm.depActFileListstatus = false;
                }
            })
        }

    }


    IPService.getExpandView($scope, vm)

    $scope.checkIsDeltaPlan = function (sysList) {
        var sss = IPService.isDeltaSL(sysList);
    }

    $scope.depActSearchExpand = function (planId, searchObject) {
        APIFactory.getPlan({ "id": planId }, function (plan_response) {
            searchObject.planObj = plan_response.data.impPlan
            $scope.loadSystemImplApproverList(searchObject.planObj)
            // $scope.$apply()
        })
    }

    // vm.uniqSegmentListDepAct = function(searchList) {
    //     return _.uniq(searchList, function(eachObj, key, index) {
    //         return eachObj.programname || eachObj.functionalarea
    //     })
    // }

    // For Source Artifact Search (Starts)
    function initSourceArtifactSearchTargetSystemField() {
        if ($scope.selectedSystemsSourceArtifactSearch.indexOf("0") >= 0) { // When User Selects "All" option
            _.each(vm.sourceArtifactSearchSystemList, function (sObj) {
                $("#searchTargetSystemsSourceArtifactSearch option[value='" + sObj.id + "']").prop('disabled', true)
            })
        } else { // When User Selects Any one system
            $("#searchTargetSystemsSourceArtifactSearch option[value='0']").prop('disabled', true)
        }
        $scope.$digest()
        $("#searchTargetSystemsSourceArtifactSearch").select2()
    }

    // For Source Artifact Search (Starts)
    function initSegmentRepositorySearchTargetSystemField() {
        if ($scope.selectedSystemsSegmentRepository.indexOf("0") >= 0) { // When User Selects "All" option
            _.each(vm.sourceSegmentRepoSystemList, function (sObj) {
                $("#searchSystemsSegmentRepository option[value='" + sObj.id + "']").prop('disabled', true)
            })
        } else { // When User Selects Any one system
            $("#searchSystemsSegmentRepository option[value='0']").prop('disabled', true)
        }
        $scope.$digest()
        $("#searchSystemsSegmentRepository").select2()
    }



    function initSourceArtifactSearch() {
        APIFactory.getSystemList(function (response) {
            if (response.status) {
                vm.sourceArtifactSearchSystemList = response.data
                APIFactory.listSourceArtifactExtenstions({}, function (response) {
                    var fileType = response.data["IBM"].concat(response.data["NON_IBM"])
                    fileType.push("sbt");
                    $scope.fileTypeList = _.unique(_.flatten(_.values(fileType)))
                })
                setTimeout(function () {
                    $("#searchTargetSystemsSourceArtifactSearch").select2()

                    $("#searchTargetSystemsSourceArtifactSearch").on("select2:select", function (evt) {
                        $scope.selectedSystemsSourceArtifactSearch = $("#searchTargetSystemsSourceArtifactSearch").select2("val")
                        initSourceArtifactSearchTargetSystemField()
                    })
                    $("#searchTargetSystemsSourceArtifactSearch").val("0").trigger("change")
                    $scope.selectedSystemsSourceArtifactSearch = ["0"]
                    initSourceArtifactSearchTargetSystemField()
                    $("#searchTargetSystemsSourceArtifactSearch").on("select2:unselect", function () {
                        $scope.selectedSystemsSourceArtifactSearch = $("#searchTargetSystemsSourceArtifactSearch").select2("val")
                        $scope.selectedSystemsSourceArtifactSearch = $scope.selectedSystemsSourceArtifactSearch ? $scope.selectedSystemsSourceArtifactSearch : []
                        if ($scope.selectedSystemsSourceArtifactSearch.indexOf("0") < 0) {
                            _.each(vm.sourceArtifactSearchSystemList, function (sObj) {
                                $("#searchTargetSystemsSourceArtifactSearch option[value='" + sObj.id + "']").prop('disabled', false)
                            })
                            $("#searchTargetSystemsSourceArtifactSearch").select2()
                        }
                        if ($scope.selectedSystemsSourceArtifactSearch.length == 0) { // When User Unselects last selected systems
                            $("#searchTargetSystemsSourceArtifactSearch option[value='0']").prop('disabled', false)
                            $("#searchTargetSystemsSourceArtifactSearch").select2()
                        }
                        $scope.$digest()
                    })
                }, 100)
            } else {
                vm.sourceArtifactSearchSystemList = []
            }
        })

    }

    function sourceArtifactSearch(saObj) {
        IPService.getExpandView($scope, vm)

        /* Pagination Table Starts */
        var columnsToBeSorted = []// ["planid", "csrnumber", "loadcategory", "targetsystem", "planstatus", "qastatus", "loaddatetime", "activateddatetime", "fallbackdatetime"]
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
            loadSourceArtifactSearchList(tableAttr)
        }

        $scope.refresh = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadSourceArtifactSearchList(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.sourceArtifactSearchList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadSourceArtifactSearchList(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadSourceArtifactSearchList(tableAttr)
        }
		
		if($scope.sourceArtifactSearchTriggered) {
			tableAttr.offset = 0
			localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
			tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
			$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
			
			$scope.sourceArtifactSearchTriggered = false
		}

        /* Pagination Table Ends */

        function loadSourceArtifactSearchList(tableAttr) {
            selectedRowId = null;
			
            // vm.depActObj = daObj
            // vm.depActTableAttr = tableAttr
            APIFactory.getSourceArtifactSearch(tableAttr, saObj, function (response) {
                if (response && response.status && response.data && Object.keys(response.data).length > 0) {
                    vm.sourceArtifactSearchList = response.data
                    // vm.depActResultCount = Object.keys(response.data).length
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    $rootScope.saveformData()
                    vm.sourceArtifactSearchstatus = true;
                } else {
                    vm.sourceArtifactSearchList = {};
                    vm.sourceArtifactSearchstatus = false;
                }
            })
        }

    }

    function initSegmentRepositorySearch() {
        APIFactory.getSystemList(function (response) {
            if (response.status) {
                vm.sourceSegmentRepoSystemList = response.data
                APIFactory.listSourceArtifactExtenstions({}, function (response) {
                    $scope.fileTypeList = response.data["IBM"].concat(response.data["NON_IBM"])
                })
                setTimeout(function () {
                    $("#searchSystemsSegmentRepository").select2()

                    $("#searchSystemsSegmentRepository").on("select2:select", function (evt) {
                        $scope.selectedSystemsSegmentRepository = $("#searchSystemsSegmentRepository").select2("val")
                        initSegmentRepositorySearchTargetSystemField()
                    })
                    $("#searchSystemsSegmentRepository").val("0").trigger("change")
                    $scope.selectedSystemsSegmentRepository = ["0"]
                    initSegmentRepositorySearchTargetSystemField()
                    $("#searchSystemsSegmentRepository").on("select2:unselect", function () {
                        $scope.selectedSystemsSegmentRepository = $("#searchSystemsSegmentRepository").select2("val")
                        $scope.selectedSystemsSegmentRepository = $scope.selectedSystemsSegmentRepository ? $scope.selectedSystemsSegmentRepository : []
                        if ($scope.selectedSystemsSegmentRepository.indexOf("0") < 0) {
                            _.each(vm.sourceSegmentRepoSystemList, function (sObj) {
                                $("#searchSystemsSegmentRepository option[value='" + sObj.id + "']").prop('disabled', false)
                            })
                            $("#searchSystemsSegmentRepository").select2()
                        }
                        if ($scope.selectedSystemsSegmentRepository.length == 0) { // When User Unselects last selected systems
                            $("#searchSystemsSegmentRepository option[value='0']").prop('disabled', false)
                            $("#searchSystemsSegmentRepository").select2()
                        }
                        $scope.$digest()
                    })
                }, 100)
            } else {
                vm.sourceSegmentRepoSystemList = []
            }
        })

    }

    function segmentRepositorySearch(srObj) {
        IPService.getExpandView($scope, vm)

        /* Pagination Table Starts */
        var columnsToBeSorted = []// ["planid", "csrnumber", "loadcategory", "targetsystem", "planstatus", "qastatus", "loaddatetime", "activateddatetime", "fallbackdatetime"]
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
            loadSegmentRepositorySearchList(tableAttr)
        }

        $scope.refresh = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadSegmentRepositorySearchList(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.SegmentRepositorySearchList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadSegmentRepositorySearchList(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadSegmentRepositorySearchList(tableAttr)
        }
		
		if($scope.segmentSearchTriggered) {
			tableAttr.offset = 0
			localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
			tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
			$rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
			
			$scope.segmentSearchTriggered = false
		}

        /* Pagination Table Ends */

        function loadSegmentRepositorySearchList(tableAttr) {
            selectedRowId = null;
            APIFactory.getSegmentRepository(tableAttr, srObj, function (response) {
                if (response && response.status && response.data && Object.keys(response.data).length > 0) {
                    vm.SegmentRepositorySearchList = response.data
                    $scope.tableConfig.totalItems = response.count
                    vm.totalNumberOfItem = response.count
                    $rootScope.saveformData()
                    vm.sourceSegmentRepoSearchstatus = true;
                } else {
                    vm.SegmentRepositorySearchList = {};
                    vm.sourceSegmentRepoSearchstatus = false;
                }
            })
        }

    }

    IPService.getExpandView($scope, vm)
    // For Source Artifact Search (Ends)




    $scope.doExport = function () {
        var tableAttrs;
        var dataObj;
        if (vm.searchType === "Advanced Search") {
            tableAttrs = vm.advSearchTableAttr;
            dataObj = vm.advSearchObj;


            try {
                APIFactory.searchExportExcel(tableAttrs, dataObj, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], {
                            type: response.metaData
                        });
                        var dateObj = new Date()
                        var fileName = "SEARCH_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }
        } else if (vm.searchType === "Deployment Activity") {
            tableAttrs = vm.depActTableAttr;
            dataObj = vm.depActObj;
            try {
                APIFactory.searchDeploymentExportExcel(tableAttrs, dataObj, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], {
                            type: response.metaData
                        });
                        var dateObj = new Date()
                        var fileName = "SEARCH_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }
        } else if (vm.searchType === "Implementation Plan") {
            var filterData = {
                filter: "",
                limit: 0,
                offset: 0
            }
            var implPlanSearchForm = {
                "implPlanId": angular.uppercase(vm.filterData)
            }

            tableAttrs = filterData;
            dataObj = implPlanSearchForm;
            try {
                APIFactory.searchExportExcel(tableAttrs, dataObj, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], {
                            type: response.metaData
                        });
                        var dateObj = new Date()
                        var fileName = "SEARCH_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }
        } else if (vm.searchType === "Implementation") {
            var filterData = {
                filter: "",
                limit: 0,
                offset: 0
            }
            var implSearchForm = {
                "implId": angular.uppercase(vm.filterData)
            }
            tableAttrs = filterData;
            dataObj = implSearchForm;
            try {
                APIFactory.searchExportExcel(tableAttrs, dataObj, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], {
                            type: response.metaData
                        });
                        var dateObj = new Date()
                        var fileName = "SEARCH_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xlsx"
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }


        }

    }
});