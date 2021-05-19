dashboard.controller("createNewRepoCtrl", function($rootScope, $templateCache, appSettings, $scope,
    $state, $location, Toaster, $mdDialog, $http, $timeout, apiService, APIFactory, WFLogger, Paginate) {
    var vm = this;
    $rootScope.titleHeading = $state.current.data.pageTitle
    var apiBase = appSettings.apiBase;
    vm.currentUser = getUserData("user")

    Paginate.refreshScrolling();

    $scope.refresh = function(){
        vm.inputRepoInfo = {}
        vm.repoInfoResponse = {}
        vm.showCreateButton = false
        vm.repoResponseReceived = false
    }
    $scope.refresh();

    //Company list
    var loadPlatforms = function() {
        try {
            APIFactory.getPlatformList({}, function (response) {
                if (response.status) {
                    vm.pList = response.data
                    $rootScope.saveformData()
                } else {
                    vm.pList = []
                }
            })
        } catch (err) { }
    }
    loadPlatforms();

    //Repository Owners List
    function repoOwners() {
        APIFactory.getUsersByRole({
            "role": "DevManager"
        }, function(response) {
            if (response.status) {
                $scope.repoOwnersList = response.data;
                $rootScope.saveformData()
            } else {
                $scope.repoOwnersList = []
            }
        })
    }
    repoOwners();

    var loadSystems = function(c){
        //Target Systems
        APIFactory.getSystemByPlatform({ "ids": c=="tp" ? 1 : 2 }, function (response) {
            vm.systemsList = _.sortBy(response.data, "name")
        })
    }  

    //Clear RepoName
    $scope.clearRepoName = function(){
        vm.inputRepoInfo.repoName =""
        vm.repoResponseReceived = false
    }


    //checkRepoExistence
    vm.checkRepoExistence = function(companyName, repoName){
        if(!companyName){
            Toaster.sayWarning("Please choose a company")
            return;
        }
        if(!repoName){
            Toaster.sayWarning("Please enter repository name")
            return;
        }
        vm.branchExistedTargetSystems = []        
        APIFactory.getRepositoryInfo({"repoName" : repoName.replace(/\s/g,''), "platform" : companyName}, function (response){
            if(response){
                if(response.status){ 
                    // If status is true, then clear all the input fields to populate the new result
                    vm.inputRepoInfo.repoDesc = ""
                    $timeout(function () {
                        $("#repositoryOwners").val("").trigger("change");
                    }, 500);
                    vm.repoResponseReceived = true // indicator to show the remaining input fields
                    vm.showCreateButton = false // indicator to show create button
                    loadSystems(companyName);
                    vm.inputRepoInfo.selectedTargetSystems = {}
                    // ------ Repo Already exists section ---------
                    if(response.data!=null){ 
                        vm.repoExistsAlready = true // indicator to disable all the input fields except target systems
                        vm.repoInfoResponse = response.data // mapping the response
                        vm.inputRepoInfo.repoDesc = vm.repoInfoResponse.repository.description
                        $timeout(function () {
                            $("#repositoryOwners").val(vm.repoInfoResponse.repository.owners).trigger("change");
                            // var checkRepoOwnersExistence = false // checking if the owner(s) available in LDAP
                            // if($scope.repoOwnersList){
                            //     for (i in vm.repoInfoResponse.repository.owners){
                            //         if(vm.repoInfoResponse.repository.owners[i] != "mtpservice"){
                            //             if(_.where($scope.repoOwnersList, {"id": vm.repoInfoResponse.repository.owners[i]}).length == 0){
                            //                 checkRepoOwnersExistence = true
                            //             }
                            //         }                                    
                            //     }
                            //     if(checkRepoOwnersExistence){
                            //         $("#repositoryOwners").val("").trigger("change");
                            //         Toaster.sayWarning("Pre selected Repo Onwer(s) has been removed from LDAP. Please Contact Administrator")
                            //     }else{
                            //         $("#repositoryOwners").val(vm.repoInfoResponse.repository.owners).trigger("change");
                            //     }
                            // }                            
                        }, 500);
                        vm.inputRepoInfo.selectedTargetSystems = vm.repoInfoResponse.branches
                        _.filter(vm.repoInfoResponse.branches, function(value,key){ // getting info about branch existence
                            if(value==true){
                                vm.branchExistedTargetSystems.push(key)
                            }
                        })
                        // ------ Repo Does not exists already section ---------
                    }else{
                        vm.repoExistsAlready = false 
                    }                    
                }else{
                    Toaster.sayError(response.errorMessage) // response is false
                }
            }
        })
    }

    //select target systems
    vm.selectSystems = function(sysId, sysName, currentBooleanValue, selectionList){ //2, PRE, true , {2:true}
        vm.inputRepoInfo.selectedTargetSystems[sysName] = currentBooleanValue
        if(_.values(selectionList).indexOf(true)>=0){
            vm.showCreateButton = true
        }else{
            vm.showCreateButton = false
        }
    }

    //submit- Create New Repo
    $scope.submitRepoInfo = function (data){
        
        if(vm.repoExistsAlready){
            vm.repoInfoResponse.branches = data.selectedTargetSystems
        }else{
            if(!data.repoDesc){
                Toaster.sayWarning("Enter repository description")
                return;
            }
            if(!data.repoOwners || data.repoOwners.length <= 0){
                Toaster.sayWarning("Select repository Owners")
                return;
            }
        }
        var postData = {}
        if(vm.repoExistsAlready){
            postData = vm.repoInfoResponse
        }else{
            postData = {
                "repository" : {
                    "name" : data.repoName.replace(/\s/g,''),
                    "description" : data.repoDesc,
                    "owners" : data.repoOwners,
                    "trimmedName" : "",
                },
                "branches" :data.selectedTargetSystems
            }
        }
        APIFactory.createRepo({"platform": data.company},postData, function (response){      
            if(response.status){
                Toaster.saySuccess("Repository/Branch created successfully")
                $scope.refresh();
                return;
            }else{
                Toaster.sayError(response.errorMessage)
                return;
            }
        })  
    }


})