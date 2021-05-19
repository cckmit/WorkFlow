var login = angular.module('login', ['ui.router', 'ngResource', 'ngAnimate']);


login.config(["$stateProvider", "$httpProvider", function($stateProvider, $httpProvider) {

    //login page state
    $stateProvider.state('login', {
        url: '/login',
        templateUrl: 'html/login.html',
        controller: 'loginCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Login'
        },
        authenticate: false
    });
    // $httpProvider.defaults.withCredentials = true;
}]);