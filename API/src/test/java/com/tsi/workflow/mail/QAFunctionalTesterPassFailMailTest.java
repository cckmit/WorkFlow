package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QAFunctionalTesterPassFailMailTest {

    private QAFunctionalTesterPassFailMail qAFunctionalTesterPassFailMail;

    @Before
    public void setUp() throws Exception {
	qAFunctionalTesterPassFailMail = new QAFunctionalTesterPassFailMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQAFunctionalTesterPassFailMail() {
	qAFunctionalTesterPassFailMail.setCurrentRole("QADeployLead");
	assertEquals("QADeployLead", qAFunctionalTesterPassFailMail.getCurrentRole());
	qAFunctionalTesterPassFailMail.setCurrentUser("ABCD");
	assertEquals("ABCD", qAFunctionalTesterPassFailMail.getCurrentUser());
	qAFunctionalTesterPassFailMail.setPlanId("P98765");
	assertEquals("P98765", qAFunctionalTesterPassFailMail.getPlanId());
	qAFunctionalTesterPassFailMail.setPlanStatus("PASSED_FUNCTIONAL_TESTING");
	assertEquals("PASSED_FUNCTIONAL_TESTING", qAFunctionalTesterPassFailMail.getPlanStatus());
	qAFunctionalTesterPassFailMail.setProjectCSR("ProjectCSR");
	assertEquals("ProjectCSR", qAFunctionalTesterPassFailMail.getProjectCSR());
	qAFunctionalTesterPassFailMail.setProjectName("ProjectName");
	assertEquals("ProjectName", qAFunctionalTesterPassFailMail.getProjectName());
	qAFunctionalTesterPassFailMail.setUserDetails(new User());
	assertNotNull(qAFunctionalTesterPassFailMail.getUserDetails());
	// Role : QADeployLead , Plan Status :PASSED_FUNCTIONAL_TESTING
	qAFunctionalTesterPassFailMail.processMessage();
	assertNotNull(qAFunctionalTesterPassFailMail.getMessage());

	// Role : QADeployLead , Plan Status :REJECTED
	qAFunctionalTesterPassFailMail.setPlanStatus("REJECTED");
	qAFunctionalTesterPassFailMail.processMessage();
	assertNotNull(qAFunctionalTesterPassFailMail.getMessage());

	// Role : Developer , Plan Status :PASSED_FUNCTIONAL_TESTING
	qAFunctionalTesterPassFailMail.setCurrentRole("Developer");
	qAFunctionalTesterPassFailMail.setPlanStatus("PASSED_FUNCTIONAL_TESTING");
	qAFunctionalTesterPassFailMail.processMessage();
	assertNotNull(qAFunctionalTesterPassFailMail.getMessage());

	// Role : Developer , Plan Status :REJECTED
	qAFunctionalTesterPassFailMail.setPlanStatus("REJECTED");
	qAFunctionalTesterPassFailMail.processMessage();
	assertNotNull(qAFunctionalTesterPassFailMail.getMessage());

    }

}
