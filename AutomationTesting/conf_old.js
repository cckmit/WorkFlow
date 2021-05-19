require("babel-register")({
    presets: ['es2015']
});


var HtmlScreenshotReporter = require('protractor-jasmine2-screenshot-reporter');

var reporter = new HtmlScreenshotReporter({
    dest: '',
    filename: 'Workflow-report.html',
    //  metadataBuilder: function(currentSpec, suites, browserCapabilities) {
    //     return { id: currentSpec.id, os: browserCapabilities.get('browserName') };
    //   },
    reportOnlyFailedSpecs: false,
    captureOnlyFailedSpecs: false,
    showSummary: true,
    showQuickLinks: true,
    showConfiguration: true,
    preserveDirectory: true,
    takeScreenShotsForSkippedSpecs: true,
    reportTitle: "TravelPort Automation Testing",
    configurationStrings: {
        logUrlOnFailure: true,
        logBrowserConsoleOnFailure: true
    },
});


exports.config = {

    // seleniumServerJar: "node_modules/protractor/node_modules/webdriver-manager/selenium/selenium-server-standalone-3.4.0.jar",
    directConnect: true,
    seleniumAddress: 'http://localhost:4444/wd/hub',
    jasmineNodeOpts: {
        defaultTimeoutInterval: 4500000,
        showTiming: true,
    },
    /* suites: {
        homepage1: 'spec/LeadSpec.js',
        homepage2: 'spec/LeadSpec.js',
        // search: ['spec/DevSpec.js','spec/BuildAndCompileSpec.js']
      }, */
    // specs: ['spec/DevSpec.js','spec/BuildAndCompileSpec.js','spec/ManagerSpec.js','spec/AdlDeploymentSpec.js','spec/QADeploymentSpec.js','spec/SystemSupportDeploymentSpec.js','spec/PreprodAndAcceptanceSpec.js','spec/ReadyForProductionSpec.js'],
    // specs: ['spec/LeadSpec.js','spec/DevSpec.js','spec/BuildAndCompileSpec.js','spec/ManagerSpec.js','spec/AdlDeploymentSpec.js','spec/QADeploymentSpec.js','spec/SystemSupportDeploymentSpec.js','spec/PreprodAndAcceptanceSpec.js','spec/ReadyForProductionSpec.js','spec/ServiceDeskSpec.js'],
    // specs: ['spec/LeadSpec.js'],
    specs: ['spec/ServiceDeskSpec.js'],
    // baseUrl: 'http://localhost:8080/src/app/index.html#/login',
    baseUrl: 'https://vhlqaztdt001.tvlport.net:8443/WorkFlow/#/login',
    framework: 'jasmine',
    "stopSpecOnExpectationFailure": false,
    /* 
e        onPrepare: () => {
            // set browser size...
            browser.manage().window().setSize(1024, 800);
    
            const SpecReporter = require('jasmine-spec-reporter');
            jasmine.getEnv().addReporter(new SpecReporter({ displayStacktrace: 'specs' }));
        },
     */
    allScriptsTimeout: 200000,
    capabilities: {
        browserName: 'chrome',
        shardTestFiles: false,
        maxInstances: 1,
        chromeOptions: {
            args: [
            //      "--headless", 'no-sandbox', "--disable-gpu", "--window-size=800x600",
                // disable chrome's wakiness
                '--disable-infobars',
                '--disable-extensions',
                'verbose',
                'log-path=/tmp/chromedriver.log'
            ],
            prefs: {
                // disable chrome's annoying password manager
                'profile.password_manager_enabled': false,
                'credentials_enable_service': false,
                'password_manager_enabled': false
            }
        }
    },

    jasmineNodeOpts: {
        showColors: true,
        displaySpecDuration: true,
        // overrides jasmine's print method to report dot syntax for custom reports
        print: () => { },
        defaultTimeoutInterval: 4500000
    },
    /* public void setupTest() {
        ChromeDriverManager.getInstance().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu");
        driver =  new ChromeDriver( options );
    } */

    // Setup the report before any tests start
    beforeLaunch: function () {
        return new Promise(function (resolve) {
            reporter.beforeLaunch(resolve);
        });

    },

    // Assign the test reporter to each running instance
    onPrepare: function () {
        jasmine.getEnv().addReporter(reporter);
          /* global angular: false, browser: false, jasmine: false */

    // Disable animations so e2e tests run more quickly
    var disableNgAnimate = function() {
        angular.module('disableNgAnimate', []).run(['$animate', function($animate) {
          $animate.enabled(false);
        }]);
      };
  
      browser.addMockModule('disableNgAnimate', disableNgAnimate);
  
    },

    // Close the report after all tests finish
    afterLaunch: function (exitCode) {
        return new Promise(function (resolve) {
            reporter.afterLaunch(resolve.bind(this, exitCode));
        });
    },

};

