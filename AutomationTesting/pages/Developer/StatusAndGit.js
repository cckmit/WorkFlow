import BasePage from '../Common/basePage';
import updateimplementationinput from '../../data/updateImplementationinput';
import findid from '../../data/findid';
import { element, browser } from 'protractor';

class StatusAndGit extends BasePage {
    constructor() {
        super();
        this.unitTestStatus = element(by.css('[aria-label="Unit Testing Completed"]'));
        this.integrationTest = element(by.css('[aria-label="Integration Testing Completed"]'))
        this.readyforqa = element(by.css('[aria-label="Ready for QA"]'))
        this.requestPeerBtn = element(by.css('[ng-click="peerReviewRequest(vm.imp)"]'))
    }

    requestPeerButton(){
        browser.wait(this.isClickable(this.requestPeerBtn),this.timeout.xl)
        this.singlebuttonclick(this.requestPeerBtn)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(this.requestPeerBtn),this.timeout.xl)
    }
    requestPeerReviewSelect(capturedid) {
        browser.sleep(4000);
        var requestForPeer = element(by.css('[aria-label="RequestPeerReview_Impl_' + capturedid + '"]'));
        browser.wait(this.isClickable(requestForPeer), this.timeout.xl)
        this.singlebuttonclick(requestForPeer)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(requestForPeer), this.timeout.xl)
    }

    readyProceedYesSelect() {
        browser.sleep(2000);
        element(by.css('[ng-click="dialog.hide()"]')).click();
        browser.waitForAngular();

    }

    unitTestingStatusChange() {
        browser.wait(this.isVisible(this.unitTestStatus), this.timeout.xl)
        this.singlebuttonclick(this.unitTestStatus)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(this.unitTestStatus), this.timeout.xl)
    }

    integarationStatusChange() {
        browser.wait(this.isVisible(this.integrationTest), this.timeout.m)
        this.singlebuttonclick(this.integrationTest)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(this.integrationTest), this.timeout.m)
    }

    readyForQAStatusChange() {
        browser.wait(this.isClickable(this.readyforqa), this.timeout.xl)
        this.singlebuttonclick(this.readyforqa)
        browser.waitForAngular();
    }
}

export default new StatusAndGit();