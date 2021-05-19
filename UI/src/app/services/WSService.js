app.service('WSService', ['$http', '$q', 'appSettings', 'Flash', '$location', function ($http, $q, appSettings, Flash, $location) {

    var WSService = {};
    var appInfo = JSON.parse(localStorage.getItem("appInfo"))
    var apiBase = appSettings.apiBase;
    if (appInfo.isSSOApp) {
        apiBase = appSettings.apiSSOBase;
    }
    //    if (apiBase.indexOf("https:") != -1) {
    //        var socketURL = apiBase.replace("https", "wss") + "/workflowStatus";
    //    } else if (apiBase.indexOf("http:") != -1) {
    //        var socketURL = apiBase.replace("http", "ws") + "/workflowStatus";
    //    }
    var socketInitialized = false
    var wf_events;

    var socket = atmosphere;
    var subSocket;
    var transport = 'websocket';

    var request = {
        url: apiBase + '/workflowStatus',
        contentType: "application/json",
        logLevel: 'debug',
        transport: transport,
        trackMessageLength: true,
        reconnectInterval: 5000
        //            timeout:300000
    };

    request.onOpen = function (response) {
        console.log('Atmosphere connected using ' + response.transport)
        transport = response.transport;

        // Carry the UUID. This is required if you want to call subscribe(request) again.
        request.uuid = response.request.uuid;
    };

    request.onClientTimeout = function (r) {
        console.log('Client closed the connection after a timeout. Reconnecting in ' + request.reconnectInterval)
        setTimeout(function () {
            subSocket = socket.subscribe(request);
        }, request.reconnectInterval);
    };

    request.onReopen = function (response) {
        console.log('Atmosphere re-connected using ' + response.transport)
    };

    // For demonstration of how you can customize the fallbackTransport using the onTransportFailure function
    request.onTransportFailure = function (errorMsg, request) {
        atmosphere.util.info(errorMsg);
        request.fallbackTransport = "long-polling";
        console.log('Atmosphere Chat. Default transport is WebSocket, fallback is ' + request.fallbackTransport)
    };

    request.onMessage = function (response) {

        var message = response.responseBody;
        try {
            var json = JSON.parse(message);
            console.log(json)
            wf_events.publish(json.userIdAndChannel, json.message)
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message);
            return;
        }


    };

    request.onClose = function (response) {
        console.log('Server closed the connection after a timeout')
    };

    request.onError = function (response) {
        console.log('Sorry, but there\'s some problem with your '
            + 'socket or the server is down')
    };

    request.onReconnect = function (request, response) {
        console.log('Connection lost, trying to reconnect. Trying to reconnect ' + request.reconnectInterval)
    };

    subSocket = socket.subscribe(request);

    var initPublish = function () {
        if (socketInitialized) {
            return;
        }
        socketInitialized = true;
        var wsocket;
        wf_events = (function () {
            var topics = {};
            var hOP = topics.hasOwnProperty;

            return {
                clearTopics: function () {
                    topics = {}
                },
                subscribe: function (topic, listener) {
                    // Delete Preexisting topic
                    delete topics[topic]
                    // Create the topic's object if not yet created
                    if (!hOP.call(topics, topic)) topics[topic] = [];

                    // Add the listener to queue
                    var index = topics[topic].push(listener) - 1;

                    // Provide handle back for removal of topic
                    return {
                        remove: function () {
                            delete topics[topic][index];
                        }
                    };
                },
                publish: function (topic, info) {
                    // If the topic doesn't exist, or there's no listeners in queue, just leave
                    if (!hOP.call(topics, topic)) return;

                    // Cycle through topics queue, fire!
                    topics[topic].forEach(function (item) {
                        item(info != undefined ? info : {});
                    });
                }
            };
        })();

        function connectSocket() {
            wsocket = new WebSocket(socketURL);
            wsocket.onmessage = onMessage;
            wsocket.onclose = onClose;
        }

        function onMessage(data) {
            try {
                if (data) {
                    // var socketData = JSON.parse(evt.data)
                    wf_events.publish(data.userIdAndChannel, data.message)
                }
            } catch (err) { }
        }

        function onClose() {
            console.log("Socket Closed... So Reopening...")
            connectSocket()
        }
        //        connectSocket()
    };
    WSService.initPublish = initPublish;

    var initBuildStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "DEVEL_BUILD", function (response) {
            cbk(response)
        })
    }
    WSService.initBuildStatus = initBuildStatus;

    var initLoaderStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "DEVEL_LOAD", function (response) {
            cbk(response)
        })
    }
    WSService.initLoaderStatus = initLoaderStatus;

    var initDateAuditStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "DATE_AUDIT", function (response) {
            cbk(response)
        })
    }
    WSService.initDateAuditStatus = initDateAuditStatus;

    var initEAuxDeployStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "E_AUX_DEPLOY_STATUS", function (response) {
            cbk(response)
        })
    }
    WSService.initEAuxDeployStatus = initEAuxDeployStatus;

    var initStagingLoadStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "STAGE_LOAD", function (response) {
            cbk(response)
        })
    }
    WSService.initStagingLoadStatus = initStagingLoadStatus;

    var initStagingBuildStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "STAGE_BUILD", function (response) {
            cbk(response)
        })
    }
    WSService.initStagingBuildStatus = initStagingBuildStatus;

    var initStagingCreateWorkspaceStatus = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "CREATE_WORKSPACE", function (response) {
            cbk(response)
        })
    }
    WSService.initStagingCreateWorkspaceStatus = initStagingCreateWorkspaceStatus;

    var initProductionLoad = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PROD_LOAD", function (response) {
            cbk(response)
        })
    }
    WSService.initProductionLoad = initProductionLoad;

    var initPreProductionLoad = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PRE_PROD_LOAD", function (response) {
            cbk(response)
        })
    }
    WSService.initPreProductionLoad = initPreProductionLoad;

    var initProdFTPIP = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PROD_FTP_IP", function (response) {
            cbk(response)
        })
    }
    WSService.initProdFTPIP = initProdFTPIP;

    var initPlanUpdate = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PLAN_UPDATE", function (response) {
            cbk(response)
        })
    }
    WSService.initPlanUpdate = initPlanUpdate;

    var initPlanSubmit = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PLAN_SUBMIT", function (response) {
            cbk(response)
        })
    }
    WSService.initPlanSubmit = initPlanSubmit;

    var initAutoReject = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "AUTO_REJECT", function (response) {
            cbk(response)
        })
    }
    WSService.initAutoReject = initAutoReject;

    var initPlanAuxOnline = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PLAN_AUX_ONLINE", function (response) {
            cbk(response)
        })
    }
    WSService.initPlanAuxOnline = initPlanAuxOnline;

    var initPlanAuxFallback = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "PLAN_AUX_FALLBACK", function (response) {
            cbk(response)
        })
    }
    WSService.initPlanAuxFallback = initPlanAuxFallback;

    var initOnlineProcess = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "ONLINE_PROCESS", function (response) {
            cbk(response)
        })
    }
    WSService.initOnlineProcess = initOnlineProcess;

    var initOnlineBuildProcess = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "ONLINE_BUILD", function (response) {
            cbk(response)
        })
    }
    WSService.initOnlineBuildProcess = initOnlineBuildProcess;

    var initFallbackBuildProcess = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "FALLBACK_BUILD", function (response) {
            cbk(response)
        })
    }
    WSService.initFallbackBuildProcess = initFallbackBuildProcess;

    var initDelegation = function (cbk) {
        wf_events.subscribe("DELEGATION", function (response) {
            cbk(response)
        })
    }
    WSService.initDelegation = initDelegation;

    var initSessionTimeout = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "SESSION_TIMEOUT", function (response) {
            cbk(response)
        })
    }
    WSService.initSessionTimeout = initSessionTimeout;

    var initPkgMovement = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "SH_PKG_MOVEMENT", function (response) {
            cbk(response)
        })
    }
    WSService.initPkgMovement = initPkgMovement;

    var initBuildMessage = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "DEVEL_BUILD_STATUS", function (response) {
            cbk(response)
        })
    }
    WSService.initBuildMessage = initBuildMessage;

    var initStageDevlBuildMessage = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "SH_BUILD_STATUS", function (response) {
            cbk(response)
        })
    }
    WSService.initStageDevlBuildMessage = initStageDevlBuildMessage;

    var clearLiveTopics = function (cbk) {
        wf_events.clearTopics()
    }
    WSService.clearLiveTopics = clearLiveTopics;

    var initGitPortChange = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "GI_PORT_CHANGE", function (response) {
            cbk(response)
        })
    }
    WSService.initGitPortChange = initGitPortChange;
    
    var initFuncPkgMove = function (cbk) {
        wf_events.subscribe(getUserData("userName") + "-" + "MOVE_SOURCE_ARTIFACT", function (response) {
            cbk(response)
        })
    }
    WSService.initFuncPkgMove = initFuncPkgMove;


    return WSService;

}]);