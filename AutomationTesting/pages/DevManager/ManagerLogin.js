import BasePage from '../Common/basePage';

class ManagerLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));
    }
    switchToManager() {
        browser.wait(this.isVisible(this.homepage), this.timeout.min)
        this.roleclick("DevManager")
    }

}

export default new ManagerLogin();