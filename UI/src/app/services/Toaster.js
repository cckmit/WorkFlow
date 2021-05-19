app.service('Toaster', ['$http', '$q', 'appSettings', 'Flash', '$location', '$mdToast', '$state', '$rootScope', function($http, $q, appSettings, Flash, $location, $mdToast, $state, $rootScope) {

    var Toaster = {};
    var messageType;


    var saySuccess = function(msg) {
        //            Flash.create('success', msg, 'large-text');
    	messageType = showModal('success', msg);
        setTimeout(function() {
            $(".okBtn").focus()
            $(".okBtn").click(function(){
            	if(messageType == "success"){
            		if($rootScope.currentState =='app.updateImpPlan' && getUserData('userRole')=='Lead' && typeof msg != 'undefined'){
                        $state.go('app.impPlan')
                    }

                    if($rootScope.currentState =='app.updateImpPlan' && getUserData('userRole')=='DevManager'){
                        if ($rootScope.previousState != undefined && $rootScope.currentState !='app.updateImpPlan') {
                            $state.go($rootScope.previousState)
                        } else {
                            $state.go("app.assignedPlan")
                        }
                    }
            	}
            })
        }, 500)
    };

    var sayWarning = function(msg) {
        //            Flash.create('warning', msg, 'large-text');
    	messageType = showModal('warning', msg);
        setTimeout(function() {
            $(".okBtn").focus()
        }, 500)
    };

    var sayWarningfocus = function(msg,field) {
        //            Flash.create('warning', msg, 'large-text');
    	messageType = showModal('warning', msg);
        setTimeout(function() {
            $(".okBtn").focus()
            $(".okBtn").click(function(){
                // $('#csrId').focus();
                if(field == "projectId"){
                $('[ng-model="vm.impPlan.projectId.id"]').focus();
                }else if(field == "planDesc" || field == "devManager" || field =="leadContact"){
                    $('[ng-model="vm.impPlan.'+field+'"]').focus();
                }
                if(field == "loadCategoryId" || field == "loadCategoryDate" || field == "loadDateTime" ||field == "loadAttendee"){
                    $('[ng-model="vm.impPlan.system[sList.id].'+field+'"]').focus();
                }
                if(field == "impDesc"){
                    $('[data-ng-model="vm.imp.'+field+'"]').focus();
                }
                if(field == "peerReviewers" || field =="devId"){
                    $('[ng-model="vm.imp.'+field+'"]').focus();
                }
            })
        }, 500)
    };

    var sayError = function(msg) {
        //            Flash.create('error', msg, 'large-text');
    	messageType = showModal('error', msg);
        setTimeout(function() {
            $(".okBtn").focus()
        }, 500)
    };

    iziToast.settings({
        timeout: 7000, // default timeout
        resetOnHover: true,
         icon: false, // icon class
        transitionIn: 'flipInX',
        transitionOut: 'flipOutX',
        messageColor:"white",
        progressBarColor:"white",

        // position: 'topLeft', // bottomRight, bottomLeft, topRight, topLeft, topCenter, bottomCenter, center
        onOpen: function () {
          console.log('callback abriu!');
        },
        onClose: function () {
          console.log("callback fechou!");
        }
      });

    // var sayStatus = function(msg) {
    //     var toast = $mdToast.simple()
    //         .content(msg)
    //         .action('Ok')
    //         .hideDelay(8000)
    //         .highlightAction(true);
    //     toast._options.position = 'bottom left';
    //     $mdToast.show(toast);
    // }
    var sayStatus = function(msg) {
         iziToast.show({position: "bottomLeft", title: '', toastOnce: true,theme:"dark",message: msg});
    }

    Toaster.sayStatus = sayStatus

    Toaster.saySuccess = saySuccess;
    Toaster.sayWarning = sayWarning;
    Toaster.sayWarningfocus = sayWarningfocus;
    Toaster.sayError = sayError;



    return Toaster;

}]);