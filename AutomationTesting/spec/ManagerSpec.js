import Basepage from '../pages/Common/basePage';
import DevManager from '../pages/DevManager/DevManagerMytask';
import ManagerLogin from '../pages/DevManager/ManagerLogin';
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

        ManagerLogin.switchToManager();
        browser.sleep(5000);

    });

    it('Manager accept', (done) => {
        DevManager.detailViewSelect(findid.capture.planidcapture);
        browser.sleep(2000);
        DevManager.implementationTabSelect(findid.capture.planidcapture);
        DevManager.approvalTabSelect(findid.capture.planidcapture);
        DevManager.activityLogTabSelect(findid.capture.planidcapture);
        DevManager.planTabSelect(findid.capture.planidcapture);
        var syskeys = {}
        DevManager.listenManagerWebsocket(findid.capture.planidcapture, syskeys, function () {
            done();
        });
        DevManager.approvalActionSelect(findid.capture.planidcapture);
        browser.sleep(2000)
        DevManager.actionApproveSelect(findid.capture.planidcapture);
        browser.sleep(2000)
        DevManager.managerCommentsSelect();
        browser.sleep(3000)
        DevManager.commentOkButton();
        browser.sleep(3000)
    });

    it('Logout Lead Login', () => {
        Common.logoutWorkflow();
    });

});