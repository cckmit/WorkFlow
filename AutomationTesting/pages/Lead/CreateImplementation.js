import Common from '../Common/Common';
import BasePage from '../Common/basePage';
import createimplementationinput from '../../data/createimplementationinput';
import findid from '../../data/findid';

class CreateImplementation extends BasePage {
    constructor() {
        super();
        this.peerReviewer = element(by.css('.select2-search__field'));
        this.developerDescription = element(by.css('[aria-label="impdesc"]'));
        this.developerContact = element(by.model('vm.imp.devContact'));
        this.createbutton = element(by.css('[aria-label="CreateImplementation"]'));
    };

    developerLocationSelect(developerlocation) {
        var LocationSelect = element(by.model('vm.imp.devLocation')).$('[value*="' + developerlocation + '"]');
        this.tabSelection(LocationSelect)
    }

    createImplementationButton() {
        browser.wait(this.isVisible(this.createbutton), this.timeout.xl)
        this.singlebuttonclick(this.createbutton);
        browser.waitForAngular();
    }

    developerContactSelect(developercontact) {
        this.enterText(this.developerContact, developercontact)
    }

    developerDescriptionSelect(developerdescription) {
        browser.wait(this.isVisible(this.createbutton), this.timeout.xl)
        this.enterText(this.developerDescription, developerdescription)
    }

    peerReviewerSelect(peerreviwer) {
        var peerReviewerValue = element(by.model('vm.imp.peerReviewers')).$('[value*="' + peerreviwer + '"]')
        browser.wait(this.isDisplayed(peerReviewerValue), this.timeout.xl)
        this.tabSelection(peerReviewerValue)
    }

    implementationSelect(capturedid) {
        var implselect = element(by.css('[href="#/app/newImp/' + capturedid + '"]'))
        browser.wait(this.isVisible(implselect), this.timeout.xl)
        browser.sleep(2000)
        this.singlebuttonclick(implselect)
        browser.waitForAngular();
    }

    implementationDeveloperSelect(dev_obj) {
        // element.all(by.css('md-input-container[class*="multipleSelect2"] select[ng-model="vm.imp.devId"] option[value="' + dev_obj + '"]')).click()

        var developerSelect = element(by.model('vm.imp.devId')).$('[value*="' + dev_obj + '"]')
        browser.wait(this.isDisplayed(developerSelect), this.timeout.xl)
        this.tabSelection(developerSelect)
    }

}

export default new CreateImplementation();