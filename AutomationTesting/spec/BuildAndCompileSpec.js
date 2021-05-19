import BasePage from '../pages/Common/basePage';
import Common from '../pages/Common/Common';
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import LeadLogin from '../pages/Lead/LeadLogin';
import CompileAndBuild from '../pages/Lead/CompileAndBuild';
import findid from '../data/findid';
import leadinput from '../data/leadinput';

describe('Lead Login Actions', () => {

    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Do Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.Lead)
        LeadLogin.switchToLead()
    });


    it('Compile and Build', (done) => {
        // CompileAndBuild.implementationPlanListTab();
        var syskeys = {}
        /*  for(index in sys_obj){
             syskeys[sys_obj[index].id] = null
         } */
        browser.sleep(6000);
        CompileAndBuild.planDetailViewSelect(findid.capture.planidcapture);
        browser.sleep(3000)
        CompileAndBuild.buildViewSelect(findid.capture.planidcapture);
        CompileAndBuild.listenBuildWebsocket(findid.capture.planidcapture, syskeys, function (err) {
            console.log(err)

            if (!err) {
                console.log("ins cbk")
                expect(err).toBe("BUILD FAILED")
                // process.exit()
                /*  var error = function () {
                     throw new TypeError("one");
                 }
                 expect(error).toThrowError() */
                done()
            } else {
                done();
            }
        });

        browser.sleep(1000)
        CompileAndBuild.clickBuildButton(findid.capture.planidcapture);
    });

    if (findid.capture.build_status == false) {
        console.log("ok")
        throw {
            'name': 'Error',
            'message': 'Execution stopped due to build failed'
        }
    }


    if (findid.capture.build_status !== false) {
        it('Generate Oldr/TLDR', (done) => {
            var syskeys = {}
            // CompileAndBuild.listenOldrWebsocket(cbk);
            findid.capture.oldr_status = true
            CompileAndBuild.listenOldrWebsocket(findid.capture.planidcapture, syskeys, function (cbk) {
                console.log("oldr ", findid.capture.oldr_status)
                done()
            });
            browser.sleep(1000)
            CompileAndBuild.generateOLDRButton(findid.capture.planidcapture);
        });

        /*             it('Submit implementation plan', (done) => {
                        CompileAndBuild.editPlanButtonSelect(findid.capture.planidcapture);
                        CompileAndBuild.submitButtonClick();
                        browser.sleep(5000)
                        Common.alertMessage1(function (cbk) {
                            done()
                        });
                    }); */

    }

    if (findid.capture.oldr_status== false) {
        console.log("oldr error message")
        throw {
            'name': 'Error',
            'message': 'Execution stopped due to oldr failed'
        }
    }

    it('Logout Lead Login', () => {
        Common.logoutWorkflow();
    });

});
