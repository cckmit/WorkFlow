dashboard.controller("manageDelegatesCtrl", function($rootScope, $templateCache, appSettings, $scope,
    $state, $location, Toaster, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;
    vm.currentUser = getUserData("user")
    vm.delegateStatus = 0
    Paginate.refreshScrolling();

    function loadDelegateSettings(param_userId) {
        vm.delegateToUser = ""
        APIFactory.getSettingsList({ "userId": param_userId }, function(response) {
            if (response.status && response.data.length > 0) {
                vm.delegateSettingsList = response.data

                var delegate_user = _.where(vm.delegateSettingsList, { "userId": param_userId, "name": "DELEGATE_USER" })
                if (delegate_user.length > 0) {
                    vm.delegateToUser = delegate_user[0].value
                }
                var delegate_status = _.where(vm.delegateSettingsList, { "userId": param_userId, "name": "DELEGATION" })
                if (delegate_status.length > 0) {
                    if (delegate_status[0].value == "TRUE") {
                        vm.delegateStatus = 1
                    } else {
                        vm.delegateStatus = 0
                    }
                }
            } else {
                vm.delegateToUser = ""
                vm.delegateStatus = 0
            }
        })
    }

    setTimeout(function() {
        $("#delegateUsersListFrom").select2()
        $('#delegateUsersListFrom').on("select2:select", function(e) {
            loadDelegateSettings(e.params.data.id)
        });
        $("#delegateSuperUsersListFrom").select2()
        $("#delegateUsersListTo").select2()
        $(".select2.select2-container.select2-container--default").css({
            "width": "100%"
        })
        $(".select2-selection.select2-selection--single").css({
            "border": "1px solid #ccc",
            "border-radius": "0px",
            "background": "#fff",
            "height": "38px",
            "padding": "10px"
        })
    }, 200)

    APIFactory.getDelegationToUsersList(function(response) {
        if (response.status) {
            vm.delegateToUsersList = response.data
        } else {
            vm.delegateUsersList = []
        }
    })

    APIFactory.getDelegationFromUsersList(function(response) {
        if (response.status) {
            vm.delegateFromUsersList = response.data
        } else {
            vm.delegateUsersList = []
        }
    })

    APIFactory.getSuperUserFromUsersList(function(response) {
        if (response.status) {
            vm.superUserFromUsersList = response.data
        } else {
            vm.superUserFromUsersList = []
        }
    })

    $scope.saveDelegation = function() {
        if (!vm.delegateFromUser || vm.delegateFromUser == "") {
            Toaster.sayWarning("Choose delegate from name")
            return
        }

        if (!vm.delegateToUser || vm.delegateToUser == "") {
            Toaster.sayWarning("Choose delegate to name")
            return
        }

        if (vm.delegateFromUser == vm.delegateToUser) {
            Toaster.sayWarning("Delegate from and to cannot be same")
            return
        }

        var delegate_user = _.where(vm.delegateSettingsList, { "userId": vm.delegateFromUser, "name": "DELEGATE_USER" })
        var delegate_status = _.where(vm.delegateSettingsList, { "userId": vm.delegateFromUser, "name": "DELEGATION" })

        var selected_from_user = _.where(vm.delegateFromUsersList, { "id": vm.delegateFromUser })[0]
        var selected_to_user = _.where(vm.delegateToUsersList, { "id": vm.delegateToUser })[0]
        var d_DELEGATE_USER, d_DELEGATION;
        if (delegate_user.length == 0 && !delegate_user.id) {
            d_DELEGATE_USER = {
                "name": "DELEGATE_USER",
                "value": selected_to_user.id,
                "userId": selected_from_user.id
            }
        } else {
            delegate_user[0].value = selected_to_user.id
            delegate_user[0].userId = selected_from_user.id
            d_DELEGATE_USER = delegate_user[0]
        }
        if (delegate_status.length == 0 && !delegate_status.id) {
            d_DELEGATION = {
                "name": "DELEGATION",
                "value": "",
                "userId": vm.delegateFromUser
            }
        } else {
            delegate_status[0].userId = vm.delegateFromUser
            d_DELEGATION = delegate_status[0]
        }

        // DELEGATION, DELEGATE_USER
        if (vm.delegateStatus == 1) {
            d_DELEGATION.value = "TRUE"
        } else {
            d_DELEGATION.value = "FALSE"
        }

        // _.map([d_DELEGATE_USER, d_DELEGATION], function(dataObj) {
        APIFactory.saveDelegation(d_DELEGATE_USER, function(response) {
            if (response.status) {
                APIFactory.saveDelegation(d_DELEGATION, function(response_2) {
                    if (response_2.status) {
                        Toaster.saySuccess("Delegation changes updated")

                    } else {
                        Toaster.sayError(response_2.errorMessage)
                    }
                    vm.delegateFromUser = ""
                    vm.delegateToUser = ""
                    vm.delegateStatus = 0
                    loadDelegateSettings()
                })
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })

        // })


    }

    $scope.resetDelegation = function() {
        vm.delegateFromUser = ""
        vm.delegateToUser = ""
        vm.delegateStatus = 0
    }

    //  Super user

    APIFactory.getSettingsList({
        "userId": vm.currentUser.id,
        "isToolAdmin": true
    }, function(response) {
        if (response.status) {
            if (response.data && response.data.length > 0) {
                vm.delegateFromSuperUser = response.data[0].id
            } else {
                $scope.resetSuperUser()
            }
        } else {
            Toaster.sayError(response.errorMessage)
        }
    })

    $scope.resetSuperUser = function() {
        vm.delegateFromSuperUser = ""
    }

    $scope.saveSuperUser = function() {
        if (!vm.delegateFromSuperUser || vm.delegateFromSuperUser == "") {
            Toaster.sayWarning("Choose delegate to name")
            return
        }

        if (vm.currentUser.id == vm.delegateFromSuperUser) {
            Toaster.sayWarning("Delegate from and to cannot be same")
            return
        }

        var SUPER_USER_DELEGATION = {
            "name": "DELEGATE_USER",
            "value": vm.currentUser.id,
            "userId": vm.delegateFromSuperUser
        }
        $scope.delegationInProgress = true
        APIFactory.setSuperUser(SUPER_USER_DELEGATION, function(response) {
            if (response.status) {
                $rootScope.super_delegated_user = vm.delegateFromSuperUser
                // Toaster.saySuccess("Delegation changes updated")
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
    }
})