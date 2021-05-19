dashboard.controller("settingsCtrl", function ($rootScope, $templateCache, appSettings, $scope,
        $state, $location, Toaster, $timeout, $mdDialog, $http, apiService, APIFactory, WFLogger, Paginate) {
    var setting = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;
    var user_info = getUserData("user")

    $scope.cancel = function () {
        $mdDialog.cancel();
    };
    Paginate.refreshScrolling();

    setting.timezoneList = getTimeZonelist()

    setting.selectedTimezone = _.where(setting.timezoneList, {"value": getTimeZone()})[0].value
    $timeout(function () {
        $("#settingTimeZone").select2()
        $(".select2-selection.select2-selection--single").css({
            "border": "1px solid #ccc",
            "border-radius": "0px",
            "background": "none",
            "height": "38px",
            "padding": "10px"
        })
    }, 200)

    $scope.setDefault = function () {
        localStorage.setItem("default_timezone", appSettings.timezone)
        $mdDialog.hide()
    }

    $scope.setTimezone = function () {
        localStorage.setItem("default_timezone", setting.selectedTimezone)
        $mdDialog.hide()
    }

})