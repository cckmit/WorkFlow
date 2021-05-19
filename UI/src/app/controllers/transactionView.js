dashboard.controller("transactionViewCtrl", function ($rootScope, $state, $stateParams, $scope, $timeout, $location, appSettings, Toaster, $http,
	fImplementationPlanValidate, $mdDialog, apiService, APIFactory, WFLogger, WSService, freezeService, IPService, Access, Paginate) {

	var vm = this;
	$rootScope.titleHeading = $state.current.data.pageTitle;
	vm.sourceData = {};
	var apiBase = appSettings.apiBase;

})