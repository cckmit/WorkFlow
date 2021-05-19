var request = require('request');

describe("Sample test", function() {
   beforeEach(function() {
       var jar = request.jar();
       var req = request.defaults({
           jar : jar
       });

       function post(url, params) {
           console.log("Calling", url);
           req.post(browser.baseUrl + url, params, function(error, message) {
               console.log("Done call to", url);
           });
       }

       function purge() {
           post('api/v1/setup/purge', {
               qs : {
                   key : browser.params.purgeSecret
               }
           });
       }

       function setupCommon() {
           post('api/v1/setup/test');
       }
        
       purge();
       setupCommon();
   });

   it("should do something", function() {
       expect(2).toEqual(2);
   });
});