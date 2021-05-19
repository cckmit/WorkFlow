package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RejectionOnPutUpgradeMailTest {

    private RejectionOnPutUpgradeMail rejectionOnPutUpgradeMail;

    @Before
    public void setUp() throws Exception {
	rejectionOnPutUpgradeMail = new RejectionOnPutUpgradeMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRejectionOnPutUpgradeMail() {
	rejectionOnPutUpgradeMail.setBackUpPut(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId());
	assertEquals(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId(), rejectionOnPutUpgradeMail.getBackUpPut());
	rejectionOnPutUpgradeMail.setPlan(DataWareHouse.getPlan());
	assertEquals(DataWareHouse.getPlan(), rejectionOnPutUpgradeMail.getPlan());
	rejectionOnPutUpgradeMail.setProdPutLevel(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId());
	assertEquals(DataWareHouse.getPlan().getSystemLoadList().get(0).getPutLevelId(), rejectionOnPutUpgradeMail.getProdPutLevel());
	rejectionOnPutUpgradeMail.processMessage();
	assertEquals("Plan - T1700484 was rejected as it had IBM source artifacts belonging to an older PUT level - PUT12A<br>Default PUT level for system - APO was changed from PUT12A to PUT12A<br><br>Plan Id - T1700484<br> Plan Description: Unit Testing", rejectionOnPutUpgradeMail.getMessage());
    }

}
