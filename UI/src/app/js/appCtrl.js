app.controller("appCtrl", function ($rootScope, $scope, $state, $http, $location, $timeout, Flash, appSettings, $timeout, $mdDialog, Toaster, APIFactory, WFLogger, Access, WSService) {

    $rootScope.theme = appSettings.theme;
    $rootScope.layout = appSettings.layout;

    var vm = this;
    var appInfo = JSON.parse(localStorage.getItem("appInfo"))
    var apiBase = appSettings.apiBase;
    if (appInfo.isSSOApp) {
        apiBase = appSettings.apiSSOBase;
    }
    // $rootScope.apiBase = appSettings.apiBase;
    $rootScope.apiBase = apiBase;
    vm.userName = getUserData('displayName');
    vm.roleName = getUserData('userRole');
    // vm.roles = getUserData('roles');
    $rootScope.templatePath = "html/templates"
    $rootScope.currentActiveUserId = getUserData("user") ? getUserData("user").id : null;
    vm.local_user_obj = getUserData('user')
    WSService.initPublish()
    $rootScope.appInfo = JSON.parse(localStorage.getItem("appInfo"))
    $rootScope.paginateDefaultValue = JSON.parse(localStorage.getItem("paginateDefaultData"))
    $rootScope.paginateValue = JSON.parse(localStorage.getItem("Pagination_number"))
    //avalilable themes
    vm.themes = [{
        theme: "black",
        color: "skin-black",
        title: "Dark - Black Skin",
        icon: ""
    },
    {
        theme: "black",
        color: "skin-black-light",
        title: "Light - Black Skin",
        icon: "-o"
    },
    {
        theme: "blue",
        color: "skin-blue",
        title: "Dark - Blue Skin",
        icon: ""
    },
    {
        theme: "blue",
        color: "skin-blue-light",
        title: "Light - Blue Skin",
        icon: "-o"
    },
    {
        theme: "green",
        color: "skin-green",
        title: "Dark - Green Skin",
        icon: ""
    },
    {
        theme: "green",
        color: "skin-green-light",
        title: "Light - Green Skin",
        icon: "-o"
    },
    {
        theme: "yellow",
        color: "skin-yellow",
        title: "Dark - Yellow Skin",
        icon: ""
    },
    {
        theme: "yellow",
        color: "skin-yellow-light",
        title: "Light - Yellow Skin",
        icon: "-o"
    },
    {
        theme: "red",
        color: "skin-red",
        title: "Dark - Red Skin",
        icon: ""
    },
    {
        theme: "red",
        color: "skin-red-light",
        title: "Light - Red Skin",
        icon: "-o"
    },
    {
        theme: "purple",
        color: "skin-purple",
        title: "Dark - Purple Skin",
        icon: ""
    },
    {
        theme: "purple",
        color: "skin-purple-light",
        title: "Light - Purple Skin",
        icon: "-o"
    },
    ];

    //available layouts
    vm.layouts = [{
        name: "Boxed",
        layout: "layout-boxed"
    },
    {
        name: "Fixed",
        layout: "fixed"
    },
    {
        name: "Sidebar Collapse",
        layout: "sidebar-collapse"
    },
    ];



    setTimeout(function () {
        $(".content-wrapper").css('height', $(window).height() - 102)
    }, 500)

    function refreshMenus() {
        Access.getDefaultMenus(function (menu_items) {
            vm.menuItems = menu_items;
        });
    }


    $rootScope.initMenus = function () {
        refreshMenus()
        if ($rootScope.super_delegated_user != "" && $rootScope.super_delegated_user != undefined) {
            vm.home_menu = getUserData("user").delegations[$rootScope.super_delegated_user]
            $rootScope.home_menu = vm.home_menu
            vm.minized_menu = []
            _.each(getUserData("user").delegations, function (dObj) {
                if (dObj.id != $rootScope.home_menu.id) {
                    vm.minized_menu.push(dObj)
                }
            })
            vm.minized_menu.push(getUserData("user"))
        } else {
            vm.home_menu = getUserData("user")
            $rootScope.home_menu = vm.home_menu
            vm.minized_menu = []
            _.each(getUserData("user").delegations, function (dObj) {
                vm.minized_menu.push(dObj)
            })
        }
    }
    $rootScope.initMenus()

    $rootScope.super_delegated_user = "";

    WSService.initDelegation(function (reponse_user) {
        var local_user_data = getUserData("user")
        if (reponse_user.id == local_user_data.id) {
            $timeout(function () {
                var userId = local_user_data.id
                if ($rootScope.super_delegated_user != "") {
                    userId = $rootScope.super_delegated_user
                }
                APIFactory.setDelegate({ "userId": userId, "force": true }, function (response) {
                    if (response.status) {
                        if ($rootScope.super_delegated_user != "" || (local_user_data.currentDelegatedUser && Object.keys(response.data.delegations).indexOf(local_user_data.currentDelegatedUser.id) < 0)) {
                            setSelectedUserData("user", response.data)
                            getLoginUserDetails(response.data.role);
                            if ($rootScope.super_delegated_user != "") {
                                $rootScope.currentActiveUserId = userId
                                vm.roleName = getUserData("user").delegations[$rootScope.super_delegated_user].currentRole
                                setSelectedUserData("userRole", vm.roleName)
                            } else {
                                vm.roleName = getUserData("user").currentRole
                                setSelectedUserData("userRole", vm.roleName)
                            }
                            $timeout(function () {
                                $("#roleSelector option").removeAttr("selected");
                                $("#roleSelector option[value='" + vm.roleName + "']").attr("selected", "selected")
                            }, 1000)
                            $rootScope.initMenus()
                            vm.switchToUserRole()
                            Toaster.saySuccess("Delegation changes updated")
                        } else {
                            setSelectedUserData("user", response.data)
                            vm.minized_menu = []
                            _.each(response.data.delegations, function (dObj) {
                                vm.minized_menu.push(dObj)
                            })
                        }
                    } else {
                        Toaster.sayError(response.errorMessage)
                    }
                    $rootScope.super_delegated_user = ""
                })
            }, 1000)
            $rootScope.saveformData()
        }
    })

    WSService.initSessionTimeout(function (response) {
        if (response) {
            $state.go("login");
            Access.clearSearchData();
            $state.reload();
            $rootScope.saveformData()
        }
    });

    vm.switch_user = function (index, status) {
        var currentUserId;
        if (status) {
            currentUserId = index;
            index = _.findIndex(vm.minized_menu, function (elem) { return elem.id == currentUserId; });
        } else {
            currentUserId = vm.minized_menu[index].id;
        }
        APIFactory.setDelegate({
            "userId": currentUserId
        }, function (response) {
            if (response.status) {
                var home_copy = angular.copy(vm.home_menu)
                setSelectedUserData("user", response.data)
                getLoginUserDetails(response.data.role);
                $rootScope.currentActiveUserId = currentUserId

                //Home Menu
                var delegate_response = response.data
                if (delegate_response.currentDelegatedUser == null) {
                    vm.home_menu = delegate_response
                } else {
                    vm.home_menu = delegate_response.currentDelegatedUser
                }
                $rootScope.home_menu = vm.home_menu
                vm.roleName = vm.home_menu.currentRole
                setSelectedUserData("userRole", vm.roleName)

                refreshMenus()
                vm.switchToUserRole()
                //Minimized Menu
                vm.minized_menu[index] = home_copy

            } else {
                Toaster.sayError(response.errorMessage)
            }
        })
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
    // After switch the user shows the current delegate User
    if (getUserData('user').currentDelegatedUser != null) {
        vm.switch_user(getUserData('user').currentDelegatedUser.id, true)
    }

    vm.checkForDefaultUser = function (deleObj) {
        // Check for original user
        // Only for original user, delegations object will not be empty
        return (deleObj != null && Object.keys(deleObj).length > 0) ? true : false;
    }

    // Delegation bug fix
    /* vm.checkIsDelegationUser = function(mmObj) {
        // Check with current role
        // Compare current role with delegated user role
        var currentrole = getUserData('userRole');
        return (mmObj && mmObj != null && mmObj.role.indexOf(currentrole) >= 0) ? true : false;
    } */


    vm.delegate_settings = function (ev) {
        $mdDialog.show({
            controller: "delegationCtrl",
            controllerAs: "dl",
            templateUrl: 'html/templates/delegation.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {}
        })
            .then(function (answer) {

            }, function () {

            });
    }

    $scope.switchTimezone = function (ev) {
        $mdDialog.show({
            controller: "settingsCtrl",
            controllerAs: "setting",
            templateUrl: 'html/templates/settings.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {}
        })
            .then(function (answer) {

            }, function () {

            });
    }



    //set the theme selected
    vm.setTheme = function (value) {
        $rootScope.theme = value;
    };

    //set the Layout in normal view
    vm.setLayout = function (value) {
        $rootScope.layout = value;

    };


    //controll sidebar open & close in mobile and normal view

    vm.sideBar = function (value) {
        if ($(window).width() <= 767) {
            if ($("body").hasClass('sidebar-open'))
                $("body").removeClass('sidebar-open');
            else
                $("body").addClass('sidebar-open');
        } else {
            if (value == 1) {
                if ($("body").hasClass('sidebar-collapse'))
                    $("body").removeClass('sidebar-collapse');
                else
                    $("body").addClass('sidebar-collapse');
            }
        }
    };

    $scope.reportLog = function (ev) {
        $mdDialog.show({
            controller: appLogCtrl,
            controllerAs: "ap",
            templateUrl: 'html/templates/applog.template.html',
            parent: angular.element(document.body),
            targetEvent: ev,
            clickOutsideToClose: false,
            locals: {}
        })
            .then(function (answer) {

            }, function () {

            });

        function appLogCtrl($scope, WFLogger, appSettings) {
            $scope.logContent = WFLogger.getLogs().join("\n")

            $scope.copyTo = function () {

                var copyTextarea = document.querySelector('.app-applog');
                copyTextarea.select();
                try {
                    var copyText = document.execCommand('copy');
                    if (copyText) {
                        alert("Log Copied! Kindly send to development team! Thanks!")
                    } else {
                        alert("Unable to Copy")
                    }
                } catch (err) {
                    alert('Oops, unable to copy');
                }
            }

            $scope.download = function () {
                var blob = new Blob([$scope.logContent], {
                    type: "text/plain;charset=utf-8"
                });
                saveAs(blob, 'Workflow_' + Access.formatAPIDate(moment()) + '_Log.txt');
            }


            $scope.cancel = function () {
                $mdDialog.cancel();
            };
        }
    }
    var arr = [];
    var arr1 = [];
    vm.refreshState = function (state) {
        localStorage.removeItem("scrolling_position");
        localStorage.removeItem("Pagination_number");
        clearInterval($rootScope.interval);
        for (var i = 0; i < localStorage.length; i++) {
            if (localStorage.key(i).substring(0, 12) == 'ActivityLog-') {
                arr.push(localStorage.key(i));
            }
        }
        for (var i = 0; i < arr.length; i++) {
            localStorage.removeItem(arr[i]);
        }
        for (var i = 0; i < localStorage.length; i++) {
            if (localStorage.key(i).substring(0, 7) == 'DepLog-') {
                arr1.push(localStorage.key(i));
            }
        }
        for (var i = 0; i < arr1.length; i++) {
            localStorage.removeItem(arr1[i]);
        }
        $rootScope.paginateValue = 0
        if ($rootScope.currentState == "app." + state || $rootScope.currentState == undefined) {
            Access.clearSearchData();
            $timeout(function () {
                $state.go("app." + state, {}, {
                    reload: "app." + state
                });
            }, 100);
        }
    }

    vm.refreshChildState = function (state) {
        localStorage.removeItem("scrolling_position");
        localStorage.removeItem("Pagination_number");
        clearInterval($rootScope.interval);
        for (var i = 0; i < localStorage.length; i++) {
            if (localStorage.key(i).substring(0, 12) == 'ActivityLog-') {
                arr.push(localStorage.key(i));
            }
        }
        for (var i = 0; i < arr.length; i++) {
            localStorage.removeItem(arr[i]);
        }
        for (var i = 0; i < localStorage.length; i++) {
            if (localStorage.key(i).substring(0, 7) == 'DepLog-') {
                arr1.push(localStorage.key(i));
            }
        }
        for (var i = 0; i < arr1.length; i++) {
            localStorage.removeItem(arr1[i]);
        }
        $rootScope.paginateValue = 0
        if ($rootScope.currentState == "app." + state || $rootScope.currentState == undefined) {
            Access.clearSearchData();
            $timeout(function () {
                $state.go("app." + state, {}, {
                    reload: "app." + state
                });
            }, 10);
        }
    }
	
	vm.refreshSubChildState = function (state) {
        localStorage.removeItem("scrolling_position");
        localStorage.removeItem("Pagination_number");
        clearInterval($rootScope.interval);
        for (var i = 0; i < localStorage.length; i++) {
            if (localStorage.key(i).substring(0, 12) == 'ActivityLog-') {
                arr.push(localStorage.key(i));
            }
        }
        for (var i = 0; i < arr.length; i++) {
            localStorage.removeItem(arr[i]);
        }
        for (var i = 0; i < localStorage.length; i++) {
            if (localStorage.key(i).substring(0, 7) == 'DepLog-') {
                arr1.push(localStorage.key(i));
            }
        }
        for (var i = 0; i < arr1.length; i++) {
            localStorage.removeItem(arr1[i]);
        }
        $rootScope.paginateValue = 0
        if ($rootScope.currentState == "app." + state || $rootScope.currentState == undefined) {
            Access.clearSearchData();
            $timeout(function () {
                $state.go("app." + state, {}, {
                    reload: "app." + state
                });
            }, 10);
        }
    }



    // end of sample

    $rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
        $rootScope.previousState = from.name;
        $rootScope.currentState = to.name;
    });
    $scope.logout = function () {
        $rootScope.super_delegated_user = ""
        setTimeout(function () {
            $(".md-dialog-container,.md-dialog-backdrop,.modal-backdrop").hide()
        }, 200)
        APIFactory.doLogout(function (response) {
            clearUserData();
            $location.path("/login");
        })
    }




    vm.switchRole = function () {
        var userdata = JSON.parse(localStorage.getItem('userdata'));
        APIFactory.setRole({
            "role": vm.roleName
        }, function (response) {
            if (response.status) {
                $rootScope.deltaUser = userdata.user.deltaEmployee
                setSelectedUserData("user", response.data)
                setSelectedUserData("userRole", vm.roleName)
                $rootScope.currentRole = getUserData('userRole')
                vm.local_user_obj = getUserData('user')
                refreshMenus()
                vm.switchToUserRole()
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })

        setTimeout(function () {
            var coll = document.getElementsByClassName("collapsible");
            var i;

            for (i = 0; i < coll.length; i++) {
                coll[i].addEventListener("click", function () {
                    this.classList.toggle("activated");
                    var content = this.nextElementSibling;
                    if (content.style.maxHeight) {
                        content.style.maxHeight = null;
                    } else {
                        content.style.maxHeight = "300px";
                    }
                })
            }

        }, 2000)
    }

    vm.switchToUserRole = function () {
        Access.clearSearchData();
        Access.getDefaultPages(vm.roleName, function (switch_state) {
            if (localStorage.getItem("previousState") != null && getUserData('userRole') == localStorage.getItem("previousRole")) {
                $state.go(localStorage.getItem("previousState"), {}, {
                    reload: localStorage.getItem("previousState")
                });
            } else {
                $state.go(switch_state, {}, {
                    reload: switch_state
                });
                localStorage.removeItem("scrolling_position");
                localStorage.removeItem("Pagination_number");
                clearInterval($rootScope.interval);
                $rootScope.paginateValue = 0
                for (var i = 0; i < localStorage.length; i++) {
                    if (localStorage.key(i).substring(0, 12) == 'ActivityLog-') {
                        arr.push(localStorage.key(i));
                    }
                }
                for (var i = 0; i < arr1.length; i++) {
                    localStorage.removeItem(arr1[i]);
                }

                for (var i = 0; i < localStorage.length; i++) {
                    if (localStorage.key(i).substring(0, 7) == 'DepLog-') {
                        arr1.push(localStorage.key(i));
                    }
                }
                for (var i = 0; i < arr1.length; i++) {
                    localStorage.removeItem(arr1[i]);
                }
            }
            localStorage.setItem("previousRole", getUserData('userRole'))
        });
    }
    if (localStorage.getItem("previousRole") != null) {
        var userData = JSON.parse(localStorage.getItem('userdata'))
        var allRoles = userData.roles
        var roleName = localStorage.getItem("previousRole");
        if (getUserData("user").currentDelegatedUser != null) {
            roleName = getUserData("user").currentDelegatedUser.currentRole
            allRoles = getUserData("user").currentDelegatedUser.role
        }
        if (allRoles.indexOf(roleName) >= 0) {
            vm.roleName = roleName;
            vm.switchRole()
        }
    }
});
app.factory('$exceptionHandler', ['$injector', function ($injector, $rootScope) {
    var WFLogger;
    return function (exception, cause) {
        $('.errorInicator').removeClass("hide").addClass("blink")
        $('.errorInicator').show()
        setTimeout(function () {
            $('.errorInicator').removeClass("blink")
            $('.errorInicator').hide()
        }, 5000);
        WFLogger = WFLogger || $injector.get('WFLogger');
        WFLogger.ERROR(exception);
    };
}]);
app.directive("hasAccess", function () {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attr, model) {
            var accessSettings = {
                "Active": ["edit", "submit", "createimplementation", "reject"],
                "Submitted": ["reject"],
                "Approved": ["reject"],
                "Deployed in QA - Functional": ["reject"],
                "Passed Functional Testing": ["reject"],
                "Deployed in Regression": ["reject"],
                "Passed Regression Testing": ["reject"],
                "Deployed in Pre-Production": ["reject"],
                "Passed Acceptance Testing": ["reject"],
                "Ready for Production Deployment": ["reject"]
            }

            if (accessSettings[attr.status]) {
                if (accessSettings[attr.status].indexOf(attr.action) >= 0) {
                    $(element).addClass("show")
                } else {
                    $(element).addClass("hide")
                }
            } else {
                $(element).addClass("hide")
            }
        }
    }
});

app.directive("planActionControl", function ($rootScope, Access) {
    return function (scope, element, attr, model) {
        var temp = Access.getPlanActionByRoleForLabel();
        if (temp[attr.status])
            if ((temp[attr.status].ACCESS_PERMISSIONS.indexOf(attr.action) >= 0) && attr.username == $rootScope.home_menu.id) {
                $(element).show()
            } else {
                $(element).hide()
            }
    }
});

app.directive('onlyDigits', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                if (inputValue == undefined) return '';
                var transformedInput = inputValue.replace(/[^0-9]/g, '');
                if (transformedInput !== inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }
                return transformedInput;
            });
        }
    };
});
app.directive('pressEnterKey', function () {
    return function (scope, element, attrs) {
        element.bind("keydown keypress", function (event) {
            if (event.which === 13) {
                scope.$apply(function () {
                    scope.$eval(attrs.pressEnterKey);
                });
                event.preventDefault();
            }
        });
    };
})

app.directive('removeDotsFromFile', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                if (inputValue == undefined) return '';
                var transformedInput = inputValue.replace(/[^a-zA-Z0-9_.]/g, '');
                if (transformedInput !== inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }
                return transformedInput;
            });
        }
    };
});

app.directive('alphaNumericLowerCaseWithoutSpace', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                if (inputValue == undefined) return '';
                var transformedInput = inputValue.replace(/[^a-z0-9]/g, '');
                // transformedInput.replace(/>\s+</g,'><');
                if (transformedInput !== inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }
                return transformedInput
            });
        }
    };
});

app.directive('filterNumberAndDash', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                if (inputValue == undefined) return '';
                var transformedInput = inputValue.replace(/[^0-9_]/g, '');
                if (transformedInput !== inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }
                return transformedInput;
            });
        }
    };
});

app.directive('filterNumberAndDot', function () {
    return {
        restrict: 'A',
        require: '?ngModel',
        link: function (scope, element, attrs, modelCtrl) {
            modelCtrl.$parsers.push(function (inputValue) {
                if (inputValue == undefined) return '';
                var transformedInput = inputValue.replace(/[^0-9.]/g, '');
                if (transformedInput !== inputValue) {
                    modelCtrl.$setViewValue(transformedInput);
                    modelCtrl.$render();
                }
                return transformedInput;
            });
        }
    };
});

app.filter('removeSeconds', function (appSettings) {
    return function (str) {
        var regExpr1 = /\d{2}:\d{2}/g
        var regExpr2 = /\d{2}-\d{2}-\d{4} \d{2}:\d{2}/g
        if (moment(str, appSettings.uiDTFormat, true).isValid()) {
            //Check for datetime format
            return regExpr2.exec(str)[0]
        }
        return regExpr1.exec(str)[0];
    }
});

app.filter('replaceSeconds', function (appSettings) {
    return function (str) {
        return str.replace(/:00$/, "")
    }
});
app.filter('nCheck', function (appSettings) {
    return function (str) {
        return str ? true : false;
    }
});
app.filter('emptyCheck', function (appSettings) {
    return function (str) {
        return str ? str : "-";
    }
});
app.filter('dCheck', function (appSettings, Access) {
    return function (str) {
        return str ? Access.refactorTimeZone(Access.formatUIDate(moment(str))) : "-";
    }
});
app.filter('timeSlot', function (appSettings, Access) {
    return function (str) {
        return moment(Access.formatUIDate(moment(str)), appSettings.uiDTFormat).format(appSettings.timeFormat);
        // moment(str).tz(getTimeZone()).format(appSettings.timeFormat);
    }
});
app.filter('getTimeslot', function (appSettings, Access) {
    return function (str) {
        return str ? str.split(" ")[1].slice(0, -3) : str
    }
});
app.filter('trimStr', function () {
    return function (str) {
        return $.trim(str);
    }
});

app.filter('formattedDateTime', function (appSettings, Access) {
    return function (str) {
        if (!str) {
            return "Not updated"
        }
        if (str == "-") {
            return "-"
        }
        return Access.refactorTimeZone(Access.formatUIDate(moment(str)));
    }
});
app.filter('convertToformattedDateTime', function (appSettings, Access) {
    return function (str) {
        return Access.refactorTimeZone(Access.formatUIDate(moment(str, "YYYY/MM/DD HH:mm:ss")));
    }
});
app.filter('formattedDateTimeWithoutSeconds', function (appSettings, Access) {
    return function (str) {
        if (!str) {
            return "Not updated"
        }
        return Access.refactorTimeZone(Access.formatUIDate(moment(str)).replace(":00 ", " "));
    }
});
app.filter('shrinkTimezone', function (appSettings, Access) {
    return function (str) {
        return Access.refactorTimeZone(str);
    }
});
app.filter('splitMak', function (appSettings) {
    return function (str) {
        return str.split(".")[0];
    }
});
app.filter('getRepoName', function (appSettings, Access) {
    return function (str) {
        var splitttedRepo = str.split("/")
        return splitttedRepo[splitttedRepo.length - 1].replace(".git", "").toUpperCase()
    }
});
app.filter('getDateOnly', function (appSettings, Access) {
    return function (val) {
        return val.split(" ")[0]
    }
});

// app.filter('getIDOnly', function (appSettings, Access) {
// return function (val) {
// return val.split("-")[1]
// }
// });
app.filter('stateformat', function () {
    return function (input) {
        return input.replace(/_/g, " ")
    }
});
app.directive("planStatusLabel", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            var allPlanStatus = Access.getAllPlanStatus()
            var temp = Access.getPlanActionByRole();
            if (temp[attrs.planStatusLabel].LABEL_TYPE) {
                $(element).addClass("label " + temp[attrs.planStatusLabel].LABEL_TYPE)
            } else {
                $(element).addClass("label label-primary")
            }
            if (attrs.planStatusLabel.length > 15) {
                $(element).addClass("label-fix flex-justify")
            }
            var str = attrs.planStatusLabel
            $(element).addClass("toUpper")
            if (allPlanStatus[str]) {
                $(element).html(allPlanStatus[str])
            } else {
                $(element).html(str)
            }
        }
    }
});
app.directive("planStatusToDisplayLabel", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            var allPlanStatus = Access.getAllPlanStatusForLabel()
            var temp = Access.getPlanActionByRoleForLabel();
            if (temp[attrs.planStatusToDisplayLabel] && temp[attrs.planStatusToDisplayLabel].LABEL_TYPE) {
                $(element).addClass("label " + temp[attrs.planStatusToDisplayLabel].LABEL_TYPE)
            } else {
                $(element).addClass("label label-default")
            }
            if (attrs.planStatusToDisplayLabel.length > 15) {
                $(element).addClass("label-fix flex-justify")
            }
            var str = attrs.planStatusToDisplayLabel
            $(element).addClass("toUpper")
            if (allPlanStatus[str]) {
                $(element).html(allPlanStatus[str])
            } else {
                $(element).html(str)
            }
        }
    }
});
app.directive("planStatusToDisplayLabelForAdvancedSearch", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            var allPlanStatus = Access.getPlanStatusListForLabelForAdvancedSearch()
            var temp = Access.getPlanActionByRoleForLabelForAdvancedSearch();
            if (temp[attrs.planStatusToDisplayLabelForAdvancedSearch] && temp[attrs.planStatusToDisplayLabelForAdvancedSearch].LABEL_TYPE) {
                $(element).addClass("label " + temp[attrs.planStatusToDisplayLabelForAdvancedSearch].LABEL_TYPE)
            } else {
                $(element).addClass("label label-default")
            }
            if (attrs.planStatusToDisplayLabelForAdvancedSearch.length > 15) {
                $(element).addClass("label-fix flex-justify")
            }
            var str = attrs.planStatusToDisplayLabelForAdvancedSearch
            $(element).addClass("toUpper")
            if (allPlanStatus[str]) {
                $(element).html(allPlanStatus[str])
            } else {
                $(element).html(str)
            }
        }
    }
});
app.directive("planStatusToDisplayLabelForSourceArtifact", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            var allPlanStatus = Access.getAllPlanStatusForLabel()
            var temp = Access.getPlanActionByRoleForLabel();
            if (temp[attrs.planStatusToDisplayLabelForSourceArtifact] && temp[attrs.planStatusToDisplayLabelForSourceArtifact].LABEL_TYPE) {
                $(element).addClass("label " + temp[attrs.planStatusToDisplayLabelForSourceArtifact].LABEL_TYPE)
            } else {
                $(element).addClass("label label-warning")
            }
            if (attrs.planStatusToDisplayLabelForSourceArtifact.length > 15) {
                $(element).addClass("label-fix flex-justify")
            }
            var str = attrs.planStatusToDisplayLabelForSourceArtifact
            $(element).addClass("toUpper")
            if (allPlanStatus[str]) {
                $(element).html(allPlanStatus[str])
            } else {
                $(element).html(str)
            }
        }
    }
});
app.directive("qaStatusLabel", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            // var allQAStatus = Access.getAllSystemQAStatus()
            $(element).addClass("label label-success toUpper")
            if (attrs.qaStatusLabel && attrs.qaStatusLabel.length > 15) {
                $(element).addClass("label-fix flex-justify")
            }
            $(element).html(attrs.qaStatusLabel)
        }
    }
});
app.directive("impStatusLabel", function (Access, WFLogger) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            // var impStatus = {
            //     "IN_PROGRESS": "label label-warning",
            //     "READY_FOR_QA": "label label-success"
            // }
            // if (impStatus[attrs.impStatusLabel]) {
            var temp = Access.getImpActionByRole();
            if (attrs.impStatusLabel.length > 0 && temp[attrs.impStatusLabel].LABEL_TYPE) {
                $(element).addClass("label " + temp[attrs.impStatusLabel].LABEL_TYPE)
            } else {
                $(element).addClass("label label-primary")
            }
            // var str = attrs.impStatusLabel
            // $(element).html(str.replace(/_/g, " "))
        }
    }
});

app.directive("showOwnerFormat", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            if (attrs.showOwnerFormat) {
                var result = attrs.showOwnerFormat.replace(/[^A-Za-z,]/g, "");
                $(element).html(result)
                $(element).addClass("work-break")
            } else {
                $(element).html("-")
            }
        }
    }
});

app.directive("planStatusToPutlevelLabel", function (Access) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            var allPlanStatus = ["INITIAL", "PRE_PROD_CO_EXIST", "DEVELOPMENT", "LOCKDOWN", "PROD_CO_EXIST", "PRODUCTION", "BACKUP", "ARCHIVE", "INACTIVE"];
            var temp = Access.getPlanActionByRoleForLabel();
            var temp = {

                "INITIAL": {
                    "LABEL_TYPE": ["label label-primary"]
                },
                "PRE_PROD_CO_EXIST": {
                    "LABEL_TYPE": ["label label-success"]
                },
                "DEVELOPMENT": {
                    "LABEL_TYPE": ["label label-success"]
                },
                "LOCKDOWN": {
                    "LABEL_TYPE": ["label label-success"]
                },
                "PROD_CO_EXIST": {
                    "LABEL_TYPE": ["label label-success"]
                },
                "PRODUCTION": {
                    "LABEL_TYPE": ["label label-success"]
                },
                "BACKUP": {
                    "LABEL_TYPE": ["label label-danger"]
                },
                "ARCHIVE": {
                    "LABEL_TYPE": ["label label-danger"]
                },
                "INACTIVE": {
                    "LABEL_TYPE": ["label label-info"]
                }
            }
            // && temp[attrs.planStatusToPutlevelLabel].LABEL_TYPE
            if (temp[attrs.planStatusToPutlevelLabel] && temp[attrs.planStatusToPutlevelLabel].LABEL_TYPE) {
                $(element).addClass("label " + temp[attrs.planStatusToPutlevelLabel].LABEL_TYPE)
            } else {
                $(element).addClass("label label-default")
            }
            if (attrs.planStatusToPutlevelLabel == "PRE_PROD_CO_EXIST") {
                $(element).addClass("label label-success")
            }
            else if (attrs.planStatusToPutlevelLabel == "DEVLOPMENT") {
                $(element).addClass("label label-success")
            } else if (attrs.planStatusToPutlevelLabel == "LOCKDOWN") {
                $(element).addClass("label label-primary")
            } else if (attrs.planStatusToPutlevelLabel == "PROD_CO_EXIST") {
                $(element).addClass("label label-primary")
            } else if (attrs.planStatusToPutlevelLabel == "PRODUCTION") {
                $(element).addClass("label label-success")
            } else if (attrs.planStatusToPutlevelLabel == "BACKUP") {
                $(element).addClass("label label-default")
            }
            else if (attrs.planStatusToPutlevelLabel == "ARCHIVE") {
                $(element).addClass("label label-primary")
            }
            else if (attrs.planStatusToPutlevelLabel == "INACTIVE") {
                $(element).addClass("label label-default")
            }
            else if (attrs.planStatusToPutlevelLabel == "INITIAL") {
                $(element).addClass("label label-primary")
            }

            var str = attrs.planStatusToPutlevelLabel
            $(element).addClass("toUpper")
            if (allPlanStatus[str]) {
                $(element).html(allPlanStatus[str])
            } else {
                $(element).html(str)
            }
        }
    }
});
app.directive("impSubStatusLabel", function (Access, WFLogger) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            // var impSubStatus = {
            //     "PEER_REVIEW_COMPLTED": "label label-warning",
            //     "INTEGRATION_TESTING_COMPLETED": "label label-success",
            //     "UNIT_TESTING_COMPLETED": "label label-primary"
            // }
            // if (impSubStatus[attrs.impSubStatusLabel]) {
            var temp = Access.getImpActionByRole();
            if (temp[attrs.impSubStatusLabel]) {
                $(element).addClass("label " + temp[attrs.impSubStatusLabel].LABEL_TYPE)
            } else {
                $(element).addClass("label label-primary")
            }
        }
    }
});

app.directive("buildStatus", function (WFLogger) {
    return {
        restrict: "EA",
        scope: true,
        link: function (scope, element, attrs, model) {
            var buildStatus = {
                "C": "label label-warning",
                "S": "label label-success",
                "F": "label label-danger"
            }
            var statusExtension = {
                "C": "CANCELLED",
                "S": "SUCCESS",
                "F": "FAILED"
            }
            if (buildStatus[attrs.buildStatusLabel]) {
                $(element).addClass("label " + buildStatus[attrs.buildStatusLabel])
                $(element).html(statusExtension[attrs.buildStatusLabel])
            } else {
                $(element).addClass("label label-warning")
                $(element).html(statusExtension[attrs.buildStatusLabel] ? statusExtension[attrs.buildStatusLabel] : "Idle")
            }

        }
    }
});
app.directive("exCo", function (WFLogger) {
    return {
        restrict: 'A',
        link: function (scope, element) {
            $(element).on("click", function () {
                if ($(this).html() === "+") {
                    $(this).html("-")
                } else {
                    $(this).html("+")
                }
            });
        }
    }
});
app.directive('disablefield', function () {
    return function (scope, element, attr) {

        scope.$watch("formDisabled", function (newValue, oldValue) {
            // Plan level fields
            if (scope.formDisabled) {
                $(element).attr("disabled", "true")
                $(".input-addition-btn").css("display", "none")
            }

            //Status dropdown
            if (attr.planDisable && attr.planDisable == "N") {
                $(element).removeAttr("disabled")
            }
            if (attr.planDisable && attr.planDisable == "Y") {
                $(element).attr("disabled", "true")
            }
            //System level fields
            if (attr.systemDisable == "N" && getUserData('userRole') !== "QA") {
                $(element).removeAttr("disabled")
                $(".input-addition").css("display", "table-cell")
            }

            if (attr.id == "relatedPlan" && getUserData('userRole') === "QA") {
                $(element).tagit({ readOnly: true });
            }

            // If macro header is enabled
            if (attr.macroheaderdisableindicator == "true") {
                $(element).attr("disabled", "true")
            }
        });

    }

})
app.directive('loadsCategoryFormDisable', function () {
    return function (scope, element, attr) {
        if (attr.lcFormDisable == "true") {
            $(element).attr("disabled", "true")
        }


    }
})