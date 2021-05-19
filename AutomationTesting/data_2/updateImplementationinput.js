export default {
    updateimpl: {
        'multipleimpl': [{
            'checkoutsegment': [{
                "name": "adddlr.mac", //single file name with type
                "functionalarea": "SYS", //single func area
                "systemspecific": ["PRE"],// can give multiple system
            }, {
                "name": "cpsd.asm",
                "functionalarea": "PUT13B",
                "systemspecific": ["APO", "PGR"]
            }, {
                "name": "bcb4.asm",
                "functionalarea": "FLS",
                "systemspecific": ["WSP"]
            }],
            'populatesegment': [/* {
                'segment': 'bc0sa.mac', //Dont exclude extension file otherwise it will not pick support files like mak
                "systemspecific": ["WSP"]
            } */],
            'developername': 'aravinth.palanisamy',
            'developerlocation': 'Onsite',
            'peerreviwer': 'aravinth.palanisamy',
            'developercontact': '+6666666666',
            'developerdescription': 'test_automation edit sanity check 1st March',
            'implementationplanid': 'T1700547',
            'commitmessage': 'automation check commit'
        },
            /*  {
                 'checkoutsegment': [{
                     "name": "bcp7",
                     "functionalarea": "PUT12A",
                     "systemspecific": ["wsp"]
                 }, {
                     "name": "bcc2",
                     "functionalarea": "put12a",
                     "systemspecific": ["PGR"]
                 }],
                 'populatesegment': [{
                     'segment': 'bam1',//Dont exclude extension file otherwise it will not pick support files like mak
                     "systemspecific": ["PGR"]
                 }],
                 'developername': 'aravinth.palanisamy',
                 'developerlocation': 'Onsite',
                 'peerreviwer': 'aravinth.palanisamy',
                 'developercontact': '+6666666666',
                 'developerdescription': 'test automation edit 2 on 12th dec',
                 'implementationplanid': 'T1700547',
                 'commitmessage': 'automamtion check commit',
             } */
        ]
    },
    /*  createartifact:
         {
             'artifacts': [{
                 'functionalarea': "IBM",//NON-IBM or IBM
                 'filename': 'one',
                 'filetype': 'asm',
                 'targetsystem': 'APO',
                 'putlevel': "PUT13A",
                 'sourcepath': 'src',
             }]
         } */
}