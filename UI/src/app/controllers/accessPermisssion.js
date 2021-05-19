dashboard.controller("accessPermissionCtrl", function ($rootScope, $state, APIFactory, $scope, Toaster) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    vm.currentUser = getUserData("user")
    $scope.RepoPermissionList = {}
    APIFactory.getRepoPermission({}, function (response) {
        if (response.status) {
            $scope.RepoPermissionList = response.data;
            $rootScope.saveformData()
        } else {
            Toaster.sayError(response.errorMessage)
        }
    })

    APIFactory.getAllRepoPermission({}, function (response) {
        if (response.status) {
            $scope.RepoAllPermissionList = response.data;
            $rootScope.saveformData()
        } else {
            Toaster.sayError(response.errorMessage)
        }
    })
    /* On Load Function Starts */
    function getSettingsList() {
        APIFactory.getSettingsList({ "userId": vm.currentUser.id }, function (response) {
            if (response.status && response.data.length) {
                vm.currentUserSetting = _.findWhere(response.data, { name: "REPO_OWNER_ALERT" });
                if (vm.currentUserSetting && vm.currentUserSetting.value == "Y") {
                    vm.notifyEmails = true;
                } else {
                    vm.notifyEmails = false;
                }
                vm.reloadsettingList = false;
            } else {
                vm.reloadsettingList = true;
                var temObj = {
                    name: "REPO_OWNER_ALERT",
                    value: "Y",
                    userId: vm.currentUser.id
                }
                vm.currentUserSetting = temObj;
                // Toaster.sayError(response.errorMessage)
            }
        })
    }

    getSettingsList();

    $scope.expandPermissionAbbreviations = function (value) {
        var keyString = value;
        _.each($scope.RepoAllPermissionList, function (rValue, rKey) {
            if (rValue == value) {
                keyString = rKey
            }
        })
        return keyString;
    }


    vm.repoOwnersList = []
    var promise1 = new Promise(function (resolve, reject) {
        APIFactory.getUsersByRole({ "role": "DevManager" }, function (response) {
            if (response.status) {
                setTimeout(resolve, 0, response.data)
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    })

    var promise2 = new Promise(function (resolve, reject) {
        APIFactory.getUsersByRole({ "role": "Lead" }, function (response) {
            if (response.status) {
                setTimeout(resolve, 0, response.data)
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    })

    var promise3 = new Promise(function (resolve, reject) {
        APIFactory.getUsersByRole({ "role": "Reviewer" }, function (response) {
            if (response.status) {
                setTimeout(resolve, 0, response.data)
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    })

    var promise4 = new Promise(function (resolve, reject) {
        APIFactory.getUsersByRole({ "role": "Developer" }, function (response) {
            if (response.status) {
                setTimeout(resolve, 0, response.data)
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    })

    Promise.all([promise1, promise2, promise3, promise4]).then(function (values) {
        _.each(values, function (val) {
            vm.repoOwnersList = vm.repoOwnersList.concat(val);
        })
        vm.repoOwnersList = _.uniq(vm.repoOwnersList, "id");
    });


    $scope.expandOwnerDisplayName = function (value) {
        var ownerName = value;
        _.each(vm.repoOwnersList, function (ownerObj) {
            if (ownerObj.id == value) {
                ownerName = ownerObj.displayName;
            }
        })
        return ownerName;
    }

    /* On Load Function Starts */
    function getProductionRepoList() {
        vm.filecreateFlag = "Yes";
        APIFactory.getProductionRepoList({}, function (response) {
            if (response.status) {
                $scope.ProductionRepoList = response.data;
                // $rootScope.saveformData() -- getting exception  TypeError: $rootScope.saveformData is not a function
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })

    }
    getProductionRepoList()


    $scope.changeEmailNotify = function () {
        if (!vm.notifyEmails) {
            vm.currentUserSetting.value = "N";
        } else {
            vm.currentUserSetting.value = "Y";
        }

        APIFactory.saveDelegation(vm.currentUserSetting, function (response) {
            if (response.status) {
                if (vm.reloadsettingList) {
                    getSettingsList();
                }
            } else {
                Toaster.sayError(response.errorMessage);
            }
        })
    }

    $scope.chooseFunctionalArea = function (selectvalue) {
        var selectObj = _.find($scope.ProductionRepoList, function (elem) {
            return elem.repository.name == selectvalue;
        });
        vm.defaultpermission = selectObj.defaultAccess;
        vm.repoFuncdesc = selectObj.repository.description;
        $scope.checkNewFileCreateFlag(selectObj.repository.name);
    }

    $scope.checkNewFileCreateFlag = function (repo) {
        APIFactory.checkNewFileCreateFlag({
            "repoName": repo
        }, function (response) {
            if (response.status) {
                if (Object.keys(response.data).length > 0) {
                    vm.filecreateFlag = response.data;
                }
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }

    vm.SetDefault = function () {

        if (!vm.defaultfunctionArea || vm.defaultfunctionArea == "") {
            Toaster.sayWarning("Choose Default Repository to Permission")
            return
        }

        var dataObj = _.find($scope.ProductionRepoList, function (elem) {
            return elem.repository.name == vm.defaultfunctionArea;
        });
        dataObj.defaultAccess = vm.defaultpermission;
        dataObj.filecreateFlag = vm.filecreateFlag;
        dataObj.repository.description = vm.repoFuncdesc;
        APIFactory.updateRepoPermission({}, dataObj, function (response) {
            if (response.status) {
                // getProductionRepoList()
                vm.defaultfunctionArea = "";
                vm.defaultpermission = "";
                vm.repoFuncdesc = "";
                Toaster.saySuccess("Updated Successfully");
            } else {
                Toaster.sayError(response.errorMessage);
            }
        });
    }

    // setTimeout(function() {
    //     $("#repositoryOwners").select2()
    //     $("#repositoryOwners").val($scope.repoOwnersList).trigger("change");
    // }, 100)


    $scope.chooseSpecialFunctionalArea = function (selectvalue) {
        $scope.showOwnerList = true;
        var selectedRepo = _.filter($scope.ProductionRepoList, function (elem) {
            return selectvalue == elem.repository.name;
        })[0];
        vm.specialDelegate = selectedRepo.defaultAccess;
        APIFactory.specialPrivilegedUsers({ repoName: selectvalue }, function (response) {
            if (response.status && Object.keys(response.data).length > 0) {
                vm.repoOwnersDisplayList = response.data;
                $scope.showRepoList = true;
                $rootScope.saveformData()

            } else {
                vm.repoOwnersDisplayList = [];
                $scope.showRepoList = false;
                //                Toaster.sayError(response.errorMessage);
            }
        });
    }

    $scope.SetSpecial = function () {
        if (!vm.specialfunctionalArea || vm.specialfunctionalArea == "") {
            Toaster.sayWarning("Choose Special Repository")
            return
        }
        if (!vm.specialRepoOwners || vm.specialRepoOwners == "") {
            Toaster.sayWarning("Choose Special Privileged Users")
            return
        }
        if (!vm.specialpermission || vm.specialpermission == "") {
            Toaster.sayWarning("Choose Special Permission")
            return
        }
        var selectedUserList = [];
        var selectUser = {};
        selectUser.userList = {}
        var repoOwnerKeys = Object.keys(vm.repoOwnersDisplayList);

        var obj = {};
        _.each(vm.specialRepoOwners, function (elem) {
            var key = elem;
            if (repoOwnerKeys.indexOf(elem) >= 0) {
                obj[key] = "";
                _.extend(selectUser.userList, obj)
            } else {
                obj[key] = "NEW";
                _.extend(selectUser.userList, obj)
            }
        })
        var selectPermission = $scope.RepoPermissionList[vm.specialpermission];
        APIFactory.setRepoOwnersPermission({ repoName: vm.specialfunctionalArea, access: selectPermission }, selectUser, function (response) {
            if (response.status) {
                vm.specialfunctionalArea = "";
                vm.specialDelegate = "";
                vm.specialpermission = "";
                $scope.showOwnerList = false;
                vm.specialRepoOwners = null
                setTimeout(function () {
                    $("#repositoryOwners").val(null).trigger("change");
                }, 500);
                Toaster.saySuccess("Updated Successfully");
            } else {
                Toaster.sayError(response.errorMessage);
            }
        });
    }

    $scope.mapPermissionValue = function (permission) {
        return $scope.RepoPermissionList[permission];
    }

    vm.searchRepo = '';
    $scope.searchFilter = function () {
        var toReturn = {}
        _.each(vm.repoOwnersDisplayList, function (value, key) {
            if (key.indexOf(vm.searchRepo) >= 0) {
                toReturn[key] = value
            }
        })
        return toReturn;
    }

    // $scope.checkRepoOwnerList = function () {
    //     if(vm.repoOwnersDisplayList)
    //     return Object.keys(vm.repoOwnersDisplayList).length >= 0;
    // }


})