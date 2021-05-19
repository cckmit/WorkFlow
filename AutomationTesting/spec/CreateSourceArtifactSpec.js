import DeveloperLogin from '../pages/Developer/DeveloperLogin';
import Credential from '../data/Credential';
import LoginPage from '../pages/Common/LoginPage';
import UpdateImplementation from '../pages/Common/UpdateImplementation';
import Common from '../pages/Common/Common';
import StatusAndGit from '../pages/Developer/StatusAndGit';
import findid from '../data/findid';
import CreateNewArtifact from '../pages/Developer/CreateNewArtifact';
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

    update_impl_list.map(function(impl_Obj, i) {

        it('Edit implementation details ' + i, () => {
            var currentIteration = i;
            browser.debugger();
            // console.log(findid.capture.planidcapture + "_00" + (currentIteration + 1))
            UpdateImplementation.implementationEditButton(findid.capture.planidcapture, findid.capture.planidcapture + "_00" + (currentIteration + 1));
        })

        var create_artif_list = updateImplementationinput.createartifact.artifacts
        create_artif_list.map(function(artif_obj) {
            it('create', () => {
                CreateNewArtifact.createNewSourceArtifact();
                browser.sleep(2000)
            });
            // CreateNewArtifact.createIBMCreate(artif_obj.functionalarea);
            it('create ibm ', () => {
                // CreateNewArtifact.createIBMCreate(artif_obj.functionalarea);
                CreateNewArtifact.createIBMCreate();
                CreateNewArtifact.fileNameEnter(artif_obj.filename);
                CreateNewArtifact.filetypeSelect(artif_obj.filetype);
                CreateNewArtifact.targetSystemSelection(artif_obj.targetsystem, function() {
                    browser.sleep(1000)
                    CreateNewArtifact.putlevelSelection()
                })
                CreateNewArtifact.sourceNameEnter(artif_obj.sourcepath)
                CreateNewArtifact.createSourceButton();
                CreateNewArtifact.artifactReport();
            });

        })


    })

    /*   it('Workflow logout', () => {
          Common.logoutWorkflow();
      });
   */
});