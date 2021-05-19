import BasePage from '../Common/basePage';
import leadinput from '../../data/leadinput';
import findid from '../../data/findid';
import { browser, element } from 'protractor';

class Common extends BasePage {
    constructor() {
        super();
        this.updateButton = element(by.css('.planSaveBtn'));
        this.messageCheck = element(by.css('.okBtn'));
        this.logOutPage = element(by.css('[aria-label="logoutButton"]'));
        this.calanderWait = element.all(by.css('.dow-clickable'));
        this.prompt = element(by.css('.sweModal-content'));
        this.logout = element(by.css('[ng-click="logout()"]'))
    }

    logoutWorkflow() {
        var out = element(by.css('[aria-label="logoutButton"]')).click();
        this.singlebuttonclick(this.logout)
        browser.waitForAngular();
    }

    triggerTimeout(e, count) {
    }

    updatePlanButtonSelect() {
        browser.wait(this.isDisplayed(this.updateButton), this.timeout.xxl)
        this.tabSelection(this.updateButton)
        browser.waitForAngular();
    }

    byDateStandardCalander(dateInput, i) {
        $('.fa.fa-calendar').click();
        browser.sleep(3000)
        element.all(by.css('.calendarDisable')).getAttribute("id").then(function (random) {
            var nextpage = random.toString().split("_")[2];
            element(by.id('zabuto_calendar_' + nextpage + '_nav-next')).click();
        })
        browser.wait(function () {
            return element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
                var item = value;
                return value !== '';
            });
        });

        element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
            var selectdate = [];
            var o = {};
            var revalue = value.toString().split("_")[2];
            var Actualdate = value.toString().split("_")[3];
            var DateReplace1 = Actualdate.replace(Actualdate, dateInput);
            var sstr = element(by.id('zabuto_calendar_' + revalue + '_' + DateReplace1 + '_day')).click();
            browser.sleep(1000);
        });
    }

    webSocketVerify(cbk) {

        browser.wait(function () {
            browser.ignoreSynchronization = true;
            return element(by.css('.md-toast-content')).isPresent().then(function (isPresent) {
                browser.ignoreSynchronization = false;
                return isPresent;
            });
        });
        console.log("Next to wait")
        element(by.css('.md-toast-content')).getText().then(function (response) {
            console.log(response)
            if (response.indexOf("Generated") >= 0) {
                cbk("success")
            } else {
                cbk("failed")
            }
        })
    }

    byCategoryStandardCalander(dateInput, i) {
        // console.log("fa.fa-calendar",i)
        // $$('.fa-calendar').count().then(function (cnt) {
        //     console.log(cnt, "Date  :", dateInput)
        // })

        var incre_day = i
        if (incre_day == 0) {
            incre_day = 1
        } else if (incre_day == 1) {
            incre_day = 3
        } else if (incre_day == 2) {
            incre_day = 5
        } else if (incre_day == 3) {
            incre_day = 7
        }
        console.log(incre_day)
        // browser.sleep(1000)
        // $$('.fa-calendar').get(incre_day).click()
        browser.sleep(3000)
        element.all(by.css('.calendarDisable')).getAttribute("id").then(function (random) {
            var nextpage = random.toString().split("_")[2];
            element(by.id('zabuto_calendar_' + nextpage + '_nav-next')).click();
        })
        browser.wait(function () {
            return element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
                var item = value;
                // console.log("item", item);
                return value !== '';
            });
        });
        var self = this
        element.all(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
            var selectdate = [];
            var o = {};
            var revalue = value.toString().split("_")[2];
            var Actualdate = value.toString().split("_")[3];
            var DateReplace1 = Actualdate.replace(Actualdate, dateInput);
            // element(by.css('.calendar-month-navigation')).click();
            var sstr = element(by.id('zabuto_calendar_' + revalue + '_' + dateInput + '_day')).click();
            browser.sleep(2000);
        })
        browser.sleep(2000);
    }

    /* byCategoryStandardCalander(dateInput, i) {
        // console.log("fa.fa-calendar",i)
        // $$('.fa-calendar').count().then(function (cnt) {
        //     console.log(cnt, "Date  :", dateInput)
        // })
        var incre_day = i
        if (incre_day == 0) {
            incre_day = 1
        } else if (incre_day == 1) {
            incre_day = 3
        } else if (incre_day == 2) {
            incre_day = 5
        } else if (incre_day == 3) {
            incre_day = 7
        }
        // console.log(incre_day)
        var self = this
        browser.sleep(2000)
        $$('.fa-calendar').get(incre_day).click()
        browser.wait(function () {
            return element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
                var item = value;
                // console.log("item", item);
                return value !== '';
            });
        });
        browser.sleep(3000)
        element.all(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
            var selectdate = [];
            var o = {};
            var revalue = value.toString().split("_")[2];
            var Actualdate = value.toString().split("_")[3];
            var DateReplace1 = Actualdate.replace(Actualdate, dateInput);
            var sstr = element(by.id('zabuto_calendar_' + revalue + '_' + dateInput + '_day'))
            browser.wait(self.isClickable(sstr), self.timeout.xl)
            element(by.id('zabuto_calendar_' + revalue + '_' + dateInput + '_day')).get(0).then(function
                (ele) {
                ele.click();
                cbk()
            })
            browser.sleep(2000);
        })
        browser.sleep(2000);
    } */

    pageSize() {
        var pageSelect = element(by.model('tableConfig.pageSize'))
        var sysid = pageSelect.$('[value="' + findid.capture.pagesizelist + '"]')
        this.singleListSelect(sysid)
    }

    alertMessage() {
        browser.wait(this.isVisible(this.prompt), this.timeout.xxl)
        element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });

            }
        });
        return browser.wait(this.isNotVisible(this.messageCheck), this.timeout.xl)
        browser.sleep(3000);
    }

    alertMessage1(cbk) {
        console.log("inside alert")
        browser.wait(function () {
            return element(by.css('.sweModal-content')).isDisplayed().then(function (isPresent) {
                console.log("I am waiting - isPresent " + isPresent)
                if (isPresent) {
                    console.log("is present return")
                    findid.capture.adlactivationstatus = false
                    element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                        console.log("Success/Error Message :" + printmessage);
                        element(by.css('.okBtn')).click();
                    });
                }
                cbk()
                return isPresent
            });
        }, this.timeout.max);
        browser.sleep(3000);
    }

    refreshPage() {
        var refreshButton = element.all(by.css('btn refreshButton'));
        element.all(by.css('btn refreshButton')).isDisplayed().then(function (e) {
            refreshButton.click();
            console.log("Refresh button clicked!!!!");
        })
    }

    logOutPageButton() {
        this.singlebuttonclick(this.logOutPage)
    }

}

export default new Common();