import findid from '../data/findid';
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import LeadLogin from '../pages/Lead/LeadLogin';
import Common from '../pages/Common/Common';
import UpdateImplementationPlan from '../pages/Lead/UpdateImplementationPlan';
import updateplan from '../data/updateplan';
import BasePage from '../pages/Common/basePage';
import AdlDeployment from '../pages/Lead/AdlDeployment';
import adldeploymentinput from '../data/adldeploymentinput';


describe('Lead Deployment Actions', () => {

    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.Lead)
        LeadLogin.switchToLead()
    });

    it('Check plan details', () => {
        browser.sleep(2000)
        AdlDeployment.adldeploymentTab();
        browser.sleep(2000)
        Common.pageSize();
        browser.sleep(2000)
        AdlDeployment.detailViewSelect(findid.capture.planidcapture);
    });

    var adl_deployment = adldeploymentinput.adldeployment.yodaid
    adl_deployment.map(function (vpar_obj, i) {
        var currentIteration = i;
        it('Enter system details', () => {
            browser.sleep(2000)
            AdlDeployment.addTargetSystemSelect(findid.capture.planidcapture);
            /*  var options = $('[aria-label="TargetSystem_T1700852_0"] option');
             
             var values = $.map(options ,function(option) {
                 console.log(option.text)
                 return option.value;
             
             }) */
            AdlDeployment.targetSystemSelect(findid.capture.planidcapture, vpar_obj.systemid);
            browser.sleep(2000);
            AdlDeployment.vparsIdSelect(findid.capture.planidcapture, vpar_obj.vparid);
        });

        it('Load And Activation select', () => {
            browser.sleep(2000);
            AdlDeployment.loadAndActivateCheckbox(findid.capture.planidcapture);
            browser.sleep(1000);
            // AdlDeployment.updateDSLCheckbox(findid.capture.planidcapture)
            // browser.sleep(1000);
        });

        it('submit for Load And Activation', (done) => {
            AdlDeployment.submitDeploymentButton();
            Common.alertMessage1(function () {
                console.log("alert received ")
                if (findid.capture.adlactivationstatus == true) {
                    AdlDeployment.statusWait(findid.capture.planidcapture, function (response) {
                        // console.log(response)
                        // console.log(findid.capture.adlactivationstatus)
                        done()
                    });
                } else {
                    console.log("load failed")
                    findid.capture.adlactivationstatus == false
                    throw {
                        name: "Error",
                        message: "Load and Activation failed"
                     }
                //    throw new Exception("InvalidArgumentException")
                    done()
                }
            });
        });
 
        if (findid.capture.adlactivationstatus === true) {
            it('Deactivate', (done) => {
                browser.sleep(2000);
                AdlDeployment.deactivateCheckbox(findid.capture.adlactivationstatus, findid.capture.planidcapture, i, function (id) {
                    done()
                });
            });
        }
/*
        if (findid.capture.adlactivationstatus === true) {
            it('Activatation', (done) => {
                AdlDeployment.activateCheckbox(findid.capture.adlactivationstatus, findid.capture.planidcapture, i, function (id) {
                    done()
                })
            });
        }

        if (findid.capture.adlactivationstatus == true) {
            it('Deactive and Delete', (done) => {
                AdlDeployment.deactivateAndDeleteCheckbox(findid.capture.adlactivationstatus, findid.capture.planidcapture, i, function (id) {
                    done()
                })
            });
        }

        it('Delete deployment details', () => {
            AdlDeployment.deleteDeploymentButton(findid.capture.planidcapture);
        });
 */
    })

});