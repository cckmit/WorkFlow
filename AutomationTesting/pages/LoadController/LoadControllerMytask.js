import BasePage from '../Common/basePage';
import qadeploymentinput from '../../data/qadeploymentinput';

class LoadControllerMytask extends BasePage {
    constructor() {
        super();
        this.mytask = element(by.css('[href="#/app/mytasks"]'));
        this.updateBtn = element(by.css('.planSaveBtn'))
        this.prompt = element(by.css('.sweModal-content'));
    }

    updatePlanButton() {
        this.singlebuttonclick(this.updateBtn)
        browser.waitForAngular();
    }

    proceedYes() {
        var yes1 = element(by.css('[ng-click="dialog.hide()"]'))
        browser.wait(this.isVisible(yes1), this.timeout.xl)
        this.singlebuttonclick(yes1)
        browser.waitForAngular();
    }

    loadMytask() {
        this.tabSelection(this.mytask)
        browser.waitForAngular();
    }

    detailViewSelect(capturedid) {
        var view = element(by.css('[aria-label="' + capturedid + '"]'))
        browser.wait(this.isVisible(view), this.timeout.xl)
        // browser.waitForAngular();
        this.tabSelection(view)
        browser.waitForAngular();
    }

    editPlanSelect(capturedid) {
        var edit = element(by.css('[href="#/app/updateImpPlan/' + capturedid + '"]'))
        browser.wait(this.isVisible(edit), this.timeout.xl)
        this.tabSelection(edit)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(edit), this.timeout.xl)
    }

    readyForProductionSelect(capturedid) {
        var ready = element(by.css('[aria-label="Ready_For_Production_Deployment_' + capturedid + '"]'))
        browser.wait(this.isVisible(ready), this.timeout.xl)
        this.singlebuttonclick(ready)
        browser.waitForAngular();
    }

    rejectProductionSelect() {
        var reject = element(by.css('[aria-label="Reject_' + capturedid + '"]'))
        browser.wait(this.isVisible(reject), this.timeout.xl)
        this.singlebuttonclick(reject)
        browser.waitForAngular();
    }
/* 
    checkSuccessLC(data) {
        // console.log("Validation "+JSON.stringify(data)) 
        var sys_obj = leadinput.createImplementationplan.targetsystemid
        // console.log(sys_obj)
        var build_status = true
        var response_received_count = 0
        for (var key in data) {
            response_received_count++
            if (data[key] !== "S") {
                build_status = false
            }
        }
        // console.log("Response Length "+response_received_count)
        // console.log("System Length "+sys_obj.length)
        if (response_received_count == sys_obj.length) {
            // console.log(build_status)
            return build_status
        }
        return "IN-Progress"
    }

    listenLoadControllerWebsocket(planid, syskeys, cbk) {
        const WebSocket = require('ws');
        require('ssl-root-cas').inject();
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var self = this;
        const ws = new WebSocket('wss://vhlqaztdt001.tvlport.net:8443/WorkFlowAPI/buildStatus');
        ws.on('open', function open() {
            ws.send('something');
            console.log("Websocket Conneciton Establisted")
        });
        ws.on('message', function incoming(e) {
            var page = JSON.parse(e);
            var pcheck = page.message.percentageCompleted
            var statuscheck = page.message.status
            console.log("PROD_FTP_IP is in progress")
            if (page.channel === "PROD_FTP_IP") {
                if ( statuscheck === "S" && page.message.planId == planid) {
                    console.log("Dev-Manager approval completed with STAGE_BUILD ")
                    syskeys[page.message.systemName] = "S"
                    if (self.checkSuccessLC(syskeys) != "IN-Progress") {
                        cbk();
                    }
                    cbk();
                } else if (pcheck === 0 && statuscheck === "F" && page.message.planId == planid) {
                    console.log("Dev-Manager approval STAGE_BUILD Failed")
                    syskeys[page.message.systemName] = "F"
                    if (self.checkSuccessLC(syskeys) != "IN-Progress") {
                        throw {
                            name: "DevManager Error",
                            message: "Unable to proceed without plan approved"
                        }
                        cbk();
                    }
                    cbk()
                }

            }
        });
        ws.on('close', function close() {
            console.log('disconnected');
            cbk();
        });
    }
 */
}

export default new LoadControllerMytask();