var dashboard = angular.module('dashboard', ['ui.router', 'ngAnimate', 'ngMaterial', 'ngSanitize', 'ngFileUpload']);
dashboard.config(["$stateProvider", function ($stateProvider) {


    // myTasks

    $stateProvider.state('app.myTasks', {
        url: '/mytasks',
        templateUrl: 'html/myTasks.html',
        controller: 'myTasks',
        controllerAs: 'vm',
        data: {
            pageTitle: 'My Tasks'
        },
        authenticate: true
    });

    $stateProvider.state('app.myTasksLC', {
        url: '/mytaskslc',
        templateUrl: 'html/myTasksLC.html',
        controller: 'myTasksLC',
        controllerAs: 'vm',
        data: {
            pageTitle: 'My Tasks'
        },
        authenticate: true
    });
    $stateProvider.state('app.myTasksLD', {
        url: '/mytasksld',
        templateUrl: 'html/myTasksLD.html',
        controller: 'myTasksLD',
        controllerAs: 'vm',
        data: {
            pageTitle: 'My Tasks'
        },
        authenticate: true
    });
    $stateProvider.state('app.myTasksDV', {
        url: '/mytasksdv',
        templateUrl: 'html/myTasksDV.html',
        controller: 'myTasksDV',
        controllerAs: 'vm',
        data: {
            pageTitle: 'My Tasks'
        },
        authenticate: true
    });

    //dashboard home page state
    $stateProvider.state('app.dashboard', {
        url: '/dashboard',
        templateUrl: 'html/home.html',
        controller: 'HomeController',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Home'
        },
        authenticate: true
    });

    $stateProvider.state('app.impPlan', {
        url: '/impPlan',
        templateUrl: 'html/impPlan.html',
        controller: 'impPlanCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Implementation Plan'
        },
        authenticate: true
    });

    // Utilities Report
    // $stateProvider.state('app.utilitiesReport', {
    // url: '/report',
    // templateUrl: 'html/utilitiesReport.html',
    // controller: 'utilitiesReportCtrl',
    // controllerAs: 'vm',
    // data: {
    // pageTitle: 'Reports'
    // },
    // authenticate: true,
    // })

    // Utilities Report
    $stateProvider.state('app.utilitiesReport', {
        url: '/utilities',
        abstract: true,
        templateUrl: 'html/utilitiesData.html',
        authenticate: true,
    });

    //Report
    $stateProvider.state('app.utilitiesReport.report', {
        url: '/report',
        parent: 'app.utilitiesReport',
        templateUrl: 'html/utilitiesReport.html',
        controller: 'utilitiesReportCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Reports'
        },
        authenticate: true,
    });

    // Project Nbr Display.
    $stateProvider.state('app.utilitiesReport.projNbrDisplay', {
        url: '/projNbrDisplay',
        parent: 'app.utilitiesReport',
        templateUrl: 'html/projNbrDisplay.html',
        controller: 'projNbrDisplayCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Proj Nbr Display'
        },
        authenticate: true,
    });
	
	//performance View
	
	$stateProvider.state('app.utilitiesReport.performanceReport', {
        url: '/performance',
        // abstract: true,
        templateUrl: 'html/performanceData.html',
        authenticate: true,
    });

    //Transaction View Report
    $stateProvider.state('app.utilitiesReport.performanceReport.transactionView', {
        url: '/transactionView',
        parent: 'app.utilitiesReport.performanceReport',
        templateUrl: 'html/performanceTransaction.html',
        controller: 'transactionViewCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Transaction View'
        },
        authenticate: true,
    });

    // System View Report.
    $stateProvider.state('app.utilitiesReport.performanceReport.systemView', {
        url: '/systemView',
        parent: 'app.utilitiesReport',
        templateUrl: 'html/performanceSystem.html',
        controller: 'systemViewCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'System View'
        },
        authenticate: true,
    });

    // Display Build Queue

    $stateProvider.state('app.buildQueue', {
        url: '/lsBuildQueueReady',
        templateUrl: 'html/buildQueue.html',
        controller: 'buildQueueCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Display Build Queue '
        },
        authenticate: true
    });

    $stateProvider.state('app.newImpPlan', {
        url: '/newImpPlan',
        templateUrl: 'html/newImpPlan.html',
        controller: 'newImpPlanCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'New Implementation Plan'
        },
        authenticate: true
    });

    $stateProvider.state('app.updateImpPlan', {
        url: '/updateImpPlan/:planId',
        templateUrl: 'html/updateImpPlan.html',
        controller: 'updateImpPlanCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Implementation Plan'
        },
        params: {
            planId: null
        },
        authenticate: true
    });

    $stateProvider.state('app.leadDeployment', {
        url: '/leadDeployment',
        templateUrl: 'html/leadDeployment.html',
        controller: 'leadDeploymentCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'E-Type Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.qaTasks', {
        url: '/qaTasks',
        templateUrl: 'html/qaTask.html',
        controller: 'qaMyTaskCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'E-Type Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.qaDeployment', {
        url: '/qaDeployment',
        templateUrl: 'html/qaDeployment.html',
        controller: 'qaDeploymentCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'E-Type Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.passedQARegressionScreen', {
        url: '/passedQARegression',
        templateUrl: 'html/passedQARegressionScreen.html',
        controller: 'passedQARegressionCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Passed QA Regression Testing'
        },
        authenticate: true
    });

    $stateProvider.state('app.tssTosDeployment', {
        url: '/preprodTos',
        templateUrl: 'html/tssDeploymentTos.html',
        controller: 'tssDeploymentCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'E-Type Deployment Screen'
        },
        authenticate: true
    });
    $stateProvider.state('app.tssYodaDeployment', {
        url: '/preprodYoda',
        templateUrl: 'html/tssDeploymentYoda.html',
        controller: 'tssDeploymentCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'E-Type Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.leadAuxDeploymentScreen', {
        url: '/leadAuxDeployment',
        templateUrl: 'html/leadAuxDeploymentScreen.html',
        controller: 'leadAuxDeploymentScreenCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Aux Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.qaAuxDeploymentScreen', {
        url: '/qaAuxDeployment',
        templateUrl: 'html/qaAuxDeploymentScreen.html',
        controller: 'qaAuxDeploymentScreenCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Aux Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.tssAuxDeploymentTos', {
        url: '/tssAuxTosDeployment',
        templateUrl: 'html/tssAuxDeploymentTos.html',
        controller: 'tssAuxDeploymentCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Aux Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.tssAuxDeploymentYoda', {
        url: '/tssAuxYodaDeployment',
        templateUrl: 'html/tssAuxDeploymentYoda.html',
        controller: 'tssAuxDeploymentCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Aux Deployment Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.plansDeployedInPreProd', {
        url: '/plansDeployedInPreProd',
        templateUrl: 'html/plansDeployedInPreProd.html',
        controller: 'plansDeployedInPreProdCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Plans Deployed in Pre-Prod'
        },
        authenticate: true
    });

    $stateProvider.state('app.reportGeneration', {
        url: '/reportGeneration',
        templateUrl: 'html/reportGenerate.html',
        controller: 'reportGenerateCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Report Generation Screen'
        },
        authenticate: true
    });

    $stateProvider.state('app.rfcChangeMgmt', {
        url: '/rfcChangeMgmt',
        templateUrl: 'html/rfcChangeMgmt.html',
        controller: 'rfcChangeMgmtCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Inbox'
        },
        authenticate: true
    });

    $stateProvider.state('app.rfcApprovedPlans', {
        url: '/rfcApprovedPlans',
        templateUrl: 'html/rfcApprovedPlans.html',
        controller: 'rfcApprovedPlansCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'RFC Approved Plans'
        },
        authenticate: true
    });

    $stateProvider.state('app.impl', {
        url: '/impl',
        templateUrl: 'html/impl.html',
        controller: 'impCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Implementation'
        },
        authenticate: true
    });

    $stateProvider.state('app.newImp', {
        url: '/newImp/:planId',
        templateUrl: 'html/newImpl.html',
        controller: 'newImpCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'New Implementation'
        },
        params: {
            planId: null
        },
        authenticate: true
    });

    $stateProvider.state('app.updateImp', {
        url: '/updateImp/:planId/:impId',
        templateUrl: 'html/updateImpl.html',
        controller: 'updateImpCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Implementation'
        },
        params: {
            planId: null,
            impId: null,
            redirect: null
        },
        authenticate: true
    });

    //  TSD

    $stateProvider.state('app.loadsetReady', {
        url: '/lsReady',
        templateUrl: 'html/loadsetReady.html',
        controller: 'loadsetReadyCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Loadset Ready to be accepted'
        },
        authenticate: true
    });

    $stateProvider.state('app.onlineFeedback', {
        url: '/lsReadyOnline',
        templateUrl: 'html/onlineFeedback.html',
        controller: 'onlineFeedbackCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Online Feedback Queue'
        },
        authenticate: true
    });

    $stateProvider.state('app.activateFallback', {
        url: '/activateFallback',
        templateUrl: 'html/activateFallback.html',
        controller: 'activateFallbackCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Activate Fallback'
        },
        authenticate: true
    });

    $stateProvider.state('app.acceptFallback', {
        url: '/acceptFallback',
        templateUrl: 'html/acceptFallback.html',
        controller: 'acceptFallbackCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Accept Fallback'
        },
        authenticate: true
    });

    $stateProvider.state('app.defultcpu', {
        url: '/defaultCPU',
        templateUrl: 'html/defaultcpu.html',
        controller: 'defaultCPUCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Manage default CPU'
        },
        authenticate: true
    });

    $stateProvider.state('app.manageDelegate', {
        url: '/manageDelegate',
        templateUrl: 'html/manageDelegate.html',
        controller: 'manageDelegatesCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Manage Delegations'
        },
        authenticate: true
    });

    $stateProvider.state('app.repositoryOwners', {
        url: '/repositoryOwners',
        templateUrl: 'html/repositoryOwners.html',
        controller: 'repositoryOwnersCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Functional Packages'
        },
        authenticate: true
    });

    $stateProvider.state('app.createNewRepo', {
        url: '/createNewRepo',
        templateUrl: 'html/createNewRepo.html',
        controller: 'createNewRepoCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Create New NON-IBM Production Repositories'
        },
        authenticate: true
    });

    $stateProvider.state('app.syncPlan', {
        url: '/syncPlan',
        templateUrl: 'html/syncImplementationPlan.html',
        controller: 'syncImplementationPlanCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Sync Implementation Plans'
        },
        authenticate: true
    });

    $stateProvider.state('app.help', {
        url: '/help',
        templateUrl: 'html/help.html',
        controller: 'helpCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'System Information'
        },
        authenticate: true
    });


    //  Review

    $stateProvider.state('app.reviewTasks', {
        url: '/reviewTasks',
        templateUrl: 'html/reviewTasks.html',
        controller: 'reviewTasks',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Review Implementation'
        },
        authenticate: true
    });

    $stateProvider.state('app.approvedReviewTasks', {
        url: '/approvedTasks',
        templateUrl: 'html/approvedReviewTasks.html',
        controller: 'approvedReviewCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Approved Implementation'
        },
        authenticate: true
    });



    //Load Controller

    $stateProvider.state('app.loadCategoriesList', {
        url: '/loadCategories',
        templateUrl: 'html/loadCategories.html',
        controller: 'loadCategories',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Load Categories'
        },
        authenticate: true
    });


    $stateProvider.state('app.freezeDateList', {
        url: '/freezeDate',
        templateUrl: 'html/freezeDate.html',
        controller: 'freezeDate',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Freeze Date'
        },
        authenticate: true
    });

    $stateProvider.state('app.loadsControlHistory', {
        url: '/loadHistory',
        templateUrl: 'html/loadHistory.html',
        controller: 'loadsControlHistoryCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Load History'
        },
        authenticate: true
    });

    $stateProvider.state('app.pLDeploymentDetails', {
        url: '/deploymentDetails',
        templateUrl: 'html/putLevelDD.html',
        controller: 'putLevelDD',
        controllerAs: 'vm',
        data: {
            pageTitle: 'zTPF Level Deployment Details'
        },
        authenticate: true
    });

    $stateProvider.state('app.masterImpPlanList', {
        url: '/masterImpPlan',
        templateUrl: 'html/masterImpPlan.html',
        controller: 'masterImpPlan',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Implementation Plan'
        },
        authenticate: true
    });



    $stateProvider.state('app.search', {
        url: '/search',
        templateUrl: 'html/search.html',
        controller: 'searchCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Search'
        },
        authenticate: true
    });


    // Dev Manager

    $stateProvider.state('app.devManager', {
        url: '/manageTasks',
        templateUrl: function (stateParams) {
            return 'html/devManager.html';
        },
        controller: 'devManagerCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Manage Tasks'
        },
        authenticate: true
    });

    // Access Permissions for Repositories

    $stateProvider.state('app.accessPermission', {
        url: '/accessPermission',
        templateUrl: function (stateParams) {
            return 'html/accessPermission.html';
        },
        controller: 'accessPermissionCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Access Permissions for Repositories'
        },
        authenticate: true
    });

    // Assigned Plan

    $stateProvider.state('app.assignedPlan', {
        url: '/assignedPlan',
        templateUrl: function (stateParams) {
            return 'html/assignedPlan.html';
        },
        controller: 'assignedPlanCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Assigned Plan'
        },
        authenticate: true
    });

    // Fallback Macro/Header only Plans

    $stateProvider.state('app.fbmacroPlan', {
        url: '/fbmacroPlan',
        templateUrl: function (stateParams) {
            return 'html/fallbackMacroPlan.html';
        },
        controller: 'fallbackmacroPlanCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Fallback Macro/Header only Plans'
        },
        authenticate: true
    });

    // Move Artifact

    $stateProvider.state('app.moveArtifacts', {
        url: '/moveArtifacts',
        templateUrl: function (stateParams) {
            return 'html/moveArtifacts.html';
        },
        controller: 'moveArtifacts',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Move Artifacts'
        },
        authenticate: true
    });
    // Assigned Plan

    $stateProvider.state('app.dtnMaintenance', {
        url: '/dtnMaintenance',
        templateUrl: function (stateParams) {
            return 'html/dtnMaintenance.html';
        },
        controller: 'dtnMaintenanceCtrl',
        controllerAs: 'vm',
        data: {
            pageTitle: 'Proj Nbr Maintenance'
        },
        authenticate: true
    });


}]);