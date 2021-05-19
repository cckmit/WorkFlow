import BasePage from '../Common/basePage';
import leadinput from '../../data/leadinput';
import findid from '../../data/findid';
import managerinput from '../../data/managerinput';

class DevManager extends BasePage {
    constructor() {
        super();
        this.managerComments = element(by.model('vm.comment.commentText'));
        this.commentOkBtn = element(by.css('[aria-label="approve_comment_"]'))
    }

    checkSuccessApprove(data) {
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

    listenManagerWebsocket(planid, syskeys, cbk) {
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
            console.log("STAGE_LOAD is in progress")
            if (page.channel === "STAGE_LOAD") {
                if (pcheck === 0 && statuscheck === "S" && page.message.planId == planid) {
                    console.log("Dev-Manager approval completed with STAGE_BUILD ")
                    syskeys[page.message.systemName] = "S"
                    if (self.checkSuccessApprove(syskeys) != "IN-Progress") {
                        cbk();
                    }
                    cbk();
                } else if (pcheck === 0 && statuscheck === "F" && page.message.planId == planid) {
                    console.log("Dev-Manager approval STAGE_BUILD Failed")
                    syskeys[page.message.systemName] = "F"
                    if (self.checkSuccessApprove(syskeys) != "IN-Progress") {
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

    detailViewSelect(capturedid) {
        var detailView = element(by.css('[aria-label="' + capturedid + '"]'));
        this.tabSelection(detailView)
        browser.waitForAngular();
    }

    implementationTabSelect(capturedid) {
        var implementationTab = element(by.css('[data-target="#implDataTab_' + capturedid + '"]'));
        this.tabSelection(implementationTab)
    }

    approvalTabSelect(capturedid) {
        var approvalTab = element(by.css('[data-target="#approvalTab_' + capturedid + '"]'));
        this.tabSelection(approvalTab)
    }

    activityLogTabSelect(capturedid) {
        var activityLogTab = element(by.css('[data-target="#activity_' + capturedid + '"]'));
        this.tabSelection(activityLogTab)
    }

    planTabSelect(capturedid) {
        var planTab = element(by.css('[data-target="#implSystemDataTab_' + capturedid + '"]'));
        this.tabSelection(planTab)
    }

    approvalActionSelect(capturedid) {
        var approvalAction = element(by.css('[aria-label="manager_action_' + capturedid + '"]'));
        this.singlebuttonclick(approvalAction)
        var actionApprove = element(by.css('[aria-label="Approve_' + capturedid + '"]'));
        browser.wait(this.isVisible(actionApprove), this.timeout.l);
    }

    actionApproveSelect(capturedid) {
        var actionApprove = element(by.css('[aria-label="Approve_' + capturedid + '"]'));
        this.singlebuttonclick(actionApprove)
        browser.wait(this.isVisible(this.managerComments), this.timeout.l);
    }

    actionRejectSelect(capturedid) {
        var actionReject = element(by.css('[aria-label="Reject_' + capturedid + '"]'));
        this.singlebuttonclick(actionReject)
        var actionApprove = element(by.css('[aria-label="Approve_' + capturedid + '"]'));
        browser.wait(this.isVisible(actionApprove), this.timeout.l);
    }
    managerCommentsSelect() {
        this.enterText(this.managerComments, managerinput.managerdetails.managercomments)
    }

    commentOkButton() {
        browser.wait(this.isClickable(this.commentOkBtn), this.timeout.l);
        this.singlebuttonclick(this.commentOkBtn)
    }
}

export default new DevManager();