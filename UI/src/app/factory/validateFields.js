app.factory('fImplementationPlanValidate', function (Toaster, $http, WFLogger) {
    return {
        validateImpFields: function (data) {
            var validationFailed = false;
            var errorFieldName;
            var dataFields = {
                "planId": "Provide Plan ID",
                "impStatus": "Provide Implementation Status",
                "devId": "Select Developer Name",
                "devContact": "Provide Developer phone number",
                "devLocation": "Provide Developer Location",
                "impDesc": "Provide Implementation Description",
                "peerReviewers": "Select Peer Reviewers"
                //                        "productionVerification": "Choose Production Verification"
            }

            for (d in dataFields) {
                if (d == "peerReviewers" && data.bypassPeerReview) {
                    continue;
                }
                if (d === "devContact") {
                    if (!data[d] || _.isEmpty(data[d])) {
                        errorFieldName = dataFields[d]
                        validationFailed = true;
                        break;
                    }
                    var pattern = new RegExp(/[0-9]{10,12}$/)
                    if (!pattern.test($.trim(data[d]))) {
                        Toaster.sayWarning("Phone number should be like (130) 312-3456-32")
                        return false;
                    }
                }
                if (!data[d] || _.isEmpty(data[d])) {
                    errorFieldName = dataFields[d]
                    validationFailed = true;
                    break;
                }
            }
            if (validationFailed) {
                Toaster.sayWarning(errorFieldName)
                return false;
            }
            //                Toaster.saySuccess("Validation Success")
            return true;

        },
        // checking for minimum validation in implementation creation
        validateImpMandatoryFields: function (data) {
            var validationFailed = false;
            var errorFieldName;
            var dataFields;
            dataFields = {
                "impDesc": "Provide Implementation Description",
                "peerReviewers": "Select Peer Reviewers",
                "devId": "Select Developer Name"
            }

            for (d in dataFields) {
                if (d == "peerReviewers" && data.bypassPeerReview) {
                    continue;
                }
                if (!data[d] || _.isEmpty(data[d])) {
                    errorFieldName = dataFields[d]
                    validationFailed = true;
                    break;
                }
            }

            if (validationFailed) {
                // Toaster.sayWarning(errorFieldName)
                var field = (_.invert(dataFields))[errorFieldName];
                Toaster.sayWarningfocus(errorFieldName, field)
                return false;
            } else {
                return true;
            }
        },
        // New Implementation Plan Validation
        validateIPFields: function (data, systemNameDetails) {
            var commonFields;
            commonFields = {
                "platformId": "Please Choose a Platform",
                "systems": "Please Choose a Target System",
                "projectId": "Please Choose Project Number",
                "planDesc": "Please Provide Plan Description",
                "devManager": "Please Choose Dev Manager",
                "loadType": "Please Choose Load Type",
                "leadContact": "Please Provide Lead Contact"
            }

            for (field in commonFields) {

                if (!(_.isObject(data[field]) ? Object.keys(data[field]).length > 0 : (data[field] == null || data[field].length === 0 ? false : true))) {
                    //  Toaster.sayWarning(commonFields[field])
                    Toaster.sayWarningfocus(commonFields[field], field)
                    return false;
                }

                if (field == "loadType") {
                    for (individualSystem in data["systems"]) {
                        if (typeof individualSystem != "undefined") {
							if(_.findWhere(systemNameDetails, { id: parseInt(individualSystem) }) != undefined || _.findWhere(systemNameDetails, { id: parseInt(individualSystem) })) {
								var systemName = _.findWhere(systemNameDetails, { id: parseInt(individualSystem) }).name
								if (!(systemValidation(data["system"][individualSystem], data, systemName))) { return false; }
							}
                        }
                    }
                }

                if (field == "leadContact") {
                    var pattern = new RegExp(/[0-9]{10,12}$/)
                    if (!pattern.test($.trim(data[field]))) {
                        Toaster.sayWarning("Phone number should be like (130) 312-3456-32")
                        return false;
                    }
                }
            }
            return true;
        }
    }

    function systemValidation(individualSystem, data, systemName) {
        var system_fields;
        // "qaFunctionalTesters": "Provide Load QA Functional Tester for "
        system_fields = {
            "1": {
                "loaddate": "Provide Load Date for ",
                "loadCategoryId": "Provide Load Category for ",
                "loadDateTime": "Provide Load Time for "
            },

            "2": {
                "loadCategoryId": "Provide Load Category for ",
                "loadCategoryDate": "Provide Load Date for ",
                "loadDateTime": "Provide Load Time for "
            },

            "EXCEPTION": {
                "exceptionLoaddate": "Provide Load Date for ",
                "loadCategoryId": "Provide Load Category for ",
                "exceptionLoadtime": "Provide Load Time for "
            },
            "EMERGENCY": {
                "exceptionLoaddate": "Provide Load Date for ",
                "loadCategoryId": "Provide Load Category for ",
                "exceptionLoadtime": "Provide Load Time for "
            }
        }

        var preload_fields = {
            "preloadDesc": "Provide Code Activated for ",
            "preloadJust": "Provide Preload Justification for "
        }
        var iplRequired_fields = {
            "iplRequired": "Provide Special Load Instruction for TSD for "
        }

        if (typeof individualSystem == "undefined") {
            Toaster.sayWarning("Please Provide System Information for " + systemName)
            return false;
        }
        switch (data["loadType"]) {
            case "STANDARD":
                if (individualSystem["loadWindowType"] == null || individualSystem["loadWindowType"] == '') {
                    Toaster.sayWarning("Please Provide load Window for " + systemName)
                    return false;
                }
                if (data.macroHeader == false && (individualSystem["loadAttendee"] == null || individualSystem["loadAttendee"] == '' || individualSystem["loadAttendee"].length === 0)) {
                    Toaster.sayWarningfocus("Provide Load Attendee for " + systemName)
                    return false;
                }
                for (field in system_fields[individualSystem["loadWindowType"]]) {
                    if (!(_.isObject(individualSystem[field]) ? Object.keys(individualSystem[field]).length > 0 : (individualSystem[field] == null || individualSystem[field].length === 0 ? false : true))) {
                        // Toaster.sayWarning(system_fields[individualSystem["loadWindowType"]][field] + systemName)
                        // if(field == "qaFunctionalTesters" && data.auxtype == false && data.macroHeader == false){
                        // Toaster.sayWarningfocus(system_fields[individualSystem["loadWindowType"]][field] + systemName , field)
                        // return false;
                        // }

                        Toaster.sayWarningfocus(system_fields[individualSystem["loadWindowType"]][field] + systemName, field)
                        return false;

                    }
                }
                break;
            case "EXCEPTION":
                if (data.macroHeader == false && (individualSystem["loadAttendee"] == null || individualSystem["loadAttendee"] == '' || individualSystem["loadAttendee"].length === 0)) {
                    Toaster.sayWarningfocus("Provide Load Attendee for " + systemName)
                    return false;
                }
                for (field in system_fields[data["loadType"]]) {
                    if (!(_.isObject(individualSystem[field]) ? Object.keys(individualSystem[field]).length > 0 : (individualSystem[field] == null || individualSystem[field].length === 0 ? false : true))) {
                        // if (field == "qaFunctionalTesters" && data.auxtype == false && data.macroHeader == false) {
                        //     Toaster.sayWarning(system_fields[data["loadType"]][field] + systemName)
                        //     return false;
                        // }
                        Toaster.sayWarning(system_fields[data["loadType"]][field] + systemName)
                        return false;

                    }
                }
                break;
            case "EMERGENCY":
                for (field in system_fields[data["loadType"]]) {
                    if (data.macroHeader == false && (individualSystem["loadAttendee"] == null || individualSystem["loadAttendee"] == '' || individualSystem["loadAttendee"].length === 0)) {
                        Toaster.sayWarningfocus("Provide Load Attendee for " + systemName)
                        return false;
                    }
                    if (!(_.isObject(individualSystem[field]) ? Object.keys(individualSystem[field]).length > 0 : (individualSystem[field] == null || individualSystem[field].length === 0 ? false : true))) {
                        // if (field == "qaFunctionalTesters" && data.auxtype == false && data.macroHeader == false) {
                        //     Toaster.sayWarning(system_fields[data["loadType"]][field] + systemName)
                        //     return false;
                        // }
                        Toaster.sayWarning(system_fields[data["loadType"]][field] + systemName)
                        return false;
                    }
                }

                break;
        }
        if (individualSystem["preload"] == "Yes") {
            for (field in preload_fields) {
                if (!(_.isObject(individualSystem[field]) ? Object.keys(individualSystem[field]).length > 0 : (individualSystem[field] == null || individualSystem[field].length === 0 ? false : true))) {
                    Toaster.sayWarning(preload_fields[field] + systemName)
                    return false;
                }
            }
        }

        if (individualSystem["iplRequired"] == "Yes") {
            for (field in iplRequired_fields) {
                if (individualSystem["loadInstruction"] == null || individualSystem["loadInstruction"] == "") {
                    Toaster.sayWarning(iplRequired_fields[field] + systemName)
                    return false;
                }
            }

        }
        return true;
    }



});