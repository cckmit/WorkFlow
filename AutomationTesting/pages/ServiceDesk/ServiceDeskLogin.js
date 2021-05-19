import BasePage from '../Common/basePage';

class ServiceDeskLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));
    }
    switchToServiceDesk() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("TechnicalServiceDesk")
    }
}

export default new ServiceDeskLogin();