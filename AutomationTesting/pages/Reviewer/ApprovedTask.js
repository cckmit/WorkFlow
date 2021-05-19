import BasePage from '../Common/basePage';
import reviewerinput from '../../data/reviewerinput';
import findid from '../../data/findid';

class ApprovedTask extends BasePage {
    constructor() {
        super();
        this.approvalTab = element(by.css('[href="#/app/approvedTasks"]'));
    }

    apporvalTabSelect() {
        browser.sleep(2000)
        this.tabSelection(this.approvalTab)
        browser.sleep(2000)
    }

    detailViewSelect(capturedid) {
        var detailView = element(by.css('[aria-label="' + capturedid + "_001" + '"]'));
        browser.wait(this.inDom(detailView), this.timeout.xl)
        this.singlebuttonclick(this.detailView)
    }

    ticketUrlSelect(capturedid) {
        this.ticketUrl = element(by.css('[href="http://vhlqaztdt001.tvlport.net:8080/gitblit/tickets/tpf!tp!source!' + capturedid + '.git/"]'));
        this.tabSelection(this.ticketUrl);
    }

}

export default new ApprovedTask();