dashboard.controller("approvedReviewCtrl", function($rootScope, $scope, $state, $location, Toaster, apiService, APIFactory, RService, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle;
    $scope.showAccess = false;
    vm.reviewTasksList = []
    Paginate.refreshScrolling();
    var getBase = RService.initReview($scope, vm, "history")
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

});