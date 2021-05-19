import DeveloperLogin from '../pages/Developer/DeveloperLogin';
import Credential from '../data/Credential';
import LoginPage from '../pages/Common/LoginPage';
import UpdateImplementation from '../pages/Common/UpdateImplementation';
import Common from '../pages/Common/Common';
import Checkout from '../pages/Developer/Checkout';
import StatusAndGit from '../pages/Developer/StatusAndGit';
import findid from '../data/findid';
import updateImplementationinput from '../data/updateImplementationinput';


describe('Developer login', () => {
    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Developer credentials', () => {
        LoginPage.loginAs(Credential.Developer)
        browser.sleep(2000);
        DeveloperLogin.switchToDeveloper();
        browser.sleep(3000);
    });

    var update_impl_list = updateImplementationinput.updateimpl.multipleimpl;    
    update_impl_list.map(function (ready_Obj, i) {
        var currentIteration = i;
        it('Edit implementation details ' + i, () => {
            // console.log(findid.capture.planidcapture + "_00" + (currentIteration + 1))
            UpdateImplementation.implementationEditButton(findid.capture.planidcapture, findid.capture.planidcapture + "_00" + (currentIteration + 1));
        })

        it('Integration testing completed' + i, () => {
            StatusAndGit.integarationStatusChange();
            console.log(findid.capture.planidcapture + "_00" + (currentIteration + 1))
        });

        it('ReadyForQA' + i, () => {
            StatusAndGit.readyForQAStatusChange();
            browser.sleep(2000);
            StatusAndGit.readyProceedYesSelect();
            Common.alertMessage();
        });

        it('Develpoer inbox view', () => {
            UpdateImplementation.developerMytaskTabSelect()
            browser.sleep(4000)

        });

    })

    it('Workflow logout', () => {
        Common.logoutWorkflow();
    });

});