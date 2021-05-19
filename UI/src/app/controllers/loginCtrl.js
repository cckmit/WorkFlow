login.controller("loginCtrl", ['$rootScope', '$mdDialog', '$scope', '$state', '$location', '$http', 'appSettings', 'Toaster', 'APIFactory', 'Access', 'WSService', 'Paginate',
    function ($rootScope, $mdDialog, $scope, $state, $location, $http, appSettings, Toaster, APIFactory, Access, WSService, Paginate) {
        var vm = this;
        var appInfo = JSON.parse(localStorage.getItem("appInfo"))
        var apiBase = appSettings.apiBase;
        if (appInfo.isSSOApp) {
            apiBase = appSettings.apiSSOBase;
        }
        Paginate.refreshScrolling();
        $rootScope.apiBase = apiBase;
        vm.login_block = true
        WSService.initPublish()
        WSService.clearLiveTopics()

        function switchToDefaultPage(userRole) {
            Access.getDefaultPages(userRole, function (switch_state) {
                $state.go(switch_state);
            });
        }

        $rootScope.appInfo = JSON.parse(localStorage.getItem("appInfo"))

        var checkUserExists = function () {
            if (localStorage.getItem('userdata') === null) {
                $state.go('login')
            } else {
                $state.go(getUserData("userHome"))
            }
        }
        window.onfocus = function () {
            //            checkUserExists()
        }

        //        function checkForSSO() {
        //            //getUserData("token")
        //            if (getCookie("SMSESSION") == "") {
        //                vm.login_block = true
        //                    // $state.go("login")
        //                checkExistingSession()
        //            } else {
        //                vm.login_block = false
        //                $http({
        //                    method: 'POST',
        //                    url: appSettings.apiBase + APIFactory.ssoLoginURL(),
        //                    headers: appSession
        //                }).success(function(response) {
        //                    if (response.status) {
        //                        switchToLandingPage(response)
        //                    } else {
        //                        Toaster.sayError(response.errorMessage)
        //                        vm.login_block = true
        //                    }
        //                })
        //            }
        //
        //        }
        //
        //        if (localStorage.getItem("401") != "true") {
        //            checkForSSO()
        //        }
        //
        //        function checkExistingSession() {
        //            if (localStorage.getItem('userdata') != null && localStorage.getItem("401") != "true") {
        //                var userRole = getUserData("user").currentRole;
        //                switchToDefaultPage(userRole)
        //            }
        //        }


        var userRole;
        //access login
        vm.login = function (data) {
            $(".btnLogin").attr('disabled', 'disabled');
            $(".btnLogin").html('<i class="fa fa-spinner fa-spin"></i>  Authenticating');
            var req = {
                method: 'POST',
                url: apiBase + APIFactory.loginURL(),
                data: {
                    "username": data.Username.toLowerCase(),
                    "password": data.Password
                }
            };
            doLogin(req, data)
        };

        var roles, userRole;

        function doLogin(req, data) {
            $http(req).success(function (response) {
                if (response.status) {
                    $(".btnLogin").removeAttr('disabled');
                    $(".btnLogin").html('LOGIN');
                    switchToLandingPage(response)
                    getLoginUserDetails(response.data.role);
                } else {
                    $(".btnLogin").removeAttr('disabled');
                    $(".btnLogin").html('LOGIN');
                    Toaster.sayError(response.errorMessage);
                }

            }).error(function () {
                $(".btnLogin").removeAttr('disabled');
                $(".btnLogin").html('LOGIN');
                Toaster.sayError("SignIn Error");
            });
        }

        function getLoginUserDetails(role) {

            APIFactory.getLoginUserDetails({
                "roles": role
            }, function (response) {
                if (response.status && response.data.length > 0) {
                    var message = response.data
                    Toaster.sayStatus(message);
                }
            })

        }
        /**
              * Help Desk Logic Start
              */

        vm.showAdvanced = function (ev) {
            $mdDialog.show({
                controller: DialogController,
                templateUrl: 'html/templates/helpDesk.template.html',
                parent: angular.element(document.body),
                targetEvent: ev,
                clickOutsideToClose: false,

            })
                .then(function (answer) {

                }, function () {

                });
        };

        function DialogController($scope, $mdDialog) {
            $scope.hide = function () {
                $mdDialog.hide();
            };

            $scope.cancel = function () {
                $mdDialog.cancel();
            };

            $scope.answer = function (answer) {
                $mdDialog.hide(answer);
            };
        }
        function switchToLandingPage(response) {
            var defaultData = Paginate.defaultPageValue()
            $rootScope.paginateDefaultValue = defaultData.defaultValue
            $rootScope.paginateValue = defaultData.paginateValue
            Access.clearSearchData();
            roles = response.data.role;
            userRole = response.data.currentRole;
            setUserData(response.data.displayName, response.data.id, userRole, userRole, response.data, response.metaData, roles, response.data.delegated, response.data.delegations, $rootScope.paginateDefaultValue, $rootScope.paginateValue)
            switchToDefaultPage(userRole);
        }
    }
]);