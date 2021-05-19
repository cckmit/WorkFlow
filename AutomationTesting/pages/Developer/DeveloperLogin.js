import BasePage from '../Common/basePage';

class DeveloperLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));
    }
    switchToDeveloper() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("Developer")
    }

}

export default new DeveloperLogin();