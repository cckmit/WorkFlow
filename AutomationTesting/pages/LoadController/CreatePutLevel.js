import BasePage from '../Common/basePage';
import createputlevel from '../../data/createputlevel';

class CreatePutLevel extends BasePage {
    constructor() {
        super();
        this.putLevelTab = element(by.css('[href="#/app/deploymentDetails"]'))
        this.insertPutLevel = element(by.css('.fa.fa-save'));
        this.systemId = element(by.model('vmid.putData.systemId'));
        this.putLevel = element(by.model('vmid.putData.putLevel'));
        this.defaultPut = element(by.model('vmid.putData.defaultPut'));
        this.defaultPutYes = element(by.css('[aria-label="Yes"]'));
        this.defaultPutNo = element(by.css('[aria-label="No"]'));
        this.scmUrl = element(by.model('vmid.putData.scmUrl'));
        this.putDate = element(by.model('vmid.putData.putDate'));
        this.putTime = element(by.model('vmid.putData.putTime'));
        this.insertPutBtn = element(by.css('[aria-label="cancelPutLevelWindow"]'));
        this.cancelPutBtn = element(by.css('[aria-label="inserPutLevelWindow"]'));
    }

    newPutLevelEnter() {
        this.enterText(this.putLevel, createputlevel.createput.putlevel)
    }

    putLevelTabSelect() {
        this.tabSelection(this.putLevelTab)
    }

    insertPutButton() {
        this.singlebuttonclick(this.insertPutBtn)
    }

    deploymentDateEnter() {
        this.enterText(this.putDate, createputlevel.createput.deployementdate)
    }
    deploymentTimeEnter() {
        this.enterText(this.putTime, createputlevel.createput.deploymenttime)
    }

    scmUrlEnter() {
        this.enterText(this.scmUrl, createputlevel.createput.putrespository)
    }

    defaultPutYesSelect() {
        this.singleListSelect(this.defaultPutYes)
    }

    defaultPutNoSelect() {
        this.singleListSelect(this.defaultPutYes)
    }

    insertPutLevelButton() {
        browser.wait(this.isVisible(this.insertPutLevel), this.timeout.xxl)
        this.singlebuttonclick(this.insertPutLevel)
    }

    systemIdSelection() {
        var sysid = this.systemId.$('[value="' + createputlevel.createput.systemid + '"]');
        this.singleListSelect(sysid)
    }
}

export default new CreatePutLevel();