import BasePage from '../Common/basePage';
import adldeploymentinput from '../../data/adldeploymentinput';
import findid from '../../data/findid';

class AdlDeployment extends BasePage {
    constructor() {
        super();
        this.adlDeploymentScreen = element(by.css('[href="#/app/deployment"]'));
        this.submitDeploymentChanges = element(by.css('[aria-label="submitDeploymentChanges"]'))
        this.refreshDeploymentChanges = element(by.css('[aria-label="refreshDeploymentChanges""]'))
    }

    ajaxRequest(cbk) {
        var http = require('http');
        var json_data;
        http.get('https://vhlqaztdt001.tvlport.net:8443/WorkFlowAPI/access/postTestSystemLoad', function (response) {
            var bodyString = '';
            response.setEncoding('utf8');
            response.on("data", function (chunk) {
                bodyString += chunk;
            });

            response.on('end', function () {
                json_data = bodyString;
                console.log("1---->" + json_data);
                console.log("2---->" + json_data);
                cbk()
            });

        }).on('error', function (e) {
            console.log("There is an error in GET request");
            cbk()
        });
    }

    deactivateCheckbox(activationstatus, cbk) {
        console.log("Yoda De-activation in progress")
        var deactivate = element(by.css('[aria-label="deactivate_' + findid.capture.planidcapture + '_0"]'))
        this.singlebuttonclick(deactivate)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_SUCCESS_0"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda De-activation done successfully")
                cbk(true)
            } else {
                cbk(false)
            }
        })

        element(by.css('[aria-label="' + findid.capture.planidcapture + '_FAILED_0"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("FTP and Yoda exeution Failed")
                cbk(true)
            } else {
                cbk(false)
            }

        })
    }

    statusWait(cbk) {
        console.log("Yoda Load and Activation in progress")
        browser.wait(this.isNotVisible($('' + findid.capture.planidcapture + '_NULL_0')), this.timeout.full);
        browser.waitForAngular();
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_SUCCESS_0"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("yoda load and activate done successfully", "")
                cbk(true)
            } else {
                cbk(false)
            }

        })
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_FAILED_0"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("yoda load and activate exeution Failed")
                cbk(true)
            } else {
                cbk(false)
            }

        })
    }

    refreshDeploymentButton() {
        this.singlebuttonclick(this.refreshDeploymentChanges)
    }

    submitDeploymentButton() {
        this.singlebuttonclick(this.submitDeploymentChanges)
    }

    deactivateAndDeleteCheckbox(activationstatus, cbk) {
        console.log("Yoda Deativation and Delete in progress")
        var deactivateAndDelete = element(by.css('[aria-label="deactivateandDelete_' + findid.capture.planidcapture + '_0"]'))
        this.singlebuttonclick(deactivateAndDelete)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_SUCCESS_0"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda Deativation and Delete done successfully")
                cbk()
            } else {
                console.log("activation not done,Timeout")
                cbk()
            }
        })
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_FAILED_0"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = falas
                console.log("Yoda Deativation and Delete Failed")
                cbk()
            }
        })
    }

    activateCheckbox(activationstatus, cbk) {
        console.log("Yoda Activation in progress")
        var activate = element(by.css('[aria-label="activate_' + findid.capture.planidcapture + '_0"]'))
        this.singlebuttonclick(activate)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_SUCCESS_0"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda Activation done successfully")
                cbk()
            } else {
                console.log("activation not done,Timeout")
                cbk()
            }

        })
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_FAILED_0"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = false
                console.log("Yoda Activation Failed")
                cbk()
            }
        })
    }

    deactivateCheckbox(activationstatus, cbk) {
        console.log(activationstatus)
        console.log("Yoda Deativation in progress")
        var deactivate = element(by.css('[aria-label="deactivate_' + findid.capture.planidcapture + '_0"]'))
        this.singlebuttonclick(deactivate)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_SUCCESS_0"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda Deativation done successfully")
                cbk()
            } else {
                console.log("activation not done,Timeout")
                cbk()
            }

        })
        element(by.css('[aria-label="' + findid.capture.planidcapture + '_FAILED_0"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = false
                console.log("Yoda Deativation Failed")
                cbk()
            }
        })
    }

    loadAndActivateCheckbox() {
        var loadAndActivate = element(by.css('[aria-label="loadandActivate_' + findid.capture.planidcapture + '_0"]'))
        this.singleListSelect(loadAndActivate)
    }

    updateDSLCheckbox() {
        var updateDSL = element(by.css('[aria-label="updateDSL_' + findid.capture.planidcapture + '_0"]'))
        this.singleListSelect(updateDSL)
    }

    deleteDeploymentButton(capturedid) {
        var deleted = element(by.css('[aria-label="deleteRow_' + capturedid + '_0"]'));
        this.singlebuttonclick(deleted)
    }

    detailViewSelect() {
        var detailview = element(by.css('[aria-label="' + findid.capture.planidcapture + '"]'))
        this.singleListSelect(detailview)
    }

    adldeploymentTab() {
        this.tabSelection(this.adlDeploymentScreen)
    }

    addTargetSystemSelect() {
        var addTargetSystem = element(by.css('[aria-label="addRow_' + findid.capture.planidcapture + '"]'))
        this.singleListSelect(addTargetSystem)
    }

    targetSystemSelect() {
        var selectSystem = element(by.css('[aria-label="TargetSystem_' + findid.capture.planidcapture + '_0"]'))
        var sysid = selectSystem.$('[value="' + findid.capture.systemid + '"]')
        this.singleListSelect(sysid)
    }

    vparsIdSelect() {
        var vparsId = element(by.css('[aria-label="testSystem_' + findid.capture.planidcapture + '_0"]'))
        var vpar = vparsId.$('[value="' + findid.capture.vparid + '"]')
        this.singleListSelect(vpar)
    }

}

export default new AdlDeployment(); 