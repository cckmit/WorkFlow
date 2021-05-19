import Common from '../Common/Common';
import BasePage from '../Common/basePage';
import updateplan from '../../data/updateplan';
import CreateImplementationPlan from '../Lead/CreateImplementationPlan';
import findid from '../../data/findid';

class UpdateImplementationPlan extends BasePage {
    constructor() {
        super();
        this.planListTab = element(by.css('[href="#/app/impPlan"]'));
        this.updateCheck = element(by.css('.planSaveBtn'))
    };

    build() {
        var i = 0; var resp = 0;
        var putdetail = $$('.buildTableHeader tr').filter(function (putfound) {
            return putfound.$$('td').get(1).getText().then(function (status) {
                if (status === "SUCCESS") {
                    console.log("Build Success")
                }
                else {
                    status !== "SUCCESS";
                    console.log("Build Success")
                }
                cbk()
                return status;
            });

        })
    };

    clickBuildButton(capturedid) {
        var clickBuild = element(by.css('[aria-label="CreateBuild_' + capturedid + '"]'))
        this.singlebuttonclick(clickBuild)
    }

    generateOLDRButton(capturedid) {
        var generateOLDR = element(by.css('[aria-label="GenerateOLDR_' + capturedid + '"]'))
        this.singlebuttonclick(generateOLDR)
    }

    planTabViewSelect(capturedid) {
        var planTabView = element(by.css('[data-target="#implSystemDataTab_' + capturedid + '"]'))
        this.tabSelection(planTabView)
        browser.waitForAngular();
    }

    activityLogViewSelect(capturedid) {
        var activityLogView = element(by.css('[data-target="#activity_' + capturedid + '"]'))
        this.tabSelection(activityLogView)
    }

    implementationViewSelect(capturedid) {
        var implementationView = element(by.css('[data-target="#implDataTab_' + capturedid + '"]'))
        this.tabSelection(implementationView)
    }

    buildViewSelect(capturedid) {
        var buildView = element(by.css('[data-target="#buildTab_' + capturedid + '"]'))
        this.tabSelection(buildView)
    }

    approvalViewSelect(capturedid) {
        var approvalView = element(by.css('[data-target="#approvalTab_' + capturedid + '"]'))
        this.tabSelection(approvalView)
    }

    testSystemCountSelect() {
        element.all(by.css('sList in vm.systemDetailsList', 'fieldset')).then(function (total) {
            console.log(total.length)
        })
    }

    implementationPlanListTab(capturedid) {
        browser.wait(this.isVisible(planListTab), this.timeout.xl)
        this.tabSelection(this.planListTab);
        browser.waitForAngular();
    }

    editPlanButtonSelect(capturedid) {
        var editPlanButton = element(by.css('[href="#/app/updateImpPlan/' + capturedid + '"]'));
        browser.wait(this.isClickable(editPlanButton), this.timeout.xl)
        this.tabSelection(editPlanButton);
        browser.waitForAngular();
    }

    planDetailViewSelect(capturedid) {
        var planDetailView = element(by.css('[aria-label="' + capturedid + '"]'));
        browser.wait(this.isVisible(planDetailView), this.timeout.xxl)
        this.tabSelection(planDetailView)
        browser.waitForAngular();
    }

    WebSocketcheck() {
        browser.wait(this.isDisplayed(this.check), this.timeout.full).then(function () {
            var toastMessage = element(by.css('md-toast-content'))
            expect(toastMessage.isDisplayed()).toBe(true)
            expect(toastMessage.getText()).toBe("OLDR/TLDR Generated for T1700547")
        })
    }
}

export default new UpdateImplementationPlan();