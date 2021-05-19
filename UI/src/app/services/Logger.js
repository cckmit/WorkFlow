app.service('WFLogger', ['$http', '$q', 'appSettings', 'Flash', '$location', function($http, $q, appSettings, Flash, $location) {

    var WFLogger = {};

    var logMessages = []

    var INFO = function(info, obj) {
        logMessages.push(_.isObject(info) ? JSON.stringify(info, null, 3) : info)
        logMessages.push(_.isObject(obj) ? JSON.stringify(obj, null, 3) : obj)
        console.info(info, obj);
    };

    var ERROR = function(error) {
        logMessages.push(error.stack)
        console.error(error);
    };

    var LOG = function(msg) {
        logMessages.push(_.isObject(msg) ? JSON.stringify(msg, null, 3) : msg)
        console.log(msg)
    }

    var getLogs = function() {
        return logMessages
    }

    WFLogger.getLogs = getLogs;
    WFLogger.LOG = LOG;
    WFLogger.INFO = INFO;
    WFLogger.ERROR = ERROR;

    return WFLogger;

}]);