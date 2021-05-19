export default {
    createImplementationplan: {
        'targetsystemid': [{
            'id': 'APO',
            'loadwindow': 'By Category', //should give 'By Category' or 'By Date'
            'loadcategory': 'D', // 'loadcategory': 'I'(9)PRE,'I'(25)PRE,'I'(17)Pgr,
            'loaddate': '2017-12-30',//should be in yyyy/mm/dd
            'loadtime': '13:00', //give element id
            'loadattendee': 'aravinth', //any 
            'preload': 'No', //Yes or No
            'dbcr': ['K1111']//!!!! WSP !!!!k0231
        }, {
            'id': 'PRE',
            'loadwindow': 'By Category', //should give 'By Category' or 'By Date'
            'loadcategory': 'C', // 'loadcategory': 'I'(9)PRE,'I'(25)PRE,'I'(17)Pgr,
            'loaddate': '2017-12-30',//should be in yyyy/mm/dd
            'loadtime': '13:00', //give element id
            'loadattendee': 'arul', //any 
            'preload': 'No', //Yes or No
            'dbcr': ['11111', '22222']//!!!! WSP !!!!k0231
        }, {
            'id': 'PGR',
            'loadwindow': 'By Category', //should give 'By Category' or 'By Date'
            'loadcategory': 'I', // 'loadcategory': 'I'(9)PRE,'I'(25)PRE,'I'(17)Pgr,
            'loaddate': '2017-12-30',//should be in yyyy/mm/dd
            'loadtime': '13:00', //give element id
            'loadattendee': 'thilak', //any 
            'preload': 'No', //Yes or No
            'dbcr': ['K1111']//!!!! WSP !!!!k0231
        }, {
            'id': 'WSP',
            'loadwindow': 'By Category', //should give 'By Category' or 'By Date'
            'loadcategory': 'F', // 'loadcategory': 'I'(9)PRE,'I'(25)PRE,'I'(17)Pgr,
            'loaddate': '2017-12-30',//should be in yyyy/mm/dd
            'loadtime': '13:00', //give element id
            'loadattendee': 'yeswanth', //any 
            'preload': 'No', //Yes or No
            'dbcr': ['K1111']//!!!! WSP !!!!k0231
        }],
        'companynameid': 'Travelport',   // companyname Travelport , Delta
        'csrid': '20',    //for apo 1,pgr 2,pre 3 and wsp 4
        'loadtype': 'Standard',
        'approvalmanager': 'aravinth.palanisamy',
        'plandescription': 'Automation Sanity test on  1st March Dont not use',//any
        'comments': 'Automation comments in lead',//any
        'leadcontact': '+9977547478', //should start with +12digit
    }
};