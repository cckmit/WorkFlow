app.service('IService', function ($rootScope, $http, $q, appSettings, Flash, $location, WFLogger,
    APIFactory, apiService, $mdDialog, Paginate, WSService, Toaster, Access, $window) {

    var IService = {};
    // var systemLoadList = []

    var initImpl = function ($scope, vm, callFrom) {

        Paginate.refreshScrolling()
        // function refreshPage () {
        // var page_y = $(".scrollingFunction").scrollTop();
        // window.location.href = window.location.href.split('?')[0] + '?page_y=' + page_y;
        // }

        // $(".scrollingFunction").scroll(function() {
        // refreshPage();
        // if ( window.location.href.indexOf('page_y') != -1 ) {
        // var match = window.location.href.split('?')[1].split("&")[0].split("=");
        // var scrolling_position = match[1];
        // localStorage.setItem("scrolling_position", scrolling_position);
        // }
        // })
        // $rootScope.saveformData = function() {

        // var elmnt = document.getElementById("scroll_function");
        // elmnt.scrollTop = JSON.parse(localStorage.getItem('scrolling_position'))
        // }

        /* Pagination Table Starts */

        var columnsToBeSorted = ["planId.id", "id", "impStatus", "loaddatetime"]
        var tableAttr;
        var initTableSettings = function () {
            $scope.tableConfig = Paginate.tableConfig()
            $scope.pageSizeList = Paginate.pageSizeList()
            tableAttr = Paginate.initTableAttr($scope.tableConfig.pageSize)
            $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
            if (Access.getSearchData().recentPage && $scope.enableRecentPage) {
                $scope.tableConfig.currentPage = Access.getSearchData().recentPage + 1
            }
        }
        initTableSettings()
        $scope.switchPageSize = function () {
            tableAttr.offset = 0
            tableAttr.limit = $scope.tableConfig.pageSize;
            $scope.tableConfig.currentPage = 1
            localStorage.setItem('paginateDefaultData', JSON.stringify(tableAttr.limit));
            tableAttr.limit = JSON.parse(localStorage.getItem("paginateDefaultData"))
            $rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
            loadImplementationList(tableAttr)
        }
        $scope.pageChangeHandler = function (num) {
            if (vm.loadCategoryList && $scope.tableConfig.lastLoadedPage === num) {
                return;
            }
            $scope.tableConfig.lastLoadedPage = num
            tableAttr.offset = num - 1
            Access.getSearchData().recentPage = tableAttr.offset

            localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
            tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
            $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))

            if (vm.filterText) {
                tableAttr.filter = vm.filterText ? vm.filterText : ""
            } else {
                tableAttr.filter = vm.searchPlanData ? vm.searchPlanData : ""
            }
            loadImplementationList(tableAttr)
        };

        $scope.searchPlanDataforDev = function (searchData) {
            $scope.enableRecentPage = false;
            initTableSettings()
            $scope.enableRecentPage = true;
            isPopupShown = false;
            yesChanged = false;
            tableAttr.filter = searchData ? searchData : ""
            tableAttr.offset = 0;
            loadImplementationList(tableAttr)
        }

        $scope.refreshData = function () {
            initTableSettings()
            tableAttr.offset = $rootScope.paginateValue
            tableAttr.filter = ""
            vm.searchPlanData = ""
            loadImplementationList(tableAttr)
        }

        $scope.pageChangeHandler($scope.tableConfig.currentPage)
        $scope.sort = function (columnName) {
            var lSort = Paginate.sort(columnName, tableAttr, $scope.tableConfig, $scope.sortColumn, columnsToBeSorted)
            tableAttr = lSort.tableAttr
            var previousSortImp = {};
            previousSortImp = tableAttr.orderBy;
            Access.getSearchData().previousSortImp = previousSortImp;
            $scope.tableConfig = lSort.tableConfig
            $scope.sortColumn = lSort.sortColumn
            loadImplementationList(tableAttr)
        }

        /* Pagination Table Ends */
        $scope.disableSearchFields = false;
        $scope.showForSearch = true;
        function loadImplementationList(tableAttr) {
            if (callFrom == "search") {
                if ($scope.onceImplementationTriggered) {
                    tableAttr.offset = 0
                    localStorage.setItem('Pagination_number', JSON.stringify(tableAttr.offset));
                    tableAttr.offset = JSON.parse(localStorage.getItem("Pagination_number"))
                    $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))

                    $scope.onceImplementationTriggered = false
                }
                if (Access.getSearchData().recentPage && Access.getSearchData().recentPage != 0 && $scope.enableRecentPage) {
                    // tableAttr.offset = Access.getSearchData().recentPage;
                    $scope.tableConfig.currentPage = tableAttr.offset + 1;
                }
                if (Access.getSearchData().previousSortImp) {
                    if (Object.keys(Access.getSearchData().previousSortImp).length > 0) {
                        tableAttr.orderBy = Access.getSearchData().previousSortImp;
                        $scope.sortColumn = Paginate.resetSortColumn(columnsToBeSorted)
                        var values = Object.keys(tableAttr.orderBy).map(function (e) {
                            return (tableAttr.orderBy)[e]
                        })
                        $scope.sortColumn[Object.keys(tableAttr.orderBy)[0]][values[0]] = true
                    }
                }
                vm.totalNumberOfItem = ""
                APIFactory.getCommonPlanListforImpl(tableAttr, function (response) {
                    if (response.status && response.data) {
                        $scope.isLoading = false
                        vm.implementationList = _.pluck(response.data, "impl")
                        vm.systemList = _.pluck(response.data, "systemLoadDetails")
                        _.each(vm.implementationList, function (sObj) {
                            sObj.systemLoadList1 = []
                            sObj.systemLoadList = []
                            _.each(vm.systemList, function (pObj) {
                                _.each(pObj, function (lObj) {
                                    if (lObj.systemLoad.planId.id == sObj.planId.id) {
                                        sObj.systemLoadList1.push(lObj.systemLoad)

                                    }


                                })
                                // })
                            })
                            sObj.systemLoadList = removeDumplicateValue(sObj.systemLoadList1)
                        })
                        $scope.tableConfig.totalItems = response.count
                        vm.totalNumberOfItem = response.count
                        vm.implementationExport = true;
                        $rootScope.saveformData()
                    } else {
                        $scope.isLoading = false
                        vm.implementationExport = false;
                        vm.implementationList = []
                    }
                })
            } else if (callFrom == "mytasks") {
                tableAttr = _.omit(tableAttr, ["orderBy", "filter"])
                APIFactory.getDevTasks(tableAttr, function (response) {
                    if (response.status && response.data && response.data.length > 0) {
                        vm.implementationList = response.data
                        $scope.tableConfig.totalItems = response.count
                        vm.totalNumberOfItem = response.count
                        $rootScope.saveformData()
                    } else {
                        vm.implementationList = []
                    }
                })
            } else {
                APIFactory.getInboxImplementationList(tableAttr, function (response) {
                    $scope.disableSearchFields = true;
                    $scope.showForSearch = false;
                    if (response.status && response.data) {
                        // vm.keyValues = Object.keys(response.data)
                        vm.implementationList = _.pluck(response.data, "impl")
                        vm.systemList = _.pluck(response.data, "systemLoadDetails")
                        _.each(vm.implementationList, function (sObj) {
                            sObj.systemLoadList1 = []
                            sObj.systemLoadList = []
                            _.each(vm.systemList, function (pObj) {
                                _.each(pObj, function (lObj) {
                                    if (lObj.systemLoad.planId.id == sObj.planId.id) {
                                        sObj.systemLoadList1.push(lObj.systemLoad)

                                    }
                                })
                                // })
                            })
                            sObj.systemLoadList = removeDumplicateValue(sObj.systemLoadList1)
                        })
                        // vm.listForDev = _.pluck(vm.systemList,"systemLoadList")
                        $scope.tableConfig.totalItems = response.count
                        vm.totalNumberOfItem = response.count
                        $rootScope.saveformData()


                    } else {
                        vm.totalNumberOfItem = response.count
                        vm.implementationList = []
                    }
                })
            }
        }

        getImplActions($scope, vm)

        function removeDumplicateValue(myArray) {
            var newArray = [];
            angular.forEach(myArray, function (value, key) {
                var exists = false;
                angular.forEach(newArray, function (val2, key) {
                    if (angular.equals(value.id, val2.id)) { exists = true };
                });
                if (exists == false && value.id != "") { newArray.push(value); }
            });
            return newArray;
        }

        // TRACKER STARTS

        $scope.planStatusModal = function (planId) {
            var current = false;
            $scope.plan_id = planId.id;
            planId.implementationMessage = [];
            planId.implementationStatus = [];
            planId.implementationId = [];
            APIFactory.getPlanTrackStatus({ "planId": $scope.plan_id }, function (l_response) {
                if (l_response.status) {
                    planId.trackerImplementationPlan = l_response.data;
                    _.each(planId.trackerImplementationPlan.stages, function (stages) {
                        if (stages.currentStatus == 'IN_PROGRESS') {
                            planId.currentStatus = stages.currentStatus;
                            planId.currentStatusId = stages.id;
                            planId.implementationPlanMessage = stages.messages;
                        }
                    })
                    _.each(planId.trackerImplementationPlan.implementations, function (message) {
                        planId.implementationMessage.push(message.messages);
                        planId.implementationStatus.push(message.currentStage.status);
                        planId.implementationId.push(message.currentStage.id);
                    })
                    if ($rootScope.currentRole == 'Developer') {
                        planId.step_indicator = true;
                    } else {
                        planId.step_indicator = false;
                    }
                }
            })
        }

        // TRACKER ENDS

        // @Override
        $scope.createWorkspace = function (implementationObj) {
            var paramObj = {
                "implId": implementationObj.id
            }
            APIFactory.getCreateWorkspaceUrl(paramObj, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Import implementation completed successfully")
                    initTableSettings()
                    $rootScope.saveformData()
                    loadImplementationList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        //WebSocket Response
        WSService.initGitPortChange(function (sResponse) {
            if (sResponse != undefined && sResponse.length > 0) {
                $window.location.reload();
                $rootScope.saveformData()
            }
        })


        // @Override
        // $scope.peerReviewRequest = function(implementationObj) {
        //     APIFactory.requestPeerReview({
        //         "implId": implementationObj.id
        //     }, function(response) {
        //         if (response.status) {
        //             Toaster.saySuccess("Peer review ticket created successfully")
        //             initTableSettings()
        //             loadImplementationList(tableAttr)
        //         } else {
        //             Toaster.sayError(response.errorMessage)
        //         }
        //     })
        // }

        $scope.revertBackToActive = function (implementationId) {
            APIFactory.revertImplementation({
                "impId": implementationId.id
            }, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Implementation Reverted back to Active")
                    initTableSettings()
                    loadImplementationList(tableAttr)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        function getGIPort() {
            currentUser = getUserData("user");
            $rootScope.GiPort = 0;
            var params = {
                "userId": currentUser.id
            }
            APIFactory.getGIPort(params, function (response) {
                if (response.status) {
                    $rootScope.GiPort = 1;
                } else {
                    $rootScope.GiPort = 0;
                }
            })
        }
        getGIPort();

        $scope.showGIonClick = function (implId) {
            var params = {
                "implId": implId
            }

            APIFactory.getGIXML(params, "", function (response) {
                if (response.status) {
                    Toaster.saySuccess(response.data);
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        }


        $scope.downloadTestCaseImpl = function (fileName, implementation) {
            try {
                var params = {
                    "implId": implementation.id,
                    "planId": implementation.planId.id,
                    "testFile": fileName
                }
                APIFactory.downloadTestCase(params, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], { type: response.metaData });
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) {
            }

        }

        return {
            "scope": $scope,
            "vm": vm
        }
    }
    IService.initImpl = initImpl;



    function getImplActions($scope, vm, plan, impId) {
        $scope.loadSegmentandTestCasesList = function (implementation) {
            paramObj = {
                "ids": implementation.id
            }
            APIFactory.getSegmentList(paramObj, function (response) {
                if (response.status && response.data.length > 0) {
                    implementation.segments = response.data
                } else {
                    implementation.segments = []
                }
            })
            APIFactory.listTestCases({
                "implId": implementation.id,
                "planId": implementation.planId.id
            }, function (response) {
                if (response.status && response.data.length > 0) {
                    implementation.uploadedFiles = response.data
                } else {
                    implementation.uploadedFiles = [];
                }
            })
        }

        // Restore File
        $scope.restoreSegments = function (impId) {
            APIFactory.developerGitRestore({ "implId": impId }, function (response) {
                if (response.status) {
                    Toaster.saySuccess(response.data)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        $scope.loadSegmentByDeveloperList = function (implementation, impId) {
            paramObj = {
                "ids": impId
            }
            APIFactory.getSegmentList(paramObj, function (response) {
                if (response.status && response.data.length > 0) {
                    implementation.segments = response.data
                } else {
                    implementation.segments = []
                }
            })
            APIFactory.listTestCases({
                "implId": impId,
                "planId": implementation.planId.id
            }, function (response) {
                if (response.status && response.data.length > 0) {
                    implementation.uploadedFiles = response.data
                } else {
                    implementation.uploadedFiles = [];
                }
            })
        }
        $scope.createWorkspace = function (implementationObj, plan) {
            var paramObj = {
                "implId": implementationObj.id
            }
            APIFactory.getCreateWorkspaceUrl(paramObj, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Import implementation completed successfully")
                    $scope.getImplementationByPlan(plan)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        // $scope.peerReviewRequest = function(implementationObj, plan) {
        //     APIFactory.requestPeerReview({
        //         "implId": implementationObj.id
        //     }, function(response) {
        //         if (response.status) {
        //             Toaster.saySuccess("Peer review ticket created successfully")
        //             $scope.getImplementationByPlan(plan)
        //         } else {
        //             Toaster.sayError(response.errorMessage)
        //         }
        //     })
        // }

        $scope.revertBackToActive = function (implementationId, plan) {
            APIFactory.revertImplementation({
                "impId": implementationId.id
            }, function (response) {
                if (response.status) {
                    Toaster.saySuccess("Implementation Reverted back to Active")
                    $scope.getImplementationByPlan(plan)
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
        }

        $scope.downloadTestCaseImpl = function (fileName, implementation) {
            try {
                var params = {
                    "implId": implementation.id,
                    "planId": implementation.planId.id,
                    "testFile": fileName
                }
                APIFactory.downloadTestCase(params, function (response) {
                    if (response.status) {
                        var resposeStr = base64ToArrayBuffer(response.data)
                        var file = new Blob([resposeStr], { type: response.metaData });
                        saveAs(file, fileName)
                    } else {
                        Toaster.sayError(response.errorMessage);
                    }
                })
            } catch (err) {
            }

        }
        $scope.checkShowGI = function (pObj, iObj) {
            return Access.getShowGIButton(pObj, iObj)
        }

        $scope.checkActionPermission = function (currentUserId, iObj) {
            return Access.getActionPermissionList(currentUserId, iObj);
        }

        //Implementation Plan Details View when clicked on Imp Plan ID - Starts
        $scope.showImpPlanDetails = function (ev, planId) {
            $mdDialog.show({
                controller: showImpPlanDetailsCtrl,
                controllerAs: "ip",
                templateUrl: 'html/templates/IPDetailsDialog.template.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,
                locals: {
                    IPId: planId
                }
            })
                .then(function (answer) {

                }, function () {

                });
        };

        function showImpPlanDetailsCtrl($scope, $mdDialog, IPId) {
            var ip = this;
            $scope.pObj = IPId;
            APIFactory.getSystemLoadByPlan({ "ids": IPId.id }, function (response) {
                if (response.status) {
                    $scope.pObj.systemLoadList = response.data
                    APIFactory.getDbcrList({ "planIds": IPId.id }, function (dresponse) {
                        if (dresponse.status) {
                            if (dresponse.data && dresponse.data.length > 0) {
                                for (index in $scope.pObj.systemLoadList) {
                                    $scope.pObj.systemLoadList[index].systemLoad.systemId.dbcrList = []
                                    _.filter(dresponse.data, function (obj) {
                                        if ($scope.pObj.systemLoadList[index].systemLoad.systemId.id == obj.systemId.id) {
                                            $scope.pObj.systemLoadList[index].systemLoad.systemId.dbcrList.push(obj)
                                        }
                                    })
                                }
                            }
                        } else {
                            Toaster.sayError(dresponse.errorMessage)
                        }
                    })
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }
        //Implementation Plan Details View when clicked on Imp Plan ID - Ends
    }

    function getcreateNewSourceArtifact($scope, ev, impId, planId) {
        $mdDialog.show({
            controller: newSourceArtifact,
            templateUrl: 'html/templates/newSourceArtifact.template.html',
            controllerAs: 'self',
            parent: angular.element(document.body),
            targetEvent: ev,
            locals: {
                implementationId: impId,
                planId: planId,
                scopeobj: $scope
            },
            clickOutsideToClose: false,
            fullscreen: $scope.customFullscreen // Only for -xs, -sm breakpoints.
        })
            .then(function (answer) {
                $scope.status = 'You said the information was "' + answer + '".';
            }, function () {
                $scope.status = 'You cancelled the dialog.';
            });
    }



    function newSourceArtifact($scope, $mdDialog, implementationId, planId, scopeobj) {
        var self = this
        self.reportView = false
        self.fileList = []
        self.createFile = false;
		var subSystemsObj = {}

		
        self.selectPSS = function (name, objFile) {
            subSystemsObj = {
                "PSS": ["AIR", "RES"]
            }
            var selectedSys = []
            var sysName;
            _.each(name, function (systemName) {
                if (systemName == "PSS") {
                    if (systemName in subSystemsObj) {
                        for (st in subSystemsObj[systemName]) {
                            sysName = _.where(self.newFile[objFile.id].systemsList, { name: subSystemsObj[systemName][st] })[0].name
                            selectedSys.push(sysName)
                        }
                        _.each(self.newFile, function (pObj) {
                            _.each(selectedSys, function (sObj) {
                                if (pObj.id == objFile.id) {
                                    self.newFile[pObj.id].targetSystemList.push(sObj)
                                    self.newFile[pObj.id].targetSystemList = self.newFile[pObj.id].targetSystemList.filter(function (v, i, self) {
                                        return i == self.indexOf(v);
                                    });
                                }
                            })
                        })
                        self.newFile[objFile.id].targetSystemList.splice(self.newFile[objFile.id].targetSystemList.indexOf("PSS"), 1);
						self.newFile[objFile.id].systemsList.splice(self.newFile[objFile.id].systemsList.indexOf(name, "PSS"), 1)
                    }
                }
            })
        }
       

        $scope.getLimitedWord = function (word, size) {
            if (word.length <= size) {
                return word;
            } else {
                return word.substr(0, 10) + '...';
            }
        }
        
        APIFactory.listSourceArtifactExtenstions({}, function (response) {
            self.ibmExtensionList = response.data["IBM"]
            self.nonibmExtensionList = response.data["NON_IBM"]
        })

        APIFactory.listNonIBMFilePaths({}, function (response) {
            self.nonibmFilePathsList = response.data;
        })
        self.fileList = []
        var objId = 0
        self.showFiles = true;

        $scope.deleteArtifact = function (id, index) {
            self.fileList = _.reject(self.fileList, {
                "id": id
            })
            delete self.newFile[id]
        }

        $scope.addSourceSet = function (sourceType) {
        	self.validateFlag=false;
            setTimeout(function () {
                $(".notallowenterkey").focus();
                $(".notallowenterkey").keypress(function (event) {
                    if (event.keyCode === 13)
                        event.preventDefault();
                });
                $(".notallowenterfilename").keypress(function (event) {
                    if (event.keyCode === 13)
                        event.preventDefault();
                });
            }, 100);

            self.showFiles = false;
            if(self.fileList.length >0){
           	 _.each(self.newFile, function (newFileObj) {
           		 if(newFileObj.programName == "" || newFileObj.targetSystemList == "" || newFileObj.fileName == "" ){
           			self.validateFlag=true;
           			 Toaster.sayWarning("Provide all details for file before proceeding with the next file")
           		        return
           		 }
           	 })
           }
            objId = objId + 1
            if (sourceType === "IBM" && !self.validateFlag) {
                self.fileList.push({
                    "id": objId,
                    "sType": sourceType
                })
				 APIFactory.getSystemListByPlan({
					"planId": planId
				}, function (response) {
					if (response.status) {
						self.newFile[objId].systemsList = response.data
						// vm.systemListaData = self.systemsList
						APIFactory.getIBMPutLevelBySystem({
							"ids": _.pluck(self.newFile[objId].systemsList, "id")
						}, function (response) {
							self.putList = response.data
						})
						APIFactory.getNonIBMPutLevelBySystem({
							"ids": _.pluck(self.newFile[objId].systemsList, "id")
						}, function (response) {
							self.NonIBMputList = response.data
						})
						if(_.pluck(_.pluck(self.newFile[objId].systemsList, "platformId"),"id")[0] == 2){
							self.newFile[objId].systemsList.push({ name: "PSS" })
						}
					} else {
						self.newFile[objId].systemsList = []
					}
				})
            } else {
            	if(!self.validateFlag){
					self.fileList.push({
						"id": objId,
						"sType": sourceType
					})
					
					 APIFactory.getSystemListByPlan({
						"planId": planId
					}, function (response) {
						if (response.status) {
							self.newFile[objId].systemsList = response.data
							// vm.systemListaData = self.systemsList
							APIFactory.getIBMPutLevelBySystem({
								"ids": _.pluck(self.newFile[objId].systemsList, "id")
							}, function (response) {
								self.putList = response.data
							})
							APIFactory.getNonIBMPutLevelBySystem({
								"ids": _.pluck(self.newFile[objId].systemsList, "id")
							}, function (response) {
								self.NonIBMputList = response.data
							})
							if(_.pluck(_.pluck(self.newFile[objId].systemsList, "platformId"),"id")[0] == 2){
								self.newFile[objId].systemsList.push({ name: "PSS" })
							}
						} else {
							self.newFile[objId].systemsList = []
						}
					})
				}
            }
			 // if(_.pluck(_.pluck(self.systemsList, "platformId"),"id")[0] == 2){
				// self.systemsList.push({ name: "PSS" })
				// self.systemsList = self.systemsList.filter(function (item, index, self) {
					// return index <= 3;
				// });
			// }
				
        }
        self.otherExt = []
        $scope.changeFileType = function (id) {
            if (self.newFile[id].fileExt === "Other") {
                self.newFile[id].fileExt = ''
                self.newFile[id].fileName = ''
                self.otherExt[id] = true;
            } else if (self.newFile[id].fileType === "NON_IBM" && self.newFile[id].fileExt != "Other") {
                self.newFile[id].fileName = self.nonibmFilePathsList[self.newFile[id].fileExt];
            }
            if (self.newFile[id].fileExt != "Other" && self.newFile[id].fileExt != "xml" && self.newFile[id].fileExt != "xsd" && self.newFile[id].fileExt != "json" && self.newFile[id].fileExt != "" && self.newFile[id].programName.length > 0) {
                self.newFile[id].programName = self.newFile[id].programName.toLowerCase()
            }
        }

        $scope.clearExt = function (id) {
            self.newFile[id].fileExt = ''
            self.otherExt[id] = false;
        }

        var validationFlag = false;

        $scope.putLevelChangeAction = function (fileObj) {
            var errorSystems = []
            var putFinalList = []
            self.newFile[fileObj.id].errorfuncArea = true
            _.each(fileObj.putList, function (psObj) {
                putFinalList.push(psObj.putLevel)
            })
            var systemforSelectedPut = _.where(putFinalList, {
                "putLevel": self.newFile[fileObj.id].funcArea.split('[')[1].replace(']', '').trim()
            })[0].systemId.name

            _.each(fileObj.putList, function (psObj) {
                if (psObj.putLevel.putLevel != self.newFile[fileObj.id].funcArea.split('[')[1].replace(']', '').trim() && psObj.putLevel.systemId.name != systemforSelectedPut) {
                    errorSystems.push(psObj.putLevel.systemId.name)
                }
            })
            if (errorSystems.length > 0) {
                validationFlag = true;
                self.newFile[fileObj.id].putLevelErrorMessage = "Selected zTPF Level not applicable for " + _.uniq(errorSystems).join(",")
            } else {
                validationFlag = false
                self.newFile[fileObj.id].errorfuncArea = false
            }
        }

        $scope.onSelect = function (fileObj, systemName, selectedSystems, result) {
            fileObj.putList = []
            if (fileObj.sType == "IBM") {
                fileObj.putList = _.filter(self.putList, function (putObj) {
                    return (selectedSystems && selectedSystems.length > 0) ? ((selectedSystems.indexOf(putObj.putLevel.systemId.name) >= 0) && (putObj.putLevel.id == putObj.putLevel.systemId.defalutPutLevel) && putObj.putLevel.status == "PRODUCTION") : false;
                })
                fileObj.putList = _.uniq(fileObj.putList, function (item, key, a) {
                    return item.putLevel.putLevel;
                });
                self.newFile[fileObj.id].funcArea = "";
                self.newFile[fileObj.id].errorfuncArea = false;
				if(result && (systemName == "AIR" || systemName == "RES")) {
					self.newFile[fileObj.id].systemsList.push({ name: "PSS" })
					self.newFile[fileObj.id].systemsList = self.newFile[fileObj.id].systemsList.filter(function (item, index, self) {
						return index <= 3;
					});
				} 
				
				var data = self.newFile[fileObj.id].targetSystemList.filter(function(item) {
				  return subSystemsObj.PSS.includes(item); 
				})
				
				if((JSON.stringify(subSystemsObj.PSS) == JSON.stringify(data))) {
					self.newFile[fileObj.id].systemsList = self.newFile[fileObj.id].systemsList.filter(function (item, index, self) {
						return index <= 2;
					});
				}
            }

            if (fileObj.sType == "NON_IBM") {
                fileObj.NonIBMputList = [];
                var nonInputList = [];
				
                _.each(selectedSystems, function (elem) {
                    nonInputList.push(self.NonIBMputList[elem])
                    //fileObj.NonIBMputList = (_.uniq(fileObj.NonIBMputList.concat(self.NonIBMputList[elem]))).sort();
                    //fileObj.NonIBMputList = _.intersection(self.NonIBMputList[elem])
                    fileObj.NonIBMputList = _.intersection.apply(null, nonInputList);
                })							
				
				var data = self.newFile[fileObj.id].targetSystemList.filter(function(item) {
				  return subSystemsObj.PSS.includes(item); 
				})
								
				// console.log(data)
				// console.log(JSON.stringify(subSystemsObj.PSS))
				// console.log(JSON.stringify(data))
				// console.log((JSON.stringify(subSystemsObj.PSS) == JSON.stringify(data)))
				if((JSON.stringify(subSystemsObj.PSS) == JSON.stringify(data))) {
					self.newFile[fileObj.id].systemsList = self.newFile[fileObj.id].systemsList.filter(function (item, index, self) {
						return index <= 2;
					});
				}
				
				if(result && (systemName == "AIR" || systemName == "RES")) {
					self.newFile[fileObj.id].systemsList.push({ name: "PSS" })
					self.newFile[fileObj.id].systemsList = self.newFile[fileObj.id].systemsList.filter(function (item, index, self) {
						return index <= 3;
					});
				}

				// self.newFile[fileObj.id].TargetSystem = self.newFile[fileObj.id].targetSystemList			
			}
            $scope.$digest()
        }
	
        var tempObj = {};
        var checkDuplicate = function (a) {
            var fileList = [];
            for (var i = 0; i < a.length; i++) {
                for (var j = 0; j < a.length; j++) {
                    if (i != j && a[i].programName == a[j].programName && a[i].fileName == a[j].fileName && a[i].fileExt == a[j].fileExt) {
                        var checkSystem = checkSystemDuplicate(a[i].targetSystemList, a[j].targetSystemList);
                        if (checkSystem.length > 0) {
                            var fileObj = {};
                            fileObj.programName = a[i].programName;
                            fileObj.fileName = a[i].fileName;
                            fileObj.fileExt = a[i].fileExt;
                            fileObj.systemName = checkSystem;
                            fileList.push(fileObj);
                            return fileList;
                        }
                    }
                }
            }
            return fileList;
        }

        function checkSystemDuplicate(systemlistA, systemlistB) {
            var systems = [];
            for (var i = 0; i < systemlistA.length; i++) {
                if (_.contains(systemlistB, systemlistA[i])) {
                    systems = systemlistA[i]
                    return systems;
                }
            }
            return systems;
        }


        $scope.createSourceArtifact = function (fileList) {
            self.createFile = true;
            if (objId == 0 || fileList.length === 0) {
                Toaster.sayWarning("Add IBM/Non-IBM Files")
                self.createFile = false;
                return;
            }

            if (validationFlag) {
                return false;
            }

            for (index in fileList) {
                if (fileList[index].fileExt != 'XML' && fileList[index].fileExt != 'XSD' && fileList[index].fileExt != 'JSON' && fileList[index].programName.length > 0 && fileList[index].fileExt != 'xml' && fileList[index].fileExt != 'xsd' && fileList[index].fileExt != 'json') {
                    fileList[index].programName = fileList[index].programName.toLowerCase()
                }
            }
            var validationFailed = false
            for (index in fileList) {
                for (key in fileList[index]) {
                    if (key.indexOf("error") >= 0) {
                        continue
                    }
                    if (fileList[index]['fileExt'] === 'Other' || fileList[index]['fileType'] === 'IBM' || (self.otherExt[index] && fileList[index]['fileType'] === 'NON_IBM')) {
                        if ((!fileList[index][key] || fileList[index][key].length === 0) && (["fileType"].indexOf(key) == -1)) {
                            fileList[index]["error" + key] = true
                            self.createFile = false;
                            validationFailed = true
                        } else {
                            // self.createFile = false;
                            fileList[index]["error" + key] = false
                        }
                    } else {
                        // If values not exists
                        if ((!fileList[index][key] || fileList[index][key].length === 0) && (["fileType", "fileName"].indexOf(key) == -1)) {
                            fileList[index]["error" + key] = true
                            if (fileList[index]['fileType'] === 'NON_IBM') {
                                self.createFile = false;
                                fileList[index]["errorfileName"] = false;
                                validationFailed = true
                            } else {
                            }
                        } else { // If value exists
                            fileList[index]["error" + key] = false
                        }
                    }
                }
            }

            if (validationFailed) {
                return false;
            }


            var FileLists = _.values(fileList);
            if (FileLists.length > 1) {
                var SystemDuplicate = checkDuplicate(FileLists);
                if (SystemDuplicate.length > 0) {
                    Toaster.sayWarning("Duplicate segment - " + SystemDuplicate[0].fileName + "/" + SystemDuplicate[0].programName + "." + SystemDuplicate[0].fileExt + " found for target system - " + SystemDuplicate[0].systemName + ", kindly remove any one");
                    self.createFile = false;
                    return;
                }
            }

            var dataObj = []
            var lBranch = []
            angular.forEach(fileList, function (fObj) {
                lBranch = []
                for (index in fObj.targetSystemList) {
                    lBranch.push({
                        "targetSystem": fObj.targetSystemList[index],
                        "fileType": fObj.fileType,
                        "funcArea": fObj.funcArea.toUpperCase().split('[')[1].replace(']', '').trim()
                    })
                }
                var pgmName = fObj.programName + "." + fObj.fileExt;
                var fileName = fObj.fileName;
                fileName = fileName.charAt(fileName.length - 1) != "/" ? fileName + "/" : fileName;
                dataObj.push({
                    "fileName": fileName + pgmName,
                    "branch": lBranch
                })
            })
            self.createFile = true;
            APIFactory.createSourceArtifact({
                "implId": implementationId
            }, dataObj, function (response) {
                self.createFile = true;
                if (response.status) {
                    self.reportView = true
                    self.resultFileList = response.data;
                    scopeobj.loadSegmentList()
                    scopeobj.loadImpData()
                    validationFlag = false
                } else {

                    Toaster.sayError(response.errorMessage)
                }
                self.createFile = false;

            })

        }

        $scope.cancel = function () {
            $mdDialog.cancel();
        };

    }



    var initImplActions = function ($scope, vm, plan, impId) {
        getImplActions($scope, vm, plan, impId)
    }
    var initCreateNewSourceArtifact = function ($scope, ev, impId, planId) {
        getcreateNewSourceArtifact($scope, ev, impId, planId);
    }
    IService.initCreateNewSourceArtifact = initCreateNewSourceArtifact;
    IService.initImplActions = initImplActions;

    return IService;

});