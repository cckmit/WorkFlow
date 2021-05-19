import findid from '../data/findid';
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import LoadsControlLogin from '../pages/LoadController/LoadControlLogin';
import Common from '../pages/Common/Common';
import UpdateImplementationPlan from '../pages/Lead/UpdateImplementationPlan';
import BasePage from '../pages/Common/basePage';
import LoadControllerMytask from '../pages/LoadController/LoadControllerMytask';

describe('Production Deployment Actions', () => {
    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Load Controller Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.LoadController)
        LoadsControlLogin.switchToLoadsControl();
    });

    it('Loadcontroller my task', () => {
        // LoadControllerMytask.loadMytask();
    });

    it('detail view', () => {
        LoadControllerMytask.detailViewSelect(findid.capture.planidcapture);
    });

    it('load My task', () => {
        LoadControllerMytask.loadMytask();
    });

    it('Edit and update implemantion', () => {
        LoadControllerMytask.readyForProductionSelect(findid.capture.planidcapture);
        // LoadControllerMytask.updatePlanButton();
    });

    it('Ready for production deployment', () => {
        LoadControllerMytask.proceedYes();
    });

    it('File transfer to TSD', () => {
        LoadControllerMytask.listenLoadControllerWebsocket()
    });
    
    it('Production success', () => {
        Common.alertMessage();
    });
    
    it('Logout Loadcontroller', () => {
        browser.sleep(3000)
        Common.logoutWorkflow();
    });

});
