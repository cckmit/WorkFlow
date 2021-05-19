import BasePage from '../Common/basePage';

class LeadLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));
    }
    switchToLead() {
        this.roleclick("Lead")
        browser.waitForAngular();
        browser.sleep(3000);
        element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });
                
            }
        });
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
    }
}

export default new LeadLogin();