import BasePage from '../Common/basePage';
import createimplementationinput from '../../data/createimplementationinput';
import updateImplementationinput from '../../data/updateImplementationinput';
import findid from '../../data/findid';
import { element } from 'protractor';

class UpdateImplementation extends BasePage {
    constructor() {
        super();
        this.peerReviewer = element(by.css('.select2-search__field'));
        this.developerDescription = element(by.css('[aria-label="impdesc"]'));
        this.developerContact = element(by.model('vm.imp.devContact'));
        this.updateButton = element(by.css('[aria-label="UpdateImplementation"]'))
        this.developerMytask = element(by.css('[href="#/app/impl"]'))
        this.checkoutSelection = element(by.css('[aria-label="Checkout"]'));
        this.createArtifact = element(by.css(".fa.fa-plus-circle"))
        this.createIbm = element(by.css("button[ng-click*=addSourceSet('IBM')]"))
        this.createIbm = element(by.css("button[ng-click*=addSourceSet('NON_IBM')]"))
    }

    createNewSourceArtifact(){
        this.singlebuttonclick(this.createArtifact)
    }

    implementationDetailSelect(capturedid) {
        var id = capturedid
        var implemetationDetail = element(by.css('[aria-label="' + id + '"]'))
        browser.wait(this.isVisible(implemetationDetail), this.timeout.xxl)
        this.singlebuttonclick(implemetationDetail)
        browser.waitForAngular();
    }

    developerMytaskTabSelect() {
        this.tabSelection(this.developerMytask)
        browser.waitForAngular();
    }

    implementationEditButton(capturedid,implid) {
        var editbtn = element(by.css('[href="#/app/updateImp/' + capturedid + '/' +implid + '"]'));
        browser.wait(this.isClickable(editbtn), this.timeout.xxl)
        this.singlebuttonclick(editbtn)
        browser.waitForAngular();
        browser.wait(this.isClickable(this.checkoutSelection), this.timeout.xxl)
    }

    developerLocationSelect(developerlocation) {
        var Location = element(by.model('vm.imp.devLocation')).$('[value="' + developerlocation + '"]');
        this.tabSelection(Location)
    }

    developerContactSelect(developername) {
        this.enterText(this.developerContact, developername)
    }

    developerDescriptionSelect(developerdescription) {
        this.enterText(this.developerDescription, developerdescription)
    }

    peerReviewerSelect() {
        var peerReviewerValue = element(by.model('vm.imp.peerReviewers')).$('[value="' + updateImplementationinput.updateimpl.peerreviwer + '"]')
        this.tabSelection(peerReviewerValue)
    }

    implementationDeveloperSelect() {
        var developerSelectValue = element(by.model('vm.imp.devName')).$('[value="' + updateImplementationinput.updateimpl.developername + '"]')
        this.tabSelection(developerSelectValue)

    }

    updateButtonSelect() {
        browser.wait(this.isClickable(this.updateButton), this.timeout.xxl)
        this.singlebuttonclick(this.updateButton)
        browser.waitForAngular();
    }

}

export default new UpdateImplementation();