dashboard.controller("impCtrl", function ($rootScope, $scope, $state, $location, $mdToast, Toaster, $http, appSettings,
    $mdDialog, apiService, APIFactory, IService, IPService, Paginate) {
    var vm = this;
    var apiBase = appSettings.apiBase;
    $rootScope.titleHeading = $state.current.data.pageTitle

    vm.currentUser = $rootScope.home_menu
    $scope.enableRecentPage = false
    Paginate.refreshScrolling();
    var getBase = IService.initImpl($scope, vm)
    _.extend($scope, getBase.scope)
    _.extend(vm, getBase.vm)

	$scope.RFCClick = function (pObj) {
		var getBase1 = IPService.initRFC($scope, vm, pObj)
	}

    $scope.downloadTestCaseImpl = function (fileName, implementation) {
        try {
            var params = {
                "implId": implementation.id,
                "planId": implementation.planId.id,
                "testFile": fileName
            }
            APIFactory.downloadTestCase(params, function (response) {
                if (response.status) {
                    var resposeStr = base64ToArrayBuffer(response.data)
                    var file = new Blob([resposeStr], { type: response.metaData });
                    saveAs(file, fileName)
                } else {
                    Toaster.sayError(response.errorMessage);
                }
            })
        } catch (err) { }

    }

    IPService.getExpandView($scope, vm)


}).directive("select2", function ($timeout) {
    return {
        restrict: 'AC',
        require: 'ngModel',
        link: function (scope, element, attrs, model, $rootScope) {
            $timeout(function () {
                $.fn.select2.amd.require(['select2/selection/search'], function (Search) {
                    var oldRemoveChoice = Search.prototype.searchRemoveChoice;

                    Search.prototype.searchRemoveChoice = function () {
                        oldRemoveChoice.apply(this, arguments);
                        this.$search.val('');
                    };

                    Search.prototype.update = function (decorated, data) {
                        var searchHadFocus = this.$search[0] == document.activeElement;

                        this.$search.attr('placeholder', '');

                        decorated.call(this, data);

                        this.$selection.find('.select2-selection__rendered')
                            .append(this.$searchContainer);

                        this.resizeSearch();
                        if (searchHadFocus) {
                            var self = this;
                            window.setTimeout(function () {
                                self.$search.focus();
                            }, 0);
                        }
                    };

                    if (attrs.istagallowed && attrs.istagallowed == "true") {
                        element.select2({
                            tags: true
                        });
                    } else {
                        element.select2()
                    }
                    $(".select2.select2-container.select2-container--default").addClass('form-control')
                    $(".select2.select2-container.select2-container--default").css({
                        "width": "100%",
                        "padding": "0px",
                        "height": "auto"
                    })
                    $(".select2-selection.select2-selection--multiple").addClass('select2Enhance')
                });
            });
            model.$render = function () {
                element.select2("val", model.$viewValue);
            }
            element.on('change', function (evt) {
                scope.$apply(function () {
                    model.$setViewValue(element.select2("val"));
                });
            })
            element.on("select2:select", function (evt) {
                try {
                    if (scope.fileObj) {
						var deselectFlag = false;
                        scope.onSelect(scope.fileObj, evt.params.data.id, element.select2("val"), deselectFlag)
                    }
                    if (evt.currentTarget.attributes.planId.value && evt.currentTarget.attributes.systemId.value && evt.currentTarget.attributes.vparList.value) {
						var deselectFlag = false;
                        scope.onSelect(evt.currentTarget.attributes.planId.value, evt.currentTarget.attributes.systemId.value, evt.currentTarget.attributes.vparList.value, evt.params.data.id, deselectFlag)
                    }
                } catch (error) {

                }
            })
            element.on("select2:unselect", function (evt) {
                if (scope.fileObj) {
					var deselectFlag = true;
                    scope.onSelect(scope.fileObj, evt.params.data.id, element.select2("val"), deselectFlag)
                }
            })
        }
    };
}).directive("select2single", function ($timeout) {
    return {
        restrict: 'AC',
        require: 'ngModel',
        link: function (scope, element, attrs, model) {
            $timeout(function () {
                $.fn.select2.amd.require(['select2/selection/search'], function (Search) {
                    var oldRemoveChoice = Search.prototype.searchRemoveChoice;

                    Search.prototype.searchRemoveChoice = function () {
                        oldRemoveChoice.apply(this, arguments);
                        this.$search.val('');
                    };
                    element.select2({
                        maximumSelectionLength: 1
                    });

                    $(".select2.select2-container.select2-container--default").addClass('form-control')
                    $(".select2.select2-container.select2-container--default").css({
                        "width": "100%",
                        "padding": "0px",
                        "height": "auto"
                    })
                    $(".select2-selection.select2-selection--multiple").addClass('select2Enhance')
                });
            });
            model.$render = function () {
                element.select2("val", model.$viewValue);
            }
            element.on('change', function (evt) {
                scope.$apply(function () {
                    model.$setViewValue(element.select2("val"));
                });
            })
            element.on("select2:select", function (evt) {
                if (scope.fileObj) {
                    scope.onSelect(scope.fileObj, evt.params.data.id, element.select2("val"))
                }
            })
            element.on("select2:unselect", function (evt) {
                if (scope.fileObj) {
                    scope.onSelect(scope.fileObj, evt.params.data.id, element.select2("val"))
                }
            })
        }
    };
}).directive("select2singleqa", function ($timeout) {
    return {
        restrict: 'AC',
        require: 'ngModel',
        link: function (scope, element, attrs, model) {
            $timeout(function () {
                $.fn.select2.amd.require(['select2/selection/search'], function (Search) {
                    var oldRemoveChoice = Search.prototype.searchRemoveChoice;

                    Search.prototype.searchRemoveChoice = function () {
                        oldRemoveChoice.apply(this, arguments);
                        this.$search.val('');
                    };
                    element.select2({
                        maximumSelectionLength: 5
                    });

                    $(".select2.select2-container.select2-container--default").addClass('form-control')
                    $(".select2.select2-container.select2-container--default").css({
                        "width": "100%",
                        "padding": "0px",
                        "height": "auto"
                    })
                    $(".select2-selection.select2-selection--multiple").addClass('select2Enhance')
                });
            });
            model.$render = function () {
                element.select2("val", model.$viewValue);
            }
            element.on('change', function (evt) {
                scope.$apply(function () {
                    model.$setViewValue(element.select2("val"));
                });
            })
            element.on("select2:select", function (evt) {
                if (scope.fileObj) {
                    scope.onSelect(scope.fileObj, evt.params.data.id, element.select2("val"))
                }
            })
            element.on("select2:unselect", function (evt) {
                if (scope.fileObj) {
                    scope.onSelect(scope.fileObj, evt.params.data.id, element.select2("val"))
                }
            })
        }
    };
});;