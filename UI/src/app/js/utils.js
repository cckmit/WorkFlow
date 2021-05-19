var ctx = "/WorkFlow"
var HOST = window.location.protocol + "//" + window.location.host + ctx + "/";



var roleJson = {
    "Lead": {
        "role": "ad-adm-devops"
    },
    "Developer": {
        "role": "ad-dev-devops"
    },
    "Reviewer": {
        "role": "ad-pr-devops"
    }
}

var showModal = function (errorType, msg) {
    $(".sweIcon").removeClass().addClass("sweIcon")
    var sweIcon = {
        "success": "fa fa-check-circle fa-4x",
        "error": "fa fa-times-circle-o fa-4x",
        "warning": "fa fa-exclamation-triangle fa-4x"
    }
    var sweColor = {
        "success": "#4CAF50",
        "error": "#EF5350",
        "warning": "#FB8C00"
    }
    $(".sweContent").css("background", sweColor[errorType])
    $(".sweIcon").addClass(sweIcon[errorType])
    $(".sweModal-content").html(msg)
    $('#sweModal').modal({
        backdrop: 'static',
        keyboard: true
    })
    return errorType;
}

var companyName = {
    "T": "T4",
    "D": "DL"
}

var tday = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
var tmonth = new Array("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

function GetClock() {
    $(".liveTime").html(moment().tz(getTimeZone()).format('dddd, MMMM Do YYYY, HH:mm:ss z'));
}

function getTimeZonelist() {
    return [{
        "timezone": "GMT",
        "value": "GMT"
    }, {
        "timezone": "EST/EDT",
        "value": "EST5EDT"
    }, {
        "timezone": "MST/MDT",
        "value": "MST7MDT"
    }, {
        "timezone": "IST",
        "value": "Asia/Kolkata"
    }, {
        "timezone": "CST/CDT",
        "value": "CST6CDT"
    }, {
        "timezone": "PST/PDT",
        "value": "PST8PDT"
    }];
}

function getTimeZone() {
    if (localStorage.getItem("default_timezone")) {
        var timezone = localStorage.getItem("default_timezone")
        if (_.where(getTimeZonelist(), { "value": timezone })[0]) {
            return timezone;
        } else {
            return appConfig.timezone
        }
    } else {
        return appConfig.timezone
    }
}

function setUserData(displayName, userName, userRole, userHome, user, token, roles, delegation_status, delegations, paginateData, paginateValueData) {
    var userDataJson = {
        "displayName": displayName,
        "userName": userName,
        "userRole": userRole,
        "userHome": userHome,
        "user": user,
        "token": token,
        "roles": roles,
        "delegation_status": delegation_status,
        "delegations": delegations
    }
    localStorage.setItem('userdata', JSON.stringify(userDataJson))
	var paginateDefaultData = paginateData;
	var paginateValue = paginateValueData;
	localStorage.setItem('Pagination_number', JSON.stringify(paginateValue))
	localStorage.setItem('paginateDefaultData', JSON.stringify(paginateDefaultData))
}

function getUserData(typeOfData) {
    try {
        var userDataJson = JSON.parse(localStorage.getItem('userdata'))
        switch (typeOfData) {
            case "displayName":
                return userDataJson.displayName;
            case "userName":
                return userDataJson.userName;
            case "userRole":
                return userDataJson.userRole;
            case "userHome":
                return userDataJson.userHome;
            case "user":
                return userDataJson.user;
            case "token":
                return userDataJson.token;
            case "roles":
                return userDataJson.roles;
            case "delegation_status":
                return userDataJson.delegation_status;
            case "delegations":
                return userDataJson.delegations;
        }
    } catch (err) {
    }
}

function setSelectedUserData(key, value) {
    var userDataJson = JSON.parse(localStorage.getItem('userdata'))
    userDataJson[key] = value
    localStorage.setItem('userdata', JSON.stringify(userDataJson))
}

function setUserPrefs(key, value) {
    var userPrefs = {}
    if (localStorage.getItem("userPrefs")) {
        userPrefs = JSON.parse(localStorage.getItem("userPrefs"))
        userPrefs[key] = value;
        localStorage.setItem("userPrefs", JSON.stringify(userPrefs))
    } else {
        localStorage.setItem("userPrefs", JSON.stringify(userPrefs))
        setUserPrefs(key, value)
    }
}

function getUserPrefs(key) {
    if (localStorage.getItem('userPrefs')) {
        return JSON.parse(localStorage.getItem('userPrefs'))[key]
    } else {
        setUserPrefs("loadWindowType", 1)
        getUserPrefs(key)
    }

}
	var arr = [];
	var arr1 = [];
function clearUserData() {
    localStorage.removeItem("iPlans")
    localStorage.removeItem('userdata')
	localStorage.removeItem('paginateDefaultData')
	localStorage.removeItem('Pagination_number')
	localStorage.removeItem('scrolling_position')
	for (var i = 0; i < localStorage.length; i++){
			if (localStorage.key(i).substring(0,12) == 'ActivityLog-') {
				arr.push(localStorage.key(i));
			}
		}
		for (var i = 0; i < arr.length; i++) {
			localStorage.removeItem(arr[i]);
		}
		for (var i = 0; i < localStorage.length; i++){
			if (localStorage.key(i).substring(0,7) == 'DepLog-') {
				arr1.push(localStorage.key(i));
			}
		}
		for (var i = 0; i < arr1.length; i++) {
			localStorage.removeItem(arr1[i]);
		}
}

function replaceNulls(jsonData) {
    return JSON.parse(JSON.stringify(jsonData).replace(/null/ig, '"-"').replace(/""/ig, '"-"').replace(/undefined/ig, '"-"'))
}

function implementationPlanStatus() {
    // return ["ACTIVE", "SUBMITTED", "APPROVED", "PASSED_FUNCTIONAL_TESTING", "PARTIAL_REGRESSION_TESTING", "PASSED_REGRESSION_TESTING", "BYPASSED_REGRESSION_TESTING", "PASSED_ACCEPTANCE_TESTING", "READY_FOR_PRODUCTION_DEPLOYMENT", "REJECTED", "ONLINE", "FALLBACK"]
    return ["ACTIVE", "SUBMITTED", "APPROVED", "DEPLOYED_IN_QA_FUNCTIONAL", "PASSED_FUNCTIONAL_TESTING", "BYPASSED_FUNCTIONAL_TESTING", "PARTIAL_FUNCTIONAL_TESTING", "PARTIAL_REGRESSION_TESTING", "DEPLOYED_IN_QA_REGRESSION", "PASSED_REGRESSION_TESTING", "BYPASSED_REGRESSION_TESTING", "DEPLOYED_IN_PRE_PRODUCTION", "PASSED_ACCEPTANCE_TESTING", "DEV_MGR_APPROVED", "READY_FOR_PRODUCTION_DEPLOYMENT", "DEPLOYED_IN_PRODUCTION", "PENDING_FALLBACK", "ACCEPTED_IN_PRODUCTION", "REJECTED", "ONLINE", "FALLBACK_DEPLOYED_IN_PRODUCTION", "FALLBACK", "PARTIALLY_DEPLOYED_IN_PRODUCTION"]
}
function getDeploumentStatus() {
    return ["DEV_MGR_APPROVED", "READY_FOR_PRODUCTION_DEPLOYMENT"]
}

function clearDTObject(tableId) {
    $("#" + tableId).DataTable().clear()
    $("#" + tableId).DataTable().destroy()
    $("#" + tableId + " tbody").unbind('click');
}

function base64ToArrayBuffer(base64) {

    try {
        var binaryString = window.atob(base64);
        var binaryLen = binaryString.length;
        var bytes = new Uint8Array(binaryLen);
        for (var i = 0; i < binaryLen; i++) {
            var ascii = binaryString.charCodeAt(i);
            bytes[i] = ascii;
        }
        return bytes;
    } catch (err) {
        // console.log(err)
    }
}

function switchArrow(e) {
    alert(e)
    if ($(e).attr("class") === "fa fa-arrow-circle-right") {
        $(e).attr("class", "fa fa-arrow-circle-down")
    } else {
        $(e).attr("class", "fa fa-arrow-circle-right")
    }
}

function showLoading(showLoading) {
    if (showLoading) {
        $(".navAnimate").css({
            'background-size': '50% 100%',
            '-webkit-animation-name': 'moving-gradient'
        })
    } else {
        $(".navAnimate").css({
            'background-size': '100%',
            '-webkit-animation-name': 'dummy'
        })
    }
}

function initMultipleSelect2(id) {
    $.fn.select2.amd.require(['select2/selection/search'], function (Search) {
        var oldRemoveChoice = Search.prototype.searchRemoveChoice;

        Search.prototype.searchRemoveChoice = function () {
            oldRemoveChoice.apply(this, arguments);
            this.$search.val('');
        };

        $(id).select2();

        setTimeout(function () {
            $(".select2.select2-container.select2-container--default").addClass('form-control')
            $(".multipleSelect2 .select2.select2-container.select2-container--default").css({
                "width": "100%",
                "padding": "0px",
                "height": "auto"
            })
            $(".select2-container--default.select2-container--focus .select2-selection--multiple").parent().parent().css({ "height": "auto" })
            $(".select2-selection.select2-selection--multiple").addClass('select2Enhance')
        }, 50)
    });

}

function initSingleSelect2(id) {

    $(id).select2();
    setTimeout(function () {
        $(".singleSelect2 .select2-container--default .select2-selection--single, .select2-selection .select2-selection--single").css({
            "border": "0px",
            "padding": "2px",
            "background": "none"
        })
    }, 50)

}

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function escapeSpecialChars(jsonString) {
    return jsonString.replace(/GALILEO\\/g, "");
    /* return jsonString.replace(/\\n/g, "\/n")
     .replace(/\\r/g, "\/r")
     .replace(/\\t/g, "\/t")
     .replace(/\\f/g, "\/f"); */
}
