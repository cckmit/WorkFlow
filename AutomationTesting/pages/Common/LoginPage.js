import BasePage from './basePage';
import conf from "../../conf"

class LoginPage extends BasePage {
    constructor() {
        super()
        this.url = conf.config.baseUrl;
        this.pageLoaded = this.inDom($('.btnLogin'));
        this.userInput = element(by.model('vm.getUser.Username'));
        this.passInput = element(by.model('vm.getUser.Password'));
        this.loginButton = $('.btnLogin');
        this.login_success = false
    }

    loginAs(userObj) {
        return this.login(userObj.username, userObj.password);
    }

    login(user, pass) {
        this.userInput.sendKeys(user);
        this.passInput.sendKeys(pass);
        this.login_success = true
        return this.loginButton.click();
    }

}

export default new LoginPage();