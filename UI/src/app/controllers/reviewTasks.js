dashboard.controller("reviewTasks", function ($rootScope, $scope, $state, $location, Toaster, apiService, APIFactory, RService, Paginate) {
    var vm = this;
    Paginate.refreshScrolling();
    $rootScope.titleHeading = $state.current.data.pageTitle;
    /* for (RTL in vm.reviewTasksList) {
                       var recentTime = 0;
                       for (ST in vm.reviewTasksList[RTL].loadDateTime) {
                           var currentSystemTime = new Date(moment(vm.reviewTasksList[RTL].loadDateTime[ST], "YYYY/MM/DD HH:mm")).getTime()
                           if (recentTime < currentSystemTime) {
                               recentTime = currentSystemTime
                           }
                           if (vm.reviewTasksList[RTL].loadDateTime.length === 1) {
                               recentTime = currentSystemTime
                           }
                       }
                       vm.reviewTasksList[RTL]["recentLoadTime"] = recentTime
                   } */
    vm.reviewTasksList = []
    $scope.showAccess = true;
    var getBase = RService.initReview($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)
    $scope.select = false;
    $scope.selectAllChange = function (status, currentsegmentId) {
        var selectedSegment = _.find(vm.reviewTasksList, function (item) { return item.id == currentsegmentId });
        if (status) {
            for (index in selectedSegment.segments) {
                if(selectedSegment.segments[index].additionalInfo.reviewStatus != 'true')
                selectedSegment.segments[index].additionalInfo.reviewStatus = true
            }
        } else {
            for (index in selectedSegment.segments) {
                if(selectedSegment.segments[index].additionalInfo.reviewStatus != 'true')
                selectedSegment.segments[index].additionalInfo.reviewStatus = false
            }
        }
    }


    $scope.checkmarkascomplete = function (data) {
        if (data) {
            var totalstatus = _.filter(data, function (item) {
                if (item.additionalInfo)
                    return item.additionalInfo.reviewStatus == 'true';
            })
            if (totalstatus.length == data.length) {
                return false;
            }
        }
        return true
    }

    $scope.checkReviewCompleted = function (data) {
        var segmentSelected = false
        var selectedReview = _.each(data, function (element) {
            if (element.additionalInfo.reviewStatus == true) {
                segmentSelected = true
            }
        })
        return segmentSelected
    }

    $scope.selectChange = function(segments,data){
        var selected = _.filter(segments,function(item){
            if(item.additionalInfo){
                if(item.additionalInfo.reviewStatus)
                return item.additionalInfo.reviewStatus == true;
            }
        })
        if(selected.length == segments.length){
            data.selectAll =true;
        }else{
            data.selectAll =false;
        }
    }

    $scope.reviewCompleted = function (data,id) {
        var selectedReview = _.filter(data.segments, function (element) {
            return element.additionalInfo.reviewStatus == true
        })

        APIFactory.reviewAsCompleted({implId:id}, selectedReview, function (response) {
            if (response.status) {
                $scope.loadSystemsAndSegmentsList(data);
            } else {
                Toaster.sayError(response.errorMessage)
            }
        })

    }
});