dashboard.controller("updateImpPlanCtrl", function ($rootScope, $state, $stateParams, $scope, $timeout, $location, appSettings, Toaster, $http,
    fImplementationPlanValidate, $mdDialog, apiService, APIFactory, WFLogger, WSService, freezeService, IPService, Access, Paginate) {

    var vm = this;
    var exceptionDate;
    var appInfo = JSON.parse(localStorage.getItem("appInfo"))
    var apiBase = appSettings.apiBase;
    if (appInfo.isSSOApp) {
        apiBase = appSettings.apiSSOBase;
    }
    $scope.userRole = getUserData('userRole')
    $rootScope.titleHeading = $state.current.data.pageTitle + " > Edit"
    $rootScope.prevSaveButton = true;
    var responseData;
    var eventListerner;
    $scope.planAuthorize = true;
    vm.rflag = false;
    $scope.receivedLoadAttendeeList = false

    Paginate.refreshScrolling();

    vm.leadId = $rootScope.home_menu.id;
    vm.leadName = $rootScope.home_menu.displayName;
    vm.showSystem = false;
    vm.pList = ""
    vm.planDefaults = {}
    vm.systemDefaults = []
    vm.allSystemInformationWithAdditionalParameters = {}
    var loadCalendarByDateFreeze = []
    var loadCategoriesOfSystem = []
    vm.exMomentstartdate = {}
    $scope.formDisabled = false
    vm.updateButtonDisabled = true;
    vm.disableLoadCategorybyMacroHeader = {}
    vm.loader = { update: false };
    vm.loadTypeChangeComment = ""
    $scope.timeZone = Access.refactorTimeZone(moment().tz(getTimeZone()).format("z"));
    /*
        Init Validation factory
    */
    $scope.fImplementationPlanValidate = fImplementationPlanValidate

    var systemCountOnLoad = 0;
    /*
        Load Platform List
    */
    var loadPlatforms = function () {
        try {
            APIFactory.getPlatformList({}, function (response) {
                if (response.status) {
                    vm.pList = response.data
                } else { }
            })
        } catch (err) { }
    }


    /*
    Load CreatedBy list
    */
    vm.loadLeadName = function (cbk) {
        try {
            APIFactory.getUsersByRole({ "role": "Lead" }, function (response) {
                if (response.status && response.data.length > 0) {
                    vm.createdByList = response.data;
                    if (cbk) {
                        cbk(true)
                    }
                } else {
                    vm.createdByList = []
                    if (cbk) {
                        cbk(false)
                    }
                }
                $timeout(function () {
                    // initMultipleSelect2("#approver")
                    $("#createdby").select2({
                        maximumSelectionLength: 1
                    });
                    // if (_.where(vm.createdByList, { "id": vm.impPlan.leadId }).length== 0) {
                    //     $("#createdby").val("").trigger("change")
                    //     Toaster.sayWarning("Pre selected Lead has been removed from LDAP. Please Contact Administrator")
                    // }
                }, 1000)
            })
        } catch (err) { }
    }

    // Target system disable after plan secure

    $scope.checkSystemDisable = function (planstatus) {
        var currentRole = getUserData("userRole");
        if (currentRole == 'QA' || implementationPlanStatus().indexOf(planstatus) >= 1) {
            return true
        }
        return false
    }

    /*
        Load devmanager list
    */
    vm.loadDevManagerName = function (cbk) {
        try {
            APIFactory.getUsersByRole({ "role": "DevManager" }, function (response) {
                if (response.status && response.data.length > 0) {
                    vm.devManagerList = response.data;
                    if (cbk) {
                        cbk(true)
                    }
                } else {
                    vm.devManagerList = []
                    if (cbk) {
                        cbk(false)
                    }
                }
                $timeout(function () {
                    // initMultipleSelect2("#approver")
                    $("#approver").select2({
                        maximumSelectionLength: 1
                    });
                    // if (_.where(vm.devManagerList, { "id": vm.impPlan.devManager }).length == 0) {
                    //     // Toaster.sayWarning("Pre selected devmanager id not found in the DevManager List")
                    //     $("#approver").val("").trigger("change")
                    //     Toaster.sayWarning("Pre selected Dev Manager has been removed from LDAP. Please Contact Administrator")
                    // }
                }, 1000)
            })
        } catch (err) { }
    }


    // Load Attendee List
    vm.getLoadAttendeeList = function (cbk) {
        try {

            APIFactory.getLoadAttendeeList({}, function (response) {
                if (response.status && response.data.length > 0) {
                    vm.loadAttendeeList = response.data;
                    if (cbk) {
                        cbk(true)
                    }
                } else {
                    vm.loadAttendeeList = []
                    if (cbk) {
                        cbk(false)
                    }
                }

            })
        } catch (err) { }
    }

    // QA Functional Tester List
    vm.getQaFunctionalTesterList = function (cbk) {
        try {

            APIFactory.getUsersByRole({ "role": "QA" }, function (response) {
                if (response.status) {
                    vm.qaFunctionalTesterList = response.data;
                    if (cbk) {
                        cbk(true)
                    }
                } else {
                    vm.qaFunctionalTesterList = []
                    if (cbk) {
                        cbk(false)
                    }
                }
                // $timeout(function () {
                //     if(vm.peerReviewers)
                //     for(i in vm.imp.peerReviewers){
                //         if(_.where(vm.peerReviewers, {"id" : vm.imp.peerReviewers[i]}).length == 0){
                //             checkExistingPeerReviewer = true
                //         }
                //     }
                //     if(checkExistingPeerReviewer && vm.imp.bypassPeerReview == false){
                //         $("#peerReviewers").val("").trigger("change");
                //         Toaster.sayWarning("Pre selected Peer Reviewer has been removed from LDAP. Please Contact Administrator")
                //     }else{
                //         $("#peerReviewers").val(vm.imp.peerReviewers).trigger("change");
                //     }
                // }, 500);

            })
        } catch (err) { }
    }



    $scope.addDbcrNum = function (ev, sysId, sysName) {
        $mdDialog.show({
            controller: dbcrCtrl,
            controllerAs: "dbcr",
            templateUrl: 'html/templates/dbcr.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {
                "systemId": sysId,
                "systemName": sysName
            }
        })
            .then(function (answer) {

            }, function () {

            });
    }

    function dbcrCtrl($scope, $mdDialog, systemId, systemName) {
        var dbcr = this;
        dbcr.fieldData = {}
        dbcr.validation = {
            "showText": false,
            "disableButton": true
        }
        dbcr.systemName = systemName

        $timeout(function () {
            $('[aria-label="dbcrNumber"]').focus()
        }, 1000)

        $scope.validateDbcr = function () {
            if (dbcr.fieldData.dbcrName.length < 4) {
                dbcr.validation.showText = false
                dbcr.validation.disableButton = true
                delete dbcr.fieldData.environment
                return
            }
            dbcr.fieldData.systemId = systemId
            APIFactory.validateDbcr(dbcr.fieldData, function (response) {
                if (response.status) {
                    dbcr.fieldData.environment = response.data.environment;
                    dbcr.validation.showText = false
                    dbcr.validation.disableButton = false
                } else {
                    dbcr.validation.showText = true
                    dbcr.validation.disableButton = true
                    delete dbcr.fieldData.environment
                }
            })
        }
        $scope.dbcrList = []
        $scope.dbcrLoadAdd = function () {
            if (_.where($scope.dbcrList, { "dbcrName": dbcr.fieldData.dbcrName }).length > 0) {
                Toaster.sayWarning("DBCR already added")
                return
            }
            $scope.dbcrList.push(dbcr.fieldData)
            dbcr.fieldData = {}
            dbcr.env = null
            dbcr.validation.showText = false
            dbcr.validation.disableButton = true
        }

        $scope.concatSystemName = function () {
            if (dbcr.fieldData.environment) {
                dbcr.env = dbcr.systemName + ' ' + dbcr.fieldData.environment
                return dbcr.systemName + ' ' + dbcr.fieldData.environment
            } else {
                dbcr.env = null
            }

        }

        $scope.loadDBCRList = function () {
            APIFactory.getDbcrList({ "planIds": vm.impPlan.id }, function (response) {
                if (response.status) {
                    if (response.data && response.data.length > 0) {
                        $scope.dbcrList = []
                        _.each(response.data, function (dbcrObj) {
                            if (dbcrObj.systemId.id == systemId) {
                                $scope.dbcrList.push(dbcrObj)
                            }
                        })
                        for (index in $scope.dbcrList) {
                            if ($scope.dbcrList[index].mandatory == "Y") {
                                $scope.dbcrList[index].mandatory = true
                            } else {
                                $scope.dbcrList[index].mandatory = false
                            }
                        }
                    } else {
                        $scope.dbcrList = []
                    }
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }
        $scope.loadDBCRList()
        $scope.disableSaveDbcr = false;

        $scope.saveDBCRList = function () {
            $scope.disableSaveDbcr = true;
            var DBCRList = $scope.dbcrList
            for (index in DBCRList) {
                if (!DBCRList[index].planId) {
                    DBCRList[index].planId = {}
                    DBCRList[index].planId.id = vm.impPlan.id
                    DBCRList[index].systemId = {}
                    DBCRList[index].systemId.id = systemId
                }
                if (DBCRList[index].mandatory == true) {
                    DBCRList[index].mandatory = "Y"
                } else {
                    DBCRList[index].mandatory = "N"
                }
            }
            $scope.disableSaveDbcr = true;
            APIFactory.saveDbcrList({}, DBCRList, function (response) {
                if (response.status) {
                    $scope.disableSaveDbcr = true;
                    vm.updateSystemDBCR()
                    $mdDialog.cancel();
                } else {
                    $scope.disableSaveDbcr = false;
                    Toaster.sayError(response.errorMessage)
                }
            })
            $scope.disableSaveDbcr = false;
        }



        $scope.deleteDBCRItem = function (dbcrObj, index) {
            if (!dbcrObj.id) {
                $scope.dbcrList.splice(index, 1)
                return;
            }
            APIFactory.deleteDbcr({ "dbcrId": dbcrObj.id }, function (response) {
                if (response.status) {
                    $scope.loadDBCRList()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        $scope.cancel = function () {
            $mdDialog.cancel();
        };
    }

    // get Load Category R from each System
    vm.getRcategory = function (systemId) {

        var lObj = _.where(vm.loadWindowCategories[systemId], { id: parseInt(vm.impPlan.system[systemId].loadCategoryId) })[0]

        if (lObj.name == 'R') {
            vm.rflag = true;
        } else {
            vm.rflag = false;
        }

    }

    var getPlanStatus = function () {
        try {
            APIFactory.getPlanStatusListForLabel({}, function (response) {
                if (response.status) {
                    vm.planStatusList = response.data
                } else {
                    vm.planStatusList = []
                }
            })
        } catch (err) { }
    }

    $scope.changeMacroHeader = function (status) {
        var macroHeader;
        if (!vm.impPlan.loadType != 'STANDARD') {
            if (vm.impPlan.macroHeader) {
                macroHeader = vm.impPlan.macroHeader;
            }
        }

        _.each(vm.systemDetailsList, function (systemId) {
            getLoadCategoryList(systemId.id, "Y", function (data) {
                vm.loadWindowCategories[systemId.id] = data
                if (macroHeader) {
                    var mcategoryId = _.find(data, { name: "M" })
                    if (mcategoryId != undefined) {
                        vm.impPlan.system[systemId.id].loadCategoryId = mcategoryId.id;

                    }
                }

            })
        })
        if (!status && vm.impPlan.system) {
            for (var prop in vm.impPlan.system) {
                vm.impPlan.system[prop].loadCategoryId = "";
                vm.impPlan.system[prop].loadWindowType = "";
                vm.impPlan.system[prop].loadDateTime = "";
                vm.impPlan.system[prop].loadCategoryDate = "";
            }
        }
        else {
            if (status && vm.impPlan.system && getUserData("userRole") == "Lead") {
                for (var dData in vm.impPlan.system) {
                    vm.impPlan.system[dData].loadCategoryId = "";
                    vm.impPlan.system[dData].loadWindowType = "";
                    vm.impPlan.system[dData].loadDateTime = "";
                    vm.impPlan.system[dData].loadCategoryDate = "";
                }
            }

        }
    }

    $scope.changeLoadTime = function (loadDate, loadTime, systemId) {
        var fulldate = loadDate + " " + loadTime + ":00 " + moment(loadDate + " " + loadTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ");
        var selectDiffTime = moment().tz(getTimeZone()).diff(moment(fulldate, "MM-DD-YYYY HH:mm:ss ZZ"));
        if (loadDate) {
            if (selectDiffTime > 0) {
                vm.impPlan.system[systemId].exceptionLoadtime = "";
                Toaster.sayWarning("Choose Load Time after the Current Time")
                return;
            }
        } else {
            Toaster.sayWarning("Choose Load Date")
            return;
        }
    }

    vm.selectedSystem = "Select System"
    vm.systemsList = ""
    /*
        Load Systems for the selected platform
    */
    $scope.selectedPlatform = function (pId, sId, next) {
        try {
            vm.systemDetailsList = []
            var pName = _.where(vm.pList, { platformId: pId });
            var loadCategoryId = vm.responseData.system[sId].loadCategoryId

            if (responseData.loadType == "EXCEPTION" || responseData.loadType == "EMERGENCY") {
                vm.impPlan.system[sId].exceptionLoaddate = moment(responseData.system[sId].loadDateTime, appSettings.uiDTFormat).format(appSettings.dateFormat)
                vm.exMomentstartdate[sId] = _.clone(vm.impPlan.system[sId].exceptionLoaddate);
                $rootScope.exceptionDate = vm.impPlan.system[sId].exceptionLoaddate
            }
            if ($rootScope.deltaUser && vm.impPlan.platformId == '2' && (vm.impPlan.macroHeader == undefined || vm.impPlan.macroHeader == false)) {
                vm.impPlan.rfcFlag = true;
            } else {
                vm.impPlan.rfcFlag = false;
            }

            if (loadCategoryId == '') {
                vm.disableLoadCategorybyMacroHeader[sId] = true
            } else {
                vm.disableLoadCategorybyMacroHeader[sId] = false
            }
            APIFactory.getSystemByPlatform({ "ids": pId }, function (response) {
                if (!vm.impPlan.systems) {
                    vm.impPlan.systems = {}
                }
                if (!vm.impPlan.system) {
                    vm.impPlan.system = {}
                }
                vm.systemsList = _.sortBy(response.data, "name")
                vm.showSystem = true;
                vm.showSystemDetails = true;
                var systemsObj = {
                    "id": sId,
                    "name": _.where(response.data, { id: parseInt(sId) })[0].name
                }
                vm.systemDetailsList.push(systemsObj)
                if ($scope.receivedLoadAttendeeList && responseData.system[sId].loadAttendeeId != null && responseData.system[sId].loadAttendeeId.length > 0 && _.where(vm.loadAttendeeList, { "id": responseData.system[sId].loadAttendeeId }).length == 0) {
                    Toaster.sayWarning("Pre selected Load Attendee has been removed from LDAP. Please Contact Administrator")
                    vm.impPlan.system[sId].loadAttendee = ""
                } else {
                    vm.impPlan.system[sId].loadAttendee = responseData.system[sId].loadAttendeeId
                }

                if (vm.impPlan.system[sId].qaFunctionalTesters !== null) {
                    vm.impPlan.system[sId].qaFunctionalTesters = responseData.system[sId].qaFunctionalTesters.split(",");
                }

                vm.impPlan.system[sId].preLoadJustify = responseData.system[sId].preLoadJustify
                if (responseData.loadType == "EXCEPTION" || responseData.loadType == "EMERGENCY") {
                    IPService.alignStructure(vm, sId, "exceptionType")
                    // vm.onDateChange("", "", sId)
                    vm.startDate = moment().format('MM/DD/YYYY')
                    getLoadCategoryList(sId, "", function (data) {
                        vm.loadWindowCategories[sId] = data
                    })
                    vm.responseData.system[sId].loadCategoryId = loadCategoryId
                    if (responseData.system[sId].loadDateTime) {
                        vm.impPlan.system[sId].exceptionLoaddate = moment(responseData.system[sId].loadDateTime, appSettings.uiDTFormat).format(appSettings.dateFormat)
                        vm.exMomentstartdate[sId] = _.clone(vm.impPlan.system[sId].exceptionLoaddate);
                        $rootScope.exceptionDate = vm.impPlan.system[sId].exceptionLoaddate
                        if (moment(vm.exMomentstartdate[sId], appSettings.dateFormat).diff(moment().format(appSettings.dateFormat)) >= 0) {
                            vm.exMomentstartdate[sId] = moment().format(appSettings.dateFormat)
                        }
                        vm.impPlan.system[sId].exceptionLoadtime = moment(responseData.system[sId].loadDateTime, appSettings.uiDTFormat).format(appSettings.timeFormat)
                    } else {
                        vm.exMomentstartdate[sId] = moment().format('MM/DD/YYYY')
                    }
                    if (vm.impPlan.system[sId].putLevelId) {
                        vm.impPlan.system[sId].putIndicator = true
                    }
                    vm.systemDefaults[sId] = []
                    $scope.populatePutLevel(sId, function (defaultPutID) {
                        if (vm.impPlan.system[sId].putLevelId != defaultPutID) {
                            vm.impPlan.system[sId].putIndicator = true
                        } else {
                            vm.impPlan.system[sId].putIndicator = false
                        }
                    })
                } else {
                    // vm.impPlan.system[sId].loadWindowType = getUserPrefs("loadWindowType") ? getUserPrefs("loadWindowType") : "2"
                    vm.impPlan.system[sId].loadWindowType = "2"
                    IPService.alignStructure(vm, sId, "standardTypeByCategory")
                    /*
                     Select By Category
                     */

                    getLoadCategoryList(sId, "Y", function (data) {
                        vm.loadWindowCategories[sId] = data
                        if (loadCategoryId != "") { // Check for system with empty values
                            vm.responseData.system[sId].loadCategoryId = loadCategoryId
                            vm.loadWindowCalendar(sId, function (response) {
                                if (response) {
                                    var toAdd = moment().add(3, 'days');
                                    vm.startDate = toAdd.format('MM-DD-YYYY');
                                    vm.impPlan.system[sId].loadCategoryDate = moment(responseData.system[sId].loadDateTime, appSettings.uiDTFormat).format(appSettings.dateFormat)
                                    updateSelectedDate(vm.impPlan.system[sId].loadCategoryDate, sId, false)
                                    // vm.impPlan.system[sId].loadtime = moment(responseData.system[sId].loadDateTime, appSettings.uiDTFormat).format(appSettings.timeFormat)
                                }
                            })
                        }
                        vm.systemDefaults[sId] = []
                        $scope.populatePutLevel(sId, function (defaultPutID) {
                            if (vm.impPlan.system[sId].putLevelId != defaultPutID) {
                                vm.impPlan.system[sId].putIndicator = true
                            }
                        })
                    })
                }
                vm.impPlan.system[sId].preload = responseData.system[sId].preload ? responseData.system[sId].preload : "No"
                vm.impPlan.system[sId].iplRequired = responseData.system[sId].iplRequired ? responseData.system[sId].iplRequired : "No"

                if (responseData.system[sId].exceptionLoaddate == undefined) {
                    vm.impPlan.system[sId].exceptionLoaddate = $rootScope.exceptionDate
                }
                $("#probTktNo").val(vm.impPlan.sdmTktNum)
                $("#probTktNo").tagit({
                    singleField: true,
                    caseSensitive: false,
                    singleFieldNode: $('#probTktNo'),
                    afterTagAdded: function (event, ui) {
                        tktAdded($(ui.tag).find("span").html())
                    },
                    readOnly: $scope.PblmTktNumDisable
                })
                $("#relatedPlan").val(vm.impPlan.relatedPlans)
                $("#relatedPlan").tagit({
                    singleField: true,
                    caseSensitive: false,
                    singleFieldNode: $('#relatedPlan'),
                    afterTagAdded: function (event, ui) {
                        relatedPlanAdded($(ui.tag).find("span").html())
                    }
                })
                next(sId)
            })
        } catch (err) {
            next(sId)
        }

    }
    vm.showSystemDetails = false;
    vm.systemDetailsList = [];
    var simpleIndicator = 0;
    /*
        When system is selected, populate the corresponding system related fields
    */
    // This function wont be triggered on load, "simpleIndicator" is to indicate first time load
    $scope.selectSystems = function (systemId, name) {
        vm.systemDetailsList = _.compact(vm.systemDetailsList)

        try {
            var subSystemsObj = {
                "PSS": ["AIR", "RES"]
            }
            if ($(".check_" + name).hasClass("md-checked")) {
                vm.impPlan.systems[systemId] = false
                if (vm.impPlan.system[systemId]) {
                    // vm.impPlan.system[systemId].active = "N"
                    delete vm.impPlan.system[systemId]
                }
                setTimeout(function () {
                    $(".check_" + name).removeClass("md-checked")
                }, 50)
            }
            simpleIndicator++;
            if (name in subSystemsObj) {
                var keyId = _.where(vm.systemsList, { "name": name })[0].id
                for (st in subSystemsObj[name]) {
                    var valueId = _.where(vm.systemsList, { "name": subSystemsObj[name][st] })[0].id
                    if (!vm.impPlan.systems[keyId]) {
                        vm.impPlan.systems[valueId] = false;
                        // vm.impPlan.system[valueId].active = "N";
                        delete vm.impPlan.system[systemId]
                        // delete vm.impPlan.systems[valueId]
                        $scope.selectSystems(valueId, subSystemsObj[name][st]);
                    } else {
                        vm.impPlan.systems[valueId] = true;
                        vm.impPlan.system[valueId].active = "Y";
                        $scope.selectSystems(valueId, subSystemsObj[name][st]);
                    }
                }
            } else {
                for (ss in subSystemsObj) {
                    if (subSystemsObj[ss].indexOf(name) >= 0) {
                        if (!vm.impPlan.systems[_.where(vm.systemsList, { "name": name })[0].id]) {
                            vm.impPlan.systems[_.where(vm.systemsList, { "name": name })[0].id] = false;
                            delete vm.impPlan.systems[_.where(vm.systemsList, { "name": name })[0].id]
                            var systmId = _.where(vm.systemsList, { "name": name })[0].id;
                            vm.systemDetailsList = _.filter(vm.systemDetailsList, function (elem) {
                                return elem.id != systmId;
                            })
                            vm.impPlan.systems[_.where(vm.systemsList, { "name": ss })[0].id] = false;
                            delete vm.impPlan.systems[_.where(vm.systemsList, { "name": ss })[0].id]
                        }
                    }
                    var selectedSystemCount = 0;
                    for (si in subSystemsObj[ss]) {
                        try {
                            if (_.where(vm.systemsList, { "name": subSystemsObj[ss][si] }).length > 0 && vm.impPlan.systems[_.where(vm.systemsList, { "name": subSystemsObj[ss][si] })[0].systemId]) {
                                selectedSystemCount++
                            }
                            if (subSystemsObj[ss].length == selectedSystemCount) {
                                if (!vm.impPlan.systems[_.where(vm.systemsList, { "name": ss })[0].id]) {
                                    vm.impPlan.systems[_.where(vm.systemsList, { "name": ss })[0].id] = true;
                                }
                            }
                        } catch (err) { }
                    }
                }
                var returnTrue = 0;
                if (_.where(vm.systemDetailsList, { "id": systemId.toString(), "name": $.trim(name.toString()) }).length > 0) {
                    returnTrue = 1;
                }
                if ((vm.impPlan.systems[systemId] || !vm.impPlan.systems[systemId]) && returnTrue === 1) {
                    if (vm.impPlan.systems) {
                        delete vm.impPlan.systems[systemId]
                    }
                    if (vm.impPlan.system[systemId]) {
                        // vm.impPlan.system[systemId].active = "N"
                        delete vm.impPlan.system[systemId]
                    }
                    vm.systemDetailsList = _.filter(vm.systemDetailsList, function (systemItem) {
                        return systemItem.id != systemId && systemItem.name != name;
                    });
                }
                if (returnTrue === 1) {
                    return false;
                }
                vm.systemDefaults[systemId] = []
                $scope.populatePutLevel(systemId)
                vm.impPlan.systems[systemId] = true;
                vm.selectedSystem = _.where(vm.systemsList, { "id": systemId })[0].name
                var systemsObj = {
                    "id": systemId.toString(),
                    "name": name
                }
                vm.disableLoadCategorybyMacroHeader[systemId] = true
                vm.systemDetailsList.push(systemsObj)
                vm.impPlan.system[systemId] = {}
                vm.impPlan.system[systemId].preload = "No";
                vm.impPlan.system[systemId].iplRequired = "No";
                vm.showSystemDetails = true;
                detectChange(systemId)
            }
        } catch (err) { }

    }

    // Load category For macro header plan default
    vm.loadMacroHeaderCategorySystem = function (loadMacroCategorySystem, id) {
        if (vm.impPlan.macroHeader && loadMacroCategorySystem.length <= 0) {
            vm.impPlan.systems[id] = false;
            vm.systemDetailsList = _.filter(vm.systemDetailsList, function (elem) {
                return elem.id != id;
            })
            Toaster.sayWarning("load category 'M' is not available for system  which is mandatory for creating macro/header only plan. Contact Tool Admin");
            return;
        } else if (vm.impPlan.macroHeader && Object.keys(vm.impPlan.system).length > 0) {
            if (loadMacroCategorySystem.length > 0) {
                var mcategoryId = _.find(loadMacroCategorySystem, { name: "M" })
                if (vm.impPlan.system[id])
                    vm.impPlan.system[id].loadCategoryId = mcategoryId.id;
            }
            vm.loadWindowCalendar(id);
        }
    }

    var getLoadCategoryList = function (id, status, cbk) {
        APIFactory.getLoadCategoryBySystem({ "ids": id }, function (response_category) {

            if (vm.impPlan.macroHeader !== undefined && vm.impPlan.macroHeader === false) {
                vm.categoryList = []

                _.each(response_category.data, function (loadCat) {
                    if (loadCat.name !== 'M') {
                        vm.categoryList.push(loadCat)
                    }
                })

                cbk(vm.categoryList)
            }
            else {
                cbk(response_category.data)
            }
            /* var categoryList = _.pluck(response_category.data, "id")
            APIFactory.getLoadWindowList({ "ids": categoryList }, function(response) {
                if (response.status) {
                    loadWindowOfLoadCategory[id] = response.data
                    $(".planSaveBtn").removeAttr("disabled")
                    cbk(response_category.data)
                } else {
                    if (vm.impPlan.systems[id]) {
                        $(".planSaveBtn").attr("disabled", "true")
                        Toaster.sayError("Load Window not exists for the selected system. <br/> Contact Tool Admin.")
                    } else {
                        $(".planSaveBtn").removeAttr("disabled")
                    }
                    cbk([])
                    loadWindowOfLoadCategory[id] = []
                }
                /* APIFactory.getLoadFreezeByLoadCategories({ "ids": categoryList }, function(response) {
                    if (response.status) {
                        loadCalendarByDateFreeze[id] = response.data
                    } else {
                        loadCalendarByDateFreeze[id] = []
                    }
                })
            }) */
        })
    }

    vm.validateloadChanged = function () {
        return implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= 1;
    }

    vm.validateADLChanged = function () {
        var currentRole = getUserData("userRole");
        var planstatus = vm.impPlan.planStatus;
        if (vm.impPlan.planStatus == 'APPROVED') {
            planstatus = "SUBMITTED";
        }
        if (currentRole == 'Lead') {
            return implementationPlanStatus().indexOf(planstatus) >= implementationPlanStatus().indexOf("PASSED_ACCEPTANCE_TESTING");
        } else if (currentRole == 'QA' || currentRole == 'DLCoreChangeTeam') {
            return true;
        } else {
            return implementationPlanStatus().indexOf(planstatus) >= implementationPlanStatus().indexOf("DEV_MGR_APPROVED");
        }
    }

    /*
        When load type is Standard and Load Window is Selected by date
    */
    var loadDateTimeFreezeDataByDate = [];
    vm.selectByDate = function (id, name) {
        setUserPrefs("loadWindowType", 1)
        var year = moment().year();
        var month = moment().month() + 1;
        try {
            IPService.alignStructure(vm, id, "standardTypeByDate")
            if (!vm.impPlan.macroHeader) {
                if (vm.impPlan.planStatus == 'ACTIVE') {
                    IPService.clearFieldsOnChange(vm, id, "standardTypeByLoadWindowChange");
                }
            }

        } catch (err) {

        }
    }

    vm.loadWindowCategories = []
    /*
        When load type is Standard and Load Window is Selected by category
    */
    vm.selectByCategory = function (id, name) {
        setUserPrefs("loadWindowType", 2)
        try {
            IPService.alignStructure(vm, id, "standardTypeByCategory")
            if (vm.impPlan.planStatus == 'ACTIVE') {
                IPService.clearFieldsOnChange(vm, id, "standardTypeByLoadWindowChange");
            }

            var macroHeader = false;
            if (vm.impPlan.macroHeader) {
                macroHeader = vm.impPlan.macroHeader;
            }
            getLoadCategoryList(id, "Y", function (data) {
                vm.loadWindowCategories[id] = data
                if (macroHeader) {
                    var macroheadercategory = _.where(data, { name: "M" });
                    vm.loadWindowCategories[id] = macroheadercategory
                    if (vm.systemDetailsList.length > 0 && vm.loadWindowCategories.length > 0) {
                        vm.loadMacroHeaderCategorySystem(vm.loadWindowCategories[id], id);
                    }
                }

            })
        } catch (err) { }

    }

    vm.selectForEmergency = function (id, name) {
        setUserPrefs("loadWindowType", 2)
        try {
            IPService.alignStructure(vm, id, "standardTypeByCategory")
            var macroHeader = false;
            if (vm.impPlan.macroHeader) {
                macroHeader = vm.impPlan.macroHeader;
            }
            getLoadCategoryList(id, "Y", function (data) {
                vm.loadWindowCategories[id] = data
                if (macroHeader) {
                    var macroheadercategory = _.where(data, { name: "M" });
                    vm.loadWindowCategories[id] = macroheadercategory
                    if (vm.systemDetailsList.length > 0 && vm.loadWindowCategories.length > 0) {
                        vm.loadMacroHeaderCategorySystem(vm.loadWindowCategories[id], id);
                    }
                }

            })
        } catch (err) { }

    }

    vm.loadTime = []
    var loadDateTimeFreezeData = [];
    vm.selectedCategoryChange = false;
    /*
        When load category is changed
    */
    vm.loadWindowCalendar = function (systemId, cbk) {
        try {
            //vm.impPlan.system[sList.id].loadCategoryId
            if (vm.impPlan.loadType == "STANDARD") {
                if (vm.impPlan.system[systemId].loadWindowType == "1") {
                    vm.selectedCategoryChange = true;
                    vm.getRcategory(systemId)
                    updateSelectedDate(vm.impPlan.system[systemId].loaddate, systemId, false)
                } else {
                    IPService.clearFieldsOnChange(vm, systemId, "standardTypeByCategory");
                    vm.getRcategory(systemId)
                    var lObj = _.where(vm.loadWindowCategories[systemId], { id: parseInt(vm.impPlan.system[systemId].loadCategoryId) })[0]
                    if (typeof cbk == "function" & typeof lObj == "undefined") {
                        cbk(false)
                    } else {
                        APIFactory.getLoadFreezeByLoadCategories({ "ids": lObj.id }, function (response) {
                            if (response.status) {
                                loadDateTimeFreezeData[systemId] = response.data
                            } else {
                                loadDateTimeFreezeData[systemId] = []
                            }
                            if (cbk) {
                                cbk(true)
                            }
                        })
                    }
                }
            }
            checkInfochanged(systemId);
        } catch (err) { }
    }


    // When Load Time Changed
    vm.chooseLoadTime = function (systemId) {
        checkInfochanged(systemId);
    }

    vm.chooseExpLoadTime = function (systemId) {
        checkInfochanged(systemId);
    }

    $scope.showQAFunctional = function (status) {
        var currentRole = getUserData("userRole");
        if (currentRole == 'DLCoreChangeTeam') {
            return true
        } else {
            return implementationPlanStatus().indexOf(vm.impPlan.planStatus) > 3;
        }
    }
    // Special Insutruction for TSD disable
    $scope.checkInsuDisable = function (planstatus) {
        var currentRole = getUserData("userRole");
        if (currentRole == 'Lead' && implementationPlanStatus().indexOf(planstatus) >= implementationPlanStatus().indexOf('PASSED_ACCEPTANCE_TESTING')) {
            return true
        } else if (currentRole == 'DevManager' && implementationPlanStatus().indexOf(planstatus) >= implementationPlanStatus().indexOf('DEV_MGR_APPROVED')) {
            return true
        } else if (currentRole == 'LoadsControl' && implementationPlanStatus().indexOf(planstatus) >= implementationPlanStatus().indexOf('READY_FOR_PRODUCTION_DEPLOYMENT')) {
            return true
        } else if (currentRole == 'DLCoreChangeTeam') {
            return true;
        } else {
            return false
        }
    }

    // Check PutLevel  disable after secured
    $scope.checkPutLevelDisable = function (planstatus) {
        if (implementationPlanStatus().indexOf(planstatus) != 0) {
            return true
        } else {
            return false
        }
    }


    // PlanDescrption and LoadAtt Disable
    $scope.checkPlanDescAndLoadAttDisable = function (planstatus) {
        var currentRole = getUserData("userRole");
        if (implementationPlanStatus().indexOf(planstatus) >= 0) {
            if (currentRole == 'Lead' && implementationPlanStatus().indexOf(planstatus) <= implementationPlanStatus().indexOf('DEPLOYED_IN_PRE_PRODUCTION')) {
                return false
            }

            else if (currentRole == 'DevManager' && (implementationPlanStatus().indexOf(planstatus) == implementationPlanStatus().indexOf('PASSED_ACCEPTANCE_TESTING'))) {
                return false
            }
            else if (currentRole == 'DevManager' && (implementationPlanStatus().indexOf(planstatus) == implementationPlanStatus().indexOf('DEV_MGR_APPROVED'))) {
                return false
            }
            else if (currentRole == 'LoadsControl' && (implementationPlanStatus().indexOf(planstatus) == implementationPlanStatus().indexOf('DEV_MGR_APPROVED'))) {
                return false
            }
            else if (currentRole == 'LoadsControl' && (implementationPlanStatus().indexOf(planstatus) == implementationPlanStatus().indexOf('READY_FOR_PRODUCTION_DEPLOYMENT'))) {
                return false
            }

            else if (currentRole == 'TechnicalServiceDesk' && (implementationPlanStatus().indexOf(planstatus) == implementationPlanStatus().indexOf('READY_FOR_PRODUCTION_DEPLOYMENT'))) {
                return false
            }
            else if (currentRole == 'TechnicalServiceDesk' && (implementationPlanStatus().indexOf(planstatus) == implementationPlanStatus().indexOf('DEPLOYED_IN_PRODUCTION'))) {
                return false
            }
            return true
        }
    }



    /*
        Get category id from name
    */
    var getCategoryIdFromName = function (systemId, name) {
        var lObj = _.where(vm.loadWindowCategories[systemId], { name: $.trim(name) })[0]
        if (!lObj) {
            lObj = _.where(vm.loadWindowCategories[systemId], { id: parseInt(name) })[0]
        }
        return lObj.id
    }

    /*  var getLoadCategoriesListByLoadCategory = function(cId, sId, cbk) {
         APIFactory.getLoadWindowList({ "ids": cId }, function(response) {
             cbk(response.data)
         })
     } */
    vm.preloadOptions = [{ id: 3, value: "Another Program Load" }, { id: 1, value: "DBCR" }, { id: 2, value: "Functional Entry" }, { id: 4, value: "Other" }]
    vm.productionVerification = [{ id: 1, value: "PASS" }, { id: 2, value: "FAIL" }]

    vm.showTtktNumberError = false;
    /*
            Problem ticket number
        */
    function tktAdded(tkt) {
        tkt = tkt.toUpperCase()
        vm.showTtktNumberError = false;
        if (!vm.impPlan.platformId) {
            $("#probTktNo").tagit("removeTagByLabel", tkt);
            Toaster.sayError("Please choose company")
        }

        if (vm.impPlan.platformId == 2) {
            return;
        }
        var removeStatus = ["Closed", "Closed Unresolved", "Cancelled", "Close Requested"]

        APIFactory.getProblemTicket({ "ticketNumber": tkt }, function (response) {
            if (response.status && response.data.length > 0) {
                vm.showTtktNumberError = false
                var specifiedTicket = _.where(response.data, { "refNum": tkt })
                if (specifiedTicket.length > 0) {
                    if (removeStatus.indexOf($.trim(specifiedTicket[0].status)) >= 0) {
                        vm.showTtktNumberError = true;
                        vm.ticketErrorMessage = tkt + " is " + $.trim(specifiedTicket[0].status)
                        $("#probTktNo").tagit("removeTagByLabel", tkt);
                        return
                    }
                    vm.impPlan.sdmTktNum = $("#probTktNo").val()
                } else {
                    $("#probTktNo").tagit("removeTagByLabel", tkt);
                }
            } else {
                $("#probTktNo").tagit("removeTagByLabel", tkt);
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    function tktRemoved(tkt) {
        vm.showTtktNumberError = false;
    }

    /*
        Related Implementation Plans
    */
    function relatedPlanAdded(tkt) {
        tkt = tkt.toUpperCase();
        if (tkt == vm.responseData.id) {
            $("#relatedPlan").tagit("removeTagByLabel", tkt);
            Toaster.sayWarning("Plan ID entered and current plan ID are same")
            return
        }
        APIFactory.getPlan({ "id": tkt }, function (response) {
            if (response.status) {
                vm.imp.relatedPlans = $("#relatedPlan").val()
            } else {
                $("#relatedPlan").tagit("removeTagByLabel", tkt);
                Toaster.sayWarning("Plan not found")
            }
        })
    }

    vm.updateSystemDBCR = function () {
        APIFactory.getDbcrList({ "planIds": vm.impPlan.id }, function (response) {
            if (response.status) {
                if (response.data && response.data.length > 0) {
                    vm.systemDBCR = {}
                    for (key in vm.impPlan.systems) {
                        vm.systemDBCR[key] = []
                        _.filter(response.data, function (obj) {
                            if (key == obj.systemId.id) {
                                vm.systemDBCR[key].push(obj)
                            }
                        })
                    }
                } else {
                    vm.systemDBCR = {}
                    for (key in vm.impPlan.systems) {
                        vm.systemDBCR[key] = []
                    }
                }
            }
        })
    }

    $scope.disablePlanStatus = "Y";
    $scope.disableSystemStatus = "Y";
    var LoadDateTime = [];
    $scope.disableDevManager = "Y";

    var updateDataOnLoad = function (id, cbk) {
        try {
            APIFactory.getPlan({ "id": id }, function (plan_response) {
                if (plan_response.status) {
                    if (!vm.planAuthorized(plan_response)) {
                        $scope.planAuthorize = false;
                        return;
                    }
                    vm.loadLeadName();
                    vm.loadDevManagerName();
                    vm.getLoadAttendeeList();
                    vm.getQaFunctionalTesterList();
                    vm.blockedSystems = plan_response.data.blockedSystems
                    plan_response.data = plan_response.data.impPlan
                    $("#csrId").select2("trigger", "select", {
                        data: { id: plan_response.data.projectId.id, text: plan_response.data.projectId.projectNumber }
                    });
                    // $("#csrId").select2('data', { id: plan_response.data.projectId.id, text: plan_response.data.projectId.projectNumber });
                    // CSR & Problem Ticket Number - Vishnupriya Shekar
                    APIFactory.getSystemLoadByPlan({ "ids": id }, function (system_response) {
                        if (system_response.status) {
                            var systemLoadLists = []
                            vm.prodStatusLabel = []
                            _.each(system_response.data, function (systemLoadObj) {
                                systemLoadLists.push(systemLoadObj.systemLoad)
                            })
                            LoadDateTime = id;
                            // Disable for LC and TSD - Vishnupriya Shekar
                            $scope.calendarDisable = false;
                            _.each(systemLoadLists, function (pObj) {
                                LCTSD(pObj.systemId.id)
                                function LCTSD(sys) {
                                    APIFactory.getStatusListForPlanUpdate({ "planId": id }, function (response) {
                                        _.each(response.data, function (Obj) {
                                            if (Obj.systemid == sys && (Obj.loadsetstatus == "ACTIVATED" || Obj.loadsetstatus == "DEACTIVATED" || Obj.loadsetstatus == "FALLBACK_LOADED")) {
                                                vm.prodStatusLabel.push(Obj.systemid)
                                            }
                                        })
                                    })
                                }
                            })


                            plan_response.data.systemLoadList = system_response.data
                            vm.allSystemInformationWithAdditionalParameters = systemLoadLists
                            IPService.wrapOnLoad(plan_response.data, function (planObj) {
                                responseData = planObj;
                                vm.responseData = planObj;
                                getPlanStatus()
                                vm.impPlan = planObj;
                                cbk()
                                vm.planDefaults = angular.copy(vm.impPlan)
                                if (getUserData("userRole") == "DLCoreChangeTeam") {
                                    $scope.formDisabled = true
                                    $scope.disableLoadTime = "N"
                                }
                                if (implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("SUBMITTED")) {
                                    $scope.formDisabled = true
                                    $scope.disableSystemStatus = "N"
                                    $scope.loadTypeDisable = "N"
                                    $scope.disableDevManager = "N";
                                    $scope.csrNumDisable = "N"
                                    $scope.disableLoadTime = "N"
                                    if (getUserData("userRole") == "Lead" && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("PASSED_ACCEPTANCE_TESTING")) {
                                        $scope.disableSystemStatus = "Y"
                                        $scope.PblmTktNumDisable = "Y"
                                        $scope.csrNumDisable = "Y"
                                        $scope.loadTypeDisable = "Y"
                                        $scope.disableLoadTime = "Y"
                                    }

                                    if ((implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("DEV_MGR_APPROVED")) || (getUserData("userRole") == "DevManager" && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("APPROVED"))) {
                                        $scope.disableDevManager = "Y"
                                    }

                                    if (getUserData("userRole") == "DevManager" && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("DEV_MGR_APPROVED")) {
                                        $scope.disableSystemStatus = "Y"
                                        $scope.PblmTktNumDisable = "Y"
                                        $scope.csrNumDisable = "Y"
                                        $scope.loadTypeDisable = "Y"
                                        $scope.disableLoadTime = "Y"
                                    }

                                    if (getUserData("userRole") == "LoadsControl" && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("DEV_MGR_APPROVED")) {
                                        $scope.formDisabled = true
                                        $scope.PblmTktNumDisable = "Y"
                                        $scope.csrNumDisable = "Y"
                                        $scope.disableLCandTSD = function (arrayList, id) {
                                            for (i = 0; i < arrayList.length; i++) {
                                                if (arrayList[i] == id) {
                                                    $scope.disableLoadTime = "Y"
                                                    $scope.calendarDisable = true;
                                                    return true;
                                                } else {
                                                    $scope.disableLoadTime = "N"
                                                    $scope.calendarDisable = false;
                                                }
                                            }
                                        }
                                    }
                                    if (getUserData("userRole") == "TechnicalServiceDesk" && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("READY_FOR_PRODUCTION_DEPLOYMENT")) {
                                        $scope.csrNumDisable = "Y"
                                        $scope.disableLCandTSD = function (arrayList, id) {
                                            for (i = 0; i < arrayList.length; i++) {
                                                if (arrayList[i] == id) {
                                                    $scope.disableLoadTime = "Y"
                                                    $scope.calendarDisable = true;
                                                    return true;
                                                } else {
                                                    $scope.disableLoadTime = "N"
                                                    $scope.calendarDisable = false;
                                                }
                                            }
                                        }
                                    }
                                    if (vm.impPlan.planStatus === 'PARTIAL_REGRESSION_TESTING' || vm.impPlan.planStatus === 'PASSED_REGRESSION_TESTING' || vm.impPlan.planStatus === 'BYPASSED_REGRESSION_TESTING' || vm.impPlan.planStatus === 'DEPLOYED_IN_PRE_PRODUCTION' && getUserData("userRole") != "DLCoreChangeTeam") {
                                        $scope.disablePlanStatus = "N"
                                    }
                                }
                                if (vm.impPlan.planStatus == "BYPASSED_FUNCTIONAL_TESTING") {
                                    $scope.formDisabled = true
                                }
                                stockImplPlanEditObj = angular.copy(vm.impPlan);
                                for (sData in planObj.system) {
                                    $scope.selectedPlatform(planObj.platformId, sData, function (systemRes) { });
                                }
                                vm.updateSystemDBCR()
                            })
                        } else {
                            Toaster.sayError(system_response.errorMessage)
                        }
                    })

                } else {
                    Toaster.sayError(plan_response.errorMessage);
                }
            })
        } catch (err) { }
    }
    // updateDataOnLoad($stateParams.planId);

    $scope.showCalendarIcon = function () {
        if (getUserData('userRole') == 'Lead' && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf('PASSED_ACCEPTANCE_TESTING')) {
            return false
        }
        if (getUserData("userRole") == "DevManager" && implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("DEV_MGR_APPROVED")) {
            return false
        }
        if (getUserData("userRole") == "QA") {
            return false
        }
        if ($scope.calendarDisable) {
            return false;
        }
        return true;
    }

    var updatedataPromise = new Promise(function (resolve, reject) {
        updateDataOnLoad($stateParams.planId, function () {
            resolve(vm.impPlan)
        })

    })

    var LeadPromise = new Promise(function (resolve, reject) {
        //lead
        vm.loadLeadName(function (isDataReady) {
            isDataReady == true ? resolve(vm.createdByList) : reject("Lead")
        });
    })

    var devManagerPromise = new Promise(function (resolve, reject) {
        //devmanager
        vm.loadDevManagerName(function (isDataReady) {
            isDataReady == true ? resolve(vm.devManagerList) : reject("Manager")
        });
    })

    var loadAttendeePromise = new Promise(function (resolve, reject) {
        //load attendee
        vm.getLoadAttendeeList(function (isDataReady) {
            isDataReady == true ? resolve(vm.loadAttendeeList) : reject("Attendee")
        });
    })
    var qaFunctionalTesterPromise = new Promise(function (resolve, reject) {
        //load attendee
        vm.getQaFunctionalTesterList(function (isDataReady) {
            isDataReady == true ? resolve(vm.qaFunctionalTesterList) : reject("QAFunctional")
        });
    })


    Promise.all([updatedataPromise, LeadPromise, devManagerPromise, loadAttendeePromise, qaFunctionalTesterPromise]).then(function (values) {
        if (_.where(vm.createdByList, { "id": vm.impPlan.leadId }).length == 0) {
            $("#createdby").val("").trigger("change")
            Toaster.sayWarning("Pre selected Lead has been removed from LDAP. Please Contact Administrator")
        }
        if (_.where(vm.devManagerList, { "id": vm.impPlan.devManager }).length == 0) {
            $("#approver").val("").trigger("change")
            Toaster.sayWarning("Pre selected Dev Manager has been removed from LDAP. Please Contact Administrator")
        }
        $scope.receivedLoadAttendeeList = true
    }, function (values) {
        switch (values) {
            case "Lead":
                Toaster.sayWarning("Application Developer Lead List has not been received correctly")
                break;
            case "Manager":
                Toaster.sayWarning("Developer Manager List has not been received correctly")
                break;
            case "Attendee":
                Toaster.sayWarning("Load Attendee List has not been received correctly")
                break;
            case "QAFunctional":
                Toaster.sayWarning("QA Functional User List has not been received correctly")
                break;
        }
        return
    });

    vm.loadWindowCategories = []
    /*
        When load type is selected either Standard or Exception
    */
    vm.chooseLoadType = function (e) {
        if (vm.impPlan.planStatus !== 'ACTIVE' && (vm.impPlan.loadType == 'STANDARD')) {
            Toaster.sayWarning("Load type cannot be changed from Exception to Standard")
            vm.impPlan.loadType = 'EXCEPTION'
            return;
        }
        if (vm.impPlan.macroHeader) {
            macroHeader = vm.impPlan.macroHeader;
        }
        if ((getUserData("userRole") == "LoadsControl" || getUserData("userRole") == "TechnicalServiceDesk" || getUserData("userRole") == "Lead") && (vm.impPlan.loadType == "EXCEPTION" || vm.impPlan.loadType == "EMERGENCY") && vm.planDefaults.loadType == "STANDARD") {
            // alert("Caught")
            $mdDialog.show({
                controller: loadTypeChangeCommentCtrl,
                controllerAs: "ld",
                templateUrl: 'html/templates/loadTypeChangeComment.template.html',
                parent: angular.element(document.body),
                targetEvent: e,
                clickOutsideToClose: false,
                locals: {
                    // "id": planId
                }
            })
                .then(function (answer) {
                    // $scope.refresh()

                }, function () {

                });
            function loadTypeChangeCommentCtrl($scope) {
                var ld = this;
                ld.showMessageError = false
                $scope.proceedSaveComment = function () {
                    if (!ld.loadTypeChangeComment) {
                        ld.showMessageError = true
                        return
                    } else {
                        ld.showMessageError = false
                    }
                    vm.loadTypeChangeComment = ld.loadTypeChangeComment.replace(/["“”<>']+/g, '')
                    $mdDialog.hide();
                }
                $scope.cancel = function () {
                    $mdDialog.cancel();
                };
            }
        }
        try {
            if (vm.impPlan.loadType == 'STANDARD') {
                var toAdd = moment().add(3, 'days');
                vm.startDate = toAdd.format('MM/DD/YYYY');
            } else {
                vm.startDate = moment().format('MM/DD/YYYY')
            }
        } catch (err) { }
    }
    /*
        Show date calendar
    */
    vm.showDateCalendar = function (ev, id) {
        $mdDialog.show({
            controller: loadDateCtrl,
            templateUrl: 'html/templates/loadCalendar.template.html',
            controllerAs: 'ld',
            parent: angular.element(document.body),
            targetEvent: ev,
            locals: {
                systemInfo: id
            },
            clickOutsideToClose: true,
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
            .then(function (answer) {
                $scope.status = 'You said the information was "' + answer + '".';
            }, function () {
                $scope.status = 'You cancelled the dialog.';
            });

    }

    /*
        Select from calendar, By date - Calendar Init
    */
    vm.onDateChange = function (selectedDate, id, $mdDialog) {
        try {
            if (vm.impPlan.loadType == "STANDARD") {
                vm.impPlan.system[id].loadDateTime = null
                vm.loadTime[id] = []
                var selectedCDate = selectedDate.split("_").slice(-1)[0]
                if (implementationPlanStatus().indexOf(vm.impPlan.planStatus) >= implementationPlanStatus().indexOf("SUBMITTED") || vm.impPlan.macroHeader) {
                    populateLoadTime(vm.impPlan.system[id].loadCategoryId, selectedCDate, id, LoadDateTime)
                } else {
                    vm.impPlan.system[id].loadCategoryId = null
                }
                var date = moment(selectedCDate);
                var day = date.format("ddd");

                var sDateObj = moment(selectedCDate, "YYYY-MM-DD")
                vm.impPlan.system[id].loaddate = sDateObj.format(appSettings.dateFormat)

                vm.loadWindowCategories[id] = []
                var macroHeader = false;
                if (vm.impPlan.macroHeader) {
                    macroHeader = vm.impPlan.macroHeader;
                }
                APIFactory.getLoadCategoriesByDate({ "id": id, "date": selectedCDate }, function (response) {
                    if (response.status && response.data.length > 0) {
                        vm.loadWindowCategories[id] = response.data
                        if (macroHeader) {
                            var macroheadercategory = _.where(response.data, { name: "M" });

                            vm.loadWindowCategories[id] = macroheadercategory;
                            if (vm.loadWindowCategories[id].length <= 0 && vm.impPlan.macroHeader) {
                                // vm.loadWindowCategories[id] = [];
                                Toaster.sayWarning("load category 'M' is not available on this Date for Macro/Header Plan. Select a date for which 'M' category is available");
                                return;
                            }
                            if (vm.systemDetailsList.length > 0) {
                                vm.loadMacroHeaderCategorySystem(vm.loadWindowCategories[id], id);
                            }
                        } else {
                            vm.loadWindowCategories[id] = []
                            _.each(response.data, function (loadCat) {
                                if (loadCat.name !== 'M') {
                                    vm.loadWindowCategories[id].push(loadCat)
                                }
                            })
                        }

                    } else {
                        vm.loadWindowCategories[id] = []
                    }
                })
                $mdDialog.hide()
            } else {
                getLoadCategoryList(id, "Y", function (data) {
                    vm.loadWindowCategories[id] = data
                })
            }
        } catch (err) { }
    }

    /*
        Select from moment picker, by date
    */

    // Select from month picker , by selectable function

    vm.isSelectable = function (date, type, listId) {
        return type != 'day' || date.format(appSettings.dateFormat) == vm.exMomentstartdate[listId] || moment(date.format(appSettings.dateFormat)).diff(moment().format(appSettings.dateFormat)) >= 0;
    };

    vm.onDateSelect = function (newVal, oldVal, systemId) {
        // vm.impPlan.system[systemId].exceptionLoadtime = null
        // if (implementationPlanStatus().indexOf(vm.impPlan.planStatus) < implementationPlanStatus().indexOf("SUBMITTED")) {

        /*the below Logic removed since load category is getting removed when load date is changed system level*/
        // if (vm.impPlan.planStatus == "ACTIVE") {
        //     IPService.clearFieldsOnChange(vm, systemId, "exceptionType");
        // }
        var macroHeader = false;
        if (vm.impPlan.macroHeader) {
            macroHeader = vm.impPlan.macroHeader;
        }
        getLoadCategoryList(systemId, "Y", function (data) {
            vm.loadWindowCategories[systemId] = data
            if (macroHeader) {
                var mcategoryId = _.find(data, { name: "M" })
                vm.impPlan.system[systemId].loadCategoryId = mcategoryId.id;
            }

        })
        checkInfochanged(systemId);
    }


    vm.chooseExpLoadTime = function (systemId, loadDate, loadTime) {
        checkInfochanged(systemId);
        var fulldate = loadDate + " " + loadTime + ":00 " + moment(loadDate + " " + loadTime, appSettings.dateFormat + " " + appSettings.timeFormat).tz(getTimeZone()).format("ZZ");
        var selectDiffTime = moment().tz(getTimeZone()).diff(moment(fulldate, "MM-DD-YYYY HH:mm:ss ZZ"));
        if (loadDate) {
            if (selectDiffTime > 0) {
                vm.impPlan.system[systemId].exceptionLoadtime = "";
                Toaster.sayWarning("Choose Load Time after the Current Time")
                return;
            }
        } else {
            Toaster.sayWarning("Choose Load Date")
            return;
        }
    }

    vm.loadChanged = {}

    function checkInfochanged(systemId) {
        try {
            vm.loadChanged[systemId] = false
            if (stockImplPlanEditObj.system[systemId].loadDateTime) {
                var selectedDateTime = stockImplPlanEditObj.system[systemId].loadDateTime.split(" ");
            }

            if (vm.impPlan.loadType == "STANDARD") {
                if (vm.impPlan.system[systemId].loadCategoryId != stockImplPlanEditObj.system[systemId].loadCategoryId) {
                    vm.loadChanged[systemId] = true
                }
                if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].loadCategoryDate == selectedDateTime[0]) {
                    vm.loadChanged[systemId] = false
                }

                if (vm.impPlan.system[systemId].loadCategoryDate)
                    if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].loadCategoryDate != selectedDateTime[0]) {
                        vm.loadChanged[systemId] = true
                    }

                if (vm.impPlan.system[systemId].loadDateTime)
                    if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].loadCategoryDate == selectedDateTime[0] && vm.impPlan.system[systemId].loadDateTime != stockImplPlanEditObj.system[systemId].loadDateTime) {
                        vm.loadChanged[systemId] = true
                    }

                if (vm.impPlan.system[systemId].loadDateTime)
                    if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].loadCategoryDate == selectedDateTime[0] && vm.impPlan.system[systemId].loadDateTime == stockImplPlanEditObj.system[systemId].loadDateTime) {
                        vm.loadChanged[systemId] = false
                    }
            } else {
                if (vm.impPlan.system[systemId].loadCategoryId)
                    if (vm.impPlan.system[systemId].loadCategoryId != stockImplPlanEditObj.system[systemId].loadCategoryId) {
                        vm.loadChanged[systemId] = true
                    }
                if (vm.impPlan.system[systemId].exceptionLoaddate)
                    if (vm.impPlan.system[systemId].exceptionLoaddate != selectedDateTime[0]) {
                        vm.loadChanged[systemId] = true
                    }

                if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].exceptionLoaddate == selectedDateTime[0]) {
                    vm.loadChanged[systemId] = false
                }

                if (vm.impPlan.system[systemId].exceptionLoaddate)
                    if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].exceptionLoaddate != selectedDateTime[0]) {
                        vm.loadChanged[systemId] = true
                    }

                if (vm.impPlan.system[systemId].exceptionLoadtime)
                    if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].exceptionLoaddate == selectedDateTime[0] && vm.impPlan.system[systemId].exceptionLoadtime != selectedDateTime[1].slice(0, 5)) {
                        vm.loadChanged[systemId] = true
                    }

                if (vm.impPlan.system[systemId].exceptionLoadtime)
                    if (vm.impPlan.system[systemId].loadCategoryId == stockImplPlanEditObj.system[systemId].loadCategoryId && vm.impPlan.system[systemId].exceptionLoaddate == selectedDateTime[0] && vm.impPlan.system[systemId].exceptionLoadtime == selectedDateTime[1].slice(0, 5)) {
                        vm.loadChanged[systemId] = false
                    }
            }
        } catch (error) {

        }
    }


    /*
        Select from calendar, by date, - Controller
    */
    function loadDateCtrl($scope, $mdDialog, systemInfo) {
        var ld = this;
        var eventData = [];
        var freezeInfo = loadDateTimeFreezeDataByDate[systemInfo]
        var lStartDate, lEndDate, tStartDate, tEndDate;
        for (lF in freezeInfo) {
            var builtFrom = moment(freezeInfo[lF].from_date).tz(getTimeZone()).format(appSettings.apiDTFormat)
            tStartDate = builtFrom.split(" ")[0]
            lStartDate = moment(builtFrom).tz(getTimeZone())
            var builtTo = moment(freezeInfo[lF].to_date).tz(getTimeZone()).format(appSettings.apiDTFormat)
            tEndDate = builtTo.split(" ")[0]
            diffDays = moment(tEndDate).diff(tStartDate, "days")
            var enumDate = lStartDate
            for (i = 0; i < diffDays; i++) {
                eventData.push(moment(enumDate).format("YYYY-MM-DD"))
                enumDate = moment(enumDate).add('days', 1).format("YYYY-MM-DD");
            }
            eventData.push(moment(enumDate).format("YYYY-MM-DD"))
        }
        var daysToSkip = 0;
        if (vm.impPlan.loadType === "STANDARD") {
            daysToSkip = 3;
        }
        if (vm.impPlan.macroHeader) {
            daysToSkip = 0;
        }
        var forMonth = moment().month() + 1,
            forYear = moment().year();
        var lStartDate, lEndDate, lStartTime, lEndTime;
        var cur_lcd = vm.impPlan.system[systemInfo].loaddate
        var m_cur_lcd = moment(vm.impPlan.system[systemInfo].loaddate)
        var current_date = (typeof cur_lcd == 'undefined' || !cur_lcd) ? null : new Date(m_cur_lcd.year(), m_cur_lcd.month(), m_cur_lcd.date(), 0, 0, 0, 0);
        $timeout(function () {
            $("#loadWindowCalendar").zabuto_calendar({
                language: "en",
                cell_border: true,
                today: true,
                year: moment().year(),
                month: moment().month() + 1,
                show_previous: false,
                ajax: {
                    url: apiBase + "/loadsControl/getLoadFreezeDateByMonth?id=" + systemInfo,
                    modal: false
                },
                skip_days: daysToSkip,
                weekstartson: 0,
                curr_date: current_date,
                freeze_dates: vm.impPlan.macroHeader ? "" : eventData,
                chooseDate: vm.impPlan.macroHeader ? false : true,
                select_date: cur_lcd,
                action: function () { vm.onDateChange(this.id, systemInfo, $mdDialog); },
                action_nav: function () {
                    var nav = $("#" + this.id).data("navigation");
                    var to = $("#" + this.id).data("to");
                    forMonth = to.month
                    forYear = to.year
                }
            });
        }, 100)
    }
    var systemId;
    /*
        Select from calendar, By category - Calendar Init
    */
    vm.showLoadCalendar = function (ev, systemid, dateLoad) {
        try {
            systemId = systemid
            $mdDialog.show({
                controller: loadWindowCtrl,
                templateUrl: 'html/templates/loadCalendar.template.html',
                controllerAs: 'lc',
                parent: angular.element(document.body),
                targetEvent: ev,
                locals: {
                    systemInfo: systemid,
                },
                clickOutsideToClose: true,
                fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
            })
                .then(function (answer) {
                    $scope.status = 'You said the information was "' + answer + '".';
                }, function () {
                    $scope.status = 'You cancelled the dialog.';
                });
        } catch (err) { }


    }

    /*
        Select from calendar, By category - Controller
    */
    function loadWindowCtrl($scope, $mdDialog, systemInfo) {
        try {
            var lc = this;
            var eventData = [];
            var freezeInfo = loadDateTimeFreezeData[systemInfo]
            var lStartDate, lEndDate, tStartDate, tEndDate;
            for (lF in freezeInfo) {
                // lStartDate = moment(freezeInfo[lF].fromDate).tz(getTimeZone())
                // lEndDate = moment(freezeInfo[lF].toDate).tz(getTimeZone())
                // diffDays = moment(lEndDate).diff(lStartDate, "days")
                var builtFrom = moment(freezeInfo[lF].fromDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
                tStartDate = builtFrom.split(" ")[0]
                lStartDate = moment(builtFrom).tz(getTimeZone())
                var builtTo = moment(freezeInfo[lF].toDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
                tEndDate = builtTo.split(" ")[0]
                diffDays = moment(tEndDate).diff(tStartDate, "days")
                var enumDate = lStartDate
                for (i = 0; i < diffDays; i++) {
                    eventData.push(moment(enumDate).format("YYYY-MM-DD"))
                    enumDate = moment(enumDate).add('days', 1).format("YYYY-MM-DD");
                }
                eventData.push(moment(enumDate).format("YYYY-MM-DD"))
            }
            var daysToSkip = 0;
            if (vm.impPlan.loadType === "STANDARD") {
                daysToSkip = 3;
            }
            if (vm.impPlan.macroHeader) {
                daysToSkip = 0;
            }
            var cur_lcd = vm.impPlan.system[systemInfo].loadCategoryDate
            var m_cur_lcd = moment(vm.impPlan.system[systemInfo].loadCategoryDate)
            var current_date = (typeof cur_lcd == 'undefined' || !cur_lcd) ? null : new Date(m_cur_lcd.year(), m_cur_lcd.month(), m_cur_lcd.date(), 0, 0, 0, 0);
            setTimeout(function () {
                //format 2017-06-19
                $("#loadWindowCalendar").zabuto_calendar({
                    language: "en",
                    cell_border: true,
                    ajax: {
                        url: apiBase + "/loadsControl/getLoadWindowDateByLoadCategory?id=" + vm.impPlan.system[systemInfo].loadCategoryId,
                        modal: false
                    },
                    year: moment().year(),
                    month: moment().month() + 1,
                    show_previous: false,
                    skip_days: daysToSkip,
                    weekstartson: 0,
                    curr_date: current_date,
                    freeze_dates: eventData,
                    chooseDate: false,
                    select_date: cur_lcd,
                    action: function () { updateSelectedDate(moment(this.id.split("_").slice(-1)[0], "YYYY-MM-DD").format(appSettings.dateFormat), systemInfo, true); },
                    action_nav: function () {
                        var nav = $("#" + this.id).data("navigation");
                        var to = $("#" + this.id).data("to");
                        forMonth = to.month
                        forYear = to.year
                    }
                });
            }, 100)

        } catch (err) {
            console.log(err)
        }

    }
    var updateSelectedDate = function (date, systemInfo, callFromDialog, loadType) {






        var selectedCDate = date
        var dateChangeValue;
        if (callFromDialog) {
            vm.impPlan.system[systemInfo].loadCategoryDate = moment(selectedCDate, "MM-DD-YYYY").format(appSettings.dateFormat)
            vm.impPlan.system[systemInfo].loadDateTime = null
            dateChangeValue = vm.impPlan.system[systemInfo].loadCategoryDate
        }
        vm.loadTime[systemInfo] = []
        var date_param;
        checkInfochanged(systemInfo);
        if (vm.impPlan.system[systemInfo].loadWindowType == "1") {
            date_param = moment(vm.impPlan.system[systemInfo].loaddate, appSettings.dateFormat).format("YYYY-MM-DD")
        } else {
            date_param = moment(vm.impPlan.system[systemInfo].loadCategoryDate, appSettings.dateFormat).format("YYYY-MM-DD")
        }
        if (date_param != "Invalid date") {
            populateLoadTime(vm.impPlan.system[systemInfo].loadCategoryId, date_param, systemInfo, LoadDateTime, callFromDialog, loadType, dateChangeValue)
        }

        // vm.impPlan.system[systemId].loadChanged = false
        // var selectedDateTime = stockImplPlanEditObj.system[systemId].loadDateTime.split(" ");
        // if(vm.impPlan.system[systemId].loadCategoryDate != selectedDateTime[0]){
        //     vm.impPlan.system[systemId].loadChanged = true
        // }
        if (callFromDialog) {
            $mdDialog.hide()
        }
    }

    vm.selectedDateTime1;
    vm.selectedDateTime = [];;
    vm.dropdownDateTimeList = [];
    vm.format_data_conv;
    vm.flag = [];
    var BreakException = {};
    vm.flagStatus = false;
    var loadDateTimeList = [];
    function populateLoadTime(loadCategoryId, date_param, systemInfo, data, dialog, loadType, dateChangeValue) {
        APIFactory.getLoadWindowByDay({ "id": loadCategoryId, "date": moment(date_param, "YYYY-MM-DD").format("MM-DD-YYYY"), "day": moment(date_param, "YYYY-MM-DD").format("ddd") }, function (response) {
            if (response.status && response.data.length > 0) {
                vm.loadTime[systemInfo] = response.data
                loadDateTimeList.push(response.data)
                _.each(vm.loadTime[systemInfo], function (elem) {
                    var formatted_ts = Access.formatUIDate(moment(elem.timeSlot, appSettings.apiDTFormat)).split(" ")
                    elem.timeSlot = moment(date_param, "YYYY-MM-DD").format("MM-DD-YYYY") + " " + formatted_ts[1] + " " + formatted_ts[2];
                    var popTimeSlot = (elem.timeSlot).split(" ")[1].slice(0, -3)
                    vm.dropdownDateTimeList[systemInfo] = popTimeSlot
                    vm.flag.push(vm.dropdownDateTimeList[systemInfo])
                    // if(dialog) {
                    // compareDateTime(vm.dropdownDateTimeList[systemInfo], data, systemInfo)
                    // }
                })
                if (dialog) {
                    compareDateTime(_.uniq(vm.flag), data, systemInfo, dateChangeValue)
                }

            } else {
                vm.loadTime[systemInfo] = []
            }
        })

    }

    function compareDateTime(list, data, systemInfo, dateChangeValue) {
        var arrayList = [];
        APIFactory.getSystemLoadByPlanAndSystem({ "pPlanId": data, "systemId": systemInfo, "loadDate": dateChangeValue }, function (response) {
            if (response.status) {
                vm.systemPlanDateTime = response.data;
                if (systemInfo == vm.systemPlanDateTime.systemId.id) {
                    vm.format_data = moment(response.metaData).tz(getTimeZone()).format(appSettings.uiDTFormat)
                    var timeZone = vm.format_data.substr(vm.format_data.length - 3);
                    vm.format_data = response.metaData.slice(0, -3) + timeZone;
                    vm.format_data_conv = (vm.format_data).split(" ")[1].slice(0, -3)
                    vm.selectedDateTime1 = includes(list, vm.format_data_conv)
                    if (vm.selectedDateTime1 == true) {
                        vm.selectedDateTime[systemInfo] = vm.format_data_conv;
                        vm.impPlan.system[systemInfo].loadDateTime = vm.format_data;
                    } else {
                        vm.selectedDateTime[systemInfo] = null;
                        vm.impPlan.system[systemInfo].loadDateTime = null;
                    }
                }
            }
        })
    }

    function getLoadTypes() {
        APIFactory.getLoadTypeList(function (response) {
            if (response.status) {
                vm.loadTypeList = response.data
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    /*
        When load type changes, build corresponding fields
    */
    $scope.$watch("vm.impPlan.loadType", function () {
        detectChange()
    })
    var detectChange = function (sId) {
        if (vm.impPlan.loadType == "STANDARD") {
            vm.showLoadWindow = true
            if (!sId) {
                for (systemId in vm.impPlan.systems) {
                    if (vm.impPlan.system[systemId]) {
                        vm.impPlan.system[systemId].loadWindowType = null
                    }
                    // vm.impPlan.system[systemId].loadWindowType = getUserPrefs("loadWindowType")
                    IPService.alignStructure(vm, systemId, "exceptionType", true)
                }
            }
        } else if (vm.impPlan.loadType == "EXCEPTION" || vm.impPlan.loadType == "EMERGENCY") {
            // vm.loadWindowCategories = [];
            vm.showLoadWindow = false
            if (sId) {
                vm.selectForEmergency(sId)
                IPService.alignStructure(vm, sId, "exceptionType")
            } else {
                for (systemId in vm.impPlan.system) {
                    vm.impPlan.system[systemId].exceptionLoaddate = vm.impPlan.system[systemId].loadCategoryDate ? vm.impPlan.system[systemId].loadCategoryDate : vm.impPlan.system[systemId].loaddate
                    vm.impPlan.system[systemId].exceptionLoadtime = vm.impPlan.system[systemId].loadDateTime ? vm.impPlan.system[systemId].loadDateTime.split(" ")[1] : ""
                    vm.selectForEmergency(systemId)
                    IPService.alignStructure(vm, systemId, "exceptionType")
                }
            }
        }

    }

    vm.putLevels = []
    $scope.populatePutLevel = function (systemId, cbk) {
        /*
            Load zTPF Levels for the selected system
        */
        APIFactory.getPutLevelBySystem({ "ids": systemId }, function (response) {
            if (response.status) {
                vm.putLevels[systemId] = response.data
                angular.forEach(vm.putLevels[systemId], function (putObj) {
                    if (putObj.id == putObj.systemId.defalutPutLevel) {
                        vm.systemDefaults[systemId].defaultPutForSystem = putObj.putLevel
                        if (cbk) {
                            cbk(putObj.id)
                        }
                    }
                })
            } else {
                vm.putLevels[systemId] = []
                cbk("")
            }
        })
    }

    /*
        Listen to update and submit
    */
    vm.forSocketPlanObj = {}
    vm.forSocketRawObj = {}
    WSService.initPlanUpdate(function (response) {
        // Update Plan Block
        if (vm.impPlan && vm.impPlan.id == response.metaData) {
            vm.checkSubmitPlanStatus()
            if (response.status) {
                if (response.data && response.data != null && response.data.length > 0) {
                    if (typeof eventListerner != 'undefined' && eventListerner != null) {
                        var displayString;
                        if (response.data.indexOf("allowDateTime") >= 0) {
                            displayString = response.data.slice(("allowDateTime").length)
                        }
                        else {
                            displayString = response.data
                        }
                        var confirm = $mdDialog.confirm()
                            .title('Warning')
                            .textContent(displayString)
                            .ariaLabel('Update Warning')
                            .ok('Yes')
                            .targetEvent(eventListerner)
                            .cancel('Cancel');
                        $mdDialog.show(confirm).then(function () {
                            var paramObj = {}
                            if (response.data.indexOf("allowDateTime") >= 0) {
                                paramObj = {
                                    "warningFlag": true,
                                    "allowDateChange": true
                                }
                            }
                            else {
                                paramObj = {
                                    "warningFlag": false,
                                    "allowDateChange": true
                                }
                            }

                            if (Object.keys(vm.forSocketPlanObj).length > 0) {
                                vm.updateButtonDisabled = true;
                                vm.loader.update = true;
                                APIFactory.updatePlan(paramObj, vm.forSocketPlanObj, function (response) {
                                    vm.loader.update = false;
                                })
                            } else {
                                Toaster.sayError("Update failed")
                            }
                            $mdDialog.hide()
                        }, function () { });
                    } else {
                        Toaster.sayError("Update failed due to refresh")
                    }
                    $rootScope.saveformData()
                } else {
                    Toaster.saySuccess("Implementation Plan " + response.metaData + " successfully updated");
                    if (Object.keys(vm.forSocketRawObj).length > 0) {
                        for (sys in vm.forSocketRawObj.system) {
                            vm.disableLoadCategorybyMacroHeader[sys] = false
                        }
                        if (vm.forSocketPlanObj.planStatus === 'PASSED_ACCEPTANCE_TESTING') {
                            if ($rootScope.previousState != undefined) {
                                $state.go($rootScope.previousState)
                            } else {
                                $state.go("app.impPlan")
                            }
                        }
                    } else {
                        $state.go("app.updateImpPlan", {}, {
                            reload: "app.updateImpPlan"
                        });
                    }
                    // Rmoved due to redirection of page issue
                    /*if(getUserData('userRole') == "Lead"){
                        $(".okBtn").click(function(){
                            $state.go("app.impPlan")
                        })
                    } */
                    $rootScope.saveformData()
                }
            } else {
                Toaster.sayError(response.errorMessage);
                $rootScope.saveformData()
            }
        }

    })



    /*
        Submit Implementation plan form
    */
    vm.submitImpPlanForm = function (data, ev) {
        eventListerner = ev;
        try {
            // if(typeof vm.impPlan.projectId.id != 'undefined' && vm.impPlan.projectId.id.length > 0){
            //     vm.impPlan.projectId.id = vm.impPlan.projectId.id.toString();
            // }
            // if(vm.impPlan.projectId.id.length == 0) {
            //     vm.impPlan.projectId = null
            // }
            if (vm.impPlan.platformId == '2' && (vm.impPlan.macroHeader == undefined || vm.impPlan.macroHeader == false)) {
                vm.impPlan.rfcFlag = true;
            }
            vm.impPlan.projectId = {}
            if ($("#csrId").val() != null) {
                vm.impPlan.projectId.id = $("#csrId").val().toString()
            } else {
                vm.impPlan.projectId = null
            }

            var rData = angular.copy(data);

            if (vm.impPlan.leadId.length <= 0) {
                Toaster.sayWarning("Please Provide Application Developer Lead");
                return;
            }
            if (!rData.platformId) {
                Toaster.sayWarning("Company is must to create Implementation Plan Id");
                return;
            }

            for (systemId in rData.system) {

                var lObj = _.where(vm.loadWindowCategories[systemId], { id: parseInt(vm.impPlan.system[systemId].loadCategoryId) })[0];
                if (lObj !== undefined && lObj.name === 'R' && (rData.system[systemId].loadInstruction === null || rData.system[systemId].loadInstruction === '')) {
                    Toaster.sayWarning("For Load category " + lObj.name + " Special  instructions for TSD is a mandatory field");
                    return;
                }
            }
            var sysList = angular.copy(vm.systemDetailsList)

            for (obj in sysList) {
                sysList[obj].id = parseInt(sysList[obj].id)
            }
            // if (fImplementationPlanValidate.validateImpPlanMandatoryFields(rData)) {
            if (fImplementationPlanValidate.validateIPFields(rData, sysList)) {
                for (sT in rData.system) {
                    try {
                        rData.system[sT].loadCategoryId = getCategoryIdFromName(sT, rData.system[sT].loadCategoryId)
                    } catch (err) { }
                    var seletedLoadAttendee = _.where(vm.loadAttendeeList, { "id": rData.system[sT].loadAttendee })[0];
                    if (_.isArray(rData.system[sT].loadAttendee)) {
                        var seletedLoadAttendee = _.where(vm.loadAttendeeList, { "id": rData.system[sT].loadAttendee[0] })[0];
                    } else {
                        var seletedLoadAttendee = _.where(vm.loadAttendeeList, { "id": rData.system[sT].loadAttendee })[0];
                    }
                    if (Array.isArray(rData.system[sT].qaFunctionalTesters)) {
                        rData.system[sT].qaFunctionalTesters = rData.system[sT].qaFunctionalTesters ? (rData.system[sT].qaFunctionalTesters.length > 1 ? rData.system[sT].qaFunctionalTesters.join(",") : rData.system[sT].qaFunctionalTesters[0]) : ""
                    }
                    if (seletedLoadAttendee !== undefined && rData.system[sT].loadAttendee !== null && rData.system[sT].loadAttendee !== '' && rData.system[sT].loadAttendee.length > 0) {
                        rData.system[sT].loadAttendee = seletedLoadAttendee.displayName;
                        rData.system[sT].loadAttendeeId = seletedLoadAttendee.id;
                    } else {
                        rData.system[sT].loadAttendee = "";
                        rData.system[sT].loadAttendeeId = "";
                    }
                    delete rData.system[sT].tempLoadDateTime
                }
                for (ds in rData.system) {
                    if (vm.impPlan.loadType == 'STANDARD') {
                        rData.system[ds].loadDateTime = Access.formatAPIDate(rData.system[ds].loadDateTime)
                    } else {
                        rData.system[ds].loadDateTime = Access.formatAPIDate(rData.system[ds].exceptionLoaddate + " " + $.trim(rData.system[ds].exceptionLoadtime) + ":00 " + moment(rData.system[ds].exceptionLoaddate + " " + $.trim(rData.system[ds].exceptionLoadtime)).tz(getTimeZone()).format("z"))
                    }


                    if (vm.impPlan.loadType == 'STANDARD') {
                        var sysLoadDate = [];
                        _.each(loadDateTimeList, function (loadDate) {
                            _.each(loadDate, function (loadDateDetails) {
                                sysLoadDate.push(Access.formatAPIDate(loadDateDetails.timeSlot));

                            })
                        })

                        var flag = includes(sysLoadDate, Access.formatAPIDate(rData.system[ds].loadDateTime))
                        if ((Object.keys(sysList).length > 0 && Object.keys(sysList).length === Object.keys(rData.system).length) && !flag) {
                            Toaster.sayWarning("Provide Load Time")
                            return;

                        }
                    }
                    if (rData.system[ds].putIndicator && !rData.system[ds].putLevelId) {
                        Toaster.sayWarning("Choose zTPF Level")
                        return;
                    }
                    if (!rData.system[ds].putIndicator && rData.system[ds].putIndicator != undefined) {
                        rData.system[ds].putLevelId = null
                    }
                    if ($rootScope.deltaUser && vm.impPlan.platformId == '2' && (vm.impPlan.macroHeader == undefined || vm.impPlan.macroHeader == false)) {
                        if (!vm.impPlan.system[ds].loadAttendeeContact) {
                            Toaster.sayWarning("Please provide Load attendee phone number");
                            return;
                        }
                    }
                    if (rData.system[ds].putLevelId && rData.system[ds].putIndicator) {
                        if (moment(rData.system[ds].loadDateTime, appSettings.uiDTFormat).diff(moment(Access.formatAPIDate(_.where(vm.putLevels[ds], { "id": parseInt(rData.system[ds].putLevelId) })[0].putDateTime), appSettings.uiDTFormat)) < 0) {
                            Toaster.sayWarning("Load date should be after the selected put date")
                            return;
                        }
                    } else {
                        angular.forEach(vm.putLevels[ds], function (putObj) {
                            if (putObj.id == putObj.systemId.defalutPutLevel) {
                                rData.system[ds].putLevelId = putObj.id
                            }
                        })
                    }
                    rData.system[ds] = _.omit(rData.system[ds], ["putIndicator", "putdate"]);
                }
                // rData.sdmTktNum = _.pluck(rData.sdmTktNum, "text")
                rData.sdmTktNum = $("#probTktNo").val()
                rData.relatedPlans = $("#relatedPlan").val().toUpperCase()
                stockImplPlanEditObj = angular.copy(rData);

                if (rData.maintanceFlag === 'Yes' && (rData.sdmTktNum === null || rData.sdmTktNum === "")) {
                    Toaster.sayWarning("Maintenance DTN requires problem ticket number. Please provide problem ticket number.");
                    return;
                }
                // rData.projectId = {}
                // rData.projectId.id = $("#csrId").val()
                if (typeof vm.impPlan.leadId != 'undefined' && vm.impPlan.leadId.length > 0) {
                    vm.impPlan.leadId = vm.impPlan.leadId.toString();
                    rData.leadId = rData.leadId.toString();
                }
                rData.leadName = _.where(vm.createdByList, { "id": rData.leadId })[0].displayName

                if (typeof vm.impPlan.devManager != 'undefined' && vm.impPlan.devManager.length > 0 && typeof rData.devManager != 'undefined' && rData.devManager.length > 0) {
                    vm.impPlan.devManager = vm.impPlan.devManager.toString();
                    rData.devManager = rData.devManager.toString();
                }
                rData.devManagerName = _.where(vm.devManagerList, { "id": rData.devManager })[0].displayName
                for (key in rData.system) {
                    rData.system[key] = _.omit(rData.system[key], ["exceptionLoaddate", "exceptionLoadtime", "loadWindowType", "loadtime", "loadCategoryDate", "loaddate"]);
                }
                // delete rData.leadName;
                var lPlan = IPService.wrapperPlanSave(rData, vm)
                vm.updateButtonDisabled = true;
                vm.loader.update = true;
                vm.forSocketPlanObj = lPlan
                vm.forSocketRawObj = rData
                if (vm.impPlan.loadType == 'STANDARD') {
                    vm.loadTypeChangeComment = ""
                }
                APIFactory.updatePlan({ "warningFlag": true, "allowDateChange": false, "loadTypeChangeComment": vm.loadTypeChangeComment }, IPService.wrapperPlanSave(rData, vm), function (response) {
                    vm.loader.update = false;
                    if (response.status && response.data == null) {
                        Toaster.saySuccess("Implementation Plan " + response.metaData + " successfully Updated ");
                        if (getUserData("userRole") == "DLCoreChangeTeam") {
                            setTimeout(function () {
                                $state.go("app.rfcApprovedPlans")
                            }, 2000)
                        }
                    } else if (response.status && response.data != null) {

                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })

            }
        } catch (err) {
            console.log(err)
        }

    };

    function includes(container, value) {
        var returnValue = false;
        var pos = container.indexOf(value);
        if (pos >= 0) {
            returnValue = true;
        }
        return returnValue;
    }

    // Plan Authorized user Access

    vm.planAuthorized = function (planDetails) {
        var currentRole = getUserData("user");
        return (currentRole.id == planDetails.data.impPlan.leadId || $rootScope.currentActiveUserId == planDetails.data.impPlan.leadId || getUserData("userRole") == "LoadsControl" || getUserData("userRole") == "QA" || getUserData("userRole") == "TechnicalServiceDesk" || getUserData("userRole") == "DevManager" || getUserData("userRole") == "DLCoreChangeTeam");
    }





    /*
        Load projects
    */
    // vm.impPlan.projectId = {}
    $scope.loadProjects = function () {

        var header;
        if (getCookie("SMSESSION") == "" || getUserData("token") != null) {
            header = {
                "Authorization": getUserData("token")
            }
        }
        if (getUserData("token") == null) {
            header = appSession
        }
        APIFactory.getProjectListUrl(function (projUrl) {
            $("#csrId").select2({
                maximumSelectionLength: 1,
                ajax: {
                    url: apiBase + projUrl,
                    dataType: 'json',
                    type: "GET",
                    headers: header,
                    data: function (params) {
                        return {
                            "filter": params.term,
                            "platform": _.findWhere(vm.pList, { id: parseInt(vm.impPlan.platformId) }).name
                        };
                    },
                    processResults: function (response) {
                        angular.forEach(response.data, function (pObj) {
                            pObj.text = pObj.projectNumber
                        })
                        vm.projectData = response.data
                        return {
                            results: response.data
                        };
                    }
                }
            })
            $('#csrId').on('change', function (evt) {
                if (!$("#csrId").select2('val') || $("#csrId").select2('val') == null) {
                    return
                }
                if (!vm.projectData) {
                    return;
                }
                var selectedProject = _.where(vm.projectData, { id: parseInt($("#csrId").select2('val')) })
                vm.impPlan.projectName = selectedProject[0].projectName
                vm.impPlan.lobName = selectedProject[0].lineOfBusiness
                vm.impPlan.projectManagerId = selectedProject[0].managerId
                vm.impPlan.projectManagerName = selectedProject[0].managerName
                vm.impPlan.isDeltaFlag = selectedProject[0].isDelta ? selectedProject[0].isDelta : false
                vm.impPlan.projectSponsorId = selectedProject[0].sponsorId
                vm.impPlan.maintanceFlag = selectedProject[0].maintanceFlag
                $scope.$digest()
            });
            setTimeout(function () {
                $(".select2-selection.select2-selection--single").css({
                    "border": "1px solid #ccc",
                    "border-radius": "0px",
                    "background": "none",
                    "height": "38px",
                    "padding": "10px"
                })
                $("#select2-csrId-container").css({
                    "padding": "0px"
                })
                $(".select2-selection__arrow").css({
                    "top": "6px"
                })
            }, 1000)
        })
    }

    vm.checkSubmitPlanStatus = function () {
        APIFactory.isSubmitReady({ "planId": $stateParams.planId }, function (response) {
            if (response.status) {
                if (response.metaData == "INPROGRESS") {
                    vm.updateButtonDisabled = true;
                    vm.loader.update = true;
                } else {
                    vm.updateButtonDisabled = false;
                    vm.loader.update = false;
                }
            } else {

                if (response.metaData == "INPROGRESS") {
                    // Update Button
                    vm.updateButtonDisabled = true;
                    vm.loader.update = true;
                } else {
                    // Update Button
                    vm.updateButtonDisabled = false;
                    vm.loader.update = false;
                }
            }
        })
    }

    $(document).ready(function () {
        // initSingleSelect2("#csrId")
        // initSingleSelect2("#approver")
        $(".select2.select2-container.select2-container--default").addClass('form-control')
        $(".select2.select2-container.select2-container--default").css('width', '100%')
        $(".select2-selection.select2-selection--single").addClass('select2Enhance')



    });

    $scope.goBack = function () {
        if ($rootScope.previousState != undefined && $rootScope.currentState != $rootScope.previousState) {
            $state.go($rootScope.previousState)
        } else {
            $state.go('app.impPlan')
        }
    }

    /*
        Calls triggered on page load
    */
    vm.checkSubmitPlanStatus()
    var callsTriggeredOnLoad = [loadPlatforms(), getLoadTypes(), $scope.loadProjects()]
}).directive("disableOption", function () {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {

            var status = implementationPlanStatus()
            var positionOfCurrentStatus = status.indexOf(attrs.disableOption),
                nextStatus = positionOfCurrentStatus + 1,
                indexOf = 0;

            if (attrs.actual == "PARTIAL_REGRESSION_TESTING") {
                indexOf = 4
            }
            if (attrs.actual == "PASSED_REGRESSION_TESTING") {
                indexOf = 3
            }
            if (attrs.actual == "BYPASSED_REGRESSION_TESTING") {
                indexOf = 2
            }
            if (attrs.actual == "DEPLOYED_IN_PRE_PRODUCTION") {
                indexOf = 1
            }
            if (status[positionOfCurrentStatus - indexOf] == attrs.actual) {
                return
            }
            if (status[positionOfCurrentStatus] == attrs.actual) {

            } else {
                $(element).attr("disabled", "disabled")
                $(element).addClass("disabled")
            }
        }
    }
})
