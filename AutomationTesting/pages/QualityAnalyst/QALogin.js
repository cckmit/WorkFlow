import BasePage from '../Common/basePage';

class QALogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));
    }
    switchToQA() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("QA")
    }

}

export default new QALogin();