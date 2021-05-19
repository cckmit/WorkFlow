import Basepage from '../pages/Common/basePage';
import DevManager from '../pages/DevManager/DevManagerMytask';
import Deleagate from '../pages/SystemSupport/Delegate';
import SystemSupportLogin from '../pages/SystemSupport/SystemSupportLogin'
import Credential from '../data/Credential';
import LoginPage from '../pages/Common/LoginPage';
import findid from '../data/findid';
import Common from '../pages/Common/Common';

describe('Manager Login Actions', () => {

    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Manager Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.Manager)
        SystemSupportLogin.switchToSystemSupport();
    });

    it('Logout Lead Login', () => {
        Common.logoutWorkflow();
    });

});