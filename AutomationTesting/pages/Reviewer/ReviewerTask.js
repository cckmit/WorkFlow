import BasePage from '../Common/basePage';
import reviewerinput from '../../data/reviewerinput';
import findid from '../../data/findid';

class ReviewerTask extends BasePage {
    constructor() {
        super();
        this.messageCheck = element(by.css('.okBtn'));
        var res = findid.capture.planidcapture.toLowerCase();
        this.ticketUrl = element(by.css('[http://vhlqaztdt001.tvlport.net:8080/gitblit/tickets/tpf!tp!source!' + res + '.git/1]'));
    }

    detailsViewSelect(implid) {
        browser.sleep(4000)
        console.log(implid)
        var detailsView = element(by.css('[aria-label="' + implid + '"]'))
        browser.wait(this.isVisible(detailsView), this.timeout.xl)
        this.singlebuttonclick(detailsView)
        browser.waitForAngular();
    }

    ticketUrlSelect() {
        this.tabSelection(this.ticketUrl);
    }

    selectAllForReview(implid) {
        var all = element(by.css('[aria-label="selectall_' + implid + '"]'))
        browser.wait(this.isClickable(all), this.timeout.xl)
        this.singlebuttonclick(all)
        // element(by.css('.list-group')).all(by.tagName('md-checkbox')).get(0).click();
        // cbk()
    }

    reviewCompleted(implid) {
        var reviewer = element(by.css('[aria-label="reviewcompleted_' + implid + '"]'))
        browser.wait(this.isVisible(reviewer), this.timeout.xxl)
        this.singlebuttonclick(reviewer)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(reviewer), this.timeout.xxl)
    }

    markCompleteButton(implid) {
        var mark = element(by.css('[aria-label="markascomplete_' + implid + '"]'))
        browser.wait(this.isVisible(mark), this.timeout.xl)
        this.singlebuttonclick(mark)
        browser.waitForAngular();
        browser.wait(this.isNotVisible(mark), this.timeout.xl)
    }

}

export default new ReviewerTask();