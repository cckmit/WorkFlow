import BasePage from '../Common/basePage'
import insertfreezeinput from '../../data/insertfreezeinput'

class FreezeDate extends BasePage {
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

}

export default new FreezeDate();