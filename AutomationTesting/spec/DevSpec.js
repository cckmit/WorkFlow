import DeveloperLogin from '../pages/Developer/DeveloperLogin';
import Credential from '../data/Credential';
import LoginPage from '../pages/Common/LoginPage';
import UpdateImplementation from '../pages/Common/UpdateImplementation';
import Common from '../pages/Common/Common';
import Checkout from '../pages/Developer/Checkout';
import StatusAndGit from '../pages/Developer/StatusAndGit';
import findid from '../data/findid';
import ReviewerTask from '../pages/Reviewer/ReviewerTask';
import ReviewerLogin from '../pages/Reviewer/ReviewerLogin';
import GitblitTask from '../pages/Reviewer/GitblitTask';
import ApprovedTask from '../pages/Reviewer/ApprovedTask';
import reviewerinput from '../data/reviewerinput';
import updateImplementationinput from '../data/updateImplementationinput';

describe('Developer login', () => {
    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Developer Credentials', () => {
        LoginPage.loginAs(Credential.Developer)
        browser.sleep(2000);
        DeveloperLogin.switchToDeveloper();
        browser.sleep(3000);
    });

    var update_impl_list = updateImplementationinput.updateimpl.multipleimpl;

    update_impl_list.map(function (impl_Obj, i) {

        it('Edit implementation details ' + i, () => {
            var currentIteration = i;
            console.log("edit imp details")
            // console.log(findid.capture.planidcapture + "_00" + (currentIteration + 1))
            UpdateImplementation.implementationEditButton(findid.capture.planidcapture, findid.capture.planidcapture + "_00" + (currentIteration + 1));
            UpdateImplementation.developerContactSelect(impl_Obj.developercontact);
            UpdateImplementation.developerDescriptionSelect(impl_Obj.developerdescription);
            UpdateImplementation.developerLocationSelect(impl_Obj.developerlocation);
            // UpdateImplementation.updateButtonSelect();
            // Common.alertMessage();

        })
/* 
        var populatelength = impl_Obj.populatesegment
        if (populatelength.length > 0) {

            it('Populate and checkout', () => {
                console.log("populate and checkout")
                browser.sleep(3000)
                Checkout.checkoutSelectionButton();

                impl_Obj.populatesegment.map(function (popObj) {
                    Checkout.checkoutSegmentEnter(popObj.segment);
                    Checkout.ibmVanillaButton();
                    Checkout.ibmVanillaSearchButton();
                    popObj.systemspecific.map(function (sys_obj) {
                        Checkout.ibmVanillaSegmentSelect(sys_obj);
                    })
                    Checkout.populateProceedButton();
                    Checkout.ibmVanillaReport();

                })
            });
        }

        var checkoutlength = impl_Obj.checkoutsegment
        if (checkoutlength.length > 0) {
            console.log("checkout segemes about to start")
            it('Checkout search and selection ' + i, () => {
                Checkout.checkoutSelectionButton();
                // console.log("Length " + impl_Obj.checkoutsegment.length)
                impl_Obj.checkoutsegment.map(function (chkObj, j) {
                    // it('Checkout segment entered ' + (j + 1), () => {
                    // console.log("22", chkObj)
                    Checkout.checkoutSegmentEnter(chkObj.name);
                    browser.sleep(1000)
                    Checkout.selectProductionTabSelect();
                    // it('search segment and checkout', () => {
                    Checkout.searchProdandNonButton();
                    browser.sleep(1000)
                    Checkout.selectSegmentCheck(chkObj);
                    // });
                })
            });

            it('Checkout proceed and report ', (done) => {
                console.log("proceed")
                Checkout.checkProceedSelection(function () {
                    done()
                });
            });

            it('Nonprod segment select and delete', () => {
                console.log("nonprod segm")
                // Checkout.deleteSearchSegment(cbk);
                Checkout.checkouFinaltButton();
            });

            it('checkout report', (done) => {
                console.log("checkout report")
                Checkout.checkoutReportCheck(function (cbk) {
                    done()
                })
                // Checkout.checkoutReportCheck(function (cbk, err) {
                //     if (err) {
                //         console.log("yes failed   :" + err)
                //     }
                //     else {
                //         console.log("no its passed   :" + err)
                //     }
                //     done()
                // })

            });
            it('close checkout report', () => {
                Checkout.reportCloseButton()
            });
            //populate and checkout///
            //  it('Checkout proceed and report ' + i, (done) => {
            //     Checkout.checkProceedSelection(function (res) {
            //         done()
            //     });
            // });

        }

        it('Segments local commit' + i, () => {
            Checkout.commitButtonSelect();
            browser.sleep(2000)
            Checkout.commitMessageEnter(impl_Obj.commitmessage);
            Checkout.commitDialogueButton();
            Common.alertMessage();
        });

        it('Segments checkin' + i, () => {
            Checkout.checkinButtonSelect();
            Common.alertMessage();
        });

        it('Unit testing completed' + i, () => {
            browser.waitForAngular();
            browser.sleep(3000)
            StatusAndGit.unitTestingStatusChange();
            Common.alertMessage();
        });
 */
        it('Request for peer reviewer' + i, () => {
            var currentIteration = i;
            //Back to developer inbox
            // UpdateImplementation.developerMytaskTabSelect();
            browser.waitForAngular();

            //Request for peer reviewer
            // StatusAndGit.requestPeerReviewSelect(findid.capture.planidcapture + "_00" + (currentIteration + 1));
            StatusAndGit.requestPeerButton()
            Common.alertMessage();
        });


    })

    it('Workflow logout', () => {
        Common.logoutWorkflow();
    });


    it('Reviewer Login', () => {
        LoginPage.loginAs(Credential.Reviewer)
        browser.sleep(2000);
        ReviewerLogin.switchToReviewer();
    });

    update_impl_list.map(function (reviewer_Obj, i) {
        var currentIteration = i;

        it('Reviwer Task Tab' + i, () => {
            ReviewerTask.detailsViewSelect(findid.capture.planidcapture + "_00" + (currentIteration + 1));
        });

        it('Select Segment for reviewer' + i, () => {
            browser.sleep(3000)
            ReviewerTask.selectAllForReview(findid.capture.planidcapture + "_00" + (currentIteration + 1));
        });

        it('ReviewTask Complete' + i, () => {
            ReviewerTask.reviewCompleted(findid.capture.planidcapture + "_00" + (currentIteration + 1));
        });

        it('Mark as Complete' + i, () => {
            ReviewerTask.markCompleteButton(findid.capture.planidcapture + "_00" + (currentIteration + 1));
        });

        it('Reviewer task completed' + i, () => {
            Common.alertMessage();
        });


    })


    // it('approved task tab', () => {
    //     ApprovedTask.apporvalTabSelect();
    //     browser.sleep(2000)
    // }); 


    it('Workflow logout', () => {
        Common.logoutWorkflow();
    });

    /* 
        it('Developer credentials', () => {
            LoginPage.loginAs(Credential.Developer)
            browser.sleep(2000);
            DeveloperLogin.switchToDeveloper();
            browser.sleep(3000);
        });
    
        update_impl_list.map(function (ready_Obj, i) {
            var currentIteration = i;
            it('Edit implementation details ' + i, () => {
                // console.log(findid.capture.planidcapture + "_00" + (currentIteration + 1))
                UpdateImplementation.implementationEditButton(findid.capture.planidcapture, findid.capture.planidcapture + "_00" + (currentIteration + 1));
            })
    
            it('Integration testing completed' + i, () => {
                StatusAndGit.integarationStatusChange();
                console.log(findid.capture.planidcapture + "_00" + (currentIteration + 1))
            });
    
            it('ReadyForQA' + i, () => {
                StatusAndGit.readyForQAStatusChange();
                browser.sleep(2000);
                StatusAndGit.readyProceedYesSelect();
                Common.alertMessage();
            });
    
            it('Develpoer inbox view', () => {
                UpdateImplementation.developerMytaskTabSelect()
                browser.sleep(4000)
    
            });
    
        })
    
        it('Workflow logout', () => {
            Common.logoutWorkflow();
        });
     */
});