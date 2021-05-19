import BasePage from '../Common/basePage';
import qainpqadeploymentinput from '../../data/qadeploymentinput';
import adldeploymentinput from '../../data/adldeploymentinput';
import findid from '../../data/findid';

class QADeployment extends BasePage {
    constructor() {
        super();
        this.adlDeploymentScreen = element(by.css('[href="#/app/deployment"]'));
        this.submitDeploymentChanges = element(by.css('[aria-label="submitDeploymentChanges"]'));
        this.refreshDeploymentChanges = element(by.css('[aria-label="refreshDeploymentChanges""]'));
    }

    qaPassedRegressionButton(capturedid) {
        var pass = element(by.css('[aria-label="passButton_' + capturedid + '"]'));
        this.singlebuttonclick(pass)
    }

    qaRejectRegressionButton(capturedid) {
        var fail = element(by.css('[aria-label="failButton_' + capturedid + '"]'));
        this.singlebuttonclick(fail)
    }

    deleteDeploymentButton(capturedid) {
        var deleted = element(by.css('[aria-label="deleteRow_' + capturedid + '"]'));
        this.singlebuttonclick(deleted)
    }

    preclear(capturedid) {
        var predel = element(by.css('[aria-label="deleteRow_' + capturedid + '"]'));
        var self = this
        element(by.css('[aria-label="deleteRow_' + capturedid + '_0"]')).isDisplayed().then(function () {
            self.singlebuttonclick(predel)
            console.log("alredy present")
            browser.waitForAngular();
        })

    }

    statusWait(capturedid, i, cbk) {
        element(by.css('.sweModal-content')).isDisplayed().then(function (isPresent) {
            console.log("I am waiting - isPresent " + isPresent)
            if (isPresent) {
                console.log("is present return")
                findid.capture.adlactivationstatus = false
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });
            }
            cbk()
            return isPresent
        });
        browser.wait(this.isNotVisible($('' + capturedid + '_NULL_' + i + '')), this.timeout.full);
        browser.waitForAngular();
        element(by.css('[aria-label="' + capturedid + '_SUCCESS_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = isPresent
                console.log("yoda load and activate done successfully", "")
                cbk(true)
            } /* else {
                cbk(false)
            } */

        })
        element(by.css('[aria-label="' + capturedid + '_FAILED_' + i + '"]')).isPresent().then(function (isPresent) {
            if (isPresent) {
                findid.capture.adlactivationstatus = false
                console.log("yoda load and activate execution Failed")
                throw {
                    name: "Yoda Error",
                    message: "Unable to proceed,, Yoda Load and Activate execution Failed"
                }
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

    loadAndActivateCheckbox(capturedid) {
        var loadAndActivate = element(by.css('[aria-label="loadandActivate_' + capturedid + '"]'))
        browser.wait(this.isVisible(loadAndActivate), this.timeout.xl)
        this.singleListSelect(loadAndActivate)
    }

    detailViewSelect(capturedid) {
        var detailview = element(by.css('[aria-label="' + capturedid + '"]'))
        browser.wait(this.isVisible(detailview), this.timeout.xl)
        this.singleListSelect(detailview)
        browser.waitForAngular();
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
                } else {
                    console.log("Given system is not present")
                }
            })
        })
        // throw 'Invalid System'
    }

    vparsIdSelect(capturedid, vpar_obj) {
        // element(by.css('[aria-label="testSystem_' + capturedid + '_0"]')).all(by.tagName('option')).get(1).click()
        // element(by.css('[aria-label="testSystem_' + capturedid + '"]')).$(['value="' + sysid + '"'])
        var index
        var self = this
        $$('[aria-label="testSystem_' + capturedid + '"] option').map(function (el, index) {
            el.getText().then(function (txt) {
                if (txt.match(new RegExp(vpar_obj, 'gi')) != null) {
                    // console.log("index" + index + "systemname :" + vpar_obj)
                    var vparsel = element.all(by.css('[aria-label="testSystem_' + capturedid + '"]')).all(by.tagName('option')).get(index)
                    self.singleListSelect(vparsel)
                } else {
                    console.log("Given Vpars is not present")
                }
            })
        })
    }

}

export default new QADeployment(); 