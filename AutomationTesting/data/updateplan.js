export default {
    editplan: {
        'editplanid': 'T1700547', //enter plan id should start with UPPERCASE eg:T170001
        'targetsystemid': '1',  //targetsystem APO=0, PRE = 1,PGR = 2, WSP = 3
        'companynameid': '0',   // companyname Travelport =0, Delta = 1
        'csrid': '2',    //for apo 1,pgr 2,pre 3 and wsp 4
        'loadtype': 'Standard',
        'plandescription': 'Creating new plan by regression script',//any
        'comments': 'Automation comments in lead',//any
        'loadwindow': 'By Date', //should give 'By Category' or 'By Date'
        'loadcategory': 'I', //loadcategory should be in uppercase   
        'loaddate': '2017-09-29',//should be in yyyy/mm/dd
        'loadtime': '18:00', //give element id
        'loadattendee': 'aravinth', //any   
        'preload': 'No', //Yes or No
        'leadcontact': '+9977547478' //should start with +12digit 
    }
};