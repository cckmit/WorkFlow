import BasePage from '../Common/basePage';
import Common from '../Common/Common';
import leadinput from '../../data/leadinput';
import findid from '../../data/findid';
import { element } from 'protractor';


var PlanId = 0;
class CreateImplementationPlan extends BasePage {
    constructor() {
        super();
        this.createImplementationPlan = element(by.css('a[href*="#/app/newImpPlan"]'));
        this.resultsPage = $('a[href*="#/app/newImpPlan"]');
        this.companyName = element(by.model('vm.impPlan.platformId')).all(by.tagName('md-radio-button'));
        this.targetSystem = element(by.css('[ng-show="vm.showSystem"]')).all(by.tagName('md-checkbox'));
        this.descriptionPlan = element(by.model('vm.impPlan.planDesc'));
        this.leadComment = element(by.model('vm.impPlan.mgmtComment'));
        this.loadCategory = element(by.css('[name="loadCategories"]'));
        this.loadTime = element(by.css('[name="loadTime"]'));
        this.loadAttendee = element.all(by.css('[name="loadAttendee"]'));
        this.leadContact = element(by.model('vm.impPlan.leadContact'));
        this.saveButton = element(by.css('.planSaveBtn'));
        this.companyCheck = element(by.css('[aria-label="platform"]'));
        this.prompt = element(by.css('.sweModal-content'));
        this.messageCheck = element(by.css('.okBtn'));
        this.dbcrCreateBtn = element.all(by.css('.input-addition-btn'));
        this.dbcrEnter = element(by.model('dbcr.fieldData.dbcrName'));
        this.dbcrValidateBtn = element.all(by.css('[ng-click="validateDbcr()"]'))
        this.dbcrActivate = element.all(by.model('dbcrObj.mandatory'));
        this.dbcrAdd = element(by.css('[ng-click="dbcrLoadAdd()"]'))
        this.dbcrSaveBtn = element(by.css('[ng-click="saveDBCRList()"]'))
    }

    dbcrValidation(dbcrdetails, idfound) {
        this.singlebuttonclick(this.dbcrCreateBtn.get(idfound))
        var lead_dbcr = leadinput.createImplementationplan.targetsystemid[idfound].dbcr
        for (var i = 0; i < lead_dbcr.length; i++) {
            this.enterText(this.dbcrEnter, lead_dbcr[i])
            this.singlebuttonclick(this.dbcrValidateBtn)
            browser.waitForAngular()
            var self = this
            var validate = element(by.css('[ng-show="dbcr.validation.showText"]'))
            validate.getText().then(function (dbcr) {
                // console.log(dbcr)
                if (dbcr !== "Invalid DBCR") {
                    console.log("Success/Error Message :DBCR validation pass")
                    browser.sleep(3000)
                    self.singlebuttonclick(self.dbcrAdd)
                    self.singleListSelect(self.dbcrActivate.get(0))
                    self.singlebuttonclick(self.dbcrSaveBtn)
                } else {
                    console.log("Success/Error Message :DBCR validation failed")
                    browser.sleep(2000)
                }
            })
        }
        var validate = element.all(by.css('button[ng-click*="useful"]')).click()

    }


    waitAction(e) {
        browser.wait(this.isVisible(e), this.timeout.l);
    }

    switchToNewImplementationTab() {
        this.tabSelection(this.createImplementationPlan)
        browser.waitForAngular();
    }

    selectByDateCalander() {
        var someDate = new Date();
        var numberOfDaysToAdd = 6;
        someDate.setDate(someDate.getDate() + numberOfDaysToAdd);
        someDate = someDate.toISOString().substr(0, 10);
        Common.byDateStandardCalander(someDate)
    }

    selectByCategoryCalander(i) {
        var someDate = new Date();
        var numberOfDaysToAdd = 4;
        someDate.setDate(someDate.getDate() + numberOfDaysToAdd);
        someDate = someDate.toISOString().substr(0, 10);
        browser.wait(this.isVisible(this.companyCheck), this.timeout.xl)
        Common.byCategoryStandardCalander(someDate, i)
    }

    getPlanId(cap, cbk) {
        browser.wait(this.isVisible(this.prompt), this.timeout.xl)
        element(by.css('.sweModal-content')).isDisplayed().then(function (idfound) {
            if (idfound) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    cap = printmessage.split(" ")[2];
                    findid.capture.planidcapture = cap
                    element(by.css('.okBtn')).click();
                    cbk(cap);
                });
            }
        });
        return browser.wait(this.isNotVisible(this.messageCheck), this.timeout.xl)
        browser.sleep(3000);
    }

    companyNameSelect() {
        browser.wait(this.isClickable(this.companyCheck), this.timeout.xl)
        element(by.model('vm.impPlan.platformId')).all(by.css('[value*="' + leadinput.createImplementationplan.companynameid + '"]')).click()
        // this.buttonclick(this.companyName, leadinput.createImplementationplan.companynameid);
        browser.waitForAngular();
    }

    targetSystemSelect(targetsystemid) {
        browser.wait(this.isClickable(this.saveButton), this.timeout.xl)
        browser.sleep(1000);
        var sysid = element(by.css('md-checkbox[class*="check_' + targetsystemid + '"]'))
        this.singleListSelect(sysid)
        browser.waitForAngular();
    }

    csrSelect() {
        browser.waitForAngular();
        var self = this
        var csr = element(by.css('[aria-labelledby="select2-csrId-container"]'))
        browser.wait(this.isClickable(csr), this.timeout.xl)
        element(by.css('[aria-labelledby="select2-csrId-container"]')).click().then(function () {
            element(by.css('.select2-search__field')).click().sendKeys(leadinput.createImplementationplan.csrid).then(function () {
                browser.waitForAngular();
                browser.sleep(3000);
                element(by.css('.select2-results__options ')).all(by.tagName('li')).get(0).click()
            })
        })
    }

    approvalManagerSelect() {
        element(by.css('[aria-labelledby="select2-approver-container"]')).click().then(function () {
            element(by.css('.select2-search__field')).click().sendKeys(leadinput.createImplementationplan.approvalmanager).then(function () {
                element(by.css('.select2-results__options')).all(by.tagName('li')).get(0).click()
            })
        })

    }

    planDescription() {
        this.enterText(this.descriptionPlan, leadinput.createImplementationplan.plandescription);
    }

    leadComments() {
        this.enterText(this.leadComment, leadinput.createImplementationplan.comments);
    }

    loadWindowSelect(loadwindow, i) {
        browser.waitForAngular();
        // console.log("loadwindow",loadwindow,"index",i)
        var loadwindowelement = element.all(by.css('[aria-label="' + loadwindow + '"]')).get(i)
        browser.wait(this.isClickable(loadwindowelement), this.timeout.xl)
        this.tabSelection(loadwindowelement)
        browser.waitForAngular();
    }

    loadcat(loadcategory, id, i) {
        console.log(loadcategory + "loadcategory.length " + loadcategory.length)
        $$(by.css('[name="loadCategories"] option')).each(function (el, index) {
            el.getText().then(function (txt) {
                console.log(txt)
                tryit = txt;
                console.log(tryit + "loadcategory  :" + loadcategory)

            })
        })
    }

    loadCategorySelect(loadcategory, id, i) {
        var self = this
        var el = $$('[name="loadCategories"] option')
        /*         console.log("loadcategory.length " + loadcategory.length)
                for (var index = 0; index < loadcategory.length; index++) {
        
                }
         */
        console.log("loadcategory.length " + loadcategory.length + "  asd" + loadcategory)
        element(by.css('[aria-label="PGR"] [name="loadCategories"] select,option:contains("F")')).click();

        /* 
                element(by.css('[aria-label="' + id + '"]')).then(function (eid) {
                    eid.$(by.css('[name="loadCategories"] option'))
                    $$(by.css('[name="loadCategories"] option')).each(function (elem, index) {
                        elem.getText().then(function (txt) {
                            console.log(txt)
                            console.log("loadcategory  :" + loadcategory)
                            if (txt.match(new RegExp(loadcategory, 'gi')) != null) {
                                // console.log("Target System " + targetloadcategoryobj + " in index" + index)
                                el.getAttribute("value").then(function (check) {
                                    console.log("check1  " + check)
                                    var trimvalue = parseInt(check.trim())
                                    // var cat = element.all(by.css('[name="loadCategories"]')).get(i).$('option[value="' + trimvalue + '"]');
                                    var cat = elem.$('option[value="' + trimvalue + '"]')
                                    self.tabSelection(cat)
                                })
                            }
                        })
                    })
                })
         */
        /* 
                element(by.repeater("sList in vm.systemDetailsList")).get(i).then(function (ele) {
                    ele.filter(function (elem, index) {
                        elem.getText().then(function (txt) {
                            console.log(txt)
                            console.log("loadcategory  :" + loadcategory)
                            if (txt.match(new RegExp(loadcategory, 'gi')) != null) {
                                // console.log("Target System " + targetloadcategoryobj + " in index" + index)
                                el.getAttribute("value").then(function (check) {
                                    console.log("check1  " + check)
                                    var trimvalue = parseInt(check.trim())
                                    var cat = element.all(by.css('[name="loadCategories"]')).get(i).$('option[value="' + trimvalue + '"]');
                                    self.tabSelection(cat)
                                })
                            }
                        })
                    })
                }).first().click();
         */

        /*     
           console.log(loadcategory + "loadcategory.length " + loadcategory.length)
           $$(by.css('[name="loadCategories"] option')).filter(function (el, index) {
               el.getText().then(function (txt) {
                   console.log(txt)
                   console.log("loadcategory  :" + loadcategory)
                   if (txt.match(new RegExp(loadcategory, 'gi')) != null) {
                       // console.log("Target System " + targetloadcategoryobj + " in index" + index)
                       el.getAttribute("value").then(function (check) {
                           console.log("check1  " + check)
                           tick = check
                       }).then(function (tick) {
                           // var trimvalue = parseInt(tick.trim())
                           console.log("check2  " + tick)
                           var cat = element.all(by.css('[name="loadCategories"]')).get(i).$('option[value="' + tick + '"]');
                           self.tabSelection(cat)
                       })
                   }
               })
           }).first().click()
    */

        /* 
                element.all(by.css('[name="loadCategories"] option')).filter(function (elem, index) {
                    return elem.getText().then(function (text) {
                        var trimvalue = parseInt(check.trim())
                        console.log("check  " + trimvalue)
                        return text === 'F';
                    });
                }).first().click();
        
         
                    $$('[name="loadCategories"] option').filter(function (el, index) {
                        el.getText().then(function (txt) {
                            console.log(txt)
                            console.log("loadcategory  :" + loadcategory)
                            if (txt.match(new RegExp(loadcategory, 'gi')) != null) {
                                // console.log("Target System " + targetloadcategoryobj + " in index" + index)
                                el.getAttribute("value").then(function (check) {
                                    var trimvalue = parseInt(check.trim())
                                    console.log("check  " + trimvalue)
                                    var cat = element.all(by.css('[name="loadCategories"]')).get(i).$('option[value="' + trimvalue + '"]');
                                    self.tabSelection(cat)
                                })
                            }
                        })
                    }).first().click() 
                     */
        // var cat = element.all(by.css('[name="loadCategories"]')).get(i).$('[value="' + loadcategory + '"]');
        // browser.wait(this.isClickable(cat), this.timeout.xl)
        // this.tabSelection(cat)
    }

    loadTimeSelect(loadtime, i) {
        var loadTimeValue = element.all(by.css('[name="loadTime"]')).get(i).$('[value="' + loadtime + '"]');
        browser.wait(this.isClickable(loadTimeValue), this.timeout.xl)
        this.tabSelection(loadTimeValue);
    }

    loadAttendeeSelect(loadattendee, idfound) {
        this.enterText(this.loadAttendee.get(idfound), loadattendee);
    }

    preLoadSelect(preload, idfound) {
        var preloadelement = element.all(by.css('[aria-label="' + preload + '"]')).get(idfound);
        browser.wait(this.isClickable(preloadelement), this.timeout.xl)
        this.tabSelection(preloadelement);
    }

    leadContactSelect() {
        this.enterText(this.leadContact, leadinput.createImplementationplan.leadcontact)
    }

    saveButtonSelect() {
        this.tabSelection(this.saveButton)
        browser.wait(this.isVisible(this.prompt), this.timeout.xl)
    }

}

export default new CreateImplementationPlan();
