dashboard.controller("reportGenerateCtrl", function ($rootScope, $scope, $state, $location, Toaster,
    $http, appSettings, apiService, APIFactory, IPService, IService, Paginate, Access, $timeout) {
    var vm = this;
    vm.reportGenre = {};
    $rootScope.titleHeading = $state.current.data.pageTitle;
    $scope.searchKeyClickedOnScreen = false;
    vm.dropDownList = ["By Load Date/Time", "By RFC Number"]
    var tableAttrForExp;
	vm.exportFlagShow = false
    Paginate.refreshScrolling();
    initreportGenre();

    function initreportGenre() {
        vm.reportGenre.startDateTime = moment(00, 'HH:mm').format("MM-DD-YYYY HH:mm");
        vm.reportGenre.endDateTime = moment(2359, 'HH:mm').add(1, 'months').format("MM-DD-YYYY HH:mm");
        APIFactory.getSystemList(function (response) {
            if (response.status) {
                vm.targetSys = response.data

                setTimeout(function () {
                    $("#searchTargetSystemsRepGen").select2()

                    $("#searchTargetSystemsRepGen").on("select2:select", function (evt) {
                        $scope.selectedSystemsReportGenre = $("#searchTargetSystemsRepGen").select2("val")
                        initReportGenreTargetSystemField()
                    })
                    $("#searchTargetSystemsRepGen").val("0").trigger("change")
                    $scope.selectedSystemsReportGenre = ["0"]
                    initReportGenreTargetSystemField()
                    $("#searchTargetSystemsRepGen").on("select2:unselect", function () {
                        $scope.selectedSystemsReportGenre = $("#searchTargetSystemsRepGen").select2("val")
                        $scope.selectedSystemsReportGenre = $scope.selectedSystemsReportGenre ? $scope.selectedSystemsReportGenre : []
                        if ($scope.selectedSystemsReportGenre.indexOf("0") < 0) {
                            _.each(vm.targetSys, function (sObj) {
                                $("#searchTargetSystemsRepGen option[value='" + sObj.name + "']").prop('disabled', false)
                            })
                            $("#searchTargetSystemsRepGen").select2()
                        }
                        if ($scope.selectedSystemsReportGenre.length == 0) { // When User Unselects last selected systems
                            $("#searchTargetSystemsRepGen option[value='0']").prop('disabled', false)
                            $("#searchTargetSystemsRepGen").select2()
                        }
                        $scope.$digest()
                    })
                }, 100)
            } else {
                vm.targetSys = []
            }
        })

    }

    function initReportGenreTargetSystemField() {
        if ($scope.selectedSystemsReportGenre.indexOf("0") >= 0) { // When User Selects "All" option
            _.each(vm.reportGenreSystemList, function (sObj) {
                $("#searchTargetSystemsRepGen option[value='" + sObj.name + "']").prop('disabled', true)
            })
        } else { // When User Selects Any one system
            $("#searchTargetSystemsRepGen option[value='0']").prop('disabled', true)
        }
        $scope.$digest()
        $("#searchTargetSystemsRepGen").select2()
    }
    $scope.searchPlanData = function () {
        $scope.generateReport()
      }

   $scope.generateReport = function () {
        if (vm.searchByDT == 'By Load Date/Time') {
            if (!vm.reportGenre.startDateTime) {
                Toaster.sayWarning("Provide Start Date/Time");
                return;
            }
            if (!vm.reportGenre.endDateTime) {
                Toaster.sayWarning("Provide End Date/Time");
                return;
            }
            var from = moment(vm.reportGenre.startDateTime, "MM-DD-YYYY HH:mm")
            var to = moment(vm.reportGenre.endDateTime, "MM-DD-YYYY HH:mm")
            if (to.diff(from, "minutes") === 0) {
                Toaster.sayWarning("From time and To time should not be same");
                return;
            }
            if (to.diff(from, "minutes") < 0) {
                Toaster.sayWarning("End date/time must be after Start date/time");
                return;
            }

            if (!$scope.selectedSystemsReportGenre || $scope.selectedSystemsReportGenre.length <= 0) {
                Toaster.sayWarning("Select Target System(s)");
                return;
            }
            
            var searchsys = []
            searchsys = $scope.selectedSystemsReportGenre

            if (searchsys[0] == 0) {
                searchsys = _.pluck(vm.targetSys, "name")
            }
            for (s in searchsys) {
                searchsys[s] = searchsys[s]
            }

            var repGenFormForView = {
                "startDate": vm.reportGenre.startDateTime + ":00 " + moment(vm.reportGenre.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
                "endDate": vm.reportGenre.endDateTime + ":00 " + moment(vm.reportGenre.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
                "systems": (searchsys),
                "rfcNumber": null
            }
			vm.exportFlagShow = false
            genRep(repGenFormForView)
        } else if (vm.searchByDT == 'By RFC Number') {
            if (!vm.reportGenre.rfcNumber) {
                Toaster.sayWarning("Provide RFC Number");
                return;
            }
            // $scope.searchKeyClickedOnScreen = true
            var repGenFormForRFCNumberForView = {
                "startDate": null,
                "endDate": null,
                "systems": null,
                "rfcNumber": vm.reportGenre.rfcNumber
            }
            genRep(repGenFormForRFCNumberForView)
        } else {
            $scope.searchKeyClickedOnScreen = false
            Toaster.sayWarning("Provide Search Type");
            return;
        }

    }



    function genRep(daObj) {

        IPService.getExpandView($scope, vm)

        /* Pagination Table Starts */
        var columnsToBeSorted = ["planid","rfcnumber","targetsystem","loaddatetime"]
        var tableAttr;
        var initTableSettings = function () {
            $scope.tableConfig = Paginate.tableConfig()
            // $scope.tableConfig.pageSize = 10
            // $scope.tableDefaultValue = Paginate.defaultPageValue()
            // $scope.tableConfig.pageSize = $scope.tableDefaultValue.defaultValue // Default page Size for Report
            // $scope.tableConfig.pageSize = $rootScope.paginateDefaultValue // Default page Size for TSD
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
            loadRepGenForView(tableAttr)
        }

        $scope.refresh = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            loadRepGenForView(tableAttr)
        }

        $scope.pageChangeHandler = function (num) {
            if (vm.rfcGenerate && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
            loadRepGenForView(tableAttr)
        };
        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadRepGenForView(tableAttr)
        }

        /* Pagination Table Ends */

        function loadRepGenForView(tableAttr) {
            tableAttrForExp = tableAttr
            // _.extend(tableAttr,daObj)
            try {
                APIFactory.getRFCReport(tableAttr, daObj,function (response) {
                    if (response.status && response.data != null) {
                        if(Object.keys(response.data).length > 0){
                            vm.rfcGenerate = response.data
                            $scope.tableConfig.totalItems = response.count
                            vm.totalNumberOfItem = response.count
							vm.exportFlagShow = true
							$scope.searchKeyClickedOnScreen = true
                            $rootScope.saveformData()
                        }else {
                            vm.rfcGenerate = []
							$scope.tableConfig.totalItems = response.count
                            vm.totalNumberOfItem = response.count
							vm.exportFlagShow = false
							$scope.searchKeyClickedOnScreen = false
                        }
                    } else {
                        vm.rfcGenerate = []
						$scope.tableConfig.totalItems = response.count
                        vm.totalNumberOfItem = response.count
						vm.exportFlagShow = false
						$scope.searchKeyClickedOnScreen = false
                    }
                })
            } catch (err) { }

        }

    }

    $scope.exportReport = function () {

        if (!vm.reportGenre.startDateTime) {
            Toaster.sayWarning("Provide Start Date/Time");
            return;
        }
        if(vm.searchByDT == 'By RFC Number'){
            if (!vm.reportGenre.rfcNumber) {
                Toaster.sayWarning("Provide RFC Number");
                return;
            }
        }
        if (!vm.reportGenre.endDateTime) {
            Toaster.sayWarning("Provide End Date/Time");
            return;
        }
        var from = moment(vm.reportGenre.startDateTime, "MM-DD-YYYY HH:mm")
        var to = moment(vm.reportGenre.endDateTime, "MM-DD-YYYY HH:mm")
        if (to.diff(from, "minutes") === 0) {
            Toaster.sayWarning("From time and To time should not be same");
            return;
        }
        if (to.diff(from, "minutes") < 0) {
            Toaster.sayWarning("End date/time must be after Start date/time");
            return;
        }

        if (!$scope.selectedSystemsReportGenre || $scope.selectedSystemsReportGenre.length <= 0) {
            Toaster.sayWarning("Select Target System(s)");
            return;
        }
        var searchsys = []
        searchsys = $scope.selectedSystemsReportGenre

        if (searchsys[0] == 0) {
            searchsys = _.pluck(vm.targetSys, "name")
        }
        for (s in searchsys) {
            searchsys[s] = searchsys[s]
        }

        var repGenForm = {
            "startDate": vm.reportGenre.startDateTime + ":00 " + moment(vm.reportGenre.startDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
            "endDate": vm.reportGenre.endDateTime + ":00 " + moment(vm.reportGenre.endDateTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ"),
            "systems": (searchsys)
        }
        var repGenFormForRFCNumber = {
            "startDate": null,
            "endDate": null,
            "systems": null,
            "rfcNumber": vm.reportGenre.rfcNumber
        }
        if (vm.searchByDT == 'By Load Date/Time') {
            expRep(repGenForm)
        } else {
            expRep(repGenFormForRFCNumber)
        }

    }
    function expRep(daObj) {
        if (vm.searchByDT == 'By Load Date/Time'){
            try {
                APIFactory.rfcDetailsExportExcel(tableAttrForExp,daObj, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], {
                            type: response.metaData
                        });
                        var dateObj = new Date()
                        var fileName = "SEARCH_RESULT_RFC_REPORT" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xls"
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }
        }else{
            try {
                APIFactory.rfcDetailsExportExcel({},daObj, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], {
                            type: response.metaData
                        });
                        var dateObj = new Date()
                        var fileName = "SEARCH_RESULT_" + dateObj.getDate() + (dateObj.getMonth() + 1) + dateObj.getFullYear() + "_" + dateObj.getHours() + dateObj.getMinutes() + dateObj.getSeconds() + ".xls"
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) { }
        }
    }
    
    // IPService.getExpandView($scope, vm)

    // /* Pagination Table Starts */
    // var columnsToBeSorted = []// ["planid", "csrnumber", "loadcategory", "targetsystem", "planstatus", "qastatus", "loaddatetime", "activateddatetime", "fallbackdatetime"]
    // var tableAttr;
    // var initTableSettings = function () {
    //     $scope.tableConfig = Paginate.tableConfig()
    //     $scope.pageSizeList = Paginate.pageSizeList()
    //     tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
    //     $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
    // }
    // initTableSettings()
    // $scope.switchPageSize = function () {
    //     tableAttr.offset = 0
    //     tableAttr.limit = $scope.tableConfig.pageSize
    //     $scope.tableConfig.currentPage = 1
    //     loadRepGen(tableAttr)
    // }

    // $scope.refresh = function () {
    //     initTableSettings()
    //     tableAttr.offset = $rootScope.paginateValue
    //     loadRepGen(tableAttr)
    // }

    // $scope.pageChangeHandler = function (num) {
    //     if (vm.depActFileList && $scope.tableConfig.lastLoadedPage === num) {
    //         return;
    //     }
    //     $scope.tableConfig.lastLoadedPage = num
    //     tableAttr.offset = num - 1
    //     localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
    //     tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
    //     $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
    //     loadRepGen(tableAttr)
    // };
    // $scope.pageChangeHandler($scope.tableConfig.currentPage)
    // $scope.sort = function (columnName) {
    //     var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
    //     tableAttr = lSort.tableAttr
    //     $scope.tableConfig = lSort.tableConfig
    //     $scope.sortColumn = lSort.sortColumn
    //     loadRepGen(tableAttr)
    // }
    $scope.dropDownChange = function(value){
        $scope.searchKeyClickedOnScreen = false;
		vm.exportFlagShow = false
        vm.reportGenre.rfcNumber = ""
    }

    $scope.rfcGenerateExpand = function(pObj, id, data) {
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
})