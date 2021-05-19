import BasePage from '../Common/basePage';
import Common from '../Common/Common';
import findid from '../../data/findid';
import servicedeskinput from '../../data/servicedeskinput';


class ServiceDeskMytask extends BasePage {
    constructor() {
        super();
        this.serviceInbox = element(by.css('[href="#/app/mytasks"]'));
        this.tosLoad = element(by.model('itObj.loadandactivate'));
        this.cpuID = element(by.model('selectedCPU'))
        this.cpuSaveBtn = element(by.css('[ng-click="save()"]'))
    }

    cpuSaveButton() {
        this.singlebuttonclick(this.cpuSaveBtn)
    }
    cpuID() {
        this.singleListSelect(this.cpuID)
    }
    tosTabSelectButton() {
        browser.sleep(2000)
        /*  console.log('data-target="#' + servicedeskinput.tosdata.targetsystem + '"')
         this.tabSelection(this.tosTabSelection) */
        var tosTabSelection = element(by.css('[data-target="#WSP"]'))
        this.tabSelection(tosTabSelection)
        browser.waitForAngular();
    }

    detailViewSelection(capturedid) {
        var detail = element(by.css('[aria-label="' + capturedid + '"]'))
        browser.wait(this.isClickable(detail), this.timeout.xl)
        this.singleListSelect(detail)
        browser.waitForAngular();
    }

    applyButton(capturedid) {
        // var apply = element(by.css('[aria-label="' + servicedeskinput.tosdata.targetsystem + '_ApplyButton_' + capturedid + '_C"]'))
        var apply = element(by.css('button[aria-label*="_ApplyButton_' + capturedid + '"]'))
        browser.wait(this.isClickable(apply), this.timeout.xl)
        this.singlebuttonclick(apply)
        browser.waitForAngular();
    }

    refreshTOSSelection(capturedid) {
        // var refresh = element(by.css('[aria-label="' + servicedeskinput.tosdata.targetsystem + '_RefreshButton_' + capturedid + '_C"]'))
        var refresh = element(by.css('button[aria-label*="' + servicedeskinput.tosdata.targetsystem + '_RefreshButton_' + capturedid + '"]'))
        browser.wait(this.isClickable(refresh), this.timeout.xl)
        this.singlebuttonclick(refresh)
        browser.waitForAngular();
    }

    deleteTOSSelection(capturedid) {
        // var deletetos = element(by.css('[aria-label="' + servicedeskinput.tosdata.targetsystem + '_DeactivateandDeleteButton_' + capturedid + '_C"]'))
        var deletetos = element(by.css('md-checkbox[aria-label*="' + servicedeskinput.tosdata.targetsystem + '_DeactivateandDeleteButton_' + capturedid + '"]'))
        browser.wait(this.isClickable(deletetos), this.timeout.xl)
        browser.waitForAngular();
        this.singlebuttonclick(deletetos)
    }

    loadTOSSelection(capturedid) {
        // var load = element(by.css('[aria-label="' + servicedeskinput.tosdata.targetsystem + '_LoadButton_' + capturedid + '_C"]'))
        var load = element(by.css('md-checkbox[aria-label*="_LoadButton_' + capturedid + '"]'))
        browser.wait(this.isClickable(load), this.timeout.xl)
        this.singlebuttonclick(load)
        browser.waitForAngular();
    }

    activationTOSSelection(capturedid, cpuid,cbk) {
        var activate = element(by.css('md-checkbox[aria-label*="_LoadandActivateButton_' + capturedid + '"]'))
        // var activate = element(by.css('[aria-label="' + servicedeskinput.tosdata.targetsystem + '_LoadandActivateButton_' + capturedid + '_C"]'))
        browser.wait(this.isClickable(activate), this.timeout.xl)
        this.singlebuttonclick(activate)
        browser.waitForAngular();
        if (servicedeskinput.tosdata.APO.cpuid == "C" && servicedeskinput.tosdata.APO.action == "ACTIVATE" || servicedeskinput.tosdata.APO.cpuid == "F" && servicedeskinput.tosdata.APO.action == "ACTIVATE") {
            findid.capture.multiple_cpu_indicator = true
            $$('[ng-model="selectedCPU"] option').each(function (ele, index) {
                ele.getText().then(function (txt) {
                    console.log(txt)
                    if (txt.trim() == cpuid) {
                        $$('[ng-model="selectedCPU"] option').get(index).click();
                        // cbk(true)
                        cbk()
                    }
                })
            });
        } else {
            console.log("Plan does not have multiple CPU,,,")
            findid.capture.multiple_cpu_indicator = false
            cbk()
        }
        browser.waitForAngular();
    }

    deactivateTOSSelection(capturedid) {
        var deactivate = element(by.css('md-checkbox[aria-label="WSP_DeactivateButton_T1800030_D"]'))
        // var deactivate = element(by.css('[aria-label="' + servicedeskinput.tosdata.targetsystem + '_DeactivateButton_' + capturedid + '_C"]'))
        browser.wait(this.isClickable(deactivate), this.timeout.xl)
        this.singlebuttonclick(deactivate)
        browser.waitForAngular();
    }
    /* 
        deactivateTOSSelection() {
            var deactivateTOS = element(by.css('.' + findid.capture.planidcapture + ' md-checkbox'))
            element(by.css('.' + findid.capture.planidcapture + ' md-checkbox')).isPresent().then(function (isPresent) {
                if (isPresent == true) {
                    this.singlebuttonclick(deactivateTOS)
                    console.log("TOS deactivation is triggered please wait")
                } else {
                    console.log("TOS last activation failed")
                }
            })
        } */

    /* listenBuildWebsocket(cbk) {
        const WebSocket = require('ws');
        require('ssl-root-cas').inject();
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        const ws = new WebSocket('wss://vhlqaztdt001.tvlport.net:8443/WorkFlowAPI/buildStatus');
        ws.on('open', function open() {
            console.log("Conneciton Establisted")
        });
        ws.on('message', function incoming(e) {
            var page = JSON.parse(e);
            var startaction = page.message.data.message
            var loadset = page.message.data.loadset
            console.log(loadset[4, 7])
            var returnval = page.message.data.returnValue
            var status = page.message.status
            if (returnval === 4) {
                console.log("STATUS=AUTOLOAD STARTING")
                cbk()
            }
            else {
                console.log("Tos in progress please wait")
                cbk()
            }

            if (returnval === 6 && status === true) {
                console.log("TOS loaded successfully")
            } else if (returnval === 6 && status === false) {
                console.log("STATUS=LOAD_NOT_FOUND LAST_STATE=ZFILE LS SENT")

            }
        });
        ws.on('close', function close() {
            console.log('disconnected');
            cbk();
        });
    } */

    listenBuildWebsocket(planid, cbk) {
        const WebSocket = require('ws');
        require('ssl-root-cas').inject();
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        const ws = new WebSocket('wss://vhlqaztdt001.tvlport.net:8443/WorkFlowAPI/buildStatus');
        console.log(ws)
        ws.on('open', function open() {
            console.log("Conneciton Establisted")
        });
        ws.on('message', function (e) {
            var response = JSON.parse(e);
            var channelcheck = response.channel
            response = response.message
            console.log("channel", channelcheck)
            var finalLoadSet = (response.data.loadset).slice(2)
            var loadsetConversion = finalLoadSet.toString();
            var matchid = planid.slice(2)
            var returnval = response.data.returnValue
            console.log(loadsetConversion == matchid)
            console.log("loadsetConversion :" + loadsetConversion, "  14:40  +", matchid)

            if (channelcheck == "PROD_LOAD" && loadsetConversion === matchid) {
                if (returnval === 4) {
                    console.log("STATUS=AUTOLOAD STARTING")
                    console.log("RC=4 SUCCESS :" + response.data.message)
                }
                else {
                    console.log("TOS ACTION IS IN PROGRESS PLEASE WAIT")
                }

                if (returnval === 0 && response.status === true) {
                    console.log("TOS ACTION SUCCESSFULLY DONE")
                    console.log("RC=0 SUCCESS :" + response.data.message)
                    cbk();
                } else if (returnval === 0 && response.status === false) {
                    // console.log("STATUS=LOAD_NOT_FOUND LAST_STATE=ZFILE LS SENT")
                    console.log("LAST ACTION RC=0 FAILED :" + response.data.message)
                    cbk();
                }

                if (returnval === 6 && response.status === true) {
                    console.log("TOS ACTION FAILED")
                    console.log("RC=6 FAILED :" + response.data.message)
                    cbk()
                } else if (returnval === 6 && response.status === false) {
                    // console.log("STATUS=LOAD_NOT_FOUND LAST_STATE=ZFILE LS SENT")
                    console.log("LAST ACTION RC=6 FAILED :" + response.data.message)
                    cbk()
                }
            }

        });
        ws.on('close', function close() {
            console.log('disconnected');
            cbk();
        });
    }
    tosLoadSelect() {
        this.singlebuttonclick(this.tosLoad)
    }

}

export default new ServiceDeskMytask();