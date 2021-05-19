package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QALoadsetActivationMailTest {

    private QALoadsetActivationMail qALoadsetActivationMail;

    @Before
    public void setUp() throws Exception {
	qALoadsetActivationMail = new QALoadsetActivationMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQALoadsetActivationMail() {
	qALoadsetActivationMail.setAction("LOADED");
	assertEquals("LOADED", qALoadsetActivationMail.getAction());
	qALoadsetActivationMail.setEnv("ENV");
	assertEquals("ENV", qALoadsetActivationMail.getEnv());
	qALoadsetActivationMail.setLoadSetName("LoadSetName");
	assertEquals("LoadSetName", qALoadsetActivationMail.getLoadSetName());
	qALoadsetActivationMail.setPlanId(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), qALoadsetActivationMail.getPlanId());
	qALoadsetActivationMail.setTssdeploy(true);
	assertTrue(qALoadsetActivationMail.getTssdeploy());
	qALoadsetActivationMail.setUserDetails(DataWareHouse.getUser());
	assertNotNull(qALoadsetActivationMail.getUserDetails());
	qALoadsetActivationMail.setVparsName("VPARSName");
	assertEquals("VPARSName", qALoadsetActivationMail.getVparsName());
	// TssDeploy: True
	qALoadsetActivationMail.processMessage();
	assertNotNull(qALoadsetActivationMail.getMessage());

	// TssDeploy: False
	qALoadsetActivationMail.setTssdeploy(false);
	qALoadsetActivationMail.processMessage();
	assertNotNull(qALoadsetActivationMail.getMessage());
    }

}
