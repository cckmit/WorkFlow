package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class QaFunctionalTesterReassignmentMailTest {

    private QaFunctionalTesterReassignmentMail qaFunctionalTesterReassignmentMail;

    @Before
    public void setUp() throws Exception {
	qaFunctionalTesterReassignmentMail = new QaFunctionalTesterReassignmentMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testQaFunctionalTesterReassignmentMail() {
	qaFunctionalTesterReassignmentMail.setAddedQaFunTestersName(getTesterNameList());
	assertNotNull(qaFunctionalTesterReassignmentMail.getAddedQaFunTestersName());
	qaFunctionalTesterReassignmentMail.setPlanId(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), qaFunctionalTesterReassignmentMail.getPlanId());
	qaFunctionalTesterReassignmentMail.setProgramNameTargetSys(getProgramNameTargetSysMap());
	assertNotNull(qaFunctionalTesterReassignmentMail.getProgramNameTargetSys());
	qaFunctionalTesterReassignmentMail.setProjectCSRNum("CSRNumber");
	assertEquals("CSRNumber", qaFunctionalTesterReassignmentMail.getProjectCSRNum());
	qaFunctionalTesterReassignmentMail.setProjectName("ProjectName");
	assertEquals("ProjectName", qaFunctionalTesterReassignmentMail.getProjectName());
	qaFunctionalTesterReassignmentMail.setQaFunTestersId(getTesterNameList());
	assertNotNull(qaFunctionalTesterReassignmentMail.getQaFunTestersId());
	qaFunctionalTesterReassignmentMail.setReAssigned(true);
	assertTrue(qaFunctionalTesterReassignmentMail.isReAssigned());
	qaFunctionalTesterReassignmentMail.setRemoved(true);
	assertTrue(qaFunctionalTesterReassignmentMail.isRemoved());
	qaFunctionalTesterReassignmentMail.setRemoveQaFunTestersName(getTesterNameList());
	assertNotNull(qaFunctionalTesterReassignmentMail.getRemoveQaFunTestersName());
	qaFunctionalTesterReassignmentMail.setUser(DataWareHouse.getUser());
	assertNotNull(qaFunctionalTesterReassignmentMail.getUser());

	// Removed: Ture , ReAssigned:True
	qaFunctionalTesterReassignmentMail.processMessage();

	// Removed: false , ReAssigned:True
	qaFunctionalTesterReassignmentMail.setRemoved(false);
	qaFunctionalTesterReassignmentMail.processMessage();

	// Removed: false , false
	qaFunctionalTesterReassignmentMail.setReAssigned(false);
	qaFunctionalTesterReassignmentMail.processMessage();
    }

    private Map<String, String> getProgramNameTargetSysMap() {
	Map<String, String> map = new HashMap<>();
	map.put(DataWareHouse.getSystemList().get(0).getAliasName(), DataWareHouse.getBranchList().get(0));
	return map;
    }

    private List<String> getTesterNameList() {
	List<String> list = new ArrayList<>();
	list.add(DataWareHouse.getUser().getDisplayName());
	return list;
    }

}
