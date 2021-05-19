import BasePage from '../Common/basePage';
import leadinput from '../../data/leadinput';
import findid from '../../data/findid';

class Common extends BasePage {
    constructor() {
        super();
        this.updateButton = element(by.css('.planSaveBtn'));
        this.messageCheck = element(by.css('.okBtn'));
        this.logOutPage = element(by.css('[aria-label="logoutButton"]'));
        this.calanderWait = element.all(by.css('.dow-clickable'));
        this.prompt = element(by.css('.sweModal-content'));
    }
    triggerTimeout(e, count) {

    }
    updatePlanButtonSelect() {
        browser.wait(this.isDisplayed(this.updateButton), this.timeout.xl)
        this.tabSelection(this.updateButton)
        browser.waitForAngular();
    }

    byDateStandardCalander(Date) {
        element(by.css('.fa.fa-calendar')).click();
        browser.sleep(3000)
        element.all(by.css('.calendarDisable')).getAttribute("id").then(function (random) {
            // console.log("random"+random)
            var nextpage = random.toString().split("_")[2];
            // console.log(nextpage)
            element(by.id('zabuto_calendar_' + nextpage + '_nav-next')).click();
        })
        browser.wait(function () {
            return element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
                var item = value;
                // console.log(item);
                return value !== '';
            });
        });


        element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
            // console.log(value);
            var selectdate = [];
            var o = {};
            var revalue = value.toString().split("_")[2];
            // console.log('ramdomvalue' + revalue);
            var Actualdate = value.toString().split("_")[3];
            // console.log('Dateassign' + Actualdate);
            var DateReplace1 = Actualdate.replace(Actualdate, Date);
            // var DateReplace1 = Actualdate.replace(Actualdate, "2017-10-06");
            var sstr = element(by.id('zabuto_calendar_' + revalue + '_' + DateReplace1 + '_day')).click();
            browser.sleep(1000);
            //JSON.stringify(selectdate);
        });


    }

    webSocketVerify(cbk) {

        browser.wait(function () {
            browser.ignoreSynchronization = true;
            return element(by.css('.md-toast-content')).isPresent().then(function (isPresent) {
                // Cleanup so later tests have the default value of false
                // console.log("It is present")s
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
                // throw new Error('old/tldr not responding! so proceeding to next!');
                cbk("failed")
            }
        })
    }

    byCategoryStandardCalander(Date) {
        element.all(by.css('.fa.fa-calendar')).get(1).click();
        browser.sleep(3000)
       /*  element.all(by.css('.calendarDisable')).getAttribute("id").then(function (random) {
            // console.log("random"+random)
            var nextpage = random.toString().split("_")[2];
            // console.log(nextpage)
            element(by.id('zabuto_calendar_' + nextpage + '_nav-next')).click();
        }) */
        /* element.all(by.css('.calendarDisable')).isPresent().then(function (isPresent) {
            console.log(isPresent)
            browser.sleep(3000)
            element.all(by.css('.calendarDisable')).getAttribute("id").then(function (random) {
                console.log(random)
                var nextpage = random.toString().split("-")[2];
                console.log(nextpage)
            })
            element(by.id('zabuto_calendar_' + nextpage + '_nav-next')).click();
        }) */
        browser.wait(function () {
            return element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
                var item = value;
                console.log(item);
                return value !== '';
            });
        });

        element(by.css('.dow-clickable')).getAttribute("id").then(function (value) {
            // console.log(value);
            var selectdate = [];
            var o = {};
            var revalue = value.toString().split("_")[2];
            // console.log('ramdomvalue' + revalue);
            var Actualdate = value.toString().split("_")[3];
            // console.log('Dateassign' + Actualdate);
            // console.log('nextpage' + nextpage);
            var DateReplace1 = Actualdate.replace(Actualdate, Date);
            // var DateReplace1 = Actualdate.replace(Actualdate, "2017-10-06");
            var sstr = element(by.id('zabuto_calendar_' + revalue + '_' + DateReplace1 + '_day')).click();
            browser.sleep(2000);
            //JSON.stringify(selectdate);
        });
        browser.sleep(2000);
    }

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

    alertMessage1() {

        // var report = element.all(by.css('.sweModal-content'))
        browser.wait(function () {
            return element(by.css('.sweModal-content')).isPresent().then(function (isPresent) {
                // Cleanup so later tests have the default value of false
                // console.log("Report not generated")
                element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
                    if (res) {
                        element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                            console.log("Success/Error Message :" + printmessage);
                            element(by.css('.okBtn')).click();
                        });

                    }
                });
                return isPresent
            });
        }, this.timeout.max);

        browser.sleep(3000);
    }

    refreshPage() {
        //Refreshing Freeze date list page
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