import BasePage from '../Common/basePage';

class SystemSupportLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));

    }
    switchToSystemSupport() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("SystemSupport")
    }

}

export default new SystemSupportLogin();