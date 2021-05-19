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

    deactivateCheckbox(activationstatus, capturedid, captureindex, i, cbk) {
        var deactivate = element(by.css('[aria-label="deactivate_' + capturedid + '"]'))
        this.singlebuttonclick(deactivate)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + captureindex + '_SUCCESS_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda De-activation done successfully")
                cbk(true)
            } /* else {
                cbk(false)
            } */
        })

        element(by.css('[aria-label="' + captureindex + '_FAILED_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = false
                console.log("FTP and Yoda exeution Failed")
                cbk()
            } /* else {
                cbk(false)
            } */

        })
    }

    statusWait(capturedid, i, cbk) {
        browser.wait(this.isNotVisible($('' + capturedid + '_NULL_' + i + '')), this.timeout.full);
        browser.waitForAngular();
        element(by.css('[aria-label="' + capturedid + '_SUCCESS_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("yoda load and activate done successfully")
                cbk(true)
            } /* else {
                cbk(false)
            } */

        })
        element(by.css('[aria-label="' + capturedid + '_FAILED_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = false
                console.log("yoda load and activate exeution Failed")
                cbk()
            } /* else {
                cbk(false)
            } */

        })

    }

    refreshDeploymentButton() {
        this.singlebuttonclick(this.refreshDeploymentChanges)
    }

    submitDeploymentButton() {
        this.singlebuttonclick(this.submitDeploymentChanges)
    }

    deactivateAndDeleteCheckbox(activationstatus, capturedid, captureindex, i, cbk) {
        var deactivateAndDelete = element(by.css('[aria-label="deactivateandDelete_' + capturedid + '"]'))
        this.singlebuttonclick(deactivateAndDelete)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + captureindex + '_SUCCESS_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda Deativation and Delete done successfully")
                //set status false after every itearation complete
                findid.capture.adlactivationstatus = false
                cbk()
            }

        })
        element(by.css('[aria-label="' + captureindex + '_FAILED_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = false
                console.log("Yoda Deativation and Delete Failed")
                cbk()
            }
        })
    }

    activateCheckbox(activationstatus, capturedid, captureindex, i, cbk) {
        var activate = element(by.css('[aria-label="activate_' + capturedid + '"]'))
        this.singlebuttonclick(activate)
        element(by.css('[aria-label="submitDeploymentChanges"]')).click();
        browser.waitForAngular();
        element(by.css('[aria-label="' + captureindex + '_SUCCESS_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = isPresent
                console.log("Yoda Activation done successfully")
                cbk()
            }

        })
        element(by.css('[aria-label="' + captureindex + '_FAILED_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.adlactivationstatus = false
                console.log("Yoda Activation Failed")
                cbk()
            }
        })
    }

    /*  deactivateCheckbox(activationstatus, capturedid, cbk) {
         var deactivate = element(by.css('[aria-label="deactivate_' + capturedid + '_0"]'))
         this.singlebuttonclick(deactivate)
         console.log("Yoda Deativation in progress")
         element(by.css('[aria-label="submitDeploymentChanges"]')).click();
         browser.waitForAngular();
         element(by.css('[aria-label="' + capturedid + '_SUCCESS_0"]')).isPresent().then(function (isPresent) {
             if (isPresent === true) {
                 findid.capture.adlactivationstatus = isPresent
                 console.log("Yoda Deativation done successfully")
                 cbk()
             }
         })
         element(by.css('[aria-label="' + capturedid + '_FAILED_0"]')).isPresent().then(function (isPresent) {
             if (isPresent === true) {
                 findid.capture.adlactivationstatus = false
                 console.log("Yoda Deativation Failed")
                 cbk()
             }
         })
     }
  */
    loadAndActivateCheckbox(capturedid) {
        var loadAndActivate = element(by.css('[aria-label="loadandActivate_' + capturedid + '"]'))
        this.singleListSelect(loadAndActivate)
    }

    updateDSLCheckbox(capturedid) {
        var updateDSL = element(by.css('[aria-label="updateDSL_' + capturedid + '_0"]'))
        this.singleListSelect(updateDSL)
    }

    deleteDeploymentButton(capturedid) {
        var deleted = element(by.css('[aria-label="deleteRow_' + capturedid + '"]'));
        browser.wait(this.isVisible(deleted), this.timeout.xl);
        this.singlebuttonclick(deleted)
    }

    detailViewSelect(capturedid) {
        var detailview = element(by.css('[aria-label="' + capturedid + '"]'))
        this.singleListSelect(detailview)
    }

    adldeploymentTab() {
        this.tabSelection(this.adlDeploymentScreen)
    }

    addTargetSystemSelect(capturedid) {
        var addTargetSystem = element(by.css('[aria-label="addRow_' + capturedid + '"]'))
        this.singleListSelect(addTargetSystem)
    }

    targetSystemSelect(capturedid, target_obj) {
        var selectSystem = element(by.css('[aria-label="TargetSystem_' + capturedid + '"]'))
        // var systemid = selectSystem.$('[value="' + sysid + '"]')
        var self = this
        var index
        $$('[aria-label="TargetSystem_' + capturedid + '"] option').map(function (el, index) {
            el.getText().then(function (txt) {
                // console.log(txt)
                if (txt.match(new RegExp(target_obj, 'gi')) != null) {
                    console.log("Target System " + target_obj + " in index" + index)
                    var target = element.all(by.css('[aria-label="TargetSystem_' + capturedid + '"]')).all(by.tagName('option')).get(index)
                    self.singleListSelect(target)
                } /* else {
                    console.log("Given system is not present")
                } */
            })
        })
    }

    vparsIdSelect(capturedid, vpar_obj) {
        var index
        var self = this
        $$('[aria-label="testSystem_' + capturedid + '"] option').map(function (el, index) {
            el.getText().then(function (txt) {
                if (txt.match(new RegExp(vpar_obj, 'gi')) != null) {
                    // console.log("index" + index + "systemname :" + vpar_obj)
                    var vparsel = element.all(by.css('[aria-label="testSystem_' + capturedid + '"]')).all(by.tagName('option')).get(index)
                    self.singleListSelect(vparsel)
                } /* else {
                    console.log("Given Vpars is not present")
                } */
            })
        })
    }
}

export default new AdlDeployment(); 