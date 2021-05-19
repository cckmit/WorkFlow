dashboard.controller("delegationCtrl", function($rootScope, $templateCache, appSettings, $scope, $timeout,
    $state, $location, Toaster, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate) {
    var dl = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;
    var user_info = getUserData("user")

    Paginate.refreshScrolling();
    $scope.cancel = function() {
        $mdDialog.cancel();
    };
    dl.delegateStatus = 0
    dl.delegation_disabled = false
    APIFactory.getSettingsList({}, function(response) {
        if (response.status) {
            dl.delegateSettingsList = response.data
            var delegate_user = _.where(dl.delegateSettingsList, { "userId": user_info.id, "name": "DELEGATE_USER" })
            if (delegate_user.length > 0) {
                dl.delegateUser = delegate_user[0].value
            }
            var delegate_status = _.where(dl.delegateSettingsList, { "userId": user_info.id, "name": "DELEGATION" })
            if (delegate_status.length > 0) {
                if (delegate_status[0].value == "TRUE") {
                    dl.delegateStatus = 1
                    dl.delegation_disabled = true
                } else {
                    dl.delegateStatus = 0
                }
            }
            $timeout(function() {
                $("#delegateUsersList").select2()
                $(".select2-selection.select2-selection--single").css({
                    "border": "1px solid #ccc",
                    "border-radius": "0px",
                    "background": "none",
                    "height": "38px",
                    "padding": "10px"
                })
            }, 200)
            $rootScope.saveformData()
        } else {}
    })

    APIFactory.getDelegationToUsersList(function(response) {
        if (response.status) {
            dl.delegateUsersList = response.data
            $rootScope.saveformData()
        } else {
            dl.delegateUsersList = []
        }
    })

    $scope.saveDelegation = function() {
        if (!dl.delegateUser) {
            Toaster.sayWarning("Choose delegate name")
            return
        }

        var delegate_user = _.where(dl.delegateSettingsList, { "userId": user_info.id, "name": "DELEGATE_USER" })
        var delegate_status = _.where(dl.delegateSettingsList, { "userId": user_info.id, "name": "DELEGATION" })

        var selected_user = _.where(dl.delegateUsersList, { "id": dl.delegateUser })[0]
        var d_DELEGATE_USER, d_DELEGATION;
        if (delegate_user.length == 0 && !delegate_user.id) {
            d_DELEGATE_USER = {
                "name": "DELEGATE_USER",
                "value": selected_user.id,
                "userId": user_info.id
            }
        } else {
            delegate_user[0].value = selected_user.id
            delegate_user[0].userId = user_info.id
            d_DELEGATE_USER = delegate_user[0]
        }
        if (delegate_status.length == 0 && !delegate_status.id) {
            d_DELEGATION = {
                "name": "DELEGATION",
                "value": "",
                "userId": user_info.id
            }
        } else {
            delegate_status[0].userId = user_info.id
            d_DELEGATION = delegate_status[0]
        }

        // DELEGATION, DELEGATE_USER
        if (dl.delegateStatus == 1) {
            d_DELEGATION.value = "TRUE"
        } else {
            d_DELEGATION.value = "FALSE"
        }

        // _.map([d_DELEGATE_USER, d_DELEGATION], function(dataObj) {
        APIFactory.saveDelegation(d_DELEGATE_USER, function(response) {
                if (response.status) {
                    APIFactory.saveDelegation(d_DELEGATION, function(response_2) {
                        if (response_2.status) {
                            var local_user = getUserData("user")
                            if (d_DELEGATION.value == "TRUE") {
                                local_user.delegated = true
                                setSelectedUserData("user", local_user)
                                Toaster.saySuccess("Delegation Success")
                            } else {
                                local_user.delegated = false
                                setSelectedUserData("user", local_user)
                            }
                        } else {
                            Toaster.sayError(response_2.errorMessage)
                        }
                        $state.reload()
                        $mdDialog.cancel();
                    })
                } else {
                    Toaster.sayError(response.errorMessage)
                }
            })
            // })
    }

})