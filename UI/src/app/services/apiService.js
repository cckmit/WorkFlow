app.service('apiService', function ($http, $q, appSettings, Toaster, $location, Upload, $window) {

    var apiService = {};

    var appInfo = JSON.parse(localStorage.getItem("appInfo"))
    var apiBase = appSettings.apiBase;
    var HashCode = " ";

    if (appInfo.isSSOApp) {
        apiBase = appSettings.apiSSOBase;
    }
    var initializeHttp = function () {
        HashCode = Math.random().toString(36).substr(2, 9);
        if (getCookie("SMSESSION") == "" || getUserData("token") != null) {
            $http.defaults.headers.get.Authorization = getUserData("token")
            $http.defaults.headers.post.Authorization = getUserData("token")
        }

        $http.defaults.headers.common.HashCode = HashCode;
        $http.defaults.headers.get['If-Modified-Since'] = 'Mon, 26 Jul 1997 05:00:00 GMT';
        $http.defaults.headers.get['Cache-Control'] = 'no-cache';
        $http.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
        if (getUserData("token") == null) {
            _.extend($http.defaults.headers.get, appSession)
            _.extend($http.defaults.headers.post, appSession)
        }
    }

    //===========================GET RESOURCE==============================
    var get = function (module, parameter, cbk) {
        initializeHttp()
        $http.get(apiBase + module, { params: parameter }, { headers: { 'Content-Type': 'application/json' } }).success(function (response) {
            //                response.data = JSON.parse(JSON.stringify(response.data).replace(/null/ig, '"-"').replace(/""/ig, '"-"'))
            cbk(response)
        }).catch(function (data, status, headers, config) { // <--- catch instead error
            if (data.status == 401) {
                localStorage.setItem("401", "true")
                clearUserData()
                $location.path("/login");
            } else if (data.status == 500) {
                Toaster.sayError("Internal Server Error")
            } else if (data.status == 417) {
                Toaster.sayError("Previous request was in progress")
            }
            else if (apiBase.match("federation")) {
                localStorage.setItem("401", "true");
                clearUserData();
                $window.location.href = apiBase.replace("WorkFlowAPI", "WorkFlow");
            }

            cbk(null)
        });
    };

    //===========================POST RESOURCE==============================
    var post = function (module, parameter, dataObj, cbk) {
        initializeHttp()
        var request = $http({
            method: "POST",
            url: apiBase + module,
            params: parameter,
            data: dataObj,
            headers: { 'Content-Type': 'application/json' }
        }).success(function (response) {
            cbk(response)
        }).catch(function (data, status, headers, config) { // <--- catch instead error
            if (data.status == 401) {
                localStorage.setItem("401", "true")
                clearUserData()
                $location.path("/login");
            } else if (data.status == 500) {
                Toaster.sayError("Internal Server Error")
            } else if (data.status == 417) {
                Toaster.sayError("Previous request was in progress")
            }
            else if (apiBase.match("federation")) {
                localStorage.setItem("401", "true");
                clearUserData();
                $window.location.href = apiBase.replace("WorkFlowAPI", "WorkFlow");
            }
            cbk(null)
        });
    };

    var upload = function (module, parameter, dataObj, cbk) {
        initializeHttp()
        Upload.upload({
            url: apiBase + module,
            data: dataObj,
            arrayKey: ''
        }).then(function (response) {
            cbk(response.data)
        }, function (error) {
            cbk(null)
        });
    };



    apiService.get = get;
    apiService.post = post;
    apiService.upload = upload;

    return apiService;

});