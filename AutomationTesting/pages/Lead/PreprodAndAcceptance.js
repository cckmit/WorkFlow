import Common from '../Common/Common';
import BasePage from '../Common/basePage';
import updateplan from '../../data/updateplan';
import qadeploymentinput from '../../data/qadeploymentinput';
import findid from '../../data/findid';
import { element } from 'protractor';

class UpdateImplementationPlan extends BasePage {
    constructor() {
        super();
        this.planListTab = element(by.css('[href="#/app/impPlan"]'));
        this.check = element(by.css('.okBtn'))
        this.planstatus = element(by.model('vm.impPlan.planStatus'));
        this.updateBtn = element(by.css('.planSaveBtn'))
        this.prompt = element(by.css('.sweModal-content'));
    };

    updatePlanButton() {
        this.singlebuttonclick(this.updateBtn)
        browser.wait(this.isVisible(this.prompt), this.timeout.full)
        browser.waitForAngular();
    }

    deployedInPreProduction() {
        var PreProduction = this.planstatus.$(by.css('option[value="' + qadeploymentinput.qadeployment.preprod + '"]'))
        this.tabSelection(PreProduction)
    }
    passedAcceptance() {

        // element('[ng-model="vm.impPlan.planStatus"]').click()
        // element(by.css('[ng-model="vm.impPlan.planStatus"]')).$(by.css('option[value="PASSED_ACCEPTANCE_TESTING"]')).click()
        // element(by.css('[ng-model*="vm.impPlan.planStatus"]')).element(by.cssContainingText('option', 'Passed Acceptance Testing')).click();

        /* $$(by.css('[ng-model*="vm.impPlan.planStatus"] option')).forEach(element => {
            console.log(element.getText())
        }); */
        console.log("passed area")
        var selc = this
        setTimeout(function () {
            /* var arr = [];
            console.log("inside timeout")
            console.log(element.all(by.css('[ng-model="vm.impPlan.planStatus"] option')).length)
            element.all(by.css('[ng-model="vm.impPlan.planStatus"] option')).each(function(ele,index){
                console.log(index)
                ele.getText().then(function (params) {
                    console.log(params)
                })
            }) */

            var Acceptance = selc.planstatus.element(by.css('option[value="PASSED_ACCEPTANCE_TESTING"]'))
            selc.tabSelection(Acceptance)
        }, 2000)

    }

    planTabViewSelect() {
        var planTabView = element(by.css('[data-target="#implSystemDataTab_' + qadeploymentinput.qadeployment.planid + '"]'))
        this.tabSelection(planTabView)
    }

    implementationPlanListTab() {
        this.tabSelection(this.planListTab);
        browser.waitForAngular();
    }

    editPlanButtonSelect(capturedid) {
        browser.waitForAngular();
        var editPlanButton = element(by.css('[href="#/app/updateImpPlan/' + capturedid + '"]'));
        browser.wait(this.isVisible(editPlanButton), this.timeout.xl)
        this.tabSelection(editPlanButton);
        browser.waitForAngular();
    }

    planDetailViewSelect() {
        var planDetailView = element(by.css('[aria-label="' + qadeploymentinput.qadeployment.planid + '"]'));
        browser.wait(this.isVisible(planDetailView), this.timeout.xl)
        this.tabSelection(planDetailView)
        browser.waitForAngular();
    }

}
export default new UpdateImplementationPlan();