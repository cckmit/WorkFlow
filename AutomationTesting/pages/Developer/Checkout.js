import BasePage from '../Common/basePage';
import createimplementationinput from '../../data/createimplementationinput';
import updateImplementationinput from '../../data/updateImplementationinput';
import findid from '../../data/findid';
import Common from '../Common/Common';

class Checkout extends BasePage {
    constructor() {
        super();
        this.checkoutSelection = element(by.css('[aria-label="Checkout"]'));
        this.enterSegmentName = element(by.css('[placeholder="Search Segments"]'));
        this.productionList = element(by.css('[flag="PROD"]'));
        this.nonProductionList = element(by.css('[flag="NONPROD"]'));
        this.selectSegment = element(by.css('[aria-label="ProdSegmentListing"]')).all(by.tagName('li'));
        this.selectNonSegment = element(by.css('[aria-label="NonProdSegmentListing"]')).all(by.tagName('li'));
        this.checkProceedButton = element(by.css('.chkSelected'));
        this.checkoutfinalBtn = element(by.css('[aria-label="FinalCheckoutbutton"]'));
        this.checkoutBackBtn = element(by.css('[aria-label="CheckOut_Back"]'));
        this.searchProdandNonBtn = element(by.css('[aria-label="Search Prod/Non-Prod"]'));
        this.checkoutReport = element(by.css('[aria-label="Close"]'));
        this.checkoutReportTitel = element(by.css('.fa.fa-times'));
        this.loadingPage = element(by.css('[id="segmentSearch"]'));
        this.selectAllSegment = element(by.xpath('/html/body/ui-view/div/div/ui-view/section[2]/div/div/div/form/div/div/md-content/fieldset/div[4]/div/div/div[1]/div/li[2]/ul[1]/li[1]/md-checkbox/div[1]'));
        this.commitBtn = element(by.css('[aria-label="Commit"]'));
        this.checkinBtn = element(by.css('[aria-label="Check-In"]'));
        this.proceedCommit = element(by.css('[ng-click="proceedCommit()"]'));
        this.commitMessage = element(by.model('cm.commitMessage'));
        this.ibmVanillaBtn = element(by.css('[ng-click="togglePopulateSCM()"]'));
        this.ibmVanillaSearchBtn = element(by.css('[aria-label="vanillaSearchText"]'));
        this.selectVanillaSegment = element.all(by.css('.ibmVanila')).all(by.tagName('md-checkbox'));
        this.waitForAlert = element(by.css('.sweModal-content'));
        this.totalCheckoutCount = element(by.css('.checkoutSegmentsTitle'));
        this.ibmPopulateReport = element(by.css('.populateReportTemplate'));
        this.populateProceedBtn = element(by.css('[ng-click="populateVanillaSCM()"]'));
        this.deleteReport = element(by.css('[data-ng-click="removeSegment(segment)"]')).all(by.tagName('ul'));
    }

    populateProceedButton() {
        browser.wait(this.isDisplayed(this.populateProceedBtn), this.timeout.xl)
        this.singlebuttonclick(this.populateProceedBtn)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(this.populateProceedBtn), this.timeout.xl)

    }

    ibmVanillaSegmentSelect(sysdetail) {
        element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });

            }
        });
        browser.wait(this.isDisplayed(this.selectVanillaSegment), this.timeout.xl)
        browser.sleep(4000)
        element.all(by.css('md-checkbox[value*="' + sysdetail + '"]')).click();
        // return cbk
    }


    //    var apply = element.all(by.css('md-checkbox[value*="APO"]')).click()    


    /* ibmVanillaSegmentSelect(cbk) {
        browser.wait(this.isDisplayed(this.selectVanillaSegment), this.timeout.xl)
        browser.sleep(4000)
        var ibm = element.all(by.css('.ibmVanila')).all(by.tagName('md-checkbox'));
        ibm.filter(function (elem, index) {
            var emt = elem.toString();
            return ibm.getAttribute('class').then(function (text) {
                element.all(by.css('.ibmVanila')).all(by.tagName('md-checkbox')).get(index).click()
                return text != null;
            })
        }).then(function (params) {
            console.log("selection complete")
            return cbk();
        })

    } */

    ibmVanillaReport() {
        var report = element.all(by.css('.populateReportTemplate'))
        browser.wait(function () {
            return element(by.css('.populateReportTemplate')).isPresent().then(function (isPresent) {
                // browser.ignoreSynchronization = false;
                return isPresent
            });
        }, this.timeout.min);

        element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });

            }
        });

        $$('ul[ng-repeat="segment in vm.populatedVanillaFileList"]').each(function (row) {
            row.getText().then(function (text) {
                console.log(text.split("\n").join(" "))
            })
        });

        element(by.css('[aria-label="W"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.populatestatus = false
                console.log("Segments already populated")
                // cbk()
            }
        })

        element(by.css('[aria-label="Y"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.populatestatus = false
                console.log("Segments successfully populated")
                // cbk()
            }
        })

        element(by.css('[aria-label="N"]')).isPresent().then(function (isPresent) {
            if (isPresent === true) {
                findid.capture.populatestatus = false
                console.log("Segments population failed")
                expect(true).toBe(false)
                // cbk()
            }
        })


        browser.wait(this.isDisplayed(this.checkoutReport), this.timeout.xl)
        browser.sleep(2000)
        this.singlebuttonclick(this.checkoutReport)
    }

    ibmVanillaSearchButton() {
        var validate = element(by.css('[ng-if="ibmVanillaMetaData.searchList.length == 0 && !vm.showCheckoutLoading"]'))
        browser.wait(this.isVisible(validate), this.timeout.xl)
        this.singlebuttonclick(this.ibmVanillaSearchBtn)
    }

    ibmVanillaButton() {
        browser.wait(this.isDisplayed(this.ibmVanillaBtn), this.timeout.xl)
        this.singlebuttonclick(this.ibmVanillaBtn)
    }

    commitDialogueButton() {
        browser.wait(this.isDisplayed(this.proceedCommit), this.timeout.xl)
        this.singlebuttonclick(this.proceedCommit)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(this.proceedCommit), this.timeout.xl)
    }

    commitMessageEnter(message) {
        browser.wait(this.isDisplayed(this.commitMessage), this.timeout.xl)
        this.enterText(this.commitMessage, message)
    }

    checkinButtonSelect() {
        browser.wait(this.isClickable(this.checkinBtn), this.timeout.xl)
        this.singlebuttonclick(this.checkinBtn)
        browser.waitForAngular();
    }

    commitButtonSelect() {
        console.log("commit start")
        browser.wait(this.isClickable(this.checkinBtn), this.timeout.xl)
        this.singlebuttonclick(this.selectAllSegment)
        browser.sleep(2000)
        this.singlebuttonclick(this.commitBtn)
        console.log("commit end")
    }

    allSegementSelect() {
        this.singlebuttonclick(this.selectAllSegment)
    }

    implementationDetailSelect(capturedid) {
        var id = element(by.css('[aria-label="' + capturedid + '"]'))
        this.singleListSelect(id)
    }

    reportCloseButton() {
        // browser.sleep(2000)
        // this.singlebuttonclick(this.checkoutReport)
        browser.sleep(5000)
        console.log("close button")
        browser.wait(this.isDisplayed(this.checkoutReport), this.timeout.xl)
        this.singlebuttonclick(this.checkoutReport)
    }

    checkoutReportCheck(cbk) {
        browser.wait(function () {
            return element(by.css('.checkoutReportTemplate')).isPresent().then(function (isPresent) {
                browser.ignoreSynchronization = false;
                console.log("im in")

                element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
                    if (res) {
                        element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                            console.log("Success/Error Message :" + printmessage);
                            element(by.css('.okBtn')).click();
                            // cbk()
                        });
                    }
                });

                $$('ul[ng-repeat="segment in vm.checkoutStatusList"]').each(function (row) {
                    row.getText().then(function (text) {
                        console.log(text.split("\n").join(" "))
                    })
                });

                element(by.css('[ng-if="bObj.isCheckedout"]')).isPresent().then(function (isPresent) {
                    if (isPresent === true) {
                        findid.capture.checkoutstatus = false
                        console.log("checkout passed")
                        cbk()
                    }
                })
                element(by.css('[ng-if="!bObj.isCheckedout"]')).isPresent().then(function (isPresent) {
                    if (isPresent === true) {
                        findid.capture.checkoutstatus = false
                        console.log("checkout failed")
                        cbk()
                    }
                })
                return isPresent;
            });
        }, this.timeout.min);

        // this.singlebuttonclick(this.checkoutReport)
        /*   browser.sleep(5000)
          browser.wait(this.isDisplayed(this.checkoutReport), this.timeout.xl)
          this.singlebuttonclick(this.checkoutReport) */
    }

    checkoutSelectionButton() {
        console.log("Checkout selection button")
        browser.wait(this.isDisplayed(this.checkoutSelection), this.timeout.xl);
        browser.sleep(3000)
        this.singlebuttonclick(this.checkoutSelection)
        browser.wait(this.isDisplayed(this.searchProdandNonBtn), this.timeout.xl);
    }



    checkoutSegmentEnter(checkoutsegment) {
        var segment = $('input[placeholder="Search Segments"]')
        browser.wait(this.isVisible(segment), this.timeout.l);
        segment.click()
        segment.clear().sendKeys(checkoutsegment);
    }

    selectNonSegmentCheck(cbk) {
        var elem = element(by.css('[ng-if="nonProductionMetaData.searchList.length == 0 && !vm.showCheckoutLoading"]'));
        var self = this
        elem.isPresent().then(function (fnct) {
            if (fnct === true) {
                elem.getText().then(function (value) {
                    console.log("In-Nonproduction " + value)
                });
                cbk()
            } else {
                var indexvalue = 0
                self.buttonclick(self.selectNonSegment, indexvalue)
                console.log("Non prod checkout selected")
                cbk()
            }

        });

    }

    deleteSearchSegment(cbk) {
        var elem = element(by.css('[ng-if="segmentsList.length!=0"]'));
        var self = this
        elem.isPresent().then(function (fnct) {
            console.log("non prod present")
            if (fnct) {
                $$(".checkoutSegmentsTitle .pull-right").getText().then(function (text) {
                    var total = text.toString().split("")[7]
                    if (total >= 2) {
                        element.all(by.css('[data-ng-click="removeSegment(segment)"]')).get(1).click();
                        cbk()
                    } else {
                        console.log("No Non prod segments")
                        cbk()
                    }
                })
            } else {
                console.log("No Non prod segments to delete")
                cbk()
            }

        });
    }

    selectProductionTabSelect() {
        this.tabSelection(this.productionList)
    }

    NonProductionTabSelect() {
        this.tabSelection(this.nonProductionList)
    }

    searchProdandNonButton() {
        browser.wait(this.isDisplayed(this.searchProdandNonBtn), this.timeout.xxl)
        this.singlebuttonclick(this.searchProdandNonBtn)
        browser.waitForAngular();
        browser.wait((this.selectSegment), this.timeout.xxl)
        browser.sleep(2000)
        element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });

            }
        });

    }
    /*   selectSegmentCheck() {
          element.all(by.css('modalProgramListing')).getAttribure("class").then(function (chktext) {
              console.log(chktext)
          })
          var indexvalue = 0
          this.buttonclick(this.selectSegment, indexvalue)
      }
    */

    selectSegmentCheck(fun_Sys_Obj) {
        // var foundindex
        $$('ul[ng-repeat="individualSegmentObj in productionMetaData.searchList"]').each(function (row_1, index) {
            row_1.$$('li.col-md-9 div[data-ng-repeat="branchObj in individualSegmentObj.branch"]').count().then(function (count_1) {
                // console.log("count_1   :" + count_1)
                if (count_1 > 1) {
                    console.log("common segment")
                    row_1.$$('div[data-ng-repeat="branchObj in individualSegmentObj.branch"]').each(function (row_2) {
                        row_2.getText().then(function (row_text) {
                            console.log("++++++++++++++++++++++++++++++++++++++")
                            console.log(row_text.split("\n").join(" "))
                            // fun_Sys_Obj.systemspecific.map(function(systemName) {
                            console.log(fun_Sys_Obj.functionalarea + " " + fun_Sys_Obj.systemspecific + row_text.split("\n")[0] + " --> " + fun_Sys_Obj.systemspecific.indexOf(row_text.split("\n")[0]))
                            // row_text.match(new RegExp(systemName, 'gi')) == null

                            row_1.$$("md-checkbox").count().then(function (cnt) {
                                // console.log("cnt" + cnt)

                                if (cnt == 1 && row_text.match(new RegExp("Online-", 'gi')) == null && row_text.match(new RegExp(fun_Sys_Obj.functionalarea, 'gi')) != null && fun_Sys_Obj.systemspecific.indexOf(row_text.split("\n")[0]) < 0) {
                                    console.log("there is only one checkbox")
                                    element(by.css('.modalProgramListing')).all(by.tagName('ul')).get(index).click()
                                }
                                else {
                                    if (row_text.match(new RegExp("Online-", 'gi')) == null && row_text.match(new RegExp(fun_Sys_Obj.functionalarea, 'gi')) != null && fun_Sys_Obj.systemspecific.indexOf(row_text.split("\n")[0]) < 0) {
                                        console.log("->Click Action<-")
                                        row_2.$$("md-checkbox").get(0).getAttribute("aria-checked").then(function (aria_status) {
                                            // element(by.css('.modalProgramListing')).all(by.tagName('ul')).get(index).click()
                                            console.log(row_text.split("\n")[0] + " is " + aria_status)
                                            if (aria_status == true || aria_status == "true") {
                                                row_2.all(by.css('.branchCheck')).get(0).click()
                                                browser.sleep(2000)
                                                element(by.css('.warnProceed')).click()
                                            }
                                        })
                                    }
                                }
                                // })
                            })
                        })
                    })
                } else {
                    console.log("single seperate segment")
                    row_1.getText().then(function (row_text1) {
                        console.log("----------------")
                        // console.log(fun_Sys_Obj.systemspecific.indexOf(row_text1.split("\n")[0]) + "seperate segment" + fun_Sys_Obj.functionalarea + "  fun: " + fun_Sys_Obj.systemspecific + "")
                        console.log(": else part  :" + index)
                        /* console.log("ROWTEST " + row_text1 + "  row end")
                        console.log("ROWTEST1 " + row_text1.split("\n") + "  row end")
                        console.log("+++++_++_+++_+__+_+_+_+_+_+_")
                        console.log(row_text1.match(new RegExp("Online-", 'gi')) == null)
                        console.log(row_text1.match(new RegExp(fun_Sys_Obj.functionalarea, 'gi')) != null)
                        console.log(fun_Sys_Obj.systemspecific.indexOf(row_text1.split("\n")[1]) >= 0) */
                        if (row_text1.match(new RegExp("Online-", 'gi')) == null && row_text1.match(new RegExp(fun_Sys_Obj.functionalarea, 'gi')) != null && fun_Sys_Obj.systemspecific.indexOf(row_text1.split("\n")[1]) >= 0) {
                            /* console.log(fun_Sys_Obj.systemspecific.indexOf(row_text1.split("\n")[1]) + "seperate segment" + fun_Sys_Obj.functionalarea + "  fun: " + fun_Sys_Obj.systemspecific + "") */
                            row_1.$$('li[data-ng-click="selectSegmentPNP(individualSegmentObj)"]').get(0).click();
                        }
                    })

                    /* .each(function (row_2) {
                        console.log(": row_1  :" + row_2)
                        row_2.getText().then(function (row_text) {
                            console.log("row_text" + row_text + ": row_1  :" + row_1)
                            if (row_text.match(new RegExp(fun_Sys_Obj.functionalarea, 'gi')) != null && fun_Sys_Obj.systemspecific.indexOf(row_text.split("\n")[0]) < 0) {
                                row_1.click()
                            }
                        })
                    }) */
                }
            })
        })
        console.log("checkdone")
    }


    checkProceedSelection(cbk) {
        browser.wait(this.isVisible(this.checkProceedButton), this.timeout.xxl)
        this.singlebuttonclick(this.checkProceedButton)
        browser.wait(this.isNotVisible(this.checkProceedButton), this.timeout.xxl)
        cbk()
    }
    checkouFinaltButton() {
        browser.wait(this.isVisible(this.checkoutfinalBtn), this.timeout.xxl)
        this.singlebuttonclick(this.checkoutfinalBtn)
        browser.sleep(2000)
        element(by.css('.sweModal-content')).isDisplayed().then(function (res) {
            if (res) {
                element(by.css('.sweModal-content')).getText().then(function (printmessage) {
                    console.log("Success/Error Message :" + printmessage);
                    element(by.css('.okBtn')).click();
                });

            }
        });
    }

    checkoutBackButtonSelect() {
        this.singlebuttonclick(this.checkoutBackBtn)
    }
}

export default new Checkout();