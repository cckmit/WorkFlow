package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PutLevelDeploymentDateMailTest {

    private PutLevelDeploymentDateMail putLevelDeploymentDateMail;

    @Before
    public void setUp() throws Exception {
	putLevelDeploymentDateMail = new PutLevelDeploymentDateMail();

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPutLevelDeploymentDateMail() {
	putLevelDeploymentDateMail.setDeploymentDate(new Date(2019, 9, 24));
	assertEquals(new Date(2019, 9, 24), putLevelDeploymentDateMail.getDeploymentDate());
	putLevelDeploymentDateMail.setDevFlag(true);
	assertTrue(putLevelDeploymentDateMail.getDevFlag());
	List list = new ArrayList<String>();
	list.add("ONLINE");
	putLevelDeploymentDateMail.setPutLevelStatus(list);
	assertNotNull(putLevelDeploymentDateMail.getPutLevelStatus());
	putLevelDeploymentDateMail.setTargetSystem("XYZ");
	assertEquals("XYZ", putLevelDeploymentDateMail.getTargetSystem());
	// For DevFlg True Test
	putLevelDeploymentDateMail.processMessage();
	assertNotNull(putLevelDeploymentDateMail.getMessage());
	assertEquals("  Action required on zTPF level in  ONLINE state", putLevelDeploymentDateMail.getSubject());
	// For DevFlg Flase Test
	putLevelDeploymentDateMail.setDevFlag(false);
	putLevelDeploymentDateMail.processMessage();
	assertNotNull(putLevelDeploymentDateMail.getMessage());
	assertEquals("  Action required: New Production zTPF Level for  XYZ", putLevelDeploymentDateMail.getSubject());

    }

}
