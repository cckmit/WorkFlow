import Basepage from '../pages/Common/basePage';
import DevManager from '../pages/DevManager/DevManagerMytask';
import ManagerLogin from '../pages/DevManager/ManagerLogin';
import Credential from '../data/Credential';
import LoginPage from '../pages/Common/LoginPage';
import findid from '../data/findid';
import Common from '../pages/Common/Common';
import LinuxTest from '../pages/Developer/LinuxTest';

describe('Manager Login Actions', () => {

    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Manager Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.Manager)

        ManagerLogin.switchToManager();
        browser.sleep(5000);

    });

    it('Logout Lead Login', () => {
        LinuxTest.newlinux(cbk);
        });

});