import Basepage from '../pages/Common/basePage';
import Credential from '../data/Credential';
import findid from '../data/findid';
import Common from '../pages/Common/Common';

var client = require('ftp');
var Ftp = new client();
var fs = require('fs');


class LinuxTest extends BasePage {
    constructor() {
        super()
    }

    newlinux = function (response) {
        function downloadFile(cbk) {
            console.log("Ftp ==>")
            var filename = "assign.asm"
            Ftp.on('ready', function () {
                console.log("FTP download Ready")
                Ftp.listSafe("/home/aravinth.palanisamy/projects/t1700377_001/wsp/base/rt/", function (err, list) {
                    if (err) throw err;
                    // console.log(list)
                    //Get file from linux
                    Ftp.get("acpd.asm", function (err, stream) {
                        if (err) throw err;

                        stream.once('close', function (err, req) {
                            //asdfconsole.log("FTP completed ==> stream is closed");
                            Ftp.end();
                            var response = true;
                            cbk(response);
                        });

                        var options = { encoding: 'binary' };
                        stream.pipe(fs.createWriteStream(filename, options));
                    });
                });

                //Script modification
                var data = fs.readFileSync('C:/Users/aravinth.palanisamy/Documents/Workflow Project/Workflow-Sprint30/Workflow/AutomationTesting/assign.asm').toString().split("\n");
                data.splice(0, 0, "*//////////////Automation testing script change///////////////////////*");
                var text = data.join("\n");
                //write file after modification
                fs.writeFile('C:/Users/aravinth.palanisamy/Documents/Workflow Project/Workflow-Sprint30/Workflow/AutomationTesting/mod.txt', text, function (err) {
                    if (err) return err;
                });

                Ftp.put('C:/Users/aravinth.palanisamy/Documents/Workflow Project/Workflow-Sprint30/Workflow/AutomationTesting/mod.txt', '/home/aravinth.palanisamy/projects/t1700377_001/wsp/base/rt/temp.asm', function (err) {
                    console.log("Sending data to server")
                    if (err) {
                        console.log(err)
                        throw err;
                    }
                    Ftp.end(console.log("FTP done successfully"));
                    cbk()
                });
            });

        };

        Ftp.connect({ host: "zhldvztbs002.tvlport.net", user: "aravinth.palanisamy", password: "Travelport5878" });

    }
}



export default new LinuxTest();