import findid from '../data/findid';
import leadinput from '../data/leadinput'
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import LeadLogin from '../pages/Lead/LeadLogin';
import Common from '../pages/Common/Common';
import PreprodAndAcceptance from '../pages/Lead/PreprodAndAcceptance';
import updateplan from '../data/updateplan';
import BasePage from '../pages/Common/basePage';

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

    it('Edit implementation plan', () => {
        browser.sleep(4000);
        PreprodAndAcceptance.editPlanButtonSelect(findid.capture.planidcapture);
        browser.sleep(4000);
    });

/*     it('Deployed in pre-prod', () => {
        PreprodAndAcceptance.deployedInPreProduction();
        PreprodAndAcceptance.updatePlanButton();
        Common.alertMessage();
    });
 */
    it('Passed Acceptance testing', () => {
        PreprodAndAcceptance.editPlanButtonSelect(findid.capture.planidcapture);
        browser.sleep(5000);
        PreprodAndAcceptance.passedAcceptance()
        PreprodAndAcceptance.updatePlanButton();
        browser.sleep(3000);
        Common.alertMessage();
    });
    
    it('Logout Lead', () => {
        browser.sleep(3000)
        Common.logoutWorkflow();
    });

});
