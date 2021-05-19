import BasePage from '../Common/basePage';
import Common from '../Common/Common';
import updateplan from '../../data/updateplan';
import findid from '../../data/findid';
import leadinput from '../../data/leadinput';


class CompileAndBuild extends BasePage {
    constructor() {
        super();
        this.planListTab = element(by.css('[href="#/app/impPlan"]'));
        this.submitBtn = element(by.css('.submitButton '))
        this.popupValidate = element(by.css('.sweModal-content'));
    }

    submitButtonClick() {
        browser.wait(this.isVisible(this.submitBtn), this.timeout.max)
        this.singlebuttonclick(this.submitBtn)
        browser.waitForAngular();
    }



    checkSuccessBuild(data) {
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
            return build_status
        }
        return "IN-Progress"
    }

    listenBuildWebsocket(planid, sys_obj, cbk) {
        var sys_details = leadinput.createImplementationplan.targetsystemid
        const WebSocket = require('ws');
        require('ssl-root-cas').inject();
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        var self = this;
        const ws = new WebSocket('wss://vhlqaztdt001.tvlport.net:8443/WorkFlowAPI/buildStatus');
        ws.on('open', function open() {
            ws.send('something');
            console.log("Websocket Conneciton Establisted")
        });
        ws.on('message', function (e) {
            var response = JSON.parse(e);
            // console.log(response)
            response = response.message
            if (response.percentageCompleted === 100 && response.planId == planid && response.status === "S") {
                sys_obj[response.systemName] = "S"
                console.log("Build completed with PASS status " + response.systemName)

                if (self.checkSuccessBuild(sys_obj) != "IN-Progress") {
                    element.all(by.css('[id="buildTab_' + findid.capture.planidcapture + '"] .cross-line')).getText().then(function (text) {
                        // console.log(text)
                        if (text.indexOf("Partial Build") >= 0) {
                            console.log("Full build completed")
                        }
                        else {
                            console.log("Partial build completed")
                        }
                    })
                    cbk(true);
                }
                // cbk(true);

            } else if (response.percentageCompleted === 100 && response.planId == planid && response.status === "F") {
                console.log("Build completed with FAIL status for " + response.systemName)
                sys_obj[response.systemName] = "F"
                // console.log(JSON.stringify(sys_obj))
                findid.capture.build_status = false
                if (self.checkSuccessBuild(sys_obj) != "IN-Progress") {
                    console.log("Failed in prog came " + findid.capture.build_status)
                    findid.capture.build_status = false
                    return cbk(false);
                }
                // return cbk(0, false);
            }
            else {
                console.log("build in progress")
            }
        });
        ws.on('close', function close() {
            console.log('disconnected');
            cbk();
        });
    }

    checkSuccessOldr(data) {
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
        if (build_status && response_received_count == sys_obj.length) {
            return build_status
            console.log("All system oldr/tldr completed" + build_status)

        }
        else if (!build_status && response_received_count == sys_obj.length) {
            console.log("Oldr/Tldr generation completed with failed status " + build_status + "chck" + findid.capture.oldr_status)
            findid.capture.oldr_status = false
            return build_status
        }
        return "IN-Progress"

    }

    listenOldrWebsocket(planid, sys_obj, cbk) {
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
            // console.log(page)
            var pcheck = page.message.percentageCompleted
            var statuscheck = page.message.status
            /* if (statuscheck === "F") {
                throw {
                    "name": "error"
                }
            } */
            if (pcheck === 0 && statuscheck === "S" && page.message.planId == planid) {
                console.log("OLDR/TLDR completed with PASS status " + page.message.systemName)
                sys_obj[page.message.systemName] = "S"

                if (self.checkSuccessOldr(sys_obj) != "IN-Progress") {
                    cbk(true);
                }
                // cbk();

            } else if (pcheck === 0 && statuscheck === "F" && page.message.planId == planid) {
                console.log("OLDR/TLDR completed with FAIL status " + page.message.systemName)
                findid.capture.oldr_status = false
                sys_obj[page.message.systemName] = "F"
                if (self.checkSuccessOldr(sys_obj) != "IN-Progress") {
                    cbk(false);
                }
                // cbk();
            }
            else {
                console.log("OLDR/TLDR in progress")
            }
        });
        ws.on('close', function close() {
            console.log('disconnected');
            cbk();
        });
    }

    implementationPlanListTab() {
        this.tabSelection(this.planListTab);
        browser.waitForAngular();
    }

    editPlanButtonSelect(captruedid) {
        var editPlanButton = element(by.css('[href="#/app/updateImpPlan/' + captruedid + '"]'));
        browser.wait(this.isVisible(editPlanButton), this.timeout.xl)
        this.tabSelection(editPlanButton);
        browser.waitForAngular();
        browser.wait(this.isNotVisible(editPlanButton), this.timeout.xl)
    }

    planDetailViewSelect(captruedid) {
        var planDetailView = element(by.css('[aria-label="' + captruedid + '"]'));
        browser.wait(this.isVisible(planDetailView), this.timeout.xl)
        this.tabSelection(planDetailView)
        browser.waitForAngular();
    }

    planTabViewSelect(captruedid) {
        var planTabView = element(by.css('[data-target="#implSystemDataTab_' + captruedid + '"]'))
        this.tabSelection(planTabView)
    }

    buildViewSelect(captruedid) {
        var buildView = element(by.css('[data-target="#buildTab_' + captruedid + '"]'))
        browser.wait(this.isDisplayed(buildView), this.timeout.xl)
        this.tabSelection(buildView)
    }

    clickBuildButton(captruedid) {
        var clickBuild = element(by.css('[aria-label="CreateBuild_' + captruedid + '"]'))
        browser.wait(this.isClickable(clickBuild), this.timeout.xl)
        this.singlebuttonclick(clickBuild)
    }

    generateOLDRButton(captruedid) {
        var generateOLDR = element(by.css('[aria-label="GenerateOLDR_' + captruedid + '"]'))
        browser.wait(this.isVisible(generateOLDR), this.timeout.xl)
        findid.capture.build_status == true
        this.singlebuttonclick(generateOLDR)
        console.log("Load generate button clicked")

    }
}

export default new CompileAndBuild();