import findid from '../data/findid';
import leadinput from '../data/leadinput';
import createimplementationinput from '../data/createimplementationinput';
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import LeadLogin from '../pages/Lead/LeadLogin';
import CreateImplementationPlan from '../pages/Lead/CreateImplementationPlan';
import Common from '../pages/Common/Common';
import UpdateImplementationPlan from '../pages/Lead/UpdateImplementationPlan';
import updateplan from '../data/updateplan';
import CreateImplementation from '../pages/Lead/CreateImplementation';
import BasePage from '../pages/Common/basePage';
import UpdateImplementation from '../pages/Common/UpdateImplementation';

describe('Lead Login Actions', () => {
    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('Do Login', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.Lead)
        LeadLogin.switchToLead()
        browser.sleep(3000);
    });

    it('In new implementation form', () => {
        CreateImplementationPlan.switchToNewImplementationTab();
    });

    it('CompanyName', () => {
        browser.sleep(3000);

        CreateImplementationPlan.companyNameSelect();
    });

    it('TargetSystem', () => {
        var lead_input = leadinput.createImplementationplan.targetsystemid
        for (var i = 0; i < lead_input.length; i++) {
            CreateImplementationPlan.targetSystemSelect(lead_input[i].id);
        }
        browser.sleep(3000);
    });

    it('Project Details', () => {
        browser.sleep(3000);
        CreateImplementationPlan.csrSelect(leadinput.createImplementationplan.csrid);
        CreateImplementationPlan.planDescription(leadinput.createImplementationplan.plandescription);
        CreateImplementationPlan.leadComments(leadinput.createImplementationplan.comments);
        CreateImplementationPlan.approvalManagerSelect(leadinput.createImplementationplan.approvalmanager);
    });

    it('system details', () => {
        var lead_input = leadinput.createImplementationplan.targetsystemid
        // lead_input.map(function (lead_obj, i) {
        //     CreateImplementationPlan.loadWindowSelect(lead_obj.loadwindow, i);
        //     browser.sleep(3000)

        //     var self = this
        //     var keyMapping = {}
        //     $$('[name="loadCategories"] option').each(function (el, index) {
        //         el.getText().then(function (txt) {
        //             // console.log(txt)
        //             browser.params.tryit = txt;
        //             // console.log("keyMapping[browser.params.tryit] " + browser.params.tryit)
        //             keyMapping[browser.params.tryit] = index
        //             console.log("keyMapping :" + JSON.stringify(keyMapping))


        //             // var temp = Object.keys().indexOf(keytoFind);
        //             // var storedvalue = browser.params.tryit
        //             // console.log("valuedee  " + storedvalue)
        //             // var keytoFind = "F";
        //             // // console.log( getObjectKeyIndex(browser.params.tryit, 'F') );
        //             // console.log("temp  " + temp)
        //             // console.log("   tryit   " + browser.params.tryit + "--index---" + index)
        //             if (txt.match(new RegExp(loadcategory, 'gi')) != null) {
        //                 // console.log("Target System " + targetloadcategoryobj + " in index" + index)
        //                 var cat = element.all(by.css('[name="loadCategories"]')).all(by.tagName('option')).get(index)
        //                 self.tabSelection(cat)
        //             }
        //         })
        //     })
        //     if (keyMapping["F"] == true) {
        //         console.log("keyMapping[]  :" + keyMapping["F"])
        //     }
        //     else {
        //         console.log("hhi")
        //     }
        //     v = { "C": 1, "B": 2 }
        //     v["C"]
        //     $$(by.css('[name="loadCategories"] option')).map(function (el, index) {
        //         el.getText().then(function (txt) {
        //             console.log("   txt   " + txt)
        //             tryit = txt;
        //             console.log("   tryit   " + tryit)
        //         })
        //     })
        //     CreateImplementationPlan.loadCategorySelect(lead_obj.loadcategory, lead_obj.id, i);
        //     browser.sleep(3000)
        //     CreateImplementationPlan.selectByCategoryCalander(i,function () {
        //         done()
        //     });
        //     CreateImplementationPlan.selectByCategoryCalander(i)
        //     CreateImplementationPlan.loadTimeSelect(lead_obj.loadtime, i);
        //     CreateImplementationPlan.loadAttendeeSelect(lead_obj.loadattendee, i);
        //     CreateImplementationPlan.preLoadSelect(lead_obj.preload, i);
        // })
        for (var i = 0; i < lead_input.length; i++) {
            CreateImplementationPlan.loadWindowSelect(lead_input[i].loadwindow, i);
            browser.sleep(3000)
            CreateImplementationPlan.loadCategorySelect(lead_input[i].loadcategory, lead_input[i].id, i);
            browser.sleep(3000)
            // CreateImplementationPlan.selectByCategoryCalander(i,function () {
            //     done()
            // });
            CreateImplementationPlan.selectByCategoryCalander(i)
            CreateImplementationPlan.loadTimeSelect(lead_input[i].loadtime, i);
            CreateImplementationPlan.loadAttendeeSelect(lead_input[i].loadattendee, i);
            CreateImplementationPlan.preLoadSelect(lead_input[i].preload, i);
            // CreateImplementationPlan.dbcrValidation(lead_input[i].dbcrdetails, i);
        }
    });

    it('other details', () => {
        CreateImplementationPlan.leadContactSelect();
    });

    it('Save button', () => {
        CreateImplementationPlan.saveButtonSelect();
        CreateImplementationPlan.getPlanId(findid.capture.planidcapture, function (res) {
        });
    });


    // it('Plan details view', () => {
    //     for (var i = 0; i < lead_input.length; i++) {
    //         UpdateImplementationPlan.planDetailViewSelect(findid.capture.planidcapture);
    //     }
    // });

    var create_impl_list = createimplementationinput.Devinput.multipleimp
    // console.log(create_impl_list.length)
    for (var j = 0; j < create_impl_list.length; j++) {
        (function (implObj) {
            it('Implementation Details ' + (j + 1), () => {
                CreateImplementation.implementationSelect(findid.capture.planidcapture);
                CreateImplementation.developerDescriptionSelect(implObj.developerdescription);
                CreateImplementation.developerContactSelect(implObj.developercontact);
                // CreateImplementation.implementationDeveloperSelect(create_impl_list[i].developername)
                CreateImplementation.implementationDeveloperSelect(implObj.developername)// need to address
            });

            it('Select reviwer and location', () => {
                CreateImplementation.peerReviewerSelect(implObj.peerreviwer);
                CreateImplementation.developerLocationSelect(implObj.developerlocation);
            });

            it('Final stage of implementation creation', () => {
                CreateImplementation.createImplementationButton();
                Common.alertMessage();
            });
        })(create_impl_list[j])
    }

    it('Logout Lead Login', () => {
        browser.sleep(3000)
        Common.logoutWorkflow();
    });


    /* 
    
        it('Developer reassign', () => {
            UpdateImplementationPlan.planDetailViewSelect(findid.capture.planidcapture);
            UpdateImplementationPlan.implementationViewSelect(findid.capture.planidcapture);
            UpdateImplementation.implementationEditButton(findid.capture.planidcapture + "_00" + createimplementationinput.leadreassign.implementationid)
        }); */
});
