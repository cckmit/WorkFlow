import Common from '../pages/Common/Common';
import findid from '../data/findid';
import LoginPage from '../pages/Common/LoginPage';
import Credential from '../data/Credential';
import ServiceDeskLogin from '../pages/ServiceDesk/ServiceDeskLogin';
import ServiceDeskMytask from '../pages/ServiceDesk/ServiceDeskMytask';
import LoadSetAccept from '../pages/ServiceDesk/LoadSetAccept';
import ManageCpu from '../pages/ServiceDesk/ManageCpu';
import Fallback from '../pages/ServiceDesk/Fallback';
import servicedeskinput from '../data/servicedeskinput';

describe('ServiceDesk Login Actions', () => {
    beforeEach(() => {
        if (!LoginPage.login_success) {
            LoginPage.goto();
        }
    });

    it('credentials', () => {
        browser.sleep(3000);
        LoginPage.loginAs(Credential.ServiceDesk)
        ServiceDeskLogin.switchToServiceDesk();
        browser.sleep(3000);
    });


    it('tab selection', () => {
        ServiceDeskMytask.tosTabSelectButton()
    });

    var tos_system_list = servicedeskinput.tosdata

    tos_system_list.map(function name(tos_obj) {


        it('ServiceDesk Mytask Selection', () => {
            ServiceDeskMytask.detailViewSelection(findid.capture.planidcapture)
        });

          it('Load', (cbk) => {
              ServiceDeskMytask.listenBuildWebsocket(findid.capture.planidcapture, cbk)
              ServiceDeskMytask.loadTOSSelection(findid.capture.planidcapture)
              ServiceDeskMytask.applyButton(findid.capture.planidcapture)
          });

        it('Activation', (done) => {

            ServiceDeskMytask.activationTOSSelection(findid.capture.planidcapture, tos_obj, function () {
                console.log("Response received init")
                if (findid.capture.multiple_cpu_indicator == true) {
                    ServiceDeskMytask.cpuSaveButton();
                }
                ServiceDeskMytask.applyButton(findid.capture.planidcapture)
                ServiceDeskMytask.listenBuildWebsocket(findid.capture.planidcapture, function (cbk) {
                    console.log("Response received")
                    done()
                })
            });
        });

        it('Deactivation', (cbk) => {
            ServiceDeskMytask.listenBuildWebsocket(cbk)
            ServiceDeskMytask.deactivateTOSSelection(findid.capture.planidcapture);
            ServiceDeskMytask.applyButton(findid.capture.planidcapture)
        });

        //Enable it only if you want to fallback directly
        // it('Delete', (cbk) => {
        //     ServiceDeskMytask.listenBuildWebsocket(cbk)
        //     ServiceDeskMytask.deleteTOSSelection(findid.capture.planidcapture);
        //     ServiceDeskMytask.applyButton(findid.capture.planidcapture)
        // });


    })



    /*******************LOADSET ACCEPT********************/
    /* 
        it('Loadset tabselection', () => {
            LoadSetAccept.loadSetTabSelection()
        });
    
        it('Accept Loadset', (cbk) => {
            LoadSetAccept.detailViewSelection(findid.capture.planidcapture);
            ServiceDeskMytask.listenBuildWebsocket(cbk)
            LoadSetAccept.loadSetProductionAccept(findid.capture.planidcapture);
        });
         */
    /*******************END LOADSET ACCEPT********************/

    /*************************FALLBACK LOADSET****************************/
    /*  it('Fallback Tabselection', () => {
         Fallback.fallbackTabSelect();
     });
     it('should behave...', () => {
         Fallback.detailViewSelection(findid.capture.planidcapture);
     });
 
     it('Load/Activate Loadsets', (cbk) => {
         Fallback.listenFTPWebsocket(cbk);
         Fallback.loadAndActivate(findid.capture.planidcapture);
     });
  */
    /*************************END FALLBACK LOADSET****************************/
    /*  it('Manage CPU', () => {
             browser.sleep(3000)
             ManageCpu.manageCputabSelection();
             browser.sleep(3000)
     
         }); 
     
         it('should behave...', () => {
             ManageCpu.tableSelection();
             browser.sleep(300000)
         });
         */
});