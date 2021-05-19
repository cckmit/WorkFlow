import BasePage from '../Common/basePage';

class LoadsControlLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));
    }
    switchToLoadsControl() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("LoadsControl")
    }
}

export default new LoadsControlLogin();