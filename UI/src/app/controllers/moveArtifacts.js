dashboard.controller("moveArtifacts", function ($rootScope, $templateCache, $scope, $http, $state, $location,
    Toaster, appSettings, apiService, APIFactory, WFLogger, $q, $filter, IPService, Paginate, WSService, $timeout, Access) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;
    Paginate.refreshScrolling();
    vm.systemsList = []
    vm.packageList = []
    $scope.userRole = getUserData('userRole')
    vm.currentUser = $rootScope.home_menu.id


    //OnLoad Function

    setTimeout(function () {
        $scope.openSegments();
    }, 200)

    setTimeout(function () {
        $(".moveArtifactsHeight").css('height', $(window).height() - 175)
        $(".moveArtifactsInProgress").css('height', $(window).height() - 195)
    }, 500)

    WSService.initPkgMovement(function (response) {
        Toaster.sayStatus(response.status)
        $rootScope.saveformData()
    })

    WSService.initFuncPkgMove(function (response) {
        if (response == "Completed") {
            $rootScope.http_loading = false;
            showLoading(false)
            $("#segmentLoader").css('display', 'none')
            Toaster.saySuccess("Migration Successful");
        } else {
            $rootScope.http_loading = true;
            showLoading(true)
            $("#segmentLoader").css('display', 'block')
        }
    })

    APIFactory.getSystemList(function (response) {
        if (response.status && response.data.length > 0) {
            vm.systemsList = response.data
            APIFactory.getNonIBMPutLevelBySystem({
                "ids": _.pluck(vm.systemsList, "id")
            }, function (response) {
                if (response.status && Object.keys(response.data).length > 0) {
                    vm.packageList = response.data
                } else {
                    vm.packageList = []
                }
            })
        } else {
            vm.systemsList = []
        }
    })

    //Segments
    /*********************************************************************************/
    /*********************************************************************************/

    var productionMetaData = {
        "pageStartCount": 0,
        "pageEndCount": 10,
        "selectedSegmentCount": 0,
        "totalCount": 0,
        "pageNo": 0,
        "pageCount": 10,
        "segSearchProcess": true,
        "selectedData": [],
        "searchText": "",
        "metaData": null,
        "searchList": [],
        "segmentsList": []
    }
    // var nonProductionMetaData = {
    //     "pageStartCount": 0,
    //     "pageEndCount": 10,
    //     "selectedSegmentCount": 0,
    //     "totalCount": 0,
    //     "pageNo": 0,
    //     "pageCount": 10,
    //     "segSearchProcess": true,
    //     "selectedData": [],
    //     "searchText": "",
    //     "metaData": null,
    //     "searchList": [],
    //     "segmentsList": []
    // }
    // var ibmVanillaMetaData = {
    //     "pageStartCount": 0,
    //     "pageEndCount": 10,
    //     "selectedSegmentCount": 0,
    //     "totalCount": 0,
    //     "pageNo": 0,
    //     "pageCount": 10,
    //     "segSearchProcess": true,
    //     "selectedData": [],
    //     "searchText": "",
    //     "metaData": null,
    //     "searchList": [],
    //     "segmentsList": []
    // }

    $scope.productionMetaData = productionMetaData
    // $scope.nonProductionMetaData = nonProductionMetaData
    // $scope.ibmVanillaMetaData = ibmVanillaMetaData
    $scope.initPNP = function () {
        productionMetaData.searchList = [];
        productionMetaData.totalCount = 0;
        // nonProductionMetaData.searchList = [];
        // nonProductionMetaData.totalCount = 0;
        // ibmVanillaMetaData.searchList = [];
        // ibmVanillaMetaData.totalCount = 0;
    }

    $scope.openSegments = function () {
        $scope.initPNP()

        $scope.searchText = ""
        // $scope.vanillaSearchText = ""
        productionMetaData.selectedData = [];
        // nonProductionMetaData.selectedData = [];
        // ibmVanillaMetaData.selectedData = [];
        productionMetaData.searchText = "";
        // nonProductionMetaData.searchText = "";
        // ibmVanillaMetaData.searchText = "";
        productionMetaData.segmentsList = [];
        // nonProductionMetaData.segmentsList = [];
        // ibmVanillaMetaData.segmentsList = [];
        productionMetaData.selectedSegmentCount = 0;
        // nonProductionMetaData.selectedSegmentCount = 0;
        // ibmVanillaMetaData.selectedSegmentCount = 0;


        APIFactory.inprogressUserActioncheck({ "user": vm.currentUser }, function (response) {
            if (response.status) {

                $rootScope.http_loading = false;
                showLoading(false)

                $("#segmentSearch").modal({
                    backdrop: 'static',
                    keyboard: true
                });


                $('#segmentSearch').on('hidden', function () {
                    $(this).data('modal', null);
                });
                $(".pnpTabs").css('display', 'block')
                // $(".populateSCMArea").css('display', 'none')
                // $(".populateReportTemplate").css('display', 'none')
                $(".checkoutReportTemplate").css('display', 'none')
                $(".checkoutSegments").css('display', 'none')
                $(".chkCheckout").css('display', 'none')
                $("#segmentLoader").css('display', 'none')

                $(".searchSegments").css('display', 'block')
                $(".chkSelected").css('display', 'block')
                $timeout(function () {
                    document.getElementById('srchtext').focus();
                }, 1000);

            } else {
                Toaster.sayError(response.errorMessage)
                $rootScope.http_loading = true;
                showLoading(true)
                $("#segmentLoader").css('display', 'block')
            }
        })


    }

    $scope.searchSegment = function (searchText, pnp, pageCount, cbk) {
        var params = {
            implId: "", // implId : ""
            fileName: $.trim(searchText).replace("*", ""),
            offset: pageCount,
            limit: 10,
            flag: pnp
        }
        APIFactory.searchFile(params, function (response) {
            cbk(response)
        })
    }
    var PNP = ["productionMetaData"]
    vm.showCheckoutLoading = false
    $scope.searchSegmentPNP = function (searchText) {
        $scope.initPNP()
        if ($.trim(searchText) == "") {
            Toaster.sayWarning("Segment name cannot be empty");
            return false;
        }
        if ($.trim(searchText).length <= 1) {
            Toaster.sayWarning("Minimum two characters required");
            return false;
        }

        var pnpErrorList = []
        vm.showCheckoutLoading = true
        $q.when()
            .then(function () {
                var deferred = $q.defer();
                var searchStatus = 0;
                angular.forEach(PNP, function (repo) {
                    $scope[repo].pageCount = 0
                    $scope[repo].pageNo = 0;
                    // var prodFlag = (repo == "productionMetaData") ? "PROD" : ""
                    var prodFlag = $scope.userRole == "DevManager" ? "MIGNONIBM" : "MIGOBS"

                    $scope.searchSegment(searchText, prodFlag, $scope[repo].pageCount, function (response) {
                        searchStatus++;
                        if (response.status) {
                            if (response.data && response.data.length > 0) {
                                $scope[repo].segSearchProcess = true;
                                $scope[repo].pageStartCount = 1;
                                $scope[repo].pageEndCount = $scope[repo].pageCount;
                                $scope[repo].totalCount = response.data.length;
                                $scope[repo].searchList = response.data;
                            } else {
                                $scope[repo].segSearchProcess = false;
                                $scope[repo].searchList = [];
                            }
                        } else {
                            pnpErrorList.push(response.errorMessage)
                        }
                        vm.showCheckoutLoading = false
                    })
                })
                setInterval(function () {
                    if (searchStatus == 2) {
                        deferred.resolve();
                    }
                }, 100);
                return deferred.promise;
            })
            .then(function () {
                vm.showCheckoutLoading = false
                $timeout(function () {
                    _.each(PNP, function (type, index) {
                        _.each($scope[type].selectedData, function (sObj) {
                            $(".class_PROD_" + sObj.fileNameWithHash).addClass('ulHighligt')

                        })
                    })
                }, 100)
                pnpErrorList = _.compact(pnpErrorList)
                if (pnpErrorList.length > 0) {
                    // Right now, not error message is returned from the backend
                    // Toaster.sayError(pnpErrorList.join(","))
                }
            });
    }
    $scope.selectSegmentPNP = function (data) {
        try {
            var repo;
            var selectedRow;

            prodFlag = "PROD"
            repo = PNP[0]
            selectedRow = ".class_" + prodFlag + "_" + data.fileNameWithHash

            // Check for already selected segment within prod itself
            var refStatus = []
            var prodSelectedListObj = _.filter(angular.copy($scope["productionMetaData"].selectedData), function (nObj) {
                var branches_iteration = _.pluck(nObj.branch, "targetSystem")
                var branches_selected = _.pluck(data.branch, "targetSystem")
                var branches_matched = _.intersection(branches_iteration, branches_selected)
                refStatus = _.pluck(data.branch, "refStatus")
                return (nObj.fileName == data.fileName) && (branches_matched.length > 0);
            })
            if (prodSelectedListObj.length > 0 && _.intersection(refStatus, _.pluck(prodSelectedListObj[0].branch, "refStatus")) == 0) {
                var preSelectedRow = ".class_" + prodFlag + "_" + prodSelectedListObj[0].fileNameWithHash
                if ($(preSelectedRow).hasClass('ulHighligt')) {
                    $(preSelectedRow).removeClass('ulHighligt');
                }
                $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                    "fileName": prodSelectedListObj[0].fileName,
                    "fileNameWithHash": prodSelectedListObj[0].fileNameWithHash
                });
            }


            var arrayData = [];
            if ($(selectedRow).hasClass('ulHighligt')) {
                $(selectedRow).removeClass('ulHighligt');
                $scope[repo].selectedData = _.reject($scope[repo].selectedData, {
                    "fileName": data.fileName,
                    "fileNameWithHash": data.fileNameWithHash
                });
            } else {
                if ($(selectedRow).length != 0) {

                    $(selectedRow).addClass('ulHighligt');
                    $scope[repo].selectedData.push(data);
                }
            }
            $scope[repo].selectedSegmentCount = $scope[repo].selectedData.length;
            try {
                $scope.$digest()
            } catch (err) { }
        } catch (err) {
            console.log(err)
        }
    }

    // Filter Functional on segment selection
    $scope.filterFunctionalArea = function (segmentSystems) {
        try {
            var selectedsys = []
            for (i in segmentSystems.targetSystems) {
                selectedsys.push(_.where(vm.systemsList, { "name": ($filter("systemFilter")(segmentSystems.targetSystems[i].toString())) })[0].id)
            }
            var result = []
            var filterPackage = []
            if (selectedsys.length > 0 && vm.packageList && Object.keys(vm.packageList).length > 0) {
                _.each(selectedsys, function (elem) {
                    result = _.uniq(result.concat(vm.packageList[_.where(vm.systemsList, { "id": parseInt(elem) })[0].name])); // change to _.union if no common functional packages required
                })
                _.each(segmentSystems.branch, function (segbranch) {
                    result = _.reject(result, function (eachPackage) {
                        // return (eachPackage == $filter("functionalFilter")(segbranch.funcArea))
                        return (eachPackage == segbranch.funcArea)
                    })
                })
            }
            if (result.length == 0) {
                segmentSystems.blockMigrationDuetoFuncArea = true
                return result;
            }
            return result;
        } catch (err) { }

    }

    // var productionBackup; // production - Added for sending production data alone
    $scope.removeSegment = function (data) {
        $scope.selectSegmentPNP(data)
        var preSelectedRow = ".class_PROD_" + data.fileNameWithHash
        if ($(preSelectedRow).hasClass('ulHighligt')) {
            $(preSelectedRow).removeClass('ulHighligt');
        }
        $scope.segmentsList = _.reject($scope.segmentsList, function (obj) {
            var branches_iteration = _.pluck(obj.branch, "targetSystem")
            var branches_selected = _.pluck(data.branch, "targetSystem")
            var branches_matched = _.intersection(branches_iteration, branches_selected)
            return (obj.fileName === data.fileName && branches_matched.length > 0 && obj.prodFlag === data.prodFlag);
        });
        _.each(PNP, function (repo) {
            $scope[repo].selectedData = _.reject($scope[repo].selectedData, function (obj) {
                var branches_iteration = _.pluck(obj.branch, "targetSystem")
                var branches_selected = _.pluck(data.branch, "targetSystem")
                var branches_matched = _.intersection(branches_iteration, branches_selected)
                return (obj.fileName === data.fileName && branches_matched.length > 0 && obj.prodFlag === data.prodFlag);
            });
            $scope[repo].selectedSegmentCount = $scope[repo].selectedData.length
        })
        // productionBackup = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData)
    }

    $scope.chooseSegment = function () {
        try {
            $(".searchSegments").fadeToggle('fast')
            $(".chkSelected").fadeToggle('fast')
            $timeout(function () {
                $(".checkoutSegments").fadeToggle('fast')
                $(".checkoutSegmentsTitle").fadeToggle('fast')
                $(".chkCheckout").fadeToggle('fast')
            }, 200)
            var prod = _.union($scope[PNP[0]].segmentsList, $scope[PNP[0]].selectedData)
            // var nonProd = _.union($scope[PNP[1]].segmentsList, $scope[PNP[1]].selectedData)
            productionBackup = prod
            // $scope.segmentsList = prod.concat(nonProd);
            $scope.segmentsList = prod
            $scope.segmentsList = angular.copy($scope.segmentsList)

        } catch (err) { }
    }
    $scope.backToSelection = function () {
        try {
            $(".checkoutSegments").fadeToggle('fast')
            $(".checkoutSegmentsTitle").fadeToggle('fast')
            $(".chkCheckout").fadeToggle('fast')
            $timeout(function () {
                $(".searchSegments").fadeToggle('fast')
                $(".chkSelected").fadeToggle('fast')
            }, 200)
        } catch (err) { }
    }

    $scope.checkoutSegments = function () {
        if ($scope.segmentsList.length === 0) {
            Toaster.sayWarning("Add Files for Checkout");
            return;
        }
        for (pbObj in $scope.segmentsList) {
            if ($scope.segmentsList[pbObj].blockMigrationDuetoFuncArea) {
                Toaster.sayWarning("Please delete " + $scope.segmentsList[pbObj].programName + " from selection to Migrate")
                return;
            }
            if (!$scope.segmentsList[pbObj].targetFuncArea || $scope.segmentsList[pbObj].targetFuncArea.length <= 0) {
                Toaster.sayWarning("Select Target Functional Area for " + $scope.segmentsList[pbObj].programName)
                return;
            }
            for (branchItem in $scope.segmentsList[pbObj].branch) {
                $scope.segmentsList[pbObj].branch[branchItem].isBranchSelected = true
                $scope.segmentsList[pbObj].branch[branchItem].targetFuncArea = $scope.segmentsList[pbObj].targetFuncArea.split('[')[1].replace(']', '')
            }
            delete ($scope.segmentsList[pbObj].blockMigrationDuetoFuncArea)
            delete ($scope.segmentsList[pbObj].targetFuncArea)
        }
        $("#chkoutAnimate").removeClass("aBox").addClass("aBox")
        $(".chkModalBody").css("background", "#455A64")
        $(".checkoutSegments").hide()
        $("#segmentLoader").css('display', 'none')
        $(".chkCheckout").hide()
        $(".checkoutLoading").show()
        for (pbObj in productionBackup) {
            productionBackup[pbObj].branch = _.reject(productionBackup[pbObj].branch, {
                "isBranchSelected": false
            })
        }
        // var dataObj = angular.copy(productionBackup)
        var dataObj = $scope.segmentsList

        var params = {
            // "implId": $stateParams.impId
            "implId": ""
        }

        APIFactory.migrateNonIbmFile(params, dataObj, function (response) {
            $timeout(function () {
                $(".chkModalBody").css("background", "#fff")
                $(".checkoutLoading").hide()
                $("#segmentLoader").css('display', 'none')
                $(".checkoutSegmentsTitle").css('display', 'none')
                $(".checkoutReportTemplate").css('display', 'block')
                if (response.status) {
                    vm.checkoutStatusList = response.data
                    _.each(vm.checkoutStatusList, function (source_obj) {
                        source_obj.branch = _.uniq(source_obj.branch, "targetSystem")
                    })
                } else {
                    vm.checkoutStatusList = response.data
                    Toaster.sayError(response.errorMessage)
                    $(".okBtn").click(function () {
                        $(function () {
                            $('#segmentSearch').modal('hide');
                        });
                    });
                }
                $scope.$digest()
            }, 1000)
        })
    }

    /*********************************************************************************/
    /*********************************************************************************/


    /*********************************************************************************/
    /*********************************************************************************/
})