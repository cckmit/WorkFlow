import BasePage from '../Common/basePage';
import createimplementationinput from '../../data/createimplementationinput';
import updateImplementationinput from '../../data/updateImplementationinput';
import findid from '../../data/findid';

class CreateNewArtifact extends BasePage {
    constructor() {
        super();
        this.createArtifact = element(by.css('[aria-label*="new-source-artifact"]'))
        this.createIbm = element(by.css("button[ng-click*=addSourceSet('IBM')]"))
        this.createNON_IBM = element(by.css("button[ng-click*=addSourceSet('NON_IBM')]"))
        this.filename = element(by.model('self.newFile[fileObj.id].programName'))
        this.filetype = element(by.model('self.newFile[fileObj.id].fileExt'))
        this.testSystem = element(by.model('self.newFile[fileObj.id].targetSystemList'))
        this.putlevel = element(by.model('self.newFile[fileObj.id].funcArea'))
        this.sourcelevel = element(by.model('self.newFile[fileObj.id].fileName'))
        this.createBtn = element(by.css('[ng-click="createSourceArtifact(self.newFile)"]'))
    }

    artifactReport() {
        browser.wait(function() {
            return element(by.css('[ng-hide="!self.reportView"]')).isPresent().then(function(isPresent) {
                browser.ignoreSynchronization = false;
                return isPresent;
            });
        }, this.timeout.min);
        browser.sleep(3000)

        $$('md-card-content[ng-repeat="fileObj in self.resultFileList"]').each(function(row) {
            row.getText().then(function(text) {
                console.log(text.split("\n").join(" "))
            })
        });


    }

    createSourceButton() {
        this.singlebuttonclick(this.createBtn)
    }
    createIBMCreate(fun_obj) {
        /*  var self = this
         $$(by.css('[ng-click*="addSourceSet"]')).map(function (ele, index) {
             ele.getText().then(function (txt) {
                 if (txt.match(new RegExp(fun_obj, 'gi')) != null) {
                     var source = element(by.css('[ng-click*="addSourceSet"]')).get(index)
                     self.singlebuttonclick(source)
                 }
             })
         }) */
        var createibm = element(by.css('[ng-hide="self.reportView"]')).all(by.tagName('button')).get(0)
        this.singlebuttonclick(createibm)


    }
    createNonIbmCreate() {
        // this.singlebuttonclick(this.createNON_IBM)
        var createnonibm = element(by.css('[ng-hide="self.reportView"]')).all(by.tagName('button')).get(1)
        this.singlebuttonclick(createnonibm)
    }
    sourceNameEnter(source_obj) {
        this.enterText(this.sourcelevel, source_obj)
    }

    createNewSourceArtifact() {
        this.singlebuttonclick(this.createArtifact)
    }
    fileNameEnter(name_obj) {
        this.enterText(this.filename, name_obj)
    }

    filetypeSelect(type_obj) {
        this.listSelect(this.filetype, type_obj)
    }

    targetSystemSelection(system_obj, cbk) {

        // var systemselect =  element(by.css('[ng-model="self.newFile[fileObj.id].targetSystemList"]')).$('[value = "' + system_obj + '"]')
        browser.waitForAngularEnabled(false)
        $("md-card-content span.select2Enhance").click()
        $("ul.select2-results__options li[id*='WSP']").click()
            // var systemselect = this.testSystem.$('[value = "' + system_obj + '"]')
            // this.singleListSelect(systemselect);
        cbk()
            /* element(by.css("input[.select2-search__field]")).click().sendKeys("WSP").then(function (id) {
                id.element(by.css(".select2-results__options .select2-results__option.select2-results__option--highlighted")).all(by.css('li[id=*"WSP"]')).click()
                cbk()
            })*/
    }

    putlevelSelection() {
        // var putSel = $(by.css('[name="putLevel"]')).all(by.tagName('ng-value')).get(1)
        // this.singleListSelect(putSel);
        //    $('option[value = "PUT13A"]').click();
        browser.waitForAngularEnabled(false)
        $('select[ng-model="self.newFile[fileObj.id].funcArea"] option[value="PUT13A"]').click();
        // var systemselect = this.testSystem.$('[value = "' + system_obj + '"]')

    }

}

export default new CreateNewArtifact();