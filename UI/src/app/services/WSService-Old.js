app.service('WSService', ['$http', '$q', 'appSettings', 'Flash', '$location', function($http, $q, appSettings, Flash, $location) {

    var WSService = {};
    var appInfo = JSON.parse(localStorage.getItem("appInfo"))
    var apiBase = appSettings.apiBase;
    if (appInfo.isSSOApp) {
        apiBase = appSettings.apiSSOBase;
    }
    if(apiBase.indexOf("https:")!= -1){
    var socketURL = apiBase.replace("https", "wss") + "/buildStatus";
    } else if(apiBase.indexOf("http:")!= -1){
    var socketURL = apiBase.replace("http", "ws") + "/buildStatus";
    }
    var socketInitialized = false
    var initBuildStatus = function(cbk) {
        // var socketProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";

        if (socketInitialized) {
            return;
        }
        socketInitialized = true;
        var wsocket;

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
        }

        function onMessage(evt) {
            /*  DEVEL_BUILD,
             DEVEL_LOAD,
             CREATE_WORKSPACE,
             STAGE_BUILD,
             STAGE_LOAD;  */
            /* {
                "channel":"name",
                "message":"data"
            } */
            if (evt.data) {
                var socketData = JSON.parse(evt.data)
                if (socketData.channel == "DEVEL_BUILD") {
                    cbk(socketData.message)
                } else {
                    cbk({})
                }
            } else {
                cbk({})
            }
        }

        connectSocket()
    };
    WSService.initBuildStatus = initBuildStatus;

    var loaderInitialized = false
    var initLoaderStatus = function(cbk) {
        if (loaderInitialized) {
            return;
        }
        loaderInitialized = true
        var wsocket;

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
        }

        function onMessage(evt) {
            if (evt.data) {
                var socketData = JSON.parse(evt.data)
                if (socketData.channel == "DEVEL_LOAD") {
                    cbk(socketData.message)
                } else {
                    cbk({})
                }
            } else {
                cbk({})
            }
        }

        connectSocket()
    };
    WSService.initLoaderStatus = initLoaderStatus;

    var stageLoadInitialized = false
    var initStagingLoadStatus = function(cbk) {
        // var socketProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";

        if (stageLoadInitialized) {
            return;
        }
        stageLoadInitialized = true
        var wsocket;

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
        }

        function onMessage(evt) {
            if (evt.data) {
                var socketData = JSON.parse(evt.data)
                if (socketData.channel == "STAGE_LOAD") {
                    cbk(socketData.message)
                } else {
                    cbk({})
                }
            } else {
                cbk({})
            }
        }

        connectSocket()
    };
    WSService.initStagingLoadStatus = initStagingLoadStatus;

    // var stageBuildInitialized = false
    var initStagingBuildStatus = function(cbk) {
        // var socketProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";

        if (stageLoadInitialized) {
            return;
        }
        stageLoadInitialized = true
            // stageBuildInitialized = true
        var wsocket;

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
        }

        function onMessage(evt) {
            if (evt.data) {
                var socketData = JSON.parse(evt.data)
                if (socketData.channel == "STAGE_BUILD") {
                    cbk(socketData.message)
                } else {
                    cbk({})
                }
            } else {
                cbk({})
            }
        }

        connectSocket()
    };
    WSService.initStagingBuildStatus = initStagingBuildStatus;

    // var stageCreateWorkSpaceInitialized = false
    var initStagingCreateWorkspaceStatus = function(cbk) {
        // var socketProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";

        if (stageLoadInitialized) {
            return;
        }
        stageLoadInitialized = true
            // stageCreateWorkSpaceInitialized = true
        var wsocket;

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
        }

        function onMessage(evt) {
            if (evt.data) {
                var socketData = JSON.parse(evt.data)
                if (socketData.channel == "CREATE_WORKSPACE") {
                    cbk(socketData.message)
                } else {
                    cbk({})
                }
            } else {
                cbk({})
            }
        }

        connectSocket()
    };
    WSService.initStagingCreateWorkspaceStatus = initStagingCreateWorkspaceStatus;

    var initProductionLoad = function(cbk) {
        // var socketProtocol = window.location.protocol === "https:" ? "wss://" : "ws://";

        if (stageLoadInitialized) {
            return;
        }
        stageLoadInitialized = true
            // stageCreateWorkSpaceInitialized = true
        var wsocket;

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
        }

        function onMessage(evt) {
            if (evt.data) {
                var socketData = JSON.parse(evt.data)
                if (socketData.channel == "PROD_LOAD") {
                    cbk(socketData.message)
                } else {
                    cbk({})
                }
            } else {
                cbk({})
            }
        }

        connectSocket()
    };
    WSService.initProductionLoad = initProductionLoad;

    return WSService;

}]);