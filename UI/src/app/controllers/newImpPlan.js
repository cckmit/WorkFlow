dashboard.controller("newImpPlanCtrl", function ($rootScope, $scope, $state, $location, $window, $timeout, appSettings,
    Toaster, $http, $mdDialog, fImplementationPlanValidate, apiService, APIFactory, WFLogger, freezeService, IPService, Access, Paginate) {

    var vm = this;
    var appInfo = JSON.parse(localStorage.getItem("appInfo"))
    var apiBase = appSettings.apiBase;
    if (appInfo.isSSOApp) {
        apiBase = appSettings.apiSSOBase;
    }
    $rootScope.titleHeading = $state.current.data.pageTitle
    $rootScope.prevSaveButton = true;
    Paginate.refreshScrolling();
    vm.leadId = $rootScope.home_menu.id;
    vm.leadName = $rootScope.home_menu.displayName;
    vm.showSystem = false;
    vm.rflag = false;
    vm.pList = ""
    vm.systemDefaults = []
    vm.exParam = {}
    vm.loader = { save: false };
    vm.impPlan = {
        auxtype: false,
        macroHeader: false,
        rfcFlag: false
    }
    $scope.timeZone = Access.refactorTimeZone(moment().tz(getTimeZone()).format("z"));

    /*
        Init Validation factory
    */
    $scope.fImplementationPlanValidate = fImplementationPlanValidate

    $scope.cancel_click = function () {
        if ($rootScope.previousState != undefined && $rootScope.currentState != $rootScope.previousState) {
            $state.go($rootScope.previousState)
        } else {
            $state.go("app.impPlan")
        }
    }

    /*
        Load Platform List
    */
    var loadPlatforms = function () {
        try {
            APIFactory.getPlatformList({}, function (response) {
                if (response.status) {
                    vm.pList = response.data
                    if (vm.pList.length == 1) {
                        vm.impPlan.platformId = vm.pList[0].id + "_" + vm.pList[0].name
                        $scope.selectedPlatform(vm.pList[0].id)
                    }
                }
            })
        } catch (err) { }
    }

    vm.selectedSystem = "Select System"
    vm.systemsList = ""

    /*
        Load Systems for the selected platform
    */
    $scope.selectedPlatform = function (pId) {
        try {
            vm.systemDetailsList = []
            var pName = _.where(vm.pList, { platformId: pId });
            if (pId == '1') {
                vm.impPlan.rfcFlag = false;
            }
            if ($rootScope.deltaUser && pId == '2' && (vm.impPlan.macroHeader == undefined || vm.impPlan.macroHeader == false)) {
                vm.impPlan.rfcFlag = true;
            } else {
                vm.impPlan.rfcFlag = false;
            }
            APIFactory.getSystemByPlatform({ "ids": pId }, function (response) {
                vm.impPlan.systems = {}
                vm.impPlan.system = {}
                vm.impPlan.loadType = "STANDARD"
                vm.systemsList = _.sortBy(response.data, "name")
                if (pId == '2') {
                    vm.systemsList.push({ name: "PSS" })
                }
                vm.showSystem = true;
            })

        } catch (err) { }
    }

    vm.showSystemDetails = false;
    vm.systemDetailsList = [];
    /*
        When system is selected, populate the corresponding system related fields
        Test Commit
    */


    $scope.rfcApplicable = function (status) {
        if (vm.impPlan.rfcApplicable) {
            rfcApplicable = vm.impPlan.rfcApplicable
        }
    }


    $scope.selectSystems = function (systemId, name) {
        if (!vm.impPlan.system) {
            vm.impPlan.system = {}
        }
        if (!vm.impPlan.system[systemId]) {
            vm.impPlan.system[systemId] = {}
        }
        vm.impPlan.system[systemId].loadWindowType = 2;
        if (typeof systemId != "undefined") {
            // vm.impPlan.system[systemId].loadWindowType = getUserPrefs("loadWindowType") ? getUserPrefs("loadWindowType") : 2;
            // if (vm.impPlan.system[systemId].loadWindowType == 1) {
            //     // vm.selectByDate(systemId, name)
            //     vm.selectByCategory(systemId, name)
            // } else {
            //     vm.selectByCategory(systemId, name)
            // }
            vm.selectByCategory(systemId, name)
            vm.impPlan.system[systemId].loadWindowType = 2;
            vm.impPlan.system[systemId].preload = "No";
            vm.impPlan.system[systemId].iplRequired = "No";
        }
        try {
            var subSystemsObj = {
                "PSS": ["AIR", "RES"]
            }

            if (name in subSystemsObj) {

                var keyId = _.where(vm.systemsList, { "name": name })[0].id
                for (st in subSystemsObj[name]) {
                    var valueId = _.where(vm.systemsList, { name: subSystemsObj[name][st] })[0].id
                    if (!vm.impPlan.systems[keyId]) {
                        vm.impPlan.systems[valueId] = false;
                        delete vm.impPlan.systems[valueId]
                        $scope.selectSystems(valueId, subSystemsObj[name][st]);
                    } else {
                        vm.impPlan.systems[valueId] = true;
                        $scope.selectSystems(valueId, subSystemsObj[name][st]);
                    }
                }

            } else {
                for (ss in subSystemsObj) {
                    if (subSystemsObj[ss].indexOf(name) >= 0) {
                        if (!vm.impPlan.systems[_.where(vm.systemsList, { "name": name })[0].id]) {
                            vm.impPlan.systems[_.where(vm.systemsList, { "name": name })[0].id] = false;
                            delete vm.impPlan.systems[_.where(vm.systemsList, { "name": name })[0].id]
                            vm.impPlan.systems[_.where(vm.systemsList, { "name": ss })[0].id] = false;
                            delete vm.impPlan.systems[_.where(vm.systemsList, { "name": ss })[0].id]
                        }

                    }
                    var selectedSystemCount = 0;
                    for (si in subSystemsObj[ss]) {
                        try {
                            var sTem = _.where(vm.systemsList, { "name": subSystemsObj[ss][si] })
                            if (sTem[0] && vm.impPlan.systems[sTem[0].id]) {
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
                if (_.findWhere(vm.systemDetailsList, { "id": systemId, "name": name })) {
                    returnTrue = 1;
                }
                //Here, remove System information template from the list
                if (!vm.impPlan.systems[systemId]) {
                    if (vm.impPlan.systems) {
                        delete vm.impPlan.systems[systemId]
                    }
                    if (vm.impPlan.system) {
                        delete vm.impPlan.system[systemId]
                    }
                    vm.systemDetailsList = _.without(vm.systemDetailsList, _.findWhere(vm.systemDetailsList, {
                        "id": systemId,
                        "name": name
                    }));
                }
                if (returnTrue === 1) {
                    return false;
                }
                vm.systemDefaults[systemId] = []
                $scope.populatePutLevel(systemId)
                vm.selectedSystem = _.where(vm.systemsList, { id: systemId })[0].name
                //Add system information template to the list
                vm.systemDetailsList.push({
                    "id": systemId,
                    "name": name
                })
                vm.showSystemDetails = true;
                detectChange(systemId)
            }
        } catch (err) { }
    }

    $scope.changeMacroHeader = function (status) {
        var systemsMacroId = [];
        var systemsNotMacroId = [];
        var systemIds = [];
        if (vm.impPlan.loadType != 'STANDARD') {
            if (vm.impPlan.macroHeader) {
                macroHeader = vm.impPlan.macroHeader;
            }
        }
        if (status && vm.systemDetailsList.length > 0) {
            _.each(vm.systemDetailsList, function (systemId) {

                APIFactory.getLoadCategoryBySystem({ "ids": systemId.id }, function (response) {
                    _.each(response.data, function (data) {
                        if (systemId.id == data.systemId.id) {
                            var macroheadercategory = _.where(response.data, { name: "M" });
                            if (vm.systemDetailsList.length > 0) {
                                vm.loadWindowMacroCategories[systemId.id] = macroheadercategory;
                                vm.loadMacroHeaderCategorySystem(vm.loadWindowMacroCategories[systemId.id], systemId.id);
                            }
                        }
                    })

                })
            })
            if (vm.loadWindowMacroCategories.length > 0) {
                _.each(vm.loadWindowMacroCategories, function (elem, key) {
                    if (elem) {
                        if (elem.length > 0) {
                            systemsMacroId.push(key)
                        } else {
                            systemsNotMacroId.push(key);
                        }
                    }
                })
            }
            if (systemsMacroId.length > 0) {
                vm.systemDetailsList = _.filter(vm.systemDetailsList, function (elem) {
                    if (systemsMacroId.indexOf(elem.id) >= 0) {
                        return elem;
                    }
                })
            }
            if (systemsNotMacroId.length > 0) {
                _.each(systemsNotMacroId, function (each) {
                    // vm.impPlan.systems[each] = false;
                    delete vm.impPlan.systems[each]
                    delete vm.impPlan.system[each]
                    delete vm.impPlan.systems[undefined]
                })
                // vm.impPlan.systems[undefined] = false;
                Toaster.sayWarning("load category 'M' is not available for system  which is mandatory for creating macro/header only plan. Contact Tool Admin");
                return
            }

            _.each(vm.systemDetailsList, function (sysObj) {
                if (vm.loadWindowMacroCategories[sysObj.id].length > 0) {
                    var mcategoryId = _.find(vm.loadWindowMacroCategories[sysObj.id], { name: "M" })
                    vm.impPlan.system[sysObj.id].loadCategoryId = mcategoryId.id;
                }
                // else{
                //     Toaster.sayWarning("load category 'M' is not available for system  which is mandatory for creating macro/header only plan. Contact Tool Admin");
                // }
                vm.loadWindowCalendar(sysObj.id);
            })

            // vm.selectByCategory(systemId, name)
        } else {
            vm.impPlan.system = {}
            vm.impPlan.systems = {}
            _.each(vm.systemDetailsList, function (elem) {
                vm.impPlan.systems[elem.id] = false;
            })
            vm.systemDetailsList = [];
            vm.loadWindowMacroCategories = [];

        }
        setUserPrefs("loadWindowType", 2)
    }

    var loadCalendarByDateFreeze = []
    var loadCategoriesOfSystem = []

    /*
        When load type is Standard and Load Window is Selected by date
    */
    var loadDateTimeFreezeDataByDate = [];
    vm.selectByDate = function (id, name) {

        setUserPrefs("loadWindowType", 1)
        // setUserPrefs("loadWindowType", 2)
        var year = moment().year();
        var month = moment().month() + 1;
        try {
            IPService.alignStructure(vm, id, "standardTypeByDate")

            if (vm.impPlan.planStatus == 'ACTIVE' || vm.impPlan.planStatus == 'DEV_MGR_APPROVED') {
                IPService.clearFieldsOnChange(vm, id, "standardTypeByLoadWindowChange");
            }
        } catch (err) { }
    }
    vm.loadWindowCategories = []
    vm.loadWindowMacroCategories = []

    // Load category For macro header plan default
    vm.loadMacroHeaderCategorySystem = function (loadMacroCategorySystem, id) {
        var loadCateList = [];
        var loadMCateList = [];
        if (vm.impPlan.macroHeader && loadMacroCategorySystem.length <= 0) {
            vm.impPlan.systems[id] = false;
            vm.systemDetailsList = _.filter(vm.systemDetailsList, function (elem) {
                return elem.id != id;
            })
            Toaster.sayWarning("load category 'M' is not available for system  which is mandatory for creating macro/header only plan. Contact Tool Admin");
            return;
        } else if (vm.impPlan.macroHeader && Object.keys(vm.impPlan.system).length > 0) {
            if (loadMacroCategorySystem.length > 0) {
                _.each(loadMacroCategorySystem, function (sysObj) {
                    loadCateList.push(sysObj)

                    var mcategoryId = _.find(loadCateList, { name: "M" })
                    loadMCateList.push(mcategoryId)
                    if (vm.impPlan.system[id]) {
                        _.each(loadMCateList, function (mCat) {
                            vm.impPlan.system[id].loadCategoryId = mCat.id;
                        })
                    }
                    loadCateList = [];
                })
            }
            vm.loadWindowCalendar(id);
        }
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


    /*
        When load type is Standard and Load Window is Selected by category
    */
    vm.selectByCategory = function (id, name) {
        setUserPrefs("loadWindowType", 2)
        try {
            IPService.alignStructure(vm, id, "standardTypeByCategory")
            if (vm.impPlan.planStatus == 'ACTIVE' || vm.impPlan.planStatus == 'DEV_MGR_APPROVED') {
                IPService.clearFieldsOnChange(vm, id, "standardTypeByLoadWindowChange");
            }
            var macroHeader = false;
            if (vm.impPlan.macroHeader) {
                macroHeader = vm.impPlan.macroHeader;
            }
            APIFactory.getLoadCategoryBySystem({ "ids": id }, function (response) {
                vm.loadWindowCategories[id] = response.data
                if (macroHeader === false) {
                    vm.loadWindowCategories[id] = []

                    _.each(response.data, function (loadCat) {
                        if (loadCat.name !== 'M') {
                            vm.loadWindowCategories[id].push(loadCat)
                        }
                    })

                }
                var macroheadercategory = _.where(response.data, { name: "M" });
                // var macroCategoryIds = _.pluck(macroheadercategory,"id");
                // if(!macroHeader && macroheadercategory.length){
                //     vm.loadWindowCategories[id] = _.filter(response.data,function(elem){
                //         return macroCategoryIds.indexOf(elem.id) < 0;
                //     })
                // }
                if (vm.systemDetailsList.length > 0) {
                    vm.loadWindowMacroCategories[id] = macroheadercategory;
                    vm.loadMacroHeaderCategorySystem(vm.loadWindowMacroCategories[id], id);
                }
                // var categoryList = _.pluck(response.data, "id")
                // APIFactory.getLoadWindowList({ "ids": categoryList }, function(response) {
                //     if (response.status) {
                //         loadWindowOfLoadCategory[id] = response.data
                //         $(".planSaveBtn").removeAttr("disabled")
                //     } else {
                //         if (vm.impPlan.systems[id]) {
                //             $(".planSaveBtn").attr("disabled", "true")
                //             Toaster.sayError("Load Window not exists for the selected system. <br/> Contact Tool Admin.")
                //         } else {
                //             $(".planSaveBtn").removeAttr("disabled")
                //         }
                //         // Toaster.sayError(response.errorMessage)
                //     }
                // })
            })
        } catch (err) { }
    }
    vm.loadTime = []
    var loadDateTimeFreezeData = [];
    /*
        When load category is changed
    */
    vm.loadWindowCalendar = function (systemId) {
        try {
            if (vm.impPlan.loadType == "STANDARD") {
                if (vm.impPlan.system[systemId].loadWindowType == "1") {
                    vm.getRcategory(systemId)
                    vm.loadTime[systemId] = []
                    var date_param = moment(vm.impPlan.system[systemId].loaddate, appSettings.dateFormat).format("YYYY-MM-DD")
                    if (date_param == "Invalid date") {
                        return
                    }
                    APIFactory.getLoadWindowByDay({ "id": vm.impPlan.system[systemId].loadCategoryId, "date": moment(date_param, "YYYY-MM-DD").format("MM-DD-YYYY"), "day": moment(date_param, "YYYY-MM-DD").format("ddd") }, function (response) {
                        if (response.status && response.data.length > 0) {
                            vm.loadTime[systemId] = response.data
                            _.each(vm.loadTime[systemId], function (elem) {
                                var formatted_ts = Access.formatUIDate(moment(elem.timeSlot, appSettings.apiDTFormat)).split(" ")
                                elem.timeSlot = vm.impPlan.system[systemId].loaddate + " " + formatted_ts[1] + " " + formatted_ts[2];
                            })

                        } else {
                            vm.loadTime[systemId] = []
                        }
                    })
                } else {
                    IPService.clearFieldsOnChange(vm, systemId, "standardTypeByCategory");
                    vm.getRcategory(systemId)
                    var lObj;
                    if (!vm.impPlan.macroHeader) {
                        lObj = _.where(vm.loadWindowCategories[systemId], { id: parseInt(vm.impPlan.system[systemId].loadCategoryId) })[0]
                    } else {
                        lObj = _.find(vm.loadWindowMacroCategories, { name: "M" });
                        vm.impPlan.system[systemId].loadCategoryId = lObj.id;
                    }
                    APIFactory.getLoadFreezeByLoadCategories({ "ids": lObj.id }, function (response) {
                        if (response.status) {
                            loadDateTimeFreezeData[systemId] = response.data
                        } else {
                            loadDateTimeFreezeData[systemId] = []
                        }
                    })
                }
            }
        } catch (err) { }

    }

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
        if (vm.impPlan.platformId.indexOf("Delta") >= 0) {
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
                    }
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
    $("#probTktNo").tagit({
        singleField: true,
        caseSensitive: false,
        singleFieldNode: $('#probTktNo'),
        afterTagAdded: function (event, ui) {
            tktAdded($(ui.tag).find("span").html())
        }
    })

    /*
        Related Implementation Plans
    */
    function relatedPlanAdded(tkt) {
        tkt = tkt.toUpperCase()
        APIFactory.getPlan({ "id": tkt }, function (response) {
            if (response.status) { } else {
                $("#relatedPlan").tagit("removeTagByLabel", tkt);
                Toaster.sayWarning("Plan not found")
            }
        })
    }
    $("#relatedPlan").tagit({
        singleField: true,
        caseSensitive: false,
        singleFieldNode: $('#relatedPlan'),
        afterTagAdded: function (event, ui) {
            relatedPlanAdded($(ui.tag).find("span").html())
        }
    })

    vm.loadWindowCategories = []
    /*
        When load type is selected either Standard or Exception
    */
    vm.chooseLoadType = function () {
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
        if (vm.impPlan.loadType == "STANDARD") {
            var selectedCDate = selectedDate.split("_").slice(-1)[0]
            var date = moment(selectedCDate);
            var day = date.format("ddd");
            var sDateObj = moment(selectedCDate, "YYYY-MM-DD")
            vm.impPlan.system[id].loaddate = sDateObj.format(appSettings.dateFormat)

            IPService.clearFieldsOnChange(vm, id, "standardTypeByDate");
            vm.loadWindowCategories[id] = []
            var macroHeader = false;
            if (vm.impPlan.macroHeader) {
                macroHeader = vm.impPlan.macroHeader;
            }
            APIFactory.getLoadCategoriesByDate({ "id": id, "date": selectedCDate }, function (response) {
                if (response.status) {
                    vm.loadWindowCategories[id] = response.data

                    if (macroHeader === false) {
                        vm.loadWindowCategories[id] = []

                        _.each(response.data, function (loadCat) {
                            if (loadCat.name !== 'M') {
                                vm.loadWindowCategories[id].push(loadCat)
                            }
                        })

                    }

                    // var macroheadercategory = _.where(response.data, { name: "M" });
                    // var macroCategoryIds = _.pluck(macroheadercategory,"id");
                    // if(!macroHeader && macroheadercategory.length){
                    //     vm.loadWindowCategories[id] = _.filter(response.data,function(elem){
                    //         return macroCategoryIds.indexOf(elem.id) < 0;
                    //     })
                    // }

                    vm.loadWindowMacroCategories[id] = _.where(response.data, { name: "M" });
                    if (vm.loadWindowMacroCategories[id].length <= 0) {
                        vm.loadWindowMacroCategories[id] = [];
                        Toaster.sayWarning("load category 'M' is not available on this Date for Macro/Header Plan. Select a date for which 'M' category is available");
                        return;
                    } else {
                        _.each(vm.systemDetailsList, function (sysObj) {
                            if (vm.loadWindowMacroCategories[sysObj.id].length > 0) {
                                var mcategoryId = _.find(vm.loadWindowMacroCategories[sysObj.id], { name: "M" })
                                vm.impPlan.system[sysObj.id].loadCategoryId = mcategoryId.id;
                            }
                            vm.loadWindowCalendar(sysObj.id);
                        })
                    }
                    if (vm.systemDetailsList.length > 0) {
                        vm.loadMacroHeaderCategorySystem(vm.loadWindowMacroCategories[id], id);
                    }
                } else {
                    vm.loadWindowCategories[id] = []
                }
            })

            $mdDialog.hide()
        } else {

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


    /*
        Select from moment picker, by date
    */
    vm.onDateSelect = function (newVal, oldVal, systemId) {
        IPService.clearFieldsOnChange(vm, systemId, "exceptionType");
        var macroHeader = false;
        if (vm.impPlan.macroHeader) {
            macroHeader = vm.impPlan.macroHeader;
        }
        APIFactory.getLoadCategoryBySystem({ "ids": systemId }, function (response) {
            vm.loadWindowCategories[systemId] = response.data
            // var macroheadercategory = _.where(response.data, { name: "M" });
            // var macroCategoryIds = _.pluck(macroheadercategory,"id");
            // if(!macroHeader && macroheadercategory.length){
            //     vm.loadWindowCategories[systemId] = _.filter(response.data,function(elem){
            //         return macroCategoryIds.indexOf(elem.id) < 0;
            //     })
            // }

            if (macroHeader) {
                var mcategoryId = _.find(response.data, { name: "M" })
                vm.impPlan.system[systemId].loadCategoryId = mcategoryId.id;
            }
            else {
                vm.loadWindowCategories[systemId] = []

                _.each(response.data, function (loadCat) {
                    if (loadCat.name !== 'M') {
                        vm.loadWindowCategories[systemId].push(loadCat)
                    }
                })
            }

        })
    }

    /*
        Select from calendar, by date, - Controller
    */
    function loadDateCtrl($scope, $mdDialog, systemInfo) {
        try {
            var ld = this;
            var eventData = [];
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
            var cur_lcd = vm.impPlan.system[systemInfo].loaddate ? vm.impPlan.system[systemInfo].loaddate : moment().format("MM-DD-YYYY");
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
        } catch (err) { }

    }


    var systemId;
    /*
        Select from calendar, By category - Calendar Init
    */
    vm.showLoadCalendar = function (ev, systemid, systemname) {
        vm.selectByCategory(systemid, systemname)
        if (vm.impPlan.system[systemid].loadCategoryId) {
            try {
                systemId = systemid
                $mdDialog.show({
                    controller: loadWindowCtrl,
                    templateUrl: 'html/templates/loadCalendar.template.html',
                    controllerAs: 'lc',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    locals: {
                        systemInfo: systemid
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
        } else {
            Toaster.sayWarning("Choose Load Category")
            return;
        }


    }

    /*
        Select from calendar, By category - Controller
    */
    function loadWindowCtrl($scope, $mdDialog, systemInfo) {
        try {
            var lc = this;
            var eventData = [];
            var lStartDate, lEndDate, tStartDate, tEndDate;
            var freezeInfo = loadDateTimeFreezeData[systemInfo]
            for (lF in freezeInfo) {
                var builtFrom = moment(freezeInfo[lF].fromDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
                tStartDate = builtFrom.split(" ")[0]
                lStartDate = moment(builtFrom).tz(getTimeZone())
                var builtTo = moment(freezeInfo[lF].toDate).tz(getTimeZone()).format(appSettings.apiDTFormat)
                tEndDate = builtTo.split(" ")[0]
                diffDays = moment(tEndDate).diff(tStartDate, "days")
                var enumDate = lStartDate
                for (i = 0; i < diffDays; i++) {
                    // eventData.push({ "date": moment(enumDate).format("YYYY-MM-DD") })
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
                $("#loadWindowCalendar").zabuto_calendar({
                    language: "en",
                    cell_border: true,
                    today: true,
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

            function updateSelectedDate(date) {
                var selectedCDate = date.split("_").slice(-1)[0]
                vm.impPlan.system[systemId].loadCategoryDate = moment(selectedCDate, "MM-DD-YYYY").format(appSettings.dateFormat)
                vm.loadTime[systemId] = []
                var date_param = moment(vm.impPlan.system[systemId].loadCategoryDate, appSettings.dateFormat).format("YYYY-MM-DD")
                if (date_param == "Invalid date") {
                    $mdDialog.hide()
                    return
                }
                APIFactory.getLoadWindowByDay({ "id": vm.impPlan.system[systemId].loadCategoryId, "date": moment(date_param, "YYYY-MM-DD").format("MM-DD-YYYY"), "day": moment(date_param, "YYYY-MM-DD").format("ddd") }, function (response) {
                    if (response.status && response.data.length > 0) {
                        vm.loadTime[systemId] = response.data
                        _.each(vm.loadTime[systemId], function (elem) {
                            var formatted_ts = Access.formatUIDate(moment(elem.timeSlot, appSettings.apiDTFormat)).split(" ")
                            elem.timeSlot = selectedCDate + " " + formatted_ts[1] + " " + formatted_ts[2];
                        })

                    } else {
                        vm.loadTime[systemId] = []
                    }
                })
                $mdDialog.hide()
            }
        } catch (err) { }
    }

    /*
        When load type changes, build corresponding fields
    */
    $scope.$watch("vm.impPlan.loadType", function () {
        detectChange()
    })

    function getLoadTypes() {
        APIFactory.getLoadTypeList(function (response) {
            if (response.status) {
                vm.loadTypeList = response.data
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }



    var detectChange = function (sId) {
        if (vm.impPlan.loadType == "STANDARD") {
            vm.showLoadWindow = true

            if (!sId) {
                for (systemId in vm.impPlan.systems) {
                    if (vm.impPlan.system[systemId]) {
                        vm.impPlan.system[systemId].loadWindowType = null
                    }
                    IPService.alignStructure(vm, systemId, "exceptionType", true)
                }
            }
        } else if (vm.impPlan.loadType == "EXCEPTION" || vm.impPlan.loadType == "EMERGENCY") {
            vm.showLoadWindow = false
            for (systemId in vm.impPlan.systems) {
                // if (vm.impPlan.system[systemId] != undefined) {
                    vm.impPlan.system[systemId].exceptionLoaddate = vm.impPlan.system[systemId].loadCategoryDate ? vm.impPlan.system[systemId].loadCategoryDate : vm.impPlan.system[systemId].loaddate
                    vm.impPlan.system[systemId].exceptionLoadtime = vm.impPlan.system[systemId].loadDateTime ? vm.impPlan.system[systemId].loadDateTime.split(" ")[1] : ""
                    vm.selectByCategory(systemId)
                    IPService.alignStructure(vm, systemId, "exceptionType")
                }
            }
        }
    // }

    vm.putLevels = []

    $scope.populatePutLevel = function (systemId) {
        /*
            Load zTPF Levels for the selected system
        */

        APIFactory.getPutLevelBySystem({ "ids": systemId }, function (response) {
            if (response.status) {
                vm.putLevels[systemId] = response.data
                angular.forEach(vm.putLevels[systemId], function (putObj) {
                    if (putObj.id == putObj.systemId.defalutPutLevel) {
                        vm.systemDefaults[systemId].defaultPutForSystem = putObj.putLevel
                    }
                })
            } else {
                vm.putLevels[systemId] = []
            }
        })
    }

    /*
        Submit Implementation plan form
    */
    vm.submitImpPlanForm = function (data) {
        if ($rootScope.deltaUser && vm.impPlan.platformId == '2_Delta' && (vm.impPlan.macroHeader == undefined || vm.impPlan.macroHeader == false)) {
            vm.impPlan.rfcFlag = true;
        } else {
            vm.impPlan.rfcFlag = false;
        }
        try {
            if (vm.impPlan.projectId && typeof vm.impPlan.projectId.id != 'undefined' && vm.impPlan.projectId.id.length > 0) {
                vm.impPlan.projectId.id = vm.impPlan.projectId.id.toString();
            }
            if (vm.impPlan.projectId && vm.impPlan.projectId.id.length == 0) {
                vm.impPlan.projectId = null
            }
            var rData = angular.copy(data);
            if (rData.system) {
                delete rData.system["undefined"]
            }
            if (rData.systems) {
                delete rData.systems["undefined"]
            }

            if (vm.systemDetailsList.length <= 0) {
                Toaster.sayWarning("Please Choose a Target System");
                return;
            }
            for (systemId in rData.system) {

                var lObj = _.where(vm.loadWindowCategories[systemId], { id: parseInt(vm.impPlan.system[systemId].loadCategoryId) })[0]
                if (lObj !== undefined && lObj.name == 'R' && (rData.system[systemId].loadInstruction == null || rData.system[systemId].loadInstruction == '')) {
                    Toaster.sayWarning("For Load category " + lObj.name + " Special  instructions for TSD is a mandatory field");
                    return;
                }
            }
            if (fImplementationPlanValidate.validateIPFields(rData, vm.systemDetailsList)) {

                for (sT in rData.system) {
                    var lObj = _.where(vm.loadWindowCategories[sT], { name: $.trim(rData.system[sT].loadCategoryId) })[0]
                    if (!lObj) {
                        lObj = _.where(vm.loadWindowCategories[sT], { id: parseInt(rData.system[sT].loadCategoryId) })[0]
                    }
                    if (rData.system[sT].loadAttendee !== undefined && rData.system[sT].loadAttendee !== null && rData.system[sT].loadAttendee !== '' && rData.system[sT].loadAttendee.length > 0) {
                        var seletedLoadAttendee = _.where(vm.loadAttendeeList, { "id": rData.system[sT].loadAttendee[0] })[0];
                        rData.system[sT].loadAttendee = seletedLoadAttendee.displayName
                        rData.system[sT].loadAttendeeId = seletedLoadAttendee.id;
                    } else {
                        rData.system[sT].loadAttendee = "";
                        rData.system[sT].loadAttendeeId = "";
                    }

                    if (Array.isArray(rData.system[sT].qaFunctionalTesters)) {
                        rData.system[sT].qaFunctionalTesters = rData.system[sT].qaFunctionalTesters ? (rData.system[sT].qaFunctionalTesters.length > 1 ? rData.system[sT].qaFunctionalTesters.join(",") : rData.system[sT].qaFunctionalTesters[0]) : ""
                    }
                    if (lObj)
                        rData.system[sT].loadCategoryId = lObj.id
                }
                for (ds in rData.system) {
                    if (vm.impPlan.loadType == 'STANDARD') {
                        rData.system[ds].loadDateTime = Access.formatAPIDate(rData.system[ds].loadDateTime)
                    } else {
                        rData.system[ds].loadDateTime = Access.formatAPIDate(rData.system[ds].exceptionLoaddate + " " + $.trim(rData.system[ds].exceptionLoadtime) + ":00 " + moment(rData.system[ds].exceptionLoaddate + " " + $.trim(rData.system[ds].exceptionLoadtime)).tz(getTimeZone()).format("z"))
                    }
                    if (rData.system[ds].putIndicator && !rData.system[ds].putLevelId) {
                        Toaster.sayWarning("Choose zTPF Level")
                        return;
                    }
                    if (!rData.system[ds].putIndicator && rData.system[ds].putIndicator != undefined) {
                        rData.system[ds].putLevelId = null
                    }

                    if ($rootScope.deltaUser && vm.impPlan.platformId == '2_Delta' && (vm.impPlan.macroHeader == undefined || vm.impPlan.macroHeader == false)) {
                        if (!vm.impPlan.system[ds].loadAttendeeContact) {
                            Toaster.sayWarning("Please provide Load attendee phone number");
                            return;
                        }
                    }

                    /* moment().tz(getTimeZone()).format(rData.system[ds].loadDateTime,"MM-DD-YYYY HH:mm:ss "); */
                    if (rData.system[ds].putLevelId) {
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
                    rData.system[ds] = _.omit(rData.system[ds], ["putdate", "putIndicator"]);
                }

                /* zTPF Level Date Check -  Check load date is after zTPF Level date */

                if (!rData.macroHeader) {
                    rData.macroHeader = false
                }
                if (!rData.rfcFlag) {
                    rData.rfcFlag = false
                }

                var arrayData = rData.platformId.split("_")
                rData.platformId = arrayData[0];
                rData.platformName = arrayData[1].charAt(0);
                rData.sdmTktNum = $("#probTktNo").val()
                rData.relatedPlans = $("#relatedPlan").val().toUpperCase()
                rData["planStatus"] = "ACTIVE"

                if (rData.maintanceFlag === 'Yes' && (rData.sdmTktNum === null || rData.sdmTktNum === "")) {
                    Toaster.sayWarning("Maintenance DTN requires problem ticket number. Please provide problem ticket number.");
                    return;
                }
                if (typeof vm.impPlan.devManager != 'undefined' && vm.impPlan.devManager.length > 0 && typeof rData.devManager != 'undefined' && rData.devManager.length > 0) {
                    vm.impPlan.devManager = vm.impPlan.devManager.toString();
                    rData.devManager = rData.devManager.toString();
                }
                rData.devManagerName = _.where(vm.devManagerList, { "id": rData.devManager })[0].displayName
                vm.loader.save = true;
                APIFactory.savePlan(IPService.wrapperPlanSave(rData, vm), function (response) {
                    vm.loader.save = false;
                    if (response.status) {
                        Toaster.saySuccess("Implementation Plan " + response.metaData + " successfully created ");
                        setTimeout(function () {
                            $state.go("app.impPlan")
                        }, 2000)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })

            }
        } catch (err) { console.log(err) }

    };

    // Load Attendee List
    vm.loadAttendeeList = function () {
        try {

            APIFactory.getLoadAttendeeList({}, function (response) {
                if (response.status) {
                    vm.loadAttendeeList = response.data;
                } else {
                    vm.loadAttendeeList = []
                }

            })
        } catch (err) { }
    }

    // QA Functional Tester List
    vm.qaFunctionalTesterList = function () {
        try {

            APIFactory.getUsersByRole({ "role": "QA" }, function (response) {
                if (response.status) {
                    vm.qaFunctionalTesterList = response.data;
                } else {
                    vm.qaFunctionalTesterList = []
                }
                // initMultipleSelect2("#approver")
                // $timeout(function () {
                //     $("#approver").select2({
                //         maximumSelectionLength: 5
                //     });
                // }, 50)

            })
        } catch (err) { }
    }

    /*
        Load devmanager list
    */
    vm.loadDevManagerName = function () {

        try {

            APIFactory.getUsersByRole({ "role": "DevManager" }, function (response) {
                if (response.status) {
                    vm.devManagerList = response.data;
                    //                          $('#approver').select2();
                    //                          initSingleSelect2('#approver')

                } else {
                    vm.devManagerList = []
                    // Toaster.sayError(response.errorMessage);
                }
                initMultipleSelect2("#approver")
                $timeout(function () {
                    $("#approver").select2({
                        maximumSelectionLength: 1
                    });
                }, 50)
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

    vm.systemDBCR = {}

    function dbcrCtrl($scope, $mdDialog, systemId, systemName) {
        var dbcr = this;

        dbcr.fieldData = {}
        dbcr.validation = {
            "showText": false,
            "disableButton": true
        }

        $timeout(function () {
            $('[aria-label="dbcrNumber"]').focus()
        }, 1000)

        dbcr.systemName = systemName
        dbcr.mandatory = true;
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
        _.each(vm.impPlan.system[systemId].dbcrList, function (dbcrObj) {
            if (dbcrObj.mandatory == 'Y') {
                dbcrObj.mandatory = true;
            }
        })

        $scope.dbcrList = [];
        if (vm.impPlan.system[systemId].dbcrList) {
            $scope.dbcrList = vm.impPlan.system[systemId].dbcrList;
        }
        $scope.dbcrLoadAdd = function () {
            if (_.where($scope.dbcrList, { "dbcrName": dbcr.fieldData.dbcrName}).length > 0) {
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

        $scope.disableSaveDbcr = false;
        $scope.saveDBCRList = function () {
            $scope.disableSaveDbcr = true;
            var DBCRList = $scope.dbcrList
            for (index in DBCRList) {
                if (DBCRList[index].mandatory == true) {
                    DBCRList[index].mandatory = "Y"
                } else {
                    DBCRList[index].mandatory = "N"
                }
            }
            vm.impPlan.system[systemId].dbcrList = DBCRList;
            var DbcrObject = _.each(vm.systemsList, function (systemObj) {
                if (systemObj.id == systemId) {
                    systemObj.dbcrList = DBCRList;
                }
            });
            $scope.dbcrList = DBCRList;

            vm.systemDBCR[systemId] = []
            // for (key in vm.impPlan.systems) {
            _.filter($scope.dbcrList, function (obj) {
                if (systemId == obj.systemId) {
                    vm.systemDBCR[systemId].push(obj)
                }
            })
            $mdDialog.cancel();

            // vm.systemDBCR[key] = vm.systemDBCR[key].join(",")
            // }
            // $scope.disableSaveDbcr = false;
            
        }

        $scope.deleteDBCRItem = function (dbcrObj, index) {
            if (!dbcrObj.id) {
                $scope.dbcrList.splice(index, 1)
                return;
            }
            APIFactory.deleteDbcr({ "dbcrId": dbcrObj.id }, function (response) {
                if (response.status) {
                    // $scope.loadDBCRList()
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        $scope.cancel = function () {
            $mdDialog.cancel();
        };
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
            // setTimeout(function(){
            $("#csrId").select2({
                minimumInputLength: 1,
                maximumSelectionLength: 1,
                ajax: {
                    url: apiBase + projUrl,
                    dataType: 'json',
                    type: "GET",
                    headers: header,
                    data: function (params) {
                        return {
                            "filter": params.term,
                            "platform": _.findWhere(vm.pList, { id: parseInt(vm.impPlan.platformId.split("_")[0]) }).name
                        };
                    },
                    processResults: function (response) {
                        _.each(response.data, function (pObj) {
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
                // alert(JSON.stringify(vm.projectData))
                var selectedProject = _.where(vm.projectData, { id: parseInt($("#csrId").select2('val')) })
                // alert(JSON.stringify(selectedProject))
                vm.impPlan.projectName = selectedProject[0].projectName
                vm.impPlan.lobName = selectedProject[0].lineOfBusiness
                vm.impPlan.projectManagerId = selectedProject[0].managerId
                vm.impPlan.projectManagerName = selectedProject[0].managerName
                vm.impPlan.isDeltaFlag = selectedProject[0].isDelta ? selectedProject[0].isDelta : false
                vm.impPlan.projectSponsorId = selectedProject[0].sponsorId
                vm.impPlan.maintanceFlag = selectedProject[0].maintanceFlag
                $scope.$digest()
            });
            // },500)
            setTimeout(function () {
                $(".select2-selection.select2-selection--single").css({
                    "border": "1px solid #ccc",
                    "border-radius": "0px",
                    "background": "none",
                    "height": "38px"
                })
                $("#select2-csrId-container").css({
                    "padding": "3px"
                })
                $(".select2-selection__arrow").css({
                    "top": "6px"
                })
            }, 1000)
        })

    }



    $(document).ready(function () {
        // initSingleSelect2("#csrId")
        //initSingleSelect2("#approver")
        // setTimeout(function(){
        $(".select2.select2-container.select2-container--default").addClass('form-control')
        $(".select2.select2-container.select2-container--default").css({
            'width': '100%'
        })

        $(".select2-selection.select2-selection--single").addClass('select2Enhance')

        /*
         Function init for selecting Approach A or B in Legend Fieldset
         */
        $(document.body).on('click', '.dropdown-menu li', function (event) {

            var $target = $(event.currentTarget);

            $target.closest('.btn-group')
                .find('[data-bind="label"]').text($target.text())
                .end()
                .children('.dropdown-toggle').dropdown('toggle');

            return false;

        });

    });

    /*
        Calls triggered on page load
    */
    var callsTriggeredOnLoad = [loadPlatforms(), getLoadTypes(), vm.loadDevManagerName(), vm.loadAttendeeList(), vm.qaFunctionalTesterList(), $scope.loadProjects()]


})
