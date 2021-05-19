import BasePage from '../Common/basePage';

class LoadSetAccept extends BasePage {
    constructor() {
        super();
        this.loadSet = element(by.css('[href="#/app/lsReady"]'))
    }

    loadSetTabSelection() {
        this.tabSelection(this.loadSet)
    }

    detailViewSelection(capturedid) {
        var detail = element(by.css('[aria-label="' + capturedid + '"]'))
        browser.wait(this.isClickable(detail), this.timeout.xl)
        this.singleListSelect(detail)
    }

    loadSetProductionAccept(capturedid) {
        var prod = element(by.css('[aria-label="accept_' + capturedid + '"]'))
        browser.wait(this.isClickable(prod),this.timeout.xl)
        this.singlebuttonclick(prod)
    }

}

export default new LoadSetAccept();