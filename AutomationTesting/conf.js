
require("babel-register")({
    presets: ['es2015']
});


var HtmlScreenshotReporter = require('protractor-jasmine2-screenshot-reporter');
var today = new Date(),
    timeStamp = today.getMonth() + 1 + '/' + today.getDate() + '/' + today.getFullYear() + ' - ' + today.getHours() + 'h-' + today.getMinutes() + 'm';

var reporter = new HtmlScreenshotReporter({
    dest: '',
    filename: 'Workflow-report.html',
    //  metadataBuilder: function(currentSpec, suites, browserCapabilities) {
    //     return { id: currentSpec.id, os: browserCapabilities.get('browserName') };
    //   },
    showTiming: true,
    reportOnlyFailedSpecs: false,
    captureOnlyFailedSpecs: false,
    showSummary: true,
    showQuickLinks: true,
    showConfiguration: true,
    preserveDirectory: true,
    takeScreenShotsForSkippedSpecs: true,
    reportTitle: "TravelPort Automation Testing -- Test run on: " + timeStamp,
    configurationStrings: {
        logUrlOnFailure: true,
        logBrowserConsoleOnFailure: true
    },
});


exports.config = {


    seleniumServerJar: "node_modules/protractor/node_modules/webdriver-manager/selenium/selenium-server-standalone-3.4.0.jar",
    directConnect: true,
    seleniumAddress: 'http://localhost:4444/wd/hub',
    jasmineNodeOpts: {
        defaultTimeoutInterval: 1000000,
        showTiming: true,
    },
    params: {
        glob: 'tryit'
    },
    suites: {
        plan: 'spec/LeadSpec.js',
        work: ['spec/BuildAndCompileSpec.js', 'spec/ManagerSpec.js', 'spec/QADeploymentSpec.js'],
        tsd: 'spec/ServiceDeskSpec.js',
        adldeploy: 'spec/AdlDeploymentSpec.js',
        e2e: ['spec/LeadSpec.js', 'spec/DevSpec.js', 'spec/BuildAndCompileSpec.js', 'spec/IntegrationAndReadyForQA.js', 'spec/AdlSubmit.js', 'spec/ManagerSpec.js', 'spec/QADeploymentSpec.js', 'spec/ReadyForProductionSpec.js', 'spec/ServiceDeskSpec.js'],
        e2edeploy: ['spec/LeadSpec.js', 'spec/DevSpec.js', 'spec/BuildAndCompileSpec.js', 'spec/IntegrationAndReadyForQA.js', 'spec/AdlSubmit.js', 'spec/ManagerSpec.js', 'spec/AdlDeploymentSpec.js', 'spec/QADeploymentSpec.js', 'spec/PreprodAndAcceptanceSpec.js', 'spec/ReadyForProductionSpec.js', 'spec/ServiceDeskSpec.js'],
        adl: 'spec/AdlDeploymentSpec.js',
        build: 'spec/BuildAndCompileSpec.js',
        manager: 'spec/ManagerSpec.js',
        dev: 'spec/DevSpec.js',
        inter: 'spec/IntegrationAndReadyForQA.js',
        support: 'spec/SystemSupportDeploymentSpec.js',
        qa: ['spec/QADeploymentSpec.js', 'spec/PreprodAndAcceptanceSpec.js', 'spec/ReadyForProductionSpec.js'],
        ready: 'spec/ReadyForProductionSpec.js',
        submit: 'spec/AdlSubmit.js',
        accept: 'spec/PreprodAndAcceptanceSpec.js',
        ajax: 'spec/google.js',
        // search: ['spec/DevSpec.js','spec/BuildAndCompileSpec.js']
        sce2 :[''],
    },
    // specs: ['spec/DevSpec.js','spec/BuildAndCompileSpec.js','spec/ManagerSpec.js','spec/AdlDeploymentSpec.js','spec/QADeploymentSpec.js','spec/SystemSupportDeploymentSpec.js','spec/PreprodAndAcceptanceSpec.js','spec/ReadyForProductionSpec.js'],
    specs: ['spec/LeadSpec.js', 'spec/DevSpec.js', 'spec/BuildAndCompileSpec.js', 'spec/ManagerSpec.js', 'spec/AdlDeploymentSpec.js', 'spec/QADeploymentSpec.js', 'spec/SystemSupportDeploymentSpec.js', 'spec/PreprodAndAcceptanceSpec.js', 'spec/ReadyForProductionSpec.js', 'spec/ServiceDeskSpec.js'],
    // specs: ['spec/QADeploymentSpec.js','spec/PreprodAndAcceptanceSpec.js','spec/ReadyForProductionSpec.js'],
    
    // baseUrl: 'http://localhost:8080/src/app/index.html#/login',
    baseUrl: 'https://vhlqaztdt001.tvlport.net:8443/WorkFlow/#/login',
    framework: 'jasmine',
    "stopSpecOnExpectationFailure": true,
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
        displayStacktrace: 'all',
        browserName: 'chrome',
        shardTestFiles: false,
        maxInstances: 1,
        chromeOptions: {
            args: [
                //  "--headless", 'no-sandbox', "--disable-gpu", "--window-size=800x600",
                // disable chrome's wakiness
                '--start-maximized',
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
        //fail fast
        realtimeFailure: true,
        // overrides jasmine's print method to report dot syntax for custom reports
        print: () => { },
        defaultTimeoutInterval: 450000
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
        var disableNgAnimate = function () {
            angular.module('disableNgAnimate', []).run(['$animate', function ($animate) {
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

/* var fs = require('fs');
var util = require('util');
var log_file = fs.createWriteStream(__dirname + '/debug.log', { flags: 'w' });
var log_stdout = process.stdout;

console.log = function (d) {
    // console.warn(d)
    if (d.toString() && d.toString().indexOf("[") < 0) {
        log_file.write(d + '\n');
        log_stdout.write(d + '\n');
    }
}; */