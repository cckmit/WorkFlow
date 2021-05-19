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


    it('Submit implementation plan', (done) => {
        CompileAndBuild.editPlanButtonSelect(findid.capture.planidcapture);
        CompileAndBuild.submitButtonClick();
        browser.sleep(5000)
        Common.alertMessage1(function (cbk) {
            done()
        });
    });

    it('Logout Lead Login', () => {
        Common.logoutWorkflow();
    });

});
