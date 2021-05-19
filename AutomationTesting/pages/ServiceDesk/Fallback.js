import BasePage from '../Common/basePage';

class LoadSetAccept extends BasePage {
    constructor() {
        super();
        this.loadSet = element(by.css('[href="#/app/fallback"]'))
    }

    fallbackTabSelect() {
        this.tabSelection(this.loadSet)
        browser.waitForAngular();
    }

    loadAndActivate(capturedid) {
        var expand = element(by.css('[aria-label="expand_' + capturedid + '"]'))
        this.singleListSelect(expand)
        var activate = element(by.css('[aria-label="loadactivate_' + capturedid + '"]'))
        browser.wait(this.isClickable(activate), this.timeout.xl)        
        this.singleListSelect(activate)
        browser.waitForAngular();
    }

    detailViewSelection(capturedid) {
        var detail = element(by.css('[aria-label="' + capturedid + '"]'))
        browser.wait(this.isClickable(detail), this.timeout.xl)
        this.singleListSelect(detail)
        browser.waitForAngular();
    }

    listenFTPWebsocket(cbk) {
        const WebSocket = require('ws');
        require('ssl-root-cas').inject();
        process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";
        const ws = new WebSocket('wss://vhlqaztdt001.tvlport.net:8443/WorkFlowAPI/buildStatus');
        ws.on('open', function open() {
            console.log("Conneciton Establisted")
        });
        ws.on('message', function incoming(e) {
            var fallbackresp = JSON.parse(e);
            console.log(fallbackresp)
            var returnVal = fallbackresp.message.data.message
        });
       /*  ws.on('close', function close() {
            console.log('disconnected');
            cbk();
        }); */
    }

}

export default new LoadSetAccept();