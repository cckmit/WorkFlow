import findid from '../data/findid';
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import LeadLogin from '../pages/Lead/LeadLogin';
import Common from '../pages/Common/Common';
import UpdateImplementationPlan from '../pages/Lead/UpdateImplementationPlan';
import updateplan from '../data/updateplan';
import BasePage from '../pages/Common/basePage';
import QADeployment from '../pages/QualityAnalyst/QADeployment';
import qadeploymentinput from '../data/qadeploymentinput'
import QALogin from '../pages/QualityAnalyst/QALogin';

describe('QA Deployment Actions', () => {
    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('QA Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.QualityAnalyst)
        QALogin.switchToQA()
        browser.sleep(4000)
    });

    it('Check plan details', () => {
        browser.sleep(2000)
        QADeployment.adldeploymentTab();
        browser.sleep(2000)
        Common.pageSize();
        browser.sleep(2000)
        QADeployment.detailViewSelect(findid.capture.planidcapture);
        browser.sleep(2000)
    });

    var qa_deploy_list = qadeploymentinput.qadeployment.vparsysid
    qa_deploy_list.map(function (qa_obj, i) {
        it('Enter system details', () => {
            QADeployment.addTargetSystemSelect(findid.capture.planidcapture);
            QADeployment.targetSystemSelect(findid.capture.planidcapture + "_" + (i), qa_obj.systemid);
            QADeployment.vparsIdSelect(findid.capture.planidcapture + "_" + (i), qa_obj.vparid);
        });


        it('Load And Activation select', () => {
            QADeployment.loadAndActivateCheckbox(findid.capture.planidcapture + "_" + (i));
        });

        it('submit for Load And Activation', (done) => {
            QADeployment.submitDeploymentButton();
           /*  Common.alertMessage1(function (cbk) {
                done()
            }); */

            QADeployment.statusWait(findid.capture.planidcapture, i, function (id) {
                done()
            });
        });

    })

    var qa_deploy_list = qadeploymentinput.qadeployment.vparsysid
    qa_deploy_list.map(function (qa_obj, i) {
        it('QA Passed regression', () => {
            QADeployment.qaPassedRegressionButton(findid.capture.planidcapture + "_" + (i));
            Common.alertMessage();
        });

    });

    it('Logout QA', () => {
        browser.sleep(3000)
        Common.logoutWorkflow();
    });


});
