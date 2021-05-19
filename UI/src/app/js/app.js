var app = angular.module('app', ['ui.router', 'ui.bootstrap', 'ngIntlTelInput', 'flash', 'login', 'dashboard',
    'ngAnimate', 'ngResource', 'ngMaterial', 'ngMessages', 'ngCookies', 'ngRoute', 'moment-picker',
    'angularUtils.directives.dirPagination','infinite-scroll','ngPatternRestrict'
]);

//set global configuration of application and it can be accessed by injecting appSettings in any modules
app.constant('appSettings', appConfig);

app.config(['$stateProvider', '$locationProvider', '$urlRouterProvider', '$httpProvider', '$mdThemingProvider', 'ngIntlTelInputProvider', function($stateProvider, $locationProvider,
    $urlRouterProvider, $httpProvider, $mdThemingProvider, ngIntlTelInputProvider, $modalInstance) {
    ngIntlTelInputProvider.set({
        defaultCountry: 'auto'
    });
    // ngIntlTelInputProvider.set({
    //     defaultCountry: 'auto'
    // });
    ngIntlTelInputProvider.set({
        nationalMode: false
    });
    $mdThemingProvider.theme('default')
        .primaryPalette('blue')
        .accentPalette('blue')
        .warnPalette('red');
    ngIntlTelInputProvider.set({
        defaultCountry: 'us'
    });
    if (!$httpProvider.defaults.headers.get) {
        $httpProvider.defaults.headers.get = {};
    }
    
    $httpProvider.interceptors.push(function($q, $rootScope) {
        return {
            'request': function(config) {
                $rootScope.http_loading = true
                if (config.url.indexOf(".html") == -1 && config.url.indexOf("buildStatus") == -1) {
                    showLoading(true)
                        // $(".loadScreen").toggle().fadeIn('slow')
                }
                return config;
            },
            'response': function(response) {
                //Will only be called for HTTP up to 300
                $rootScope.http_loading = false
                showLoading(false)
                $(".loadScreen").toggle().fadeOut('slow')
                return response;
            },
            'responseError': function(rejection) {
                $(".loadScreen").toggle().fadeOut('slow')
                if (rejection.status === 401) {
                    localStorage.setItem("401", "true")
                    clearUserData()
                    location.reload(true)
//                    showModal('error', "Session Expired -  Login Again");
                }
                if (rejection.status === 404) {
                    showModal('error', "Not Found");
                }
                return $q.reject(rejection);
            }
        };
    });
    //IdleScreenList
    $stateProvider
        .state('app', {
            url: '/app',
            templateUrl: 'html/app.html',
            controller: 'appCtrl',
            controllerAs: 'vm',
            data: {
                pageTitle: 'Login'
            },
            authenticate: true
        });

    $urlRouterProvider.otherwise('login');

    //$urlRouterProvider.otherwise('app/dashboard');
    //$urlRouterProvider.otherwise('/app/dashboard');
}]);

app.run(function($rootScope, $state, Access,appSettings) {
    $rootScope.isTravelportApp = appSettings.isTravelportApp == 'true';
    $rootScope.$on("$stateChangeStart", function(event, toState, toParams, fromState, fromParams) {
        if (toState.authenticate && localStorage.getItem('userdata') === null) {
            // User isn’t authenticated
            $state.transitionTo("login");
            event.preventDefault();
        } else if (localStorage.getItem('userdata') != null) {
            Access.getDefaultMenus(function(menu_items) {
                localStorage.setItem("previousState",toState.name)
                var state = toState.name;
                var currentstate = state.split(".")[1];
                var menus_states = _.pluck(menu_items, 'state');
                var Editpage = toState.params;
                if (currentstate && menus_states.indexOf(currentstate) < 0 && Editpage == undefined && toState.url.indexOf("help") < 0) {
                    $state.transitionTo('app.' + menus_states[0]);
                    event.preventDefault();
                }
                if (!toState.authenticate && toState.url.indexOf("help") < 0) {
                    $state.transitionTo('app.' + menus_states[0]);
                    event.preventDefault();
                }
            });
        }
    });
});

