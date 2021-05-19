(function() {
    'use strict';
    var app = angular.module('flash', []);

    app.run(['$rootScope', function($rootScope) {
        // initialize variables
        $rootScope.flash = {};
        $rootScope.flash.text = '';
        $rootScope.flash.type = '';
        $rootScope.flash.icon = '';
        $rootScope.flash.timeout = 5000;
        $rootScope.hasFlash = false;
    }]);

    // Directive for compiling dynamic html
    app.directive('dynamic', ['$compile', function($compile) {
        return {
            restrict: 'A',
            replace: true,
            link: function(scope, ele, attrs) {
                scope.$watch(attrs.dynamic, function(html) {
                    ele.html(html);
                    $compile(ele.contents())(scope);
                });
            }
        };
    }]);

    // Directive for closing the flash message
    app.directive('closeFlash', ['$compile', 'Flash', function($compile, Flash) {
        return {
            link: function(scope, ele) {
                ele.on('click', function() {
                    Flash.dismiss();
                });
            }
        };
    }]);

    // Create flashMessage directive
    app.directive('flashMessage', ['$compile', '$rootScope', function($compile, $rootScope) {
        return {
            restrict: 'A',
            template: '<div role="alert" ng-show="hasFlash" class="flashClass container alert {{flash.addClass}} alert-{{flash.type}} alert-dismissible ng-hide alertIn alertOut"><i class="fa fa-{{flash.icon}}"></i> <span dynamic="flash.text"></span> <button type="button" style="color:white;" class="close" close-flash><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button> </div>',
            link: function(scope, ele, attrs) {
                // get timeout value from directive attribute and set to flash timeout
                $rootScope.flash.timeout = parseInt(attrs.flashMessage, 10);
            }
        };
    }]);

    app.factory('Flash', ['$rootScope', '$timeout',
        function($rootScope, $timeout) {

            var dataFactory = {},
                timeOut;

            // Create flash message
            dataFactory.create = function(type, text, addClass, timeOutIn) {
                var $this = this;
                $timeout.cancel(timeOut);
                $rootScope.flash.type = type;
                if(type === "error"){
                    $rootScope.flash.icon = "close"
                }else if(type === "success"){
                    $rootScope.flash.icon = "check"
                }else{
                    $rootScope.flash.icon = "warning"
                }
                $rootScope.flash.text = text;
                $rootScope.flash.addClass = addClass;
                $timeout(function() {
                    $rootScope.hasFlash = true;
                }, 100);
                if(timeOutIn){
                    timeOut = $timeout(function() {
                        $this.dismiss();
                    }, timeOutIn);
                }else{
                    timeOut = $timeout(function() {
                        $this.dismiss();
                    }, $rootScope.flash.timeout);
                }
            };

            // Cancel flashmessage timeout function
            dataFactory.pause = function() {
                $timeout.cancel(timeOut);
            };

            // Dismiss flash message
            dataFactory.dismiss = function() {
                $timeout.cancel(timeOut);
                $timeout(function() {
                    $rootScope.hasFlash = false;
                });
            };
            return dataFactory;
        }
    ]);
}());