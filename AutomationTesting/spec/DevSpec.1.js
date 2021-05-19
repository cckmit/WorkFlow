import DeveloperLogin from '../pages/Developer/DeveloperLogin'
import Credential from '../data/Credential';
import LoginPage from '../pages/Common/LoginPage'
import UpdateImplementation from '../pages/Common/UpdateImplementation'
import Common from '../pages/Common/Common'
import Checkout from '../pages/Developer/Checkout'
import StatusAndGit from '../pages/Developer/StatusAndGit'
import findid from '../data/findid';
import ReviewerTask from '../pages/Reviewer/ReviewerTask';
import ReviewerLogin from '../pages/Reviewer/ReviewerLogin';
import GitblitTask from '../pages/Reviewer/GitblitTask';
import ApprovedTask from '../pages/Reviewer/ApprovedTask'
import reviewerinput from '../data/reviewerinput'
describe('Developer login', () => {
    beforeEach(() => {
       if (!LoginPage.login_success) {
           LoginPage.goto();
       }
    }); 

    it('Credentials', () => {
        LoginPage.loginAs(Credential.Developer)
        browser.sleep(2000);
        DeveloperLogin.switchToDeveloper();
        browser.sleep(3000);
    });

    it('Edit implementation details', () => {
        UpdateImplementation.implementationEditButton(findid.capture.planidcapture);
        UpdateImplementation.developerContactSelect();
        UpdateImplementation.developerDescriptionSelect();
        UpdateImplementation.developerLocationSelect();
    });

    it('Update implementaionplan', () => {
        UpdateImplementation.updateButtonSelect();
        Common.alertMessage();
    });

    it('Checkout and Search', () => {
        Checkout.checkoutSelectionButton();
        Checkout.checkoutSegmentEnter();
        browser.sleep(1000)
        Checkout.selectProductionTabSelect();
    });

    it('select segment and checkout', () => {
        Checkout.searchProdandNonButton();
        browser.sleep(1000)
        Checkout.selectSegment();
    });
/* 
    it('Non prod selection', (cbk) => {
        Checkout.NonProductionTabSelect();
        Checkout.selectNonSegmentCheck(cbk);
        Checkout.checkProceedSelection();
        browser.sleep(3000)
    });
 */
    it('Nonprod segment select and delete', (cbk) => {
        Checkout.deleteSearchSegment(cbk);
        Checkout.checkouFinaltButton();
    });

    it('checkout report', (cbk) => {
        Checkout.checkoutReportCheck(cbk);
        browser.sleep(3000)
    });

/* 
    it('ibm vanilla populate button select', () => {
        Checkout.checkoutSelectionButton();
    });

    it('checkoutSegmentEnter', () => {
        Checkout.checkoutSegmentEnter();
    });

    it('ibmVanillaButton', () => {
        Checkout.ibmVanillaButton();
    });

    it('ibmVanillaSearchButton', () => {
        Checkout.ibmVanillaSearchButton();
    });

    it('ibmVanillaSegmentSelect', (done) => {
        Checkout.ibmVanillaSegmentSelect(function (id) {
            done()
        });
    });

    it('populate proceed', () => {
        Checkout.populateProceedButton();
    });

    it('IBM populate report', (done) => {
        Checkout.ibmVanillaReport(function (id) {
            done()
        });
    });
 */
    it('Commit segments', () => {
        Checkout.commitButtonSelect();
        browser.sleep(2000)
        Checkout.commitMessageEnter();
        Checkout.commitDialogueButton();
        Common.alertMessage();
    });

    it('Checkin Segments', () => {
        Checkout.checkinButtonSelect();
        Common.alertMessage();
    });

    it('unit testing completed', () => {
        browser.waitForAngular();
        browser.sleep(3000)
        StatusAndGit.unitTestingStatusChange();
        Common.alertMessage();
    });


    it('Request for peer reviewr', () => {
        UpdateImplementation.developerMytaskTabSelect(findid.capture.planidcapture);
        StatusAndGit.requestPeerReviewSelect(findid.capture.planidcapture);
        Common.alertMessage();
    });


    it('Reviewer Login', () => {
        ReviewerLogin.switchToReviewer();
    });

    it('Reviwer Task Tab', () => {
        browser.sleep(4000)
        ReviewerTask.detailsViewSelect();
    });

    it('Select Segment for reviewer', (done) => {
        browser.sleep(4000)
        ReviewerTask.selectAllForReview(function (id) {
            done()
        });

    });

    it('ReviewTask Complete', () => {
        ReviewerTask.reviewCompleted();
    });

    it('Mark as Complete', () => {
        ReviewerTask.markCompleteButton();
    });

    it('Reviewer task complted', () => {
        Common.alertMessage();
    });

    it('approved task tab', () => {
        ApprovedTask.apporvalTabSelect();
        browser.sleep(2000)
    });

    it('Integration testing completed', () => {
        DeveloperLogin.switchToDeveloper();
        browser.sleep(5000)
        UpdateImplementation.implementationEditButton(findid.capture.planidcapture);
        browser.sleep(5000)
        StatusAndGit.integarationStatusChange();
        Common.alertMessage();
    });

    it('ReadyForQA', () => {
        StatusAndGit.readyForQAStatusChange();
        browser.sleep(2000);
        StatusAndGit.readyProceedYesSelect();
        Common.alertMessage();
    });

});