dashboard.controller("loadCategories", function ($rootScope, $templateCache, appSettings, $scope,
    $state, $location, $filter, Toaster, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;

    var table = null
    var selectedRowId = null;
    vm.timeSlot = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
    vm.loadCategoryList = []
    vm.direction = ['down', 'right']
    $scope.timeZone = Access.refactorTimeZone(moment().tz(getTimeZone()).format("z"));
    Paginate.refreshScrolling();
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
        loadCategoryList(tableAttr)
    }

    $scope.refresh = function () {
        initTableSettings()
        tableAttr.offset = $rootScope.paginateValue
        loadCategoryList(tableAttr)
    }

    $scope.pageChangeHandler = function (num) {
        if (vm.loadCategoryList && $scope.tableConfig.lastLoadedPage === num) {
            return;
        }
        $scope.tableConfig.lastLoadedPage = num
        tableAttr.offset = num - 1
        localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
        tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
        $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
        loadCategoryList(tableAttr)
    };
    $scope.pageChangeHandler($scope.tableConfig.currentPage)
    $scope.sort = function (columnName) {
        var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
        tableAttr = lSort.tableAttr
        $scope.tableConfig = lSort.tableConfig
        $scope.sortColumn = lSort.sortColumn
        loadCategoryList(tableAttr)
    }
    /* Pagination Table Ends */

    function loadCategoryList(tableAttr) {
        selectedRowId = null;
        APIFactory.getLoadCategoriesList(tableAttr, function (response) {
            if (response.status && response.data.length > 0) {
                var systemList = _.pluck(response.data, "id")
                APIFactory.getLoadWindowList({ "ids": systemList }, function (response_window) {
                    _.map(response.data, function (obj) {
                        obj.loadWindow = []
                        for (rwObj in response_window.data) {
                            var lObj = response_window.data[rwObj]
                            if (lObj.loadCategoryId.id == obj.id) {
                                obj.loadWindow.push(lObj)
                            }
                        }
                    })
                    for (rObj in response.data) {
                        response.data[rObj].loadDaysOfWeekWithTime = _.groupBy(response.data[rObj].loadWindow, "daysOfWeek")


                    }
                })
                vm.loadCategoryList = response.data
                $scope.tableConfig.totalItems = response.count
                vm.totalNumberOfItem = response.count
                $rootScope.saveformData()
            } else {
                vm.loadCategoryList = []
            }
        })
    }

    //deactivate load category dialog box --

    $scope.confirmDeactivateLoadCategory = function (ev, id) {

        try {
            // Appending dialog to document.body to cover sidenav in docs app
            var confirm = $mdDialog.confirm()
                .title('Would you like to deactivate ?') //.textContent('All of the banks have agreed to forgive you your debts.')
                .ariaLabel('Confirm deactivate load Category')
                .targetEvent(ev)
                .ok('Yes')
                .cancel('No');
            $mdDialog.show(confirm).then(function () {
                vm.deactivateLoad(id);
            }, function () { });
        } catch (err) { }
    };

    vm.deactivateLoad = function (id) {

        try {
            selectedRowId = id
            APIFactory.deleteLoadCategory({ "id": selectedRowId }, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Load Category successfully de-activated");
                    initTableSettings()
                    loadCategoryList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        } catch (err) { }
    }

    /*   var newJson = []
      for (data in json.loadWindows) {
          var newData = json.loadWindows[data]
          var dummyArr = []
          for (nD in newData) {
              dummyArr.push(newData[nD] + ":00")
          }
          newJson.push({
              "day": data,
              "time": dummyArr
          })
      }
      json.loadWindows = newJson */
    /**************  Insert Dialog - Starts  ********************/
    $scope.showInsertDialog = function (ev) {
        try {
            $mdDialog.show({
                controller: insertDialogCtrl,
                templateUrl: 'html/templates/insertLoadCategory.template.html',
                controllerAs: 'vmid',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });
        } catch (err) { }

    }

    $scope.showUpdateDialog = function (ev, loadObj) {
        try {
            selectedRowId = loadObj
            $mdDialog.show({
                controller: updateDialogCtrl,
                templateUrl: 'html/templates/updateLoadCategory.template.html',
                controllerAs: 'vmid',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });
        } catch (err) { }
    }




    function insertDialogCtrl($scope, $mdDialog) {
        try {
            var vmid = this;
            vmid.form = {}
            vmid.form.loadCategory = {}
            vmid.form.loadCategory.systemId = {}

            vmid.loadDays = []
            vmid.timeslots = {}
            vmid.daysList = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]

            APIFactory.getSystemList(function (response) {
                if (response.status && response.data.length > 0) {
                    vmid.systemsList = response.data
                    $rootScope.saveformData()
                } else {
                    vmid.systemsList = []
                }
            })

            $scope.currentDate = new Date();
            $scope.hide = function () {
                try {
                    $mdDialog.hide();
                } catch (err) { }
            };

            $scope.cancel = function () {
                try {
                    $mdDialog.cancel();
                } catch (err) { }
            };

            $scope.answer = function (answer) {
                try {
                    $mdDialog.hide(answer);
                } catch (err) { }

            };
            $scope.addSlot = function (dayName) {
                try {
                    vmid.timeslots[dayName] = vmid.timeslots[dayName] ? vmid.timeslots[dayName] : []
                    vmid.timeslots[dayName].push({
                        "daysOfWeek": dayName,
                        "timeSlot": ""
                    })
                } catch (err) { }

            }

            vmid.submitInsertCategory = function (data) {
                try {
                    var fieldData = angular.copy(data)


                    if (!data.loadCategory.systemId || !Object.keys(data.loadCategory.systemId).length) {
                        Toaster.sayWarning("Please choose Target system");
                        return;
                    }
                    if (!data.loadCategory.name) {
                        Toaster.sayWarning("Please enter Load category name");
                        return;
                    }
                    if (!data.loadCategory.description) {
                        Toaster.sayWarning("Please enter Load category description");
                        return;
                    }

                    if (Object.keys(vmid.loadDays).length == 0 || _.keys(_.pick(vmid.loadDays, _.identity)).length == 0) {
                        Toaster.sayWarning("Please select any one days");
                        return;
                    }

                    for (day in vmid.daysList) {
                        if (vmid.loadDays[vmid.daysList[day]]) {
                            if (!vmid.timeslots[vmid.daysList[day]] || vmid.timeslots[vmid.daysList[day]].length == 0) {
                                Toaster.sayWarning("Please add time slot for " + vmid.daysList[day])
                                return;
                            }
                        }
                    }


                    var formVM = angular.copy(vmid.form)
                    var timeSlots = angular.copy(vmid.timeslots)
                    formVM.loadWindows = []
                    for (dL in vmid.daysList) {
                        if (vmid.loadDays[vmid.daysList[dL]]) {
                            _.map(timeSlots[vmid.daysList[dL]], function (tObj) {
                                tObj.timeSlot = tObj.timeSlot + ":00";
                            })

                            formVM.loadWindows = formVM.loadWindows.concat(timeSlots[vmid.daysList[dL]])
                        }
                    }
                    formVM.loadWindows = _.uniq(formVM.loadWindows)
                    APIFactory.saveLoadCategory(formVM, function (response) {
                        if (response.status) {
                            Toaster.saySuccess("Load Category Created Successfully")
                            $mdDialog.hide();
                            initTableSettings()
                            loadCategoryList(tableAttr)
                        } else {
                            Toaster.sayError(response.errorMessage);
                        }
                    })
                } catch (err) { }
            };
        } catch (err) { }

    }
    /**************  Insert Dialog - Ends  ********************/

    $('#upper').keyup(function () {
        this.value = this.value.toUpperCase();
    });


    function updateDialogCtrl($scope, $mdDialog) {
        try {
            var vmid = this;
            vmid.form = {}
            vmid.form.loadCategory = angular.copy(selectedRowId)
            delete vmid.form.loadCategory.loadDaysOfWeekWithTime

            vmid.form.loadWindows = vmid.form.loadCategory.loadWindow
            delete vmid.form.loadCategory.loadWindow
            vmid.loadDays = []
            vmid.timeslots = {}
            vmid.daysList = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]


            APIFactory.getSystemList(function (response) {
                if (response.status && response.data.length > 0) {
                    vmid.systemsList = response.data
                    $rootScope.saveformData()
                } else {
                    vmid.systemsList = []
                }
            })

            for (dL in vmid.daysList) {
                var filtered_loadwindows = _.where(vmid.form.loadWindows, { "daysOfWeek": vmid.daysList[dL] })
                _.each(filtered_loadwindows, function (dObj) {
                    dObj.timeSlot = $filter("replaceSeconds")(dObj.timeSlot)
                    //moment(dObj.timeSlot, appSettings.apiDTFormat).tz(getTimeZone()).format("HH:mm")
                })

                vmid.timeslots[vmid.daysList[dL]] = filtered_loadwindows
            }
            vmid.loadDays = vmid.loadDays ? vmid.loadDays : []
            for (day in vmid.form.loadWindows) {
                vmid.loadDays[vmid.form.loadWindows[day].daysOfWeek] = true;
            }

            $scope.currentDate = new Date();
            $scope.hide = function () {
                try {
                    $mdDialog.hide();
                    selectedRowId = null;
                } catch (err) { }
            };

            $scope.cancel = function () {
                try {
                    $mdDialog.cancel();
                } catch (err) { }
            };

            $scope.answer = function (answer) {
                try {
                    $mdDialog.hide(answer);
                } catch (err) { }
            };
            $scope.addSlot = function (dayName) {
                try {
                    vmid.timeslots[dayName].push({
                        "daysOfWeek": dayName,
                        "timeSlot": ""
                    })
                } catch (err) { }
            }
            $scope.removeSlot = function (timeSlotObj, dayObj, dayList, currentDay) {
                try {
                    dayObj.splice(dayObj.indexOf(_.findWhere(dayObj, { "id": timeSlotObj.id })), 1)
                    if (dayObj.length == 0) {
                        delete dayList[currentDay]
                    }
                } catch (err) { }
            }



            vmid.submitUpdateCategory = function (data) {
                try {
                    var json = angular.copy(data)

                    if (!data.loadCategory.systemId || !Object.keys(data.loadCategory.systemId).length) {
                        Toaster.sayWarning("Please choose Target system");
                        return;
                    }
                    if (!data.loadCategory.name) {
                        Toaster.sayWarning("Please enter Load category name");
                        return;
                    }
                    if (!data.loadCategory.description) {
                        Toaster.sayWarning("Please enter Load category description");
                        return;
                    }


                    if (Object.keys(vmid.loadDays).length == 0 || _.keys(_.pick(vmid.loadDays, _.identity)).length == 0) {
                        Toaster.sayWarning("Please select any one days");
                        return;
                    }

                    for (day in vmid.daysList) {
                        if (vmid.loadDays[vmid.daysList[day]]) {
                            if (!vmid.timeslots[vmid.daysList[day]] || vmid.timeslots[vmid.daysList[day]].length == 0) {
                                Toaster.sayWarning("Please add time slot for " + vmid.daysList[day])
                                return;
                            }
                        }
                    }
                    var formVM = angular.copy(vmid.form)
                    var timeSlots = angular.copy(vmid.timeslots)
                    formVM.loadWindows = []
                    for (dL in vmid.daysList) {
                        if (vmid.loadDays[vmid.daysList[dL]]) {
                            _.map(timeSlots[vmid.daysList[dL]], function (tObj) {
                                tObj.timeSlot = tObj.timeSlot + ":00";
                            })
                            formVM.loadWindows = formVM.loadWindows.concat(timeSlots[vmid.daysList[dL]])
                        }
                    }
                    formVM.loadWindows = _.uniq(formVM.loadWindows)
                    APIFactory.updateLoadCategory(formVM, function (response) {
                        if (response.status) {
                            Toaster.saySuccess("Load Category Update Successfully")
                            $mdDialog.hide();
                            initTableSettings()
                            loadCategoryList(tableAttr)
                        } else {
                            Toaster.sayError(response.errorMessage);
                        }
                    })
                } catch (err) { }


            };
        } catch (err) { }

    }
    /**************  Update Dialog - Ends  ********************/



})