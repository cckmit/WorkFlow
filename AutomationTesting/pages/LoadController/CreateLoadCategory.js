import BasePage from '../Common/basePage';
import createloadcontrolinput from '../../data/createloadcontrolinput'

class CreateLoadCategory extends BasePage {
    constructor() {
        super();
        this.loadCategoryTab = element(by.css('[href="#/app/loadCategories"]'));
        this.insertBtn = element(by.css('.insertButton'));
        this.targetSystem = element(by.model('vmid.form.loadCategory.systemId.id'));
        this.loadCategory = element(by.model('vmid.form.loadCategory.name'));
        this.loadDescription = element(by.model('vmid.form.loadCategory.description'));
        this.loadDays = element(by.css('[name="days_' + createloadcontrolinput.CreateLoadCategory.daysadd + '"]'));
        this.addSlot = element(by.css('[aria-label="' + createloadcontrolinput.CreateLoadCategory.daysadd + '_Add Slot"]'));
        this.entertime = element(by.css('[aria-label="timeSlot_' + createloadcontrolinput.CreateLoadCategory.daysadd + '"]')); ``
        this.createLoadBtn = element(by.css('[aria-label="insertNewLoadCategory"]'));
        this.timeslotdetail = element(by.css('[aria-label="' + createloadcontrolinput.CreateLoadCategory.daysadd + '_timeSlot_0"]'))
    }

    enterTimeslot() {
        this.enterText(this.timeslotdetail, createloadcontrolinput.CreateLoadCategory.timeslot)
    }

    createLoadButton() {
        this.singlebuttonclick(this.createLoadBtn)
    }

    loadCategoryTabSelect() {
        this.tabSelection(this.loadCategoryTab)
    }

    insertButton() {
        this.singlebuttonclick(this.insertBtn)
    }

    targetSystemSelect() {
        var id = element(by.model('vmid.form.loadCategory.systemId.id')).$('[value="' + createloadcontrolinput.CreateLoadCategory.targetsystem + '"]');
        this.singlebuttonclick(id)
    }

    loadCategorySelect() {
        this.enterText(this.loadCategory, createloadcontrolinput.CreateLoadCategory.loadcategory)
    }

    loadDescriptionSelect() {
        this.enterText(this.loadDescription, createloadcontrolinput.CreateLoadCategory.loadcategorydescription)
    }

    loadDaysSelect() {
        this.singlebuttonclick(this.loadDays)
    }

    addSlotSelect() {
        this.singlebuttonclick(this.addSlot)
    }
}

export default new CreateLoadCategory();