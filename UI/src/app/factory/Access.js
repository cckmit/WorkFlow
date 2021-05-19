app.factory('Access', function ($rootScope, apiService, appSettings, APIFactory) {

    var Access = {};
    var appSession = JSON.parse(localStorage.getItem("appInfo"))

    var getDefaultPages = function (userRole, cbk) {
        var bpmRoles = ['LoadsControl', 'TechnicalServiceDesk']
        if (bpmRoles.indexOf(userRole) >= 0) {
            cbk("app.myTasks")
        } else if (userRole === "QA") {
            cbk("app.qaTasks")
        } else if (userRole === "QADeployLead") {
            cbk("app.qaTasks")
        } else if (userRole === "DLCoreChangeTeam") {
            if (appSettings.isDeltaApp == 'true') {
                cbk("app.rfcChangeMgmt")
            }

        } else if (userRole === "SystemSupport") {
            if (appSettings.isDeltaApp == 'true') {
                cbk("app.tssYodaDeployment")
            } else {
                cbk("app.tssTosDeployment")
            }
        } else if (userRole === "Lead") {
            cbk("app.impPlan")
        } else if (userRole === "Developer") {
            cbk("app.impl")
        } else if (userRole === "Reviewer") {
            cbk("app.reviewTasks")
        } else if (userRole === "DevManager") {
            cbk("app.devManager")
        } else if (userRole === "ToolAdmin") {
            cbk("app.repositoryOwners")
        }
    }
    Access.getDefaultPages = getDefaultPages;

    var allPlanStatus = {};
    var allPlanStatusForLabel = {};
    var allPlanStatusForLabelForAdvancedSearch = {};
    var allSystemQAStatus = {};
    var isOBS = false

    function getOnloadData() {
        APIFactory.getPlanStatusList({}, function (response) {
            if (response) {
                if (response.status) {
                    allPlanStatus = response.data
                } else {
                    allPlanStatus = {}
                }
            }
        })
        APIFactory.getPlanStatusListForLabel({}, function (response) {
            if (response) {
                if (response.status) {
                    allPlanStatusForLabel = response.data
                } else {
                    allPlanStatusForLabel = {}
                }
            }
        })

        APIFactory.getPlanStatusListForLabelForAdvancedSearch({}, function (response) {
            if (response) {
                if (response.status) {
                    allPlanStatusForLabelForAdvancedSearch = response.data
                } else {
                    allPlanStatusForLabelForAdvancedSearch = {}
                }
            }
        })
        APIFactory.getSystemQATestingStatusList({}, function (response) {
            if (response) {
                if (response.status) {
                    allSystemQAStatus = response.data
                } else {
                    allSystemQAStatus = {}
                }
            }
        })
        if (getUserData('user') && getUserData('user').role.indexOf("Developer") >= 0) {
            APIFactory.isOBS(function (response) {
                if (response) {
                    if (response.status) {
                        isOBS = true
                    } else {
                        isOBS = false
                    }
                }
            })
        }
    }

    var getAllPlanStatus = function () {
        return allPlanStatus
    }
    Access.getAllPlanStatus = getAllPlanStatus;

    var getAllPlanStatusForLabel = function () {
        return allPlanStatusForLabel
    }
    Access.getAllPlanStatusForLabel = getAllPlanStatusForLabel;

    var getPlanStatusListForLabelForAdvancedSearch = function () {
        return allPlanStatusForLabel
    }
    Access.getPlanStatusListForLabelForAdvancedSearch = getPlanStatusListForLabelForAdvancedSearch;

    var getAllSystemQAStatus = function () {
        return allSystemQAStatus
    }
    Access.getAllSystemQAStatus = getAllSystemQAStatus;

    var getisOBS = function () {
        return isOBS
    }
    Access.getisOBS = getisOBS;

    var getDefaultMenus = function (cbk) {
        if (Object.keys(allPlanStatus).length == 0) { // Collect all data on successfull login
            getOnloadData()
        }
        var leadMenu = [
            /* {
                title: "My Tasks",
                icon: "fa fa-inbox",
                state: "myTasks"
            }, */
            {
                title: "Inbox",
                icon: "file-text",
                state: "impPlan"
            },
            {
                title: "New Implementation Plan",
                icon: "fa fa-file-o",
                state: "newImpPlan"
            },
            {
                title: "E-Type Deployment Screen",
                icon: "fa fa-rocket",
                state: "leadDeployment"
            },
            {
                title: "Aux Deployment Screen",
                icon: "fa fa-bolt",
                state: "leadAuxDeploymentScreen"
            },
            {
                title: "zTPF Level Details",
                icon: "fa fa-server",
                state: "pLDeploymentDetails"
            },
            {
                title: "Functional Packages",
                icon: "fa fa-lock",
                state: "repositoryOwners"
            },
            {
                title: "Utilities",
                icon: "fa fa-flag",
                state: 'utilitiesReport',
                children: [
                    {
                        title: "Report",
                        icon: "fa fa-flag",
                        state: "utilitiesReport.report"
                    },
					// {
                        // title: "Performance",
                        // icon: "fa fa-fighter-jet",
                        // state: "utilitiesReport.performanceReport",
						// children: [
						// {
							// title: "Transaction View",
							// icon: "fa fa-exchange",
							// state: "utilitiesReport.performanceReport.transactionView",
						// },
						// {
							// title: "System View",
							// icon: "fa fa-tablet",
							// state: "utilitiesReport.performanceReport.systemView",
						// },
						
					// ]
                    // },
					
				]
            },
            {
                title: "Freeze Date",
                icon: "fa fa-calendar",
                state: "freezeDateList"
            },
            {
                title: "Display Build Queue",
                icon: "fa fa-flag",
                state: "buildQueue"
            },
            {
                title: "Search",
                icon: "fa fa-search",
                state: "search"
            }

        ];
        if (appSettings.isDeltaApp == 'true') {
            _.where(leadMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var developerMenu = [
            // {
            //     title: "My Tasks",
            //     icon: "fa fa-inbox",
            //     state: "myTasks"
            // },
            {
                title: "Inbox",
                icon: "file-text",
                state: "impl"
            },
            {
                title: "zTPF Level Details",
                icon: "fa fa-server",
                state: "pLDeploymentDetails"
            },
            {
                title: "Functional Packages",
                icon: "fa fa-lock",
                state: "repositoryOwners"
            },
            {
                title: "Utilities",
                icon: "fa fa-flag",
                state: 'utilitiesReport',
                children: [
                    {
                        title: "Report",
                        icon: "fa fa-flag",
                        state: "utilitiesReport.report"
                    },
					// {
                        // title: "Performance",
                        // icon: "fa fa-fighter-jet",
                        // state: "utilitiesReport.performanceReport",
						// children: [
						// {
							// title: "Transaction View",
							// icon: "fa fa-exchange",
							// state: "utilitiesReport.performanceReport.transactionView",
						// },
						// {
							// title: "System View",
							// icon: "fa fa-tablet",
							// state: "utilitiesReport.performanceReport.systemView",
						// },
						
					// ]
                    // },
					
				]
            },
            {
                title: "Freeze Date",
                icon: "fa fa-calendar",
                state: "freezeDateList"
            },
            {
                title: "Display Build Queue",
                icon: "fa fa-flag",
                state: "buildQueue"
            },
            {
                title: "Search",
                icon: "fa fa-search",
                state: "search"
            }

        ];

        if (Access.getisOBS()) {
            developerMenu.push({
                title: "Move artifacts from <br/> &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; OBS Functional Area",
                icon: "fas fa-chevron-circle-right",
                state: "moveArtifacts"
            })
        }

        if (appSettings.isDeltaApp == 'true') {
            _.where(developerMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }


        var loadsControllerMenu = [{
            title: "Inbox",
            icon: "fa fa-inbox",
            state: "myTasks"
        }, {
            title: "Load History",
            icon: "fa fa-history",
            state: "loadsControlHistory"
        },
        {
            title: "Load Categories",
            icon: "fa fa-folder",
            state: "loadCategoriesList"
        },
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ]

        if (appSettings.isDeltaApp == 'true') {
            _.where(loadsControllerMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })

        }



        var managerMenu = [{
            title: "Inbox",
            icon: "fa fa-inbox",
            state: "myTasks"
        }, {
            title: "Approve Plans",
            icon: "file-text",
            state: "approveReject"
        }];

        var reviewerMenu = [{
            title: "Review Tasks",
            icon: "fa fa-inbox",
            state: "reviewTasks"
        },
        {
            title: "Approved Tasks",
            icon: "fa fa-history",
            state: "approvedReviewTasks"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ];

        if (appSettings.isDeltaApp == 'true') {
            _.where(reviewerMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var devManagerMenu = [{
            title: "Inbox",
            icon: "fa fa-inbox",
            state: "devManager"
        },
        {
            title: "Access Permissions",
            icon: "fa fa-arrow-circle-right",
            state: "accessPermission"
        },
        {
            title: "Assigned Plan",
            icon: "fa fa-share",
            state: "assignedPlan"
        },
        {
            title: "Fallback Macro/Header Plan",
            icon: "fa fa-bolt",
            state: "fbmacroPlan"
        },
        {
            title: "Move artifacts across <br/> &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; Functional Area",
            icon: "fas fa-chevron-circle-right",
            state: "moveArtifacts"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ];

        if (appSettings.isDeltaApp == 'true') {
            devManagerMenu.push({
                title: "Proj Nbr Maintenance",
                icon: "fas fa-table",
                state: "dtnMaintenance"
            })
        }

        if (appSettings.isDeltaApp == 'true') {
            _.where(devManagerMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var tsdMenu = [{
            title: "Activate Loadset",
            icon: "fa fa-inbox",
            state: "myTasks"
        }, {
            title: "Accept Loadset",
            icon: "fa fa-bolt",
            state: "loadsetReady"
        }, {
            title: "Online FeedBack Queue",
            icon: "fa fa-bolt",
            state: "onlineFeedback"
        }, {
            title: "Activate Fallback Loadset",
            icon: "fa fa-chain-broken",
            state: "activateFallback"
        }, {
            title: "Accept Fallback Loadset",
            icon: "fa fa-bolt",
            state: "acceptFallback"
        }, {
            title: "Manage default CPU",
            icon: "fa fa-hdd-o",
            state: "defultcpu"
        }, {
            title: "Sync Implementation Plan",
            icon: "fa fa-exchange",
            state: "syncPlan"
        },
        {
            title: "Fallback Macro/Header Plan",
            icon: "fa fa-bolt",
            state: "fbmacroPlan"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ];

        if (appSettings.isDeltaApp == 'true') {
            _.where(tsdMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var qaMenu = [{
            title: "Inbox",
            icon: "fa fa-inbox",
            state: "qaTasks"
        },
        {
            title: "E-Type Deployment Screen",
            icon: "fa fa-rocket",
            state: "qaDeployment"
        },
        {
            title: "Aux Deployment Screen",
            icon: "fa fa-bolt",
            state: "qaAuxDeploymentScreen"
        },
        {
            title: "Passed QA Regression Testing",
            icon: "fa fa-list",
            state: "passedQARegressionScreen"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ];

        if (appSettings.isDeltaApp == 'true') {
            _.where(qaMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var qaDeployedMenu = [{
            title: "Inbox",
            icon: "fa fa-inbox",
            state: "qaTasks"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ];

        if (appSettings.isDeltaApp == 'true') {
            _.where(qaDeployedMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var DLCoreChangeTeamMenu = [{
            title: "Inbox",
            icon: "fa fa-inbox",
            state: "rfcChangeMgmt"
        },
        {
            title: "Report Generation",
            icon: "fa fa-history",
            state: "reportGeneration"
        },
        {
            title: "RFC Approved Plans",
            icon: "fa fa-calendar",
            state: "rfcApprovedPlans"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},
				// {
					// title: "Performance",
					// icon: "fa fa-fighter-jet",
					// state: "utilitiesReport.performanceReport",
					// children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					// {
						// title: "System View",
						// icon: "fa fa-tablet",
						// state: "utilitiesReport.performanceReport.systemView",
					// },
					
				// ]
				// },
				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "Search",
            icon: "fa fa-search",
            state: "search"
        }
        ];

        if (appSettings.isDeltaApp == 'true') {
            _.where(DLCoreChangeTeamMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var systemSupportMenu = [];
        if (appSettings.isDeltaApp == 'true') {
            systemSupportMenu.push({
                title: "E-Type Deployment Screen",
                icon: "fa fa-rocket",
                state: "tssYodaDeployment"
            })
            systemSupportMenu.push({
                title: "Aux Deployment Screen",
                icon: "fa fa-bolt",
                state: "tssAuxDeploymentYoda"
            })
        }
        if (appSettings.isTravelportApp == 'true') {
            systemSupportMenu.push({
                title: "E-Type Deployment Screen",
                icon: "fa fa-rocket",
                state: "tssTosDeployment"
            })
            systemSupportMenu.push({
                title: "Aux Deployment Screen",
                icon: "fa fa-bolt",
                state: "tssAuxDeploymentTos"
            })
        }
        systemSupportMenu.push({
            title: "Plans Deployed in Pre-Prod",
            icon: "fa fa-list",
            state: "plansDeployedInPreProd"
        },
            {
                title: "zTPF Level Details",
                icon: "fa fa-server",
                state: "pLDeploymentDetails"
            }, {
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        },
            {
                title: "Utilities",
                icon: "fa fa-flag",
                state: 'utilitiesReport',
                children: [
                    {
                        title: "Report",
                        icon: "fa fa-flag",
                        state: "utilitiesReport.report"
                    },
					// {
                        // title: "Performance",
                        // icon: "fa fa-fighter-jet",
                        // state: "utilitiesReport.performanceReport",
						// children: [
						// {
							// title: "Transaction View",
							// icon: "fa fa-exchange",
							// state: "utilitiesReport.performanceReport.transactionView",
						// },
						// {
							// title: "System View",
							// icon: "fa fa-tablet",
							// state: "utilitiesReport.performanceReport.systemView",
						// },
						
					// ]
                    // },
					
				]
            },{
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
            {
                title: "Display Build Queue",
                icon: "fa fa-flag",
                state: "buildQueue"
            },
            {
                title: "Search",
                icon: "fa fa-search",
                state: "search"
            })

        if (appSettings.isDeltaApp == 'true') {
            _.where(systemSupportMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }

        var toolAdminMenu = [{
            title: "Functional Packages",
            icon: "fa fa-lock",
            state: "repositoryOwners"
        }, {
            title: "Manage Delegates",
            icon: "fa fa-share",
            state: "manageDelegate"
        }, {
            title: "Create new repositories",
            icon: "fa fa-file",
            state: "createNewRepo"
        },
        {
            title: "zTPF Level Details",
            icon: "fa fa-server",
            state: "pLDeploymentDetails"
        },
        {
			title: "Utilities",
			icon: "fa fa-flag",
			state: 'utilitiesReport',
			children: [
				{
					title: "Report",
					icon: "fa fa-flag",
					state: "utilitiesReport.report"
				},				
			]
		},
        {
            title: "Freeze Date",
            icon: "fa fa-calendar",
            state: "freezeDateList"
        },
        {
            title: "Display Build Queue",
            icon: "fa fa-flag",
            state: "buildQueue"
        },
        {
            title: "System Information",
            icon: "file-text",
            state: "help"
        }]

        if (appSettings.isDeltaApp == 'true') {
            _.where(toolAdminMenu, { title: "Utilities" })[0].children.push({
                title: "Proj Nbr Display",
                icon: "fa fa-television",
                state: "utilitiesReport.projNbrDisplay"
            })
        }
		
		if(getUserData('userRole') == 'ToolAdmin') {
			_.where(toolAdminMenu, { title: "Utilities" })[0].children.push({
					title: "Performance",
					icon: "fa fa-fighter-jet",
					state: "utilitiesReport.performanceReport",
					children: [
					// {
						// title: "Transaction View",
						// icon: "fa fa-exchange",
						// state: "utilitiesReport.performanceReport.transactionView",
					// },
					{
						title: "System View",
						icon: "fa fa-tablet",
						state: "utilitiesReport.performanceReport.systemView",
					}]
				})
		} else {
			console.log("false")
		}

        if (getUserData('userRole') === 'Lead') {
            cbk(leadMenu);
        } else if (getUserData('userRole') === 'Developer') {
            cbk(developerMenu);
        } else if (getUserData('userRole') === 'LoadsControl') {
            cbk(loadsControllerMenu);
        } else if (getUserData('userRole') === 'Reviewer') {
            cbk(reviewerMenu);
        } else if (getUserData('userRole') === 'DevManager') {
            cbk(devManagerMenu);
        } else if (getUserData('userRole') === 'QA') {
            cbk(qaMenu);
        } else if (getUserData('userRole') === 'QADeployLead') {
            cbk(qaDeployedMenu);
        } else if (getUserData('userRole') === 'DLCoreChangeTeam') {
            cbk(DLCoreChangeTeamMenu);
        } else if (getUserData('userRole') === 'SystemSupport') {
            cbk(systemSupportMenu);
        } else if (getUserData('userRole') === 'TechnicalServiceDesk') {
            cbk(tsdMenu);
        } else if (getUserData('userRole') === 'ToolAdmin') {
            cbk(toolAdminMenu);
        }
    }

    Access.getDefaultMenus = getDefaultMenus;

    /*
        Plan Level Actions - Edit, create implementation
    */
    var getPlanStatusList = function () {
        var status = {
            "ACTIVE": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-primary"]
            }
        }
    };
    Access.getPlanStatusList = getPlanStatusList;

    var getActionPermissionList = function (currentUserId, iObj) {
        var currentRole = getUserData("userRole");
        return ((iObj.devId == currentUserId && currentRole == 'Developer' || iObj.planId.leadId == currentUserId && currentRole == 'Lead'))
    }
    Access.getActionPermissionList = getActionPermissionList;

    var getShowGIButton = function (pObj, iObj) {
        var currentName = getUserData("displayName");
        return ((iObj.impStatus == "IN_PROGRESS" && currentName == iObj.devName && $rootScope.GiPort > 0))
    }
    Access.getShowGIButton = getShowGIButton;

    // search dashboard content
    var searchData = {};
    var getSearchData = function () {
        return searchData;
    }
    Access.getSearchData = getSearchData;

    var clearSearchData = function () {
        searchData = {};
    }
    Access.clearSearchData = clearSearchData;

    var getPlanActionByRole = function () {

        var roleActionMap = {

            "ACTIVE": {
                "ACCESS_PERMISSIONS": ["edit", "delete", "submit", "create_implementation"],
                "LABEL_TYPE": ["label label-primary"]
            },
            "APPROVED": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "BYPASSED_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "BYPASSED_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DELETED": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "FALLBACK": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "ONLINE": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "PARTIAL_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PARTIAL_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_ACCEPTANCE_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "READY_FOR_PRODUCTION_DEPLOYMENT": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PARTIALLY_DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "ONLINE_RELOAD": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "REJECTED": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "SUBMITTED": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "FALLBACK_DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            },
            "PENDING_FALLBACK": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            },
            "ACCEPTED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            }
        }

        return roleActionMap;
    }
    Access.getPlanActionByRole = getPlanActionByRole;

    var getPlanActionByRoleForLabel = function () {

        var roleActionMap = {

            "ACTIVE": {
                "ACCESS_PERMISSIONS": ["edit", "delete", "submit", "create_implementation"],
                "LABEL_TYPE": ["label label-primary"]
            },
            "APPROVED": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "BYPASSED_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "BYPASSED_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DELETED": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "FALLBACK": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "ONLINE": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "PARTIAL_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PARTIAL_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_ACCEPTANCE_TESTING": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "READY_FOR_PRODUCTION_DEPLOYMENT": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            }, 
            "PARTIALLY_DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_PRE_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "ONLINE_RELOAD": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "REJECTED": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "SUBMITTED": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEV_MGR_APPROVED": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_QA_FUNCTIONAL": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_QA_REGRESSION": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "FALLBACK_DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            },
            "PENDING_FALLBACK": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "ACCEPTED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            }
        }

        return roleActionMap;
    }
    Access.getPlanActionByRoleForLabel = getPlanActionByRoleForLabel;

    var getPlanActionByRoleForLabelForAdvancedSearch = function () {

        var roleActionMap = {

            "ACTIVE": {
                "ACCESS_PERMISSIONS": ["edit", "delete", "submit", "create_implementation"],
                "LABEL_TYPE": ["label label-primary"]
            },
            "APPROVED": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "BYPASSED_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "BYPASSED_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DELETED": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "FALLBACK": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "ONLINE": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "PARTIAL_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PARTIAL_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_ACCEPTANCE_TESTING": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_FUNCTIONAL_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PASSED_REGRESSION_TESTING": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "READY_FOR_PRODUCTION_DEPLOYMENT": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PARTIALLY_DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "ONLINE_RELOAD": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "REJECTED": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-danger"]
            },
            "SUBMITTED": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEV_MGR_APPROVED": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "PENDING": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-info"]
            },
            "DEPLOYED_IN_QA_FUNCTIONAL": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_QA_REGRESSION": {
                "ACCESS_PERMISSIONS": ["reject", "edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "DEPLOYED_IN_PRE_PRODUCTION": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "FALLBACK_DEPLOYED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            },
            "PENDING_FALLBACK": {
                "ACCESS_PERMISSIONS": ["edit"],
                "LABEL_TYPE": ["label label-success"]
            },
            "ACCEPTED_IN_PRODUCTION": {
                "ACCESS_PERMISSIONS": [],
                "LABEL_TYPE": ["label label-success"]
            }

        }

        return roleActionMap;
    }
    Access.getPlanActionByRoleForLabelForAdvancedSearch = getPlanActionByRoleForLabelForAdvancedSearch;

    var getImpActionByRole = function () {

        var statusLabelMap = {

            "IN_PROGRESS": {
                "LABEL_TYPE": ["label label-primary"]
            },
            "READY_FOR_QA": {
                "LABEL_TYPE": ["label label-success"]
            },
            "PEER_REVIEW_COMPLTED": {
                "LABEL_TYPE": ["label label-warning"]
            },
            "INTEGRATION_TESTING_COMPLETED": {
                "LABEL_TYPE": ["label label-success"]
            },
            "UNIT_TESTING_COMPLETED": {
                "LABEL_TYPE": ["label label-primary"]
            }
        }

        return statusLabelMap;

    }
    Access.getImpActionByRole = getImpActionByRole;

    var formatAPIDate = function (dateToBeFormatted) {
        if (_.isObject(dateToBeFormatted)) {
            return dateToBeFormatted.tz(getTimeZone()).format(appSettings.apiDTFormat)
        }
        return moment(dateToBeFormatted).tz(getTimeZone()).format(appSettings.apiDTFormat)
    }
    Access.formatAPIDate = formatAPIDate

    var formatUIDate = function (dateToBeFormatted) {
        if (_.isObject(dateToBeFormatted)) {
            return dateToBeFormatted.tz(getTimeZone()).format(appSettings.uiDTFormat)
        }
        return moment(dateToBeFormatted).tz(getTimeZone()).format(appSettings.uiDTFormat)
    }
    Access.formatUIDate = formatUIDate

    var refactorTimeZone = function (dateToBeFormatted) {
        return dateToBeFormatted;
    }
    Access.refactorTimeZone = refactorTimeZone

    return Access;

});
