import BasePage from '../Common/basePage';

class ReviewerLogin extends BasePage {
    constructor() {
        super();
        this.homepage = element(by.model("vm.roleName"));

    }
    switchToReviewer() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("Reviewer")
    }
}

export default new ReviewerLogin();