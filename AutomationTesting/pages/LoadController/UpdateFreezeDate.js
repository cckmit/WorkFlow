import BasePage from '../Common/basePage';
import updatefreezeinput from '../../data/updatefreezeinput';

class UpdateFreezeDate extends BasePage {
    constructor() {
        super();
        this.freezeTab = element(by.css('[href="#/app/freezeDate"]'));
        this.systemId = element(by.model('vmid.form.loadCategoryId.systemId.id'));
        this.loadCategory = element(by.model('vmid.form.loadCategoryId.id'));
        this.reason = element(by.model('vmid.form.reason'));
        this.startDate = element(by.model('vmid.formExtra.fDate'));
        this.startTime = element(by.model('vmid.formExtra.fTime'));
        this.endDate = element(by.model('vmid.formExtra.tDate'));
        this.endTime = element(by.model('vmid.formExtra.tTime'));
        this.updateFreeze = element(by.css('[aria-label="updateFreezeDateWindow"]'))
        this.deleteYes = element(by.xpath('/html/body/div[5]/md-dialog/md-dialog-actions/button[2]'))
        this.deleteNo = element(by.xpath('/html/body/div[5]/md-dialog/md-dialog-actions/button[1]'))
    }

    deleteYesSelect() {
        this.singlebuttonclick(this.deleteYes)
    }

    deleteNoSelect() {
        this.singlebuttonclick(this.deleteNo)
    }

    updateFreezeButton() {
        this.singlebuttonclick(this.updateFreeze)
    }

    freezeTabSelect() {
        this.tabSelection(this.freezeTab);
    }

    systemIdSelect() {
        var sysid = this.systemId.$('[value="' + updatefreezeinput.updatefreeze.targetsystemid + '"]');
        this.singleListSelect(sysid)
    }

    loadCategorySelect() {
        var loadcat = this.loadCategory.$('[value="' + updatefreezeinput.updatefreeze.loadcategory + '"]');
        this.singleListSelect(loadcat)
    }

    editFreezeSelect() {
        var i = 0; var resp = 0;
        var putdetail = $$('.frzDate tr').filter(function (frzDatefind) {
            return frzDatefind.$$('td').get(1).getText().then(function (frezselc) {
                i += 1;
                if (frezselc === updatefreezeinput.updatefreeze.freezereason) {
                    resp = i;
                }
                return frezselc === updatefreezeinput.updatefreeze.freezereason;
            })
        }).then(function (putfound) {
            element(by.xpath('//*[@id="freezeDatesTable"]/tbody/tr[' + resp + ']/td[6]/div/button[1]')).click();
        });
    }

    deleteFreezeSelect() {
        var i = 0; var resp = 0;
        var putdetail = $$('.frzDate tr').filter(function (frzDatefind) {
            return frzDatefind.$$('td').get(1).getText().then(function (frezselc) {
                i += 1;
                console.log(frezselc)
                if (frezselc === updatefreezeinput.updatefreeze.editreason) {
                    console.log(i)
                    resp = i;
                }
                return frezselc === updatefreezeinput.updatefreeze.editreason;
            })
        }).then(function () {
            element(by.xpath('//*[@id="freezeDatesTable"]/tbody/tr[' + resp + ']/td[6]/div/button[2]')).click();
        });
    }

    reasonEnter() {
        this.enterText(this.reason, updatefreezeinput.updatefreeze.editreason)
    }

    startDateEnter() {
        this.enterText(this.startDate, updatefreezeinput.updatefreeze.startdate)
    }

    startTimeEnter() {
        this.enterText(this.startTime, updatefreezeinput.updatefreeze.starttime)
    }

    endDateEnter() {
        this.enterText(this.endDate, updatefreezeinput.updatefreeze.enddate)
    }

    endTimeEnter() {
        this.enterText(this.endTime, updatefreezeinput.updatefreeze.endtime)
    }
}

export default new UpdateFreezeDate();