package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.User;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QAAssignmentMailTest {

    private QAAssignmentMail qAAssignmentMail;

    @Before
    public void setUp() throws Exception {
	qAAssignmentMail = new QAAssignmentMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQAAssignmentMail() {
	qAAssignmentMail.setAssignment(true);
	assertTrue(qAAssignmentMail.isAssignment());
	qAAssignmentMail.setPlanId("P8756");
	assertEquals("P8756", qAAssignmentMail.getPlanId());
	qAAssignmentMail.setProgramNameTargetSys(getProgramNameTargetMap());
	assertNotNull(qAAssignmentMail.getProgramNameTargetSys());
	qAAssignmentMail.setProjectCSR("Project CSR");
	assertEquals("Project CSR", qAAssignmentMail.getProjectCSR());
	qAAssignmentMail.setProjectName("ProjectName");
	assertEquals("ProjectName", qAAssignmentMail.getProjectName());
	qAAssignmentMail.setUserDetails(new User());
	assertNotNull(qAAssignmentMail.getUserDetails());
	qAAssignmentMail.setQaFunctionalTestersList(new HashSet<String>());
	// Assignment Flag equals to True
	qAAssignmentMail.processMessage();
	assertEquals("QA Functional Tester assign for P8756", qAAssignmentMail.getSubject());

	// Assignment Flag equals to False
	qAAssignmentMail.setAssignment(false);
	qAAssignmentMail.processMessage();
	assertEquals("Action required: P8756 is ready for QA Functional Testing ", qAAssignmentMail.getSubject());
    }

    private Map<String, String> getProgramNameTargetMap() {
	Map<String, String> map = new HashMap<String, String>();
	map.put("XYZ", "Program");
	return map;
    }

}
