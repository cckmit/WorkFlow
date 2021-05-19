import BasePage from '../Common/basePage';
import insertfreezeinput from '../../data/insertfreezeinput';

class CreateFreezeDate extends BasePage {
    constructor() {
        super();
        this.freezeTab = element(by.css('[href="#/app/freezeDate"]'));
        this.insertFreeze = element(by.css('.insertButton'));
        this.systemId = element(by.model('vmid.form.loadCategoryId.systemId.id'));
        this.loadCategory = element(by.model('vmid.form.loadCategoryId.id'));
        this.reason = element(by.model('vmid.form.reason'));
        this.startDate = element(by.model('vmid.form.fDate'));
        this.startTime = element(by.model('vmid.form.fTime'));
        this.endDate = element(by.model('vmid.form.tDate'));
        this.endTime = element(by.model('vmid.form.tTime'));
        this.createFreeze = element(by.css('[aria-label="insertFreezeDateWindow"]'))
    }

    createFreezeButton() {
        this.singlebuttonclick(this.createFreeze)
    }

    freezeTabSelect() {
        this.tabSelection(this.freezeTab);
    }

    insertFreezeSelect() {
        this.singlebuttonclick(this.insertFreeze);
    }

    systemIdSelect() {
        var sysid = this.systemId.$('[value="' + insertfreezeinput.insertfreeze.targetsystemid + '"]');
        this.singleListSelect(sysid)
    }

    loadCategorySelect() {
        var loadcat = this.loadCategory.$('[value="' + insertfreezeinput.insertfreeze.loadcategory + '"]');
        this.singleListSelect(loadcat)
    }

    reasonEnter() {
        this.enterText(this.reason, insertfreezeinput.insertfreeze.freezereason)
    }

    startDateEnter() {
        this.enterText(this.startDate, insertfreezeinput.insertfreeze.startdate)
    }

    startTimeEnter() {
        this.enterText(this.startTime, insertfreezeinput.insertfreeze.starttime)
    }

    endDateEnter() {
        this.enterText(this.endDate, insertfreezeinput.insertfreeze.enddate)
    }

    endTimeEnter() {
        this.enterText(this.endTime, insertfreezeinput.insertfreeze.endtime)
    }
}

export default new CreateFreezeDate();