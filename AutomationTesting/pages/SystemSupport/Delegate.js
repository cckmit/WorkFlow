import BasePage from './../Common/basePage';
import conf from "../../conf"

class Delegate extends BasePage {
    constructor() {
        super()
        this.homepage = element(by.model("vm.roleName"));
        this.delegateTab = element(by.model('[aria-label="delegateTab"]'));
    }
    switchtoToolAdmin() {
        browser.wait(this.isVisible(this.homepage), this.timeout.full)
        this.roleclick("ToolAdmin")
    }

    delegateTabSelect(){
        this.tabSelection(this.delegateTab)
    }

}

export default new Delegate();