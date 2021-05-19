import BasePage from '../Common/basePage';
import updateputlevel from '../../data/updateputlevel';
import createputlevel from '../../data/createputlevel';

class UpdatePutLevel extends BasePage {
    constructor() {
        super();
        this.putLevelTab = element(by.css('[href="#/app/deploymentDetails"]'))
        this.updatePutLevelBtn = element(by.css('[aria-label="updatePutLevelWindow"]'));
        this.systemId = element(by.model('vmid.putData.systemId'));
        this.putLevel = element(by.model('vmid.putData.putLevel'));
        this.defaultPut = element(by.model('vmid.putData.defaultPut'));
        this.defaultPutYes = element(by.css('[aria-label="Yes"]'));
        this.defaultPutNo = element(by.css('[aria-label="No"]'));
        this.scmUrl = element(by.model('vmid.putData.scmUrl'));
        this.putDate = element(by.model('vmid.putData.putDate'));
        this.putTime = element(by.model('vmid.putData.putTime'));
        this.insertPutBtn = element(by.css('[aria-label="cancelPutLevelWindow"]'));//need to check
        this.cancelPutBtn = element(by.css('[aria-label="inserPutLevelWindow"]'));//need to check
        this.deleteYes = element(by.css('[ng-click="dialog.hide()"]'));
        this.deleteNO = element(by.css('[ng-click="dialog.abort()"]'));
    }

    deleteYesSelect() {
        this.singlebuttonclick(this.deleteYes)
    }

    deleteNoSelect() {
        this.singlebuttonclick(this.deleteNO)
    }

    editPutLevel() {
        var i = 0; var resp = 0;
        var putdetail = $$('.putLevel tr').filter(function (putfound) {
            return putfound.$$('td').get(1).getText().then(function (putLevel) {
                i += 1;
                if (putLevel === createputlevel.createput.putlevel) {
                    resp = i;
                }
                return putLevel === createputlevel.createput.putlevel;

            })

        }).then(function (putfound) {
            element(by.xpath('//*[@id="putLevelTable"]/tbody/tr[' + resp + ']/td[5]/div/button[1]')).click();
            browser.sleep(3000)
        });
    };


    newPutLevelEnter() {
        this.enterText(this.putLevel, updateputlevel.updateput.putlevel)
    }

    putLevelTabSelect() {
        this.tabSelection(this.putLevelTab)
    }

    insertPutButton() {
        this.singlebuttonclick(this.insertPutBtn)
    }

    deletePutLevel() {
        var i = 0; var resp = 0;
        var putdetail = $$('.putLevel tr').filter(function (putfound) {
            return putfound.$$('td').get(1).getText().then(function (putLevel) {
                i += 1;
                if (putLevel === updateputlevel.updateput.putlevel) {
                    resp = i;
                }
                else {
                    if (putLevel == createputlevel.createput.putlevel) {
                        resp = i;
                    }
                }
                return putLevel === updateputlevel.updateput.putlevel;

            })

        }).then(function (putfound) {
            element(by.xpath('//*[@id="putLevelTable"]/tbody/tr[' + resp + ']/td[5]/div/button[2]')).click();
            browser.sleep(3000)
        });
    };

    deploymentDateEnter() {
        this.enterText(this.putDate, updateputlevel.updateput.deployementdate)
    }

    deploymentTimeEnter() {
        this.enterText(this.putTime, updateputlevel.updateput.deploymenttime)
    }

    scmUrlEnter() {
        this.enterText(this.scmUrl, updateputlevel.updateput.putrespository)
    }

    defaultPutYesSelect() {
        this.singleListSelect(this.defaultPutYes)
    }

    defaultPutNoSelect() {
        this.singleListSelect(this.defaultPutYes)
    }

    forcepush() {
        element(by.css('.okBtn')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    if (printmessage === "Load Freeze Deleted Successfully") {
                        console.log("Success/Error Message :" + printmessage);
                        element(by.css('.okBtn')).click();
                    }
                    else {
                        if (printmessage === "Default put already exists for the selected system") {
                            browser.sleep(2000)
                            element(by.css('.okBtn')).click();
                            browser.sleep(2000)
                            element(by.css('[aria-label="updatePutLevelWindow"]')).click();
                            element(by.css('.okBtn')).isDisplayed().then(function (force) {
                                if (force) {
                                    element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                                        console.log("Success/Error Message :" + printmessage);
                                        console.log("Contact Developer")
                                        element(by.css('.okBtn')).click();
                                        browser.sleep(2000)
                                        element(by.css('[aria-label="cancelPutLevel_UpdateWindow"]')).click();
                                    });

                                }
                            });
                        } else {
                            console.log("Update putlevel not at all working")
                            element(by.css('[aria-label="inserPutLevelWindow"]')).click();
                        }

                        browser.sleep(3000);
                    }
                });

            }
        });
        browser.sleep(3000);
    }

    updatePutLevelButton() {
        this.singlebuttonclick(this.updatePutLevelBtn)
    }

    systemIdSelection() {
        var sysid = this.systemId.$('[value="' + updateputlevel.updateput.systemid + '"]');
    }

}

export default new UpdatePutLevel();