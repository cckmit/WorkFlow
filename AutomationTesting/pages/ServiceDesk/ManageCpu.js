import BasePage from '../Common/basePage';

class ManageCpu extends BasePage {
    constructor() {
        super();
        this.managetCputab = element(by.css('[href="#/app/defaultCPU"]'))
    }

    detailViewSelection(capturedid) {
        var detail = element(by.css('[aria-label="' + capturedid + '"]'))
        browser.wait(this.isClickable(detail), this.timeout.xl)
        this.singleListSelect(detail)
    }

    manageCputabSelection() {
        this.tabSelection(this.managetCputab)
    }

    tableSelection() {
        var i = 0;
        var resp = 0;
        var putdetail = $$('.table tr').filter(function (loadCateg) {
            return loadCateg.$$('td').get(0).getText().then(function (loadEdit) {
                i += 1;
                console.log("loadEdit" + loadEdit)
                if (loadEdit === "PRE") {
                    console.log("inside" + i)
                    resp = i;
                }
                return loadEdit === "PRE";

            })
            console.log("resp" + resp)
            console.log("resp1" + loadCateg)
            element(by.xpath('/html/body/ui-view/div/div/ui-view/section/div/div/div/div/div/table/tbody/tr[' + resp + ']/td[4]/button')).click()
        }).then(function () {
            browser.sleep(2000);
        });
    }
}

export default new ManageCpu();