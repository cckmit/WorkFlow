import BasePage from '../Common/basePage';
import updateloadcategoryinput from '../../data/updateloadcategoryinput';
import Common from '../Common/Common'

class UpdateLoadCategory extends BasePage {
    constructor() {
        super();
        this.loadCategoryTab = element(by.css('[href="#/app/loadCategories"]'));
        this.targetSystem = element(by.model('vmid.form.loadCategory.systemId.id'));
        this.loadCategory = element(by.model('vmid.form.loadCategory.name'));
        this.loadDescription = element(by.model('vmid.form.loadCategory.description'));
        this.loadDays = element(by.css('[name="days_' + updateloadcategoryinput.loadcategoryedit.daysadd + '"]'));
        this.addSlot = element(by.css('[aria-label="' + updateloadcategoryinput.loadcategoryedit.daysadd + '_Add Slot"]'));
        this.updateLoadBtn = element(by.css('[aria-label="updateLoadCategory"]'));
        this.timeslotdetail = element(by.css('[aria-label="' + updateloadcategoryinput.loadcategoryedit.daysadd + '_timeSlot_0"]'))
        this.entertime = element(by.css('[aria-label="timeSlot_' + updateloadcategoryinput.loadcategoryedit.daysadd + '"]')); ``
        this.deactivateYes = element(by.xpath('/html/body/div[5]/md-dialog/md-dialog-actions/button[2]'))
        this.deactivateNo = element(by.xpath('/html/body/div[5]/md-dialog/md-dialog-actions/button[1]'))
    }

    enterTimeslot() {
        this.enterText(this.timeslotdetail, updateloadcategoryinput.loadcategoryedit.timeslot)
    }

    updateLoadButton() {
        this.singlebuttonclick(this.updateLoadBtn)
    }

    loadCategoryTabSelect() {
        this.tabSelection(this.loadCategoryTab)
    }

    targetSystemSelect() {
        var id = element(by.model('vmid.form.loadCategory.systemId.id')).$('[value="' + updateloadcategoryinput.loadcategoryedit.targetsystem + '"]');
        this.singlebuttonclick(id)
    }

    loadCategorySelect() {
        this.enterText(this.loadCategory, updateloadcategoryinput.loadcategoryedit.loadcategory)
    }

    loadDescriptionSelect() {
        this.enterText(this.loadDescription, updateloadcategoryinput.loadcategoryedit.loadcategorydescription)
    }

    loadDaysSelect() {
        this.singlebuttonclick(this.loadDays)
    }

    addSlotSelect() {
        this.singlebuttonclick(this.addSlot)
    }

    deactivateYesSelect() {
        this.singlebuttonclick(this.deactivateYes)
    }

    deactivateNoSelect() {
        this.singlebuttonclick(this.deactivateNo)
    }

    editValueSelect() {
        var i = 0;
        var resp = 0;
        var putdetail = $$('.loadCategories tr').filter(function (loadCateg) {
            return loadCateg.$$('td').get(3).getText().then(function (loadEdit) {
                i += 1;
                if (loadEdit === updateloadcategoryinput.loadcategoryedit.loadcategorydescription) {
                    resp = i;
                }
                return loadEdit === updateloadcategoryinput.loadcategoryedit.loadcategorydescription;

            })
        }).then(function (loadCateg) {
            var addresp = resp + 1;
            browser.sleep(2000);
            element(by.xpath('//*[@id="loadCategoryTable"]/tbody/tr[' + addresp + ']/td[5]/div/button[1]')).click();
            Common.alertMessage();
            return addresp;
        });

    }

    deactivateSelect() {
        var i = 0;
        var resp = 0;
        var putdetail = $$('.loadCategories tr').filter(function (loadCateg) {
            return loadCateg.$$('td').get(3).getText().then(function (loadEdit) {
                i += 1;
                if (loadEdit === updateloadcategoryinput.loadcategoryedit.loadcategorydescription) {
                    resp = i;
                }
                return loadEdit === updateloadcategoryinput.loadcategoryedit.loadcategorydescription;
            })
        }).then(function (loadCateg) {
            var addresp = resp + 1;
            browser.sleep(2000);
            element(by.xpath('//*[@id="loadCategoryTable"]/tbody/tr[' + addresp + ']/td[5]/div/button[2]')).click();
            Common.alertMessage();
        });

    }

}

export default new UpdateLoadCategory();