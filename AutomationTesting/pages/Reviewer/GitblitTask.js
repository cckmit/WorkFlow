browser.ignoreSynchronization = true;
import BasePage from '../Common/basePage'
import reviewerinput from '../../data/reviewerinput'
class GitblitTask extends BasePage {
    constructor() {
        super();
        this.searchfield = $('.repository');
        var res = reviewerinput.reviewdetail.planid.toLowerCase();
        this.url = 'http://vhlqaztdt001.tvlport.net:8080/gitblit/tickets/tpf!tp!source!' + res + '.git/'
        this.pageLoaded = this.isVisible(this.searchfield);
    }


}

export default new GitblitTask();