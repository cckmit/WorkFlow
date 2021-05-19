app.service('freezeService', ['$http', '$q', 'appSettings', 'Flash', '$location', function($http, $q, appSettings, Flash, $location) {
    var FreezeService = {};

    function findDiffDays(d1, d2) {
        return moment(d1).diff(d2, "days");
    }

    function findDiffTimes(t1, t2) {
        return t1.diff(t2, 'minutes');
    }

    var getFreezeInfo = function(freezeObj) {
        var loadTime = []
        loadTime[freezeObj.systemInfo] = []
        var selectedDate = moment(freezeObj.selectedCDate, appSettings.dateFormat)
        var freezeInfo = freezeObj.loadDateTimeFreezeData[freezeObj.systemInfo]
        var selectedDateInFreezePeriod = false;
        var selectedDay = selectedDate.format("ddd")
        var loadWindows = _.where(freezeObj.loadWindows[freezeObj.systemInfo], { "daysOfWeek": selectedDay })
        var lStartDate, lEndDate, lStartTime, lEndTime;
        for (lF in freezeInfo) {
            lStartDate = moment(moment(freezeInfo[lF].fromDate).format(appSettings.dateFormat), appSettings.dateFormat)
            lEndDate = moment(moment(freezeInfo[lF].toDate).format(appSettings.dateFormat), appSettings.dateFormat)
            lStartTime = moment(moment(freezeInfo[lF].fromDate).format(appSettings.timeFormat), appSettings.timeFormat)
            lEndTime = moment(moment(freezeInfo[lF].toDate).format(appSettings.timeFormat), appSettings.timeFormat)
                //            selectedDate = moment(moment(selectedDate.format("MM-DD-YYYY"),"MM-DD-YYYY").format("YYYY-MM-DD"))
            for (lW in loadWindows) {
                if (loadWindows[lW].daysOfWeek === selectedDay) {
                    var availableSlots = _.pluck(_.where(loadWindows, { "daysOfWeek": selectedDay }), "timeSlot")
                    _.each(availableSlots, function(item, index) {
                        availableSlots[index] = moment(availableSlots[index], appSettings.uiDTFormat).format(appSettings.timeFormat)
                    })
                    if (lStartDate && lEndDate) {
                        //If the selected date is same as freeze start date
                        if (findDiffDays(selectedDate, lStartDate) == 0) {
                            selectedDateInFreezePeriod = true;
                            var freezeStartTime = lStartTime
                            for (avs in availableSlots) {
                                var avlSlot = moment(availableSlots[avs], "HH:mm")
                                if (findDiffTimes(freezeStartTime, avlSlot) > 0 && loadTime[freezeObj.systemInfo].indexOf(availableSlots[avs]) < 0) {
                                    loadTime[freezeObj.systemInfo].push(availableSlots[avs])
                                }
                            }
                        }
                        //If the selected date is same as freeze end date
                        if (findDiffDays(selectedDate, lEndDate) == 0) {
                            selectedDateInFreezePeriod = true;
                            var freezeEndTime = lEndTime
                            for (avs in availableSlots) {
                                var avlSlot = moment(availableSlots[avs], "HH:mm")
                                if (findDiffTimes(freezeEndTime, avlSlot) < 0 && loadTime[freezeObj.systemInfo].indexOf(availableSlots[avs]) < 0) {
                                    loadTime[freezeObj.systemInfo].push(availableSlots[avs])
                                }
                            }
                        }
                        //If the selected date is between freeze start and end date
                        if (selectedDate.isBetween(lStartDate, lEndDate)) {
                            selectedDateInFreezePeriod = true;
                        }
                    }
                }
            }
        }
        if (!selectedDateInFreezePeriod && loadWindows.length) {
            var filterLt = []
            for (lIndex in loadWindows) {
                if (loadWindows[lIndex].loadCategoryId.id == freezeObj.loadCategoryId) {
                    filterLt.push(moment(loadWindows[lIndex].timeSlot, appSettings.uiDTFormat).format(appSettings.timeFormat))
                }
            }
            return filterLt;
        }
        return loadTime[freezeObj.systemInfo];
    };


    FreezeService.getFreezeInfo = getFreezeInfo;
    return FreezeService;


}]);